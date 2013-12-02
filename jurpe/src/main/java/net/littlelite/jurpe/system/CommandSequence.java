package net.littlelite.jurpe.system;

/**
 J.U.R.P.E. @version@
 Copyright (C) 2002-12 LittleLite Software

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 */

import java.util.AbstractList;
import java.util.ArrayList;

/**
 * Implements a sequence of commands to be executed.
 * 
 * @see Command
 * 
 */
public class CommandSequence implements ICommand
{

	private AbstractList<ICommand> commands;
	private AbstractList<String> log;

	/**
	 * Build new command sequence
	 */
	public CommandSequence()
	{
		this.commands = new ArrayList<ICommand>();
		this.log = new ArrayList<String>();
	}

	/**
	 * Add a command to be executed in sequence.
	 * 
	 * @param command
	 *            command to add to the sequence
	 */
	public void add(ICommand command)
	{
		commands.add(command);
	}

	/**
	 * Executes a command sequence.
	 * 
	 * @return true, if the last command returned true.
	 */
	public boolean execute()
	{
		boolean returnOk = true;

		for (ICommand nextCommand : this.commands)
		{
			returnOk = nextCommand.execute();
			log.add(nextCommand.getLog());
		}
		return returnOk;
	}

	/**
	 * Returns log of every command executed in this sequence.
	 * 
	 * @return String separated by LF of every command executed by this
	 *         sequence.
	 */
	public String getLog()
	{
		StringBuilder sb = new StringBuilder();

		for (String nextString : this.log)
		{
			sb.append(nextString);
			sb.append(OSProps.LINEFEED);
		}

		return sb.toString();
	}

	/**
	 * Returns the log as a List of Strings
	 * 
	 * @return List of Strings
	 */
	public AbstractList<String> getLogItems()
	{
		return this.log;
	}
}
