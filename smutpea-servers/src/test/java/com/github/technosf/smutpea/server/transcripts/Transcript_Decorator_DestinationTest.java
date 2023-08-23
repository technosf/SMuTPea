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

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

import com.github.technosf.smutpea.server.transcripts.Transcript.Decorator.Destination;

public class Transcript_Decorator_DestinationTest {

    @Test
    void testDetermine_systemout() 
    {
        assertEquals( Destination.determine("SYSTEM.OUT"), Destination.OUT);
        assertEquals( Destination.determine("system.out"), Destination.OUT);
        assertEquals( Destination.determine("SYSTEM.out"), Destination.OUT);
        assertEquals( Destination.determine(" SYSTEM.OUT"), Destination.OUT);
        assertEquals( Destination.determine("system.out "), Destination.OUT);
        assertEquals( Destination.determine(" SYSTEM.out "), Destination.OUT);
        
        assertEquals( Destination.determine("SYSTEMout"), Destination.INVALID);
        assertEquals( Destination.determine("SYSTEM_out"), Destination.INVALID);
        assertEquals( Destination.determine("_SYSTEM.out"), Destination.INVALID);
    }


    @Test
    void testDetermine_file() 
    {
        assertEquals( Destination.determine("file:///root/file/system"),Destination.FILE);
        assertEquals( Destination.determine("FILE:///root/file/system"),Destination.FILE);
        assertEquals( Destination.determine("FILE:///ROOT/FILE/SYSTEM"),Destination.FILE);
        assertEquals( Destination.determine(" FILE:///root/file/system"),Destination.FILE);
        assertEquals( Destination.determine("FILE:///root/file/system "),Destination.FILE);
        assertEquals( Destination.determine(" FILE:///root/file/system "),Destination.FILE);
        assertEquals( Destination.determine(" FILE:/// "),Destination.FILE);
        assertEquals( Destination.determine("file://root/file/system"),Destination.FILE);
        assertEquals( Destination.determine("file:/root/file"),Destination.FILE);
        assertEquals( Destination.determine("file:/"),Destination.FILE);

        assertEquals( Destination.determine("file//root/file/system"),Destination.INVALID);
        assertEquals( Destination.determine("file//"),Destination.INVALID);
        assertEquals( Destination.determine("file:"),Destination.INVALID);
    }

    @Test
    void testDetermine_http() 
    {
        assertEquals( Destination.determine("http://root/file/system"), Destination.HTTP);
        assertEquals( Destination.determine("HTTP://root/file/system"), Destination.HTTP);
        assertEquals( Destination.determine("HTTP://ROOT/FILE/SYSTEM"), Destination.HTTP);
        assertEquals( Destination.determine(" HTTP://root/file/system"), Destination.HTTP);
        assertEquals( Destination.determine("HTTP://root/file/system "), Destination.HTTP);
        assertEquals( Destination.determine(" HTTP://root/file/system "), Destination.HTTP);
        assertEquals( Destination.determine("http:/root/file"), Destination.HTTP);
        assertEquals( Destination.determine("http:/"), Destination.HTTP);

        assertEquals( Destination.determine(" HTTP:// "), Destination.INVALID);
        assertEquals( Destination.determine("http//root/file/system"), Destination.INVALID);
        assertEquals( Destination.determine("http//"), Destination.INVALID);
        assertEquals( Destination.determine("http:"), Destination.INVALID);
    }

    @Test
    void testDetermine_https() 
    {
        assertEquals( Destination.determine("https://root/file/system"), Destination.HTTP);
        assertEquals( Destination.determine("HTTPS://root/file/system"), Destination.HTTP);
        assertEquals( Destination.determine("HTTPs://ROOT/FILE/SYSTEM"), Destination.HTTP);
        assertEquals( Destination.determine(" HTTPS://root/file/system"), Destination.HTTP);
        assertEquals( Destination.determine("HTTPS://root/file/system "), Destination.HTTP);
        assertEquals( Destination.determine(" HTTPS://root/file/system "), Destination.HTTP);
        assertEquals( Destination.determine("https:/root/file"), Destination.HTTP);
        assertEquals( Destination.determine("https:/"), Destination.HTTP);

        assertEquals( Destination.determine(" HTTPS:// "), Destination.INVALID);
        assertEquals( Destination.determine("https//root/file/system"), Destination.INVALID);
        assertEquals( Destination.determine("https//"), Destination.INVALID);
        assertEquals( Destination.determine("https:"), Destination.INVALID);
    }

    @Test
    void testDetermine_invalid() 
    {
        assertEquals( Destination.determine(""), Destination.INVALID);
        assertEquals( Destination.determine("      "), Destination.INVALID);
        assertEquals( Destination.determine("1"), Destination.INVALID);
        assertEquals( Destination.determine(" 12345"), Destination.INVALID);
        assertEquals( Destination.determine("://test "), Destination.INVALID);
        assertEquals( Destination.determine(" URI://root/file/system "), Destination.INVALID);
        assertEquals( Destination.determine(" URI:// "), Destination.INVALID);
        assertEquals( Destination.determine("URL//root/file/system"), Destination.INVALID);
        assertEquals( Destination.determine("URL:/root/file"), Destination.INVALID);
        assertEquals( Destination.determine("URL:/"), Destination.INVALID);
        assertEquals( Destination.determine("URL//"), Destination.INVALID);
        assertEquals( Destination.determine("URL:"), Destination.INVALID);
    }
}
