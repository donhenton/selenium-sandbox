/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.screenshots.single;

import com.dhenton9000.screenshots.ConfigurationManager;
import com.dhenton9000.screenshots.ScreenShot;
import com.dhenton9000.screenshots.ScreenshotRepository;
import com.dhenton9000.screenshots.compare.CompareResult;
import com.dhenton9000.screenshots.compare.ImageComparator;
import com.dhenton9000.selenium.generic.AppspotRepository;
import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.dhenton9000.screenshots.compare.SingleImageTestComparator.ROOT_FOLDER_LOCATION;
/**
 * This class will be used for actual junit tests comparing a current screenshot
 * to a stored image generated at a prior time.
 *
 * It will contain the code to position the screen, take the shot, and clean up.
 * Additionally, it will perform the comparison and return the collection of
 * region differences, and also write out the comparison image based on the gold
 * file.
 *
 *
 * @author dhenton
 */
public abstract class AbstractSingleScreenShot {

    private ConfigurationManager configurationManager = new ConfigurationManager();
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSingleScreenShot.class);

    private final ScreenshotRepository screenshotRepository;
    private final GenericAutomationRepository genericAutomationRepository;
    private final AppspotRepository appspotRepository;

    public abstract void setUpScreenshot();

    public abstract void cleanUp();

    public AbstractSingleScreenShot(GenericAutomationRepository g) {
        this.genericAutomationRepository = g;
        this.appspotRepository = new AppspotRepository(g);
        screenshotRepository = new ScreenshotRepository(
                configurationManager,
                genericAutomationRepository.getDriver());
    }


    /**
     * this is the routine to call for taking the image and saving it.
     *
     * @param fullPathToImage does NOT include the extension
     * @param envString
     */
    public void takeScreenShot(String fullPathToImage, String envString) {

        LOG.info(String.format("Beginning screenshot for '%s' '%s'",
                fullPathToImage, envString));

        checkOrCreateDestFolder(fullPathToImage);

        setUpScreenshot();
        this.getScreenshotRepository().
                takeDirectScreenshotAndWriteEnv(
                        fullPathToImage, envString);

        cleanUp();
    }

    public static CompareResult performComparison(File fullPathToGoldFile,
            File fullPathToCompFile, String fullPathToWriteComparison) throws IOException {

        CompareResult results = null;
        InputStream goldStream = null;
        InputStream compareStream = null;

        ImageComparator comparator
                = new ImageComparator(new ConfigurationManager());

        try {

            goldStream
                    = FileUtils.openInputStream(fullPathToGoldFile);
            compareStream
                    = FileUtils.openInputStream(fullPathToCompFile);
            results = comparator.compareImages(goldStream, compareStream);
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (goldStream != null) {
                try {
                    goldStream.close();
                    goldStream = null;
                } catch (IOException ex) {

                }
            }

            if (compareStream != null) {
                try {
                    compareStream.close();
                } catch (IOException ex) {

                }
            }
        }

        new File((new File(fullPathToWriteComparison)).getParent()).mkdirs();
        goldStream
                = FileUtils.openInputStream(fullPathToGoldFile);
        LOG.debug("writing compare file '" + fullPathToWriteComparison + "'");

        if (results != null && results.getFailedAreas().size() > 0) {
            LOG.info("found screenshot comparison differences for  '"
                    + fullPathToWriteComparison + "'");

            results.setErrorMessage("errors found '" + fullPathToWriteComparison + "'");
            results.setInError(true);

            comparator.markImageAreas(goldStream, results.getFailedAreas(), fullPathToWriteComparison);
            goldStream.close();
        } else {
            LOG.info("nothing found for screenshot comparison '"
                    + fullPathToWriteComparison + "'");
        }

        return results;

    }

    private void checkOrCreateDestFolder(String fullPathToImage) {

        File f = new File(fullPathToImage).getParentFile();
        if (!f.exists()) {
            try {
                FileUtils.forceMkdir(f.getAbsoluteFile());
            } catch (IOException ex) {
                throw new RuntimeException("problem with image creation " + ex.getMessage());
            }
        }
    }

    /**
     * @return the configurationManager
     */
    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    /**
     * @return the screenshotRepository
     */
    public ScreenshotRepository getScreenshotRepository() {
        return screenshotRepository;
    }

    /**
     * @return the genericAutomationRepository
     */
    public GenericAutomationRepository getGenericAutomationRepository() {
        return genericAutomationRepository;
    }

    /**
     * @return the appspotRepository
     */
    public AppspotRepository getAppspotRepository() {
        return appspotRepository;
    }

        public abstract String getSimpleImageName();

//    @Override
//    public void cleanUp() {
//        .logout();
//        this.getGenericAutomationRepository().getDriver().close();
//    }

    
    public String fullPathToWriteComparisonResultsFile() {
        return ROOT_FOLDER_LOCATION+"images/comp_images/"
                + getSimpleImageName() + "_comparison"+ScreenShot.IMAGE_EXTENSION;
    }

     
    public String classPathToGoldFile() {
        return "/gold_files/appsample/" + getSimpleImageName()
                + ScreenShot.IMAGE_EXTENSION;
    }

    
    public String fullPathToNewImageFilePath() {
        return  ROOT_FOLDER_LOCATION+"images/newImages/"
                + getSimpleImageName() + ScreenShot.IMAGE_EXTENSION;
    }
    
    
}
