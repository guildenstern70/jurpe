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

import net.littlelite.jurpe.combat.AllOutAttackType;
import net.littlelite.jurpe.combat.DefenseType;
import net.littlelite.jurpe.items.Weapon;
import net.littlelite.jurpe.system.JurpeUtils;
import net.littlelite.jurpe.system.resources.LibraryStrings;

/**
 * Base class for every Playing Character
 * 
 * 
 */
public abstract class PC extends AbstractPC implements Cloneable, Serializable
{
    // UID
    private static final long serialVersionUID = 2L;
    protected DefenseType[] activeDefenses;
    protected boolean isAlive;
    protected boolean isUnconscious;

    /**
     * New Character without MVMT rate
     * 
     * @param gca
     *            Character Attributes
     * @see net.littlelite.jurpe.characters.CharacterAttributes
     */
    protected PC(CharacterAttributes gca)
    {
        this.charAttributes = gca;
        this.isAlive = true;
        this.isUnconscious = false;
        // Active Defenses for this character. They are usually 2,
        // because 2 is the number of ADs implied in an All Out Defense command.
        this.activeDefenses = new DefenseType[2];
        this.activeDefenses[0] = DefenseType.ACTIVE_DODGE; // default
        this.activeDefenses[1] = null; // it cannot be another, because other ADs imply some items weared.
    }

    /**
     * Add or remove points from current character ht. If points added are
     * greater than or equal to HT, this function restores HT.
     * 
     * @param points
     *            If positive adds to current HT, else remove from current HT
     */
    public void addToCurrentHP(int points)
    {
        this.charAttributes.primaryStats().addToHP(points);
    }

    /**
     * Cloning function
     * 
     * @return cloned object
     */
    @Override
    public Object clone()
    {

        Object o = null;
        try
        {
            o = super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            System.err.println("PC can't clone");
        }
        return o;
    }

    /**
     * Get current character active defense
     * 
     * @return DefenseType ie: dodging, parrying, blocking
     * @see DefenseType
     */
    public DefenseType getActiveDefense()
    {
        return this.activeDefenses[0];
    }

    /**
     * Get current character active defenses, first and next preferred
     * 
     * @return DefenseType 2 out of dodging, parrying, blocking
     * @see DefenseType
     */
    public DefenseType[] getActiveDefenses()
    {
        return this.activeDefenses;
    }

    /**
     * Get active defenses type available for this character
     * 
     * @return Array of DefenseType(s) available for this PC
     * @see DefenseType
     */
    public DefenseType[] getActiveDefensesAvailable()
    {

        int numberOfDefenses = 1;
        int counter = 0;
        boolean parry = false;
        boolean block = false;

        if (this.wearsWeapon())
        {
            ++numberOfDefenses; // Character can Parry
            parry = true;
        }
        if (this.isShielded())
        {
            ++numberOfDefenses; // Character can BLOCK
            block = true;
        }

        DefenseType[] dts = new DefenseType[numberOfDefenses];
        dts[counter] = DefenseType.ACTIVE_DODGE; // always available

        if (parry)
        {
            dts[++counter] = DefenseType.ACTIVE_PARRY;
        }

        if (block)
        {
            dts[++counter] = DefenseType.ACTIVE_BLOCK;
        }

        return dts;
    }

    /**
     * All Aou Attack for this PC
     * 
     * @return current All Out Attack type
     */
    public abstract AllOutAttackType getAllOutAttackType();

    /**
     * Get current skill in unarmed combat.
     * 
     * @return skill level in bare hands combat
     */
    public abstract int getBareHandsSkill();

    /**
     * Get current skill level for current shield.
     * 
     * @return current skill level
     */
    public abstract int getCurrentShieldLevel();

    /**
     * Get current weapon, if any.
     * 
     * @return current weapon (Arma)
     */
    public abstract Weapon getCurrentWeapon();

    /**
     * Get current weapon, if any.
     * 
     * @return current weapon (Arma)
     */
    public abstract Weapon getCurrentRangedWeapon();

