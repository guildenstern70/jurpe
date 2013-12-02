package net.littlelite.jurpe.system.generation;

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
import java.util.ArrayList;

import net.littlelite.jurpe.containers.AbstractXMLCollection;
import net.littlelite.jurpe.system.Config;
import net.littlelite.utils.AxsRand;
import net.littlelite.utils.ValuedString;
import net.littlelite.utils.metadata.XMLLeaf;

/**
 * Class for creating random names for monsters, items, and such. Random names
 * may be composed by 2 or 3 words. In case of 2 words they are in the form
 * attribute+name, ie: Red Dragon, in case of 3 words they are in the form
 * attribute+name+geography, ie: Red Dragon of the Mysty Lands. Strings are read
 * from an XML file like dictionary.xml. You may specify tags to read from. When
 * read, strings are stored in ArrayList of ValuedString objects. You may then
 * ask for a string with an associated value. Higher values corresponds to rarer
 * objects.
 * 
 * 
 */
public final class RandomNames extends AbstractXMLCollection
{

	private AbstractList<ValuedString> attributes = null;
	private AbstractList<ValuedString> geographies = null;
	private AbstractList<ValuedString> shieldNames = null;
	private AbstractList<ValuedString> armorNames = null;
	private AbstractList<ValuedString> monsterNames = null;
	private String attributeOf; // XML Tag for Attributes
	private String nameOf; // XML Tag for Monsters
	private String geographyOf; // XML Tag for Geography
	private String armorOf; // XML Tag for Armors
	private String shieldOf; // XML Tag for Shields

	/**
	 * Constructor with default values.
	 */
	public RandomNames()
	{
		attributes = new ArrayList<ValuedString>();
		monsterNames = new ArrayList<ValuedString>();
		geographies = new ArrayList<ValuedString>();
		shieldNames = new ArrayList<ValuedString>();
		armorNames = new ArrayList<ValuedString>();

		this.attributeOf = "Attribute";
		this.nameOf = "MonsterName";
		this.geographyOf = "Geography";
		this.armorOf = "ArmorName";
		this.shieldOf = "ShieldName";
		this.init(Config.DICTIONARYXML);
	}

	/**
	 * Constructor. You pass the tags to read from the XML file, ie:
	 * "MonsterName", "ItemName", "Attribute", "Geography"
	 * 
	 * @param attribute
	 *            The first tag name to be read from dictionary.xml
	 * @param name
	 *            The second tag name to be read from dictionary.xml
	 * @param geography
	 *            The third tag name to be read from dictionary.xml
	 * @param armor
	 *            The fourth tag name to be read from dictionary.xml
	 * @param shield
	 *            The fifth tag name to be read from dictionary.xml
	 */
	public RandomNames(String attribute, String name, String geography, String armor, String shield)
	{
		this.attributeOf = attribute;
		this.nameOf = name;
		this.geographyOf = geography;
		this.armorOf = armor;
		this.shieldOf = shield;

		attributes = new ArrayList<ValuedString>();
		monsterNames = new ArrayList<ValuedString>();
		geographies = new ArrayList<ValuedString>();
		shieldNames = new ArrayList<ValuedString>();
		armorNames = new ArrayList<ValuedString>();

		this.init(Config.DICTIONARYXML);
	}

	/**
	 * Get generated a complete Monster name (attribute+name+geography)., with a
	 * default level of 100 points.
	 * 
	 * @return random generated name for a 100 points monster
	 */
	public String getCompleteMonsterName()
	{
		return this.buildCompleteName(100);
	}

	/**
	 * Get generated a complete Monster name (attribute+name+geography).
	 * 
	 * @param value
	 *            monster level (monster character points) (from 40 to 140)
	 * @return random generated name for a 'value' points monster
	 */
	public String getCompleteMonsterName(int value)
	{
		return this.buildCompleteName(value);
	}

	/**
	 * Get random generated attribute (Red, Mysty, Hungry ...). Every attribute
	 * has an associated value in the XML dictionary.xml). Values ranges from 10
	 * (common) to 70 (very rare or dangerous or wonderful)
	 * 
	 * @param value
	 *            Rarity or exceptionality. Range from 10 (common) to 70 (rare)
	 * @return A random generated attribute string
	 */
	public String getAttribute(ItemValueType value)
	{
		return this.getRandom(value.getType(), (byte) 3);
	}

	/**
	 * Get random generated geography (of the Red Lands, of the Misty Mountains
	 * ...). Every attribute has an associated value in the XML dictionary.xml).
	 * Values ranges from 10 (common) to 70 (very rare or dangerous or
	 * wonderful)
	 * 
	 * @param value
	 *            Rarity or exceptionality. Range from 10 (common) to 70 (rare)
	 * @return A random generated geography string
	 */
	public String getGeography(ItemValueType value)
	{
		return this.getRandom(value.getType(), (byte) 2);
	}

	/**
	 * Get random generated shield name. Every attribute has an associated value
	 * in the XML dictionary.xml). Values ranges from 10 (common) to 70 (very
	 * rare or dangerous or wonderful)
	 * 
	 * @param value
	 *            Rarity or exceptionality. Range from 10 (common) to 70 (rare)
	 * @return A random generated geography string
	 */
	public String getShield(ItemValueType value)
	{
		return this.getRandom(value.getType(), (byte) 4);
	}

	/**
	 * Get random generated armor name. Every attribute has an associated value
	 * in the XML dictionary.xml). Values ranges from 10 (common) to 70 (very
	 * rare or dangerous or wonderful)
	 * 
	 * @param value
	 *            Rarity or exceptionality. Range from 10 (common) to 70 (rare)
	 * @return A random generated geography string
	 */
	public String getArmor(ItemValueType value)
	{
		return this.getRandom(value.getType(), (byte) 5);
	}

