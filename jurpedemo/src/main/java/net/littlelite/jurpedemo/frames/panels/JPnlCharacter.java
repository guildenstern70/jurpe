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
package net.littlelite.jurpedemo.frames.panels;

import java.awt.Dimension;
import java.util.AbstractList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import net.littlelite.jurpe.characters.Innate;
import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.characters.PrimaryStats;
import net.littlelite.jurpe.combat.AllOutAttackType;
import net.littlelite.jurpedemo.frames.JurpeMain;
import net.littlelite.jurpedemo.frames.TabbedPanel;

/**
 * Main character tab
 * 
 */
public final class JPnlCharacter extends JPanelTemplate
{

    // UID
    private static final long serialVersionUID = 1L;
    // Business Logic
    private String lastImage; // last loaded character image file

    /**
     * Constructor
     * @param gui Main Form handler
     */
    public JPnlCharacter(JurpeMain gui)
    {
        super(gui);
        initComponents();
        this.customInit();
    }

    /**
     * Set character's image
     * 
     * @param imagefile
     */
    public void setImage(String imagefile)
    {
        // There's no need to reload picture every time
        if ((this.lastImage == null) || (!this.lastImage.equals(imagefile)))
        {
            ImageIcon image = this.rf.getResourceAsImage(imagefile);
            this.jImagePC.setIcon(image);
            this.jImagePC.setPreferredSize(new Dimension(154, 131));
            this.jImagePC.revalidate();
            this.lastImage = imagefile;
        }
    }

    /**
     * Set character's name
     * 
     * @param name
     */
    public void setCharacterName(String name)
    {
        this.txtName.setText(name);
    }

    /**
     * @todo Kick
     * @todo Gestione della Fatica (in combattimento)
     */
    @Override
    public void refresh()
    {
        if (theSystem.isPCgenerated())
        {
            PCharacter curPC = theSystem.getPC();
            PrimaryStats ps = curPC.getPrimaryStats();
            
            this.jImagePC.setToolTipText(curPC.getName());

            // Primary Stats
            this.txtStr.setText(String.valueOf(ps.getST())); // ST
            this.txtDex.setText(String.valueOf(ps.getDX())); // DX
            this.txtInt.setText(String.valueOf(ps.getIQ())); // IQ
            this.txtHT.setText(String.valueOf(ps.getHT())); // HT
            this.txtHP.setText(ps.getHPvsHPMAX()); // Hit Points
            this.txtName.setText(curPC.getCharacterAttributes().getName());
            // Name
            this.txtFatigue.setText(String.valueOf(ps.getFatigue()));
            // Fatigue

            // Refresh MVMT
            this.txtMov.setText(String.valueOf(curPC.getMvmt())); // MVMT

            // Refresh SPEED
            this.txtSpd.setText(curPC.getFormattedVel()); // Speed

            // Refresh Physical Attributes
            this.jTextAge.setText(String.valueOf(curPC.getCharacterAttributes().getAge()));
            this.jTextHeight.setText(String.valueOf(curPC.getCharacterAttributes().getHeight()));
            this.jTextWeight.setText(String.valueOf(curPC.getCharacterAttributes().getWeight()));
            this.jTextSex.setText(String.valueOf(curPC.getCharacterAttributes().getSex()));

            // Refresh ALLOUTATTACK
            this.jCboAllAttack.setSelectedItem(curPC.getAllOutAttackType());

            // Refresh DEFENSE
            this.txtDodging.setText(String.valueOf(curPC.getDodgePoints()));
            this.txtParrying.setText(String.valueOf(curPC.getParryPoints()));
            this.txtBlocking.setText(String.valueOf(curPC.getBlockPoints()));            
            this.txtPD.setText(String.valueOf(curPC.getPassiveDefense()));
            this.txtDR.setText(String.valueOf(curPC.getDamageResistance()));

            // Refresh ATTACK
            this.txtThrust.setToolTipText(curPC.getCrushDamage());
            this.txtThrust.setText(curPC.getCrushDamage());
            this.txtSwing.setToolTipText(curPC.getSwingDamage());
            this.txtSwing.setText(curPC.getSwingDamage());
            this.txtKick.setText("-");

            // Refresh character points
            this.txtCurrentCharPoints.setText(String.valueOf(curPC.getSpentPoints()));
            this.txtCharPoints.setText(String.valueOf(curPC.getAvailablePoints()));

            // Innates
            this.refreshInnates(curPC);

        }
        else
        {
            parentGUI.enablePanel(TabbedPanel.TAB_CHARACTER, false);
            // will be re-enabled after character creation
            this.txtStr.setText("");
            this.txtDex.setText("");
            this.txtInt.setText("");
            this.txtHT.setText("");
            this.txtName.setText("");
            this.txtMov.setText("");
            this.txtSpd.setText("");
            this.txtDodging.setText("");
            this.txtParrying.setText("");
            this.txtFatigue.setText("");
            this.txtBlocking.setText("");
            this.txtThrust.setText("");
            this.txtKick.setText("");
            this.txtSwing.setText("");
            this.txtPD.setText("");
            this.txtDR.setText("");
            this.txtCharPoints.setText("");
            this.txtCurrentCharPoints.setText("");
            this.jTextAge.setText("");
            this.jTextHeight.setText("");
            this.jTextWeight.setText("");
            this.jTextSex.setText("");
            DefaultListModel dlm = (DefaultListModel) this.jLstInnates.getModel();
            dlm.removeAllElements();
        }
    }

