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

import com.github.technosf.smutpea.core.Buffer;
import com.github.technosf.smutpea.core.exceptions.MTAException;
import com.github.technosf.smutpea.core.rfc.ReplyCode;
import com.github.technosf.smutpea.core.rfc.Command.CommandLine;
import com.github.technosf.smutpea.mta.AbstractMTA;

/**
 * {@code RefuseMTA} is a basic MTA that refuses to send email.
 * <p>
 * {@code RefuseMTA} returns a <em>554 No SMTP service here</em> message on
 * HELO/EHLO.
 * 
 * @author technosf
 * @since 0.0.2
 * @version 0.0.5
 */
public final class RefuseMTA
        extends AbstractMTA
{
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
        setResponse(ReplyCode._421);
        close();    // Close connection after sending response
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
        setResponse(ReplyCode._421);
        close();    // Close connection after sending response
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.impl.AbstractMTA#sendMessage(java.lang.String)
     */
    @Override
    protected final void sendMessage(final String message)
    {
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.MTA#getBuffer()
     */
    @Override
    public Buffer getBuffer()
    {
        return null;
    }

}
