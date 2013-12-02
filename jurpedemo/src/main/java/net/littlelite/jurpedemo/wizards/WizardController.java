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

/**
 * Encapsulate all attributes for creating a new character. Used by
 * CharacterWizardN classes.
 */
public class WizardController
{

    private static final int PANELS_NUMBER = 4;  // 0 BASED
    private CharacterAttributes charAtt;
    private boolean isCreated;
    private int currentPanel;
    private AbstractWizardPanel[] wizardPanels;

    public WizardController()
    {
        this.isCreated = false;
        this.currentPanel = 0;
        this.charAtt = new CharacterAttributes();
        this.wizardPanels = new AbstractWizardPanel[PANELS_NUMBER + 1];
        this.wizardPanels[0] = new PanelWizard0();
        this.wizardPanels[1] = new PanelWizard1(this);
        this.wizardPanels[2] = new PanelWizard2(this);
        this.wizardPanels[3] = new PanelWizard3(this);
        this.wizardPanels[4] = new PanelWizard4(this);
    }

    public CharacterAttributes getCharacterAttrs()
    {
        return this.charAtt;
    }

    public void setCreated()
    {
        this.isCreated = true;
    }

    public int getCurrentPanel()
    {
        return this.currentPanel;
    }

    public boolean isLastWizardPanel()
    {
        return (this.currentPanel == PANELS_NUMBER);
    }

    public boolean isFirstWizardPanel()
    {
        return (this.currentPanel == 0);
    }

    public boolean isCharacterCreated()
    {
        return this.isCreated;
    }

    public AbstractWizardPanel getPrevious()
    {   
        AbstractWizardPanel target;
        if (this.currentPanel > 0)
        {
            target =  this.wizardPanels[--this.currentPanel];
        }
        else
        {
            this.currentPanel = 0;
            target =  this.wizardPanels[this.currentPanel]; 
        }
        return target;
    }

    public AbstractWizardPanel getNext()
    {
        AbstractWizardPanel target;
        if (this.currentPanel < PANELS_NUMBER)
        {
            target = this.wizardPanels[++this.currentPanel];
        }
        else
        {
            this.currentPanel = PANELS_NUMBER;
            target = this.wizardPanels[this.currentPanel];
        }
        return target;
    }
}

