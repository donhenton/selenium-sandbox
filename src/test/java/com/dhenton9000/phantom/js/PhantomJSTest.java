/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.phantom.js;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class PhantomJSTest extends PhantomJSBase {

    private static final Logger LOG
            = LoggerFactory.getLogger(PhantomJSTest.class);

    private static final int PORT = 4444;
    private static final String CONTEXT_PATH = "/app";
   // private static final String APP_URL = "http://localhost:"
    //         + PORT + CONTEXT_PATH;
    private static String downloadURI200;

    @BeforeClass
    public static void start() throws Exception {

    }

    @AfterClass
    public static void stop() throws Exception {
        stopServer();
    }

    public PhantomJSTest() throws Exception {
        super(PORT, CONTEXT_PATH);
        initServer(null);
        downloadURI200 = composeURL("/phantom.jsp");
        LOG.debug("url '" + downloadURI200 + "'");
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

}
