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
import com.dhenton9000.selenium.wicket.WicketBy;
import com.dhenton9000.filedownloader.FileDownloader;
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
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.html5.LocalStorage;

public class GenericAutomationRepository {

    protected WebDriver driver;
    private static final Logger LOG = LoggerFactory.getLogger(GenericAutomationRepository.class);
    private Configuration config;
    private WaitMethods waitMethods;
    private JavascriptExecutor js;
    private LocalStorage localStorage;
    private JSMethods jsMethods;
    private String tempDownloadPath;

    /**
     * construct the repository from a config file, which will specify the
     * driver
     *
     * @param config
     */
    public GenericAutomationRepository(Configuration config) {
        driver = null;
        this.config = config;
        configureDriver();
        this.waitMethods = new WaitMethods(driver);
        this.js = (JavascriptExecutor) driver;
        this.jsMethods = new JSMethods(this);

    }

    /**
     * construct the repository with a given configuration and web driver
     *
     * @param driver
     * @param config
     */
    public GenericAutomationRepository(WebDriver driver, Configuration config) {
        this.driver = driver;
        this.config = config;
        this.waitMethods = new WaitMethods(driver);
        this.js = (JavascriptExecutor) driver;

    }

    public WaitMethods getWaitMethods() {
        return this.waitMethods;
    }

    /**
     * given a jquery Snippet, return a list of web elements this is awfully
     * similar to css selector
     *
     * @param jQuerySelector
     * @return
     */
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

    /**
     * @return the jsMethods
     */
    public JSMethods getJsMethods() {
        return jsMethods;
    }

    /**
     * this is the folder where requested downloads will appear. currently for
     * csv and xcel files
     *
     * @return the tempDownloadPath, it has a trailing slash
     */
    public String getTempDownloadPath() {

        if (tempDownloadPath == null) {
            tempDownloadPath = FileUtils.getTempDirectoryPath();
        }
        return tempDownloadPath;
    }

    /**
     * an enumeration of selection types, eg. select by css selector use
     * 'cssSelector'
     */
    public enum SELECTOR_CHOICE {

        id, name, className, linkText,
        xpath, tagName, cssSelector,
        partialLinkText, wicketPathMatch, wicketPathContains
    }

    /**
     * driver types currently only firefox supported
     */
    public enum DRIVER_TYPES {

        FireFox, InternetExplorer, Opera, Safari, Chrome;
    }

    /**
     * set up the driver with configuration parameters
     *
     */
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
            throw new RuntimeException("must specify 'test.selenium.browser' in prop file");
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

