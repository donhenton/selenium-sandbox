/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author dhenton
 */
public class SheetHandler extends DefaultHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SheetHandler.class);

    /**
     * Stores Header column {name, index} pairs
     */
    private Map<String, Integer> headerIndexMap = new HashMap<String, Integer>();

    /**
     * Stores header column {index, name} pairs
     */
    private Map<Integer, String> indexHeaderMap = new HashMap<Integer, String>();

    /**
     * Stores row values in { header column name, cell value} pairs
     */
    private Map<String, Object> rowValueMap = new HashMap<String, Object>();

    private ArrayList<Map<String, Object>> accumulatedRows = new ArrayList<Map<String, Object>>();

    /**
     * Table of sheet String (required by POI)
     */
    private SharedStringsTable sst;

    /**
     * Maintains current cell contents
     */
    private StringBuilder cellContents;

    /**
     * tracks beginning of a row
     */
    private boolean insideRow = false;

    /**
     * tracks whether next cell value is a String
     */
    private boolean nextIsString;

    /**
     * tracks whether next cell value is a number
     */
    private boolean nextIsNumber;

    /**
     * tracks the row index
     */
    private int rowIndex = -1;

    /**
     * tracks the column index
     */
    private int colIndex = -1;
    /**
     * the result to return
     */
    private SheetResults sheetResults = null;

    public SheetHandler(final SharedStringsTable sst) {

        this.sst = sst;
        this.cellContents = new StringBuilder();

        LOG.debug("sheet handler constructor");

    }

    @Override
    public void endDocument() throws SAXException {
        LOG.info("ending document " + accumulatedRows);

    }

    /**
     * {@inheritDoc}
     * <strong>note:</strong> this method is called from within the
     * XMLReader.parse method
     *
     * @see XMLReader#parse(InputSource)
     */
    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        //new row found

        if ("row".equals(name)) {
            insideRow = true;
            rowValueMap.clear();
            ++rowIndex;
        } // c => cell (new column found)
        else if ("c".equals(name)) {
            String cellType = attributes.getValue("t");

            if (cellType != null) {
                nextIsString = "s".equals(cellType);
            } else {
                //LOG.debug("attr size "+attributes.getLength());
                //nextIsNumber = "1".equals(attributes.getValue("s"));
                nextIsNumber = attributes.getLength() == 1;
            }

            colIndex = attributes.getValue("r").toCharArray()[0];  //H28 = [H,2,8]
        }

        // Clear contents cache
        cellContents.setLength(0);
        // LOG.debug(String.format("st row %d col %d name %s", rowIndex,colIndex, name));
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {

        String currentVal = "";

        //end of row
        if ("row".equals(name)) {
            insideRow = false;

            //if rowIndex is 0, then its the header row so validate
            if (rowIndex == 0) {
                LOG.debug("end for header index: {}", headerIndexMap);

            } //if rowIndex > 0 and rowValueMap has data, then add data to the mediaFilter list
            else if (!rowValueMap.isEmpty()) {
                LOG.debug("end for row index: {}", rowValueMap);
                HashMap<String, Object> tMap = new HashMap<String, Object>();
                for (String k : rowValueMap.keySet()) {
                    tMap.put(k, rowValueMap.get(k));
                }
                accumulatedRows.add(tMap);
            }
        }

        //inside row
        if (insideRow) {
            //next element is a cell value so process
            if ("v".equals(name) && (nextIsString || nextIsNumber)) {

                int idx = Integer.valueOf(cellContents.toString());
                if (nextIsString) {
                    currentVal = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                } else {
                    currentVal = String.valueOf(idx);
                }

                //header row so update header maps
                if (rowIndex == 0) {
                    headerIndexMap.put(currentVal, colIndex);
                    indexHeaderMap.put(colIndex, currentVal);
                } //update row maps
                else {
                    rowValueMap.put(indexHeaderMap.get(colIndex), currentVal);
                }
                nextIsString = false;
                nextIsNumber = false;
            }
        }

        //  LOG.debug(String.format("end row %d col %d name %s", rowIndex,colIndex, name));
    }

    /**
     * {@inheritDoc}
     * <strong>note:</strong> this method is called from within the
     * XMLReader.parse method
     *
     * @see XMLReader#parse(InputSource)
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        cellContents.append(ch, start, length);
    }

    SheetResults getSheetResults() {
        if (this.sheetResults == null) {
            sheetResults = new SheetResults();

            Map<String, Object> sample = accumulatedRows.get(0);
            for (String key : sample.keySet()) {
                this.sheetResults.getHeaders().add(key);

            }

            for (Map<String, Object> row : accumulatedRows) {
                HashMap<String, Object> rowCopy = new HashMap<String, Object>();
                for (String key : row.keySet()) {
                    Object item = row.get(key);
                    rowCopy.put(key, item);
                }
                this.sheetResults.getRows().add(rowCopy);
            }
        }
        return this.sheetResults;
    }

}
