/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.excel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author dhenton
 */
public class ExcelParser {
    
    
       public SheetResults parse(InputStream inputStream) throws Exception {

        OPCPackage pkg = OPCPackage.open(inputStream);
        XSSFReader reader = new XSSFReader(pkg);
        SheetHandler sheetHandler = new SheetHandler(reader.getSharedStringsTable());
        XMLReader parser = fetchSheetParser(sheetHandler);

        // There should only be one sheet
        final Iterator<InputStream> it = reader.getSheetsData();
        final InputStream sheet = it.next();

        final InputSource sheetSource = new InputSource(sheet);
        parser.parse(sheetSource);
        sheet.close();
        
        return sheetHandler.getSheetResults();
         
    }
    
    private XMLReader fetchSheetParser(DefaultHandler sheetHandler) throws SAXException, ParserConfigurationException {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader parser = saxParser.getXMLReader();
        parser.setContentHandler(sheetHandler);

        return parser;
    }

    
}
