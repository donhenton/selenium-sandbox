/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.springmvc.app;

import com.dhenton9000.selenium.sandbox.wicketapp.BaseSeleniumWicketTest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This test can be run with a javascript enabled HtmlUnit Driver.
 *
 * @author dhenton
 */
public class SpringMVCUsingFireFoxTest extends BaseSeleniumWicketTest {

    //  public static final String SPRING_MVC_HOME_PAGE = "http://localhost:8080/SpringMVCJSON/";
    public static final String SPRING_MVC_HOME_PAGE = "http://donhenton.appspot.com";
    private static JavascriptExecutor js;
    private final static Logger logger = LoggerFactory.getLogger(SpringMVCUsingFireFoxTest.class);
    private static WebDriver driver;

    @BeforeClass
    public static void beforeClass() {

        driver = new FirefoxDriver();
        //  true enables javascript
        //  driver = new HtmlUnitDriver(true);
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException ex) {
//           logger.error("thread sleep problem");
//        }
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        driver.manage().window().maximize();
        //  driver.get(SPRING_MVC_HOME_PAGE);
        js = (JavascriptExecutor) driver;

        // load jQuery dynamically should it not be on the page you are
        // testing
        // this allows selenium to exploit jquery;
//        URL jqueryUrl = Resources.getResource("jquery.min.js");
//        String jqueryText = null;
//        try {
//            jqueryText = Resources.toString(jqueryUrl, Charsets.UTF_8);
//        } catch (IOException ex) {
//            logger.error("problems finding jquery");
//            throw new RuntimeException("problems finding jquery " + ex.getMessage());
//        }
        // js.executeScript(jqueryText);
    }

    @Before
    public void before() {
    }

    @AfterClass
    public static void after() {
        driver.close();
        driver.quit();
    }

     
    private void getFormSamplesPage() {
        driver.get(SPRING_MVC_HOME_PAGE);
        WebElement jQueryDemoLink = driver.findElement(By.partialLinkText("JQuery Demos"));
        jQueryDemoLink.click();

        By formsMenuLocator = By.partialLinkText("Forms");

        new WebDriverWait(driver, 3)
                .until(ExpectedConditions.presenceOfElementLocated(
                                formsMenuLocator));

        WebElement formsLink = driver.findElement(formsMenuLocator);
        this.mouseOverElement(formsLink, driver);

        By formSamplesLocator = By.partialLinkText("rm Samp");
        new WebDriverWait(driver, 3)
                .until(ExpectedConditions.presenceOfElementLocated(
                                formSamplesLocator));
        
        WebElement formSamplesDemoLink = driver.findElement(formSamplesLocator);
        this.mouseOverElement(formSamplesDemoLink, driver);
        
        formSamplesDemoLink.click();
        
    }

    @Test
    public void testMarkingMaryOnceGreysOutTheOtherMarys() {

        getFormSamplesPage();
        String jQuerySelector = "'tr:contains(\"Union\")  td input:eq(1)'";

        WebElement maryUnionRadio = (WebElement) js.executeScript("return $(" + jQuerySelector + ").get(0);");
        assertEquals("team2_1", maryUnionRadio.getAttribute("id"));
        maryUnionRadio.click();
        List<WebElement> markedItems = driver.findElements(By.cssSelector("label.text-error"));
        assertEquals(3, markedItems.size());
        for (WebElement w : markedItems) {
            assertEquals("Mary", w.getText());
        }

    }

}
