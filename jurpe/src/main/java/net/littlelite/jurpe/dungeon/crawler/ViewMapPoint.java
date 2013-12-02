package net.littlelite.jurpe.dungeon.crawler;

import net.littlelite.jurpe.dungeon.rpgmap.MapView;

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
 * Point in a RpgViewMap.
 */
public class ViewMapPoint extends RpgMapPoint
{
	private static final long serialVersionUID = 3317L;

	private MapView viewport;

	/**
	 * Point constructor
	 * 
	 * @param view
	 *            RpgMapView in which this point lies.
	 */
	public ViewMapPoint(MapView view)
	{
		super((short) 0, (short) 0);
		this.viewport = view;
	}

	/**
	 * Point constructor
	 * 
	 * @param view
	 *            RpgMapView in which this point lies
	 * @param xx
	 *            X point coordinate
	 * @param yy
	 *            Y point coordinate
	 */
	public ViewMapPoint(MapView view, short xx, short yy)
	{
		super(xx, yy);
		this.viewport = view;
	}

	/**
	 * Point constructor
	 * 
	 * @param view
	 *            RpgMapView in which this point lies
	 * @param p
	 *            absolute RpgMapPoint on RpgMap
	 */
	public ViewMapPoint(MapView view, RpgMapPoint p)
	{
		super((short) 0, (short) 0);
		this.viewport = view;
		RpgMapPoint topleft = view.getTopLeft();
		this.x = (short) (p.x - topleft.x);
		this.y = (short) (p.y - topleft.y);
	}

	/**
	 * Convert a ViewMapPoint in a RpgMapPoint
	 * 
	 * @return RpgMapPoint
	 */
	public RpgMapPoint toRpgMapPoint()
	{
		RpgMapPoint p = new RpgMapPoint((short) 0, (short) 0);
		RpgMapPoint topleft = this.viewport.getTopLeft();
		p.x = (short) (this.x + topleft.x);
		p.y = (short) (this.y + topleft.y);
		return p;
	}

}