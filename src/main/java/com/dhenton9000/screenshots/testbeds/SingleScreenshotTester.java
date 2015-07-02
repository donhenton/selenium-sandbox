/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.screenshots.testbeds;

import com.dhenton9000.screenshots.ScreenShot;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import com.dhenton9000.screenshots.AbstractSingleScreenShot;
import com.dhenton9000.selenium.drivers.DriverFactory;
import com.dhenton9000.selenium.generic.AppspotRepository;
import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import org.apache.commons.configuration.Configuration;
import org.openqa.selenium.WebDriver;

/**
 * Used to take a single screenshot
 *
 * @author dhenton
 */
public class SingleScreenshotTester {

    private final static Logger LOG
            = LoggerFactory.getLogger(SingleScreenshotTester.class);
    private DriverFactory driverFactory = new DriverFactory();

    public static void main(String[] args) {

        try {
            (new SingleScreenshotTester()).doTestBedNavigation();
        } catch (Exception ex) {
            LOG.error("error in main " + ex.getMessage(),ex);
        }

    }

    public void doTestBedNavigation() throws IOException {

        GenericAutomationRepository g;
        AppspotRepository app;
        Configuration config = DriverFactory.getConfiguration();
        WebDriver driver = driverFactory.getDriver();
        g = new GenericAutomationRepository(driver, config);
        app = new AppspotRepository(g);
        g = app.getAutomation();

        AppComparisonScreenShot sample = new AppComparisonScreenShot(g, app);

        sample.takeScreenShot("screenshots/comp/"
                + AppComparisonScreenShot.SIMPLE_IMAGE_NAME, "qa");

    }

    public void doComparison() throws IOException {

        File goldFile
                = FileUtils.toFile(getClass()
                        .getResource("/gold_files/appsample/"
                                + AppComparisonScreenShot.SIMPLE_IMAGE_NAME
                                + ScreenShot.IMAGE_EXTENSION));

        File compareFile = new File("screenshots/comp/"
                + AppComparisonScreenShot.SIMPLE_IMAGE_NAME
                + ScreenShot.IMAGE_EXTENSION);

        String destFileString = "screenshots/comp/comp.png";
        AbstractSingleScreenShot.performComparison(goldFile, compareFile, destFileString);
    }

}