                 // sets the driver to automatically skip download dialog
                // and save csv,xcel files to a temp directory
                // that directory is set in the constructor and has a trailing
                // slash
                FirefoxProfile firefoxProfile = new FirefoxProfile();
                firefoxProfile.setPreference("browser.download.folderList", 2);
                firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);

                String target = this.getTempDownloadPath();
                firefoxProfile.setPreference("browser.download.dir", target);
                firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv,application/vnd.ms-excel");

                desiredCapabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);

                LOG.debug("creating firefox driver");
                driver = new FirefoxDriver(desiredCapabilities);
                LOG.debug("got firefox driver");
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
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        LOG.debug("driver is loaded via config " + driver.toString());

    }

    public WebDriver getDriver() {
        return this.driver;
    }

    public JavascriptExecutor getJavascriptExecutor() {
        if (js == null) {
            js = (JavascriptExecutor) driver;
        }
        return js;
    }

    public LocalStorage getLocalStorage() {
        if (localStorage == null) {
            localStorage = (LocalStorage) driver;
        }
        return localStorage;
    }

    /**
     * set the browser size to specific values pass in null if you don't want to
     * use that portion of the code
     *
     * @param pos
     * @param size
     */
    public void setBrowserSize(Point pos, Dimension size) {
        if (pos != null) {
            driver.manage().window().setPosition(pos);
        }
        if (size != null) {
            driver.manage().window().setSize(size);
        }
    }

    public void maximizeWindow() {
        driver.manage().window().maximize();
    }

    /**
     * generate the By selector
     *
     * @param selectorChoice the desired selector, eg by css Selector
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

    /**
     * dump the driver logs
     */
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

    /**
     * Hover over the selected item
     *
     * @param selectorChoice selection type, eg cssSelector
     * @param selectorValue the value to select by eg. '.myStyleClass'
     */
    public void hoverOn(SELECTOR_CHOICE selectorChoice, String selectorValue) {

        Actions builder = new Actions(driver);
        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        builder.moveToElement(driver.findElement(selectionBy)).build().perform();

    }

    /**
     * Hover over the given WebElement
     *
     * @param e
     */
    public void hoverOn(WebElement e) {
        Actions builder = new Actions(driver);
        builder.moveToElement(e).build().perform();

    }

    /**
     * Enter text into a text box
     *
     * @param selectorChoice selection type, eg cssSelector
     * @param selectorValue the value to select by eg. '.myStyleClass'
     * @param textToSend
     */
    public void enterText(SELECTOR_CHOICE selectorChoice, String selectorValue, String textToSend) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        driver.findElement(selectionBy).clear();
        driver.findElement(selectionBy).sendKeys(textToSend);

    }

    /**
     * Enter text into a text box
     *
     * @param selectorChoice selection type, eg cssSelector
     * @param selectorValue the value to select by eg. '.myStyleClass'
     * @param number the number to enter
     */
    public void enterText(SELECTOR_CHOICE selectorChoice, String selectorValue, int number) {

        String numberAsText = Integer.toString(number);
        enterText(selectorChoice, selectorValue, numberAsText);

    }

    public void scrollElementIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

    }

    public void scrollElementIntoView(String cssSelector) {
        String exScript = "$('" + cssSelector + "')[0].scrollIntoView(true)";
        ((JavascriptExecutor) driver).executeScript(exScript);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {

        }
    }

    /**
     *
     * @param selectorChoice selection type, eg cssSelector
     * @param selectorValue the value to select by eg. '.myStyleClass'
     * @return the elements found
     */
    public List<WebElement> getListOfOptionsForDropdown(SELECTOR_CHOICE selectorChoice, String selectorValue) {
        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        Select dropdown = new Select(driver.findElement(selectionBy));
        return dropdown.getOptions();
    }

    /**
     * Is the option with the given text found in the dropbox?
     *
     * @param selectorChoice selection type, eg cssSelector
     * @param selectorValue the value to select by eg. '.myStyleClass'
     * @param textToFind
     * @return
     */
    public boolean isOptionTextPresentForDropdown(SELECTOR_CHOICE selectorChoice, String selectorValue, String textToFind) {
        List<WebElement> elems = getListOfOptionsForDropdown(selectorChoice, selectorValue);
        boolean foundIt = false;
        if (elems != null & !elems.isEmpty()) {
            for (WebElement e : elems) {
                if (e.getText().equals(textToFind)) {
                    foundIt = true;
                    break;
                }
            }
        }
        return foundIt;
    }

    /**
     * return the text of the first selected option for a dropdown or null if
     * not found
     *
     * @param selectorChoice selection type, eg cssSelector
     * @param selectorValue the value to select by eg. '.myStyleClass'
     * @return first selected option or null
     */
    public String getSelectedOptionForDropdown(SELECTOR_CHOICE selectorChoice, String selectorValue) {
        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        Select dropdown = new Select(driver.findElement(selectionBy));
        if (dropdown.getFirstSelectedOption() == null) {
            return null;
        }
        return dropdown.getFirstSelectedOption().getText();
    }

    /**
     * Select a dropdown item by text
     *
     * @param selectorChoice selection type, eg cssSelector
     * @param selectorValue the value to select by eg. '.myStyleClass'
     * @param optionText
     */
    public void selectDropdownOptionByText(SELECTOR_CHOICE selectorChoice, String selectorValue, String optionText) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        Select dropdown = new Select(driver.findElement(selectionBy));
        dropdown.selectByVisibleText(optionText);

    }

    /**
     * Select a dropdown item by item value
     *
     * @param selectorChoice selection type, eg cssSelector
     * @param selectorValue the value to select by eg. '.myStyleClass'
     * @param value
     */
    public void selectDropdownOptionByValue(SELECTOR_CHOICE selectorChoice, String selectorValue, String value) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        Select dropdown = new Select(driver.findElement(selectionBy));
        dropdown.selectByValue(value);

    }

    /**
     * select a dropdown item by index
     *
     * @param selectorChoice selection type, eg cssSelector
     * @param selectorValue the value to select by eg. '.myStyleClass'
     * @param index
     */
    public void selectDropdownOptionByIndex(SELECTOR_CHOICE selectorChoice, String selectorValue, int index) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        Select dropdown = new Select(driver.findElement(selectionBy));
        dropdown.selectByIndex(index);

    }

    /**
     * get the text of a web element
     *
     * @param selectorChoice selection type, eg cssSelector
     * @param selectorValue the value to select by eg. '.myStyleClass'
     * @return
     */
    public String getText(SELECTOR_CHOICE selectorChoice, String selectorValue) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        return driver.findElement(selectionBy).getText();

    }

    /**
     * get the attribute of a web element
     *
     * @param selectorChoice selection type, eg cssSelector
     * @param selectorValue the value to select by eg. '.myStyleClass'
     * @param attribute the desired attribute
     * @return
     */
    public String getAttribute(SELECTOR_CHOICE selectorChoice, String selectorValue, String attribute) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        return driver.findElement(selectionBy).getAttribute(attribute);

    }

    /**
     * check if an element is visible
     *
     * @param selectorChoice selection type, eg cssSelector
     * @param selectorValue the value to select by eg. '.myStyleClass'
     * @return true if visible
     */
    public boolean verifyElementIsVisible(SELECTOR_CHOICE selectorChoice, String selectorValue) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        return driver.findElement(selectionBy).isDisplayed();
    }

    /**
     * check if an element is enabled
     *
     * @param selectorChoice selection type, eg cssSelector
     * @param selectorValue the value to select by eg. '.myStyleClass'
     * @return true if visible
     */
    public boolean verifyElementIsEnabled(SELECTOR_CHOICE selectorChoice, String selectorValue) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        return driver.findElement(selectionBy).isEnabled();
    }

    /**
     * find all elements that match the selector
     *
     * @param selectorChoice selection type, eg cssSelector
     * @param selectorValue the value to select by eg. '.myStyleClass'
     * @return a list of all elements
     */
    public List<WebElement> findElements(SELECTOR_CHOICE selectorChoice, String selectorValue) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        return driver.findElements(selectionBy);
    }

    /**
     * find the first element that matchs the selector
     *
     * @param selectorChoice selection type, eg cssSelector
     * @param selectorValue the value to select by eg. '.myStyleClass'
     * @return
     */
    public WebElement findElement(SELECTOR_CHOICE selectorChoice, String selectorValue) {

        By selectionBy = generateSelectorBy(selectorChoice, selectorValue);
        return driver.findElement(selectionBy);

    }

    /**
     * close and clean up the driver
     */
    public void quitDriver() {
        driver.close();
        driver.quit();
    }

    /**
     * navigate to a page url
     *
     * @param url
     */
    public void navigateToWebPage(String url) {
        driver.get(url);

    }

    /**
     * get the page title of web page
     *
     * @return
     */
    public String getPageTitle() {
        String pageTitle = driver.getTitle();
        return pageTitle;

    }
    
    
    /**
     * Determines visibility of an element in a window.
     * @param w
     * @return true if the element is in the window, false if not
     */
    public boolean isElementScrolledIntoView(WebElement w) {

        if (w == null) {
            return false;
        }
        Point p = w.getLocation();
        Dimension d =  getDriver().manage().window().getSize();
        java.awt.Rectangle r = new java.awt.Rectangle(d.getWidth(), d.getHeight());
        java.awt.Point awtPoint = new java.awt.Point(p.x, p.y);
        return r.contains(awtPoint);
    }

    /**
     * return a file downloader instance which is not thread safe
     *
     * @return
     */
    public FileDownloader getNewFileDownloader() {
        return new FileDownloader(this.getDriver());
    }

}
