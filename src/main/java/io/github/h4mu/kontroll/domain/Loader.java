package io.github.h4mu.kontroll.domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.persistence.EntityManager;

import org.apache.commons.io.FileUtils;
import org.springframework.transaction.annotation.Transactional;

public class Loader {
	private final PrintStream outputStream;
	private HashMap<String, Integer> routes = new HashMap<>();
	private HashMap<String, Integer> stops = new HashMap<>();
	private HashMap<String, Integer> trips = new HashMap<>();

	public Loader(PrintStream outputStream) {
		this.outputStream = outputStream;
	}
	
	private void write(String line) throws IOException {
		outputStream.println(line);
		outputStream.flush();
	}

	@Transactional
	public void load(String gtfsUrl) throws IOException {
		File file = openGtfsFile(gtfsUrl);
		
		cleanDB();
		
		ZipInputStream stream = new ZipInputStream(new FileInputStream(file));
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			write("Routes...");
			getEntry(stream, "routes.txt");
			parseRoutes(reader);
			write("Stops...");
			getEntry(stream, "stops.txt");
			parseStops(reader);
			write("Trips...");
			getEntry(stream, "trips.txt");
			parseTrips(reader);
			write("Stop Times...");
			getEntry(stream, "stop_times.txt");
			parseStopTimes(reader);
			write("Finialising...");
		} catch (Exception e) {
			write(e + ": " + e.getMessage());
		} finally {
			Route.entityManager().flush();
			stream.close();
			write("Done.");
		}
	}

	private File openGtfsFile(String gtfsUrl) throws IOException, MalformedURLException {
		if (gtfsUrl == null) {
			throw new IllegalArgumentException("URL needed");
		}
		File file = new File("gtfs.zip");
		boolean isNewFile = !file.exists();
		if (isNewFile) {
			try {
				file.createNewFile();
			} catch (SecurityException e) {
				file = File.createTempFile("gtfs", ".zip");
				file.deleteOnExit();
			}
		}
		write("Opened file " + file.getAbsolutePath());
		Calendar lastMonth = Calendar.getInstance();
		lastMonth.add(Calendar.MONTH, -1);
		if (isNewFile || FileUtils.isFileOlder(file, lastMonth.getTime())) {
			FileUtils.copyURLToFile(new URL(gtfsUrl), file);
			write("Downloaded " + gtfsUrl);
		}
		return file;
	}

	private void cleanDB() {
		EntityManager entityManager = StopTime.entityManager();
		entityManager.createQuery("DELETE FROM StopTime").executeUpdate();
		entityManager.createQuery("DELETE FROM Trip").executeUpdate();
		entityManager.createQuery("DELETE FROM Route").executeUpdate();
		entityManager.createQuery("DELETE FROM Stop").executeUpdate();
	}

	private void getEntry(ZipInputStream stream, String name) throws IOException {
		for (ZipEntry nextEntry = stream.getNextEntry(); nextEntry != null; nextEntry = stream.getNextEntry()) {
			if (name.equals(nextEntry.getName())) {
				return;
			}
		}
		stream.reset();
		for (ZipEntry nextEntry = stream.getNextEntry(); nextEntry != null; nextEntry = stream.getNextEntry()) {
			if (name.equals(nextEntry.getName())) {
				return;
			}
		}
		throw new IllegalStateException("entry not found");
	}

	private void parseTrips(BufferedReader reader) throws IOException {
		String line = reader.readLine(); // skip column names
		HashMap<String, Trip> processedTrips = new HashMap<>();
		// route_id,service_id,trip_id,trip_headsign,direction_id,block_id,shape_id,wheelchair_accessible,trips_bkk_ref
		Pattern pattern = Pattern.compile("^([^,]+),[^,]*,([^,]+),(?:\"([^\"]+)\"|([^,]+)),(0|1),[^,]*,([^,]+),.*");
		while ((line = reader.readLine()) != null) {
			Matcher matcher = pattern.matcher(line);
			if (!matcher.matches()) {
				write(line);
				continue;
			}
			Integer routeId = routes.get(matcher.group(1));
			Route route = routeId != null ? Route.findRoute(routeId) : null;
			if (route != null) {
				String direction = matcher.group(5);
				String quotedHeadsign = matcher.group(3);
				String headSign = quotedHeadsign != null ? quotedHeadsign : matcher.group(4);
				String tripKey = headSign + ":" + direction + ":" + matcher.group(6) + ":" + routeId;
				Trip trip = processedTrips.get(tripKey);
				if (trip == null) {
					trip = new Trip();
					trip.setHeadSign(headSign);
					trip.setIsReturn("1".equals(direction));
					trip.setRoute(route);
					processedTrips.put(tripKey, trip);
					trip.persist();
				}
				trips.put(matcher.group(2), trip.getId());
			}
		}
	}

	private void parseStopTimes(BufferedReader reader)
			throws IOException {
		HashSet<String> processedStopTimes = new HashSet<>();
		Calendar wholeDay = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		wholeDay.setTimeInMillis(0);
		Date dayBeginning = wholeDay.getTime();
		wholeDay.add(Calendar.DAY_OF_YEAR, 1);
		wholeDay.add(Calendar.MILLISECOND, -1);
		Date dayEnd = wholeDay.getTime();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		String line = reader.readLine(); // skip column names
		while ((line = reader.readLine()) != null) {
			// trip_id,arrival_time,departure_time,stop_id,stop_sequence,shape_dist_traveled
			String[] split = line.split(",");
			if (split.length < 6) {
				write(line);
				continue;
			}
			Integer tripId = trips.get(split[0]);
			Trip trip = tripId != null ? Trip.findTrip(tripId) : null;
			Integer stopId = stops.get(split[3]);
			Stop stop = Stop.findStop(stopId);
			if (trip != null && stop != null) {
				try {
					Date arrival = format.parse(split[1].replace("\"", ""));
					Date startTime = trip.getStartTime();
					if (startTime == null || arrival.before(startTime)) {
						trip.setStartTime(arrival);
					}
				} catch (ParseException e) {
					write(line);
					continue;
				}
				try {
					Date departure = format.parse(split[2].replace("\"", ""));
					Date endTime = trip.getEndTime();
					if (endTime == null || departure.after(endTime)) {
						trip.setEndTime(departure);
					}
				} catch (ParseException e) {
					write(line);
					continue;
				}
				if (trip.getStartTime() != null && trip.getEndTime() != null) {
					Calendar diff = Calendar.getInstance();
					diff.setTimeInMillis(trip.getEndTime().getTime() - trip.getStartTime().getTime());
					if (diff.get(Calendar.DAY_OF_YEAR) > 1) {
						trip.setStartTime(dayBeginning);
						trip.setEndTime(dayEnd);
					}
				}
				String stopTimeKey = split[3] + ":" + tripId + ":" + stopId;
				if (!processedStopTimes.contains(stopTimeKey)) {
					StopTime stopTime = new StopTime();
					stopTime.setSequence(Integer.parseInt(split[4].replace("\"", "").trim()));
					stopTime.setTrip(trip);
					stopTime.setStop(stop);
					processedStopTimes.add(stopTimeKey);
					stopTime.persist();
				}
			}
		}
	}

	private void parseStops(BufferedReader reader) throws IOException {
		HashMap<String, Stop> processedStops = new HashMap<>();
		String line = reader.readLine(); // skip column names
		while ((line = reader.readLine()) != null) {
			// stop_id,stop_name,stop_lat,stop_lon,location_type,parent_station,wheelchair_boarding
			StringTokenizer tokenizer = new StringTokenizer(line, ",");
			try {
				String id = tokenizer.nextToken();
				String name = tokenizer.nextToken().replace("\"", "").trim();
				Stop stop = processedStops.get(name);
				if (stop == null) {
					stop = new Stop();
					stop.setName(name);
					processedStops.put(name, stop);
					stop.persist();
				}
				stops.put(id, stop.getId());
			} catch (NoSuchElementException e) {
				write(line);
			}
		}
	}

	private void parseRoutes(BufferedReader reader) throws IOException {
		HashSet<String> blacklist = getBlackList();
		// route_id,agency_id,route_short_name,route_long_name,route_desc,route_type,route_color,route_text_color
		Pattern pattern = Pattern.compile("^([^,]+),[^,]*,(?:\"([^\"]+)\"|([^,]+)),(?:\"[^\"]*\"|[^,]*),(?:\"[^\"]*\"|[^,]*),[^,]*,([0-9a-fA-F]{6}),([0-9a-fA-F]{6})");
		String line = reader.readLine(); // skip column names
		while ((line = reader.readLine()) != null) {
			Matcher matcher = pattern.matcher(line);
			if (!matcher.matches()) {
				write(line);
				continue;
			}
			String unquotedShortName = matcher.group(3);
			String shortName = unquotedShortName != null ? unquotedShortName : matcher.group(2);
			if (!blacklist.contains(shortName)) {
				Route route = new Route();
				route.setShortName(shortName);
				route.setColor(matcher.group(5));
				route.setTextColor(matcher.group(4));
				route.persist();
				routes.put(matcher.group(1), route.getId());
			}
		}
	}

	private HashSet<String> getBlackList() {
		File file = new File("blacklist.txt");
		HashSet<String> blacklist = new HashSet<>();
		if (file.exists() && file.canRead()) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				try {
					String line;
					while ((line = reader.readLine()) != null) {
						blacklist.add(line);
					}
				} finally {
					reader.close();
				}
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		}
		return blacklist;
	}
}
