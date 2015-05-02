/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.mouse.actions;

import com.dhenton9000.embedded.jetty.JettyServer;
import com.dhenton9000.selenium.js.JavascriptLibrary;
import com.lazerycode.selenium.tools.RobotPowered;
import java.awt.AWTException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * This test explore using mouse actions to interact with Hicharts javascript
 * charts
 *
 *
 * @author dhenton
 */
public class HighChartTests {

    private static final int PORT = 4444;
    private static final String CONTEXT_PATH = "/app";
    private static final Logger LOG = LoggerFactory.getLogger(HighChartTests.class);
    private static JettyServer localWebServer;
    private static final String APP_URL = "http://localhost:"
            + PORT + CONTEXT_PATH;

    private static WebDriver driver;

    @BeforeClass
    public static void start() throws Exception {
        localWebServer = new JettyServer(PORT, CONTEXT_PATH, null);
        FirefoxProfile profile = new FirefoxProfile();
        //this may be needed for actions to work
        profile.setEnableNativeEvents(true);
        driver = new FirefoxDriver(profile);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    
    /**
     * This test demonstrates various mouse movement strategies.
     * 
     * <ul>
     * <li>AWT Robot</li>
     * <li>Builder actions (most recent)</li>
     * <li>Injected javascript</li>
     * 
     * </ul>
     * 
     * @throws IOException
     * @throws AWTException 
     */
    @Test
    public void testHighChartsWithMouseMovements() throws IOException, AWTException {

        driver.get(APP_URL + "/highcharts/highcharts.html");
        RobotPowered r = null;

        r = new RobotPowered(driver);
        r.robotPoweredMoveMouseToAbsoluteCoordinates(1200, 0);
        this.waitForDuration(2);
        //g.highcharts-tracker rect:nth-child(2)
        List<WebElement> bars
                = driver.findElements(By.cssSelector("g.highcharts-tracker rect:nth-child(2)"));
        WebElement elem = bars.get(1);
        r.robotPoweredMoveMouseToWebElementCoordinates(elem);
        this.waitForDuration(2);

        //this sends events simulating a mouse over
        Actions builder = new Actions(driver);
        builder.moveToElement(elem, 40, 40).build().perform();
        this.waitForDuration(2);
        //this injects and is for browsers that don't support native events
        JavascriptLibrary javascript = new JavascriptLibrary();
        //int barNumber = getXAxisLabelsText().indexOf(xAxisLabel);
        // WebElement pointToHoverOver = chart.findElements(By.cssSelector("g.highcharts-tracker > g:nth-of-type(" + series + ") > rect")).get(barNumber);

        bars
                = driver.findElements(By.cssSelector("g.highcharts-tracker rect:nth-child(1)"));
        //For browsers not supporting native events also illustrates
        //large javascript injection

        javascript.callEmbeddedSelenium(driver, "triggerEvent", bars.get(0), "mouseover");
        
    }

    public void waitForDuration(int timeInSeconds) {

        try {
            Thread.sleep(timeInSeconds * 1000);
        } catch (InterruptedException e) {

        }
    }

    @AfterClass
    public static void stop() throws Exception {
        // driver.close();
        // driver.quit();
        localWebServer.stopJettyServer();
    }

}
