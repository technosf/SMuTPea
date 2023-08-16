/*
 * Copyright 2014 technosf [https://github.com/technosf]
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
package com.github.technosf.smutpea.server;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public abstract class AbstractSocketServer 
extends AbstractServer
{
	private static final Logger logger = LoggerFactory.getLogger(AbstractSocketServer.class);

	/*
	 * Constants
	 */
	private static final String CONST_ERR_SOCKET_SERVE = "Serving socket...";
	private static final String CONST_ERR_SOCKET_CLOSE = "Exception closing socket";

	protected final Socket socket;

	/**
	 * @param socket
	 * @param mta
	 * @throws IOException
	 */
	protected AbstractSocketServer(Socket socket) throws IOException
	{
		super(socket.getInputStream(), socket.getOutputStream());
		this.socket = socket;
		MDC.put("Local InetAddress", socket.getLocalAddress().getHostAddress());
		MDC.put("Local Port", Integer.toString(socket.getLocalPort()));
		logger.info(CONST_ERR_SOCKET_SERVE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.technosf.smutpea.server.Server#cleanup()
	 */
	@Override
    public void cleanup()
	{
		if (!socket.isClosed())
		{
			try
			{
				socket.close();
			}
			catch (IOException e)
			{
				logger.error(CONST_ERR_SOCKET_CLOSE, e);
			}
		}

		MDC.remove("Local InetAddress");
		MDC.remove("Local Port");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.technosf.smutpea.server.Server#getServerId()
	 */
    @Override
    public String getServerId() {
        return String.format("%1$s:%2$s", socket.getInetAddress(), socket.getPort());
    }
}