	/**
	 * Get random generated monster name (kavu, donkey, dragon ...). Every
	 * attribute has an associated value in the XML dictionary.xml). Values
	 * ranges from 10 (common) to 70 (very rare or dangerous or wonderful)
	 * 
	 * @param value
	 *            Rarity or exceptionality. Range from 10 (common) to 70 (rare)
	 * @return A random generated geography string
	 */
	public String getMonsterName(ItemValueType value)
	{
		return this.getRandom(value.getType(), (byte) 1);
	}

	/**
	 * Create a single instance of ValuedString (string with an associated
	 * value) from an XmlLeaf read from a file.
	 * 
	 * @param leaf
	 *            XmlLeaf containing specifics of monster
	 * @return ValuedString A String that contains the name and the value read
	 *         from XmlLeaf
	 * @see AbstractXMLCollection
	 * @see ValuedString
	 */
	private static ValuedString createName(XMLLeaf leaf)
	{
		String nm = leaf.getValue("Name");
		return new ValuedString(nm, Short.parseShort(leaf.getValue("Value")));
	}

	/**
	 * See AbstractXMLCollection
	 * 
	 * @param cont
	 *            List to parsed
	 */
	protected void listToArray(AbstractList<XMLLeaf> cont)
	{
		ValuedString tempVal;

		for (XMLLeaf tmpNode : cont)
		{
			tempVal = RandomNames.createName(tmpNode);

			if (tmpNode.getCategory().equals(this.attributeOf))
			{
				this.attributes.add(tempVal);
			}
			else if (tmpNode.getCategory().equals(this.nameOf))
			{
				this.monsterNames.add(tempVal);
			}
			else if (tmpNode.getCategory().equals(this.geographyOf))
			{
				this.geographies.add(tempVal);
			}
			else if (tmpNode.getCategory().equals(this.armorOf))
			{
				this.armorNames.add(tempVal);
			}
			else if (tmpNode.getCategory().equals(this.shieldOf))
			{
				this.shieldNames.add(tempVal);
			}
		}
	}

	/**
	 * Get random name, or attribute or geography, based on 'what' input
	 * 
	 * @param value
	 *            Rarity value: integer ranging from 10 to 70 ca.
	 * @param what
	 *            1=name, 2=geography, else= attribute
	 * @return random word
	 */
	private String getRandom(short value, byte what)
	{
		AxsRand axs = AxsRand.getReference();
		int obtValue = 0;
		ValuedString att = new ValuedString("Not Found", (short) 0);
		AbstractList<ValuedString> wordCollection;

		switch (what)
		{
			case 1:
				wordCollection = this.monsterNames;
				break;
			case 2:
				wordCollection = this.geographies;
				break;
			case 4:
				wordCollection = this.shieldNames;
				break;
			case 5:
				wordCollection = this.armorNames;
				break;
			case 3:
			default:
				wordCollection = this.attributes;
		}

		int maxAttribute = wordCollection.size();
		while (!RandomNames.isInRange(value, obtValue)) // ca. 10 points range
		{
			att = wordCollection.get(axs.randInt(maxAttribute));
			obtValue = att.getValue();
		}

		return att.getName();
	}

	private String buildCompleteName(int value)
	{
		AxsRand axs = AxsRand.getReference();
		int maxAttribute = this.attributes.size();
		int maxNames = this.monsterNames.size();
		int maxGeos = this.geographies.size();
		int totpoints = 0;
		StringBuilder complete = new StringBuilder("Not found!");
		int tries = 0;

		while (tries < 100000)
		{
			ValuedString att = this.attributes.get(axs.randInt(maxAttribute));
			ValuedString nam = this.monsterNames.get(axs.randInt(maxNames));
			ValuedString geo = this.geographies.get(axs.randInt(maxGeos));

			totpoints = att.getValue() + nam.getValue() + geo.getValue();

			if (RandomNames.isInRange(value, totpoints))
			{
				complete = new StringBuilder(att.getName());
				complete.append(" ");
				complete.append(nam.getName());
				complete.append(" ");
				complete.append(geo.getName());
				break;
			}

			tries++;
		}

		return complete.toString();

	}

	/**
	 * Determines if x number is "sufficiently next to" value. That is: if x in
	 * range.
	 * 
	 * @param value
	 *            target value
	 * @param x
	 *            number to confront
	 * @param range
	 *            admissible range
	 * @return true if x is "suficiently next to" value
	 */
	private static boolean isInRange(int value, int x, int range)
	{
		if (x < (value - range) || x >= (value + range))
		{
			return false;
		}

		return true;
	}

	private static boolean isInRange(int value, int x)
	{
		return RandomNames.isInRange(value, x, 5);
	}

	/**
	 * For test purposes only
	 * 
	 * @deprecated
	 * @param args
	 *            not used
	 */
	public static void main(String[] args)
	{
		RandomNames md = new RandomNames();

		for (int j = 20; j <= 140; j += 10) // Value
		{
			ItemValueType x = ItemValueType.fromValue((byte) (j / 2));
			for (short s = 1; s <= 10; s++) // Tests
			{
				System.out.print(String.valueOf(j));
				System.out.print(">\t NAME:");
				System.out.println(md.getCompleteMonsterName(j));
				System.out.print("    \t ATTRIBUTE:");
				System.out.println(md.getAttribute(x));
				System.out.print("    \t GEOGRAPHY:");
				System.out.println(md.getGeography(x));
				System.out.print("    \t MONSTER:");
				System.out.println(md.getMonsterName(x));
				System.out.print("    \t SHIELD:");
				System.out.println(md.getShield(x));
				System.out.print("    \t ARMOR:");
				System.out.println(md.getArmor(x));
			}
			System.out.println("\n");
		}

	}

}
