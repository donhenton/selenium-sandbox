/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.screenshots.compare;

import com.dhenton9000.screenshots.ConfigurationManager;
import com.dhenton9000.screenshots.ScreenShot;
import java.io.IOException;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class ImageControl {

   // private final Configuration config;

   
    private static final Logger LOG = LoggerFactory.getLogger(ImageControl.class);
    private final ConfigurationManager configurationManager;

    public ImageControl(ConfigurationManager configurationManager) {
         this.configurationManager = configurationManager;
    }

    public void compareImages() throws IOException {

        List<ScreenShot> screenShots = configurationManager.getScreenShotList();
        for (ScreenShot shot : screenShots) {

            CompareResult compareResult
                    = (new ImageComparator(getConfigurationManager())).
                    compareImagePairs(shot.getFileDescription());

            if (compareResult.isInError()) {
                LOG.info("Issue with '" + compareResult.getSimpleFileName()
                        + "' issue: " + compareResult.getErrorMessage());
            } else {
                LOG.info("no differences found for '" 
                        + compareResult.getSimpleFileName() + "'");

            }
        }

    }

    /**
     * @return the configurationManager
     */
    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

}
