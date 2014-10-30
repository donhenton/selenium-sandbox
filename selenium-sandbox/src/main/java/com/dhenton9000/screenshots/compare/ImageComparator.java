package com.dhenton9000.screenshots.compare;

import com.dhenton9000.screenshots.ConfigurationManager;
import com.dhenton9000.screenshots.ScreenShot;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.imageio.ImageIO;
import static com.dhenton9000.screenshots.compare.CompareResult.Area;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.FileUtils;

public class ImageComparator {

    private final ConfigurationManager configManager;

    public ImageComparator(ConfigurationManager config) {
      this.configManager = config;
    }

    private final static Logger LOG = LoggerFactory.getLogger(ImageComparator.class);

    
    public CompareResult compareImagePairs(String srcFileName) throws IOException {

        CompareResult result = null;
        String srcFileLocation = this.getConfigManager().getSourceFolder() + srcFileName + ScreenShot.IMAGE_EXTENSION;
        LOG.debug("srcFile " + srcFileLocation);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-dd-MM");

        String dateString = sdf.format(d);
        String targetFileLocation = this.getConfigManager().getTargetFolder() + srcFileName + ScreenShot.IMAGE_EXTENSION;
        LOG.debug("targetFile " + targetFileLocation);
        InputStream stream1
                = FileUtils.openInputStream(new File(srcFileLocation));
        InputStream stream2 = null;
        try {
            stream2 = FileUtils.openInputStream(new File(targetFileLocation));
        } catch (IOException ierr) {
            LOG.error("io problem with loading target image " + targetFileLocation + " " + ierr.getMessage());
            result = new CompareResult();
            result.setErrorMessage("Unable to load target file '" + targetFileLocation + "'");
            result.setInError(true);
            result.setSimpleFileName(srcFileName);
            return result;
        }

        result = compareImages(stream1, stream2);
        result.setSimpleFileName(srcFileName);
        if (result.getFailedAreas().size() > 0) {

            LOG.debug("number of areas failing " + result.getFailedAreas().size());
            Iterator<Area> iter = result.getFailedAreas().iterator();
            while (iter.hasNext()) {
                LOG.debug(iter.next() + "");
            }

            stream2.close();
            stream2
                    = FileUtils.openInputStream(new File(targetFileLocation));
            String compareFilePath = this.getConfigManager().getCompareFolder() 
                    + srcFileName + "_" + dateString + ScreenShot.IMAGE_EXTENSION;
            
            
            File f = new File(this.getConfigManager().getCompareFolder());
            f.mkdirs();
            
            LOG.debug("writing compare file '" + compareFilePath + "'");
            markImageAreas(stream2, result.getFailedAreas(), compareFilePath);
            result.setErrorMessage("errors found '" + compareFilePath + "'");
            result.setInError(true);
        } else {
            LOG.debug("no fail for '" + srcFileLocation + "'");
        }

        return result;
    }

    private CompareResult compareImages(InputStream stream1, InputStream stream2)
            throws IOException {
        // HashSet<Area> failedAreas = new HashSet<Area>();

        InputStream[] stream = new InputStream[2];

        stream[0] = stream1;
        stream[1] = stream2;

        BufferedImage image[] = new BufferedImage[2];

        Raster raster[] = new Raster[2];

        for (int id = 0; id < 2; id++) {
            image[id] = ImageIO.read(stream[id]);
            if (image[id] == null) {
                throw new RuntimeException("unable to read from input stream");

            }
            raster[id] = image[id].getData();
        }

        int w[] = new int[2];
        int h[] = new int[2];
        for (int id = 0; id < 2; id++) {
            w[id] = image[id].getWidth();
            h[id] = image[id].getHeight();
        }

        int width = Math.min(w[0], w[1]);
        int height = Math.min(h[0], h[1]);
        int maxW = Math.max(w[0], w[1]);
        int maxH = Math.max(h[0], h[1]);

        CompareResult result = new CompareResult(maxW, maxH, 0);

        if (width != maxW) {
            // Fail all extra width pixels
            for (int x = width + 1; x < maxW; x++) {
                for (int y = 0; y < maxH; y++) {
                    result.failed(x, y, 255);
                }
            }
        }

        if (height != maxH) {
            // Fail all extra height pixels
            for (int y = height + 1; y < maxH; y++) {
                for (int x = 0; x < maxW; x++) {
                    result.failed(x, y, 255);
                }
            }
        }

        int[][] pixelData = new int[2][];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int difference = 0;

                for (int id = 0; id < 2; id++) {
                    if (x < w[id] && y < h[id]) {
                        pixelData[id] = raster[id].getPixel(x, y, (int[]) null);
                    } else {
                        pixelData[id] = new int[]{-1, -1, -1};
                    }
                }

                for (int i = 0; i < 3; i++) {
                    difference += Math.abs(pixelData[0][i] - pixelData[1][i]);
                }

                if (difference > 0) {
                    result.failed(x, y, difference);
                }
            }
        }

        return result;
    }

    private void markImageAreas(InputStream stream, Set<Area> toBeMarked,
            String outputFileName) throws IOException {

        // We have errors that need to be marked
        BufferedImage image = ImageIO.read(stream);
        Raster raster = image.getData();

        int w = raster.getWidth();
        int h = raster.getHeight();

        Graphics2D graphics = image.createGraphics();
        graphics.setColor(new Color(250, 0, 0, 180));
        graphics.setStroke(new BasicStroke(CompareResult.LIMIT));

        for (Area area : toBeMarked) {
            int x1 = Math.max(0, area.x1 - CompareResult.LIMIT);
            int y1 = Math.max(0, area.y1 - CompareResult.LIMIT);
            int x2 = Math.min(w, area.x2 + CompareResult.LIMIT);
            int y2 = Math.min(h, area.y2 + CompareResult.LIMIT);

            if (x1 >= w) {
                x1 = w - 1;
            }
            if (x2 >= w) {
                x2 = w - 1;
            }
            if (y1 >= h) {
                y1 = h - 1;
            }
            if (y2 >= h) {
                y2 = h - 1;
            }
            graphics.drawRoundRect(x1, y1, x2 - x1, y2 - y1,
                    CompareResult.LIMIT, CompareResult.LIMIT);

            // System.out.println("Failed area: (" + area.x1 + "," + area.y1
            // + " - " + area.x2 + "," + area.y2 + ")"); }
        }
        ImageIO.write(image, "PNG", new File(outputFileName));
    }

    /**
     * @return the configManager
     */
    public ConfigurationManager getConfigManager() {
        return configManager;
    }

    

    public class Pair {

        public int x, y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            Pair other = (Pair) obj;
            return other.x == x && other.y == y;
        }

        @Override
        public int hashCode() {
            return x * 100000 + y;
        }

    }

}
