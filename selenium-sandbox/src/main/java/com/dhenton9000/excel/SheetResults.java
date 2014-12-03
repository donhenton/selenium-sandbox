/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.excel;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author dhenton
 */
public class SheetResults {
    
    private ArrayList<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
    private ArrayList<String> headers = new ArrayList<String>();

    /**
     * @return the headers
     */
    public ArrayList<String> getHeaders() {
        return headers;
    }

    

    /**
     * @return the rows
     */
    public ArrayList<Map<String, Object>> getRows() {
        return rows;
    }

    
    
}
