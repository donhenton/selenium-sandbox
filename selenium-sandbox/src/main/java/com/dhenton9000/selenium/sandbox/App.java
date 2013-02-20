package com.dhenton9000.selenium.sandbox;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
      System.setProperty("webdriver.chrome.driver", "/home/dhenton/selenium/driver/chromedriver");
      WebDriver d = new ChromeDriver();
      d.get("http://cnn.com");
      String title = d.getTitle();
      logger.debug(title);
      d.close();
      d.quit();
    }
}
