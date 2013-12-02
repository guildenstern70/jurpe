package net.littlelite.jurpe.dungeon.rpgmap;

/**
 J.U.R.P.E. @version@ (DungeonCrawler Package)
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

import net.littlelite.jurpe.dungeon.crawler.AbstractHexPoint;
import net.littlelite.jurpe.dungeon.crawler.RpgMapPoint;

/**
 * Interface for RPG maps. Known subclasses: Map and MapView
 * 
 * @see RpgMap
 * @see MapView
 */
public interface ILogicalMap
{
	/**
	 * Get RpgMapCell at RpgMapCoordinates XY
	 * 
	 * @param hexPoint
	 *            RpgMapPoint View relative point
	 * @return RpgMapCell Cell in RpgMap
	 */
	Cell getCell(AbstractHexPoint hexPoint);

	/**
	 * Get RpgMapCell at RpgMapCoordinates XY
	 * 
	 * @param position
	 *            RpgMapPoint View relative point
	 * @return RpgMapCell Cell in RpgMap
	 */
	boolean isInside(RpgMapPoint position);

	/**
	 * Viewport width
	 * 
	 * @return Viewport width
	 */
	short getWidth();

	/**
	 * Viewport height
	 * 
	 * @return Viewport height
	 */
	short getHeight();

	/**
	 * Get top left coordinates, relative to RpgMap, of viewport
	 * 
	 * @return top left coordinates, relative to RpgMap, of viewport
	 */
	RpgMapPoint getTopLeft();

	/**
	 * Get bottom right coordinates, relative to RpgMap, of viewport
	 * 
	 * @return bottom right coordinates, relative to RpgMap, of viewport
	 */
	RpgMapPoint getBottomRight();

	/**
	 * Iterator
	 * 
	 * @return RpgMapIterator
	 */
	MapIterator iterator();
}
