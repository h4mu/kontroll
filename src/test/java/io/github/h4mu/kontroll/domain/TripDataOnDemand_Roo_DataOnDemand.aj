// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package io.github.h4mu.kontroll.domain;

import io.github.h4mu.kontroll.domain.RouteDataOnDemand;
import io.github.h4mu.kontroll.domain.Trip;
import io.github.h4mu.kontroll.domain.TripDataOnDemand;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect TripDataOnDemand_Roo_DataOnDemand {
    
    declare @type: TripDataOnDemand: @Component;
    
    private Random TripDataOnDemand.rnd = new SecureRandom();
    
    private List<Trip> TripDataOnDemand.data;
    
    @Autowired
    RouteDataOnDemand TripDataOnDemand.routeDataOnDemand;
    
    public Trip TripDataOnDemand.getNewTransientTrip(int index) {
        Trip obj = new Trip();
        setEndTime(obj, index);
        setHeadSign(obj, index);
        setIsReturn(obj, index);
        setStartTime(obj, index);
        return obj;
    }
    
    public void TripDataOnDemand.setEndTime(Trip obj, int index) {
        Date endTime = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setEndTime(endTime);
    }
    
    public void TripDataOnDemand.setHeadSign(Trip obj, int index) {
        String headSign = "headSign_" + index;
        obj.setHeadSign(headSign);
    }
    
    public void TripDataOnDemand.setIsReturn(Trip obj, int index) {
        Boolean isReturn = Boolean.TRUE;
        obj.setIsReturn(isReturn);
    }
    
    public void TripDataOnDemand.setStartTime(Trip obj, int index) {
        Date startTime = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setStartTime(startTime);
    }
    
    public Trip TripDataOnDemand.getSpecificTrip(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Trip obj = data.get(index);
        Integer id = obj.getId();
        return Trip.findTrip(id);
    }
    
    public Trip TripDataOnDemand.getRandomTrip() {
        init();
        Trip obj = data.get(rnd.nextInt(data.size()));
        Integer id = obj.getId();
        return Trip.findTrip(id);
    }
    
    public boolean TripDataOnDemand.modifyTrip(Trip obj) {
        return false;
    }
    
    public void TripDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Trip.findTripEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Trip' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Trip>();
        for (int i = 0; i < 10; i++) {
            Trip obj = getNewTransientTrip(i);
            try {
                obj.persist();
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
