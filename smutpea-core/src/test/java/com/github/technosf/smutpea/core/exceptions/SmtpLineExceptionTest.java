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

package com.github.technosf.smutpea.core.exceptions;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.testng.annotations.Test;

/**
 * Unit test for {@code SMTPLineException}
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 * 
 */
public class SmtpLineExceptionTest
{
	/**
	 * 
	 */
	@Test
	public void SMTPLineExceptionString()
	{
		String string = "abc";
		SmtpLineException x = new SmtpLineException(string);
		assertEquals(x.getMessage(), string);
		assertNull(x.getCause());
	}


	/**
	 * 
	 */
	@Test
	public void SMTPLineExceptionStringThrowable()
	{
		String string = "abc";
		Exception z = new Exception();
		SmtpLineException x = new SmtpLineException(string, z);
		assertEquals(x.getMessage(), string);
		assertEquals(x.getCause(), z);
	}
}
