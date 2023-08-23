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

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Clock;
import java.util.LinkedList;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transcript
 * <p>
 * Capture transcripts of the server & client transactions.
 * Format and output the transaction, if needed, using a
 * {@code Decorator} set in a System property
 * <p>
 * Transcripts are stored in a 
 *  
 * 
 * @author technosf
 * @since 0.0.6
 * @version 0.0.6
 */
public class Transcript 
{
    private static final Logger logger = LoggerFactory
            .getLogger(Transcript.class);

    /**
     * 
     */
    public record Entry( boolean isClient, long offset, String line) {};

    /**
     * {@code Decorator} decorates a transcript into a given format, for example JSON
     * and puts it out to the approriate channel
     */
    public interface Decorator 
    {
        // Use the Transcript logger for this subclass
        static final Logger logger = Transcript.logger; 
        
        /**
         * Destination
         */
        enum Destination 
        {   
            INVALID
        ,   OUT
        ,   HTTP
        ,   FILE;

            /**
             * Determine the Destination enum appropriate for the given destination string
             * <p>
             * Known values are 'system.out', or URIs for HTTP/S and FILEs.
             * 
             * @param destination
             * @return Destination enum
             */
            public static Destination determine(String destination) 
            {
                destination = destination.strip();
                if ("system.out".equals(destination.toLowerCase())) return OUT;
                try 
                {
                    URI uri = new URI(destination);
                    switch (uri.getScheme().toLowerCase())
                    {
                        case "file" : return FILE;
                        case "http" : return HTTP;                    
                        case "https" : return HTTP;
                    };

                } 
                catch (Exception e) {}

                return INVALID;
            }
        };

        /**
         * Format and flush data from an ordered list of {@code Entry} stanzas
         * 
         * @param stanzas stanzas of the dialogue 
         */
        public void flush(LinkedList<Entry> stanzas);
        
    };

    /*
     * 
     */

    //
    private static final String CONST_JSON_KEY = "transcriptJSON";
    private static final String CONST_PLAIN_KEY = "transcriptPlain";

    private static final Properties PROPS = System.getProperties();

    /* 
     * Reference UTC Clock for dialogue log timing 
     */
    private static final Clock clock = Clock.systemUTC();



    // Timer offset
    private long startOffset = 0 - clock.millis();

    /*
     * General info available at package level
     */
    final String mtaName;
    final String agentId;
    final String ephemeralId;

    private final Decorator decorator;

    /*
     * Ordered list of entries
     */
    private final LinkedList<Entry> entries = new LinkedList<Entry>();


    /**
     * Returns a properly configured Transcript
     * 
     * @param mtaName
     * @param agentId
     * @param ephemeralId
     * @return the transcript
     */
    public static Transcript getTranscript(String mtaName, String agentId, String ephemeralId)
    {
        Decorator d = new NullDecorator();

        if (PROPS.containsKey(CONST_PLAIN_KEY))
        {
            d = new PlainDecorator( PROPS.getProperty(CONST_PLAIN_KEY).strip());
        }

        if ( PROPS.containsKey(CONST_JSON_KEY) )
        {
            d = new JsonDecorator(  mtaName,  agentId,  ephemeralId, PROPS.getProperty(CONST_JSON_KEY).strip());
        }

        Transcript t =  new Transcript(mtaName, agentId, ephemeralId, d);

        return t;
    }

    /**
     * Initialize a Transcript with basic info
     * 
     * @param MtaName
     * @param agentId
     * @param ephemeralId
     * @param decorator
     */
    private Transcript(String mtaName, String agentId, String ephemeralId, Decorator decorator)
    {
        this.mtaName = mtaName;
        this.agentId = agentId;
        this.ephemeralId = ephemeralId;
        this.decorator = decorator;
    }


    /**
     * Process a Client sentance into the transcript 
     * 
     * @param clientSays what the client says
     */
    public void client(String clientSays )
    {
        entries.add(new Entry(true, clock.millis() + startOffset, clientSays));
    }


    /**
     * Process what the Server says into the transcript
     * 
     * @param serverSays what the server says
     */
    public void server(String serverSays)
    {
        entries.add(new Entry(false, clock.millis() + startOffset, serverSays));
    }


    /**
     * Let the GC own sending the Trnscript to its final destination
     */
    @Override
    protected void finalize() {
        decorator.flush(entries);
    }
}
