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

import java.util.Arrays;

import net.littlelite.utils.metadata.IXMLConverter;
import net.littlelite.utils.metadata.SaxConverter;

/**
 * Implements differences between different platforms, such as .NET Visual
 * JSharp. When initializing system, use Platform.PLATFORM = "DotNet" if you
 * want to compile under DotNet platform.
 */
public class Platform
{

	/**
	 * Returns a Java Platform Independant XMLConverter
	 * 
	 * @return XML Converter based on platform
	 */
	public static IXMLConverter getXMLConverter()
	{
		return new SaxConverter();
		// DotNet: return new name.alessiosaltarin.utils.DotNetXMLConverter();
	}

	/**
	 * Sort a given array
	 * 
	 * @param array
	 *            Object[] to sort
	 * @return sorted array
	 */
	public static Object[] sortArray(Object[] array)
	{

		Arrays.sort(array);
		return array;

		/*
		 * System.Array.Sort(array); return array;
		 */
	}

}