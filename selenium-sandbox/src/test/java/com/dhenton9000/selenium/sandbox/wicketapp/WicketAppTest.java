/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.wicketapp;

import com.dhenton9000.selenium.wicket.WicketBy;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.NoSuchElementException;
import static org.junit.Assert.*;

/**
 *
 * @author dhenton
 */
public class WicketAppTest {

    public static final String WEB_APP_HOME_PAGE = "http://localhost:9090/wicket-sandbox";
    public static final String RESTAURANT_PAGE = WEB_APP_HOME_PAGE + "/MaintainRestaurantsTwo";
    private final Logger logger = LoggerFactory.getLogger(WicketAppTest.class);
    private WebDriver driver;

    @BeforeClass
    public static void beforeClass() {
    }

    @Before
    public void before() {

        driver = new HtmlUnitDriver();
        driver.get(RESTAURANT_PAGE);


    }
    //

    @Test
    public void testRestaurantPage() {
        //initial state 
        // add button yes
        // edit panel no
        // pick panel yes

        WebElement addPanel = driver.findElement(WicketBy.wicketPath("addPanel"));
        assertTrue(addPanel.isDisplayed());
        WebElement pickPanel = driver.findElement(WicketBy.wicketPath("pickPanel"));
        assertTrue(pickPanel.isDisplayed());
        assertTrue(isNotOnPage("editorPanel", driver));
        WebElement addButton =
                addPanel.findElement(WicketBy.wicketPath("addPanel_addForm_addButton"));
        //move to add state
        //editor Panel still present
        //add button gone
        //
        addButton.click();
        assertFalse(isNotOnPage("editorPanel", driver));
        assertTrue(isNotOnPage("addPanel_addForm_addButton", driver));
        assertFalse(isNotOnPage("editorPanel_restaurantForm_cancelButton", driver));
        WebElement cancelButton = 
        driver.findElement(WicketBy.wicketPath("editorPanel_restaurantForm_cancelButton"));
        cancelButton.click();
        addPanel = driver.findElement(WicketBy.wicketPath("addPanel"));
        assertTrue(addPanel.isDisplayed());
        pickPanel = driver.findElement(WicketBy.wicketPath("pickPanel"));
        assertTrue(pickPanel.isDisplayed());
        assertTrue(isNotOnPage("editorPanel", driver));

    }

    private boolean isNotOnPage(String wicketPath, WebDriver driver) {
        boolean isNotOnPage = false;
        try {
            driver.findElement(WicketBy.wicketPath(wicketPath));
        } catch (NoSuchElementException err) {
            isNotOnPage = true;
        }


        return isNotOnPage;
    }
}
