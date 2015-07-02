/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.testng.listeners;

 
import com.dhenton9000.selenium.generic.BaseTest;
import com.dhenton9000.selenium.generic.UtilMethods;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * adapted from
 * https://github.com/yev/seleniumMvnScreenshot/blob/master/src/main/java/com/github/yev/FailTestScreenshotListener.java.
 *
 *
 * On test failure this listener will take a screenshot and place it in the
 * build directory of the Jenkins server.
 *
 *
 * @author dhenton
 */
public class FailedTestScreenShotListener extends TestListenerAdapter {

    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        BaseTest currentTest = (BaseTest) tr.getInstance();
         
        String imageName = tr.getTestName() + "_"
                + tr.getMethod().getMethodName() + "_fail_"+ 
                getParameterDescription(tr);

        String failedScreenshotsFolderLocation = "target/failed_test_images/";
        File f = new File(failedScreenshotsFolderLocation);
        if (!f.exists()) {
            try {
                FileUtils.forceMkdir(f);
            } catch (IOException ex) {
                throw new RuntimeException("cannot create " + f.getAbsolutePath());
            }
        }

        UtilMethods.takeScreenshot(failedScreenshotsFolderLocation + imageName,
                currentTest.getAutomationRepository().getDriver());

    }

    private String getParameterDescription(ITestResult tr) {
        String desc = "";
        Object[] parms = tr.getParameters();
        if (parms != null && parms.length > 0)
        {
            for (Object p: parms)
            {
                String t = p.toString().replaceAll("\\s+", "")+"_";
                desc = desc + t;
            }
        }
        
        
        return desc;
    }
    
     

}
