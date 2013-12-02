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
import java.util.Random;

import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.crawler.AbstractHexPoint;
import net.littlelite.jurpe.dungeon.crawler.RpgMapPoint;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.world.LocationType;

/**
 * RpgMap is the description of a single map, that is a level in the HexDungeon
 */
public class RpgMap implements ILogicalMap, Serializable, Iterable<Cell>
{
	private static final long serialVersionUID = 3335L;

	private MapTiles tiles;
	private Random randomSeed;

	/**
	 * Constructor
	 * 
	 * @param rnd
	 *            Random seed
	 * @param sizeX
	 *            Width in hexagons of RpgMap
	 * @param sizeY
	 *            Height in hexagons of RpgMap
	 */
	public RpgMap(Random rnd, short sizeX, short sizeY)
	{
		this.randomSeed = rnd;
		this.tiles = new MapTiles(sizeX, sizeY);
	}

	/**
	 * Return random seed.
	 * 
	 * @return The random seed object
	 */
	public Random getRandomSeed()
	{
		return this.randomSeed;
	}

	/**
	 * Iterator
	 * 
	 * @return RpgMapIterator
	 */
	public MapIterator iterator()
	{
		return new MapIterator(this);
	}

	/**
	 * Width of RpgMap in Hexagons
	 * 
	 * @return Width of RpgMap in Hexagons
	 */
	public short getWidth()
	{
		return this.tiles.getWidth();
	}

	/**
	 * Height of RpgMap in Hexagons
	 * 
	 * @return Height of RpgMap in Hexagons
	 */
	public short getHeight()
	{
		return this.tiles.getHeight();
	}

	/**
	 * Get a random point inside this map
	 * 
	 * @return RpgMapPoint
	 */
	public RpgMapPoint getRandomPoint()
	{
		short x;
		short y;
		RpgMapPoint p = null;

		while (!this.isInside(p))
		{
			x = (short) (this.randomSeed.nextInt(this.getWidth()));
			y = (short) (this.randomSeed.nextInt(this.getHeight()));
			p = new RpgMapPoint(x, y);
		}

		return p;
	}

	/**
	 * Get a random cell in this map
	 * 
	 * @return a random cell in this map
	 */
	public Cell getRandomCell()
	{
		return this.tiles.getRandomCell(this.randomSeed);
	}

	/**
	 * Return true if point p is inside this map.
	 * 
	 * @param p
	 *            RpgPoint to be checked
	 * @return true if point p is inside this map
	 */
	public boolean isInside(RpgMapPoint p)
	{
		boolean isIn = false;

		if (p != null)
		{
			isIn = this.tiles.isInside(p);
		}

		return isIn;
	}

	/**
	 * Set special cell
	 * 
	 * @param p
	 *            RpgMapPoint to set as special
	 * @param type
	 *            Location type
	 * @throws JurpeException
	 */
	public void setSpecialCell(RpgMapPoint p, LocationType type) throws JurpeException
	{
		this.setSpecialCell(p, type, null);
	}

	/**
	 * Set special cell with a message
	 * 
	 * @param message
	 *            Message that this cell displays when actioned
	 * @param p
	 *            Map point
	 * @param type
	 *            Type of cell
	 * @throws JurpeException
	 */
	public void setSpecialCell(RpgMapPoint p, LocationType type, String message) throws JurpeException
	{
		int j = 0;
		Cell specialCell = this.getCell(p);

		while (specialCell.isWall())
		{
			j++;
			specialCell = this.getEmptyRandomCell(false);
			if (j > 100)
			{
				throw new JurpeException("Cannot find a cell that is not a wall or special!");
			}
		}

		if (message != null)
		{
			specialCell.setLocation(type, message);
		}
		else
		{
			specialCell.setLocation(type);
		}
	}

	/**
	 * Get the first found cell of the specified location type
	 * 
	 * @param type
	 *            Special cell type
	 * @return the first found cell of the specified location type
	 */
	public Cell getSpecialCell(LocationType type)
	{
		MapIterator mi = this.iterator();
		Cell foundCell = null;

		while (mi.hasNext())
		{
			Cell temp = mi.next();
			if (temp.isSpecial())
			{
				if (temp.getLocation().getType().equals(type))
				{
					foundCell = temp;
					break;
				}
			}
		}

		return foundCell;
	}

	/**
	 * Get coordinates of Top/Left map point (0,0)
	 * 
	 * @return RpgMapPoint
	 */
	public RpgMapPoint getTopLeft()
	{
		return new RpgMapPoint((short) (0), (short) (0));
	}

	/**
	 * Get coordinates of Bottom/Right map point (w,h)
	 * 
	 * @return RpgMapPoint
	 */
	public RpgMapPoint getBottomRight()
	{
		return new RpgMapPoint(this.getWidth(), this.getWidth());
	}

	/**
	 * Returns true if all cells in the map were visited
	 * 
	 * @return true if all cells in the map were visited
	 */
	public boolean areAllVisitedCells()
	{
		return this.tiles.areAllVisitedCells();
	}

