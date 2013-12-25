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

import com.github.technosf.smutpea.core.MTA;
import com.github.technosf.smutpea.core.exceptions.MTAException;
import com.github.technosf.smutpea.core.rfc2821.Command.CommandLine;
import com.github.technosf.smutpea.core.rfc2821.ReplyCode;


/**
 * {@code SinkMTA} is a basic MTA that dumps all email passed to it for transfer.
 * <p>
 * Although {@code SinkMTA} is a valid MTA, no validation of destinations, hosts or email addresses is done - everything
 * is accepted so that to the client, everything looks OK unless the client violates the RFC.
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public final class SinkMTA
				extends AbstractMTA
				implements MTA
{

	/**
     * 
     */
	private final static String CONST_MTA_NAME = "SinkMTA v1.0.0";

	/**
	 * Used as a guard.
	 */
	private final MTA mta;

	/**
	 * Reply code
	 */
	private ReplyCode replyCode;


	/**
	 * Constructor
	 * 
	 * @param mtaDomain
	 * @throws MTAException
	 */
	public SinkMTA(final String domain) throws MTAException
	{
		super(CONST_MTA_NAME, domain);
		mta = this;
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
		synchronized (mta)
		{
			switch (commandLine.getCommand())
			{
				case DATA:
					replyCode = ReplyCode._354;
					break;
				case HELP:
					replyCode = ReplyCode._214;
					break;
				case QUIT:
					replyCode = ReplyCode._221;
					break;
				case RSET:
				case VRFY:
				case EXPN:
				case NOOP:
				case EHLO:
				case HELO:
				case MAIL:
				case RCPT:
				default:
					replyCode = ReplyCode._250;
					break;
			}

			setResponse(replyCode);
		}
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
		synchronized (mta)
		{
			replyCode = ReplyCode._503;

			if (commandLine != null
							&& commandLine.getCommand() != null)
			{
				switch (commandLine.getCommand())
				{
					case RSET:
						replyCode = ReplyCode._501;
						break;
					case VRFY:
						replyCode = ReplyCode._501;
						break;
					case EHLO:
					case HELO:
					case MAIL:
					case RCPT:
					case DATA:
					case EXPN:
					case HELP:
					case NOOP:
					case QUIT:
					default:
						replyCode = ReplyCode._503;
						break;
				}
			}

			setResponse(replyCode);
		}
	}


	/**
	 * {@inheritDoc}
	 * 
	 * @see com.github.technosf.smutpea.core.impl.AbstractMTA#sendMessage(java.lang.String)
	 */
	@Override
	protected final void sendMessage(final String message)
	{
		synchronized (mta)
		{
			// Do nothing.
			System.out.println("Ate a message.");
			replyCode = ReplyCode._250;
			setResponse(String.format("%1$s %2$s", getReplyCode().getCode(),
							"Mail sent to /dev/null"));
		}
	}


	/**
	 * {@inheritDoc}
	 * 
	 * @see com.github.technosf.smutpea.core.MTA#getReplyCode()
	 */
	@Override
	public final ReplyCode getReplyCode()
	{
		synchronized (mta)
		{
			return replyCode;
		}
	}
}
