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

import java.awt.Component;
import java.awt.Container;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.LayoutFocusTraversalPolicy;
import net.littlelite.jurpe.characters.CharacterAttributes;
import net.littlelite.jurpe.system.generation.Dice;
import net.littlelite.utils.Conversion;

/**
 *
 */
public final class PanelWizard2 extends AbstractWizardPanel
{

    private static final long serialVersionUID = 3317L;
    private static final String[] ASPECTS =
            {"Hideous", "Ugly", "Unattractive", "Average", "Handsome/Beautiful", "Very handsome/beautiful"};
    private static String weightPostfix = " kg";
    private static String heightPostfix = " cm";

    /** Creates new form PanelWizard2 */
    public PanelWizard2(WizardController cc)
    {
        super(cc);
        initComponents();
        this.setName("Set your age and physical attributes");
        this.jCboAspect.setModel(new DefaultComboBoxModel(ASPECTS));
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(new PanelWizard2FocusTraversalPolicy());
    }

    @Override
    public void syncCharacter()
    {
        CharacterAttributes ca = this.wizard.getCharacterAttrs();
        this.jTxtAge.setText(String.valueOf(ca.getAge()));
        this.jTxtHeight.setText(ca.getHeight());
        this.jTxtWeight.setText(ca.getWeight());
        this.synchAspect(ca.getAspect());
    }

    @Override
    public void updateCharacter()
    {
        CharacterAttributes ca = this.wizard.getCharacterAttrs();
        try
        {
            ca.setAge(Integer.parseInt(this.jTxtAge.getText()));
        }
        catch (NumberFormatException e)
        {
            System.err.println(e.getMessage());
            ca.setAge(18);
        }
        ca.setHeight(this.jTxtHeight.getText());
        ca.setWeight(this.jTxtWeight.getText());
        ca.setAspect((String) this.jCboAspect.getSelectedItem());
    }

    private int getNumerical(String measure)
    {
        int iSpc = measure.indexOf(" ");
        int number = 0;
        if (iSpc > 0)
        {
            String value = measure.substring(0, iSpc);
            number = Integer.parseInt(value);
        }
        return number;
    }

    private void changeMetrics()
    {
        int newHeight, newWeight;
        int oldHeight, oldWeight;

        oldHeight = this.getNumerical(this.jTxtHeight.getText());
        oldWeight = this.getNumerical(this.jTxtWeight.getText());

        if (this.jRadioMetrical.isSelected())
        {
            newHeight = Conversion.inches2cm(oldHeight);
            newWeight = Conversion.lbs2kg(oldWeight);
            weightPostfix = " kg";
            heightPostfix = " cm";
        }
        else
        {
            newHeight = Conversion.cm2inches(oldHeight);
            newWeight = Conversion.kg2lbs(oldWeight);
            weightPostfix = " lbs";
            heightPostfix = " inches";
        }

        jTxtWeight.setText(String.valueOf(newWeight) + weightPostfix);
        jTxtHeight.setText(String.valueOf(newHeight) + heightPostfix);
    }

    private void synchAspect(String pAspect)
    {
        ComboBoxModel cmod = this.jCboAspect.getModel();
        for (int k = 0; k < cmod.getSize(); k++)
        {
            if (((String) cmod.getElementAt(k)).equals(pAspect))
            {
                this.jCboAspect.setSelectedIndex(k);
                break;
            }
        }
    }

    private void setRandomValues()
    {

        Dice d = new Dice(3);
        int stdAge = 18 + d.throwDice();
        int stdWeight = 80 + this.modifier(d.throwDice(), false); // kg
        int stdHeight = 175 + this.modifier(d.throwDice(), true); // cm
        if (this.jRadioEnglish.isSelected())
        {
            stdWeight = this.kg2lbs(stdWeight);
            stdHeight = this.cm2inches(stdHeight);
            weightPostfix = " lbs";
            heightPostfix = " inches";
        }
        this.jTxtAge.setText(String.valueOf(stdAge));
        this.jTxtWeight.setText(String.valueOf(stdWeight) + weightPostfix);
        this.jTxtHeight.setText(String.valueOf(stdHeight) + heightPostfix);

        // Aspect
        int diceNumber = 1;
        try
        {
            diceNumber = Dice.roll() - 1;
        }
        catch (Exception exc) {}
        this.jCboAspect.setSelectedIndex(diceNumber);

        // Repaint
        this.repaint();

    }

    private int kg2lbs(int kg)
    {
        return Conversion.kg2lbs(kg);
    }

    private int cm2inches(int cm)
    {
        return Conversion.cm2inches(cm);
    }

