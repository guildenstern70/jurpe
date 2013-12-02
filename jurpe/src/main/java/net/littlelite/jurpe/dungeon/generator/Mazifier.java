package net.littlelite.jurpe.dungeon.generator;

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

import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.rpgmap.Cell;
import net.littlelite.jurpe.dungeon.rpgmap.MapIterator;
import net.littlelite.jurpe.dungeon.rpgmap.RpgMap;
import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.Log;

/**
 * Mazifier class. Create a new maze. The first basic IDungeonGenerator class.
 */
public class Mazifier implements IDungeonGenerator
{

	protected short ww;
	protected short hh;
	protected Random rndgen; // Random seed
	protected Log log;

	/**
	 * Constructor
	 * 
	 * @param rndSeed
	 *            Random Seed
	 * @param sizex
	 *            Maze width
	 * @param sizey
	 *            Maze height
	 */
	public Mazifier(Random rndSeed, Log log, short sizex, short sizey)
	{
		this.log = log;
		this.ww = sizex;
		this.hh = sizey;
		this.rndgen = rndSeed;
	}

	/**
	 * Create dungeon
	 * 
	 * @return Newly created dungeon
	 */
	public RpgMap createDungeon(int level) throws JurpeException
	{
		RpgMap rawDungeon = null;

		rawDungeon = this.drawPerfectMaze();
		this.closeEdges(rawDungeon);

		return rawDungeon;
	}

	/**
	 * Create dungeon with perfect algorithm (try to)
	 * 
	 * @return Newly created dungeon
	 * @throws JurpeException
	 */
	protected RpgMap drawPerfectMaze() throws JurpeException
	{

		RpgMap logicMap = new RpgMap(this.rndgen, this.ww, this.hh);
		this.log.addDetail("Generating dungeon...");

		Cell fromCell = null;
		Cell toCell = null;
		boolean isValidDirection;
		Direction d = null;
		boolean pickedDirs[] = new boolean[6];
		int steps = 0;
		int minsteps = logicMap.getWidth() * logicMap.getHeight();
		int visitedCounter = 0;

		while (true)
		{
			steps++;

			// 1. Pick a random cell and mark it visited
			if (fromCell == null)
			{
				fromCell = logicMap.getRandomCell();
			}
			fromCell.setVisited(true);

			if ((steps > minsteps) && (steps % 10 == 0))
			{
				visitedCounter++;
				// check if all cells where visited
				if (logicMap.areAllVisitedCells())
				{
					log.addDetail("Perfect Maze Created with " + visitedCounter + " trials");
					break;
				}

				if (visitedCounter > 1000)
				{
					log.addDetail("Less Than Perfect Maze Created (over " + visitedCounter + " trials)");
					break;
				}
			}

			isValidDirection = false;
			for (short j = 0; j < 6; j++)
			{
				pickedDirs[j] = false;
			}

			while (!isValidDirection)
			{
				// 2. From current cell, pick random direction
				d = Direction.getRandom(rndgen);
				isValidDirection = true;

				// 3. Did we already pick all directions?
				pickedDirs[d.getValue()] = true;
				if (this.isAllTrue(pickedDirs)) // all directions where searched
				{
					// Pick another cell
					fromCell = null;
					break;
				}

				// 3. Get adiacent cell in that direction
				toCell = logicMap.getCell(fromCell, d);
				if (toCell == null)
				{
					isValidDirection = false;
				}
				else
				{
					if (toCell.isVisited())
					{
						isValidDirection = false;
					}
				}
			}

			if (fromCell != null)
			{
				fromCell.setCorridor(d, true); // open corridor forward
				toCell.setCorridor(Direction.getInverse(d), true); // open
				// backward
				fromCell = toCell;
			}
		}

		this.log.addDetail("New dungeon [" + this.ww + "," + this.hh + "] generated");

		return logicMap;

	}

	protected void closeEdges(RpgMap map)
	{
		MapIterator i = map.iterator();

		while (i.hasNext())
		{
			Cell cell = i.next();

			short xc = cell.getCoordinates().x;
			short yc = cell.getCoordinates().y;

			if ((xc < 1) || (xc > Config.DUNGEON_WIDTH - 1))
			{
				cell.setWall();
			}

			if ((yc < 1) || (yc > Config.DUNGEON_HEIGHT - 1))
			{
				cell.setWall();
			}
		}
	}

	/**
	 * Reset visited flag. It will serve for dungeon discovering. See
	 * Drawer.refresh()
	 * 
	 * @param map
	 */
	protected void resetVisited(RpgMap map)
	{
		MapIterator i = map.iterator();

		while (i.hasNext())
		{
			Cell cell = i.next();
			cell.setVisited(false);
		}
	}

	/**
	 * Perimeters cell are made walls. This method was added to fix a bug in
	 * dungeon generator
	 * 
	 * @param map
	 *            Map to close
	 */
	protected void closePerimeter(RpgMap map)
	{
		Cell[] pCell = new Cell[2];
		short www = (short) (this.ww - 1);
		short hhh = (short) (this.hh - 1);

		for (short x = 0; x < www; x++)
		{
			pCell[0] = map.getCell(x, (short) 0);
			if (!pCell[0].isWall())
			{
				pCell[0].setWall();
			}

			pCell[1] = map.getCell(x, hhh);
			if (!pCell[1].isWall())
			{
				pCell[1].setWall();
			}

		}
	}

	protected boolean isAllTrue(boolean[] bools)
	{
		boolean allTrue = false;
		int all = 0;

		for (int j = 0; j < bools.length; j++)
		{
			if (bools[j])
			{
				all++;
			}
		}

		if (all == bools.length)
		{
			allTrue = true;
		}

		return allTrue;
	}

}