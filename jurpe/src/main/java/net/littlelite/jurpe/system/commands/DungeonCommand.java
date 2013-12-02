package net.littlelite.jurpe.system.commands;

/**
 J.U.R.P.E. @version@ (DungeonCrawler Package)
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

/**
 * A dungeon command is a group of commands issued by a player in the dungeon.
 * It encapsulate a "feedback" string to communicate to the user the results of
 * the command and an "option" so that the command can have a parameter.
 */
public class DungeonCommand
{

	private PlayerCommand command;
	private String commandFeedback;
	private String commandOption;

	/**
	 * Constructor
	 * 
	 * @param command
	 *            CommandID (see static fields)
	 */
	public DungeonCommand(PlayerCommand cmd)
	{
		this.command = cmd;
		this.commandFeedback = null;
		this.commandOption = null;
	}

	/**
	 * Constructor
	 * 
	 * @param command
	 *            CommandID (see static fields)
	 * @param feedback
	 *            Feedback when the user selects this command
	 */
	public DungeonCommand(PlayerCommand cmd, String feedback)
	{
		this.command = cmd;
		this.commandFeedback = feedback;
	}

	/**
	 * Get command ID
	 * 
	 * @return command ID
	 */
	public PlayerCommand getCommand()
	{
		return this.command;
	}

	/**
	 * Set the feedback for when the user selects this command
	 * 
	 * @param f
	 *            Feedback when the user selects this command
	 */
	public void setFeedback(String f)
	{
		this.commandFeedback = f;
	}

	/**
	 * Get feedback of this command
	 * 
	 * @return feedback of this command
	 */
	public String getFeedback()
	{
		return this.commandFeedback;
	}

	/**
	 * Set an option for this command
	 * 
	 * @param o
	 *            Option for this command (ie> monster to attack)
	 */
	public void setOption(String o)
	{
		this.commandOption = o;
	}

	/**
	 * Get command option
	 * 
	 * @return command option if present, else null
	 */
	public String getOption()
	{
		return this.commandOption;
	}

	/**
	 * Direction hash code.
	 * 
	 * @return Hash code
	 */
        @Override
	public int hashCode()
	{
		// Very simple approach:
		int result = 23;
		result *= this.command.ordinal();
		return result;
	}

	/**
	 * Equality conditions
	 * 
	 * @param o
	 *            Object to compare
	 * @return True if o is equal to this
	 */
        @Override
	public boolean equals(Object o)
	{
		return (o instanceof DungeonCommand) && (this.command == ((DungeonCommand) o).getCommand());
	}

}
