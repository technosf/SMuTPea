/*
 * Copyright 2015 technosf [https://github.com/technosf]
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.technosf.smutpea.core.Buffer;
import com.github.technosf.smutpea.core.MTA;
import com.github.technosf.smutpea.core.exceptions.MTAException;
import com.github.technosf.smutpea.core.rfc2821.Command.CommandLine;
import com.github.technosf.smutpea.core.rfc2821.ReplyCode;
import com.github.technosf.smutpea.mta.AbstractMTA;

/**
 * {@code RefuseMTA} is a basic MTA that refuses to send email.
 * <p>
 * {@code RefuseMTA} returns a <em>554 No SMTP service here</em> message on
 * HELO/EHLO.
 * 
 * @author technosf
 * @since 0.0.2
 * @version 0.0.2
 */
public final class RefuseMTA
        extends AbstractMTA
        implements MTA
{
    private static final Logger logger =
            LoggerFactory.getLogger(RefuseMTA.class);

    /*
     * Constants
     */
    //    private static final String CONST_MSG_VALID_PROCESSED =
    //            "Valid command processed:[{}] giving code:[{}]";
    //    private static final String CONST_MSG_INVALID_PROCESSED =
    //            "Invalid command processed:[{}] giving code:[{}]";
    private static final String CONST_MSG_SENT =
            "Mail sent to /dev/null with code:[{}]";

    /**
     * Use a {@code NullBuffer} as no EMail will be sent
     */
    private final Buffer buffer = new NullBuffer();

    /**
    * 
    */
    private final static String CONST_MTA_NAME = "RefuseMTA v1.0.0";


    /**
     * Constructor
     * 
     * @param mtaDomain
     * @throws MTAException
     */
    public RefuseMTA(final String domain) throws MTAException
    {
        super(CONST_MTA_NAME, domain);
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.impl.AbstractMTA#processValidCommand(com.github.technosf.smutpea.core.rfc2821.Command.CommandLine)
     */
    @Override
    protected void processValidCommand(CommandLine commandLine)
            throws MTAException
    {
        setResponse(ReplyCode._554);
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.impl.AbstractMTA#processInvalidCommand(com.github.technosf.smutpea.core.rfc2821.Command.CommandLine)
     */
    @Override
    protected void processInvalidCommand(CommandLine commandLine)
            throws MTAException
    {
        setResponse(ReplyCode._554);
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.impl.AbstractMTA#sendMessage(java.lang.String)
     */
    @Override
    protected final void sendMessage(final String message)
    {
        // Do nothing.
        setResponse(ReplyCode._554,
                String.format("%1$s %2$s", ReplyCode._554.getCode(), // TODO validate
                        "No SMTP service here"));
        logger.info(CONST_MSG_SENT, getReplyCode().getCode());
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.MTA#getBuffer()
     */
    @Override
    public Buffer getBuffer()
    {
        return buffer;
    }

}
