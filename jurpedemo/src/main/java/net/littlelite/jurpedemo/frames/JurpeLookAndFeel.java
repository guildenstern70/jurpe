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

package net.littlelite.jurpedemo.frames;

/**
 * Look and feel enumeration. This is a jdk 1.5 type safe enum
 */
public enum JurpeLookAndFeel
{
	LOOKS(0), METAL(1), WINDOWS(2), SYSTEM(3), PLASTIC(4), PLASTICXP(5), XPLATFORM(6), OCEAN(7), NIMBUS(8);

	private JurpeLookAndFeel(int value)
	{
		this.laf = value;
	}

	public int value()
	{
		return laf;
	}

	private final int laf;

}
