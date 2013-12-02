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

package net.littlelite.jurpe.characters;

/**

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

import net.littlelite.jurpe.combat.AllOutAttackType;
import net.littlelite.jurpe.combat.Defense;
import net.littlelite.jurpe.combat.DefenseType;
import net.littlelite.jurpe.items.Shield;
import net.littlelite.jurpe.items.Weapon;
import net.littlelite.jurpe.system.JurpeUtils;
import net.littlelite.jurpe.system.generation.Dice;
import net.littlelite.jurpe.system.resources.LibraryStrings;
import net.littlelite.utils.AxsRand;

/**
 * Type class for monster (Monster). Monster is a PC (Playing Character)
 * 
 * 
 */
public class Monster extends PC implements Serializable
{

	private static final long serialVersionUID = 3317L;

	/**
	 * It serves for datagrids to display monster fields, such as ST, DX, MVMT
	 * and others. It says how many properties a monster have.
	 */
	public static final int MONSTERFIELDS = 9;

	/**
	 * Monster armor (scales, shell, hide, thick fat...)
	 */
	protected int armor;

	/**
	 * Monster weapon/shield
	 */
	protected Weapon curWeapon;
	protected int curWeaponLevel = 0;
	protected Shield curShield;
	protected int curShieldLevel = 0;

	// MVMT Bonus
	private int mvmtBonus = 0;

	/**
	 * Creates new monster.
	 * 
	 * @param ca
	 *            Monster Attributes
	 * @param armr
	 *            Monster Natural armor Points
	 */
	public Monster(CharacterAttributes ca, int armr)
	{
		super(ca);
		Dice dc = new Dice();
		this.mvmtBonus = dc.throwDice();
		this.isArtificial = true;
		this.armor = armr;
		// Armor will be randomly assigned to PD or DR
		AxsRand axs = AxsRand.getReference();
		if (axs.randBoolean())
		{
			this.damageResistance = armr;
		}
		else
		{
			this.passiveDefense = armr;
		}
	}

	/**
	 * Restore current hit points. Overrides PC.restoreHT()
	 */
	public void restoreHT()
	{
		this.getPrimaryStats().restoreHitPoints();
	}

	/**
	 * Get Monster armor
	 * 
	 * @return Monster Armor points
	 */
	public int getArmor()
	{
		return armor;
	}

	public int getCurrentHP()
	{
		return this.getPrimaryStats().getCurrentHitPoints();
	}

	/**
	 * Get Monster MVMT
	 * 
	 * @return Returns Monster Movement Rate
	 */
	public int getMvmt()
	{

		int mvmt = this.mvmtBonus;

		int roundedVel = this.getPrimaryStats().getMove();
		mvmt += roundedVel;

		// Injury modifiers
		int ht = this.getCurrentHP();
		if (ht > 0 && ht <= 3)
		{
			mvmt /= 2;
		}

		return mvmt;
	}

	/**
	 * Get properties of monster as an array of Strings
	 * 
	 * @return Array of Strings for monster
	 */
	public String[] toStrings()
	{
		CharacterAttributes ca = this.getCharacterAttributes();
		PrimaryStats ps = ca.primaryStats();

		String[] strs = new String[MONSTERFIELDS];
		strs[0] = ca.getName();
		strs[1] = this.getHTHP();
		strs[2] = String.valueOf(ps.getST());
		strs[3] = String.valueOf(ps.getDX());
		strs[4] = String.valueOf(ps.getIQ());
		strs[5] = String.valueOf(ps.getMove());
		strs[6] = String.valueOf(this.armor);
		if (this.wearsWeapon())
		{
			Weapon wpn = this.getCurrentWeapon();
			strs[7] = wpn.getName();
		}
		else
		{
			strs[7] = "None";
		}
		if (this.getCurrentShield() != null)
		{
			strs[8] = this.getCurrentShield().getName();
		}
		else
		{
			strs[8] = "None";
		}
		return strs;
	}

	/**
	 * @return True when monster is ready to hit.
	 */
	public boolean isReady()
	{
		boolean isReady = false;

		// Unconscious character is always unready!
		if (this.isUnconscious)
		{
			return false;
		}

		// Bare combat is always ready
		if (this.wearsWeapon())
		{
			Weapon currentWeapon = this.getCurrentWeapon();
			isReady = currentWeapon.isReady();
		}
		else
		{
			isReady = true;
		}

		return isReady;

	}

	/**
	 * A monster is alive if: <br>
	 * <ul>
	 * <li>Has HT>0
	 * </ul>
	 * 
	 * @return false if character is dead
	 */
	public boolean isAlive()
	{
		if (this.getCurrentHP() < 0)
		{
			this.isAlive = false;
		}

		return this.isAlive;
	}

