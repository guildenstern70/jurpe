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
import java.util.Random;

import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.crawler.RpgMapPoint;
import net.littlelite.jurpe.dungeon.hexmap.HexMap;
import net.littlelite.jurpe.dungeon.hexmap.HexMapView;
import net.littlelite.jurpe.dungeon.rpgmap.Cell;
import net.littlelite.jurpe.dungeon.rpgmap.MapView;
import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.Log;

/**
 * Avatar placeholder. This is the current position of the player in the game
 * map (RpgMap)
 * 
 * @see Placeholder
 * @see net.littlelite.jurpe.dungeon.rpgmap.RpgMap
 */
public final class Avatar implements IDungeonPawn
{
	private Random rnd;
	private Log log;
	private static Avatar hAvatar;
	private Placeholder placeHolder;
	private Gunsight aimGunsight;

	/**
	 * Creates a new Avatar.
	 * 
	 * @param rnd
	 *            Random Random seed
	 * @param dng
	 *            Dungeon Handle to dungeons
	 * @return Avatar Newly created avatar
	 */
	public static Avatar getAvatar(Random rnd, Dungeons dng)
	{
		Log log = dng.getLog();

		try
		{
			if (hAvatar == null)
			{
				log.addDetail("Initializing Avatar");
				hAvatar = new Avatar(rnd, dng);
			}
		}
		catch (JurpeException jex)
		{
			log.addDetail("Cannot initialize Avatar.");
			jex.printStackTrace();
			System.exit(-1);
		}

		return hAvatar;
	}

	/**
	 * Get Avatar level
	 * 
	 * @return Level
	 */
	public Level getLevel()
	{
		return this.placeHolder.getLevel();
	}

	/**
	 * Get Avatar placeholder
	 */
	@Override
	public Placeholder getPlaceholder()
	{
		return this.placeHolder;
	}

	/**
	 * Get the underground level of the Avatar.
	 * 
	 * @return Zero if Avatar is in the Village, 1 if he's in the first
	 *         underground level and so on
	 */
	public int getZLevel()
	{
		return this.placeHolder.getLevel().getZ();
	}

	/**
	 * Set avatar level
	 * 
	 * @param lvl
	 *            Level
	 */
	public void setLevel(Level lvl)
	{
		this.placeHolder.setLevel(lvl);
	}

	/**
	 * Return true if the Avatar is in the village (dungeon level 0)
	 * 
	 * @return True if the Avatar is in the village
	 */
	public boolean isInVillage()
	{
		boolean inVillage = false;
		if (this.placeHolder.getLevel().getZ() == 0)
		{
			inVillage = true;
		}
		return inVillage;
	}

	/**
	 * Get Avatar foreground color
	 * 
	 * @return Avatar foreground color
	 */
	public Color getAvatarColor()
	{
		return this.placeHolder.getForegroundColor();
	}

	/**
	 * Get Avatar background color
	 * 
	 * @return Avatar background color
	 */
	public Color getAvatarBackground()
	{
		return this.placeHolder.getBackgroundColor();
	}

	/**
	 * Move avatar in some direction
	 * 
	 * @param d
	 *            A direction, as in Direction class
	 */
	public void move(Direction d) throws JurpeException
	{
		// Old Cell
		Cell actualCell = this.placeHolder.getCell();
		if (actualCell.isSpecial())
		{
			this.placeHolder.backGround = actualCell.getLocation().getColor();
		}
		else
		{
			this.placeHolder.backGround = Config.MAZE_BACKGROUND;
		}
		this.placeHolder.move(d);
	}

	/**
	 * Get the direction in which Avatar is getting close to the perimenter
	 * 
	 * @return the direction in which Avatar is getting close to the perimenter
	 */
	public Direction getEdge(HexMap hexMap) throws JurpeException
	{
		Direction dir = null;

		HexMapView hexMapView = hexMap.getHexMapView();
		MapView mapview = hexMapView.getMapView();
		if (mapview != null)
		{
			dir = mapview.getPerimeterEdge(this.placeHolder.getPosition());
		}
		else
		{
			throw new JurpeException("Avatar.getEdge(): Map view is null.");
		}

		return dir;
	}

	/**
	 * Set Avatar initial position.
	 * 
	 * @param hexMap
	 *            HexMap
	 */
	public void setInitialPosition(HexMap hexMap) throws JurpeException
	{

		if (this.getZLevel() == 0)
		{
			this.placeHolder.setCenteredPosition();
		}
		else
		{
			this.placeHolder.setDisplayableInitialPosition(this.rnd, hexMap);
		}

		RpgMapPoint iPos = this.placeHolder.getPosition();

		if (iPos == null)
		{
			this.log.addDetail("Cannot initialize random avatar position");
			System.exit(-1);
		}
		else
		{
			this.log.addDetail("Placed avatar in position " + iPos.toString());
		}
	}

	/**
	 * Reset avatar
	 */
	public void resetAvatar()
	{
		Avatar.hAvatar = null;
	}

	/**
	 * Gunsight (aim mode). If enabled, a new placeholder has to be generated.
	 * 
	 * @param enabled
	 *            If true, enables aim mode. Else exit aim mode.
	 */
	public void enableGunsight(boolean enabled)
	{
		if (enabled)
		{
			this.aimGunsight = Gunsight.getGunsight(this);
		}
		else
		{
			this.aimGunsight = null;
		}
	}

	/**
	 * If the Avatar has started an aim mode.
	 * 
	 * @return true if avatar has an active gunsight (ie, it is in aim mode)
	 */
	public boolean hasGunsight()
	{
		if (this.aimGunsight != null)
		{
			return true;
		}
		return false;
	}

	/**
	 * Aim gunsight
	 * 
	 * @return Aim gunsight object
	 */
	public Gunsight getGunsight()
	{
		return this.aimGunsight;
	}

	/**
	 * Initialize Avatar instance. The Avatar is constructed by
	 * Avatar.getAvatar()
	 * 
	 * @param randSeed
	 *            Random seed
	 * @param log
	 *            Log handle
	 */
	private Avatar(Random randSeed, Dungeons dng) throws JurpeException
	{
		// Avatar properties
		this.rnd = randSeed;
		this.log = dng.getLog();

		// Avatar placeholder is in level 0
		this.placeHolder = new Placeholder(dng.getDungeonLevel(0), PlaceholderType.PLAYER, "Avatar");
		this.placeHolder.setColors(Config.AVATAR_FORE, Config.MAZE_BACKGROUND);
		this.placeHolder.setCenteredPosition();

		// The gunsight for the aim
		this.aimGunsight = null;
	}

}
