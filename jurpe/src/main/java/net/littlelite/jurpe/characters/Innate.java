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

package net.littlelite.jurpe.characters;

import java.io.Serializable;

/**
 * Base class for advantages/disadvantages/quirks. An innate may be: -
 * recognized: Correctly apply related modifiers to character. or: - supported:
 * a character may get that Innate, but will just show its description and no
 * modifier will be applied. Innate will be read from an XML file. When
 * contructed, if the Innate name corresponds to one
 */
public class Innate implements Serializable
{

	private static final long serialVersionUID = 3317L;

	private String name;
	private String description;
	private short points;
	private boolean implemented;

	/**
	 * Construct a new Innate Ability, such as Advantage, Disadvantage, Quirk
	 * 
	 * @param iName
	 *            Name of Innate Ability
	 * @param iDescription
	 *            Description of Innate Ability
	 * @param iPoints
	 *            Character Points needed to have the Innate Ability (may be
	 *            negative)
	 */
	public Innate(String iName, String iDescription, short iPoints)
	{
		this.implemented = false;
		this.name = iName;
		this.description = iDescription;
		this.points = iPoints;
	}

	/**
	 * Get Innate Ability name
	 * 
	 * @return Innate Ability name
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Get Innate Ability description
	 * 
	 * @return Innate Ability description
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * Get Character Points needed
	 * 
	 * @return character points needed
	 */
	public short getPoints()
	{
		return this.points;
	}

	/**
	 * toString
	 * 
	 * @return name of Innate
	 */
        @Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
                sb.append(this.name);
                sb.append(" (");
                sb.append(this.points);
                sb.append(" pt.)");
                return sb.toString();
	}

	/**
	 * Comparison is made on names
	 * 
	 * @param rv
	 * @return 1 if equal
	 */
	public int compareTo(Object rv)
	{
		Innate rvi;

		try
		{
			rvi = (Innate) rv;
		}
		catch (Exception e)
		{
			return 0;
		}

		return this.name.compareTo(rvi.name);
	}

	/**
	 * Set if this Innate is implemented (recognized) Library
	 * 
	 * @param i
	 *            true if this Innate is recognized
	 * @version@ Library
	 */
	protected void setImplemented(boolean i)
	{
		this.implemented = i;
	}

	/**
	 * Return true if this Innate has been implemented
	 * 
	 * @return True if this Innate has been implemented
	 */
	public boolean isImplemented()
	{
		return implemented;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string)
	{
		description = string;
	}

	/**
	 * @param string
	 */
	public void setName(String string)
	{
		name = string;
	}

	/**
	 * @param s
	 */
	public void setPoints(short s)
	{
		points = s;
	}

}