package net.littlelite.jurpe.dungeon;

/**
J.U.R.P.E. @version@ (DungeonCrawler Package)
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
import java.awt.Color;
import java.io.File;
import java.io.InvalidClassException;
import java.io.Serializable;
import java.util.Random;

import net.littlelite.jurpe.containers.Lootables;
import net.littlelite.jurpe.dungeon.generator.Dungeonifier;
import net.littlelite.jurpe.dungeon.generator.IDungeonGenerator;
import net.littlelite.jurpe.dungeon.generator.OpenSpace;
import net.littlelite.jurpe.dungeon.rpgmap.RpgMap;
import net.littlelite.jurpe.items.AbstractItem;
import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.Log;
import net.littlelite.utils.Serializer;

/**
 * Dungeon Levels class. This class handles the persistance of dungeon levels on
 * files. When it is first called it initialize the initial number of levels.
 * With "createDungeons()" it creates the village (openspace) dungeon plus the
 * number of dungeons in numberOfLevels. When created, the dungeon is
 * serialized. Through "getDungeonLevel(int)" this class returns the related
 * RpgMap object deserializing it from the disk.
 */
public class DungeonLevels implements Serializable
{

    private static final long serialVersionUID = 3317L;
    private static final int LEVELS_GAP = 5; // Number of levels to be generated each time
    
    protected Dungeons dungeon;
    protected IDungeonGenerator dungeonGenerator;
    protected int numberOfLevels;
    protected Log log;
    protected Random rnd;
    private Level currentLevel;
    private int levelInMemory;

    /**
     * Constructor
     * 
     * @param inDungeon
     *            Handle to dungeon
     * @param levels
     *            int Initial Number of dungeons to be created
     */
    public DungeonLevels(Dungeons inDungeon, int levels)
    {
        this.dungeon = inDungeon;
        this.dungeonGenerator = null; // will be set by CreateDungeonLevel
        this.numberOfLevels = levels;
        this.log = this.dungeon.getLog();
        this.rnd = this.dungeon.getRandomSeed();
        // Reset
        this.zero();
    }

    /**
     * Create dungeon levels. When created, the level is then serialized.
     */
    public void createLevels() throws JurpeException
    {
        this.log.addDetail("Creating dungeon levels...");
        this.createVillage(Config.VILLAGE_WIDTH, Config.VILLAGE_HEIGHT, Config.MAZE_BACKGROUND, Config.AVATAR_FORE);
        for (int level = 1; level <= this.numberOfLevels; level++)
        {
            this.createDungeonLevel(level, Config.DUNGEON_WIDTH, Config.DUNGEON_HEIGHT, 
                                    Config.MAZE_BACKGROUND, Config.AVATAR_FORE);
        }
        this.log.addDetail("... done.");
    }

    /**
     * Add a number of LEVELS_GAP levels to the dungeon. The level are created and then
     * serialized.
     */
    public void addLevels() throws JurpeException
    {
        this.log.addDetail("Adding " + this.numberOfLevels + " dungeon levels...");
        for (int level = this.numberOfLevels + 1; level <= this.numberOfLevels + LEVELS_GAP; level++)
        {
            this.createDungeonLevel(level, Config.DUNGEON_WIDTH, Config.DUNGEON_HEIGHT, 
                                    Config.MAZE_BACKGROUND, Config.AVATAR_FORE);
        }
        this.numberOfLevels += LEVELS_GAP;
        this.log.addDetail("... done.");
    }

    /**
     * Add an item to a dungeon level
     * 
     * @param level Level to add the item to (0=Village, 1=first level...)
     * @param itemToAdd The item to be added
     * @param phType The placeholder type for a representation of this item in the map
     * @param unique If this item has to be the only one in the level
     */
    public void addItemToLevel(int level, AbstractItem itemToAdd, PlaceholderType phType, boolean unique)
    {
        try
        {
            Level editLevel = this.getDungeonLevel(level);
            boolean addToLevel = true;
            if (editLevel != null)
            {
                Lootables items = editLevel.getItems();
                if (unique)
                {
                    for (DungeonItem di : items.getDungeonItems())
                    {
                        if (di.item().getName().equals(itemToAdd))
                        {
                            addToLevel = false;
                            break;
                        }
                    }
                }
                
                if (addToLevel)
                {
                    items.addMandatoryItem(itemToAdd, phType);
                    editLevel.save();
                    this.log.addDetail("Added "+itemToAdd.getName()+" to level "+level);
                }
                else
                {
                    this.log.addDetail("An "+itemToAdd.getName()+" already exists in level "+level);
                }

            }
        }
        catch (JurpeException jex)
        {
            this.log.addDetail("Cannot add item to level " + level);
        }
    }

