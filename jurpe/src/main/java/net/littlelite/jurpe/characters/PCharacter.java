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
import java.util.AbstractList;
import java.util.ArrayList;

import net.littlelite.jurpe.combat.AllOutAttackType;
import net.littlelite.jurpe.combat.Defense;
import net.littlelite.jurpe.combat.DefenseType;
import net.littlelite.jurpe.containers.Inventory;
import net.littlelite.jurpe.containers.Skills;
import net.littlelite.jurpe.dialogs.DialogStatus;
import net.littlelite.jurpe.items.Armor;
import net.littlelite.jurpe.items.AbstractItem;
import net.littlelite.jurpe.items.Item;
import net.littlelite.jurpe.items.Shield;
import net.littlelite.jurpe.items.Weapon;
import net.littlelite.jurpe.system.JurpeUtils;
import net.littlelite.utils.Conversion;

/**
 * Playing Character class. This class holds information about the player. Note
 * that this is in no way related to the Avatar, which represents the current
 * position of the player in the game map.
 * 
 * 
 * @see net.littlelite.jurpe.characters.PC
 * @see net.littlelite.jurpe.dungeon.Avatar
 */
public final class PCharacter extends PC implements Serializable
{

    private static final long serialVersionUID = 3318L;
    private static final int NUMBER_OF_SKILL_FIELDS = 4;
    private float availablePoints; // total available points for this character
    private int personalScore; // score earned by this character
    private Inventory inventory; // inventory
    private Skills skillLevels; // skill levels
    private String swingDamage; // description of swing damage
    private String crushDamage; // description of crushing damage
    private AllOutAttackType allOutAttackType; // type of all out attack
    private Weapon currentWeapon; // current weapon
    private Weapon currentRangedWeapon; // current ranged weapon
    private Armor currentArmor; // current armor
    private Shield currentShield; // current shield
    private Item currentItem; // current item
    private AbstractList<PCharacterSkill> characterSkills; // current skills
    private AbstractList<DialogStatus> dialogStatus; // the dialogs took by the character
    private boolean saved; // if the character has been saved lately

    /**
     * Constructor.
     * 
     * @param ca
     *            Character Attributes (Name, DX, ST, etc)
     * @param points
     *            PCharacter Overall Available Points / total available points
     *            for this character
     * @param lvl
     *            Available Skills
     * @see net.littlelite.jurpe.characters.CharacterAttributes
     */
    public PCharacter(CharacterAttributes ca, int points, Skills lvl)
    {
        super(ca);

        this.init(points);
        skillLevels = lvl;
    }

    /**
     * Initialize PCharacter
     * 
     * @param puntiPers
     *            CharacterPoints
     * @todo this.characterSkills va inizializzato con gli Innates
     */
    private void init(int puntiPers)
    {

        this.availablePoints = puntiPers;
        this.isArtificial = false; // PCharacter is human controlled

        this.damageResistance = 0;
        this.passiveDefense = 0;

        int st = this.charAttributes.primaryStats().getST();

        swingDamage = JurpeUtils.getFormattedSwingDamage(st);
        crushDamage = JurpeUtils.getFormattedDannoThrust(st);

        allOutAttackType = AllOutAttackType.TWOATTACK;

        this.inventory = new Inventory();
        this.characterSkills = new ArrayList<PCharacterSkill>();
        this.saved = false;

        this.dialogStatus = new ArrayList<DialogStatus>();

    }

    public void addNewDialogStatus(String dialogID)
    {
        this.dialogStatus.add(new DialogStatus(dialogID));
    }
    
        public void addNewDialogStatus(String dialogID, String unit)
    {
        this.dialogStatus.add(new DialogStatus(dialogID, unit));
    }

    public DialogStatus getDialogStatus(String dialogID)
    {
        DialogStatus status = null;

        for (DialogStatus ds : this.dialogStatus)
        {
            if (ds.getDialogID().equals(dialogID))
            {
                status = ds;
                break;
            }
        }

        return status;
    }

    /**
     * If the character has been saved lately. Usually every "set" method on
     * character puts saved in false state.
     * 
     * @return True if the character has been saved
     */
    public boolean isSaved()
    {
        return this.saved;
    }

    /**
     * The character has been saved (persisted) to a file
     */
    public void setSaved()
    {
        this.saved = true;
    }

