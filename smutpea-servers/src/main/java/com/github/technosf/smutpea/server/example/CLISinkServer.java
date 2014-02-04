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

package com.github.technosf.smutpea.server.example;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.technosf.smutpea.core.MTA;
import com.github.technosf.smutpea.core.exceptions.MTAException;
import com.github.technosf.smutpea.mta.impl.SinkMTA;
import com.github.technosf.smutpea.server.AbstractServer;

/**
 * CLISinkServer
 * <p>
 * A command line interface server based MTA that dumps all email
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public class CLISinkServer extends AbstractServer
{

	private static final Logger logger = LoggerFactory.getLogger(CLISinkServer.class);

	/*
	 * Constants
	 */
	private static final String CONST_MSG_MTA_OPEN = "MTA connection opened.";
	private static final String CONST_MSG_MTA_CLOSE = "MTA connection closed";
	private static final String CONST_ERR_MTA_ERR = "MTA cannot be instantiated";

	/**
	 * The MTA for this instance
	 */
	private final MTA mta;

	/**
	 * Run a CLI SinkMTA
	 * 
	 */
	public static void main(String[] args)
	{
		CLISinkServer server = null;

		try
		{
			server = new CLISinkServer();
			logger.info(CONST_MSG_MTA_OPEN);
			server.open();
		}
		catch (MTAException e)
		{
			logger.debug(CONST_ERR_MTA_ERR, e);
		}

	} // public static void main(String[] args)


	/**
	 * Constructor placing a {@code SinkMTA} on the standard command line
	 * 
	 * @throws MTAException
	 *             the {@code SinkMTA} could not be created.
	 */
	CLISinkServer() throws MTAException
	{
		super(System.in, System.out);
		mta = new SinkMTA("local.cli.server");
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.technosf.smutpea.server.AbstractServer#close()
	 */
	@Override
	protected void close()
	{
		try
		{
			closeInputStream();
		}
		catch (IOException e)
		{
			// Close quietly
		}

		try
		{
			closeOutputStream();
		}
		catch (IOException e)
		{
			// Close quietly
		}

		logger.info(CONST_MSG_MTA_CLOSE);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.technosf.smutpea.server.AbstractServer#newMTA()
	 */
	@Override
	protected MTA getMTA()
	{
		return mta;
	}
}
