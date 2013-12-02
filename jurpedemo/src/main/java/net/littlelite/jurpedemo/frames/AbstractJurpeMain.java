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

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;
import com.jgoodies.looks.plastic.theme.Silver;
import com.jgoodies.looks.plastic.theme.SkyKrupp;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import net.littlelite.jurpe.system.Core;
import net.littlelite.jurpe.system.EnabledDisabled;
import net.littlelite.jurpe.system.IJurpeGui;
import net.littlelite.jurpe.system.OSProps;
import net.littlelite.jurpe.system.commands.DungeonCommand;
import net.littlelite.jurpe.system.resources.ResourceFinder;
import net.littlelite.jurpedemo.JurpeDemoConfig;
import net.littlelite.jurpedemo.Options;

/**
 * Main Frame for Jurpedemo Swing Application
 * 
 */
public abstract class AbstractJurpeMain extends javax.swing.JFrame implements IJurpeGui
{
	private static final long serialVersionUID = 4020L;
	private ButtonGroup radioButtonsGroup = null;

	// Business Logic Members
	protected EnabledDisabled currentFastLog;
	private ResourceFinder rf; // resources
	protected Options options;

	/** Creates new form AbstractJurpeMain */
	public AbstractJurpeMain()
	{
		initComponents();
		this.initialize();
	}

	/**
	 * Write an announce to log and status bar
	 * 
	 * @param testo
	 *            text to output
	 */
	protected void announce(String testo)
	{
		// add text to log
		this.getSystem().getLog().addEntry(testo);

		// updates status bar
		this.jlblStatusBar.setText(testo);
	}

	/**
	 * Write an announce to the log and, optionally, to the status bar
	 * 
	 * @param testo
	 *            text to output
	 * @param statusbar
	 *            if true, outputs also to the statusbar
	 */
	protected void announce(String testo, boolean statusbar)
	{
		if (!statusbar)
		{
			this.getSystem().getLog().addEntry(testo);
		}
		else
		{
			this.announce(testo);
		}
	}
        
        @Override
        public void executeDungeonCommand(DungeonCommand c)
        {
            return;
        }
        
        @Override
        public void executeAttackByAMonster(String monster)
        {
            return;
        }

	/**
	 * If FastLog is enabled, disable it, else enable it.
	 */
	protected void switchFastLog()
	{
		if (this.currentFastLog != null)
		{
			if (this.currentFastLog.isEnabled())
			{
				this.currentFastLog = EnabledDisabled.DISABLED;
			}
			else
			{
				this.currentFastLog = EnabledDisabled.ENABLED;
			}

			this.options.setFastlogs(this.currentFastLog);
		}
	}

