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
import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.combat.DualCombat;
import net.littlelite.jurpe.combat.ICombat;
import net.littlelite.jurpe.containers.MasterShop;
import net.littlelite.jurpe.containers.Skills;
import net.littlelite.jurpe.dungeon.Dungeons;
import net.littlelite.jurpe.game.MagesGuild;
import net.littlelite.jurpe.game.session.HighScores;
import net.littlelite.jurpe.system.generation.RandomNames;
import net.littlelite.jurpe.world.GameWorld;
import net.littlelite.jurpe.world.Inn;
import net.littlelite.jurpe.world.Shop;

/**
 * Handles to other objects in the game.
 */
public abstract class CoreHandles
{

    protected GameWorld world;
    protected PCharacter pc;
    protected MasterShop allItems;
    protected Shop mainShop;
    protected Skills levels;
    protected Log log;
    protected OSProps os;
    protected TurnTable tt;
    protected Inn inn;
    protected RandomNames names;
    protected ICombat combat;
    protected HighScores highScores;
    protected Dungeons dungeon;
    protected MagesGuild magesGuild;

    /**
     * Handle to character
     * 
     * @return current playing character
     */
    public PCharacter getPC()
    {
        return this.pc;
    }

    /**
     * Handle to dungeon
     * 
     * @return Dungeon
     */
    public Dungeons getDungeon()
    {
        return this.dungeon;
    }

    /**
     * Handle to inn
     * 
     * @return current Inn
     */
    public Inn getInn()
    {
        return this.inn;
    }

    public MagesGuild getMagesGuild()
    {
        return this.magesGuild;
    }

    /**
     * Turn table is a metaphor for a table in which PCs and NPCs sit and play.
     * 
     * @return current turn table
     * @see TurnTable
     */
    public TurnTable getTurnTable()
    {
        TurnTable turns = this.tt;
        if (turns == null)
        {
            DualCombat dc = (DualCombat) this.combat;
            return dc.getTurnTable();
        }
        return turns;
    }

    /**
     * Combat is the reference to active combat, if there is one. To create a
     * combat, select enterCombat... function.
     * 
     * @return null if no combat is taking place, else a reference to Combat
     *         object
     */
    public ICombat getCombat()
    {
        return this.combat;
    }

    /**
     * Gets reference to High Scores Table
     * 
     * @return High Score Table
     */
    public HighScores getHighScores()
    {
        HighScores tmp = this.highScores;
        if (tmp == null)
        {
            tmp = new HighScores();
        }
        return tmp;
    }

    /**
     * Handle to current log
     * 
     * @return Log object
     */
    public Log getLog()
    {
        return this.log;
    }

    /**
     * Handle to available Skills for this game
     * 
     * @return Livelli object
     * @see Livelli
     */
    public Skills getSkills()
    {
        return this.levels;
    }

    /**
     * Handle to Shop
     * 
     * @return Shop object
     * @see Shop
     */
    public Shop getShop()
    {
        return this.mainShop;
    }

    /**
     * Handle to Game World
     * 
     * @return a Game World handle
     */
    public GameWorld getGameWorld()
    {
        return this.world;
    }

    /**
     * If a Playing Character has been generated
     * 
     * @return true if system has a valid Playing PCharacter reference
     */
    public boolean isPCgenerated()
    {
        boolean isPCgen = true;
        if (this.pc == null)
        {
            isPCgen = false;
        }
        return isPCgen;
    }

    /**
     * Handle to Operating System Properties
     * 
     * @return OSProps object
     * @see OSProps
     */
    public OSProps operatingSystem()
    {
        return os;
    }
}
