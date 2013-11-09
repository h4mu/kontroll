package io.github.h4mu.kontroll.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(identifierType = Integer.class)
public class StopTime {

    /**
     */
    private int sequence;

    /**
     */
    @ManyToOne
    private Trip trip;

    /**
     */
    @ManyToOne
    private Stop stop;

	public static TypedQuery<StopTime> findStopTimesByTripIdOrderedBySequence(Trip trip) {
		if (trip == null) throw new IllegalArgumentException("Needed Trip argument");
		EntityManager entityManager = trip.entityManager();
		TypedQuery<StopTime> q = entityManager.createQuery("SELECT o FROM StopTime AS o WHERE o.trip = :trip ORDER BY o.sequence", StopTime.class);
		q.setParameter("trip", trip);
		return q;
	}
}
