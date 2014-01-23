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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.technosf.smutpea.core.exceptions.MTAException;
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

	AbstractMTA mta;

	/**
	 * Creates and returns a new AbstractMTA
	 * 
	 * @return a new AbstractMTA implementation
	 */
	protected abstract AbstractMTA getNewClassUnderTest();

	/**
	 * @throws MTAException
	 */
	@BeforeMethod
	public void beforeMethod() throws MTAException
	{
		mta = getNewClassUnderTest();
		assertNotNull(mta);
	}

	/*
	 * @Test public void command() { throw new RuntimeException("Test not implemented"); }
	 */

	@Test
	public void connect()
	{
		mta.connect();
		assertEquals(mta.getResponse().substring(0, 4), "220 ", "Unexpected response code for connect");
		assertSame(mta.getReplyCode(), ReplyCode._220);
	}


	@Test
	public void getMTADateTime()
	{
		assertNotNull(mta.getMTADateTime());
	}


	@Test
	public void getMTADomain()
	{
		assertNotNull(mta.getMTADomain());
		assertTrue(mta.getMTADomain().trim().length() > 0);
	}

	@Test
	public void getMTAName()
	{
		assertNotNull(mta.getMTAName());
		assertTrue(mta.getMTAName().trim().length() > 0);
	}

	/*
	 * @Test public void getResponse() { throw new RuntimeException("Test not implemented"); }
	 * 
	 * @Test public void isClosed() { throw new RuntimeException("Test not implemented"); }
	 * 
	 * @Test public void processInvalidCommand() { throw new RuntimeException("Test not implemented"); }
	 * 
	 * @Test public void processLine() { throw new RuntimeException("Test not implemented"); }
	 * 
	 * @Test public void processValidCommand() { throw new RuntimeException("Test not implemented"); }
	 * 
	 * @Test public void send() { throw new RuntimeException("Test not implemented"); }
	 * 
	 * @Test public void sendMessage() { throw new RuntimeException("Test not implemented"); }
	 * 
	 * @Test public void setResponseString() { throw new RuntimeException("Test not implemented"); }
	 * 
	 * @Test public void setResponseReplyCode() { throw new RuntimeException("Test not implemented"); }
	 */
}
