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

import static java.util.Objects.requireNonNull;

import java.util.LinkedList;

import com.github.technosf.smutpea.core.exceptions.SessionStateException;

/**
 * StateTable, per RFC2821 Section 2.3.6
 * <p>
 * Keeps track of the current state and state change history
 * 
 * @see http://tools.ietf.org/html/rfc2821#section-2.3.6
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public final class StateTable
{

	private static final String				CONST_MSG_ERR_TRANSSTATE	= "Transition state unavailable.";

	/**
	 * The history of the state table, including current SessionState.
	 */
	//@SuppressWarnings("serial")
	private final LinkedList<SessionState>	stateHistory =
		new LinkedList<SessionState>()
			{
				{
					add(SessionState.CONNECT);
				}
			};


	/**
	 * Returns the current {@code SessionState}
	 * 
	 * @return the current state
	 */
	public final SessionState getState()
	{
		return stateHistory.getLast();
	}


	/**
	 * Updates the current {@code SessionState}, adding it to the history.
	 * 
	 * @param state
	 *            the new current state
	 * @throws SessionStateException
	 *             thrown if the new state is {@literal null}
	 */
	public final void updateState(SessionState newState)
			throws SessionStateException
	{
		try
		{
			stateHistory.add(requireNonNull(newState));
		}
		catch (NullPointerException e)
		{
			throw new SessionStateException(CONST_MSG_ERR_TRANSSTATE);
		}
	}


	/**
	 * Returns an array of past and current {@code SessionState}
	 * 
	 * @return {@code SessionState} history
	 */
	public final SessionState[] getStateHistory()
	{
		return stateHistory.toArray(new SessionState[] {});
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getState().name();
	}
}
