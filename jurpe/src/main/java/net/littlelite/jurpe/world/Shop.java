package net.littlelite.jurpe.world;

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
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.TreeSet;

import net.littlelite.jurpe.containers.MasterShop;
import net.littlelite.jurpe.items.Armor;
import net.littlelite.jurpe.items.AbstractItem;
import net.littlelite.jurpe.items.Item;
import net.littlelite.jurpe.items.ItemType;
import net.littlelite.jurpe.items.Shield;
import net.littlelite.jurpe.items.Weapon;

/**
 * Shop Instance is an actual Shop in the game, containing a subset of items in
 * the Shop class
 * 
 * 
 */
public class Shop
{

    private MasterShop master;
    private String shopName;
    private int availableArmors;
    private int availableShields;
    private int availableWeapons;
    private int availableItems;
    private float randomPercentage;
    private AbstractSet<Armor> armors;
    private AbstractSet<Shield> shields;
    private AbstractSet<Weapon> weapons;
    private AbstractSet<Item> items;

    /**
     * Constructor.
     * 
     * @param name
     *            Shop's name
     * @param masterS
     *            Handle to Master Shop
     * @param numberOfItems
     *            Number Of Items per category (Armors, Weapons, Shields, Items)
     */
    public Shop(String name, MasterShop masterS, int numberOfItems)
    {
        this.shopName = name;
        this.master = masterS;
        this.availableArmors = numberOfItems - 1;
        this.availableShields = numberOfItems - 1;
        this.availableWeapons = numberOfItems - 1;
        this.availableItems = numberOfItems - 1;
        this.randomPercentage = 0.5f; // 50% random items in the shop
        this.supply();
    }

    /**
     * Constructor.
     * 
     * @param name
     *            Shop's name
     * @param masterS
     *            Handle to Master Shop
     * @param aArmors
     *            number of available armors in the shop
     * @param aShields
     *            number of available shields in the shop
     * @param aWeapons
     *            number of available weapons in the shop
     * @param aItems
     *            number of available items in the shop
     */
    public Shop(String name, MasterShop masterS, int aArmors, int aShields, int aWeapons, int aItems)
    {
        this.shopName = name;
        this.master = masterS;
        this.availableArmors = aArmors - 1;
        this.availableShields = aShields - 1;
        this.availableWeapons = aWeapons - 1;
        this.availableItems = aItems - 1;
        this.randomPercentage = 0.5f; // 50% random items in the shop
        this.supply();
    }

    /**
     * Constructor.
     * 
     * @param name
     *            Shop's name
     * @param masterS
     *            Handle to Master Shop
     * @param aArmors
     *            number of available armors in the shop
     * @param aShields
     *            number of available shields in the shop
     * @param aWeapons
     *            number of available weapons in the shop
     * @param aItems
     *            number of available items in the shop
     * @param randomItems
     *            percentage of random items (0.0 means all items are read from
     *            shop.xml, 0.5 means that 50% are read from shop.xml and the
     *            other are randomly created)
     */
    public Shop(String name, MasterShop masterS, int aArmors, int aShields, int aWeapons, int aItems, float randomItems)
    {
        this.shopName = name;
        this.master = masterS;
        this.availableArmors = aArmors - 1;
        this.availableShields = aShields - 1;
        this.availableWeapons = aWeapons - 1;
        this.availableItems = aItems - 1;
        this.randomPercentage = randomItems;
        this.supply();
    }

    /**
     * Return shop name.
     * 
     * @return Shop name.
     */
    public String getName()
    {
        return this.shopName;
    }

    /**
     * Set random percentage of items in this shop.
     * 
     * @param perc
     *            Percentage of random items
     */
    public void setRandomPercentage(float perc)
    {
        this.randomPercentage = perc;
    }

    /**
     * Set the number of available Armors in the shop
     * 
     * @param number
     *            Number of available Armors in the shop
     */
    public void setAvailableArmors(int number)
    {
        this.availableArmors = number;
    }

    /**
     * Set the number of available Shields in the shop
     * 
     * @param number
     *            Number of available Shields in the shop
     */
    public void setAvailableShields(int number)
    {
        this.availableShields = number;
    }

    /**
     * Set the number of available Weapons in the shop
     * 
     * @param number
     *            Number of available Weapons in the shop
     */
    public void setAvailableWeapons(int number)
    {
        this.availableWeapons = number;
    }

