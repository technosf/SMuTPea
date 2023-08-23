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


import com.github.technosf.smutpea.server.transcripts.Transcript.Decorator;
import com.github.technosf.smutpea.server.transcripts.Transcript.Entry;

/**
 * AbstractDecorator
 * <p>
 * Common function that take dialogue stanzas, process them 
 * into a dialogue and post that dialogue to output channel. 
 * 
 * @author technosf
 * @since 0.0.6
 * @version 0.0.6
 */
abstract class AbstractDecorator 
implements Decorator 
{
    /* 
     * Statics
     */


     private static final String CONST_ERROR_FLUSH = "Exception flushing Transcript";


    // Reference UTC Clock for dialogue log timing 
    static final Clock clock = Clock.systemUTC();


    
    /*
     * Working storage
     */

    private final Destination destination;
    private final String location;


    /**
     * 
     * @param location the output loation
     */
    AbstractDecorator(String location)
    {
        this.location = location;
        this.destination =  Destination.determine(location);

    } // JsonDecorator


    /**
     * @see 
     */
    @Override
    public final void flush(LinkedList<Entry> stanzas) 
    {
        process(stanzas);
        
        try {
            switch (destination)
            {
                case OUT :
                    print();
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
     * Process the SMTP client-server stanzas 
     * 
     * @param stanzas
     */
    abstract void process(LinkedList<Entry> stanzas);


    /**
     * Returns the SMTP client-server dialogue
     * 
     * @return the dialogue
     */
    abstract String getDialogue();


    //-------------------------------------------------------------

    /**
     * Post dialogue to a HTTP URI
     * 
     * @throws InterruptedException
     * @throws IOException
     * 
     */
    final void post() throws IOException, InterruptedException 
    {
        if ( Destination.HTTP != destination) return;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(location))
            .POST(HttpRequest.BodyPublishers.ofString(getDialogue()))
            .build();
            client.send(request, HttpResponse.BodyHandlers.discarding());
    }


    /**
     * Append dialogue to a file
     * 
     * @throws IOException
     * 
     */
    final void append() throws IOException 
    {
        if ( Destination.FILE != destination) return;

        FileWriter file = new FileWriter(new File(URI.create(location)),true);
        file.write(getDialogue());
        file.flush();
    }

    /**
     * Print dialogue to System.Out
     * 
     * @throws IOException
     * 
     */
    final void print() throws IOException 
    {
        if ( Destination.OUT != destination) return;

        PrintWriter printer = new PrintWriter(System.out, true);
        printer.write(getDialogue());
        printer.flush();
    }

}
