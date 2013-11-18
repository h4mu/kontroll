package io.github.h4mu.kontroll.web;
import io.github.h4mu.kontroll.domain.Checkin;
import io.github.h4mu.kontroll.domain.Route;
import io.github.h4mu.kontroll.domain.StopTime;
import io.github.h4mu.kontroll.domain.Trip;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/checkin/**")
@Controller
public class CheckinController {

    @RequestMapping(method = RequestMethod.POST)
    public String post(@RequestParam Integer id, HttpServletRequest httpServletRequest) {
    	Checkin checkin = new Checkin();
    	checkin.setSpottingTime(Calendar.getInstance());
    	checkin.setStopTime(StopTime.findStopTime(id));
    	checkin.persist();
    	return "redirect:checkin/" + encodeUrlPathSegment(checkin.getId().toString(),
    			httpServletRequest);
    }
    
    private String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
	}

	@RequestMapping("{id}")
    public String show(@PathVariable Integer id, Model uiModel) {
    	uiModel.addAttribute("checkin", Checkin.findCheckin(id));
        uiModel.addAttribute("itemId", id);
    	return "checkin/show";
    }

	@RequestMapping("list")
	public String list(Model uiModel) {
		uiModel.addAttribute("checkins", Checkin.findAllCheckins());
		return "checkin/list";
	}
	
    @RequestMapping("index")
    public String index(Model uiModel) {
    	uiModel.addAttribute("routes", Route.findAllRoutesOrderedByShortName().getResultList());
        return "checkin/index";
    }
    
    @RequestMapping("route/{id}")
    public String route(@PathVariable Integer id, Model uiModel) {
    	uiModel.addAttribute("trips", Trip.findTripsByRouteIdOrderedByHeadSign(Route.findRoute(id)).getResultList());
    	return "checkin/route";
    }
    
    @RequestMapping("trip/{id}")
    public String trip(@PathVariable Integer id, Model uiModel) {
    	uiModel.addAttribute("stopTimes",
    			StopTime.findStopTimesByTripIdOrderedBySequence(Trip.findTrip(id)).getResultList());
    	return "checkin/trip";
    }
    
    @RequestMapping("stop/{id}")
    public String stop(@PathVariable Integer id, Model uiModel) {
    	uiModel.addAttribute("stopTime", id);
    	return "checkin/stop";
    }
}
