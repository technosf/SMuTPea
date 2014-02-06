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

package com.github.technosf.smutpea.core;

/**
 * Buffer, per RFC2821 Section 2.3.6
 * <p>
 * Defines the operations of email message buffer per RFC2821 Section 2.3.6.
 * 
 * @see http://tools.ietf.org/html/rfc2821#section-2.3.6
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public interface Buffer
{
    /**
     * Appends to the mail data
     * 
     * @param mailData
     *            the mail data to append
     * @throws NullPointerException
     *             mail data cannot be {@literal null}
     */
    void appendMailData(String mailData) throws NullPointerException;


    /**
     * Sets the forward path
     * 
     * @param forwardPath
     *            the forward path
     * @throws NullPointerException
     *             the forward path cannot be {@literal null}
     */
    void setForwardPath(String forwardPath) throws NullPointerException;


    /**
     * Sets the reverse path
     * 
     * @param reversePath
     *            the reverse path
     * @throws NullPointerException
     *             reverse path cannot be {@literal null}
     */
    void setReversePath(String reversePath) throws NullPointerException;


    /**
     * Return the mail data
     * 
     * @return the mail data
     */
    String getMailData();


    /**
     * Returns the forward path
     * 
     * @return the forward path
     */
    String getForwardPath();


    /**
     * Returns the reverse path
     * 
     * @return the reverse path
     */
    String getReversePath();


    /**
     * Clear the mail data, forward and reverse paths
     */
    void clear();


    /**
     * Clear the mail data
     */
    void clearMailData();


    /**
     * Clear the forward path
     */
    void clearForwardPath();


    /**
     * Clear the reverse path
     */
    void clearReversePath();
}
