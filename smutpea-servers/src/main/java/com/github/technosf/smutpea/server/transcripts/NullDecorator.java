/*
 * Copyright 2013 technosf [https://github.com/technosf]
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

import java.util.LinkedList;

import com.github.technosf.smutpea.server.transcripts.Transcript.Decorator;
import com.github.technosf.smutpea.server.transcripts.Transcript.Entry;

/**
 * Decorator that is mute.
 *  
 * * @author technosf
 * @since 0.0.6
 * @version 0.0.6
 */
public class NullDecorator 
implements Decorator 
{
    @Override
    public void flush(LinkedList<Entry> entries) {}
}
