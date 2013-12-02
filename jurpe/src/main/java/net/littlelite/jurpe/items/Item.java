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
import net.littlelite.jurpe.characters.PrimaryStats;
import net.littlelite.jurpe.system.OSProps;
import net.littlelite.jurpe.system.generation.CostGenerator;
import net.littlelite.jurpe.system.generation.Dice;
import net.littlelite.jurpe.system.generation.ItemValueType;
import net.littlelite.jurpe.system.generation.RandomNames;
import net.littlelite.jurpe.system.resources.LibraryStrings;

/**
 * Item Type Class. An item may be a potion, a necklace, a ring and such...
 * 
 * 
 * @see AbstractItem
 */
public class Item extends AbstractItem
{

	private static final long serialVersionUID = 3319L;

	protected String brfDescription;
	protected int modifier;
	protected int viewRadius;
	protected String pcAttribute;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            Item's name
	 * @param cost
	 *            Item's cost
	 * @param weight
	 *            Item's weight
	 * @param description
	 *            Item's description
	 * @param mod
	 *            int number that applies to Attribute when item is weared or
	 *            used
	 * @param attribute
	 *            describes what attribute is to be modified with this item
	 *            (currently HT,DX,IQ,ST)
         * @param view
         *            the view radius (in hexagons) of the light, if this is a source of light
	 * @param wearbool
	 *            true if this item is wearable
	 */
	public Item(String name, int cost, int weight, String description, int mod, String attribute, int view, boolean wearbool)
	{
		super(ItemType.ITEM, name, cost, weight);

		this.brfDescription = description;
		this.modifier = mod;
		this.viewRadius = view;
		if (attribute != null)
		{
			this.pcAttribute = attribute.toUpperCase();
		}
		this.isWearable = wearbool;
		if (!isWearable)
		{
			this.isUsable = true;
		}
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
		desc.append(" (" + LibraryStrings.ITEM + ")");
		desc.append(OSProps.LINEFEED);
		desc.append(this.getCommonDescription());
		desc.append(OSProps.LINEFEED);
		if ((this.pcAttribute != null) && (this.modifier != 0))
		{
			String plusminus;
			if (this.modifier > 0)
			{
				plusminus = "+";
			}
			else
			{
				plusminus = "-";
			}
			desc.append(LibraryStrings.MODIFYW + " " + this.pcAttribute + plusminus + String.valueOf(this.modifier));
			desc.append(OSProps.LINEFEED);
		}
		desc.append(this.brfDescription);
		desc.append(OSProps.LINEFEED);

		return desc.toString();
	}

	/**
	 * Polymorphic call to wear this item
	 * 
	 * @param pc
	 *            PCharacter who's going to wear this Item
	 * @return True, if Item is correctly wore
	 */
	@Override
	public boolean wear(PCharacter pc)
	{
		boolean ok = false;

		if (this.isWearable)
		{
			ok = pc.setCurrentItem(this);
			this.applyModifierToPC(pc);
			return ok;
		}

		return false;
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
		if (this.isWearable)
		{
			this.removeModifierToPC(pc);
			pc.setCurrentItem(null);
			pc.getInventory().addBasicItem(this);
		}
	}

	/**
	 * Use AbstractItem (polymorphic)
	 * 
	 * @param pc
	 *            PCharacter that is going to use this AbstractItem
	 * @return true if oggetto was correctly used.
	 */
	@Override
	public boolean use(PCharacter pc)
	{
		if (this.isUsable)
		{
			this.applyModifierToPC(pc);
			pc.getInventory().removeBasicItem(this);
			return true;
		}

		return false;
	}

