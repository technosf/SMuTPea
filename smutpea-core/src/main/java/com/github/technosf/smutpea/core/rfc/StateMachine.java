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

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import com.github.technosf.smutpea.core.exceptions.SessionClosedException;
import com.github.technosf.smutpea.core.exceptions.SessionStateException;

/**
 * StateMachine synthesized from RFC5321 Section 4.1.4.
 * <p>
 * A state machine defining valid transitions from a given state to a new state
 * for a given command. Synthesized from RFC5321 Section 4.1.4.
 * 
 * @see http://tools.ietf.org/html/rfc5321#section-4.1.4
 * @see http://tools.ietf.org/html/rfc2821#section-4.1.4
 * @author technosf
 * @since 0.0.1
 * @version 0.0.5
 */
public final class StateMachine
{
    private static final String CONST_MSG_ERR_SESSION_CLOSED =
            "Session is CLOSED.";
    private static final String CONST_FMT_INVALID_TX =
            "Invalid State transition [%1$s, %2$s].";

    /**
     * Valid state transition definition
     */
    //@SuppressWarnings("serial")
    static final Map<Map.Entry<SessionState, Command>, SessionState> STATE_TRANSITIONS =
            new HashMap<Map.Entry<SessionState, Command>, SessionState>()
            {
                {
                    /*
                     * From CONNECT state
                     */
                    put(key(SessionState.CONNECT,
                            Command.EHLO),
                            SessionState.COMMAND);
                    put(key(SessionState.CONNECT,
                            Command.HELO),
                            SessionState.COMMAND);
                    put(key(SessionState.CONNECT,
                            Command.QUIT),
                            SessionState.CLOSED);
                    put(key(SessionState.CONNECT,
                            Command.HELP),
                            SessionState.CONNECT);
                    put(key(SessionState.CONNECT,
                            Command.NOOP),
                            SessionState.CONNECT);
                    put(key(SessionState.CONNECT,
                            Command.RSET),
                            SessionState.CONNECT);
                    put(key(SessionState.CONNECT,
                            Command.VRFY),
                            SessionState.CONNECT);
                    put(key(SessionState.CONNECT,
                            Command.EXPN),
                            SessionState.CONNECT);

                    /*
                     * From COMMAND state
                     */
                    put(key(SessionState.COMMAND,
                            Command.MAIL),
                            SessionState.RCPT);
                    put(key(SessionState.COMMAND,
                            Command.QUIT),
                            SessionState.CLOSED);
                    put(key(SessionState.COMMAND,
                            Command.HELP),
                            SessionState.COMMAND);
                    put(key(SessionState.COMMAND,
                            Command.NOOP),
                            SessionState.COMMAND);
                    put(key(SessionState.COMMAND,
                            Command.RSET),
                            SessionState.COMMAND);
                    put(key(SessionState.COMMAND,
                            Command.VRFY),
                            SessionState.COMMAND);
                    put(key(SessionState.COMMAND,
                            Command.EXPN),
                            SessionState.COMMAND);
                    /*
                     * From RCPT state
                     */
                    put(key(SessionState.RCPT,
                            Command.RCPT),
                            SessionState.RCPT);
                    put(key(SessionState.RCPT,
                            Command.DATA),
                            SessionState.DATA);
                    put(key(SessionState.RCPT,
                            Command.QUIT),
                            SessionState.CLOSED);
                    put(key(SessionState.RCPT,
                            Command.HELP),
                            SessionState.RCPT);
                    put(key(SessionState.RCPT,
                            Command.NOOP),
                            SessionState.RCPT);
                    put(key(SessionState.RCPT,
                            Command.RSET),
                            SessionState.COMMAND);
                    put(key(SessionState.RCPT,
                            Command.VRFY),
                            SessionState.RCPT);
                    put(key(SessionState.RCPT,
                            Command.EXPN),
                            SessionState.RCPT);
                }
            };


    /**
     * Identifies the the next {@code SessionState} when transitioning from a
     * starting {@code SessionState} using a
     * given {@code Command}.
     * 
     * @param startingState
     *            the starting {@code SessionState}
     * @param command
     *            the {@code Command} identifying the transition
     * @return the next {@code SessionState} from the current
     *         {@code SessionState} using the given {@code Command}
     * @throws SessionClosedException
     *             the {@code Session} has been closed and cannot transition
     * @throws SessionStateException
     *             cannot transition from the current {@code SessionState} using
     *             the given {@code Command}
     */
    public static SessionState nextState(SessionState startingState,
            Command command)
            throws SessionClosedException, SessionStateException
    {
        if (SessionState.CLOSED == startingState)
        // If the session is closed, then no more commands can be satisfied.
        {
            throw new SessionClosedException(CONST_MSG_ERR_SESSION_CLOSED);
        }

        SessionState newState =
                STATE_TRANSITIONS.get(key(startingState, command));

        if (newState == null)
        // State transition cannot be found in list of valid transitions
        {
            throw new SessionStateException(String.format(CONST_FMT_INVALID_TX,
                    startingState,
                    command));
        }

        return newState;

    }


    /**
     * Helper method to generate a {@code Map.Entry} containing the given
     * {@code SessionState} and {@code Command}
     * 
     * @param state
     *            The {@code SessionState}
     * @param command
     *            The {@code Command}
     * @return the {@code Map.Entry} containing the given {@code SessionState}
     *         and {@code Command}
     */
    static Map.Entry<SessionState, Command> key(
            SessionState state,
            Command command)
    {
        return new AbstractMap.SimpleImmutableEntry<SessionState, Command>(
                state, command);
    }

}
