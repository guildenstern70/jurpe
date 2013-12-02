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

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;

import net.littlelite.jurpe.characters.PC;
import net.littlelite.jurpe.system.resources.LibraryStrings;

/**
 * This class is a metaphor for a table in which players sit to play.
 */
public class TurnTable implements Serializable
{

	private static final long serialVersionUID = 3317L;

	private AbstractList<PC> players;

	private int currentPlayer;

	private int turn;

	private EffectsCollection effects;

	/**
	 * Constructor
	 */
	public TurnTable()
	{
		currentPlayer = 0;
		turn = 0;
		players = new ArrayList<PC>();
		effects = new EffectsCollection();
	}

	/**
	 * Add players (either PC or NPC) to the table.
	 * 
	 * @param player
	 *            Player to add
	 */
	public void subscribe(PC player)
	{
		players.add(player);
	}

	/**
	 * Remove player (either PC or NPC) from table
	 * 
	 * @param player
	 *            Player to remove
	 */
	public void unsubscribe(PC player)
	{
		for (PC curpc : this.players)
		{
			if (curpc.getName().equals(player.getName()))
			{
				players.remove(curpc);
				break;
			}
		}
	}

	/**
	 * Advances turn to next player.
	 */
	public void nextPlayer()
	{
		if (currentPlayer == players.size() - 1)
		{
			currentPlayer = 0;
		}
		else
		{
			currentPlayer++;
		}
	}

	/**
	 * Get current player, based upon current turn.
	 * 
	 * @return Player that can play now
	 */
	public PC getCurrentPlayer()
	{
		PC player = players.get(currentPlayer);
		return player;
	}

	/**
	 * Get next player, based upon current table disposition.
	 * 
	 * @return Player that can play after current player
	 */
	public PC getNextPlayer()
	{
		int nextPlayer;
		if (currentPlayer == players.size() - 1)
		{
			nextPlayer = 0;
		}
		else
		{
			nextPlayer = currentPlayer + 1;
		}
		return players.get(nextPlayer);
	}

	/**
	 * Get current turn. A turn is ended when player took an action
	 * 
	 * @return current turn number
	 */
	public int getCurrentTurn()
	{
		return this.turn;
	}

	/**
	 * Order players, confronting their Speed rating. (PC implements compareTo()
	 * based on speed)
	 */
	public void orderBySpeed()
	{
		Object[] toSortPlayers = Platform.sortArray(players.toArray());
		ArrayList<PC> sortedPlayers = new ArrayList<PC>();
		for (int k = 0; k < toSortPlayers.length; k++)
		{
			sortedPlayers.add((PC) toSortPlayers[k]);
		}
		this.players = sortedPlayers;
	}

	/**
	 * Get MVMT description and order of fight.
	 * 
	 * @return verbose description of who begins combat and why.
	 */
	public String getOrderDescription()
	{
		StringBuilder sb = new StringBuilder();
		int i = 0;

		for (PC curPlayer : this.players)
		{
			i++;
			sb.append(curPlayer.getCharacterAttributes().getName());
			sb.append(LibraryStrings.HASSPDOF);
			sb.append(curPlayer.getPrimaryStats().getSpeed());
			if (i == 1)
			{
				sb.append(LibraryStrings.ANDBEGINS);
			}
			else
			{
				sb.append(LibraryStrings.ANDFOLLOW);
			}
			sb.append(OSProps.LINEFEED);
		}

		return sb.toString();
	}

	/**
	 * Current player plays his turn. A turn happens every time a player (either
	 * NPC or PC) chooses a command
	 * 
	 * @param log
	 *            Log to communicate to
	 */
	public void nextTurn(Log log)
	{
		++turn;
		// Add turns effects
		this.effects.applyAll(log);
	}

	/**
	 * Get the modifier for a specified Effect in charge for current player
	 * 
	 * @param et
	 *            Effect type to apply
	 * @return modifier if effect is in charge, 0 else
	 */
	public int getEffect(EffectType et)
	{
		int modifier = 0;

		if (this.effects.isVoid())
		{
			return 0;
		}

		AbstractList<TurnEffect> eff = this.effects.effects();

		for (TurnEffect te : eff)
		{
			if (te.getEffectType().equals(et))
			{
				modifier = te.getModifier();
				break;
			}
		}

		return modifier;
	}

	/**
	 * Add an effect to current player with a duration of n turns
	 * 
	 * @param et
	 *            EffectType to add
	 * @param modifier
	 *            Modifier to add to character stats
	 * @param nT
	 *            Number of turns in which effect is in charge
	 */
	public void addEffect(EffectType et, int modifier, short nT)
	{
		this.addEffect(this.getCurrentPlayer(), et, modifier, nT);
	}

	/**
	 * Add an effect to a particular player with a duration of nT turns
	 * 
	 * @param player
	 *            PC who suffers effect
	 * @param et
	 *            EffectType to add
	 * @param modifier
	 *            Modifier to add to character stats
	 * @param nT
	 *            Number of turns in which effect is in charge
	 */
	public void addEffect(PC player, EffectType et, int modifier, short nT)
	{
		this.effects.addEffect(new TurnEffect(player, et, modifier, nT));
	}

	/**
	 * Call this method when a combat ends (ie: fightee runs away)
	 */
	public void removeAllEffects()
	{
		this.effects.removeAll();
	}

	/**
	 * Get number of players
	 * 
	 * @return the number of players at this table
	 */
	public int getPlayers()
	{
		return this.players.size();
	}

}
