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

package net.littlelite.jurpe.dialogs;

/**
 * An answer class.
 * Any answer has an "answer" string, plus a key code (shortcut for
 * the action to be taken if the answer is chosen) and a gotoCode,
 * that is the ID of the next question.
 * 
 */
public class Answer
{
    private String answer;
    private String gotoCode;
    private String keyCode;
    
    /**
     * Answer object constructor
     * @param answ Answer text
     * @param key Answer code (id)
     * @param gtoCode Dialog unit goto code (the code to go to, when the answer is chosen)
     * @see net.littlelite.jurpe.dialogs.DialogUnit
     */
    public Answer(String answ, String key, String gtoCode)
    {
        this.answer = answ;
        this.gotoCode = gtoCode;
        this.keyCode = key;
    }
    
    /**
     * This is a code to communicate something to the program,
     * telling it that the answer has been chosen
     * @return A unit code to be loaded
    */
    public String getKey()
    {
        return this.keyCode;
    }

    /**
     * The answer text
     * @return the answer text
     */
    public String getAnswer()
    {
        return answer;
    }

    /**
     * Set answer text
     * @param answer the answer text
     */
    public void setAnswer(String answer)
    {
        this.answer = answer;
    }

    /**
     * This is the code string representing the next unit to be shown
     * if the answer is selected.
     * @return A unit code to be loaded
     */
    public String getGotoCode()
    {
        return gotoCode;
    }

    /**
     * Set the goto code, that is the next dialog unit called
     * when this answer is chosen
     * @param gotoCode The dialog unit goto code
     */
    public void setGotoCode(String gotoCode)
    {
        this.gotoCode = gotoCode;
    }
    
}
