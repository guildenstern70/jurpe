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

package net.littlelite.jurpe.ai;

import net.littlelite.jurpe.characters.PC;
import net.littlelite.jurpe.combat.AllOutAttack;
import net.littlelite.jurpe.combat.Attack;
import net.littlelite.jurpe.combat.Move;
import net.littlelite.jurpe.combat.ReadyWeapon;
import net.littlelite.jurpe.combat.DualCombat;
import net.littlelite.jurpe.combat.ICombat;
import net.littlelite.jurpe.items.Weapon;
import net.littlelite.jurpe.system.ICommand;

/**
 * Monster Artificial Intelligence.
 * 
 * 
 */
public class MonsterAI
{

	private final transient PC monster;
	private final transient PC attacked;
	private final DualCombat dualCombat;
	private final String[] availableCommands;

	/**
	 * Initiates Artificial Intelligence relative to a Dual Combat
	 * 
	 * @param cdc
	 *            DualCombat in which one combatant is artificial life.
	 * @see DualCombat
	 */
	public MonsterAI(DualCombat cdc)
	{
		dualCombat = cdc;
		monster = dualCombat.getFighter();
		attacked = dualCombat.getFightee();
		availableCommands = ICombat.AVAILABLE_COMBAT_COMMANDS;
	}

	/**
	 * Returns a command choosen by AI
	 * 
	 * @return choosen command, seen the context
	 */
	public ICommand getChoosenCommand()
	{
		int command = this.intelligence();
		ICommand cmd = null;

		if (command == ICombat.CM_ATTAK)
		{
			cmd = new Attack(dualCombat);
		}
		else if (command == ICombat.CM_MOVE)
		{
			cmd = new Move(dualCombat);
		}
		else if (command == ICombat.CM_ALLOATT)
		{
			cmd = new AllOutAttack(dualCombat);
		}
		else if (command == ICombat.CM_RDYWPN)
		{
			cmd = new ReadyWeapon(dualCombat);
		}

		return cmd;
	}

	private int intelligence()
	{
		final int currentMonsterHT = monster.getCurrentHP();
		final int currentAttackedHT = attacked.getCurrentHP();
		int choosenMove = 0;

		// If monster has an unbalanced weapon which is not ready, she will
		// ready it
		if (this.monster.wearsWeapon())
		{
			Weapon monsterWeapon = this.monster.getCurrentWeapon();
			if ((!monsterWeapon.isBalanced()) && (!monsterWeapon.isReady()))
			{
				choosenMove = ICombat.CM_RDYWPN;
				return choosenMove;
			}
		}

		if (Math.abs(currentMonsterHT - currentAttackedHT) < 2)
		{
			choosenMove = ICombat.CM_ATTAK;
		}
		// tries to escape
		else if (currentMonsterHT < 2)
		{
			choosenMove = ICombat.CM_MOVE;
		}
		// all out attack
		else if (Math.abs(currentMonsterHT - currentAttackedHT) > 5)
		{
			// Probability of an All Out Attack
			// depends on the IQ of Monster. High intelligent monsters
			// attacks more frequently. Range between 10%-20%ca
			if (Math.random() <= (0.1 + 0.01 * monster.getPrimaryStats().getIQ()))
			{
				choosenMove = ICombat.CM_ALLOATT;
			}
			else
			{
				choosenMove = ICombat.CM_ATTAK;
			}
		}
		else
		{
			choosenMove = ICombat.CM_ATTAK;
		}

		return choosenMove;

	}

	/**
	 * For test purposes only.
	 * 
	 * @deprecated
	 */
	public static void test()
	{
		/*
		 * DualCombat attackResponse = new DualCombat(fightee,fighter);
		 * MonsterAI mai = new MonsterAI(attackResponse); Command cmd =
		 * mai.getChoosenCommand(); cmd.execute();
		 * this.log().addEntry(attackResponse.log());
		 */
	}

	/**
	 * @return Available Commands
	 */
	public String[] getAvailableCommands()
	{
		return availableCommands;
	}

	/**
	 * @return Dual Combat handle
	 */
	public DualCombat getDualCombat()
	{
		return dualCombat;
	}

}