    /**
     * The character has/has not been saved to a file
     */
    public void setSaved(boolean s)
    {
        this.saved = s;
    }

    /**
     * Get Total Available Points for this PCharacter
     * 
     * @return PCharacter Points (used or not)
     */
    public float getAvailablePoints()
    {
        return this.availablePoints;
    }

    /**
     * Get Unspent PCharacter Points. Difference between Total Points and Spent
     * Points.
     * 
     * @return Unspent character points.
     */
    public float getUnspentPoints()
    {
        return (this.availablePoints - this.getSpentPoints());
    }

    /**
     * Get Spent Points for this character. It computes current points "value"
     * of this character.
     * 
     * @return Spent PCharacter Points
     */
    public float getSpentPoints()
    {

        // Basic features:
        float points = JurpeUtils.computeCharacterPoints(this);

        // Add Skill Points:
        for (PCharacterSkill characterSkill : this.characterSkills)
        {
            int level = characterSkill.getLevel();
            Skill sk = characterSkill.getSkill();
            int difficulty = sk.getIntDifficulty();
            float pointsToAdd = 0f;
            if (sk.isPhysical())
            {
                int dx = this.charAttributes.primaryStats().getDX();

                // To do: not always Physical skill depends on dx!
                if (difficulty == Skill.EASY)
                {
                    pointsToAdd = Skill.getCostPhysicalSkillEasy(dx, level);
                }
                else if (difficulty == Skill.MEDIUM)
                {
                    pointsToAdd = Skill.getCostPhysicalSkillMedium(dx, level);
                }
                else
                {
                    pointsToAdd = Skill.getCostPhysicalSkillHard(dx, level);
                }
            }
            else
            {
                int iq = this.charAttributes.primaryStats().getIQ();

                if (difficulty == Skill.EASY)
                {
                    pointsToAdd = Skill.getCostMentalSkillEasy(iq, level);
                }
                else if (difficulty == Skill.MEDIUM)
                {
                    pointsToAdd = Skill.getCostMentalSkillMedium(iq, level);
                }
                else
                {
                    pointsToAdd = Skill.getCostMentalSkillHard(iq, level);
                }
            }

            points += pointsToAdd;
        }

        return points;
    }

    /**
     * Add points to available experience level.
     * 
     * @param points
     *            to add to experience level.
     */
    public void addToAvailablePoints(float points)
    {
        this.availablePoints += points;
    }

    /**
     * Add points to current score for this character.
     * 
     * @param points
     *            Points to add
     */
    public void addToScore(int points)
    {
        this.personalScore += points;
    }

    /**
     * Get current score for this character.
     * 
     * @return score points.
     */
    public int getScore()
    {
        return this.personalScore;
    }

    /**
     * Every character must choose a type of AllOutAttack.
     * 
     * @return type of all out attack for this character
     * @see AllOutAttackType
     */
    public AllOutAttackType getAllOutAttackType()
    {
        return this.allOutAttackType;
    }

    /**
     * Use this function to change current type of AllOutAttack
     * 
     * @param all
     *            All Out attack for this character
     * @see AllOutAttackType
     */
    public void setAllOutAttackType(AllOutAttackType all)
    {
        this.allOutAttackType = all;
    }

    /**
     * Every character must choose a sex
     * 
     * @return sex for this character
     * @see SexType
     */
    public SexType getSexType()
    {
        if (this.charAttributes.getSex() == 'M')
        {
            return SexType.MALE;
        }

        if (this.charAttributes.getSex() == 'F')
        {
            return SexType.FEMALE;
        }

        return SexType.UNKNOWN;
    }

    /**
     * Set available skills for this game. Use it when restoring character after
     * serialization.
     * 
     * @param livelli
     *            Current livelli active reference.
     */
    public void setSkills(Skills livelli)
    {
        this.saved = false;
        this.skillLevels = livelli;
    }

