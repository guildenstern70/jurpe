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

import net.littlelite.jurpe.characters.Skill;
import net.littlelite.jurpe.system.Config;
import net.littlelite.utils.metadata.XMLLeaf;

/**
 * Skills available to this game, read from abilita.xml. Is a AbstractXMLCollection.
 * Divides between mental and physical skills.
 * 
 * 
 */
public class Skills extends AbstractXMLCollection implements Serializable
{
	private static final long serialVersionUID = 3317L;

	/**
	 * Constructor
	 */
	public Skills()
	{
		this.objects = new ArrayList<Object>();
		this.init(Config.SKILLXML);
	}

	/**
	 * Get an Skill (skill) object searching for its name.
	 * 
	 * @param name
	 *            Skill name to get
	 * @return Corresponding Skill Object
	 * @see Skill
	 */
	public Skill forName(String name)
	{
		Skill exAbil = null;

		for (Object obj : this.objects)
		{
			Skill abil = (Skill) obj;
			if (abil.getName().equals(name))
			{
				exAbil = abil;
				break;
			}
		}

		return exAbil;
	}

	/**
	 * Get Mental Skills available.
	 * 
	 * @return array of Mental Skills
	 */
	public Object[] getMental()
	{
		return this.GetSkill(false);
	}

	/**
	 * Get Physical Skills available
	 * 
	 * @return array of Physical Skills
	 */
	public Object[] getPhysical()
	{
		return this.GetSkill(true);
	}

	/**
	 * Get Weapon Physical Skills
	 * 
	 * @return Array of physical skills for weapons
	 */
	public AbstractList<Skill> getWeapon()
	{
		AbstractList<Skill> tmpVector = new ArrayList<Skill>();

		for (Object tmpObj : this.objects)
		{
			Skill tmpSkill = (Skill) tmpObj;
			if (tmpSkill.isWeapon())
			{
				tmpVector.add(tmpSkill);
			}
		}

		return tmpVector;
	}

	/**
	 * Converts an ArrayList to an internal array
	 * 
	 * @param cont
	 *            ArrayList containing skills to add to this collection
	 * @see AbstractXMLCollection
	 */
	protected void listToArray(AbstractList<XMLLeaf> cont)
	{
		Skill tmp;

		for (XMLLeaf xmltempnode : cont)
		{
			if (xmltempnode.getCategory().equals("Skill"))
			{
				tmp = this.createSkill(xmltempnode);
				objects.add(tmp);
			}
		}
	}

	/**
	 * Creates an Skill (skill) from XmlLeaf read from abilita.xml
	 * 
	 * @param leaf
	 *            XmlLeaf with Skill properties
	 * @return New Skill (skill)
	 * @see XmlLeaf
	 */
	protected Skill createSkill(XMLLeaf leaf)
	{
		String name;
		String type; // Physical or mental
		String difficulty; // Easy, Medium, Hard
		String basedOn; // DX, ST, HT, IQ
		int defModifier;
		double parryCoeff;
		int balanced;

		name = leaf.getValue("Name");
		type = leaf.getValue("Type");
		difficulty = leaf.getValue("Difficulty");
		basedOn = leaf.getValue("Attribute");
		defModifier = leaf.getIntValue("Default");
		parryCoeff = leaf.getDoubleValue("Parry");
		balanced = leaf.getIntValue("Balanced");

		return new Skill(name, type, difficulty, basedOn, defModifier, parryCoeff, balanced);

	}

	private Object[] GetSkill(boolean physical)
	{
		ArrayList<Object> tmpPhysical = new ArrayList<Object>();
		ArrayList<Object> tmpMental = new ArrayList<Object>();
		ArrayList<Object> retSkills;

		for (Object obj : this.objects)
		{
			Skill tmp = (Skill) obj;
			if (tmp.isPhysical())
			{
				tmpPhysical.add(tmp);
			}
			else
			{
				tmpMental.add(tmp);
			}
		}

		if (physical)
		{
			retSkills = tmpPhysical;
		}
		else
		{
			retSkills = tmpMental;
		}

		return retSkills.toArray();
	}

}
