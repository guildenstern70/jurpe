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
 * AbstractPC is the super base class for all Playing Characters, either human or
 * artificial.
 */
public abstract class AbstractPC implements Serializable, Comparable<AbstractPC>
{
	// UID
	private static final long serialVersionUID = 3L;

	protected boolean isArtificial; // true if character is Artificial Intelligence
	protected CharacterAttributes charAttributes; // ST, DX, IQ, HT and related
	protected int damageResistance; // Damage Resistance
	protected int passiveDefense; // Passive Defense

	/**
	 * Comparing function. PC comparison is based on speed.
	 * 
	 * @param compareObject
	 *            PC to be compared with this
	 * @return -1 if rv is less fast than this, 1 if it's faster, 0 else
	 */
	public int compareTo(final AbstractPC compareObject)
	{
		float tvel = this.charAttributes.primaryStats().getSpeed();
		float rvSpeed = compareObject.charAttributes.primaryStats().getSpeed();
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
		return charAttributes;
	}

	/**
	 * Get character current hit points
	 * 
	 * @return character current hit points
	 */
	public int getCurrentHP()
	{
		return this.charAttributes.primaryStats().getCurrentHitPoints();
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
		return this.charAttributes.primaryStats().getHT();
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
		return this.charAttributes.primaryStats();
	}

	/**
	 * Get character short description following this pattern: Name:
	 * ST,DX,IQ,HT/HTmax
	 * 
	 * @return short character description
	 */
	public String getShortDescription()
	{
		final StringBuilder sb = new StringBuilder(this.charAttributes.getName());
		if (this.isAI())
		{
			sb.append(" (CPU)");
		}
		sb.append(" ");
		sb.append("ST:");
		sb.append(this.charAttributes.primaryStats().getST());
		sb.append(" DX:");
		sb.append(this.charAttributes.primaryStats().getDX());
		sb.append(" IQ:");
		sb.append(this.charAttributes.primaryStats().getIQ());
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
		return this.isArtificial;
	}

	/**
	 * Set if this character is AI
	 * 
	 * @param isAI
	 */
	public void setAI(boolean isAI)
	{
		this.isArtificial = isAI;
	}

	/**
	 * Set character attributes (ST,IQ,DX,HT).
	 * 
	 * @see net.littlelite.jurpe.characters.PrimaryStats
	 * @param attributes
	 */
	public void setCharacterAttributes(CharacterAttributes attributes)
	{
		charAttributes = attributes;
	}

	/**
	 * Set damage resistance
	 * 
	 * @param damageRes
	 *            Character's damage resistance
	 */
	public void setDamageResistance(int damageRes)
	{
		damageResistance = damageRes;
	}

	/**
	 * Set passive defense
	 * 
	 * @param passiveDef
	 *            Character's passive defense
	 */
	public void setPassiveDefense(int passiveDef)
	{
		passiveDefense = passiveDef;
	}

}
