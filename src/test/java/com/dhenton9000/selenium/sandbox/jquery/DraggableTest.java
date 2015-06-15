/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.jquery;

import com.dhenton9000.selenium.generic.BaseTest;
import java.io.IOException;
import org.openqa.selenium.interactions.Action;
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

// http://selenium.polteq.com/en/perform-a-sequence-of-actions-with-selenium-webdriver

/**
 *
 * @author dhenton
 */
public class DraggableTest extends BaseTest {

    private static WebDriver driver;
    
    public DraggableTest() throws IOException
    {
         driver = getDriver();
    }

    @BeforeClass
    public static void setUp() {
      
    }

    @AfterClass
    public static void tearDown() {
        driver.close();
        driver.quit();
    }

    @Test
    public void draggable() {
       // http://jqueryui.com/resources/demos/draggable/default.html
        driver.get("http://jqueryui.com/resources/demos/draggable/default.html");
        WebElement draggable = driver.findElement(By.id("draggable"));
        new Actions(driver).dragAndDropBy(draggable, 120, 120).build().perform();
    }

    @Test
    public void droppable() {
        driver.get("http://jqueryui.com/resources/demos/droppable/default.html");
        WebElement draggable = driver.findElement(By.id("draggable"));
        WebElement droppable = driver.findElement(By.id("droppable"));
        new Actions(driver).dragAndDrop(draggable, droppable).build().perform();
    }

    @Test
    public void selectMultiple() throws InterruptedException {
        driver.get("http://jqueryui.com/resources/demos/selectable/default.html");
        List<WebElement> listItems = driver.findElements(By.cssSelector("ol#selectable *"));
        Actions builder = new Actions(driver);
        builder.click(listItems.get(1)).click(listItems.get(2)).click();
        Action selectMultiple = builder.build();
        selectMultiple.perform();
    }

    @Test
    public void sliding() {
        driver.get("http://jqueryui.com/resources/demos/slider/default.html");
        WebElement draggable = driver.findElement(By.className("ui-slider-handle"));
        new Actions(driver).dragAndDropBy(draggable, 120, 0).build().perform();
    }
}
