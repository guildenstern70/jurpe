package net.littlelite.jurpe.system;

/**
 * J.U.R.P.E.
 * 
 * @version@ Copyright (C) 2002-12 LittleLite Software
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */

/**
 * Enabled/Disabled typesafe enum
 */
public enum EnabledDisabled
{

	ENABLED(true), DISABLED(false);

	private EnabledDisabled(boolean en)
	{
		this.enabled = en;
	}

	public boolean isEnabled()
	{
		return this.enabled;
	}

	private boolean enabled;

}
