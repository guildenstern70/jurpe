package net.littlelite.jurpe.combat;

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

import net.littlelite.jurpe.characters.Monster;
import net.littlelite.jurpe.characters.PC;
import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.characters.PrimaryStats;
import net.littlelite.jurpe.items.Weapon;
import net.littlelite.jurpe.system.EffectType;
import net.littlelite.jurpe.system.JurpeUtils;
import net.littlelite.jurpe.system.OSProps;
import net.littlelite.jurpe.system.TurnTable;
import net.littlelite.jurpe.system.resources.LibraryStrings;

/**
 * Class for combat between PCs. Every instance of DualCombat represents an
 * entire combat, from the beginning to the end. Variabile "isAlive" check if
 * current combat is still taking place. For instance, in a combat between JOE
 * and JAK we will have: <br>
 * 
 * <pre>
 * DualCombat combat = new DualCombat(Joe, Jak); // Joe vs. Jack
 * 
 * </pre>
 * 
 * Remember to swap fighters at the end of every turn with combat.swapFighters()
 * 
 * 
 * @see ICombat
 */
public class DualCombat implements ICombat
{

	private TurnTable turns;
	private PC fighter;
	private PC fightee;
	private String fighterName;
	private String fighteeName;
	private boolean isCombatAlive;
	private AbstractList<String> combatLog = new ArrayList<String>();

	/**
	 * Creates a random, default Dual Combat for test purposes.
	 */
	public DualCombat()
	{
		turns = new TurnTable();
		// Create one random player
		PC pc1 = Monster.createRandom("Monster One", 100);
		PC pc2 = Monster.createRandom("Monster Two", 100);
		turns.subscribe(pc1);
		turns.subscribe(pc2);
		turns.orderBySpeed();
		this.fighter = turns.getCurrentPlayer();
		this.fightee = turns.getNextPlayer();
		fighterName = fighter.getName();
		fighteeName = fightee.getName();
	}

	/**
	 * Constructor. Initialize fighters, fighters' names. It sets isAlive=true
	 * 
	 * @param tt
	 *            is the active TurnTable
	 * @see TurnTable
	 */
	public DualCombat(TurnTable tt)
	{
		turns = tt;
		fighter = turns.getCurrentPlayer();
		fightee = turns.getNextPlayer();
		fighterName = fighter.getName();
		fighteeName = fightee.getName();
		isCombatAlive = true;
	}

	/**
	 * Get Fightee (first attacked) in this DualCombat
	 * 
	 * @return PC character
	 * @see PC
	 */
	public PC getFightee()
	{
		return this.fightee;
	}

	/**
	 * In a DualCombat one of the combatants could be a PCharacter. In every
	 * case this method returns the first PCharacter between fighter and
	 * fightee. If noone is human, it returns null
	 * 
	 * @return The Human Playing Character in combat
	 */
	public PCharacter getHumanFighter()
	{
		if (this.fighter instanceof PCharacter)
		{
			return (PCharacter) this.fighter;
		}
		if (this.fightee instanceof PCharacter)
		{
			return (PCharacter) this.fightee;
		}
		return null;
	}

	/**
	 * In a DualCombat one of the combatants could be a Monster. In every case
	 * this method returns the first Monster between fighter and fightee. If
	 * noone is monster, it returns null.
	 * 
	 * @return The AI Monster fighter
	 */
	public Monster getMonsterFighter()
	{
		if (this.fighter instanceof Monster)
		{
			return (Monster) this.fighter;
		}
		if (this.fightee instanceof Monster)
		{
			return (Monster) this.fightee;
		}
		return null;
	}

	/**
	 * Return Turn Table associated with this DualCombat
	 * 
	 * @return current turns table
	 * @see TurnTable
	 */
	public TurnTable getTurnTable()
	{
		return this.turns;
	}

	/**
	 * Get Fighter (first attacker) in this DualCombat
	 * 
	 * @return PC character
	 * @see PC
	 */
	public PC getFighter()
	{
		return this.fighter;
	}

	/**
	 * Call this method at the end of every combat turn, if combatants are still
	 * alive.
	 */
	public void swapFighters()
	{
		PC oldfighter = this.fighter;
		this.fighter = fightee;
		this.fightee = oldfighter;
		this.fighterName = this.fighter.getName();
		this.fighteeName = this.fightee.getName();
		combatLog.clear();
	}

