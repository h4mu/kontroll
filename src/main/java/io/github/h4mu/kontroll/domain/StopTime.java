package io.github.h4mu.kontroll.domain;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

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
		TypedQuery<StopTime> q = trip.entityManager.createQuery(
				"SELECT o FROM StopTime AS o WHERE o.trip = :trip ORDER BY o.sequence", StopTime.class);
		q.setParameter("trip", trip);
		return q;
	}
}
