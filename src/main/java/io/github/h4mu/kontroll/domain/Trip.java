package io.github.h4mu.kontroll.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

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
}
