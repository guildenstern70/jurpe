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
 * Shield Type.
 * 
 * @see AbstractItem
 * 
 */
public class Shield extends AbstractItem
{

	private static final long serialVersionUID = 3317L;

	private int difesapassiva;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            Shields's name
	 * @param cost
	 *            Shields's cost
	 * @param weight
	 *            Shields's weight
	 * @param dp
	 *            Shields's Passive Defense
	 */
	public Shield(String name, int dp, int cost, int weight)
	{
		super(ItemType.SHIELD, name, cost, weight);
		difesapassiva = dp;
	}

	/**
	 * Creates a random generated Shield
	 * 
	 * @param value
	 *            ItemValueType indicating rarity of Item
	 * @param rn
	 *            Handle to Random Names
	 * @return newly created Shield
	 */
	public static Shield createRandom(ItemValueType value, RandomNames rn)
	{
		StringBuilder sb = new StringBuilder(rn.getAttribute(value));
		sb.append(" ");
		sb.append(rn.getShield(value));
		sb.append(" ");
		sb.append(rn.getGeography(value));
		String name = sb.toString();
		AxsRand rnd = AxsRand.getReference();
		int passivedefense = rnd.randInt(6) + 1;
		int weight = rnd.randInt(10) + passivedefense + 7;
		CostGenerator cg = new CostGenerator(50, passivedefense * 5, weight);
		int cost = cg.getCost();

		Shield born = new Shield(name, passivedefense, cost, weight);
		born.setRandom(true);

		return born;
	}

	/**
	 * Set cost
	 * 
	 * @param sCosto
	 *            Shield's cost
	 */
	public void setCost(int sCosto)
	{
		this.cost = sCosto;
	}

	/**
	 * Set weight
	 * 
	 * @param sPeso
	 *            Shield's weight
	 */
	public void setWeight(int sPeso)
	{
		this.weight = sPeso;
	}

	/**
	 * Get Passive Defense
	 * 
	 * @return passive defense of shield
	 */
	public int getPassiveDefense()
	{
		return difesapassiva;
	}

	/**
	 * Get Long Description
	 * 
	 * @return long description
	 */
	@Override
	public String getDescription()
	{

		StringBuilder desc = new StringBuilder();
		desc.append(this.name.toUpperCase());
		desc.append(" (" + LibraryStrings.SHIELD + ")");
		desc.append(OSProps.LINEFEED);
		desc.append(this.getCommonDescription());
		desc.append(LibraryStrings.PD + ": ");
		desc.append(this.difesapassiva);
		desc.append(OSProps.LINEFEED);

		return desc.toString();
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
		if (pc.isShielded())
		{
			this.warning = "you are already carrying a shield.";
			return false;
		}

		return pc.setCurrentShield(this);
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
		pc.setCurrentShield(null);
		pc.getInventory().addBasicItem(this);
	}

	/**
	 * Test random creation
	 * 
	 * @param args
	 *            arguments
	 * @deprecated
	 */
	public static void main(String[] args)
	{
		RandomNames rn = new RandomNames();
		for (byte j = 10; j <= 70; j += 10)
		{
			for (short k = 1; k < 5; k++)
			{
				Shield s = Shield.createRandom(ItemValueType.fromValue(j), rn);
				System.out.print(s);
				System.out.print(" - ");
				System.out.println(s.getDescription());
			}
		}
	}

}