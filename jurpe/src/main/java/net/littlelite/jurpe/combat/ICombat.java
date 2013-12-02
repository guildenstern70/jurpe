package net.littlelite.jurpe.combat;

/**
 J.U.R.P.E. @version@
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

import net.littlelite.jurpe.system.resources.LibraryStrings;

/**
 * Combat commands available. Implement every command with Functor Design
 * Pattern. IE: move() function must be implemented as a Move.class
 * 
 * @see DualCombat
 * @see net.littlelite.jurpedemo.frames.FrameCombat
 * 
 */
public interface ICombat
{
	/**
	 * Make a move and/or escape.
	 * 
	 * @return boolean value used for utility purpose
	 */
	boolean move();

	/**
	 * Ready an unbalanced weapon.
	 * 
	 * @return boolean value used for utility purpose
	 */
	boolean readyWeapon();

	/**
	 * Reload weapon
	 * 
	 * @return boolean value used for utility purpose
	 */
	boolean reload();

	/**
	 * Aim a ranged weapon
	 * 
	 * @return boolean value used for utility purpose
	 */
	boolean aim();

	/**
	 * Make an attack
	 * 
	 * @return boolean value used for utility purpose
	 */
	boolean attack();

	/**
	 * Make a special, all out attack
	 * 
	 * @return boolean value used for utility purpose
	 */
	boolean allOutAttack();

	/**
	 * Make a special, all out defense
	 * 
	 * @return boolean value used for utility purpose
	 */
	boolean allOutDefense();

	/**
	 * Get log of actions.
	 * 
	 * @return String log of actions, LINEFEED separated.
	 */
	String log();

	int CM_MOVE = 1;

	int CM_RDYWPN = 2;

	int CM_RELOAD = 3;

	int CM_AIM = 4;

	int CM_ATTAK = 5;

	int CM_ALLOATT = 6;

	int CM_ALLODEF = 7;

	/**
	 * Returns all available commands as an array of Strings
	 */
	String[] AVAILABLE_COMBAT_COMMANDS =
	{ LibraryStrings.CM_MOVE, LibraryStrings.CM_RDYWPN, LibraryStrings.CM_RELOAD, LibraryStrings.CM_AIM, LibraryStrings.CM_ATTAK, LibraryStrings.CM_ALLOATT,
			LibraryStrings.CM_ALLODEF };

}