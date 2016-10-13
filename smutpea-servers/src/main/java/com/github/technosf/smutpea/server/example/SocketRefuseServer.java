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

package com.github.technosf.smutpea.server.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.technosf.smutpea.core.MTA;
import com.github.technosf.smutpea.core.exceptions.MTAException;
import com.github.technosf.smutpea.mta.impl.RefuseMTA;
import com.github.technosf.smutpea.server.AbstractSocketServer;

/**
 * SocketRefuseServer
 * <p>
 * A socket server based MTA that refuses to send email with a
 * <em>554 No SMTP service here</em> message on HELO/EHLO
 * 
 * @author technosf
 * @since 0.0.2
 * @version 0.0.2
 */
public final class SocketRefuseServer
        extends AbstractSocketServer
        implements Runnable
{

    private static final Logger logger = LoggerFactory
            .getLogger(SocketRefuseServer.class);

    private static final ExecutorService executorService = Executors
            .newCachedThreadPool();

    /*
     * Constants
     */
    private static final String CONST_SERVER_NAME =
            "smutpea SocketSinkServer[%1$s]";
    private static final String CONST_MSG_MAIN_START = "Main starting.";
    private static final String CONST_MSG_MAIN_END = "Main ending.";
    private static final String CONST_MSG_MTA_OPEN = "MTA connection opening.";
    private static final String CONST_MSG_MTA_CLOSE = "MTA connection closed.";
    private static final String CONST_MSG_SERVER_START =
            "Starting server on port:[{}]";
    private static final String CONST_ERR_MTA_ERR =
            "MTA cannot be instantiated.";
    private static final String CONST_ERR_SERVER_REQ =
            "Unable to process client request.";

    private static final Integer CONST_DEFAULT_SMTP_PORT = 25;

    private static Set<Integer> ports = new HashSet<Integer>();
    private static Map<Integer, Runnable> servers =
            new HashMap<Integer, Runnable>();


    /**
     * Run Socket-connected SinkMTA's
     * 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        logger.info(CONST_MSG_MAIN_START);

        /*
         * Marshal params
         */
        for (String arg : args)
        {
            ports.add(Integer.valueOf(arg));
        }

        if (ports.isEmpty())
        {
            ports.add(CONST_DEFAULT_SMTP_PORT);
        }

        /*
         * Create socket servers on ports
         */

        for (int port : ports)
        {
            servers.put(port, createConnectionListener(port));
        }

        logger.info(CONST_MSG_MAIN_END);

    } // public static void main(String[] args)


    /**
     * Creates a connection listening service on a given port, spawning MTA
     * servers as needed.
     * 
     * @param port
     *            the port to listen on
     * @return the runnable that will handle each incoming connection
     */
    private static Runnable createConnectionListener(final int port)
    {
        Runnable serverTask = new Runnable()
        {
            @Override
            public void run()
            {
                logger.info(CONST_MSG_SERVER_START, port);

                try (ServerSocket serverSocket = new ServerSocket(port))
                {
                    while (true)
                    {
                        Socket clientSocket = serverSocket.accept();
                        executorService.submit(new SocketRefuseServer(
                                clientSocket));
                    }
                }
                catch (IOException e)
                {
                    logger.error(CONST_ERR_SERVER_REQ, e);
                }
            }
        };

        Thread serverThread =
                new Thread(serverTask, String.format(CONST_SERVER_NAME, port));
        serverThread.start();
        return serverTask;
    }


    /**
     * Constructor creating a Sink MTA server for the given socket.
     * 
     * @param socket
     *            the socket
     * @throws IOException
     */
    public SocketRefuseServer(Socket socket) throws IOException
    {
        super(socket);
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.github.technosf.smutpea.server.AbstractServer#close()
     */
    @Override
    protected void close()
    {
        super.close();
        logger.info(CONST_MSG_MTA_CLOSE);
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.github.technosf.smutpea.server.AbstractServer#getMTA()
     */
    @Override
    protected MTA getMTA()
    {
        try
        {
            return new RefuseMTA("local.refuse.server");
        }
        catch (MTAException e)
        {
            logger.error(CONST_ERR_MTA_ERR, e);
        }

        return null;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run()
    {
        logger.info(CONST_MSG_MTA_OPEN);
        open(); // Open the connection
    }

}
