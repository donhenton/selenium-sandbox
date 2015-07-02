package com.dhenton9000.screenshots;

import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
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
        try {
            for (ScreenShot shot : screenshots) {
                shot.getNavigator().navigate(getGenericRepository());
                takeDirectScreenshot(targetFolder + shot.getFileDescription() + ScreenShot.IMAGE_EXTENSION);

            }
        } catch (WebDriverException err) {
            String info = String.format("Error '%s' message '%s'",
                    err.getClass().getName(),err.getMessage());
            throw new RuntimeException(info);
            
        } finally {

            this.getDriver().close();
            this.getDriver().quit();
        }
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
     * actually create the screenshot for a given environment. Prior to calling
     * this, you will have to position the browser at the location you want the
     * shot taken
     *
     * @param fullPathToPngImg full path to a PNG image
     * @param imageEnvironment string that represents env eg, QA, DEV3
     */
    public void takeDirectScreenshotAndWriteEnv(String fullPathToPngImg, String imageEnvironment) {
        FileInputStream stream = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String d = sdf.format(new Date());
        try {

            LOG.info("creating screen shot at '" + fullPathToPngImg + "'");
            // File pngFile = new File(fullPathToPngImg);
            File scrFile = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);
            stream = FileUtils.openInputStream(scrFile);
            BufferedImage image = ImageIO.read(stream);
            Graphics2D graphics = image.createGraphics();
            graphics.setColor(new Color(250, 0, 0, 255));
            graphics.setFont(new Font("Courier", Font.BOLD, 15));
            graphics.drawString(imageEnvironment, 10, 30);

            ImageIO.write(image, "PNG", new File(fullPathToPngImg + ScreenShot.IMAGE_EXTENSION));
            stream.close();

        } catch (WebDriverException e) {
            LOG.error("webdriver problem " + e.getClass().getName() + " " + e.getMessage());

        } catch (IOException e) {
            LOG.error("io problem " + e.getClass().getName() + " " + e.getMessage());
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {

                }
            }
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
