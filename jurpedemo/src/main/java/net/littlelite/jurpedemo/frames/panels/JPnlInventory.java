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

import java.util.AbstractList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JList;

import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.containers.Inventory;
import net.littlelite.jurpe.items.AbstractItem;
import net.littlelite.jurpe.system.resources.ResourceFinder;
import net.littlelite.jurpedemo.frames.InventoryListBox;
import net.littlelite.jurpedemo.frames.JurpeMain;
import net.littlelite.jurpedemo.resources.GameStrings;

/**
 *
 */
public class JPnlInventory extends JPanelTemplate
{

    // UID
    private static final long serialVersionUID = 3L;

    // Inventory List
    private InventoryListBox jLstOggetti;

    /**
     * Create new inventory panel
     * @param gui
     */
    public JPnlInventory(JurpeMain gui)
    {
        super(gui);
        initComponents();

        this.setInventoryPicture();
        this.setInventoryList();
    }

    /**
     * Get the inventory list control
     * @return the inventory list control
     */
    public JList getJLstOggetti()
    {
            return this.jLstOggetti;
    }

    /**
     * Set inventory picture
     */
    private void setInventoryPicture()
    {
            ImageIcon panelIcon = ResourceFinder.resources().getResourceAsImage("sketches.jpg");
            this.lblImage.setIcon(panelIcon);
    }

    /**
     * Set inventory control
     */
    private void setInventoryList()
    {
            this.jLstOggetti = new InventoryListBox(this.parentGUI);
            jLstOggetti.setToolTipText("Inventory");
            jScrollPane.setViewportView(jLstOggetti);
    }

    /**
     * Refresh panel
     */
    @Override
    public void refresh()
    {
            PCharacter curPC;
            Inventory curInv;

            if (this.theSystem.isPCgenerated())
            {
                    curPC = theSystem.getPC();
                    curInv = curPC.getInventory();

                    // Refresh Money
                    this.jTxtMoney.setText("$" + String.valueOf(curInv.getAvailableMoney()));
                    // Refresh Encumbrance
                    this.jTxtEncumbrance.setText(String.valueOf(curPC.getEncumbrance()) + " kg");
                    // Refresh Encumbrance Level
                    this.jTxtEncumbranceLevel.setText(String.valueOf(curPC.getEncumbranceLevel()));

                    // Refresh Inventory List
                    AbstractList<AbstractItem> oggetti = curInv.getInventoryItems();
                    if (oggetti.size() > 0)
                    {
                            this.jLstOggetti.setListData(oggetti.toArray());
                            this.jLstOggetti.setMouseListeners(false);
                    }
                    else
                    {
                            Vector<String> nothing = new Vector<String>();
                            nothing.add(GameStrings.CARRNOTHING);
                            this.jLstOggetti.setListData(nothing);
                            this.jLstOggetti.setMouseListeners(true);
                    }

                    // Refresh Armor
                    String strArmatura, strArma, strRanged, strShield, strItem;
                    if (curPC.getCurrentArmor() != null)
                    {
                            strArmatura = curPC.getCurrentArmor().toString();
                            this.jBtnUnwearArmor.setEnabled(true);
                    }
                    else
                    {
                            strArmatura = "";
                            this.jBtnUnwearArmor.setEnabled(false);
                    }
                    this.txtWoreArmor.setText(strArmatura);

                    // Refresh Weapon
                    if (curPC.getCurrentWeapon() != null)
                    {
                            strArma = curPC.getCurrentWeapon().toString();
                            this.jBtnUnwearWeapon.setEnabled(true);
                    }
                    else
                    {
                            strArma = "";
                            this.jBtnUnwearWeapon.setEnabled(false);
                    }
                    this.txtWoreWeapon.setText(strArma);

                    // Refresh Ranged Weapon
                    if (curPC.getCurrentRangedWeapon() != null)
                    {
                            strRanged = curPC.getCurrentRangedWeapon().toString();
                            this.jBtnUnwearRangedWeapon.setEnabled(true);
                    }
                    else
                    {
                            strRanged = "";
                            this.jBtnUnwearRangedWeapon.setEnabled(false);
                    }
                    this.txtRangedWeapon.setText(strRanged);

                    // Refresh Shield
                    if (curPC.getCurrentShield() != null)
                    {
                            strShield = curPC.getCurrentShield().toString();
                            this.jBtnUnwearShield.setEnabled(true);
                    }
                    else
                    {
                            strShield = "";
                            this.jBtnUnwearShield.setEnabled(false);
                    }
                    this.txtWoreShield.setText(strShield);

                    // Refresh Item
                    if (curPC.getCurrentItem() != null)
                    {
                            strItem = curPC.getCurrentItem().toString();
                            this.jBtnUnwearItem.setEnabled(true);
                    }
                    else
                    {
                            strItem = "";
                            this.jBtnUnwearItem.setEnabled(false);
                    }
                    this.txtItemInd.setText(strItem);
            }
            else
            {
                    this.jTxtMoney.setText("");
                    this.jTxtEncumbrance.setText("");
                    this.jLstOggetti.setListData(new Vector<String>());
                    this.txtWoreArmor.setText("");
                    this.txtWoreWeapon.setText("");
                    this.txtRangedWeapon.setText("");
                    this.txtWoreShield.setText("");
                    this.txtItemInd.setText("");
            }
    }