	/**
	 * Set fighters. Order is important: first parameter is fighter (the one
	 * that attacks), second is fightee (defender).
	 * 
	 * @param fighter
	 *            PC who is making the attack
	 * @param fightee
	 *            PC who is defending
	 */
	public void setFighters(PC fighter, PC fightee)
	{
		this.fighter = fighter;
		this.fightee = fightee;
		this.fighterName = this.fighter.getName();
		this.fighteeName = this.fightee.getName();
		combatLog.clear();
	}

	/**
	 * Return true combat is alive (ie: will not terminate next turn). Combat is
	 * not alive when one combatants is dead or escaped.
	 * 
	 * @return true if fighters are both still alive
	 */
	public boolean isAlive()
	{
		if ((!fighter.isAlive()) || (!fightee.isAlive()))
		{
			this.isCombatAlive = false;
		}

		return this.isCombatAlive;
	}

	/**
	 * Use this method to quit a combat when combatants are still living. IE:
	 * one of them escapes.
	 * 
	 * @param alive
	 *            set boolean condition. If true, character is alive.
	 */
	public void setAlive(boolean alive)
	{
		this.isCombatAlive = alive;
	}

	/**
	 * Combatants use this command to try to escape. You succeed in escaping if
	 * fighter's movement points are greater than fightee's
	 * 
	 * @return true if combat ends after this command
	 */
	public boolean move()
	{

		boolean success = false;

		combatLog.add(fighterName + LibraryStrings.TRIESTOESC);
		int fighterMvmt = this.fighter.getMvmt();
		int fighteeMvmt = this.fightee.getMvmt();
		combatLog.add(fighterName + LibraryStrings.MVMTIS + String.valueOf(fighterMvmt));
		combatLog.add(fighteeName + LibraryStrings.MVMTIS + String.valueOf(fighteeMvmt));
		if (fighterMvmt > fighteeMvmt)
		{
			combatLog.add(fighterName + LibraryStrings.FASTERTHN + fighteeName);
			combatLog.add(fighterName + LibraryStrings.ESCAPES);
			success = true;
			if (fighter.isAI())
			{
				fighter.getPrimaryStats().restoreHitPoints();
				// when a monster escapes, he's able to restore his HP (not
				// precisely legal!!)
			}
			this.isCombatAlive = false; // when a characters escapes, combat
			// ends.
		}
		else
		{
			combatLog.add(fighteeName + LibraryStrings.FASTERTHN + fighterName);
			combatLog.add(fighterName + LibraryStrings.CANNOTESC);
		}

		return success;
	}

	/**
	 * Ready Weapon combat option. Use this command to ready an unready weapon.
	 * 
	 * @return true
	 */
	public boolean readyWeapon()
	{
		boolean ready = false;

		combatLog.add(this.fighter.getName() + " readies his weapon.");
		if (this.fighter.wearsWeapon())
		{
			Weapon weapon = this.fighter.getCurrentWeapon();
			weapon.setWeaponReady(true);
			combatLog.add(weapon + LibraryStrings.IS + " " + LibraryStrings.READY);
			ready = weapon.isReady();
		}
		else
		{
			combatLog.add(this.fighterName + LibraryStrings.NOWEPTORD);
			ready = true;
		}

		return ready;
	}

	/**
	 * Reload combat option.
	 * 
	 * @return true if combat ends after this command
	 */
	public boolean reload()
	{
		System.err.println("DualCombat->reload not yet implemented.");
		return true;
	}

	/**
	 * Aim combat option
	 * 
	 * @return true if combat ends after this command
	 */
	public boolean aim()
	{
		System.err.println("DualCombat->aim  not yet implemented.");
		return true;
	}

	/**
	 * Attack routine for DualCombat. <br>
	 * <br>
	 * 1. Check if fighter is ready 2. If so makes an attack with current weapon
	 * 3. If attacks suceeds, fightee executes its defense 4. If defense fails,
	 * it rolls for damages <br>
	 * <br>
	 * 
	 * @return true if combat ends after this command
	 */
	public boolean attack()
	{
		combatLog.add(fighterName + LibraryStrings.ATTACKS + fighteeName);
		int attackModifier = this.turns.getEffect(EffectType.ATTACK_MODIFIER);
		combatLog.add(fighterName + LibraryStrings.HASATTMOD + attackModifier);
		this.singleAttack(0, attackModifier);
		return false;
	}

