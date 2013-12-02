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

package net.littlelite.jurpe.characters;

import java.io.Serializable;

/**
 * Utility class to hold character's primary statistics for PC and NPC
 * (ST,DX,HT,IQ)
 */
public class PrimaryStats implements Serializable
{

	private static final long serialVersionUID = 3317L;

	protected int st;
	protected int dx;
	protected int ht;
	protected int iq;
	protected int fatigue;
	protected int hitpoints; // initial hit points (may differ from HT in
	// some monsters)
	protected int currentHitPoints; // current hit points

	/**
	 * Default Primary Statistics
	 */
	public PrimaryStats()
	{
		this.st = 10;
		this.dx = 10;
		this.ht = 10;
		this.iq = 10;

		this.fatigue = 0; // fatigue initially equals 0
		this.hitpoints = this.ht;
		// hitpoints initially equals health, if not specified else
		this.currentHitPoints = this.hitpoints;
	}

	/**
	 * Primary Statistics
	 * 
	 * @param s
	 *            Strength
	 * @param d
	 *            Dexterity
	 * @param h
	 *            Health
	 * @param i
	 *            Intelligence
	 */
	public PrimaryStats(int s, int d, int h, int i)
	{
		this.st = s;
		this.dx = d;
		this.ht = h;
		this.iq = i;

		this.fatigue = 0;
		this.hitpoints = this.ht;
		this.currentHitPoints = this.hitpoints;
	}

	/**
	 * Primary Statistics
	 * 
	 * @param s
	 *            Strength
	 * @param d
	 *            Dexterity
	 * @param h
	 *            Health
	 * @param i
	 *            Intelligence
	 * @param hp
	 *            Hit Points
	 */
	public PrimaryStats(int s, int d, int h, int i, int hp)
	{
		this.st = s;
		this.dx = d;
		this.ht = h;
		this.iq = i;

		this.fatigue = 0;
		this.hitpoints = hp;
		this.currentHitPoints = hp;
	}

	/**
	 * Add f points of fatigue to character. Fatigue is added to current fatigue
	 * points. If f adds more fatigue than initial strength, f is set to this.ST
	 * 
	 * @param f
	 *            Fatigue points to add to current fatigue
	 */
	public void addFatigue(int f)
	{
		if (this.st - this.fatigue - f >= 0)
		{
			this.fatigue += f;
		}
		else
		{
			if (f > 0)
			{
				this.fatigue = this.st;
			}
			else
			{
				this.fatigue = 0;
			}
		}
	}

	/**
	 * Set Fatigue. Can be any number between 0 and this.ST
	 * 
	 * @param f
	 *            Character fatigue.
	 */
	public void setFatigue(int f)
	{
		if (f >= 0 && f <= this.st)
		{
			this.fatigue = f;
		}
	}

	/**
	 * Get Fatigue
	 * 
	 * @return Fatigue. This number is the number to subtract to this.ST in
	 *         order to get the actual ST of a character
	 */
	public int getFatigue()
	{
		return this.fatigue;
	}

	/**
	 * Set Hit Points (remaining points before death)
	 * 
	 * @param hp
	 *            Hit Points
	 */
	public void setHitPoints(int hp)
	{
		this.hitpoints = hp;
	}

	/**
	 * Initial Hit Points = Hit Points may differ from HT if character is
	 * non-human
	 * 
	 * @return hit points original value
	 */
	public int getInitialHitPoints()
	{
		return this.hitpoints;
	}

	/**
	 * Current Hit Points
	 * 
	 * @return current hit points
	 */
	public int getCurrentHitPoints()
	{
		return this.currentHitPoints;
	}

	/**
	 * Get a string [current HP]/[HP max]
	 * 
	 * @return [current HP]/[HP max]
	 */
	public String getHPvsHPMAX()
	{
		StringBuilder hphpmax = new StringBuilder(String.valueOf(this.currentHitPoints));
		hphpmax.append("/");
		hphpmax.append(this.hitpoints);
		return hphpmax.toString();
	}

