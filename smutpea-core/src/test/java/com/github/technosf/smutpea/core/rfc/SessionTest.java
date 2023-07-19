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

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.util.Date;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.technosf.smutpea.core.MTA;
import com.github.technosf.smutpea.core.exceptions.MTAException;
import com.github.technosf.smutpea.core.exceptions.SmtpLineException;


import com.github.technosf.smutpea.core.rfc.Command.CommandLine;

/**
 * Unit test for {@code Session}
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.5
 * 
 */
public class SessionTest
{

	private final MTA mta = createNiceMock(MTA.class);

	private Session classUnderTest = null;

	/**
	 * Train the Mock MTA and create the CUT prior to Test
	 * 
	 * @throws MTAException
	 */
	@BeforeMethod
	public void beforeMethod() throws MTAException
	{
		reset(mta);

		mta.connect();
		expectLastCall().once();
		expect(mta.getMTADomain()).andReturn("mock.mta.domain").anyTimes();
		expect(mta.getMTAName()).andReturn("mock_mta").anyTimes();
		expect(mta.getMTADateTime()).andReturn(new Date().toString())
						.anyTimes();
		replay(mta);

		classUnderTest = new Session(mta);

	}

	/*
	* Test Fixtures
	* Create Commands, with expected response
	*/ 
	private static Object[][] CMD_QUIT_221 = new Object[][]
		{
						{
										new CommandLine(Command.QUIT, false,
														new String[] {}),
										ReplyCode._221, "Quit", "QUIT"
			}
	};

	private static Object[][] CMD_QUIT_450 = new Object[][]
		{
						{
										new CommandLine(Command.QUIT, false,
														new String[] {}),
										ReplyCode._450, "Quit", "QUIT"
			}
	};

	private static Object[][] CMD_DATA_221 = new Object[][]
		{
						{
										new CommandLine(Command.DATA, false,
														new String[] {}),
										ReplyCode._221, "Data", "DATA"
			}
	};

	private static Object[][] CMD_DATA_503 = new Object[][]
		{
						{
										new CommandLine(Command.DATA, false,
														new String[] {}),
										ReplyCode._503, "Data", "DATA"
			}
	};

	@SuppressWarnings("unused")
	private static Object[][] CMD_MAIL_250 = new Object[][]
		{
						{
										new CommandLine(Command.MAIL, false,
														new String[] {}),
										ReplyCode._250, "Mail", "MAIL FROM:<userx@y.foo.org>"
			}
	};

	@SuppressWarnings("unused")
	private static Object[][] CMD_MAIL_503 = new Object[][]
		{
						{
										new CommandLine(Command.MAIL, false,
														new String[] {}),
										ReplyCode._503, "Mail", "MAIL FROM:<userx@y.foo.org>"
			}
	};

	@SuppressWarnings("unused")
	private static Object[][] CMD_RCPT_250 = new Object[][]
		{
						{
										new CommandLine(Command.RCPT, false,
														new String[] {}),
										ReplyCode._250, "Rcpt",
										"RCPT TO:<@hosta.int,@jkl.org:userc@d.bar.org>"
			}
	};

	@SuppressWarnings("unused")
	private static Object[][] CMD_RCPT_503 = new Object[][]
		{
						{
										new CommandLine(Command.RCPT, false,
														new String[] {}),
										ReplyCode._503, "Rcpt",
										"RCPT TO:<@hosta.int,@jkl.org:userc@d.bar.org>"
			}
	};

	private static Object[][] CMD_EHLO_250 = new Object[][]
		{
						{
										new CommandLine(Command.EHLO, false,
														new String[] {}),
										ReplyCode._250, "Ehlo", "EHLO bar.com"
			}
	};

	private static Object[][] CMD_EHLO_500 =
					new Object[][]
						{
										{
														new CommandLine(Command.EHLO, false,
																		new String[] {}),
														ReplyCode._500, "Ehlo command too long",
														"EHLO too.long.domain.com"
							}
					};

	private static Object[][] CMD_EHLO_501 = new Object[][]
		{
						{
										new CommandLine(Command.EHLO, false,
														new String[] {}),
										ReplyCode._501, "Ehlo, invalid domain name", "EHLO"
			}
	};

	private static Object[][] CMD_EHLO_502 = new Object[][]
		{
						{
										new CommandLine(Command.EHLO, false,
														new String[] {}),
										ReplyCode._502, "Ehlo recognized, but not implemented", "EHLO"
			}
	};

	private static Object[][] CMD_EHLO_504 = new Object[][]
		{
						{
										new CommandLine(Command.EHLO, false,
														new String[] {}),
										ReplyCode._504, "Ehlo", "EHLO notafqdn"
			}
	};

	private static Object[][] CMD_EHLO_521 = new Object[][]
		{
						{
										new CommandLine(Command.EHLO, false,
														new String[] {}),
										ReplyCode._521, "Not accepting mail", "EHLO"
			}
	};

	private static Object[][] CMD_RSET_250 = new Object[][]
		{
						{
										new CommandLine(Command.RSET, false,
														new String[] {}),
										ReplyCode._250, "Rset", "RSET"
			}
	};

	private static Object[][] CMD_RSET_501 = new Object[][]
		{
						{
										new CommandLine(Command.RSET, false,
														new String[] {}),
										ReplyCode._503, "Rset unexpected argument data", "RSET params"
			}
	};