    /**
     * Get PCharacter Skill Level
     * 
     * @param nomeAbilita
     *            name of skill
     * @return Skill Level
     */
    public int getSkillLevel(String nomeAbilita)
    {

        int livello = 0;
        PCharacterSkill ap = this.getCharacterSkill(nomeAbilita);

        /* Get default level if character did not learned skill */
        if (ap == null)
        {

            Skill abLivello = this.skillLevels.forName(nomeAbilita);
            int base = abLivello.getIntBase();
            int modif = abLivello.getModifier();

            switch (base)
            {

                case Skill.HT_BASED:
                    livello = this.charAttributes.primaryStats().getHT() + modif;
                    break;

                case Skill.IQ_BASED:
                    livello = this.charAttributes.primaryStats().getIQ() + modif;
                    break;

                case Skill.DX_BASED:
                    livello = this.charAttributes.primaryStats().getDX() + modif;
                    break;

                case Skill.ST_BASED:
                    livello = this.charAttributes.primaryStats().getST() + modif;
                    break;

                default:
                    livello = 0;
            }
        }
        else
        {
            // get character level
            livello = ap.getLevel();
        }

        return livello;

    }

    /**
     * Get a Skill learned by this character (PCharacterSkill)
     * 
     * @param skill
     *            generic skill
     * @return PCharacterSkill object. If character did not learned this skill,
     *         it returns null.
     * @see PCharacterSkill
     */
    public PCharacterSkill getCharacterSkill(Skill skill)
    {
        return this.getCharacterSkill(skill.getName());
    }

    /**
     * Get a Skill learned by this character (PCharacterSkill)
     * 
     * @param name
     *            name of skill
     * @return PCharacterSkill object. If character did not learned this skill,
     *         it returns null.
     * @see PCharacterSkill
     */
    public PCharacterSkill getCharacterSkill(String name)
    {

        PCharacterSkill ab = null;

        for (PCharacterSkill abs : this.characterSkills)
        {
            if (abs.getName().equals(name))
            {
                ab = abs;
                break;
            }
        }

        return ab;
    }

    /**
     * Improve Skill Level
     * 
     * @param name
     *            name of skill
     * @param cost
     *            cost in character point to spend to get this skill improved
     * @return true if skill was correctly improved
     */
    public boolean improveSkill(String name, float cost)
    {

        PCharacterSkill ab = getCharacterSkill(name);
        return this.improveSkill(ab, cost);
    }

    /**
     * Improve Skill Level
     * 
     * @param skill
     *            Character's skill to improve
     * @param cost
     *            cost in character point to spend to get this skill improved
     * @return true if skill was correctly improved
     */
    public boolean improveSkill(PCharacterSkill skill, float cost)
    {
        // PCharacter will have some unspent points if skill is improved
        if (areThereEnoughPointsToLearnSkill(cost))
        {
            skill.improveSkillLevel(1); // improve skill
            // Unspent points will be automatically computed in
            // this.getUnspentPoints.
            return true;
        }

        return false;
    }

    /**
     * Set current weapon
     * 
     * @param arma
     *            Weapon (Weapon) object
     * @return true, if weapon is now the current weapon
     * @see Weapon
     */
    public boolean setCurrentWeapon(Weapon arma)
    {
        this.saved = false;

        if (arma != null)
        {
            if (arma.isRanged())
            {
                return this.setCurrentRangedWeapon(arma);
            }

            // Check minimum strength required
            if (arma.getMinimalStrength() > this.charAttributes.primaryStats().getST())
            {
                return false;
            }
        }

        this.currentWeapon = arma;

        return true;
    }

    /**
     * Set current ranged weapon
     * 
     * @param rWeapon
     *            Ranged weapon
     * @return true, if weapon is now the current weapon
     * @see Weapon
     */
    public boolean setCurrentRangedWeapon(Weapon rWeapon)
    {
        this.saved = false;

        if (rWeapon != null)
        {
            // Check minimum strength required
            if (rWeapon.getMinimalStrength() > this.charAttributes.primaryStats().getST())
            {
                return false;
            }

            if (rWeapon.isRanged())
            {
                this.currentRangedWeapon = rWeapon;
            }
        }
        else
        {
            this.currentRangedWeapon = null;
        }

        return true;
    }

    /**
     * Get current weapon
     * 
     * @return arma Weapon (Weapon) object
     * @see Weapon
     */
    public Weapon getCurrentWeapon()
    {
        return this.currentWeapon;
    }

    /**
     * Get current ranged weapon
     * 
     * @return Current ranged weapon
     */
    public Weapon getCurrentRangedWeapon()
    {
        return this.currentRangedWeapon;
    }

