/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.generic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static com.dhenton9000.selenium.generic.GenericAutomationRepository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class WaitMethods {

    private WebDriver driver;
    private Logger LOG = LoggerFactory.getLogger(WaitMethods.class);
    

    public WaitMethods(WebDriver driver) {
        this.driver = driver;
    }
    public void waitForElement(int timeInSeconds) {

        driver.manage().timeouts().implicitlyWait(timeInSeconds, java.util.concurrent.TimeUnit.SECONDS);
    }

    public void waitForPage(int timeInSeconds) {

        driver.manage().timeouts().pageLoadTimeout(timeInSeconds, java.util.concurrent.TimeUnit.SECONDS);
    }

    public void waitForDuration(int timeInSeconds) {

        try {
            Thread.sleep(timeInSeconds * 1000);
        } catch (InterruptedException e) {
            
        }
    }

    public void waitForExpectedElement(int timeInSeconds, SELECTOR_CHOICE selectorChoice, String selectorValue) {

        WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        wait.until(ExpectedConditions.visibilityOfElementLocated(selectionBy));

    }

    public void waitForExpectedPage(int timeInSeconds, String pageTitle) {

        WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
        wait.until(ExpectedConditions.titleContains(pageTitle));

    }

    public void waitForElementToBeClickable(int timeInSeconds, SELECTOR_CHOICE selectorChoice, String selectorValue) {

        WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        wait.until(ExpectedConditions.elementToBeClickable(selectionBy));

    }

}
