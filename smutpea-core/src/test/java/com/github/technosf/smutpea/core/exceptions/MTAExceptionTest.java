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

import static org.easymock.EasyMock.createNiceMock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.testng.annotations.Test;

import com.github.technosf.smutpea.core.rfc2821.Command.CommandLine;

/**
 * Unit test for {@code MTAException}
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 * 
 */
public class MTAExceptionTest
{
	CommandLine cl = createNiceMock(CommandLine.class);


	/**
	 * 
	 */
	@Test
	public void MTAExceptionString()
	{
		String string = "abc";
		MTAException x = new MTAException(string);
		assertEquals(x.getMessage(), string);
		assertNull(x.getCommandLine());
		assertNull(x.getCause());
	}


	/**
	 * 
	 */
	@Test
	public void MTAExceptionStringCommandLine()
	{
		String string = "abc";
		MTAException x = new MTAException(string, cl);
		assertEquals(x.getMessage(), string);
		assertEquals(x.getCommandLine(), cl);
		assertNull(x.getCause());
	}


	/**
	 * 
	 */
	@Test
	public void MTAExceptionStringThrowable()
	{
		String string = "abc";
		Exception z = new Exception();
		MTAException x = new MTAException(string, z);
		assertEquals(x.getMessage(), string);
		assertNull(x.getCommandLine());
		assertEquals(x.getCause(), z);
	}


	/**
	 * 
	 */
	@Test
	public void MTAExceptionStringCommandLineThrowable()
	{
		String string = "abc";
		Exception z = new Exception();
		MTAException x = new MTAException(string, cl, z);
		assertEquals(x.getMessage(), string);
		assertEquals(x.getCommandLine(), cl);
		assertEquals(x.getCause(), z);
	}
}
