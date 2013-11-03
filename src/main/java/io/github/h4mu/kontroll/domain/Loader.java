package io.github.h4mu.kontroll.domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.persistence.EntityManager;

import org.apache.commons.io.FileUtils;
import org.springframework.transaction.annotation.Transactional;

public class Loader {
	private final String gtfsUrl;
	private final PrintStream outputStream;
	private HashMap<String, Integer> routes = new HashMap<>();
	private HashMap<String, Integer> stops = new HashMap<>();
	private HashMap<String, Integer> trips = new HashMap<>();

	public Loader(String gtfsUrl, PrintStream outputStream) {
		this.gtfsUrl = gtfsUrl;
		this.outputStream = outputStream;
		
	}
	
	private void write(String line) throws IOException {
		outputStream.println(line);
		outputStream.flush();
	}

	@Transactional
	public void load() throws IOException {
		File file = File.createTempFile("kontroll", ".zip");
		file.deleteOnExit();
		write("Opened file " + file.getAbsolutePath());
		Calendar lastMonth = Calendar.getInstance();
		lastMonth.add(Calendar.MONTH, -1);
		FileUtils.copyURLToFile(new URL(gtfsUrl), file);
		write("Downloaded " + gtfsUrl);
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
		while ((line = reader.readLine()) != null) {
			String[] split = line.split(",");
			// route_id,service_id,trip_id,trip_headsign,direction_id,block_id,shape_id,wheelchair_accessible,trips_bkk_ref
			Integer routeId = routes.get(split[0]);
			Route route = Route.findRoute(routeId);
			if (route != null) {
				String tripKey = split[3] + ":" + split[4] + ":" + split[6] + ":" + routeId;
				Trip trip = processedTrips.get(tripKey);
				if (trip == null) {
					trip = new Trip();
					trip.setHeadSign(split[3].replace("\"", "").trim());
					trip.setIsReturn("1".equals(split[4].replace("\"", "").trim()));
					trip.setRoute(route);
					processedTrips.put(tripKey, trip);
					trip.persist();
				}
				trips.put(split[2], trip.getId());
			}
		}
	}

	private void parseStopTimes(BufferedReader reader)
			throws IOException {
		HashSet<String> processedStopTimes = new HashSet<>();
		String line = reader.readLine(); // skip column names
		while ((line = reader.readLine()) != null) {
			// trip_id,arrival_time,departure_time,stop_id,stop_sequence,shape_dist_traveled
			String[] split = line.split(",");
			Integer tripId = trips.get(split[0]);
			Trip trip = Trip.findTrip(tripId);
			Integer stopId = stops.get(split[3]);
			Stop stop = Stop.findStop(stopId);
			if (trip != null && stop != null) {
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
			// tokenizer.nextToken(); // stop_lat
			// tokenizer.nextToken(); // stop_lon
		}
	}

	private void parseRoutes(BufferedReader reader) throws IOException {
		String line = reader.readLine(); // skip column names
		while ((line = reader.readLine()) != null) {
			String[] split = line.split(",");
			// route_id,agency_id,route_short_name,route_long_name,route_desc,route_type,route_color,route_text_color
			Route route = new Route();
			route.setShortName(split[2].replace("\"", "").trim());
//			route.setLongName(split[3]);
//			route.setDescription(split[4]);
			route.persist();
			routes.put(split[0], route.getId());
		}
	}
}
