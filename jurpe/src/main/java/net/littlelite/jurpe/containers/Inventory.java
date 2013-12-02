package net.littlelite.jurpe.containers;

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
import java.util.AbstractList;
import java.util.ArrayList;

import net.littlelite.jurpe.items.AbstractItem;
import net.littlelite.jurpe.items.ItemType;
import net.littlelite.jurpe.items.MoneyBag;
import net.littlelite.jurpe.system.Config;

/**
 * PCharacter Inventory. Contains an ArrayList of BasicItems
 * 
 * 
 */
public class Inventory implements Serializable
{

	private static final long serialVersionUID = 3317L;

	/**
	 * List of Oggetti currently in Inventory
	 */
	private AbstractList<AbstractItem> inventoryItems;

	/**
	 * Money available
	 */
	private int money;

	/**
	 * Constructs a new Inventory with default money quantity.
	 */
	public Inventory()
	{
		money = Config.INITIAL_MONEY;
		this.inventoryItems = new ArrayList<AbstractItem>();
	}
        
        /**
         * The number of items in this inventory
         * @return number of items in this inventory
         */
        public int count()
        {
            return this.inventoryItems.size();
        }

	/**
	 * Constructs new Inventory with "denary" money
	 * 
	 * @param denari
	 *            Money
	 */
	public Inventory(int denari)
	{
		money = denari;
	}

	/**
	 * Get current money available
	 * 
	 * @return Money currently available
	 */
	public int getAvailableMoney()
	{
		return money;
	}

	/**
	 * Get ArrayList containing AbstractItem available to character
	 * 
	 * @return ArrayList of AbstractItem
	 */
	public AbstractList<AbstractItem> getInventoryItems()
	{
		if (inventoryItems != null)
		{
			return inventoryItems;
		}

		return new ArrayList<AbstractItem>();
	}

	/**
	 * Add AbstractItem x to current Inventory
	 * 
	 * @param x
	 *            AbstractItem to add to Inventory
	 */
	public void addBasicItem(AbstractItem x)
	{
		if (x.getType() == ItemType.MONEY)
		{
			MoneyBag mb = (MoneyBag) x;
			this.earnMoney(mb.getAmount());
		}
		else
		{
			this.inventoryItems.add(x);
		}
	}

	/**
	 * Remove AbstractItem x from Inventory
	 * 
	 * @param x
	 *            AbstractItem to remove
	 * @see AbstractItem
	 */
	public void removeBasicItem(AbstractItem x)
	{
		this.inventoryItems.remove(x);
	}

	/**
	 * Remove AbstractItem x from Inventory
	 * 
	 * @param x
	 *            AbstractItem to remove
	 * @param price
	 *            Earn money amount when selling item
	 * @see AbstractItem
	 */
	public void sellBasicItem(AbstractItem x, int price)
	{
		this.earnMoney(price);
		this.inventoryItems.remove(x);
	}

	/**
	 * Remove "somma" money from current money
	 * 
	 * @param somma
	 *            Money to be spent
	 */
	public void spendMoney(int somma)
	{
		this.money -= somma;
	}

	/**
	 * Add "somma" money to current money
	 * 
	 * @param somma
	 */
	public void earnMoney(int somma)
	{
		this.money += somma;
	}

	/**
	 * Add "somma" money to current money
	 * 
	 * @param somma
	 */
	public void earnMoney(long somma)
	{
		this.money += somma;
	}

	/**
	 * Get current character encumbrance
	 * 
	 * @return Encumbrance
	 */
	public int getEncumbrance()
	{
		int peso = 0;

		for (AbstractItem bi : this.inventoryItems)
		{
			peso += bi.getWeight();
		}

		return peso;
	}

}
