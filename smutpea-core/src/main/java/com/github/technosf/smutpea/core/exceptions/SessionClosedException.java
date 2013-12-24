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
 * Session Closed Exception
 * <p>
 * Thrown when commands are actioned against a closed transition channel
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public class SessionClosedException extends Exception
{

	/**
	 * guid
	 */
	private static final long	serialVersionUID	= 6738836393134703240L;


	/**
	 * Constructor
	 * 
	 * @param message
	 *            An explanation of the exception
	 */
	public SessionClosedException(String message)
	{
		super(message);
	}
}
