/**
 * J.U.R.P.E.
 * 
 * @version@ (System Package) Copyright (C) 2002-12 LittleLite Software
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

package net.littlelite.jurpe.system.commands.player;

import net.littlelite.jurpe.dungeon.Avatar;
import net.littlelite.jurpe.dungeon.Dungeons;
import net.littlelite.jurpe.dungeon.Gunsight;
import net.littlelite.jurpe.dungeon.Level;
import net.littlelite.jurpe.dungeon.Movement;
import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.crawler.HexMapCommand;
import net.littlelite.jurpe.dungeon.rpgmap.Cell;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.commands.DungeonCommand;
import net.littlelite.jurpe.system.commands.PlayerCommand;
import net.littlelite.jurpe.system.resources.LibraryStrings;

public final class Walk extends AbstractCommand implements IPlayerCommand
{

	private Avatar avatar;
	private Dungeons dungeon;
	private String option;

	public Walk(HexMapCommand hmc, CommandFlags flags, Dungeons game) throws JurpeException
	{
		super(hmc, flags);
		this.avatar = game.getAvatar();
		this.dungeon = game;
		this.generate();
	}

	private void generate() throws JurpeException
	{
		String feedback;

		if (this.flags.isDig())
		{
			this.flags.setDig(false);
			feedback = this.doDig(this.hexmapCmd.getDirection());
			this.dngCmd = new DungeonCommand(PlayerCommand.DIG, feedback);
			if (feedback.equals(LibraryStrings.OK))
			{
				this.dngCmd.setOption(this.hexmapCmd.getDirection().toString());
			}
			else
			{
				this.dngCmd.setOption(null);
			}
		}
		else if (this.flags.isOpen())
		{
			this.flags.setOpen(false);
			String direction = this.hexmapCmd.getDirection().toString();
			this.dngCmd = new DungeonCommand(PlayerCommand.OPEN_DOOR, "Opening " + direction + " door");
			this.dngCmd.setOption(direction);
		}
		else if (this.flags.isClose())
		{
			this.flags.setClose(false);
			String direction = this.hexmapCmd.getDirection().toString();
			this.dngCmd = new DungeonCommand(PlayerCommand.CLOSE_DOOR, "Closing " + direction + " door");
			this.dngCmd.setOption(direction);
		}
		else if (this.flags.isAim())
		{
			// Move gunsight
			feedback = this.moveGunsight(this.hexmapCmd.getDirection());
			this.dngCmd = new DungeonCommand(PlayerCommand.MOVE, feedback);
		}
		else
		{
			// Move Avatar
			feedback = this.moveAvatar(this.hexmapCmd.getDirection());
			if ((feedback != null) && (feedback.startsWith(LibraryStrings.MONSTER_HERE)))
			{
				this.dngCmd = new DungeonCommand(PlayerCommand.ATTACK_MONSTER, feedback);
				this.dngCmd.setOption(this.option);
				this.option = null;
			}
			else
			{
				this.dngCmd = new DungeonCommand(PlayerCommand.MOVE, feedback);
			}
		}
	}

	private String doDig(Direction d) throws JurpeException
	{
		String feedback = "DIG> ";
		Cell toCell = this.getDestinationCell(d);

		if (toCell.isWall())
		{
			feedback = "Ok";
		}
		else
		{
			switch (d)
			{
				case NORTH:
					feedback += "You cannot dig north";
					break;

				case SOUTH:
					feedback += "You cannot dig south";
					break;

				case NORTHEAST:
					feedback += "You cannot dig NE";
					break;

				case NORTHWEST:
					feedback += "You cannot dig NW";
					break;

				case SOUTHEAST:
					feedback += "You cannot dig SE";
					break;

				case SOUTHWEST:
					feedback += "You cannot dig SW";
					break;

				default:
					feedback += "What??";
			}
		}

		return feedback;

	}

	private Cell getDestinationCell(Direction movement) throws JurpeException
	{
		Cell fromCell = this.avatar.getPlaceholder().getCell();
		return this.dungeon.getCurrentMap().getCell(fromCell, movement);
	}

	private String moveGunsight(Direction movement) throws JurpeException
	{
		String feedback = null;
		if (movement != null)
		{
			if (this.avatar == null)
			{
				this.avatar = this.dungeon.getAvatar();
			}

			Gunsight gSight = this.avatar.getGunsight();
			Cell fromCell = gSight.getPlaceholder().getCell();
			Level dungeonLevel = this.dungeon.getCurrentLevel();
			Movement mov = new Movement(dungeonLevel, fromCell, movement);
			mov.setAllowOverMonsters(true);
			if (mov.isMovementAllowed())
			{
				gSight.move(movement);
			}
		}

		return feedback;

	}

	private String moveAvatar(Direction movement) throws JurpeException
	{
		String feedback = null;
		if (movement != null)
		{
			if (this.avatar == null)
			{
				this.avatar = this.dungeon.getAvatar();
			}

			Cell fromCell = this.avatar.getPlaceholder().getCell();
			Level dungeonLevel = this.dungeon.getCurrentLevel();

			Movement checkMovement = new Movement(dungeonLevel, fromCell, movement);
			if (checkMovement.isMovementAllowed())
			{
				this.avatar.move(movement);
			}

			feedback = checkMovement.getFeedback();
			this.option = checkMovement.getMonsterName(); // null, if no
			// monster is in
			// destination Cell
		}
		return feedback;
	}

}