    /**
     * Set the number of available Items in the shop
     * 
     * @param number
     *            Number of available Items in the shop
     */
    public void setAvailableItems(int number)
    {
        this.availableItems = number;
    }

    /**
     * Get armors in the Shop.
     * 
     * @return List of armors (Armor) in the shop
     * @see Armor
     */
    public AbstractSet<Armor> getArmors()
    {
        return this.armors;
    }

    /**
     * Get weapons in the Shop.
     * 
     * @return List of weapons (Weapon) in the shop
     * @see Weapon
     */
    public AbstractSet<Weapon> getWeapons()
    {
        return this.weapons;
    }

    /**
     * Get items in the Shop.
     * 
     * @return List of items (Item) in the shop
     * @see Item
     */
    public AbstractSet<Item> getItems()
    {
        return this.items;
    }

    /**
     * Get shields in the Shop.
     * 
     * @return List of shields (Shield) in the shop
     * @see Shield
     */
    public AbstractSet<Shield> getShields()
    {
        return this.shields;
    }

    private static int getRandomNumberOfItems(int totalItems, float percentage)
    {
        return (int) (totalItems * percentage);
    }

    private AbstractSet<Weapon> supplyWeapons()
    {
        AbstractSet<Weapon> aW = new TreeSet<Weapon>();
        for (AbstractItem bi : this.supplyItemMix(this.availableWeapons, ItemType.WEAPON))
        {
            aW.add((Weapon) bi);
        }
        return aW;
    }

    private AbstractSet<Item> supplyItems()
    {
        AbstractSet<Item> aI = new TreeSet<Item>();
        for (AbstractItem bi : this.supplyItemMix(this.availableItems, ItemType.ITEM))
        {
            aI.add((Item) bi);
        }
        return aI;
    }

    private AbstractSet<Shield> supplyShields()
    {
        AbstractSet<Shield> aS = new TreeSet<Shield>();
        for (AbstractItem bi : this.supplyItemMix(this.availableShields, ItemType.SHIELD))
        {
            aS.add((Shield) bi);
        }
        return aS;
    }

    private AbstractSet<Armor> supplyArmors()
    {
        AbstractSet<Armor> aS = new TreeSet<Armor>();
        for (AbstractItem bi : this.supplyItemMix(this.availableArmors, ItemType.ARMOR))
        {
            aS.add((Armor) bi);
        }
        return aS;
    }

    private AbstractList<AbstractItem> supplyItemMix(int total, ItemType basicItemType)
    {
        int randomItems = Shop.getRandomNumberOfItems(total, this.randomPercentage);
        int staticItems = total - randomItems;

        AbstractList<AbstractItem> mix = this.supplyBasicItem(basicItemType, randomItems, true);
        mix.addAll(this.supplyBasicItem(basicItemType, staticItems, false));

        return mix;
    }

    private AbstractList<AbstractItem> supplyBasicItem(ItemType basicItemType, int num, boolean random)
    {
        AbstractList<AbstractItem> al = new ArrayList<AbstractItem>();
        int j = 0;

        for (AbstractItem bi : this.master.getItems())
        {
            if (j > num)
            {
                break;
            }

            if (bi.getType() == basicItemType)
            {
                if (bi.isItemRandom() == random)
                {
                    al.add(bi);
                    j++;
                }
            }
        }
        return al;
    }

    private void addMandatoryItems()
    {
        // If I can't find a shovel, a lamp or a grimoire, I add them to the shop Items
        Item shovel = null;
        Item lamp = null;

        for (Item currentItem : this.items)
        {
            if (currentItem.getName().equals("Shovel"))
            {
                shovel = currentItem;
            }

            if (currentItem.getName().endsWith("Lamp"))
            {
                lamp = currentItem;
            }

            if (lamp != null && shovel != null)
            {
                break;
            }
        }

        if (shovel == null)
        {
            shovel = new Item("Shovel", 60, 4, "Useful to dig into dungeons", 0, null, -1, true);
            this.items.add(shovel);
        }

        if (lamp == null)
        {
            lamp = new Item("Rusty Lamp", 30, 1, "A rusty, common lamp to light dark dungeons", 0, null, 5, true);
            this.items.add(lamp);
        }

    }

    private void supply()
    {
        this.shields = this.supplyShields();
        this.armors = this.supplyArmors();
        this.weapons = this.supplyWeapons();
        this.items = this.supplyItems();
        this.addMandatoryItems();
    }
}