	/**
	 * All Out Attack (AOA) combat option. When a character chooses to do an All
	 * Out Attack, she may make no active defenses at all until next turn.
	 * 
	 * @return true if combat ends after this command
	 */
	public boolean allOutAttack()
	{
		AllOutAttackType aType = fighter.getAllOutAttackType();
		int currentSkillAbility = 0;
		combatLog.add(fighterName + LibraryStrings.MAKESAOA);
		combatLog.add(fighterName + LibraryStrings.ALLMAKE + aType.toString() + ")");

		if (aType.equals(AllOutAttackType.TWOATTACK))
		{

			// "Two attack" all out attack type. You can do this only if you are
			// bare handed or if you have a balanced weapon.
			if (fighter.wearsWeapon())
			{
				Weapon fighterWeapon = this.fighter.getCurrentWeapon();
				// AI never selects 2 attacks AOA when it carries an unbalanced
				// weapon
				if (!fighter.isAI())
				{
					if (!fighterWeapon.isBalanced())
					{
						combatLog.add(fighterName + LibraryStrings.ALLCANT);
						return false;
					}
				}
				currentSkillAbility = fighter.getCurrentWeaponLevel();
				combatLog.add(fighterName + LibraryStrings.SKILLWITH + fighterWeapon.getSkill().getName() + LibraryStrings.IS
						+ String.valueOf(currentSkillAbility));
			}
			else
			{
				currentSkillAbility = fighter.getBareHandsSkill();
				combatLog.add(fighterName + LibraryStrings.UNARMEDSK + String.valueOf(currentSkillAbility));
			}

			for (byte j = 0; j < 2; j++)
			{
				// Fighter makes the attack two times
				this.makeAttack(currentSkillAbility, 0);
			}

		}
		else if (aType.equals(AllOutAttackType.BONUSDAMG))
		{
			// Bonus damage
			// You make a single attack, at normal skill, doing +2 damage if you
			// hit
			this.singleAttack(2, 0);
		}
		else if (aType.equals(AllOutAttackType.BONUSKILL))
		{
			// Bonus skill:
			// You make a single attack, at +4 bonus skill
			this.singleAttack(0, 4);
		}

		// Set Active Defenses not available until next turn.
		fighter.setActiveDefenseAvailability(false);
		return false;
	}

	/**
	 * All Out Defense combat option. <br>
	 * "Defend yourself; do nothing else this turn. If you fail your defense
	 * roll against any attack, you may try another (different) defense". In
	 * this implementation, we arbitrarily choose to make a first roll with the
	 * active defense choosen by the character and the second one from dodging,
	 * blocking and parrying, in this order. Of course, if character has just
	 * one active defense available, he cannot make an All Out Defense. Parrying
	 * is considered unavailable if character wears an unbalanced weapon. <br>
	 * If character has only two active defenses available, he will have two
	 * rolls: the first one with the AD he choose, the second one with the
	 * other. <br>
	 * If character has three active defenses available, he will have two rolls:
	 * the first one with the AD he choose, the second one with blocking, or
	 * with dodging, if blocking is its first choose. (Beware: this is not a
	 * Jurpe Rule!). The All Out Defense Command is implemented as follows. When
	 * the command is issued, it sets BOTH active defenses for the fighter.
	 * When, during an attack (the next turn) the defenseFromAttack() method is
	 * called, a check is done. If fightee.getActiveDefenses returns an array of
	 * length=2 then the fightee chose All Out Defense in the turn before. The
	 * defenseFromAttack() method will then perform an All Out Defense for that
	 * character and, after that, will restore the ActiveDefenses to length=1.
	 * 
	 * @return true if combat ends after this command
	 */
	public boolean allOutDefense()
	{

		combatLog.add(fighterName + LibraryStrings.MAKESA0D);

		DefenseType[] dts = fighter.getActiveDefensesAvailable();
		DefenseType[] choosenDts = new DefenseType[2];

		DefenseType primaryAD = fighter.getActiveDefense();

		if (dts.length == 1)
		{
			combatLog.add(fighterName + LibraryStrings.CANTAODEF);
			return false;
		}
		else if (dts.length == 2)
		{

			// If one of them is parrying and character wears an unbalanced
			// weapon,
			// AOD is unavailable
			if (dts[0].equals(DefenseType.ACTIVE_PARRY) || dts[1].equals(DefenseType.ACTIVE_PARRY))
			{
				if (!fighter.getCurrentWeapon().isBalanced())
				{
					combatLog.add(fighterName + LibraryStrings.CANTAODEF);
					return false;
				}
			}

			choosenDts[0] = primaryAD;

			if (primaryAD.equals(dts[0]))
			{
				choosenDts[1] = dts[1];
			}
			else
			{
				choosenDts[1] = dts[0];
			}
		}
		else
		// Every AD is available
		{
			choosenDts[0] = primaryAD;

			if (primaryAD.equals(DefenseType.ACTIVE_BLOCK))
				choosenDts[1] = DefenseType.ACTIVE_DODGE;
			else
				choosenDts[1] = DefenseType.ACTIVE_BLOCK;
		}

		// Set ActiveDefenses for this character
		fighter.setActiveDefenses(choosenDts);
		return false;
	}

