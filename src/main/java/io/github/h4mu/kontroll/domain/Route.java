package io.github.h4mu.kontroll.domain;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(identifierType = Integer.class)
public class Route {

    /**
     */
    private String shortName;

    /**
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "route")
    private Set<Trip> trips = new HashSet<Trip>();

    public static List<Route> findAllRoutesOrderedByShortName() {
        EntityManager entityManager = entityManager();
		Calendar epoch = Calendar.getInstance();
		epoch.setTimeInMillis(0);
        TypedQuery<Route> query = entityManager.createQuery("SELECT DISTINCT o FROM Trip t INNER JOIN t.route o "
			+ "WHERE :timeNow BETWEEN t.startTime AND t.endTime " 
	        + "OR (t.startTime > t.endTime AND " 
			+ "(:timeNow <= t.startTime OR :timeNow >= t.endTime)) " 
	        + "ORDER BY o.color, o.textColor, o.shortName", Route.class);
		Calendar timeNow = Calendar.getInstance();
		timeNow.set(Calendar.YEAR, epoch.get(Calendar.YEAR));
		timeNow.set(Calendar.DAY_OF_YEAR, epoch.get(Calendar.DAY_OF_YEAR));
        query.setParameter("timeNow", timeNow.getTime());
        return query.getResultList();
    }

    /**
     */
    @Size(max = 6)
    @Pattern(regexp = "[0-9a-fA-F]{0,6}")
    private String color;

    /**
     */
    @Size(max = 6)
    @Pattern(regexp = "[0-9a-fA-F]{0,6}")
    private String textColor;
}
