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

package com.github.technosf.smutpea.server;

import com.github.technosf.smutpea.core.MTA;

/**
 * Server
 * <p>
 * Basic server function definitions
 * 
 * @author technosf
 * @since 0.0.5
 * @version 0.0.5
 */
public interface Server 
{

    /**
     * Provides unique identifying info on this server instance,
     * for example hostname, and or interface or port etc.
     * 
     * @return the identifier
     */
    String getServerId();


    /**
     * Provides unique identifying info on the client connection,
     * for example hostname, and or interface or port etc.
     * 
     * @return the identifier
     */
   // String getClientId();


    /**
     * Provides an MTA for this instance
     * 
     * @return the MTA
     */
    MTA getMTA();


    /**
     * Clean up on MTA close.
     */
    void cleanup();


}