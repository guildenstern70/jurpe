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
import java.util.AbstractList;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import net.littlelite.jurpe.characters.CharacterAttributes;
import net.littlelite.jurpe.characters.Innate;
import net.littlelite.jurpe.containers.Innates;
import net.littlelite.jurpe.system.Config;

/**
 *
 *
 */
public final class PanelWizard4 extends AbstractWizardPanel
{

    private static final long serialVersionUID = 3317L;
    private DefaultListModel listModel;
    private AbstractList<Innate> tmpInnates;

    /** Creates new form PanelWizard4 */
    public PanelWizard4(WizardController cc)
    {
        super(cc);
        this.tmpInnates = new ArrayList<Innate>();
        initComponents();
        this.setName("Choose inborn skills");
        this.initializeList();
    }

    @Override
    public void syncCharacter()
    {
        this.jTxtCP.setText(this.getCharacterPoints());
    }

    @Override
    public void updateCharacter()
    {
        CharacterAttributes ca = this.wizard.getCharacterAttrs();
        ca.setInnates(this.tmpInnates);
    }
    
    private int currentCharacterPoints()
    {
        int currentInnatesPoints = 0;
        for (Innate i : this.tmpInnates)
        {
            currentInnatesPoints += i.getPoints();
        }
        int currentCharacterPoints = this.wizard.getCharacterAttrs().getCharacterPoints();
        currentCharacterPoints += currentInnatesPoints;
        
        return currentCharacterPoints;
    }

    private void updateCharacterPoints()
    {
        this.jTxtCP.setText(this.getCharacterPoints(this.currentCharacterPoints()));
    }

    private void initializeList()
    {
        // Innates cell renderer
        this.jLstYourInnates.setModel(new DefaultListModel());
        this.jLstYourInnates.setCellRenderer(new SimpleSkillsCellRenderer());

        // Read innates
        Innates innates = new Innates();
        this.listModel = new DefaultListModel();
        Object[] innatesItem = innates.getAdvantages();

        for (int j = 0; j < innatesItem.length; j++)
        {
            this.listModel.addElement(innatesItem[j]);
        }

        this.jLstAvailableInnates = new JList(this.listModel);
        this.jLstAvailableInnates.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.getViewport().add(this.jLstAvailableInnates, null);
        jLstAvailableInnates.addListSelectionListener(new javax.swing.event.ListSelectionListener()
        {

            public void valueChanged(ListSelectionEvent e)
            {
                jLstAvailableInnates_valueChanged(e);
            }
        });

        this.jLstYourInnates.addListSelectionListener(new javax.swing.event.ListSelectionListener()
        {

            public void valueChanged(ListSelectionEvent e)
            {
                jLstYourInnates_valueChanged(e);
            }
        });
    }

    private void removeSkill()
    {
        Innate selected = (Innate) this.jLstYourInnates.getSelectedValue();
        this.tmpInnates.remove(selected);
        this.listModel.addElement(selected);
        this.jLstYourInnates.setListData(this.tmpInnates.toArray());
        this.updateCharacterPoints();
    }

    private void addSkill()
    {
        
        // Get selected skill
        int index = this.jLstAvailableInnates.getSelectedIndex();
        Innate selected = (Innate) this.jLstAvailableInnates.getSelectedValue();
        
        // See if you can afford it
        int characterPoints = this.currentCharacterPoints() + selected.getPoints();
        if (characterPoints > Config.AVAILABLE_POINTS)
        {
            this.showCharPointsLimitExceed();
            return;
        }
        
        this.tmpInnates.add(selected);
        this.setCharacterInnatesList();
        this.listModel.removeElementAt(index);

        
        // Update left list
        int size = this.listModel.getSize();
        if (size == 0)
        { // Nobody's left, disable firing.
            this.jBtnAdd.setEnabled(false);
        }
        else
        { // Select an index.
            if (index == this.listModel.getSize())
            {
                // removed item in last position
                index--;
            }
            this.jLstAvailableInnates.setSelectedIndex(index);
            this.jLstAvailableInnates.ensureIndexIsVisible(index);
        }
        this.updateCharacterPoints();
    }