	/**
	 * Set monster's shield and level
	 * 
	 * @param shield
	 *            Shield owned by monster
	 * @param level
	 *            Level of skill in using that weapon
	 */
	public void setMonsterShield(Shield shield, int level)
	{
		this.curShield = shield;
		this.curShieldLevel = level;
	}

	/**
	 * Monster shield.
	 * 
	 * @return Weapon owned by monster
	 */
	public Shield getCurrentShield()
	{
		return this.curShield;
	}

	/**
	 * Monster shield level.
	 * 
	 * @return Monster shield skill level
	 */
	public int getCurrentShieldLevel()
	{
		return this.curShieldLevel;
	}

	/**
	 * Set monster's weapon and level.
	 * 
	 * @param weapon
	 *            Weapon owned by monster
	 * @param level
	 *            Monster weapon skill level
	 */
	public void setMonsterWeapon(Weapon weapon, int level)
	{
		this.curWeapon = weapon;
		this.curWeaponLevel = level;
	}

	/**
	 * Monster weapon.
	 * 
	 * @return Monster Weapon. null if monster has no weapon
	 * @see Weapon
	 */
	public Weapon getCurrentWeapon()
	{
		return this.curWeapon;
	}

	/**
	 * Monster ranged weapon.
	 * 
	 * @return Monster Weapon. null if monster has no weapon
	 * @see Weapon
	 */
	public Weapon getCurrentRangedWeapon()
	{
		return null; // for now monsters cannot have ranged weapons
	}

	/**
	 * Monster weapon SKILL level. Since Monsters make attack roll against their
	 * DX, their weapon skill is DX. If they have a weapon, this is the monster
	 * skill with that weapon.
	 * 
	 * @return Monster weapon Level
	 */
	public int getCurrentWeaponLevel()
	{
		int level = 0;

		if (this.curWeapon == null)
		{
			level = this.getBareHandsSkill();
		}
		else
		{
			level = this.curWeaponLevel;
		}

		return level;
	}

	/**
	 * Monsters do not (yet) fire ranged weapon
	 */
	public int getCurrentRangedWeaponLevel()
	{
		return 0;
	}

	/**
	 * Monster total passive defenses. Equal to armor if monster has no shield.
	 * Else, Armor + Shield Passive Defence
	 * 
	 * @return Monster Passive Defense
	 */
	public float getTotalPassiveDefenses()
	{
		float def = this.armor;
		if (this.curShield != null)
		{
			def += this.curShield.getPassiveDefense();
		}
		return this.armor;
	}

	/**
	 * Taken from GURPSLite: Dodge is the only active defense for a beast
	 * without a shield. It is equal to half DX or half move, whichever is
	 * better up to a maximum of 10. If beast has a shield, she always tries to
	 * block with active shield.
	 * 
	 * @return Monster Total Active Defenses.
	 */
	public float getTotalActiveDefenses()
	{

		int activePoints = 0;

		if (this.curShield == null)
		{
			activePoints = Math.max((this.getPrimaryStats().getDX() / 2), (this.getMvmt() / 2));
			activePoints = Math.max(10, activePoints);
		}
		else
		{
			Defense def = new Defense(this);
			activePoints = def.getActiveDefensePoints(DefenseType.ACTIVE_BLOCK);
		}

		return activePoints;

	}

	/**
	 * Get monster short description following this pattern: Name:
	 * ST,DX,IQ,HT/HTmax
	 * 
	 * @return short character description
	 */
	public String getShortDescription()
	{
		StringBuilder sb = new StringBuilder(this.getCharacterAttributes().getName());
		PrimaryStats ps = this.getPrimaryStats();
		if (this.isAI())
		{
			sb.append(" (CPU)");
		}
		sb.append(" ");
		sb.append("ST: ");
		sb.append(ps.getST());
		sb.append(" DX: ");
		sb.append(ps.getDX());
		sb.append(" IQ: ");
		sb.append(ps.getIQ());
		sb.append(" HT: ");
		sb.append(this.getHTHP());
		sb.append("(");
		sb.append(ps.getInitialHitPoints());
		sb.append(")");

		return sb.toString();
	}

	/**
	 * Monsters have no ranged weapons
	 */
	public int getRangedWeaponDamage()
	{
		return 0;
	}

