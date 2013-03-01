/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.wicketapp;

import com.dhenton9000.selenium.wicket.WicketBy;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class WicketAppTest extends BaseSeleniumWicketTest {

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
        //start in the initial state

        checkPageIsInInitialState();
        WebElement addButton =
                driver.findElement(WicketBy.wicketPath("addPanel_addForm_addButton"));
        //move to add state
        //editor Panel still present
        //add button gone
        //
        addButton.click();
        assertFalse(isNotOnPageViaWicketPath("editorPanel", driver));
        assertTrue(isNotOnPageViaWicketPath("addPanel_addForm_addButton", driver));
        assertFalse(isNotOnPageViaWicketPath("editorPanel_restaurantForm_cancelButton", driver));
        WebElement cancelButton =
                driver.findElement(WicketBy.wicketPath("editorPanel_restaurantForm_cancelButton"));
        cancelButton.click();
        //this should be in add state
        checkPageIsInInitialState();

    }

    @Test
    public void testEditMode() {
        final String selectedRestaurantText = "Buffas Delicatessen";
        final String newName = "fred";
        WebElement selectedRestaurant =
                driver.findElement(By.linkText(selectedRestaurantText));
        assertEquals(selectedRestaurantText, selectedRestaurant.getText());
        selectedRestaurant.click();
        // now in edit mode
        WebElement editFormNameField =
                driver.findElement(WicketBy.wicketPath("editorPanel_restaurantForm_name"));

        assertEquals(selectedRestaurantText, editFormNameField.getAttribute("value"));
        editFormNameField.clear();
        editFormNameField.sendKeys(newName);
        WebElement saveButton =
                driver.findElement(WicketBy.wicketPath("editorPanel_restaurantForm_submitButton"));
        saveButton.click();
        assertTrue(isNotOnPageViaLinkText(selectedRestaurantText, driver));

        selectedRestaurant =
                driver.findElement(By.linkText(newName));
        selectedRestaurant.click();
        editFormNameField =
                driver.findElement(WicketBy.wicketPath("editorPanel_restaurantForm_name"));
        editFormNameField.clear();
        editFormNameField.sendKeys(selectedRestaurantText);
        saveButton =
                driver.findElement(WicketBy.wicketPath("editorPanel_restaurantForm_submitButton"));
        saveButton.click();
        checkPageIsInInitialState();
    }

    public void testModalDialog()
    {
        
        
        
    }

    private void checkPageIsInInitialState() {
        // initial state
        // add button yes
        // edit panel no
        // pick panel yes

        WebElement addPanel = driver.findElement(WicketBy.wicketPath("addPanel"));
        assertTrue(addPanel.isDisplayed());
        WebElement pickPanel = driver.findElement(WicketBy.wicketPath("pickPanel"));
        assertTrue(pickPanel.isDisplayed());
        assertTrue(isNotOnPageViaWicketPath("editorPanel", driver));

    }
}
