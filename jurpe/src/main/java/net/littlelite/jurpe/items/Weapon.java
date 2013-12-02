package net.littlelite.jurpe.items;

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

import java.util.AbstractList;

import net.littlelite.jurpe.characters.AbstractPC;
import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.characters.Skill;
import net.littlelite.jurpe.combat.DamageMode;
import net.littlelite.jurpe.combat.DamageType;
import net.littlelite.jurpe.combat.WeaponDamage;
import net.littlelite.jurpe.containers.MasterShop;
import net.littlelite.jurpe.containers.Skills;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.JurpeUtils;
import net.littlelite.jurpe.system.OSProps;
import net.littlelite.jurpe.system.generation.CostGenerator;
import net.littlelite.jurpe.system.generation.ItemValueType;
import net.littlelite.jurpe.system.generation.RandomNames;
import net.littlelite.jurpe.system.resources.LibraryStrings;
import net.littlelite.utils.AxsRand;

/**
 * Weapon Type Class. Every weapon must have a skill (Skill) to use it. Also,
 * every weapons has Swing and Thrust damage for each type of Attack: Cutting,
 * Impalling, Crushing.
 * 
 * @see AbstractItem
 */

public class Weapon extends AbstractItem
{

	private static final long serialVersionUID = 3317L;

	/**
	 * Attack available for this weapon. IE: sw+2 imp-1
	 */
	private WeaponAttack att;

	/**
	 * Max Damage this weapon can do
	 */
	private int weaponMaxDamage;

	/**
	 * Selected mode for this weapon.
	 */
	private DamageMode selectedMode;

	/**
	 * Min Strength required to use this Weapon
	 */
	private int weaponMinStrength;

	/**
	 * Associated skill for using weapon
	 * 
	 * @see Skill
	 */
	private Skill skill;

	/**
	 * If the weapon is ranged this value is > 0
	 */
	private int range;

	/**
	 * If this weapon is balanced, here we store if it's ready to use.
	 */
	private boolean ready;

