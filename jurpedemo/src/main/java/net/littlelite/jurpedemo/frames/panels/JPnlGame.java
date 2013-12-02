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
package net.littlelite.jurpedemo.frames.panels;

import java.awt.Color;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import net.littlelite.jurpe.dungeon.*;
import net.littlelite.jurpe.dungeon.hexmap.HexMap;
import net.littlelite.jurpe.dungeon.hexmap.HexMapFactory;
import net.littlelite.jurpe.items.Item;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpedemo.JurpeDemoConfig;
import net.littlelite.jurpedemo.frames.JurpeMain;

/**
 * Panel tab showing the main game interface (map)
 */
public final class JPnlGame extends JPanelTemplate
{

    // UID
    private static final long serialVersionUID = 2L;

    // Buisiness Logic
    private boolean isDungeonVisible;
    private HexMap hexMap;

    /**
     * Creates new main tab
     */
    public JPnlGame(JurpeMain gui)
    {
        super(gui);

        initComponents();
        this.setPanelDimension();
        this.isDungeonVisible = false;
        this.initConsole();
        this.initLayout();
    }

    /**
     * @return Current Hexagonal Map
     */
    public HexMap getHexMap()
    {
        return this.hexMap;
    }

    public JList getConsole()
    {
        return this.jList;
    }

    /**
     * Refresh panel. Scroll textbar down
     * 
     * @throws JurpeException
     */
    @Override
    public void refresh()
    {
        // Refresh Log
        this.parentGUI.refreshLog();

        // Refresh Score
        this.parentGUI.refreshScore();

        // Refresh Dungeon
        if (this.isDungeonVisible)
        {
            this.hexMap.repaint();
        }
        else
        {
            try
            {
                this.initDungeon();
            }
            catch (JurpeException jex)
            {
                this.theSystem.getLog().addEntry(jex.getMessage());
            }
        }
    }

    private void initDungeon() throws JurpeException
    {
        if (this.theSystem.isPCgenerated())
        {

            // create new dungeon
            if (this.theSystem.getDungeon() == null)
            {
                this.theSystem.initializeDungeon();
            }

            Dungeons d = this.theSystem.getDungeon();

            // Syncing HexMap
            if (this.hexMap == null)
            {
                this.createHexMap(d);
            }
            else
            {
                this.hexMap.synchHexMapView();
            }

            Avatar av = d.getAvatar();
            if (av == null)
            {
                d.initAvatar(this.hexMap);
            }

            if (!this.isDungeonVisible)
            {
                // enable dungeon in UI
                this.showDungeon();
            }

            // Add an item

            // Phisically create dungeon
            DungeonLevels dngLevels = d.getDungeonLevels();
            //dngLevels.createLevels();

            // Add items to the dungeon
            Item grimoire = new Item("Old grimoire", 500, 1, "An old, dusty book, filled with symbols", -1, null, 0, false);
            dngLevels.addItemToLevel(5, grimoire, PlaceholderType.ITEM, true);


            Level initialLevel = d.getAvatar().getLevel();
            this.theSystem.getDungeon().drawDungeon(initialLevel, this.hexMap);
            this.parentGUI.refreshLog();
        }
    }

    private void createHexMap(Dungeons d)
    {
        int hmWidth = this.jPanelJurpe.getWidth() - 5;
        int hmHeight = this.jPanelJurpe.getHeight() - 5;
        this.hexMap = HexMapFactory.createHexMap(this, d, hmWidth, hmHeight);
    }

    private void initConsole()
    {
        // Init List
        this.jList.setModel(new DefaultListModel());
        this.jList.setSelectionBackground(Color.WHITE);
        this.jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void initLayout()
    {
        this.jPanelJurpe.setBackground(JurpeDemoConfig.WALL_COLOR);
        ImageIcon gameBackground = this.rf.getResourceAsImage(JurpeDemoConfig.JURPEBAKGIF);
        this.jLblImage.setIcon(gameBackground);
    }

    /**
     * Show Dungeon and connect it to game
     * 
     */
    private void showDungeon()
    {
        this.jLblImage.setVisible(false);
        this.jPanelJurpe.remove(this.jLblImage);
        this.jPanelJurpe.add(this.hexMap);
        this.hexMap.setVisible(true);
        this.isDungeonVisible = true;
    }

    /**
     * Removes the dungeon and shows a nice background image
     */
    public void removeDungeon()
    {
        if (this.hexMap != null)
        {
            this.jPanelJurpe.remove(this.hexMap);
            this.hexMap = null;
            this.jPanelJurpe.add(this.jLblImage);
            this.jLblImage.setVisible(true);
            this.isDungeonVisible = false;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane = new javax.swing.JScrollPane();
        jList = new javax.swing.JList();
        jPanelJurpe = new javax.swing.JPanel();
        jLblImage = new javax.swing.JLabel();

        jScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jList.setFont(jList.getFont().deriveFont(jList.getFont().getStyle() & ~java.awt.Font.BOLD, jList.getFont().getSize()-1));
        jList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jList.setSelectionForeground(new java.awt.Color(51, 153, 255));
        jScrollPane.setViewportView(jList);

        jPanelJurpe.setBackground(new java.awt.Color(102, 102, 102));
        jPanelJurpe.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanelJurpe.setFocusable(false);
        jPanelJurpe.setMaximumSize(new java.awt.Dimension(550, 282));
        jPanelJurpe.setMinimumSize(new java.awt.Dimension(550, 282));
        jPanelJurpe.setPreferredSize(new java.awt.Dimension(550, 282));
        jPanelJurpe.setRequestFocusEnabled(false);
        jPanelJurpe.setVerifyInputWhenFocusTarget(false);
        jPanelJurpe.setLayout(new java.awt.BorderLayout());
        jPanelJurpe.add(jLblImage, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelJurpe, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelJurpe, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLblImage;
    private javax.swing.JList jList;
    private javax.swing.JPanel jPanelJurpe;
    private javax.swing.JScrollPane jScrollPane;
    // End of variables declaration//GEN-END:variables
}
