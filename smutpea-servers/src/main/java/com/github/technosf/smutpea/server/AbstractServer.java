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

package com.github.technosf.smutpea.server;

import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.SocketException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.technosf.smutpea.core.MTA;
import com.github.technosf.smutpea.core.exceptions.MTAException;
import com.github.technosf.smutpea.server.transcripts.Transcript;

/**
 * AbstractServer
 * <p>
 * Basic server that will place an MTA on the given input/output stream for one
 * transmission cycle.
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.6
 */
public abstract class AbstractServer 
    implements Server
{
    private static final Logger logger = LoggerFactory
            .getLogger(AbstractServer.class);

    /*
     * Constants
     */
    private static final String CONST_MSG_CLIENT_DIALOGUE =
            "Dialogue <{}#{}> Client => MTA:[{}]";
    private static final String CONST_MSG_MTA_DIALOGUE =
            "Dialogue <{}#{}> MTA => Client:[{}]";
    private static final String CONST_ERR_MTA_NULL = "MTA cannot be null";
    private static final String CONST_ERR_IO_CLOSED =
            "Client closed connection";
    private static final String CONST_ERR_IO_READ =
            "IO Error reading input line";
    private static final String CONST_ERR_MTA_PROCESSING =
            "MTA error processing input line: {}  MTA:[{}]";
    private static final String CONST_ERR_CLOSE =
            "Error closing resources";

    private static final String CONST_ZPAD = "%04d";

    private static final long CONST_WAIT_SLEEP = 250;


    //
    private final InputStream in;
    private final OutputStream out;


    /**
     * Constructor setting the {@code MTA} and input/output
     * 
     * @param in
     * @param out
     */
    protected AbstractServer(final InputStream in, final OutputStream out)
    {
        this.in = in;
        this.out = out;
    }

    
    /**
     * Closes the InputStream
     * 
     * @throws IOException
     */
    protected final void closeInputStream() throws IOException
    {
        in.close();
    }


    /**
     * Closes the OutputStream
     * 
     * @throws IOException
     */
    protected final void closeOutputStream() throws IOException
    {
        out.close();
    }


    /**
     * Serve the MTA up on the Input and Output streams
     */
    public void open()
    {
        // Initialize identifeirs
        int interaction = 0;
        long uniquer = ProcessHandle.current().pid() 
                + System.nanoTime();

        // Initialize and use the MTA, mta output and transcript         
        try (
            MTA mta = getMTA();
            Transcript transcript = Transcript.getTranscript(mta.getMTAName(),getServerId(),String.valueOf(uniquer));
            PrintStream output = new PrintStream(out)
        ) 
        // try-with-resources
        {
            try
            // Flush detrius from the input stream at the last moment.
            {
                in.skip(in.available());
            }
            catch (IOException e)
            {
                // NOOP
            }

            try (BufferedReader input = new BufferedReader(new InputStreamReader(in)))
            {
                try
                {
                    /*
                    * Test the MTA, connect to it and present the initial response
                    */
                    requireNonNull(mta).connect();
                    logger.info(CONST_MSG_MTA_DIALOGUE, uniquer,
                            String.format(CONST_ZPAD, interaction++), mta.getResponse());
                    output.println(mta.getResponse());
                    transcript.server(mta.getResponse());
                }
                catch (NullPointerException e)
                // MTA was null
                {
                    logger.error(CONST_ERR_MTA_NULL);
                }

                // String line = null;
                // String response = null;

                while (!mta.isClosed()) 
                {
                    try
                    // Read and respond to a line of input
                    {
                        interaction = processStanza(interaction, uniquer, mta, transcript, output, input);
                    } // Read a line of input
                    /*
                    * The Server is likely to be using straight IO or Socket IO,
                    * so just deal with it in the abstract class to deal with most 
                    * cases we are going to hit.
                    */
                    catch (SocketException e)
                    {
                        logger.warn(CONST_ERR_IO_CLOSED);
                        break;
                    }
                    catch (IOException e)
                    {
                        logger.error(CONST_ERR_IO_READ, e);
                        break;
                    }
                } // while (!mta.isClosed())
            } // try-with-resources input
            catch (IOException e)
            {
                logger.error(CONST_ERR_IO_READ, e);
            } // try(input)
        } // try-with-resources mta, output, transcript
        catch( Exception e)
        {
            logger.error(CONST_ERR_CLOSE, e);
        }

        cleanup();

    } // public void serve(MTA mta)


    /**
     * Process a stanza of Client-Server communication 
     * <p>
     * Broken out from {@code open} for readability
     * 
     * @param interaction the interaction number
     * @param uniquer the uniquer
     * @param mta the MTA
     * @param transcript the transscript
     * @param output the output stream
     * @param input the input stream
     * @return the new interaction #
     * @throws IOException 
     */
    private int processStanza(int interaction, long uniquer, MTA mta, Transcript transcript, PrintStream output,
            BufferedReader input) 
        throws Exception 
    {
        String line;
        String response;

        if (input.ready())
        // There was input from the client
        {
            mta.resetClientIdle();      // reset the idle
            line = input.readLine();

            try
            // Process the input line
            {
                logger.info(CONST_MSG_CLIENT_DIALOGUE, uniquer,
                        String.format(CONST_ZPAD, interaction++), line);
                mta.processInputLine(line);                        
                transcript.client(line);
            }
            catch (MTAException e)
            {
                logger.info(CONST_ERR_MTA_PROCESSING, line, e.getMessage());
                transcript.client(line);
            }

            if ((response = mta.getResponse()) != null
                    && !response.isEmpty())
            // There is output
            {
                // Print out the response
                logger.info(CONST_MSG_MTA_DIALOGUE, uniquer,
                        String.format(CONST_ZPAD, interaction++),
                        response);
                output.println(response);
                transcript.server(response);
            }

            return interaction;

        } //if (input.ready())

        /*
         * Input was not ready, so sleep for a while and check for timeouts
         */
        try
        {
            Thread.sleep(CONST_WAIT_SLEEP);  

            if (
                    mta.updateClientIdle(CONST_WAIT_SLEEP)
                && (response = mta.getResponse()) != null
                && !response.isEmpty()
                )
            /* 
             * There was a timeout while sleeping that generated a response. 
             * Log output to client and transcript
            */
            {
                logger.info(CONST_MSG_MTA_DIALOGUE, uniquer,
                        String.format(CONST_ZPAD, interaction++),
                        response);

                output.println(response);

                transcript.server(response);

            } // if
        } // try
        catch (InterruptedException e)
        {
            // NOOP
        }

        return interaction;

    } //private int session
}

