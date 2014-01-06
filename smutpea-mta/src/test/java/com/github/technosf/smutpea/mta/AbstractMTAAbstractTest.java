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

package com.github.technosf.smutpea.mta;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.technosf.smutpea.core.exceptions.MTAException;
import com.github.technosf.smutpea.core.rfc2821.Command.CommandLine;
import com.github.technosf.smutpea.core.rfc2821.ReplyCode;

/**
 * AbstractMTAAbstractTest
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 * 
 */
public abstract class AbstractMTAAbstractTest
{

	/**
	 * The class under test
	 */
	AbstractMTA classUnderTest = getClassUnderTest();


	/**
	 * @return
	 */
	protected abstract AbstractMTA getClassUnderTest();


	@BeforeClass
	public void BeforeClass() throws MTAException
	{
		assertNotNull(classUnderTest);

		AbstractMTA mta = new AbstractMTA(null, null)
			{

				@Override
				public ReplyCode getReplyCode()
				{
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				protected void processValidCommand(CommandLine commandLine) throws MTAException
				{
					// TODO Auto-generated method stub

				}

				@Override
				protected void processInvalidCommand(CommandLine commandLine) throws MTAException
				{
					// TODO Auto-generated method stub

				}

				@Override
				protected void sendMessage(String message)
				{
					// TODO Auto-generated method stub

				}
			};
	}

	/**
	 * @throws MTAException
	 * 
	 */
	@Test
	public void AbstractMTA() throws MTAException
	{

	}

	@Test
	public void command()
	{
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void connect()
	{
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void getMTADateTime()
	{
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void getMTADomain()
	{
		assertNotNull(classUnderTest.getMTADomain());
		assertTrue(classUnderTest.getMTADomain().trim().length() > 0);
	}

	@Test
	public void getMTAName()
	{
		assertNotNull(classUnderTest.getMTAName());
		assertTrue(classUnderTest.getMTAName().trim().length() > 0);
	}

	@Test
	public void getResponse()
	{
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void isClosed()
	{
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void processInvalidCommand()
	{
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void processLine()
	{
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void processValidCommand()
	{
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void send()
	{
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void sendMessage()
	{
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void setResponseString()
	{
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void setResponseReplyCode()
	{
		throw new RuntimeException("Test not implemented");
	}
}
