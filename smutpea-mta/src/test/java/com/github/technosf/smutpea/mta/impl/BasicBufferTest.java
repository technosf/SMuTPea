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

package com.github.technosf.smutpea.mta.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.util.Random;
import java.util.UUID;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.technosf.smutpea.core.Buffer;
import com.github.technosf.smutpea.core.rfc2821.Session;

/**
 * Unit test for {@code Buffer}
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 * 
 */
public class BasicBufferTest
{

	private static final String UNIQUE_VALUE = String.valueOf(System
					.currentTimeMillis());

	private static final Random rnd = new Random(System
					.currentTimeMillis());

	private static final String INIT_FORWARD = new UUID(rnd.nextLong(),
					rnd.nextLong()).toString();

	private static final String INIT_REVERSE = new UUID(rnd.nextLong(),
					rnd.nextLong()).toString();

	private static final String INIT_MAIL = new UUID(rnd.nextLong(),
					rnd.nextLong()).toString();

	private Buffer classUnderTest;


	/**
     * 
     */
	@BeforeClass
	public void setupCheck()
	{
		assertNotEquals(INIT_FORWARD, INIT_REVERSE,
						"INIT_FORWARD,INIT_REVERSE are equivilent.");
		assertNotEquals(INIT_FORWARD, INIT_MAIL,
						"INIT_FORWARD,INIT_MAIL are equivilent.");
		assertNotEquals(INIT_MAIL, INIT_REVERSE,
						"INIT_MAIL,INIT_REVERSE are equivilent.");
	}


	/**
     * 
     */
	@BeforeMethod
	public void setupClassUnderTest()
	{
		classUnderTest = new BasicBuffer();
		// Prime
		classUnderTest.setForwardPath(INIT_FORWARD);
		classUnderTest.setReversePath(INIT_REVERSE);
		classUnderTest.appendMailData(INIT_MAIL);
	}


	/**
     * 
     */
	@Test
	public void initialization()
	{
		Buffer uninitializedBuffer = new BasicBuffer();

		assertEquals(uninitializedBuffer.getForwardPath(), "");
		assertEquals(uninitializedBuffer.getReversePath(), "");
		assertEquals(uninitializedBuffer.getMailData(), "");

	}


	/**
     * 
     */
	@Test
	public void nullification()
	{
		try
		{
			classUnderTest.setForwardPath(null);
			fail("Expected NPE on setForwardPath()");
		}
		catch (NullPointerException e2)
		{
			// NPE Expected
		}

		try
		{
			classUnderTest.setReversePath(null);
			fail("Expected NPE on setReversePath()");
		}
		catch (NullPointerException e1)
		{
			// NPE Expected
		}

		try
		{
			classUnderTest.appendMailData(null);
			fail("Expected NPE on setMailData()");
		}
		catch (NullPointerException e)
		{
			// NPE Expected
		}

	}


	/**
     * 
     */
	@Test
	public void clear()
	{
		assertNotNull(classUnderTest.getForwardPath());
		assertNotNull(classUnderTest.getReversePath());
		assertNotNull(classUnderTest.getMailData());

		classUnderTest.clear();

		assertNotNull(classUnderTest.getForwardPath());
		assertNotNull(classUnderTest.getReversePath());
		assertNotNull(classUnderTest.getMailData());

		assertEquals(classUnderTest.getForwardPath(), "");
		assertEquals(classUnderTest.getReversePath(), "");
		assertEquals(classUnderTest.getMailData(), "");

	}


	/**
     * 
     */
	@Test
	public void clearForwardPath()
	{
		assertNotNull(classUnderTest.getForwardPath());
		assertNotNull(classUnderTest.getReversePath());
		assertNotNull(classUnderTest.getMailData());

		assertNotEquals(classUnderTest.getForwardPath(), "");
		classUnderTest.clearForwardPath();
		assertEquals(classUnderTest.getForwardPath(), "");

		assertNotNull(classUnderTest.getForwardPath());
		assertNotNull(classUnderTest.getReversePath());
		assertNotNull(classUnderTest.getMailData());

		assertNotEquals(classUnderTest.getReversePath(), "");
		assertNotEquals(classUnderTest.getMailData(), "");
	}


