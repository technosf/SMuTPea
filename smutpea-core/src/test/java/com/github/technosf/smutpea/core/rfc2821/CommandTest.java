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

import com.github.technosf.smutpea.core.rfc2821.Command.CommandLine;

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
																"Reset, no params, OK", "rset", Command.RSET,
																true, EMPTY
												},
												{
																"Reset, params, FAIL", "rset some params",
																Command.RSET, false, EMPTY
												},
												{
																"Hello, no domain, FAIL", "helo", Command.HELO,
																false, EMPTY
												},
												{
																"Hello, no domain, FAIL", "HELO", Command.HELO,
																false, EMPTY
												},
												{
																"Hello, no domain, FAIL", "Helo", Command.HELO,
																false, EMPTY
												},
												{
																"Hello, domain, OK", "Helo parameters",
																Command.HELO, true, new String[]
																	{
																		"parameters"
																	}
												},
												{
																"Hello, domain, OK", "Helo <parameters",
																Command.HELO, true, new String[]
																	{
																		"<parameters"
																	}
												},
												{
																"Hello, domain, OK", "Helo parameters>",
																Command.HELO, true, new String[]
																	{
																		"parameters>"
																	}
												},
												{
																"Hello, domain, OK", "Helo <parameters>",
																Command.HELO, true, new String[]
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
																"Data, no params", "data", Command.DATA, true, EMPTY
												},
												{
																"Data, params, FAIL", "data abc", Command.DATA,
																false, EMPTY
												},
												{
																"Mail, no params, FAIL", "MAIL", Command.MAIL,
																false, EMPTY
												},
												{
																"Mail, param, FAIL", "MAIL abc", Command.MAIL,
																false, EMPTY
												},
												{
																"Mail, param, FAIL", "MAIL <abc>",
																Command.MAIL, false, EMPTY
												},
												{
																"Mail, param, FAIL", "MAIL FROM<abc>",
																Command.MAIL, false, EMPTY
												},
												{
																"Mail, param, FAIL", "MAIL FROM:abc>",
																Command.MAIL, false, EMPTY
												},
												{
																"Mail, param, FAIL", "MAIL FROM: <abc>",
																Command.MAIL, false, EMPTY
												},
												{
																"Mail, param, OK", "MAIL FROM:<abc>",
																Command.MAIL, true, new String[]
																	{
																		"abc"
																	}
												},
												{
																"Mail, params, FAIL", "MAIL <abc> xyz",
																Command.MAIL,
																false, EMPTY
												},
												{
																"Mail, params, OK", "MAIL FROM:<abc> <xyz>",
																Command.MAIL, true,
																new String[]
																	{
																					"abc", "xyz"
																	}
												},
												{
																"Mail, too many params, FAIL",
																"MAIL <abc> <xyz> <qwerty>", Command.MAIL,
																false, EMPTY
												},
												{
																"Unknown command, FAIL", "ELOH", null, false, EMPTY
												},
												{
																"Ehlo, no domain, FAIL", "EHLO", Command.EHLO,
																false, EMPTY
												},
												{
																"Ehlo, domain, OK", "EHLO parameters",
																Command.EHLO, true, new String[]
																	{
																		"parameters"
																	}
												},
												{
																"Ehlo, domain, OK", "EHLO <parameters",
																Command.EHLO, true, new String[]
																	{
																		"<parameters"
																	}
												},
												{
																"Ehlo, domain, OK", "EHLO parameters>",
																Command.EHLO, true, new String[]
																	{
																		"parameters>"
																	}
												},
												{
																"Ehlo, domain, OK", "EHLO <parameters>",
																Command.EHLO, true, new String[]
																	{
																		"<parameters>"
																	}
												},
												{
																"Verify, no param, FAIL", "vrfy", Command.VRFY,
																false, EMPTY
												},
												{
																"Verify, param, OK", "vrfy dasdada",
																Command.VRFY, true, new String[]
																	{
																		"dasdada"
																	}
												},
												{
																"Verify, too many params, FAIL",
																"vrfy abc xyz", Command.VRFY, false, EMPTY
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
																Command.RCPT, false, EMPTY
												},
												{
																"Receipt, param, FAIL", "rcpt abc",
																Command.RCPT, false, EMPTY
												},
												{
																"Receipt, param, FAIL", "rcpt <abc>",
																Command.RCPT, false, EMPTY
												},
												{
																"Receipt, param, FAIL", "rcpt to<abc>",
																Command.RCPT, false, EMPTY
												},
												{
																"Receipt, param, FAIL", "rcpt to:abc>",
																Command.RCPT, false, EMPTY
												},
												{
																"Receipt, param, FAIL", "rcpt to: <abc>",
																Command.RCPT, false, EMPTY
												},
												{
																"Receipt, param, OK", "Rcpt to:<abc>",
																Command.RCPT, true, new String[]
																	{
																		"abc"
																	}
												},
												{
																"Receipt, params, FAIL", "rcpt <abc> xyz",
																Command.RCPT, false, EMPTY
												},
												{
																"Receipt, params, OK", "RCPT TO:<abc> <xyz>",
																Command.RCPT, true, new String[]
																	{
																					"abc", "xyz"
																	}
												},
												{
																"Receipt, too many params, FAIL",
																"rcpt <abc> <xyz> <qwerty>", Command.RCPT,
																false, EMPTY
												},
												{
																"Expand, no param, FAIL", "Expn", Command.EXPN,
																false, EMPTY
												},
												{
																"Expand, param, OK", "Expn dasdada",
																Command.EXPN, true, new String[]
																	{
																		"dasdada"
																	}
												},
												{
																"Expand, too many params, FAIL",
																"Expn abc xyz", Command.EXPN, false, EMPTY
												},
												{
																"Unknown comand, FAIL", "help!", null, false, null
												},
												{
																"Help, no params, OK", "HELP", Command.HELP,
																true, EMPTY
												},
												{
																"Help, no params, OK", "help", Command.HELP,
																true, EMPTY
												},
												{
																"Help, params, OK", "help !", Command.HELP,
																true, new String[]
																	{
																		"!"
																	}
												},
												{
																"Help, too many params, FAIL", "help one two",
																Command.HELP, false, EMPTY
												},
												{
																"No Op, no params, OK", "noop", Command.NOOP,
																true, EMPTY
												},
												{
																"No Op, params, OK", "noop abc", Command.NOOP,
																true, new String[]
																	{
																		"abc"
																	}
												},
												{
																"No Op, too many params, FAIL", "noop abc xyz",
																Command.NOOP, false, EMPTY
												},
												{
																"Unknown comand, FAIL", "quite", null, false, EMPTY
												},
												{
																"Quit, too many params, FAIL", "quit xyz",
																Command.QUIT, false, EMPTY
												},
												{
																"Quit, OK", "quit", Command.QUIT, true, EMPTY
												},
												{
																"Quit, OK", "QUIT", Command.QUIT, true, EMPTY
												}
						};

		return testdata;

	}


	@Test(dataProvider = "commands")
	public void testParseCommand(String description, String testCommand,
					Command result, boolean validity, String[] parameters)
	{

		CommandLine processedCommand = Command.parseLine(testCommand);

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
		for (Command command : Command.values())
		{
			for (ReplyCode responseCode : ReplyCode.values())
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
		for (Command command : Command.values())
		{
			assertTrue(!command.toString().isEmpty(), command.name());
		}
	}
}
