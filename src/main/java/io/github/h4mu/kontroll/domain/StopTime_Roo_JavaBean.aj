// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package io.github.h4mu.kontroll.domain;

import io.github.h4mu.kontroll.domain.Stop;
import io.github.h4mu.kontroll.domain.StopTime;
import io.github.h4mu.kontroll.domain.Trip;

privileged aspect StopTime_Roo_JavaBean {
    
    public int StopTime.getSequence() {
        return this.sequence;
    }
    
    public void StopTime.setSequence(int sequence) {
        this.sequence = sequence;
    }
    
    public Trip StopTime.getTrip() {
        return this.trip;
    }
    
    public void StopTime.setTrip(Trip trip) {
        this.trip = trip;
    }
    
    public Stop StopTime.getStop() {
        return this.stop;
    }
    
    public void StopTime.setStop(Stop stop) {
        this.stop = stop;
    }
    
}