    private void refreshInnates(PCharacter curPC)
    {
        AbstractList<Innate> inn = curPC.getCharacterAttributes().innates();
        DefaultListModel dlm = (DefaultListModel) this.jLstInnates.getModel();
        dlm.removeAllElements();
        for (Innate i : inn)
        {
            dlm.addElement(i.getName());
        }
    }

    private void customInit()
    {
        AllOutAttackType[] attacks =
                {AllOutAttackType.TWOATTACK, AllOutAttackType.BONUSDAMG, AllOutAttackType.BONUSKILL};
        this.jCboAllAttack.setModel(new DefaultComboBoxModel(attacks));
        this.jLstInnates.setModel(new DefaultListModel());
    }

    private void changeAttack()
    {
        this.parentGUI.setAllOutAttackType(this.jCboAllAttack.getSelectedItem());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelPicture = new javax.swing.JPanel();
        jImagePC = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        txtCharPoints = new javax.swing.JTextField();
        txtCurrentCharPoints = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        pnlSexAge = new javax.swing.JPanel();
        jTextWeight = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jTextHeight = new javax.swing.JTextField();
        jTextAge = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jTextSex = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jPnlDefense = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtDR = new javax.swing.JTextField();
        txtPD = new javax.swing.JTextField();
        txtParrying = new javax.swing.JTextField();
        txtBlocking = new javax.swing.JTextField();
        txtDodging = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtName = new javax.swing.JLabel();
        pnlStats = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtHT = new javax.swing.JTextField();
        txtDex = new javax.swing.JTextField();
        txtInt = new javax.swing.JTextField();
        txtStr = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtMov = new javax.swing.JTextField();
        txtSpd = new javax.swing.JTextField();
        txtHP = new javax.swing.JTextField();
        txtFatigue = new javax.swing.JTextField();
        pnlInnates = new javax.swing.JPanel();
        jScrollPane = new javax.swing.JScrollPane();
        jLstInnates = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jPnlAttack = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtThrust = new javax.swing.JTextField();
        txtSwing = new javax.swing.JTextField();
        txtKick = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jCboAllAttack = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(550, 400));

        jPanelPicture.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jImagePC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jImagePC.setToolTipText("Character's Portrait");
        jImagePC.setAlignmentX(0.5F);
        jImagePC.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jImagePC.setMaximumSize(new java.awt.Dimension(155, 130));
        jImagePC.setMinimumSize(new java.awt.Dimension(155, 130));
        jImagePC.setPreferredSize(new java.awt.Dimension(155, 130));

        jLabel28.setFont(jLabel28.getFont().deriveFont(jLabel28.getFont().getStyle() | java.awt.Font.BOLD, jLabel28.getFont().getSize()-1));
        jLabel28.setForeground(new java.awt.Color(0, 0, 204));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Character Points");

