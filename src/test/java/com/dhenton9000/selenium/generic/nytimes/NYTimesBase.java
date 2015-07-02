/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.generic.nytimes;

import com.dhenton9000.selenium.generic.BaseTest;
import com.dhenton9000.selenium.generic.NYTimesRepository;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;

/**
 *
 * @author dhenton
 */
public class NYTimesBase extends BaseTest {

    private NYTimesRepository nyTimesRepository = null;
    private static final Logger LOG = LoggerFactory.getLogger(NYTimesBase.class);
    /**
     * if true then close driver after each test in the @Before method, if set
     * to false, then browser stays open for inspection
     *
     */
    private boolean closeDriver = true;

    public NYTimesBase() {
         

        nyTimesRepository = new NYTimesRepository(getAutomationRepository());
    }

    

    /**
     * @return the appspotRepository
     */
    public NYTimesRepository getNYTimesRepository() {
        return nyTimesRepository;
    }

    

    @AfterClass
    public void teardownTest() {

        LOG.debug("in teardown class with flag " + isCloseDriver());
        if (isCloseDriver()) {
            LOG.info("quitting driver ");
            getNYTimesRepository().getAutomation().quitDriver();
        }
    }

    /**
     * @return the closeDriver
     */
    public boolean isCloseDriver() {
        return closeDriver;
    }

    /**
     * @param closeDriver the closeDriver to set
     */
    public void setCloseDriver(boolean closeDriver) {
        this.closeDriver = closeDriver;
    }

}
