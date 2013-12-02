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
import net.littlelite.jurpe.characters.CharacterAttributes;
import net.littlelite.jurpe.characters.Monster;
import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.characters.Skill;
import net.littlelite.jurpe.combat.DualCombat;
import net.littlelite.jurpe.dungeon.Level;
import net.littlelite.jurpe.game.session.HighScores;
import net.littlelite.jurpe.items.AbstractItem;
import net.littlelite.jurpe.system.commands.CombatCommands;
import net.littlelite.jurpe.system.commands.CreationCommands;
import net.littlelite.jurpe.system.commands.PersistenceCommands;
import net.littlelite.jurpe.system.commands.SkillCommands;
import net.littlelite.jurpe.system.generation.ItemValueType;
import net.littlelite.jurpe.system.resources.LibraryStrings;

/**
 * Core related commands
 */
public abstract class CoreCommands extends CoreHandles
{

    protected abstract void init() throws JurpeException;

    /**
     * Executes combat command(s).
     * 
     * @param cs
     *            CommandString to execute
     * @return String with results in output (log)
     * @see CommandSequence
     */
    public boolean executeCommand(CommandSequence cs)
    {
        boolean exitCombat = cs.execute();
        this.log.addEntry(cs.getLog());
        return exitCombat;
    }

    /**
     * Get how much points a Skill costs it's valued, or the initial points
     * needed to learn a skill
     * 
     * @param skillName
     *            Skill name
     * @param level
     *            Skill level
     * @return points needed
     * @see Skill
     */
    public float getSkillCost(String skillName, int level)
    {
        Skill cSkill = this.levels.forName(skillName);
        return SkillCommands.getSkillCost(cSkill, this.pc, level);
    }

    /**
     * Get how much points an improvement of one level for a Skill costs
     * 
     * @param skillName
     *            name of Skill
     * @return points needed
     * @see Skill
     */
    public float getImprovingSkillCost(String skillName)
    {
        Skill abilitaSelezionata = this.levels.forName(skillName);
        int livelloAttualeAbilita = this.pc.getSkillLevel(skillName);
        return SkillCommands.getImprovingSkillCost(this.pc, livelloAttualeAbilita, abilitaSelezionata);
    }

    /**
     * Add Skill to Playing Character. When character learns a new skill, this
     * is automatically improved until this operation costs some character
     * points (until improving it's free).
     * 
     * @param skillToAdd
     *            Skill to Add to Playing Character
     * @return false if skill was already learned by character.
     */
    public boolean addSkill(Skill skillToAdd)
    {
        return SkillCommands.addSkill(this.getPC(), skillToAdd, true);
    }

    /**
     * Get First Aid regained health points.
     * 
     * @param player
     *            PCharacter to be aided
     * @param sk
     *            PCharacter Skill Level in First Aid
     * @param tl
     *            Tech Level of the Game World
     * @return gained HP
     */
    public int firstAid(PCharacter player, int sk, int tl)
    {
        return SkillCommands.firstAid(player, sk, tl);
    }

    /**
     * Generate a random item
     * 
     * @param ivt
     *            Rarity of item to generate
     * @return A newly created random item
     */
    public AbstractItem generateItem(ItemValueType ivt) throws JurpeException
    {
        return CreationCommands.generateItem(ivt, this.names);
    }

    /**
     * Creates a new Playing Character
     * 
     * @param crpc
     *            Main character attributes for the character to create.
     */
    public void generatePC(CharacterAttributes crpc)
    {
        pc = CreationCommands.generatePC(crpc, this.levels);
    }

    /**
     * Creates a random monster, with a random name.
     * 
     * @param value
     *            total starting points assigned to the new character
     * @return newly generated monster
     */
    public Monster generateMonster(int value)
    {
        return CreationCommands.generateMonster(value, this.names);
    }

    /**
     * Serialize Playing Character and his references (Inventory, Skills and
     * such)
     * 
     * @return true if character has been correctly saved
     */
    public boolean savePC()
    {
        return this.savePC(Config.SYSTEMFILE);
    }

    /**
     * Serialize Playing Character and his references (Inventory, Skills and
     * such) Extension is taken from Config file.
     * 
     * @param namepath
     *            complete path where you want to save your character, without
     *            extension.
     * @return true if character has been correctly saved.
     */
    public boolean savePC(String namepath)
    {
        if (this.isPCgenerated())
        {
            return PersistenceCommands.savePC(this.pc, namepath);
        }

        return false;
    }