    /**
     * Get current weapon skill level
     * 
     * @return skill level
     * @see Weapon
     */
    public int getCurrentWeaponLevel()
    {
        String abilitaWeapon;
        int livello = 0;

        if (this.currentWeapon != null)
        {
            abilitaWeapon = this.currentWeapon.getSkill().toString();
            livello = this.getSkillLevel(abilitaWeapon);
        }

        return livello;
    }

    /**
     * Get current weapon skill level
     * 
     * @return skill level
     * @see Weapon
     */
    public int getCurrentRangedWeaponLevel()
    {
        String abilitaWeapon;
        int livello = 0;

        if (this.currentRangedWeapon != null)
        {
            abilitaWeapon = this.currentRangedWeapon.getSkill().toString();
            livello = this.getSkillLevel(abilitaWeapon);
        }

        return livello;
    }

    /**
     * Get current shield skill level
     * 
     * @return skill level with shield
     * @see Shield
     */
    public int getCurrentShieldLevel()
    {
        int livello = 0;

        if (this.currentShield != null) // character wields a shield
        {
            PCharacterSkill ap = this.getCharacterSkill("Shield");
            if (ap == null)
            {
                livello = (this.charAttributes.primaryStats().getDX() - 4);
            // default skill for shield
            }
            else
            {
                livello = ap.getLevel();
            }
        }

        return livello;
    }

    /**
     * Get current merchant skill level
     * 
     * @return skill level in buying/selling items.
     */
    public int getCurrentMerchantLevel()
    {
        int livello = 0;

        PCharacterSkill ap = this.getCharacterSkill("Merchant");
        if (ap == null)
        {
            livello = (this.charAttributes.primaryStats().getIQ() - 5);
        // default skill for merchant
        }
        else
        {
            livello = ap.getLevel();
        }

        return livello;
    }

    /**
     * You are ready if: - you are unarmed - you are using a weapon which is
     * ready
     * 
     * @return true, if character is ready to attack
     */
    public boolean isReady()
    {

        boolean isReady = false;

        // Unconscious character is always unready!
        if (this.isUnconscious)
        {
            return false;
        }

        // Bare combat is always ready
        if (this.wearsWeapon())
        {
            isReady = this.getCurrentWeapon().isReady();

        }
        else
        {
            isReady = true;
        }

        return isReady;

    }

    /* Armor */
    /**
     * Set current armor. (PCharacter wears armor).
     * 
     * @param armatura
     *            Armor (Armor) object
     * @return true, if character can wear armor
     * @see Armor
     */
    public boolean setCurrentArmor(Armor armatura)
    {

        this.saved = false;

        if (armatura != null)
        {
            // Update DP
            this.passiveDefense += armatura.getPD();
            // Update RD
            this.damageResistance += armatura.getDR();
        }
        else
        {
            this.passiveDefense -= this.currentArmor.getPD();
            this.damageResistance -= this.currentArmor.getDR();
        }
        currentArmor = armatura;
        return true;
    }

    /**
     * Get current armor. (PCharacter wears armor).
     * 
     * @return Armor (Armor) object
     * @see Armor
     */
    public Armor getCurrentArmor()
    {
        return currentArmor;
    }

    /* Shield */
    /**
     * Set current shield (character wears shield). If null, character unwears
     * shield.
     * 
     * @param scudo
     *            Shield (Shield) object
     * @return true, if character can wear shield
     * @see Shield
     */
    public boolean setCurrentShield(Shield scudo)
    {
        this.saved = false;

        // Update DP
        if (scudo != null)
        {
            this.passiveDefense += scudo.getPassiveDefense();
        }
        else
        {
            this.passiveDefense -= this.currentShield.getPassiveDefense();
        }
        currentShield = scudo;
        return true;
    }

    /**
     * Get current shield (character wears shield)
     * 
     * @return Shield (Shield) object
     * @see Shield
     */
    public Shield getCurrentShield()
    {
        return currentShield;
    }

    /**
     * Playing characters have HP=HT
     * 
     * @return initial Hit Points
     */
    public int getMaximumHP()
    {
        return this.charAttributes.primaryStats().getInitialHitPoints();
    }

