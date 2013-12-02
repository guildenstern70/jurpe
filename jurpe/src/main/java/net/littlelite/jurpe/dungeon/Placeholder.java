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

package net.littlelite.jurpe.dungeon;

import java.awt.Color;
import java.io.Serializable;
import java.util.Random;

import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.crawler.RpgMapPoint;
import net.littlelite.jurpe.dungeon.hexmap.HexMap;
import net.littlelite.jurpe.dungeon.hexmap.HexMapView;
import net.littlelite.jurpe.dungeon.rpgmap.Cell;
import net.littlelite.jurpe.dungeon.rpgmap.RpgMap;
import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.JurpeException;

/**
 * A placeholder is the base class for every PC or NPC displayed on a map. The
 * placeholder retains the coordinates, the color and the name of each PC and
 * NPC shown on a HexDungeon
 * 
 * @see net.littlelite.jurpe.dungeon.hexmap.HexDungeon
 */
public class Placeholder implements Serializable
{
	private static final long serialVersionUID = 3317L;

	protected String name;
	protected PlaceholderType type;
	protected RpgMapPoint lastPosition;
	protected RpgMapPoint position; // position of placeholder in the current
	// RpgMap
	protected Color foreGround;
	protected Color backGround;
	protected Level level;

	/**
	 * Constructor
	 * 
	 * @param level
	 *            Dungeon level
	 * @param phType
	 *            Type of this placeholder
	 * @param name
	 *            Name of this placeholder
	 */
	public Placeholder(Level level, PlaceholderType phType, String name)
	{
		this.level = level;
		this.type = phType;
		this.name = name;
		this.position = null;
	}

	/**
	 * Get the name of this placeholder
	 * 
	 * @return Name of placeholder
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Return the type of this placeholder, what this placeholder stands for
	 * (monster, player, money...)
	 * 
	 * @return The type of this placeholder
	 */
	public PlaceholderType getType()
	{
		return this.type;
	}

	/**
	 * Get Placeholder position in RpgMap coordinates
	 * 
	 * @return Placeholder position in RpgMap coordinates
	 */
	public RpgMapPoint getPosition()
	{
		return this.position;
	}

	/**
	 * Get Placeholder last position in RpgMap coordinates. Last position is the
	 * position of the placeholder before the last movement.
	 * 
	 * @return last position in RpgMap coordinates
	 */
	public RpgMapPoint getLastPosition()
	{
		return this.lastPosition;
	}

	/**
	 * Background color
	 * 
	 * @return Background color
	 */
	public Color getBackgroundColor()
	{
		return this.backGround;
	}

	/**
	 * Foreground color
	 * 
	 * @return Foreground color
	 */
	public Color getForegroundColor()
	{
		return this.foreGround;
	}

	/**
	 * Set Placeholder last position in RpgMap coordinates. Last position is the
	 * position of the placeholder before the last movement.
	 * 
	 * @param rpgPoint
	 *            position in RpgMap coordinates
	 */
	public void setLastPosition(RpgMapPoint rpgPoint)
	{
		this.lastPosition = rpgPoint;
	}

	/**
	 * Set background and foreground colors
	 * 
	 * @param foreground
	 *            Foreground color
	 * @param background
	 *            Background color
	 */
	public void setColors(Color foreground, Color background)
	{
		this.foreGround = foreground;
		this.backGround = background;
	}

	/**
	 * Set foreground color
	 * 
	 * @param color
	 *            Foreground color
	 */
	public void setForegroundColor(Color color)
	{
		this.foreGround = color;
	}

	/**
	 * Set background color
	 * 
	 * @param c
	 *            Background color
	 */
	public void setBackgroundColor(Color c)
	{
		this.backGround = c;
	}

	/**
	 * Get RpgMapCell in which Placeholder is
	 * 
	 * @return RpgMapCell in which Placeholder is
	 */
	public Cell getCell()
	{
		RpgMap map = this.getCurrentMap();
		return map.getCell(this.position);
	}

	/**
	 * Set RpgMapCell in which Placeholder is
	 * 
	 * @param c
	 *            Cell in which this placeholder is
	 */
	public void setCell(Cell c)
	{
		this.position = c.getCoordinates();
	}

	/**
	 * Set Placeholder Position. The placeholders positions are always logical.
	 * The positions on the hexmap is drawn only by calling Hexmap.refresh()
	 * method. The refresh method reads the placeholder positions and then draw
	 * them accordingly.
	 * 
	 * @param rpgPoint
	 *            coordinates of RpgMap
	 * @throws JurpeException
	 */
	public void setPosition(RpgMapPoint rpgPoint) throws JurpeException
	{
		if (this.getCurrentMap().isInside(rpgPoint))
		{
			this.position = rpgPoint;
		}
		else
		{
			throw new JurpeException("PlaceHolder is outside map");
		}
	}

	/**
	 * Move placeholder
	 * 
	 * @param direction
	 *            Direction to move placeholder
	 * @throws JurpeException
	 */
	public void move(Direction direction) throws JurpeException
	{
		Cell actualCell = this.getCell();
		if (actualCell.isSpecial())
		{
			this.backGround = actualCell.getLocation().getColor();
		}
		else
		{
			this.backGround = Config.MAZE_BACKGROUND;
		}
		this.setPosition(this.getCurrentMap().moveTo(this.position, direction));
	}

	/**
	 * Set a random position for this placeholder. The position will be chosen
	 * among free cells (cells that are not walls or specials)
	 * 
	 * @param rnd
	 *            Random Seed
	 */
	public void setRandomPosition()
	{
		this.setDefaultInitialPosition();
	}

	/**
	 * Describes this placeholder
	 * 
	 * @return Placeholder description
	 */
        @Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("Placeholder for ");
		sb.append(this.name);
		sb.append(" in position ");
		sb.append(this.position);
		return sb.toString();
	}

	/**
	 * Set centered position for this placeholder. The position will be chosen
	 * among free cells (cells that are not walls or specials)
	 * 
	 * @throws JurpeException
	 */
	public void setCenteredPosition() throws JurpeException
	{
		RpgMap currentMap = this.getCurrentMap();
		RpgMapPoint rpgPoint = new RpgMapPoint((short) (currentMap.getWidth() / 2), (short) (currentMap.getHeight() / 2));
		this.setPosition(rpgPoint);
	}

	/**
	 * Set initial random position (displayable in current HexMap view)
	 * 
	 * @param rnd
	 *            Random
	 * @param hexmap
	 *            HexMap
	 */
	protected void setDisplayableInitialPosition(Random rnd, HexMap hexmap)
	{
		HexMapView hmv = new HexMapView(hexmap, this.getCurrentMap());
		Cell initial = hmv.getDisplayableEmptyRandomCell(rnd, true);
		this.position = initial.getCoordinates();
	}

	/**
	 * Set initial random position
	 * 
	 * @param rnd
	 *            Random Seed
	 */
	public void setDefaultInitialPosition()
	{
		RpgMap map = this.getCurrentMap();
		Cell initial = map.getEmptyRandomCell(false);
		if (initial != null)
		{
			this.position = initial.getCoordinates();
		}
	}

	/**
	 * Get placeholder's level
	 * 
	 * @return Level
	 */
	public Level getLevel()
	{
		return this.level;
	}

	/**
	 * Set placeholder's level
	 * 
	 * @param lvl
	 *            Level
	 */
	public void setLevel(Level lvl)
	{
		this.level = lvl;
	}

	private RpgMap getCurrentMap()
	{
		return this.level.getRpgMap();
	}

}
