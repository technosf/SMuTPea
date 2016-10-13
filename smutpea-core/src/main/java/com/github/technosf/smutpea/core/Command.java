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

package com.github.technosf.smutpea.core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public interface Command
{

    /*
     * Constants
     */
    static final String CONST_MSG_EMPTY_CMD =
            "Command is empty. Returning null command line.";
    static final String CONST_MSG_CMD_NOT_FOUND =
            "Command:[{}] was not found or didn't parse. Returning null command line.";
    static final String CONST_MSG_CMD_FOUND =
            "Command line:[{}] parsed and found. Returning valid command line.";
    static final String CONST_MSG_CMD_PARSE =
            "Command line:[{}] didn't parse. Returning invalid command line.";

    static final Logger logger =
            LoggerFactory.getLogger(Command.class);


    /**
     * Returns the
     * {@code Command} and
     * parameters for given input
     * string, all
     * {@code null} if command
     * not found.
     * 
     * @param <T>
     * @param line
     *            The input line
     *            string
     * @return A
     *         {@code Map.Entry}
     *         containing the
     *         command and it's
     *         parts.
     */

    @SuppressWarnings({ "rawtypes", "unchecked" })
    static <C extends Command> CommandLine<C> parseLine(
            Class c,
            final String line)
    {
        if (line == null || "".equals(line.trim()))
        // Check for empty strings
        {
            logger.debug(CONST_MSG_EMPTY_CMD);
            return new CommandLine<C>(null, false, null);
        }

        // Container for the parsed Command and parameter parts.
        CommandLine<C> commandLine = null;

        // Create a cleaned-up line
        String cleanLine = line.replaceAll("\\s+\\z", "");

        // Get the first word in the input string as the command.
        String commandPart = cleanLine.split(" ")[0] // Split into an array along spaces
                .toUpperCase(); // Uppercase the command part

        C command;

        try
        {
            command = (C) Enum.valueOf(c, commandPart); // Find the command by value
        }
        catch (IllegalArgumentException e)
        // The command was not found or didn't parse out correctly, so return
        // null as a valid command wasn't found.
        {
            logger.debug(CONST_MSG_CMD_NOT_FOUND, commandPart);
            return new CommandLine<C>(null, false, null);
        }

        if (command != null)
        // Line is recognized
        {
            boolean validity = false;
            List<String> parameters = new ArrayList<String>();

            Matcher matcher =
                    command.getCommandPattern().matcher(cleanLine);

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
                    new CommandLine<C>(command, validity,
                            parameters.toArray(new String[] {}));

        } // if (command != null)

        return commandLine;

    } // static CommandLine parseLine(final String line)

    /**
     * Data structure for a Command and it's parameters
     */
    public static class CommandLine<C extends Command>
    {

        /**
         * The {@code Command}
         */
        C command;

        /**
         * Command validity
         */
        boolean valid;

        /**
         * Command parameters
         */
        String[] parameters;


        /**
         * Constructor
         * 
         * @param command
         *            The {@code Command}
         * @param parameters
         *            Command parameters
         */
        public CommandLine(C command, boolean valid, String[] parameters)
        {
            this.command = command;
            this.valid = valid;
            this.parameters = parameters;
        }


        /**
         * Returns the {@code Command}
         * 
         * @return the {@code Command}
         */
        public C getCommand()
        {
            return command;
        }


        /**
         * Is the {@code Command} valid
         * 
         * @return true if the {@code Command} is valid
         */
        public boolean isValid()
        {
            return valid;
        }


        /**
         * Returns the parameters provided with this {@code Command}
         * 
         * @return the {@code Command} parameters
         */
        public String[] getParams()
        {
            return parameters;
        }


        /*
         * (non-Javadoc)
         * 
         * @see java.util.AbstractMap.SimpleImmutableEntry#toString()
         */
        @Override
        public String toString()
        {
            StringBuilder result = new StringBuilder();

            if (command != null)
            {
                result.append(command)
                        .append("|")
                        .append(valid)
                        .append("|");

                if (parameters != null)
                {
                    for (String parameter : parameters)
                    {
                        result.append(" - ")
                                .append(parameter);
                    }
                }
            }
            return result.toString();
        }

    } // public static class CommandLine


    /**
     * Validates the given {@code ReplyCode} against actual valid
     * <em>ReplyCodes</em> for this command.
     * 
     * @param replyCode
     *            The response code being validated against this command.
     * @return true if this is a valid reply code.
     */
    boolean validateReplyCode(ReplyCode replyCode);


    /**
     * @return
     */
    Pattern getCommandPattern();

}
