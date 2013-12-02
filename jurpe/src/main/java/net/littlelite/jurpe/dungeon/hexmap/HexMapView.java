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

import java.util.Random;

import net.littlelite.jurpe.dungeon.crawler.RpgMapPoint;
import net.littlelite.jurpe.dungeon.rpgmap.Cell;
import net.littlelite.jurpe.dungeon.rpgmap.Drawer;
import net.littlelite.jurpe.dungeon.rpgmap.MapView;
import net.littlelite.jurpe.dungeon.rpgmap.RpgMap;

/**
 * The HexMapView class offers methods related to HexMap and RpgMap classes.
 */
public class HexMapView
{

	private HexMap hexMapControl;
	private RpgMap rpgMap;
	private MapView view;
	private Drawer drawer;

	/**
	 * Construct a new view of the HexMap. A view is a matrix of cells that will
	 * be displayed on the user interface.
	 * 
	 * @param sHexMap
	 *            HexMap
	 * @param sRpgMap
	 *            RpgMap
	 */
	public HexMapView(HexMap sHexMap, RpgMap sRpgMap)
	{
		this.hexMapControl = sHexMap;
		this.rpgMap = sRpgMap;
		short width = this.hexMapControl.getWidthInHexagons();
		short height = this.hexMapControl.getHeightInHexagons();
		this.view = new MapView(this.rpgMap, width, height);
		this.drawer = new Drawer(this);
	}

	/**
	 * Get the RpgMap associated to this view
	 * 
	 * @return RpgMap
	 */
	public RpgMap getRpgMap()
	{
		return this.rpgMap;
	}

	/**
	 * Reload RpgMap into the HexMapView
	 * 
	 * @param map
	 */
	public void reloadRpgMap(RpgMap map)
	{
		this.rpgMap = map;
	}

	/**
	 * Get the HexMap control to show this view
	 * 
	 * @return HexMap
	 */
	public HexMap getHexMap()
	{
		return this.hexMapControl;
	}

	/**
	 * Handle to Map drawer tool
	 * 
	 * @return Handle to Map drawer tool
	 */
	public Drawer getDrawer()
	{
		return this.drawer;
	}

	/**
	 * Get a random RpgMapPoint inside RpgMapView
	 * 
	 * @param rndgen
	 *            Random seed
	 * @return A random RpgMapCell inside the displayable view
	 */
	public RpgMapPoint getRandomDisplayablePoint(Random rndgen)
	{
		RpgMapPoint topleft = this.view.getTopLeft();

		short x = 0, y = 0;

		while (x == 0)
		{
			x = (short) rndgen.nextInt(this.view.getWidth() - 1);
		}

		while (y == 0)
		{
			y = (short) rndgen.nextInt(this.view.getHeight() - 1);
		}

		topleft.x += x;
		topleft.y += y;

		return topleft;
	}

	/**
	 * Get a random cell in this map. This cell cannot be where already is a
	 * wall or a special.
	 * 
	 * @param rnd
	 *            Random generator
	 * @param nextToCenter
	 *            if true, it will try to return a cell next to the center of
	 *            the dungeon
	 * @return null if no cell is found. Else return the wanted cell
	 */
	public Cell getDisplayableEmptyRandomCell(Random rnd, boolean nextToCenter)
	{
		Cell initial = null;
		RpgMapPoint iPos = null;

		if (nextToCenter)
		{
			iPos = this.getDisplayableCenter();
			initial = this.rpgMap.getCell(iPos);
		}
		else
		{
			initial = this.rpgMap.getRandomCell();
		}

		short tentatives = 0, count = 0;

		while (initial.isWall() || initial.isSpecial())
		{
			tentatives++;

			if ((nextToCenter) && (tentatives < 25))
			{
				iPos.x++;
				count++;

				if (count > 5)
				{
					iPos.x -= 5;
					iPos.y++;
					count = 0;
				}
			}
			else
			{
				iPos = this.getRandomDisplayablePoint(rnd);
			}

			if (tentatives > 100)
			{
				initial = null;
				break;
			}

			initial = this.rpgMap.getCell(iPos);
		}

		if (initial.isWall())
		{
			System.out.println("Cell is wall!");
		}

		return initial;
	}

	/**
	 * Get the coordinates of the center in the view map as a RgpMapCell
	 * 
	 * @return RpgMapPoint Coordinates of the center in the viewmap;
	 */
	public RpgMapPoint getDisplayableCenter()
	{
		RpgMapPoint topleft = this.view.getTopLeft();

		short x = (short) (this.view.getWidth() / 2);
		short y = (short) (this.view.getHeight() / 2);

		topleft.x += x;
		topleft.y += y;

		return topleft;
	}

	/**
	 * Get Map View
	 * 
	 * @return
	 */
	public MapView getMapView()
	{
		return this.view;
	}

	/**
	 * Return true if the point is over the edge of this view.
	 * 
	 * @return true if the point is over the edge of this view.
	 */
	public boolean isOnTheEdge(RpgMapPoint p)
	{
		boolean isEdge = false;

		if (p.x == this.view.getCurrentLeft() || p.x == this.view.getCurrentRight() || p.y == this.view.getCurrentTop() || p.y == this.view.getCurrentBottom())
		{
			isEdge = true;
		}

		return isEdge;
	}

	/**
	 * ToString
	 */
        @Override
	public String toString()
	{
		String s = "View [" + this.view.getCurrentLeft() + "," + this.view.getCurrentTop() + "," + this.view.getWidth() + "," + this.view.getHeight()
				+ "] of [0,0," + this.hexMapControl.getWidth() + "," + this.hexMapControl.getHeight() + "]";
		return s;
	}

}
