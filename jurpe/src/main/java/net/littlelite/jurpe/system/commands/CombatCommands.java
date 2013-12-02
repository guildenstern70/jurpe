package net.littlelite.jurpe.system.commands;

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

import net.littlelite.jurpe.ai.MonsterAI;
import net.littlelite.jurpe.characters.Monster;
import net.littlelite.jurpe.characters.PC;
import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.combat.ReadyWeapon;
import net.littlelite.jurpe.combat.DualCombat;
import net.littlelite.jurpe.system.ICommand;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.JurpeUtils;
import net.littlelite.jurpe.system.TurnTable;
import net.littlelite.jurpe.system.resources.LibraryStrings;

/**
 * Combat related commands.
 * 
 * @see net.littlelite.jurpe.system.CoreCommands
 */
public class CombatCommands extends GenericCommands
{

	/**
	 * Starts a combat session. Updates TurnTable
	 * 
	 * @param fighter
	 *            PCharacter who is initiating the combat
	 * @param monsterToFight
	 *            Monster to fight
	 * @return Initialized DualCombat object
	 * @see DualCombat
	 * @throws JurpeException
	 */
	public static DualCombat enterCombat(PCharacter fighter, Monster monsterToFight)
	{
		// Combat session
		PC fightee = monsterToFight;
		TurnTable tt = new TurnTable();

		// Subscribe combatants to the fight
		tt.subscribe(fighter);
		tt.subscribe(fightee);

		// Order fighters by speed
		tt.orderBySpeed();
		log.addEntry(tt.getOrderDescription());

		// Updates turn
		tt.nextTurn(log);
		CombatCommands.logTurn(tt);

		// Fighter and fightee have been sorted before.
		PC firstFighter = tt.getCurrentPlayer();

		DualCombat combat = new DualCombat(tt);
		log.addEntry(LibraryStrings.NEWCOMBAT + fighter.getName() + " vs. " + fightee.getName());

		// Check if current player is AI, let's make a complete AI turn before
		// entering the Human turn
		if (firstFighter.isAI())
		{
			// Readyness feedback
			if (firstFighter.isReady())
			{
				log.addEntry(firstFighter + LibraryStrings.ISREADYTO);
			}
			else
			{
				log.addEntry(firstFighter + LibraryStrings.ISNOTRDYT);
			}
			CombatCommands.executeAIcommand(combat, firstFighter);
			CombatCommands.nextCombatTurn(combat);
		}

		log.addEntry(LibraryStrings.CURPISHM); // Player is Human

		PCharacter humanFighter = (PCharacter) tt.getCurrentPlayer();
		if (CombatCommands.checkUnconsciousness(humanFighter))
		{
			// Player: select your move
			log.addEntry(humanFighter.getName() + " " + LibraryStrings.SELECTMV);
		}
		else
		{
			log.addEntry(humanFighter + LibraryStrings.ISUNCONSC);
		}

		return combat;
	}

	/**
	 * After having initialized Combat and TurnTable objects, this method return
	 * false until combat is ended.
	 * 
	 * @param cbt
	 *            Dual Combat that's taking place
	 * @return true, if next player is human (control is returned to GUI) or
	 *         combat is ended
	 */
	public static boolean continueCombat(DualCombat cbt)
	{
		boolean exitLoop = false;

		// Next turn begins
		CombatCommands.nextCombatTurn(cbt);
		PC fighter = cbt.getFighter();

		// Readyness feedback
		if (fighter.isReady())
		{
			log.addEntry(fighter + LibraryStrings.ISREADYTO);
		}
		else
		{
			log.addEntry(fighter + LibraryStrings.ISNOTRDYT);
		}

		// Checks if current player is AI
		if (fighter.isAI())
		{
			exitLoop = CombatCommands.executeAIcommand(cbt, fighter);
		}
		else
		{
			PCharacter pFighter = (PCharacter) fighter;
			log.addEntry(LibraryStrings.CURPISHM); // Player is Human
			// Set exitLoop so that control passes to UI, then to a Combat
			// object (DualCombat)
			exitLoop = CombatCommands.checkUnconsciousness(pFighter);
			// Check injury effects for current fighter (monster are not
			// affected by this check
			// this is a particular interpretation of the rules I'm not sure
			// about)

			// Human character can now choose his move
			if (exitLoop)
			{
				log.addEntry(fighter.getCharacterAttributes().getName() + " " + LibraryStrings.SELECTMV);
			}
		}

		return exitLoop;
	}

	//
	// PRIVATE
	//

