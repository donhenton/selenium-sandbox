/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.screenshots;

import com.dhenton9000.screenshots.compare.CompareResult;
import com.dhenton9000.screenshots.compare.SingleImageTestComparator;
import com.dhenton9000.screenshots.single.AbstractSingleScreenShot;

import com.dhenton9000.selenium.generic.BaseTest;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class ScreenshotTestBase extends BaseTest  {
    private Logger LOG = LoggerFactory.getLogger(ScreenshotTestBase.class);
    
     public  void peformSingleScreenshot(AbstractSingleScreenShot sample) throws IOException {
        SingleImageTestComparator comparator
                = new SingleImageTestComparator(this.getAutomationRepository());
        CompareResult compResult = comparator.runComparisonTest(sample);
        if (compResult.isInError() && compResult.getFailedAreas().size() > 0) {
            String info
                    = "\ncomparison fail for "
                    + sample.getSimpleImageName() + "\n" + "Comparison image is at\n"
                    + sample.fullPathToWriteComparisonResultsFile() + "\n"
                    + "areaCount " + compResult.getFailedAreas().size() + "\n";

            LOG.info(info);

            try {
                comparator.writeToJsonFile(sample, getTestName());
            } catch (IOException err) {
                LOG.error("unable to write json file for test comparison "
                        + sample.classPathToGoldFile() + "\n" + err.getMessage());
            }

        }
    }
}