	/**
	 * A Monster bite depends on its ST. The following table is taken from
	 * CORPS.
	 * 
	 * @return Monster Damage Roll (without a weapon - byte)
	 */
	public int getWeaponDamage()
	{
		int dmg = 0;
		int st = this.getPrimaryStats().getST();
		Dice d = new Dice();

		switch (st)
		{
			case 1:
			case 2:
				dmg = d.throwDice() - 5;
				break;
			case 3:
			case 4:
			case 5:
				dmg = d.throwDice() - 4;
				break;
			case 6:
			case 7:
			case 8:
				dmg = d.throwDice() - 3;
				break;
			case 9:
			case 10:
			case 11:
				dmg = d.throwDice() - 2;
				break;
			case 12:
			case 13:
			case 14:
			case 15:
				dmg = d.throwDice() - 1;
				break;
			case 16:
			case 17:
			case 18:
			case 19:
			case 20:
				dmg = d.throwDice();
				break;
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
				dmg = d.throwDice() + 1;
				break;
			case 26:
			case 27:
			case 28:
			case 29:
			case 30:
				dmg = d.throwDice() + 2;
				break;
			case 31:
			case 32:
			case 33:
			case 34:
			case 35:
				dmg = d.throwDice(2) - 1;
				break;
			case 36:
			case 37:
			case 38:
			case 39:
			case 40:
				dmg = d.throwDice(2);
				break;
			default:
				dmg = d.throwDice(st / 20) + (st | 20);
		}

		return Math.max(dmg, 0);
	}

	public int getWeaponMaxDamage()
	{
		int st = this.getPrimaryStats().getST();
		int dmg = 0;

		switch (st)
		{
			case 1:
			case 2:
				dmg = 1;
				break;
			case 3:
			case 4:
			case 5:
				dmg = 2;
				break;
			case 6:
			case 7:
			case 8:
				dmg = 3;
				break;
			case 9:
			case 10:
			case 11:
				dmg = 4;
				break;
			case 12:
			case 13:
			case 14:
			case 15:
				dmg = 5;
				break;
			case 16:
			case 17:
			case 18:
			case 19:
			case 20:
				dmg = 6;
				break;
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
				dmg = 7;
				break;
			case 26:
			case 27:
			case 28:
			case 29:
			case 30:
				dmg = 8;
				break;
			case 31:
			case 32:
			case 33:
			case 34:
			case 35:
				dmg = 11;
				break;
			case 36:
			case 37:
			case 38:
			case 39:
			case 40:
				dmg = 12;
				break;
			default:
				dmg = 6 * (st / 20) + (st | 20);
		}

		return Math.max(dmg, 0);
	}

	/**
	 * Creates a Non Playing Character (monster), with given points points.
	 * 
	 * @param nm
	 *            PCharacter's name
	 * @param points
	 *            Monster overall value
	 * @return Monster object
	 * @see Monster
	 * @see JurpeUtils
	 */
	public static Monster createRandom(String nm, int points)
	{

		Dice d = new Dice();

		int puntiCharacter = 0;
		int pST = 0, pDX = 0, pIN = 0, pHT = 0;

		while (puntiCharacter != points)
		{
			pST = d.throwDice(8);
			pDX = d.throwDice(3);
			pIN = Math.min(6, d.throwDice(1));
			pHT = d.throwDice(3);

			puntiCharacter = JurpeUtils.computeCharacterPoints(pST, pDX, pIN, pHT);
		}

		// Hit points are HT plus two random dice
		int pf = pHT + d.throwDice(2);
		int armor = Math.max(0, 3 - d.throwDice());

		PrimaryStats ps = new PrimaryStats(pST, pDX, pIN, pHT, pf);
		CharacterAttributes ca = new CharacterAttributes(nm, ps);

		Monster monster = new Monster(ca, armor);

		return monster;

	}

	/**
	 * Get current skill in unarmed combat.
	 * 
	 * @return Monster Skill Level in unarmed combat.
	 */
	public int getBareHandsSkill()
	{
		return (this.getPrimaryStats().getDX() + 2);
		// plus 2 made on experience...
	}

	/**
	 * Get AllOutAttack Type. AI can choose any type of AOA, except the TWO
	 * attack when it wears an unbalanced weapon.
	 * 
	 * @return Current All Out Attack Mode for this character.
	 */
	public AllOutAttackType getAllOutAttackType()
	{
		AllOutAttackType attack = AllOutAttackType.BONUSDAMG;
		AxsRand rand = AxsRand.getReference();
		byte x = rand.randByte((byte) 3);
		Weapon currentWeapon = this.getCurrentWeapon();
		switch (x)
		{
			case 0:
			default:
				attack = AllOutAttackType.BONUSDAMG;
				break;
			case 1:
				attack = AllOutAttackType.BONUSKILL;
				break;
			case 2:
				if (currentWeapon != null)
				{
					if (currentWeapon.isBalanced())
					{
						attack = AllOutAttackType.TWOATTACK;
					}
				}
				else
				{
					attack = AllOutAttackType.BONUSKILL;
				}
				break;
		}

		return attack;
	}

	/**
	 * If a character is a beast, he will have HT in the form HT/current HP
	 * 
	 * @return String describing HT/HP
	 */
	public String getHTHP()
	{

		int mHP = this.getPrimaryStats().getInitialHitPoints();
		int mHT = this.getPrimaryStats().getHT();
		if (mHT != mHP)
		{
			return (String.valueOf(mHT) + "/" + String.valueOf(mHP));
		}

		return String.valueOf(mHP);

	}

