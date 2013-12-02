package net.littlelite.jurpe.combat;

/**
 * J.U.R.P.E.
 * 
 * @version@ Copyright (C) 2002-12 LittleLite Software This program is free
 *           software; you can redistribute it and/or modify it under the terms
 *           of the GNU General Public License as published by the Free Software
 *           Foundation; either version 2 of the License, or (at your option)
 *           any later version. This program is distributed in the hope that it
 *           will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *           warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *           See the GNU General Public License for more details. You should
 *           have received a copy of the GNU General Public License along with
 *           this program; if not, write to the Free Software Foundation, Inc.,
 *           59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

/**
 * DefenseType util class for type of defenses.
 * 
 * 
 */
public enum DefenseType
{

	ACTIVE_BLOCK((byte) 92), ACTIVE_DODGE((byte) 90), ACTIVE_PARRY((byte) 91);

	private boolean available;
	private byte type;

	// if character made AllOutAttack in the previous turn,
	// no Active Defense can be available.

	private DefenseType(byte defenseType)
	{
		this.type = defenseType;
		this.available = true;
	}

	/**
	 * If Active Defense is available this turn.
	 * 
	 * @return true, if Active Defenses are available this turn.
	 */
	public boolean isAvailable()
	{
		return this.available;
	}

	/**
	 * Set availability of Active Defenses.
	 * 
	 * @param avail
	 *            true, if active defenses are available this turn.
	 */
	public void setAvailable(boolean avail)
	{
		this.available = avail;
	}

	/**
	 * Description of this defense type.
	 * 
	 * @return A description of this defense type.
	 */
        @Override
	public String toString()
	{
		String defense = null;

		if (this.type == 90)
		{
			defense = "Dodge";
		}
		else if (this.type == 91)
		{
			defense = "Parry";
		}
		else if (this.type == 92)
		{
			defense = "Block";
		}

		return defense;
	}

}