package io.github.h4mu.kontroll.web;
import io.github.h4mu.kontroll.domain.Route;
import io.github.h4mu.kontroll.domain.StopTime;
import io.github.h4mu.kontroll.domain.Trip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/checkin/**")
@Controller
public class CheckinController {

    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }

    @RequestMapping("index")
    public String index(Model uiModel) {
    	uiModel.addAttribute("routes", Route.findAllRoutes());
        return "checkin/index";
    }
    
    @RequestMapping("route/{id}")
    public String route(@PathVariable("id") Integer id, Model uiModel) {
    	uiModel.addAttribute("trips", Route.findRoute(id).getTrips());
    	return "checkin/route";
    }
    
    @RequestMapping("trip/{id}")
    public String trip(@PathVariable("id") Integer id, Model uiModel) {
    	uiModel.addAttribute("stopTimes", StopTime.findStopTimesByTripIdOrderedBySequence(Trip.findTrip(id)).getResultList());
    	return "checkin/trip";
    }
    
    @RequestMapping("stop/{id}")
    public String stop(@PathVariable("id") Integer id, Model uiModel) {
    	uiModel.addAttribute("stopTime", id);
    	return "checkin/stop";
    }
}
