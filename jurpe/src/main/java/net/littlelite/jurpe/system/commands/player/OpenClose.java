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
import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.crawler.HexMapCommand;
import net.littlelite.jurpe.dungeon.rpgmap.Cell;
import net.littlelite.jurpe.system.commands.DungeonCommand;
import net.littlelite.jurpe.system.commands.PlayerCommand;

public final class OpenClose extends AbstractCommand implements IPlayerCommand
{

	/**
	 * Open/Close constructor
	 * 
	 * @param hmc
	 * @param flags
	 * @param av
	 * @param toOpen
	 */
	public OpenClose(HexMapCommand hmc, CommandFlags flags, Avatar av, boolean toOpen)
	{
		super(hmc, flags);
		this.generate(av, toOpen);
	}

	private void generate(Avatar av, boolean isToOpen)
	{
		String openCloseWhat = "Nothing to open here.";
		String openingClosing = "Opening ";
		PlayerCommand openCloseCommand = PlayerCommand.OPEN_DOOR;

		if (!isToOpen)
		{
			openCloseWhat = "Close what?";
			openingClosing = "Closing ";
			openCloseCommand = PlayerCommand.CLOSE_DOOR;
		}

		Cell avatarCell = av.getPlaceholder().getCell();
		Direction[] pass = avatarCell.getDoors(false); // get only closed doors
		if (pass == null)
		{
			this.dngCmd = new DungeonCommand(PlayerCommand.UNKNOWN, openCloseWhat);
		}
		else
		{
			this.dngCmd = new DungeonCommand(openCloseCommand);
			if (pass.length > 1)
			{
				this.dngCmd.setOption(null);
				this.dngCmd.setFeedback("Which one?");
				if (isToOpen)
				{
					this.flags.setOpen(true);
				}
				else
				{
					this.flags.setClose(true);
				}
			}
			else
			{
				String doorDirection = pass[0].toString();
				this.dngCmd.setFeedback(openingClosing + doorDirection + " door");
				this.dngCmd.setOption(doorDirection);
			}
		}

	}

}
