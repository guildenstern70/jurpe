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

package net.littlelite.jurpe.dungeon;

import java.io.Serializable;
import net.littlelite.jurpe.items.AbstractItem;

/**
 * A dungeon item is a AbstractItem displayable in the dungeon.
 */
public class DungeonItem implements Serializable, IDungeonPawn
{

	private static final long serialVersionUID = 3317L;

	private AbstractItem dungeonItem;
	private Placeholder itemPlaceHolder;

	/**
	 * A dungeon item is the composition of a Basic Item with its representation
	 * on a RpgMap, which is the PlaceHolder
	 * 
	 * @param item
	 *            Item
	 * @param holder
	 *            Representation of item in the RpgMap
	 */
	public DungeonItem(AbstractItem item, Placeholder holder)
	{
		this.dungeonItem = item;
		this.itemPlaceHolder = holder;
	}

	/**
	 * Return the representation of item in the RpgMap
	 * 
	 * @return Representation of item in the RpgMap
	 */
	public Placeholder getPlaceholder()
	{
		return this.itemPlaceHolder;
	}

	/**
	 * Basic Item type
	 * 
	 * @return The basic item of this dungeon item.
	 */
	public AbstractItem item()
	{
		return this.dungeonItem;
	}

	/**
	 * To String
	 * 
	 * @return String
	 */
        @Override
	public String toString()
	{
		return this.dungeonItem.toString() + " @ " + this.itemPlaceHolder.getCell().toString();
	}

}
