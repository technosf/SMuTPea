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


import java.util.LinkedList;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.technosf.smutpea.server.transcripts.Transcript.Entry;

/**
 * Transcript
 * <p>
 * Capture JSON transcripts of the client transactions.
 * <p>
 * Clients, and servers may say several things in a row,
 * 
 * <code>
{    "@timestamp" : "the timestamp"
,    "agent.xyz"  : "agent details"
,    "transcript" :
    [
        {
            "who" : "Server"
            "dialogue" : ["dialogue"]
            "ended" : milli 
        },     
        {
            "who" : "Client"
            "dialogue" : ["dialogue", "dialogue"]
            "ended" : milli 
        },     
        {
            "who" : "Server"
            "dialogue" : ["dialogue", "dialogue"]
            "ended" : milli 
        },
    }
}
 * </code>    
 * 
 * @author technosf
 * @since 0.0.6
 * @version 0.0.6
 */
public class JsonDecorator 
extends AbstractDecorator
{
    /* 
     * Statics
     */
    private static final String CONST_JSON_TIMESTAMP = "@timestamp";
    private static final String CONST_JSON_AGENT_TYPE = "agent.type";
    private static final String CONST_JSON_AGENT_VERSION = "agent.version";
    private static final String CONST_JSON_AGENT_NAME = "agent.name";
    private static final String CONST_JSON_AGENT_ID = "agent.id";
    private static final String CONST_JSON_AGENT_ID_EPHEMERAL = "agent.ephemeral_id";


    /*
     * Working storage
     */


    //private final Transcript transcript;
    private final JsonObject json = new JsonObject();



    /**
     * 
     * @param transcript
     * @param key
     */
    JsonDecorator(String mtaName, String agentId, String ephemeralId, String location)
    {
        super(location);

        // Initialize JSON trnscript
        json.put(CONST_JSON_TIMESTAMP,clock.instant().toString());
        json.put(CONST_JSON_AGENT_TYPE,"SMuTPea");
        json.put(CONST_JSON_AGENT_VERSION,"??");
        json.put(CONST_JSON_AGENT_NAME,mtaName);
        json.put(CONST_JSON_AGENT_ID,agentId);
        json.put(CONST_JSON_AGENT_ID_EPHEMERAL,ephemeralId);

    } // JsonDecorator


    /**
     * 
     * @param stanzas
     */
    void process(LinkedList<Entry> stanzas) 
    {
        String source;
        String lastSource = "unset";
        JsonArray jsonStanzas = new JsonArray();
        JsonArray  jsonDialogue = new JsonArray();
        JsonObject lastJsonStanza = new JsonObject();    
        jsonStanzas.add(lastJsonStanza);
        long ended = -1;

        json.put("transcript", jsonStanzas);

        for ( Entry stanza : stanzas ) 
        {
            source = stanza.isClient() ? "Client" : "Server";

            if ( !source.equals(lastSource) && ended > -1)
            // Check that this isn't the first change in speaker 
            {
                lastJsonStanza.put("ended",ended);
                lastJsonStanza = new JsonObject();  
                jsonStanzas.add(lastJsonStanza);
                jsonDialogue = new JsonArray();
                lastJsonStanza.put("who",source);
                lastJsonStanza.put("dialogue",jsonDialogue);
            }        
            jsonDialogue.add( stanza.line()); 
            ended = stanza.offset();
        }
        lastJsonStanza.put("ended",ended);
    }


    @Override
    String getDialogue() 
    {
        return json.toJson();
    }

    @Override
    public String getName() 
    {
        return "JSON";
    }
}
