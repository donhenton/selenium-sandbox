/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.wicketapp;

import com.dhenton9000.selenium.wicket.WicketBy;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author dhenton
 */
public class ModalDialogTest extends BaseSeleniumWicketTest {

    public static final String WEB_APP_HOME_PAGE = "http://localhost:9090/wicket-sandbox";
    public static final String MODAL_DIALOG_PAGE = WEB_APP_HOME_PAGE + "/ModalInputPage";
    public static final String MODAL_DIALOG_TITLE = "Enter Your Data";
    public static final int NEW_AGE_VALUE = 768;
    private final Logger logger = LoggerFactory.getLogger(RestaurantEditTest.class);
    private static WebDriver driver;

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("webdriver.chrome.driver", "/home/dhenton/selenium/driver/chromedriver");
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.get(MODAL_DIALOG_PAGE);
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
    public void testModalDialogIsPresentAndNothingHappensIfYouDismissTheDialog()
            throws Exception {
        // String oldWindowHandle = driver.getWindowHandle();
        // logger.debug("old "+oldWindowHandle);

        WebElement ageField =
                driver.findElement(WicketBy.wicketPath("form_age"));
        assertEquals("25", ageField.getAttribute("value"));



        assertTrue(isNotOnPageViaWicketPath("chooserWindow_content", driver));

        WebElement chooserLink =
                driver.findElement(WicketBy.wicketPath("form_chooserLink"));
        chooserLink.click();

        
         new WebDriverWait(driver, 10)
               .until(ExpectedConditions.presenceOfElementLocated(
                WicketBy.wicketPath("chooserWindow_content")));
        
        
        WebElement chooserPopup =
                driver.findElement(WicketBy.wicketPath("chooserWindow_content"));
        assertTrue(chooserPopup.isDisplayed());
        // find the close button
        WebElement closeButton = driver.findElement(By.className("w_close"));
        closeButton.click();
        ageField = null;
        
         new WebDriverWait(driver, 10)
               .until(ExpectedConditions.presenceOfElementLocated(
                WicketBy.wicketPath("form_age")));
        ageField = null;
        ageField =
                driver.findElement(WicketBy.wicketPath("form_age"));
        assertEquals("25", ageField.getAttribute("value"));
        // now make a change in the modal 


    }

    @Test
    public void testModalDialogMakesAChange() {
        WebElement chooserLink =
                driver.findElement(WicketBy.wicketPath("form_chooserLink"));
        chooserLink.click();

         new WebDriverWait(driver, 10)
               .until(ExpectedConditions.presenceOfElementLocated(
                WicketBy.wicketPath("chooserWindow_content")));
           
        WebElement chooserPopup =
                driver.findElement(WicketBy.wicketPath("chooserWindow_content"));
        assertTrue(chooserPopup.isDisplayed());
        WebElement modalDialogAgeField =
                driver.findElement(WicketBy.wicketPath("chooserWindow_content_chooser_chooserForm_age"));
        modalDialogAgeField.clear();
        modalDialogAgeField.sendKeys("" + NEW_AGE_VALUE);

        WebElement modalDialogSubmit =
                driver.findElement(WicketBy.wicketPath("chooserWindow_content_chooser_chooserForm_button"));
        modalDialogSubmit.click();
        
         new WebDriverWait(driver, 10)
                 .until(ExpectedConditions.invisibilityOfElementLocated(
                WicketBy.wicketPath("chooserWindow_content")));
        
        
        WebElement ageField =
                driver.findElement(WicketBy.wicketPath("form_age"));
        assertEquals("" + NEW_AGE_VALUE, ageField.getAttribute("value"));
        

    }
}
