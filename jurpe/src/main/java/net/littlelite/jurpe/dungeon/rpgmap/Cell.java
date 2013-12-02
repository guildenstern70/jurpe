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
import java.util.ArrayList;

import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.crawler.RpgMapPoint;
import net.littlelite.jurpe.dungeon.furnishing.Door;
import net.littlelite.jurpe.world.Location;
import net.littlelite.jurpe.world.LocationType;

/**
 * Cell is the logical abstraction of a dungeon cell. A cell contains six
 * passages. A cell with six passages closed is a wall. Can contain doors and
 * other furnishing.
 */
public final class Cell implements Serializable
{

	private static final long serialVersionUID = 3321L;
	private static int cellCount = 0;

	protected boolean visited;
	protected RpgMapPoint coordinates;
	protected int id;
	protected CellPassages passages;
	protected Location location;
	protected boolean lit;
	protected boolean wasLit;

	/**
	 * Constructor. Use setCoordinates after this call to set cell coordinates.
	 * 
	 */
	public Cell()
	{
		this.id = Cell.cellCount++;
		this.visited = false;
		this.passages = new CellPassages();
		this.location = new Location(LocationType.CORRIDOR);
	}

	/**
	 * Constructor
	 * 
	 * @param xx
	 *            X coordinate of cell
	 * @param yy
	 *            Y coordinate of cell
	 */
	public Cell(short xx, short yy)
	{
		this.id = Cell.cellCount++;
		this.coordinates = new RpgMapPoint(xx, yy);
		this.visited = false;
		this.passages = new CellPassages();
		this.location = new Location(LocationType.CORRIDOR);
	}

	/**
	 * Constructor
	 * 
	 * @param xx
	 *            X coordinate of cell
	 * @param yy
	 *            Y coordinate of cell
	 * @param location
	 *            type of this cell (ie: inn, trainer, normal...)
	 */
	public Cell(short xx, short yy, LocationType location)
	{
		this.coordinates = new RpgMapPoint(xx, yy);
		this.visited = false;
		this.passages = new CellPassages();
		this.location = new Location(location);
	}

	/**
	 * If this cell has been visited
	 */
	public boolean isVisited()
	{
		return this.visited;
	}

	/**
	 * If this cell has been visited
	 * 
	 * @param isVisited
	 */
	public void setVisited(boolean isVisited)
	{
		this.visited = isVisited;
	}

	/**
	 * Set cell coordinates
	 */
	public void setCoordinates(short x, short y)
	{
		this.coordinates = new RpgMapPoint(x, y);
	}

	/**
	 * The passages from this cell
	 * 
	 * @return The passages from this cell
	 */
	public CellPassages getPassages()
	{
		return this.passages;
	}

	/**
	 * @return Location
	 */
	public Location getLocation()
	{
		return this.location;
	}

	/**
	 * Set location type
	 * 
	 * @param location
	 *            LocationType
	 */
	public void setLocation(LocationType location)
	{
		this.location = new Location(location);
	}

	/**
	 * Set location type and message.
	 * 
	 * @param message
	 *            Message shown when selecting location
	 * @param location
	 *            LocationType
	 */
	public void setLocation(LocationType location, String message)
	{
		this.location = new Location(location);
		this.location.setMessage(message);
	}

	/**
	 * Return true if this cell is not a wall or a corridor.
	 * 
	 * @return boolean
	 */
	public boolean isSpecial()
	{
		return this.location.isSpecial();
	}

	/**
	 * Return true if this cell is lit (by a torch...)
	 * 
	 * @return true if this cell is lit
	 */
	public boolean isLit()
	{
		return this.lit;
	}

	/**
	 * Return true if this cell has been lit (discovered) by the gamer
	 * 
	 * @return
	 */
	public boolean wasLit()
	{
		return this.wasLit;
	}

	/**
	 * Set this cell lit or not
	 * 
	 * @param isEnlight
	 */
	public void setLit(boolean isEnlight)
	{
		// If this is the first time the cell is being lit,
		// then turn on the 'wasLit' flag, so the cell has
		// been discovered.
		if (isEnlight)
		{
			if (!this.wasLit)
			{
				this.wasLit = true;
			}
		}
		this.lit = isEnlight;
	}

	/**
	 * Get cell coordinates
	 * 
	 * @return cell coordinates
	 */
	public RpgMapPoint getCoordinates()
	{
		return this.coordinates;
	}

	/**
	 * Get the distance (in number of cells) from the given RpgMapPoint.
	 * 
	 * @param point
	 * @return
	 */
	public int getDistanceFrom(RpgMapPoint point)
	{
		return this.coordinates.getDistanceFrom(point);
	}

	/**
	 * A cell without exits is considered a wall
	 * 
	 * @return true if this cell is a wall
	 */
	public boolean isWall()
	{
		return this.passages.areAllClosed();
	}

	/**
	 * This cell becomes a wall.
	 */
	public void setWall()
	{
		this.passages.setAll(false);
		this.location = new Location(LocationType.WALL);
	}

