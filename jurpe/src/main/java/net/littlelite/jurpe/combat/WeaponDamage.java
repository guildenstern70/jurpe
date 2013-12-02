package net.littlelite.jurpe.combat;

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

import net.littlelite.jurpe.system.resources.LibraryStrings;

/**
 * This class describe the damage done by a weapon. The damage is decribed by a
 * damage type (SWING, THRUST...) and a modifier
 * 
 */
public class WeaponDamage implements Serializable
{

	private static final long serialVersionUID = 33921;

	private DamageType type;
	private boolean valid;
	private int modifier;

	/**
	 * Constructor
	 * 
	 * @param damageType
	 *            Either DamageType.SWING or DamageType.THRUST
	 * @param mod
	 *            modifier value for this attack type
	 */
	public WeaponDamage(DamageType damageType, int mod)
	{
		this.type = damageType;

		if (damageType != DamageType.SWING && damageType != DamageType.THRUST)
		{
			this.type = DamageType.NONE;
			this.valid = false;
			this.modifier = 0;
		}
		else
		{
			this.type = damageType;
			this.modifier = mod;
		}
	}

	/**
	 * Constructor. This constructor is used when reading values from XML files
	 * containing weapons description. A typical weapon can be: KNIFE(2,3) This
	 * means: Knife makes 2 damages when in swing mode, and 3 damages when in
	 * thrust mode. The XML value for "non applicable" is -111.
	 * 
	 * @param tdSW
	 *            Damage for SWING mode (DamageType.INVALID if this is not a
	 *            Swing attack)
	 * @param tdTH
	 *            Damage for THRUST mode (DamageType.INVALID if this is not a
	 *            Thrust attack)
	 */
	public WeaponDamage(int tdSW, int tdTH)
	{

		if (tdSW != -111)
		{
			this.type = DamageType.SWING;
			this.modifier = tdSW;
			this.valid = true;
		}
		else if (tdTH != -111)
		{
			this.type = DamageType.THRUST;
			this.modifier = tdTH;
			this.valid = true;
		}
		else
		{
			this.valid = false;
			this.type = DamageType.NONE;
			this.modifier = 0;
		}

	}

	/**
	 * Get the damage type
	 * 
	 * @return Damage type
	 */
	public DamageType getType()
	{
		return this.type;
	}

	/**
	 * Get modifier for this DamageType, ie: in SWING+3 modifier is 3.
	 * 
	 * @return modifier Modifier for this DamageType
	 */
	public int getModifier()
	{
		return this.modifier;
	}

	/**
	 * Set modifier for this DamageType, ie: in SWING+3 modifier is 3.
	 * 
	 * @param modifier
	 *            Modifier for this DamageType
	 */
	public void setModifier(int modifier)
	{
		this.modifier = modifier;
	}

	/**
	 * If this DamageType is valid for the weapon
	 */
	public boolean isValid()
	{
		return this.valid;
	}

	/**
	 * Return description of weapon.
	 * 
	 * @return weapon description
	 */
	@Override
	public String toString()
	{

		StringBuilder out = new StringBuilder(LibraryStrings.SH_SWING);

		if (this.type == DamageType.THRUST)
		{
			out = new StringBuilder(LibraryStrings.SH_THRUST);
		}
		else if (this.type == DamageType.NONE)
		{
			out = new StringBuilder("?");
		}
		if (this.modifier > 0)
		{
			out.append("+");
			out.append(this.modifier);
		}
		else if (this.modifier < 0)
		{
			out.append(this.modifier);
		}

		return out.toString();
	}

}
