package io.github.h4mu.kontroll.web;

import io.github.h4mu.kontroll.domain.Checkin;
import io.github.h4mu.kontroll.domain.Route;
import io.github.h4mu.kontroll.domain.Stop;
import io.github.h4mu.kontroll.domain.StopTime;
import io.github.h4mu.kontroll.domain.Trip;

import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

    public Converter<Checkin, String> getCheckinToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<Checkin, String>() {
            public String convert(Checkin checkin) {
                return checkin.getSpottingTime().toString();
            }
        };
    }
    
    public Converter<Integer, Checkin> getIdToCheckinConverter() {
        return new org.springframework.core.convert.converter.Converter<Integer, Checkin>() {
            public Checkin convert(Integer id) {
                return Checkin.findCheckin(id);
            }
        };
    }
    
    public Converter<String, Checkin> getStringToCheckinConverter() {
        return new org.springframework.core.convert.converter.Converter<String, Checkin>() {
            public Checkin convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Checkin.class);
            }
        };
    }
    
    public Converter<Route, String> getRouteToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<Route, String>() {
            public String convert(Route route) {
                return route.getShortName();
            }
        };
    }
    
    public Converter<Integer, Route> getIdToRouteConverter() {
        return new org.springframework.core.convert.converter.Converter<Integer, Route>() {
            public Route convert(Integer id) {
                return Route.findRoute(id);
            }
        };
    }
    
    public Converter<String, Route> getStringToRouteConverter() {
        return new org.springframework.core.convert.converter.Converter<String, Route>() {
            public Route convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Route.class);
            }
        };
    }
    
    public Converter<Stop, String> getStopToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<Stop, String>() {
            public String convert(Stop stop) {
                return stop.getName();
            }
        };
    }
    
    public Converter<Integer, Stop> getIdToStopConverter() {
        return new org.springframework.core.convert.converter.Converter<Integer, Stop>() {
            public Stop convert(Integer id) {
                return Stop.findStop(id);
            }
        };
    }
    
    public Converter<String, Stop> getStringToStopConverter() {
        return new org.springframework.core.convert.converter.Converter<String, Stop>() {
            public Stop convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Stop.class);
            }
        };
    }
    
    public Converter<StopTime, String> getStopTimeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<StopTime, String>() {
            public String convert(StopTime stopTime) {
                return stopTime.getStop().getName();
            }
        };
    }
    
    public Converter<Integer, StopTime> getIdToStopTimeConverter() {
        return new org.springframework.core.convert.converter.Converter<Integer, StopTime>() {
            public StopTime convert(Integer id) {
                return StopTime.findStopTime(id);
            }
        };
    }
    
    public Converter<String, StopTime> getStringToStopTimeConverter() {
        return new org.springframework.core.convert.converter.Converter<String, StopTime>() {
            public StopTime convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), StopTime.class);
            }
        };
    }
    
    public Converter<Trip, String> getTripToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<Trip, String>() {
            public String convert(Trip trip) {
                return trip.getHeadSign();
            }
        };
    }
    
    public Converter<Integer, Trip> getIdToTripConverter() {
        return new org.springframework.core.convert.converter.Converter<Integer, Trip>() {
            public Trip convert(Integer id) {
                return Trip.findTrip(id);
            }
        };
    }
    
    public Converter<String, Trip> getStringToTripConverter() {
        return new org.springframework.core.convert.converter.Converter<String, Trip>() {
            public Trip convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Trip.class);
            }
        };
    }
    
    public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getCheckinToStringConverter());
        registry.addConverter(getIdToCheckinConverter());
        registry.addConverter(getStringToCheckinConverter());
        registry.addConverter(getRouteToStringConverter());
        registry.addConverter(getIdToRouteConverter());
        registry.addConverter(getStringToRouteConverter());
        registry.addConverter(getStopToStringConverter());
        registry.addConverter(getIdToStopConverter());
        registry.addConverter(getStringToStopConverter());
        registry.addConverter(getStopTimeToStringConverter());
        registry.addConverter(getIdToStopTimeConverter());
        registry.addConverter(getStringToStopTimeConverter());
        registry.addConverter(getTripToStringConverter());
        registry.addConverter(getIdToTripConverter());
        registry.addConverter(getStringToTripConverter());
    }
    
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}
