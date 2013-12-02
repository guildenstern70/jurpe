package net.littlelite.jurpe.dungeon.rpgmap;

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

import java.io.Serializable;

import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.furnishing.Door;

/**
 * Passage is an open or closed passage between two RPGMAP cells. Can contain a
 * door or not.
 */
public class Passage implements Serializable
{

	private static final long serialVersionUID = 3319L;

	private Direction direction;
	private Door door;
	private boolean opened;

	/**
	 * Constructor. Defaults to open passage.
	 */
	public Passage(Direction dir)
	{
		this.direction = dir;
		this.door = null;
		this.opened = false;
	}

	/**
	 * Return the direction of this passage. The direction is always relative to
	 * the center of the cell
	 * 
	 * @return Direction of the passage
	 */
	public Direction getDirection()
	{
		return this.direction;
	}

	/**
	 * Returns true if this passage is closed
	 * 
	 * @return true if this passage is closed
	 */
	public boolean isClosed()
	{
		return !this.opened;
	}

	/**
	 * Returns true if this passage is opened
	 * 
	 * @return True if this passage is opened
	 */
	public boolean isOpened()
	{
		return this.opened;
	}

	/**
	 * Set the passage closed
	 */
	public void setClosed()
	{
		this.opened = false;
	}

	/**
	 * Set the passage open
	 */
	public void setOpen()
	{
		this.opened = true;
	}

	/**
	 * Set the passage open
	 * 
	 * @param open
	 *            If this passage is open, set to true
	 */
	public void setOpen(boolean open)
	{
		this.opened = open;
	}

	/**
	 * Set a door in this passage
	 */
	public void setDoor(Door door)
	{
		this.door = door;
	}

	/**
	 * Get door, if any, in this passage
	 * 
	 * @return Door in this passage. Null if there is no door.
	 */
	public Door getDoor()
	{
		return this.door;
	}

	/**
	 * If this passage has a door it will return true
	 * 
	 * @return True, if this passage has a door
	 */
	public boolean hasDoor()
	{
		boolean hasIt = false;

		if (this.door != null)
		{
			hasIt = true;
		}

		return hasIt;
	}

	/**
	 * A representation of the passage: O = opened C = closed If the passage has
	 * a door, a representation of the door is added
	 */
        @Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (this.opened)
		{
			sb.append("O");
		}
		else
		{
			sb.append("C");
		}

		if (this.door != null)
		{
			sb.append(this.door.toString());
		}
		return sb.toString();
	}
}
