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
package net.littlelite.jurpedemo.wizards;

import net.littlelite.jurpe.characters.CharacterAttributes;
import net.littlelite.jurpe.characters.PrimaryStats;
import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.JurpeUtils;

/**
 *
 */
public final class PanelWizard3 extends AbstractWizardPanel
{

    private static final long serialVersionUID = 3317L;
    
    private boolean checkCharactersPointsLimit = true;

    /** Creates new form PanelWizard3 */
    public PanelWizard3(WizardController cc)
    {
        super(cc);
        initComponents();
        this.setName("Choose basic attributes");
    }

    @Override
    public void syncCharacter()
    {
        CharacterAttributes ca = this.wizard.getCharacterAttrs();
        this.setCharacterAttributes(ca);
        this.refresh();
    }

    @Override
    public void updateCharacter()
    {
        CharacterAttributes ca = this.wizard.getCharacterAttrs();
        PrimaryStats ps = ca.primaryStats();
        // Set initial HP = HT
        Integer hti = (Integer) this.txtCos.getValue();
        ps.setHitPoints(hti.intValue());
        ps.restoreHitPoints();
    }

    private void rollDice()
    {
        this.checkCharactersPointsLimit = false;  // disable check character points
        
        CharacterAttributes ca = this.wizard.getCharacterAttrs();
        ca.setPrimaryStats(JurpeUtils.generateStats(Config.AVAILABLE_POINTS - 10));
        this.setCharacterAttributes(ca);
        this.updateCharacter();
        this.refresh();
        
        this.checkCharactersPointsLimit = true;  // enable check character points
    }
    
    private void setCharacterAttributes(CharacterAttributes ca)
    {
        this.txtFor.setValue(new Integer(ca.primaryStats().getST()));
        this.txtDes.setValue(new Integer(ca.primaryStats().getDX()));
        this.txtInt.setValue(new Integer(ca.primaryStats().getIQ()));
        this.txtCos.setValue(new Integer(ca.primaryStats().getHT()));
    }

    private void refresh()
    {
        CharacterAttributes ca = this.wizard.getCharacterAttrs();
        this.jTxtPCPoints.setText(this.getCharacterPoints());

        int st = ca.primaryStats().getST();
        int dx = ca.primaryStats().getDX();
        int iq = ca.primaryStats().getIQ();
        int ht = ca.primaryStats().getHT();

        this.jTxtP_ST.setText(String.valueOf(JurpeUtils.scoreValue(st)));
        this.jTxtP_DX.setText(String.valueOf(JurpeUtils.scoreValue(dx)));
        this.jTxtP_IQ.setText(String.valueOf(JurpeUtils.scoreValue(iq)));
        this.jTxtP_HT.setText(String.valueOf(JurpeUtils.scoreValue(ht)));

        this.txtVel.setText(String.valueOf(ca.primaryStats().getSpeed()));
        this.txtMov.setText(String.valueOf(ca.primaryStats().getMove()));
        this.txtHP.setText(String.valueOf(ca.primaryStats().getInitialHitPoints()));
    }

    private int attributesValue(int st, int dx, int iq, int ht)
    {
        int stP = JurpeUtils.scoreValue(st);
        int dxP = JurpeUtils.scoreValue(dx);
        int iqP = JurpeUtils.scoreValue(iq);
        int hqP = JurpeUtils.scoreValue(ht);

        return (stP + dxP + iqP + hqP);
    }

    private boolean checkAttributesValue()
    {

        Integer stV = (Integer) this.txtFor.getValue();
        Integer dxV = (Integer) this.txtDes.getValue();
        Integer iqV = (Integer) this.txtInt.getValue();
        Integer htV = (Integer) this.txtCos.getValue();

        if (this.attributesValue(stV.intValue(), dxV.intValue(), iqV.intValue(), htV.intValue()) <= Config.AVAILABLE_POINTS)
        {
            return true;
        }

        return false;
    }