    private void unwear(AbstractItem ogg)
    {
            this.parentGUI.unwearItem(ogg);
    }

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlImage = new javax.swing.JPanel();
        lblImage = new javax.swing.JLabel();
        pnlEquipment = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtItemInd = new javax.swing.JTextField();
        txtWoreArmor = new javax.swing.JTextField();
        txtWoreShield = new javax.swing.JTextField();
        txtRangedWeapon = new javax.swing.JTextField();
        txtWoreWeapon = new javax.swing.JTextField();
        jBtnUnwearWeapon = new javax.swing.JButton();
        jBtnUnwearRangedWeapon = new javax.swing.JButton();
        jBtnUnwearShield = new javax.swing.JButton();
        jBtnUnwearArmor = new javax.swing.JButton();
        jBtnUnwearItem = new javax.swing.JButton();
        pnlEncumbrance = new javax.swing.JPanel();
        jTxtEncumbrance = new javax.swing.JTextField();
        jTxtEncumbranceLevel = new javax.swing.JTextField();
        jTxtMoney = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        pnlInventory = new javax.swing.JPanel();
        jScrollPane = new javax.swing.JScrollPane();

        pnlImage.setBackground(new java.awt.Color(255, 255, 255));
        pnlImage.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pnlImageLayout = new javax.swing.GroupLayout(pnlImage);
        pnlImage.setLayout(pnlImageLayout);
        pnlImageLayout.setHorizontalGroup(
            pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlImageLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblImage, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlImageLayout.setVerticalGroup(
            pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlImageLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblImage, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() & ~java.awt.Font.BOLD));
        jLabel1.setText("Melee Weapon");

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() & ~java.awt.Font.BOLD));
        jLabel2.setText("Ranged Weapon");

        jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getStyle() & ~java.awt.Font.BOLD));
        jLabel3.setText("Shield");

        jLabel4.setFont(jLabel4.getFont().deriveFont(jLabel4.getFont().getStyle() & ~java.awt.Font.BOLD));
        jLabel4.setText("Armor");

        jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getStyle() & ~java.awt.Font.BOLD));
        jLabel5.setText("Item");

        txtItemInd.setBackground(new java.awt.Color(255, 204, 153));
        txtItemInd.setEditable(false);
        txtItemInd.setFocusable(false);

        txtWoreArmor.setBackground(new java.awt.Color(255, 204, 153));
        txtWoreArmor.setEditable(false);
        txtWoreArmor.setFocusable(false);

        txtWoreShield.setBackground(new java.awt.Color(255, 204, 153));
        txtWoreShield.setEditable(false);
        txtWoreShield.setFocusable(false);

        txtRangedWeapon.setBackground(new java.awt.Color(255, 204, 153));
        txtRangedWeapon.setEditable(false);
        txtRangedWeapon.setFocusable(false);

        txtWoreWeapon.setBackground(new java.awt.Color(255, 204, 153));
        txtWoreWeapon.setEditable(false);
        txtWoreWeapon.setFocusable(false);

        jBtnUnwearWeapon.setText("-");
        jBtnUnwearWeapon.setToolTipText("Unequip");
        jBtnUnwearWeapon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnUnwearWeaponActionPerformed(evt);
            }
        });

        jBtnUnwearRangedWeapon.setText("-");
        jBtnUnwearRangedWeapon.setToolTipText("Unequip");
        jBtnUnwearRangedWeapon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnUnwearRangedWeaponActionPerformed(evt);
            }
        });

        jBtnUnwearShield.setText("-");
        jBtnUnwearShield.setToolTipText("Unequip");
        jBtnUnwearShield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnUnwearShieldActionPerformed(evt);
            }
        });

        jBtnUnwearArmor.setText("-");
        jBtnUnwearArmor.setToolTipText("Unequip");
        jBtnUnwearArmor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnUnwearArmorActionPerformed(evt);
            }
        });

        jBtnUnwearItem.setText("-");
        jBtnUnwearItem.setToolTipText("Unequip");
        jBtnUnwearItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnUnwearItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlEquipmentLayout = new javax.swing.GroupLayout(pnlEquipment);
        pnlEquipment.setLayout(pnlEquipmentLayout);
        pnlEquipmentLayout.setHorizontalGroup(
            pnlEquipmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEquipmentLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlEquipmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEquipmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtItemInd)
                    .addComponent(txtWoreArmor)
                    .addComponent(txtWoreShield)
                    .addComponent(txtRangedWeapon)
                    .addComponent(txtWoreWeapon, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEquipmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBtnUnwearWeapon)
                    .addComponent(jBtnUnwearRangedWeapon)
                    .addComponent(jBtnUnwearShield)
                    .addComponent(jBtnUnwearArmor)
                    .addComponent(jBtnUnwearItem))
                .addContainerGap())
        );
        pnlEquipmentLayout.setVerticalGroup(
            pnlEquipmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEquipmentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEquipmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtWoreWeapon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnUnwearWeapon))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEquipmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtRangedWeapon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnUnwearRangedWeapon))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEquipmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtWoreShield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnUnwearShield))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEquipmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtWoreArmor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnUnwearArmor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEquipmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtItemInd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnUnwearItem))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTxtEncumbrance.setBackground(new java.awt.Color(255, 255, 204));
        jTxtEncumbrance.setEditable(false);
        jTxtEncumbrance.setToolTipText("Encumbrance");
        jTxtEncumbrance.setFocusable(false);

        jTxtEncumbranceLevel.setBackground(new java.awt.Color(255, 255, 204));
        jTxtEncumbranceLevel.setEditable(false);
        jTxtEncumbranceLevel.setToolTipText("Encumbrance Level");
        jTxtEncumbranceLevel.setFocusable(false);

        jTxtMoney.setBackground(new java.awt.Color(255, 255, 204));
        jTxtMoney.setEditable(false);
        jTxtMoney.setFocusable(false);

        jLabel7.setFont(jLabel7.getFont().deriveFont(jLabel7.getFont().getStyle() & ~java.awt.Font.BOLD));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Encumbrance");

        jLabel9.setFont(jLabel9.getFont().deriveFont(jLabel9.getFont().getStyle() & ~java.awt.Font.BOLD));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Money");

        javax.swing.GroupLayout pnlEncumbranceLayout = new javax.swing.GroupLayout(pnlEncumbrance);
        pnlEncumbrance.setLayout(pnlEncumbranceLayout);
        pnlEncumbranceLayout.setHorizontalGroup(
            pnlEncumbranceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncumbranceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEncumbranceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEncumbranceLayout.createSequentialGroup()
                        .addComponent(jTxtEncumbrance, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTxtEncumbranceLevel, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEncumbranceLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTxtMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlEncumbranceLayout.setVerticalGroup(
            pnlEncumbranceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEncumbranceLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEncumbranceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTxtEncumbrance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtEncumbranceLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(1, 1, 1)
                .addComponent(jTxtMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        jScrollPane.setFont(jScrollPane.getFont().deriveFont(jScrollPane.getFont().getStyle() & ~java.awt.Font.BOLD));

        javax.swing.GroupLayout pnlInventoryLayout = new javax.swing.GroupLayout(pnlInventory);
        pnlInventory.setLayout(pnlInventoryLayout);
        pnlInventoryLayout.setHorizontalGroup(
            pnlInventoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlInventoryLayout.setVerticalGroup(
            pnlInventoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlInventory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlEquipment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlImage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlEncumbrance, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlEquipment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlInventory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(pnlImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlEncumbrance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void jBtnUnwearWeaponActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jBtnUnwearWeaponActionPerformed
		unwear(theSystem.getPC().getCurrentWeapon());
	}// GEN-LAST:event_jBtnUnwearWeaponActionPerformed

	private void jBtnUnwearRangedWeaponActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jBtnUnwearRangedWeaponActionPerformed
		unwear(theSystem.getPC().getCurrentRangedWeapon());
	}// GEN-LAST:event_jBtnUnwearRangedWeaponActionPerformed

	private void jBtnUnwearShieldActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jBtnUnwearShieldActionPerformed
		unwear(theSystem.getPC().getCurrentShield());
	}// GEN-LAST:event_jBtnUnwearShieldActionPerformed

	private void jBtnUnwearArmorActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jBtnUnwearArmorActionPerformed
		unwear(theSystem.getPC().getCurrentArmor());
	}// GEN-LAST:event_jBtnUnwearArmorActionPerformed

	private void jBtnUnwearItemActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_jBtnUnwearItemActionPerformed
		unwear(theSystem.getPC().getCurrentItem());
	}// GEN-LAST:event_jBtnUnwearItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnUnwearArmor;
    private javax.swing.JButton jBtnUnwearItem;
    private javax.swing.JButton jBtnUnwearRangedWeapon;
    private javax.swing.JButton jBtnUnwearShield;
    private javax.swing.JButton jBtnUnwearWeapon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTextField jTxtEncumbrance;
    private javax.swing.JTextField jTxtEncumbranceLevel;
    private javax.swing.JTextField jTxtMoney;
    private javax.swing.JLabel lblImage;
    private javax.swing.JPanel pnlEncumbrance;
    private javax.swing.JPanel pnlEquipment;
    private javax.swing.JPanel pnlImage;
    private javax.swing.JPanel pnlInventory;
    private javax.swing.JTextField txtItemInd;
    private javax.swing.JTextField txtRangedWeapon;
    private javax.swing.JTextField txtWoreArmor;
    private javax.swing.JTextField txtWoreShield;
    private javax.swing.JTextField txtWoreWeapon;
    // End of variables declaration//GEN-END:variables
}
