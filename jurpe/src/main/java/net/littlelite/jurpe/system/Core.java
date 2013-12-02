package net.littlelite.jurpe.system;

/**
J.U.R.P.E. @version@
Copyright (C) LittleLite Software
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
import java.io.File;
import java.io.Serializable;
import java.util.Random;

import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.containers.MasterShop;
import net.littlelite.jurpe.containers.Skills;
import net.littlelite.jurpe.dungeon.Dungeons;
import net.littlelite.jurpe.game.MagesGuild;
import net.littlelite.jurpe.system.generation.ItemValueType;
import net.littlelite.jurpe.system.generation.RandomNames;
import net.littlelite.jurpe.system.resources.LibraryStrings;
import net.littlelite.jurpe.world.GameTime;
import net.littlelite.jurpe.world.GameWorld;
import net.littlelite.jurpe.world.Inn;
import net.littlelite.jurpe.world.Shop;

/**
 * Contains references to every object in the game.
 * 
 * @see GameWorld
 * @see CoreCommands
 * @see CoreHandles
 * @see PCharacter
 * @see Shop
 * @see Skills
 * 
 */
public final class Core extends CoreCommands implements Serializable
{

    private static final long serialVersionUID = 3318L;
    private static final Random RNDSEED = new Random();

    /**
     * Constructor. Initialize system and related objects
     * 
     * @throws JurpeException
     */
    public Core() throws JurpeException
    {
        String runningPath = new File(".").getAbsolutePath();
        this.log = Log.getReference();
        this.log.addEntry("Welcome to " + Config.APPNAME + " " + Config.VERSION);
        this.log.addEntry();
        this.log.addDetail(LibraryStrings.RUNNINGIN + runningPath);
        this.os = OSProps.getReference();
        this.log.addDetail(this.os.toString());
    }

    /**
     * Get the unique random seed
     * 
     * @return A random seed
     */
    public Random getRandomSeed()
    {
        return Core.RNDSEED;
    }

    /**
     * Set dungeon related handles
     */
    public void initializeDungeon()
    {
        this.dungeon = new Dungeons(this);
        this.dungeon.initializeLevels();
    }

    /**
     * Core initialization routines
     * 
     * @throws JurpeException
     */
    @Override
    public void init() throws JurpeException
    {
        // Add a DAT directory to save temporary files
        File datDir = new File("dat");
        if (!datDir.exists())
        {
            this.getLog().addDetail("Created dat/ dir.");
            datDir.mkdir();
        }
        else
        {
            this.getLog().addDetail("Temporary dat/ dir found.");
        }

        this.initGameWorld();
        this.initSkills();
        this.initDictionary();
        this.initMasterShop();
        this.addRandomItems();
        this.initShop();
        this.initHighScores();
        this.initInn();
        this.initMagesGuild();
    }

    private void initSkills()
    {
        // Init available Skills
        this.levels = new Skills();
        this.log.addDetail(LibraryStrings.SKILLSLOAD);
    }

    private void initGameWorld()
    {
        // Setup Technology level
        this.world = new GameWorld(3); // Init time and world
        GameTime gt = this.world.getGameTime();
        gt.setRandomTime(Core.RNDSEED);
        this.log.addEntry(gt.whatTimeIsIt());
    }

    private void initDictionary()
    {
        // ...load dictionary.xml
        this.names = new RandomNames();
        this.log.addDetail(LibraryStrings.MDICTLOADD);
    }

    private void initMasterShop()
    {
        // Init Master Shop: contains all items available in the game
        this.allItems = new MasterShop(this.levels);
    }

    private void initMagesGuild()
    {
        this.magesGuild = new MagesGuild(this);
    }

    private void addRandomItems() throws JurpeException
    {
        // Add some random items...
        this.log.addDetail(LibraryStrings.CETNCADM);
        this.allItems.addRandomItems(this.names, ItemValueType.AVERAGE, 10);
        this.allItems.addRandomItems(this.names, ItemValueType.COMMON, 7);
        this.allItems.addRandomItems(this.names, ItemValueType.MORETHANAVG, 5);
        this.allItems.addRandomItems(this.names, ItemValueType.NORMAL, 3);
        // Then shuffle it up
        this.allItems.shuffleItems();
        this.log.addDetail(LibraryStrings.IESCETDA);
    }

    private void initShop()
    {
        // Create one real shop
        final int nrOfAvailableArmors = 8;
        final int nrOfAvailableShields = 8;
        final int nrOfAvailableWeapons = 8;
        final int nrOfAvailableItems = 8;
        final float percentOfRandomItems = 0.6f;

        this.mainShop = new Shop(LibraryStrings.SHOPNAME, this.allItems, nrOfAvailableArmors, nrOfAvailableShields, nrOfAvailableWeapons, nrOfAvailableItems,
                percentOfRandomItems);
        this.log.addDetail(LibraryStrings.SHOPLOADED);
    }

    private void initHighScores()
    {
        // Load High Scores
        this.loadHighScores();
        this.log.addDetail(LibraryStrings.HIGHSCORES + " " + LibraryStrings.LOADED);
    }

    private void initInn()
    {
        this.inn = new Inn((int) (Core.RNDSEED.nextDouble() * 10 + 5));
        this.log.addEntry(LibraryStrings.TODAYINN + this.inn.getCostPerHour() + LibraryStrings.DOLPERHOUR);
    }
}
