package net.littlelite.jurpe.dungeon;

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

import java.util.AbstractList;
import java.util.LinkedList;
import java.util.Random;

import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.hexmap.HexMap;
import net.littlelite.jurpe.dungeon.rpgmap.Cell;
import net.littlelite.jurpe.dungeon.rpgmap.RpgMap;
import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.Core;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.JurpeUtils;
import net.littlelite.jurpe.system.Log;
import net.littlelite.jurpe.world.LocationType;

/**
 * Dungeons is a representation of a multi-level Dungeon made by maps of
 * hexagonal cells. The class holds references with other system objects.
 * 
 * @see net.littlelite.jurpe.dungeon.rpgmap.RpgMap
 * @see net.littlelite.jurpe.dungeon.Avatar
 * @see net.littlelite.jurpe.dungeon.generator.IDungeonGenerator
 */
public class Dungeons
{
	private Core coreHandle;

	protected Avatar avatar;
	protected DungeonLevels levels;
	protected Log log;

	private AbstractList<Placeholder> attackingMonsters;
	private boolean monsterAttackFlag;

	/**
	 * Constructor
	 * 
	 * @param system
	 *            Handle to core system.
	 */
	public Dungeons(Core system)
	{
		this.coreHandle = system;
		this.log = this.coreHandle.getLog();
	}

	/**
	 * Initialize dungeon levels
	 */
	public void initializeLevels()
	{
		this.levels = new  DungeonLevels(this, Config.INITIAL_DUNGEON_LEVELS);
		if (this.avatar == null)
		{
			this.avatar = Avatar.getAvatar(this.coreHandle.getRandomSeed(), this);
		}
	}

	/**
	 * Get random seed
	 * 
	 * @return Get random seed
	 */
	public Random getRandomSeed()
	{
		return this.coreHandle.getRandomSeed();
	}

	/**
	 * Handle to Dungeon level
	 * 
	 * @param level
	 *            Level of dungeon
	 * @return Level of dungeon level
	 * @throws JurpeException
	 */
	public Level getDungeonLevel(int level) throws JurpeException
	{
		Level l = this.levels.getDungeonLevel(level);
		if (l == null)
		{
			throw new JurpeException("No Level " + level);
		}
		return l;
	}

	/**
	 * Return dungeon levels container
	 * 
	 * @return DungeonLevels
	 */
	public DungeonLevels getDungeonLevels()
	{
		return this.levels;
	}

	/**
	 * The level where avatar is
	 * 
	 * @return Level
	 * @throws JurpeException
	 */
	public Level getCurrentLevel() throws JurpeException
	{
		if (this.avatar == null)
		{
			return null;
		}

		return this.avatar.getPlaceholder().getLevel();
	}

	/**
	 * @param level
	 *            int
	 * @throws JurpeException
	 */
	public void setCurrentLevel(Level level) throws JurpeException
	{
		if (this.avatar == null)
		{
			throw new JurpeException("Dungeon->getCurrentLevel : Avatar not initialized. Call initAvatar()");

		}
		this.avatar.setLevel(level);
	}

	/**
	 * Get current map
	 * 
	 * @return RpgMap Current map. Returns null if avatar is not created.
	 */
	public RpgMap getCurrentMap() throws JurpeException
	{
		RpgMap map = null;

		if (this.avatar != null)
		{
			int level = this.avatar.getZLevel();
			map = this.getDungeonLevel(level).getRpgMap();
		}

		return map;
	}

	/**
	 * The current map level is where avatar is
	 * 
	 * @return Avatar's level
	 */
	public int getCurrentMapLevel()
	{
		return this.avatar.getZLevel();
	}

	/**
	 * Handle to Avatar
	 * 
	 * @return Avatar
	 */
	public Avatar getAvatar()
	{
		return this.avatar;
	}

	/**
	 * Handle to Log
	 * 
	 * @return Log handle
	 */
	public Log getLog()
	{
		return this.log;
	}

	/**
	 * Set a new Avatar. If an Avatar exists, restore his position. Else create
	 * a new Avatar and set initial position
	 * 
	 * @param hexMapControl
	 *            HexMap HexMap control
	 * @throws JurpeException
	 */
	public void initAvatar(HexMap hexMapControl) throws JurpeException
	{

		if (this.avatar == null)
		{
			this.avatar = Avatar.getAvatar(this.getRandomSeed(), this);
		}
		this.avatar.setInitialPosition(hexMapControl);

	}

	/**
	 * Add Avatar placeholder to current map
	 * 
	 * @throws JurpeException
	 */
	public void addAvatarToCurrentMap() throws JurpeException
	{
		Level currentMap = this.getCurrentLevel();
		if (currentMap == null)
		{
			throw new JurpeException("A map must be initialized before setting Avatar");
		}
		currentMap.addAvatar(this.avatar);
	}

	/**
	 * Draw dungeon into and HexMap control and connect its logic.
	 * 
	 * @param dungeonLevel
	 *            Underground level of dungeon
	 * @param hexMapControl
	 *            HexMap
	 * @throws JurpeException
	 */
	public void drawDungeon(Level dungeonLevel, HexMap hexMapControl) throws JurpeException
	{
		RpgMap map = dungeonLevel.getRpgMap();
		this.addAvatarToCurrentMap();
		if (dungeonLevel.getZ() > 0)
		{
			// Position stairs up
			map.setSpecialCell(this.getAvatar().getPlaceholder().getPosition(), LocationType.STAIRSUP);
		}
		hexMapControl.repaint();
	}

	/**
	 * Get core handle
	 * 
	 * @return Core
	 */
	public Core getSystem()
	{
		return this.coreHandle;
	}

	/**
	 * Reset dungeon (when restarting a new game)
	 */
	public void reset()
	{
		this.avatar = null;
		this.getDungeonLevels().destroyLevelFiles();
	}

	/**
	 * Get a list of attacking monsters
	 * 
	 * @return a list of attacking monsters
	 */
	public AbstractList<Placeholder> getAttackingMonsters()
	{
		if (this.monsterAttackFlag == false)
		{
			return null;
		}

		return this.attackingMonsters;
	}

	/**
	 * Move monsters on map
	 * 
	 * @throws JurpeException
	 */
	public void moveMonsters() throws JurpeException
	{
		this.monsterAttackFlag = false;
		this.attackingMonsters = null;

		// This happens in game over
		if (this.avatar == null)
		{
			return;
		}

		Level currentLevel = this.getCurrentLevel();
		Direction dir;

		for (DungeonMonster dm : currentLevel.getMonsters().getDungeonMonsters())
		{
			dir = Direction.getRandom(this.getRandomSeed());
			Placeholder p = dm.getPlaceholder();

			Cell fromCell = p.getCell();
			Movement mvmnt = new Movement(currentLevel, fromCell, dir);
			if (mvmnt.isMovementAllowed())
			{
				if (JurpeUtils.successRoll(dm.getMvmt()))
				{
					p.move(dir);
				}
			}

			String feedback = mvmnt.getFeedback();
			if (feedback != null)
			{
				if (feedback.equals("Monster attack"))
				{
					this.monsterAttackFlag = true;
					if (this.attackingMonsters == null)
					{
						this.attackingMonsters = new LinkedList<Placeholder>();
					}
					this.attackingMonsters.add(p);
				}
			}
		}

	}

}
