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

/**
 * SMTP conversation session states
 * <p>
 * {@code SessionState} is a synthesis of <em>The SMTP Procedures</em>
 * 
 * @see http://tools.ietf.org/html/rfc2821#section-3
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public enum SessionState
	{
		/** CONNECT state: waiting for a Hello. */
		CONNECT,

		/** COMMAND state: waiting for a command. */
		COMMAND,

		/** RCPT state: waiting for a RCPT &lt;email address&gt; command. */
		RCPT,

		/** Waiting for data. */
		DATA,

		/** End of client transmission. */
		CLOSED;

	}
