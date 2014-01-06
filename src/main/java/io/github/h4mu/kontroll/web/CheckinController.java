package io.github.h4mu.kontroll.web;
import io.github.h4mu.kontroll.domain.Checkin;
import io.github.h4mu.kontroll.domain.Route;
import io.github.h4mu.kontroll.domain.StopTime;
import io.github.h4mu.kontroll.domain.Trip;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/checkin/**")
@Controller
public class CheckinController {

    @RequestMapping(method = RequestMethod.POST)
    public String post(@RequestParam Integer id, HttpServletRequest httpServletRequest) {
    	Checkin checkin = new Checkin();
    	checkin.setSpottingTime(Calendar.getInstance());
    	StopTime stopTime = StopTime.findStopTime(id);
    	checkin.setStopName(stopTime.getStop().getName());
    	Trip trip = stopTime.getTrip();
    	checkin.setTripHeadSign(trip.getHeadSign());
    	checkin.setIsReturnTrip(trip.getIsReturn());
    	Route route = trip.getRoute();
    	checkin.setRouteShortName(route.getShortName());
    	checkin.setRouteColor(route.getColor());
    	checkin.setRouteTextColor(route.getTextColor());
    	checkin.persist();
    	return "redirect:checkin/list";
    }

	@RequestMapping("list")
	public String list(Model uiModel) {
		uiModel.addAttribute("checkins", Checkin.findTodaysCheckins());
		return "checkin/list";
	}
	
    @RequestMapping("routes")
    public String routes(Model uiModel) {
    	uiModel.addAttribute("routes", Route.findAllRoutesOrderedByShortName());
        return "checkin/routes";
    }
    
    @RequestMapping("route/{id}")
    public String route(@PathVariable Integer id, Model uiModel) {
    	uiModel.addAttribute("trips", Trip.findTripsByRouteIdOrderedByHeadSign(Route.findRoute(id)));
    	return "checkin/route";
    }
    
    @RequestMapping("trip/{id}")
    public String trip(@PathVariable Integer id, Model uiModel) {
    	uiModel.addAttribute("stopTimes",
    			StopTime.findStopTimesByTripIdOrderedBySequence(Trip.findTrip(id)));
    	return "checkin/trip";
    }
    
    @RequestMapping("stop/{id}")
    public String stop(@PathVariable Integer id, Model uiModel) {
    	uiModel.addAttribute("stopTime", id);
    	return "checkin/stop";
    }
}
