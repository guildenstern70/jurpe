/**
 J.U.R.P.E. @version@ Swing Demo
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

package net.littlelite.jurpedemo.frames;

import java.awt.Color;
import java.awt.event.*;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import net.littlelite.jurpe.characters.CharacterAttributes;
import net.littlelite.jurpe.characters.Monster;
import net.littlelite.jurpe.characters.PC;
import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.combat.*;
import net.littlelite.jurpe.items.Weapon;
import net.littlelite.jurpe.system.*;
import net.littlelite.jurpe.system.resources.LibraryStrings;
import net.littlelite.jurpe.system.resources.ResourceFinder;
import net.littlelite.jurpedemo.resources.GameStrings;

/**
 * Handles all UI commands for Combat dialog form
 * 
 */
public class FrameCombat extends JDialogCombatBase
{
	private static final long serialVersionUID = 3L;

	private Core theSystem;
	private Timer lastLogTimer;
	private boolean fastlogs;
	private DualCombat dualCombat;
	Color currentPanelColor;
	private ResourceFinder rf;

	/**
	 * The main class for combats
	 * 
	 * @param xp
	 *            Parent JurpeMain window
	 * @param monsterName
	 *            Name of the monster
	 * @param fastlog
	 *            If true, fastlogs are enabled
	 */
	public FrameCombat(JurpeMain xp, String monsterName, boolean fastlog)
	{
		super(xp);

		try
		{
			this.rf = ResourceFinder.resources();
			this.theSystem = xp.getSystem();
			this.theSystem.enterCombatWith(monsterName);
			this.dualCombat = (DualCombat) this.theSystem.getCombat();
			this.initPortraitsImage();
			this.fastlogs = fastlog;
			this.customInit();
			this.currentPanelColor = this.jPnlFightee.getBackground();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void refreshPanel()
	{
		// Refresh title bar with current turn
		this.setTitle(LibraryStrings.COMBAT + " " + LibraryStrings.TURN.toLowerCase() + " " + this.theSystem.getTurnTable().getCurrentTurn());

		// Refresh current PC weapon
		this.refreshWeapon();

		// Refresh Fighter/Fightee panels with names and HTs
		this.refreshFighterFighteePanels();

		this.refreshLog();

	}

	@Override
	protected void processWindowEvent(WindowEvent e)
	{
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
		{
			if (!this.dualCombat.isAlive())
			{
				this.dispose();
			}
			else
			{
				JOptionPane.showMessageDialog(this, GameStrings.COMBATALIVE, Config.APPNAME, JOptionPane.WARNING_MESSAGE);
			}
		}

		super.processWindowEvent(e);
	}

	private void customInit()
	{
		this.initToolbar();
		this.initWeapon();
		this.initActiveDefense();

		PCharacter fighter = this.dualCombat.getHumanFighter();
		Monster fightee = this.dualCombat.getMonsterFighter();

		// Player weapon
		Weapon pcWeapon = fighter.getCurrentWeapon();
		if (pcWeapon != null)
		{
			this.jTxtArmaCorrente.setToolTipText(pcWeapon.getDescription(" - "));
		}

		this.jTxtArmaCorrente.setText(FrameCombat.weaponName(pcWeapon));

		// Monster Weapon
		Weapon monsterWeapon = fightee.getCurrentWeapon();
		if (monsterWeapon != null)
		{
			this.jTxtMonsterWeapon.setText(FrameCombat.weaponName(monsterWeapon));
			this.jTxtMonsterWeapon.setToolTipText(monsterWeapon.getDescription(" - "));
			this.jTxtMonsterWpnMode.setText(monsterWeapon.getStringMode());
		}
		else
		{
			this.jTxtMonsterWeapon.setText("No weapon");
			this.jTxtMonsterWeapon.setToolTipText("");
			this.jTxtMonsterWpnMode.setText("");
		}

		this.refreshPanel();
		this.jPnlFighter.requestFocus();

	}

	/**
	 * Any command is passed to theSystem.executeCommand(). After that, control
	 * is passed to AI through theSystem.continueCombat()
	 * 
	 * @param commandString
	 *            command
	 * @throws JurpeException
	 */
	void executeGUICommand(String commandString) throws JurpeException
	{

		if (!this.dualCombat.isAlive())
		{
			if (commandString.equals("Exit"))
			{
				this.dispose();
			}

			return;
		}

		// When user selects a command, System plays the move.
		// Then control passes to next player through TurnTable. If next player
		// is AI
		// AI will choose next command. Else it exits...
		ICommand userCommand = this.getUserCommand(commandString);

		// Set weapon mode
		this.setWeaponMode();

		// Set active defense
		this.setActiveDefense();

		// Execute command
		if (userCommand != null)
		{
			CommandSequence cs = new CommandSequence();
			cs.add(userCommand);
			this.theSystem.executeCommand(cs);
			while (!this.theSystem.continueCombat())
			{
				// After having chosen any move, combat must go on...
			}
			if (!this.dualCombat.isAlive())
			{
				this.endCombat();
			}
			else
			{
				// Special case: auto ready weapon
				if ((this.jChkAutoReady.isSelected()) && (!this.theSystem.getPC().isReady()))
				{
					CommandSequence crw = new CommandSequence();
					crw.add(new ReadyWeapon(this.dualCombat));
					this.theSystem.executeCommand(crw);
					while (!this.theSystem.continueCombat())
					{
					}
					if (!this.dualCombat.isAlive())
					{
						this.endCombat();
					}
				}
			}
		}

		this.refreshPanel();
	}

	private void endCombat()
	{
		// Enable exit button
		this.jToolBar.getComponent(5).setEnabled(true);
		this.refreshPanel();
	}

	private void initWeapon()
	{
		PCharacter curPC;

		if (this.theSystem.isPCgenerated())
		{
			curPC = this.theSystem.getPC();
			if (curPC.wearsWeapon())
			{
				Weapon weapon = curPC.getCurrentWeapon();
				if (weapon.isBalanced())
				{
					this.jToolBar.getComponent(4).setEnabled(false);
					this.jChkAutoReady.setEnabled(false);
				}
				DefaultComboBoxModel dcbm = new DefaultComboBoxModel(weapon.getAttacks());
				this.jCboWeaponMode.setModel(dcbm);
			}
			else
			{
				this.theSystem.getLog().addEntry(GameStrings.CHRNOTARM);
			}
		}
		else
		{
			this.theSystem.getLog().addEntry(GameStrings.NONEST);
		}
	}

	private static String weaponName(Weapon weapon)
	{
		String weaponName;

		if (weapon == null)
		{
			weaponName = "None";
		}
		else
		{
			weaponName = weapon.toString();
			if (weaponName.length() > 15)
			{
				StringTokenizer st = new StringTokenizer(weaponName, " ", true);
				weaponName = "";
				while (weaponName.length() <= 15)
				{
					weaponName += st.nextToken();
				}
			}
		}
		return weaponName;
	}

	private void initActiveDefense()
	{
		PCharacter curPC;

		if (this.theSystem.isPCgenerated())
		{
			curPC = this.theSystem.getPC();
			DefenseType[] activeDefenses = curPC.getActiveDefensesAvailable();
			DefaultComboBoxModel dcbm = new DefaultComboBoxModel(activeDefenses);
			this.jCboActiveDefense.setModel(dcbm);

		}
		else
		{
			this.theSystem.getLog().addEntry(GameStrings.NONEST);
		}

		this.refreshLog();

	}

	private void refreshWeapon()
	{
		PC curPC = this.theSystem.getPC();

		if (curPC != null)
		{
			Weapon weapon = this.theSystem.getPC().getCurrentWeapon();
			if (weapon != null)
			{
				int indexAttacchi = weapon.getIndexAttacks(weapon.getMode());
				this.jCboWeaponMode.setSelectedIndex(indexAttacchi);
			}
		}
	}

	private static String buildCharacterInfo(PC fighter)
	{
		return fighter.getInfo("  ");
	}

	/**
	 * Return first nWords words from input string
	 * 
	 * @param input
	 *            string
	 * @param nWords
	 *            number of words
	 * @return first nWords of input
	 */
	private static String getFirstWords(String input, int nWords)
	{
		short count = 0;
		StringBuilder first = new StringBuilder();
		StringTokenizer st = new StringTokenizer(input, " ", true);

		if (st.countTokens() == 0)
		{
			return input;
		}

		while (st.hasMoreTokens())
		{
			count++;
			if (count == nWords)
			{
				break;
			}
			first.append(st.nextToken());
		}

		return first.toString();

	}

	private void refreshFighterFighteePanels()
	{

		PCharacter fighter = this.dualCombat.getHumanFighter();
		Monster fightee = this.dualCombat.getMonsterFighter();

		boolean firstTime = true;

		// Dynamic tooltips showing characters properties.
		this.jPnlFighter.setToolTipText(FrameCombat.buildCharacterInfo(fighter));
		this.jPnlFightee.setToolTipText(FrameCombat.buildCharacterInfo(fightee));

		// Name of fighters
		this.jLblFighterName.setText(fighter.getName());
		this.jLblFighteeName.setText(FrameCombat.getFirstWords(fightee.getName(), 4));

		String oldFgtHT = null;
		String oldFgeHT = null;

		// Refresh Health, blinking if it changes
		String HTFighter = this.jTxtHTFighter.getText();
		String HTFighteee = this.jTxtHTFighteee.getText();
		if (!HTFighter.equals(""))
		{
			oldFgtHT = HTFighter;
			oldFgeHT = HTFighteee;
			firstTime = false;
		}
		this.jTxtHTFighter.setText(String.valueOf(fighter.getCurrentHP()));
		this.jTxtHTFighteee.setText(String.valueOf(fightee.getCurrentHP()));

		String fgtHT = this.jTxtHTFighter.getText();
		String fgeHT = this.jTxtHTFighteee.getText();

		this.refreshFighterReadyness(fighter.isReady());
		this.refreshFighteeReadyness(fightee.isReady());

		// The combat is over
		if (!this.dualCombat.isAlive())
		{
			this.jToolBar.setEnabled(false);
			// The combat is over because fighter is died...
			if (!fighter.isAlive())
			{
				this.jTxtHTFighter.setBackground(Color.red);
				this.refreshFighterReadyness(false);
				this.blinkPanel(this.jPnlFighter, 12, 150);
			}
			else if (!fightee.isAlive())
			// The combat is over because fightee is died
			{
				this.jTxtHTFighteee.setBackground(Color.red);
				this.refreshFighteeReadyness(false);
				this.blinkPanel(this.jPnlFightee, 12, 150);
			}
			else
			// The combat is over because fightee escaped
			{
				this.refreshFighteeReadyness(false);
				this.jTxtHTFighteee.setBackground(Color.GREEN);
				this.blinkPanel(this.jPnlFightee, 6, 110, Color.GREEN);
			}
		}
		else
		// the combat is still taking place...
		{
			// Active Defense
			String actDefense = fightee.getActiveDefense().toString();
			this.jTxtMonsterActiveDef.setText(actDefense);

			if (!firstTime)
			{
				if (!fgtHT.equals(oldFgtHT))
				{
					this.blinkPanel(this.jPnlFighter);
				}
				if (!fgeHT.equals(oldFgeHT))
				{
					this.blinkPanel(this.jPnlFightee);
				}
			}

		}
		this.jPnlFightee.add(this.jTxtHTFighteee, null);
		this.jPnlFightee.add(this.jTxtFighteeReady, null);

	}

	private void refreshFighterReadyness(boolean value)
	{
		FrameCombat.setReadyness(this.jTxtFighterReady, value);
	}

	private void refreshFighteeReadyness(boolean value)
	{
		FrameCombat.setReadyness(this.jTxtFighteeReady, value);
	}

	private static void setReadyness(JTextField jtxt, boolean value)
	{
		if (value)
		{
			jtxt.setBackground(Color.green);
			jtxt.setText(GameStrings.YES);
		}
		else
		{
			jtxt.setBackground(Color.red);
			jtxt.setText(GameStrings.NO);
		}
	}

	private void refreshLog()
	{
		Log log = this.theSystem.getLog();
		String[] entries = log.getEntries();

		if (entries.length > 0) // if new log entries are available
		{
			if (this.lastLogTimer != null)
			{
				this.lastLogTimer.cancel();
				this.lastLogTimer = null;
			}
			final DefaultListModel list = (DefaultListModel) this.jLstLog.getModel();
			list.removeAllElements();
			if (!this.fastlogs) // slow paced log
			{
				int count = 0;
				this.lastLogTimer = new Timer();
				for (String element : entries)
				{
					this.lastLogTimer.schedule(new TimerLog(this.jLstLog, element), (200 * count + 200));
					count++;
				}
			}
			else
			{
				for (String element : entries)
				{
					list.addElement(element);
				}

				SwingUtilities.invokeLater(new Runnable()
				{
                                        @Override
					public void run()
					{
						int sz = list.getSize();
						FrameCombat.this.jLstLog.setSelectedIndex(sz - 1);
						FrameCombat.this.jLstLog.ensureIndexIsVisible(sz - 1);
					}
				});
			}
		}
	}

	/*
	 * Sample implementation of Command Design Pattern usage. Command stored
	 * here will be executed in this.executeGUICommand()
	 */
	private ICommand getUserCommand(String userCommandString)
	{

		ICommand cmd = null;
		Log lg = this.theSystem.getLog();

		if (userCommandString != null)
		{
			if (userCommandString.equals(LibraryStrings.CM_AIM))
			{
				lg.addEntry(GameStrings.NOTYETIMPL);
			}
			else if (userCommandString.equals(LibraryStrings.CM_ALLOATT))
			{
				cmd = new AllOutAttack(this.dualCombat);
			}
			else if (userCommandString.equals(LibraryStrings.CM_ALLODEF))
			{
				cmd = new AllOutDefense(this.dualCombat);
			}
			else if (userCommandString.equals(LibraryStrings.CM_ATTAK))
			{
				cmd = new Attack(this.dualCombat);
			}
			else if (userCommandString.equals(LibraryStrings.CM_MOVE))
			{
				cmd = new Move(this.dualCombat);
			}
			else if (userCommandString.equals(LibraryStrings.CM_RDYWPN))
			{
				cmd = new ReadyWeapon(this.dualCombat);
			}
			else if (userCommandString.equals(LibraryStrings.CM_RELOAD))
			{
				lg.addEntry(GameStrings.NOTYETIMPL);
			}
		}

		return cmd;
	}

	private void setWeaponMode()
	{
		DamageMode weaponMode = DamageMode.NOTHING;

		String weaponStrMode = (String) this.jCboWeaponMode.getSelectedItem();
		PCharacter curPC = this.theSystem.getPC();
		Weapon currentWeaponPC = curPC.getCurrentWeapon();
                
                if (currentWeaponPC == null)
                {
                    return;
                }

		if (weaponStrMode != null)
		{
			if (weaponStrMode.startsWith("CUT"))
			{
				weaponMode = DamageMode.CUTTING;
				this.theSystem.getLog().addEntry(GameStrings.CUTDAMAGE);
			}
			else if (weaponStrMode.startsWith("IMP"))
			{
				weaponMode = DamageMode.IMPALING;
				this.theSystem.getLog().addEntry(GameStrings.IMPDAMAGE);
			}
			else if (weaponStrMode.startsWith("CRU"))
			{
				weaponMode = DamageMode.CRUSHING;
				this.theSystem.getLog().addEntry(GameStrings.CRUDAMAGE);
			}
			currentWeaponPC.setMode(weaponMode);
		}
	}

	private void setActiveDefense()
	{
		DefenseType activeDefense = (DefenseType) this.jCboActiveDefense.getSelectedItem();
		PCharacter curPC = this.theSystem.getPC();

		if (activeDefense.equals(DefenseType.ACTIVE_DODGE))
		{
			curPC.setActiveDefense(DefenseType.ACTIVE_DODGE);
			this.theSystem.getLog().addEntry(GameStrings.ACTIVSET + " " + LibraryStrings.ACTDODGE2);
		}
		else if (activeDefense.equals(DefenseType.ACTIVE_BLOCK))
		{
			curPC.setActiveDefense(DefenseType.ACTIVE_BLOCK);
			this.theSystem.getLog().addEntry(GameStrings.ACTIVSET + " " + LibraryStrings.ACTBLOCK2);
		}
		else if (activeDefense.equals(DefenseType.ACTIVE_PARRY))
		{
			curPC.setActiveDefense(DefenseType.ACTIVE_PARRY);
			this.theSystem.getLog().addEntry(GameStrings.ACTIVSET + " " + LibraryStrings.ACTPARRY2);
		}
	}

	private void initPortraitsImage()
	{
		Monster fightee = this.dualCombat.getMonsterFighter();
		CharacterAttributes ca = this.theSystem.getPC().getCharacterAttributes();
		ImageIcon imagePC = this.rf.getResourceAsImage(ca.getImageFileName());
		String monsterImageFile = fightee.getCharacterAttributes().getImageFileName();
		ImageIcon imageMonster = this.rf.getResourceAsImage(monsterImageFile);
		this.jImgPC.setIcon(imagePC);
		this.jImgMonster.setIcon(imageMonster);
	}

	private void initToolbar()
	{
		JButton button = null;

		// ATTACK button (0)
		button = this.makeToolbarButton("att.gif", LibraryStrings.CM_ATTAK, "Attack [A]", "ATT");
		this.jToolBar.add(button);

		// ALL OUT ATTACK button (1)
		button = this.makeToolbarButton("aoa.gif", LibraryStrings.CM_ALLOATT, "All Out Attack [S]", "AOA");
		this.jToolBar.add(button);

		// ALL OUT DEFENSE button (2)
		button = this.makeToolbarButton("aod.gif", LibraryStrings.CM_ALLODEF, "All Out Defense [D]", "AOD");
		this.jToolBar.add(button);

		// MOVE button (3)
		button = this.makeToolbarButton("mov.gif", LibraryStrings.CM_MOVE, "Escape [F]", "MOV");
		this.jToolBar.add(button);

		// READY WEAPON button (4)
		button = this.makeToolbarButton("rdy.gif", LibraryStrings.CM_RDYWPN, "Ready weapon [Z]", "RDW");
		this.jToolBar.add(button);

		// EXIT button (5)
		button = this.makeToolbarButton("exit.gif", "Exit", "Exit [X]", " EXIT ");
		this.jToolBar.add(button);

		// Disable exit and aim button
		this.jToolBar.getComponent(5).setEnabled(false); // exit

	}

	private JButton makeToolbarButton(String imageName, String actionCommand, String toolTipText, String altText)
	{
		ImageIcon toolbarIcon = null;

		if (imageName != null)
		{
			// Look for the image.
			StringBuilder iconLocation = new StringBuilder("icos");
			iconLocation.append("/");
			iconLocation.append(imageName);
			toolbarIcon = ResourceFinder.resources().getResourceAsImage(iconLocation.toString());
		}

		// Create and initialize the button.
		JButton button = new JButton();
		button.setActionCommand(actionCommand);
		button.setToolTipText(toolTipText);
		ToolBarListener tbl = new ToolBarListener(button);
		button.addActionListener(tbl);

		if (toolbarIcon != null)
		{ // image found
			toolbarIcon.setDescription(altText);
			button.setIcon(toolbarIcon);
		}
		else
		{ // no image found
			button.setText(altText);
		}

		return button;
	}

	class KeyPressListener implements KeyListener
	{
		@Override
		public void keyReleased(KeyEvent ke)
		{
		}

		@Override
		public void keyTyped(KeyEvent ke)
		{
		}

		@Override
		public void keyPressed(KeyEvent ke)
		{
			String command = this.getCommand(ke.getKeyCode());
			try
			{
				if (command != null)
				{
					FrameCombat.this.executeGUICommand(command);
				}
			}
			catch (JurpeException jex)
			{
				jex.printStackTrace();
				System.exit(-1);
			}
		}

		private String getCommand(int keyCode)
		{
			String command = null;

			switch (keyCode)
			{
				case KeyEvent.VK_A:
					command = LibraryStrings.CM_ATTAK;
					break;

				case KeyEvent.VK_S:
					command = LibraryStrings.CM_ALLOATT;
					break;

				case KeyEvent.VK_D:
					command = LibraryStrings.CM_ALLODEF;
					break;

				case KeyEvent.VK_F:
					command = LibraryStrings.CM_MOVE;
					break;

				case KeyEvent.VK_Z:
					command = LibraryStrings.CM_RDYWPN;
					break;

				case KeyEvent.VK_X:
					command = "Exit";
					break;
			}

			return command;

		}

	}

	class ToolBarListener implements ActionListener
	{
		private String actionCommand;

		public ToolBarListener(JButton btn)
		{
			this.actionCommand = btn.getActionCommand();
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				FrameCombat.this.executeGUICommand(this.actionCommand);
			}
			catch (JurpeException jex)
			{
				jex.printStackTrace();
				System.exit(-1);
			}
		}
	}

	class Blinker
	{
		// Blinks panel
		private java.util.Timer timer;

		Color colour;
		JPanel pan;

		/**
		 * Constructor
		 * 
		 * @param panel
		 *            panel to blink
		 * @param times
		 *            times to blink
		 * @param interval
		 *            milliseconds between one blink and the other
		 */
		Blinker(JPanel panel, int times, int interval)
		{
			this.pan = panel;
			this.timer = new java.util.Timer();

			for (int j = 1; j < times; j++)
			{
				this.timer.schedule(new Blink(Color.red), j * interval);
			}

			this.colour = Color.red;
		}

		Blinker(JPanel panel, int times, int interval, Color blinkColor)
		{
			this.pan = panel;
			this.timer = new java.util.Timer();

			for (int j = 1; j < times; j++)
			{
				this.timer.schedule(new Blink(blinkColor), j * interval);
			}

			this.colour = blinkColor;
		}

		class Blink extends TimerTask
		{
			private Color bColor;

			public Blink(Color blink)
			{
				this.bColor = blink;
			}

			@Override
			public void run()
			{
				if (Blinker.this.colour.equals(this.bColor))
				{
					Blinker.this.colour = FrameCombat.this.currentPanelColor;
				}
				else
				{
					Blinker.this.colour = this.bColor;
				}
				Blinker.this.pan.setBackground(Blinker.this.colour);
			}
		}
	}

	@SuppressWarnings("unused")
	private void blinkPanel(JPanel panel)
	{
		new Blinker(panel, 8, 300);
	}

	@SuppressWarnings("unused")
	private void blinkPanel(JPanel panel, int times, int interval)
	{
		new Blinker(panel, times, interval);
	}

	@SuppressWarnings("unused")
	private void blinkPanel(JPanel panel, int times, int interval, Color blink)
	{
		new Blinker(panel, times, interval, blink);
	}

}

final class TimerLog extends TimerTask
{
	private String entry;
	private DefaultListModel list;
	private JList control;

	public TimerLog(JList jLstLog, String xentri)
	{
		this.control = jLstLog;
		this.list = (DefaultListModel) jLstLog.getModel();
		this.entry = xentri;
	}

	@Override
	public void run()
	{
		this.list.addElement(this.entry);
		int sz = this.list.getSize();
		this.control.setSelectedIndex(sz - 1);
		this.control.ensureIndexIsVisible(sz - 1);
	}
}
