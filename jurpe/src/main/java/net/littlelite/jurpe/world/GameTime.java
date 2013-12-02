package net.littlelite.jurpe.world;

/**
 J.U.R.P.E. @version@
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

import java.util.Random;

import net.littlelite.jurpe.system.resources.LibraryStrings;

/**
 * This class implements the time in the game
 * 
 */
public class GameTime
{

	protected int gtDay;
	protected short gtHour;
	protected short gtMinute;
	protected short gtSecond;

	/**
	 * Initializes a new Game Time
	 */
	public GameTime()
	{
		this.gtDay = 0;
		this.gtHour = 0;
		this.gtMinute = 0;
	}

	/**
	 * Inititializes new Game Time
	 * 
	 * @param day
	 *            Day of the game
	 * @param hour
	 *            Current Hour in the game
	 * @param minute
	 *            Current Minute in the game
	 */
	public GameTime(int day, short hour, short minute)
	{
		this.gtDay = day;
		this.gtHour = hour;
		this.gtMinute = minute;
	}

	/**
	 * Set a random hour in the game
	 * 
	 * @param rnd
	 *            Random seed
	 */
	public void setRandomTime(Random rnd)
	{
		this.gtHour = (short) rnd.nextInt(11);
		this.gtMinute = (short) rnd.nextInt(59);
	}

	/**
	 * Get the current date and time in the game
	 * 
	 * @return A string representing the current date and time in the game
	 */
	public String getCurrentDate()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(this.gtDay);
		sb.append("] ");
		sb.append(this.getCurrentTime());
		return sb.toString();
	}

	/**
	 * Get the current time in the game
	 * 
	 * @return A string representing the current time in the game
	 */
	public String getCurrentTime()
	{
		StringBuilder sb = new StringBuilder();
		if (this.gtHour < 10)
		{
			sb.append("0");
		}
		sb.append(this.gtHour);
		sb.append(":");
		if (this.gtMinute < 10)
		{
			sb.append("0");
		}
		sb.append(this.gtMinute);
		return sb.toString();
	}

	public void addOneMinute()
	{
		this.gtMinute += 1;

		if (this.gtMinute > 59)
		{
			this.addOneHour();
			this.gtMinute = 0;
		}
	}

	public void addOneHour()
	{
		this.gtHour++;
		if (this.gtHour > 23)
		{
			this.gtDay++;
			this.gtHour = 0;
		}
	}

	public String whatTimeIsIt()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(LibraryStrings.TIMEISNOW);
		sb.append(' ');
		sb.append(this.getCurrentTime());
		return sb.toString();
	}

	@Override
	public String toString()
	{
		return this.getCurrentTime();
	}

	public static final void main(String args[])
	{
		GameTime gt = new GameTime();
		for (int j = 0; j < 100; j++)
		{
			gt.addOneMinute();
			System.out.println(gt.getCurrentDate());
		}
	}

	/**
	 * @return Returns the gtDay.
	 */
	public int getDay()
	{
		return gtDay;
	}

	/**
	 * @param gtDay
	 *            The gtDay to set.
	 */
	public void setDay(int gtDay)
	{
		this.gtDay = gtDay;
	}

	/**
	 * @return Returns the gtHour.
	 */
	public short getHour()
	{
		return gtHour;
	}

	/**
	 * @param gtHour
	 *            The gtHour to set.
	 */
	public void setHour(short gtHour)
	{
		this.gtHour = gtHour;
	}

	/**
	 * @return Returns the gtMinute.
	 */
	public short getMinute()
	{
		return gtMinute;
	}

	/**
	 * @param gtMinute
	 *            The gtMinute to set.
	 */
	public void setMinute(short gtMinute)
	{
		this.gtMinute = gtMinute;
	}

	/**
	 * @return Returns the gtSecond.
	 */
	public short getSecond()
	{
		return gtSecond;
	}

	/**
	 * @param gtSecond
	 *            The gtSecond to set.
	 */
	public void setSecond(short gtSecond)
	{
		this.gtSecond = gtSecond;
	}
}
