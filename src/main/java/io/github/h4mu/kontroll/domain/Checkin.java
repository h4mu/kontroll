package io.github.h4mu.kontroll.domain;
import java.util.Calendar;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(identifierType = Integer.class)
public class Checkin {

    /**
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "SM")
    private Calendar spottingTime;

    /**
     */
    @NotNull
    @ManyToOne
    private StopTime stopTime;

	public static List<Checkin> findTodaysCheckins() {
		Calendar aDayAgo = Calendar.getInstance();
		aDayAgo.add(Calendar.DAY_OF_YEAR, -1);
		TypedQuery<Checkin> query = entityManager().createQuery("SELECT o FROM Checkin o WHERE o.spottingTime > :aDayAgo ORDER BY o.spottingTime DESC", Checkin.class);
		query.setParameter("aDayAgo", aDayAgo);
		return query.getResultList();
	}
}
