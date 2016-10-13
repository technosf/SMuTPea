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

package com.github.technosf.smutpea.core.impl.rfc2821;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.technosf.smutpea.core.Command;
import com.github.technosf.smutpea.core.ReplyCode;

/**
 * SMTP Commands, syntax and valid reply code definition
 * <p>
 * Command {@code enum} with data to parse the input and validate potential
 * response codes.
 * 
 * @see http://tools.ietf.org/html/rfc2821#section-4.1.1
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public enum Commands
    implements Command
{

    //
    EHLO("EHLO SP Domain", "EHLO (\\S*)$", new ReplyCodes[] {
            ReplyCodes._250, ReplyCodes._421, ReplyCodes._500, ReplyCodes._501,
            ReplyCodes._502, ReplyCodes._503, ReplyCodes._504, ReplyCodes._550
    }),

    //
    HELO("HELO SP Domain", "HELO (\\S*)$", new ReplyCodes[] {
            ReplyCodes._250, ReplyCodes._421, ReplyCodes._500, ReplyCodes._501,
            ReplyCodes._502, ReplyCodes._503, ReplyCodes._504
    }),

    //
    MAIL("MAIL FROM:<reverse-path> [SP <mail-parameters> ]",
            "MAIL FROM:<(\\S*)>(?: <(\\S*)>)?$",
            new ReplyCodes[] {
                    ReplyCodes._250, ReplyCodes._421, ReplyCodes._451,
                    ReplyCodes._452,
                    ReplyCodes._503, ReplyCodes._550,
                    ReplyCodes._552, ReplyCodes._553
            }),

    //
    RCPT("RCPT TO:<forward-path> [SP <rcpt-parameters> ]",
            "RCPT TO:<(\\S*)>(?: <(\\S*)>)?$", new ReplyCodes[] {
                    ReplyCodes._250, ReplyCodes._251, ReplyCodes._421,
                    ReplyCodes._450,
                    ReplyCodes._451, ReplyCodes._452,
                    ReplyCodes._503, ReplyCodes._550, ReplyCodes._552,
                    ReplyCodes._553
            }),

    //
    DATA("DATA", "DATA$", new ReplyCodes[] {
            ReplyCodes._354, ReplyCodes._421, ReplyCodes._451, ReplyCodes._452,
            ReplyCodes._501, ReplyCodes._503, ReplyCodes._552,
            ReplyCodes._554
    }),

    //
    RSET("RSET", "RSET$", new ReplyCodes[] {
            ReplyCodes._250, ReplyCodes._421, ReplyCodes._501
    }),

    //
    VRFY("VRFY SP String", "VRFY (\\S*)$", new ReplyCodes[] {
            ReplyCodes._250, ReplyCodes._421, ReplyCodes._502, ReplyCodes._504,
            ReplyCodes._550, ReplyCodes._551,
            ReplyCodes._553
    }),

    //
    EXPN("EXPN SP String", "EXPN (\\S*)$", new ReplyCodes[] {
            ReplyCodes._250, ReplyCodes._252, ReplyCodes._421, ReplyCodes._500,
            ReplyCodes._502, ReplyCodes._503,
            ReplyCodes._504, ReplyCodes._550
    }),

    //
    HELP("HELP [SP String]", "HELP(?: (\\S*))?$", new ReplyCodes[] {
            ReplyCodes._211, ReplyCodes._214, ReplyCodes._502,
            ReplyCodes._503, ReplyCodes._504
    }),

    //
    NOOP("NOOP [SP String]", "NOOP(?: (\\S*))?$", new ReplyCodes[] {
            ReplyCodes._250, ReplyCodes._421, ReplyCodes._503
    }),

    //
    QUIT("Quit", "QUIT$", new ReplyCodes[] {
            ReplyCodes._221, ReplyCodes._421, ReplyCodes._501, ReplyCodes._503
    });

    private static final Logger logger =
            LoggerFactory.getLogger(Commands.class);

    /*
     * Constants
     */
    private static final String CONST_MSG_EMPTY_CMD =
            "Command is empty. Returning null command line.";
    private static final String CONST_MSG_CMD_NOT_FOUND =
            "Command:[{}] was not found or didn't parse. Returning null command line.";
    private static final String CONST_MSG_CMD_FOUND =
            "Command line:[{}] parsed and found. Returning valid command line.";
    private static final String CONST_MSG_CMD_PARSE =
            "Command line:[{}] didn't parse. Returning invalid command line.";

    /**
     * Command description
     */
    private final String description;

    /**
     * Command validation pattern
     */
    private final Pattern pattern;

    /**
     * Command valid responses
     */
    final List<ReplyCodes> validReplyCodes;

    /**
     * Data structure for a Command and it's parameters
     */
    //		public static class CommandLine
    //		{
    //			/**
    //			 * The {@code Command}
    //			 */
    //			Command command;
    //
    //			/**
    //			 * Command validity
    //			 */
    //			boolean valid;
    //
    //			/**
    //			 * Command parameters
    //			 */
    //			String[] parameters;
    //
    //
    //			/**
    //			 * Constructor
    //			 * 
    //			 * @param command
    //			 *            The {@code Command}
    //			 * @param parameters
    //			 *            Command parameters
    //			 */
    //			CommandLine(Command command, boolean valid, String[] parameters)
    //			{
    //				this.command = command;
    //				this.valid = valid;
    //				this.parameters = parameters;
    //			}
    //
    //
    //			/**
    //			 * Returns the {@code Command}
    //			 * 
    //			 * @return the {@code Command}
    //			 */
    //			public Command getCommand()
    //			{
    //				return command;
    //			}
    //
    //
    //			/**
    //			 * Is the {@code Command} valid
    //			 * 
    //			 * @return true if the {@code Command} is valid
    //			 */
    //			public boolean isValid()
    //			{
    //				return valid;
    //			}
    //
    //
    //			/**
    //			 * Returns the parameters provided with this {@code Command}
    //			 * 
    //			 * @return the {@code Command} parameters
    //			 */
    //			public String[] getParams()
    //			{
    //				return parameters;
    //			}
    //
    //
    //			/*
    //			 * (non-Javadoc)
    //			 * 
    //			 * @see java.util.AbstractMap.SimpleImmutableEntry#toString()
    //			 */
    //			@Override
    //			public String toString()
    //			{
    //				StringBuilder result = new StringBuilder();
    //
    //				if (command != null)
    //				{
    //					result.append(command)
    //									.append("|")
    //									.append(valid)
    //									.append("|");
    //
    //					if (parameters != null)
    //					{
    //						for (String parameter : parameters)
    //						{
    //							result.append(" - ")
    //											.append(parameter);
    //						}
    //					}
    //				}
    //				return result.toString();
    //			}
    //
    //		} // public static class CommandLine


    /**
     * Constructor for {@code Command} enums
     * 
     * @param description
     *            The {@code Command} description
     * @param validationRegex
     *            Regex rule for the command
     */
    Commands(String description, String validationRegex,
            ReplyCodes[] validResponseCodes)
    {
        this.description = description;
        pattern = Pattern.compile(validationRegex, Pattern.CASE_INSENSITIVE);
        this.validReplyCodes = Arrays.asList(validResponseCodes);
    }


    /**
     * Returns the {@code Command} and parameters for given input string, all
     * {@code null} if command not found.
     * 
     * @param line
     *            The input line string
     * @return A {@code Map.Entry} containing the command and it's parts.
     */
    static CommandLine<Commands> parseLine(final String line)
    {
        if (line == null || "".equals(line.trim()))
        // Check for empty strings
        {
            logger.debug(CONST_MSG_EMPTY_CMD);
            return new CommandLine<Commands>(null, false, null);
        }

        // Container for the parsed Command and parameter parts.
        CommandLine<Commands> commandLine = null;

        // Create a cleaned-up line
        String cleanLine = line.replaceAll("\\s+\\z", "");

        // Get the first word in the input string as the command.
        String commandPart = cleanLine.split(" ")[0] // Split into an array along spaces
                .toUpperCase(); // Uppercase the command part

        Commands command;

        try
        {
            command = Commands.valueOf(commandPart); // Find the command by value
        }
        catch (IllegalArgumentException e)
        // The command was not found or didn't parse out correctly, so return
        // null as a valid command wasn't found.
        {
            logger.debug(CONST_MSG_CMD_NOT_FOUND, commandPart);
            return new CommandLine<Commands>(null, false, null);
        }

        if (command != null)
        // Line is recognized
        {
            boolean validity = false;
            List<String> parameters = new ArrayList<String>();

            Matcher matcher =
                    command.pattern.matcher(cleanLine);

            if (matcher.matches())
            // The input line parses to the command definition, and is valid.
            {
                logger.debug(CONST_MSG_CMD_FOUND, cleanLine);

                validity = true;

                for (int groupNumber = 1; groupNumber <= matcher
                        .groupCount(); groupNumber++)
                // Parse out the parameter from the definition regex groups
                // Group 0 is the full match. 1+ are the groups
                {
                    if (matcher.group(groupNumber) != null)
                    // Optional parameters that are not present will be null - ignore them
                    {
                        parameters.add(matcher.group(groupNumber));
                    }

                } // for (int groupNumber = 1; groupNumber <= matcher.groupCount(); groupNumber++)

            } // if (matcher.matches())
            else
            {
                logger.debug(CONST_MSG_CMD_PARSE, cleanLine);
            }

            commandLine =
                    new CommandLine<Commands>(command, validity,
                            parameters.toArray(new String[] {}));

        } // if (command != null)

        return commandLine;

    } // static CommandLine parseLine(final String line)


    /**
     * Validates the given {@code ReplyCode} against actual valid
     * <em>ReplyCodes</em> for this command.
     * 
     * @param replyCode
     *            The response code being validated against this command.
     * @return true if this is a valid reply code.
     */
    public boolean validateReplyCode(ReplyCodes replyCode)
    {
        return validReplyCodes.contains(replyCode);
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString()
    {
        return description + " {" + pattern.toString() + "}";
    }


    @Override
    public boolean validateReplyCode(ReplyCode replyCode)
    {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public Pattern getCommandPattern()
    {
        return pattern;
    }

}
