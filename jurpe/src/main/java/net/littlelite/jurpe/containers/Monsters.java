package net.littlelite.jurpe.containers;

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

import java.awt.Color;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Random;

import net.littlelite.jurpe.characters.Monster;
import net.littlelite.jurpe.dungeon.DungeonMonster;
import net.littlelite.jurpe.dungeon.Dungeons;
import net.littlelite.jurpe.dungeon.Level;
import net.littlelite.jurpe.dungeon.Placeholder;
import net.littlelite.jurpe.dungeon.PlaceholderType;
import net.littlelite.jurpe.dungeon.rpgmap.RpgMap;
import net.littlelite.jurpe.items.Weapon;
import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.Core;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.utils.AxsRand;

/**
 * Monsters class holds a reference to every monster per level.
 */
public class Monsters implements Serializable
{

	private static final long serialVersionUID = 3317L;

	private AbstractList<DungeonMonster> dungeonMonsters;
	private Level level;
	private Color fore;
	private Color back;

	/**
	 * Constructor
	 * 
	 * @param dungeonLevel
	 *            Level Level of the dungeon
	 */
	public Monsters(Level dungeonLevel)
	{
		this.dungeonMonsters = new ArrayList<DungeonMonster>();
		this.level = dungeonLevel;
	}

	/**
	 * The current number of living monsters
	 * 
	 * @return
	 */
	public int size()
	{
		return this.dungeonMonsters.size();
	}

	/**
	 * Update living monsters.
	 * 
	 * @return true if the number of living monsters actually changed after this
	 *         call
	 */
	public boolean updateLiving()
	{
		boolean changed = false;

		int oldSize = this.size();
		int newSize = 0;

		AbstractList<DungeonMonster> livingMonsters = new ArrayList<DungeonMonster>();
		for (DungeonMonster monster : this.dungeonMonsters)
		{
			if (monster.isAlive())
			{
				livingMonsters.add(monster);
			}
		}

		newSize = livingMonsters.size();

		if (newSize != oldSize)
		{
			changed = true;
		}

		this.dungeonMonsters = livingMonsters;

		return changed;
	}

	/**
	 * Get monster by name
	 * 
	 * @param monsterName
	 *            String Name of the monster
	 * @return Monster Monster in the collection
	 */
	public Monster getByName(String monsterName)
	{
		Monster m = null;

		for (DungeonMonster monster : this.dungeonMonsters)
		{
			if (monster.getName().equals(monsterName))
			{
				m = monster;
				break;
			}
		}

		return m;
	}

	/**
	 * Dungeon Monsters
	 * 
	 * @return AbstractList<DungeonMonster>
	 */
	public AbstractList<DungeonMonster> getDungeonMonsters()
	{
		return this.dungeonMonsters;
	}

	/**
	 * Get monsters placeholder
	 * 
	 * @return AbstractList
	 */
	public AbstractList<Placeholder> getPlaceHolders()
	{
		AbstractList<Placeholder> placeHolders = new ArrayList<Placeholder>();
		for (DungeonMonster dm : this.dungeonMonsters)
		{
			placeHolders.add(dm.getPlaceholder());
		}
		return placeHolders;
	}

	/**
	 * Generate new wave of monsters. Every monster will have associated an
	 * image file based on this pattern (monsterXX.gif where XX is a number
	 * between 1 and availableImages)
	 * 
	 * @param dungeon
	 *            Handle to dungeons
	 * @throws JurpeException
	 */
	public void initialize(Dungeons dungeon, Color monstersBackground, Color monstersForeground)
	{

		int dungeonLevel = this.level.getZ();
		int number = this.getNumberOfMonsters(); // to generate
		this.fore = monstersForeground;
		this.back = monstersBackground;

		for (int j = 0; j < number; j++) // monster generation
		{
			DungeonMonster generatedMonster = this.createMonster(dungeon);

			// Set monster image file
			StringBuilder imgFilePath = new StringBuilder("images/monsters/");
			imgFilePath.append(Monsters.generateMonsterImageFile());
			generatedMonster.getCharacterAttributes().setImageFileName(imgFilePath.toString());

			// If wave is >= 3 then the monster will have a weapon.
			// Since weapons are sort by power,
			// we set the weapon to be (wave/2)
			// (0 for the first 2 waves, 1 for the second 3 and so on...
			// Also, the skill level will improve wave after wave.
			if (dungeonLevel >= 3)
			{
				Weapon weap = this.getWeapon(dungeon);
				if (weap != null)
				{
					generatedMonster.setMonsterWeapon(weap, Math.min(18, 10 + dungeonLevel));
				}
			}

			this.dungeonMonsters.add(generatedMonster);
		}
	}

	private Weapon getWeapon(Dungeons dungeon)
	{
		Weapon weap = null;
		short counter = 0;
		AbstractSet<Weapon> weapons = dungeon.getSystem().getShop().getWeapons();
		int weaponPower = this.getWeaponsPower(weapons.size());
		// since weapons are ordered by power, the first weapon is the less
		// powered...
		for (Weapon w : weapons)
		{
			if (counter == weaponPower)
			{
				weap = w;
				break;
			}
			counter++;
		}
		return weap;
	}

	private int getWeaponsPower(int size)
	{
		return Math.min(size, this.level.getZ() / 2);
	}

	private int getNumberOfMonsters()
	{
		return Math.min(Config.AVAILABLEMONSTERSPICS, (3 + this.level.getZ()));
	}

	private int getMonstersCP()
	{
		return Math.min((60 + (this.level.getZ() * 20)), Config.MAXMONSTERLEVEL);
	}

	private DungeonMonster createMonster(Dungeons dungeon)
	{
		Monster m = dungeon.getSystem().generateMonster(this.getMonstersCP());

		Placeholder ph = new Placeholder(this.level, PlaceholderType.MONSTER, m.getName());
		ph.setDefaultInitialPosition();
		ph.setForegroundColor(this.fore);
		ph.setBackgroundColor(this.back);

		return new DungeonMonster(m, ph);
	}

	private static String generateMonsterImageFile()
	{
		AxsRand axs = AxsRand.getReference();
		int num = axs.randInt(Config.AVAILABLEMONSTERSPICS);
		StringBuilder sb = new StringBuilder("monster");
		sb.append(num);
		sb.append(".gif");
		return sb.toString();
	}

	/**
	 * This test class is also a good starting point to know how to set up Jurpe
	 * Engine.
	 * 
	 * @param args
	 *            String[]
	 */
	public static void main(String[] args)
	{
		try
		{
			// Initialize core reading xml files
			Core core = new Core();
			core.init();

			// Initialize a new dungeon
			Dungeons dungeon = new Dungeons(core);

			// Initialize a new dungeon map 16x16 hexes
			RpgMap rpgmap = new RpgMap(new Random(), (short) 16, (short) 16);

			// Initialize a new level of difficulty: 6
			Level level = new Level(rpgmap, 6);

			// Creates a new mosters collection
			Monsters monsters = new Monsters(level);
			monsters.initialize(dungeon, Color.WHITE, Color.RED);

			// Write them down to the console
			for (DungeonMonster monster : monsters.dungeonMonsters)
			{
				System.out.println(monster.toString());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
