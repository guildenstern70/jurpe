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

import java.io.Serializable;

import net.littlelite.jurpe.characters.Monster;

/**
 * A monster's placeholder
 */
public class DungeonMonster extends Monster implements Serializable, IDungeonPawn
{

	private static final long serialVersionUID = 3320L;
	private Placeholder ph;

	public DungeonMonster(Monster monster, Placeholder iph)
	{
		super(monster.getCharacterAttributes(), monster.getArmor());
		this.ph = iph;
	}

	/**
	 * Get placeholder for monster
	 * 
	 * @return PlaceHolder
	 */
	public Placeholder getPlaceholder()
	{
		return this.ph;
	}

	/**
	 * Get a dungeon monster from a placeholder
	 * 
	 * @param currentLevel
	 *            The current dungeon level
	 * @param ph
	 *            A placeholder in the dungeon level
	 * @return The dungeon monster if found, else null
	 */
	public static DungeonMonster fromPlaceHolder(Level currentLevel, Placeholder ph)
	{
		DungeonMonster xdm = null;
		for (DungeonMonster dm : currentLevel.getMonsters().getDungeonMonsters())
		{
			if (dm.getPlaceholder().getPosition() == ph.getPosition())
			{
				xdm = dm;
				break;
			}
		}

		return xdm;
	}

}
