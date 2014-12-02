/*
 * Copyright (c) 2010-2012 Lazery Attack - http://www.lazeryattack.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dhenton9000.filedownloader;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckFileHash {

    private static final Logger LOG = LoggerFactory.getLogger(CheckFileHash.class);

    public static String getFileHash(File fileToCheck, TypeOfHash typeOfHash) 
            throws FileNotFoundException, IOException {
         
        String  actualFileHash = null;
        switch (typeOfHash) {
            case MD5:
                actualFileHash = DigestUtils.md5Hex(new FileInputStream(fileToCheck));
                 
                break;
            case SHA1:
                actualFileHash = DigestUtils.shaHex(new FileInputStream(fileToCheck));
                 
                break;
        }
        
        return  actualFileHash;
        
        
    }
    private final TypeOfHash typeOfHash;
    private final String expectedFileHash;
    private final File fileToCheck;

    public CheckFileHash(File fileToCheck, String expectedFileHash, TypeOfHash typeOfHash) {
        this.fileToCheck = fileToCheck;
        this.expectedFileHash = expectedFileHash;
        this.typeOfHash = typeOfHash;

    }

    /**
     * Performs a expectedFileHash check on a File.
     *
     * @return
     * @throws IOException
     */
    public boolean hasAValidHash() throws IOException {
        if (this.fileToCheck == null ) {
            throw new FileNotFoundException("File to check has not been set!");
        }
        if (this.expectedFileHash == null || this.typeOfHash == null) {
            throw new NullPointerException("Hash details have not been set!");
        }
        if (!this.fileToCheck.exists() ) {
            throw new FileNotFoundException("File '"+fileToCheck.getCanonicalPath()+"' not found");
        }
        

        String actualFileHash = "";
        boolean isHashValid = false;

        switch (this.typeOfHash) {
            case MD5:
                actualFileHash = DigestUtils.md5Hex(new FileInputStream(this.fileToCheck));
                if (this.expectedFileHash.equals(actualFileHash)) {
                    isHashValid = true;
                }
                break;
            case SHA1:
                actualFileHash = DigestUtils.shaHex(new FileInputStream(this.fileToCheck));
                if (this.expectedFileHash.equals(actualFileHash)) {
                    isHashValid = true;
                }
                break;
        }

        LOG.info("Filename = '" + this.fileToCheck.getName() + "'");
        LOG.info("Expected Hash = '" + this.expectedFileHash + "'");
        LOG.info("Actual Hash = '" + actualFileHash + "'");

        return isHashValid;
    }

}
