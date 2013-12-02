package net.littlelite.jurpedemo.logic;

/**
 J.U.R.P.E. @version@ Swing Demo
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

import javax.swing.JOptionPane;

import net.littlelite.jurpe.characters.Monster;
import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.characters.PCharacterSkill;
import net.littlelite.jurpe.dungeon.Avatar;
import net.littlelite.jurpe.dungeon.DungeonItem;
import net.littlelite.jurpe.dungeon.Dungeons;
import net.littlelite.jurpe.dungeon.Level;
import net.littlelite.jurpe.dungeon.Placeholder;
import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.crawler.RpgMapPoint;
import net.littlelite.jurpe.dungeon.furnishing.Door;
import net.littlelite.jurpe.dungeon.furnishing.OpenClose;
import net.littlelite.jurpe.dungeon.rpgmap.Cell;
import net.littlelite.jurpe.dungeon.rpgmap.RpgMap;
import net.littlelite.jurpe.items.AbstractItem;
import net.littlelite.jurpe.items.Item;
import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.JurpeUtils;
import net.littlelite.jurpe.system.commands.DungeonCommand;
import net.littlelite.jurpe.world.GameTime;
import net.littlelite.jurpe.world.GameWorld;
import net.littlelite.jurpe.world.LocationType;
import net.littlelite.jurpedemo.resources.GameStrings;
import net.littlelite.jurpedemo.frames.JurpeMain;
import net.littlelite.jurpedemo.frames.TabbedPanel;

/**
 * Executes a command entered in a Game Panel
 */
public class DungeonCommander
{
	private Dungeons dungeon;
	private DungeonCommand command;
	private JurpeMain parent;

	/**
	 * Constructor
	 * 
	 * @param frame
	 *            Parent swing frame
	 * @param command
	 *            Command to be executed
	 */
	public DungeonCommander(JurpeMain frame, DungeonCommand command)
	{
		this.parent = frame;
		this.command = command;
		this.dungeon = this.parent.getSystem().getDungeon();
	}

	/**
	 * Execute the command. Every command advances game time by 1 minute
	 * 
	 * @throws JurpeException
	 */
	public void execute() throws JurpeException
	{

		this.autoRecovery();

		switch (command.getCommand())
		{
			case AIM:
				this.aim();
				break;

			case SAYTIME:
				break;

			case DIG:
				this.dig();
				break;

			case ENTER_INN:
				this.enterInn();
				break;

			case ENTER_SHOP:
				this.enterShop();
				break;

			case ENTER_TRAINER:
				this.enterTrainer();
				break;
                                
                        case ENTER_MAGESGUILD:
                                this.enterMagesGuild();
                                break;

			case STAIRS_DOWN:
				this.goDownStairs();
				break;

			case STAIRS_UP:
				this.goUpStairs();
				break;

			case ATTACK_MONSTER:
				this.attackMonster();
				break;

			case OPEN_DOOR:
				this.openDoor();
				break;

			case CLOSE_DOOR:
				this.closeDoor();
				break;

			case PICK_UP:
				this.pickUp();
				break;

			case USEFIRSTAID:
				this.useFirstAid();
				break;

			case QUITGAME:
				this.parent.exitProgram();
                                break;

			case PASS:
				break;

			default:
				// do nothing
				break;
		}
	}