	/**
	 * This method returns the coordinates of the arrival point when starting
	 * from a point a taking a direction.
	 * 
	 * @param from
	 *            Point coordinates of starting hexagon
	 * @param dir
	 *            Direction taken
	 * @return the coordinates in hexagons of the arrival point
	 */
	public RpgMapPoint moveTo(RpgMapPoint from, Direction dir)
	{

		RpgMapPoint fromPoint = from;
		RpgMapPoint toPoint = fromPoint;

		switch (dir)
		{
			case NORTH:
				toPoint = this.up(fromPoint);
				break;

			case SOUTH:
				toPoint = this.down(fromPoint);
				break;

			case NORTHEAST:
				toPoint = this.right(fromPoint);
				if (toPoint.x % 2 != 0)
				{
					toPoint = this.up(toPoint);
				}
				break;

			case NORTHWEST:
				toPoint = this.left(fromPoint);
				if (toPoint.x % 2 != 0)
				{
					toPoint = this.up(toPoint);
				}
				break;

			case SOUTHEAST:
				toPoint = this.right(fromPoint);
				if (toPoint.x % 2 == 0)
				{
					toPoint = this.down(toPoint);
				}
				break;

			case SOUTHWEST:
				toPoint = this.left(fromPoint);
				if (toPoint.x % 2 == 0)
				{
					toPoint = this.down(toPoint);
				}
				break;
		}

		return toPoint;
	}

	/**
	 * Get the coordinates of the center of this map
	 * 
	 * @return RpgMapPoint Center of the map
	 */
	public RpgMapPoint getCenter()
	{
		return new RpgMapPoint((short) (this.getWidth() / 2), (short) (this.getHeight() / 2));

	}

	/**
	 * Get a random cell in this map that is not a wall or a special cell.
	 * Optionally can be a cell "next to the center".
	 * 
	 * @param nextToCenter
	 *            if true, it will try to return a cell next to the center of
	 *            the dungeon
	 * @return null if no cell is found. Else return the wanted cell
	 */
	public Cell getEmptyRandomCell(boolean nextToCenter)
	{
		Cell initial = null;
		RpgMapPoint iPos = null;

		if (nextToCenter)
		{
			iPos = this.getCenter();
			initial = this.getCell(iPos);
		}
		else
		{
			initial = this.getRandomCell();
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
				iPos = this.getRandomPoint();
			}

			if (tentatives > 100)
			{
				initial = null;
				break;
			}

			initial = this.getCell(iPos);
		}

		return initial;
	}

	/**
	 * Get Cell
	 * 
	 * @param p
	 *            AbstractHexPoint
	 * @return The cell at hexpoint p
	 */
	public Cell getCell(AbstractHexPoint p)
	{
		return this.tiles.getCell(p);
	}

	/**
	 * Get Cell
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate AbstractHexPoint
	 * @return The cell at hexpoint(x,y)
	 */
	public Cell getCell(short x, short y)
	{
		return this.tiles.getCell(new RpgMapPoint(x, y));
	}

	/**
	 * Get all adiacent cells of cell 'from'
	 * 
	 * @param from
	 *            The cell from
	 * @return An array of 6 adiacent cells
	 * @throws JurpeException
	 */
	public Cell[] getAdiacentCells(Cell from) throws JurpeException
	{
		Cell[] cells = new Cell[6];

		for (short j = 0; j < 6; j++)
		{
			cells[j] = this.getCell(from, Direction.fromValue(j));
		}

		return cells;
	}

	/**
	 * Get RpgMapCell that is located in direction 'to' starting from 'from'
	 * Cell
	 * 
	 * @param from
	 *            Initial RpgMapCell
	 * @param to
	 *            Direction to go to
	 * @return Cell that will be reached in that direction
	 */
	public Cell getCell(Cell from, Direction to)
	{
		Cell toCell = null;
		RpgMapPoint fromCoordinates = from.getCoordinates();
		RpgMapPoint toCoordinates = this.moveTo(fromCoordinates, to);

		if (this.isInside(toCoordinates))
		{
			toCell = this.getCell(toCoordinates);
		}

		return toCell;
	}

	private RpgMapPoint left(RpgMapPoint position)
	{
		short x = position.x;
		if (x > 0)
		{
			x--;
		}
		return new RpgMapPoint(x, position.y);
	}

	private RpgMapPoint right(RpgMapPoint position)
	{
		short x = position.x;
		if (x < this.getWidth())
		{
			x++;
		}

		return new RpgMapPoint(x, position.y);
	}

	private RpgMapPoint up(RpgMapPoint position)
	{
		short y = position.y;
		if (y > 0)
		{
			y--;
		}

		return new RpgMapPoint(position.x, y);
	}

	private RpgMapPoint down(RpgMapPoint position)
	{
		short y = position.y;
		if (y < this.getHeight())
		{
			y++;
		}

		return new RpgMapPoint(position.x, y);
	}

}
