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
package net.littlelite.jurpedemo.frames;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import javax.swing.*;
import net.littlelite.jurpe.characters.PC;
import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.characters.PrimaryStats;
import net.littlelite.jurpe.characters.Skill;
import net.littlelite.jurpe.combat.AllOutAttackType;
import net.littlelite.jurpe.containers.Inventory;
import net.littlelite.jurpe.dungeon.Avatar;
import net.littlelite.jurpe.dungeon.Dungeons;
import net.littlelite.jurpe.dungeon.Level;
import net.littlelite.jurpe.dungeon.hexmap.HexMap;
import net.littlelite.jurpe.dungeon.rpgmap.Cell;
import net.littlelite.jurpe.items.AbstractItem;
import net.littlelite.jurpe.system.*;
import net.littlelite.jurpe.system.commands.DungeonCommand;
import net.littlelite.jurpe.system.resources.LibraryStrings;
import net.littlelite.jurpe.system.resources.ResourceFinder;
import net.littlelite.jurpe.world.Inn;
import net.littlelite.jurpe.world.LocationType;
import net.littlelite.jurpedemo.JurpeDemoConfig;
import net.littlelite.jurpedemo.Options;
import net.littlelite.jurpedemo.frames.panels.*;
import net.littlelite.jurpedemo.logic.DungeonCommander;
import net.littlelite.jurpedemo.resources.GameStrings;
import net.littlelite.jurpedemo.wizards.JWizard;
import net.littlelite.jurpedemo.wizards.WizardController;

public final class JurpeMain extends AbstractJurpeMain
{

    private Core theSystem;
    private JFileChooser jFileChooser;

    // Panels Declaration
    protected JPnlGame pnlGame;
    protected JPnlCharacter pnlCharacter;
    protected JPnlInventory pnlInventory;
    protected ShopPanel pnlShop;
    protected JPnlSkills pnlSkills;
    protected JPnlInn pnlInn;
    protected JPnlMages pnlMagesGuild;
    protected JPnlGrimoire pnlGrimoire;

    /**
     * JurpeMain frame constructor
     * 
     * @param system
     * @throws JurpeException
     */
    public JurpeMain(Core system) throws JurpeException
    {
        this.theSystem = system;
        this.jFileChooser = new JFileChooser();

        this.setTitle(Config.APPNAME);
        this.initFileChooser();
        this.addTabs();

        // Hide panels Hero can reach only when on a location
        this.restorePanels();

        /* Options */
        this.options = Options.getOptions(this.theSystem.getLog());
        JurpeLookAndFeel laf = this.options.getLookAndFeel();
        this.setLookAndFeel(laf);
        this.LAFSynchronize(laf);
        this.currentFastLog = this.options.getFastlogs();
        this.jCheckBoxMenuItemFastLogs.setSelected(this.currentFastLog.isEnabled());

        /* All Done */
        this.setEnabled(true);
    }

    /**
     * Get access to all handles
     * 
     * @return
     */
    @Override
    public Core getSystem()
    {
        return this.theSystem;
    }

    /**
     * Write an announce to log and status bar
     * 
     * @param testo
     *            text to output
     */
    @Override
    public void announce(String text)
    {
        // add text to log
        this.theSystem.getLog().addEntry(text);

        // updates status bar
        this.jlblStatusBar.setText(text);
    }

    /**
     * If the panel Skills may show skill adding and improvements
     * 
     * @param enable
     */
    public void allowSkillImprovements(boolean enable)
    {
        this.pnlSkills.enableImproving(enable);
    }

    // Accessors for UI elements
    public JTabbedPane getUIPanels()
    {
        return this.jTabbedPane;
    }

    /**
     * @return
     */
    public JPanel getUIPanelGame()
    {
        return this.pnlGame;
    }

    /**
     * @return
     */
    public HexMap getHexMap()
    {
        return this.pnlGame.getHexMap();
    }

    @Override
    protected void menuFileExit()
    {
        this.exitProgram();
    }

    private void addTabs() throws JurpeException
    {
        this.pnlGame = new JPnlGame(this);
        this.pnlCharacter = new JPnlCharacter(this);
        this.pnlInventory = new JPnlInventory(this);
        this.pnlSkills = new JPnlSkills(this);
        this.pnlShop = new ShopPanel(this);
        this.pnlInn = new JPnlInn(this);
        this.pnlMagesGuild = new JPnlMages(this);
        this.pnlGrimoire = new JPnlGrimoire(this);

        this.jTabbedPane.addTab(LibraryStrings.GAME, null, this.pnlGame, null); // TAB_GAME
        this.jTabbedPane.addTab(GameStrings.CHARACTER, null, this.pnlCharacter, null); // TAB_CHARACTER
        this.jTabbedPane.addTab(GameStrings.INVENTORY, null, this.pnlInventory, null); // TAB_INVENTORY
        this.jTabbedPane.addTab(GameStrings.SKILLS, null, this.pnlSkills, null); // TAB_SKILLS
        this.jTabbedPane.addTab(GameStrings.SHOP, null, this.pnlShop, null); //TAB_SHOP
        this.jTabbedPane.addTab(GameStrings.INN, null, this.pnlInn, null);
        this.jTabbedPane.addTab(GameStrings.MAGESGUILD, null, this.pnlMagesGuild, null);

    // We do not add pnlGrimoire for now. See setGrimoirePanel
    }

    @Override
    protected void menuFileNew()
    {
        this.generateCharacter();
    }

