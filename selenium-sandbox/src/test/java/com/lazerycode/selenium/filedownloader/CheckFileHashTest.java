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

package com.lazerycode.selenium.filedownloader;

import com.dhenton9000.filedownloader.CheckFileHash;
import com.dhenton9000.filedownloader.FileDownloader;
import com.dhenton9000.filedownloader.TypeOfHash;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import java.io.IOException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.WebDriver;

public class CheckFileHashTest {

     private static final String GOOD_FILE_LOCATION = "/web/download.zip";
   // private final URL testFile = this.getClass().getResource("/web/download.zip");

    @Test
    public void fileExists() throws Exception
    {
        File f = convertClassPathToFileRef(GOOD_FILE_LOCATION);
        assertNotNull(f);
        
        assertTrue(f.exists());
    }
    
    
    @Test(expected=FileNotFoundException.class)
    public void testHashNeedsInput() throws IOException {
        CheckFileHash checkHash = new CheckFileHash(null, null, null);
        checkHash.hasAValidHash();
    }

    //File fileToCheck, String expectedFileHash, TypeOfHash typeOfHash
    @Test(expected=FileNotFoundException.class)
    public void testHashGetsNotFile() throws IOException {
        File file = null;
        String expectedHash = "x";
        TypeOfHash hashType = TypeOfHash.MD5;
        CheckFileHash checkHash = new CheckFileHash(file, expectedHash, hashType);
        checkHash.hasAValidHash();
    }

    @Test(expected=FileNotFoundException.class)
    public void testHashGetsBadFile() throws IOException {
        File file = new File("garbage");
        String expectedHash = "x";
        TypeOfHash hashType = TypeOfHash.MD5;
        CheckFileHash checkHash = new CheckFileHash(file, expectedHash, hashType);
        checkHash.hasAValidHash();
    }

    @Test
    public void testHashMismatch() throws  Exception {
        
        File file   = convertClassPathToFileRef(GOOD_FILE_LOCATION);
        String expectedHash = "x";
        TypeOfHash hashType = TypeOfHash.MD5;
        CheckFileHash checkHash = new CheckFileHash(file, expectedHash, hashType);
        assertFalse(checkHash.hasAValidHash());
    }

    @Test
    public void testHashMatch() throws  Exception {
         File file   = convertClassPathToFileRef(GOOD_FILE_LOCATION);
        String expectedHash = CheckFileHash.getFileHash(file,TypeOfHash.MD5);
        TypeOfHash hashType = TypeOfHash.MD5;
        CheckFileHash checkHash = new CheckFileHash(file, expectedHash, hashType);
        assertTrue(checkHash.hasAValidHash());
    }
    
    
    private File convertClassPathToFileRef(String path) throws  Exception {
        return (new FileDownloader((WebDriver) null)).convertClassPathToFileRef(path);
    }
    
}
