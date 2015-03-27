/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.wicketapp;

import com.dhenton9000.selenium.wicket.WicketBy;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class LightBoxTest extends BaseSeleniumWicketTest {
    
    
    public static final String WEB_APP_HOME_PAGE = "http://localhost:9090/wicket-sandbox";
    public static final String LIGHTBOX_PAGE = WEB_APP_HOME_PAGE + "/LightboxPage";
    private final Logger logger = LoggerFactory.getLogger(LightBoxTest.class);
    private static WebDriver driver;

    @BeforeClass
    public static void beforeClass() {
         
        driver = new FirefoxDriver();
        //will wait 2 seconds for a search for an element
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.get(LIGHTBOX_PAGE);
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
    public void testThatDisplayBoxIsNotVisibleInitially()
    {
        WebElement pixDisplay = 
        driver.findElement(By.id("pixDisplay"));
        assertFalse(pixDisplay.isDisplayed());
    }
    
    
    @Test
    public void testThatDisplayBoxIsVisibleWhenAnImageIsSelected()
    {
        WebElement pixDisplay = 
        driver.findElement(By.id("pixDisplay"));
        assertFalse(pixDisplay.isDisplayed());
        WebElement image4 =
                driver.findElement(WicketBy.wicketPath("a4__link_a4__thumb"));
        image4.click();
        pixDisplay = null;
        pixDisplay = 
        driver.findElement(By.id("pixDisplay"));
        WebElement closeButton = pixDisplay.findElement(By.linkText("× Close"));
        assertTrue(pixDisplay.isDisplayed());
        closeButton.click();
        
         new WebDriverWait(driver, 10)
                 .until(ExpectedConditions.invisibilityOfElementLocated(
                WicketBy.wicketPath("pixDisplay")));
    
        pixDisplay = null;
        pixDisplay = 
        driver.findElement(By.id("pixDisplay"));
        assertFalse(pixDisplay.isDisplayed());
        
        
    }
    
}
