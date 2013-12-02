package net.littlelite.jurpe.system.generation;

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
 * ItemValueType constants determine the rarity of an object, ie:
 * 
 * <pre>
 * switch (ivt)
 * {
 * 	case TRASH:
 * 		break;
 * 	case COMMON:
 * 		break;
 * 	case NORMAL:
 * 		break;
 * 	case AVERAGE:
 * 		break;
 * 	case MORETHANAVG:
 * 		break;
 * 	case RARE:
 * 		break;
 * 	case UNIQUE:
 * 		break;
 * }
 * </pre>
 * 
 * 
 */
public enum ItemValueType
{
	TRASH((byte) 10), COMMON((byte) 20), NORMAL((byte) 30), AVERAGE((byte) 40), MORETHANAVG((byte) 50), RARE((byte) 60), UNIQUE((byte) 70);

	/**
	 * Create a new ItemValueType of the give value
	 * 
	 * @param value
	 *            Value of item (10 to 250 where 10 is common and 250 is very
	 *            rare)
	 * @return newly created ItemValueType
	 */
	public static ItemValueType fromValue(byte value)
	{
		ItemValueType ivt = null;

		switch (value)
		{
			case 10:
				ivt = ItemValueType.TRASH;
				break;

			case 20:
				ivt = ItemValueType.COMMON;
				break;

			case 30:
				ivt = ItemValueType.NORMAL;
				break;

			case 40:
				ivt = ItemValueType.AVERAGE;
				break;

			case 50:
				ivt = ItemValueType.MORETHANAVG;
				break;

			case 60:
				ivt = ItemValueType.RARE;
				break;

			case 70:
				ivt = ItemValueType.UNIQUE;
				break;
		}

		return ivt;
	}

	/**
	 * Return the value of this item
	 * 
	 * @return Type of this item
	 */
	public byte getType()
	{
		return this.itemValue;
	}

	private byte itemValue;

	private ItemValueType(byte value)
	{
		this.itemValue = value;
	}
}
