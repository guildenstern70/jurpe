package net.littlelite.jurpe.dungeon.crawler;

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

import java.awt.Point;
import net.littlelite.jurpe.system.Config;


public class HexagonCalc
{

	protected int pixelX, pixelY; // pixel info from screen

	protected int adjustedPixelX, adjustedPixelY; // pixel info adjusted for

	// hexside edge slope

	protected int hexcellX, hexcellY; // hexcell

	protected int hexcellType;

	protected int hexagonX, hexagonY;

	protected int hexagonHexpartX, hexagonHexpartY;

	public HexagonCalc()
	{
		this.setPixels(0, 0);
	}

	/**
	 * Adjust info based on current pixels
	 * 
	 * @param x
	 * @param y
	 */
	public void setPixels(int x, int y)
	{
		this.pixelX = x;
		this.pixelY = y;
		this.calculateHexcellType();
		this.adjustPixels();
		this.calculateHexagon();
		this.calculateHexpart();
	}

	/**
	 * Set Hexagon parts
	 * 
	 * @param x
	 * @param y
	 */
	public void setHexagonHexparts(int x, int y)
	{
		this.hexagonHexpartX = x;
		this.hexagonHexpartY = y;
	}

	/**
	 * Hexagon center pixel coordinates
	 * 
	 * @return Hexagon center pixel coordinates
	 */
	public Point getHexagonCenter()
	{
		Point center = new Point(this.hexagonHexpartX * Config.HEXCELL_WIDTH * 3, this.hexagonHexpartY * Config.HEXCELL_HEIGHT);
		return center;
	}

	/**
	 * Get hexagon name
	 * 
	 * @return hexagon name
	 */
	public String getName()
	{
		int hexagon;
		String hexagonName;
		int x, y;
		x = this.hexagonHexpartX;
		y = this.hexagonHexpartY;
		x = (x / 2) - 1;
		y = (int) (Math.floor(y / 4)) - 1;
		// set the hexagon name
		hexagon = (int) Math.floor(x * 100 + y);
		if (x < 10)
		{
			hexagonName = "0" + hexagon;
		}
		else
		{
			hexagonName = "" + hexagon;
		}
		if (x <= 0 && y < 10)
		{
			hexagonName = "000" + y;
		}
		if (x <= 0 && y >= 10)
		{
			hexagonName = "00" + y;
		}
		if (y < 0)
		{
			hexagonName = "----";
		}

		return hexagonName;

	}

	public int getAdjustedX()
	{
		return this.adjustedPixelX;
	}

	public int getAdjustedY()
	{
		return this.adjustedPixelY;
	}

	public int getHexagonX()
	{
		return this.hexagonX;
	}

	public int getHexagonY()
	{
		return this.hexagonY;
	}

	public int getHexpartX()
	{
		return this.hexagonHexpartX;
	}

	public int getHexpartY()
	{
		return this.hexagonHexpartY;
	}

	public int getHexcellType()
	{
		return this.hexcellType;
	}

	/**
	 * Chop up map into 24 little rectangles per hexagon Pixels are referenced
	 * from the center of hexagon 0,0 the hexcell rectangle starts from the left
	 * edge which is over by 3 * HEXCELL_WIDTH and up by 2 * HEXCELL_HEIGHT
	 */
	private void calculateHexcellType()
	{
		this.pixelX = this.pixelX + (3 * Config.HEXCELL_WIDTH);
		this.pixelY = this.pixelY + (2 * Config.HEXCELL_HEIGHT);

		this.hexcellX = (int) Math.floor(this.pixelX / Config.HEXCELL_WIDTH);
		this.hexcellY = (int) Math.floor(this.pixelY / Config.HEXCELL_HEIGHT);
		this.hexcellType = (this.hexcellX % 6) + (6 * (this.hexcellY % 4));

		// shift the odd columns down by a half hexagon
		if (Math.floor(this.hexcellX / Config.HEXAGON_WIDTH) % 2 == 1)
		{
			this.hexcellType = this.hexcellType + 12;
		}

		// set type to 1 thru 24 instead of 0 thru 23
		this.hexcellType = (this.hexcellType % 24) + 1;
	}

	/**
	 * Test for hexcellType to check for rectangles that fall on the sloping
	 * edge hexside which will be hexcell types of 1, 6, 19 and 24 move the
	 * pixel to the right or left to position it in the correct hexagon. When
	 * user select a pixel that is "slightly" outside the hexagon, the pixel is
	 * corrected so that it falls inside the hexagon.
	 */
	private void adjustPixels()
	{

		int testX = this.pixelX % Config.HEXCELL_WIDTH;
		int testY = this.pixelY % Config.HEXCELL_HEIGHT;

		switch (this.hexcellType)
		{
			case 1:
				if (testX * Config.HEXCELL_HEIGHT < (Config.HEXCELL_HEIGHT - testY) * Config.HEXCELL_WIDTH)
				{
					this.adjustedPixelX = this.pixelX - Config.HEXCELL_WIDTH;
				}
				break;

			case 6:
				if (testX * Config.HEXCELL_HEIGHT > testY * Config.HEXCELL_WIDTH)
				{
					this.adjustedPixelX = this.pixelX + Config.HEXCELL_WIDTH;
				}
				break;

			case 19:
				if (testX * Config.HEXCELL_HEIGHT < testY * Config.HEXCELL_WIDTH)
				{
					this.adjustedPixelX = this.pixelX - Config.HEXCELL_WIDTH;
				}
				break;

			case 24:
				if (testX * Config.HEXCELL_HEIGHT > (Config.HEXCELL_HEIGHT - testY) * Config.HEXCELL_WIDTH)
				{
					this.adjustedPixelX = this.pixelX + Config.HEXCELL_WIDTH;
				}
				break;

			default:
				this.adjustedPixelX = this.pixelX;
				break;
		}

		this.adjustedPixelY = this.pixelY;
	}

	private void calculateHexagon()
	{

		boolean hexagonIsEven;

		// chop up the map into large rectangles

		this.hexagonX = (int) Math.floor(this.adjustedPixelX / Config.HEXAGON_WIDTH) - 1;

		if (this.hexagonX % 2 == 0)
		{
			hexagonIsEven = true;
		}
		else
		{
			hexagonIsEven = false;
		}

		this.hexagonY = this.adjustedPixelY;

		// account for shift down of even column hexagons
		if (hexagonIsEven)
		{
			this.hexagonY = this.hexagonY - Config.HEXAGON_HEIGHT / 2;
		}

		this.hexagonY = (int) Math.floor(this.hexagonY / Config.HEXAGON_HEIGHT) - 1;
	}

	private void calculateHexpart()
	{
		// calculate the hexpart for the center
		this.hexagonHexpartY = 4 * (this.hexagonY + 1);
		if ((this.hexagonX % 2) == 0)
		{
			this.hexagonHexpartY = this.hexagonHexpartY + 2;
		}
		this.hexagonHexpartX = 2 * (this.hexagonX + 1);

	}

}