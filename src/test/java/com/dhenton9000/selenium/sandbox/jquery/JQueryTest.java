/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.jquery;

import com.dhenton9000.selenium.sandbox.wicketapp.BaseSeleniumWicketTest;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.thoughtworks.selenium.Selenium;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Don
 */
public class JQueryTest extends BaseSeleniumWicketTest {
    public static final String HTML_FILE = "selenium3.html";

    private static JavascriptExecutor js;
    private final static Logger logger = LoggerFactory.getLogger(JQueryTest.class);
    private static WebDriver driver;

    @BeforeClass
    public static void beforeClass() {
        //  FirefoxProfile profile = new FirefoxProfile();
        //  FirefoxBinary binary = new FirefoxBinary(new File("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe"));

        //  driver = new FirefoxDriver(binary, profile);
        driver = new FirefoxDriver();
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.get("file:///"+createPathToTestResources(HTML_FILE));
        js = (JavascriptExecutor) driver;

        // load jQuery dynamically
        // this allows selenium to exploit jquery;
        URL jqueryUrl = Resources.getResource("jquery.min.js");
        String jqueryText = null;
        try {
            jqueryText = Resources.toString(jqueryUrl, Charsets.UTF_8);
        } catch (IOException ex) {
            logger.error("problems finding jquery");
            throw new RuntimeException("problems finding jquery " + ex.getMessage());
        }
        js.executeScript(jqueryText);



    }

    @Before
    public void before() {
    }

    @AfterClass
    public static void after() {

        driver.close();
        driver.quit();

    }

    @Test
    public void testSelectingByJQueryAlone() {

        String jQuerySelector = "#users tr:has(td:contains('Bob')) button:contains('Remove')";
        WebElement removeButton = (WebElement) js.executeScript("return $(\"" + jQuerySelector + "\").get(0);");
        assertEquals("Remove", removeButton.getText());
    }
    
    @Ignore
    public void testClickBySeleniumObjectCSSSelector()
    {
    
        //Selenium can only select via id, text, linktext or css selectors
        Selenium selenium = new WebDriverBackedSelenium(driver, "file://"+createPathToTestResources(HTML_FILE));
        selenium.click("css=#users tr:has(td:contains('Frank')) button:contains('Remove')");
    }
    
    @Ignore
    public void testClickByDirectExecute()
    {
    
         String jQuerySelector = "#users tr:has(td:contains('John')) button:contains('Remove')";
        js.executeScript("$(\"" + jQuerySelector + "\").click();");
    }
}