    private void modifyValue(String what, int valueNumber)
    {
        if (!this.checkAttributesValue() && this.checkCharactersPointsLimit)
        {
            this.showCharPointsLimitExceed();
            return;
        }

        CharacterAttributes ca = this.wizard.getCharacterAttrs();

        if (what.equals("ST"))
        {
            ca.primaryStats().setST(valueNumber);
        }
        else if (what.equals("DX"))
        {
            ca.primaryStats().setDX(valueNumber);
        }
        else if (what.equals("IQ"))
        {
            ca.primaryStats().setIQ(valueNumber);
        }
        else if (what.equals("HT"))
        {
            ca.primaryStats().setHT(valueNumber);
        }

        this.refresh();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlStatistics = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtFor = new javax.swing.JSpinner();
        txtDes = new javax.swing.JSpinner();
        txtInt = new javax.swing.JSpinner();
        txtCos = new javax.swing.JSpinner();
        jTxtP_ST = new javax.swing.JTextField();
        jTxtP_DX = new javax.swing.JTextField();
        jTxtP_IQ = new javax.swing.JTextField();
        jTxtP_HT = new javax.swing.JTextField();
        jBtnRoll = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtVel = new javax.swing.JTextField();
        txtMov = new javax.swing.JTextField();
        txtHP = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTxtPCPoints = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        pnlStatistics.setBackground(new java.awt.Color(204, 255, 204));

        jLabel6.setFont(jLabel6.getFont().deriveFont(jLabel6.getFont().getStyle() | java.awt.Font.BOLD, jLabel6.getFont().getSize()+1));
        jLabel6.setText("ST");

        jLabel7.setFont(jLabel7.getFont().deriveFont(jLabel7.getFont().getStyle() | java.awt.Font.BOLD, jLabel7.getFont().getSize()+1));
        jLabel7.setText("DX");

        jLabel8.setFont(jLabel8.getFont().deriveFont(jLabel8.getFont().getStyle() | java.awt.Font.BOLD, jLabel8.getFont().getSize()+1));
        jLabel8.setText("IQ");

        jLabel9.setFont(jLabel9.getFont().deriveFont(jLabel9.getFont().getStyle() | java.awt.Font.BOLD, jLabel9.getFont().getSize()+1));
        jLabel9.setText("HT");

        txtFor.setFont(txtFor.getFont().deriveFont(txtFor.getFont().getSize()+1f));
        txtFor.setModel(new javax.swing.SpinnerNumberModel(10, 1, 30, 1));
        txtFor.setEditor(new javax.swing.JSpinner.NumberEditor(txtFor, ""));
        txtFor.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                txtForStateChanged(evt);
            }
        });

        txtDes.setFont(txtDes.getFont().deriveFont(txtDes.getFont().getSize()+1f));
        txtDes.setModel(new javax.swing.SpinnerNumberModel(10, 1, 30, 1));
        txtDes.setEditor(new javax.swing.JSpinner.NumberEditor(txtDes, ""));
        txtDes.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                txtDesStateChanged(evt);
            }
        });

        txtInt.setFont(txtInt.getFont().deriveFont(txtInt.getFont().getSize()+1f));
        txtInt.setModel(new javax.swing.SpinnerNumberModel(10, 1, 30, 1));
        txtInt.setEditor(new javax.swing.JSpinner.NumberEditor(txtInt, ""));
        txtInt.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                txtIntStateChanged(evt);
            }
        });

        txtCos.setFont(txtCos.getFont().deriveFont(txtCos.getFont().getSize()+1f));
        txtCos.setModel(new javax.swing.SpinnerNumberModel(10, 1, 30, 1));
        txtCos.setEditor(new javax.swing.JSpinner.NumberEditor(txtCos, ""));
        txtCos.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                txtCosStateChanged(evt);
            }
        });

        jTxtP_ST.setEditable(false);
        jTxtP_ST.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTxtP_ST.setText("0");

        jTxtP_DX.setEditable(false);
        jTxtP_DX.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTxtP_DX.setText("0");

        jTxtP_IQ.setEditable(false);
        jTxtP_IQ.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTxtP_IQ.setText("0");

        jTxtP_HT.setEditable(false);
        jTxtP_HT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTxtP_HT.setText("0");

        jBtnRoll.setText("Roll");
        jBtnRoll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnRollActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlStatisticsLayout = new javax.swing.GroupLayout(pnlStatistics);
        pnlStatistics.setLayout(pnlStatisticsLayout);
        pnlStatisticsLayout.setHorizontalGroup(
            pnlStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatisticsLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(pnlStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jBtnRoll, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                    .addGroup(pnlStatisticsLayout.createSequentialGroup()
                        .addGroup(pnlStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addGap(10, 10, 10)
                        .addGroup(pnlStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlStatisticsLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCos, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlStatisticsLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtInt, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
                            .addComponent(txtDes, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                            .addGroup(pnlStatisticsLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFor, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTxtP_ST, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                            .addComponent(jTxtP_DX)
                            .addComponent(jTxtP_IQ)
                            .addComponent(jTxtP_HT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(41, 41, 41))
        );

        pnlStatisticsLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTxtP_DX, jTxtP_HT, jTxtP_IQ, jTxtP_ST});

        pnlStatisticsLayout.setVerticalGroup(
            pnlStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatisticsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTxtP_ST, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtFor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTxtP_DX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTxtP_IQ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtCos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtP_HT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(jBtnRoll)
                .addContainerGap())
        );

        pnlStatisticsLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtCos, txtDes, txtFor, txtInt});

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        txtVel.setEditable(false);
        txtVel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtVel.setText("0");

        txtMov.setEditable(false);
        txtMov.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMov.setText("0");

        txtHP.setEditable(false);
        txtHP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtHP.setText("0");

        jLabel2.setForeground(new java.awt.Color(204, 255, 255));
        jLabel2.setText("SPEED");

        jLabel4.setForeground(new java.awt.Color(204, 255, 255));
        jLabel4.setText("MOVEMENT");

        jLabel5.setForeground(new java.awt.Color(204, 255, 255));
        jLabel5.setText("HIT POINTS");

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("These attributes grant:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(96, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtHP, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMov, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtVel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtHP, txtMov, txtVel});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtVel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtMov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(71, Short.MAX_VALUE))
        );

        jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel3.setText("Choose basic attributes ");

        jTxtPCPoints.setEditable(false);
        jTxtPCPoints.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTxtPCPoints.setText("100/100");

        jLabel1.setText("Character Points:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlStatistics, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTxtPCPoints, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTxtPCPoints, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlStatistics, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    private void txtForStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_txtForStateChanged
        Integer stval = (Integer) this.txtFor.getValue();
        this.modifyValue("ST", stval.intValue());
    }//GEN-LAST:event_txtForStateChanged

    private void txtDesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_txtDesStateChanged
        Integer stval = (Integer) this.txtDes.getValue();
        this.modifyValue("DX", stval.intValue());
    }//GEN-LAST:event_txtDesStateChanged

    private void txtIntStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_txtIntStateChanged
        Integer stval = (Integer) this.txtInt.getValue();
        this.modifyValue("IQ", stval.intValue());
    }//GEN-LAST:event_txtIntStateChanged

    private void txtCosStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_txtCosStateChanged
        Integer stval = (Integer) this.txtCos.getValue();
        this.modifyValue("HT", stval.intValue());
    }//GEN-LAST:event_txtCosStateChanged

    private void jBtnRollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnRollActionPerformed
        this.rollDice();
    }//GEN-LAST:event_jBtnRollActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnRoll;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTxtPCPoints;
    private javax.swing.JTextField jTxtP_DX;
    private javax.swing.JTextField jTxtP_HT;
    private javax.swing.JTextField jTxtP_IQ;
    private javax.swing.JTextField jTxtP_ST;
    private javax.swing.JPanel pnlStatistics;
    private javax.swing.JSpinner txtCos;
    private javax.swing.JSpinner txtDes;
    private javax.swing.JSpinner txtFor;
    private javax.swing.JTextField txtHP;
    private javax.swing.JSpinner txtInt;
    private javax.swing.JTextField txtMov;
    private javax.swing.JTextField txtVel;
    // End of variables declaration//GEN-END:variables
}
