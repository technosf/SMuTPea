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
 * {@code SinkMTA} is a basic MTA that dumps all email passed to it for transfer.
 * <p>
 * Although {@code SinkMTA} is a valid MTA, no validation of destinations, hosts or email addresses is done - everything
 * is accepted so that to the client, everything looks OK unless the client violates the RFC.
 * <p>
 * {@code SinkMTA} uses {@code NullBuffer} and the mail <em>Buffer</em>, so no data sent to the MTA is retrievable.
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public final class SinkMTA
				extends AbstractMTA
				implements MTA
{
	private static final Logger logger = LoggerFactory.getLogger(SinkMTA.class);

	/*
	 * Constants
	 */
	private static final String CONST_MSG_VALID_PROCESSED =
					"Valid command processed:[{}] giving code:[{}]";
	private static final String CONST_MSG_INVALID_PROCESSED =
					"Invalid command processed:[{}] giving code:[{}]";
	private static final String CONST_MSG_SENT =
					"Mail sent to /dev/null with code:[{}]";

	/**
	 * Use a {@code NullBuffer} as no EMail will be sent
	 */
	private final Buffer buffer = new NullBuffer();

	/**
     * 
     */
	private final static String CONST_MTA_NAME = "SinkMTA v1.0.0";


	/**
	 * Constructor
	 * 
	 * @param mtaDomain
	 * @throws MTAException
	 */
	public SinkMTA(final String domain) throws MTAException
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
		ReplyCode replyCode;

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

		logger.debug(CONST_MSG_VALID_PROCESSED, commandLine.getCommand(),
						replyCode);

		setResponse(replyCode);
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
		ReplyCode replyCode = ReplyCode._503;

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

		logger.debug(CONST_MSG_INVALID_PROCESSED, commandLine.getCommand(),
						replyCode);

		setResponse(replyCode);
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
		setResponse(ReplyCode._250,
						String.format("%1$s %2$s", ReplyCode._250.getCode(), // TODO validate
										"Mail sent to /dev/null"));
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
