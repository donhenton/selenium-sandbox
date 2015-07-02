/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.angular;

import com.dhenton9000.selenium.generic.BaseTest;
import com.dhenton9000.testng.listeners.FailedTestScreenShotListener;
import com.dhenton9000.testng.listeners.TestingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;

/**
 *
 * @author dhenton
 */
@Listeners({TestingListener.class,FailedTestScreenShotListener.class})
public class AngularTestBase extends BaseTest{

    private RestaurantRepository restaurantRepository = null;
    private static final Logger LOG = LoggerFactory.getLogger(AngularTestBase.class);
    /**
     * if true then close driver after each test in the @Before method, if set
     * to false, then browser stays open for inspection
     *
     */
    private boolean closeDriver = true;

    public AngularTestBase() {

    }

    /**
     * @return the AngularRepository
     */
    public RestaurantRepository getRestaurantRepository() {
        if (restaurantRepository == null) {
            restaurantRepository = new RestaurantRepository(this.getAutomationRepository());
        }
        return restaurantRepository;
    }

     

    @AfterClass
    public void teardownTest() {

        LOG.debug("in teardown class with flag " + isCloseDriver());
        if (isCloseDriver()) {
            LOG.info("quitting driver ");
            getRestaurantRepository().getAutomation().quitDriver();
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
