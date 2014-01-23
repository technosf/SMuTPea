package com.github.technosf.smutpea.mta.impl;

import static org.testng.Assert.fail;

import com.github.technosf.smutpea.core.exceptions.MTAException;
import com.github.technosf.smutpea.mta.AbstractMTAAbstractTest;

public class SinkMTATest extends AbstractMTAAbstractTest
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.technosf.smutpea.mta.AbstractMTAAbstractTest#getClassUnderTest()
	 */
	@Override
	protected SinkMTA getNewClassUnderTest()
	{
		SinkMTA mta = null;

		try
		{
			mta = new SinkMTA("Test");
		}
		catch (MTAException e)
		{
			fail("Cannot create new MTA");
		}

		return mta;
	}
}
