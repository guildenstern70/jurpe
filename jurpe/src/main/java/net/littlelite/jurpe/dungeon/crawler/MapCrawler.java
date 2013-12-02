package net.littlelite.jurpe.dungeon.crawler;

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

import java.awt.event.KeyEvent;
import net.littlelite.jurpe.dungeon.Dungeons;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.commands.DungeonCommand;
import net.littlelite.jurpe.system.commands.player.CommandFlags;
import net.littlelite.jurpe.system.commands.player.IPlayerCommand;
import net.littlelite.jurpe.system.commands.player.PlayerCommandFactory;

/**
 * HexMap interactive manager class. Handles all keyboard commands for the
 * hexmap, moves placeholders and so on.
 */
public final class MapCrawler
{
	private Dungeons dungeon;
	private CommandFlags flags;

	private static MapCrawler mapSingleton;

	/**
	 * Per ottenere un handle alla classe instanziata (singleton)
	 * 
	 * @return Reference a Singleton
	 */
	public static MapCrawler getReference(Dungeons game)
	{
		if (mapSingleton == null)
		{
			mapSingleton = new MapCrawler(game);
		}

		return mapSingleton;
	}

	/**
	 * Process any keyboard command given to hexmap. The command is usually
	 * processed by DungeonCommander.execute()
	 * 
	 * @param ke
	 *            KeyEvent
	 * @return A command to execute
	 * @see net.littlelite.jurpe.system.commands.DungeonCommand
	 */
	public DungeonCommand recognizeCommand(KeyEvent ke) throws JurpeException
	{
		IPlayerCommand pCmd = PlayerCommandFactory.create(ke, this.flags, this.dungeon);
		DungeonCommand dngCommand = null;
		if (pCmd != null)
		{
			dngCommand = pCmd.getCommand();
		}
		return dngCommand;
	}

	/**
	 * Constructor
	 * 
	 * @param map
	 *            HexMap control
	 * @param game
	 *            Dungeons handle
	 */
	private MapCrawler(Dungeons game)
	{
		this.dungeon = game;
		this.flags = new CommandFlags();
	}

}
