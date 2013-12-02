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

import java.util.AbstractList;
import javax.swing.JOptionPane;
import net.littlelite.jurpe.dialogs.Answer;
import net.littlelite.jurpe.dialogs.IDialogLogic;
import net.littlelite.jurpe.game.MagesGuild;
import net.littlelite.jurpedemo.JurpeDemoConfig;
import net.littlelite.jurpedemo.frames.JurpeMain;
import net.littlelite.jurpedemo.frames.TabbedPanel;
import net.littlelite.jurpedemo.logic.MagesGuildLogic;
import net.littlelite.jurpedemo.resources.GameStrings;

/**
 * Mages Guild Tab Panel
 */
public class JPnlMages extends JPanelTemplate
{
    // UID
    private static final long serialVersionUID = 1L;
    private MagesGuild guild;

    /**
     * Create a new Mages Guild tab panel
     * @param gui Parent component
     */
    public JPnlMages(JurpeMain gui)
    {
        super(gui);
        initComponents();
        this.setMagePicture();
    }
    
    @Override
    protected void formComponentShown(java.awt.event.ComponentEvent evt)
    {
        this.setupDialog();
    }

    private void setMagePicture()
    {
        this.lblMagePicture.setIcon(this.rf.getResourceAsImage(JurpeDemoConfig.MAGESJPG));
    }

    private void setupDialog()
    {
        this.btnAnswer.setEnabled(true);
        this.radAnswer1.setVisible(true);
        this.radAnswer2.setVisible(true);
        this.radAnswer3.setVisible(true);
        
        this.guild = this.theSystem.getMagesGuild();
        this.guild.initializeGuild();
        String question = this.guild.getCurrentQuestion();
        this.lblQuestion.setText(question);
        this.theSystem.getLog().addDetail("A mage asks you: " + question);

        AbstractList<Answer> answers = this.guild.getCurrentAnswers();
        String answer1 = answers.get(0).getAnswer();
        String answer2 = answers.get(1).getAnswer();
        String answer3 = answers.get(2).getAnswer();

        this.radAnswer1.setText(answer1);
        this.radAnswer2.setText(answer2);
        this.radAnswer3.setText(answer3);
        
        this.clearAnswers();
    }

    private String manageAnswer()
    {
        String response = null;
        int selectedAnswer = this.getAnswer();
        IDialogLogic idl = new MagesGuildLogic(this.theSystem.getPC());

        if (selectedAnswer < 0)
        {
            this.noAnswerWarning();
            this.theSystem.getLog().addDetail("No answer...");
            return null;
        }

        Answer theAnswer = this.guild.getCurrentAnswers().get(selectedAnswer);
        this.theSystem.getLog().addDetail("Your answer: " + theAnswer.getAnswer());
        
        String answerCode = theAnswer.getKey();        
        if (answerCode.equals("X"))
        {
            this.leaving();
        }
        else
        {
            response = this.guild.processAnswer(theAnswer, idl);
            this.setupDialog();
        }
              
        return response;
    }

    private void clearAnswers()
    {
        this.radAnswer1.setSelected(false);
        this.radAnswer2.setSelected(false);
        this.radAnswer2.setSelected(false);
    }

    private int getAnswer()
    {
        int answer = -1;

        if (this.radAnswer1.isSelected())
        {
            answer = 0;
        }
        else if (this.radAnswer2.isSelected())
        {
            answer = 1;
        }
        else if (this.radAnswer3.isSelected())
        {
            answer = 2;
        }

        return answer;
    }

    private void noAnswerWarning()
    {
        JOptionPane.showMessageDialog(this, "Select an answer!", "No answer", JOptionPane.WARNING_MESSAGE);
    }
    
    private void leaving()
    {
        this.btnAnswer.setEnabled(false);
        this.radAnswer1.setVisible(false);
        this.radAnswer2.setVisible(false);
        this.radAnswer3.setVisible(false);
        this.lblQuestion.setText(GameStrings.FAREWELL);
    }

