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

import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.system.OSProps;
import net.littlelite.jurpe.system.generation.CostGenerator;
import net.littlelite.jurpe.system.generation.ItemValueType;
import net.littlelite.jurpe.system.generation.RandomNames;
import net.littlelite.jurpe.system.resources.LibraryStrings;
import net.littlelite.utils.AxsRand;

/**
 * Armor Type.
 * 
 * @see AbstractItem
 * 
 */
public class Armor extends AbstractItem
{

	private static final long serialVersionUID = 3317L;

	private int dp; // passive defense

	private int rd; // damage resistance

	/**
	 * Constructor
	 * 
	 * @param name
	 *            Armor's name
	 * @param cost
	 *            Armor's cost
	 * @param weight
	 *            Armor's weight
	 * @param aDp
	 *            Armor's Passive Defense
	 * @param aRd
	 *            Armor's Damage Resistance
	 */
	public Armor(String name, int cost, int weight, int aDp, int aRd)
	{
		super(ItemType.ARMOR, name, cost, weight);
		dp = aDp;
		rd = aRd;
	}

	/**
	 * Creates a random generated Armor
	 * 
	 * @param value
	 *            ItemValueType indicating rarity of Item
	 * @param rn
	 *            Handle to Random Names
	 * @return newly created Shield
	 */
	public static Armor createRandom(ItemValueType value, RandomNames rn)
	{
		StringBuilder sb = new StringBuilder(rn.getAttribute(value));
		sb.append(" ");
		sb.append(rn.getArmor(value));
		sb.append(" ");
		sb.append(rn.getGeography(value));
		String name = sb.toString();
		AxsRand rnd = AxsRand.getReference();
		int passivedefense = rnd.randInt((value.getType() + 10) / 10);
		// usually between 0 and 5
		int damageResistance = rnd.randInt((value.getType() + 10) / 10);
		// usually between 0 and 5
		int weight = rnd.randInt(10) + passivedefense * 10 + damageResistance * 5;
		CostGenerator cg = new CostGenerator(150, (passivedefense * 5 + damageResistance * 5), weight);
		int cost = cg.getCost();

		Armor born = new Armor(name, cost, weight, passivedefense, damageResistance);
		born.setRandom(true);

		return born;
	}

	/**
	 * Long description
	 * 
	 * @return Long description
	 */
	@Override
	public String getDescription()
	{
		StringBuilder desc = new StringBuilder();
		desc.append(this.name.toUpperCase());
		desc.append(" (" + LibraryStrings.ARMOR + ")");
		desc.append(OSProps.LINEFEED);
		desc.append(this.getCommonDescription());
		desc.append(LibraryStrings.PD + ": ");
		desc.append(this.dp);
		desc.append(OSProps.LINEFEED);
		desc.append(LibraryStrings.RD + ": ");
		desc.append(this.rd);
		desc.append(OSProps.LINEFEED);

		return desc.toString();
	}

	/**
	 * Polymorphic call to wear this armor (AbstractItem)
	 * 
	 * @param pc
	 *            PCharacter who's going to wear this Armor
	 * @return True, if Armor is correctly worn
	 */
	@Override
	public boolean wear(PCharacter pc)
	{
		if (pc.getCurrentArmor() != null)
		{
			this.warning = "you are already wearing an armor.";
			return false;
		}
		return pc.setCurrentArmor(this);
	}

	/**
	 * Polymorphic call to unwear this armor
	 * 
	 * @param pc
	 *            PCharacter who's going to unwear this armor
	 */
	@Override
	public void unwear(PCharacter pc)
	{
		pc.setCurrentArmor(null);
		pc.getInventory().addBasicItem(this);
	}

	/**
	 * Get Passive Defense
	 * 
	 * @return Passive Defense of this Armor
	 */
	public int getPD()
	{
		return this.dp;
	}

	/**
	 * Get Damage Resistance
	 * 
	 * @return Damage Resistance of this Armor
	 */
	public int getDR()
	{
		return this.rd;
	}

}