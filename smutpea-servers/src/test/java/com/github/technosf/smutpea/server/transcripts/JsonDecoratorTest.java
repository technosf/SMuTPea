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

import java.io.IOException;
import java.net.URISyntaxException;

import org.testng.annotations.Test;

import com.github.technosf.smutpea.server.transcripts.Transcript.Decorator;

public class JsonDecoratorTest extends AbstractDecoratorTest 
{
    @Override
    Decorator getClassUnderTest(String location) {
        return new JsonDecorator("FlushTest", "anMTA", "99",location);
    }

    @Override
    long getDialogueSize() {
        return 1278;
    }

    @Test
    public void testFlush_Out() throws IOException {
        super.testFlush_Out();
    }

    @Test
    public void testFlush_File() throws IOException, URISyntaxException {
        super.testFlush_File();
    }

    @Test
    public void testFlush_Http() throws InterruptedException {
        super.testFlush_Http();
    }
}
