// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package io.github.h4mu.kontroll.domain;

import io.github.h4mu.kontroll.domain.Checkin;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Checkin_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "checkinPersistenceUnit")
    transient EntityManager Checkin.entityManager;
    
    public static final EntityManager Checkin.entityManager() {
        EntityManager em = new Checkin().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Checkin.countCheckins() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Checkin o", Long.class).getSingleResult();
    }
    
    public static List<Checkin> Checkin.findAllCheckins() {
        return entityManager().createQuery("SELECT o FROM Checkin o", Checkin.class).getResultList();
    }
    
    public static Checkin Checkin.findCheckin(Integer id) {
        if (id == null) return null;
        return entityManager().find(Checkin.class, id);
    }
    
    public static List<Checkin> Checkin.findCheckinEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Checkin o", Checkin.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional("checkinTransactionManager")
    public void Checkin.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional("checkinTransactionManager")
    public void Checkin.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Checkin attached = Checkin.findCheckin(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional("checkinTransactionManager")
    public void Checkin.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional("checkinTransactionManager")
    public void Checkin.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional("checkinTransactionManager")
    public Checkin Checkin.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Checkin merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
