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

import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.dungeon.Dungeons;
import net.littlelite.jurpe.dungeon.crawler.HexMapCommand;
import net.littlelite.jurpe.items.Weapon;
import net.littlelite.jurpe.system.commands.DungeonCommand;
import net.littlelite.jurpe.system.commands.PlayerCommand;

public class Aim extends AbstractCommand implements IPlayerCommand
{

	private Dungeons dungeon;

	public Aim(HexMapCommand hmc, CommandFlags flags, Dungeons game)
	{
		super(hmc, flags);
		this.dungeon = game;
		this.generate();
	}

	private void generate()
	{
		String feedback = "";

		PCharacter player = this.dungeon.getSystem().getPC();
		Weapon currentBow = player.getCurrentRangedWeapon();
		if (currentBow != null)
		{
			if (this.flags.isAim())
			{
				this.flags.setAim(false);
				feedback = "Aim mode off";
			}
			else
			{
				this.flags.setAim(true);
				feedback = "Aiming with : " + currentBow.getName() + ". Aim mode on";
			}

			this.dngCmd = new DungeonCommand(PlayerCommand.AIM, feedback);
		}
		else
		{
			this.dngCmd = new DungeonCommand(PlayerCommand.PASS, "You have no ranged weapon ready.");
		}

	}
}