	/**
	 * If this weapon is balanced
	 */
	private boolean balanced;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            Weapon's name
	 * @param att
	 *            Available Attack types for this weapon
	 * @param cost
	 *            Weapon's Cost
	 * @param weight
	 *            Weapon's Weight
	 * @param skl
	 *            Required Skill to use this weapon
	 * @param minStrength
	 *            Minimal Strength (ST) to use Weapon
	 * @param maxDamage
	 *            Max Damage this weapon can do, if any
	 * @param wRange
	 *            Weapon's range (if ranged)
	 */
	public Weapon(String name, WeaponAttack att, int cost, int weight, int maxDamage, int minStrength, int wRange, Skill skl)
	{
		super(ItemType.WEAPON, name, cost, weight);
		this.initialize(att, maxDamage, minStrength, wRange, skl);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            Weapon's name
	 * @param cost
	 *            Weapon's Cost
	 * @param weight
	 *            Weapon's Weight
	 * @param skl
	 *            Required Skill to use this weapon
	 * @param dCUT_SW
	 *            Amount of damage made by Cutting/Swing.
	 * @param dCUT_TH
	 *            Amount of damage made by Cutting/Thrust
	 * @param dCR_SW
	 *            Amount of damage made by Crushing/Swing
	 * @param dCR_TH
	 *            Amount of damage made by Crushing/Thrust
	 * @param dIMP_SW
	 *            Amount of damage made by Impalling/Swing
	 * @param dIMP_TH
	 *            Amount of damage made by Impalling/Thrust
	 * @param minStrength
	 *            Minimal Strength (ST) to use Weapon
	 * @param maxDamage
	 *            Max Damage this weapon can do, if any
	 * @param wRange
	 *            Weapon's range (if ranged)
	 */
	public Weapon(String name, int cost, int weight, Skill skl, int dCUT_SW, int dCUT_TH, int dCR_SW, int dCR_TH, int dIMP_SW, int dIMP_TH, int minStrength,
			int maxDamage, int wRange)
	{
		super(ItemType.WEAPON, name, cost, weight);
		WeaponDamage dannoDaTaglio = new WeaponDamage(dCUT_SW, dCUT_TH); // cut
		WeaponDamage dannoDaImpalamento = new WeaponDamage(dIMP_SW, dIMP_TH); // imp
		WeaponDamage dannoDaBotta = new WeaponDamage(dCR_SW, dCR_TH); // cr
		WeaponAttack attk = new WeaponAttack(dannoDaTaglio, dannoDaBotta, dannoDaImpalamento);
		this.initialize(attk, maxDamage, minStrength, wRange, skl);
	}

	/**
	 * Creates a random generated Weapon
	 * 
	 * @param value
	 *            ItemValueType indicating rarity of Item
	 * @param rn
	 *            Handle to Random Names
	 * @param ms
	 *            Handle to MasterShop
	 * @param sk
	 *            Handle to Skills for this world
	 * @return newly created Weapon
	 * @throws JurpeException
	 */
	public static Weapon createRandom(ItemValueType value, 
			                          RandomNames rn, MasterShop ms, Skills sk) throws JurpeException
	{

		// Random weapon creation means the following:
		// 1. Randomly select Skill
		// 2. Get Weapons associated with this skill (from shop.xml)
		// 3. Randomly select Weapon
		// 4. Random modification of values
		// 5. Name of the weapon
		// 6. Random cost, weight,maxDamage,minStrength

		// 1
		AxsRand rand = AxsRand.getReference();
		AbstractList<Skill> weapSkills = sk.getWeapon();
		AbstractList<Weapon> weapons = null;
		Skill skill = null;
		int availableWeapons = 0;
		int tries = 0;
		int xrange = 0;
		while (availableWeapons < 1)
		{
			tries++;
			if (tries > 500)
			{
				throw new JurpeException("Can't create weapon!");
			}
			int skillNum = rand.randInt(weapSkills.size());
			skill = weapSkills.get(skillNum);
			// 2
			weapons = ms.getWeapons(skill, false);
			availableWeapons = weapons.size();
		}
		// 3
		Weapon selectedWeapon = weapons.get(rand.randInt(availableWeapons));
		WeaponAttack weaponAttack = selectedWeapon.getAttackTypes();
		// 4
		int damage = 0;
		WeaponDamage cutting = weaponAttack.getCuttingDamage();
		WeaponDamage impaling = weaponAttack.getImpalingDamage();
		WeaponDamage crushing = weaponAttack.getCrushingDamage();
		int weaponRarity = value.getType() / 10;
		if (cutting.isValid())
		{
			damage = cutting.getModifier() + Weapon.randomDamageModifier(weaponRarity);
			cutting.setModifier(damage);
		}
		if (impaling.isValid())
		{
			damage = impaling.getModifier() + Weapon.randomDamageModifier(weaponRarity);
			impaling.setModifier(damage);
		}
		if (crushing.isValid())
		{
			damage = impaling.getModifier() + Weapon.randomDamageModifier(weaponRarity);
			crushing.setModifier(damage);
		}
		if (skill.getName().equals("Bow")) // If the weapon's skill is Bow, we
											// determine a range
		{
			xrange = Math.max(3, rand.randInt(9));
		}
		WeaponAttack att = new WeaponAttack(cutting, crushing, impaling);
		// 5
		StringBuilder name = new StringBuilder(rn.getAttribute(value));
		name.append(" ");
		name.append(selectedWeapon.getName());
		name.append(" ");
		name.append(rn.getGeography(value));
		//
		int weight = selectedWeapon.getWeight();
		// +rand.randInt(value.getType())-rand.randInt(Math.max(0,damage));
		CostGenerator cg = new CostGenerator(selectedWeapon.getCost(), damage, weight);
		int cost = cg.getCost();
		// Balanced weapons should cost a little more
		if (selectedWeapon.isBalanced())
		{
			cost += 200;
		}
		// Low damage weapons are cheaper (ie: a knife is balanced but with low
		// damage)
		if (damage < 1)
		{
			cost -= 300;
		}
		int maxDamage = Math.max(0, selectedWeapon.getMaxDamage() + rand.randInt(6) - rand.randInt(6));
		if (maxDamage > 2 && maxDamage < 5)
		{
			maxDamage = 5;
			cost -= 100;
		}
		int minStrength = Math.max(0, selectedWeapon.getMinimalStrength() + rand.randInt(4) - rand.randInt(3));
		if (minStrength > 12)
		{
			cost -= 100;
		}
		if (xrange > 0)
		{
			cost += (100 * xrange);
		}
		cost = Math.max((int) (selectedWeapon.getCost() * 0.5), cost);
		Weapon born = new Weapon(name.toString(), att, cost, weight, maxDamage, minStrength, xrange, skill);
		born.setRandom(true);

		return born;

	}

	/**
	 * Set mode for this weapon (ie: cutting, crushing, impalling). Use
	 * Weapon.MODE_IMP Weapon.MODE_CRU Weapon.MODE_CUT
	 * 
	 * @param mode
	 *            It can be
	 *            <ul>
	 *            <li>Weapon.MODE_IMP -> Impaling
	 *            <li>Weapon.MODE_CRU -> Crushing
	 *            <li>Weapon.MODE_CUT -> Cutting
	 *            </ul>
	 */
	public void setMode(DamageMode mode)
	{
		switch (mode)
		{
			case IMPALING:
			default:
				if (this.att.getImpalingDamage().isValid())
				{
					this.selectedMode = mode;
				}
				else
				{
					System.err.println("Invalid weapon mode for this weapon: " + mode);
				}
				break;
			case CRUSHING:
				if (this.att.getCrushingDamage().isValid())
				{
					this.selectedMode = mode;
				}
				else
				{
					System.err.println("Invalid weapon mode for this weapon: " + mode);
				}
				break;
			case CUTTING:
				if (this.att.getCuttingDamage().isValid())
				{
					this.selectedMode = mode;
				}
				else
				{
					System.err.println("Invalid weapon mode for this weapon: " + mode);
				}
				break;
		}
	}

	/**
	 * Get selected mode for this weapon as a string.
	 * 
	 * @return weapon mode
	 */
	public String getStringMode()
	{
		String wMode = null;
		switch (this.selectedMode)
		{
			case CUTTING:
				wMode = LibraryStrings.CUTTING; // "Cutting";
				break;
			case IMPALING:
				wMode = LibraryStrings.IMPALING; // "Impaling";
				break;
			case CRUSHING:
				wMode = LibraryStrings.CRUSHING; // "Crushing";
				break;
			default:
				wMode = "";
		}
		return wMode;
	}

	/**
	 * The weapon range
	 * 
	 * @return Weapon's range
	 */
	public int getRange()
	{
		return this.range;
	}

	/**
	 * Get Selected Mode for this weapon.
	 * 
	 * @return selected mode, ie:
	 *         <ul>
	 *         <li>cutting
	 *         <li>impaling
	 *         <li>crushing
	 *         </ul>
	 */
	public DamageMode getMode()
	{
		return this.selectedMode;
	}

	/**
	 * Get Minimal Strength to wear this weapon
	 * 
	 * @return PCharacter must have at least this strength (ST) to wear this
	 *         weapon
	 */
	public int getMinimalStrength()
	{
		return weaponMinStrength;
	}

	/**
	 * Return an array of String determining a long description for each
	 * available Attack
	 * 
	 * @return Strings describing available attacks
	 */
	public String[] getAttacks()
	{
		return att.getAttacks();
	}

	/**
	 * If getAttacks returns 3 different types of attacks, when you pass the
	 * mode for this weapon, ie: Weapon.MODE_IMP, this method returns the
	 * position of that mode in that array.
	 * 
	 * @param mode
	 *            The weapon mode as in Weapon (MODE_CRU, MODE_CUT, MODE_IMP)
	 * @return index of given weapon mode.
	 */
	public int getIndexAttacks(DamageMode mode)
	{
		return att.getIndexAttacks(mode);
	}

	/**
	 * Get Skill required to use this weapon
	 * 
	 * @return An Skill object
	 */
	public Skill getSkill()
	{
		return this.skill;
	}

	/**
	 * If this weapon is ranged
	 * 
	 * @return
	 */
	public boolean isRanged()
	{
		if (this.range > 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * If this weapon need to get ready (not balanced).
	 * 
	 * @return true, if this weapon is balanced.
	 */
	public boolean isBalanced()
	{
		return this.balanced;
	}

	/**
	 * If this weapon is balanced, then returns true if it's also ready.
	 * Otherwise it's always ready.
	 * 
	 * @return true, if this weapon is ready to use.
	 */
	public boolean isReady()
	{
		if (this.isBalanced())
		{
			return true;
		}

		return this.ready;
	}

	/**
	 * If current weapon is unbalanced, you may set its status here.
	 * 
	 * @param isReady
	 *            to set for this weapon
	 */
	public void setWeaponReady(boolean isReady)
	{
		if (this.isBalanced())
		{
			this.ready = true;
		}
		else
		{
			this.ready = isReady;
		}
	}

	/**
	 * Return Attack meta object
	 * 
	 * @return Attack meta object
	 */
	public WeaponAttack getAttackTypes()
	{
		return att;
	}

	/**
	 * Get a long description of weapon.
	 * 
	 * @param divisor
	 *            Divisor character
	 * @return long description
	 */
	public String getDescription(String divisor)
	{
		StringBuilder desc = new StringBuilder();
		// Name of weapon
		desc.append(this.name.toUpperCase());
		desc.append(" (" + LibraryStrings.WEAPON + ")");
		desc.append(divisor);
		// Cost, weight
		desc.append(this.getCommonDescription());
		desc.append(divisor);
		// Skill
		desc.append(LibraryStrings.SKILL);
		desc.append(": ");
		desc.append(this.skill.toString());
		desc.append(" (");
		desc.append(this.skill.getDefault());
		desc.append(")");
		desc.append(divisor);
		if (this.skill.isBalanced())
		{
			desc.append(LibraryStrings.BALANCED);
		}
		else
		{
			desc.append(LibraryStrings.UNBALANCED);
		}
		desc.append(divisor);
		// Kind of attacks
		desc.append(this.att);
		desc.append(divisor);
		// Range
		if (this.range > 0)
		{
			desc.append("Range: ");
			desc.append(this.range);
			desc.append(divisor);
		}
		// Minimum Strength
		if (this.weaponMinStrength > 0)
		{
			desc.append(LibraryStrings.MINSTRENG);
			desc.append(this.weaponMinStrength);
			desc.append(divisor);
		}
		// Max Damage
		if (this.weaponMaxDamage != 0)
		{
			desc.append(LibraryStrings.MAXDAMAGE);
			desc.append(this.weaponMaxDamage);
		}

		return desc.toString();

	}
	
	@Override
	public boolean canBeWorn(AbstractPC character)
	{
		int pcSt = character.getPrimaryStats().getST();
		if ( pcSt >= this.getMinimalStrength() )
		{
			return true;
		}
		return false;
	}

	/**
	 * Get long description of this weapon
	 * 
	 * @return detailed description of this weapon
	 */
	@Override
	public String getDescription()
	{
		return this.getDescription(OSProps.LINEFEED);
	}

	/**
	 * Polymorphic method to wear this AbstractItem
	 * 
	 * @param pc
	 *            PCharacter that is going to wear this AbstractItem
	 * @return True if oggetto can be worn by PCharacter
	 */
	@Override
	public boolean wear(PCharacter pc)
	{
		// Already with a weapon
		if (this.isRanged())
		{
			if (pc.wearsRangedWeapon())
			{
				warning = "you are already wearing a ranged weapon!";
				return false;
			}
		}
		else
		{
			if (pc.wearsWeapon())
			{
				warning = "you are already wearing a weapon!";
				return false;
			}
		}

		boolean retbol = pc.setCurrentWeapon(this);
		if (!retbol)
		{
			warning = "( " + LibraryStrings.REQST + ": " + String.valueOf(this.getMinimalStrength()) + ")";
		}

		return retbol;
	}

	/**
	 * Unwear AbstractItem (polymorphic)
	 * 
	 * @param pc
	 *            PCharacter that is going to unwear this AbstractItem
	 */
	@Override
	public void unwear(PCharacter pc)
	{
		if (this.isRanged())
		{
			pc.setCurrentRangedWeapon(null);
		}
		else
		{
			pc.setCurrentWeapon(null);
		}
		pc.getInventory().addBasicItem(this);
	}

	/**
	 * Returns type of damage based on weapon mode.
	 * 
	 * @return Either WeaponDamage.SWING or WeaponDamage.THRUST
	 * @see WeaponDamage
	 */
	public DamageType getDamageType()
	{
		DamageType dType = DamageType.INVALID;

		if (this.selectedMode == DamageMode.CRUSHING)
		{
			dType = att.getCrushingDamage().getType();
		}
		else if (this.selectedMode == DamageMode.CUTTING)
		{
			dType = att.getCuttingDamage().getType();
		}
		else if (this.selectedMode == DamageMode.IMPALING)
		{
			dType = att.getImpalingDamage().getType();
		}

		return dType;
	}

	/**
	 * Returns modifier of damage based on weapon mode.
	 * 
	 * @return modifier of damage, given weapon mode
	 */
	public int getDamageModifier()
	{
		int modifier = 0;

		if (this.selectedMode == DamageMode.CRUSHING)
		{
			modifier = att.getCrushingDamage().getModifier();
		}
		else if (this.selectedMode == DamageMode.CUTTING)
		{
			modifier = att.getCuttingDamage().getModifier();
		}
		else if (this.selectedMode == DamageMode.IMPALING)
		{
			modifier = att.getImpalingDamage().getModifier();
		}

		return modifier;
	}

	/**
	 * Get Basic Damage for weapon
	 * 
	 * @param characterStrength
	 *            Strength (ST) of character who wears this weapon
	 * @return basic damage (always > 0)
	 */
	public int getBasicDamage(int characterStrength)
	{
		int damage = 0;
		DamageType dType = this.getDamageType();
		int modifier = this.getDamageModifier();

		if (dType == DamageType.SWING)
		{
			damage = JurpeUtils.getSwingDamage(characterStrength);
		}
		else if (dType == DamageType.THRUST)
		{
			damage = JurpeUtils.getThrustDamage(characterStrength);
		}

		return Math.max(0, (damage + modifier));
	}

	/**
	 * Get Weapon's max damage. This is an intrisic weapon attribute. To get
	 * effective maximum damage for a weapon wore by a character, use
	 * getMaxDamage(int).
	 * 
	 * @return generic maximum weapon's damage
	 */
	public int getMaxDamage()
	{
		return this.weaponMaxDamage;
	}

	/**
	 * Get Maximum Damage for this weapon. If weapon has a maximum damage,
	 * return max between that and damage. Else return damage.
	 * 
	 * @param characterStrength
	 *            Strength (ST) of character who wears this weapon
	 * @return maximum damage (always >= basic damage)
	 */
	public int getMaxDamage(int characterStrength)
	{
		int maxD = this.weaponMaxDamage;
		int damage = 0;

		DamageType dType = this.getDamageType();
		if (dType == DamageType.SWING)
		{
			damage = JurpeUtils.getMaxSwingDamage(characterStrength);
		}
		else if (dType == DamageType.THRUST)
		{
			damage = JurpeUtils.getMaxThrustDamage(characterStrength);
		}
		return Math.max(maxD, damage);
	}

	/**
	 * Name of weapon
	 * 
	 * @return Name of weapon
	 */
	@Override
	public String toString()
	{
		return this.name;
	}

	/**
	 * See constructor.
	 */
	private void initialize(WeaponAttack atc, int maxDamage, int minStrength, int wrange, Skill skl)
	{
		this.att = atc;
		this.range = wrange;
		weaponMaxDamage = maxDamage;
		weaponMinStrength = minStrength;
		skill = skl;

		selectedMode = DamageMode.NOTHING;

		// Automatic mode, if weapon has just one
		if (this.att.getNumAttacks() == 1)
		{
			if (this.att.getCrushingDamage().isValid())
			{
				selectedMode = DamageMode.CRUSHING;
			}
			else if (this.att.getImpalingDamage().isValid())
			{
				selectedMode = DamageMode.IMPALING;
			}
			else if (this.att.getCuttingDamage().isValid())
			{
				selectedMode = DamageMode.CUTTING;
			}
		}
		else
		// select first available
		{
			if (this.att.getCrushingDamage().isValid())
			{
				selectedMode = DamageMode.CRUSHING;
			}
			if (this.att.getImpalingDamage().isValid())
			{
				selectedMode = DamageMode.IMPALING;
			}
			if (this.att.getCuttingDamage().isValid())
			{
				selectedMode = DamageMode.CUTTING;
			}
		}

		balanced = skill.isBalanced();
		if (!balanced)
		{
			ready = false;
		}
		else
		{
			ready = true;
		}

	}

	private static int randomDamageModifier(int weaponRarity)
	{
		int damageModifier;
		AxsRand rand = AxsRand.getReference();
		if (weaponRarity > 2)
		{
			damageModifier = rand.randInt(weaponRarity);
			// Adds up to (ItemValueType/10)-1 to cut damage
		}
		else
		{
			damageModifier = -(rand.randInt(weaponRarity));
		}

		return damageModifier;
	}

	/**
	 * Test random creation
	 * 
	 * @param args
	 * @deprecated
	 */
	public static void main(String[] args)
	{
		RandomNames rn = new RandomNames();
		Skills skls = new Skills();
		MasterShop ms = new MasterShop(skls);

		try
		{
			for (byte j = 10; j <= 70; j += 10)
			{
				System.out.println("NEW LEVEL");
				for (short k = 1; k < 5; k++)
				{
					System.out.println();
					System.out.println();
					Weapon s = Weapon.createRandom(ItemValueType.fromValue(j), rn, ms, skls);
					System.out.print(s);
					System.out.print(" - ");
					System.out.println(s.getDescription());
				}
				System.out.println();
				System.out.println("=====================");
				System.out.println();
			}
			System.out.println();
			System.out.println("=====================");
			System.out.println();
		}
		catch (JurpeException e)
		{
			e.printStackTrace();
		}

	}

}
