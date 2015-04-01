/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.embedded.jetty;

import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import java.io.IOException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.openqa.selenium.browserlaunches.Proxies;

/**
 *
 * @author dhenton
 */
public class PhantomJSTest {

    private static final Logger LOG
            = LoggerFactory.getLogger(PhantomJSTest.class);
    private static JettyServer localWebServer;
    private static final int PORT = 4444;
    private static final String CONTEXT_PATH = "/app";
    private static final String APP_URL = "http://localhost:"
            + PORT + CONTEXT_PATH;
    private static String downloadURI200;

    private PropertiesConfiguration config;
    private WebDriver driver = null;
    private GenericAutomationRepository automation = null;

    @BeforeClass
    public static void start() throws Exception {
        localWebServer = new JettyServer(PORT, CONTEXT_PATH);
        downloadURI200 = APP_URL + "/phantom.jsp";

    }

    public PhantomJSTest() {

        LOG.debug("using properties file");
        try {
            config = new PropertiesConfiguration("env.properties");
            LOG.debug("reading config in " + this.getClass().getName());
            driver = configureDriver(config);

            this.automation = new GenericAutomationRepository(driver, config);
        } catch (ConfigurationException | IOException ex) {
            throw new RuntimeException(ex);

        }
    }

    public GenericAutomationRepository getAutomation() {
        return automation;
    }

    @AfterClass
    public static void stop() throws Exception {
        localWebServer.stopJettyServer();
    }

    @After
    public void closeWebDriver() {
        this.getAutomation().getDriver().close();
    }

    // ghost driver tests
    // https://github.com/detro/ghostdriver/blob/master/test/java/src/test/java/ghostdriver/FileUploadTest.java
    @Test
    public void testPhantomSetup() {

        this.getAutomation().getDriver().get(downloadURI200);

        assertEquals("Phantom Page", this.getAutomation().getPageTitle());

    }

    public final WebDriver configureDriver(PropertiesConfiguration sConfig)
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
