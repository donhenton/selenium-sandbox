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
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class SignInTest extends BaseSeleniumWicketTest {

    private final Logger logger = LoggerFactory.getLogger(SignInTest.class);
    public static final String WEB_APP_HOME_PAGE = "http://localhost:9090/wicket-sandbox";
    public static final String SIGN_IN_LINK_TEXT = "sign in";
    public static final String SIGN_OUT_LINK_TEXT = "sign out";
    private static WebDriver driver;

    @BeforeClass
    public static void beforeClass() {

        driver = new HtmlUnitDriver();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
     //   driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

    }

    @Before
    public void before() {
        driver.get(WEB_APP_HOME_PAGE);
    }

    @AfterClass
    public static void after() {
          driver.close();
          driver.quit();
    }

    @Test
    public void testLogin() {
        WebElement signInLink = driver.findElement(By.linkText(SIGN_IN_LINK_TEXT));
        signInLink.click();

        WebElement signInForm = driver.findElement(WicketBy.wicketPath("signInForm"));
        WebElement userNameText = driver.findElement(WicketBy.wicketPath("signInForm_username"));
        WebElement passWordText = driver.findElement(WicketBy.wicketPath("signInForm_password"));
        userNameText.clear();
        passWordText.clear();
        userNameText.sendKeys("jadmin");
        passWordText.sendKeys("admin");
        signInForm.submit();

        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.presenceOfElementLocated(
                By.linkText(SIGN_OUT_LINK_TEXT)));

        // check that the fullname shows
        WebElement fullNameLabel = driver.findElement(WicketBy.wicketPath("userPanel_fullname"));
        assertEquals("John Admin", fullNameLabel.getText());
        assertTrue(isNotOnPageViaWicketPath(SIGN_IN_LINK_TEXT, driver));
        WebElement signOutLink = driver.findElement(By.linkText(SIGN_OUT_LINK_TEXT));
        signOutLink.click();
         new WebDriverWait(driver, 1000)
                .until(ExpectedConditions.presenceOfElementLocated(
                By.linkText(SIGN_IN_LINK_TEXT)));
        assertEquals("SignOutPage",driver.getTitle());

    }
}