	/**
	 * Set Hit Points
	 * 
	 * @param hp
	 *            Character's hit points
	 */
	public void setCurrentHitPoints(int hp)
	{
		this.currentHitPoints = hp;
	}

	/**
	 * Set current hit points to initial hit points (full health)
	 */
	public void restoreHitPoints()
	{
		this.currentHitPoints = this.hitpoints;
	}

	/**
	 * Add points to current hit points.
	 * 
	 * @param points
	 *            Points to add to current hit points. If points to be added
	 *            will result in current HP > than initial HP, HP will be
	 *            restored to initial HP.
	 */
	public void addToHP(int points)
	{
		if (this.currentHitPoints + points <= this.hitpoints)
		{
			this.currentHitPoints += points;
		}
		else
		{
			this.restoreHitPoints();
		}
	}

	/**
	 * Set character's ST (do not compute fatigue here. Set fatigue first).
	 * 
	 * @param s
	 *            Character overall strength
	 */
	public void setST(int s)
	{
		this.st = s;
	}

	/**
	 * Set character's DX
	 * 
	 * @param d
	 *            Dexterity
	 */
	public void setDX(int d)
	{
		this.dx = d;
	}

	/**
	 * Set character's HT
	 * 
	 * @param h
	 *            Health
	 */
	public void setHT(int h)
	{
		this.ht = h;
	}

	/**
	 * Set character's IQ
	 * 
	 * @param i
	 *            Intelligence
	 */
	public void setIQ(int i)
	{
		this.iq = i;
	}

	/**
	 * Get character's ST
	 * 
	 * @return Character's strength (without computing fatigue)
	 */
	public int getST()
	{
		return this.st;
	}

	/**
	 * Get character's ST computing fatigue
	 * 
	 * @return Character's strength (computing fatigue)
	 */
	public int getSTminusFatigue()
	{
		return (this.st - this.fatigue);
	}

	/**
	 * Get character's DX
	 * 
	 * @return Character's dexterity
	 */
	public int getDX()
	{
		return this.dx;
	}

	/**
	 * Get character's HT
	 * 
	 * @return Character's health
	 */
	public int getHT()
	{
		return this.ht;
	}

	/**
	 * Get character's IQ
	 * 
	 * @return Character's intelligence
	 */
	public int getIQ()
	{
		return this.iq;
	}

	/**
	 * Get character's speed (HP+DX)/4
	 * 
	 * @return Character's speed
	 */
	public float getSpeed()
	{
		return (this.hitpoints + this.dx) / 4;
	}

	/**
	 * Movement. Equals to speed, rounded to closest integer.
	 * 
	 * @return Movement value
	 */
	public int getMove()
	{
		return Math.round(this.getSpeed());
	}

	/**
	 * Hash code. Very basic.
	 * 
	 * @return hash code
	 */
	@Override
	public int hashCode()
	{
		int result = 19;

		result *= this.dx;
		result += this.iq;
		result *= this.ht;
		result += this.st;

		result += this.fatigue;

		return result;
	}

	/**
	 * Equals condition for PrimaryStats
	 * 
	 * @param e
	 *            Object to confront with
	 * @return true if e has same values as this
	 */
	@Override
	public boolean equals(Object e)
	{
		if (e.getClass().getName().equals(this.getClass().getName()))
		{
			PrimaryStats confr = (PrimaryStats) e;
			if (this.dx == confr.getDX() && this.st == confr.getST() && this.iq == confr.getIQ() && this.ht == confr.getHT())
			{
				if (this.fatigue == confr.getFatigue() && this.getCurrentHitPoints() == confr.getCurrentHitPoints())
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * To string
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("ST:");
		sb.append(this.st);
		sb.append(" DX:");
		sb.append(this.dx);
		sb.append(" IQ:");
		sb.append(this.iq);
		sb.append(" HT:");
		sb.append(this.ht);
		sb.append(" HP:");
		sb.append(this.currentHitPoints);
		sb.append('/');
		sb.append(this.hitpoints);
		return sb.toString();
	}

}