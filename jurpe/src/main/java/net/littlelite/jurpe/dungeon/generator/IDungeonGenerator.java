package net.littlelite.jurpe.dungeon.generator;

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

import net.littlelite.jurpe.dungeon.rpgmap.RpgMap;
import net.littlelite.jurpe.system.JurpeException;

/**
 * A dungeon generator class is a factory for classes that builds new dungeons.
 */
public interface IDungeonGenerator
{
	/**
	 * Create a new dungeon
	 * 
	 * @param level
	 *            Level of the dungeon
	 * @return dungeon
	 */
	RpgMap createDungeon(int level) throws JurpeException;
}
