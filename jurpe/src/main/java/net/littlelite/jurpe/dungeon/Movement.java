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

import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.crawler.RpgMapPoint;
import net.littlelite.jurpe.dungeon.furnishing.Door;
import net.littlelite.jurpe.dungeon.rpgmap.Cell;
import net.littlelite.jurpe.system.resources.LibraryStrings;
import net.littlelite.jurpe.world.Location;

/**
 * The class Movement takes in input a direction in the current level, scan the
 * cell in the destination and returns if the desired movement is allowed and a
 * feedback string.
 * 
 */
public class Movement
{
	private Level level;
	private Cell from, to;
	private Direction movement;
	private boolean movementAllowed;
	private String feedback;
	private String monsterName;
	private boolean allowOverMonsters; // allow movement over monsters

	/**
	 * Create a movement instance
	 * 
	 * @param dungeonLevel
	 *            Current level
	 * @param fromCell
	 *            Starting cell
	 * @param dir
	 *            Direction from starting cell
	 * @param toCell
	 *            Destination cell
	 */
	public Movement(Level dungeonLevel, Cell fromCell, Direction dir)
	{
		this.level = dungeonLevel;
		this.from = fromCell;
		this.to = this.getDestinationCell(fromCell, dir);
		this.movement = dir;

		this.movementAllowed = false;
		this.allowOverMonsters = false;
		this.feedback = null;
		this.monsterName = null; // this field will hold the monster name in
		// the destination Cell
	}

	/**
	 * Get the monster name in the destination cell
	 * 
	 * @return Null, if no monster in the destination cell. Else, the monster
	 *         name
	 */
	public String getMonsterName()
	{
		return this.monsterName;
	}

	/**
	 * Get any communication from the destination cell (ie: there's a sign
	 * here...)
	 * 
	 * @return Any communication from the destination cell
	 */
	public String getFeedback()
	{
		return this.feedback;
	}

	/**
	 * If the movement between the starting cell and the destination cell is
	 * allowed
	 * 
	 * @return True if the movement toward destination is allowed
	 */
	public boolean isMovementAllowed()
	{
		this.computeTrack();
		return this.movementAllowed;
	}

	/**
	 * Allow movement over monsters and PC (ie: gunsight)
	 * 
	 * @param allowOverMonsters
	 *            if true, isMovementAllowed returns true even in the cell is a
	 *            monster
	 */
	public void setAllowOverMonsters(boolean allow)
	{
		this.allowOverMonsters = allow;
	}

	/**
	 * True, if movement is allowed over monsters
	 * 
	 * @return
	 */
	public boolean allowOverMonsters()
	{
		return allowOverMonsters;
	}

	private Cell getDestinationCell(Cell fromCell, Direction dir)
	{
		return this.level.getRpgMap().getCell(fromCell, dir);
	}

	private void computeTrack()
	{
		if (this.to == null)
		{
			// LibraryStrings.NOWHERE + " destcell is NULL.";
			return;
		}

		if (this.from == this.to)
		{
			this.feedback = "You are at the edge: from = to";
			return; // when you are at the edges of the dungeon
		}

		if (!this.to.isWall())
		{
			boolean closedDoor = false;

			if (this.from.isDoor(this.movement))
			{
				Door d = this.from.getPassages().getPassage(this.movement).getDoor();
				if (!d.isOpen())
				{
					closedDoor = true;
					this.feedback = "A closed door blocks your way.";
					return;
				}
			}

			if ((this.from.isCorridor(this.movement) && (!closedDoor)))
			{
				// Is there an NCP there?
				RpgMapPoint p = this.to.getCoordinates();

				if (this.level.isTherePlaceHolder(p))
				{
					Placeholder placeH = this.level.getPlaceHolderIn(p);
					if (placeH != null)
					{
						String phName = placeH.getName();
						switch (placeH.getType())
						{
							case MONSTER:
								// Do not modify the text.
								// This text ('there's a') identyfies
								// the fact that the cell is occupied by a
								// placeholder
								if (this.allowOverMonsters)
								{
									this.movementAllowed = true;
								}
								this.monsterName = phName;
								this.feedback = LibraryStrings.MONSTER_HERE + phName + " here.";
								break;

							case PLAYER:
								if (this.allowOverMonsters)
								{
									this.movementAllowed = true;
								}
								this.feedback = "Monster attack";
								break;

							default:
								// You can walk there, so do it
								this.movementAllowed = true;
								this.feedback = "There is a " + phName + " here.";
								break;
						}
					}
				}
				else
				{
					// You can walk there, so do it
					this.movementAllowed = true;
					// scan destination cell
					this.feedback = this.scanCell(this.to);
				}
			}
			else
			{
				// System.err.println("Strange: this is not a corridor, but to
				// cell is not a WALL...");
				// TODO: fix this bug. At the edges of dungeon, there are cells
				// without a Passage that contains no walls and no doors...
				this.movementAllowed = true;
			}
		}
	}

	private String scanCell(Cell c)
	{
		String sfeedback = null;
		if (c.isSpecial())
		{
			Location loc = c.getLocation();
			sfeedback = loc.getDescription();
		}
		return sfeedback;
	}

}
