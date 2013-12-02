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

package net.littlelite.jurpedemo.logic;

import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.dialogs.IDialogLogic;

/**
 *
 * @todo: render the logic scriptable outside the program
 */
public class MagesGuildLogic implements IDialogLogic
{
    
    private PCharacter player;
    
    public MagesGuildLogic(PCharacter playingChar)
    {
        this.player = playingChar;
    }
    
    public String getNextUnitCode(String currentUnit)
    {
        String nextUnitCode = null;
                
        // Unit AO2: player has magical skills?
        if (currentUnit.equals("A02")) // Sense magic
        {
            if (this.player.hasInnate("Magical Aptitude"))
            {
                nextUnitCode = "A03";
            }
            else
            {
                nextUnitCode = "A04";
            }
        }

        // Unit A03: player has a grimoire with him?
        if (currentUnit.equals("A03"))
        {
            if (this.player.hasGrimoire())
            {
                nextUnitCode = "A12";
            }
            else
            {
                nextUnitCode = "A05";
            }
        }

        // Unit A05: player has a grimoire with him?
        if (currentUnit.equals("A05"))
        {
            if (this.player.hasGrimoire())
            {
                nextUnitCode = "A13";
            }
            else
            {
                nextUnitCode = "A06";
            }
        }
        
        return nextUnitCode;
    }   
}