    /**
     * Delete level files
     */
    public void destroyLevelFiles()
    {
        for (int j = 0; j <= this.numberOfLevels; j++)
        {
            File toBeKilledFile = new File(Config.addDatPathTo(Level.getLevelFileName(j)));
            boolean success = toBeKilledFile.delete();
            if (!success)
            {
                // System.out.println(toBeKilledFile.getAbsolutePath()+" will be
                // deleted on exit...");
                toBeKilledFile.deleteOnExit();
            }
        }
        this.zero();
    }

    /**
     * Get number of levels
     * 
     * @return int Number of Levels
     */
    public int getNumberOfLevels()
    {
        return this.numberOfLevels;
    }

    /**
     * Get the dungeon map associated with that level
     * 
     * @param level
     *            int Level. If zero, returns the Village (open space).
     * @return RpgMap The map level. If null, new levels have to be generated
     * @throws JurpeException
     */
    public Level getDungeonLevel(int level) throws JurpeException
    {
        Level dungeonLevel = null;

        if ((this.currentLevel != null) && (this.levelInMemory == level))
        {
            dungeonLevel = this.currentLevel;
        }
        else
        {
            this.log.addDetail("Getting level " + String.valueOf(level) + " from disk");
            dungeonLevel = this.restoreLevelFromDisk(level);

            if (dungeonLevel == null)
            {
                this.log.addDetail("Failed to get level" + String.valueOf(level) + " from disk");
                if (level <= this.numberOfLevels)
                {
                    this.createLevels();
                }
                else
                {
                    this.addLevels();
                }

                try
                {
                    dungeonLevel = this.restoreLevelFromDisk(level);
                }
                catch (Exception exc)
                {
                    dungeonLevel = null;
                }

                if (dungeonLevel == null)
                {
                    this.log.addDetail("Cannot retrieve map #" + level);
                    throw new JurpeException("Cannot retrieve map #" + level);
                }
            }

            this.currentLevel = dungeonLevel;
            this.levelInMemory = dungeonLevel.getZ();
        }

        return dungeonLevel;
    }

    private void createDungeonLevel(int level, short dungeonWidth, short dungeonHeight,
                                    Color background, Color foreground) throws JurpeException
    {
        this.log.addDetail("... adding Dungeon Level " + level);
        this.dungeonGenerator = new Dungeonifier(this.rnd, this.log, dungeonWidth, dungeonHeight);
        this.initDungeonMapLevel(level, background, foreground);
    }

    private void createVillage(short villageWidth, short villageHeght, Color background, Color foreground) throws JurpeException
    {
        this.log.addDetail("... adding Village");
        this.dungeonGenerator = new OpenSpace(this.rnd, this.log, villageWidth, villageHeght);
        this.initDungeonMapLevel(0, background, foreground);
    }

    private void initDungeonMapLevel(int level, Color mazeBackground, Color monsterForeground) throws JurpeException
    {
        RpgMap mapLevel = this.dungeonGenerator.createDungeon(level);
        Level dungeonLevel = new Level(mapLevel, level);
        if (level > 0)
        {
            dungeonLevel.fillWithMonsters(this.dungeon, mazeBackground, monsterForeground);
            dungeonLevel.fillWithItems(this.dungeon);
        }
        dungeonLevel.save();
    }

    private Level restoreLevelFromDisk(int level)
    {
        Level dungeonLevel = null;
        dungeonLevel = DungeonLevels.restore(level);

        if (dungeonLevel != null)
        {
            this.currentLevel = dungeonLevel;
        }
        else
        {
            this.log.addDetail("Cannot restore level " + level + " from disk!");
        }
        return dungeonLevel;
    }

    private void zero()
    {
        this.currentLevel = null;
        this.levelInMemory = -1;
    }

    /**
     * Get a serialized Level
     * 
     * @param level
     *            int Dungeon map level
     * @return RpgMap
     * @throws InvalidClassException
     */
    public static Level restore(int level)
    {
        Level restoredLevel = null;
        File mapPath = new File(Config.addDatPathTo(Level.getLevelFileName(level)));

        if (mapPath.exists())
        {
            restoredLevel = (Level) Serializer.deSerialize(mapPath);
        }

        return restoredLevel;
    }
}