	private static Object[][] CMD_RSET_503 = new Object[][]
		{
						{
										new CommandLine(Command.RSET, false,
														new String[] {}),
										ReplyCode._503, "Rset invalid response code", "RSET"
			}
	};


	/**
	 * commandLine, ReplyCode, Reply Description, Input
	 */
	@DataProvider(name = "lines")
	private Object[][] testdata()
	// String description, Exception expectedException,
	// Object[][] conversation { commandLine, ReplyCode, ReplyDescription, input line }
	{
		Object[][] testdata =
			new Object[][] {
				{
					"Ehlo after connect, good response code.",
					null,
					CMD_EHLO_250
				},
				{
					"Ehlo after connect, not recognized.",
					null,
					CMD_EHLO_500
				},
				{
					"Ehlo after connect, good response code.",
					null,
					CMD_EHLO_501
				},
				{
					"Ehlo after connect, recognized, but not implemented - Invalid code from MTA.",
					MTAException.class,
					CMD_EHLO_502
				},
				{
					"Ehlo after connect, domain not a FQDN.",
					null,
					CMD_EHLO_504
				},
				{
					"Ehlo recognized but not implemented.",
					null,
					CMD_EHLO_521
				},
				{
					"Quit after Connect, valid Reply code",
					null,
					CMD_QUIT_221
				},
				{
					"Quit after Connect, invalid Reply code",
					MTAException.class,
					CMD_QUIT_450
				},

				{
					"DATA out of order, valid Reply code",
					MTAException.class,
					CMD_DATA_503
				},
				{
					"DATA out of order, invalid Reply code",
					MTAException.class,
					CMD_DATA_221
				},
				{
					"Reset, good reply code",
					null,
					CMD_RSET_250
				},
				{
					"Reset, unexpected args",
					MTAException.class,
					CMD_RSET_501
				},
				{
					"Reset, bad reply code",
					MTAException.class,
					CMD_RSET_503
				}

			};

		return testdata;
	}


	/**
	 * 
	 */
	@Test
	public void SessionNull()
	{
		try
		{
			new Session(null);
			fail("Session instantiated with NULL MTA");
		}
		catch (MTAException e)
		{
			// Expected
		}
	}


	/**
	 * @throws MTAException
	 */
	@Test
	public void Session() throws MTAException
	{
		reset(mta);

		expect(mta.isClosed()).andReturn(false).atLeastOnce();
		expect(mta.getMTADomain()).andReturn("mock.mta.domain").anyTimes();
		expect(mta.getMTAName()).andReturn("mock_mta").anyTimes();
		expect(mta.getMTADateTime()).andReturn(new Date().toString())
						.anyTimes();
		replay(mta);

		classUnderTest = new Session(mta);

		assertNotNull(classUnderTest);

		verify(mta);
	}


	/**
	 * 
	 */
	@Test
	public void getStateTable()
	{
		assertNotNull(classUnderTest.getStateTable());
		assertEquals(classUnderTest.getStateTable().getClass(),
						StateTable.class);
	}


	/**
	 * 
	 */
	@Test
	public void processNull()
	{
		try
		{
			classUnderTest.process(null);
			fail("Expected MTAException not thrown for NULL input.");
		}
		catch (MTAException e)
		{
			fail("MTAException thrown for NULL input.");
		}
		catch (SmtpLineException e)
		{
			// Expected
		}
	}


	/**
	 * 
	 */
	@Test
	public void processCRLF()
	{
		try
		{
			classUnderTest.process(Session.CRLF);
			fail("Expected MTAException not thrown for CRLF input.");
		}
		catch (MTAException e)
		{
			fail("MTAException thrown for CRLF input.");
		}
		catch (SmtpLineException e)
		{
			// Expected
		}

		try
		{
			classUnderTest.process("abc" + Session.CRLF + "xyz");
			fail("Expected MTAException not thrown for CRLF input.");
		}
		catch (MTAException e)
		{
			fail("MTAException thrown for CRLF input.");
		}
		catch (SmtpLineException e)
		{
			// Expected
		}
	}


	/**
	 * @param description
	 * @param expectedException
	 * @param conversation
	 * @throws MTAException
	 */
	@Test(dataProvider = "lines")
	public void process(String description, Class<Exception> expectedException,
					Object[][] conversation) throws MTAException
	{

		assertNotNull(classUnderTest);

		for (Object[] line : conversation) {
			boolean lastline = ( line == conversation[conversation.length - 1]);
			
			reset(mta);

			if (line[0] == null)
			// Expect a SEND
			{
				mta.send();
				expectLastCall().anyTimes();
			}
			else
			{
				mta.command((CommandLine) line[0]);
				expectLastCall().anyTimes();
			}

			expect(mta.getReplyCode())
							.andReturn((ReplyCode) line[1])
							.anyTimes();

			replay(mta);

			try
			{
				classUnderTest.process((String) line[3]);

				if (lastline && expectedException != null)
				// Last command, and an exception is expected
				{
					fail(String.format("'%1$s' - Expected %2$s not thrown for '%3$s'.",
						description, expectedException.getClass(), line[0]));
				}
			}
			catch (Exception e)
			{
				if (lastline && expectedException != null)
				// Last command, and an exception is expected
				{
					assertEquals(expectedException, e.getClass());
				}
				else
				{
					fail(String.format("'%1$s' - Unexpected %2$s [%3$s] thrown for %4$s",
					description, e.getClass(), e.getMessage(),line[0]));
				}
			}

			verify(mta);

		}
	}
}
