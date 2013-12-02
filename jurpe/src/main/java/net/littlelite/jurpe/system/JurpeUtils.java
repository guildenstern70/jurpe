package net.littlelite.jurpe.system;

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

import net.littlelite.jurpe.characters.CharacterAttributes;
import net.littlelite.jurpe.characters.PC;
import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.characters.PrimaryStats;
import net.littlelite.jurpe.characters.Skill;
import net.littlelite.jurpe.combat.DamageMode;
import net.littlelite.jurpe.containers.Skills;
import net.littlelite.jurpe.items.Weapon;
import net.littlelite.jurpe.system.generation.Dice;
import net.littlelite.jurpe.world.Inn;

/**
 * Collection of GURPS related rules. Maintains a static variable (roll) to the
 * latest dice roll.
 * 
 * 
 */
public final class JurpeUtils
{

	private static int roll;
	private static Dice dice;

	static
	{
		roll = 0;
		dice = new Dice();
	}

	/**
	 * Creates a Playing Character
	 * 
	 * @param ca
	 *            Character Attributes
	 * @param abilitaDisponibili
	 *            Skills available (read from Livelli)
	 * @param maximumCharacterPoints
	 *            Points available to create this character
	 * @return PCharacter object
	 * @see Skills
	 * @see PCharacter
	 * @see Skill
	 * @see net.littlelite.jurpe.system.CharacterAttributes
	 */
	public static PCharacter generatePC(CharacterAttributes ca, Skills abilitaDisponibili, int maximumCharacterPoints)
	{
		PCharacter pg = new PCharacter(ca, maximumCharacterPoints, abilitaDisponibili);
		return pg;
	}

	/**
	 * Generates ST,DX,IQ,HT that matches given character points
	 * 
	 * @param characterPoints
	 *            Character Points
	 * @return ST,DX,IQ,HT that matches given character points
	 */
	public static PrimaryStats generateStats(int characterPoints)
	{

		int points = 0;

		int pFor = dice.throwDice(3);
		int pDes = dice.throwDice(3);
		int pInt = dice.throwDice(3);
		int pCos = dice.throwDice(3);

		while (points != characterPoints)
		{
			pFor = dice.throwDice(3);
			pDes = dice.throwDice(3);
			pInt = dice.throwDice(3);
			pCos = dice.throwDice(3);

			points = JurpeUtils.scoreValue(pCos);
			points += JurpeUtils.scoreValue(pDes);
			points += JurpeUtils.scoreValue(pFor);
			points += JurpeUtils.scoreValue(pInt);
		}

		return new PrimaryStats(pFor, pDes, pInt, pCos);
	}

	/**
	 * Creates a Playing Character, with default maximum points. (Maximum points
	 * are read from Config).
	 * 
	 * @param ca
	 *            Character Attributes
	 * @param abilitaDisponibili
	 *            Skills available (read from Livelli)
	 * @return PCharacter object
	 * @see Skills
	 * @see PCharacter
	 * @see net.littlelite.jurpe.characters.Skill
	 */
	public static PCharacter generatePC(CharacterAttributes ca, Skills abilitaDisponibili)
	{
		return JurpeUtils.generatePC(ca, abilitaDisponibili, Config.AVAILABLE_POINTS);
	}

	/**
	 * Computes character points.
	 * 
	 * @param pc
	 *            character to calculate points
	 * @return character points
	 */
	public static int computeCharacterPoints(PC pc)
	{
		PrimaryStats ps = pc.getPrimaryStats();
		int characterPoints = JurpeUtils.computeCharacterPoints(ps.getST(), ps.getDX(), ps.getIQ(), ps.getHT());
		return characterPoints;
	}

	/**
	 * Computes character points.
	 * 
	 * @param pCos
	 *            character health (HT)
	 * @param pDes
	 *            character dexterity (DX)
	 * @param pFor
	 *            character strength (ST)
	 * @param pInt
	 *            character intelligence (IQ)
	 * @return character points
	 */
	public static int computeCharacterPoints(int pCos, int pDes, int pFor, int pInt)
	{
		int characterPoints = 0;

		characterPoints += JurpeUtils.scoreValue(pCos);
		characterPoints += JurpeUtils.scoreValue(pDes);
		characterPoints += JurpeUtils.scoreValue(pFor);
		characterPoints += JurpeUtils.scoreValue(pInt);

		return characterPoints;
	}