    /**
     * Restore serialized PCharacter.
     * 
     * @param pcName
     *            complete path to file with extension.
     * @return true if character has been correctly restored.
     * @throws JurpeException
     */
    public boolean loadPC(String pcName) throws JurpeException
    {
        boolean loadedOk = true;

        this.init();
        this.pc = PersistenceCommands.loadPC(pcName);

        // Updates skills and waves of monsters
        if (this.pc != null)
        {
            this.pc.setSkills(this.levels);
        }
        else
        {
            loadedOk = false;
        }

        return loadedOk;
    }

    /**
     * Loads High Score Table. If it's not present, it loads a default one.
     */
    public void loadHighScores()
    {
        this.highScores = PersistenceCommands.loadHighScores();
    }

    /**
     * Serialize High Scores
     */
    public void saveHighScores()
    {
        PersistenceCommands.saveHighScores(this.highScores);
    }

    /**
     * Saves high score
     */
    public void enterHighScore()
    {
        this.highScores.addToScore(this.pc.getName(), this.pc.getScore());
    }

    /**
     * Starts a combat session. Updates TurnTable
     * 
     * @param monsterName
     *            Name of the monster to fight
     * @throws JurpeException
     */
    public void enterCombatWith(String monsterName) throws JurpeException
    {
        PCharacter fighter = this.getPC();
        Level level = this.dungeon.getCurrentLevel();

        fighter.setSaved(false);
        Monster monsterToFight = level.getMonsters().getByName(monsterName);
        if (this.getPC() == null)
        {
            throw new JurpeException("PC character null");
        }

        if (monsterToFight != null)
        {
            this.combat = CombatCommands.enterCombat(fighter, monsterToFight);
        }

    }

    /**
     * After having initialized Combat and TurnTable objects, this method return
     * false until combat is ended.
     * 
     * @return true, if next player is human (control is returned to GUI) or
     *         combat is ended
     */
    public boolean continueCombat() throws JurpeException
    {
        DualCombat cbt = (DualCombat) this.combat;
        // If either combatant died, we return control to GUI
        if (!cbt.isAlive())
        {
            this.afterCombat();
            return true;
        }

        return CombatCommands.continueCombat(cbt);
    }

    /**
     * Reset current Playing Character. Usually when PC dies.
     */
    public void cancelCharacter()
    {
        this.pc = null;
        this.dungeon.getAvatar().resetAvatar();
        this.magesGuild.synchronizeUnit(); // Reset mages guild dialogs
        this.dungeon.reset();
    }

    /**
     * Update monsters on level
     * 
     * @throws JurpeException
     */
    public void updateMonsters() throws JurpeException
    {
        // Refresh monsters
        Level currentLevel = this.getDungeon().getCurrentLevel();
        if (currentLevel.getMonsters().updateLiving())
        {
            // Save only if the number actually changed
            if (!currentLevel.save())
            {
                this.getLog().addEntry("Cannot update level " + currentLevel.getZ() + " on disk. Why?");
            }
        }
    }

    /** ******* PRIVATE ************* */
    /**
     * Actions to take when the combat is over. Notice that every won combat,
     * the PC loses 1 fatigue point.
     */
    private void afterCombat() throws JurpeException
    {
        PCharacter humanPC = this.getPC();

        if (humanPC.isAlive())
        {
            // player loses fatigue
            humanPC.getPrimaryStats().addFatigue(1);
            // monster is defeated
            CombatCommands.monsterDefetead((DualCombat) this.combat);
            // Refresh monsters
            this.updateMonsters();
        }
        else
        {
            this.playerDies();
        }

    }

    /**
     * Procedure of current character death.
     */
    public void playerDies()
    {
        PCharacter humanPC = this.getPC();
        String pcName = humanPC.getName();
        int points = humanPC.getScore();

        // Human lost
        this.log.addEntry(LibraryStrings.YOUDIED);
        // High Score?
        this.log.addEntry(LibraryStrings.YOUSCORED + points);
        HighScores hs = this.getHighScores();
        if (hs.addToScore(pcName, points))
        {
            this.log.addEntry(LibraryStrings.YOUDIDIT);
            // Saves High Scores
            this.saveHighScores();
        }
        else
        {
            this.log.addEntry(LibraryStrings.YOUDIDNTIT);
        }

        // Reset character
        this.cancelCharacter();

        // Human lost
        this.log.addEntry(LibraryStrings.YOUDIED);

    }
}
