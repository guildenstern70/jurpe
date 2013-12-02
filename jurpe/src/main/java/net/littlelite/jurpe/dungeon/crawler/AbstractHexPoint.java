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

import java.awt.Point;
import java.io.Serializable;

/**
 * Base point class for each point.
 */
public abstract class AbstractHexPoint implements Serializable
{
	// UID
	private static final long serialVersionUID = 2L;

	public short x;
	public short y;

	/**
	 * Empty constructor
	 */
	public AbstractHexPoint()
	{
		this.x = 0;
		this.y = 0;
	}

	/**
	 * Costructor. Initialize AbstractHexPoint with the given point. Only x and y are
	 * taken from Point properties
	 * 
	 * @param c
	 */
	public AbstractHexPoint(Point c)
	{
		this.x = (short) c.x;
		this.y = (short) c.y;
	}

	/**
	 * Construction.
	 * 
	 * @param xx
	 *            X coordinate
	 * @param yy
	 *            Y coordinate
	 */
	public AbstractHexPoint(short xx, short yy)
	{
		this.x = xx;
		this.y = yy;
	}

	/**
	 * Hash code
	 */
	public int hashCode()
	{
		// Very simple approach:
		int result = 17;
		result = 37 * this.x + this.y;
		return result;
	}

	/**
	 * Equality
	 */
	public boolean equals(Object o)
	{
		return (o instanceof AbstractHexPoint) && this.x == (((AbstractHexPoint) o).x) && this.y == (((AbstractHexPoint) o).y);
	}

	/**
	 * String
	 */
        @Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("AbstractHexPoint [");
		sb.append(this.x);
		sb.append(",");
		sb.append(this.y);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Returns a Point from this AbstractHexPoint
	 * 
	 * @return Point from this AbstractHexPoint
	 * @see java.awt.Point
	 */
	public Point toPoint()
	{
		return new Point(this.x, this.y);
	}

}