	/**
	 * Computes first aid gained HT points. A minimum of 1 HT point is always
	 * restored.
	 * 
	 * @param firstAidSkillPoints
	 *            Points in First Aid skill
	 * @param techLevel
	 *            Technology Level of the World
	 * @return HT points gained with First Aid Skill, based on tech level
	 */
	public static int getFirstAid(int firstAidSkillPoints, int techLevel)
	{
		int dado = dice.throwDice();

		switch (techLevel)
		{
			case 1:
				dado -= 4;
				break;
			case 2:
			case 3:
				dado -= 3;
				break;
			case 4:
			case 5:
				dado -= 2;
				break;
			case 6:
			case 7:
				dado -= 1;
				break;
		}

		return Math.max(1, dado);

	}

	/**
	 * Time needed (in minutes) to get First Aid.
	 * 
	 * @param techLevel
	 *            Technology level of this world
	 * @return Minutes needed to get First Aid on a character
	 */
	public static int getFirstAidTime(int techLevel)
	{
		int minutes = 30;

		if (techLevel > 7)
		{
			minutes = 10;
		}

		return minutes;
	}

	/**
	 * Verifies a success roll
	 * 
	 * @param valore
	 *            Value to confront with
	 * @return True, if success
	 */
	public static boolean successRoll(int valore)
	{
		roll = dice.throwDice(3);

		if (roll <= valore)
		{
			return true;
		}

		return false;
	}

	/**
	 * To recover from uncosciousness you must rest for your current negative HT
	 * points minutes. (You set that at the Inn). Furthermore you roll against
	 * HT. If you fail you remain in coma.
	 * 
	 * @param pc
	 *            Unconscious character that needs to recover
	 * @return true if character successfully recovered.
	 * @see Inn
	 */
	public static boolean recoverFromUnconsciousness(PCharacter pc)
	{

		boolean recovers = false;

		if (pc.isUnconscious())
		{
			recovers = JurpeUtils.successRoll(pc.getPrimaryStats().getHT());
			if (recovers)
			{
				pc.setUnconscious(false);
			}
		}

		return recovers;

	}

	/**
	 * To use natural recovery you must have a day of rest and decent food (a
	 * night at Inn, in this implementation). With this you roll against HT. If
	 * success, you gain 1 HT point.
	 * 
	 * @param pc
	 *            PCharacter that needs recovery
	 * @return true if character recovers naturally
	 */
	public static boolean naturalRecovery(PCharacter pc)
	{
		boolean success = JurpeUtils.successRoll(pc.getPrimaryStats().getHT());
		if (success)
		{
			pc.addToCurrentHP(1);
		}
		return success;
	}

	/**
	 * Full recovery
	 * 
	 * @param pc
	 *            PCharacter that needs recovery
	 */
	public static void fullRecovery(PCharacter pc)
	{
		pc.restoreHP();
	}

	/**
	 * Get the latest roll of dice.
	 * 
	 * @return the latest dice roll
	 */
	public static int getLatestRoll()
	{
		return roll;
	}

	/**
	 * Returns a sum function of monster character points. You will have xx
	 * probabilities to find money. If you found money, the amount is computed
	 * as a random range between 0 and character points.
	 * 
	 * @param monster
	 *            reference to a PC
	 * @param percentile
	 *            number between 0 and 1. Probability to find some money.
	 * @return money looted to monster
	 */
	public static long lootMoney(PC monster, float percentile)
	{
		// When you loot a monster you receive an amount of money
		// function of monster character points
		int points = JurpeUtils.computeCharacterPoints(monster);
		long money = 0;
		float perc = percentile;

		if (perc < 0f)
		{
			perc = 0f;
		}
		else if (perc >= 1f)
		{
			perc = 1f;
		}

		// Found money?
		if (Math.random() < perc)
		{
			money = Math.round(Math.random() * (points * 0.3));
		}

		return money;
	}

