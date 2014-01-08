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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.technosf.smutpea.core.MTA;
import com.github.technosf.smutpea.core.exceptions.MTAException;

/**
 * AbstractServer
 * <p>
 * Basic server that will place an MTA on the given input/output stream for one transmission cycle.
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public abstract class AbstractServer
{
	private static final Logger logger = LoggerFactory.getLogger(AbstractServer.class);

	/*
	 * Constants
	 */
	private static final String CONST_MSG_CLIENT_DIALOGUE = "Dialogue from Client:[{}]";
	private static final String CONST_MSG_MTA_DIALOGUE = "Dialogue from MTA:[{}]";
	private static final String CONST_ERR_MTA_NULL = "MTA cannot be null";
	private static final String CONST_ERR_IO_READ = "IO Error reading input line";
	private static final String CONST_ERR_MTA_PROCESSING = "MTA error processing input line";


	private final BufferedReader input;
	private final PrintStream output;

	/**
	 * Constructor setting the {@code MTA} and input/output
	 * 
	 * @param in
	 * @param out
	 */
	protected AbstractServer(final InputStream in, final OutputStream out)
	{
		this.input = new BufferedReader(new InputStreamReader(in));
		this.output = new PrintStream(out);
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
	 * Serve the MTA up on the Input and Output streams
	 * 
	 */
	public void open()
	{
		MTA mta = getMTA();

		try
		{
			/*
			 * Test the MTA, connect to it and present the initial response
			 */
			requireNonNull(mta).connect();
			output.println(mta.getResponse());
			logger.info(CONST_MSG_MTA_DIALOGUE, mta.getResponse());
		}
		catch (NullPointerException e)
		// MTA was null
		{
			logger.error(CONST_ERR_MTA_NULL);
		}

		String line = null;

		while (!mta.isClosed())
		{
			try
			// Read a line of input
			{
				line = input.readLine();
				logger.info(CONST_MSG_CLIENT_DIALOGUE, line);
			}
			catch (IOException e)
			{
				logger.error(CONST_ERR_IO_READ, e);
				break;
			}

			try
			// Process the input line
			{
				mta.processLine(line);
			}
			catch (MTAException e)
			{
				logger.error(CONST_ERR_MTA_PROCESSING, e);
			}

			// Print out the response
			output.println(mta.getResponse());
			logger.info(CONST_MSG_MTA_DIALOGUE, mta.getResponse());

		} // while (!mta.isClosed())

		close();

	} // public void serve(MTA mta)
}
