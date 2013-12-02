package net.littlelite.jurpe.dungeon.generator;

/**
 * J.U.R.P.E. @version@ (DungeonCrawler Package) Copyright (C) 2002-05
 * LittleLite Software
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

import java.util.Random;

import net.littlelite.jurpe.dungeon.rpgmap.Cell;
import net.littlelite.jurpe.dungeon.rpgmap.MapIterator;
import net.littlelite.jurpe.dungeon.rpgmap.RpgMap;
import net.littlelite.jurpe.system.Log;
import net.littlelite.jurpe.world.LocationType;

/**
 * Create a map without dungeons (open space).
 */
public class OpenSpace implements IDungeonGenerator
{

	protected short ww;
	protected short hh;
	protected Log log;
	protected Random rndgen;

	/**
	 * Constructor
	 * 
	 * @param log
	 *            Log to write output info
	 * @param sizex
	 *            Maze width
	 * @param sizey
	 *            Maze height
	 */
	public OpenSpace(Random rnd, Log log, short sizex, short sizey)
	{
		this.log = log;
		this.ww = sizex;
		this.hh = sizey;
		this.rndgen = rnd;
	}

	/**
	 * Create a new dungeon
	 */
	public RpgMap createDungeon(int level)
	{
		RpgMap map = this.createMap();
		this.addSpecialLocations(map);
		return map;
	}

	/**
	 * Create a dungeon map
	 * 
	 * @return
	 */
	protected RpgMap createMap()
	{
		RpgMap logicMap = new RpgMap(this.rndgen, this.ww, this.hh);
		MapIterator i = logicMap.iterator();

		while (i.hasNext())
		{
			Cell cell = i.next();
			cell.setLocation(LocationType.CORRIDOR);
			cell.getPassages().setAll(true);
		}

		this.log.addEntry("A new village has been created.");

		return logicMap;
	}

	/**
	 * Add special locations
	 * 
	 * @param randSeed
	 *            Random Seed
	 * @param logicMap
	 *            Map to add special locations to
	 */
	protected void addSpecialLocations(RpgMap logicMap)
	{
		this.setLocation(LocationType.SHOP, logicMap);
		log.addDetail("Shop built.");

		this.setLocation(LocationType.STAIRSDOWN, logicMap);
		log.addDetail("Dungeon entrance built.");

		this.setLocation(LocationType.TRAINING, logicMap);
		log.addDetail("Trainer house built.");

		this.setLocation(LocationType.INN, logicMap);
		log.addDetail("Inn built.");
                
                this.setLocation(LocationType.MAGESGUILD, logicMap);
		log.addDetail("Mages Guild built.");

		// add some trees
		for (int j = 0; j < (this.ww * this.hh * .15); j++)
		{
			this.setLocation(LocationType.TREE, logicMap);
		}
		log.addDetail("Added some trees.");

		int nrHouses = (int) (this.ww * this.hh * .1);
		// add some houses
		for (int j = 0; j < nrHouses; j++)
		{
			this.setLocation(LocationType.HOUSE, logicMap);
		}
		log.addDetail("Added " + nrHouses + " houses.");

		String welcome = "The sign reads: 'Welcome to Jurpe Village. ";
		welcome += (nrHouses * 4);
		welcome += " inhabitants'";

		this.setLocation(LocationType.SIGN, logicMap, welcome);
	}

	/**
	 * Modify a random location of a map into a LocationType location
	 * 
	 * @param type
	 *            LocationType
	 * @param logicMap
	 *            Map
	 * @param rndgen
	 *            Random
	 * @param message
	 *            String message to show when user select/enter location
	 */
	private void setLocation(LocationType type, RpgMap logicMap, String message)
	{
		Cell specialCell = logicMap.getRandomCell();

		while (specialCell.isSpecial() || (!this.isOutsidePerimeter(specialCell, logicMap)))
			specialCell = logicMap.getRandomCell();

		if (message != null)
		{
			specialCell.setLocation(type, message);
		}
		else
		{
			specialCell.setLocation(type);
		}
	}

	private void setLocation(LocationType type, RpgMap logicMap)
	{
		this.setLocation(type, logicMap, null);
	}

	private boolean isOutsidePerimeter(Cell cell, RpgMap map)
	{
		boolean isOutside = false;

		int x = cell.getCoordinates().x;
		int y = cell.getCoordinates().y;

		if ((x > 1) && (x < map.getWidth() - 2))
		{
			if ((y > 1) && (y < map.getHeight() - 2))
			{
				isOutside = true;
			}
		}

		return isOutside;
	}

}