package net.littlelite.jurpe.dungeon.hexmap;

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

import net.littlelite.jurpe.dungeon.Dungeons;
import net.littlelite.jurpe.system.JurpeException;

/**
 * The HexMapFactory creates HexMap objects
 * 
 * @see net.littlelite.jurpe.dungeon.hexmap.HexMap
 */
public class HexMapFactory
{
	/**
	 * Create a new HexMap
	 * 
	 * @param container
	 *            Container panel
	 * @param d
	 *            Dungeons handle
	 * @param hmWidth
	 *            HexMap width
	 * @param hmHeight
	 *            HexMap height
	 * @return a newly created hexmap
	 */
	public static HexMap createHexMap(IPanelTemplate container, 
                                          Dungeons d, int hmWidth, int hmHeight)
	{
		HexMap hexmap = null;

		try
		{
			hexmap = new HexMap(container);
			hexmap.setSize(hmWidth, hmHeight);
			hexmap.setDungeon(d);
			hexmap.synchHexMapView();
		}
		catch (JurpeException jex)
		{
			jex.printStackTrace();
		}

		return hexmap;
	}
}