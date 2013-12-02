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

import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.crawler.AbstractHexPoint;
import net.littlelite.jurpe.dungeon.crawler.RpgMapPoint;
import net.littlelite.jurpe.dungeon.crawler.ViewMapPoint;

/**
 * A MapView is a sort of viewport of RpgMap, that is, what is visible of an
 * RpgMap.
 */
public class MapView implements ILogicalMap, Iterable<Cell>
{

	protected RpgMap map;

	private short width;
	private short height;
	private RpgMapPoint center;

	/**
	 * Constructor
	 * 
	 * @param rpgmap
	 *            Entire map for which this is a view
	 * @param viewWidth
	 *            Width of this viewport
	 * @param viewHeight
	 *            Height of this viewport
	 */
	public MapView(RpgMap rpgmap, short viewWidth, short viewHeight)
	{
		this.map = rpgmap;
		this.width = viewWidth;
		this.height = viewHeight;

		short viewCenterX;
		short viewCenterY;

		if (this.map.getWidth() > this.width)
		{
			viewCenterX = (short) (viewWidth / 2);
		}
		else
		{
			viewCenterX = (short) (this.map.getWidth() / 2);
			if ((viewCenterX % 2) == 0)
			{
				viewCenterX++; // pair cell fixing
			}
		}

		if (this.map.getHeight() > this.height)
		{
			viewCenterY = (short) (viewHeight / 2);
		}
		else
		{
			viewCenterY = (short) (this.map.getHeight() / 2);
		}

		this.center = new RpgMapPoint(viewCenterX, viewCenterY);
	}

	/**
	 * Get current center
	 * 
	 * @return RpgMapPoint
	 */
	public RpgMapPoint getCenter()
	{
		return this.center;
	}

	/**
	 * Get RpgMapCell at RpgMapCoordinates XY
	 * 
	 * @param p
	 *            RpgMapPoint View relative point
	 * @return RpgMapCell Cell in RpgMap
	 */
	public Cell getCell(AbstractHexPoint p)
	{
		return this.getCell(p.x, p.y);
	}

	/**
	 * Get RpgMapCell at RpgMapCoordinates XY
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return RpgMapCell Cell in RpgMap
	 */
	public Cell getCell(short x, short y)
	{
		ViewMapPoint vp = new ViewMapPoint(this, x, y);
		RpgMapPoint rpgp = vp.toRpgMapPoint();
		return this.map.getCell(rpgp); // Return the RPG absolute cell in the
		// rpgp position
	}

	/**
	 * Returns true if the position is inside the RpgMapView width and height
	 * 
	 * @param position
	 *            Point coordinates of an hexagon
	 * @return True if the position is inside the RpgMapView width and height
	 */
	public boolean isInside(RpgMapPoint position)
	{
		boolean isIn = false;
		RpgMapPoint topleft = this.getTopLeft();
		RpgMapPoint bottomright = this.getBottomRight();
		if ((position.x >= topleft.x) && (position.x < bottomright.x))
		{
			if ((position.y >= topleft.y) && (position.y < bottomright.y))
			{
				isIn = true;
			}
		}

		return isIn;
	}

	/**
	 * Viewport width
	 * 
	 * @return Viewport width
	 */
	public short getWidth()
	{
		return this.width;
	}

	/**
	 * Viewport height
	 * 
	 * @return Viewport height
	 */
	public short getHeight()
	{
		return this.height;
	}

	/**
	 * Get top left coordinates, relative to RpgMap, of viewport
	 * 
	 * @return top left coordinates, relative to RpgMap, of viewport
	 */
	public RpgMapPoint getTopLeft()
	{
		short x = this.getCurrentLeft();
		short y = this.getCurrentTop();
		return new RpgMapPoint(x, y);
	}

	/**
	 * Get bottom right coordinates, relative to RpgMap, of viewport
	 * 
	 * @return bottom right coordinates, relative to RpgMap, of viewport
	 */
	public RpgMapPoint getBottomRight()
	{
		short x = this.getCurrentRight();
		short y = this.getCurrentBottom();
		return new RpgMapPoint(x, y);
	}

	/**
	 * Iterator
	 * 
	 * @return RpgMapIterator
	 */
	public MapIterator iterator()
	{
		return new MapIterator(this);
	}

	/**
	 * Set new center
	 * 
	 * @param sCenter
	 *            RpgMapPoint
	 */
	public void setCenter(RpgMapPoint sCenter)
	{
		if ((sCenter.x > 0) && (sCenter.y > 0))
		{
			this.center = sCenter;
		}
	}

	/**
	 * Scroll this view in the Direction d. The new center will be: - In case of
	 * North/South scrolling, the centerX is retained and centerY will be
	 * avatarY - In case of East/West scrolling, the centerY is retained and
	 * centerX will be avatarX. In this case, for the particular hex geometry,
	 * the scroll will take place ONLY if the x coordinate of the avatar is not
	 * pair.
	 * 
	 * @param avatarPoint
	 *            Point in which avatar is
	 * @param dir
	 *            Direction in which avatar is going
	 */
	public void scrollView(RpgMapPoint avatarPoint, Direction dir)
	{
		short cX = this.center.x;
		short cY = this.center.y;

		if (dir == Direction.NORTH || dir == Direction.SOUTH)
		{
			cY = avatarPoint.y;
		}
		else
		{
			if ((avatarPoint.x % 2) == 0) // scroll only in pair columns
			{
				if (dir == Direction.NORTHEAST || dir == Direction.SOUTHEAST)
				{
					cX = (short) (avatarPoint.x + 1);
				}
				else
				{
					cX = (short) (avatarPoint.x - 1);
				}
			}
			else
			{
				cX = avatarPoint.x;
			}
		}

		this.setCenter(new RpgMapPoint(cX, cY));
	}

	/**
	 * Return coordinates in view position, given the RpgMap absolute position
	 * 
	 * @param a
	 *            RpgMap point
	 * @return Coordinates as a view map point
	 */
	public ViewMapPoint getViewPosition(RpgMapPoint a)
	{
		return new ViewMapPoint(this, a);
	}

	/**
	 * This method returns true if the point avp is "next" to a viewport
	 * perimeter (up, down, left, right). Uses value=4 to determine by how many
	 * cells avp is to be distant from perimeter to be considered 'next'. Use
	 * this function to determine if a new center viewport is to be set.
	 * 
	 * @param avp
	 *            Point in RpgMap
	 * @return True if point is next to the perimeter
	 */
	public Direction getPerimeterEdge(RpgMapPoint avp)
	{
		ViewMapPoint coords = avp.toViewMapPoint(this);

		int topDist = coords.y;
		int bottomDist = this.height - coords.y;
		int leftDist = coords.x;
		int rightDist = this.width - coords.x;

		int danger = 4;
		Direction d = null;

		if (topDist < danger)
		{
			d = Direction.NORTH;
		}
		else if (bottomDist < danger)
		{
			d = Direction.SOUTH;
		}
		else if (leftDist < danger)
		{
			d = Direction.NORTHWEST;
		}
		else if (rightDist < danger)
		{
			d = Direction.NORTHEAST;
		}

		return d;
	}

	public short getCurrentTop()
	{
		return (short) (this.center.y - (this.height / 2));
	}

	public short getCurrentBottom()
	{
		return (short) (this.center.y + (this.height / 2));
	}

	public short getCurrentLeft()
	{
		return (short) (this.center.x - (this.width / 2));
	}

	public short getCurrentRight()
	{
		return (short) (this.center.x + (this.width / 2));
	}
}