	/**
	 * Set Look And Feel
	 * 
	 * @param n
	 */
	protected void setLookAndFeel(JurpeLookAndFeel n)
	{
		String laf = "";

		switch (n)
		{
			case XPLATFORM:
				laf = UIManager.getCrossPlatformLookAndFeelClassName();
				break;
			case SYSTEM:
				laf = UIManager.getSystemLookAndFeelClassName();
				break;
			case WINDOWS:
				laf = "com.jgoodies.looks.windows.WindowsLookAndFeel";
				break;
			case METAL:
				laf = "javax.swing.plaf.metal.MetalLookAndFeel";
				MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
				break;
			case OCEAN:
				laf = "javax.swing.plaf.metal.MetalLookAndFeel";
				try
				{
					MetalLookAndFeel.class.getMethod("getCurrentTheme", (Class[]) null);
					MetalLookAndFeel.setCurrentTheme((MetalTheme) Class.forName("javax.swing.plaf.metal.OceanTheme").newInstance());
				}
				catch (Exception e)
				{
					MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
				}
				break;
			case PLASTIC:
				PlasticLookAndFeel.setTabStyle(PlasticLookAndFeel.TAB_STYLE_METAL_VALUE);
				PlasticLookAndFeel.setPlasticTheme(new SkyKrupp());
				laf = "com.jgoodies.looks.plastic.PlasticLookAndFeel";
				break;
			case PLASTICXP:
				PlasticLookAndFeel.setTabStyle(PlasticLookAndFeel.TAB_STYLE_METAL_VALUE);
				PlasticLookAndFeel.setPlasticTheme(new ExperienceBlue());
				laf = "com.jgoodies.looks.plastic.PlasticXPLookAndFeel";
				break;
			case LOOKS:
				PlasticLookAndFeel.setTabStyle(PlasticLookAndFeel.TAB_STYLE_DEFAULT_VALUE);
				PlasticLookAndFeel.setPlasticTheme(new Silver());
				laf = "com.jgoodies.looks.plastic.Plastic3DLookAndFeel";
				break;
			default:
				laf = UIManager.getSystemLookAndFeelClassName();
				break;
		}

		try
		{
			UIManager.setLookAndFeel(laf);
			SwingUtilities.updateComponentTreeUI(this);
			this.validate();
		}
		catch (ClassNotFoundException cnfe)
		{
			this.announce("Unknown Look And Feel: " + laf, true);
		}
		catch (UnsupportedLookAndFeelException ulafe)
		{
			this.announce("Unsupported Look And Feel: " + laf, true);
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.rf = ResourceFinder.resources();
		ImageIcon jurpeIcon = this.rf.getResourceAsImage("jurpedemo_icon.gif");
		this.setIconImage(jurpeIcon.getImage());
		this.setResizable(false);

		this.setMaximumSize(new java.awt.Dimension(JurpeDemoConfig.FRAMESIZE));
		this.setMinimumSize(new java.awt.Dimension(JurpeDemoConfig.FRAMESIZE));
		this.setSize(JurpeDemoConfig.FRAMESIZE);

		this.jTabbedPane.setSize(JurpeDemoConfig.PANELSIZE);

		this.setTitle("Jurpe");
		this.setRadioGroup();
		this.setMenuIcons();
		this.setMenuHelpers();
	}

	private void setRadioGroup()
	{
		this.radioButtonsGroup = new ButtonGroup();
		this.radioButtonsGroup.add(this.jRadioButtonLAFXPlatform);
		this.radioButtonsGroup.add(this.jRadioButtonLAFMetal);
		this.radioButtonsGroup.add(this.jRadioButtonLAFWindows);
		this.radioButtonsGroup.add(this.jRadioButtonLAFOcean);
		this.radioButtonsGroup.add(this.jRadioButtonLAFSystem);
		this.radioButtonsGroup.add(this.jRadioButtonLAFLooks);
		this.radioButtonsGroup.add(this.jRadioButtonLAFPlastic);
		this.radioButtonsGroup.add(this.jRadioButtonLAFPlasticXP);
	}

	/**
	 * Set the menu radio buttons according to selected Look And Feel
	 * 
	 * @param laf
	 *            The selected Look And Feel
	 */
	protected void LAFSynchronize(JurpeLookAndFeel laf)
	{
		switch (laf)
		{
			case LOOKS:
				this.jRadioButtonLAFLooks.setSelected(true);
				break;
			case PLASTIC:
				this.jRadioButtonLAFPlastic.setSelected(true);
				break;
			case PLASTICXP:
				this.jRadioButtonLAFPlasticXP.setSelected(true);
				break;
			case XPLATFORM:
				this.jRadioButtonLAFXPlatform.setSelected(true);
				break;
			case METAL:
				this.jRadioButtonLAFMetal.setSelected(true);
				break;
			case WINDOWS:
				this.jRadioButtonLAFWindows.setSelected(true);
				break;
			case OCEAN:
				this.jRadioButtonLAFOcean.setSelected(true);
				break;
			case SYSTEM:
				this.jRadioButtonLAFSystem.setSelected(true);
				break;
			case NIMBUS:
				break;
			default:
				break;
		}
	}

	private void LAFChange()
	{
		JurpeLookAndFeel selectedLaf = null;

		if (this.jRadioButtonLAFXPlatform.isSelected())
		{
			selectedLaf = JurpeLookAndFeel.XPLATFORM;
		}
		else if (this.jRadioButtonLAFMetal.isSelected())
		{
			selectedLaf = JurpeLookAndFeel.METAL;
		}
		else if (this.jRadioButtonLAFWindows.isSelected())
		{
			selectedLaf = JurpeLookAndFeel.WINDOWS;
		}
		else if (this.jRadioButtonLAFOcean.isSelected())
		{
			selectedLaf = JurpeLookAndFeel.OCEAN;
		}
		else if (this.jRadioButtonLAFSystem.isSelected())
		{
			selectedLaf = JurpeLookAndFeel.SYSTEM;
		}
		else if (this.jRadioButtonLAFLooks.isSelected())
		{
			selectedLaf = JurpeLookAndFeel.LOOKS;
		}
		else if (this.jRadioButtonLAFPlastic.isSelected())
		{
			selectedLaf = JurpeLookAndFeel.PLASTIC;
		}
		else if (this.jRadioButtonLAFPlasticXP.isSelected())
		{
			selectedLaf = JurpeLookAndFeel.PLASTICXP;
		}

		if (selectedLaf != null)
		{
			this.setLookAndFeel(selectedLaf);
			this.options.setLookAndFeel(selectedLaf);
		}
	}

	private void setMenuIcons()
	{
		this.jMenuFileNew.setIcon(this.rf.getResourceAsImage("icos/documents_16.gif"));
		this.jMenuFileQuickSave.setIcon(this.rf.getResourceAsImage("icos/save_16.gif"));
		this.jMenuFileExit.setIcon(this.rf.getResourceAsImage("icos/delete_16.gif"));
		this.jMenuOptionsLAF.setIcon(this.rf.getResourceAsImage("icos/computer_16.gif"));
		this.jMenuToolsHighScores.setIcon(this.rf.getResourceAsImage("icos/arrow-up_16.gif"));
		this.jMenuHelpJurpeHome.setIcon(this.rf.getResourceAsImage("icos/home_16.gif"));
		this.jMenuHelpAbout.setIcon(this.rf.getResourceAsImage("icos/info.gif"));
	}

	private void setMenuHelpers()
	{
		this.jMenuFile.addMenuListener(new FrameJurpeMainMenuAdapter());
		this.jMenuFileNew.addMouseListener(new FrameJurpeMainMouseAdapter("Begin a new game with a new character"));
		this.jMenuFileOpen.addMouseListener(new FrameJurpeMainMouseAdapter("Restore a saved game"));
		this.jMenuFileExit.addMouseListener(new FrameJurpeMainMouseAdapter("Exit Jurpedemo"));
		this.jMenuFileLog.addMouseListener(new FrameJurpeMainMouseAdapter("Save the game log to a file"));
		this.jMenuFileQuickLoad.addMouseListener(new FrameJurpeMainMouseAdapter("Quick load the game"));
		this.jMenuFileQuickSave.addMouseListener(new FrameJurpeMainMouseAdapter("Quick save the game"));
		this.jMenuFileSaveAs.addMouseListener(new FrameJurpeMainMouseAdapter("Save the current game"));
		this.jMenuOptions.addMenuListener(new FrameJurpeMainMenuAdapter());
		this.jMenuOptionsLAF.addMouseListener(new FrameJurpeMainMouseAdapter("Set Jurpedemo Look And Feel"));
		this.jMenuHelp.addMenuListener(new FrameJurpeMainMenuAdapter());
		this.jMenuHelpAbout.addMouseListener(new FrameJurpeMainMouseAdapter("About Jurpedemo"));
		this.jMenuHelpCommands.addMouseListener(new FrameJurpeMainMouseAdapter("Show Jurpedemo commands"));
		this.jMenuHelpJurpeDevel.addMouseListener(new FrameJurpeMainMouseAdapter("Visit to Jurpe Sourceforge site"));
		this.jMenuHelpJurpeHome.addMouseListener(new FrameJurpeMainMouseAdapter("Visit Jurpe Home Page"));
		this.jMenuHelpTutorial.addMouseListener(new FrameJurpeMainMouseAdapter("Learn how to play Jurpedemo"));
		this.jMenuTools.addMenuListener(new FrameJurpeMainMenuAdapter());
		this.jMenuToolsHighScores.addMouseListener(new FrameJurpeMainMouseAdapter("Show high scores table"));

	}

	// Nested menu adapter class
	class FrameJurpeMainMenuAdapter implements MenuListener
	{
		@Override
		public void menuSelected(MenuEvent e)
		{
		}

		@Override
		public void menuDeselected(MenuEvent e)
		{
			AbstractJurpeMain.this.jlblStatusBar.setText("Ready");
		}

		@Override
		public void menuCanceled(MenuEvent e)
		{
			AbstractJurpeMain.this.jlblStatusBar.setText("Ready");
		}
	}

	// Nested menu mouse adapter class
	class FrameJurpeMainMouseAdapter extends MouseAdapter
	{
		private String statusBarMessage;

		FrameJurpeMainMouseAdapter(String message)
		{
			this.statusBarMessage = message;
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
			AbstractJurpeMain.this.jlblStatusBar.setText(this.statusBarMessage);
		}
	}
        
        // Event-handlers
	public abstract Core getSystem();

	public abstract void showHighScores();

	public abstract void showTutorial();

	public abstract void restorePanels();

	public abstract void showHelpCommands();

	protected abstract void menuFileNew();

	protected abstract void menuFileOpen();

	protected abstract void menuFileSaveAs();

	protected abstract void menuFileQuickLoad();

	protected abstract void menuFileQuickSave();

	protected abstract void menuFileLogToFile();

	protected abstract void menuFileExit();

	protected abstract void exitProgram();

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	@SuppressWarnings("synthetic-access")
	private void initComponents() {

        this.jTabbedPane = new javax.swing.JTabbedPane();
        this.jlblStatusBar = new javax.swing.JLabel();
        this.txtScore = new javax.swing.JLabel();
        this.jMenuBar = new javax.swing.JMenuBar();
        this.jMenuFile = new javax.swing.JMenu();
        this.jMenuFileNew = new javax.swing.JMenuItem();
        this.jMenuFileOpen = new javax.swing.JMenuItem();
        this.jMenuFileSaveAs = new javax.swing.JMenuItem();
        this.jSeparator1 = new javax.swing.JSeparator();
        this.jMenuFileQuickSave = new javax.swing.JMenuItem();
        this.jMenuFileQuickLoad = new javax.swing.JMenuItem();
        this.jSeparator2 = new javax.swing.JSeparator();
        this.jMenuFileLog = new javax.swing.JMenuItem();
        this.jSeparator3 = new javax.swing.JSeparator();
        this.jMenuFileExit = new javax.swing.JMenuItem();
        this.jMenuOptions = new javax.swing.JMenu();
        this.jMenuOptionsLAF = new javax.swing.JMenu();
        this.jRadioButtonLAFOcean = new javax.swing.JRadioButtonMenuItem();
        this.jRadioButtonLAFMetal = new javax.swing.JRadioButtonMenuItem();
        this.jRadioButtonLAFWindows = new javax.swing.JRadioButtonMenuItem();
        this.jRadioButtonLAFXPlatform = new javax.swing.JRadioButtonMenuItem();
        this.jRadioButtonLAFSystem = new javax.swing.JRadioButtonMenuItem();
        this.jRadioButtonLAFLooks = new javax.swing.JRadioButtonMenuItem();
        this.jRadioButtonLAFPlastic = new javax.swing.JRadioButtonMenuItem();
        this.jRadioButtonLAFPlasticXP = new javax.swing.JRadioButtonMenuItem();
        this.jCheckBoxMenuItemFastLogs = new javax.swing.JCheckBoxMenuItem();
        this.jMenuTools = new javax.swing.JMenu();
        this.jMenuToolsHighScores = new javax.swing.JMenuItem();
        this.jMenuHelp = new javax.swing.JMenu();
        this.jMenuHelpJurpeHome = new javax.swing.JMenuItem();
        this.jMenuHelpTutorial = new javax.swing.JMenuItem();
        this.jMenuHelpCommands = new javax.swing.JMenuItem();
        this.jMenuHelpJurpeDevel = new javax.swing.JMenuItem();
        this.jSeparator4 = new javax.swing.JSeparator();
        this.jMenuHelpAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(572, 508));
        setName("jurpeFrame"); // NOI18N
        setResizable(false);

        this.jTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneStateChanged(evt);
            }
        });

        this.jlblStatusBar.setBackground(new java.awt.Color(102, 102, 102));
        this.jlblStatusBar.setFont(this.jlblStatusBar.getFont());
        this.jlblStatusBar.setForeground(new java.awt.Color(153, 153, 153));
        this.jlblStatusBar.setText("Ready");

        this.txtScore.setBackground(new java.awt.Color(102, 102, 102));
        this.txtScore.setFont(this.txtScore.getFont());
        this.txtScore.setForeground(new java.awt.Color(153, 153, 153));
        this.txtScore.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        this.txtScore.setText("The village - Score: 0000");
        this.txtScore.setToolTipText("");

        this.jMenuBar.setFont(this.jMenuBar.getFont().deriveFont(this.jMenuBar.getFont().getStyle() & ~java.awt.Font.BOLD));

        this.jMenuFile.setText("File");

        this.jMenuFileNew.setText("New");
        this.jMenuFileNew.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFileNewActionPerformed(evt);
            }
        });
        this.jMenuFile.add(this.jMenuFileNew);

        this.jMenuFileOpen.setText("Open");
        this.jMenuFileOpen.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFileOpenActionPerformed(evt);
            }
        });
        this.jMenuFile.add(this.jMenuFileOpen);

        this.jMenuFileSaveAs.setText("Save As...");
        this.jMenuFileSaveAs.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFileSaveAsActionPerformed(evt);
            }
        });
        this.jMenuFile.add(this.jMenuFileSaveAs);
        this.jMenuFile.add(this.jSeparator1);

        this.jMenuFileQuickSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        this.jMenuFileQuickSave.setText("Quick Save");
        this.jMenuFileQuickSave.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFileQuickSaveActionPerformed(evt);
            }
        });
        this.jMenuFile.add(this.jMenuFileQuickSave);

        this.jMenuFileQuickLoad.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        this.jMenuFileQuickLoad.setText("Quick Load");
        this.jMenuFileQuickLoad.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFileQuickLoadActionPerformed(evt);
            }
        });
        this.jMenuFile.add(this.jMenuFileQuickLoad);
        this.jMenuFile.add(this.jSeparator2);

        this.jMenuFileLog.setText("Log to File");
        this.jMenuFileLog.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFileLogActionPerformed(evt);
            }
        });
        this.jMenuFile.add(this.jMenuFileLog);
        this.jMenuFile.add(this.jSeparator3);

        this.jMenuFileExit.setText("Exit");
        this.jMenuFileExit.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFileExitActionPerformed(evt);
            }
        });
        this.jMenuFile.add(this.jMenuFileExit);

        this.jMenuBar.add(this.jMenuFile);

        this.jMenuOptions.setText("Options");

        this.jMenuOptionsLAF.setText("Look And Feel");

        this.jRadioButtonLAFOcean.setText("Ocean");
        this.jRadioButtonLAFOcean.addItemListener(new java.awt.event.ItemListener() {
            @Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonLAFOceanItemStateChanged(evt);
            }
        });
        this.jMenuOptionsLAF.add(this.jRadioButtonLAFOcean);

        this.jRadioButtonLAFMetal.setText("Metal");
        this.jRadioButtonLAFMetal.addItemListener(new java.awt.event.ItemListener() {
            @Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonLAFMetalItemStateChanged(evt);
            }
        });
        this.jMenuOptionsLAF.add(this.jRadioButtonLAFMetal);

        this.jRadioButtonLAFWindows.setText("Windows");
        this.jRadioButtonLAFWindows.addItemListener(new java.awt.event.ItemListener() {
            @Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonLAFWindowsItemStateChanged(evt);
            }
        });
        this.jMenuOptionsLAF.add(this.jRadioButtonLAFWindows);

        this.jRadioButtonLAFXPlatform.setText("Cross Platform");
        this.jRadioButtonLAFXPlatform.addItemListener(new java.awt.event.ItemListener() {
            @Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonLAFXPlatformItemStateChanged(evt);
            }
        });
        this.jMenuOptionsLAF.add(this.jRadioButtonLAFXPlatform);

        this.jRadioButtonLAFSystem.setSelected(true);
        this.jRadioButtonLAFSystem.setText("System");
        this.jRadioButtonLAFSystem.addItemListener(new java.awt.event.ItemListener() {
            @Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonLAFSystemItemStateChanged(evt);
            }
        });
        this.jMenuOptionsLAF.add(this.jRadioButtonLAFSystem);

        this.jRadioButtonLAFLooks.setText("Looks");
        this.jRadioButtonLAFLooks.addItemListener(new java.awt.event.ItemListener() {
            @Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonLAFLooksItemStateChanged(evt);
            }
        });
        this.jMenuOptionsLAF.add(this.jRadioButtonLAFLooks);

        this.jRadioButtonLAFPlastic.setText("Plastic");
        this.jRadioButtonLAFPlastic.addItemListener(new java.awt.event.ItemListener() {
            @Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonLAFPlasticItemStateChanged(evt);
            }
        });
        this.jMenuOptionsLAF.add(this.jRadioButtonLAFPlastic);

        this.jRadioButtonLAFPlasticXP.setText("Plastic XP");
        this.jRadioButtonLAFPlasticXP.addItemListener(new java.awt.event.ItemListener() {
            @Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonLAFPlasticXPItemStateChanged(evt);
            }
        });
        this.jMenuOptionsLAF.add(this.jRadioButtonLAFPlasticXP);

        this.jMenuOptions.add(this.jMenuOptionsLAF);

        this.jCheckBoxMenuItemFastLogs.setSelected(true);
        this.jCheckBoxMenuItemFastLogs.setText("Fast Logs");
        this.jCheckBoxMenuItemFastLogs.addItemListener(new java.awt.event.ItemListener() {
            @Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxMenuItemFastLogsItemStateChanged(evt);
            }
        });
        this.jMenuOptions.add(this.jCheckBoxMenuItemFastLogs);

        this.jMenuBar.add(this.jMenuOptions);

        this.jMenuTools.setText("Tools");

        this.jMenuToolsHighScores.setText("High Scores");
        this.jMenuToolsHighScores.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuToolsHighScoresActionPerformed(evt);
            }
        });
        this.jMenuTools.add(this.jMenuToolsHighScores);

        this.jMenuBar.add(this.jMenuTools);

        this.jMenuHelp.setText("Help");

        this.jMenuHelpJurpeHome.setText("Jurpe Home");
        this.jMenuHelpJurpeHome.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuHelpJurpeHomeActionPerformed(evt);
            }
        });
        this.jMenuHelp.add(this.jMenuHelpJurpeHome);

        this.jMenuHelpTutorial.setText("Tutorial");
        this.jMenuHelpTutorial.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuHelpTutorialActionPerformed(evt);
            }
        });
        this.jMenuHelp.add(this.jMenuHelpTutorial);

        this.jMenuHelpCommands.setText("Commands");
        this.jMenuHelpCommands.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuHelpCommandsActionPerformed(evt);
            }
        });
        this.jMenuHelp.add(this.jMenuHelpCommands);

        this.jMenuHelpJurpeDevel.setText("Jurpe Devel");
        this.jMenuHelpJurpeDevel.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuHelpJurpeDevelActionPerformed(evt);
            }
        });
        this.jMenuHelp.add(this.jMenuHelpJurpeDevel);
        this.jMenuHelp.add(this.jSeparator4);

        this.jMenuHelpAbout.setText("About");
        this.jMenuHelpAbout.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuHelpAboutActionPerformed(evt);
            }
        });
        this.jMenuHelp.add(this.jMenuHelpAbout);

        this.jMenuBar.add(this.jMenuHelp);

        setJMenuBar(this.jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(this.jTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(this.jlblStatusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(this.txtScore, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(this.jTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(this.jlblStatusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(this.txtScore, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	void jMenuFileNewActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jMenuFileNewActionPerformed
		this.menuFileNew();
	}// GEN-LAST:event_jMenuFileNewActionPerformed

	private void jTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt)
	{// GEN-FIRST:event_jTabbedPaneStateChanged
		this.restorePanels();
		this.jlblStatusBar.setText("Ready");
	}// GEN-LAST:event_jTabbedPaneStateChanged

	private void jMenuFileOpenActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jMenuFileOpenActionPerformed
		this.menuFileOpen();
	}// GEN-LAST:event_jMenuFileOpenActionPerformed

	private void jMenuFileSaveAsActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jMenuFileSaveAsActionPerformed
		this.menuFileSaveAs();
	}// GEN-LAST:event_jMenuFileSaveAsActionPerformed

	private void jMenuFileQuickSaveActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jMenuFileQuickSaveActionPerformed
		this.menuFileQuickSave();
	}// GEN-LAST:event_jMenuFileQuickSaveActionPerformed

	private void jMenuFileQuickLoadActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jMenuFileQuickLoadActionPerformed
		this.menuFileQuickLoad();
	}// GEN-LAST:event_jMenuFileQuickLoadActionPerformed

	void jMenuFileLogActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jMenuFileLogActionPerformed
		this.menuFileLogToFile();
	}// GEN-LAST:event_jMenuFileLogActionPerformed

	private void jMenuFileExitActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jMenuFileExitActionPerformed
		this.menuFileExit();
	}// GEN-LAST:event_jMenuFileExitActionPerformed

	private void jCheckBoxMenuItemFastLogsItemStateChanged(java.awt.event.ItemEvent evt)
	{// GEN-FIRST:event_jCheckBoxMenuItemFastLogsItemStateChanged
		this.switchFastLog();
	}// GEN-LAST:event_jCheckBoxMenuItemFastLogsItemStateChanged

	private void jMenuToolsHighScoresActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jMenuToolsHighScoresActionPerformed
		this.showHighScores();
	}// GEN-LAST:event_jMenuToolsHighScoresActionPerformed

	private void jRadioButtonLAFOceanItemStateChanged(java.awt.event.ItemEvent evt)
	{// GEN-FIRST:event_jRadioButtonLAFOceanItemStateChanged
		this.LAFChange();
	}// GEN-LAST:event_jRadioButtonLAFOceanItemStateChanged

	private void jRadioButtonLAFMetalItemStateChanged(java.awt.event.ItemEvent evt)
	{// GEN-FIRST:event_jRadioButtonLAFMetalItemStateChanged
		this.LAFChange();
	}// GEN-LAST:event_jRadioButtonLAFMetalItemStateChanged

	private void jRadioButtonLAFWindowsItemStateChanged(java.awt.event.ItemEvent evt)
	{// GEN-FIRST:event_jRadioButtonLAFWindowsItemStateChanged
		this.LAFChange();
	}// GEN-LAST:event_jRadioButtonLAFWindowsItemStateChanged

	private void jRadioButtonLAFXPlatformItemStateChanged(java.awt.event.ItemEvent evt)
	{// GEN-FIRST:event_jRadioButtonLAFXPlatformItemStateChanged
		this.LAFChange();
	}// GEN-LAST:event_jRadioButtonLAFXPlatformItemStateChanged

	private void jRadioButtonLAFSystemItemStateChanged(java.awt.event.ItemEvent evt)
	{// GEN-FIRST:event_jRadioButtonLAFSystemItemStateChanged
		this.LAFChange();
	}// GEN-LAST:event_jRadioButtonLAFSystemItemStateChanged

	private void jRadioButtonLAFLooksItemStateChanged(java.awt.event.ItemEvent evt)
	{// GEN-FIRST:event_jRadioButtonLAFLooksItemStateChanged
		this.LAFChange();
	}// GEN-LAST:event_jRadioButtonLAFLooksItemStateChanged

	private void jRadioButtonLAFPlasticItemStateChanged(java.awt.event.ItemEvent evt)
	{// GEN-FIRST:event_jRadioButtonLAFPlasticItemStateChanged
		this.LAFChange();
	}// GEN-LAST:event_jRadioButtonLAFPlasticItemStateChanged

	private void jRadioButtonLAFPlasticXPItemStateChanged(java.awt.event.ItemEvent evt)
	{// GEN-FIRST:event_jRadioButtonLAFPlasticXPItemStateChanged
		this.LAFChange();
	}// GEN-LAST:event_jRadioButtonLAFPlasticXPItemStateChanged

	private void jMenuHelpAboutActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jMenuHelpAboutActionPerformed
		JDialogAbout ag = new JDialogAbout(this);
		ag.setVisible(true);
	}// GEN-LAST:event_jMenuHelpAboutActionPerformed

	private static void jMenuHelpJurpeDevelActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jMenuHelpJurpeDevelActionPerformed
		OSProps.showURL("https://sourceforge.net/projects/jurpe/");
	}// GEN-LAST:event_jMenuHelpJurpeDevelActionPerformed

	private void jMenuHelpCommandsActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jMenuHelpCommandsActionPerformed
		this.showHelpCommands();
	}// GEN-LAST:event_jMenuHelpCommandsActionPerformed

	private void jMenuHelpTutorialActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jMenuHelpTutorialActionPerformed
		this.showTutorial();
	}// GEN-LAST:event_jMenuHelpTutorialActionPerformed

	private static void jMenuHelpJurpeHomeActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jMenuHelpJurpeHomeActionPerformed
		OSProps.showURL("http://net.littlelite.jurpe.sourceforge.net/");
	}// GEN-LAST:event_jMenuHelpJurpeHomeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemFastLogs;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuFileExit;
    private javax.swing.JMenuItem jMenuFileLog;
    private javax.swing.JMenuItem jMenuFileNew;
    private javax.swing.JMenuItem jMenuFileOpen;
    private javax.swing.JMenuItem jMenuFileQuickLoad;
    private javax.swing.JMenuItem jMenuFileQuickSave;
    private javax.swing.JMenuItem jMenuFileSaveAs;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuHelpAbout;
    private javax.swing.JMenuItem jMenuHelpCommands;
    private javax.swing.JMenuItem jMenuHelpJurpeDevel;
    private javax.swing.JMenuItem jMenuHelpJurpeHome;
    private javax.swing.JMenuItem jMenuHelpTutorial;
    private javax.swing.JMenu jMenuOptions;
    private javax.swing.JMenu jMenuOptionsLAF;
    private javax.swing.JMenu jMenuTools;
    private javax.swing.JMenuItem jMenuToolsHighScores;
    private javax.swing.JRadioButtonMenuItem jRadioButtonLAFLooks;
    private javax.swing.JRadioButtonMenuItem jRadioButtonLAFMetal;
    private javax.swing.JRadioButtonMenuItem jRadioButtonLAFOcean;
    private javax.swing.JRadioButtonMenuItem jRadioButtonLAFPlastic;
    private javax.swing.JRadioButtonMenuItem jRadioButtonLAFPlasticXP;
    private javax.swing.JRadioButtonMenuItem jRadioButtonLAFSystem;
    private javax.swing.JRadioButtonMenuItem jRadioButtonLAFWindows;
    private javax.swing.JRadioButtonMenuItem jRadioButtonLAFXPlatform;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    protected javax.swing.JTabbedPane jTabbedPane;
    protected javax.swing.JLabel jlblStatusBar;
    protected javax.swing.JLabel txtScore;
    // End of variables declaration//GEN-END:variables
}
