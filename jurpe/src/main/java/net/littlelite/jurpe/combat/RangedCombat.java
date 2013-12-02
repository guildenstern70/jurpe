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

package net.littlelite.jurpe.combat;

import net.littlelite.jurpe.characters.PC;
import net.littlelite.jurpe.items.Weapon;
import net.littlelite.jurpe.system.JurpeUtils;

/**
 * Ranged Combat methods
 * 
 */
public class RangedCombat
{

	private PC attacker;
	private PC defender;
	private StringBuilder feedback;

	/**
	 * A new ranged combat
	 * 
	 * @param pAttacker
	 *            The attacker
	 * @param pDefender
	 *            The defender
	 */
	public RangedCombat(PC pAttacker, PC pDefender)
	{
		this.attacker = pAttacker;
		this.defender = pDefender;
		this.feedback = new StringBuilder();
	}

	/**
	 * Shoot target
	 * 
	 * @param distance
	 *            Distance between attacker and defender in cells
	 */
	public void shoot(int distance)
	{
		Weapon rangedWeapon = this.attacker.getCurrentRangedWeapon();

		if (rangedWeapon != null)
		{
			int range = rangedWeapon.getRange();
			int maxDamage = rangedWeapon.getMaxDamage();

			if (this.attackRoll(distance, range)) // attack succeeds
			{
				if (!this.defenseRoll()) // defense fails
				{
					this.damageRoll(maxDamage);
				}
			}
		}
		else
		{
			this.feedback.append("No ranged weapon is ready.");
		}
	}

	/**
	 * Get ranged combat feedback
	 * 
	 * @return Get a description of what happened in this combat
	 */
	public String getFeedback()
	{
		return this.feedback.toString();
	}

	private boolean attackRoll(int distance, int range)
	{
		boolean success = false;

		if (range < distance)
		{
			this.feedback.append("Target out of range");
			return false;
		}

		int skillLevel = this.attacker.getCurrentRangedWeaponLevel();
		skillLevel -= distance; // applying malus for distance
		success = JurpeUtils.successRoll(skillLevel);
		int roll = JurpeUtils.getLatestRoll();

		if (roll > 16)
		{
			// Automatic miss
			this.feedback.append("You completely miss the target.");
		}
		else
		{
			if (success)
			{
				this.feedback.append("You hit the target [");
			}
			else
			{
				this.feedback.append("You miss the target [");
			}
			this.feedback.append(roll);
			this.feedback.append("/");
			this.feedback.append(skillLevel);
			this.feedback.append("]");
		}

		return success;
	}

	private boolean defenseRoll()
	{
		Defense defense = new Defense(this.defender);
		int defensePoints = this.getTotalDefensePoints(defense);
		boolean success = JurpeUtils.successRoll(defensePoints);
		int roll = JurpeUtils.getLatestRoll();

		this.feedback.append(" - ");

		if (success)
		{
			this.feedback.append("Monster dodges [");
		}
		else
		{
			this.feedback.append("HIT! [");
		}

		this.feedback.append(roll);
		this.feedback.append("/");
		this.feedback.append(defensePoints);
		this.feedback.append("]");

		return success;

	}

	private void damageRoll(int maxWeaponDamage)
	{
		int damage = this.attacker.getRangedWeaponDamage();
		if (damage > 0)
		{
			damage = Math.min(maxWeaponDamage, damage);
			int fighteeRD = this.defender.getDamageResistance();
			int effectiveDamage = JurpeUtils.computeDamage(damage, fighteeRD);
			this.defender.receiveDamage(effectiveDamage);
			this.feedback.append(" - You made ");
			this.feedback.append(effectiveDamage);
			this.feedback.append(" dmg(s) ");
			int curHp = this.defender.getCurrentHP();
			if (curHp >= 0)
			{
				this.feedback.append(" - Monster HP: ");
				this.feedback.append(curHp);
			}
			else
			{
				this.feedback.append(" - Monster dies ");
			}
		}

	}

	private int getTotalDefensePoints(Defense defense)
	{
		// Defender of missile attack can only dodge
		this.defender.setActiveDefense(DefenseType.ACTIVE_DODGE);

		int activeDefense = defense.getActiveDefensePoints();
		float passiveDefense = this.defender.getTotalPassiveDefenses();

		return activeDefense + Math.round(passiveDefense);
	}

}
