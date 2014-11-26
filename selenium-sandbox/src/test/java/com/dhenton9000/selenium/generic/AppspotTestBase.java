/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.generic;
 
 
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

/**
 *
 * @author dhenton
 */
public class AppspotTestBase {

    private   AppspotRepository appspotRepository = null;
    private static final Logger LOG = LoggerFactory.getLogger(AppspotTestBase.class);
    /**
     * if true then close driver after each test
     * in the @Before method, if set to false, then browser stays 
     * open for inspection
     * 
     */
    private boolean closeDriver = true;

    public AppspotTestBase() {
        Configuration config = null;
        LOG.debug("using properties file");
        try {
            config = new PropertiesConfiguration("env.properties");
            LOG.debug("reading config in " + this.getClass().getName());
        } catch (ConfigurationException ex) {
            LOG.info("did not find env.properties file");
        }

        appspotRepository = new AppspotRepository(config);
    }

    public AppspotTestBase(Configuration config) {

        appspotRepository = new AppspotRepository(config);
    }

    /**
     * @return the appspotRepository
     */
    public AppspotRepository getAppspotRepository() {
        return appspotRepository;
    }
    
    public GenericAutomationRepository getAutomation()
    {
        return appspotRepository.getAutomation();
    }

    

    @After 
    public void teardownTest () {
     
      LOG.debug("in teardown class with flag "+isCloseDriver());
      if (isCloseDriver())
      {
            LOG.info("quitting driver ");
            getAppspotRepository().getAutomation().quitDriver();
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
