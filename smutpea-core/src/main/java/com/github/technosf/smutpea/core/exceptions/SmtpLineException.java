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

/**
 * SMTP Line Exception
 * <p>
 * Thrown when a submitted SMPT line is incorrectly formatted.
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public class SmtpLineException extends Exception
{
	/**
	 * guid
	 */
	private static final long	serialVersionUID	= 2412274435613750314L;


	/**
	 * Constructor
	 * 
	 * @param message
	 *            An explanation of the exception
	 */
	public SmtpLineException(final String message)
	{
		super(message);
	}


	/**
	 * Constructor
	 * 
	 * @param message
	 *            An explanation of the exception
	 * @param exception
	 *            The original exception
	 */
	public SmtpLineException(final String message, final Throwable exception)
	{
		super(message, exception);
	}
}
