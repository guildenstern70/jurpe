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
 * BasicPC is the super base class for all Playing Characters, either human or
 * artificial.
 */
public abstract class BasicPC implements Serializable, Comparable<BasicPC>
{
	protected boolean ai; // true if character is Artificial Intelligence
	// (NPC)
	protected CharacterAttributes characterAttributes; // ST, DX, IQ, HT and
	// related
	protected int damageResistance; // Damage Resistance
	protected int passiveDefense; // Passive Defense

	/**
	 * Comparing function. PC comparison is based on speed.
	 * 
	 * @param rv
	 *            PC to be compared with this
	 * @return -1 if rv is less fast than this, 1 if it's faster, 0 else
	 */
	public int compareTo(BasicPC rv)
	{
		float tvel = this.characterAttributes.primaryStats().getSpeed();
		float rvSpeed = rv.characterAttributes.primaryStats().getSpeed();
		return (tvel > rvSpeed ? -1 : (tvel == rvSpeed ? 0 : 1));
	}

	/**
	 * Get Character Attributes of PC - name, height and such
	 * 
	 * @see net.littlelite.jurpe.characters.CharacterAttributes
	 * @return character attributes
	 */
	public CharacterAttributes getCharacterAttributes()
	{
		return characterAttributes;
	}

	/**
	 * Get character current hit points
	 * 
	 * @return character current hit points
	 */
	public int getCurrentHP()
	{
		return this.characterAttributes.primaryStats().getCurrentHitPoints();
	}

	/**
	 * Get Damage Resistance
	 * 
	 * @return damage resistance
	 */
	public int getDamageResistance()
	{
		return damageResistance;
	}

	/**
	 * Return character HEALTH points (HT)
	 * 
	 * @return character HEALTH points (HT)
	 */
	public int getHT()
	{
		return this.characterAttributes.primaryStats().getHT();
	}

	/**
	 * Get Passive Defense
	 * 
	 * @return passive defense
	 */
	public int getPassiveDefense()
	{
		return passiveDefense;
	}

	/**
	 * Return Primary Statistics of character (ST, DX, IQ and such)
	 * 
	 * @see net.littlelite.jurpe.characters.PrimaryStats
	 * @return Primary Statistics of character
	 */
	public PrimaryStats getPrimaryStats()
	{
		return this.characterAttributes.primaryStats();
	}

	/**
	 * Get character short description following this pattern: Name:
	 * ST,DX,IQ,HT/HTmax
	 * 
	 * @return short character description
	 */
	public String getShortDescription()
	{
		StringBuilder sb = new StringBuilder(this.characterAttributes.getName());
		if (this.isAI())
		{
			sb.append(" (CPU)");
		}
		sb.append(" ");
		sb.append("ST:");
		sb.append(this.characterAttributes.primaryStats().getST());
		sb.append(" DX:");
		sb.append(this.characterAttributes.primaryStats().getDX());
		sb.append(" IQ:");
		sb.append(this.characterAttributes.primaryStats().getIQ());
		sb.append(" HT:");
		sb.append(this.getCurrentHP());
		sb.append("/");
		sb.append(this.getHT());

		return sb.toString();
	}

	/**
	 * Return true if this charater is controlled by Artificial Intelligence
	 * 
	 * @return true if this charater is controlled by Artificial Intelligence
	 */
	public boolean isAI()
	{
		return this.ai;
	}

	/**
	 * Set if this character is AI
	 * 
	 * @param b
	 */
	public void setAI(boolean b)
	{
		this.ai = b;
	}

	/**
	 * Set character attributes (ST,IQ,DX,HT).
	 * 
	 * @see net.littlelite.jurpe.characters.PrimaryStats
	 * @param attributes
	 */
	public void setCharacterAttributes(CharacterAttributes attributes)
	{
		characterAttributes = attributes;
	}

	/**
	 * Set damage resistance
	 * 
	 * @param i
	 *            Character's damage resistance
	 */
	public void setDamageResistance(int i)
	{
		damageResistance = i;
	}

	/**
	 * Set passive defense
	 * 
	 * @param i
	 *            Character's passive defense
	 */
	public void setPassiveDefense(int i)
	{
		passiveDefense = i;
	}

}
