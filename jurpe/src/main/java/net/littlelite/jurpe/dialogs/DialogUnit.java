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

import java.util.AbstractList;
import java.util.ArrayList;

/**
 * A dialog unit object is composed by a question, some answers, and an ID code
 */
public class DialogUnit
{

    private String id;
    private String question;
    private AbstractList<Answer> answers;

    /**
     * Dialog Unit constructor
     * @param code The Dialog Unit code (ID)
     */
    public DialogUnit(String code)
    {
        this.id = code;
        this.answers = new ArrayList<Answer>();
    }

    /**
     * Set Dialog Unit question
     * @param qst Question text
     */
    public void setQuestion(String qst)
    {
        this.question = qst;
    }

    /**
     * Get Dialog Unit question
     * @return The Dialog Unit question
     */
    public String getQuestion()
    {
        return this.question;
    }

    /**
     * Get Dialog Unit code (ID)
     * @return The dialog unit code
     */
    public String getId()
    {
        return id;
    }

    /**
     * Add an answer to the answers of this dialog unit
     * @param ans Answer to be added
     */
    public void addAnswer(Answer ans)
    {
        this.answers.add(ans);
    }

    /**
     * Get the answers related to this dialog unit
     * @return Answers of this dialog unit
     */
    public AbstractList<Answer> getAnswers()
    {
        return this.answers;
    }

    /**
     * String representation of the dialog unit
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.question);
        sb.append("\n");
        int counter = 0;
        for (Answer a : this.getAnswers())
        {
            sb.append(++counter);
            sb.append(") ");
            sb.append(a.getAnswer());
            sb.append(" [");
            sb.append(a.getGotoCode());
            sb.append("]");
            sb.append("\n");
        }
        return sb.toString();
    }
}
