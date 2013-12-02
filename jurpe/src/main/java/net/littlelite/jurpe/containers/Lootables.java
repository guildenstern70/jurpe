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
package net.littlelite.jurpe.containers;

import java.awt.Color;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Random;

import net.littlelite.jurpe.dungeon.DungeonItem;
import net.littlelite.jurpe.dungeon.Dungeons;
import net.littlelite.jurpe.dungeon.Level;
import net.littlelite.jurpe.dungeon.Placeholder;
import net.littlelite.jurpe.dungeon.PlaceholderType;
import net.littlelite.jurpe.dungeon.crawler.RpgMapPoint;
import net.littlelite.jurpe.items.AbstractItem;
import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.generation.ItemValueType;

/**
 * This class holds references to items or money hidden in the dungeon. These
 * are lootables
 * 
 * 
 */
public class Lootables implements Serializable
{

    private static final long serialVersionUID = 3317L;
    private AbstractList<DungeonItem> lootables;
    private Level level;

    /**
     * Constructor
     * 
     * @param dungeonLevel
     *            Level Level of the dungeon
     */
    public Lootables(Level dungeonLevel)
    {
        this.lootables = new ArrayList<DungeonItem>();
        this.level = dungeonLevel;
    }
    
    /**
     * The dungeon items
     * @return the dungeon items
     */
    public AbstractList<DungeonItem> getDungeonItems()
    {
        return lootables;
    }

    /**
     * Get monsters placeholder
     * 
     * @return AbstractList
     */
    public AbstractList<Placeholder> getPlaceHolders()
    {
        AbstractList<Placeholder> placeHolders = new ArrayList<Placeholder>();
        for (DungeonItem di : this.lootables)
        {
            placeHolders.add(di.getPlaceholder());
        }
        return placeHolders;
    }

    /**
     * Remove the item at the specified position
     * 
     * @param p
     */
    public void removeItemIn(RpgMapPoint p)
    {
        DungeonItem item = null;

        for (DungeonItem di : this.lootables)
        {
            if (di.getPlaceholder().getPosition() == p)
            {
                item = di;
                break;
            }
        }

        if (item != null)
        {
            this.lootables.remove(item);
        }

    }

    /**
     * Get the item at the specified position
     * 
     * @param p
     *            RpgMapPoint in which DungeonItem is supposed to be
     * @return A dungeon item if found, else null
     */
    public DungeonItem getItemIn(RpgMapPoint p)
    {
        DungeonItem item = null;

        for (DungeonItem di : this.lootables)
        {
            if (di.getPlaceholder().getPosition() == p)
            {
                item = di;
                break;
            }
        }

        return item;
    }

    /**
     * Generate new set of dungeon items, that is money or other items that can
     * be found in the dungeon.
     * 
     * @param dungeon
     *            Handle to dungeons
     * @throws JurpeException
     */
    public void initialize(Dungeons dungeon) throws JurpeException
    {
        // Fill dungeon with lootables only if that was not
        // serialized before
        if (this.lootables.size() == 0)
        {
            int number = dungeon.getRandomSeed().nextInt(Config.LOOTABLES);
            for (int j = 0; j < number; j++) // lootables generation
            {
                DungeonItem di = this.createLootable(dungeon);
                this.lootables.add(di);
            }
        }
    }

    /**
     * Ad the given item to a dungeon level
     *  
     * @param itemToAdd Item to add
     * @param type Placeholder type for the item
     */
    public void addMandatoryItem(AbstractItem itemToAdd, PlaceholderType type)
    {
        Placeholder ph = new Placeholder(this.level, type, itemToAdd.getName());
        ph.setDefaultInitialPosition();
        this.lootables.add(new DungeonItem(itemToAdd, ph));
    }

    private DungeonItem createLootable(Dungeons dungeon) throws JurpeException
    {
        Random rnd = dungeon.getRandomSeed();
        AbstractItem bi = dungeon.getSystem().generateItem(this.getRarity(rnd));
        Placeholder ph = new Placeholder(this.level, PlaceholderType.ITEM, bi.getName());
        //System.out.println("Added " + bi.getDescription() + " in level " + this.level.getZ());
        ph.setDefaultInitialPosition();
        ph.setForegroundColor(Color.ORANGE); //Why?

        return new DungeonItem(bi, ph);
    }

    private ItemValueType getRarity(Random seed)
    {
        ItemValueType ivt = ItemValueType.AVERAGE;
        int rndToken = seed.nextInt(100);

        if (rndToken < 10)
        {
            ivt = ItemValueType.TRASH; // 10%
        }
        else if (rndToken > 10 && rndToken < 49)
        {
            ivt = ItemValueType.COMMON; // 30%
        }
        else if (rndToken > 50 && rndToken < 60)
        {
            ivt = ItemValueType.NORMAL; // 10%
        }
        else if (rndToken > 60 && rndToken < 65)
        {
            ivt = ItemValueType.MORETHANAVG; // 5%
        }
        else if (rndToken > 65 && rndToken < 69)
        {
            ivt = ItemValueType.RARE; // 4%
        }
        else if (rndToken == 77)
        {
            ivt = ItemValueType.UNIQUE;
        }

        return ivt;
    }
}
