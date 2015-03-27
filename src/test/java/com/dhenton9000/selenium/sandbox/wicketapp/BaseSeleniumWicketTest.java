/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.wicketapp;

import com.dhenton9000.selenium.wicket.WicketBy;
import java.io.File;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class BaseSeleniumWicketTest {

    private final Logger logger = LoggerFactory.getLogger(BaseSeleniumWicketTest.class);

    public void mouseOverElement(WebElement element, WebDriver driver) {

        Locatable hoverItem = (Locatable) element;
        Mouse mouse = ((HasInputDevices) driver).getMouse();
        mouse.mouseMove(hoverItem.getCoordinates());

    }

     public static String createPathToTestResources(String htmlFilename) {
        char sc = File.separatorChar;
        String currentDir = System.getProperty("user.dir");
        String resourcesPath = currentDir + sc + "src" + sc + "test" + sc + "resources";
        String htmlPath = resourcesPath + sc + htmlFilename;
        return htmlPath;
    }
    
    protected boolean isAlertPresent(WebDriver driver) {
        boolean isAlertPresent = true;
        String oldWindow = driver.getWindowHandle();
        try {
            driver.switchTo().alert();
        } catch (NoAlertPresentException err) {
            return false;
        }
        driver.switchTo().window(oldWindow);
        return isAlertPresent;
    }

    protected boolean isNotOnPageViaLinkText(String linkText, WebDriver driver) {
        boolean isNotOnPage = false;
        try {
            driver.findElement(By.linkText(linkText));
        } catch (NoSuchElementException err) {
            isNotOnPage = true;
        }
        return isNotOnPage;
    }

    protected boolean isNotOnPageViaId(String Id, WebDriver driver) {
        boolean isNotOnPage = false;
        try {
            driver.findElement(By.id(Id));
        } catch (NoSuchElementException err) {
            isNotOnPage = true;
        }
        return isNotOnPage;
    }

    protected boolean isNotOnPageViaWicketPath(String wicketPath, WebDriver driver) {
        boolean isNotOnPage = false;
        try {
            driver.findElement(WicketBy.wicketPath(wicketPath));
        } catch (NoSuchElementException err) {
            isNotOnPage = true;
        }
        return isNotOnPage;
    }

    /**
     * This only tests if the item exists, and returns the state of the driver.
     *
     * @param frameTitle
     * @param driver
     * @return true if frame is present
     */
    protected boolean isFramePresent(String frameTitle, WebDriver driver) {
        boolean framePresent = true;
        WebDriver oldDriver = driver;
        try {
            driver.switchTo().frame("ModelFrameTitle");
            driver = oldDriver;
        } catch (NoSuchFrameException err) {
            framePresent = false;
        } catch (Exception err) {
            logger.error("err " + err.getClass().getName());
        }
        return framePresent;

    }
}
