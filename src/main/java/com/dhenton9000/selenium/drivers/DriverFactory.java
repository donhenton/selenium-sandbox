/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.drivers;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class DriverFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DriverFactory.class);

    /**
     * driver types currently only firefox supported
     */
    public enum DRIVER_TYPES {

        FireFox, InternetExplorer, Opera, Safari, Chrome;
    }

    /**
     * local points to a local firefox, chrome instance remoteAlpha would be
     * saucelabs or a docker selenuminum grid/standalone
     */
    public static enum DRIVER_ENV {

        local, remoteAlpha,phantomjs
    };

    /**
     * set up the driver with configuration parameters
     *
     * @param driverType
     * @param tempDownloadPath the temp folder for downloads if null then that
     * property is not set
     * @return the configured Driver
     */
    public WebDriver configureDriver(DRIVER_TYPES driverType, String tempDownloadPath) {
        WebDriver driver = null;
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.SEVERE);
        logs.enable(LogType.CLIENT, Level.SEVERE);
        logs.enable(LogType.DRIVER, Level.SEVERE);
        logs.enable(LogType.PERFORMANCE, Level.SEVERE);
        logs.enable(LogType.PROFILER, Level.SEVERE);
        logs.enable(LogType.SERVER, Level.SEVERE);

        switch (driverType) {
            case FireFox:
            default:
                DesiredCapabilities desiredCapabilities = DesiredCapabilities.firefox();
                desiredCapabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);

                // sets the driver to automatically skip download dialog
                // and save csv,xcel files to a temp directory
                // that directory is set in the constructor and has a trailing
                // slash
                FirefoxProfile firefoxProfile = new FirefoxProfile();
                if (tempDownloadPath != null) {
                    firefoxProfile.setPreference("browser.download.folderList", 2);
                    firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
                    firefoxProfile.setPreference("browser.download.dir", tempDownloadPath);
                    firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv,application/vnd.ms-excel");
                }
                desiredCapabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);

                LOG.debug("creating firefox driver");
                driver = new FirefoxDriver(desiredCapabilities);
                LOG.debug("got firefox driver");
                break;
            case InternetExplorer:
                break;
            case Opera:
                break;
            case Safari:
                break;
            case Chrome:
                break;

        }
        if (driver == null) {
            throw new RuntimeException("could not configure driver");
        } else {
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            LOG.debug("driver is loaded via config " + driver.toString());
        }
        return driver;
    }
    
    
     public final WebDriver configurePhantomJsDriver(Configuration sConfig)
            throws IOException {
        DesiredCapabilities sCaps = new DesiredCapabilities();
        sCaps.setJavascriptEnabled(true);
        sCaps.setCapability("takesScreenshot", false);
        if (sConfig.getProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY) != null) {
            sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                    sConfig.getProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY));
        } else {
            throw new IOException(String.format("Property '%s' not set!",
                    PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY));
        }
        // "phantomjs_driver_path"
        if (sConfig.getProperty(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_PATH_PROPERTY) != null) {
            LOG.debug("Test will use an external GhostDriver");
            sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_PATH_PROPERTY,
                    sConfig.getProperty(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_PATH_PROPERTY));
        } else {
            LOG.debug("Test will use PhantomJS internal GhostDriver");
        }

        WebDriver mDriver = new PhantomJSDriver(sCaps);

        return mDriver;

    }
}