    private void setCharacterInnatesList()
    {
        DefaultListModel model = (DefaultListModel) this.jLstYourInnates.getModel();
        model.clear();
        for (Innate innate : this.tmpInnates)
        {
            model.addElement(innate);
        }
    }

    private void showInnateDescription(JList innatesList)
    {
        try
        {
            Innate selected = (Innate) innatesList.getSelectedValue();
            if (selected != null)
            {
                this.jTxtDescription.setText(selected.getDescription());
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Single click on available innate list
     * 
     * @param e
     */
    private void jLstAvailableInnates_valueChanged(ListSelectionEvent e)
    {
        this.showInnateDescription(this.jLstAvailableInnates);
    }

    /**
     * Single click on available innate list
     * 
     * @param e
     */
    private void jLstYourInnates_valueChanged(ListSelectionEvent e)
    {
        this.showInnateDescription(this.jLstYourInnates);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTxtCP = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabelTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLstAvailableInnates = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jLstYourInnates = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTxtDescription = new javax.swing.JTextArea();
        jBtnAdd = new javax.swing.JButton();
        jBtnRemove = new javax.swing.JButton();

        jTxtCP.setEditable(false);
        jTxtCP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTxtCP.setText("100/100");

        jLabel1.setText("Character Points:");

        jLabelTitle.setFont(jLabelTitle.getFont().deriveFont(jLabelTitle.getFont().getStyle() | java.awt.Font.BOLD));
        jLabelTitle.setText("Determine inborn skills");

        jScrollPane1.setViewportView(jLstAvailableInnates);

        jScrollPane2.setViewportView(jLstYourInnates);

        jTxtDescription.setColumns(20);
        jTxtDescription.setFont(jTxtDescription.getFont().deriveFont(jTxtDescription.getFont().getSize()-1f));
        jTxtDescription.setLineWrap(true);
        jTxtDescription.setRows(5);
        jTxtDescription.setWrapStyleWord(true);
        jScrollPane3.setViewportView(jTxtDescription);

        jBtnAdd.setFont(jBtnAdd.getFont().deriveFont(jBtnAdd.getFont().getStyle() | java.awt.Font.BOLD));
        jBtnAdd.setText("->");
        jBtnAdd.setToolTipText("Add skill");
        jBtnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnAddActionPerformed(evt);
            }
        });

        jBtnRemove.setFont(jBtnRemove.getFont().deriveFont(jBtnRemove.getFont().getStyle() | java.awt.Font.BOLD));
        jBtnRemove.setText("<-");
        jBtnRemove.setToolTipText("Remove skill");
        jBtnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnRemoveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTxtCP, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jBtnRemove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBtnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTitle)
                    .addComponent(jTxtCP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jBtnAdd)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnRemove)
                        .addGap(64, 64, 64))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                                .addGap(6, 6, 6)))))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    private void jBtnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnAddActionPerformed
        this.addSkill();
    }//GEN-LAST:event_jBtnAddActionPerformed

    private void jBtnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnRemoveActionPerformed
        this.removeSkill();
    }//GEN-LAST:event_jBtnRemoveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnAdd;
    private javax.swing.JButton jBtnRemove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JList jLstAvailableInnates;
    private javax.swing.JList jLstYourInnates;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTxtCP;
    private javax.swing.JTextArea jTxtDescription;
    // End of variables declaration//GEN-END:variables
    class SimpleSkillsCellRenderer extends JLabel implements ListCellRenderer
    {

        private static final long serialVersionUID = 3317L;
        protected Border noFocusBorder;

        public SimpleSkillsCellRenderer()
        {
            super();
            // Don't paint behind the component
            setOpaque(true);
            noFocusBorder = new EmptyBorder(1, 1, 1, 1);
            setBorder(noFocusBorder);
        }

        // Set the attributes of the 
        //class and return a reference
        public Component getListCellRendererComponent(JList list,
                Object value, // value to display
                int index, // cell index
                boolean isSelected, // is selected
                boolean cellHasFocus)  // cell has focus?
        {
            this.setText(((Innate) value).getName());

            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

            setFont(list.getFont());
            setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);

            return this;
        }
    }
}
