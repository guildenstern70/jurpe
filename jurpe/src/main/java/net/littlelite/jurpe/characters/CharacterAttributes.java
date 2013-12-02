package net.littlelite.jurpe.characters;

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

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;

import net.littlelite.jurpe.system.JurpeUtils;

/**
 * Character (usually playing) Attributes class.
 * 
 * 
 */
public class CharacterAttributes implements Serializable
{
	private static final long serialVersionUID = 3317L;

	private String name;
	
	private char sex;
	private String aspect;
	private int age;
	private String height;
	private String weight;
	private AbstractList<Innate> cInnates;
	private PrimaryStats primaries;
	private String imageFile;

	/**
	 * Constructor. Set some defaults attributes.
	 */
	public CharacterAttributes()
	{
		this.name = "Hero";
		this.sex = 'M';
		this.aspect = "Average";
		this.age = 18;
		this.height = "180 cm";
		this.weight = "80 kg";
		this.cInnates = new ArrayList<Innate>();
		this.primaries = new PrimaryStats();
	}

	/**
	 * Create character attributes with name and PrimaryStats
	 * 
	 * @param name
	 *            Name of the character to create
	 * @param stats
	 *            Primary Stats (ST,DX etc) associated
	 * @see net.littlelite.jurpe.characters.PrimaryStats
	 */
	public CharacterAttributes(final String name, final PrimaryStats stats)
	{
		this.name = name;
		this.sex = 'U'; // unknown
		this.aspect = "";
		this.age = 100;
		this.height = "";
		this.weight = "";
		this.cInnates = new ArrayList<Innate>();
		this.primaries = stats;
	}

	/**
	 * Return current character ST,DX,IQ,HT
	 * 
	 * @return current character ST,DX,IQ,HT
	 */
	public PrimaryStats primaryStats()
	{
		return this.primaries;
	}

	/**
	 * Set primary stats
	 * 
	 * @param stats
	 *            primary stats (ST,DX,IQ,HT)
	 */
	public void setPrimaryStats(final PrimaryStats stats)
	{
		this.primaries = stats;
	}

	/**
	 * Return updated character points
	 * 
	 * @return updated character points
	 */
	public int getCharacterPoints()
	{
		PrimaryStats ps = this.primaries;
		int points = JurpeUtils.computeCharacterPoints(ps.getST(), ps.getDX(), ps.getIQ(), ps.getHT());
		return (points + this.getInnatesPoints());
	}

	/**
	 * Character's name
	 * 
	 * @return characters's name
	 */
	public String getName()
	{
		String nm = "";

		if (this.name != null)
		{
			nm = this.name;
		}

		return nm;
	}

	/**
	 * Set character's name
	 * 
	 * @param xName
	 *            name
	 */
	public void setName(String xName)
	{
		this.name = xName;
	}

	/**
	 * Get character's image file name, as in the resources file
	 * 
	 * @return character's image file name, ie: images/pcs/fpc01.gif
	 */
	public String getImageFileName()
	{
		return this.imageFile;
	}

	/**
	 * Set character's image file, as in the resources file
	 * 
	 * @param sImage
	 *            character's image file name, ie: images/pcs/fpc01.gif
	 */
	public void setImageFileName(String sImage)
	{
		this.imageFile = sImage;
	}

	/**
	 * Character Sex
	 * 
	 * @return 'M' if male, 'F' if female, 'O' if other
	 */
	public char getSex()
	{
		return this.sex;
	}

	/**
	 * Set Character Sex
	 * 
	 * @param s
	 *            can be "Male","M","Female","F","Other","O"
	 */
	public void setSex(String s)
	{
		this.sex = s.charAt(0);
	}

	/**
	 * String describing character appearance (ie: Ugly,Average, Beautiful)
	 * 
	 * @return character appearance
	 */
	public String getAspect()
	{
		return this.aspect;
	}

	/**
	 * Set string describing character appearance (ie: Ugly,Average, Beautiful)
	 * 
	 * @param appearance
	 *            character appearance
	 */
	public void setAspect(String appearance)
	{
		this.aspect = appearance;
	}

	/**
	 * Get character's age
	 * 
	 * @return character's age (in years or other measure depending on world)
	 */
	public int getAge()
	{
		return this.age;
	}

	/**
	 * Set character's age
	 * 
	 * @param sAge
	 *            character's age (in years or other measure depending on world)
	 */
	public void setAge(int sAge)
	{
		this.age = sAge;
	}

	/**
	 * Get character's height
	 * 
	 * @return character's height
	 */
	public String getHeight()
	{
		return this.height;
	}

	/**
	 * Set character's height
	 * 
	 * @param sHeight
	 *            character's height (in metrical or english system. Specify it:
	 *            ie: "180 cm")
	 */
	public void setHeight(String sHeight)
	{
		this.height = sHeight;
	}

	/**
	 * Get character's weight
	 * 
	 * @return character's weight (in metrical or english system. Specify it:
	 *         ie: "80 kg")
	 */
	public String getWeight()
	{
		return this.weight;
	}

	/**
	 * Set character's weight
	 * 
	 * @param sWeight
	 *            Character's weight
	 */
	public void setWeight(String sWeight)
	{
		this.weight = sWeight;
	}

	/**
	 * Get list of characters' innate skills (advantages, disadvantages)
	 * 
	 * @return Character innate skills
	 */
	public AbstractList<Innate> innates()
	{
		return this.cInnates;
	}

	/**
	 * Set character innate skills
	 * 
	 * @param innates
	 *            Collection of Innate objects
	 */
	public void setInnates(AbstractList<Innate> innates)
	{
		this.cInnates = innates;
	}

	/**
	 * Add innate to character
	 * 
	 * @param innate
	 *            Innate (advantage, disadvantage) to add to character
	 */
	public void addInnate(Innate innate)
	{
		this.cInnates.add(innate);
	}

	/**
	 * Get the value in character points of the innated skills
	 * 
	 * @return value in character points of the innated skills
	 */
	public int getInnatesPoints()
	{
		int points = 0;
		for (Innate inn : this.cInnates)
		{
			points += inn.getPoints();
		}
		return points;
	}

	/**
	 * Sets a whole list of innates to character
	 * 
	 * @param innate
	 *            Innate skill to add to character
	 */
	public void addInnates(AbstractList<Innate> innate)
	{
		this.cInnates = innate;
	}

	/**
	 * Get character's innate skills
	 * 
	 * @return Character's innate skills
	 */
	public AbstractList<Innate> getInnates()
	{
		return this.cInnates;
	}

	/**
	 * Get character's primary statistics
	 * 
	 * @return Character's primary statistics
	 */
	public PrimaryStats getPrimariyStats()
	{
		return this.primaries;
	}

}