	/**
     * 
     */
	@Test
	public void clearMailData()
	{
		assertNotNull(classUnderTest.getForwardPath());
		assertNotNull(classUnderTest.getReversePath());
		assertNotNull(classUnderTest.getMailData());

		assertNotEquals(classUnderTest.getMailData(), "");
		classUnderTest.clearMailData();
		assertEquals(classUnderTest.getMailData(), "");

		assertNotNull(classUnderTest.getForwardPath());
		assertNotNull(classUnderTest.getReversePath());
		assertNotNull(classUnderTest.getMailData());

		assertNotEquals(classUnderTest.getForwardPath(), "");
		assertNotEquals(classUnderTest.getReversePath(), "");
	}


	/**
     * 
     */
	@Test
	public void clearReversePath()
	{
		assertNotNull(classUnderTest.getForwardPath());
		assertNotNull(classUnderTest.getReversePath());
		assertNotNull(classUnderTest.getMailData());

		assertNotEquals(classUnderTest.getReversePath(), "");
		classUnderTest.clearReversePath();
		assertEquals(classUnderTest.getReversePath(), "");

		assertNotNull(classUnderTest.getForwardPath());
		assertNotNull(classUnderTest.getReversePath());
		assertNotNull(classUnderTest.getMailData());

		assertNotEquals(classUnderTest.getForwardPath(), "");
		assertNotEquals(classUnderTest.getMailData(), "");
	}


	@Test
	public void getForwardPath()
	{
		assertEquals(classUnderTest.getForwardPath(), INIT_FORWARD);
	}


	@Test
	public void getMailData()
	{
		assertEquals(classUnderTest.getMailData(), INIT_MAIL);
	}


	@Test
	public void getReversePath()
	{
		assertEquals(classUnderTest.getReversePath(), INIT_REVERSE);
	}


	@Test
	public void setForwardPath()
	{
		assertEquals(classUnderTest.getReversePath(), INIT_REVERSE);
		assertEquals(classUnderTest.getMailData(), INIT_MAIL);

		assertEquals(classUnderTest.getForwardPath(), INIT_FORWARD);
		classUnderTest.setForwardPath(UNIQUE_VALUE);
		assertEquals(classUnderTest.getForwardPath(), UNIQUE_VALUE);

		assertEquals(classUnderTest.getReversePath(), INIT_REVERSE);
		assertEquals(classUnderTest.getMailData(), INIT_MAIL);
	}


	@Test
	public void appendMailData()
	{
		assertEquals(classUnderTest.getForwardPath(), INIT_FORWARD);
		assertEquals(classUnderTest.getReversePath(), INIT_REVERSE);

		assertEquals(classUnderTest.getMailData(), INIT_MAIL);
		classUnderTest.appendMailData(UNIQUE_VALUE);
		assertEquals(classUnderTest.getMailData(), INIT_MAIL + Session.CRLF
						+ UNIQUE_VALUE);

		assertEquals(classUnderTest.getForwardPath(), INIT_FORWARD);
		assertEquals(classUnderTest.getReversePath(), INIT_REVERSE);
	}


	@Test
	public void setReversePath()
	{
		assertEquals(classUnderTest.getForwardPath(), INIT_FORWARD);
		assertEquals(classUnderTest.getMailData(), INIT_MAIL);

		assertEquals(classUnderTest.getReversePath(), INIT_REVERSE);
		classUnderTest.setReversePath(UNIQUE_VALUE);
		assertEquals(classUnderTest.getReversePath(), UNIQUE_VALUE);

		assertEquals(classUnderTest.getForwardPath(), INIT_FORWARD);
		assertEquals(classUnderTest.getMailData(), INIT_MAIL);
	}
}