        txtCharPoints.setEditable(false);
        txtCharPoints.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCharPoints.setText("104");
        txtCharPoints.setFocusable(false);

        txtCurrentCharPoints.setEditable(false);
        txtCurrentCharPoints.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCurrentCharPoints.setText("100");
        txtCurrentCharPoints.setFocusable(false);

        jLabel30.setFont(jLabel30.getFont().deriveFont(jLabel30.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel30.getFont().getSize()-1));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Current");

        jLabel29.setFont(jLabel29.getFont().deriveFont(jLabel29.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel29.getFont().getSize()-1));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Total");

        jTextWeight.setEditable(false);
        jTextWeight.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextWeight.setText("80 kg");
        jTextWeight.setFocusable(false);

        jLabel27.setFont(jLabel27.getFont().deriveFont(jLabel27.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel27.getFont().getSize()-1));
        jLabel27.setText("Weight");

        jLabel26.setFont(jLabel26.getFont().deriveFont(jLabel26.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel26.getFont().getSize()-1));
        jLabel26.setText("Height");

        jTextHeight.setEditable(false);
        jTextHeight.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextHeight.setText("176 cm");
        jTextHeight.setFocusable(false);

        jTextAge.setEditable(false);
        jTextAge.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextAge.setText("34");
        jTextAge.setFocusable(false);

        jLabel25.setFont(jLabel25.getFont().deriveFont(jLabel25.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel25.getFont().getSize()-1));
        jLabel25.setText("Age");

        jTextSex.setEditable(false);
        jTextSex.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextSex.setText("Male");
        jTextSex.setFocusable(false);

        jLabel24.setFont(jLabel24.getFont().deriveFont(jLabel24.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel24.getFont().getSize()-1));
        jLabel24.setText("Sex");

