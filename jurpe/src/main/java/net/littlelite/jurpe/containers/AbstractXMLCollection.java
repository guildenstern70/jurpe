package net.littlelite.jurpe.containers;

/**
 J.U.R.P.E. @version@
 Copyright (C) LittleLite Software

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

import java.io.File;
import java.util.AbstractList;

import net.littlelite.jurpe.system.Platform;
import net.littlelite.jurpe.system.resources.ResourceFinder;
import net.littlelite.utils.metadata.IXMLConverter;
import net.littlelite.utils.metadata.XMLLeaf;

/**
 * AbstractXMLCollection is a container class for objects of type Oggetto that are read
 * from an XML file.
 * 
 * 
 * @see SaxConverter
 * @see XML_Writer
 * @see XML_Navigator
 * @see XmlLeaf
 */
public abstract class AbstractXMLCollection
{

	/**
	 * XmlLeafs
	 */
	protected AbstractList<Object> objects = null;

	/**
	 * Read from xmlnomefile and populate objects AbstractList
	 * 
	 * @param xmlFileName
	 *            File name without path (which is read from Config class)
	 */
	protected void init(String xmlFileName)
	{
		ResourceFinder rf = ResourceFinder.resources();
		rf.extractResource(xmlFileName, "temp.xml");

		try
		{
			this.listToArray(this.readFromFile("temp.xml"));
		}
		catch (Exception e)
		{
			System.err.println("Problem loading configuration file: " + xmlFileName + " (" + e.getMessage() + ")");
			e.printStackTrace();
			System.exit(1);
		}

		File temp = new File("temp.xml");
		temp.delete();
	}

	/**
	 * Return object's iterator
	 * 
	 * @return contained objects iterator
	 */
	protected AbstractList<Object> getLeafs()
	{
		return this.objects;
	}

	/**
	 * Populate an array list from the XML file. Needs to be implemented with
	 * particulars different from types of Oggetto to types of Oggetto.
	 * 
	 * @param nomexml
	 *            Complete path to XML file
	 * @return Array of objects read from file.
	 * @see XML_Navigator
	 */
	protected AbstractList<XMLLeaf> readFromFile(String nomexml)
	{
		IXMLConverter mn = Platform.getXMLConverter();
		mn.parse(nomexml);
		return mn.getCollectedXML();
	}

	/**
	 * Number of items/objects contained.
	 * 
	 * @return Size of objects arraylist
	 */
	public int size()
	{
		return this.objects.size();
	}

	/**
	 * From the output of readFromFile() builds an AbstractList of specific
	 * objects. IE: Skill container will fill the AbstractList with XmlLeaf
	 * objects
	 * 
	 * @param cont
	 *            Container output of "leggiDaFile"
	 */
	protected abstract void listToArray(AbstractList<XMLLeaf> cont);
	/*
	 * implement for instance as follow: { Iterator i = cont.iterator(); Abilita
	 * tmp; while (i.hasNext()) { XmlLeaf xmltempnode = (XmlLeaf)i.next(); if
	 * (xmltempnode.getCategoria().equals("Abilita")) { tmp =
	 * this.creaAbilita(xmltempnode); abilita.add(tmp); } } }
	 */

}
