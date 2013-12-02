package net.littlelite.jurpe.system.commands;

/**
 * J.U.R.P.E.
 * 
 * @version@ (DungeonCrawler Package) Copyright (C) 2002-12 LittleLite Software
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

/**
 * A PlayerCommand enum is a command issued by a human player
 */
public enum PlayerCommand
{
	AIM, 
        ATTACK_MONSTER, 
        CLOSE_DOOR, 
        DIG, 
        ENTER, 
        ENTER_DUNGEON, 
        ENTER_INN, 
        ENTER_SHOP, 
        ENTER_TRAINER, 
        ENTER_MAGESGUILD,
        MOVE, 
        OPEN_DOOR, 
        PASS, // do nothing
	PICK_UP,
	QUITGAME,
	SAYTIME,
	SHOOT,
	STAIRS_DOWN,
	STAIRS_UP,
	UNKNOWN,
	USEFIRSTAID;
}