    /**
     * @todo IMPLEMENT GRIMOIRE
     * @return
     */
    public boolean hasGrimoire()
    {
        boolean hasIt = false;
        if (this.inventory.count() > 0)
        {
            for (AbstractItem item : this.inventory.getInventoryItems())
            {
                if (item.getName().endsWith("grimoire"))
                {
                    hasIt = true;
                    break;
                }
            }
        }
        return hasIt;
    }

    /**
     * Returns true if character wears a shield.
     * 
     * @return true, if character wears a shield.
     */
    @Override
    public boolean isShielded()
    {
        if (this.currentShield != null)
        {
            return true;
        }

        return false;
    }

    /**
     * Set current item. If item is null, character unwears it.
     * 
     * @param item
     *            Item object
     * @see Item
     * @return true if item was correctly set
     */
    public boolean setCurrentItem(Item item)
    {
        this.saved = false;
        currentItem = item;
        return true;
    }

    /**
     * Get current item
     * 
     * @return item Item object
     * @see Item
     */
    public Item getCurrentItem()
    {
        return this.currentItem;
    }

    /**
     * Get current Swing Damage (description)
     * 
     * @return printable Swing Damage
     */
    public String getSwingDamage()
    {
        return swingDamage;
    }

    /**
     * Get current Thrust Damage (description)
     * 
     * @return printable Thrust Damage
     */
    public String getCrushDamage()
    {
        return crushDamage;
    }

    /**
     * Encumbrance level is a measure of weight relative to strength. Since
     * original Jurpe rules define encumbrance level in terms of lb, a
     * conversion is defined.
     * 
     * @return encumbrance level
     */
    public int getEncumbranceLevel()
    {
        int encumbranceLevel = 0;
        int cst = this.charAttributes.primaryStats().getSTminusFatigue();
        int weightKg = this.getEncumbrance();
        int wlb = Conversion.lbs2kg(weightKg);
        if (wlb > 2 * cst && wlb <= 4 * cst)
        {
            encumbranceLevel = 1;
        }
        else if (wlb > 4 * cst && wlb <= 6 * cst)
        {
            encumbranceLevel = 2;
        }
        else if (wlb > 6 * cst && wlb <= 12 * cst)
        {
            encumbranceLevel = 3;
        }
        else if (wlb > 12 * cst && wlb <= 20 * cst)
        {
            encumbranceLevel = 4;
        }
        else if (wlb > 20 * cst)
        {
            encumbranceLevel = 9; // say infinite
        }

        return encumbranceLevel;
    }

    /**
     * Get MVMT (Movement).
     * 
     * @return character movement
     */
    public int getMvmt()
    {

        int mvmt = this.charAttributes.primaryStats().getMove();

        // Weight modifiers
        mvmt -= this.getEncumbranceLevel();

        // Injury modifiers
        int hp = this.charAttributes.primaryStats().getCurrentHitPoints();
        if (hp > 0 && hp <= 3)
        {
            mvmt /= 2;
        }
        if (this.isUnconscious())
        {
            mvmt = 0;
        }

        return Math.max(0, mvmt);
    }

    /**
     * Get character parry points
     * 
     * @return character parry points
     */
    public int getParryPoints()
    {
        Defense defense = new Defense(this);
        return defense.getActiveDefensePoints(DefenseType.ACTIVE_PARRY);
    }

    /**
     * Get character dodge points
     * 
     * @return character parry points
     */
    public int getDodgePoints()
    {
        Defense defense = new Defense(this);
        return defense.getActiveDefensePoints(DefenseType.ACTIVE_DODGE);
    }

    /**
     * Get character block points
     * 
     * @return character parry points
     */
    public int getBlockPoints()
    {
        Defense defense = new Defense(this);
        return defense.getActiveDefensePoints(DefenseType.ACTIVE_BLOCK);
    }

    /**
     * Get character encumbrance
     * 
     * @return encumbrance
     */
    public int getEncumbrance()
    {
        int ing = this.inventory.getEncumbrance();
        if (currentWeapon != null)
        {
            ing += currentWeapon.getWeight();
        }
        if (currentArmor != null)
        {
            ing += currentArmor.getWeight();
        }
        if (currentShield != null)
        {
            ing += currentShield.getWeight();
        }
        if (this.currentItem != null)
        {
            ing += this.currentItem.getWeight();
        }
        return ing;
    }

    /**
     * Get reference to character inventory
     * 
     * @return Inventory (Inventory) object
     * @see Inventory
     */
    public Inventory getInventory()
    {
        return this.inventory;
    }

