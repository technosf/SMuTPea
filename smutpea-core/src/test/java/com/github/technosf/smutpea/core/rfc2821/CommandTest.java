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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.technosf.smutpea.core.impl.rfc2821.Commands;
import com.github.technosf.smutpea.core.impl.rfc2821.Commands.CommandLine;

/**
 * Unit test for {@code Command}
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 * 
 */
public class CommandTest
{
	private static String[] EMPTY = new String[] {};


	// private static Command nullCommandLine = new CommandLine(null, false, null);

	@DataProvider(name = "commands")
	private Object[][] testdata()
	{
		Object[][] testdata =
						new Object[][]
							{
												{
																"Null input, FAIL", null, null, false, EMPTY
												},
												{
																"Empty input, FAIL", "", null, false, EMPTY
												},
												{
																"Whitespace input, FAIL", " ", null, false, EMPTY
												},
												{
																"Reset, no params, OK", "rset", Commands.RSET,
																true, EMPTY
												},
												{
																"Reset, params, FAIL", "rset some params",
																Commands.RSET, false, EMPTY
												},
												{
																"Hello, no domain, FAIL", "helo", Commands.HELO,
																false, EMPTY
												},
												{
																"Hello, no domain, FAIL", "HELO", Commands.HELO,
																false, EMPTY
												},
												{
																"Hello, no domain, FAIL", "Helo", Commands.HELO,
																false, EMPTY
												},
												{
																"Hello, domain, OK", "Helo parameters",
																Commands.HELO, true, new String[]
																	{
																		"parameters"
																	}
												},
												{
																"Hello, domain, OK", "Helo <parameters",
																Commands.HELO, true, new String[]
																	{
																		"<parameters"
																	}
												},
												{
																"Hello, domain, OK", "Helo parameters>",
																Commands.HELO, true, new String[]
																	{
																		"parameters>"
																	}
												},
												{
																"Hello, domain, OK", "Helo <parameters>",
																Commands.HELO, true, new String[]
																	{
																		"<parameters>"
																	}
												},
												{
																"Unknown command, FAIL", "unknowncmd abc",
																null, false, EMPTY
												},
												{
																"Space before DATA command, FAIL", " data",
																null, false, null
												},
												{
																"Data, no params", "data", Commands.DATA, true, EMPTY
												},
												{
																"Data, params, FAIL", "data abc", Commands.DATA,
																false, EMPTY
												},
												{
																"Mail, no params, FAIL", "MAIL", Commands.MAIL,
																false, EMPTY
												},
												{
																"Mail, param, FAIL", "MAIL abc", Commands.MAIL,
																false, EMPTY
												},
												{
																"Mail, param, FAIL", "MAIL <abc>",
																Commands.MAIL, false, EMPTY
												},
												{
																"Mail, param, FAIL", "MAIL FROM<abc>",
																Commands.MAIL, false, EMPTY
												},
												{
																"Mail, param, FAIL", "MAIL FROM:abc>",
																Commands.MAIL, false, EMPTY
												},
												{
																"Mail, param, FAIL", "MAIL FROM: <abc>",
																Commands.MAIL, false, EMPTY
												},
												{
																"Mail, param, OK", "MAIL FROM:<abc>",
																Commands.MAIL, true, new String[]
																	{
																		"abc"
																	}
												},
												{
																"Mail, params, FAIL", "MAIL <abc> xyz",
																Commands.MAIL,
																false, EMPTY
												},
												{
																"Mail, params, OK", "MAIL FROM:<abc> <xyz>",
																Commands.MAIL, true,
																new String[]
																	{
																					"abc", "xyz"
																	}
												},
												{
																"Mail, too many params, FAIL",
																"MAIL <abc> <xyz> <qwerty>", Commands.MAIL,
																false, EMPTY
												},
												{
																"Unknown command, FAIL", "ELOH", null, false, EMPTY
												},
												{
																"Ehlo, no domain, FAIL", "EHLO", Commands.EHLO,
																false, EMPTY
												},
												{
																"Ehlo, domain, OK", "EHLO parameters",
																Commands.EHLO, true, new String[]
																	{
																		"parameters"
																	}
												},
												{
																"Ehlo, domain, OK", "EHLO <parameters",
																Commands.EHLO, true, new String[]
																	{
																		"<parameters"
																	}
												},
												{
																"Ehlo, domain, OK", "EHLO parameters>",
																Commands.EHLO, true, new String[]
																	{
																		"parameters>"
																	}
												},
												{
																"Ehlo, domain, OK", "EHLO <parameters>",
																Commands.EHLO, true, new String[]
																	{
																		"<parameters>"
																	}
												},
												{
																"Verify, no param, FAIL", "vrfy", Commands.VRFY,
																false, EMPTY
												},
												{
																"Verify, param, OK", "vrfy dasdada",
																Commands.VRFY, true, new String[]
																	{
																		"dasdada"
																	}
												},
												{
																"Verify, too many params, FAIL",
																"vrfy abc xyz", Commands.VRFY, false, EMPTY
												},
												{
																"Unknown command, FAIL", "non rcpt", null,
																false, EMPTY
												},
												{
																"Space before Receipt command, FAIL", " rcpt",
																null, false, EMPTY
												},
												{
																"Receipt, no params, FAIL", "rcpt",
																Commands.RCPT, false, EMPTY
												},
												{
																"Receipt, param, FAIL", "rcpt abc",
																Commands.RCPT, false, EMPTY
												},
												{
																"Receipt, param, FAIL", "rcpt <abc>",
																Commands.RCPT, false, EMPTY
												},
												{
																"Receipt, param, FAIL", "rcpt to<abc>",
																Commands.RCPT, false, EMPTY
												},
												{
																"Receipt, param, FAIL", "rcpt to:abc>",
																Commands.RCPT, false, EMPTY
												},
												{
																"Receipt, param, FAIL", "rcpt to: <abc>",
																Commands.RCPT, false, EMPTY
												},
												{
																"Receipt, param, OK", "Rcpt to:<abc>",
																Commands.RCPT, true, new String[]
																	{
																		"abc"
																	}
												},
												{
																"Receipt, params, FAIL", "rcpt <abc> xyz",
																Commands.RCPT, false, EMPTY
												},
												{
																"Receipt, params, OK", "RCPT TO:<abc> <xyz>",
																Commands.RCPT, true, new String[]
																	{
																					"abc", "xyz"
																	}
												},
												{
																"Receipt, too many params, FAIL",
																"rcpt <abc> <xyz> <qwerty>", Commands.RCPT,
																false, EMPTY
												},
												{
																"Expand, no param, FAIL", "Expn", Commands.EXPN,
																false, EMPTY
												},
												{
																"Expand, param, OK", "Expn dasdada",
																Commands.EXPN, true, new String[]
																	{
																		"dasdada"
																	}
												},
												{
																"Expand, too many params, FAIL",
																"Expn abc xyz", Commands.EXPN, false, EMPTY
												},
												{
																"Unknown comand, FAIL", "help!", null, false, null
												},
												{
																"Help, no params, OK", "HELP", Commands.HELP,
																true, EMPTY
												},
												{
																"Help, no params, OK", "help", Commands.HELP,
																true, EMPTY
												},
												{
																"Help, params, OK", "help !", Commands.HELP,
																true, new String[]
																	{
																		"!"
																	}
												},
												{
																"Help, too many params, FAIL", "help one two",
																Commands.HELP, false, EMPTY
												},
												{
																"No Op, no params, OK", "noop", Commands.NOOP,
																true, EMPTY
												},
												{
																"No Op, params, OK", "noop abc", Commands.NOOP,
																true, new String[]
																	{
																		"abc"
																	}
												},
												{
																"No Op, too many params, FAIL", "noop abc xyz",
																Commands.NOOP, false, EMPTY
												},
												{
																"Unknown comand, FAIL", "quite", null, false, EMPTY
												},
												{
																"Quit, too many params, FAIL", "quit xyz",
																Commands.QUIT, false, EMPTY
												},
												{
																"Quit, OK", "quit", Commands.QUIT, true, EMPTY
												},
												{
																"Quit, OK", "QUIT", Commands.QUIT, true, EMPTY
												}
						};

		return testdata;

	}


	@Test(dataProvider = "commands")
	public void testParseCommand(String description, String testCommand,
					Commands result, boolean validity, String[] parameters)
	{

		CommandLine processedCommand = Commands.parseLine(testCommand);

		if (result == null)
		{
			assertNull(processedCommand.getCommand(), description);
			return;
		}

		assertEquals(processedCommand.getCommand(), result, description
						+ " on command");
		assertEquals(processedCommand.isValid(), validity, description
						+ " on validity");
		assertEquals(processedCommand.getParams(), parameters, description
						+ " on parameters");

	}


	@Test
	public void testValidateResponse()
	{
		for (Commands command : Commands.values())
		{
			for (ReplyCodeRFC2821 responseCode : ReplyCodeRFC2821.values())
			{
				assertEquals(
								command.validateReplyCode(responseCode),
								command.validReplyCodes.contains(responseCode),
								String.format(
												"Validating response code:[%2$s] for command:[%1$s]",
												command.name(), responseCode.name()));
			}
		}
	}


	@Test
	public void testToString()
	{
		for (Commands command : Commands.values())
		{
			assertTrue(!command.toString().isEmpty(), command.name());
		}
	}
}
