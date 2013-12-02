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
package net.littlelite.jurpedemo;

import java.io.*;

import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.EnabledDisabled;
import net.littlelite.jurpe.system.Log;
import net.littlelite.jurpedemo.frames.JurpeLookAndFeel;

/**
 * Serialized class for Options
 */
public class Options implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static final String optionsFile = Config.addDatPathTo("options.dat");

	private JurpeLookAndFeel laf;
	private EnabledDisabled fastlogs;
	transient private Log log;

	private Options(Log journal)
	{
		this.laf = JurpeDemoConfig.LOOKANDFEEL;
		this.fastlogs = EnabledDisabled.DISABLED;
		this.log = journal;
	}

	public void save()
	{
		try
		{
			FileOutputStream f_out = new FileOutputStream(Options.optionsFile);
			ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
			obj_out.writeObject(this);
			obj_out.close();
		}
		catch (IOException ioex)
		{
			this.log.addEntry("Cannot save Options: " + ioex.getMessage());
		}
	}

	public void setLookAndFeel(JurpeLookAndFeel laf)
	{
		this.laf = laf;
	}

	public JurpeLookAndFeel getLookAndFeel()
	{
		return this.laf;
	}

	public void setFastlogs(EnabledDisabled fastlogs)
	{
		this.fastlogs = fastlogs;
	}

	public EnabledDisabled getFastlogs()
	{
		return this.fastlogs;
	}

	public static Options getOptions(Log theLog)
	{
		Options opt = null;
		FileInputStream f_in = null;
		ObjectInputStream obj_in = null;

		try
		{
			f_in = new FileInputStream(Options.optionsFile);
			obj_in = new ObjectInputStream(f_in);
			Object obj = obj_in.readObject();
			if (obj instanceof Options)
			{
				// Cast object to a Vector
				opt = (Options) obj;
				theLog.addDetail("Loaded options values.");
			}
			else
			{
				opt = new Options(theLog);
				theLog.addDetail("Cannot load options: setting default values.");
			}

		}
		catch (IOException ioex)
		{
			opt = new Options(theLog);
			theLog.addDetail("Cannot load options: setting default values.");
		}
		catch (ClassNotFoundException cnf)
		{
			opt = new Options(theLog);
			theLog.addDetail("Cannot load options: setting default values.");
		}
		finally
		{

			if (obj_in != null)
			{
				try
				{
					obj_in.close();

				}
				catch (IOException e)
				{
				}
			}

			if (f_in != null)
			{
				try
				{
					f_in.close();
				}
				catch (IOException e)
				{
				}
			}

		}

		return opt;
	}

}
