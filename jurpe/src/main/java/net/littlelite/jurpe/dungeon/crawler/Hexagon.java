package net.littlelite.jurpe.dungeon.crawler;

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

import java.awt.Point;
import java.awt.Polygon;
import net.littlelite.jurpe.system.Config;

import net.littlelite.jurpe.system.JurpeException;

/**
 * A hexagon.
 * 
 * @author Antti Kerminen, Alessio Saltarin
 */
public class Hexagon extends Polygon
{

	private static final long serialVersionUID = 3317L;
	private Point center; // center of this hexagon
	private int width; // width of this hexagon
	private int height; // radius of this hexagon

	/**
	 * Create a new hexagon.
	 * 
	 * @param x
	 *            the x coordinate of the center point.
	 * @param y
	 *            the y coordinate of the center point.
	 * @param w
	 *            width of the hexagon.
	 * @param h
	 *            height of the hexagon.
	 */
	public Hexagon(int x, int y, int w, int h)
	{
		this(new Point(x, y), w, h);
	}

	/**
	 * Create a new hexagon.
	 * 
	 * @param center
	 *            the center point.
	 * @param w
	 *            width of the hexagon.
	 * @param h
	 *            height of the hexagon.
	 */
	public Hexagon(Point center, int w, int h)
	{
		this.center = center;
		this.height = h;
		this.width = w;
		addPoints();
	}

	/**
	 * @return
	 */
	public Point getCenter()
	{
		return this.center;
	}

	/**
	 * @param border
	 */
	private void addPoint(int border)
	{
		int sixthW = this.width / 6;
		int halfH = this.height / 2;

		addPoint(center.x - 2 * sixthW + border, center.y + halfH - border); // 0
		addPoint(center.x + 2 * sixthW - border, center.y + halfH - border); // 1
		addPoint(center.x + 4 * sixthW - border, center.y); // 2
		addPoint(center.x + 2 * sixthW - border, center.y - halfH + border); // 3
		addPoint(center.x - 2 * sixthW + border, center.y - halfH + border); // 4
		addPoint(center.x - 4 * sixthW + border, center.y); // 5
		addPoint(center.x - 2 * sixthW + border, center.y + halfH - border); // 0
	}

	/**
	 * Get the coordinates of 1 side of hexagon
	 * 
	 * @param dir
	 *            Direction from the center
	 * @return Coordinates of line in the form (x1,y1-x2,y2)
	 */
	public int[] getSide(Direction dir)
	{
		int[] sidePoint = new int[4];
		short a = 0, b = 0;

		if (dir == Direction.NORTH)
		{
			a = 3;
			b = 4;
		}
		else if (dir == Direction.SOUTH)
		{
			a = 0;
			b = 1;
		}
		else if (dir == Direction.NORTHEAST)
		{
			a = 2;
			b = 3;
		}
		else if (dir == Direction.NORTHWEST)
		{
			a = 4;
			b = 5;
		}
		else if (dir == Direction.SOUTHEAST)
		{
			a = 1;
			b = 2;
		}
		else if (dir == Direction.SOUTHWEST)
		{
			a = 0;
			b = 5;
		}

		sidePoint[0] = this.xpoints[a];
		sidePoint[1] = this.ypoints[a];
		sidePoint[2] = this.xpoints[b];
		sidePoint[3] = this.ypoints[b];

		return sidePoint;
	}

	/**
	 * Add points to the polygon.
	 */
	private void addPoints()
	{
		this.addPoint(0);
	}

	/**
	 * Get the height.
	 */
	public double height()
	{
		return this.height;
	}

	/**
	 * Get the width.
	 */
	public int width()
	{
		return this.width;
	}

	/**
	 * Get radius
	 * 
	 * @return radius
	 */
	public double radius()
	{
		return Math.sqrt((this.height / 2) * (this.height / 2) + (this.width / 6) * (this.width / 6));
	}

	/**
	 * Return to string pepresentation of this object.
	 */
        @Override
	public String toString()
	{
		return center + " radius: " + this.radius() + "\n" + super.toString();
	}

	public static void main(String[] args)
	{
		Direction d = null;
		Hexagon hex = new Hexagon(48, 12, Config.HEXAGON_WIDTH, Config.HEXAGON_HEIGHT);

		for (short j = 0; j < 6; j++)
		{
			try
			{
				d = Direction.fromValue(j);
			}
			catch (JurpeException jex)
			{
				System.err.println(jex.getMessage());
			}
			System.out.print("Side " + d + ": >");
			int[] p = hex.getSide(d);
			System.out.println("[" + p[0] + "," + p[1] + "][" + p[2] + "," + p[3] + "]");
		}
	}

}