/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.d3;

import com.dhenton9000.selenium.generic.JSMethods;
import com.dhenton9000.phantom.js.PhantomJSBase;
import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.NoSuchElementException;

/**
 * Demonstration of embedded jetty and phantomjs driver
 *
 * @author dhenton
 */
public class MouseSandboxTests extends PhantomJSBase {

    private static final Logger LOG = LoggerFactory.getLogger(D3Tests.class);
    private static final int PORT = 4444;
    private static final String CONTEXT_PATH = "/mouseevent";
    private static final String APP_URL = "http://localhost:"
            + PORT + CONTEXT_PATH;

    public MouseSandboxTests() throws Exception {
        super(PORT, CONTEXT_PATH);
        initServer("src/test/resources/mouseevent");
    }

    @BeforeClass
    public static void start() throws Exception {

    }

    @AfterClass
    public static void stop() throws Exception {
        stopServer();
    }

    @Before
    public void beforeTest() {
        String url = composeURL("/index.html");
        LOG.debug("url "+url);
        this.getAutomation().maximizeWindow();
        this.getAutomation().getDriver().get(url);
    }

    @After
    public void closeWebDriver() {
        this.getAutomation().getDriver().close();

    }

    @Test
    public void testJQuery() {

        String info = null;
        try {
            WebElement w = getAutomation().findElement(
                    GenericAutomationRepository.SELECTOR_CHOICE.tagName, "h2");
            info = w.getText();
        } catch (NoSuchElementException err) {

        }

        assertNotNull(info);
        assertEquals("On Hover",info);
    }

    @Test
    public void testMouseMove() {

        String cssSelector = "div#hoverItem";
        String testSelector = "div.tooltip";
        boolean notThere = true;
        try {
            getAutomation().findElement(
                    GenericAutomationRepository.SELECTOR_CHOICE.cssSelector, testSelector);
        } catch (NoSuchElementException err) {
            notThere = false;
        }

        assertFalse(notThere);

        JSMethods.ElementDimension dim
                = this.getAutomation()
                .getJsMethods().getElementDimensions(cssSelector);

        this.getAutomation().getJsMethods()
                .mouseOver(cssSelector, 10 + dim.left, 10 + dim.top);

        WebElement w = null;
        try {
            w = getAutomation().findElement(
                    GenericAutomationRepository.SELECTOR_CHOICE.cssSelector,
                    testSelector);

        } catch (NoSuchElementException err) {

        }
        assertNotNull(w);
    }

}
