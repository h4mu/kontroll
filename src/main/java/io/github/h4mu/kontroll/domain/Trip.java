package io.github.h4mu.kontroll.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.ManyToOne;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(identifierType = Integer.class, persistenceUnit = "persistenceUnit", transactionManager = "transactionManager")
public class Trip {

    /**
     */
    private Boolean isReturn;

    /**
     */
    private String headSign;

    /**
     */
    @ManyToOne
    private Route route;

    /**
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trip")
    private Set<StopTime> stopTimes = new HashSet<StopTime>();

    public static List<Trip> findTripsByRouteIdOrderedByHeadSign(Route route) {
        if (route == null) throw new IllegalArgumentException("Wrong route parameter");
		Calendar epoch = Calendar.getInstance();
		epoch.setTimeInMillis(0);
		Calendar timeNow = Calendar.getInstance();
		timeNow.set(Calendar.YEAR, epoch.get(Calendar.YEAR));
		timeNow.set(Calendar.DAY_OF_YEAR, epoch.get(Calendar.DAY_OF_YEAR));
        TypedQuery<Trip> query = route.entityManager.createQuery("SELECT t FROM Trip AS t WHERE t.route = :route " 
			+ "AND (:timeNow BETWEEN t.startTime AND t.endTime " 
	        + "OR (t.startTime > t.endTime AND " 
			+ "(:timeNow >= t.startTime OR :timeNow <= t.endTime))) " 
    		+ "ORDER BY t.headSign", Trip.class);
        query.setParameter("route", route);
        query.setParameter("timeNow", timeNow.getTime());
        return query.getResultList();
    }

    /**
     */
    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "hh:mm:ss")
    private Date startTime;

    /**
     */
    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "hh:mm:ss")
    private Date endTime;
}