	/**
	 * Returns log, appending LINEFEED after every item.
	 * 
	 * @return String log separated by LF
	 */
	public String log()
	{
		StringBuilder logStr = new StringBuilder();

		for (String s : combatLog)
		{
			logStr.append(s);
			logStr.append(OSProps.LINEFEED);
		}

		return logStr.toString();
	}

	/**
	 * Returns the score points earned in this combat. If fighter is PC and
	 * combat is not alive -> PC earned a score. If fighter is NPC and combat is
	 * not alive -> PC earned nothing.
	 * 
	 * @param turnsPlayed
	 *            number of turns played in this combat
	 * @return score points
	 */
	public int earnedScore(int turnsPlayed)
	{

		if (this.isCombatAlive || fighter.isAI())
		{
			return 0;
		}

		int score;

		// Score is based on fightee's HT,DX,ST and is inversely proportional to
		// the number
		// of turns in which it was defied.
		PrimaryStats ps = fightee.getPrimaryStats();
		score = ((ps.getHT() * 5 + ps.getDX() * 5 + ps.getST() * 5) / turnsPlayed) + 10;
		return score;
	}

	/**
	 * Returns the experience points earned in this combat. If fighter is PC and
	 * combat is not alive -> PC earned experience points If fighter is NPC and
	 * combat is not alive -> PC earned nothing. There is a chance you get no
	 * points even if you won the combat.
	 * 
	 * @return score points
	 */
	public float earnedExperiencePoints()
	{

		if (this.isCombatAlive || fighter.isAI())
		{
			return 0f;
		}

		PrimaryStats ps = fightee.getPrimaryStats();
		float experience;

		if (Math.random() > 0.6)
		{
			experience = 0;
		}
		else
		{
			experience = Math.min(Math.max(0.5f, (ps.getHT() + ps.getDX() + ps.getST()) / 40), 3.0f);
		}

		return experience;

	}

	/*
	 * +++++++++++++ PRIVATE MEMBERS +++++++++++++++++++++++++++
	 */

	/**
	 * Applies modifiers due to type of weapon mode.
	 * 
	 * @param damageRoll
	 *            Damage Roll
	 * @return effective damage done
	 */
	private int computesDamage(int damageRoll)
	{
		int fighteeRD = fightee.getDamageResistance();
		combatLog.add(fighteeName + LibraryStrings.HASRDOF + String.valueOf(fighteeRD));
		int effectiveDamage = 0;

		if (fighter.wearsWeapon())
		{
			DamageMode weaponMode = fighter.getCurrentWeapon().getMode();
			effectiveDamage = JurpeUtils.computeWeaponDamage(damageRoll, weaponMode, fighteeRD);
		}
		else
		{
			effectiveDamage = JurpeUtils.computeDamage(damageRoll, fighteeRD);
		}
		return effectiveDamage;
	}

	private void singleAttack(int bonusDamage, int bonusSkill)
	{
		// Check if current player is ready
		if (this.fighter.isReady())
		{
			int currentSkillAbility = 0;
			combatLog.add(fighterName + LibraryStrings.ISREADY);

			if (fighter.wearsWeapon())
			{
				Weapon fighterWeapon = this.fighter.getCurrentWeapon();
				// If fighter has an unbalanced weapon, next turn this weapon
				// will be unready
				if (!fighterWeapon.isBalanced())
				{
					fighterWeapon.setWeaponReady(false);
				}
				currentSkillAbility = fighter.getCurrentWeaponLevel() + bonusSkill;
				combatLog.add(fighterName + LibraryStrings.SKILLWITH + fighterWeapon.getSkill().getName() + LibraryStrings.IS
						+ String.valueOf(currentSkillAbility));
			}
			else
			{
				currentSkillAbility = fighter.getBareHandsSkill() + bonusSkill;
				combatLog.add(fighterName + LibraryStrings.UNARMEDSK + String.valueOf(currentSkillAbility));
			}

			// Fighter makes the attack
			this.makeAttack(currentSkillAbility, bonusDamage);
		}
		else
		{
			combatLog.add(LibraryStrings.YOUNOTRDY);
		}
	}

