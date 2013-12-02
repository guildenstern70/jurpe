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
import net.littlelite.jurpe.combat.RangedCombat;
import net.littlelite.jurpe.dungeon.Avatar;
import net.littlelite.jurpe.dungeon.DungeonMonster;
import net.littlelite.jurpe.dungeon.Dungeons;
import net.littlelite.jurpe.dungeon.Gunsight;
import net.littlelite.jurpe.dungeon.Level;
import net.littlelite.jurpe.dungeon.Placeholder;
import net.littlelite.jurpe.dungeon.PlaceholderType;
import net.littlelite.jurpe.dungeon.crawler.HexMapCommand;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.commands.DungeonCommand;
import net.littlelite.jurpe.system.commands.PlayerCommand;

public final class Shoot extends AbstractCommand implements IPlayerCommand
{

	private Dungeons dungeon;

	public Shoot(HexMapCommand hmc, CommandFlags flags, Dungeons game) throws JurpeException
	{
		super(hmc, flags);
		this.dungeon = game;
		this.generate();
	}

	private void generate() throws JurpeException
	{
		String feedback;

		Avatar avatar = this.dungeon.getAvatar();
		if (avatar.hasGunsight())
		{
			Gunsight gSight = avatar.getGunsight();
			Level curLevel = this.dungeon.getCurrentLevel();
			Placeholder placeH = curLevel.getPlaceHolderIn(gSight.getPlaceholder().getPosition());

			if ((placeH != null) && (placeH.getType() == PlaceholderType.MONSTER))
			{
				DungeonMonster monster = DungeonMonster.fromPlaceHolder(curLevel, placeH);
				String actionResult = "You shot " + monster.getShortDescription();
				// Distance between shooter and monster
				int distance = placeH.getLastPosition().getDistanceFrom(avatar.getPlaceholder().getLastPosition());
				feedback = this.combat(curLevel, monster, distance);
				this.dungeon.getLog().addEntry(actionResult);
			}
			else
			{
				feedback = "There is nothing to shoot at here";
			}

			this.flags.setAim(false);
			this.dungeon.getAvatar().enableGunsight(false);

			this.dngCmd = new DungeonCommand(PlayerCommand.SHOOT, feedback);
		}
		else
		{
			// Nothing happens
		}
	}

	private String combat(Level curLevel, DungeonMonster monster, int distance)
	{
		String feedback;

		if (monster != null)
		{
			PCharacter player = this.dungeon.getSystem().getPC();
			RangedCombat rc = new RangedCombat(player, monster);
			rc.shoot(distance);
			feedback = rc.getFeedback();

			// Refresh monsters
			if (curLevel.getMonsters().updateLiving())
			{
				// Save only if the number actually changed
				curLevel.save();
			}

		}
		else
		{
			feedback = "Unknown monster!!";
		}

		return feedback;
	}

}
