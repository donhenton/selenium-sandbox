/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.generic;

/**
 *
 * @author dhenton
 */
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.configuration.Configuration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericAutomationRepository {

    protected WebDriver driver;
    private static final Logger LOG = LoggerFactory.getLogger(GenericAutomationRepository.class);
    private Configuration config;
    private WaitMethods waitMethods;
    private JavascriptExecutor js;

    public GenericAutomationRepository(Configuration config) {
        driver = null;
        this.config = config;
        configureDriver();
        this.waitMethods = new WaitMethods(driver);

    }

    public GenericAutomationRepository(WebDriver driver, Configuration config) {
        this.driver = driver;
        this.config = config;
        this.waitMethods = new WaitMethods(driver);
        this.js = (JavascriptExecutor) driver;
    }

    public WaitMethods getWaitMethods() {
        return this.waitMethods;
    }

    public List<WebElement> getElementsByJQuery(String jQuerySelector) {

        List<WebElement> elements = null;
        try {
            elements = (List<WebElement>) js.executeScript("return $(\"" + jQuerySelector + "\");");
        } catch (Exception err) {
            LOG.error("Error " + err.getClass().getName() + " " + err.getMessage()
                    + " in getElementsByJQuery '" + jQuerySelector + "'");
        }
        if (elements == null) {
            elements = new ArrayList<WebElement>();
        }
        return elements;
    }

    public enum SELECTOR_CHOICE {
        id, name, className, linkText,
        xpath, tagName, cssSelector,
        partialLinkText, wicketPathMatch, wicketPathContains
    }

    public enum DRIVER_TYPES {
        FireFox, InternetExplorer, Opera, Safari, Chrome;
    }

    private void configureDriver() {

        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.SEVERE);
        logs.enable(LogType.CLIENT, Level.SEVERE);
        logs.enable(LogType.DRIVER, Level.SEVERE);
        logs.enable(LogType.PERFORMANCE, Level.SEVERE);
        logs.enable(LogType.PROFILER, Level.SEVERE);
        logs.enable(LogType.SERVER, Level.SEVERE);

        String driverTypeString = this.config.getString("test.selenium.browser");
        if (driverTypeString == null) {
            throw new RuntimeException("must specify 'test.selenium.browser' in env.properties");
        }

        DRIVER_TYPES driverType = DRIVER_TYPES.valueOf(driverTypeString);
        LOG.debug(" found driver type " + driverType.toString());
        if (driverType == null) {
            throw new RuntimeException("cannot find driver type of " + driverTypeString);
        }

        switch (driverType) {
            case FireFox:
            default:
                 DesiredCapabilities desiredCapabilities = DesiredCapabilities.firefox();
                 desiredCapabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);
                 driver = new FirefoxDriver(desiredCapabilities);
                break;
            case InternetExplorer:
                break;
            case Opera:
                break;
            case Safari:
                break;
            case Chrome:
                break;
            

        }
        LOG.debug("driver is loaded via config " + driver.toString());

    }

    public WebDriver getDriver() {
        return this.driver;
    }

    public JavascriptExecutor getJavascriptExecutor() {
        if (js == null)
            js = (JavascriptExecutor) driver;
        return js;
    }

    /**
     * generate the By selector
     *
     * @param selectorChoice the desired selector
     * @param selectorValue the string to search for
     * @return the By element
     */
    public static By generateSelectorBy(SELECTOR_CHOICE selectorChoice, String selectorValue) {
        By selectorBy = null;

        switch (selectorChoice) {
            case id:
                selectorBy = By.id(selectorValue);
                break;
            case name:
                selectorBy = By.name(selectorValue);
                break;
            case className:
                selectorBy = By.className(selectorValue);
                break;
            case linkText:
                selectorBy = By.linkText(selectorValue);
                break;
            case xpath:
                selectorBy = By.xpath(selectorValue);
                break;
            case tagName:
                selectorBy = By.tagName(selectorValue);
                break;
            case cssSelector:
                selectorBy = By.cssSelector(selectorValue);
                break;
            case partialLinkText:
                selectorBy = By.partialLinkText(selectorValue);
                break;
            case wicketPathMatch:
                selectorBy = WicketBy.wicketPathMatch(selectorValue);
                break;
            case wicketPathContains:
                selectorBy = WicketBy.wicketPathContains(selectorValue);
                break;
            default:
                throw new AssertionError("unable to match selector " + selectorChoice.name());
        }

        return selectorBy;
    }

    /**
     * method for clicking
     *
     * @param selectorChoice
     * @param selectorValue
     */
    public void clickOn(SELECTOR_CHOICE selectorChoice, String selectorValue) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        driver.findElement(selectionBy).click();

    }

    public void dumpLogs() {
        Logs logs = driver.manage().logs();
        LogEntries logEntries = logs.get(LogType.DRIVER);
        LOG.debug("dumping click driver errors");
        for (LogEntry logEntry : logEntries) {
            LOG.debug(logEntry.getMessage());
        }
        logEntries = logs.get(LogType.BROWSER);
        LOG.debug("dumping click browser errors");
        for (LogEntry logEntry : logEntries) {
            LOG.debug(logEntry.getMessage());
        }
    }

    public void hoverOn(SELECTOR_CHOICE selectorChoice, String selectorValue) {

        Actions builder = new Actions(driver);
        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        builder.moveToElement(driver.findElement(selectionBy)).build().perform();

    }

    public void hoverOn(WebElement e) {
        Actions builder = new Actions(driver);
        builder.moveToElement(e).build().perform();
    }

    public void enterText(SELECTOR_CHOICE selectorChoice, String selectorValue, String textToSend) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        driver.findElement(selectionBy).clear();
        driver.findElement(selectionBy).sendKeys(textToSend);

    }

    public void enterText(SELECTOR_CHOICE selectorChoice, String selectorValue, int number) {

        String numberAsText = Integer.toString(number);
        enterText(selectorChoice, selectorValue, numberAsText);

    }
    
    public List<WebElement> getListOfOptionsForDropdown(SELECTOR_CHOICE selectorChoice, String selectorValue)
    {
        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        Select dropdown = new Select(driver.findElement(selectionBy));
        return dropdown.getOptions();
    }
    
    
    public boolean checkOptionTextPresentForDropdown(SELECTOR_CHOICE selectorChoice, String selectorValue,String textToFind)
    {
       List<WebElement> elems =  getListOfOptionsForDropdown(selectorChoice,  selectorValue);
       boolean foundIt = false;
       if (elems != null & !elems.isEmpty())
       {
           for (WebElement e: elems)
           {
               if (e.getText().equals(textToFind))
               {
                   foundIt = true;
                   break;
               }
           }
       }
       return foundIt;
    }
    

    public String getSelectedOptionForDropdown(SELECTOR_CHOICE selectorChoice, String selectorValue) {
        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        Select dropdown = new Select(driver.findElement(selectionBy));
        if (dropdown.getFirstSelectedOption() == null) {
            return null;
        }
        return dropdown.getFirstSelectedOption().getText();
    }

    public void selectDropdownOptionByText(SELECTOR_CHOICE selectorChoice, String selectorValue, String optionText) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        Select dropdown = new Select(driver.findElement(selectionBy));
        dropdown.selectByVisibleText(optionText);
        

    }

    public void selectDropdownOptionByValue(SELECTOR_CHOICE selectorChoice, String selectorValue, String optionText) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        Select dropdown = new Select(driver.findElement(selectionBy));
        dropdown.selectByValue(optionText);
        

    }
    
    public void selectDropdownOptionByIndex(SELECTOR_CHOICE selectorChoice, String selectorValue, int index) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        Select dropdown = new Select(driver.findElement(selectionBy));
        dropdown.selectByIndex(index);
        

    }
    
    public String getText(SELECTOR_CHOICE selectorChoice, String selectorValue) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        return driver.findElement(selectionBy).getText();

    }

    public String getAttribute(SELECTOR_CHOICE selectorChoice, String selectorValue, String attribute) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        return driver.findElement(selectionBy).getAttribute(attribute);

    }

    public boolean verifyElementIsVisible(SELECTOR_CHOICE selectorChoice, String selectorValue) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        return driver.findElement(selectionBy).isDisplayed();
    }

    public boolean verifyElementIsEnabled(SELECTOR_CHOICE selectorChoice, String selectorValue) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        return driver.findElement(selectionBy).isEnabled();
    }

    public List<WebElement> findElements(SELECTOR_CHOICE selectorChoice, String selectorValue) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        return driver.findElements(selectionBy);
    }

    public WebElement findElement(SELECTOR_CHOICE selectorChoice, String selectorValue) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        return driver.findElement(selectionBy);

    }

    public void quitDriver() {
        driver.close();
        driver.quit();
    }

    public void navigateToWebPage(String url) {
        driver.get(url);

    }

    public String getPageTitle() {
        String pageTitle = driver.getTitle();
        return pageTitle;

    }

}
