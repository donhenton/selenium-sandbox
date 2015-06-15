/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.generic;
 
 
import org.junit.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

/**
 *
 * @author dhenton
 */
public class AppspotTestBase extends BaseTest {

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
        
        appspotRepository = new AppspotRepository(getAutomationRepository());
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
