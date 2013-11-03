package io.github.h4mu.kontroll.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.ManyToOne;

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
}
