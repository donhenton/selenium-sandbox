/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.wicketapp;

import com.dhenton9000.selenium.wicket.WicketBy;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
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
public class OnHoverTest extends BaseSeleniumWicketTest {

    private final Logger logger = LoggerFactory.getLogger(OnHoverTest.class);
    public static final String WEB_APP_HOME_PAGE = "http://localhost:9090/wicket-sandbox";
    public static final String ONHOVER_PAGE = WEB_APP_HOME_PAGE + "/OnHoverPage";
    public static final String STUFF = "stuff";
    private static WebDriver driver;
    // in onhover.js
    public static final String TOOLTIP_DIV_ID = "hoverMessageDivBlock";

    @BeforeClass
    public static void beforeClass() {

        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

    }

    @Before
    public void before() {
        driver.get(ONHOVER_PAGE);
    }

    @AfterClass
    public static void after() {
        if (driver != null)
        {
            driver.close();
            driver.quit();
        }
    }

    @Test
    public void testThatPageHasNoHoverWhenTextBoxIsEmpty() {
        WebElement hoverTextBox =
                driver.findElement(WicketBy.wicketPath("hoverForm_hoverText"));
        assertTrue(isNotOnPageViaId(TOOLTIP_DIV_ID, driver));
        String hoverText = hoverTextBox.getAttribute("value");
        assertTrue(StringUtils.isEmpty(hoverText));
    
    }
    
    @Test
    public void testThatHoverHasSubmittedTextUsingFirefoxDriver() {
        performHoverTest();
         

    }
    //htmlunit driver does not work!!!!
//    @Test
//    public void testThatHoverHasSubmittedTextUsingHtmlDriver() {
//        driver.close();
//        driver.quit();
//        driver = new HtmlUnitDriver(true);
//        performHoverTest();
//        
//         
//
//    }

    private void performHoverTest() {
        WebElement hoverTextBox =
                driver.findElement(WicketBy.wicketPath("hoverForm_hoverText"));
       // assertTrue(isNotOnPageViaWicketPath(TOOLTIP_DIV_ID, driver));
       // String hoverText = hoverTextBox.getAttribute("value");
       // assertTrue(StringUtils.isEmpty(hoverText));

        WebElement hoverForm = driver.findElement(WicketBy.wicketPath("hoverForm"));
        hoverTextBox.sendKeys(STUFF);
        hoverForm.submit();
        
        new WebDriverWait(driver, 10)
               .until(ExpectedConditions.presenceOfElementLocated(
                WicketBy.wicketPath("hoverLabel")));
                
        
        String expectedLabelText = "Rollover this to see the onhover text '" + STUFF + "'";
        WebElement hoverLabel =
                driver.findElement(WicketBy.wicketPath("hoverLabel"));
        assertTrue(isNotOnPageViaId(TOOLTIP_DIV_ID, driver));
        this.mouseOverElement(hoverLabel, driver);
        assertFalse(isNotOnPageViaId(TOOLTIP_DIV_ID, driver));
        String labelText = hoverLabel.getText();
        assertEquals(expectedLabelText, labelText);
        WebElement hoverDiv =
                 driver.findElement(By.id(TOOLTIP_DIV_ID));
        String divText = hoverDiv.getText();
        assertTrue(divText.indexOf(STUFF)> -1);
    }
}