	/**
	 * Creates a new random item.
	 * 
	 * @param value
	 *            ItemValueType indicating rarity of Item
	 * @param rn
	 *            Handle to current RandomNames object
	 * @return newly created Item
	 * @see ItemValueType
	 */
	public static Item createRandom(ItemValueType value, RandomNames rn)
	{
		String newName = null;
		String newDescription = null;
		String newAttr = null;
		int newCost = 0;
		int newWeight = 0;
		int newMod = 0;
		int basicCost = 0;
		boolean newWearable = false;

		Dice d = new Dice();

		// Choose Item type (totally random)
		short type = (short) d.throwDice();
		String itemType = "Nothing";

		switch (type)
		{
			case 1:
				itemType = LibraryStrings.POTION;
				newWeight = 1;
				basicCost = 10;
				newWearable = false;
				break;

			case 2:
				itemType = LibraryStrings.RING;
				newWeight = 1;
				basicCost = 30;
				newWearable = true;
				break;

			case 3:
				itemType = LibraryStrings.LACE;
				newWeight = 2;
				basicCost = 20;
				newWearable = true;
				break;

			case 4:
				itemType = LibraryStrings.BELT;
				newWeight = 3;
				basicCost = 30;
				newWearable = true;
				break;

			case 5:
				itemType = LibraryStrings.PHIAL;
				newWeight = 1;
				basicCost = 5;
				newWearable = false;
				break;

			case 6:
			default:
				itemType = LibraryStrings.AMULET;
				newWeight = 8;
				basicCost = 30;
				newWearable = true;
				break;
		}

		// Choose Item Name
		newName = rn.getAttribute(value) + " " + itemType;
		newDescription = itemType;

		// Choose Random Modifier. Modifier can be "HT","DX","IQ", "ST" plus a
		// value (between 1 and 6 for HT, between 1 and 3 for other stats)
		// Modifiers on HT are pretty cheap and common.
		short rndMod = (short) d.throwDice();

		// Cost generator
		CostGenerator gc = new CostGenerator(basicCost, 0, 1);

		switch (rndMod)
		{
			case 1:
				newAttr = LibraryStrings.COS;
				newMod = d.throwDice();
				gc.setUpperModifier(newMod - 1);
				newName += " " + LibraryStrings.OZETRZEL;
				newDescription += LibraryStrings.THATRETRS + String.valueOf(newMod) + LibraryStrings.HTPOINTS;
				break;

			case 2:
				newAttr = LibraryStrings.COS;
				newMod = d.throwDice();
				gc.setUpperModifier(newMod - 1);
				newName += " " + LibraryStrings.OFWEALT;
				newDescription += LibraryStrings.THATRETRS + String.valueOf(newMod) + LibraryStrings.HTPOINTS;
				break;

			case 3:
			default:
				newAttr = LibraryStrings.COS;
				newMod = d.throwDice() + (value.getType() / 10);
				newName += " " + LibraryStrings.OFTHESUN;
				newDescription += LibraryStrings.THATRETRS + String.valueOf(newMod) + LibraryStrings.HTPOINTS;
				gc.setUpperModifier(newMod);
				break;

			case 4:
				newAttr = LibraryStrings.INT;
				newMod = Math.max(1, d.throwDice() / 2 + (value.getType() / 10) - 1);
				gc.setUpperModifier(newMod + 10);
				newName += " " + LibraryStrings.OFINTEL;
				newDescription += LibraryStrings.THATINCRZ + " " + LibraryStrings.INT + " " + LibraryStrings.STATOFSPC + String.valueOf(newMod)
						+ LibraryStrings.SPPOINTSZ;
				break;

			case 5:
				newAttr = LibraryStrings.DEX;
				newMod = Math.max(1, d.throwDice() / 2 + (value.getType() / 10) - 1);
				gc.setUpperModifier(newMod + 10);
				newName += " " + LibraryStrings.OFDEXTE;
				newDescription += LibraryStrings.THATINCRZ + " " + LibraryStrings.DEX + " " + LibraryStrings.STATOFSPC + String.valueOf(newMod)
						+ LibraryStrings.SPPOINTSZ;
				break;

			case 6:
				newAttr = LibraryStrings.FOR;
				newMod = Math.max(1, d.throwDice() / 2 + (value.getType() / 10) - 1);
				newName += " " + LibraryStrings.OFSTRENY;
				gc.setUpperModifier(newMod + 10);
				newDescription += LibraryStrings.THATINCRZ + " " + LibraryStrings.FOR + " " + LibraryStrings.STATOFSPC + String.valueOf(newMod)
						+ LibraryStrings.SPPOINTSZ;
				break;
		}

		// Item Cost
		newCost = gc.getCost();

		// New Item
		Item born = new Item(newName, newCost, newWeight, newDescription, newMod, newAttr, -1, newWearable);
		born.setRandom(true);

		return born;

	}

	/**
	 * Apply points to attribute to modify with this item.
	 * 
	 * @param pc
	 *            PC
	 */
	private void applyModifierToPC(PCharacter pc)
	{

		if (this.pcAttribute == null)
		{
			return;
		}

		PrimaryStats ps = pc.getPrimaryStats();

		if (this.pcAttribute.equals(LibraryStrings.COS))
		{
			pc.addToCurrentHP(this.modifier);
		}
		else if (this.pcAttribute.equals(LibraryStrings.DEX))
		{
			ps.setDX(ps.getDX() + this.modifier);
		}
		else if (this.pcAttribute.equals(LibraryStrings.FOR))
		{
			ps.setST(ps.getST() + this.modifier);
		}
		else if (this.pcAttribute.equals(LibraryStrings.INT))
		{
			ps.setIQ(ps.getIQ() + this.modifier);
		}
	}

	/**
	 * Remove points to attribute to modify with this item.
	 * 
	 * @param pc
	 *            PC
	 */
	private void removeModifierToPC(PCharacter pc)
	{
		if (this.pcAttribute == null)
		{
			return;
		}

		PrimaryStats ps = pc.getPrimaryStats();

		if (this.pcAttribute.equals(LibraryStrings.COS))
		{
			pc.addToCurrentHP(-this.modifier);
		}
		else if (this.pcAttribute.equals(LibraryStrings.DEX))
		{
			ps.setDX(ps.getDX() - this.modifier);
		}
		else if (this.pcAttribute.equals(LibraryStrings.FOR))
		{
			ps.setST(ps.getST() - this.modifier);
		}
		else if (this.pcAttribute.equals(LibraryStrings.INT))
		{
			ps.setIQ(ps.getIQ() - this.modifier);
		}
	}

	/**
	 * Test
	 * 
	 * @deprecated
	 * @param args
	 *            Arguments
	 */
	public static void main(String[] args)
	{
		RandomNames rn = new RandomNames();
		for (int k = 0; k <= 12; k++)
		{
			for (byte j = 10; j <= 70; j += 10)
			{
				Item tmp = Item.createRandom(ItemValueType.fromValue(j), rn);
				System.out.print(tmp.brfDescription);
				System.out.print("-- $");
				System.out.println(tmp.getCost());
			}
		}
	}

}
