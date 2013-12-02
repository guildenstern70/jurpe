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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;

import net.littlelite.jurpe.characters.Innate;
import net.littlelite.utils.metadata.XMLLeaf;

/**
 * Innates (inborn) skills container class
 */
public class Innates extends AbstractXMLCollection
{

	/**
	 * Constructor
	 */
	public Innates()
	{
		this.objects = new ArrayList<Object>();
		this.init("innates.xml");
	}

	/**
	 * Get an Innate (skill) searching for its name.
	 * 
	 * @param name
	 *            Innate name to get
	 * @return Corresponding Innate Object
	 * @see Innate
	 */
	public Innate forName(String name)
	{

		Innate innt = null;
		Iterator<Object> i = objects.iterator();

		while (i.hasNext())
		{
			innt = (Innate) i.next();
			if (innt.getName().equals(name))
			{
				break;
			}
		}

		return innt;
	}

	/**
	 * Get all innates advantages and disadvantages
	 * 
	 * @return array of Innates
	 */
	public Object[] getAll()
	{
		ArrayList<Object> tmpVector = new ArrayList<Object>();

		for (Object tmp : this.objects)
		{
			tmpVector.add(tmp);
		}

		return tmpVector.toArray();
	}

	/**
	 * Get available advantages.
	 * 
	 * @return array of Advantages
	 */
	public Object[] getAdvantages()
	{
		ArrayList<Object> tmpVector = new ArrayList<Object>();
		for (Object tmp : this.objects)
		{
			Innate innate = (Innate) tmp;
			// An innate is an advantage when its points are positive
			short points = innate.getPoints();
			if (points > 0)
			{
				tmpVector.add(tmp);
			}
		}

		return tmpVector.toArray();
	}

	/**
	 * Get available disadvantages
	 * 
	 * @return array of Disadvantages
	 */
	public Object[] getDisadvantages()
	{
		ArrayList<Object> tmpVector = new ArrayList<Object>();
		for (Object tmp : this.objects)
		{
			Innate innate = (Innate) tmp;
			// An innate is an advantage when its points are positive
			short points = innate.getPoints();
			if (points < -1)
			{
				tmpVector.add(tmp);
			}
		}

		return tmpVector.toArray();
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
		Innate tmp;

		for (XMLLeaf xmlNode : cont)
		{
			if (xmlNode.getCategory().equals("ISKILL"))
			{
				tmp = this.createInnate(xmlNode);
				this.objects.add(tmp);
			}
		}
	}

	/**
	 * Creates an Innate (skill) from XmlLeaf read from innates.xml
	 * 
	 * @param leaf
	 *            XmlLeaf with Skill properties
	 * @return New Innate
	 * @see XmlLeaf
	 */
	protected Innate createInnate(XMLLeaf leaf)
	{
		String name;
		String description;
		short points;

		name = leaf.getValue("NAME");
		points = (short) leaf.getIntValue("POINTS");
		description = leaf.getValue("DESCRIPTION");

		return new Innate(name, description, points);
	}

}
