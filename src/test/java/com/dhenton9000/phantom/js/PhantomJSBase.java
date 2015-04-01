/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.phantom.js;

import com.dhenton9000.embedded.jetty.JettyServer;
import com.dhenton9000.phantom.js.PhantomJSTest;
import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import java.io.IOException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class PhantomJSBase {

    private static final Logger LOG
            = LoggerFactory.getLogger(PhantomJSTest.class);

    private final PropertiesConfiguration config;
    private WebDriver driver = null;
    private GenericAutomationRepository automation = null;
    private static JettyServer localWebServer;
    private static int port;
    private static String appContext;

    public PhantomJSBase(int portNumber, String appContextStr) {
        port = portNumber;
        appContext = appContextStr;
        LOG.debug("using properties file");
        try {
            config = new PropertiesConfiguration("env.properties");
            LOG.debug("reading config in " + this.getClass().getName());
            driver = configureDriver(getConfig());

            this.automation = new GenericAutomationRepository(driver, getConfig());
        } catch (ConfigurationException | IOException ex) {
            throw new RuntimeException(ex);

        }

    }

    /**
     * @return the localWebServer
     */
    public static JettyServer getLocalWebServer() {
        return localWebServer;
    }

    protected static void stopServer() throws Exception {
        localWebServer.stopJettyServer();
    }

    protected static void initServer( String resourceBase) throws Exception {

        localWebServer = new JettyServer(port, appContext,resourceBase);
        
    }

    public GenericAutomationRepository getAutomation() {
        return automation;
    }

    public static String composeURL(String item) {
        return "http://localhost:" + getPort() + getAppContext() + item;
    }

    protected final WebDriver configureDriver(PropertiesConfiguration sConfig)
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

    /**
     * @return the config
     */
    public final PropertiesConfiguration getConfig() {
        return config;
    }

    /**
     * @return the port
     */
    public static int getPort() {
        return port;
    }

    /**
     * @return the appContext
     */
    public static String getAppContext() {
        return appContext;
    }

}
