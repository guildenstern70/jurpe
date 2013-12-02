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
import net.littlelite.utils.BrowserControl;

/**
 * Contains Operating System related functions. Implemented as a singleton. Use
 * it with OSProps op = OSProps.getReference()
 * 
 * 
 */
public final class OSProps
{

	// Public Fields
	public final static String LINEFEED = System.getProperty("line.separator");
	public final static String FILESEPARATOR = System.getProperty("file.separator");

	// Private Fields
	private static OSProps osp = new OSProps();
	private final static String OS_NAME = System.getProperty("os.name");
	private final static String OS_ARCH = System.getProperty("os.arch");
	private final static String OS_VERSION = System.getProperty("os.version");
	private final static String JAVA_VERSION = System.getProperty("java.version");

	/**
	 * Use this method to get to class methods
	 * 
	 * @return OSProps handle
	 */
	public static OSProps getReference()
	{
		return osp;
	}

	/**
	 * String describing OS and Architecture in which program is run.
	 * 
	 * @return string describing OS and Architecture in which program is run
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("Running on ");
		sb.append(OS_ARCH);
		sb.append(" (");
		sb.append(OS_NAME);
		if ((OS_VERSION != null) && (!OS_NAME.startsWith("Windows")))
		{
			sb.append(" ");
			sb.append(OS_VERSION);
		}
		sb.append(")");
		sb.append(" with JRE ");
		sb.append(JAVA_VERSION);

		return sb.toString();
	}

	private OSProps()
	{
	}

	/**
	 * Displays a URL in current browser application. (It does not function very
	 * well...)
	 * 
	 * @param url
	 *            URL to display
	 */
	public static void showURL(String url)
	{
		System.out.println("Trying :" + url);
		BrowserControl.displayURL(url);

	}

	/**
	 * If program is running on Windows XP
	 * 
	 * @return true if program is running on a Windows XP OS
	 */
	public boolean isXP()
	{
		if (OS_NAME.contains("XP"))
		{
			return true;
		}
		return false;
	}

	/**
	 * If program is running on Windows Vista
	 * 
	 * @return true if program is running on a Windows Vista OS
	 */
	public boolean isVista()
	{
		if (OS_NAME.contains("Vista"))
		{
			return true;
		}
		return false;
	}

	/**
	 * If program is running on Linux
	 * 
	 * @return true if program is running on Linux
	 */
	public boolean isLinux()
	{
		if (OS_NAME.equals("Linux"))
		{
			return true;
		}
		return false;
	}
}
