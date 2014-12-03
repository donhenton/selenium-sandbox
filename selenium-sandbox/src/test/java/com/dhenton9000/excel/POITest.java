/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.excel;

 
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
 

/**
 *
 * @author dhenton
 */
public class POITest {

    private static final String GOOD_FILE_LOCATION
            = "/excel/workbook_test.xlsx";

    private static final Logger LOG = LoggerFactory.getLogger(POITest.class);

    @Test
    public void testGeneralExcelRead() throws Exception {
        final File file = new File(FileUtils.toFile(getClass().getResource(GOOD_FILE_LOCATION)).getAbsolutePath());
        ExcelParser eP = new ExcelParser();
        SheetResults results = eP.parse(new FileInputStream(file));
        assertEquals(3, results.getHeaders().size());
        assertEquals(3, results.getRows().size());
        String[] test = {"alpha", "beta", "gamma"};
        List<String> testList = Arrays.asList(test);
        for (String t : testList) {
            assertTrue(results.getHeaders().contains(t));
        }
        
        assertEquals(results.getRows().get(1).get("beta"),"B");
        

    }

}
