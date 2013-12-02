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
import net.littlelite.jurpe.dungeon.crawler.HexMapCommand;
import net.littlelite.jurpe.dungeon.rpgmap.Cell;
import net.littlelite.jurpe.system.commands.DungeonCommand;
import net.littlelite.jurpe.system.commands.PlayerCommand;
import net.littlelite.jurpe.world.LocationType;

public final class Enter extends AbstractCommand implements IPlayerCommand
{

	public Enter(HexMapCommand hmc, CommandFlags flags, Avatar theAvatar)
	{
		super(hmc, flags);
		this.generate(theAvatar);
	}

	private void generate(Avatar av)
	{
		String feedback = "> ";
		this.dngCmd = null;

		Cell avatarCell = av.getPlaceholder().getCell();
		if (avatarCell.isSpecial())
		{
			feedback += avatarCell.getLocation().getEnterMessage();
			LocationType where = avatarCell.getLocation().getType();

			if (where.equals(LocationType.SHOP))
			{
				this.dngCmd = new DungeonCommand(PlayerCommand.ENTER_SHOP, feedback);
			}
			else if (where.equals(LocationType.INN))
			{
				this.dngCmd = new DungeonCommand(PlayerCommand.ENTER_INN, feedback);
			}
			else if (where.equals(LocationType.TRAINING))
			{
				this.dngCmd = new DungeonCommand(PlayerCommand.ENTER_TRAINER, feedback);
			}
			else if (where.equals(LocationType.STAIRSDOWN))
			{
				this.dngCmd = new DungeonCommand(PlayerCommand.STAIRS_DOWN, feedback);
			}
			else if (where.equals(LocationType.STAIRSUP))
			{
				this.dngCmd = new DungeonCommand(PlayerCommand.STAIRS_UP, feedback);
			}
                        else if (where.equals(LocationType.MAGESGUILD))
                        {
                                this.dngCmd = new DungeonCommand(PlayerCommand.ENTER_MAGESGUILD, feedback);
                        }
			else
			{
				this.dngCmd = new DungeonCommand(PlayerCommand.ENTER, feedback);
			}
		}
		else
		{
			this.dngCmd = new DungeonCommand(PlayerCommand.UNKNOWN, "Enter what?");
		}

	}

}
