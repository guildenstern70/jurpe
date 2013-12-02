package net.littlelite.jurpe.dungeon.rpgmap;

/**
 J.U.R.P.E. @version@ (DungeonCrawler Package)
 Copyright (C) 2002-05 LittleLite Software

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
import java.util.Random;

import net.littlelite.jurpe.dungeon.crawler.AbstractHexPoint;
import net.littlelite.jurpe.dungeon.crawler.RpgMapPoint;

/**
 * 
 * 
 */
public class MapTiles implements Serializable
{
	private static final long serialVersionUID = 3319L;

	protected short mapWidth, mapHeight;

	private final Cell[][] tiles;

	/**
	 * Constructor
	 * 
	 * @param width
	 *            map width
	 * @param height
	 *            map height
	 */
	public MapTiles(short width, short height)
	{
		this.mapWidth = width;
		this.mapHeight = height;
		this.tiles = new Cell[this.mapWidth][this.mapHeight];
		this.initMap();
	}

	/**
	 * Get a cell
	 * 
	 * @param x
	 *            X coordinate of cell
	 * @param y
	 *            Y coordinate of cell
	 * @return Wanted cell
	 */
	public Cell getCell(short x, short y)
	{
		Cell cell;

		if (this.isInside(x, y))
		{
			cell = this.tiles[x][y];
		}
		else
		{
			cell = new Cell(x, y);
		}

		return cell;
	}

	/**
	 * Get a MapCell in p point hexagon coordinates
	 * 
	 * @param p
	 *            Point coordinates of hexagon
	 * @return RpgMapCell in p point coordinates
	 */
	public Cell getCell(AbstractHexPoint p)
	{
		return this.getCell(p.x, p.y);
	}

	/**
	 * Width of RpgMap in Hexagons
	 * 
	 * @return Width of RpgMap in Hexagons
	 */
	public short getWidth()
	{
		return this.mapWidth;
	}

	/**
	 * Height of RpgMap in Hexagons
	 * 
	 * @return Height of RpgMap in Hexagons
	 */
	public short getHeight()
	{
		return this.mapHeight;
	}

	/**
	 * Get a random RpgMapCell
	 * 
	 * @param rndgen
	 *            Random seed
	 * @return A random RpgMapCell (coordinates 0-(sizeX-2) and 0-(sizeY-2))
	 */
	public Cell getRandomCell(Random rndgen)
	{
		short x = (short) rndgen.nextInt(this.mapWidth - 1);
		short y = (short) rndgen.nextInt(this.mapHeight - 1);
		return this.getCell(x, y);
	}

	/**
	 * Returns true if the position is inside the RpgMap width and height
	 * 
	 * @param position
	 *            Point coordinates of an hexagon
	 * @return True if the position is inside the RpgMap width and height
	 */
	public boolean isInside(RpgMapPoint position)
	{
		return this.isInside(position.x, position.y);
	}

	/**
	 * Returns true if the position is inside the RpgMap width and height
	 * 
	 * @param x
	 *            X coordinate of an hexagon
	 * @param y
	 *            Y coordinate of an hexagon
	 * @return True if the position is inside the RpgMap width and height
	 */
	public boolean isInside(short x, short y)
	{

		boolean isIn = false;

		if ((x < this.mapWidth) && (x >= 0))
		{
			if ((y < this.mapHeight) && (y >= 0))
			{
				isIn = true;
			}
		}

		return isIn;
	}

	/**
	 * Returns true if all cells in the map were visited
	 * 
	 * @return true if all cells in the map were visited
	 */
	public boolean areAllVisitedCells()
	{
		int w = this.getWidth();
		int h = this.getHeight();
		int mapsize = w * h;
		int counter = 0;
		boolean allVisited = false;

		for (short x = 0; x < w; x++)
		{
			for (short y = 0; y < h; y++)
			{
				if (this.tiles[x][y].visited)
				{
					counter++;
				}
			}
		}

		if (counter == mapsize)
		{
			allVisited = true;
		}

		return allVisited;
	}

	private void initMap()
	{
		for (short x = 0; x < this.mapWidth; x++)
		{
			for (short y = 0; y < this.mapHeight; y++)
			{
				this.tiles[x][y] = new Cell(x, y);
			}
		}
	}

}
