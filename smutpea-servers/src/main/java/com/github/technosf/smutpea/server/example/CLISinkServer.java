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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.technosf.smutpea.core.MTA;
import com.github.technosf.smutpea.core.exceptions.MTAException;
import com.github.technosf.smutpea.mta.impl.SinkMTA;
import com.github.technosf.smutpea.server.AbstractCLIServer;

/**
 * CLISinkServer
 * <p>
 * A CLI based MTA that dumps all email
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public class CLISinkServer extends AbstractCLIServer
{
	private static final Logger logger = LoggerFactory.getLogger(CLISinkServer.class);

	/*
	 * Constants
	 */
	private static final String CONST_ERR_MTA_ERR = "MTA cannot be instantiated";


	public static void main(String[] args)
	{
		MTA mta = null;

		try
		{
			mta = new SinkMTA("local.cli.server");
		}
		catch (MTAException e)
		{
			logger.debug(CONST_ERR_MTA_ERR, e);
		}

		serve(mta);

	} // public static void main(String[] args)
}
