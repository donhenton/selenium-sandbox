/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.wicketapp;

import com.dhenton9000.selenium.wicket.WicketBy;
import java.util.Set;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 *
 * @author dhenton
 */
public class ModalDialogTest extends BaseSeleniumWicketTest {

    public static final String WEB_APP_HOME_PAGE = "http://localhost:9090/wicket-sandbox";
    public static final String MODAL_DIALOG_PAGE = WEB_APP_HOME_PAGE + "/ModalInputPage";
    public static final String MODAL_DIALOG_TITLE = "Enter Your Data";
    private final Logger logger = LoggerFactory.getLogger(WicketAppTest.class);
    private WebDriver driver;

    @BeforeClass
    public static void beforeClass() {
      //  System.setProperty("webdriver.chrome.driver", "/home/dhenton/selenium/driver/chromedriver");
    }

    @Before
    public void before() {

        driver = new HtmlUnitDriver();
        driver.get(MODAL_DIALOG_PAGE);


    }

    @Test
    public void testModalDialogIsNotPresentAtStart() {
        assertFalse(isFramePresent("bozo", driver));
        assertFalse(isFramePresent(MODAL_DIALOG_TITLE, driver));

    }

    @Test
    public void testModalDialogIsPresent() throws InterruptedException {
        // String oldWindowHandle = driver.getWindowHandle();
        // logger.debug("old "+oldWindowHandle);
       assertTrue(isNotOnPageViaWicketPath("chooserWindow_content",driver));
        
        WebElement chooserLink =
                driver.findElement(WicketBy.wicketPath("form_chooserLink"));
        chooserLink.click();
        //Thread.sleep(3000);
        //Set<String> ss = driver.getWindowHandles();
        //for (String s: ss)
        //{

        //    logger.debug(s);
        //}
        //  assertTrue(isFramePresent(MODAL_DIALOG_TITLE, driver));
        WebElement chooserPopup =
                driver.findElement(WicketBy.wicketPath("chooserWindow_content"));
        assertTrue(chooserPopup.isDisplayed());


    }
}
