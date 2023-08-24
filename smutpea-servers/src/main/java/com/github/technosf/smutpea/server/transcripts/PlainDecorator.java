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

import com.github.technosf.smutpea.server.transcripts.Transcript.Entry;

/**
 * Decorator that is mute.
 * 
 * 
 * @author technosf
 * @since 0.0.6
 * @version 0.0.6
 */
public class PlainDecorator 
extends AbstractDecorator
{

    private static final String CRLF = "\n";
    private final StringBuffer dialogue = new StringBuffer();

    PlainDecorator(String location) {
        super(location);
    }


    @Override
    void process(LinkedList<Entry> stanzas) 
    {
        dialogue.append( CRLF );
        for ( Entry stanza : stanzas ) 
        {
            dialogue.append( stanza.isClient() ? "Client @ " : "Server @ " );
            dialogue.append( stanza.offset() );
            dialogue.append( "\t:: " );
            dialogue.append( stanza.line() );
            dialogue.append( CRLF );
        }        
        dialogue.append( CRLF );
    }

    
    @Override
    String getDialogue() {

        return dialogue.toString();
    }


    @Override
    public String getName() {
        return "Plain";
    }
}
