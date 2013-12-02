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

import net.littlelite.jurpe.system.resources.LibraryStrings;

/**
 * AllOutAttackType util class for type of All Out Attacks.
 * 
 * 
 */
public enum AllOutAttackType
{

	TWOATTACK((byte) 90), BONUSKILL((byte) 91), BONUSDAMG((byte) 92);

	private byte type;

	private AllOutAttackType(byte AType)
	{
		this.type = AType;
	}

        @Override
	public String toString()
	{
		String attackType = null;

		switch (this.type)
		{
			case 90:
				attackType = LibraryStrings.ALLOUT1;
				break;

			case 91:
				attackType = LibraryStrings.ALLOUT2;
				break;

			case 92:
				attackType = LibraryStrings.ALLOUT3;
				break;
		}

		return attackType;
	}
}
