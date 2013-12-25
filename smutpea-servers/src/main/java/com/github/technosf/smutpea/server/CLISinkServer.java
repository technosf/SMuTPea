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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.technosf.smutpea.core.MTA;
import com.github.technosf.smutpea.core.exceptions.MTAException;
import com.github.technosf.smutpea.mta.SinkMTA;

/**
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public class CLISinkServer
{

	static InputStreamReader istream = new InputStreamReader(System.in);

	static BufferedReader bufRead = new BufferedReader(istream);

	static MTA mta;


	public static void main(String[] args)
	{
		try
		{
			mta = new SinkMTA("local.cli.server");
			mta.connect();
			System.out.println(mta.getResponse());
		}
		catch (MTAException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String line = null;

		while (!mta.isClosed())
		{
			try
			{
				line = bufRead.readLine();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try
			{
				mta.processLine(line);
			}
			catch (MTAException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println(mta.getResponse());

		}

	}
}
