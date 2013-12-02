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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.AbstractList;

import net.littlelite.jurpe.dungeon.Avatar;
import net.littlelite.jurpe.dungeon.Dungeons;
import net.littlelite.jurpe.dungeon.Level;
import net.littlelite.jurpe.dungeon.Placeholder;
import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.crawler.HexMapPoint;
import net.littlelite.jurpe.dungeon.crawler.Hexagon;
import net.littlelite.jurpe.dungeon.crawler.HexagonCalc;
import net.littlelite.jurpe.dungeon.crawler.RpgMapPoint;
import net.littlelite.jurpe.dungeon.crawler.ViewMapPoint;
import net.littlelite.jurpe.dungeon.rpgmap.Drawer;
import net.littlelite.jurpe.dungeon.rpgmap.MapView;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.Config;

/**
 * Drawing inside the HexMap
 */
public class HexMapDrawing
{
	private HexMapGeometry parent;
	private HexMap hexmap;

	/**
	 * Constructor
	 * 
	 * @param geometry
	 *            Handle to Geometry
	 */
	public HexMapDrawing(HexMapGeometry geometry)
	{
		this.parent = geometry;
		this.hexmap = geometry.getHexMap();
	}

	/**
	 * Fill an hexagon. Use this function if you want to draw many hexagons, so
	 * that you call Graphics.dispose once.
	 * 
	 * @param g
	 *            Graphics object
	 * @param p
	 *            Center point of hexagon to fill
	 * @param c
	 *            Hexagon color.
	 */
	public void fillHexagon(Graphics g, Point p, Color c)
	{
		g.setColor(c);
		Hexagon hex = new Hexagon(p, Config.HEXAGON_WIDTH, Config.HEXAGON_HEIGHT);
		g.fillPolygon(hex);
	}

	/**
	 * Fill an hexagon with wall color, that is, erase the hexagon.
	 * 
	 * @param g
	 *            Graphics handler
	 * @param p
	 *            Point to fill
	 */
	public void eraseHexagon(Graphics g, Point p)
	{
		this.fillHexagon(g, p, Config.WALL_COLOR);
	}

	/**
	 * Draw a maze inside hexes
	 * 
	 * @param m
	 *            DungeonGenerator utility
	 */
	public void drawMaze(Graphics g, Avatar av) throws JurpeException
	{
		Drawer rmapdrawer = this.hexmap.getHexMapView().getDrawer();
		if (rmapdrawer == null)
		{
			throw new JurpeException("HexMapDrawing.drawMaze with Drawer not initialized.");
		}

		if (av == null)
		{
			throw new JurpeException("HexMapDrawing.drawMaze with Avatar not initialized.");
		}

		if (g != null)
		{
			rmapdrawer.drawMap(g, av);
		}
	}

	/**
	 * Draw the placeholders in this hexmap
	 * 
	 * @param g
	 */
	public void drawPlaceholders(Graphics g, Dungeons dungeon) throws JurpeException
	{
		if (dungeon != null)
		{
			Avatar av = dungeon.getAvatar();
			Level map = dungeon.getCurrentLevel();
			if (map != null)
			{
				this.drawPlaceholders(g, av, map.placeHolders());
			}
		}
	}

	/**
	 * Draw placeholders on hexmap. Placeholders are: avatar, monsters, other
	 * npc's.
	 * 
	 * @param placeholders
	 */
	public void drawPlaceholders(Graphics g, Avatar av, AbstractList<Placeholder> placeholders) throws JurpeException
	{
		RpgMapPoint avatarLocation = av.getPlaceholder().getPosition();
		RpgMapPoint phPosition = null;
		int radius;

		// Draw placeholders
		for (Placeholder ph : placeholders)
		{
			phPosition = ph.getPosition();
			radius = phPosition.getDistanceFrom(avatarLocation);
			if (radius <= 3)
			{
				this.drawPlaceholder(ph, phPosition, ph.getForegroundColor(), g);
			}
			ph.setLastPosition(phPosition);
		}
	}

	/**
	 * Draw placeholder on map
	 * 
	 * @param ph
	 *            PlaceHolder to draw
	 * @param g
	 *            Graphics device
	 */
	public void drawPlaceholder(Placeholder ph, RpgMapPoint position, Color phColor, Graphics g) throws JurpeException
	{
		Point phPoint = null;
		if (position != null)
		{
			phPoint = this.getHexMapCoordinates(position);
			if (!this.hexmap.getHexMapView().isOnTheEdge(position))
			{
				switch (ph.getType())
				{
					case PLAYER:
						this.drawPoint(g, phPoint, phColor, 6);
						break;

					case MONSTER:
						this.drawPoint(g, phPoint, phColor, 4);
						break;

					case ITEM:
						this.drawDebris(g, phPoint, phColor);
						break;

					case GUNSIGHT:
						this.drawGunsight(g, phPoint, phColor);
						break;

					default:
						break;
				}
			}

		}
	}

