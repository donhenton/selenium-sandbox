/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.generic;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.dhenton9000.selenium.drivers.DriverFactory;
import com.dhenton9000.selenium.drivers.DriverFactory.DRIVER_ENV;
import com.dhenton9000.selenium.wicket.WicketBy;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
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
 * base class for tests that which to have the driver set via the remote.server
 * property of the pom.xml file
 *
 * @author dhenton
 */
public class BaseTest {

    private final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    private DriverFactory driverFactory = new DriverFactory();

    protected GenericAutomationRepository getAutomationRepository() {
        WebDriver d = null;
        try {
            d = getDriver();
        } catch (IOException ex) {
            throw new RuntimeException("io problem in repository creation "
                    +ex.getMessage());
        }
        return new GenericAutomationRepository(d, getConfiguration());
    }

    protected   WebDriver getDriver() throws IOException {
        //TODO: use the remote.server property here to configure
        //the drivers going to driver factory
        DRIVER_ENV env = null;
        String envString = System.getProperty("remote.server");
        if (envString == null)
        {
            env = DRIVER_ENV.local;
        }
        else
        {
            env = DRIVER_ENV.valueOf(envString);
        }

        return getDriver(env);

    }

    protected WebDriver getDriver(DRIVER_ENV env) throws IOException {
        WebDriver driver = null;
        switch (env) {
            case phantomjs:
                driver = driverFactory.configurePhantomJsDriver(getConfiguration());
                break;
            case local:
                driver
                        = driverFactory.configureDriver(
                                DriverFactory.DRIVER_TYPES.FireFox, null);
                driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
                driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
                break;
            default:

        }

        return driver;
    }

    protected Configuration getConfiguration() {
        Configuration config = null;
        logger.debug("using properties file");
        try {
            config = new PropertiesConfiguration("env.properties");
            logger.debug("reading config in " + this.getClass().getName());
        } catch (ConfigurationException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return config;
    }

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
