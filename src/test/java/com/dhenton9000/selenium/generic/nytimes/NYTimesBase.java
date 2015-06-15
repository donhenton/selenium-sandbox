/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.generic.nytimes;

import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import com.dhenton9000.selenium.generic.NYTimesRepository;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;

/**
 *
 * @author dhenton
 */
public class NYTimesBase {

    private NYTimesRepository nyTimesRepository = null;
    private static final Logger LOG = LoggerFactory.getLogger(NYTimesBase.class);
    /**
     * if true then close driver after each test in the @Before method, if set
     * to false, then browser stays open for inspection
     *
     */
    private boolean closeDriver = true;

    public NYTimesBase() {
        Configuration config = null;
        LOG.debug("using properties file");
        try {
            config = new PropertiesConfiguration("env.properties");
            LOG.debug("reading config in " + this.getClass().getName());
        } catch (ConfigurationException ex) {
            LOG.info("did not find env.properties file");
        }

        nyTimesRepository = new NYTimesRepository(config);
    }

    public NYTimesBase(Configuration config) {

        nyTimesRepository = new NYTimesRepository(config);
    }

    /**
     * @return the appspotRepository
     */
    public NYTimesRepository getNYTimesRepository() {
        return nyTimesRepository;
    }

    public GenericAutomationRepository getAutomation() {
        return nyTimesRepository.getAutomation();
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
