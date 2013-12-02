package net.littlelite.jurpe.world;

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
import java.io.Serializable;

import net.littlelite.jurpe.system.Config;

/**
 * Describes a location.
 */
public class Location implements Serializable
{
	private static final long serialVersionUID = 3317L;

	private boolean special;
	private Color color;
	private LocationType type;
	private String description;
	private String enterMessage;

	/**
	 * Constructor
	 * 
	 * @param loc
	 *            LocationType A kind of location (ie: Inn, Shop, House...)
	 */
	public Location(LocationType loc)
	{
		this.type = loc;
		this.initLocation();
	}

	/**
	 * Init any location
	 */
	public void initLocation()
	{
		switch (this.type)
		{
			case WALL: // wall
				this.special = false;
				break;

			case CORRIDOR: // corridor
				this.special = false;
				break;

			case SHOP: // shop
				this.special = true;
				this.color = Config.SHOP_COLOR;
				this.enterMessage = "You enter the small shop";
				this.description = "There's a small shop here";
				break;

			case TRAINING: // training
				this.special = true;
				this.color = Config.TRAINER_COLOR;
				this.enterMessage = "You enter the trainer house. You can improve your skills now.";
				this.description = "Your trainer house is here";
				break;

			case STAIRSUP: // stairsup
				this.special = true;
				this.color = Config.STAIRS_UP_COLOR;
				this.enterMessage = "You follow the stairs up";
				this.description = "You can see stairs leading up from here";
				break;

			case STAIRSDOWN: // stairsdown
				this.special = true;
				this.color = Config.STAIRS_DOWN_COLOR;
				this.enterMessage = "You follow the stairs down";
				this.description = "There are strange stairs leading down here";
				break;

			case TREE: // tree
				this.special = true;
				this.color = Config.TREE_COLOR;
				this.enterMessage = "You cannot enter a small tree!";
				this.description = "You are under a small tree";
				break;

			case HOUSE: // house
				this.special = true;
				this.color = Config.HOUSE_COLOR;
				this.enterMessage = "The door is locked.";
				this.description = "There is a house here";
				break;

			case INN: // inn
				this.special = true;
				this.color = Config.INN_COLOR;
				this.enterMessage = "You enter a quiet inn.";
				this.description = "There is a comfortable inn here";
				break;
                                
                        case MAGESGUILD:
                                this.special = true;
                                this.color = Config.MAGES_COLOR;
                                this.enterMessage = "A mage welcomes you.";
                                this.description = "You are in front of a mages guild palace";
                                break;

			case SIGN: // SIGN
				this.special = true;
				this.color = Config.SIGN_COLOR;
				this.enterMessage = "There is a strange sign here";
				this.description = "There is a sign here";
				break;

			default:
				System.err.println("Unknown location type: " + this.type);
				break;
		}
	}

	/**
	 * True if location is special. A special location is a location where some
	 * event can be fired. Ie: an inn is a special location because you can
	 * enter an inn and display the inn panel.
	 * 
	 * @return boolean
	 */
	public boolean isSpecial()
	{
		return this.special;
	}

	/**
	 * Get default color of location
	 * 
	 * @return Color
	 */
	public Color getColor()
	{
		return this.color;
	}

	/**
	 * Set color of this location
	 * 
	 * @param col
	 *            Color
	 */
	public void setColor(Color col)
	{
		this.color = col;
	}

	/**
	 * Get type of location.
	 * 
	 * @return LocationType Type of location
	 * @see LocationType
	 */
	public LocationType getType()
	{
		return this.type;
	}

	/**
	 * Set the message of this location
	 * 
	 * @param message
	 *            Message when user select this location
	 */
	public void setMessage(String message)
	{
		this.enterMessage = message;
	}

	/**
	 * Get message to display when Avatar enter this location
	 * 
	 * @return String message to display when Avatar enter this location
	 */
	public String getEnterMessage()
	{
		return this.enterMessage;
	}

	/**
	 * Get message to display when Avatar is on this location
	 * 
	 * @return String
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * @return String
	 */
	@Override
	public String toString()
	{
		return "Location " + this.type.toString();
	}
}