	/**
	 * Draw a corridor between the hexagon having center in from, toward
	 * direction
	 * 
	 * @param from
	 * @param direction
	 */
	public void drawCorridor(Graphics g, RpgMapPoint from, Direction direction) throws JurpeException
	{
		g.setColor(Config.MAZE_BACKGROUND);
		Dungeons game = this.hexmap.getDungeon();
		RpgMapPoint to = game.getCurrentMap().moveTo(from, direction);

		// draw FROM triangle
		this.drawTriangle(g, from.toPoint(), direction);
		// draw TO triangle
		this.drawTriangle(g, to.toPoint(), Direction.getInverse(direction));
	}

	/**
	 * Draw a corridor between the hexagon having center in from, toward
	 * direction
	 * 
	 * @param g
	 *            Graphics handle
	 * @param center
	 *            Center of the hexagon
	 * @param direction
	 *            Direction in which corridor lies
	 * @param open
	 *            If true, draws an open corridor. Else, a closed one.
	 */
	public void drawDoor(Graphics g, Point center, Direction direction, boolean open)
	{

		Hexagon hex = new Hexagon(center, Config.HEXAGON_WIDTH, Config.HEXAGON_HEIGHT);

		int x = hex.getCenter().x;
		int y = hex.getCenter().y;

		int[] vertex = this.getTriangleVertex(x, y, direction);

		if (open)
		{
			g.setColor(Config.WALL_COLOR);
			g.drawLine(vertex[0], vertex[2], vertex[1], vertex[3]);

		}
		else
		{
			g.setColor(Config.DOOR_COLOR);
			g.drawLine(vertex[0], vertex[2], vertex[1], vertex[3]);
		}
	}

	public void drawPoint(Graphics g, Point p, Color color, int pointWidth)
	{
		// Draw new point
		g.setColor(color);
		g.fillOval(p.x - pointWidth / 2, p.y - pointWidth / 2, pointWidth, pointWidth);
	}

	private void drawGunsight(Graphics g, Point p, Color c)
	{
		int w = Config.HEXAGON_WIDTH - 4;
		int h = Config.HEXAGON_HEIGHT - 4;
		this.drawHexagon(g, p, w, h, c);
	}

	/**
	 * Draw a triangle in P coordinates (where P is in hexagon coordinates) and
	 * clears old point. This method is used to draw placeholders.
	 * 
	 * @param g
	 *            Graphics device
	 * @param p
	 *            Point to draw
	 * @param lastPoint
	 *            Point to delete (if not == p)
	 * @param cFore
	 *            Foreground color
	 * @param cBack
	 *            Background color
	 */
	public void drawTriangleAndClear(Graphics g, Point p, Point lastPoint, Color cFore, Color cBack)
	{

		int[] trX =
		{ p.x, p.x - 3, p.x + 3 };
		int[] trY =
		{ p.y - 3, p.y + 3, p.y + 3 };

		// Clear old point if it differs from p
		if ((lastPoint != null) && (p != lastPoint))
		{
			int[] lastTrX =
			{ lastPoint.x, lastPoint.x - 3, lastPoint.x + 3 };
			int[] lastTrY =
			{ lastPoint.y - 3, lastPoint.y + 3, lastPoint.y + 3 };

			// Clear old point
			g.setColor(cBack);
			g.fillPolygon(lastTrX, lastTrY, 3);
		}

		// Draw new point
		g.setColor(cFore);
		g.fillPolygon(trX, trY, 3);
	}

	/**
	 * Draw some debris in P coordinates (where P is in hexagon coordinates) and
	 * clears old point. This method is used to draw placeholders.
	 * 
	 * @param g
	 *            Graphics device
	 * @param p
	 *            Point to draw
	 * @param lastPoint
	 *            Point to delete (if not == p)
	 * @param cFore
	 *            Foreground color
	 * @param cBack
	 *            Background color
	 */
	public void drawDebris(Graphics g, Point p, Color color)
	{
		int[] trX =
		{ p.x - 1, p.x - 4, p.x + 2, p.x - 1, p.x - 1 };
		int[] trY =
		{ p.y + 2, p.y + 3, p.y + 3, p.y, p.y + 3 };

		g.setColor(color);

		for (int j = 0; j < trX.length; j++)
		{
			g.drawOval(trX[j], trY[j], 2, 2);
		}
	}

