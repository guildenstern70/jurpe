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

import java.util.Random;

import net.littlelite.jurpe.system.JurpeException;

/**
 * Direction helper class. Incapsulates direction info as an integer value.
 */
public enum Direction
{

	NORTH((short) 0), NORTHEAST((short) 1), SOUTHEAST((short) 2), SOUTH((short) 3), SOUTHWEST((short) 4), NORTHWEST((short) 5);

	private short dir;

	/**
	 * Get a random direction
	 * 
	 * @param rnd
	 *            Random seed
	 * @return New random Direction object
	 */
	public static Direction getRandom(Random rnd) throws JurpeException
	{
		return Direction.fromValue((short) (rnd.nextInt(6)));
	}

	/**
	 * Get a direction from a string. If not recognized, a null will be
	 * returned.
	 * 
	 * @param str
	 *            Can be North,South,NorthEast,NorthWest,SouthEast,SouthWest.
	 * @return A direction from a string
	 */
	public static Direction fromString(String str)
	{
		Direction d = null;

		if (str.equals("North"))
		{
			d = Direction.NORTH;
		}
		else if (str.equals("South"))
		{
			d = Direction.SOUTH;
		}
		else if (str.equals("NorthEast"))
		{
			d = Direction.NORTHEAST;
		}
		else if (str.equals("NorthWest"))
		{
			d = Direction.NORTHWEST;
		}
		else if (str.equals("SouthEast"))
		{
			d = Direction.SOUTHEAST;
		}
		else if (str.equals("SouthWest"))
		{
			d = Direction.SOUTHWEST;
		}

		return d;
	}

	/**
	 * Return a direction from a given value
	 * 
	 * @param x
	 *            0=N,1=NE,2=SE,3=S,4=SW,5=NW
	 * @return The Direction
	 * @throws JurpeException
	 */
	public static Direction fromValue(short x) throws JurpeException
	{
		Direction d;

		switch (x)
		{
			case 0:
				d = Direction.NORTH;
				break;

			case 1:
				d = Direction.NORTHEAST;
				break;

			case 2:
				d = Direction.SOUTHEAST;
				break;

			case 3:
				d = Direction.SOUTH;
				break;

			case 4:
				d = Direction.SOUTHWEST;
				break;

			case 5:
				d = Direction.NORTHWEST;
				break;

			default:
				throw new JurpeException("Unknown direction " + x);

		}

		return d;
	}

	/**
	 * Get the inverse direction (ie: North->South, SouthWest->NorthEast)
	 * 
	 * @param d
	 *            Direction to be inverted
	 * @return New direction that is the inverse of d
	 */
	public static Direction getInverse(Direction d)
	{
		Direction inverse = d; // default

		if (d == Direction.NORTH)
		{
			inverse = Direction.SOUTH;
		}
		else if (d == Direction.SOUTH)
		{
			inverse = Direction.NORTH;
		}
		else if (d == Direction.NORTHEAST)
		{
			inverse = Direction.SOUTHWEST;
		}
		else if (d == Direction.NORTHWEST)
		{
			inverse = Direction.SOUTHEAST;
		}
		else if (d == Direction.SOUTHEAST)
		{
			inverse = Direction.NORTHWEST;
		}
		else if (d == Direction.SOUTHWEST)
		{
			inverse = Direction.NORTHEAST;
		}

		return inverse;

	}

	/**
	 * To string
	 */
        @Override
	public String toString()
	{
		Direction d = this;
		String str = null;

		if (d == Direction.NORTH)
		{
			str = "North";
		}
		else if (d == Direction.SOUTH)
		{
			str = "South";
		}
		else if (d == Direction.NORTHEAST)
		{
			str = "NorthEast";
		}
		else if (d == Direction.NORTHWEST)
		{
			str = "NorthWest";
		}
		else if (d == Direction.SOUTHEAST)
		{
			str = "SouthEast";
		}
		else if (d == Direction.SOUTHWEST)
		{
			str = "SouthWest";
		}

		return str;
	}

	/**
	 * Get the value of this direction
	 * 
	 * @return The value of this direction
	 */
	public short getValue()
	{
		return this.dir;
	}

	/**
	 * Constructor. New direction given a direction. Please use this class final
	 * static fields to select a direction, ie: Direction.NORTH
	 * 
	 * @param d
	 *            Direction number
	 */
	private Direction(short d)
	{
		this.dir = d;
	}

}