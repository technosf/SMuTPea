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

import static java.util.Objects.requireNonNull;

import com.github.technosf.smutpea.core.Buffer;
import com.github.technosf.smutpea.core.rfc2821.Session;

/**
 * Basic Buffer, per RFC2821 Section 2.3.6
 * <p>
 * Email message buffer per RFC2821 Section 2.3.6. Contains the email message,
 * the forward and reverse paths.
 * 
 * @see http://tools.ietf.org/html/rfc2821#section-2.3.6
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public final class BasicBuffer implements Buffer
{

    /**
     * The email data - the message
     */
    private StringBuffer mailData = new StringBuffer();

    /**
     * The email forward path
     */
    private String forwardPath = "";

    /**
     * The email reverse path
     */
    private String reversePath = "";


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#appendMailData(java.lang.String)
     */
    public void appendMailData(String mailData) throws NullPointerException
    {
        this.mailData.append(requireNonNull(mailData)).append(Session.CRLF);
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#setForwardPath(java.lang.String)
     */
    public void setForwardPath(String forwardPath) throws NullPointerException
    {
        this.forwardPath = requireNonNull(forwardPath);
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#setReversePath(java.lang.String)
     */
    public void setReversePath(String reversePath) throws NullPointerException
    {
        this.reversePath = requireNonNull(reversePath);
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#getMailData()
     */
    public String getMailData()
    {
        return mailData.toString().trim();
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#getForwardPath()
     */
    public String getForwardPath()
    {
        return forwardPath;
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#getReversePath()
     */
    public String getReversePath()
    {
        return reversePath;
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#clear()
     */
    public void clear()
    {
        clearMailData();
        clearForwardPath();
        clearReversePath();
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#clearMailData()
     */
    public void clearMailData()
    {
        mailData = new StringBuffer();
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#clearForwardPath()
     */
    public void clearForwardPath()
    {
        forwardPath = "";
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#clearReversePath()
     */
    public void clearReversePath()
    {
        reversePath = "";
    };
}
