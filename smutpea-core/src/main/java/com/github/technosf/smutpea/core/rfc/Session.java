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

import static com.github.technosf.smutpea.core.rfc.Command.parseLine;
import static java.util.Objects.requireNonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.technosf.smutpea.core.MTA;
import com.github.technosf.smutpea.core.exceptions.MTAException;
import com.github.technosf.smutpea.core.exceptions.SessionClosedException;
import com.github.technosf.smutpea.core.exceptions.SessionStateException;
import com.github.technosf.smutpea.core.exceptions.SmtpLineException;
import com.github.technosf.smutpea.core.rfc.Command.CommandLine;

/**
 * A SMTP session.
 * <p>
 * Defines a SMTP session, handling {@code SessionState} management, calling an
 * {@code MTA} implementation with events and for responses.
 * 
 * @see http://tools.ietf.org/html/rfc2821#section-3
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public final class Session
{
    private static final Logger logger = LoggerFactory.getLogger(Session.class);

    /**
     * The SMTP end-of-line signifier, CRLF
     */
    public final static String CRLF = "\r\n";

    /*
     * Constants
     */
    private static final String CONST_MSG_PROCESS_CMD =
            "Processing command line:[{}]";
    private static final String CONST_MSG_STATE_TX =
            "State transition proposed from:[{}] to [{}]";
    private static final String CONST_MSG_MTA_CMD =
            "Sending command line:[{}] to MTA";
    private static final String CONST_MSG_STATE_SAME =
            "Invalid command - leaving state as:[{}]";
    private static final String CONST_MSG_STATE_UPDATE =
            "Updating state from:[{}] to [{}]";
    private static final String CONST_MSG_RESPONSE = "Returning response:[{}]";
    private static final String CONST_MSG_PROCESS_DATA =
            "Processing data line:[{}]";
    private static final String CONST_MSG_SEND =
            "Requesting MTA send the email in the Buffer";

    /*
     * Error Messages
     */
    private final static String ERR_MTA_NULL =
            "Session cannot be instantiated with null MTA.";
    private final static String ERR_CRLF_INPUT =
            "Input error - CRLF found in the input.";
    private final static String ERR_NULL_INPUT =
            "Input error - Input was NULL.";
    private final static String ERR_SESSION_CLOSED =
            "Session is closed and cannot be updated.";
    private final static String ERR_INVALID_TRANSITION =
            "Session State transition is invalid.";
    private final static String ERR_STATE_UPDATE =
            "Session State update failed.";
    private final static String ERR_PROGRAM =
            "Program error - Could not transition State from DATA to COMMAND.";
    private final static String ERR_INVALID_REPLY =
            "Invalid Reply - The reply code returned by the MTA is invalid for the command.";

    /**
     * The Session StateTable
     */
    private final StateTable stateTable = new StateTable();

    /**
     * The MTA called by this Session
     */
    private final MTA mta;


    /**
     * Constructor for a {@code MTA} Session.
     * <p>
     * The {@code Session} will use the given {@code MTA} to process a
     * {@code Command} and provide the response.
     * 
     * @param mta
     *            The {@code MTA} that this {@code Session} belongs to.
     * @throws MTAException
     *             The {@code MTA} was null
     */
    public Session(final MTA mta) throws MTAException
    {
        try
        {
            this.mta = requireNonNull(mta);
        }
        catch (Exception e)
        {
            throw new MTAException(ERR_MTA_NULL);
        }

        assert (!mta.isClosed());
    }


    /**
     * Returns the state table for this {@code Session}
     * 
     * @return the state table for this {@code Session}
     */
    public StateTable getStateTable()
    {
        return stateTable;
    }


    /**
     * Process SMTP conversation from the {@code MTA}.
     * <p>
     * Takes the client input provided by the {@code MTA} and processes it
     * according to the {@code SessionState}. This method is synchronized to
     * ensure that {@code SessionState} is updated atomically.
     * 
     * @param line
     *            the client input provided by the {@code MTA}.
     * @return the reply to be output by the {@code MTA} to the client.
     * @throws SmtpLineException
     *             line was {@literal null} or contained a {@literal CRLF}.
     * @throws MTAException
     *             the {@code MTA} could not process the input.
     */
    public final synchronized String process(final String line)
            throws SmtpLineException, MTAException
    {
        /*
         * Validate input
         */
        try
        {
            if (requireNonNull(line).contains(CRLF))
            // lines are implicitly terminated by CRLF. Finding a CRLF in a line indicate that multiple lines are be
            // passed in.
            {
                throw new SmtpLineException(ERR_CRLF_INPUT);
            }
        }
        catch (NullPointerException e)
        // line cannot be null. Empty lines should be the empty string.
        {
            throw new SmtpLineException(ERR_NULL_INPUT);
        }

        String response = null;

        if (SessionState.DATA == stateTable.getState())
        // In DATA State. Process mail data
        {
            response = dataStateProcessor(line);
        }
        else
        // In COMMAND State. Process command
        {
            response = commandStateProcessor(line);
        }

        return response;
    }


    /**
     * Process {@code MTA} input while the {@code Session} is in a
     * {@literal COMMAND} state.
     * <p>
     * Expects and parses SMTP command input, passing the result to the
     * {@code MTA} to decide the return code and description.
     * 
     * @param line
     *            the input line from the {@code MTA}.
     * @return the reply to be output by the {@code MTA} to the client.
     * @throws MTAException
     */
    private final String commandStateProcessor(final String line)
            throws MTAException
    {
        logger.debug(CONST_MSG_PROCESS_CMD, line);

        CommandLine commandLine = parseLine(line); // Parse out the command

        SessionState nextState = null;

        try
        // Identify and validate the next state given the current state and command
        {
            nextState =
                    StateMachine.nextState(stateTable.getState(),
                            requireNonNull(commandLine).getCommand());

            logger.debug(CONST_MSG_STATE_TX, stateTable.getState(), nextState);
        }
        catch (NullPointerException e)
        // No command found
        {
            throw new MTAException(ERR_NULL_INPUT);
        }
        catch (SessionClosedException e1)
        // Session is closed
        {
            throw new MTAException(ERR_SESSION_CLOSED, commandLine, e1);
        }
        catch (SessionStateException e2)
        // Invalid command
        {
            throw new MTAException(ERR_INVALID_TRANSITION, commandLine, e2);
        }

        /*
         * Process the command in the MTA
         */
        logger.debug(CONST_MSG_MTA_CMD, commandLine);
        mta.command(commandLine);

        /*
         * Validate the <em>return code</em> from the MTA is valid for the Command
         */
        if (!commandLine
                .getCommand()
                .validateReplyCode(mta.getReplyCode()))
        /*
         * The reply code provided by the MTA is not valid for the input command.
         */
        {
            throw new MTAException(ERR_INVALID_REPLY, commandLine);
        }

        if (!commandLine.isValid())
        // Command was not valid, do not update the state
        {
            logger.debug(CONST_MSG_STATE_SAME, stateTable.getState());
        }
        else
        // Command was valid, update the state
        {
            try
            /*
             * Update the state table
             */
            {
                logger.debug(CONST_MSG_STATE_UPDATE, stateTable.getState(),
                        nextState);
                stateTable.updateState(nextState);
            }
            catch (SessionStateException e)
            {
                throw new MTAException(ERR_STATE_UPDATE, commandLine);
            }
        }

        String response = mta.getResponse();

        logger.debug(CONST_MSG_RESPONSE, response);

        return response;
    }


    /**
     * Processes {@code MTA} input while the {@code Session} is in the
     * <em>DATA</em> state.
     * <p>
     * Expects and collates email body input until the end-of-data signifier (a
     * containing a single period) is received. When the signifier is received
     * the message is <em>sent</em> from the MTA and the session state moved
     * back to <em>COMMAND</em>.
     * 
     * @param line
     *            the input line from the {@code MTA}.
     * @return the reply to be output by the {@code MTA} to the client.
     * @throws MTAException
     *             generated by the {@code MTA} on send.
     */
    private final String dataStateProcessor(final String line)
            throws MTAException
    {
        logger.debug(CONST_MSG_PROCESS_DATA, line);

        if (!".".equals(line))
        /*
         * The end of the mail body was not signaled. Input is a line of mail body to append for processing
         */
        {
            mta.getBuffer().appendMailData(line);
            return "";
        }

        /*
         * The end of the mail body was signaled. Ask the MTA to <em>send</em> and return the MTA's reply.
         */
        logger.debug(CONST_MSG_SEND);
        mta.send();

        try
        // Update State from DATA to COMMAND
        {
            stateTable.updateState(SessionState.COMMAND);
            logger.debug(CONST_MSG_STATE_UPDATE, stateTable.getState(),
                    SessionState.COMMAND);
        }
        catch (SessionStateException e)
        {
            throw new MTAException(ERR_PROGRAM, e);
        }

        return mta.getResponse();
    }
}
