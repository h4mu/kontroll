package io.github.h4mu.kontroll.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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

    public static TypedQuery<Route> findAllRoutesOrderedByShortName() {
        EntityManager entityManager = entityManager();
        TypedQuery<Route> query = entityManager.createQuery("SELECT o FROM Route AS o ORDER BY o.shortName", Route.class);
        return query;
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
