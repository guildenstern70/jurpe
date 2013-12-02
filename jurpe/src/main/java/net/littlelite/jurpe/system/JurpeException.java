package net.littlelite.jurpe.system;

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

/**
 * Class for reporting exceptions in eGURPS API usage
 * 
 * 
 */
public class JurpeException extends Exception
{

	private static final long serialVersionUID = 3317L;

	/**
	 * Exception in Jurpe API usage
	 * 
	 * @param message
	 *            Message of this exception
	 */
	public JurpeException(String message)
	{
		super(message);
	}

}
