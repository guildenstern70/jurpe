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
import java.util.Collections;

import net.littlelite.jurpe.characters.Skill;
import net.littlelite.jurpe.items.Armor;
import net.littlelite.jurpe.items.AbstractItem;
import net.littlelite.jurpe.items.Item;
import net.littlelite.jurpe.items.Shield;
import net.littlelite.jurpe.items.Weapon;
import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.generation.ItemValueType;
import net.littlelite.jurpe.system.generation.RandomNames;
import net.littlelite.utils.metadata.XMLLeaf;

/**
 * Shop type contains every BasicItems and derived available in the game-world.
 * It is an AbstractXMLCollection. A shop contains four ArrayLists of: <br>
 * <ul>
 * <li>Shield (shields)
 * <li>Armor (armors)
 * <li>Weapon (weapons)
 * <li>Item (such as potions)
 * </ul>
 * 
 * 
 * @see AbstractXMLCollection
 * @see AbstractItem
 */
public class MasterShop extends AbstractXMLCollection implements Serializable
{

	private static final long serialVersionUID = 3319L;

	/**
	 * Basic Items: may be Armors, Weapons, Items, Shields...
	 */
	protected AbstractList<AbstractItem> basicItems;

	/**
	 * Available skills for this game.
	 */
	private Skills avSkills;

	/**
	 * Constructor
	 * 
	 * @param skills
	 *            Available Skills For this game.
	 * @see Skills
	 */
	public MasterShop(Skills skills)
	{
		basicItems = new ArrayList<AbstractItem>();
		avSkills = skills;
		this.init(Config.SHOPXML);
	}

	/**
	 * Add Shield object to Shop collection
	 * 
	 * @param sh
	 *            Shield to add to the Shop
	 */
	public void addNewItem(AbstractItem sh)
	{
		this.basicItems.add(sh);
	}

	/**
	 * Add some random items to the ones available
	 * 
	 * @param names
	 *            Handle to dictionary.xml
	 * @param ivt
	 *            Value for random items to create
	 * @param number
	 *            Number to items to randomly create.
	 * @throws JurpeException
	 */
	public void addRandomItems(RandomNames names, ItemValueType ivt, int number) throws JurpeException
	{

		for (int j = 0; j < number; j++)
		{
			this.addNewItem(Item.createRandom(ivt, names));
			this.addNewItem(Shield.createRandom(ivt, names));
			this.addNewItem(Armor.createRandom(ivt, names));
			this.addNewItem(Weapon.createRandom(ivt, names, this, this.avSkills));
		}

	}

	/**
	 * Changes the order of the items. Useful when there are a lot of random
	 * items.
	 */
	public void shuffleItems()
	{
		MasterShop.shuffle(this.basicItems);
	}

	/**
	 * Get list of items (BasicItems) in this shop.
	 * 
	 * @return ArrayList of AbstractItem in the shop
	 * @see AbstractItem
	 */
	public AbstractList<AbstractItem> getItems()
	{
		return this.basicItems;
	}

	/**
	 * Get a list of the first n items (BasicItems) in this shop. This routine
	 * returns first the "static" items (items with isRandom=false), then the
	 * "random" items.
	 * 
	 * @param n
	 *            Total number of shields to be returned (actually the first n
	 *            ones in the shop.xml)
	 * @return ArrayList of AbstractItem in the shop
	 * @see net.littlelite.jurpe.items.AbstractItem
	 */
	public AbstractList<AbstractItem> getItems(int n)
	{
		int count = 0;
		AbstractList<AbstractItem> ret = new ArrayList<AbstractItem>();

		for (AbstractItem basItem : this.basicItems)
		{
			count++;
			if (count > n)
			{
				break;
			}

			ret.add(basItem);

		}

		return ret;
	}

	/**
	 * Get available weapons for a given skill
	 * 
	 * @param skl
	 *            Skill to search the weapon for
	 * @param rand
	 *            Set to false if you want to search among the weapon from
	 *            shop.xml, true if you want to search in the randomly created
	 *            weapons
	 * @return List of weapons (Weapon) in the shop
	 * @see Weapon
	 */
	public AbstractList<Weapon> getWeapons(Skill skl, boolean rand)
	{
		AbstractList<Weapon> al = new ArrayList<Weapon>();

		for (AbstractItem bi : this.basicItems)
		{
			if (bi instanceof Weapon)
			{
				Weapon wpn = (Weapon) bi;
				if (wpn.getSkill().equals(skl) && (wpn.isItemRandom() == rand))
				{
					al.add(wpn);
				}
			}
		}

		return al;
	}

