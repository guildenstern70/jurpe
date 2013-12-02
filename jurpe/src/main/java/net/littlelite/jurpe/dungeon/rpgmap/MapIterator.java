package net.littlelite.jurpe.dungeon.rpgmap;

/**
 * J.U.R.P.E.
 *
 * @version@ (DungeonCrawler Package) Copyright (C) 2002-12 LittleLite Software
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.littlelite.jurpe.dungeon.crawler.RpgMapPoint;

public class MapIterator implements Iterator<Cell>
{
	private ILogicalMap map;
	private short mapWidth;
	private int totalCells;
	private int currentCellNumber;

	/**
	 * An RpgMap Iterator
	 * 
	 * @param rpgmap
	 *            LogicalMap
	 */
	public MapIterator(ILogicalMap rpgmap)
	{
		this.map = rpgmap;
		short ww = map.getWidth();
		short hh = map.getHeight();
		this.mapWidth = map.getWidth();
		this.currentCellNumber = 0;
		this.totalCells = ww * hh;
	}

	/**
	 * True if the iterator has a next object
	 * 
	 * @return boolean
	 */
	public boolean hasNext()
	{
		boolean next = false;

		if (this.currentCellNumber < this.totalCells)
		{
			next = true;
		}

		return next;
	}

	/**
	 * Get next object
	 * 
	 * @throws NoSuchElementException
	 * @return RpgMapCell
	 */
	public Cell next() throws NoSuchElementException
	{
		Cell cell;
		RpgMapPoint cellPoint;

		if (this.hasNext())
		{
			cellPoint = this.cellToPoint(this.currentCellNumber);
			cell = this.map.getCell(cellPoint);
			this.currentCellNumber++;
		}
		else
		{
			throw new NoSuchElementException();
		}

		return cell;
	}

	/**
	 * Remove is not supported by this iterator
	 * 
	 * @throws UnsupportedOperationException
	 */
	public void remove() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	private RpgMapPoint cellToPoint(int cell)
	{
		short col = (short) (cell % this.mapWidth);
		short row = (short) (cell / this.mapWidth);
		return new RpgMapPoint(col, row);
	}
}
