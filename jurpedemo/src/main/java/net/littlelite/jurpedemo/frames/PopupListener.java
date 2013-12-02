package net.littlelite.jurpedemo.frames;

/**
 J.U.R.P.E. @version@ Swing Demo
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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

/**
 * Custom listener for popup menus
 */
public class PopupListener extends MouseAdapter
{

	protected JPopupMenu popup;

	/**
	 * Constructor
	 * 
	 * @param menu
	 *            Associated menu
	 */
	public PopupListener(JPopupMenu menu)
	{
		this.popup = menu;
	}

	/**
	 * On mouse pressed
	 * 
	 * @param e
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		maybeShowPopup(e);
	}

	/**
	 * On mouse released (necessary for non MS platforms)
	 * 
	 * @param e
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		maybeShowPopup(e);
	}

	/**
	 * What to do in case of mouse pressed
	 * 
	 * @param e
	 */
	public void maybeShowPopup(MouseEvent e)
	{
		if (e.isPopupTrigger())
		{
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

}