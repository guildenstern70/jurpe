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

import net.littlelite.jurpe.characters.PC;
import net.littlelite.jurpe.system.resources.LibraryStrings;

/**
 * Utility class that computes all defense points for a given character.
 * 
 * 
 */
public class Defense
{

	private PC playingChr;

	/**
	 * Constructor. Initialize player and defense type
	 * 
	 * @param character
	 *            Current PCharacter
	 */
	public Defense(PC character)
	{
		this.playingChr = character;
	}

	/**
	 * Get active defense points based on character defense type.
	 * 
	 * @return defense points
	 */
	public int getActiveDefensePoints()
	{
		DefenseType dt = this.playingChr.getActiveDefense();
		return this.getActiveDefensePoints(dt);
	}

	/**
	 * Get active defense points based on given defense type. Unconscious
	 * characters won't have active defenses.
	 * 
	 * @param dt
	 *            Taken from this class, IE: Defense.ACTIVE_DODGE
	 * @return defense points
	 */
	public int getActiveDefensePoints(DefenseType dt)
	{

		int defensePoints = 0;

		if (this.playingChr.isUnconscious())
		{
			return 0;
		}

		if (dt.isAvailable())
		{
			if (dt.equals(DefenseType.ACTIVE_DODGE))
			{
				defensePoints = this.getDodgePoints(this.playingChr.getMvmt());
			}
			else if (dt.equals(DefenseType.ACTIVE_PARRY))
			{
				defensePoints = Defense.getParryPoints(this.playingChr.getCurrentWeaponLevel());
			}
			else if (dt.equals(DefenseType.ACTIVE_BLOCK))
			{
				defensePoints = Defense.getBlockPoints(this.playingChr.getCurrentShieldLevel());
			}
		}

		return defensePoints;
	}

	/**
	 * Returns a description of the action taken by defender when using active
	 * defense (ie: parries, blocks, dodges).
	 * 
	 * @param dt
	 *            type of actrive defense
	 * @return String description of active defense
	 */
	public String activeDefenseString(DefenseType dt)
	{
		String action = "";

		if (dt.equals(DefenseType.ACTIVE_DODGE))
		{
			action = LibraryStrings.DODGES;
		}
		else if (dt.equals(DefenseType.ACTIVE_BLOCK))
		{
			action = LibraryStrings.BLOCKS;
		}
		else if (dt.equals(DefenseType.ACTIVE_PARRY))
		{
			action = LibraryStrings.PARRIES;
		}

		return action;
	}

	/**
	 * Get First Active Defense choosen by defender
	 * 
	 * @return type of active defense choosen
	 */
	public DefenseType getPreferredActiveDefense()
	{
		DefenseType[] dts = this.playingChr.getActiveDefenses();
		return dts[0];
	}

	/**
	 * Get Second Active Defense choosen by defender
	 * 
	 * @return type of active defense choosen
	 */
	public DefenseType getSecondActiveDefense()
	{
		DefenseType[] dts = this.playingChr.getActiveDefenses();
		return dts[1];
	}

	/**
	 * Will determine if defending character issued an All Out Defense the turn
	 * before.
	 * 
	 * @return true if this defense in al AllOutDefense
	 */
	public boolean isAllOutDefense()
	{
		boolean isAllOutDefense = false;
		DefenseType[] dts = this.playingChr.getActiveDefenses();
		if (dts[1] != null)
		{
			isAllOutDefense = true;
		}
		return isAllOutDefense;
	}

	/**
	 * Get passive defense points based on character defense type.
	 * 
	 * @return defense points
	 */
	public int getPassiveDefensePoints()
	{
		int dp = this.playingChr.getPassiveDefense();
		int rd = this.playingChr.getDamageResistance();

		return (dp + rd);
	}

	/*
	 * PRIVATE MEMBERS
	 */

	/**
	 * Dodge active defense is based on DX.
	 * 
	 * @param move
	 * @return
	 */
	private int getDodgePoints(int move)
	{
		int dodge;

		if (this.playingChr.isAI())
		{
			dodge = Math.max(move / 2, this.playingChr.getPrimaryStats().getDX() / 2);
			dodge = Math.min(dodge, 10);
		}
		else
		{
			dodge = this.playingChr.getMvmt();
		}
		return dodge;
	}

	/**
	 * Parry points are skill with weapon / 2
	 * 
	 * @param skillWithWeapon
	 * @return
	 */
	private static int getParryPoints(int skillWithWeapon)
	{
		return Math.round(skillWithWeapon / 2);
	}

	/**
	 * Block points are the shield skill
	 * 
	 * @param shieldSkill
	 * @return
	 */
	private static int getBlockPoints(int shieldSkill)
	{
		return Math.round(shieldSkill / 2);
	}

}