    /**
     * Get current skill level for current weapon.
     * 
     * @return current skill level
     */
    public abstract int getCurrentWeaponLevel();

    /**
     * Get current skill level for current ranged weapon.
     * 
     * @return current skill level
     */
    public abstract int getCurrentRangedWeaponLevel();

    /**
     * Get Damage Roll
     * 
     * @return damage roll
     */
    public abstract int getWeaponDamage();

    /**
     * Get Damage Roll
     * 
     * @return damage roll
     */
    public abstract int getRangedWeaponDamage();

    /**
     * Get Character Speed
     * 
     * @return string describing character speed.
     */
    public String getFormattedVel()
    {
        String strVel = String.valueOf(this.charAttributes.primaryStats().getSpeed());

        if (strVel.length() >= 4)
        {
            strVel.substring(0, 4);
        }

        return strVel;
    }

    /**
     * Return the HT and/or HP of character as a string.
     * 
     * @return a string with value of HT and/or HP of character
     */
    protected abstract String getHTHP();

    /**
     * Get verbose info
     * 
     * @param separator
     *            Character or string to separate (ie:\n)
     * @return character summary
     */
    public String getInfo(final String separator)
    {
        PrimaryStats ps = this.charAttributes.primaryStats();

        StringBuilder sb = new StringBuilder(this.charAttributes.getName());
        if (this.isAI())
        {
            sb.append(" (CPU)");
        }
        sb.append(separator);
        sb.append(LibraryStrings.FOR);
        sb.append(":");

        if (ps.getFatigue() > 0)
        {
            sb.append(ps.getSTminusFatigue());
            sb.append("/");
            sb.append(ps.getST());
        }
        else
        {
            sb.append(ps.getST());
        }
        sb.append(separator);
        sb.append(LibraryStrings.DEX);
        sb.append(":");
        sb.append(ps.getDX());
        sb.append(separator);
        sb.append(LibraryStrings.INT);
        sb.append(":");
        sb.append(ps.getIQ());
        sb.append(separator);
        sb.append(LibraryStrings.COS);
        sb.append(":");
        sb.append(ps.getCurrentHitPoints());
        sb.append(" (");
        sb.append(ps.getHT());
        sb.append(")");
        sb.append(separator);
        sb.append(LibraryStrings.RD);
        sb.append(":");
        sb.append(this.getDamageResistance());
        sb.append(separator);
        sb.append(LibraryStrings.PD);
        sb.append(":");
        sb.append(this.getPassiveDefense());
        sb.append(separator);
        sb.append(LibraryStrings.MVMT);
        sb.append(":");
        sb.append(ps.getMove());
        sb.append(separator);

        return sb.toString();
    }

    /**
     * Get Max Damage
     * 
     * @return maximum damage
     */
    public abstract int getWeaponMaxDamage();

    /**
     * Get current movement points
     * 
     * @return current MVMT
     */
    public abstract int getMvmt();

    /**
     * Get Character's Name
     * 
     * @return Name of character
     */
    public String getName()
    {
        return this.charAttributes.getName();
    }

    /**
     * Get Total Active Defenses: chosen defense between dodging, blocking and
     * parrying
     * 
     * @return total active defense value
     */
    public abstract float getTotalActiveDefenses();

    /**
     * Get Total Passive Defenses: armor+shield
     * 
     * @return total passive defense value
     */
    public abstract float getTotalPassiveDefenses();

    /**
     * If character is alive it returns true
     * 
     * @return true if character is alive
     */
    public abstract boolean isAlive();

    /**
     * True, if this character is wearing a weapon
     * 
     * @return True, if this character is wearing a weapon
     */
    public boolean wearsWeapon()
    {
        if (this.getCurrentWeapon() != null)
        {
            return true;
        }

        return false;
    }

    /**
     * True, if this character is wearing a ranged weapon
     * 
     * @return True, if this character is wearing a weapon
     */
    public boolean wearsRangedWeapon()
    {
        if (this.getCurrentRangedWeapon() != null)
        {
            return true;
        }

        return false;
    }