	/**
	 * Get verbose info
	 * 
	 * @param separator
	 *            Character or string to separate (ie:\n)
	 * @return character summary
	 */
	public String getInfo(final String separator)
	{
		StringBuilder sb = new StringBuilder(this.getCharacterAttributes().getName());
		if (this.isAI())
		{
			sb.append(" (CPU)");
		}
		sb.append(separator);
		sb.append(this.getStats(separator));

		return sb.toString();
	}

	/**
	 * Get info about monster statistics
	 * 
	 * @param separator
	 *            Field separator
	 * @return info about monster statistics
	 */
	public String getStats(final String separator)
	{
		PrimaryStats ps = this.getPrimaryStats();

		StringBuilder sb = new StringBuilder();
		sb.append(LibraryStrings.FOR);
		sb.append(":");
		sb.append(ps.getST());
		sb.append(separator);
		sb.append(LibraryStrings.DEX);
		sb.append(":");
		sb.append(ps.getDX());
		sb.append(separator);
		sb.append(LibraryStrings.INT);
		sb.append(":");
		sb.append(ps.getIQ());
		sb.append(separator);
		sb.append(LibraryStrings.COS);
		sb.append(":");
		sb.append(ps.getHT());
		sb.append("/");
		sb.append(ps.getCurrentHitPoints());
		sb.append("(");
		sb.append(ps.getInitialHitPoints());
		sb.append(")");
		sb.append(separator);
		sb.append(LibraryStrings.RD);
		sb.append(":");
		sb.append(this.getDamageResistance());
		sb.append(separator);
		sb.append(LibraryStrings.PD);
		sb.append(":");
		sb.append(this.getPassiveDefense());
		sb.append(separator);
		sb.append(LibraryStrings.MVMT);
		sb.append(":");
		sb.append(ps.getMove());
		sb.append(separator);
		if (this.wearsWeapon())
		{
			sb.append(LibraryStrings.WEAPON);
			sb.append(":");
			sb.append(this.getCurrentWeapon().toString());
		}
		return sb.toString();
	}

	/**
	 * Monster current shield
	 * 
	 * @return Current shield
	 */
	public Shield getCurShield()
	{
		return curShield;
	}

	/**
	 * Monster current shield level
	 * 
	 * @return Current shield level
	 */
	public int getCurShieldLevel()
	{
		return curShieldLevel;
	}

	/**
	 * Monster current weapon
	 * 
	 * @return Current weapon
	 */
	public Weapon getCurWeapon()
	{
		return curWeapon;
	}

	/**
	 * Monster current weapon level
	 * 
	 * @return Current weapon level
	 */
	public int getCurWeaponLevel()
	{
		return curWeaponLevel;
	}

	/**
	 * Monster movement bonus
	 * 
	 * @return Monster movement bonus
	 */
	public int getMvmtBonus()
	{
		return mvmtBonus;
	}

	/**
	 * Set monster movement bonus
	 * 
	 * @param i
	 *            Monster movement bonus
	 */
	public void setMvmtBonus(int i)
	{
		mvmtBonus = i;
	}

	/**
	 * Set monster armor level
	 * 
	 * @param i
	 *            Monster armor leve
	 */
	public void setArmor(int i)
	{
		armor = i;
	}

	/**
	 * Set monster current shield
	 * 
	 * @param shield
	 *            Shield to wear
	 */
	public void setCurShield(Shield shield)
	{
		curShield = shield;
	}

	/**
	 * Set monster current shield skill level
	 * 
	 * @param i
	 *            Current shield skill level
	 */
	public void setCurShieldLevel(int i)
	{
		curShieldLevel = i;
	}

	/**
	 * Set monster current weapon
	 * 
	 * @param weapon
	 *            Weapon to wear
	 */
	public void setCurWeapon(Weapon weapon)
	{
		curWeapon = weapon;
	}

	/**
	 * Set monster weapon skill level
	 * 
	 * @param i
	 *            Weapon skill level
	 */
	public void setCurWeaponLevel(int i)
	{
		curWeaponLevel = i;
	}

	/**
	 * Description of this Monster
	 * 
	 * @return description
	 */
        @Override
	public String toString()
	{
		StringBuilder desc = new StringBuilder(this.charAttributes.getName());
		desc.append(" (");
		desc.append(this.charAttributes.primaryStats().toString());
		desc.append(" ");
		desc.append(LibraryStrings.SPD);
		desc.append(":");
		desc.append(this.charAttributes.primaryStats().getSpeed());
		desc.append(")");
		if (this.curWeapon != null)
		{
			desc.append(" with ");
			desc.append(this.curWeapon.toString());
		}
		return desc.toString();
	}

}
