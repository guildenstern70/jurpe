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

import java.awt.event.KeyEvent;

import net.littlelite.jurpe.dungeon.Dungeons;
import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.crawler.HexMapCommand;
import net.littlelite.jurpe.dungeon.crawler.InstructionByKey;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.world.GameWorld;

public class PlayerCommandFactory
{
	/**
	 * Transforms keyboard input into command
	 * 
	 * @param ke
	 *            KeyEvent
	 * @return HexMapCommand
	 */
	public static IPlayerCommand create(KeyEvent ke, CommandFlags flags, Dungeons game) throws JurpeException
	{
		IPlayerCommand cmd = null;
		int keypressed = ke.getKeyCode();
		HexMapCommand hmc = null;

		switch (keypressed)
		{
			case KeyEvent.VK_A:
				hmc = new HexMapCommand(null, InstructionByKey.AIM);
				cmd = new Aim(hmc, flags, game);
				break;

			case KeyEvent.VK_I:
				hmc = new HexMapCommand(Direction.NORTH, InstructionByKey.WALK);
				cmd = new Walk(hmc, flags, game);
				break;

			case KeyEvent.VK_K:
				hmc = new HexMapCommand(Direction.SOUTH, InstructionByKey.WALK);
				cmd = new Walk(hmc, flags, game);
				break;

			case KeyEvent.VK_U:
				hmc = new HexMapCommand(Direction.NORTHWEST, InstructionByKey.WALK);
				cmd = new Walk(hmc, flags, game);
				break;

			case KeyEvent.VK_O:
				hmc = new HexMapCommand(Direction.NORTHEAST, InstructionByKey.WALK);
				cmd = new Walk(hmc, flags, game);
				break;

			case KeyEvent.VK_J:
				hmc = new HexMapCommand(Direction.SOUTHWEST, InstructionByKey.WALK);
				cmd = new Walk(hmc, flags, game);
				break;

			case KeyEvent.VK_L:
				hmc = new HexMapCommand(Direction.SOUTHEAST, InstructionByKey.WALK);
				cmd = new Walk(hmc, flags, game);
				break;

			case KeyEvent.VK_SPACE:
				hmc = new HexMapCommand(null, InstructionByKey.ENTER);
				cmd = new Enter(hmc, flags, game.getAvatar());
				break;

			case KeyEvent.VK_D:
				hmc = new HexMapCommand(null, InstructionByKey.DIG);
				cmd = new Dig(hmc, flags);
				break;

			case KeyEvent.VK_P:
				hmc = new HexMapCommand(null, InstructionByKey.OPEN);
				cmd = new OpenClose(hmc, flags, game.getAvatar(), true);
				break;

			case KeyEvent.VK_C:
				hmc = new HexMapCommand(null, InstructionByKey.CLOSE);
				cmd = new OpenClose(hmc, flags, game.getAvatar(), false);
				break;

			case KeyEvent.VK_X:
				hmc = new HexMapCommand(null, InstructionByKey.PICKUP);
				cmd = new Pickup(hmc, flags, game);
				break;

			case KeyEvent.VK_T:
				hmc = new HexMapCommand(null, InstructionByKey.ASKTIME);
				GameWorld gw = game.getSystem().getGameWorld();
				cmd = new AskTime(hmc, flags, gw);
				break;

			case KeyEvent.VK_F:
				hmc = new HexMapCommand(null, InstructionByKey.FIRSTAID);
				cmd = new FirstAid(hmc, flags);
				break;

			case KeyEvent.VK_ESCAPE:
				hmc = new HexMapCommand(null, InstructionByKey.QUIT);
				cmd = new Quit(hmc, flags);
				break;

			case KeyEvent.VK_ENTER:
				hmc = new HexMapCommand(null, InstructionByKey.SHOOT);
				cmd = new Shoot(hmc, flags, game);
				break;

			default:
				hmc = null;
		}

		return cmd;
	}

}
