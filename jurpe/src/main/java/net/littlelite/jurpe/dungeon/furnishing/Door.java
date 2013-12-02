package net.littlelite.jurpe.dungeon.furnishing;

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

import java.io.Serializable;

public class Door implements Serializable
{

	private static final long serialVersionUID = 3318L;

	private OpenClose status;

	/**
	 * Constructor for a door. By default the door is closed.
	 */
	public Door()
	{
		this.status = OpenClose.CLOSED;
	}

	/**
	 * Opens a door. If it is locked, it must be unclocked first
	 * 
	 * @return true if the door was successfully opened
	 */
	public boolean open()
	{
		boolean isNowOpen = false;

		if (this.status == OpenClose.CLOSED)
		{
			this.status = OpenClose.OPENED;
		}

		return isNowOpen;
	}

	/**
	 * Closes a door
	 * 
	 * @return
	 */
	public boolean close()
	{
		boolean isNowClosed = false;

		if (this.status == OpenClose.OPENED)
		{
			this.status = OpenClose.CLOSED;
		}

		return isNowClosed;
	}

	/**
	 * Locks a door
	 * 
	 * @return True if the door has been succesfully locked
	 */
	public boolean lock()
	{
		boolean isNowLocked = false;

		if (this.status == OpenClose.CLOSED)
		{
			this.status = OpenClose.CLOSED;
		}

		return isNowLocked;

	}

	/**
	 * @return boolean
	 */
	public boolean isOpen()
	{
		boolean open = false;

		if (this.status == OpenClose.OPENED)
		{
			open = true;
		}

		return open;
	}

	/**
	 * Status (opened/closed/locked) of the door
	 * 
	 * @return Status of the door
	 */
	public OpenClose getStatus()
	{
		return this.status;
	}

	/**
	 * Set the door open or closed
	 * 
	 * @param status
	 *            Door status
	 * 
	 */
	public void setStatus(OpenClose sStatus)
	{

		this.status = sStatus;
	}

	/**
	 * Return a representation of the door: - DC door closed - DO door opened -
	 * DL door locked
	 * 
	 * @return a representation of the door
	 */
        @Override
	public String toString()
	{
		String sDoor = "DC"; // door closed

		if (this.status == OpenClose.OPENED)
		{
			sDoor = "DO";
		}
		else if (this.status == OpenClose.LOCKED)
		{
			sDoor = "DL";
		}

		return sDoor;

	}
}