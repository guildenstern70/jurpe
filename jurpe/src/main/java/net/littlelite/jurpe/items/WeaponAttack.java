package net.littlelite.jurpe.items;

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
import net.littlelite.jurpe.combat.DamageMode;
import net.littlelite.jurpe.combat.DamageType;
import net.littlelite.jurpe.combat.WeaponDamage;
import net.littlelite.jurpe.system.OSProps;
import net.littlelite.jurpe.system.resources.LibraryStrings;

/**
 * Utility type for Attack. An attack contains up to 3 possibile types of damage
 * (WeaponDamage): cutting, crushing, impalling. If an attack does not contain
 * one of the specified types of damage, the corresponding WeaponDamage is
 * valued to WeaponDamage.NONE (Nothing)
 */
public class WeaponAttack implements Serializable
{
	private static final long serialVersionUID = 3317L;

	private WeaponDamage cuttingDamage;
	private WeaponDamage impallingDamage;
	private WeaponDamage crushingDamage;
	private String[] availAttacks;

	/**
	 * Constructs new type for Attacks available for a weapon.
	 * 
	 * @param dTaglio
	 *            WeaponDamage for cutting mode
	 * @param dBotta
	 *            WeaponDamage for crushing mode
	 * @param dImpalamento
	 *            WeaponDamage for impaling mode
	 */
	public WeaponAttack(WeaponDamage dTaglio, WeaponDamage dBotta, WeaponDamage dImpalamento)
	{
		cuttingDamage = dTaglio;
		impallingDamage = dImpalamento;
		crushingDamage = dBotta;
		this.setAvailableAttacks();
	}

	/**
	 * Return Cutting WeaponDamage
	 * 
	 * @return Cutting WeaponDamage
	 * @see WeaponDamage
	 */
	public WeaponDamage getCuttingDamage()
	{
		return this.cuttingDamage;
	}

	/**
	 * Return Crushing WeaponDamage
	 * 
	 * @return Crushing WeaponDamage
	 * @see WeaponDamage
	 */
	public WeaponDamage getCrushingDamage()
	{
		return this.crushingDamage;
	}

	/**
	 * Return Impaling WeaponDamage
	 * 
	 * @return Impaling WeaponDamage
	 * @see WeaponDamage
	 */
	public WeaponDamage getImpalingDamage()
	{
		return this.impallingDamage;
	}

	/**
	 * Get the number of available attacks for this weapon (0-3)
	 * 
	 * @return The number of available attacks
	 */
	public int getNumAttacks()
	{
		int tipo = 0;
		if (cuttingDamage.getType() != DamageType.NONE)
		{
			tipo++;
		}
		if (impallingDamage.getType() != DamageType.NONE)
		{
			tipo++;
		}
		if (crushingDamage.getType() != DamageType.NONE)
		{
			tipo++;
		}
		return tipo;
	}

	/**
	 * Return an array of String determining a long description for each
	 * available Attack
	 * 
	 * @return Strings describing available attacks
	 */
	public String[] getAttacks()
	{
		return availAttacks;
	}

	/**
	 * If getAttacks returns 3 different types of attacks, when you pass the
	 * mode for this weapon, ie: Weapon.MODE_IMP, this method returns the
	 * position of that mode in that array.
	 * 
	 * @param weaponMode
	 *            The weapon mode as in Weapon (MODE_CRU, MODE_CUT, MODE_IMP)
	 * @return index of given weapon mode.
	 */
	public int getIndexAttacks(DamageMode weaponMode)
	{

		int index = 0;
		String attString;

		switch (weaponMode)
		{
			case CRUSHING:
				attString = LibraryStrings.CRUSHING;
				break;
			case CUTTING:
				attString = LibraryStrings.CUTTING;
				break;
			case IMPALING:
			default:
				attString = LibraryStrings.IMPALING;
				break;
		}

		for (int j = 0; j < availAttacks.length; j++)
		{
			if (this.availAttacks[j].startsWith(attString.toUpperCase()))
			{
				index = j;
			}
		}

		return index;

	}

	/**
	 * Long description of all available attack for this weapon.
	 * 
	 * @return Long description of all available attack for this weapon.
	 */
	@Override
	public String toString()
	{

		StringBuilder att = new StringBuilder();

		if (cuttingDamage.getType() != DamageType.NONE)
		{
			att.append(LibraryStrings.CUTTING + ": ");
			att.append(cuttingDamage);
		}

		if (impallingDamage.getType() != DamageType.NONE)
		{
			att.append(OSProps.LINEFEED);
			att.append(LibraryStrings.IMPALING + ": ");
			att.append(impallingDamage);
		}

		if (crushingDamage.getType() != DamageType.NONE)
		{
			att.append(OSProps.LINEFEED);
			att.append(LibraryStrings.CRUSHING + ": ");
			att.append(crushingDamage);
		}

		return att.toString();
	}

	private final void setAvailableAttacks()
	{
		int numAtt = this.getNumAttacks();
		availAttacks = new String[numAtt];
		int cont = -1;

		if (cuttingDamage.getType() != DamageType.NONE)
		{
			availAttacks[++cont] = LibraryStrings.CUTTING.toUpperCase() + ": " + cuttingDamage.toString();
		}
		if (impallingDamage.getType() != DamageType.NONE)
		{
			availAttacks[++cont] = LibraryStrings.IMPALING.toUpperCase() + ": " + impallingDamage.toString();
		}
		if (crushingDamage.getType() != DamageType.NONE)
		{
			availAttacks[++cont] = LibraryStrings.CRUSHING.toUpperCase() + ": " + crushingDamage.toString();
		}
	}
}
