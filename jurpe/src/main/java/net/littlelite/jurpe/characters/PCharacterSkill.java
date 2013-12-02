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

/**
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

/**
 * It's a type that defines a skill associated to a PCharacter. PCharacterSkill
 * differs from Skill in being associated with a level, and maintained as an
 * attribute of a Charater. For instance, while Skill may be 'Sword',
 * PCharacterSkill is the 'Sword' skill learned by current character at level
 * 14.
 * 
 * @see Skill
 * 
 */
public class PCharacterSkill implements Serializable
{

	private static final long serialVersionUID = 3317L;

	/**
	 * Skill of this PCharacterSkill
	 */
	private Skill a;

	/**
	 * Current level for this PCharacterSkill
	 */
	private int level;

	/**
	 * Constructor
	 * 
	 * @param aA
	 *            Ability
	 * @param aLivello
	 *            current level
	 */
	public PCharacterSkill(Skill aA, int aLivello)
	{
		a = aA;
		level = aLivello;
	}

	/**
	 * Returns the name and level of ability
	 * 
	 * @return The name and level of ability
	 */
	@Override
	public String toString()
	{
		StringBuilder ab = new StringBuilder(a.getName());
		ab.append(" (");
		ab.append(level);
		ab.append(")");
		return ab.toString();
	}

	/**
	 * An PCharacterSkill is equal to another if they have THE SAME NAME. (IE: A
	 * character cannot have two abilities with the same name)
	 * 
	 * @param e
	 *            PCharacterSkill to confront with.
	 * @return true if argument is equal to this
	 */
	@Override
	public boolean equals(Object e)
	{

		if (e.getClass().getName().equals(this.getClass().getName()))
		{
			PCharacterSkill confr = (PCharacterSkill) e;
			if (this.a.getName().equals(confr.getSkill().getName()))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Return hash key for object. For our purposes, the hash is made upon the
	 * letter of the name of the skill
	 * 
	 * @return hash code
	 */
	@Override
	public int hashCode()
	{
		int hash = 1;
		String name = this.getName();
		for (int j = 0; j <= name.length(); j++)
		{
			hash *= name.charAt(j);
		}
		return hash;
	}

	/**
	 * Returns the name of the ability
	 * 
	 * @return The name and level of ability
	 */
	public String getName()
	{
		return this.a.getName();
	}

	/**
	 * Returns the current level of the ability
	 * 
	 * @return The name and level of ability
	 */
	public int getLevel()
	{
		return level;
	}

	/**
	 * Add "delta" to the current ability level
	 * 
	 * @param delta
	 *            Number of levels to add to current ability level
	 */
	public void improveSkillLevel(int delta)
	{
		this.level += delta;
	}

	/**
	 * Returns an array of Strings that describes the PCharacterSkill. Dialog
	 * Box needs it to display info.
	 * 
	 * @return Array of 4 strings to describe current ability.
	 */
	public String[] toStrings()
	{

		String[] strs = new String[4];
		strs[0] = a.getName();
		strs[1] = String.valueOf(this.level);
		strs[2] = a.getType();
		strs[3] = a.getBase();

		return strs;
	}

	/**
	 * Returns the Skill associated with this current ability.
	 * 
	 * @return Skill associated with this current ability.
	 */
	public Skill getSkill()
	{
		return a;
	}

}