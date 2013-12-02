package net.littlelite.jurpe.system;

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

import net.littlelite.jurpe.characters.PC;
import net.littlelite.jurpe.characters.PrimaryStats;
import net.littlelite.jurpe.system.resources.LibraryStrings;

/**
 * Turn effect is a modifier for an attribute of a character that has the
 * duration of n turns. A turn effect is appliable to a PC.
 * 
 * 
 */
public class TurnEffect
{
	private PC affectedPC; // affected PC

	private short nrTurns; // total number of turns the effect is valid

	private short pastTurns; // number of turns in which effect has been
	// valid

	private EffectType effectType; // type of effect (ie: HT modifier)

	private int modifier; // modifier (ie: 2 is HT+2)

	/**
	 * Contruct a new Turn Effect
	 * 
	 * @param affected
	 *            affected PC
	 * @param et
	 *            type of effect (modify which attribute, ie: DX)
	 * @param mod
	 *            modifier (ie: 2 is HT+2)
	 * @param turns
	 *            total number of turns the effect is valid
	 */
	public TurnEffect(PC affected, EffectType et, int mod, short turns)
	{
		this.affectedPC = affected;
		this.nrTurns = turns;
		this.effectType = et;
		this.modifier = mod;
		this.pastTurns = 0;
	}

	/**
	 * Get Effect type of this Turn Effect
	 * 
	 * @return EffectType of this effect
	 */
	public EffectType getEffectType()
	{
		return this.effectType;
	}

	/**
	 * Return the numeric modifier for this effect
	 * 
	 * @return numeric modifier for this effect
	 */
	public int getModifier()
	{
		return this.modifier;
	}

	/**
	 * If this turn effect is still in action (valid).
	 * 
	 * @return true if this turn effect is valid
	 */
	public boolean isValid()
	{
		if (this.pastTurns == this.nrTurns)
		{
			return false;
		}

		return true;
	}

	/**
	 * Apply turn effect to affected PC
	 */
	public void apply()
	{
		// if it's not been applied before: (apply effect only first time)
		if (this.pastTurns == 0)
		{
			PrimaryStats ps = this.affectedPC.getPrimaryStats();

			if (this.effectType.equals(EffectType.DX_MODIFIER))
			{
				ps.setDX(ps.getDX() + this.modifier);
			}
			else if (this.effectType.equals(EffectType.HT_MODIFIER))
			{
				this.affectedPC.addToCurrentHP(this.modifier);
			}
			else if (this.effectType.equals(EffectType.IQ_MODIFIER))
			{
				ps.setIQ(ps.getIQ() + this.modifier);
			}
			else if (this.effectType.equals(EffectType.ST_MODIFIER))
			{
				ps.setST(ps.getST() + this.modifier);
			}

		}
		this.pastTurns++;
	}

	/**
	 * Remove turn effect to affected PC
	 */
	public void remove()
	{
		PrimaryStats ps = this.affectedPC.getPrimaryStats();

		if (this.effectType.equals(EffectType.DX_MODIFIER))
		{
			ps.setDX(ps.getDX() - this.modifier);
		}
		else if (this.effectType.equals(EffectType.HT_MODIFIER))
		{
			this.affectedPC.addToCurrentHP(-this.modifier);
		}
		else if (this.effectType.equals(EffectType.IQ_MODIFIER))
		{
			ps.setIQ(ps.getIQ() - this.modifier);
		}
		else if (this.effectType.equals(EffectType.ST_MODIFIER))
		{
			ps.setDX(ps.getDX() - this.modifier);
		}
	}

	/**
	 * Long description of this turn effect
	 * 
	 * @return description of turn effect
	 */
        @Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(this.effectToString());
		sb.append(" for ");
		sb.append(this.nrTurns);
		sb.append(" turns. ");
		sb.append("(Passed ");
		sb.append(this.pastTurns);
		sb.append(")");
		return sb.toString();
	}

	/**
	 * Get PC affected by this turn effect
	 * 
	 * @return PC affected
	 */
	public PC getAffectedPC()
	{
		return this.affectedPC;
	}

	private String effectToString()
	{

		String effect = null;
		String mod = String.valueOf(this.modifier);

		if (this.effectType.equals(EffectType.DX_MODIFIER))
		{
			effect = LibraryStrings.DXEIYMDF;
		}
		else if (this.effectType.equals(EffectType.HT_MODIFIER))
		{
			effect = LibraryStrings.HATXOIIR;
		}
		else if (this.effectType.equals(EffectType.IQ_MODIFIER))
		{
			effect = LibraryStrings.ITLIECWO;
		}
		else if (this.effectType.equals(EffectType.ST_MODIFIER))
		{
			effect = LibraryStrings.SRNTQOII;
		}
		else if (this.effectType.equals(EffectType.ATTACK_MODIFIER))
		{
			effect = LibraryStrings.ATCXOIIR;
		}

		effect += mod;
		effect += ") ";

		return effect;

	}

}