	/**
	 * Draw Hexagon
	 * 
	 * @param g
	 *            Graphics object
	 * @param p
	 *            Coordinates in RpgMap coordinates
	 * @param width
	 *            Hexagon width
	 * @param height
	 *            Hexagon height
	 * @param cFore
	 *            Foreground color
	 */
	public void drawHexagon(Graphics g, Point p, int width, int height, Color cFore)
	{
		g.setColor(cFore);
		g.drawPolygon(new Hexagon(p.x, p.y, width, height));
	}

	/**
	 * Draw Hexagon
	 * 
	 * @param g
	 *            Graphics object
	 * @param p
	 *            Coordinates in RpgMap coordinates
	 * @param cFore
	 *            Foreground color
	 */
	public void drawHexagon(Graphics g, Point p, Color cFore)
	{
		this.drawHexagon(g, p, Config.HEXAGON_WIDTH, Config.HEXAGON_HEIGHT, cFore);
	}

	/**
	 * Draw a triangle, starting from center, toward direction.
	 * 
	 * @param g
	 *            Graphics object
	 * @param center
	 *            Point center
	 * @param dir
	 *            Direction dir
	 */
	private void drawTriangle(Graphics g, Point center, Direction dir)
	{
		int[] x = new int[3];
		int[] y = new int[3]; // Triangle vertexes

		HexagonCalc c = this.parent.getHexagonCalc(center);
		Point hexagonCenter = c.getHexagonCenter();

		x[0] = hexagonCenter.x;
		y[0] = hexagonCenter.y;

		int vertex[] = this.getTriangleVertex(x[0], y[0], dir);

		x[1] = vertex[0];
		x[2] = vertex[1];
		y[1] = vertex[2];
		y[2] = vertex[3];

		g.fillPolygon(x, y, 3);
	}

	/**
	 * Calculates hexagon vertex in d Direction
	 * 
	 * @param x
	 *            center of hexagon (x) [pixel]
	 * @param y
	 *            center of hexagon (y) [pixel]
	 * @param dir
	 *            direction
	 * @return int[] Array of vertex: [0][1][2][3] = x1,x2,y1,y2
	 */
	private int[] getTriangleVertex(int x, int y, Direction dir)
	{
		int[] v = new int[4];

		switch (dir)
		{
			case NORTH:
				v[0] = x + Config.HEXAGON_WIDTH / 3;
				v[1] = x - Config.HEXAGON_WIDTH / 3;
				v[2] = y - Config.HEXAGON_HEIGHT / 2;
				v[3] = y - Config.HEXAGON_HEIGHT / 2;
				break;

			case SOUTH:
				v[0] = x + Config.HEXAGON_WIDTH / 3;
				v[1] = x - Config.HEXAGON_WIDTH / 3;
				v[2] = y + Config.HEXAGON_HEIGHT / 2;
				v[3] = y + Config.HEXAGON_HEIGHT / 2;
				break;

			case NORTHEAST:
				v[0] = x + Config.HEXAGON_WIDTH / 3;
				v[1] = x + Config.HEXAGON_WIDTH * 2 / 3;
				v[2] = y - Config.HEXAGON_HEIGHT / 2;
				v[3] = y;
				break;

			case NORTHWEST:
				v[0] = x - Config.HEXAGON_WIDTH * 2 / 3;
				v[1] = x - Config.HEXAGON_WIDTH / 3;
				v[2] = y;
				v[3] = y - Config.HEXAGON_HEIGHT / 2;
				break;

			case SOUTHEAST:
				v[0] = x + Config.HEXAGON_WIDTH / 3;
				v[1] = x + Config.HEXAGON_WIDTH * 2 / 3;
				v[2] = y + Config.HEXAGON_HEIGHT / 2;
				v[3] = y;
				break;

			case SOUTHWEST:
				v[0] = x - Config.HEXAGON_WIDTH / 3;
				v[1] = x - Config.HEXAGON_WIDTH * 2 / 3;
				v[2] = y + Config.HEXAGON_HEIGHT / 2;
				v[3] = y;
				break;
		}

		return v;

	}

	private Point getHexMapCoordinates(RpgMapPoint p) throws JurpeException
	{
		MapView view = this.hexmap.getHexMapView().getMapView();
		ViewMapPoint viewP = view.getViewPosition(p);
		HexMapPoint center = this.parent.getPhysicalCoordinatesFromLogical(viewP);
		return center.toPoint();
	}

}
