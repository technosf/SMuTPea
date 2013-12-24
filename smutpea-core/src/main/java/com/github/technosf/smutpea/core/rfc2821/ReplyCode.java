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

package com.github.technosf.smutpea.core.rfc2821;

import java.util.HashMap;
import java.util.Map;

/**
 * Server Reply Codes.
 * <p>
 * RFC2821 SMTP Server Reply Codes, per RFC2821 Section 4.2
 * 
 * <pre>
 * Code| Action / Response  | Error Code | Description
 * ----|-----------------------------------------------------------------------------------------------------
 * 211 |                    | 503        | System status, or system help reply
 * 214 |                    | 503        | Help message
 * 220 | HELO/250, EHLO/250 | 503        | <domain> Service ready
 * 221 |                    | 503        | <domain> Service closing transmission channel
 * 250 |                    | 503        | Requested mail action okay, completed
 * 251 |                    | 503        | User not local; will forward to <forward-path>
 * 252 |                    | 503        | Cannot VRFY user, but will accept message and attempt delivery
 * 354 |                    | 503        | Start mail input; end with <CRLF>.<CRLF>
 * 421 |                    | 503        | <domain> Service not available, closing transmission channel
 * 450 |                    | 503        | Requested mail action not taken: mailbox unavailable
 * 451 |                    | 503        | Requested action aborted: local error in processing
 * 452 |                    | 503        | Requested action not taken: insufficient system storage
 * 500 |                    | 503        | Syntax error, command unrecognized
 * 501 |                    | 503        | Syntax error in parameters or arguments
 * 502 |                    | 503        | Command not implemented
 * 503 |                    | 503        | Bad sequence of commands
 * 504 |                    | 503        | Command parameter not implemented
 * 550 |                    | 503        | Requested action not taken: mailbox unavailable
 * 551 |                    | 503        | User not local; please try <forward-path>
 * 552 |                    | 503        | Requested mail action aborted: exceeded storage allocation
 * 553 |                    | 503        | Requested action not taken: mailbox name not allowed
 * 554 |                    | 503        | Transaction failed (Or, in the case of a connection-opening response, "No SMTP service here")
 * </pre>
 * 
 * @see http://tools.ietf.org/html/rfc2821#section-4.2
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public enum ReplyCode
	{
		/*
		 * The reply codes and default descriptions.
		 */
		_211(211, "System status, or system help reply"),
		_214(214, "Help message"),
		_220(220, "<domain> Service ready"),
		_221(221, "<domain> Service closing transmission channel"),
		_250(250, "Requested mail action okay, completed"),
		_251(251, "User not local; will forward to <forward-path>"),
		_252(252, "Cannot VRFY user, but will accept message and attempt delivery"),
		_354(354, "Start mail input; end with <CRLF>.<CRLF>"),
		_421(421, "<domain> Service not available, closing transmission channel"),
		_450(450, "Requested mail action not taken: mailbox unavailable"),
		_451(451, "Requested action aborted: local error in processing"),
		_452(452, "Requested action not taken: insufficient system storage"),
		_500(500, "Syntax error, command unrecognized"),
		_501(501, "Syntax error in parameters or arguments"),
		_502(502, "Command not implemented"),
		_503(503, "Bad sequence of commands"),
		_504(504, "Command parameter not implemented"),
		_550(550, "Requested action not taken: mailbox unavailable"),
		_551(551, "User not local; please try <forward-path>"),
		_552(552, "Requested mail action aborted: exceeded storage allocation"),
		_553(553, "Requested action not taken: mailbox name not allowed"),
		_554(554, "Transaction failed");

		/**
		 * Description formatter
		 */
		private static final String FORMAT_DESCRIPTION = "%1$s (%2$s)";

		/**
		 * Index of codes
		 */
		private static final Map<Integer, ReplyCode> codeIndex =
						new HashMap<Integer, ReplyCode>();

		/** Response code */
		private Integer code;

		/** Response description. */
		private String description;

		static
		// Populate the code index
		{
			for (ReplyCode replyCode : ReplyCode.values())
			{
				codeIndex.put(replyCode.code, replyCode);
			}
		}


		/**
		 * Constructor.
		 * 
		 * @param code
		 *            response code
		 * @param message
		 *            response message
		 */
		ReplyCode(int code, String description)
		{
			this.code = code;
			this.description = description;
		}


		/**
		 * Returns the {@code ReplyCode} for the given integer code.
		 * 
		 * @param code
		 *            the code to look-up
		 * @return the corresponding ReplyCode, or {@literal null} if none found.
		 */
		public static ReplyCode findReplyCode(int code)
		{
			return codeIndex.get(code);
		}


		/**
		 * Returns the SMTP code
		 * 
		 * @return the integer SMTP code
		 */
		public int getCode()
		{
			return code;
		}


		/**
		 * Returns the code description
		 * 
		 * @return the code description
		 */
		public String getDescription()
		{
			return description;
		}


		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return String.format(FORMAT_DESCRIPTION, code, description);
		}

	}
