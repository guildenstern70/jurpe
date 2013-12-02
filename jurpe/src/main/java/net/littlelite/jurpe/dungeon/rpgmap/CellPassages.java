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

/**
 * This class handles the six passages leading out from a cell.
 */
public class CellPassages implements Serializable
{
	private static final long serialVersionUID = 3320L;
	private Passage[] passage = new Passage[6];

	/**
	 * Constructor
	 */
	public CellPassages()
	{
		int count = 0;
		for (Direction d : Direction.values())
		{
			passage[count++] = new Passage(d);
		}
	}

	/**
	 * Set all passages open or closed
	 * 
	 * @param open
	 *            If true set all passages open, else closed.
	 */
	public void setAll(boolean open)
	{
		for (Passage p : this.passage)
		{
			p.setOpen(open);
		}
	}

	/**
	 * Returns true when all the passages are closed.
	 * 
	 * @return Returns true when all the passages are closed.
	 */
	public boolean areAllClosed()
	{
		return ((this.passage[0].isClosed()) && (this.passage[1].isClosed()) && (this.passage[2].isClosed()) && (this.passage[3].isClosed())
				&& (this.passage[4].isClosed()) && (this.passage[5].isClosed()));
	}

	/**
	 * Get the passage in direction d
	 * 
	 * @param dir
	 *            Direction
	 * @return The passage in direction dir
	 */
	public Passage getPassage(Direction dir)
	{
		Passage pass = null;

		for (Passage p : this.passage)
		{
			if (p.getDirection() == dir)
			{
				pass = p;
				break;
			}
		}
		return pass;
	}

	/**
	 * Get all passages
	 * 
	 * @return All passages
	 */
	public Passage[] getPassages()
	{
		return this.passage;
	}

	/**
	 * Get the leftward passage respect to dir
	 * 
	 * @param dir
	 *            Direction
	 * @return Passage left to dir
	 */
	public Passage getLeftward(Direction dir)
	{
		Direction left = null;

		switch (dir)
		{
			case NORTH:
				left = Direction.NORTHWEST;
				break;

			case SOUTH:
				left = Direction.SOUTHEAST;
				break;

			case NORTHEAST:
				left = Direction.NORTH;
				break;

			case NORTHWEST:
				left = Direction.SOUTHWEST;
				break;

			case SOUTHEAST:
				left = Direction.NORTHEAST;
				break;

			case SOUTHWEST:
				left = Direction.SOUTH;
				break;
		}

		return this.getPassage(left);
	}

	public Passage getRightward(Direction dir)
	{
		Direction right = null;

		switch (dir)
		{
			case NORTH:
				right = Direction.NORTHEAST;
				break;

			case SOUTH:
				right = Direction.SOUTHWEST;
				break;

			case NORTHEAST:
				right = Direction.SOUTHEAST;
				break;

			case NORTHWEST:
				right = Direction.NORTH;
				break;

			case SOUTHEAST:
				right = Direction.SOUTH;
				break;

			case SOUTHWEST:
				right = Direction.NORTHWEST;
				break;

		}

		return this.getPassage(right);
	}
}