	/**
	 * From an XmlLeaf read from XML file, constructs a new Shield (shield)
	 * 
	 * @param leaf
	 *            XmlLeaf read from file
	 * @return Shield (shield)
	 */
	private static Shield createShield(XMLLeaf leaf)
	{
		int costo;
		int peso;
		String nome;
		int dp;

		nome = leaf.getValue("Name");
		costo = leaf.getIntValue("Cost");
		peso = leaf.getIntValue("Weight");
		dp = leaf.getIntValue("DP");
		Shield tmpscudo = new Shield(nome, dp, costo, peso);
		return tmpscudo;
	}

	/**
	 * From an XmlLeaf read from XML file, constructs a new Armor (armor)
	 * 
	 * @param leaf
	 * @return Armor object from XML file
	 */
	private static Armor createArmor(XMLLeaf leaf)
	{
		int costo;
		int peso;
		String nome;
		int dp;
		int rd;

		nome = leaf.getValue("Name");
		costo = leaf.getIntValue("Cost");
		peso = leaf.getIntValue("Weight");
		dp = leaf.getIntValue("DP");
		rd = leaf.getIntValue("RD");

		Armor tmparmatura = new Armor(nome, costo, peso, dp, rd);
		return tmparmatura;
	}

	/**
	 * From an XmlLeaf read from XML file, constructs a new Item (items)
	 * 
	 * @param leaf
	 * @see Item
	 * @return Item object from XML file
	 */
	private static Item createItem(XMLLeaf leaf)
	{
		int costo;
		int peso;
		String nome;
		String ds;
		String attribute;
		int modifier;
		int wearable;
		int viewRad;

		nome = leaf.getValue("Name");
		costo = leaf.getIntValue("Cost");
		peso = leaf.getIntValue("Weight");
		ds = leaf.getValue("Description");
		attribute = leaf.getValue("Attribute");
		viewRad = leaf.getIntValue("ViewRadius");
		modifier = leaf.getIntValue("Points");
		wearable = leaf.getIntValue("Wearable");

		boolean wear = false;

		if (wearable == 1)
		{
			wear = true;
		}

		Item tmpItem = new Item(nome, costo, peso, ds, modifier, attribute, viewRad, wear);

		return tmpItem;
	}

	/**
	 * From an XmlLeaf read from XML file, constructs a new Arma (weapon)
	 * 
	 * @param leaf
	 *            Leaf containing info
	 * @param availableSkills
	 *            A weapon to be created needs a relative skill
	 * @return Weapon object from XML file
	 */
	private static Weapon createWeapon(XMLLeaf leaf, Skills availableSkills)
	{

		int costo;
		int peso;
		String nome;
		String strAbilita;
		int dCUT_SW, dCUT_TH;
		int dCR_SW, dCR_TH;
		int dIMP_SW, dIMP_TH;
		int frz_min, danno_max;
		int wrange;

		nome = leaf.getValue("Name");
		costo = leaf.getIntValue("Cost");
		peso = leaf.getIntValue("Weight");
		strAbilita = leaf.getValue("Skill");
		dCUT_SW = leaf.getIntValue("DamageCUT-SW");
		dCUT_TH = leaf.getIntValue("DamageCUT-TH");
		dCR_SW = leaf.getIntValue("DamageCR-SW");
		dCR_TH = leaf.getIntValue("DamageCR-TH");
		dIMP_SW = leaf.getIntValue("DamageIMP-SW");
		dIMP_TH = leaf.getIntValue("DamageIMP-TH");
		frz_min = leaf.getIntValue("MinStrenght");
		wrange = leaf.getIntValue("Range");
		danno_max = leaf.getIntValue("MAXDamage");

		Skill abilita = availableSkills.forName(strAbilita);
		return new Weapon(nome, costo, peso, abilita, dCUT_SW, dCUT_TH, dCR_SW, dCR_TH, dIMP_SW, dIMP_TH, frz_min, danno_max, wrange);
	}

	/**
	 * See AbstractXMLCollection
	 * 
	 * @param cont
	 * @see AbstractXMLCollection
	 */
	protected void listToArray(AbstractList<XMLLeaf> cont)
	{
		AbstractItem tmp;
		for (XMLLeaf tmpNode : cont)
		{
			if (tmpNode.getCategory().equals("Shield"))
			{
				tmp = MasterShop.createShield(tmpNode);
				basicItems.add(tmp);
			}
			else if (tmpNode.getCategory().equals("Armor"))
			{
				tmp = MasterShop.createArmor(tmpNode);
				basicItems.add(tmp);
			}
			else if (tmpNode.getCategory().equals("Weapon"))
			{
				tmp = MasterShop.createWeapon(tmpNode, this.avSkills);
				basicItems.add(tmp);
			}
			else if (tmpNode.getCategory().equals("Item"))
			{
				tmp = MasterShop.createItem(tmpNode);
				basicItems.add(tmp);
			}
		}
	}

	/**
	 * Shuffle Items
	 * 
	 * @param toShuffle
	 */
	private static void shuffle(AbstractList<AbstractItem> toShuffle)
	{
		Collections.shuffle(toShuffle);
	}

}