    private void exit()
    {
        this.theSystem.getLog().addEntry(GameStrings.LEAVEMG);
        parentGUI.showPanel(TabbedPanel.TAB_GAME);
        parentGUI.restorePanels();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        answersGroup = new javax.swing.ButtonGroup();
        pnlImage = new javax.swing.JPanel();
        lblMagePicture = new javax.swing.JLabel();
        pnlQuestions = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lblQuestion = new javax.swing.JTextArea();
        pnlAnswers = new javax.swing.JPanel();
        radAnswer1 = new javax.swing.JRadioButton();
        radAnswer2 = new javax.swing.JRadioButton();
        radAnswer3 = new javax.swing.JRadioButton();
        btnExit = new javax.swing.JButton();
        btnAnswer = new javax.swing.JButton();

        pnlImage.setBackground(new java.awt.Color(204, 204, 204));

        lblMagePicture.setBackground(new java.awt.Color(255, 153, 153));

        javax.swing.GroupLayout pnlImageLayout = new javax.swing.GroupLayout(pnlImage);
        pnlImage.setLayout(pnlImageLayout);
        pnlImageLayout.setHorizontalGroup(
            pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblMagePicture, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
        );
        pnlImageLayout.setVerticalGroup(
            pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblMagePicture, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
        );

        pnlQuestions.setBackground(new java.awt.Color(204, 204, 255));
        pnlQuestions.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlQuestions.setFont(pnlQuestions.getFont());

        jScrollPane1.setBorder(null);
        jScrollPane1.setFocusable(false);
        jScrollPane1.setFont(jScrollPane1.getFont());

        lblQuestion.setBackground(new java.awt.Color(204, 204, 255));
        lblQuestion.setColumns(20);
        lblQuestion.setEditable(false);
        lblQuestion.setFont(lblQuestion.getFont().deriveFont(lblQuestion.getFont().getSize()-1f));
        lblQuestion.setLineWrap(true);
        lblQuestion.setRows(5);
        lblQuestion.setWrapStyleWord(true);
        lblQuestion.setBorder(null);
        lblQuestion.setDisabledTextColor(new java.awt.Color(204, 204, 255));
        lblQuestion.setFocusable(false);
        lblQuestion.setMaximumSize(new java.awt.Dimension(160, 65));
        lblQuestion.setMinimumSize(new java.awt.Dimension(160, 65));
        lblQuestion.setRequestFocusEnabled(false);
        lblQuestion.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setViewportView(lblQuestion);

        javax.swing.GroupLayout pnlQuestionsLayout = new javax.swing.GroupLayout(pnlQuestions);
        pnlQuestions.setLayout(pnlQuestionsLayout);
        pnlQuestionsLayout.setHorizontalGroup(
            pnlQuestionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlQuestionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlQuestionsLayout.setVerticalGroup(
            pnlQuestionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlQuestionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        answersGroup.add(radAnswer1);
        radAnswer1.setText("Answer1");

        answersGroup.add(radAnswer2);
        radAnswer2.setText("Answer2");

        answersGroup.add(radAnswer3);
        radAnswer3.setText("Answer3");

        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        btnAnswer.setText("Answer");
        btnAnswer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnswerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAnswersLayout = new javax.swing.GroupLayout(pnlAnswers);
        pnlAnswers.setLayout(pnlAnswersLayout);
        pnlAnswersLayout.setHorizontalGroup(
            pnlAnswersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAnswersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAnswersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radAnswer1, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                    .addComponent(radAnswer2, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                    .addComponent(radAnswer3, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAnswersLayout.createSequentialGroup()
                        .addComponent(btnAnswer, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlAnswersLayout.setVerticalGroup(
            pnlAnswersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAnswersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radAnswer1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radAnswer2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radAnswer3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(pnlAnswersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExit)
                    .addComponent(btnAnswer))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlQuestions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlAnswers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlQuestions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlAnswers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        this.exit();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnAnswerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnswerActionPerformed
        this.manageAnswer();
    }//GEN-LAST:event_btnAnswerActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup answersGroup;
    private javax.swing.JButton btnAnswer;
    private javax.swing.JButton btnExit;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblMagePicture;
    private javax.swing.JTextArea lblQuestion;
    private javax.swing.JPanel pnlAnswers;
    private javax.swing.JPanel pnlImage;
    private javax.swing.JPanel pnlQuestions;
    private javax.swing.JRadioButton radAnswer1;
    private javax.swing.JRadioButton radAnswer2;
    private javax.swing.JRadioButton radAnswer3;
    // End of variables declaration//GEN-END:variables
}