	/**
	 * Determine if this cell is a wall or not
	 * 
	 * @param isWall
	 *            true if this cell is a wall
	 */
	public void setWall(boolean isWall)
	{
		if (isWall)
		{
			this.setWall();
		}
		else
		{
			this.passages.setAll(true);
			this.location = new Location(LocationType.CORRIDOR);
		}
	}

	/**
	 * Get all directions with doors that are opened or closed
	 * 
	 * @param open
	 *            If true, returns opened doors. Else, closed doors
	 * @return All opened or closed doors in a cell
	 * @throws JurpeException
	 */
	public Direction[] getDoors(boolean open)
	{
		Direction[] dirs = null;

		if (this.isWall())
		{
			dirs = null;
		}
		else
		{
			ArrayList<Direction> doorDirections = new ArrayList<Direction>(6);
			Passage pass = null;

			for (Direction d : Direction.values())
			{
				pass = this.passages.getPassage(d);
				if (pass.hasDoor())
				{
					Door passDoor = pass.getDoor();
					if (passDoor.isOpen() == open)
					{
						doorDirections.add(d);
					}
				}
			}

			if (doorDirections.size() > 0)
			{
				dirs = doorDirections.toArray(new Direction[1]);
			}
		}

		return dirs;

	}

	/**
	 * Get all directions with doors
	 * 
	 * @return Direction[]
	 * @throws JurpeException
	 */
	public Direction[] getDoors()
	{
		Direction[] dirs = null;

		if (this.isWall())
		{
			dirs = null;
		}
		else
		{
			ArrayList<Direction> doorDirections = new ArrayList<Direction>(6);
			Passage pass = null;

			for (Direction d : Direction.values())
			{
				pass = this.passages.getPassage(d);
				if (pass.hasDoor())
				{
					doorDirections.add(d);
				}
			}

			if (doorDirections.size() > 0)
			{
				dirs = doorDirections.toArray(new Direction[1]);
			}
		}

		return dirs;
	}

	/**
	 * Set a door. A door exist in the passages of this cell, and in the ones of
	 * the connected cell.
	 * 
	 * @param dir
	 *            Direction
	 */
	public void setDoor(Direction dir)
	{
		this.passages.getPassage(dir).setDoor(new Door());
	}

	/**
	 * Return all the directions in which this cell may have a door. A cell is
	 * doorable if it has at least one corridor between two non-corridors.
	 * 
	 * @return Direction
	 */
	public Direction[] getDoorables()
	{
		Direction[] ds = null;
		// Maximum doorables dirs per cell are 4
		ArrayList<Direction> directions = new ArrayList<Direction>(4);

		Passage passage, left, right;

		for (Direction d : Direction.values())
		{
			passage = this.passages.getPassage(d);
			if (!passage.isClosed())
			{
				left = this.passages.getLeftward(d);
				right = this.passages.getRightward(d);

				if (left.isClosed() && (right.isClosed()))
				{
					directions.add(d);
				}
			}
		}

		if (directions.size() > 0)
		{
			ds = directions.toArray(new Direction[1]);
		}

		return ds;
	}

	/**
	 * Return true if the passage between this cell and the cell in d direction
	 * is a corridor, that is, it's opened.
	 * 
	 * @param dir
	 *            Direction adiacent cell
	 * @return true if the passage between this cell and the cell in d direction
	 *         is opened
	 */
	public boolean isCorridor(Direction dir)
	{
		return !(this.passages.getPassage(dir).isClosed());
	}

	/**
	 * Return true if there is a door in the direction dir
	 * 
	 * @param dir
	 *            Direction in which looking for a door
	 * @return True if door is found in direction dir
	 */
	public boolean isDoor(Direction dir)
	{
		return this.passages.getPassage(dir).hasDoor();
	}

	/**
	 * Set if the passage between this cell and the cell in dir direction is
	 * opened
	 * 
	 * @param dir
	 *            Direction from the center
	 * @param opened
	 *            true if the passage in this direction is opened
	 */
	public void setCorridor(Direction dir, boolean opened)
	{
		this.passages.getPassage(dir).setOpen(opened);
	}

	/**
	 * Get the number of open corridors leading out of this cell.
	 * 
	 * @return the number of open corridors leading out of this cell
	 */
	public short getNumberOfCorridors()
	{
		short counter = 0;

		for (Passage p : this.passages.getPassages())
		{
			if (p.isOpened())
			{
				counter++;
			}
		}

		return counter;
	}

	/**
	 * String with cell coordinates
	 * 
	 * @return String with cell coordinates
	 */
        @Override
	public String toString()
	{
		String output = "RpgMap Cell " + this.id + " @ " + this.coordinates;

		if (this.isWall())
		{
			output += " (WALL)";
		}
		else if (this.isSpecial())
		{
			output += " (SPECIAL)";
		}

		for (Passage pd : this.passages.getPassages())
		{
			output += "[";
			output += pd.toString();
			output += "]";
		}

		return output;
	}

}
