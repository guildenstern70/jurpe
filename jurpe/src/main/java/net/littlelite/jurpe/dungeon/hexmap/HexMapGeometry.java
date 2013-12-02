package net.littlelite.jurpe.dungeon.hexmap;

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

import net.littlelite.jurpe.dungeon.crawler.HexMapPoint;
import net.littlelite.jurpe.dungeon.crawler.HexagonCalc;
import net.littlelite.jurpe.dungeon.crawler.ViewMapPoint;
import net.littlelite.jurpe.dungeon.rpgmap.MapView;
import net.littlelite.jurpe.system.*;

/**
 * Geometry helper class for HexMap JComponent. In this class all hexparts
 * calculation is handled. Also, matrix corrispondence between hexagons and
 * physical hexagon representation is handles.
 */
public class HexMapGeometry
{
	private HexMap parent;
	private HexagonCalc currentHexagon; // Hexagon coordinates facilities
	public HexMapDrawing drawing;

	public HexMapGeometry(HexMap map)
	{
		this.parent = map;
		this.currentHexagon = new HexagonCalc();
		this.currentHexagon.setPixels(map.getWidth() / 2, map.getHeight() / 2);
		this.drawing = new HexMapDrawing(this);
	}

	public HexMap getHexMap()
	{
		return this.parent;
	}

	public HexagonCalc getCurrentHexagon()
	{
		return this.currentHexagon;
	}

	/**
	 * Get hexagon geometry given an insider point physical coordinates
	 * 
	 * @param x
	 *            Physical x coordinate
	 * @param y
	 *            Physical y coordinate
	 * @return HexagonCalc object
	 */
	private HexagonCalc getHexagonCalc(int x, int y)
	{
		HexagonCalc c = new HexagonCalc();
		c.setPixels(x, y);
		return c;
	}

	/**
	 * Get physical hexagon center coordinates, given some physical coordinates
	 * inside the hexagon
	 * 
	 * @param x
	 *            X logical coordinate of hexagon
	 * @param y
	 *            Y logical coordinate of hexagon
	 * @return Physical center coordinates
	 */
	public Point getPhysicalCenterCoordinates(int x, int y)
	{
		HexagonCalc hex = this.getHexagonCalc(x, y);
		return hex.getHexagonCenter();
	}

	/**
	 * Get the physical center coordinates from logical coordinates
	 * 
	 * @param vP
	 *            ViewMap point
	 * @return Physical center coordinates
	 */
	public HexMapPoint getPhysicalCoordinatesFromLogical(ViewMapPoint vP)
	{
		int physX = (vP.x) * (Config.HEXAGON_WIDTH);
		int physY = (vP.y) * (Config.HEXAGON_HEIGHT);
		if ((vP.x % 2) == 0)
		{
			physY -= (Config.HEXAGON_HEIGHT / 2);
		}

		HexMapPoint p = new HexMapPoint((short) physX, (short) physY);

		return p;
	}

	/**
	 * Get logical hexagon center coordinates, given the hexagon physical
	 * coordinates
	 * 
	 * @param view
	 *            Current viewmap
	 * @param c
	 *            Physical Coordinates
	 * @return Logical coordinates in RpgMap
	 */
	public ViewMapPoint getLogicalCoordinatesFromPhysical(MapView view, Point c)
	{
		ViewMapPoint p = new ViewMapPoint(view);

		this.currentHexagon.setPixels(c.x, c.y);
		p.x = (short) (this.currentHexagon.getHexagonX());
		p.y = (short) (this.currentHexagon.getHexagonY());

		System.out.println("Logical from phys:" + p.toString());

		return p;
	}

	/**
	 * Convert hexagon geometry coordinates into HexMap physical coordinates
	 * 
	 * @param c
	 *            Coordinates of hexagon
	 * @return An hexagon calc object
	 */
	public HexagonCalc getHexagonCalc(Point c)
	{
		return this.getHexagonCalc(c.x, c.y);
	}

}