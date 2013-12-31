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
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.technosf.smutpea.core.MTA;
import com.github.technosf.smutpea.core.exceptions.MTAException;

/**
 * AbstractCLIServer
 * <p>
 * Basic CLI server that will place an MTA on the Command Line for one transmission cycle.
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public abstract class AbstractCLIServer
{
	private static final Logger logger = LoggerFactory.getLogger(AbstractCLIServer.class);

	private static final InputStreamReader istream = new InputStreamReader(System.in);

	private static final BufferedReader bufRead = new BufferedReader(istream);

	/*
	 * Constants
	 */
	private static final String CONST_ERR_MTA_NULL = "MTA cannot be null";
	private static final String CONST_ERR_IO_READ = "IO Error reading input line";
	private static final String CONST_ERR_MTA_PROCESSING = "MTA error processing input line";


	/**
	 * Serve the MTA up on the Command Line Interface
	 * 
	 * @param mta
	 *            the MTA to serve
	 */
	public static void serve(final MTA mta)
	{
		try
		{
			/*
			 * Test the MTA, connect to it and present the initial reponse
			 */
			requireNonNull(mta).connect();
			System.out.println(mta.getResponse());
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
				line = bufRead.readLine();
			}
			catch (IOException e)
			{
				logger.error(CONST_ERR_IO_READ, e);
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
			System.out.println(mta.getResponse());

		} // while (!mta.isClosed())
	} // public static void serve(MTA mta)
}
