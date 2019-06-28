/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.drivers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class DriverFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DriverFactory.class);
    public static final String ENV_PROPERTIES_FILENAME = "env.properties";
    public static final String DOCKER_SELENIUM_URL_PROPERTY = "docker.selenium.url";

    /**
     * driver types currently only firefox supported
     */
    public enum DRIVER_TYPES {

        FireFox, InternetExplorer, Opera, Safari, Chrome;
    }

    /**
     * local points to a local firefox, chrome instance remoteAlpha would be
     * saucelabs or a docker selenuminum grid/standalone
     *
     */
    public static enum REMOTE_SERVER_VALUE {

        local, docker, phantomjs
    };

    public static enum ENV {

        alpha, beta,dev,prod
    }

    /**
     * get a driver based on the remote.server System property
     *
     * @return
     * @throws IOException
     */
    public WebDriver getDriver() throws IOException {

        REMOTE_SERVER_VALUE env = getRemoteServerValue();
 
        if (env == null) {
            env = REMOTE_SERVER_VALUE.local;
        }
        LOG.info("remote driver is "+env.toString());

        return getDriver(env);

    }

    /**
     * the remote server value set at the command line.
     *
     * @return
     */
    public static REMOTE_SERVER_VALUE getRemoteServerValue() {
        String envString = System.getProperty("remote.server");
        REMOTE_SERVER_VALUE rValue = null;
        try {
            rValue = REMOTE_SERVER_VALUE.valueOf(envString);
        } catch (Exception err) {
        }

        return rValue;
    }

    /**
     * this is a value set in maven or at the maven command line. This is for
     * demo simple items
     *
     * @return
     */
    public static String getENVString() {
        ENV t = getENV();
        if (t == null) {
            return null;
        } else {
            return t.toString();
        }

    }

    /**
     * this is a value set in maven or at the maven command line. This is for
     * demo simple items
     *
     * @return
     */
    public static ENV getENV() {
        ENV currentENV = null;
        String envString = System.getProperty("test.env");
        if (StringUtils.isBlank(envString)) {
            currentENV = null;
        } else {
            try {
                currentENV = ENV.valueOf(envString);
            } catch (Exception err) {
            }
        }

        return currentENV;

    }

    public WebDriver getDriver(REMOTE_SERVER_VALUE env) throws IOException {
        WebDriver driver = null;
        switch (env) {
            case phantomjs:
                driver = configurePhantomJsDriver();
                break;
            case docker:
                driver = configureRemoteAlphaDriver();
                break;
            case local:
                driver
                        = configureDriver(
                                DriverFactory.DRIVER_TYPES.FireFox, null);
                driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
                driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
                break;
            default:
                throw new UnsupportedOperationException("driver "
                        + env.toString() + "not supported yet");

        }

        return driver;
    }

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
                    firefoxProfile.setPreference(
                            "browser.helperApps.neverAsk.saveToDisk",
                            "text/csv,application/vnd.ms-excel");
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

    public static Configuration getConfiguration() {
        Configuration config = null;

        try {
            config = new PropertiesConfiguration(ENV_PROPERTIES_FILENAME);
            LOG.debug("reading config in   DriverFactory");
        } catch (ConfigurationException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return config;
    }

    public final WebDriver configurePhantomJsDriver()
            throws IOException {
        Configuration sConfig = getConfiguration();
        DesiredCapabilities sCaps = new DesiredCapabilities();
        sCaps.setJavascriptEnabled(true);
        sCaps.setCapability("takesScreenshot", true);
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

    /**
     * this if for the docker instance in the docker folder.
     *
     * @return
     */
    private WebDriver configureRemoteAlphaDriver() {
        WebDriver driver = null;
        Configuration config = getConfiguration();
        String remoteURL = config.getString(DOCKER_SELENIUM_URL_PROPERTY,null);
        LOG.info("docker remote URL "+remoteURL);
        if (remoteURL == null)
        {
            throw new RuntimeException("you must set "+
                    DOCKER_SELENIUM_URL_PROPERTY + " in your "
            + ENV_PROPERTIES_FILENAME+" file");
        }
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.SEVERE);
        logs.enable(LogType.CLIENT, Level.SEVERE);
        logs.enable(LogType.DRIVER, Level.SEVERE);
        logs.enable(LogType.PERFORMANCE, Level.SEVERE);
        logs.enable(LogType.PROFILER, Level.SEVERE);
        logs.enable(LogType.SERVER, Level.SEVERE);

        DesiredCapabilities desiredCapabilities = DesiredCapabilities.firefox();
        desiredCapabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);

        try {

            driver = new RemoteWebDriver(new URL(remoteURL), desiredCapabilities);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Malformed remote url '" + remoteURL + "'");
        }

        return driver;
    }

}
