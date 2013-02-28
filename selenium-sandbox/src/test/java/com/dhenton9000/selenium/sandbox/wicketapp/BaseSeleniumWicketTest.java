/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.wicketapp;

import com.dhenton9000.selenium.wicket.WicketBy;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class BaseSeleniumWicketTest {

    private final Logger logger = LoggerFactory.getLogger(BaseSeleniumWicketTest.class);

    protected boolean isNotOnPageViaLinkText(String linkText, WebDriver driver) {
        boolean isNotOnPage = false;
        try {
            driver.findElement(By.linkText(linkText));
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
