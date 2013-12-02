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
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Keeps a log of events in the game. The log is sent to the console by a
 * console handler and to a collection of strings. There are two type of logs:
 * <ul>
 * <li>Entry</li>
 * <li>Detail</li>
 * </ul>
 * The entry is displayed to the final user, while details AND entries are
 * displayed to the console.
 * 
 * 
 */
public final class Log implements Serializable
{

	private static final long serialVersionUID = 3322L;
	private Logger theLog;
	private ListLoggerHandler logHandler;
	private Handler fileHdlr;

	/**
	 * Add new detail to the log Details are only sent to the console
	 * 
	 * @param text
	 *            Text of the detail.
	 */
	public void addDetail(String text)
	{
		theLog.log(Level.INFO, text);
	}

	/**
	 * Add new entry to log. Entries are displayed to the console and to the
	 * user interface. If the text has new lines, each line is sent as a single
	 * message
	 * 
	 * @param text
	 *            String to log
	 */
	public void addEntry(String text)
	{
		if (text.indexOf(OSProps.LINEFEED) != -1)
		{
			StringTokenizer st = new StringTokenizer(text, OSProps.LINEFEED, true);
			while (st.hasMoreTokens())
			{
				String msg = st.nextToken();
				if (msg.length() > 1) // Skipping control chars...
				{
					theLog.log(Level.SEVERE, msg);
				}
			}
		}
		else
		{
			theLog.log(Level.SEVERE, text);
		}
	}

	/**
	 * Add a blank line
	 * 
	 */
	public void addEntry()
	{
		theLog.log(Level.SEVERE, " ");
	}

	/**
	 * Log to external file
	 * 
	 * @param file
	 *            File to save log into
	 * @throws IOException
	 */
	public void setLogToFile(File file) throws IOException
	{
		final DateFormat df = DateFormat.getDateTimeInstance();
		fileHdlr = new FileHandler(file.getAbsolutePath());
		fileHdlr.setFormatter(new Formatter()
		{

			public String format(LogRecord record)
			{
				String dt = df.format(new Date(record.getMillis()));
				return dt + "  :  " + record.getMessage() + OSProps.LINEFEED;
			}
		});
		theLog.addHandler(fileHdlr);
	}

	/**
	 * End a log to external file session.
	 */
	public void unsetLogToFile()
	{
		theLog.removeHandler(fileHdlr);
	}

	/**
	 * Get latest entries.
	 * 
	 * @return last added entries to log.
	 */
	public String getNewEntries()
	{
		StringBuilder sb = new StringBuilder();
		List<String> logs = logHandler.getList();

		for (String s : logs)
		{
			sb.append(s);
			sb.append(OSProps.LINEFEED);
		}

		logHandler.flush();

		return sb.toString();
	}

	/**
	 * Get log entries. It makes the log empty when called.
	 * 
	 * @return Log as an array
	 */
	public String[] getEntries()
	{
		String[] entries = new String[this.getNumberOfEntries()];
		logHandler.setLevel(Level.SEVERE);
		logHandler.getList().toArray(entries);
		logHandler.flush();
		return entries;
	}

	/**
	 * Get number of log entries.
	 * 
	 * @return number of log entries.
	 */
	public int getNumberOfEntries()
	{
		return logHandler.getNumItems();
	}

	/**
	 * Get the last inserted entry
	 * 
	 * @return last inserted entry
	 */
	public String getLatestEntry()
	{
		String entry = "";
		List<String> avvenimenti = logHandler.getList();
		if (avvenimenti.size() > 1)
		{
			entry = avvenimenti.get(avvenimenti.size() - 1);
		}
		return entry;
	}

	/**
	 * Returns current log as a single String.
	 * 
	 * @return log entries.
	 */
	@Override
	public String toString()
	{
		return this.getNewEntries();
	}

	/**
	 * Return this log
	 * 
	 * @return handler to log
	 */
	public static Log getReference()
	{
		return ow;
	}
	private static Log ow = new Log();

	/**
	 * Constructor
	 */
	private Log()
	{
		final DateFormat df = DateFormat.getDateTimeInstance();
		theLog = Logger.getLogger("eJurpe");
		theLog.setUseParentHandlers(false);

		// Jurpe log
		logHandler = new ListLoggerHandler();
		logHandler.setLevel(Level.SEVERE);
		logHandler.setFormatter(new Formatter()
		{
			public String format(LogRecord record)
			{
				return record.getMessage() + OSProps.LINEFEED;
			}
		});

		// Log to console
		Handler conHdlr = new ConsoleHandler();
		conHdlr.setLevel(Level.INFO);
		conHdlr.setFormatter(new Formatter()
		{
			public String format(LogRecord record)
			{
				String dt = df.format(new Date(record.getMillis()));
				return dt + ":  " + record.getMessage() + OSProps.LINEFEED;
			}
		});
		theLog.addHandler(conHdlr);
		theLog.addHandler(logHandler);
	}
}

/**
 * Custom Log Handler to hold log message in a java.util.List collection
 */
final class ListLoggerHandler extends java.util.logging.Handler
{

	private static AbstractList<String> strHolder = new ArrayList<String>();

	public void publish(LogRecord logRecord)
	{
		if (logRecord.getLevel().intValue() >= this.getLevel().intValue())
		{
			strHolder.add(logRecord.getMessage());
		}

		if (strHolder.size() > Config.MAXLOGSIZE)
		{
			strHolder.remove(0);
		}
	}

	public AbstractList<String> getList()
	{
		return ListLoggerHandler.strHolder;
	}

	public int getNumItems()
	{
		return ListLoggerHandler.strHolder.size();
	}

	public void flush()
	{
		strHolder.clear();
	}

	public void close()
	{
	}
}
