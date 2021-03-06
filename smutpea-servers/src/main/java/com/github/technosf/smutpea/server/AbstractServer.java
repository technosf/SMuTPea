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

/**
 * AbstractServer
 * <p>
 * Basic server that will place an MTA on the given input/output stream for one
 * transmission cycle.
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public abstract class AbstractServer
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
            "MTA error processing input line";

    private static final String CONST_ZPAD = "%04d";

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
     * Provides an MTA for this instance
     * 
     * @return the MTA
     */
    protected abstract MTA getMTA();


    /**
     * Clean up on MTA close.
     */
    protected abstract void close();


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
        int dialogue = 0;
        long uniquer = (Math.abs(System.nanoTime() / 1000000) * -1000000)
                + System.nanoTime();

        MTA mta = getMTA();

        PrintStream output = new PrintStream(out);

        try
        // Flush detrius from the input stream at the last moment.
        {
            in.skip(in.available());
        }
        catch (IOException e)
        {
            // NOOP
        }

        BufferedReader input = new BufferedReader(new InputStreamReader(in));

        try
        {
            /*
             * Test the MTA, connect to it and present the initial response
             */
            requireNonNull(mta).connect();
            logger.debug(CONST_MSG_MTA_DIALOGUE, uniquer,
                    String.format(CONST_ZPAD, dialogue++), mta.getResponse());
            output.println(mta.getResponse());
        }
        catch (NullPointerException e)
        // MTA was null
        {
            logger.error(CONST_ERR_MTA_NULL);
        }

        String line = null;
        String response = null;
        //int readAttempts = 0;

        while (!mta.isClosed()) //&& readAttempts++ < 10)
        {
            try
            // Read a line of input
            {
                if (input.ready())
                {
                    //readAttempts = 0;
                    line = input.readLine();

                    try
                    // Process the input line
                    {
                        logger.debug(CONST_MSG_CLIENT_DIALOGUE, uniquer,
                                String.format(CONST_ZPAD, dialogue++), line);
                        mta.processLine(line);
                    }
                    catch (MTAException e)
                    {
                        logger.error(CONST_ERR_MTA_PROCESSING, e);
                    }

                    if ((response = mta.getResponse()) != null
                            && !response.isEmpty())
                    // There is output
                    {
                        // Print out the response
                        logger.debug(CONST_MSG_MTA_DIALOGUE, uniquer,
                                String.format(CONST_ZPAD, dialogue++),
                                response);
                        output.println(response);
                    }
                }
                else
                {
                    try
                    {
                        Thread.sleep(250);
                    }
                    catch (InterruptedException e)
                    {
                        // NOOP
                    }
                }
            }
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

        close();

    } // public void serve(MTA mta)
}
