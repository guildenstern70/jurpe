package net.littlelite.jurpe.items;

import net.littlelite.jurpe.system.generation.Dice;
import net.littlelite.jurpe.system.generation.ItemValueType;

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

/**
 * MoneyBag is the representation of an item containing money.
 * 
 */
public class MoneyBag extends AbstractItem
{

	private static final long serialVersionUID = 3317L;

	private int amount;

	/**
	 * Constructor
	 * 
	 * @param money
	 *            How many money this bag contains
	 */
	public MoneyBag(int money)
	{
		super(ItemType.MONEY, "bag with " + String.valueOf(money) + " coins", 0, (int) (money * .5));
		this.amount = money;
		this.isWearable = false;
	}

	/**
	 * Get the amount of money
	 * 
	 * @return The amount of money
	 */
	public int getAmount()
	{
		return this.amount;
	}

	/**
	 * Get the description
	 */
	@Override
	public String getDescription()
	{
		return "A bag with " + String.valueOf(this.amount) + " coins";
	}

	/**
	 * Add money to this money bag
	 * 
	 * @param amountOfMoney
	 *            Amount of money to add
	 */
	public void addMoney(int amountOfMoney)
	{
		this.amount += amountOfMoney;
	}

	/**
	 * Remove some money from this money bag
	 * 
	 * @param amountOfMoney
	 *            Money to be removed
	 */
	public void removeMoney(int amountOfMoney)
	{
		this.amount -= amountOfMoney;
	}

	/**
	 * Create a new money bag
	 * 
	 * @param ivt
	 *            The value (rare, common...) of this item
	 * @return A new money bag
	 */
	public static MoneyBag createRandom(ItemValueType ivt)
	{
		MoneyBag money = null;
		int amount = 0;
		Dice d = new Dice();

		switch (ivt)
		{
			case TRASH:
				amount = d.throwDice(2);
				break;
			case COMMON:
				amount = d.throwDice(4);
				break;
			case NORMAL:
				amount = d.throwDice(6);
				break;
			case AVERAGE:
				amount = d.throwDice(10);
				break;
			case MORETHANAVG:
				amount = d.throwDice(25);
				break;
			case RARE:
				amount = d.throwDice(50);
				break;
			case UNIQUE:
				amount = d.throwDice(200);
				break;
		}

		if (amount > 0)
		{
			money = new MoneyBag(amount);
		}

		return money;
	}

}
