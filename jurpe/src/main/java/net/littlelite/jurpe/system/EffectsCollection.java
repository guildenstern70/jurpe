package net.littlelite.jurpe.system;

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

import java.util.AbstractList;
import java.util.ArrayList;

/**
 * Collection of TurnEffects in charge
 */
public class EffectsCollection
{
	private AbstractList<TurnEffect> turnEffects; // TurnEffect objects

	public EffectsCollection()
	{
		turnEffects = new ArrayList<TurnEffect>();
	}

	/**
	 * Effects iterator
	 * 
	 * @return iterator for this effects
	 */
	public AbstractList<TurnEffect> effects()
	{
		return this.turnEffects;
	}

	/**
	 * Add effect
	 * 
	 * @param te
	 *            turn effect to add to this collection
	 * @see TurnEffect
	 */
	public void addEffect(TurnEffect te)
	{
		turnEffects.add(te);
	}

	/**
	 * If this collection is empty
	 * 
	 * @return true if no effect is in charge
	 */
	public boolean isVoid()
	{
		if (turnEffects.size() == 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * Apply every effect in the collection, calling the apply method of
	 * TurnEffect class.
	 * 
	 * @param log
	 *            Log handle
	 */
	public void applyAll(Log log)
	{
		this.updateAll(log);

		if (!this.isVoid())
		{
			for (TurnEffect t : this.turnEffects)
			{
				t.apply();
			}
		}
	}

	/**
	 * Remove all effects
	 */
	public void removeAll()
	{
		if (!this.isVoid())
		{
			for (TurnEffect t : this.turnEffects)
			{
				t.remove();
			}
		}
	}

	/**
	 * Updating effects. When a turn is past, turn effect counter is updated. An
	 * effect is removed when it's no more in charge.
	 * 
	 * @param log
	 *            Log Handle
	 */
	private void updateAll(Log log)
	{
		if (!this.isVoid())
		{

			log.addDetail("Updating turn effects");
			log.addDetail("There are " + turnEffects.size() + " effects in charge");
			AbstractList<TurnEffect> keptEffects = new ArrayList<TurnEffect>();

			for (TurnEffect t : this.turnEffects)
			{
				log.addEntry(t.getAffectedPC().toString() + " has " + t);
				if (t.isValid())
				{
					keptEffects.add(t);
					log.addDetail("Keeping " + t);
				}
				else
				{
					t.remove();
				}
			}

			this.turnEffects = keptEffects;

		}
	}

}
