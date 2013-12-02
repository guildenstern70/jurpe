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
import net.littlelite.jurpe.system.OSProps;
import net.littlelite.jurpe.system.resources.LibraryStrings;

/**
 * Class type for any CORPS skill.
 * 
 * 
 */
public class Skill implements Serializable
{

	private static final long serialVersionUID = 3317L;

	/**
	 * Easy skill
	 */
	public static final int EASY = 7;

	/**
	 * Medium skill
	 */
	public static final int MEDIUM = 8;

	/**
	 * Difficult skill
	 */
	public static final int HARD = 9;

	/**
	 * Mental skill
	 */
	public static final int MENTAL_SK = 10;

	/**
	 * Physical Ability
	 */
	public static final int PHYSICAL_SK = 11;

	/**
	 * Based upon intelligence (IQ)
	 */
	public static final int IQ_BASED = 12;

	/**
	 * Based upon DX
	 */
	public static final int DX_BASED = 13;

	/**
	 * Based upon ST
	 */
	public static final int ST_BASED = 14;

	/**
	 * Based upon HT
	 */
	public static final int HT_BASED = 15;

	/**
	 * Ability name
	 */
	protected String nome;

	/**
	 * Skill type (mental or physical)
	 */
	protected int tipo; // Physical or mental

	/**
	 * Difficulty: easy, medium, hard
	 */
	public int difficult;

	/**
	 * Based upon: IQ, HT, DX, ST
	 */
	protected int basedOn;

	/**
	 * Default level for skill. If a skill is IQ-5, modif_default will be -5 and
	 * basata_su will be Abilita.INT_BASED.
	 */
	protected int defaultModifier;

	/**
	 * If skill has parry modifier, store it here.
	 */
	protected double parry;

	/**
	 * If skill is HandWeapon, this is Balanced attribute
	 */
	protected boolean balanced;

	/**
	 * Constructor (made with int values)
	 * 
	 * @param aNome
	 *            Skill name
	 * @param aTipo
	 *            Skill type: physical or mental. Use static numbers
	 *            Abilita.AB_FISICA, Abilita.AB_MENTALE.
	 * @param aDifficolta
	 *            Skill difficulty. Use static numbers Abilita.FACILE,
	 *            Abilita.DIFFICILE, Abilita.MEDIA.
	 * @param basataSu
	 *            Based on which proprery: IQ, DX, COS, ST. Use corresponding
	 *            static number Abilita.INT_BASED and such.
	 * @param modifDefault
	 *            Default level for skill. For instance, if ability is defaulted
	 *            at IQ-5 we will have: <br>
	 *            basata_su = Abilita.INT_BASED <br>
	 *            modifDefault = -5<br>
	 * @param coeffParata
	 *            For skills that have Parry modifiers.
	 * @param aBalanced
	 *            Balanced coefficient.
	 */
	public Skill(String aNome, int aTipo, int aDifficolta, int basataSu, int modifDefault, double coeffParata, int aBalanced)
	{
		nome = aNome;
		tipo = aTipo;
		difficult = aDifficolta;
		basedOn = basataSu;
		defaultModifier = modifDefault;
		parry = coeffParata;

		if (aBalanced == 1)
		{
			balanced = true;
		}
		else
		{
			balanced = false;
		}
	}

	/**
	 * Constructor (made with string values read from XML)
	 * 
	 * @param xNome
	 *            Skill name
	 * @param xTipo
	 *            Skill type: physical or mental. Use static numbers
	 *            Abilita.AB_FISICA, Abilita.AB_MENTALE.
	 * @param xDifficolta
	 *            Skill difficulty: it can be "Facile", "Medio" or "Difficile"
	 * @param xBasata_su
	 *            Based on which proprery: IQ, DX, COS, ST.
	 * @param xCoeff_parata
	 *            For skills that have Parry modifiers.
	 * @param xModif_default
	 *            Default modifier
	 * @param xBalanced
	 *            if == 1 then Skill is for balanced weapon
	 */
	public Skill(String xNome, String xTipo, String xDifficolta, String xBasata_su, int xModif_default, double xCoeff_parata, int xBalanced)
	{
		this(xNome, 0, 0, 0, xModif_default, xCoeff_parata, xBalanced);

		if (xTipo.equalsIgnoreCase(LibraryStrings.PHYSICAL))
		{
			this.tipo = PHYSICAL_SK;
		}
		else
		{
			this.tipo = MENTAL_SK;
		}

		if (xDifficolta.equalsIgnoreCase(LibraryStrings.MEDIUM))
		{
			this.difficult = MEDIUM;
		}
		else if (xDifficolta.equalsIgnoreCase(LibraryStrings.EASY))
		{
			this.difficult = EASY;
		}
		else if (xDifficolta.equalsIgnoreCase(LibraryStrings.HARD))
		{
			this.difficult = HARD;
		}

		if (xBasata_su.equalsIgnoreCase(LibraryStrings.DEX))
		{
			this.basedOn = DX_BASED;
		}
		else if (xBasata_su.equalsIgnoreCase(LibraryStrings.INT))
		{
			this.basedOn = IQ_BASED;
		}
		else if (xBasata_su.equalsIgnoreCase(LibraryStrings.FOR))
		{
			this.basedOn = ST_BASED;
		}
		else if (xBasata_su.equalsIgnoreCase(LibraryStrings.COS))
		{
			this.basedOn = HT_BASED;
		}
	}

