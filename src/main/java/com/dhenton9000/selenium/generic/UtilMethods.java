/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.selenium.generic;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class UtilMethods {
     private static final Logger LOG = LoggerFactory.getLogger(UtilMethods.class);
    
    
    public UtilMethods()
    {
       
    }
    
    
    public static String generateRandomName(String rootName) {
        Random r = new Random();
        String randomStr = Long.toString(Math.abs(r.nextLong()), 21);
        String randomName = rootName + "_" + randomStr.substring(0, 4);
        return randomName;
    }

   

    public static String getPreviousDate(int daysAgo) {
        DateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy");
        newFormat.setLenient(true);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date());
        cal.add(Calendar.DATE, -1 * daysAgo);
        return newFormat.format(cal.getTime());
    }

    
    /**
     * include full file name/path
     * @param fileName 
     * @param driver 
     */
    public static void takeScreenshot(String fileName,WebDriver driver) {
        try {
            String date = Long.toString(new Date().getTime());
            File pngFile = new File(  fileName + date
                    + ".png");
            File scrFile = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, pngFile);
        } catch (WebDriverException e) {
            LOG.error("webdriver problem with screenshot: " +" "+e.getMessage());
        } catch (IOException e) {
            LOG.error("io problem with screenshot: " +" "+e.getMessage());
         }
    }
    
 
//     public static boolean isAlertPresent(WebDriver driver) {
//        boolean isAlertPresent = true;
//        String oldWindow = driver.getWindowHandle(); // this throws an error if dialog already present?
//        try {
//            driver.switchTo().alert();
//        } catch (NoAlertPresentException err) {
//            return false;
//        }
//        driver.switchTo().window(oldWindow);
//        return isAlertPresent;
//    }
    
    /**
     * handle an alert if no alert present error caught and reported
     * @param driver
     * @param doAccept 
     */
    public static void handleAlert(WebDriver driver,boolean doAccept) {

        try {
            Alert a = driver.switchTo().alert();
            if (doAccept)
                a.accept();
            else
                a.dismiss();
        }
        catch(Exception err)
        {
            LOG.warn("handle alert error "+
            err.getClass().getName()+" "+err.getMessage());
            
        }
        

    }
    
    
    
}
