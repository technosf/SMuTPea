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

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.technosf.smutpea.core.exceptions.SessionClosedException;
import com.github.technosf.smutpea.core.exceptions.SessionStateException;


/**
 * Unit test for {@code StateMachine}
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.5
 * 
 */
public class StateMachineTest
{

	/*
	 * Test fixtures
	 */
	@DataProvider(name = "keys")
	private Object[][] testdata()
	{
		List<Object[]> data = new ArrayList<Object[]>();

		for (SessionState state : SessionState.values())
		{
			for (Command command : Command.values())
			{
				Object result = null;

				if (SessionState.CLOSED == state)
				{
					result = new SessionClosedException(null);
				}
				else
				{
					result =
									StateMachine.STATE_TRANSITIONS
													.get(new AbstractMap.SimpleImmutableEntry<SessionState, Command>(
																	state, command));
					if (result == null)
					{
						result = new SessionStateException(null);
					}
				}

				data.add(new Object[]
					{
									state, command, result
				});
			}
		}

		return data.toArray(new Object[][] {});
	};

	
	/*
	 * Tests
	 */

	@Test(dataProvider = "keys")
	public void key(SessionState state, Command command, Object result)
	{
		assertEquals(StateMachine.key(state, command),
						new AbstractMap.SimpleImmutableEntry<SessionState, Command>(
										state, command));
	}


	@Test(dataProvider = "keys")
	public void nextState(SessionState state, Command command, Object result)
	{
		try
		{
			SessionState actual = StateMachine.nextState(state, command);
			assertEquals(actual, result);
		}
		catch (Exception e)
		{
			assertEquals(e.getClass(), result.getClass());
		}
	}
}