    /* Skills */
    public boolean hasInnate(String innate)
    {
        boolean flag = false;

        AbstractList<Innate> innates = this.getCharacterAttributes().innates();
        for (Innate inn : innates)
        {
            if (inn.getName().startsWith(innate))
            {
                flag = true;
                break;
            }
        }

        return flag;
    }

    public boolean hasSkill(PCharacterSkill skill)
    {
        boolean flag = false;

        for (PCharacterSkill sk : this.characterSkills)
        {
            if (sk.equals(skill))
            {
                flag = true;
                break;
            }
        }

        return flag;
    }

    /**
     * Add skill to character. Checks if a is not yet learned
     * 
     * @param a
     *            PCharacter Skill (PCharacterSkill)
     * @param cost
     *            Cost of skill to learn
     * @return true, if character succesfully learned this skill
     * @see PCharacterSkill
     */
    public boolean learnSkill(PCharacterSkill a, float cost)
    {
        boolean flag = false;

        if (!this.hasSkill(a))
        {
            // Check if character has enough points to learn skill.
            if (areThereEnoughPointsToLearnSkill(cost))
            {
                characterSkills.add(a);
                flag = true;
            }
        }

        return flag;
    }

    /**
     * Get all skills learned by this character and their name and level.
     * Displayable by a grid.
     * 
     * @return array [ability][properties]
     */
    public Object[][] getAllSkills()
    {

        int howmany = this.getNrSkills();
        Object[][] abs = new Object[howmany][NUMBER_OF_SKILL_FIELDS];
        int k = -1;

        for (PCharacterSkill ab : this.characterSkills)
        {
            abs[++k] = ab.toStrings();
        }

        return abs;
    }

    /**
     * Get how many skills this character learned.
     * 
     * @return number of skill learned
     */
    public int getNrSkills()
    {
        return characterSkills.size();
    }

    /**
     * Get total passive defense.
     * 
     * @return sum of RD and DP for this character
     */
    public float getTotalPassiveDefenses()
    {
        Defense def = new Defense(this);
        return def.getPassiveDefensePoints();
    }

    /**
     * Get total active defense for this character. Total active defenses can't
     * be unavailable more than one time. This function restores availability of
     * active defenses.
     * 
     * @return defense based on dodge/parry/block.
     */
    public float getTotalActiveDefenses()
    {
        int activePoints = 0;
        Defense def = new Defense(this);
        activePoints = def.getActiveDefensePoints();
        DefenseType activeD = this.getActiveDefense();
        if (!activeD.isAvailable())
        {
            activeD.setAvailable(true);
        }
        return activePoints;
    }

    /**
     * Get Damage, after having made a success attack. Damage is based on
     * current weapon mode and current character strength
     * 
     * @return Damage value
     */
    public int getWeaponDamage()
    {
        if (this.currentWeapon == null)
        {
            return 0;
        }

        return this.currentWeapon.getBasicDamage(this.charAttributes.primaryStats().getSTminusFatigue());
    }

    /**
     * Get maximum damage. All rolls at 6.
     * 
     * @return Critical Damage Value
     */
    public int getWeaponMaxDamage()
    {
        if (this.currentWeapon == null)
        {
            return 0;
        }
        return this.currentWeapon.getMaxDamage(this.charAttributes.primaryStats().getSTminusFatigue());
    }

    /**
     * Get Damage, after having made a success attack. Damage is based on
     * current weapon mode and current character strength
     * 
     * @return Damage value
     */
    public int getRangedWeaponDamage()
    {
        if (this.currentRangedWeapon == null)
        {
            return 0;
        }
        return this.currentRangedWeapon.getBasicDamage(this.charAttributes.primaryStats().getSTminusFatigue());
    }

    /**
     * Get maximum damage. All rolls at 6.
     * 
     * @return Critical Damage Value
     */
    public int getRangedWeaponMaxDamage()
    {
        if (this.currentRangedWeapon == null)
        {
            return 0;
        }
        return this.currentRangedWeapon.getMaxDamage(this.charAttributes.primaryStats().getSTminusFatigue());
    }

    /**
     * Get current skill in unarmed combat.
     * 
     * @return 0
     */
    public int getBareHandsSkill()
    {
        return 0;
    }

