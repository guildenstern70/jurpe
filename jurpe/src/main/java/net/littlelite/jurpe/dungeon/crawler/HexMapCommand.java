package net.littlelite.jurpe.dungeon.crawler;

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
 * 
 * A command issued by the player in the HexMap control
 * 
 */
public class HexMapCommand
{
	private Direction d;
	private InstructionByKey i;

	/**
	 * Constructor
	 * 
	 * @param dir
	 *            Direction to move in HexMap
	 * @param ins
	 *            Instruction associated to the command
	 */
	public HexMapCommand(Direction dir, InstructionByKey ins)
	{
		this.d = dir;
		this.i = ins;
	}

	/**
	 * Constructor.
	 * 
	 * @param dir
	 *            Direction to move in HexMap
	 */
	public HexMapCommand(Direction dir)
	{
		this.d = dir;
		this.i = null;
	}

	/**
	 * Constructor
	 * 
	 * @param ins
	 *            Instruction associated to the command
	 */
	public HexMapCommand(InstructionByKey ins)
	{
		this.d = null;
		this.i = ins;
	}

	/**
	 * Get the Instruction associated to the command
	 * 
	 * @return Instruction associated to the command
	 */
	public InstructionByKey getInstruction()
	{
		return this.i;
	}

	/**
	 * Get the Direction to move in HexMap
	 * 
	 * @return Direction to move in HexMap
	 */
	public Direction getDirection()
	{
		return this.d;
	}

	/**
	 * To string
	 */
        @Override
	public String toString()
	{
		String dir = "-";
		String cmd = "-";
		if (this.d != null)
		{
			dir = this.d.toString();
		}
		if (this.i != null)
		{
			cmd = this.i.toString();
		}
		return "HexMapCommand " + dir + "," + cmd;
	}
}