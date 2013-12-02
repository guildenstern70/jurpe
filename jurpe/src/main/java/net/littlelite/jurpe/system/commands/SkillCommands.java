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

import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.characters.PCharacterSkill;
import net.littlelite.jurpe.characters.PrimaryStats;
import net.littlelite.jurpe.characters.Skill;
import net.littlelite.jurpe.system.JurpeUtils;
import net.littlelite.jurpe.system.resources.LibraryStrings;

/**
 * Skills related commands.
 * 
 * @see net.littlelite.jurpe.system.CoreCommands
 */
public class SkillCommands extends GenericCommands
{

	/**
	 * Get First Aid regained health points.
	 * 
	 * @param pc
	 *            PCharacter to be aided
	 * @param sk
	 *            PCharacter Skill Level in First Aid
	 * @param tl
	 *            Tech Level of the Game World
	 * @return Gained Hit Points
	 */
	public static int firstAid(PCharacter pc, int sk, int tl)
	{
		int restoredPoints = 0;

		// Log
		log.addEntry(pc.getName() + LibraryStrings.XOLZOZIS);
		boolean success = JurpeUtils.successRoll(sk);
		int roll = JurpeUtils.getLatestRoll();
		// Critical success: restoresHT
		// Critical failure: -2 HT
		if (JurpeUtils.checkCriticalSuccess(roll, sk))
		{
			log.addEntry(LibraryStrings.CIIACUCS + roll + LibraryStrings.RYOYOPEE);
			int maxPoints = pc.getPrimaryStats().getInitialHitPoints();
			int currentPoints = pc.getPrimaryStats().getCurrentHitPoints();
			pc.getPrimaryStats().restoreHitPoints();
			restoredPoints = maxPoints - currentPoints;
		}
		else if (JurpeUtils.checkCriticalFailure(roll, sk))
		{
			log.addEntry(LibraryStrings.CIIAZALR + roll + LibraryStrings.TNONOE2H);
			pc.receiveDamage(2);
			restoredPoints = -2;
		}
		else if (success)
		{
			log.addEntry(LibraryStrings.FRTADSCE + roll);
			restoredPoints = JurpeUtils.getFirstAid(sk, tl);
			log.addEntry(LibraryStrings.YOUXGAIN + restoredPoints + LibraryStrings.HHTCPOIN);
			pc.getPrimaryStats().addToHP(restoredPoints);
		}
		else
		{
			log.addEntry(LibraryStrings.FRTADUSC + roll);
		}

		return restoredPoints;
	}

	/**
	 * Add Skill to Playing Character. When character learns a new skill, this
	 * is automatically improved until this operation costs some character
	 * points (until improving it's free).
	 * 
	 * @param curPC
	 *            Playing Character
	 * @param skillToAdd
	 *            Skill to Add to Playing Character
	 * @param autoImprove
	 *            if it's true, Playing Character will improve skill until doing
	 *            so it's free
	 * @return false if skill was already learned by character.
	 */
	public static boolean addSkill(PCharacter curPC, Skill skillToAdd, boolean autoImprove)
	{
		String defaultAbilita = skillToAdd.getBase();

		log.addEntry(curPC.getName() + " studies to learn " + skillToAdd.getName());

		PrimaryStats ps = curPC.getPrimaryStats();

		// Determining initial skill level
		int livelloIniziale = 0;
		if (defaultAbilita.equals(LibraryStrings.DEX))
		{
			livelloIniziale = ps.getDX();
		}
		else if (defaultAbilita.equals(LibraryStrings.COS))
		{
			livelloIniziale = ps.getHT();
		}
		else if (defaultAbilita.equals(LibraryStrings.INT))
		{
			livelloIniziale = ps.getIQ();
		}
		else if (defaultAbilita.equals(LibraryStrings.FOR))
		{
			livelloIniziale = ps.getST();
		}
		livelloIniziale += skillToAdd.getModifier();

		log.addEntry("Default skill level is " + livelloIniziale);

		// Learning new skill
		PCharacterSkill skillToLearn = new PCharacterSkill(skillToAdd, livelloIniziale);
		float skillCost = SkillCommands.getSkillCost(skillToLearn.getSkill(), curPC, livelloIniziale);

		if (!curPC.learnSkill(skillToLearn, skillCost))
		{
			return false;
		}

		log.addEntry(curPC.getName() + " successfully learnt " + skillToAdd.getName());
		if (autoImprove)
		{
			// Adding skill levels until it's free
			PCharacterSkill pskill = curPC.getCharacterSkill(skillToAdd);
			int initialSkillLevel = pskill.getLevel();
			int skillLevel = initialSkillLevel;
			while (SkillCommands.getImprovingSkillCost(curPC, skillLevel, skillToAdd) == 0)
			{
				curPC.improveSkill(pskill, 0f);
				skillLevel = pskill.getLevel();
			}
			log.addEntry(skillToAdd.getName() + " improved from level " + initialSkillLevel + " to level " + skillLevel);
		}

		return true;
	}

