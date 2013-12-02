/**
 J.U.R.P.E. @version@
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
package net.littlelite.jurpe.game;

import java.io.File;
import java.util.AbstractList;
import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.dialogs.Answer;
import net.littlelite.jurpe.dialogs.DialogManager;
import net.littlelite.jurpe.dialogs.DialogStatus;
import net.littlelite.jurpe.dialogs.DialogUnit;
import net.littlelite.jurpe.dialogs.IDialogLogic;
import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.Core;
import net.littlelite.jurpe.system.Log;
import net.littlelite.jurpe.system.resources.ResourceFinder;

/**
 * This class manages the Mages Guild dialogs
 */
public class MagesGuild
{

    private final static String DIALOG_ID = "MagesGuild";
    
    private Log log;
    private String currentUnit;
    private DialogManager dm;
    private String currentQuestion;
    private AbstractList<Answer> currentAnswers;
    private Core theSystem;
    private PCharacter player;
    private DialogStatus status;

    /**
     * Constructor
     * @param system Handle to a Core object
     * @see net.littlelite.jurpe.system.Core
     */
    public MagesGuild(Core system)
    {
        this.theSystem = system;
        this.log = system.getLog();
    }

    /**
     * Initialized dialogs
     */
    public void initializeGuild()
    {
        this.player = this.theSystem.getPC();
        this.initializeMageDialog();
        this.synchronizeUnit();
    }

    /**
     * Synchronize the dialog with the last dialog phase
     * taken by the character
     */
    public void synchronizeUnit()
    {
        if (this.status != null)
        {
            this.setCurrentUnit(this.status.getDialogPhase());
        }
    }

    /**
     * Get the current dialog unit
     * @return the current dialog unit
     */
    public String getCurrentUnit()
    {
        return currentUnit;
    }

    /**
     * Set the current dialog unit
     * @param currentUnit the current dialog unit
     */
    public void setCurrentUnit(String currentUnit)
    {
        this.currentUnit = currentUnit;
        this.loadUnit();
    }

    /**
     * Get the current question
     * @return the current question
     */
    public String getCurrentQuestion()
    {
        return this.currentQuestion;
    }

    /**
     * Get a list of the answers tied to the current question
     * @return a list of the answers tied to the current question
     */
    public AbstractList<Answer> getCurrentAnswers()
    {
        return this.currentAnswers;
    }

    /**
     * If the answer has been chosen, a next unit will be set
     * as current, and a key code will be returned to the caller.
     * @param selectedAnswer
     * @return the next unit code
     */
    public String setNextUnit(Answer selectedAnswer)
    {
        // Select next unit
        String gotoCode = selectedAnswer.getGotoCode();
        if (!gotoCode.equals("EXIT"))
        {
            this.setCurrentUnit(gotoCode);
        }

        // Communicate with caller
        return gotoCode;
    }

    /**
     * Process the answer given by the character to the current question.
     * It returns the code of the next dialog unit
     * @param theAnswer Answer answered
     * @param logic The logic that governs the choice of the next question
     * @return the code of the next dialog unit.
     */
    public String processAnswer(Answer theAnswer, IDialogLogic logic)
    {
        String nextUnit;
        // Process answer logic
        if (theAnswer.getGotoCode().equals("LOGIC"))
        {
            nextUnit = this.processAnswerWithLogic(logic);
        }
        else
        {
            nextUnit = this.setNextUnit(theAnswer);
        }
        this.status.setDialogPhase(nextUnit);

        return nextUnit;
    }

    /**
     * Intelligence behind dialogs code. If the gotoCode of the
     * answer is "LOGIC", then this method is called to determine 
     * the next unit.
     * @param unitCode
     * @return next dialog unit code
     */
    public String processAnswerWithLogic(IDialogLogic logic)
    {
        String nextUnitCode = logic.getNextUnitCode(this.currentUnit);

        if (nextUnitCode != null)
        {
            if (!nextUnitCode.equals("EXIT"))
            {
                this.setCurrentUnit(nextUnitCode);
            }
        }

        return nextUnitCode;
    }

    private void loadUnit()
    {
        DialogUnit du = null;

        this.log.addDetail("Loading dialog unit " + this.currentUnit);
        du = this.dm.getUnit(this.currentUnit);

        if (du != null)
        {
            this.currentQuestion = du.getQuestion();
            this.currentAnswers = du.getAnswers();
            this.log.addDetail("Done. Current question: " + this.currentQuestion);
        }
        else
        {
            this.log.addDetail("Can't find dialog unit " + this.currentUnit);
        }
    }

    private void initializeMageDialog()
    {
        String magesDialogFilePath = Config.addDatPathTo(Config.MAGESGUILDDIALOG);
        File magesDialog = new File(magesDialogFilePath);
        if (!magesDialog.exists())
        {
            this.log.addDetail("Extracting mages dialog to " + magesDialogFilePath);
            ResourceFinder rf = ResourceFinder.resources();
            rf.extractResource(Config.MAGESGUILDDIALOG, magesDialogFilePath);
        }
        else
        {
            this.log.addDetail("Mages dialog found.");
        }

        this.dm = new DialogManager(magesDialogFilePath);
        this.loadUnit();

        // Set PC's dialog
        if (this.player.getDialogStatus(DIALOG_ID) == null)
        {
            this.player.addNewDialogStatus(DIALOG_ID);
        }

        this.status = this.player.getDialogStatus(DIALOG_ID);
    }
}