	private static boolean executeAIcommand(DualCombat cbt, PC fighter)
	{
		boolean exitLoop;

		// AI will choose next move
		log.addEntry(LibraryStrings.CURPISAI); // Player is AI
		log.addEntry(LibraryStrings.NOWAICHS);
		MonsterAI mai = new MonsterAI(cbt);
		ICommand cmd;
		if (fighter.isReady())
		{
			cmd = mai.getChoosenCommand();
			exitLoop = cmd.execute();
		}
		else
		{
			cmd = new ReadyWeapon(cbt);
			cmd.execute();
			exitLoop = false; // Now that AI has a ready weapon, go to next
			// turn.
		}
		log.addEntry(cbt.log());

		return exitLoop;

	}

	private static void logTurn(TurnTable tt)
	{
		log.addEntry("======== " + LibraryStrings.COMBAT + " " + LibraryStrings.TURN + " : " + String.valueOf((tt.getCurrentTurn())) + " ========");
	}

	private static void nextCombatTurn(DualCombat cbt)
	{
		TurnTable tt = cbt.getTurnTable();
		tt.nextTurn(log);
		tt.nextPlayer();
		CombatCommands.logTurn(tt);

		// Updates combat objects
		cbt.setFighters(tt.getCurrentPlayer(), tt.getNextPlayer());
	}

	/**
	 * Check if a character is unconscious
	 * 
	 * @param pFighter
	 *            Character to check unconsciousness for
	 * @return true if character can enter a move in this turn (that is, he is
	 *         conscious)
	 */
	private static boolean checkUnconsciousness(PCharacter pFighter)
	{

		boolean exitLoop = true;

		//
		// If character has 0 or less points left,
		// she must roll against basic HT. A success means
		// the turn can be taken normally. Else the character
		// falls unconscious (she can't take any other move).
		//
		if (pFighter.getPrimaryStats().getCurrentHitPoints() < 1)
		{
			// If character is not already unconscious,
			// she rolls for life.
			if (!pFighter.isUnconscious())
			{
				int roll = pFighter.rollForLife();

				log.addEntry(pFighter + LibraryStrings.UROLL4LIF);
				log.addEntry(LibraryStrings.ROLL4LIFP + String.valueOf(roll));

				if (pFighter.isUnconscious())
				{
					log.addEntry(pFighter + LibraryStrings.FALLUNCSC);
					exitLoop = false;
				}
				else
				{
					log.addEntry(pFighter + LibraryStrings.URECOVERH);
				}
			}
			else
			{
				log.addEntry(pFighter + LibraryStrings.ISUNCONSC);
			}
		}

		return exitLoop;
	}

	/**
	 * Process human victory over the monster (loot, experience points and so
	 * on...)
	 * 
	 * @param cbt
	 *            Handle to Dual Combat
	 */
	public static void monsterDefetead(DualCombat cbt)
	{
		float earnedExperience;
		int earnedScore;
		long earnedMoney;

		PCharacter humanPC = cbt.getHumanFighter();
		Monster monster = cbt.getMonsterFighter();
		TurnTable tt = cbt.getTurnTable();

		// Remove all effects
		tt.removeAllEffects();

		// Monster is either escaped or dead
		if (!monster.isAlive())
		{

			log.addEntry(monster.getName() + " dies.");
			// Reset character capabilities
			humanPC.setActiveDefenseAvailability(true);

			// Loot some money (30% of probability)
			earnedMoney = JurpeUtils.lootMoney(cbt.getFightee(), 0.3f);
			log.addEntry(LibraryStrings.YOULOOT);
			if (earnedMoney > 0)
			{
				log.addEntry(LibraryStrings.YOUFIND + " " + String.valueOf(earnedMoney) + " " + LibraryStrings.MONEY);
				humanPC.getInventory().earnMoney(earnedMoney);
			}
			else
			{
				log.addEntry(LibraryStrings.YOUFIND + " " + LibraryStrings.NOTHING);
			}

			// Computes experience
			earnedExperience = cbt.earnedExperiencePoints();

			log.addEntry(LibraryStrings.YOUEARNED + "> " + String.valueOf(earnedExperience) + " " + LibraryStrings.EXPPOINTS);

			// Computes score
			earnedScore = cbt.earnedScore(tt.getCurrentTurn());
			log.addEntry(LibraryStrings.YOUEARNED + "> " + String.valueOf(earnedScore) + " " + LibraryStrings.SCRPOINTS);

			// Add experience to PC
			humanPC.addToScore(earnedScore);
			humanPC.addToAvailablePoints(earnedExperience);
		}
		else
		{
			log.addEntry("Monster escaped. You earned 0 experience points.");
		}

	}

}