	/**
	 * Get how much points a Skill costs it's valued, or the initial points
	 * needed to learn a skill
	 * 
	 * @param skill
	 *            Skill to get cost for
	 * @param level
	 *            Skill level
	 * @param pc
	 *            Playing Character who wants to learn skill
	 * @return points needed
	 * @see Skill
	 */
	public static float getSkillCost(Skill skill, PCharacter pc, int level)
	{
		int tipoAbilita = skill.difficult;
		int currentStat;
		float costo = 0.0f;
		float delta = 0.0f;

		PrimaryStats ps = pc.getPrimaryStats();

		if (skill.isPhysical())
		{
			currentStat = ps.getDX();
			switch (tipoAbilita)
			{
				case Skill.EASY:
					costo = Skill.getCostPhysicalSkillEasy(currentStat, level);
					break;
				case Skill.MEDIUM:
				default:
					costo = Skill.getCostPhysicalSkillMedium(currentStat, level);
					break;
				case Skill.HARD:
					costo = Skill.getCostPhysicalSkillHard(currentStat, level);
					break;
			}
		}
		else
		{
			currentStat = ps.getIQ();
			switch (tipoAbilita)
			{
				case Skill.EASY:
					costo = Skill.getCostMentalSkillEasy(currentStat, level);
					break;
				case Skill.MEDIUM:
				default:
					costo = Skill.getCostMentalSkillMedium(currentStat, level);
					break;
				case Skill.HARD:
					costo = Skill.getCostMentalSkillHard(currentStat, level);
					break;
			}
		}

		log.addEntry("To learn " + skill.getName() + " " + pc.getName() + " needs to spend " + costo + " character points.");

		return delta;
	}

	/**
	 * Get how much points an improvement of one level for a determinate Skill
	 * costs
	 * 
	 * @param pc
	 *            Playing Character who wants to improve skill level
	 * @param currentLevel
	 *            Skill level before improvement
	 * @param skill
	 *            Skill to upgrade
	 * @return points needed
	 * @see Skill
	 */
	public static float getImprovingSkillCost(PCharacter pc, int currentLevel, Skill skill)
	{
		int tipoAbilita = skill.difficult;
		int currentStat = 0;
		float costo = 0.0f;
		float delta = 0.0f;

		PrimaryStats ps = pc.getPrimaryStats();

		if (skill.isPhysical())
		{
			currentStat = ps.getDX();
			switch (tipoAbilita)
			{
				case Skill.EASY:
					costo = Skill.getCostPhysicalSkillEasy(currentStat, currentLevel + 1);
					delta = costo - Skill.getCostPhysicalSkillEasy(currentStat, currentLevel);
					break;
				case Skill.MEDIUM:
				default:
					costo = Skill.getCostPhysicalSkillMedium(currentStat, currentLevel + 1);
					delta = costo - Skill.getCostPhysicalSkillMedium(currentStat, currentLevel);
					break;
				case Skill.HARD:
					costo = Skill.getCostPhysicalSkillHard(currentStat, currentLevel + 1);
					delta = costo - Skill.getCostPhysicalSkillHard(currentStat, currentLevel);
					break;
			}
		}
		else
		{
			currentStat = ps.getIQ();
			switch (tipoAbilita)
			{
				case Skill.EASY:
					costo = Skill.getCostMentalSkillEasy(currentStat, currentLevel + 1);
					delta = costo - Skill.getCostMentalSkillEasy(currentStat, currentLevel);
					break;
				case Skill.MEDIUM:
				default:
					costo = Skill.getCostMentalSkillMedium(currentStat, currentLevel + 1);
					delta = costo - Skill.getCostMentalSkillMedium(currentStat, currentLevel);
					break;
				case Skill.HARD:
					costo = Skill.getCostMentalSkillHard(currentStat, currentLevel + 1);
					delta = costo - Skill.getCostMentalSkillHard(currentStat, currentLevel);
					break;
			}
		}

		// log.addEntry("Improving "+skill.getName()+" will cost "+delta+"
		// points to "+pc.getName());

		return delta;

	}

}
