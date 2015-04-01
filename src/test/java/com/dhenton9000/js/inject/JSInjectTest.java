/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.js.inject;

import com.dhenton9000.phantom.js.*;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class JSInjectTest extends PhantomJSBase {

    private static final Logger LOG
            = LoggerFactory.getLogger(JSInjectTest.class);

    private static final int PORT = 4444;
    private static final String CONTEXT_PATH = "/app";
    private static final String APP_URL = "http://localhost:"
            + PORT + CONTEXT_PATH;
    private static String downloadURI200;

    @BeforeClass
    public static void start() throws Exception {

    }

    @AfterClass
    public static void stop() throws Exception {
        stopServer();
    }

    public JSInjectTest() throws Exception {

        super(PORT, CONTEXT_PATH);
        initServer("src/test/resources/jsinject");
        downloadURI200 = composeURL("/");
        LOG.debug("url '" + downloadURI200 + "'");
        this.getAutomation().getDriver().get(downloadURI200);
        String runScript = "var s=window.document.createElement('script'); "
                + "s.src='qa_scripts/test.js';  "
                + "window.document.head.appendChild(s);";

        this.getAutomation().getJavascriptExecutor().executeScript(runScript);
    }

    @After
    public void closeWebDriver() {
        this.getAutomation().getDriver().close();

    }

    @Test
    public void testPhantomSetup() {

        assertEquals("Javascript Inject Page", this.getAutomation().getPageTitle());

    }

    @Test
    public void testInjection() {

        String t = (String) this.getAutomation().getJavascriptExecutor().executeScript("return testFunction('bonzo')");

        assertTrue(t.contains("get a job, bonzo!"));

    }

    @Test
    public void testJQueryAvailable() {
        

        String t = (String) this.getAutomation().getJavascriptExecutor().executeScript("return testJQuery('#test')");

        assertEquals(t,"Get a job");

    }

}
