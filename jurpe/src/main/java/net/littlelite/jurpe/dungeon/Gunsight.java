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

package net.littlelite.jurpe.dungeon;

import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.rpgmap.Cell;
import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.JurpeException;

public final class Gunsight implements IDungeonPawn
{

	private Placeholder placeHolder;
	private Avatar avatar;

	public static Gunsight getGunsight(Avatar av)
	{
		Gunsight aimGun = null;

		try
		{
			aimGun = new Gunsight(av);
		}
		catch (JurpeException jex)
		{
			jex.printStackTrace();
		}

		return aimGun;

	}

	/**
	 * Get Gunsight placeholder
	 */
	@Override
	public Placeholder getPlaceholder()
	{
		return this.placeHolder;
	}

	/**
	 * Move gunsight in some direction
	 * 
	 * @param d
	 *            A direction, as in Direction class
	 */
	public void move(Direction d) throws JurpeException
	{
		// Old Cell
		Cell actualCell = this.placeHolder.getCell();
		if (actualCell.isSpecial())
		{
			this.placeHolder.backGround = actualCell.getLocation().getColor();
		}
		else
		{
			this.placeHolder.backGround = Config.MAZE_BACKGROUND;
		}
		this.placeHolder.move(d);
	}

	private Gunsight(Avatar av) throws JurpeException
	{
		this.avatar = av;

		// Gunsite placeholder starts where Avatar is
		this.placeHolder = new Placeholder(this.avatar.getLevel(), PlaceholderType.GUNSIGHT, "Gunsight");
		this.placeHolder.setColors(Config.GUNSIGHT_FORE, Config.MAZE_BACKGROUND);
		this.placeHolder.setPosition(this.avatar.getPlaceholder().getPosition());
	}

}
