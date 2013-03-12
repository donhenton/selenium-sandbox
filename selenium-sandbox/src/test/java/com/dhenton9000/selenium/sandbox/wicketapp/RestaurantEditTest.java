/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.wicketapp;

import com.dhenton9000.selenium.wicket.WicketBy;
import java.util.Set;
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
public class RestaurantEditTest extends BaseSeleniumWicketTest {

    public static final String WEB_APP_HOME_PAGE = "http://localhost:9090/wicket-sandbox";
    public static final String RESTAURANT_PAGE = WEB_APP_HOME_PAGE + "/MaintainRestaurantsTwo";
    private final Logger logger = LoggerFactory.getLogger(RestaurantEditTest.class);
    private static WebDriver driver;
    public static final String ADD_RESTAURANT_SAMPLE = "Alices Restaurant";

    @BeforeClass
    public static void beforeClass() {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void after() {
        driver.close();
        driver.quit();
    }

    @Before
    public void before() {


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

    @Test
    public void testAddANewRetaurantThenDeleteIt() {

        // add the test restaurant

        WebElement addButton =
                driver.findElement(WicketBy.wicketPath("addPanel_addForm_addButton"));
        addButton.click();
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.presenceOfElementLocated(
                WicketBy.wicketPath("editorPanel_restaurantForm_name")));
        WebElement editFormNameField =
                driver.findElement(WicketBy.wicketPath("editorPanel_restaurantForm_name"));
        editFormNameField.clear();
        editFormNameField.sendKeys(ADD_RESTAURANT_SAMPLE);
        WebElement editFormVersionField =                
                driver.findElement(WicketBy.wicketPath("editorPanel_restaurantForm_versionString"));
        editFormVersionField.clear();
        editFormVersionField.sendKeys("22");
        WebElement editFormSubmitButton =
                driver.findElement(WicketBy.wicketPath("editorPanel_restaurantForm_submitButton"));
        editFormSubmitButton.click();
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.invisibilityOfElementLocated(
                WicketBy.wicketPath("editorPanel_restaurantForm_name")));
        
        
        assertFalse(isNotOnPageViaLinkText(ADD_RESTAURANT_SAMPLE,driver));

        WebElement selectedRestaurant =
                driver.findElement(By.linkText(ADD_RESTAURANT_SAMPLE));
        assertEquals(ADD_RESTAURANT_SAMPLE, selectedRestaurant.getText());
        selectedRestaurant.click();
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.presenceOfElementLocated(
                WicketBy.wicketPath("editorPanel_restaurantForm_deleteButton")));        

        WebElement deleteButton =
                driver.findElement(WicketBy.wicketPath("editorPanel_restaurantForm_deleteButton"));
        deleteButton.click();
        
        new WebDriverWait(driver, 15)
               .until(ExpectedConditions.alertIsPresent());
        
        
        driver.switchTo().alert().accept();
        
        
         new WebDriverWait(driver, 10)
                .until(ExpectedConditions.invisibilityOfElementLocated(
                WicketBy.wicketPath("editorPanel_restaurantForm_name")));

         assertTrue(isNotOnPageViaLinkText(ADD_RESTAURANT_SAMPLE,driver));
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