    /**
     * If character is ready to make an attack this turn.
     * 
     * @return true if character is ready to make an attack this turn.
     */
    public abstract boolean isReady();

    /**
     * Returns true if character wears a shield.
     * 
     * @return false. Can change that overriding.
     */
    public boolean isShielded()
    {
        return false;
    }

    /**
     * Returns true if character cannot move/decide actions (is unconscious)
     * 
     * @return true if character is unconscious
     */
    public boolean isUnconscious()
    {
        return this.isUnconscious;
    }

    /**
     * Subtracts points to current Health. Notice that Monsters receive damage
     * on Hit Points, not on current HT. So this method should be overriden by
     * monster classes.
     * 
     * @param points
     *            If positive adds to current HT, else remove from current HT
     */
    public void receiveDamage(int points)
    {
        this.charAttributes.primaryStats().addToHP(-points);
    }

    /**
     * Restore character Hit Points. (IE: after a rest)
     */
    public void restoreHP()
    {
        this.charAttributes.primaryStats().restoreHitPoints();
    }

    /**
     * This character executes a roll against its MaximumHT points. If failed,
     * it dies. (isAlive is set to false). Use like this:
     * 
     * <pre>
     * 
     * 
     * 
     * 
     * 
     * 
     *          int points = pc.rollForLife();
     *          if (pc.isUnconscious())
     *           // ....
     * 
     * 
     * 
     * 
     * 
     * 
     * </pre>
     * 
     * @return dice roll points for this test
     */
    public int rollForLife()
    {
        if (!JurpeUtils.successRoll(this.charAttributes.primaryStats().getHT()))
        {
            this.isUnconscious = true;
        }
        return JurpeUtils.getLatestRoll();
    }

    /**
     * Set current character active defense
     * 
     * @param dt
     *            ie: dodging, parrying, blocking
     * @see DefenseType
     */
    public void setActiveDefense(DefenseType dt)
    {
        this.activeDefenses[0] = dt;
        this.activeDefenses[1] = null;
    }

    /**
     * Set current character active defense availability. Normally, after an all
     * out attack active defenses become unavailable.
     * 
     * @param avail
     *            false, if active defenses will not be available next turn
     * @see DefenseType
     */
    public void setActiveDefenseAvailability(boolean avail)
    {
        DefenseType activeDefense = this.getActiveDefense();
        activeDefense.setAvailable(avail);
    }

    /**
     * Set current character active defense and next preferred active defense
     * 
     * @param dt
     *            2 dts out of dodging, parrying, blocking
     * @see DefenseType
     */
    public void setActiveDefenses(DefenseType[] dt)
    {
        if (dt.length >= 2)
        {
            this.activeDefenses[0] = dt[0];
            this.activeDefenses[1] = dt[1];
        }
    }

    /**
     * Set if current character is alive or not.
     * 
     * @param alive
     *            false if character is dead
     */
    public void setAlive(boolean alive)
    {
        this.isAlive = alive;
    }

    /**
     * Set unconsciousness of current character
     * 
     * @param uncon
     *            true if character has to be made unconscious
     */
    public void setUnconscious(boolean uncon)
    {
        this.isUnconscious = uncon;
    }

    /**
     * Description of this PC
     * 
     * @return PC description
     */
    @Override
    public String toString()
    {
        StringBuilder desc = new StringBuilder(this.charAttributes.getName());
        if (this.isAI())
        {
            desc.append(" CPU ");
        }
        desc.append("(");
        desc.append(LibraryStrings.HP);
        desc.append(":");
        desc.append(this.charAttributes.primaryStats().getCurrentHitPoints());
        desc.append(" ");
        desc.append(LibraryStrings.SPD);
        desc.append(":");
        desc.append(this.charAttributes.primaryStats().getSpeed());
        desc.append(")");
        return desc.toString();
    }
}
