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

import java.io.Serializable;

import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.system.OSProps;
import net.littlelite.jurpe.system.resources.LibraryStrings;

/**
 * Base class for every PHYSICAL OBJECT of the game, ie: weapons, shields,
 * armors, rings and so on.
 * 
 * @author Alessio Saltarin
 */
public abstract class BasicItem implements Cloneable, Serializable, Comparable<BasicItem>
{

	/**
	 * Type of item: weapon, shield, item, armor, other...
	 */
	protected ItemType type;

	/**
	 * Is usable
	 */
	protected boolean isUsable;

	/**
	 * Is wearable
	 */
	protected boolean isWearable;

	/**
	 * Item was randomly generated
	 */
	protected boolean isRandom;

	/**
	 * Cost
	 */
	protected int cost;

	/**
	 * Weight
	 */
	protected int weight;

	/**
	 * Name
	 */
	protected String name;

	/**
	 * String of text
	 */
	protected String warning;

	/**
	 * Constructor for wearable/not usable objects
	 * 
	 * @param itemType
	 *            Describes item type. Use BasicItem.WEAPON, BasicItem.ARMOR,
	 *            BasicItem.SHIELD, BasicItem.OTHER or BasicItem.ITEM
	 * @param oNome
	 *            Name of object
	 * @param oCosto
	 *            Cost of object
	 * @param oPeso
	 *            Weight of object
	 */
	public BasicItem(ItemType itemType, String oNome, int oCosto, int oPeso)
	{
		type = itemType;
		name = oNome;
		cost = oCosto;
		weight = oPeso;
		isUsable = false;
		isRandom = false;
		isWearable = true;
	}

	/**
	 * Generic Constructor
	 * 
	 * @param itemType
	 *            Describes item type. Use BasicItem.WEAPON, BasicItem.ARMOR,
	 *            BasicItem.SHIELD, BasicItem.OTHER or BasicItem.ITEM
	 * @param oNome
	 *            Name of object
	 * @param oCosto
	 *            Cost of object
	 * @param oPeso
	 *            Weight of object
	 * @param usable
	 *            If this object is usable by a character (ie: potion)
	 * @param wearable
	 *            If this object can be worn by a character (ie: ring)
	 */
	public BasicItem(ItemType itemType, String oNome, int oCosto, int oPeso, boolean usable, boolean wearable)
	{
		this(itemType, oNome, oCosto, oPeso);
		isUsable = usable;
		isWearable = wearable;
	}

	/**
	 * Returns cost of object
	 * 
	 * @return The cost of object (in abstract units)
	 */
	public int getCost()
	{
		return this.cost;
	}

	/**
	 * Returns type of item, such as: BasicItem.WEAPON, BasicItem.ARMOR,
	 * BasicItem.SHIELD, BasicItem.OTHER or BasicItem.ITEM.
	 * 
	 * @return type of item.
	 */
	public ItemType getType()
	{
		return this.type;
	}

	/**
	 * If this item is usable (character can use it)
	 * 
	 * @return true, if this object is usable
	 */
	public boolean isItemUsable()
	{
		return this.isUsable;
	}

	/**
	 * If this item is wearable (character can wear it)
	 * 
	 * @return true, if this object is wearable
	 */
	public boolean isItemWearable()
	{
		return this.isWearable;
	}

	/**
	 * If this item was randomly created
	 * 
	 * @return true, if this item was not read from an xml file, but rather
	 *         randomly created.
	 */
	public boolean isItemRandom()
	{
		return this.isRandom;
	}

	/**
	 * Returns weight of object
	 * 
	 * @return Object's weight.
	 */
	public int getWeight()
	{
		return weight;
	}

	/**
	 * Get the name of the object
	 * 
	 * @return Object's name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Every time an BasicItem needs to send a message back, this field can be
	 * used.
	 * 
	 * @return A message string
	 */
	public String warningMessage()
	{
		return warning;
	}

	/**
	 * Returns cost and weight of an BasicItem. (Every object MUST have a cost
	 * and weight associated).
	 * 
	 * @return String containing cost and weight, separated by \n
	 */
	public String getCommonDescription()
	{
		StringBuilder desc = new StringBuilder();

		desc.append(LibraryStrings.COST + ": $");
		desc.append(this.cost);
		desc.append(OSProps.LINEFEED + LibraryStrings.WEIGHT + ": ");
		desc.append(this.weight);
		desc.append(" kg");
		desc.append(OSProps.LINEFEED);

		return desc.toString();
	}

	/**
	 * Every object is able to describe himself
	 * 
	 * @return String containing object name, cost, weight and other properties.
	 */
	public String getDescription()
	{
		return this.getCommonDescription();
	}

	/**
	 * Every object is able to wear itself in the correct position of a
	 * character (ie: arms in right hand). Returns false if object is not
	 * wearable
	 * 
	 * @param pc
	 *            PCharacter who is wearing this object
	 * @return true if character correctly wore BasicItem
	 */
	public boolean wear(@SuppressWarnings("unused")
	PCharacter pc)
	{
		return false;
	}

	/**
	 * Every object is able to unwear itself
	 * 
	 * @param pc
	 *            PCharacter who is unwearing this object
	 */
	public void unwear(@SuppressWarnings("unused")
	PCharacter pc)
	{
		return;
	}

	/**
	 * Use this object onto given character
	 * 
	 * @param pc
	 *            PCharacter who is using this object
	 * @return true, if object was correctly used. If false, call
	 *         this.warningMessage to discover why.
	 */
	public boolean use(@SuppressWarnings("unused")
	PCharacter pc)
	{
		warning = this.name + LibraryStrings.CANTBSD;
		return false;
	}

	/**
	 * Object's name
	 * 
	 * @return Object's name
	 */
	public String toString()
	{
		return this.name;
	}

	/**
	 * Object's name and cost
	 * 
	 * @return Name and cost of the object
	 */
	public String toStringWCost()
	{
		StringBuilder sb = new StringBuilder(this.name);
		sb.append(" ($");
		sb.append(this.cost);
		sb.append(")");
		return sb.toString();
	}

	/**
	 * Clone BasicItem.
	 * 
	 * @return Cloned BasicItem
	 */
	public Object clone() // adds clonability to all classes derived from
	// BasicItem
	{
		Object o = null;

		try
		{
			o = super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			System.err.println("Can't clone.");
		}

		return o;
	}

	/**
	 * Adds comparability to all derived classes Comparing is always made on
	 * cost
	 * 
	 * @param rv
	 *            Compared BasicItem
	 * @return 0 if objects are equals (they have the same cost). -1 if this
	 *         object has minor cost that rv 1 if this object costs more than rv
	 */
	public int compareTo(BasicItem rv)
	{
		int rvCosto = rv.cost;
		return (this.cost < rvCosto) ? (-1) : ((this.cost == rvCosto) ? (0) : (1));
	}

	/**
	 * Set if this item was randomly created
	 * 
	 * @param rnd
	 *            true if this item has been generated at random
	 */
	public void setRandom(boolean rnd)
	{
		this.isRandom = rnd;
	}

}