    @Override
    protected void menuFileOpen()
    {
        try
        {
            this.loadGUICharacter();
        }
        catch (JurpeException jex)
        {
            this.theSystem.getLog().addEntry(jex.getMessage());
        }
    }

    @Override
    protected void menuFileSaveAs()
    {
        this.saveAsCharacter();
    }

    @Override
    protected void menuFileQuickLoad()
    {
        try
        {
            this.loadCharacter();
        }
        catch (JurpeException jex)
        {
            this.theSystem.getLog().addEntry(jex.getMessage());
        }
    }

    @Override
    protected void menuFileQuickSave()
    {
        this.saveCharacter();
    }

    @Override
    protected void menuFileLogToFile()
    {
        try
        {
            this.setLogFile(true);
        }
        catch (IOException iex)
        {
            this.theSystem.getLog().addEntry(iex.getMessage());
        }
    }

    /**
     * Refresh Log
     */
    public void refreshLog()
    {
        Log log = this.theSystem.getLog();
        final JList jList = this.pnlGame.getConsole();
        DefaultListModel list = (DefaultListModel) jList.getModel();
        String[] entries = log.getEntries();
        int nrEntries = entries.length;

        if (nrEntries > 0)
        {
            // Remove '>' from last entry
            int curSize = list.getSize();
            if (curSize > 0)
            {
                String last = (String) list.getElementAt(curSize - 1);
                if (last.charAt(0) == '>')
                {
                    last = last.substring(2, last.length());
                    list.remove(curSize - 1);
                    list.addElement(last);
                }
            }

            // Add elements (last element will begin with ">"
            String entry;
            for (int j = 0; j < nrEntries; j++)
            {
                entry = entries[j];
                if (j == nrEntries - 1)
                {
                    entry = "> " + entry;
                }
                list.addElement(entry);
                jList.setSelectedIndex(j);
            }

            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    int sz = ((DefaultListModel) jList.getModel()).getSize();
                    jList.ensureIndexIsVisible(sz - 1);
                }
            });
        }

        // Removes items when reached maxsize
        if (list.getSize() > Config.MAXLOGSIZE)
        {
            list.removeRange(Config.MAXLOGSIZE / 2, list.getSize() - 1);
        }

    }

    /**
     * Refresh Inventory (Inventory) Panel.
     */
    public void refreshInventory()
    {
        this.pnlInventory.refresh();
        if (!this.theSystem.isPCgenerated())
        {
            enablePanel(TabbedPanel.TAB_INVENTORY, false);
        }
    }

    /**
     * Examine selected item.
     */
    public void examineItem()
    {
        AbstractItem selBasicItem = (AbstractItem) this.pnlInventory.getJLstOggetti().getSelectedValue();
        if (selBasicItem != null)
        {
            this.examineItem(selBasicItem);
        }
        this.refreshInventory();
    }

    /**
     * Show dialog for selected item.
     * 
     * @param selBasicItem
     *            selected AbstractItem object
     * @see AbstractItem
     */
    public void examineItem(AbstractItem selBasicItem)
    {
        JDialogBase dialogbox = new JDialogBase(this, selBasicItem.getDescription(), "Examine item");
        dialogbox.setVisible(true);
    }

    /**
     * Learn selected Skill. When you learn a new skill, this is automatically
     * improved until this operation costs some character points (until
     * improving it's free)
     */
    public void addSkill(Skill selSkill)
    {
        int result = JOptionPane.showConfirmDialog(this, GameStrings.DLEARNSK + selSkill + "' ?", Config.APPNAME, JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION)
        {
            announce(GameStrings.IMPSKL + selSkill.toString() + GameStrings.ZEROCOST);
            if (!this.theSystem.addSkill(selSkill))
            {
                // You already learnt that skill
                JOptionPane.showMessageDialog(this, GameStrings.ALREADYSK + selSkill.toString(), Config.APPNAME, JOptionPane.WARNING_MESSAGE);
            }
        }

        this.refreshSkills();
    }

    /**
     * Improve selected skill. That skill must have been learned by character.
     */
    public void improveSkill()
    {
        String selectedSkill = this.pnlSkills.getSelectedSkill();
        if (selectedSkill.length() > 0)
        {
            float deltaCost = this.theSystem.getImprovingSkillCost(selectedSkill);
            String strCosto = String.valueOf(deltaCost);
            int result = JOptionPane.showConfirmDialog(this, GameStrings.WOULDYOU + strCosto + GameStrings.SKILLP + "?", Config.APPNAME,
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION)
            {
                if (!this.theSystem.getPC().improveSkill(selectedSkill, deltaCost))
                {
                    JOptionPane.showMessageDialog(this, GameStrings.NOMORECP, Config.APPNAME, JOptionPane.WARNING_MESSAGE);
                }
                this.refreshSkills();
            }
        }
    }

    /**
     * Wear selected item.
     */
    public void wearItem()
    {
        AbstractItem selBasicItem = (AbstractItem) this.pnlInventory.getJLstOggetti().getSelectedValue();
        if (selBasicItem != null)
        {

            if (selBasicItem.isItemWearable())
            {
                if (!selBasicItem.wear(this.theSystem.getPC()))
                {
                    JOptionPane.showMessageDialog(this, GameStrings.CANTWEAR + selBasicItem.toString() + ": " + selBasicItem.warningMessage(), Config.APPNAME,
                            JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(this, GameStrings.YOUWEAR + selBasicItem.toString(), Config.APPNAME, JOptionPane.INFORMATION_MESSAGE);
                    Inventory inv = this.theSystem.getPC().getInventory();
                    inv.removeBasicItem(selBasicItem);
                }
                this.refreshInventory();
            }
            else
            {
                this.announce(selBasicItem + GameStrings.ISNOTWRBL);
                this.theSystem.getLog().addEntry(selBasicItem + GameStrings.ISNOTWRBL);
            }
        }
    }

    /**
     * Use selected item.
     */
    public void useItem()
    {
        AbstractItem selBasicItem = (AbstractItem) this.pnlInventory.getJLstOggetti().getSelectedValue();
        if (selBasicItem != null)
        {
            if (selBasicItem.isItemUsable())
            {
                this.useItem(selBasicItem);
            }
            else
            {
                this.announce(selBasicItem + GameStrings.ISNOTUSBL);
                this.theSystem.getLog().addEntry(selBasicItem + GameStrings.ISNOTUSBL);
            }
        }
    }

    /**
     * Buy selected item, adding it to your inventory. It checks the current
     * Merchant skill of playing character. If Merchant skill is > 10, then for
     * each point over 10, the cost will be lowered of a 10% (ie: Merchant
     * skill=13, cost=cost*0,7) until a maximum of 50% discount. If Merchant
     * skill is minor than 8, then for each point under 8, the cost will be raised of a
     * 10% (ie: Merchant skill=5, cost=cost*1,3) until a maximum of 50% more.
     * 
     * @param selBasicItem
     *            Item to buy
     */
    public void buyItem(AbstractItem selBasicItem)
    {
        if (this.theSystem.isPCgenerated())
        {
            PCharacter pc = this.theSystem.getPC();
            Inventory curInv = pc.getInventory();
            String nomeoggetto = selBasicItem.getName();
            int finalCost = pc.getCustomizedCost(selBasicItem);
            JOptionPane.showMessageDialog(this, GameStrings.BARGAIN + String.valueOf(finalCost) + "$", nomeoggetto, JOptionPane.INFORMATION_MESSAGE);
            // Enough money?
            if (finalCost <= curInv.getAvailableMoney())
            {
                int result = JOptionPane.showConfirmDialog(this, GameStrings.WANTBUY + nomeoggetto + " ?", nomeoggetto, JOptionPane.YES_NO_OPTION);
                if (result != JOptionPane.NO_OPTION)
                {
                    JOptionPane.showMessageDialog(this, GameStrings.YOUBOUGHT + selBasicItem.getName(), nomeoggetto, JOptionPane.INFORMATION_MESSAGE);
                    // Spend money
                    curInv.spendMoney(finalCost);
                    // Add to inventory
                    pc.getInventory().addBasicItem(selBasicItem);
                    // Refresh
                    this.pnlShop.refresh();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(this, GameStrings.NOMONEY, Config.APPNAME, JOptionPane.WARNING_MESSAGE);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, GameStrings.CREATEC, Config.APPNAME, JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Refresh Character Panel
     */
    public void refreshCharacter()
    {
        if (this.theSystem.isPCgenerated())
        {
            PCharacter pc = this.theSystem.getPC();
            // Set character photo
            this.pnlCharacter.setImage(pc.getCharacterAttributes().getImageFileName());
            // Refresh score
            this.refreshScore();
        }
        this.pnlCharacter.refresh();
    }

    /**
     * Refresh available skills (Livelli) panel.
     */
    public void refreshSkills()
    {
        boolean canImprove = false;
        if (this.theSystem.isPCgenerated())
        {
            Dungeons dungeon = this.theSystem.getDungeon();
            if (dungeon != null)
            {
                Avatar av = dungeon.getAvatar();
                if (av != null)
                {
                    if (av.getPlaceholder().getCell().getLocation().getType().equals(LocationType.TRAINING))
                    {
                        canImprove = true;
                    }
                }
            }

        }
        this.pnlSkills.enableImproving(canImprove);
        this.pnlSkills.refresh();
    }

    /**
     * Refresh Inn
     */
    public void refreshInn()
    {
        this.pnlInn.refresh();
    }

    /**
     * Refresh current character score.
     */
    public void refreshScore()
    {
        StringBuilder scoreText = new StringBuilder();
        String score;
        // String level = "";
        // String monsters = "";

        DecimalFormat scoreFormatter = new DecimalFormat("0000");

        try    
        {
            if (this.theSystem.isPCgenerated())
            {
                Dungeons dungeon = this.theSystem.getDungeon();
                score = scoreFormatter.format(this.theSystem.getPC().getScore());
                Level lvl = dungeon.getCurrentLevel();
                scoreText.append(lvl.getLevelName());
                scoreText.append(" - Score: ");
                scoreText.append(score);
                // level = Integer.toString(dungeon.getCurrentMapLevel());
                // monsters = Integer.toString(dungeon.getCurrentLevel()
                // .getMonsters().getDungeonMonsters().size());
            }
            else
            {
                scoreText.append(" ");
            }
        }
        catch (Exception e)
        {
        } // ignore format errors

        this.txtScore.setText(scoreText.toString());

    }

    /**
     * Show Combat dialog frame.
     * 
     * @param monsterName
     *            Name of the monster to combat
     * @throws JurpeException
     */
    public void showCombat(String monsterName) throws JurpeException
    {
        boolean pcWins = true; // true if PC wins combat

        String announce;
        PC fighter = this.theSystem.getPC();

        // Non armed PC warning
        if (!fighter.wearsWeapon())
        {
            announce = GameStrings.ARMED + " Are you sure you want to combat?";

            if (JOptionPane.showConfirmDialog(this, announce, Config.APPNAME, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
            {
                this.announce("You cautiously walk away.");
                return;
            }
        }

        // If PC died
        if (!this.theSystem.isPCgenerated())
        {
            throw new JurpeException("PC is not generated");
        }
        else if (fighter.isUnconscious()) // PC is unconscious
        {
            announce = fighter.getName() + GameStrings.UCANTFIGT;
            JOptionPane.showMessageDialog(this, announce, Config.APPNAME, JOptionPane.WARNING_MESSAGE);
            this.announce(announce);
        }
        else
        {
            if (monsterName != null) // if a monster is selected
            {
                announce(GameStrings.UENTERCOMBAT + monsterName);
                boolean fastlogs = this.jCheckBoxMenuItemFastLogs.isSelected();
                FrameCombat gfc = new FrameCombat(this, monsterName, fastlogs);
                JurpeMain.showDialog(gfc);
                // When you exit combat you can be
                // a) Victorious
                // b) Dead
                // If you are dead we display a Message
                if (!fighter.isAlive())
                {
                    pcWins = false;

                    JOptionPane.showMessageDialog(this, "You died in combat.", Config.APPNAME, JOptionPane.WARNING_MESSAGE);
                    announce("You died in combat.");
                    announce("Game over.");
                }
            }
            this.jlblStatusBar.setText(this.theSystem.getLog().getLatestEntry());
        }

        this.refreshScore();
        this.refreshDungeon(pcWins);

    }

    public void refreshDungeon(boolean pcWins)
    {
        if (this.theSystem.getDungeon().getAvatar() == null)
        {
            this.pnlGame.removeDungeon();
        }
        else
        {
            if (!pcWins)
            {
                this.pnlGame.removeDungeon();
                this.theSystem.cancelCharacter();
            }
        }
    }

    /**
     * Set preferred AllOutAttack Type for playing character
     * 
     * @param type
     *            AllOutAttackType returned by a GUI
     */
    public void setAllOutAttackType(Object type)
    {
        PCharacter plc = this.theSystem.getPC();

        AllOutAttackType aout = (AllOutAttackType) type;
        plc.setAllOutAttackType(aout);
        announce(GameStrings.SELECTAOA + aout.toString());
    }

    /**
     * Generate a new playing character.
     */
    public void generateCharacter()
    {
        int risp;

        if (this.theSystem.isPCgenerated())
        // Do you want to delete existing character?
        {
            risp = JOptionPane.showConfirmDialog(this, GameStrings.DELCHR, Config.APPNAME, JOptionPane.YES_NO_OPTION);
            if (risp == JOptionPane.NO_OPTION)
            {
                return;
            }

            this.pnlGame.removeDungeon();
            this.theSystem.cancelCharacter();
            this.pnlCharacter.setCharacterName("");
        }

//		CharacterCreation cr = new CharacterCreation(this);
////		CharacterWizard0 wiz = new CharacterWizard0(cr);
////		wiz.setVisible(true);

        WizardController wc = new WizardController();
        JWizard wizard = new JWizard(this, wc);
        wizard.setVisible(true);

        if (wc.isCharacterCreated())
        {
            this.theSystem.generatePC(wc.getCharacterAttrs());
            this.refreshCharacter();
            this.showPanel(this.pnlCharacter);
            this.restorePanels();
        }
    }

    /**
     * Save current character with default name
     * 
     * @see Config
     */
    public void saveCharacter()
    {

        if (!this.theSystem.isPCgenerated())
        {
            JOptionPane.showMessageDialog(this, GameStrings.MUSTCREA, Config.APPNAME, JOptionPane.WARNING_MESSAGE);
            return;
        }

        String fileName = Config.SYSTEMFILE;
        this.saveCharacter(Config.addDatPathTo(fileName));
    }

    /**
     * Save current character
     * 
     * @param filename
     *            complete character path and name without extension.
     */
    public void saveCharacter(String filename)
    {
        String result = GameStrings.SAVED;
        int icon = JOptionPane.INFORMATION_MESSAGE;

        if (!this.theSystem.isPCgenerated())
        {
            JOptionPane.showMessageDialog(this, GameStrings.MUSTCREA, Config.APPNAME, JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Saves data
        if (!this.theSystem.savePC(filename))
        {
            result = GameStrings.CANTSAVE;
            icon = JOptionPane.WARNING_MESSAGE;
        }

        JOptionPane.showMessageDialog(this, result, Config.APPNAME, icon);
        announce(result);
    }

    /**
     * Save log displaying save as dialog
     * 
     * @param set
     *            if false, logging to file will be disabled
     * @throws IOException
     */
    public void setLogFile(boolean set) throws IOException
    {

        if (!set)
        {
            this.theSystem.getLog().unsetLogToFile();
            return;
        }

        ChrFileFilter txt = this.getChrFileFilter("txt", "Text File");
        ChrFileFilter log = this.getChrFileFilter("log", "Log File");
        JFileChooser jfcx = new JFileChooser();
        jfcx.addChoosableFileFilter(txt);
        jfcx.addChoosableFileFilter(log);
        jfcx.setFileFilter(txt);
        jfcx.setDialogTitle("Select a path to save the log into");
        int retval = jfcx.showSaveDialog(this);
        if (retval == JFileChooser.APPROVE_OPTION)
        {
            File file = jfcx.getSelectedFile();
            this.theSystem.getLog().setLogToFile(file);
        }

    }

    /**
     * Save current character displaying save as dialog
     */
    public void saveAsCharacter()
    {

        if (this.theSystem.isPCgenerated())
        {
            this.jFileChooser.setApproveButtonText(GameStrings.SAVECHR);
            this.jFileChooser.setDialogTitle(GameStrings.SAVECHRF);
            this.jFileChooser.setMultiSelectionEnabled(false);
            int retval = this.jFileChooser.showSaveDialog(this);
            if (retval == JFileChooser.APPROVE_OPTION)
            {
                String filename = this.jFileChooser.getSelectedFile().getAbsolutePath();
                this.theSystem.getLog().addEntry("Saving character info to " + filename);
                this.saveCharacter(filename);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, GameStrings.MUSTCREA, Config.APPNAME, JOptionPane.WARNING_MESSAGE);
        }

    }

    /**
     * Restore current character
     * 
     * @param pathName
     *            complete character path and name, without extension.
     * @throws JurpeException
     */
    public void loadCharacter(String pathName) throws JurpeException
    {
        String result = GameStrings.SYSLOAD;
        int xicon = JOptionPane.INFORMATION_MESSAGE;

        if (!this.theSystem.loadPC(pathName))
        {
            result = GameStrings.SYSERROR;
            xicon = JOptionPane.WARNING_MESSAGE;
        }
        else
        {
            this.refreshCharacter();
            this.refreshInventory();
            this.refreshSkills();
            this.refreshInn();

            this.pnlGame.doLayout();
            this.restorePanels();
        }

        JOptionPane.showMessageDialog(this, result, Config.APPNAME, xicon);
        //announce(result);

        this.refreshLog();
    }

    /**
     * Quick Restore current character. It will be loaded the character with
     * default name.
     * 
     * @see Config
     * @throws JurpeException
     */
    public void loadCharacter() throws JurpeException
    {
        this.loadCharacter(Config.addDatPathTo(Config.SYSTEMFILE + Config.CHAREXTENSION));
    }

    /**
     * Show GUI dialog for loading a character. Occurs when user presses "Load
     * character..."
     * 
     * @throws JurpeException
     */
    public void loadGUICharacter() throws JurpeException
    {
        this.jFileChooser.setApproveButtonText(GameStrings.LOADCHR);
        this.jFileChooser.setDialogTitle(GameStrings.LOADCHRF);
        int retval = this.jFileChooser.showOpenDialog(this);
        if (retval == JFileChooser.APPROVE_OPTION)
        {
            String filename = this.jFileChooser.getSelectedFile().getAbsolutePath();
            this.loadCharacter(filename);
        }
    }

    /**
     * Show tutorial html dialog
     */
    @Override
    public void showTutorial()
    {
        final String tutorialfile = JurpeDemoConfig.HTMLTUTORIAL;
        
        File tutorialHtml = new File(Config.addDatPathTo(tutorialfile));
        if (!tutorialHtml.exists())
        {
            this.extractTutorialResources();                            
        }
        String urlFile = tutorialHtml.getAbsolutePath();
        urlFile = "file:///" + urlFile;
        OSProps.showURL(urlFile);
    }
    
    private void extractTutorialResources()
    {
        final String tutorialfile = JurpeDemoConfig.HTMLTUTORIAL;
        
        String[] resources = {
            "CreateACharacter.PNG", "WalkingAroung.PNG", "Shopping.PNG", "Equip.PNG", 
            "Skills.PNG",  "Dungeon.PNG", "Combat.PNG", "Ranged.PNG"
        };
        
        ResourceFinder rf = ResourceFinder.resources();
        rf.extractResource(tutorialfile, Config.addDatPathTo(tutorialfile));
        
        for (String res : resources)
        {
            this.theSystem.getLog().addDetail("Extracting "+res+"...");
            rf.extractResource(res, Config.addDatPathTo("images/"+res));
        }
    }

    /**
     * Show high score table
     */
    @Override
    public void showHighScores()
    {
        JDialogScores hst = new JDialogScores(this);
        // this.showDialog(hst);
        hst.setVisible(true);
    }

    public void showPanel(TabbedPanel panel)
    {
        int selectedPanel = panel.getIndex();
        JPanel selectedTab = (JPanel) this.jTabbedPane.getComponentAt(selectedPanel);
        this.showPanel(selectedTab);
    }

    /**
     * Show panel
     * 
     * @param panel
     *            JScrollPane to display
     */
    public void showPanel(JPanel panel)
    {
        this.jTabbedPane.setSelectedComponent(panel);
        panel.setVisible(true);
    }

    /**
     * Restore panels view
     */
    @Override
    public void restorePanels()
    {

        boolean isCharacterPanel = this.theSystem.isPCgenerated();

        this.enablePanel(TabbedPanel.TAB_CHARACTER, isCharacterPanel);
        this.enablePanel(TabbedPanel.TAB_INVENTORY, isCharacterPanel);
        this.enablePanel(TabbedPanel.TAB_SKILLS, isCharacterPanel);
        this.enablePanel(TabbedPanel.TAB_GAME, true);
        this.enablePanel(TabbedPanel.TAB_SHOP, false);
        this.enablePanel(TabbedPanel.TAB_INN, false);
        this.enablePanel(TabbedPanel.TAB_MAGES, false);
        this.setGrimoirePanel();

        // Initially the trainer is always off
        this.pnlSkills.enableImproving(false);
    }

    private boolean isGrimoireTabShown()
    {
        return (this.jTabbedPane.getTabCount() > TabbedPanel.TAB_GRIMOIRE.getIndex());
    }

    private void setGrimoirePanel()
    {
        boolean showGrimoire = false;
        boolean isGrimoireAlreadyShown = this.isGrimoireTabShown();

        if (this.theSystem.isPCgenerated())
        {
            PCharacter player = this.theSystem.getPC();
            if (player.hasGrimoire())
            {
                showGrimoire = true;
            }
        }

        if (showGrimoire)
        {
            if (!isGrimoireAlreadyShown)
            {
                this.jTabbedPane.addTab(GameStrings.GRIMOIRE, null, this.pnlGrimoire, null);
            }
        }
        else
        {
            if (isGrimoireAlreadyShown)
            {
                this.jTabbedPane.removeTabAt(TabbedPanel.TAB_GRIMOIRE.getIndex());
            }

        }

    }

    /**
     * Show "About" dialog frame
     * 
     * @see JurpeFrame_AboutBox
     */
    public void showAboutBox()
    {
        final JDialogAbout ad = new JDialogAbout(this);
        ad.setVisible(true);
    }

    /**
     * Show information about a selected skill
     */
    public void showSkillInfo(Skill sk)
    {
        JDialogBase db = new JDialogBase(this, sk.description(), "Skill Description");
        db.setVisible(true);
    }

    /**
     * Use First Aid Skill
     */
    public void useFirstAid()
    {
        PCharacter curPC = this.theSystem.getPC();

        if (curPC.isUnconscious())
        {
            JOptionPane.showMessageDialog(this, GameStrings.CANTFIRAID, Config.APPNAME, JOptionPane.WARNING_MESSAGE);
            this.announce(GameStrings.CANTFIRAID);
            return;
        }

        PrimaryStats ps = curPC.getPrimaryStats();

        if (ps.getCurrentHitPoints() == ps.getInitialHitPoints())
        {
            JOptionPane.showMessageDialog(this, GameStrings.FULLHEALTH, Config.APPNAME, JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            int firstAidLevel = curPC.getSkillLevel("First Aid");
            int restored = this.theSystem.firstAid(curPC, firstAidLevel, 3);
            StringBuilder restString = new StringBuilder("You get ");
            restString.append(restored);
            restString.append(" HP with First Aid");
            JOptionPane.showMessageDialog(this, restString.toString(), Config.APPNAME, JOptionPane.INFORMATION_MESSAGE);
            refreshInn();
        }
    }

    /**
     * Rest. You stay at the Inn (and you pay for that). You rest for one night
     * (8 hours), in which you get COMPLETE RESTORE (full HP and fatigue=0) If
     * you are unconscious, you rest for abs(-HT) hours, in which you try to
     * recover from unconsciousness
     */
    public void useRest()
    {
        PCharacter curPC = this.theSystem.getPC();
        Inn theInn = this.theSystem.getInn();

        PrimaryStats ps = curPC.getPrimaryStats();

        // Character always restores fatigue
        curPC.getPrimaryStats().setFatigue(0);
        this.announce("Your fatigue is vanished.");

        if (curPC.isUnconscious())
        {
            int hoursNeeded = Math.abs(ps.getCurrentHitPoints());

            int result = JOptionPane.showConfirmDialog(this, GameStrings.TRYRECOVER + String.valueOf(hoursNeeded) + GameStrings.HOURS + " ?", Config.APPNAME,
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION)
            {
                int dueFee = theInn.getCostPerNight();

                // If you are unconscious and don't have enough money to rest at
                // the Inn, you miserably die...
                if (curPC.getInventory().getAvailableMoney() < dueFee)
                {
                    JOptionPane.showMessageDialog(this, GameStrings.NOMONEY, Config.APPNAME, JOptionPane.WARNING_MESSAGE);
                    JOptionPane.showMessageDialog(this, GameStrings.ALONEDIE, Config.APPNAME, JOptionPane.WARNING_MESSAGE);
                    this.announce(LibraryStrings.YOUDIED);
                    curPC.setAlive(false);
                    this.theSystem.playerDies();
                }
                else
                {
                    curPC.getInventory().spendMoney(dueFee);
                    this.announce(GameStrings.TRYUNCONS);

                    if (JurpeUtils.recoverFromUnconsciousness(curPC))
                    {
                        this.announce(GameStrings.RECOVEROLL + JurpeUtils.getLatestRoll() + GameStrings.OUTOF + ps.getInitialHitPoints() + "(" + LibraryStrings.COS + ")");
                        JOptionPane.showMessageDialog(this, GameStrings.URECOVERUN, Config.APPNAME, JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                    {
                        this.announce(GameStrings.FAILTORECV + JurpeUtils.getLatestRoll() + GameStrings.OUTOF + ps.getInitialHitPoints() + "(" + LibraryStrings.COS + ")");
                    }
                }

            }
        }
        else
        {

            int result = JOptionPane.showConfirmDialog(this, GameStrings.DQOQATTQ, Config.APPNAME, JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION)
            {
                int dueFee = theInn.getCostPerNight();

                if (curPC.getInventory().getAvailableMoney() < dueFee)
                {
                    JOptionPane.showMessageDialog(this, GameStrings.NOMONEY, Config.APPNAME, JOptionPane.WARNING_MESSAGE);
                    return;
                }

                curPC.getInventory().spendMoney(dueFee);
                this.announce(GameStrings.YUPAEULW);

                JurpeUtils.fullRecovery(curPC);
                JOptionPane.showMessageDialog(this, "You feel much better now!", Config.APPNAME, JOptionPane.INFORMATION_MESSAGE);
                this.announce("You feel much better now!");
            }
        }

        this.refreshInn();
    }

    /**
     * Natural recovery on PC
     */
    public void useNaturalRecovery()
    {
        PCharacter curPC = this.theSystem.getPC();
        Inn theInn = this.theSystem.getInn();

        int result = JOptionPane.showConfirmDialog(this, GameStrings.RSTONEHR, Config.APPNAME, JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION)
        {
            int dueFee = theInn.getCostPerHour();

            if (curPC.getInventory().getAvailableMoney() < dueFee)
            {
                JOptionPane.showMessageDialog(this, GameStrings.NOMONEY, Config.APPNAME, JOptionPane.WARNING_MESSAGE);
                return;
            }

            curPC.getInventory().spendMoney(dueFee);
            this.announce(GameStrings.YUPAEULW);

            // Recover 1 point of fatigue
            curPC.getPrimaryStats().addFatigue(-1);
            this.announce(GameStrings.RECOVFTG);

            // You may or not recover some HP
            if (JurpeUtils.naturalRecovery(curPC))
            {
                this.announce(GameStrings.YURCVRNT);
            }
            else
            {
                this.announce(GameStrings.YUFIEWOR);
            }
        }

        this.refreshInn();
    }

    /**
     * Unwear selected item.
     * 
     * @param ogg
     *            Which AbstractItem to unwear
     * @see AbstractItem
     */
    public void unwearItem(AbstractItem ogg)
    {

        PCharacter curPC;

        if (ogg != null)
        {
            curPC = this.theSystem.getPC();
            int result = JOptionPane.showConfirmDialog(this, GameStrings.DUNWEAR + ogg + " ?", Config.APPNAME, JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION)
            {
                ogg.unwear(curPC);
            }
            refreshInventory();
        }

    }

    /**
     * Sell selected item
     */
    public void sellItem()
    {
        // You can sell an item only at the local shop
        Cell whereIsAvatar = this.theSystem.getDungeon().getAvatar().getPlaceholder().getCell();
        if (whereIsAvatar.getLocation().getType() == LocationType.SHOP)
        {
            AbstractItem selBasicItem = (AbstractItem) this.pnlInventory.getJLstOggetti().getSelectedValue();
            if (selBasicItem != null)
            {
                int sellprice = this.theSystem.getPC().getCustomizedPrice(selBasicItem);

                if (JOptionPane.showConfirmDialog(this, GameStrings.THEYOFFR + "$" + String.valueOf(sellprice) + GameStrings.FOROZZ + selBasicItem.toString() + GameStrings.DZOZCETE, Config.APPNAME, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                {
                    Inventory inv = this.theSystem.getPC().getInventory();
                    inv.sellBasicItem(selBasicItem, sellprice);
                    this.refreshInventory();
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "You can sell an item only at the local shop", Config.APPNAME, JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Drop selected item
     */
    public void dropItem()
    {
        AbstractItem selBasicItem = (AbstractItem) this.pnlInventory.getJLstOggetti().getSelectedValue();
        if (selBasicItem != null)
        {
            if (JOptionPane.showConfirmDialog(this, GameStrings.DOUWANDRP + selBasicItem.toString() + "? ", Config.APPNAME, JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
            {
                Inventory inv = this.theSystem.getPC().getInventory();
                inv.removeBasicItem(selBasicItem);
                this.refreshInventory();
            }
        }
    }

    /**
     * Use selected item.
     * 
     * @param ogg
     *            item to use
     */
    public void useItem(AbstractItem ogg)
    {
        PCharacter curPC;

        if (ogg != null)
        {
            curPC = this.theSystem.getPC();
            boolean used = true;
            int result = JOptionPane.showConfirmDialog(this, GameStrings.DUSE + ogg + " ?", Config.APPNAME, JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION)
            {
                used = ogg.use(curPC);
                this.theSystem.getLog().addEntry(ogg + " " + GameStrings.USED);
            }
            if (!used)
            {
                JOptionPane.showMessageDialog(this, ogg.warningMessage(), Config.APPNAME, JOptionPane.WARNING_MESSAGE);
            }
            refreshInventory();
        }
    }

    /**
     * Execute an attack made by a monster (usually when monster walks over the
     * position in which Avatar is)
     * 
     * @param monster
     *            Dungeon monster name
     */
    @Override
    public void executeAttackByAMonster(String monster)
    {
        try
        {
            Level currentLevel = this.theSystem.getDungeon().getCurrentLevel();
            JOptionPane.showMessageDialog(this, "You are attacked by a " + monster + ".", Config.APPNAME, JOptionPane.WARNING_MESSAGE);
            this.showCombat(monster);
        
            if (currentLevel.getMonsters().getDungeonMonsters().size() == 0)
            {
                int dungeonLevel = this.theSystem.getDungeon().getCurrentMapLevel();
                this.theSystem.getDungeon().getLog().addEntry("Compliments! You cleared dungeon level #" + dungeonLevel);
            }
        }
        catch (JurpeException jex)
        {
            this.theSystem.getLog().addEntry(jex.getMessage());
        }
    }

    /**
     * Dungeon Command Execution
     * 
     * @param c
     *            DungeonCommand Command to execute
     */
    @Override
    public void executeDungeonCommand(DungeonCommand c)
    {
        try
        {
            DungeonCommander cmd = new DungeonCommander(this, c);
            cmd.execute();
        }
        catch (JurpeException jex)
        {
            this.theSystem.getLog().addEntry(jex.getMessage());
        }
    }

    /**
     * True if tab is visible (not actually showing, just visible)
     * 
     * @param tabbedPanel
     *            int
     * @return boolean
     * @throws JurpeException
     */
    public static boolean isTabVisible(TabbedPanel tabbedPanel) throws JurpeException
    {
        boolean isVisible = false;
        int index = tabbedPanel.getIndex();
        if (index > 0)
        {
            isVisible = true;
        }
        return isVisible;
    }

    /**
     * Enable/Disable one panel in the tabbed pane
     * 
     * @param as
     *            in TabbedPanel enumeration Panel to add/remove
     * @param show
     *            if false, remove this tab
     * @see TabbedPanel
     * @throws JurpeException
     */
    public void enablePanel(TabbedPanel panel, boolean show)
    {
        int index = panel.getIndex();
        if ((index >= 0) && (index < this.jTabbedPane.getTabCount()))
        {
            this.jTabbedPane.setEnabledAt(index, show);
        }
    }

    /**
     * Show help commands
     */
    @Override
    public void showHelpCommands()
    {
        JDialogCommands help = new JDialogCommands(this);
        help.setLocationRelativeTo(this);
        help.setModal(false);
        help.setVisible(true);
    }

    /**
     * Exit program (ask if you want to save your character)
     */
    @Override
    public void exitProgram()
    {
        if (this.theSystem.isPCgenerated())
        {
            int result = JOptionPane.showConfirmDialog(this, LibraryStrings.EXITNOW, Config.APPNAME, JOptionPane.YES_NO_CANCEL_OPTION);
            if (result != JOptionPane.YES_OPTION)
            {
                return;
            }
        }

        exitNow();
    }

    private void exitNow()
    {
        this.options.save();
        this.cleanTempFiles();
        this.theSystem.getLog().addEntry("Bye.");
        this.dispose();
        System.exit(0);
    }

    /**
     * Save user options
     */
    public void saveOptions() throws JurpeException
    {
        String laf = String.valueOf(this.options.getLookAndFeel());
        String fastlog = String.valueOf(this.currentFastLog);
        Properties props = JurpeDemoConfig.getProperties();
        props.setProperty("Jurpe.LookAndFeel", laf);
        props.setProperty("Jurpe.FastLog", fastlog);
        this.theSystem.getLog().addEntry("Saving properties...");
        JurpeDemoConfig.saveProperties(props);
        this.theSystem.getLog().addEntry("Done.");
    }

    /**
     * Display dialog in the center of the screen
     * 
     * @param frame
     *            parent frame
     */
    public static void showDialog(JDialog frame)
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height)
        {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width)
        {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
    }

    /** P R I V A T E M E M B E R S * */
    private void cleanTempFiles()
    {
        Dungeons curDungeon = this.theSystem.getDungeon();
        if (curDungeon != null)
        {
            this.theSystem.getLog().addEntry("Clearing temp files...");
            curDungeon.getDungeonLevels().destroyLevelFiles();
        }
    }

    private final class ChrFileFilter extends javax.swing.filechooser.FileFilter
    {

        private HashMap<String, ChrFileFilter> filters = null;
        private String description = null;
        private String fullDescription = null;
        private boolean useExtensionsInDescription = true;

        public ChrFileFilter()
        {
            this.filters = new HashMap<String, ChrFileFilter>();
        }

        public ChrFileFilter(String filter, String description)
        {
            this();
            addExtension(filter);
            if (description != null)
            {
                setDescription(description);
            }
        }

        @Override
        public boolean accept(File f)
        {
            if (f != null)
            {
                if (f.isDirectory())
                {
                    return true;
                }
                String extension = getExtension(f);
                if (extension != null && this.filters.get(getExtension(f)) != null)
                {
                    return true;
                }
            }
            return false;
        }

        public String getExtension(File f)
        {
            if (f != null)
            {
                String filename = f.getName();
                int i = filename.lastIndexOf('.');
                if (i > 0 && i < filename.length() - 1)
                {
                    return filename.substring(i + 1).toLowerCase();
                }
            }
            return null;
        }

        public void addExtension(String extension)
        {
            if (this.filters == null)
            {
                this.filters = new HashMap<String, ChrFileFilter>(5);
            }
            this.filters.put(extension.toLowerCase(), this);
            this.fullDescription = null;
        }

        @Override
        public String getDescription()
        {
            if (this.fullDescription == null)
            {
                if (this.description == null || isExtensionListInDescription())
                {
                    this.fullDescription = this.description == null ? "(" : this.description + " (";
                    // build the description from the extension list
                    Set<String> extensions = this.filters.keySet();
                                 
                    if (extensions != null)
                    {
                        for (String ext : extensions)
                        {
                            this.fullDescription += "." + ext;
                            this.fullDescription += ",";
                        }
                    }
                    
                    this.fullDescription = this.fullDescription.substring(0, this.fullDescription.length()-1) + ")";
                }
                else
                {
                    this.fullDescription = this.description;
                }
            }
            return this.fullDescription;
        }

        public void setDescription(String description)
        {
            this.description = description;
            this.fullDescription = null;
        }

        public boolean isExtensionListInDescription()
        {
            return this.useExtensionsInDescription;
        }
    }

    private ChrFileFilter getChrFileFilter(String extension, String description)
    {
        return new ChrFileFilter(extension, description);
    }

    private void initFileChooser()
    {
        // Character file
        ChrFileFilter chr = this.getChrFileFilter("chr", "Character File");
        ChrFileFilter bin = this.getChrFileFilter("bin", "Binary File");
        this.jFileChooser.addChoosableFileFilter(chr);
        this.jFileChooser.addChoosableFileFilter(bin);
        this.jFileChooser.setFileFilter(chr);

        // Set working directory
        try
        {
            this.jFileChooser.setCurrentDirectory(new File(Config.DATPATH));
        }
        catch (Exception exc)
        {
        }
    }
    private static final long serialVersionUID = 3317L;
}
