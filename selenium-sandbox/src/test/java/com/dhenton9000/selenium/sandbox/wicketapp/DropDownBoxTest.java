/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.sandbox.wicketapp;

import com.dhenton9000.selenium.wicket.WicketBy;
import java.util.List;
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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class DropDownBoxTest {

    public static final String WEB_APP_HOME_PAGE = "http://localhost:9090/wicket-sandbox";
    public static final String DROPBOX_PAGE = WEB_APP_HOME_PAGE + "/ApplicationsUsers";
    private final Logger logger = LoggerFactory.getLogger(RestaurantEditTest.class);
    private static WebDriver driver;
    private static final String DROPBOX_PATH = "applicationsSelect";

    @BeforeClass
    public static void beforeClass() {

        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.get(DROPBOX_PAGE);
    }

    @Before
    public void before() {
    }

    @AfterClass
    public static void after() {

         driver.close();
         driver.quit();

    }

    @Test
    public void testDropBox() {
        WebElement dropDownListBox =
                driver.findElement(WicketBy.wicketPath(DROPBOX_PATH));
        
        Select clickThis = new Select(dropDownListBox);
        //clickThis.selectByVisibleText("Hero Color");
       // clickThis.selectByValue("55");
        
        logger.debug(""+clickThis.getOptions().size());
        logger.debug(clickThis.getOptions().get(15).getText());
        List<WebElement> companyItems = clickThis.getOptions();
        for (WebElement w:companyItems)
        {
           // logger.debug(w.getText()+" "+w.getAttribute("value"));
            if (w.getText().equals("HeroColor"))
            {
                w.click();
                break;
            }
        }
        
        //this is here to give the call to the db to take place
        //rows to the table need to have some time to show
        new WebDriverWait(driver, 10)
               .until(ExpectedConditions.presenceOfElementLocated(
                WicketBy.wicketPath("userTable")));
                
        
        
        WebElement dataTable = 
                driver.findElement(WicketBy.wicketPath("userTable"));
        List<WebElement> rows = dataTable.findElements(By.tagName("tr"));
        assertEquals(6,rows.size());
       


    }
}
