/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.springmvc.app;

import com.dhenton9000.selenium.sandbox.jquery.JQueryTest;
import com.dhenton9000.selenium.sandbox.wicketapp.BaseSeleniumWicketTest;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 *
 * @author dhenton
 */
public class SpringMVCUsingFireFoxTest extends BaseSeleniumWicketTest {

  //  public static final String SPRING_MVC_HOME_PAGE = "http://localhost:8080/SpringMVCJSON/";
    public static final String SPRING_MVC_HOME_PAGE = "http://donhenton.appspot.com";
    private static JavascriptExecutor js;
    private final static Logger logger = LoggerFactory.getLogger(SpringMVCUsingFireFoxTest.class);
    private static WebDriver driver;

    @BeforeClass
    public static void beforeClass() {

        driver = new FirefoxDriver();
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.get(SPRING_MVC_HOME_PAGE);
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
    public void testThatHomePageLoads() {
        WebElement jQueryDemoLink = driver.findElement(By.linkText("JQuery Demos"));
        jQueryDemoLink.click();

        WebElement formDemoLink = driver.findElement(By.linkText("Form Samples"));
        formDemoLink.click();

        String jQuerySelector = "'tr:contains(\"Union\")  td input:eq(1)'";

        WebElement maryUnionRadio = (WebElement) js.executeScript("return $(" + jQuerySelector + ").get(0);");
        assertEquals("team2_1", maryUnionRadio.getAttribute("id"));
        maryUnionRadio.click();

        String jQuerySelectorLabel = "'label.greyOut:last'";
        WebElement lastMary = (WebElement) js.executeScript("return $(" + jQuerySelectorLabel + ").get(0);");
        assertEquals("Mary", lastMary.getText());


    }
}
