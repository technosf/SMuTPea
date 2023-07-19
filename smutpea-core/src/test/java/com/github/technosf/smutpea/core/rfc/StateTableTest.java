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

package com.github.technosf.smutpea.core.rfc;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.technosf.smutpea.core.exceptions.SessionStateException;


/**
 * Unit test for {@code StateTable}
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.5
 * 
 */
public class StateTableTest
{

	private static final SessionState[] SESSION_STATES = SessionState.values();

	private StateTable classUnderTest;


	/**
     * 
     */
	@BeforeTest
	public void setupClassUnderTest()
	{
		classUnderTest = new StateTable();
	}


	/**
	 * @return
	 */
	@DataProvider(name = "states")
	private Object[][] testdata()
	{
		List<Object[]> data =
						new ArrayList<Object[]>();
		for (Object o : SESSION_STATES)
		{
			data.add(new Object[]
				{
								o
			});
		}

		return data.toArray(new Object[][] {});
	}


	/**
	 * @param state
	 */
	@Test
	public void updateState_BOUNDRY()
	{
		try
		{
			classUnderTest.updateState(null);
			fail("Exception expected.");
		}
		catch (SessionStateException e)
		{
			// Exception expected.
		}
	}


	/**
	 * @param state
	 * @throws SessionStateException
	 */
	@Test(dataProvider = "states")
	public void updateState(SessionState state) throws SessionStateException
	{
		classUnderTest.updateState(state);
	}


	/**
	 * @param state
	 * @throws SessionStateException
	 */
	@Test(dependsOnMethods =
		{
						"updateState"
	}, dataProvider = "states")
	public void getState(SessionState state) throws SessionStateException
	{
		classUnderTest.updateState(state);
		assertEquals(classUnderTest.getState(), state);
	}


	/**
     * 
     */
	@Test(dependsOnMethods =
		{
						"getState"
	})
	public void getStateHistory()
	{
		List<SessionState> history = new ArrayList<SessionState>();
		history.add(SessionState.CONNECT);
		history.addAll(Arrays.asList(SessionState.values()));
		history.addAll(Arrays.asList(SessionState.values()));
		assertEquals(classUnderTest.getStateHistory(), history.toArray());
	}

}
