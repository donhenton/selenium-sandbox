package com.dhenton9000.screenshots;


import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles screen shot requests. To make additional screenshots, you
 * will need to do the following
 *
 * <ol>
 * <li>add to the switch in 
 * {@link com.dhenton9000.screenshots.ConfigurationManager.#addScreenShotNavigation }
 * <li>add to the PAGE_ACTIONS enumeration
 * {@link com.dhenton9000.screenshots.ConfigurationManager}
 * <li>create a navigator by extending
 * {@link com.dhenton9000.screenshots.navigator.ScreenShotNavigator}
 * <li>add descriptive information to the screenshots.xml file
 *
 * </ol>
 *
 * @author dhenton
 */
public class ScreenshotRepository {

    //private List<ScreenShot> screenShotList = null;
    private static final Logger LOG = LoggerFactory.getLogger(ScreenshotRepository.class);
    private WebDriver driver;
    private final GenericAutomationRepository genericRepository;
    private final ConfigurationManager configurationManager;

    public ScreenshotRepository(ConfigurationManager configManager, WebDriver driver) {
        this.configurationManager = configManager;
        this.driver = driver;
        genericRepository = new GenericAutomationRepository(driver, this.getConfigurationManager().getTestPropertiesConfig());
       
    }

     

    private void createImages(String targetFolder) {
        List<ScreenShot> screenshots = this.getConfigurationManager().getScreenShotList();
  
        for (ScreenShot shot : screenshots) {
            shot.getNavigator().navigate( getGenericRepository());
            takeDirectScreenshot(targetFolder + shot.getFileDescription() + ScreenShot.IMAGE_EXTENSION);

        }

        this.getDriver().close();
        this.getDriver().quit();
    }

    /**
     * create the source (before) images for the comparison
     */
    public void createSourceImages() {
        createImages(this.getConfigurationManager().getSourceFolder());

    }

    /**
     * create the target images (after) for the comparison
     */
    public void createTargetImages() {
        createImages(this.getConfigurationManager().getTargetFolder());
    }

   

    /**
     * compare the images in the src and target, it locates the images by a file
     * reference NOT a url
     */
    /**
     * compare the images in the src and target, it locates the images by a file
     * reference NOT a url
     *
     * @param fullPathToPngImg full path to a PNG image
     */
    public void takeDirectScreenshot(String fullPathToPngImg) {
        try {

            LOG.debug("creating screen shot at '" + fullPathToPngImg + "'");
            File pngFile = new File(fullPathToPngImg);
            File scrFile = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, pngFile);
        } catch (Exception e) {
            LOG.error("shot problem " + e.getClass().getName() + " " + e.getMessage());

        }
    }

    /**
     * @return the driver
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * @param driver the driver to set
     */
    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * @return the genericRepository
     */
    public GenericAutomationRepository getGenericRepository() {
        return genericRepository;
    }



    /**
     * @return the configurationManager
     */
    public final ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

   

   
    
}
