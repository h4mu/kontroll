// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package io.github.h4mu.kontroll.domain;

import io.github.h4mu.kontroll.domain.StopTime;
import io.github.h4mu.kontroll.domain.StopTimeDataOnDemand;
import io.github.h4mu.kontroll.domain.StopTimeIntegrationTest;
import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect StopTimeIntegrationTest_Roo_IntegrationTest {
    
    declare @type: StopTimeIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: StopTimeIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: StopTimeIntegrationTest: @Transactional;
    
    @Autowired
    StopTimeDataOnDemand StopTimeIntegrationTest.dod;
    
    @Test
    public void StopTimeIntegrationTest.testCountStopTimes() {
        Assert.assertNotNull("Data on demand for 'StopTime' failed to initialize correctly", dod.getRandomStopTime());
        long count = StopTime.countStopTimes();
        Assert.assertTrue("Counter for 'StopTime' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void StopTimeIntegrationTest.testFindStopTime() {
        StopTime obj = dod.getRandomStopTime();
        Assert.assertNotNull("Data on demand for 'StopTime' failed to initialize correctly", obj);
        Integer id = obj.getId();
        Assert.assertNotNull("Data on demand for 'StopTime' failed to provide an identifier", id);
        obj = StopTime.findStopTime(id);
        Assert.assertNotNull("Find method for 'StopTime' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'StopTime' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void StopTimeIntegrationTest.testFindAllStopTimes() {
        Assert.assertNotNull("Data on demand for 'StopTime' failed to initialize correctly", dod.getRandomStopTime());
        long count = StopTime.countStopTimes();
        Assert.assertTrue("Too expensive to perform a find all test for 'StopTime', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<StopTime> result = StopTime.findAllStopTimes();
        Assert.assertNotNull("Find all method for 'StopTime' illegally returned null", result);
        Assert.assertTrue("Find all method for 'StopTime' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void StopTimeIntegrationTest.testFindStopTimeEntries() {
        Assert.assertNotNull("Data on demand for 'StopTime' failed to initialize correctly", dod.getRandomStopTime());
        long count = StopTime.countStopTimes();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<StopTime> result = StopTime.findStopTimeEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'StopTime' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'StopTime' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void StopTimeIntegrationTest.testFlush() {
        StopTime obj = dod.getRandomStopTime();
        Assert.assertNotNull("Data on demand for 'StopTime' failed to initialize correctly", obj);
        Integer id = obj.getId();
        Assert.assertNotNull("Data on demand for 'StopTime' failed to provide an identifier", id);
        obj = StopTime.findStopTime(id);
        Assert.assertNotNull("Find method for 'StopTime' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyStopTime(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'StopTime' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void StopTimeIntegrationTest.testMergeUpdate() {
        StopTime obj = dod.getRandomStopTime();
        Assert.assertNotNull("Data on demand for 'StopTime' failed to initialize correctly", obj);
        Integer id = obj.getId();
        Assert.assertNotNull("Data on demand for 'StopTime' failed to provide an identifier", id);
        obj = StopTime.findStopTime(id);
        boolean modified =  dod.modifyStopTime(obj);
        Integer currentVersion = obj.getVersion();
        StopTime merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'StopTime' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void StopTimeIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'StopTime' failed to initialize correctly", dod.getRandomStopTime());
        StopTime obj = dod.getNewTransientStopTime(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'StopTime' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'StopTime' identifier to be null", obj.getId());
        try {
            obj.persist();
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        obj.flush();
        Assert.assertNotNull("Expected 'StopTime' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void StopTimeIntegrationTest.testRemove() {
        StopTime obj = dod.getRandomStopTime();
        Assert.assertNotNull("Data on demand for 'StopTime' failed to initialize correctly", obj);
        Integer id = obj.getId();
        Assert.assertNotNull("Data on demand for 'StopTime' failed to provide an identifier", id);
        obj = StopTime.findStopTime(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'StopTime' with identifier '" + id + "'", StopTime.findStopTime(id));
    }
    
}
