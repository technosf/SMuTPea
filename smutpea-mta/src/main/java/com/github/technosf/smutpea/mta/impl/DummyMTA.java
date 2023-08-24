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
import com.github.technosf.smutpea.core.rfc.Command.CommandLine;
import com.github.technosf.smutpea.core.rfc.ReplyCode;
import com.github.technosf.smutpea.mta.AbstractMTA;

/**
 * {@code DummyMTA} is a basic MTA that refuses connections, per RFC7504.
 * <p>
 * {@code RefuseMTA} returns a <em>521 Server does not accept mail</em> message on
 * Connect.
 * 
 * @author technosf
 * @since 0.0.5
 * @version 0.0.5
 */
public final class DummyMTA
    extends AbstractMTA
{

    /**
    * 
    */
    private final static String CONST_MTA_NAME = "DummyMTA v1.0.0";


    /**
     * Constructor
     * 
     * @param mtaDomain
     * @throws MTAException
     */
    public DummyMTA(final String domain) throws MTAException
    {
        super(CONST_MTA_NAME, domain);
    }


    @Override
    public void connect()
    {
        setResponse(ReplyCode._521);
        close();
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
