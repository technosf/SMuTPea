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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Clock;
import java.util.LinkedList;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.technosf.smutpea.server.transcripts.Transcript.Decorator;
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
    {   "client" :
        {
            {   "milli" : "millisecond"
            ,   "input" : "input..."
            }   
        ,   {   "milli" : "millisecond"
            ,   "input" : "input..."
            }
        }
    ,   "server" : 
        {
            {   "milli" : "millisecond"
            ,   "output": "output..."
            }
        }
    ,   "client" :
        {
            {   "milli" : "millisecond"
            ,   "input" : "input..."
            }   
    }
}
 * </code>    
 * 
 * @author technosf
 * @since 0.0.6
 * @version 0.0.6
 */
public class JsonDecorator 
implements Decorator 
{
    /* 
     * Statics
     */

    private static final String CONST_ERROR_FLUSH = "Exception flushing JSON Transcript";

    private static final String CONST_JSON_TIMESTAMP = "@timestamp";
    private static final String CONST_JSON_AGENT_TYPE = "agent.type";
    private static final String CONST_JSON_AGENT_VERSION = "agent.version";
    private static final String CONST_JSON_AGENT_NAME = "agent.name";
    private static final String CONST_JSON_AGENT_ID = "agent.id";
    private static final String CONST_JSON_AGENT_ID_EPHEMERAL = "agent.ephemeral_id";


     // Reference UTC Clock for dialogue log timing 
    private static final Clock clock = Clock.systemUTC();

        // Statics
    // private static String hostname;
    
    // static {
    //     try {
    //         hostname = InetAddress.getLocalHost().getHostName();
    //     } catch (UnknownHostException e) {
    //         hostname = "Unknown";
    //     }
    // }

    
    /*
     * Working storage
     */


    //private final Transcript transcript;
    private final JsonObject json = new JsonObject();
    private final Destination destination;
    private final String location;



    /**
     * 
     * @param transcript
     * @param key
     */
    JsonDecorator(String mtaName, String agentId, String ephemeralId, String location)
    {
        this.location = location;
        destination =  Destination.determine(location);


       // this.transcript = transcript;

        // Initialize JSON trnscript
        json.put(CONST_JSON_TIMESTAMP,clock.instant());
        json.put(CONST_JSON_AGENT_TYPE,"SMuTPea");
        json.put(CONST_JSON_AGENT_VERSION,"??");
        json.put(CONST_JSON_AGENT_NAME,mtaName);
        json.put(CONST_JSON_AGENT_ID,agentId);
        json.put(CONST_JSON_AGENT_ID_EPHEMERAL,ephemeralId);

    } // JsonDecorator

    @Override
    public void flush(LinkedList<Entry> entries) 
    {
        try {
            switch (destination)
            {
                case OUT :
                    json.toJson(new PrintWriter(System.out, true));
                    break;
                case FILE :
                    append();
                    break;
                case HTTP :
                    post();
                    break;
                default:
                    break;
            };
        } catch (Exception e) {
            logger.info(CONST_ERROR_FLUSH, e);
        }
    } // flush


    /**
     * Post to a HTTP URI
     * 
     * @throws InterruptedException
     * @throws IOException
     * 
     */
    private void post() throws IOException, InterruptedException 
    {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(location))
            .POST(HttpRequest.BodyPublishers.ofString(json.toJson()))
            .build();
            client.send(request, HttpResponse.BodyHandlers.discarding());
    }


    /**
     * Append JSON to a file
     * 
     * @throws IOException
     * 
     */
    private void append() throws IOException 
    {
        FileWriter file = new FileWriter(new File(URI.create(location)),true);
        json.toJson(file);
    }

}
