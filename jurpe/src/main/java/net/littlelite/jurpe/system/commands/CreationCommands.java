package net.littlelite.jurpe.system.commands;

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
import net.littlelite.jurpe.characters.Monster;
import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.containers.Skills;
import net.littlelite.jurpe.items.Armor;
import net.littlelite.jurpe.items.AbstractItem;
import net.littlelite.jurpe.items.Item;
import net.littlelite.jurpe.items.MoneyBag;
import net.littlelite.jurpe.items.Shield;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.JurpeUtils;
import net.littlelite.jurpe.system.generation.Dice;
import net.littlelite.jurpe.system.generation.ItemValueType;
import net.littlelite.jurpe.system.generation.RandomNames;

/**
 * Creation related commands.
 * 
 * @see net.littlelite.jurpe.system.CoreCommands
 */
public class CreationCommands extends GenericCommands
{

	/**
	 * Creation of a new Playing Character
	 * 
	 * @param ca
	 *            Character Attributes
	 * @param availableSkills
	 *            Available Skills
	 * @return newly created Playing Character
	 * @see Skills
	 */
	public static PCharacter generatePC(CharacterAttributes ca, Skills availableSkills)
	{
		PCharacter pc = JurpeUtils.generatePC(ca, availableSkills);
		log.addDetail("New PC generated: " + pc);
		return pc;
	}

	/**
	 * Creates a random monster, with a random name.
	 * 
	 * @param value
	 *            total starting points assigned to the new character
	 * @param names
	 *            random names available
	 * @return newly generated monster
	 * @see RandomNames
	 */
	public static Monster generateMonster(int value, RandomNames names)
	{
		String monsterName = names.getCompleteMonsterName(value);
		Monster monstr = Monster.createRandom(monsterName, value);
		log.addDetail("New Monster generated: " + monstr);
		return monstr;
	}

	/**
	 * Creates a random generated item (money or other items). This can be
	 * useful to fill a treasure chest or to generate lootables in the dungeon.
	 * The creation procedure is the following: a die is thrown, and that
	 * determines if the object is a Bag of Money, a Shield, an Item, an Armor.
	 * Then the object is created and returnd
	 * 
	 * @param value
	 *            Value of the item to create
	 * @return A newly created item
	 */
	public static AbstractItem generateItem(ItemValueType value, RandomNames names) throws JurpeException
	{
		AbstractItem ivt = null;
		int destiny = Dice.roll() + Dice.roll();

		switch (destiny)
		{
			case 2:
				ivt = MoneyBag.createRandom(value);
				break;
			case 3:
				ivt = Item.createRandom(value, names);
				break;
			case 4:
				ivt = MoneyBag.createRandom(value);
				break;
			case 5:
				ivt = Armor.createRandom(value, names);
				break;
			case 6:
				ivt = Shield.createRandom(value, names);
				break;
			case 7:
				ivt = MoneyBag.createRandom(value);
				break;
			case 8:
				ivt = Item.createRandom(value, names);
				break;
			case 9:
				ivt = Item.createRandom(value, names);
				break;
			case 10:
				ivt = MoneyBag.createRandom(value);
				break;
			case 11:
				ivt = MoneyBag.createRandom(value);
				break;
			case 12:
				ivt = MoneyBag.createRandom(value);
				MoneyBag mb = (MoneyBag) ivt;
				mb.addMoney(400);
				ivt = mb;
				break;
		}

		return ivt;
	}

}