        javax.swing.GroupLayout pnlSexAgeLayout = new javax.swing.GroupLayout(pnlSexAge);
        pnlSexAge.setLayout(pnlSexAgeLayout);
        pnlSexAgeLayout.setHorizontalGroup(
            pnlSexAgeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSexAgeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSexAgeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel26)
                    .addComponent(jLabel25)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSexAgeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextWeight)
                    .addComponent(jTextSex, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addComponent(jTextAge)
                    .addComponent(jTextHeight))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSexAgeLayout.setVerticalGroup(
            pnlSexAgeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSexAgeLayout.createSequentialGroup()
                .addGroup(pnlSexAgeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jTextSex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSexAgeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jTextAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSexAgeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jTextHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSexAgeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlSexAge, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtCharPoints, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtCurrentCharPoints, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtCharPoints, txtCurrentCharPoints});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(pnlSexAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCurrentCharPoints, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCharPoints, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel29)))
        );

        javax.swing.GroupLayout jPanelPictureLayout = new javax.swing.GroupLayout(jPanelPicture);
        jPanelPicture.setLayout(jPanelPictureLayout);
        jPanelPictureLayout.setHorizontalGroup(
            jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPictureLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jImagePC, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanelPictureLayout.setVerticalGroup(
            jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPictureLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jImagePC, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPnlDefense.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel11.setFont(jLabel11.getFont().deriveFont(jLabel11.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Defense");

        txtDR.setEditable(false);
        txtDR.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDR.setText("20");
        txtDR.setToolTipText("Damage Resistance");
        txtDR.setFocusable(false);

        txtPD.setEditable(false);
        txtPD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPD.setText("20");
        txtPD.setToolTipText("Passive Defense");
        txtPD.setFocusable(false);

        txtParrying.setEditable(false);
        txtParrying.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtParrying.setText("99");
        txtParrying.setFocusable(false);

        txtBlocking.setEditable(false);
        txtBlocking.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBlocking.setText("99");
        txtBlocking.setFocusable(false);

        txtDodging.setEditable(false);
        txtDodging.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDodging.setText("99");
        txtDodging.setFocusable(false);

        jLabel16.setFont(jLabel16.getFont().deriveFont(jLabel16.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel16.getFont().getSize()-1));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setLabelFor(txtDR);
        jLabel16.setText("DR");
        jLabel16.setToolTipText("Resistance to damage");

        jLabel17.setFont(jLabel17.getFont().deriveFont(jLabel17.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel17.getFont().getSize()-1));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setLabelFor(txtPD);
        jLabel17.setText("PD");
        jLabel17.setToolTipText("Passive Defense");

        jLabel18.setFont(jLabel18.getFont().deriveFont(jLabel18.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel18.getFont().getSize()-1));
        jLabel18.setLabelFor(txtParrying);
        jLabel18.setText("Parrying");

        jLabel19.setFont(jLabel19.getFont().deriveFont(jLabel19.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel19.getFont().getSize()-1));
        jLabel19.setLabelFor(txtBlocking);
        jLabel19.setText("Blocking");

        jLabel20.setFont(jLabel20.getFont().deriveFont(jLabel20.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel20.getFont().getSize()-1));
        jLabel20.setLabelFor(txtDodging);
        jLabel20.setText("Dodging");

        jLabel21.setFont(jLabel21.getFont().deriveFont(jLabel21.getFont().getStyle() | java.awt.Font.BOLD, jLabel21.getFont().getSize()-1));
        jLabel21.setForeground(new java.awt.Color(0, 0, 204));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Active");

        jLabel22.setFont(jLabel22.getFont().deriveFont(jLabel22.getFont().getStyle() | java.awt.Font.BOLD, jLabel22.getFont().getSize()-1));
        jLabel22.setForeground(new java.awt.Color(0, 0, 204));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Passive");

        javax.swing.GroupLayout jPnlDefenseLayout = new javax.swing.GroupLayout(jPnlDefense);
        jPnlDefense.setLayout(jPnlDefenseLayout);
        jPnlDefenseLayout.setHorizontalGroup(
            jPnlDefenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
            .addGroup(jPnlDefenseLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPnlDefenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtDR, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPnlDefenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtPD, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPnlDefenseLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(jPnlDefenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18)
                    .addComponent(txtParrying, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPnlDefenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPnlDefenseLayout.createSequentialGroup()
                        .addComponent(txtBlocking, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDodging, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPnlDefenseLayout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20)))
                .addContainerGap())
        );

        jPnlDefenseLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtDR, txtPD});

        jPnlDefenseLayout.setVerticalGroup(
            jPnlDefenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlDefenseLayout.createSequentialGroup()
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPnlDefenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPnlDefenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPnlDefenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtParrying, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBlocking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDodging, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPnlDefenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        txtName.setFont(txtName.getFont().deriveFont(txtName.getFont().getStyle() | java.awt.Font.BOLD));
        txtName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtName.setText("HERO");
        txtName.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel5.getFont().getSize()-1));
        jLabel5.setLabelFor(txtHT);
        jLabel5.setText("HT");

        jLabel4.setFont(jLabel4.getFont().deriveFont(jLabel4.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel4.getFont().getSize()-1));
        jLabel4.setLabelFor(txtInt);
        jLabel4.setText("IQ");

        jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel3.getFont().getSize()-1));
        jLabel3.setLabelFor(txtDex);
        jLabel3.setText("DX");

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel2.getFont().getSize()-1));
        jLabel2.setLabelFor(txtStr);
        jLabel2.setText("ST");

        txtHT.setEditable(false);
        txtHT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtHT.setText("10");
        txtHT.setToolTipText("Health");
        txtHT.setFocusable(false);

        txtDex.setEditable(false);
        txtDex.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDex.setText("10");
        txtDex.setToolTipText("Intelligence");
        txtDex.setFocusable(false);

        txtInt.setEditable(false);
        txtInt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtInt.setText("10");
        txtInt.setToolTipText("Dexterity");
        txtInt.setFocusable(false);

        txtStr.setEditable(false);
        txtStr.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtStr.setText("10");
        txtStr.setToolTipText("Strength");
        txtStr.setFocusable(false);

        jLabel7.setFont(jLabel7.getFont().deriveFont(jLabel7.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel7.getFont().getSize()-1));
        jLabel7.setText("SPD");
        jLabel7.setToolTipText("Speed");

        jLabel8.setFont(jLabel8.getFont().deriveFont(jLabel8.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel8.getFont().getSize()-1));
        jLabel8.setText("FTG");

        jLabel9.setFont(jLabel9.getFont().deriveFont(jLabel9.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel9.getFont().getSize()-1));
        jLabel9.setText("HP");

        jLabel6.setFont(jLabel6.getFont().deriveFont(jLabel6.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel6.getFont().getSize()-1));
        jLabel6.setLabelFor(txtMov);
        jLabel6.setText("MVM");
        jLabel6.setToolTipText("Movement");

        txtMov.setEditable(false);
        txtMov.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMov.setText("16/16");
        txtMov.setToolTipText("Movement");
        txtMov.setFocusable(false);

        txtSpd.setEditable(false);
        txtSpd.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSpd.setText("16/16");
        txtSpd.setToolTipText("Speed");
        txtSpd.setFocusable(false);

        txtHP.setEditable(false);
        txtHP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtHP.setText("16/16");
        txtHP.setToolTipText("Current Hit Points");
        txtHP.setFocusable(false);

        txtFatigue.setEditable(false);
        txtFatigue.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtFatigue.setText("16/16");
        txtFatigue.setToolTipText("Fatigue");
        txtFatigue.setFocusable(false);

        javax.swing.GroupLayout pnlStatsLayout = new javax.swing.GroupLayout(pnlStats);
        pnlStats.setLayout(pnlStatsLayout);
        pnlStatsLayout.setHorizontalGroup(
            pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlStatsLayout.createSequentialGroup()
                .addContainerGap(75, Short.MAX_VALUE)
                .addGroup(pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtMov)
                    .addComponent(txtSpd)
                    .addComponent(txtHP)
                    .addComponent(txtFatigue, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                .addGap(23, 23, 23))
            .addGroup(pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlStatsLayout.createSequentialGroup()
                    .addGap(13, 13, 13)
                    .addGroup(pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addComponent(jLabel2))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtHT, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtDex, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtInt, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtStr, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(99, Short.MAX_VALUE)))
        );

        pnlStatsLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtDex, txtHT, txtInt, txtStr});

        pnlStatsLayout.setVerticalGroup(
            pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatsLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(7, 7, 7)
                .addGroup(pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSpd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(5, 5, 5)
                .addGroup(pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFatigue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(7, 7, 7)
                .addGroup(pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addContainerGap(26, Short.MAX_VALUE))
            .addGroup(pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlStatsLayout.createSequentialGroup()
                    .addGap(24, 24, 24)
                    .addGroup(pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtStr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtInt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(6, 6, 6)
                    .addGroup(pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(txtDex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(5, 5, 5)
                    .addGroup(pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txtHT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(25, Short.MAX_VALUE)))
        );

        jScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jLstInnates.setBackground(new java.awt.Color(204, 255, 204));
        jLstInnates.setFont(jLstInnates.getFont().deriveFont(jLstInnates.getFont().getSize()-1f));
        jLstInnates.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jLstInnates.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jLstInnates.setFocusable(false);
        jLstInnates.setSelectionBackground(new java.awt.Color(204, 255, 204));
        jLstInnates.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jScrollPane.setViewportView(jLstInnates);

        jLabel1.setFont(jLabel1.getFont().deriveFont((jLabel1.getFont().getStyle() | java.awt.Font.ITALIC) & ~java.awt.Font.BOLD, jLabel1.getFont().getSize()-1));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setLabelFor(jScrollPane);
        jLabel1.setText("Innates");

        javax.swing.GroupLayout pnlInnatesLayout = new javax.swing.GroupLayout(pnlInnates);
        pnlInnates.setLayout(pnlInnatesLayout);
        pnlInnatesLayout.setHorizontalGroup(
            pnlInnatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInnatesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
            .addComponent(jScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
        );
        pnlInnatesLayout.setVerticalGroup(
            pnlInnatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInnatesLayout.createSequentialGroup()
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jPnlAttack.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel10.setFont(jLabel10.getFont().deriveFont(jLabel10.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Attack");

        txtThrust.setEditable(false);
        txtThrust.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtThrust.setText("2d-1");
        txtThrust.setFocusable(false);

        txtSwing.setEditable(false);
        txtSwing.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSwing.setText("2d-1");
        txtSwing.setFocusable(false);

        txtKick.setEditable(false);
        txtKick.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtKick.setText("2d-1");
        txtKick.setFocusable(false);

        jLabel12.setFont(jLabel12.getFont().deriveFont(jLabel12.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel12.getFont().getSize()-1));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setLabelFor(txtThrust);
        jLabel12.setText("Thrust");

        jLabel13.setFont(jLabel13.getFont().deriveFont(jLabel13.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel13.getFont().getSize()-1));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setLabelFor(txtSwing);
        jLabel13.setText("Swing");

        jLabel14.setFont(jLabel14.getFont().deriveFont(jLabel14.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel14.getFont().getSize()-1));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setLabelFor(txtKick);
        jLabel14.setText("Kick");

        jCboAllAttack.setFont(jCboAllAttack.getFont().deriveFont(jCboAllAttack.getFont().getStyle() & ~java.awt.Font.BOLD, jCboAllAttack.getFont().getSize()-1));
        jCboAllAttack.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jCboAllAttack.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCboAllAttackItemStateChanged(evt);
            }
        });

        jLabel15.setFont(jLabel15.getFont().deriveFont(jLabel15.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("All Out Attack");

        javax.swing.GroupLayout jPnlAttackLayout = new javax.swing.GroupLayout(jPnlAttack);
        jPnlAttack.setLayout(jPnlAttackLayout);
        jPnlAttackLayout.setHorizontalGroup(
            jPnlAttackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlAttackLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPnlAttackLayout.createSequentialGroup()
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPnlAttackLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnlAttackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtThrust, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPnlAttackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(txtSwing, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPnlAttackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(txtKick, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addGap(18, 18, 18))
            .addGroup(jPnlAttackLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCboAllAttack, 0, 133, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );
        jPnlAttackLayout.setVerticalGroup(
            jPnlAttackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlAttackLayout.createSequentialGroup()
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addGroup(jPnlAttackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtThrust, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSwing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPnlAttackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel12))
                .addGap(33, 33, 33)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCboAllAttack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPnlDefense, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pnlStats, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlInnates, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPnlAttack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelPicture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPnlDefense, pnlStats});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlStats, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPnlDefense, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(pnlInnates, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPnlAttack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanelPicture, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    private void jCboAllAttackItemStateChanged(java.awt.event.ItemEvent evt)
    {// GEN-FIRST:event_jCboAllAttackItemStateChanged
        this.changeAttack();
    }// GEN-LAST:event_jCboAllAttackItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jCboAllAttack;
    private javax.swing.JLabel jImagePC;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jLstInnates;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelPicture;
    private javax.swing.JPanel jPnlAttack;
    private javax.swing.JPanel jPnlDefense;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTextField jTextAge;
    private javax.swing.JTextField jTextHeight;
    private javax.swing.JTextField jTextSex;
    private javax.swing.JTextField jTextWeight;
    private javax.swing.JPanel pnlInnates;
    private javax.swing.JPanel pnlSexAge;
    private javax.swing.JPanel pnlStats;
    private javax.swing.JTextField txtBlocking;
    private javax.swing.JTextField txtCharPoints;
    private javax.swing.JTextField txtCurrentCharPoints;
    private javax.swing.JTextField txtDR;
    private javax.swing.JTextField txtDex;
    private javax.swing.JTextField txtDodging;
    private javax.swing.JTextField txtFatigue;
    private javax.swing.JTextField txtHP;
    private javax.swing.JTextField txtHT;
    private javax.swing.JTextField txtInt;
    private javax.swing.JTextField txtKick;
    private javax.swing.JTextField txtMov;
    private javax.swing.JLabel txtName;
    private javax.swing.JTextField txtPD;
    private javax.swing.JTextField txtParrying;
    private javax.swing.JTextField txtSpd;
    private javax.swing.JTextField txtStr;
    private javax.swing.JTextField txtSwing;
    private javax.swing.JTextField txtThrust;
    // End of variables declaration//GEN-END:variables
}
