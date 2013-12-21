package io.github.h4mu.kontroll.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.ManyToOne;
import java.util.HashSet;
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
@RooJpaActiveRecord(identifierType = Integer.class)
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

    public static TypedQuery<Trip> findTripsByRouteIdOrderedByHeadSign(Route route) {
        if (route == null) throw new IllegalArgumentException("Wrong route parameter");
        TypedQuery<Trip> query = route.entityManager.createQuery("SELECT o FROM Trip AS o WHERE o.route = :route ORDER BY o.headSign", Trip.class);
        query.setParameter("route", route);
        return query;
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
