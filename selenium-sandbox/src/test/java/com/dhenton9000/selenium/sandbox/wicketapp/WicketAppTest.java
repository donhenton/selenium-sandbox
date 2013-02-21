/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.wicketapp;

import com.dhenton9000.selenium.sandbox.AppTest;
import com.dhenton9000.selenium.sandbox.google.AnnotatedGoogleSearchPage;
import com.dhenton9000.selenium.wicket.WicketBy;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class WicketAppTest {

    public static final String WEB_APP_HOME_PAGE = "http://localhost:9090/wicket-sandbox";
    private final Logger logger = LoggerFactory.getLogger(WicketAppTest.class);
    private WebDriver driver;

    @BeforeClass
    public static void beforeClass() {
    }

    @Before
    public void before() {

        driver = new HtmlUnitDriver();
        driver.get(WEB_APP_HOME_PAGE);


    }
    //
     @Test
    public void testWebSite() {
        // WebElement links = driver.findElement(WicketBy.wicketPath("navPanel_maintainRestaurantsTwoLink"));
        
        // WebElement pickList = pickPanel_pickRestaurantForm
         
       //  logger.debug(links.getText());
    }
}
