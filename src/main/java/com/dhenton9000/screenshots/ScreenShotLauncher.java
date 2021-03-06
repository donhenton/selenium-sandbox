/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.screenshots;

import com.dhenton9000.screenshots.compare.ImageControl;
import com.dhenton9000.selenium.drivers.DriverFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration.ConfigurationException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main launcher program runs from the command line. Specify the action
 *
 * <ul>
 * <li>--action=source create the source images (before or reference)
 * <li>--action=target create the target or images (after)
 * <li>--action=compare compare source and target
 *
 * </ul>
 *
 *
 * @author dhenton http://commons.apache.org/proper/commons-cli/usage.html
 * http://www.vineetmanohar.com/2009/11/3-ways-to-run-java-main-from-maven/
 */
public class ScreenShotLauncher {

    private static final Logger LOG = LoggerFactory.getLogger(ScreenShotLauncher.class);
    private ConfigurationManager configurationManager = new ConfigurationManager();
    private DriverFactory driverFactory = new DriverFactory();

    public enum ACTIONS {

        source, target, compare
    };

    /**
     * main launching method that takes command line arguments
     *
     * @param args
     */
    public static void main(String[] args) {

        final String[] actions = {
            ACTIONS.source.toString(),
            ACTIONS.target.toString(),
            ACTIONS.compare.toString()};
        final List actionArray = Arrays.asList(actions);

        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("action")
                .hasArg()
                .isRequired()
                .withArgName("action")
                .create());
        HelpFormatter formatter = new HelpFormatter();

        String header = "Process screenshots\n"
                + "--action=source   create source screenshots\n"
                + "--action=target     create the screenshots for comparison\n"
                + "--action=compare  compare the images\n"
                + "%s\n\n";

        String action = null;
        try {
            // parse the command line arguments
            CommandLineParser parser = new PosixParser();
            CommandLine line = parser.parse(options, args);

            // validate that action option has been set
            if (line.hasOption("action")) {
                action = line.getOptionValue("action");
                LOG.debug("action '" + action + "'");
                if (!actionArray.contains(action)) {

                    formatter.printHelp(ScreenShotLauncher.class.getName(),
                            String.format(header,
                                    String.format("action option '%s' is invalid", action)),
                            options, "\n\n", true);
                    System.exit(1);

                }

            } else {
                formatter.printHelp(ScreenShotLauncher.class.getName(),
                        String.format(header, "not found"),
                        options, "\n\n", false);
                System.exit(1);
            }
        } catch (ParseException exp) {
            formatter.printHelp(ScreenShotLauncher.class.getName(),
                    String.format(header, "problem " + exp.getMessage()),
                    options, "\n\n", false);
            System.exit(1);

        }
        ACTIONS actionEnum = ACTIONS.valueOf(action);
        ScreenShotLauncher launcher
                = new ScreenShotLauncher();
        try {
            launcher.handleRequest(actionEnum);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);

        }
    }

    /**
     * process the command line request
     *
     * @param actionEnum
     * @throws ConfigurationException
     */
    private void handleRequest(ACTIONS actionEnum) throws ConfigurationException, IOException {

        LOG.debug("in handle request with action '" + actionEnum.toString() + "'");
        WebDriver driver = null;
        ScreenshotRepository creator = null;
        try {
            switch (actionEnum) {

                case source:
                    driver = driverFactory.getDriver();
                    creator = new ScreenshotRepository(configurationManager, driver);
                    creator.createSourceImages();
                    break;
                case target:
                    driver = driverFactory.getDriver();
                    creator = new ScreenshotRepository(configurationManager, driver);
                    creator.createTargetImages();
                    break;
                case compare:
                    ImageControl imageControl
                            = new ImageControl(configurationManager);
                    imageControl.compareImages();
                    break;

                default:
                    break;

            };
        } finally {
            if (driver != null) {
                try {
                    driver.close();
                    driver.quit();
                } catch (SessionNotFoundException ss) {
                }
            }
        }
    }

}
