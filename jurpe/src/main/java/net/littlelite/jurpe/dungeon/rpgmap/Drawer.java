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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import net.littlelite.jurpe.dungeon.Avatar;
import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.crawler.AbstractHexPoint;
import net.littlelite.jurpe.dungeon.crawler.RpgMapPoint;
import net.littlelite.jurpe.dungeon.crawler.ViewMapPoint;
import net.littlelite.jurpe.dungeon.furnishing.Door;
import net.littlelite.jurpe.dungeon.hexmap.HexMap;
import net.littlelite.jurpe.dungeon.hexmap.HexMapDrawing;
import net.littlelite.jurpe.dungeon.hexmap.HexMapView;
import net.littlelite.jurpe.system.Config;

/**
 * Display an RpgMap on an HexMap control.
 */
public class Drawer
{
	private HexMap hexmap;
	private HexMapDrawing drawer;
	private HexMapView hexMapView;

	/**
	 * Constructor
	 * 
	 * @param sHexMapView
	 *            HexMapView handle
	 */
	public Drawer(HexMapView sHexMapView)
	{
		this.hexMapView = sHexMapView;
		this.hexmap = this.hexMapView.getHexMap();
		this.drawer = this.hexmap.getGeometry().drawing;
	}

	/**
	 * Draw map. The map is drawn by enumerating every Cell object in the
	 * current MapTiles collection.
	 * 
	 * @param g
	 *            Graphics object
	 */
	public void drawMap(Graphics g, Avatar av)
	{
		MapView view = this.hexMapView.getMapView();
		Cell cellToDraw;
		Point pointToDraw;
		ViewMapPoint vP;

		RpgMapPoint avatarLocation = av.getPlaceholder().getPosition();
		MapIterator i = view.iterator();

		while (i.hasNext())
		{
			cellToDraw = i.next(); // the next cell to draw
			vP = new ViewMapPoint(view, cellToDraw.getCoordinates());
			pointToDraw = this.getPhysicalCoordinatesOf(vP);

			if (av.isInVillage()) // every cell is visible
			{
				this.drawLitCell(g, cellToDraw, pointToDraw);
			}
			else
			// compute field of view
			{
				int radius = cellToDraw.getDistanceFrom(avatarLocation);
				if (radius <= 3)
				{
					cellToDraw.setVisited(true);
					this.drawLitCell(g, cellToDraw, pointToDraw);
				}
				else
				{
					this.drawUnlitCell(g, cellToDraw, pointToDraw);
				}
			}
		}
	}

	private void drawLitCell(Graphics g, Cell cellToDraw, Point pointToDraw)
	{
		if (cellToDraw.isSpecial())
		{
			this.drawOpenCell(g, pointToDraw, cellToDraw.getLocation().getColor());
		}
		else
		{
			if (cellToDraw.isWall())
			{
				this.drawWall(g, pointToDraw);
			}
			else
			{
				this.drawOpenCell(g, pointToDraw);
				this.drawDoors(cellToDraw, g, pointToDraw);
			}
		}
	}

	private void drawUnlitCell(Graphics g, Cell cellToDraw, Point pointToDraw)
	{
		if (cellToDraw.isVisited())
		{
			if (cellToDraw.isWall())
			{
				this.drawWall(g, pointToDraw);
			}
			else
			{
				this.drawVisitedCell(g, pointToDraw);
			}
		}
		else
		{
			this.drawWall(g, pointToDraw);
		}
	}

	private void drawDoors(Cell fromCell, Graphics g, Point center)
	{
		Direction dirs[] = fromCell.getDoors();
		if (dirs != null)
		{
			for (Direction dd : dirs)
			{
				Door d = fromCell.getPassages().getPassage(dd).getDoor();
				if (d.isOpen())
				{
					this.drawDoor(g, center, dd, true);
				}
				else
				{
					this.drawDoor(g, center, dd, false);
				}
			}
		}
	}

	private void drawDoor(Graphics g, Point center, Direction d, boolean isOpen)
	{
		this.drawer.drawDoor(g, center, d, isOpen);
	}

	private void drawWall(Graphics g, Point center)
	{
		this.drawer.fillHexagon(g, center, Config.WALL_COLOR);
		this.drawer.drawHexagon(g, center, Config.WALL_COLOR);
	}

	private void drawOpenCell(Graphics g, Point center)
	{
		this.drawer.fillHexagon(g, center, Config.MAZE_BACKGROUND);
		this.drawer.drawHexagon(g, center, Config.HEXAGON_COLOR);
	}

	private void drawVisitedCell(Graphics g, Point center)
	{
		this.drawOpenCell(g, center, Config.VISITED_COLOR);
	}

	private void drawOpenCell(Graphics g, Point center, Color c)
	{
		this.drawer.fillHexagon(g, center, c);
		this.drawer.drawHexagon(g, center, Config.HEXAGON_COLOR);
	}

	private Point getPhysicalCoordinatesOf(ViewMapPoint vp)
	{
		AbstractHexPoint hp = this.hexmap.getGeometry().getPhysicalCoordinatesFromLogical(vp);
		return hp.toPoint();
	}

}