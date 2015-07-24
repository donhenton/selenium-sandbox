package com.dhenton9000.screenshots.compare;

 
 
import com.dhenton9000.screenshots.ConfigurationManager;
import com.dhenton9000.screenshots.single.AbstractSingleScreenShot;
import com.dhenton9000.selenium.drivers.DriverFactory;
import com.dhenton9000.selenium.drivers.DriverFactory.REMOTE_SERVER_VALUE;
import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * This class performs the comparison for image gold files.
 *
 *
 */
public class SingleImageTestComparator {

    private static final Logger LOG = LoggerFactory.getLogger(SingleImageTestComparator.class);
    private final GenericAutomationRepository genericAutomationRepository;
    public final static String ROOT_FOLDER_LOCATION = "target/classes/public_html/";
    private final static String JSON_FOLDER_LOCATION = ROOT_FOLDER_LOCATION + "json/";
    public static final String JSON_TEST_DATA_FIELD = "testData";

    public SingleImageTestComparator(GenericAutomationRepository genericAutomationRepository) {
        this.genericAutomationRepository = genericAutomationRepository;
    }

    /**
     * perform the comparison and return the results.
     *
     * @param fullPathToGoldFile
     * @param fullPathToCompFile
     * @param fullPathToWriteComparison
     * @return
     * @throws IOException
     */
    public static CompareResult performComparison(File fullPathToGoldFile,
            File fullPathToCompFile, String fullPathToWriteComparison)
            throws IOException {

        CompareResult results = null;
        InputStream goldStream = null;
        InputStream compareStream = null;

        ImageComparator comparator = new ImageComparator(new ConfigurationManager());

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

    /**
     * run the test. The parameters define where to write the comparison, find
     * the gold file, etc.
     *
     * @param sample
     * @return
     * @throws IOException
     */
    public CompareResult runComparisonTest(AbstractSingleScreenShot sample) throws IOException {
        DriverFactory.ENV env = DriverFactory.getENV();
        LOG.info("env is " + env.toString());
        REMOTE_SERVER_VALUE type =  DriverFactory.getRemoteServerValue();

        // the image to compare to, the mvn -P (qa,dev ....) param
        // will determine which env to use
        final String goldFileDesr = sample.classPathToGoldFile();
        LOG.info("gold file is " + goldFileDesr);

        // where the new image will be written
        String newImageFilePath = sample.fullPathToNewImageFilePath();
        LOG.info("newImage  is " + newImageFilePath);

        File goldFile
                = FileUtils.toFile(getClass()
                        .getResource(goldFileDesr));

        if (goldFile == null || !goldFile.exists()) {
            throw new RuntimeException("cannot find goldfile " + goldFileDesr);
        }

        sample.takeScreenShot(newImageFilePath, env.toString() );

        File newImageFile = new File(newImageFilePath);

        if (newImageFile == null) {
            throw new RuntimeException("cannot find comp dir " + newImageFilePath);
        }

        CompareResult compResult = null;
        //where the comparison image is written
        String destFileString = sample.fullPathToWriteComparisonResultsFile();
        LOG.info("comparison written to " + destFileString);

        compResult
                = SingleImageTestComparator
                .performComparison(goldFile, newImageFile, destFileString);

        return compResult;
    }

    /**
     * @return the genericAutomationRepository
     */
    public GenericAutomationRepository getGenericAutomationRepository() {
        return genericAutomationRepository;
    }

    public void writeToJsonFile(AbstractSingleScreenShot sample, String testDescription) throws IOException {
        
        synchronized (this) {
            final String jsonFileStr = JSON_FOLDER_LOCATION + "testimage_metadata.json";

            String goldFileStr
                    = FileUtils.toFile(getClass()
                            .getResource(sample.classPathToGoldFile())).toString();

        //gold file needs to be set to .. target/
            goldFileStr = "target" + goldFileStr.split("target")[1];

            String newImageFilePath = sample.fullPathToNewImageFilePath();
            String compareFileString = sample.fullPathToWriteComparisonResultsFile();
         // window.location.origin+window.location.pathname.split("target")[0] has a slash at the end
            // 

            String mergeTemplate
                    = "{\n"
                    + "\"testDescription\": \"%s\",\n"
                    + "\"goldFile\": \"%s\",\n"
                    + "\"newImageFile\": \"%s\",\n"
                    + "\"compareFile\": \"%s\" \n" + "}";

            String mergeResults
                    = String.format(mergeTemplate, testDescription, goldFileStr,
                            newImageFilePath, compareFileString);

            final File jsonFile2 = new File(jsonFileStr);
            String jsonData;

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String dateStr = sdf.format(new Date());
            ObjectMapper m = new ObjectMapper();
            ObjectNode json = (ObjectNode) m.readTree(jsonFile2);

            json.put("date", dateStr);
            ArrayNode arrayData = (ArrayNode) json.get(JSON_TEST_DATA_FIELD);

            JsonNode newThing = m.readValue(mergeResults, JsonNode.class);

            arrayData.add(newThing);

            jsonData
                    = m.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(json);
            LOG.info("writing comparison " + jsonFile2.getAbsolutePath());
            FileUtils.writeStringToFile(jsonFile2, jsonData);

            final String jsFileStr = JSON_FOLDER_LOCATION + "testimage_metadata.js";

            final File jsFile = new File(jsFileStr);
            if (jsonData == null) {
                jsonData = "null;";
            }
            FileUtils.writeStringToFile(jsFile, "var " + JSON_TEST_DATA_FIELD + " = \n" + jsonData);
        }
    }

}
