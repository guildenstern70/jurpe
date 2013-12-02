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
import java.io.Serializable;
import java.util.AbstractList;

import net.littlelite.jurpe.containers.Lootables;
import net.littlelite.jurpe.containers.Monsters;
import net.littlelite.jurpe.dungeon.crawler.RpgMapPoint;
import net.littlelite.jurpe.dungeon.rpgmap.RpgMap;
import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.utils.Serializer;

/**
 * Level contain objects found on a dungeon level, and it is composed by:
 * <ul>
 * <li>a Map (RpgMap)
 * <li>a collection of monsters (Wave)
 * <li>a level (int)
 * <li>some items
 * </ul>
 * 
 * @see net.littlelite.jurpe.dungeon.rpgmap.RpgMap;
 */
public class Level implements Serializable
{

    private static final long serialVersionUID = 3318L;
    protected int zLevel;
    protected RpgMap map;
    protected Monsters monsters;
    protected Lootables items;
    protected transient Avatar avatar;

    /**
     * Dungeon Level
     * 
     * @param sMap
     *            RpgMap associated to this level
     * @param sLevel
     *            Dungeon level
     */
    public Level(RpgMap sMap, int sLevel)
    {
        this.map = sMap;
        this.zLevel = sLevel;
        this.monsters = new Monsters(this);
        this.items = new Lootables(this);
        this.avatar = null;
    }

    /**
     * Fill this level with random monsters
     * 
     * @param dungeon
     *            Dungeons handle
     */
    public void fillWithMonsters(Dungeons dungeon, Color back, Color fore)
    {
        this.initMonsters(dungeon, fore, back);
        dungeon.log.addDetail("Dungeon level filled with monsters.");
    }

    /**
     * Fill this level with random items
     * 
     * @param dungeon
     *            Dungeons handle
     */
    public void fillWithItems(Dungeons dungeon)
    {
        this.initItems(dungeon);
        dungeon.log.addDetail("Dungeon level filled with items.");
    }

    /**
     * @param av
     *            Avatar
     */
    public void addAvatar(Avatar av)
    {
        this.avatar = av;
    }

    /**
     * 
     */
    public void removeAvatar()
    {
        this.avatar = null;
    }

    /**
     * Return the random items in this level
     * 
     * @return Random items in this level
     */
    public Lootables getItems()
    {
        return this.items;
    }

    /**
     * @return Avatar
     */
    public Avatar getAvatar()
    {
        return this.avatar;
    }

    /**
     * Monsters object collection
     * 
     * @return Monsters Monsters object collection
     */
    public Monsters getMonsters()
    {
        return this.monsters;
    }

    /**
     * The placeholders in this map
     * 
     * @return AbstractList A placeholders list
     */
    public AbstractList<Placeholder> placeHolders()
    {
        // Monsters
        AbstractList<Placeholder> iPh = this.monsters.getPlaceHolders();

        // Items
        iPh.addAll(this.items.getPlaceHolders());

        // Avatar
        if (this.avatar != null)
        {
            iPh.add(this.avatar.getPlaceholder());

            // Any gunsight?
            if (this.avatar.hasGunsight())
            {
                iPh.add(this.avatar.getGunsight().getPlaceholder());
            }
        }

        return iPh;
    }

    /**
     * Get placeholder in this level rpgmappoint
     * 
     * @param rpgPoint
     *            RpgMapPoint
     * @return PlaceHolder Placeholder in this level
     */
    public Placeholder getPlaceHolderIn(RpgMapPoint rpgPoint)
    {
        Placeholder ph = null;
        for (Placeholder temp : this.placeHolders())
        {
            RpgMapPoint pThat = temp.getPosition();
            if (pThat.equals(rpgPoint))
            {
                ph = temp;
                break;
            }
        }

        return ph;
    }

    /**
     * Return true if there is a placeholder in this rpgmappoint
     * 
     * @param p
     *            RpgMapPoint
     * @return boolean
     */
    public boolean isTherePlaceHolder(RpgMapPoint p)
    {
        boolean found = false;

        for (Placeholder ph : this.placeHolders())
        {
            RpgMapPoint pThis = ph.getPosition();
            if (pThis.equals(p))
            {
                found = true;
                break;
            }
        }

        return found;
    }

    /**
     * Get the level. The more this number is high, the more difficult the level
     * should be.
     * 
     * @return The level of the dungeon.
     */
    public int getZ()
    {
        return this.zLevel;
    }

    /**
     * Get RpgMap of this level
     * 
     * @return RpgMap of this level
     * @see net.littlelite.jurpe.dungeon.rpgmap.RpgMap;
     */
    public RpgMap getRpgMap()
    {
        return this.map;
    }

    /**
     * Persist this level
     * 
     * @return true if the save was successful
     */
    public boolean save()
    {
        File levelFile = new File(Config.addDatPathTo(this.getLevelFileName()));
        return Serializer.serialize(levelFile, this);
    }

    /**
     * Name of the level (ie> Underground_02)
     * 
     * @return The name of this level
     */
    public String getLevelName()
    {
        return Level.getLevelName(this.zLevel, false);
    }

    /**
     * Get the name for this level
     * 
     * @param level
     *            Level id
     * @return String name for this level
     */
    public static String getLevelName(int level, boolean forFile)
    {
        String description = "Map";
        if (!forFile)
        {
            if (level == 0)
            {
                description = "The village";
            }
            else
            {
                description = "Underground Level " + Integer.toString(level);
            }
        }
        else
        {
            if (level == 0)
            {
                description = "village";
            }
            else
            {
                description = "dnglevel" + Integer.toString(level);
            }
        }
        return description;
    }

    /**
     * Return the name of this map
     * 
     * @return Name of this map
     */
    @Override
    public String toString()
    {
        return this.getLevelName();
    }

    /**
     * Get the name that the file with this map will have.
     * 
     * @return String Name of the file (without path) to which this map will be
     *         saved
     */
    public String getLevelFileName()
    {
        return Level.getLevelFileName(this.zLevel);
    }

    /**
     * The name that the file with the level map will have
     * 
     * @param level
     *            Level number of the map
     * @return Name of the level file (without path)
     */
    public static String getLevelFileName(int level)
    {
        return Level.getLevelName(level, true) + ".map";
    }

    /**
     * Fill level with dungeon monsters
     * 
     * @param dungeons
     *            Handle to dungeons
     */
    protected void initMonsters(Dungeons dungeons, Color fore, Color back)
    {
        this.monsters.initialize(dungeons, back, fore);
    }

    /**
     * Fill level with random items
     * 
     * @param dungeons
     *            Handle to dungeons
     */
    protected void initItems(Dungeons dungeons)
    {
        try
        {
            this.items.initialize(dungeons);
        }
        catch (JurpeException jex)
        {
            dungeons.getSystem().getLog().addEntry(jex.getMessage());
            jex.printStackTrace();
            System.exit(-1);
        }
    }
}
