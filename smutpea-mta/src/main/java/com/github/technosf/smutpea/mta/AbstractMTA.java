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

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.technosf.smutpea.core.MTA;
import com.github.technosf.smutpea.core.exceptions.MTAException;
import com.github.technosf.smutpea.core.exceptions.SessionStateException;
import com.github.technosf.smutpea.core.exceptions.SmtpLineException;
import com.github.technosf.smutpea.core.rfc.ReplyCode;
import com.github.technosf.smutpea.core.rfc.Session;
import com.github.technosf.smutpea.core.rfc.SessionState;
import com.github.technosf.smutpea.core.rfc.Command.CommandLine;

/**
 * An Abstract {@code MTA}
 * <p>
 * Handles the housekeeping leaving key logic to the implementing {@code MTA}
 * for:
 * <ul>
 * <li>Processing valid commands passed to the MTA
 * <li>Processing invalid commands passed to the MTA
 * <li>Processing the sending of mail buffered by the MTA
 * </ul>
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.6
 */
public abstract class AbstractMTA 
implements MTA
{
    private static final Logger logger = LoggerFactory
            .getLogger(AbstractMTA.class);

    /*
     * Constants
     */
    private static final String CONST_ERR_TIMEOUT = "Error: timeout exceeded";

    private static final String CONST_MSG_PROCESS = "Client:[{}]";
    private static final String CONST_MSG_NO_CMD = "No command found";
    private static final String CONST_MSG_INVLD_SESS_STATE =
            "Invalid session state:[{}] for command:[{}s]";
    private static final String CONST_MSG_UNKNOWN_ERR =
            "Propagating unknown error:[{}]";
    private static final String CONST_MSG_CLOSE_ERR =
            "Exception closing session";

    /**
     * Command response format - code, message, extra
     */
    protected static final String CONST_FMT_CMD_RESPONSE = "%1$s %2$s %3$s";

    /**
     * The MTA name
     */
    private final String mtaName;

    /**
     * The domain the MTA is answering for
     */
    private final String mtaDomain;

    /**
     * The Session this MTA wraps
     */
    protected final Session session;

    /**
     * The current/last response
     */
    private String response;

    /**
     * The current/last Reply Code
     */
    private ReplyCode replyCode;


    /**
     * The approximate time the MTA has been idle
     * waiting for Client input
     */
    protected long clientIdleTimeMillis;


    /**
     * Constructor setting the domain and mta name.
     * 
     * @param mtaName
     *            The MTA name
     * @param mtaDomain
     *            The domain the MTA is answering for
     * @throws MTAException
     *             Session creation failed.
     */
    protected AbstractMTA(final String mtaName, final String mtaDomain)
            throws MTAException
    {
        this.mtaName = mtaName;
        this.mtaDomain = mtaDomain;
        this.session = new Session(this);
    }


    /*
     * ------------------------------------------------------------------------
     * 
     * Abstract methods
     * 
     * ------------------------------------------------------------------------
     */

    /**
     * <em>Process</em> the given valid command.
     * <p>
     * The implementing {@code MTA} will define how the valid
     * {@code CommandLine} is processed.
     * 
     * @param commandLine
     *            the {@code CommandLine} to process
     * @throws MTAException
     */
    protected abstract void processValidCommand(final CommandLine commandLine)
            throws MTAException;


    /**
     * <em>Process</em> the given invalid command.
     * <p>
     * The implementing {@code MTA} will define how the invalid
     * {@code CommandLine} is processed.
     * 
     * @param commandLine
     *            the invalid {@code CommandLine} to process
     * @throws MTAException
     */
    protected abstract void processInvalidCommand(final CommandLine commandLine)
            throws MTAException;


    /**
     * <em>Send</em> the given message.
     * <p>
     * The implementing {@code MTA} will define how the message is processed.
     * 
     * @param message
     *            the message to send
     */
    protected abstract void sendMessage(final String message);



    /*
     * ------------------------------------------------------------------------
     * 
     * AbstractMTA methods
     * 
     * ------------------------------------------------------------------------
     */

    /**
     * Check the client idle time against timeouts for the current State
     * <p>
     * Override as needed
     * 
     * @param milliseconds the amount of idle time in milliseconds
     * @return true if the client timed out
     */
    protected boolean checkClientTimeout(long milliseconds)
    {
        if  (
                ( milliseconds < TIMEOUT_SERVER*1000 ) 
            &&
                (       ( SessionState.DATA != session.getStateTable().getState() ) 
                    ||  ( milliseconds < TIMEOUT_DATABLOCK*1000 )
                )
            ) return false;
        
        setResponse(ReplyCode._421, "421 " + CONST_ERR_TIMEOUT);
        close();

        return true;
    }


    /**
     * Sets the MTA response description
     * 
     * @param response
     *            the response description
     */
    private final void setResponseDescription(String response)
    {
        this.response = response;
    }


    /**
     * Sets the MTA ReplyCode and response
     * 
     * @param response
     *            the response
     */
    protected final void setResponse(ReplyCode replyCode, String response)
    {
        this.replyCode = replyCode;
        this.response = response;
    }


    /**
     * Sets the MTA ReplyCode and response
     * 
     * @param code
     *            the response code
     */
    protected final void setResponse(ReplyCode replyCode)
    {
        setResponse(replyCode,
                String.format(CONST_FMT_CMD_RESPONSE, replyCode.getCode(),
                        replyCode.getDescription(), ""));
    }


    /*
     * ------------------------------------------------------------------------
     * 
     * MTA methods
     * 
     * ------------------------------------------------------------------------
     */

    /**
     * {@inheritDoc}
     * 
     * Default "220" implementation. Can be overriden.
     * 
     * @see com.github.technosf.smutpea.core.MTA#connect(java.lang.String)
     */
    @Override
    public void connect()
    {
        setResponse(ReplyCode._220, String.format(CONST_FMT_CMD_RESPONSE,
                ReplyCode._220.getCode(),
                getMTADomain(), getMTAName(), getMTADateTime()));
    }


    /**
     * {@inheritDoc}
     * 
     * @throws MTAException
     * @see com.github.technosf.smutpea.core.MTA#processLine(java.lang.String)
     */
    @Override
    public final void processInputLine(String line) throws MTAException
    {
        logger.debug(CONST_MSG_PROCESS, line);

        try
        {
            setResponseDescription(session.process(line));
        }
        catch (MTAException e)
        // Could not process the line
        {
            if (e.getCommandLine() == null)
            // No command found
            {
                logger.debug(CONST_MSG_NO_CMD);

                setResponse(ReplyCode._500); // Syntax error, command unrecognized

                return;
            }
            else if (SessionStateException.class.isInstance(e.getCause())
                    && e.getCommandLine() != null)
            /*
             * Command found, but invalid for the current SessionState Send it to the invalid command processor to set
             * the response
             */
            {
                logger.debug(CONST_MSG_INVLD_SESS_STATE,
                        session.getStateTable().getState(),
                        e.getCommandLine());

                processInvalidCommand(e.getCommandLine());

                return;
            }
            else
            // Not a well defined MTA exception, so propagate it up
            {
                logger.debug(CONST_MSG_UNKNOWN_ERR, e.getMessage());
                throw e;
            }
        }
        catch (SmtpLineException e)
        {
            logger.debug(e.getMessage());
        }
    } // public final void processLine(String line) throws MTAException


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.MTA#isClosed()
     */
    @Override
    public final boolean isClosed()
    {
        return session != null
                && SessionState.CLOSED == session.getStateTable().getState();
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.MTA#command(com.github.technosf.smutpea.core.rfc.Command.CommandLine)
     */
    @Override
    public final void command(final CommandLine commandLine)
            throws MTAException
    {
        if (commandLine.isValid())
        // Valid command processor
        {
            processValidCommand(commandLine);
        }
        else
        // Invalid command processor
        {
            processInvalidCommand(commandLine);
        }
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.MTA#send()
     */
    @Override
    public final void send() throws MTAException
    {
        sendMessage(getBuffer().toString());
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.MTA#resetClientIdle()
     */
    public final void resetClientIdle()
    {
        clientIdleTimeMillis = 0;
    }

    
    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.MTA#updateClientIdle()
     */
    public final boolean updateClientIdle(long milliseconds)
    {
        return checkClientTimeout(clientIdleTimeMillis += milliseconds);
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.MTA#getMTADomain()
     */
    @Override
    public final String getMTADomain()
    {
        return mtaDomain;
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.MTA#getMTAName()
     */
    @Override
    public final String getMTAName()
    {
        return mtaName;
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.MTA#getMTADateTime()
     */
    @Override
    public final String getMTADateTime()
    {
        return new Date().toString();
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.MTA#getResponse()
     */
    @Override
    public final String getResponse()
    {
        return response;
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.MTA#getReplyCode()
     */
    @Override
    public final ReplyCode getReplyCode()
    {
        return replyCode;
    }

    
    /**
     * {@inheritDoc}
     * 
     * Override as needed
     * 
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close()
    {
        try 
        {
            session.close();
        }
        catch (SessionStateException e)
        {
            logger.debug(CONST_MSG_CLOSE_ERR, e.getMessage());
        }
    }
}
