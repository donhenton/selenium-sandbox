/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.screenshots;

import com.dhenton9000.screenshots.single.DemoScreenShot;
import java.io.IOException;
import org.testng.annotations.Test;

/**
 *
 * @author dhenton
 */
public class ScreenShotComparisonTest extends ScreenshotTestBase {

    @Test
    public void testSampleScreenshot() throws IOException {
        System.setProperty("test.env", "alpha");
        DemoScreenShot sample
                = new DemoScreenShot(this.getAutomationRepository());

        peformSingleScreenshot(sample);

    }

}
