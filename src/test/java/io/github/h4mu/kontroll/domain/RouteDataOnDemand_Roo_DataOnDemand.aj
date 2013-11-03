// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package io.github.h4mu.kontroll.domain;

import io.github.h4mu.kontroll.domain.Route;
import io.github.h4mu.kontroll.domain.RouteDataOnDemand;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

privileged aspect RouteDataOnDemand_Roo_DataOnDemand {
    
    declare @type: RouteDataOnDemand: @Component;
    
    private Random RouteDataOnDemand.rnd = new SecureRandom();
    
    private List<Route> RouteDataOnDemand.data;
    
    public Route RouteDataOnDemand.getNewTransientRoute(int index) {
        Route obj = new Route();
        setShortName(obj, index);
        return obj;
    }
    
    public void RouteDataOnDemand.setShortName(Route obj, int index) {
        String shortName = "shortName_" + index;
        obj.setShortName(shortName);
    }
    
    public Route RouteDataOnDemand.getSpecificRoute(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Route obj = data.get(index);
        Integer id = obj.getId();
        return Route.findRoute(id);
    }
    
    public Route RouteDataOnDemand.getRandomRoute() {
        init();
        Route obj = data.get(rnd.nextInt(data.size()));
        Integer id = obj.getId();
        return Route.findRoute(id);
    }
    
    public boolean RouteDataOnDemand.modifyRoute(Route obj) {
        return false;
    }
    
    public void RouteDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Route.findRouteEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Route' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Route>();
        for (int i = 0; i < 10; i++) {
            Route obj = getNewTransientRoute(i);
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