    /**
     * If isHeight it returns true it' height modifier, else weight modifier
     * 
     * @return If isHeight it returns height modifier, else weight modifier
     */
    private int modifier(int dice, boolean isHeight)
    {
        int modH = 0;
        int modW = 0;

        switch (dice)
        {
            case 3:
                modH = -15;
                modW = -18;
                break;

            case 4:
                modH = -13;
                modW = -14;
                break;

            case 5:
                modH = -10;
                modW = -9;
                break;

            case 6:
                modH = -8;
                modW = -5;
                break;

            case 7:
                modH = -5;
                modW = -2;
                break;

            case 8:
                modH = -3;
                modW = -2;
                break;

            case 9:
                modH = -2;
                modW = -2;
                break;

            case 10:
                modH = 0;
                modW = 0;
                break;

            case 11:
                modH = 1;
                modW = 1;
                break;

            case 12:
            default:
                modH = 3;
                modW = 2;
                break;

            case 13:
                modH = 5;
                modW = 2;
                break;

            case 14:
                modH = 8;
                modW = 5;
                break;

            case 15:
                modH = 10;
                modW = 9;
                break;

            case 16:
                modH = 13;
                modW = 14;
                break;

            case 17:
                modH = 15;
                modW = 18;
                break;

            case 18:
                modH = 15;
                modW = 23;
                break;
            }

        if (isHeight)
        {
            return modH;
        }

        return modW;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTxtHeight = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTxtWeight = new javax.swing.JTextField();
        jRadioMetrical = new javax.swing.JRadioButton();
        jRadioEnglish = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTxtAge = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jCboAspect = new javax.swing.JComboBox();

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel1.setText("Choose age and physical attributes:");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Weight and Height"));

        jTxtHeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtHeightFocusLost(evt);
            }
        });

        jLabel3.setText("Height");

        jLabel2.setText("Weight");

        jTxtWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtWeightFocusLost(evt);
            }
        });

        buttonGroup.add(jRadioMetrical);
        jRadioMetrical.setSelected(true);
        jRadioMetrical.setText("Metrical");
        jRadioMetrical.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioMetricalActionPerformed(evt);
            }
        });

        buttonGroup.add(jRadioEnglish);
        jRadioEnglish.setText("English");
        jRadioEnglish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioEnglishActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTxtWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioMetrical)
                    .addComponent(jRadioEnglish))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jRadioMetrical)
                    .addComponent(jTxtWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jRadioEnglish)
                    .addComponent(jTxtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setBackground(new java.awt.Color(204, 255, 204));
        jButton1.setText("Random values");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Age"));

        jTxtAge.setText("18");

        jLabel4.setText("Age");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTxtAge, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTxtAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Aspect"));

        jLabel5.setText("Appearance");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jCboAspect, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCboAspect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(208, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(16, 16, 16))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setRandomValues();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jRadioMetricalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioMetricalActionPerformed
        this.changeMetrics();
    }//GEN-LAST:event_jRadioMetricalActionPerformed

    private void jRadioEnglishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioEnglishActionPerformed
        this.changeMetrics();
    }//GEN-LAST:event_jRadioEnglishActionPerformed

    private void jTxtWeightFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtWeightFocusLost
        if ((!this.jTxtWeight.getText().endsWith("kg") && (!this.jTxtWeight.getText().endsWith("lbs"))))
        {
            if (this.jRadioMetrical.isSelected())
            {
                weightPostfix = " kg";
            }
            else
            {
                weightPostfix = " lbs";
            }
            this.jTxtWeight.setText(this.jTxtWeight.getText() + weightPostfix);
        }
    }//GEN-LAST:event_jTxtWeightFocusLost

    private void jTxtHeightFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtHeightFocusLost
        if ((!this.jTxtHeight.getText().endsWith("cm") && (!this.jTxtHeight.getText().endsWith("ches"))))
        {
            if (this.jRadioMetrical.isSelected())
            {
                heightPostfix = " cm";
            }
            else
            {
                heightPostfix = " inches";
            }
            this.jTxtHeight.setText(this.jTxtHeight.getText() + heightPostfix);
        }
    }//GEN-LAST:event_jTxtHeightFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jCboAspect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioEnglish;
    private javax.swing.JRadioButton jRadioMetrical;
    private javax.swing.JTextField jTxtAge;
    private javax.swing.JTextField jTxtHeight;
    private javax.swing.JTextField jTxtWeight;
    // End of variables declaration//GEN-END:variables
    /**
     * TraversalPolicy inner class to handle focus control of tabs
     */
    class PanelWizard2FocusTraversalPolicy extends LayoutFocusTraversalPolicy
    {

        private static final long serialVersionUID = 3317L;

        @Override
        public Component getComponentAfter(Container focusCycleRoot, Component aComponent)
        {
            if (aComponent.equals(jTxtWeight))
            {
                return jTxtHeight;
            }
            else if (aComponent.equals(jTxtHeight))
            {
                return jTxtAge;
            }
            else if (aComponent.equals(jTxtAge))
            {
                return jCboAspect;
            }
            else if (aComponent.equals(jCboAspect))
            {
                return jTxtWeight;
            }
            else
            {
                return null;
            }
        } // end getComponentAfter

        @Override
        public Component getComponentBefore(Container focusCycleRoot, Component aComponent)
        {
            if (aComponent.equals(jCboAspect))
            {
                return jTxtAge;
            }
            else if (aComponent.equals(jTxtAge))
            {
                return jTxtHeight;
            }
            else if (aComponent.equals(jTxtHeight))
            {
                return jTxtWeight;
            }
            else
            {
                return null;
            }
        } // end getComponentBefore
    }
}
