/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.phantomjs;

import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import com.lazerycode.selenium.JettyServer;
import java.io.IOException;
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

/**
 *
 * @author dhenton
 */
public class PhantomJSTest {

    private static final Logger LOG
            = LoggerFactory.getLogger(PhantomJSTest.class);
    private static JettyServer localWebServer;
    private static String webServerURL = "http://localhost";
    private static int webServerPort = 9081;
    private static String downloadURI200;

    private PropertiesConfiguration config;
    private WebDriver driver = null;
    private GenericAutomationRepository automation = null;

    public PhantomJSTest() {

        LOG.debug("using properties file");
        try {
            config = new PropertiesConfiguration("env.properties");
            LOG.debug("reading config in " + this.getClass().getName());
            driver = configureDriver(config);

            this.automation = new GenericAutomationRepository(driver, config);
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public GenericAutomationRepository getAutomation() {
        return automation;
    }

    @BeforeClass
    public static void start() throws Exception {
        localWebServer = new JettyServer(webServerPort, "/phantom_web");
        downloadURI200 = webServerURL + ":" + webServerPort + "/phantom.html";

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