	private void makeAttack(int currentSkillAbility, int bonusDamage)
	{
		StringBuilder sb = new StringBuilder();

		if (fighter.isUnconscious())
		{
			combatLog.add(fighterName + LibraryStrings.ISUNXCONS);
			return;
		}

		boolean success = JurpeUtils.successRoll(currentSkillAbility);
		int roll = JurpeUtils.getLatestRoll();

		// Automatic Miss (CORPS pg.25)
		if (roll > 16)
		{
			sb.append(fighterName);
			sb.append(LibraryStrings.MISMISSES);
			sb.append(this.rollLog(roll, currentSkillAbility));
			combatLog.add(sb.toString());
		}
		else
		{
			// Fighter succeeds in hitting fightee: fightee can't defend himself
			if (success)
			{
				this.hitFightee(roll, currentSkillAbility, bonusDamage);
				int remainingHT = this.fightee.getCurrentHP();
				sb.append(this.fighteeName);
				sb.append(LibraryStrings.HASNOW);
				sb.append(remainingHT);
				sb.append(" HP");
				combatLog.add(sb.toString());
			}
			else
			// attack fails
			{
				sb.append(fighterName);
				sb.append(LibraryStrings.ATTAFAILS);
				sb.append(this.rollLog(roll, currentSkillAbility));
				combatLog.add(sb.toString());
			}
		}
	}

	/**
	 * According to CORPS, when a player is injured, its IQ and DX are reduced
	 * to the amount of damage received.
	 * 
	 * @param roll
	 *            dice roll
	 * @param currentSkillAbility
	 *            current skill level
	 * @param bonus
	 *            attack bonus and or modifier
	 */
	private void hitFightee(int roll, int currentSkillAbility, int bonus)
	{

		int damage = 0;
		StringBuilder hitLog = new StringBuilder();

		// fighter scores a critical
		if (JurpeUtils.checkCriticalSuccess(roll, currentSkillAbility))
		{
			// fighter rolls a super critical
			if (roll == 3)
			{
				hitLog.append(LibraryStrings.SUPCRITIC);
				hitLog.append(this.rollLog(roll, currentSkillAbility));
				combatLog.add(hitLog.toString());
				damage = fighter.getWeaponMaxDamage();
				combatLog.add(fighteeName + LibraryStrings.CANTDEFEND + LibraryStrings.ANDRECEIVE + String.valueOf(damage) + LibraryStrings.DAMAGES);
			}
			// fighter rolls a critical
			else
			{
				hitLog.append(LibraryStrings.CRITICHIT);
				hitLog.append(this.rollLog(roll, currentSkillAbility));
				combatLog.add(hitLog.toString());
				combatLog.add(fighteeName + LibraryStrings.CANTDEFEND + LibraryStrings.AGAINSTTHT);
				damage = fighter.getWeaponDamage();
				combatLog.add(fighterName + LibraryStrings.INFLICTS + String.valueOf(damage) + LibraryStrings.DAMAGES);
			}
		}
		// fighter scores normal: fightee must defend himself
		else
		{
			hitLog.append(fighterName);
			hitLog.append(LibraryStrings.ATTVALID);
			hitLog.append(this.rollLog(roll, currentSkillAbility));
			hitLog.append(fighteeName);
			hitLog.append(LibraryStrings.MUSTDEFEND);
			combatLog.add(hitLog.toString());

			if (this.defenseFromAttack())
			{
				// fightee succeeds to defend
				combatLog.add(LibraryStrings.NODAMAGE);
			}
			else
			{
				// fightee can't defend himself
				combatLog.add(fighterName + LibraryStrings.ROLLSDAMGE);
				// computes damage
				damage = fighter.getWeaponDamage();
				combatLog.add(LibraryStrings.ANDMAKES + String.valueOf(damage) + LibraryStrings.DAMAGES);

			}
		}

		// Apply weapon/skill modifiers
		if (damage > 0)
		{
			damage = this.computesDamage(damage);
			combatLog.add(LibraryStrings.SWEPMODI + damage);

			// Add bonus damage
			if (bonus > 0)
			{
				damage += bonus;
				combatLog.add(LibraryStrings.ALLOUTBNS + String.valueOf(bonus));
			}

			// Inflicts damage to enemy
			fightee.receiveDamage(damage);

			/* Add Shock effects */
			turns.addEffect(fightee, EffectType.IQ_MODIFIER, -damage, (short) 1);
			turns.addEffect(fightee, EffectType.ATTACK_MODIFIER, -damage, (short) 1);
			combatLog.add(LibraryStrings.SHOCKREDS + String.valueOf(damage));

		}

		combatLog.add(fighteeName + LibraryStrings.RECEIVES + String.valueOf(damage) + LibraryStrings.DAMAGES);
	}

