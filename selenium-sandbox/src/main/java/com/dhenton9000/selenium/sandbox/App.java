package com.dhenton9000.selenium.sandbox;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 * http://www.codeproject.com/Articles/143430/Test-your-web-application-s-UI-with-JUnit-and-Sele
 * http://wiki.openqa.org/display/SEL/Home
 * http://docs.seleniumhq.org/docs/02_selenium_ide.jsp
 * https://github.com/thucydides-webtests/thucydides/wiki
 * http://thomassundberg.wordpress.com/2011/10/18/testing-a-web-application-with-selenium-2/
 * http://mestachs.wordpress.com/2012/08/13/selenium-best-practices/
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
