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
package net.littlelite.jurpedemo.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import net.littlelite.jurpedemo.resources.GameStrings;

/**
 * Extends JList with popup menu functionalities for handling inventory
 * 
 */
public class InventoryListBox extends JList
{

    private static final long serialVersionUID = 3318L;
    
    private JPopupMenu popup;
    JurpeMain parent;
    private boolean isEmpty;
    private PopupListener inventoryListener;
    /**
     * Popup menu commands
     */
    static final String[] COMMANDS =
            {GameStrings.WEAR, GameStrings.DROP, GameStrings.USE, GameStrings.EXAMINE, GameStrings.SELL};

    /**
     * Constructor
     * 
     * @param parentFrame
     *            Parent Frame
     */
    public InventoryListBox(JurpeMain parentFrame)
    {
        this.parent = parentFrame;
        this.isEmpty = true;
        this.inventoryListener = null;
        this.init();
    }

    /**
     * Builds up popup menu
     */
    private void init()
    {
        this.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        this.setToolTipText(GameStrings.RIGHTINT);
        JMenuItem mi;
        this.popup = new JPopupMenu(GameStrings.INVENTORY);

        for (short k = 0; k < InventoryListBox.COMMANDS.length; k++)
        {
            mi = new JMenuItem(COMMANDS[k]);
            ActionListener al = new ActionListener()
            {

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    String command = e.getActionCommand();
                    if (command.equals(COMMANDS[0])) // Wear
                    {
                        InventoryListBox.this.parent.wearItem();
                    }
                    else if (command.equals(COMMANDS[1])) // Drop
                    {
                        InventoryListBox.this.parent.dropItem();
                    }
                    else if (command.equals(COMMANDS[2])) // Use
                    {
                        InventoryListBox.this.parent.useItem();
                    }
                    else if (command.equals(COMMANDS[3])) // Examine
                    {
                        InventoryListBox.this.parent.examineItem();
                    }
                    else if (command.equals(COMMANDS[4])) // Sell
                    {
                        InventoryListBox.this.parent.sellItem();
                    }
                }
            };

            mi.addActionListener(al);
            this.popup.add(mi);
        }

        this.inventoryListener = new PopupListener(this.popup);
        this.addMouseListener(this.inventoryListener);
        this.add(this.popup);
    }

    /**
     * If the inventory contains no items
     * @return true, if the inventory is empty
     */
    public boolean isEmpty()
    {
        return this.isEmpty;
    }

    /**
     * Set mouse listeners, based on if the inventory is empty
     * @param isEmpty
     */
    public void setMouseListeners(boolean isEmpty)
    {
        this.isEmpty = isEmpty;
        int mouseListeners = this.getMouseListeners().length;

        if (this.isEmpty)
        {
            if (mouseListeners == 3)
            {
                this.removeMouseListener(this.inventoryListener);
                this.remove(this.popup);
            }
        }
        else
        {
            if (mouseListeners == 2)
            {
                this.addMouseListener(this.inventoryListener);
                this.add(this.popup);
            }
        }
    }
}
