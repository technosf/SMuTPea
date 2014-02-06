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

import com.github.technosf.smutpea.core.Buffer;

/**
 * A Non Operative Buffer, per RFC2821 Section 2.3.6 that stores and returns no
 * information
 * <p>
 * Email message buffer per RFC2821 Section 2.3.6. Contains the email message,
 * the forward and reverse paths.
 * 
 * @see http://tools.ietf.org/html/rfc2821#section-2.3.6
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public final class NullBuffer implements Buffer
{

    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#appendMailData(java.lang.String)
     */
    public void appendMailData(String mailData) throws NullPointerException
    {
        //NOOP
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#setForwardPath(java.lang.String)
     */
    public void setForwardPath(String forwardPath) throws NullPointerException
    {
        //NOOP
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#setReversePath(java.lang.String)
     */
    public void setReversePath(String reversePath) throws NullPointerException
    {
        //NOOP
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#getMailData()
     */
    public String getMailData()
    {
        return ""; //NOOP
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#getForwardPath()
     */
    public String getForwardPath()
    {
        return ""; //NOOP
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#getReversePath()
     */
    public String getReversePath()
    {
        return ""; //NOOP
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#clear()
     */
    public void clear()
    {
        //NOOP
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#clearMailData()
     */
    public void clearMailData()
    {
        //NOOP
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#clearForwardPath()
     */
    public void clearForwardPath()
    {
        //NOOP
    };


    /**
     * {@inheritDoc}
     * 
     * @see com.github.technosf.smutpea.core.Buffer#clearReversePath()
     */
    public void clearReversePath()
    {
        //NOOP
    };
}
