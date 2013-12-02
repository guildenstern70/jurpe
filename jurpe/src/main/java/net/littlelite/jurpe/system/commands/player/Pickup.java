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
import net.littlelite.jurpe.dungeon.DungeonItem;
import net.littlelite.jurpe.dungeon.Dungeons;
import net.littlelite.jurpe.dungeon.Level;
import net.littlelite.jurpe.dungeon.crawler.HexMapCommand;
import net.littlelite.jurpe.dungeon.crawler.RpgMapPoint;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.commands.DungeonCommand;
import net.littlelite.jurpe.system.commands.PlayerCommand;

public final class Pickup extends AbstractCommand implements IPlayerCommand
{

	public Pickup(HexMapCommand hmc, CommandFlags flags, Dungeons game)
	{
		super(hmc, flags);
		this.generate(game);
	}

	private void generate(Dungeons game)
	{
		Avatar avatar = game.getAvatar();

		try
		{
			RpgMapPoint p = avatar.getPlaceholder().getCell().getCoordinates();
			Level dungeonLevel = game.getCurrentLevel();
			DungeonItem di = dungeonLevel.getItems().getItemIn(p);
			if (di != null)
			{
				this.dngCmd = new DungeonCommand(PlayerCommand.PICK_UP, "Picking up " + di.item().toString());
			}
			else
			{
				this.dngCmd = new DungeonCommand(PlayerCommand.UNKNOWN, "There is nothing to pick up here.");
			}
		}
		catch (JurpeException jex)
		{
			System.err.println(jex.getMessage());
		}

	}

}