	/**
	 * Returns true if dice roll is a critical success
	 * 
	 * @param dieRoll
	 *            dice roll
	 * @param skillLevel
	 *            character skill level
	 * @return true, if hit is critical
	 */
	public static boolean checkCriticalSuccess(int dieRoll, int skillLevel)
	{
		if (dieRoll < 5)
		{
			return true;
		}

		if (skillLevel == 15)
		{
			if (dieRoll < 6)
			{
				return true;
			}
		}

		if (skillLevel > 16)
		{
			if (dieRoll < 7)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if dice roll is a critical failure
	 * 
	 * @param dieRoll
	 *            dice roll
	 * @param skillLevel
	 *            character skill level
	 * @return true, if dice roll is a critical failure
	 */
	public static boolean checkCriticalFailure(int dieRoll, int skillLevel)
	{
		if (dieRoll == 18)
		{
			return true;
		}

		if (skillLevel > 15)
		{
			if (dieRoll == 17)
			{
				return true;
			}
		}

		if (dieRoll > skillLevel + 10)
		{
			return true;
		}

		return false;
	}

	/**
	 * Utility to display a String with Damage caused by Swing attack, ie: 1d-1.
	 * It is based on character ST
	 * 
	 * @param frz
	 *            PCharacter ST
	 * @return String such as 1d+2
	 */
	public static String getFormattedSwingDamage(int frz)
	{

		String dannoSwing = "0";

		switch (frz)
		{
			case 5:
				dannoSwing = "1d-5";
				break;
			case 6:
				dannoSwing = "1d-4";
				break;
			case 7:
				dannoSwing = "1d-3";
				break;
			case 8:
				dannoSwing = "1d-2";
				break;
			case 9:
				dannoSwing = "1d-1";
				break;
			case 10:
				dannoSwing = "1d";
				break;
			case 11:
				dannoSwing = "1d+1";
				break;
			case 12:
				dannoSwing = "1d+2";
				break;
			case 13:
				dannoSwing = "2d-1";
				break;
			case 14:
				dannoSwing = "2d";
				break;
			case 15:
				dannoSwing = "2d+1";
				break;
			case 16:
				dannoSwing = "2d+2";
				break;
			case 17:
				dannoSwing = "3d-1";
				break;
			case 18:
				dannoSwing = "3d";
				break;
			case 19:
				dannoSwing = "3d+1";
				break;
			case 20:
				dannoSwing = "3d+2";
				break;
		}

		return dannoSwing;

	}

	/**
	 * Utility to compute damage for Swing Attack It is based on character ST
	 * 
	 * @param frz
	 *            PCharacter ST
	 * @return damage, based on dice rolls
	 */
	public static int getSwingDamage(int frz)
	{

		int dannoSwing = 0;
		Dice dice = new Dice();

		switch (frz)
		{
			case 5:
				dannoSwing = dice.throwDice() - 5;
				break;
			case 6:
				dannoSwing = dice.throwDice() - 4;
				break;
			case 7:
				dannoSwing = dice.throwDice() - 3;
				break;
			case 8:
				dannoSwing = dice.throwDice() - 2;
				break;
			case 9:
				dannoSwing = dice.throwDice() - 1;
				break;
			case 10:
				dannoSwing = dice.throwDice();
				break;
			case 11:
				dannoSwing = dice.throwDice() + 1;
				break;
			case 12:
				dannoSwing = dice.throwDice() + 2;
				break;
			case 13:
				dannoSwing = dice.throwDice(2) - 1;
				break;
			case 14:
				dannoSwing = dice.throwDice(2);
				break;
			case 15:
				dannoSwing = dice.throwDice(2) + 1;
				break;
			case 16:
				dannoSwing = dice.throwDice(2) + 2;
				break;
			case 17:
				dannoSwing = dice.throwDice(3) - 1;
				break;
			case 18:
				dannoSwing = dice.throwDice(3);
				break;
			case 19:
				dannoSwing = dice.throwDice(3) + 1;
				break;
			case 20:
				dannoSwing = dice.throwDice(3) + 2;
				break;
		}

		return dannoSwing;

	}

	/**
	 * Utility to compute damage for Swing Attack when die roll is supposed to
	 * be 6. It is based on character ST
	 * 
	 * @param frz
	 *            PCharacter ST
	 * @return damage, based on dice rolls
	 */
	public static int getMaxSwingDamage(int frz)
	{

		int dannoSwing = 0;

		switch (frz)
		{
			case 5:
				dannoSwing = 1;
				break;
			case 6:
				dannoSwing = 2;
				break;
			case 7:
				dannoSwing = 3;
				break;
			case 8:
				dannoSwing = 4;
				break;
			case 9:
				dannoSwing = 5;
				break;
			case 10:
				dannoSwing = 6;
				break;
			case 11:
				dannoSwing = 7;
				break;
			case 12:
				dannoSwing = 8;
				break;
			case 13:
				dannoSwing = 11;
				break;
			case 14:
				dannoSwing = 12;
				break;
			case 15:
				dannoSwing = 13;
				break;
			case 16:
				dannoSwing = 14;
				break;
			case 17:
				dannoSwing = 17;
				break;
			case 18:
				dannoSwing = 18;
				break;
			case 19:
				dannoSwing = 19;
				break;
			case 20:
				dannoSwing = 20;
				break;
		}

		return dannoSwing;

	}

	/**
	 * Utility to display a String with Damage caused by Thrust attack, ie:
	 * 1d-1. It is based on character ST
	 * 
	 * @param frz
	 *            PCharacter ST
	 * @return String like "1d-1"
	 */
	public static String getFormattedDannoThrust(int frz)
	{

		String dannoThrust = "0";

		switch (frz)
		{
			case 5:
				dannoThrust = "1d-5";
				break;
			case 6:
				dannoThrust = "1d-4";
				break;
			case 7:
				dannoThrust = "1d-3";
				break;
			case 8:
				dannoThrust = "1d-3";
				break;
			case 9:
				dannoThrust = "1d-2";
				break;
			case 10:
				dannoThrust = "1d-2";
				break;
			case 11:
				dannoThrust = "1d-1";
				break;
			case 12:
				dannoThrust = "1d-1";
				break;
			case 13:
				dannoThrust = "1d";
				break;
			case 14:
				dannoThrust = "1d";
				break;
			case 15:
				dannoThrust = "1d+1";
				break;
			case 16:
				dannoThrust = "1d+1";
				break;
			case 17:
				dannoThrust = "1d+2";
				break;
			case 18:
				dannoThrust = "1d+2";
				break;
			case 19:
				dannoThrust = "2d-1";
				break;
			case 20:
				dannoThrust = "2d-1";
				break;
		}

		return dannoThrust;
	}

	/**
	 * Utility to compute Damage caused by Thrust attack, It is based on
	 * character ST
	 * 
	 * @param frz
	 *            PCharacter ST
	 * @return damage, based on dice rolls
	 */
	public static int getThrustDamage(int frz)
	{

		int dannoThrust = 0;
		Dice d = new Dice();

		switch (frz)
		{
			case 5:
				dannoThrust = d.throwDice() - 5;
				break;
			case 6:
				dannoThrust = d.throwDice() - 4;
				break;
			case 7:
				dannoThrust = d.throwDice() - 3;
				break;
			case 8:
				dannoThrust = d.throwDice() - 3;
				break;
			case 9:
				dannoThrust = d.throwDice() - 2;
				break;
			case 10:
				dannoThrust = d.throwDice() - 2;
				break;
			case 11:
				dannoThrust = d.throwDice() - 1;
				break;
			case 12:
				dannoThrust = d.throwDice() - 1;
				break;
			case 13:
				dannoThrust = d.throwDice();
				break;
			case 14:
				dannoThrust = d.throwDice();
				break;
			case 15:
				dannoThrust = d.throwDice() + 1;
				break;
			case 16:
				dannoThrust = d.throwDice() + 1;
				break;
			case 17:
				dannoThrust = d.throwDice() + 2;
				break;
			case 18:
				dannoThrust = d.throwDice() + 2;
				break;
			case 19:
				dannoThrust = d.throwDice(2) - 1;
				break;
			case 20:
				dannoThrust = d.throwDice(2) - 1;
				break;
		}

		return dannoThrust;
	}

	/**
	 * Utility to compute Damage caused by Thrust attack, when die roll is
	 * supposed to be 6. It is based on character ST
	 * 
	 * @param frz
	 *            PCharacter ST
	 * @return damage, based on dice rolls
	 */
	public static int getMaxThrustDamage(int frz)
	{

		int dannoThrust = 0;

		switch (frz)
		{
			case 5:
				dannoThrust = 1;
				break;
			case 6:
				dannoThrust = 2;
				break;
			case 7:
				dannoThrust = 3;
				break;
			case 8:
				dannoThrust = 4;
				break;
			case 9:
				dannoThrust = 4;
				break;
			case 10:
				dannoThrust = 4;
				break;
			case 11:
				dannoThrust = 5;
				break;
			case 12:
				dannoThrust = 5;
				break;
			case 13:
				dannoThrust = 6;
				break;
			case 14:
				dannoThrust = 6;
				break;
			case 15:
				dannoThrust = 7;
				break;
			case 16:
				dannoThrust = 7;
				break;
			case 17:
				dannoThrust = 8;
				break;
			case 18:
				dannoThrust = 10;
				break;
			case 19:
				dannoThrust = 11;
				break;
			case 20:
				dannoThrust = 11;
				break;
		}

		return dannoThrust;
	}

	/**
	 * Computer damage points when an unarmed attack succeeds. Effective damage
	 * depends on <br>
	 * <ul>
	 * <li>damage roll
	 * <li>damage resistance
	 * </ul>
	 * 
	 * @param damageRoll
	 *            dice roll for damage
	 * @param damageResistance
	 *            armor points of defender
	 * @return effective damage done
	 */
	public static int computeDamage(int damageRoll, int damageResistance)
	{
		int effectiveDamage = 0;
		effectiveDamage = Math.max(0, damageRoll - damageResistance);

		return effectiveDamage;
	}

	/**
	 * Computer damage points when an attack succeeds. Effective damage depends
	 * on <br>
	 * <ul>
	 * <li>damage roll
	 * <li>weapon mode (impaling, crushing, cutting)
	 * <li>damage resistance
	 * </ul>
	 * 
	 * @param damageResistance
	 *            armor points of defender
	 * @param damageRoll
	 *            dice roll for damage
	 * @param weaponMode
	 *            mode of attack (ie: Weapon.MODE_CRU)
	 * @return effective damage done
	 * @see Weapon
	 */
	public static int computeWeaponDamage(int damageRoll, DamageMode weaponMode, int damageResistance)
	{
		int effectiveDamage = 0;
		int minimumDamage = 1;

		if (weaponMode == DamageMode.CRUSHING)
		{
			minimumDamage = 0;
		}

		// Damage before any DR is subtracted
		effectiveDamage = damageRoll;
		// Damage with DR
		effectiveDamage -= damageResistance;

		if (effectiveDamage > 0)
		{
			// Damage with damage type modifier
			switch (weaponMode)
			{
				case CRUSHING:
				default:
					// No modifier
					break;
				case CUTTING:
					effectiveDamage = Math.round(effectiveDamage * 1.5f);
					break;
				case IMPALING:
					effectiveDamage *= 2;
					break;
			}
		}
		else
			effectiveDamage = minimumDamage;

		return effectiveDamage;
	}

	/**
	 * Computes character points for some attributes, such as ST and DX. For
	 * instance, a ST 6 is valued -30 character points. It is useful in
	 * character creation and points computation.
	 * 
	 * @param punteggio
	 *            Attribute value to compute correspondent character points
	 * @return Number to add to current character points
	 */
	public static int scoreValue(int score)
	{

		int v = 0;

		switch (score)
		{
			case 1:
				v = -80;
				break;
			case 2:
				v = -70;
				break;
			case 3:
				v = -60;
				break;
			case 4:
				v = -50;
				break;
			case 5:
				v = -40;
				break;
			case 6:
				v = -30;
				break;
			case 7:
				v = -20;
				break;
			case 8:
				v = -15;
				break;
			case 9:
				v = -10;
				break;
			case 10:
				v = 0;
				break;
			case 11:
				v = 10;
				break;
			case 12:
				v = 20;
				break;
			case 13:
				v = 30;
				break;
			case 14:
				v = 45;
				break;
			case 15:
				v = 60;
				break;
			case 16:
				v = 80;
				break;
			case 17:
				v = 100;
				break;
			case 18:
				v = 125;
				break;
			default:
				v = -100;
		}

                // Superior levels
		if (v == -100)
		{
			v = (125 + 25 * (score - 18));
		}

		return v;
	}

	private JurpeUtils()
	{
	}

}
