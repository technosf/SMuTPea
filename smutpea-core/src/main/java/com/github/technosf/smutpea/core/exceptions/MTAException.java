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

package com.github.technosf.smutpea.core.exceptions;

import com.github.technosf.smutpea.core.rfc2821.Command.CommandLine;

/**
 * Message Transfer Agent Exception
 * <p>
 * Exceptions generated from {@code MTA} implementations.
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public class MTAException extends Exception
{

	/**
	 * guid
	 */
	private static final long	serialVersionUID	= -3698290378816841524L;

	/**
	 * The command line at the root of this exception
	 */
	private final CommandLine	commandLine;


	/**
	 * Constructor
	 * 
	 * @param message
	 *            A helpful message
	 */
	public MTAException(final String message)
	{
		super(message);
		this.commandLine = null;
	}


	/**
	 * Constructor
	 * 
	 * @param message
	 *            A helpful message
	 * @param commandLine
	 *            The command line at the root of this exception
	 */
	public MTAException(final String message,
			final CommandLine commandLine)
	{
		super(message);
		this.commandLine = commandLine;
	}


	/**
	 * Constructor
	 * 
	 * @param message
	 *            A helpful message
	 * @param exception
	 *            The original exception
	 */
	public MTAException(final String message, final Throwable exception)
	{
		super(message, exception);
		this.commandLine = null;
	}


	/**
	 * Constructor
	 * 
	 * @param message
	 *            A helpful message
	 * @param commandLine
	 *            The command line at the root of this exception
	 * @param exception
	 *            The original exception
	 */
	public MTAException(final String message,
			final CommandLine commandLine, final Throwable exception)
	{
		super(message, exception);
		this.commandLine = commandLine;
	}


	/**
	 * Returns the command line that the MTA choked on.
	 * 
	 * @return the erring command line
	 */
	public final CommandLine getCommandLine()
	{
		return commandLine;
	}

}