	/**
	 * Returns name of skill
	 * 
	 * @return Name of skill
	 */
	public String getName()
	{
		return nome;
	}

	/**
	 * Returns true if Skill is HandWeapon, and the particular Weapon is
	 * balanced.
	 * 
	 * @return Balance type of weapon
	 */
	public boolean isBalanced()
	{
		return this.balanced;
	}

	/**
	 * Returns type of skill (mental or physical)
	 * 
	 * @return Type of skill (mental or physical)
	 */
	public String getType()
	{

		String restr;

		switch (this.tipo)
		{
			case MENTAL_SK:
				restr = LibraryStrings.MENTAL;
				break;
			case PHYSICAL_SK:
				restr = LibraryStrings.PHYSICAL;
				break;
			default:
				restr = LibraryStrings.UNKNOWN;
		}

		return restr;
	}

	/**
	 * Returns true if it is a Physical skill
	 * 
	 * @return true if it is a Physical skill
	 */
	public boolean isPhysical()
	{
		if (this.tipo == PHYSICAL_SK)
		{
			return true;
		}

		return false;
	}

	/**
	 * Returns true if it is a Physical skill used for Weapons. This is a check
	 * on Parry capability (it is a weapon if parry>-1)
	 * 
	 * @return true if it is a Physical Weapon skill
	 */
	public boolean isWeapon()
	{
		if (this.tipo == PHYSICAL_SK)
		{
			if (this.parry > -1)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Get static number for based upon, i.e.: INT_BASED, COS_BASED
	 * 
	 * @return Get static number for based upon, i.e.: INT_BASED, COS_BASED
	 */
	public int getIntBase()
	{
		return this.basedOn;
	}

	/**
	 * Get String saying if skill is based upon IQ,DX,COS,ST
	 * 
	 * @return Get String saying if skill is based upon IQ,DX,COS,ST
	 */
	public String getBase()
	{

		String restr;

		switch (this.basedOn)
		{
			case DX_BASED:
				restr = LibraryStrings.DEX;
				break;
			case IQ_BASED:
				restr = LibraryStrings.INT;
				break;
			case ST_BASED:
				restr = LibraryStrings.FOR;
				break;
			case HT_BASED:
				restr = LibraryStrings.COS;
				break;
			default:
				restr = LibraryStrings.UNKNOWN;
		}

		return restr;
	}

	/**
	 * Returns difficulty of skill (String for descriptions)
	 * 
	 * @return "Facile" if it's easy. "Media" if it's medium. "Difficile" if
	 *         it's difficult.
	 */
	public String getDifficolta()
	{

		String restr;

		switch (this.difficult)
		{
			case EASY:
				restr = LibraryStrings.EASY;
				break;
			case MEDIUM:
				restr = LibraryStrings.MEDIUM;
				break;
			case HARD:
				restr = LibraryStrings.HARD;
				break;
			default:
				restr = LibraryStrings.UNKNOWN;
		}

		return restr;

	}

	/**
	 * Returns difficulty of skill (static number)
	 * 
	 * @return Number for "easy" (FACILE), "medium" (MEDIA), "difficult"
	 *         (DIFFICILE)
	 */
	public int getIntDifficulty()
	{
		return this.difficult;
	}

	/**
	 * Returns default level for skill (along with "getBase" and/or
	 * "getIntBase")
	 * 
	 * @return Default level for skill
	 */
	public int getModifier()
	{
		return this.defaultModifier;
	}

	/**
	 * Returns string for default level, ie: "DX-5"
	 * 
	 * @return String for default level, ie: "DX-5"
	 */
	public String getDefault()
	{

		String retstr;
		if (this.defaultModifier > 0)
		{
			retstr = "+" + String.valueOf(this.defaultModifier);
		}
		else
		{
			retstr = String.valueOf(this.defaultModifier);
		}

		retstr = this.getBase() + retstr;

		return retstr;
	}

	/**
	 * Long description for skill.
	 * 
	 * @return Long description for skill.
	 */
	public String description()
	{
		StringBuilder sb = new StringBuilder(this.nome);
		sb.append(OSProps.LINEFEED);
		sb.append(this.getType());
		sb.append(OSProps.LINEFEED);
		sb.append(this.getDifficolta());
		sb.append(OSProps.LINEFEED);
		sb.append(LibraryStrings.BASEDUPON);
		sb.append(this.getBase());
		sb.append(String.valueOf(this.defaultModifier));
		return sb.toString();
	}

	/**
	 * Brief description of skill (name, only).
	 * 
	 * @return Brief description of skill (name, only).
	 */
	@Override
	public String toString()
	{
		return nome;
	}

	/**
	 * How many points you must spend to increase an Easy Physical skill. Or,
	 * how many points the skill is valued, given level and character's DX.
	 * 
	 * @param persDex
	 *            DX of character
	 * @param desiredLevel
	 *            level to reach
	 * @return Points to spend
	 */
	public static float getCostPhysicalSkillEasy(int persDex, int desiredLevel)
	{
		float gap = 1000f;

		if (desiredLevel < (persDex - 1))
		{
			gap = 0f;
		}
		else if (desiredLevel == persDex - 1)
		{
			gap = 0.5f;
		}
		else if (desiredLevel == persDex)
		{
			gap = 1f;
		}
		else if (desiredLevel == persDex + 1)
		{
			gap = 2f;
		}
		else if (desiredLevel == persDex + 2)
		{
			gap = 4f;
		}
		else if (desiredLevel == persDex + 3)
		{
			gap = 8f;
		}
		else if (desiredLevel == persDex + 4)
		{
			gap = 16f;
		}
		else if (desiredLevel > persDex + 4)
		{
			gap = 16f + (8f * (desiredLevel - persDex - 4f));
		}

		return gap;
	}

	/**
	 * How many points you must spend to increase an Medium Physical skill. Or,
	 * how many points the skill is valued, given level and character's DX.
	 * 
	 * @param persDex
	 *            DX of character
	 * @param desiredLevel
	 *            level to reach
	 * @return Points to spend
	 */
	public static float getCostPhysicalSkillMedium(int persDex, int desiredLevel)
	{

		float gap = 1000f;

		if (desiredLevel < (persDex - 2))
		{
			gap = 0f;
		}
		else if (desiredLevel == persDex - 2)
		{
			gap = 0.5f;
		}
		else if (desiredLevel == persDex - 1)
		{
			gap = 1f;
		}
		else if (desiredLevel == persDex)
		{
			gap = 2f;
		}
		else if (desiredLevel == persDex + 1)
		{
			gap = 4f;
		}
		else if (desiredLevel == persDex + 2)
		{
			gap = 8f;
		}
		else if (desiredLevel == persDex + 3)
		{
			gap = 16f;
		}
		else if (desiredLevel == persDex + 4)
		{
			gap = 24f;
		}
		else if (desiredLevel > persDex + 4)
		{
			gap = 24f + (8f * (desiredLevel - persDex - 4f));
		}

		return gap;

	}

	/**
	 * How many points you must spend to increase a Hard Physical skill
	 * 
	 * @param persDex
	 *            DX of character
	 * @param desiredLevel
	 *            level to reach
	 * @return Points to spend
	 */
	public static float getCostPhysicalSkillHard(int persDex, int desiredLevel)
	{

		float gap = 1000f; // A large number, that's all

		if (desiredLevel < (persDex - 3))
		{
			gap = 0f;
		}
		else if (desiredLevel == persDex - 3)
		{
			gap = 0.5f;
		}
		else if (desiredLevel == persDex - 2)
		{
			gap = 1f;
		}
		else if (desiredLevel == persDex - 1)
		{
			gap = 2f;
		}
		else if (desiredLevel == persDex)
		{
			gap = 4f;
		}
		else if (desiredLevel == persDex + 1)
		{
			gap = 8f;
		}
		else if (desiredLevel == persDex + 2)
		{
			gap = 16f;
		}
		else if (desiredLevel == persDex + 3)
		{
			gap = 24f;
		}
		else if (desiredLevel == persDex + 4)
		{
			gap = 32f;
		}
		else if (desiredLevel > persDex + 4)
		{
			gap = 32f + (8f * (desiredLevel - persDex - 4f));
		}

		return gap;
	}

	/**
	 * How many points you must spend to increase an Easy Mental skill. Or, how
	 * many points the skill is valued, given level and character's IQ.
	 * 
	 * @param persIQ
	 *            IQ of character
	 * @param desiredLevel
	 *            level to reach
	 * @return Points to spend
	 */
	public static float getCostMentalSkillEasy(int persIQ, int desiredLevel)
	{
		float gap = 1000f;

		if (desiredLevel < (persIQ - 1))
		{
			gap = 0f;
		}
		else if (desiredLevel == persIQ - 1)
		{
			gap = 0.5f;
		}
		else if (desiredLevel == persIQ)
		{
			gap = 1f;
		}
		else if (desiredLevel == persIQ + 1)
		{
			gap = 2f;
		}
		else if (desiredLevel == persIQ + 2)
		{
			gap = 4f;
		}
		else if (desiredLevel == persIQ + 3)
		{
			gap = 6f;
		}
		else if (desiredLevel == persIQ + 4)
		{
			gap = 8f;
		}
		else if (desiredLevel > persIQ + 4)
		{
			gap = 10f + (2f * (desiredLevel - persIQ - 4f));
		}

		return gap;
	}

	/**
	 * How many points you must spend to increase an Medium Mental skill. Or,
	 * how many points the skill is valued, given level and character's DX.
	 * 
	 * @param persIQ
	 *            IQ of character
	 * @param desiredLevel
	 *            level to reach
	 * @return Points to spend
	 */
	public static float getCostMentalSkillMedium(int persIQ, int desiredLevel)
	{

		float gap = 1000f;

		if (desiredLevel < (persIQ - 2))
		{
			gap = 0f;
		}
		else if (desiredLevel == persIQ - 2)
		{
			gap = 0.5f;
		}
		else if (desiredLevel == persIQ - 1)
		{
			gap = 1f;
		}
		else if (desiredLevel == persIQ)
		{
			gap = 2f;
		}
		else if (desiredLevel == persIQ + 1)
		{
			gap = 4f;
		}
		else if (desiredLevel == persIQ + 2)
		{
			gap = 6f;
		}
		else if (desiredLevel == persIQ + 3)
		{
			gap = 8f;
		}
		else if (desiredLevel == persIQ + 4)
		{
			gap = 10f;
		}
		else if (desiredLevel > persIQ + 4)
		{
			gap = 10f + (2f * (desiredLevel - persIQ - 4f));
		}

		return gap;

	}

	/**
	 * How many points you must spend to increase a Hard Physical skill
	 * 
	 * @param persIQ
	 *            DX of character
	 * @param desiredLevel
	 *            level to reach
	 * @return Points to spend
	 */
	public static float getCostMentalSkillHard(int persIQ, int desiredLevel)
	{

		float gap = 1000f;

		if (desiredLevel < (persIQ - 4))
		{
			gap = 0f;
		}
		else if (desiredLevel == persIQ - 4)
		{
			gap = 0.5f;
		}
		else if (desiredLevel == persIQ - 3)
		{
			gap = 1f;
		}
		else if (desiredLevel == persIQ - 2)
		{
			gap = 2f;
		}
		else if (desiredLevel == persIQ - 1)
		{
			gap = 4f;
		}
		else if (desiredLevel == persIQ)
		{
			gap = 8f;
		}
		else if (desiredLevel == persIQ + 1)
		{
			gap = 12f;
		}
		else if (desiredLevel == persIQ + 2)
		{
			gap = 16f;
		}
		else if (desiredLevel == persIQ + 3)
		{
			gap = 20f;
		}
		else if (desiredLevel == persIQ + 4)
		{
			gap = 24f;
		}
		else if (desiredLevel > persIQ + 4)
		{
			gap = 24f + (4f * (desiredLevel - persIQ - 4f));
		}

		return gap;
	}

}