    /**
     * If there are enough points to learn a Skill that costs cost points
     * 
     * @param cost
     *            cost of the skill to learn
     * @return true, if character has enough points.
     */
    protected boolean areThereEnoughPointsToLearnSkill(float costo)
    {

        float puntiRimanenti = this.getUnspentPoints() - costo;

        // PCharacter will have some unspent points if skill is improved
        if (puntiRimanenti >= 0.0)
        {
            return true;
        }

        return false;
    }

    /**
     * A character is alive if: <br>
     * <ul>
     * <li>Has HT>0
     * <li>Failed a roll on MaximumHT if CurrentHO <=0
     * <li>Lost a total of 1 times MaximumHT
     * </ul>
     * 
     * @return false if character is dead
     */
    public boolean isAlive()
    {
        if (this.charAttributes.primaryStats().getCurrentHitPoints() < -this.charAttributes.primaryStats().getHT())
        {
            this.isAlive = false;
        }

        return this.isAlive;
    }

    /**
     * When character IS BUYING an item, he will be asked for this cost because
     * of his Merchant skill
     * 
     * @param item
     *            Item to buy
     * @return cost of item
     */
    public int getCustomizedCost(AbstractItem item)
    {
        double costoBasicItem = item.getCost();

        // Merchant modifier
        int merchantLevel = this.getCurrentMerchantLevel();
        if (merchantLevel > 10)
        {
            costoBasicItem = Math.round(costoBasicItem * (1 - (merchantLevel - 10) * 0.1));
            costoBasicItem = Math.max(costoBasicItem * 0.5, costoBasicItem);
        }
        else if (merchantLevel < 8)
        {
            costoBasicItem = Math.round(costoBasicItem * (1 + 0.1 * (8 - merchantLevel)));
            costoBasicItem = Math.min(costoBasicItem * 1.5, costoBasicItem);
        }

        return Math.round((long) costoBasicItem);
    }

    /**
     * When character IS SELLING an item, he will be offered this price because
     * of his Merchant skill
     * 
     * @param item
     *            Item to sell
     * @return price of item
     */
    public int getCustomizedPrice(AbstractItem item)
    {
        double costoBasicItem = item.getCost();

        // Merchant modifier
        int merchantLevel = this.getCurrentMerchantLevel();
        if (merchantLevel > 10)
        {
            costoBasicItem = Math.round(costoBasicItem * (1 + (merchantLevel - 10) * 0.1));
            costoBasicItem = Math.min(costoBasicItem * 1.5, costoBasicItem);
        }
        else if (merchantLevel < 8)
        {
            costoBasicItem = Math.round(costoBasicItem * (1 - 0.1 * (8 - merchantLevel)));
            costoBasicItem = Math.max(costoBasicItem * 0.5, costoBasicItem);
        }

        return Math.round((long) costoBasicItem);
    }

    /**
     * If a character is human, he will have his HT value (ie: 12) If a
     * character is a beast, he will have HT in the form HT/current HP (ie:
     * 12/16)
     * 
     * @return String describing HT/HP
     */
    public String getHTHP()
    {
        return String.valueOf(this.getPrimaryStats().getHT());
    }

    /**
     * @param f
     */
    public void setAvailablePoints(float f)
    {
        availablePoints = f;
    }

    /**
     * Player personal score
     * 
     * @return Player personal score
     */
    public int getPersonalScore()
    {
        return personalScore;
    }

    /**
     * Set player personal score
     * 
     * @param i
     *            Player score
     */
    public void setPersonalScore(int i)
    {
        personalScore = i;
    }

    /**
     * Set player inventory
     * 
     * @param inventory
     *            Inventory
     */
    public void setInventory(Inventory inventory)
    {
        this.inventory = inventory;
    }

    /**
     * Get player's skills
     * 
     * @return Player's skills
     */
    public Skills getSkillLevels()
    {
        return skillLevels;
    }

    /**
     * Set player skill levels
     * 
     * @param skills
     *            Skill levels
     */
    public void setSkillLevels(Skills skills)
    {
        skillLevels = skills;
    }

    /**
     * @param string
     */
    public void setSwingDamage(String string)
    {
        swingDamage = string;
    }

    /**
     * @param string
     */
    public void setCrushDamage(String string)
    {
        crushDamage = string;
    }
}