	protected int showQuestionMessage(String message)
	{
		return JOptionPane.showConfirmDialog(this.parent, message, Config.APPNAME, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	}

	private void aim()
	{
		Avatar av = this.dungeon.getAvatar();
		av.enableGunsight(!av.hasGunsight()); // turn off aim if it's on and
												// viceversa
	}

	private void useFirstAid()
	{
		this.parent.useFirstAid();
	}

	private void pickUp()
	{
		try
		{
			Level currentLevel = this.dungeon.getCurrentLevel();
			RpgMapPoint p = this.dungeon.getAvatar().getPlaceholder().getCell().getCoordinates();
			DungeonItem item = currentLevel.getItems().getItemIn(p);
			AbstractItem bi = item.item();
			if (warning("Pick up " + bi.toString() + "?"))
			{
				this.dungeon.getSystem().getPC().getInventory().addBasicItem(bi);
				this.dungeon.getLog().addEntry("You get a " + bi.toString());
				currentLevel.getItems().removeItemIn(p);
			}
		}
		catch (JurpeException jex)
		{
			System.err.println(jex.getMessage());
		}
		catch (NullPointerException npe)
		{
			npe.printStackTrace();
		}
	}

	private void openDoor() throws JurpeException
	{
		this.actionDoor(true);
	}

	private void closeDoor() throws JurpeException
	{
		this.actionDoor(false);
	}

	private void actionDoor(boolean isToOpen) throws JurpeException
	{

		OpenClose operation = OpenClose.OPENED;
		String yet = "Can't see anything to open here";

		if (!isToOpen)
		{
			operation = OpenClose.CLOSED;
			yet = "Can't see anything to close here";
		}

		Cell currentCell = dungeon.getAvatar().getPlaceholder().getCell();
		Direction[] dirs = currentCell.getDoors();
		if (dirs != null)
		{
			String where = this.command.getOption();
			if (where != null)
			{
				Direction dir = Direction.fromString(where);
				Door d = currentCell.getPassages().getPassage(dir).getDoor();

				if ((isToOpen && !d.isOpen()) || (!isToOpen && d.isOpen()))
				{
					// When you open/close a door you actual open two doors
					// in two passages, because every connected cell has
					// a door...
					d.setStatus(operation);
					Cell adiacentCell = dungeon.getCurrentMap().getCell(currentCell, dir);
					Direction dd = Direction.getInverse(dir);
					Door ad = adiacentCell.getPassages().getPassage(dd).getDoor();
					ad.setStatus(operation);
					this.log("The door is now: " + d.getStatus().toString());
				}
				else
				{
					this.log("The door is already " + operation.toString());
				}
			}
		}
		else
		{
			this.log(yet);
		}
	}

	private void dig() throws JurpeException
	{
		if (this.command.getOption() != null)
		{
			PCharacter pc = this.parent.getSystem().getPC();
			Item shovel = pc.getCurrentItem();
			if (shovel == null)
			{
				this.log("Can't dig without a shovel!");
				return;
			}

			if (shovel.getName().equals("Shovel"))
			{
				this.log("Trying to dig toward " + this.command.getOption());
				PCharacterSkill digSkill = pc.getCharacterSkill("Digging");
				int digLevel = pc.getSkillLevel("Digging");
				if (digSkill == null)
				{
					this.log(pc.getName() + " has default digging skill level: " + digLevel);
				}
				else
				{
					this.log(pc.getName() + " has digging skill level: " + digLevel);
				}
				if (JurpeUtils.successRoll(digLevel))
				{
					RpgMap currentMap = dungeon.getCurrentMap();
					Direction where = Direction.fromString(this.command.getOption());
					Cell fromCell = dungeon.getAvatar().getPlaceholder().getCell();
					fromCell.setCorridor(where, true);
					Cell digCell = currentMap.getCell(fromCell, where);
					if (digCell.isWall())
					{
						digCell.setWall(false);
						// Open all inverse corridors...
						for (Direction dir : Direction.values())
						{
							Cell destCell = currentMap.getCell(digCell, dir);
							if (!destCell.isWall())
							{
								destCell.setCorridor(Direction.getInverse(dir), true);
							}
						}
					}
					this.log(pc.getName() + " succeeds in digging.");
				}
				else
				{
					this.log(pc.getName() + " fails in digging.");
				}
			}
			else
			{
				this.log("You should use a shovel to dig...");
			}
		}
	}

	private void attackMonster() throws JurpeException
	{
		String monsterName = this.command.getOption();
		Level currentLevel = this.dungeon.getCurrentLevel();
		Monster monsterToFight = currentLevel.getMonsters().getByName(monsterName);
		if (monsterToFight != null)
		{
			if (warning("Attack " + monsterToFight.toString() + "?"))
			{
				this.parent.showCombat(monsterName);
				if (currentLevel.getMonsters().getDungeonMonsters().size() == 0)
				{
					int dungeonLevel = this.parent.getSystem().getDungeon().getCurrentMapLevel();
					this.log("Compliments! You cleared dungeon level #" + dungeonLevel);
				}
			}
		}
	}
        
        private void enterMagesGuild()
        {
            	if (warning("Would you like to enter the Mages Guild?"))
		{
			this.parent.enablePanel(TabbedPanel.TAB_MAGES, true);
			this.parent.getUIPanels().setSelectedIndex(this.parent.getUIPanels().indexOfTab(GameStrings.MAGESGUILD));
		}
            
        }

	private void enterInn() throws JurpeException
	{
		if (warning("Would you like to enter the Inn?"))
		{
			this.parent.enablePanel(TabbedPanel.TAB_INN, true);
			this.parent.getUIPanels().setSelectedIndex(this.parent.getUIPanels().indexOfTab(GameStrings.INN));
		}
	}

	private void enterShop() throws JurpeException
	{
		if (warning("Would you like to enter the shop?"))
		{
			this.parent.enablePanel(TabbedPanel.TAB_SHOP, true);
			this.parent.getUIPanels().setSelectedIndex(this.parent.getUIPanels().indexOfTab(GameStrings.SHOP));
		}
	}

	private void enterTrainer()
	{
		if (warning("Your trainer welcomes you. Would you like to start a session now?"))
		{
			this.parent.getUIPanels().setSelectedIndex(this.parent.getUIPanels().indexOfTab(GameStrings.SKILLS));
			this.parent.allowSkillImprovements(true);
		}
	}

	private void goDownStairs() throws JurpeException
	{
		Avatar avatar = dungeon.getAvatar();
		int currentLevel = avatar.getZLevel();
		String warn = "Would you like to go downstairs?";

		if (currentLevel == 0)
		{
			warn = "Would you like to enter dungeon?";
		}

		if (warning(warn))
		{
			Level newLevel = this.dungeon.getDungeonLevel(++currentLevel);
			this.dungeon.setCurrentLevel(newLevel);
			this.dungeonFirstDraw(newLevel);

			// Avatar is in the stairs up location
			// of the new dungeon
			this.setAvatarPosition(LocationType.STAIRSUP);

			// Synchronize HexMapView
			this.parent.getHexMap().synchHexMapView();
			this.parent.getUIPanelGame().doLayout();
		}
	}

	private void goUpStairs() throws JurpeException
	{
		Avatar avatar = dungeon.getAvatar();
		int currentLevel = avatar.getZLevel();
		String warn = "Would you like to go upstairs?";

		if (warning(warn))
		{
			Level newLevel = this.dungeon.getDungeonLevel(--currentLevel);
			this.dungeon.setCurrentLevel(newLevel);
			this.dungeonFirstDraw(newLevel);
			this.setAvatarPosition(LocationType.STAIRSDOWN);

			this.parent.getHexMap().synchHexMapView();
			this.parent.getUIPanelGame().doLayout();
		}
	}

	private void setAvatarPosition(LocationType location) throws JurpeException
	{
		Avatar avatar = this.dungeon.getAvatar();
		Cell downLocation = dungeon.getCurrentMap().getSpecialCell(location);
		if ((downLocation == null) || (!dungeon.getCurrentMap().isInside(avatar.getPlaceholder().getPosition())))
		{
			System.err.println(">>> Cannot find " + location.toString() + " here...");
			avatar.setInitialPosition(this.parent.getHexMap());
		}
		else
		{
			RpgMapPoint avatarPoint = downLocation.getCoordinates();
			avatar.getPlaceholder().setPosition(avatarPoint);
		}
	}

	/**
	 * Find an initial place for every placeholder which is not occupied by
	 * another placeholder
	 * 
	 * @param dungeonLevel
	 *            Level of the dungeon
	 * @throws JurpeException
	 */
	private void setPlaceHoldersPosition(Level dungeonLevel) throws JurpeException
	{

		if (dungeonLevel == null)
		{
			throw new JurpeException("DungeonCommander->addMonstersToDungeon : dungeon level is NULL");
		}

		// Set a position for every placeholder
		for (Placeholder ph : dungeonLevel.placeHolders())
		{
			RpgMapPoint p = ph.getPosition();
			if (p == null)
			{
				do
				{
					ph.setDefaultInitialPosition();
					// Look if there is no another monster in the same
					// position
					p = ph.getPosition();
				}
				while (dungeonLevel.isTherePlaceHolder(p));
			}
		}
	}

	private void dungeonFirstDraw(Level dungeonLevel) throws JurpeException
	{
		// Draw dungeon structure
		this.dungeon.drawDungeon(dungeonLevel, this.parent.getHexMap());

		// Add placeholders
		this.setPlaceHoldersPosition(dungeonLevel);
	}

	/**
	 * Print a warning on screen, asking yes or no. If user chooses yes return
	 * true, else false.
	 * 
	 * @param message
	 *            Warning message to display
	 * @return true if user choose YES
	 */
	private boolean warning(String message)
	{
		boolean yesgo = false;
		if (this.showQuestionMessage(message) == JOptionPane.YES_OPTION)
		{
			yesgo = true;
		}
		return yesgo;
	}

	/**
	 * Write a message to the system log
	 * 
	 * @param message
	 *            Message to log
	 */
	private void log(String message)
	{
		this.parent.getSystem().getLog().addEntry(message);
	}

	/**
	 * Autorecovery
	 */
	private void autoRecovery()
	{
		GameWorld gw = this.dungeon.getSystem().getGameWorld();
		GameTime gt = gw.getGameTime();
		gt.addOneMinute();

		// Automatic recovery
		if ((gt.getMinute() % 5) == 0)
		{
			PCharacter pc = this.parent.getSystem().getPC();
			if (pc != null)
			{
				int constitution = pc.getPrimaryStats().getHT();
				if (pc.getCurrentHP() < constitution)
				{
					if (JurpeUtils.successRoll(constitution))
					{
						pc.addToCurrentHP(1);
						this.dungeon.getLog().addEntry("You naturally regain 1 hit point");
						if (pc.getPrimaryStats().getFatigue() > 0)
						{
							pc.getPrimaryStats().setFatigue(0);
							this.dungeon.getLog().addEntry("Your fatigue vanishes.");
						}
					}
				}
			}
		}
	}

}
