package com.dhenton9000.selenium.sandbox;

import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for simple App.
 */
public class AppTest {

    public static final String WEB_APP_HOME_PAGE = "http://localhost:9090/wicket-sandbox";
    private final Logger logger = LoggerFactory.getLogger(AppTest.class);
    private WebDriver driver;
    
    
    @BeforeClass
    public static void beforeClass() {
    }

    @Before
    public void before() {
        
        driver = new HtmlUnitDriver();

	
	driver.get(WEB_APP_HOME_PAGE);
        
        
    }

    @Test
    public void testWebSite() {
        List<WebElement> links = driver.findElements(By.tagName("a"));
        for(WebElement w: links)
        {
            logger.debug("== "+w.getText());
        }
    }
}
