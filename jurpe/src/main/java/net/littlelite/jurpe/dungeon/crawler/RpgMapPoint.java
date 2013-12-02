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

import net.littlelite.jurpe.dungeon.rpgmap.MapView;

/**
 * Coordinates of a point over an RPG map
 */
public class RpgMapPoint extends AbstractHexPoint implements Serializable
{
	private static final long serialVersionUID = 3317L;

	/**
	 * Constructor
	 * 
	 * @param c
	 *            Coordinates of this point
	 */
	public RpgMapPoint(Point c)
	{
		super(c);
	}

	/**
	 * Constructor
	 * 
	 * @param xx
	 *            x coordinate of point
	 * @param yy
	 *            y coordinate of point
	 */
	public RpgMapPoint(short xx, short yy)
	{
		super(xx, yy);
	}

	/**
	 * Transform this point into a ViewMap point
	 * 
	 * @param view
	 *            Viewmap
	 * @return A ViewMap point
	 * @see MapView
	 */
	public ViewMapPoint toViewMapPoint(MapView view)
	{
		return new ViewMapPoint(view, this);
	}

	/**
	 * Get distance from this point to another RpgMapPoint
	 * 
	 * @param point
	 * @return
	 */
	public int getDistanceFrom(RpgMapPoint point)
	{
		int ax = this.x;
		int ay = this.y;
		int px = point.x;
		int py = point.y;

		int dx = Math.abs(px - ax);
		int dy = Math.abs(py - ay);

		return (int) Math.sqrt((double) (dx * dx + dy * dy));
	}
}