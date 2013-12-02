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
 * A dialog manager class handles the dialog between two characters.
 * It is based upon a dialog written in XML and parsed by a DialogParser class.
 * 
 */
public class DialogManager
{
    private DialogParser parser;
    private String xmlDialogFile;
    
    /**
     * Dialog Manager
     * @param path The path of the XML file containing the dialog
     * @see net.littlelite.jurpe.dialogs.DialogUnit
     */
    public DialogManager(String path)
    {
        this.xmlDialogFile = path;
        this.parser = new DialogParser(this.xmlDialogFile);
    }
        
    /**
     * Get the specified dialog unit from the dialog
     * in the XML file
     * @param code Dialog Unit code to be extracted
     * @return The Dialog Unit with the given code
     */
    public DialogUnit getUnit(String code)
    {
        return this.parser.getDialogUnit(code);
    }
        
}
