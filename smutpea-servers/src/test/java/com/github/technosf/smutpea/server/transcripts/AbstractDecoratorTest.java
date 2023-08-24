/*
 * Copyright 2023 technosf [https://github.com/technosf]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.github.technosf.smutpea.server.transcripts;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.technosf.smutpea.server.transcripts.Transcript.Decorator;
import com.github.technosf.smutpea.server.transcripts.Transcript.Entry;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

abstract class AbstractDecoratorTest 
{
    static final String TMP = System.getProperty("java.io.tmpdir");

    Decorator classUnderTest;

    LinkedList<Entry> entries = new LinkedList<Entry>();

    static MockWebServer mockServer;
    static int mockServerPort = 8888;


    /*
     * Output size from the decorator
     */
    abstract long getDialogueSize();



    // ----------------------------

    @BeforeClass
    public void setup() throws IOException 
    {
        mockServer = new MockWebServer();
        mockServer.start(mockServerPort);

        // Example SMTP dialogue
        entries.add(new Entry(false, 0, "220 dummy.server ESMTP Dummy MTA"));
        entries.add(new Entry(true, 0, "EHLO faux.smtp.client"));
        entries.add(new Entry(false, 0, "250-dummy.server"));
        entries.add(new Entry(false, 0, "250 OK"));
        entries.add(new Entry(true, 1, "MAIL FROM:<test@test.qwerty>"));
        entries.add(new Entry(false, 1, "250 OK"));
        entries.add(new Entry(true, 1, "RCPT TO:<dummy@dummy.mta>"));
        entries.add(new Entry(false, 1, "250 OK"));
        entries.add(new Entry(true, 2, "DATA"));
        entries.add(new Entry(false, 2, "354 End data with <CR><LF>.<CR><LF>"));
        entries.add(new Entry(true, 2, "Subject: Test message"));
        entries.add(new Entry(true, 2, "From: Unit test <test@unit.test>"));
        entries.add(new Entry(true, 2, "Content-Type: text/html; charset=\"UTF-8\""));
        entries.add(new Entry(true, 2, "My email body"));
        entries.add(new Entry(true, 3, ""));
        entries.add(new Entry(true, 4, "."));
        entries.add(new Entry(true, 4, ""));
        entries.add(new Entry(false, 5, "250 OK"));
        entries.add(new Entry(true, 5, "QUIT"));
        entries.add(new Entry(false, 5, "221 BYE"));
        
    }

    @AfterClass
    public void teardown() throws IOException 
    {
        mockServer.shutdown();
    }

    /**
     * 
     * @param location
     * @return
     */
    abstract Decorator getClassUnderTest(String location);

    @Test
    public void testFlush_Out() throws IOException 
    {
        PrintStream ps = new PrintStream(new FileOutputStream(FileDescriptor.out));
        ByteArrayOutputStream y = new ByteArrayOutputStream();
        PrintStream x = new PrintStream(y,true);

        System.setOut(x);
        classUnderTest = getClassUnderTest("System.Out");
        classUnderTest.flush(entries);   
        
        x.flush();
        assertEquals(y.size(), getDialogueSize(), "String length");
      
        System.setOut(ps);  // reset System.Out
    }

    @Test
    public void testFlush_File() throws IOException, URISyntaxException 
    {
        String tmpFileUri = "File://"+ TMP + "/JDTest" + System.currentTimeMillis() + ".tmp";
         
        Path path = Path.of(new URI(tmpFileUri));
 
        assertFalse( Files.exists(path), "File should not exists");
      
        // new file
        classUnderTest = getClassUnderTest(tmpFileUri);
        classUnderTest.flush(entries);   
         
        assertTrue( Files.exists(path), "File should exist"); 
        assertEquals( Files.size(path), getDialogueSize(), "File length"); 

        // append file
        classUnderTest = getClassUnderTest(tmpFileUri);
        classUnderTest.flush(entries);   

        assertTrue( Files.exists(path), "File appended should exist"); 
        assertEquals( Files.size(path), 2*getDialogueSize(), "File append length"); 

        Files.delete(path);     // remove temp file
    }

    @Test
    public void testFlush_Http() throws InterruptedException 
    {
        String tmpHttpUri = "http://localhost:"+ mockServerPort + "/JDTest";
        mockServer.enqueue(new MockResponse());

        classUnderTest = getClassUnderTest(tmpHttpUri);
        classUnderTest.flush(entries);     
        
        RecordedRequest request = mockServer.takeRequest();

        assertEquals("/JDTest", request.getPath(), "Path");
        assertEquals( request.getMethod(), "POST", "Method");
        assertEquals( request.getBodySize(), getDialogueSize(), "Body Size");
    }

}