	private int getTotalDefensePoints(int activeDefense, String activeDefDescription, float passiveDefense)
	{
		String defender = this.fightee.getName();
		// Defender has ... total defense
		int totalDefencePoints = activeDefense + Math.round(passiveDefense);

		StringBuilder defLog = new StringBuilder();
		defLog.append(defender);
		defLog.append(LibraryStrings.DEFENSESR);
		defLog.append(String.valueOf(totalDefencePoints));
		defLog.append(" [AD: ");
		defLog.append(activeDefense);
		defLog.append("+PD: ");
		defLog.append(passiveDefense);
		defLog.append("]");
		this.combatLog.add(defLog.toString());

		return totalDefencePoints;

	}

	private int getTotalDefensePoints(Defense defense)
	{

		int activeDefense = defense.getActiveDefensePoints();
		String activeDescription = defense.activeDefenseString(this.fightee.getActiveDefense());
		float passiveDefense = this.fightee.getTotalPassiveDefenses();

		return this.getTotalDefensePoints(activeDefense, activeDescription, passiveDefense);
	}

	private boolean rollForDefense(boolean success, int roll)
	{
		boolean canDefend = false;
		String defender = this.fightee.getName();

		// Critical
		if (roll < 5)
		{
			combatLog.add(defender + LibraryStrings.PARSUPERB);
			canDefend = true;
		}
		else if (roll > 16)
		{
			combatLog.add(defender + LibraryStrings.MISMISSES);
		}
		else if (success)
		{
			// success
			combatLog.add(defender + LibraryStrings.DEFENDWEL);
			canDefend = true;
		}
		else
		{
			// fail
			combatLog.add(defender + LibraryStrings.CANTDEFEND);
		}

		return canDefend;
	}

	/**
	 * Defense from attack
	 * 
	 * @see Defense
	 * @return true if defense is successful
	 */
	private boolean defenseFromAttack()
	{

		String defender = this.fightee.getName();
		Defense defense = new Defense(this.fightee);
		boolean canDefend = false;

		boolean success = JurpeUtils.successRoll(this.getTotalDefensePoints(defense));
		int roll = JurpeUtils.getLatestRoll();
		combatLog.add(defender + LibraryStrings.ROLLS + String.valueOf(roll));

		canDefend = this.rollForDefense(success, roll);

		if (!canDefend)
		{
			// fail
			if (defense.isAllOutDefense())
			{
				combatLog.add(defender + LibraryStrings.HASHAS + LibraryStrings.DEFALLODF);
				DefenseType[] dts = this.fightee.getActiveDefenses();

				int totalDefencePoints = this.getTotalDefensePoints(defense.getActiveDefensePoints(dts[1]), defense.activeDefenseString(dts[1]), this.fightee
						.getTotalPassiveDefenses());

				combatLog.add(defender + LibraryStrings.DEFENSESR + String.valueOf(totalDefencePoints));
				success = JurpeUtils.successRoll(totalDefencePoints);
				// Restores second preferred Active Defense to null
				dts[1] = null;
				this.fightee.setActiveDefenses(dts);
				// then, the same as before:
				roll = JurpeUtils.getLatestRoll();
				combatLog.add(defender + LibraryStrings.ROLLS + String.valueOf(roll));

				canDefend = this.rollForDefense(success, roll);

			}
		}
		return canDefend;

	}

	private String rollLog(int roll, int skill)
	{
		StringBuilder hitLog = new StringBuilder();
		hitLog.append("[");
		hitLog.append(roll);
		hitLog.append('/');
		hitLog.append(skill);
		hitLog.append("]. ");
		return hitLog.toString();
	}

}
