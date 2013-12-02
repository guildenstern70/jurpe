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

/**
 * HexMapPoint Point in the HexMap
 */
public class HexMapPoint extends AbstractHexPoint
{
	private static final long serialVersionUID = 3317L;

	/**
	 * Constructor
	 * 
	 * @param c
	 *            Point Point in AbstractHexPoint coordinates
	 */
	public HexMapPoint(Point c)
	{
		super((short) c.x, (short) c.y);
	}

	/**
	 * Constructor
	 * 
	 * @param xx
	 *            short AbstractHexPoint x point
	 * @param yy
	 *            short AbstractHexPoint y point
	 */
	public HexMapPoint(short xx, short yy)
	{
		super(xx, yy);
	}
}