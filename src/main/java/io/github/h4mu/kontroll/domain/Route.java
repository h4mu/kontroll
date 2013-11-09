package io.github.h4mu.kontroll.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

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
}
