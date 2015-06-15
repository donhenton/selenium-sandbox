/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.phantom.js;

import com.dhenton9000.embedded.jetty.JettyServer;
import com.dhenton9000.selenium.drivers.DriverFactory;
import com.dhenton9000.selenium.drivers.DriverFactory.DRIVER_ENV;
import com.dhenton9000.selenium.generic.BaseTest;
import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class forms the basis for tests that want to use a embedded server
 * and phantomjs  web driver.
 * 
 * For an example of use  see {@link com.dhenton9000.js.inject.JSInjectTest}
 *
 * @author dhenton
 */
public class PhantomJSBase extends BaseTest {

    private static final Logger LOG
            = LoggerFactory.getLogger(PhantomJSTest.class);

   // private final PropertiesConfiguration config;
    private WebDriver driver = null;
    private GenericAutomationRepository automation = null;
    private static JettyServer localWebServer = null;
    private static int port;
    /**
     * the app context, eg http://localhost:4444/app_context
     */
    private static String appContext;

    public PhantomJSBase(int portNumber, String appContextStr) {

        port = portNumber;
        appContext = appContextStr;
        LOG.debug("using properties file");
        try {
            //config = new PropertiesConfiguration("env.properties");
            LOG.debug("reading config in " + this.getClass().getName());
            driver = getDriver(DRIVER_ENV.phantomjs);

            this.automation = new GenericAutomationRepository(driver, getConfiguration());
        } catch ( IOException ex  ) {
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

    /**
     * initialize the server with an optional resource base. The resource base
     * is where files will be served from
     * null will default to 'src/main/resources'
     * 
     * @param resourceBase
     * @throws Exception 
     */
    protected static void initServer(String resourceBase) throws Exception {
        if (localWebServer == null) {
            localWebServer = new JettyServer(port, appContext, resourceBase);
        }

    }

    public GenericAutomationRepository getAutomation() {
        return automation;
    }

    public static String composeURL(String item) {
        return "http://localhost:" + getPort() + getAppContext() + item;
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
