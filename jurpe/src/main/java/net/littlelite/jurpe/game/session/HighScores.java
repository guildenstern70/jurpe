package net.littlelite.jurpe.game.session;

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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.littlelite.jurpe.system.OSProps;

/**
 * Container type for High Scores.
 */
public class HighScores implements Serializable
{

	private static final long serialVersionUID = 3317L;
	private SortedSet<Score> scores;

	private int maxNrScores;

	/**
	 * Constructor.
	 * 
	 * @param maxRows
	 *            Max Number of entries this High Score Table can contain
	 */
	public HighScores(int maxRows)
	{
		this.scores = new TreeSet<Score>();
		maxNrScores = maxRows;
	}

	/**
	 * Constructor. 20 rows.
	 */
	public HighScores()
	{
		this.scores = new TreeSet<Score>();
		maxNrScores = 20; // default
	}

	/**
	 * Add a new Score to current High Score Table. Since scores is a Sorted
	 * Set, it will enter in the correct sorted place.
	 * 
	 * @param nsc
	 *            Score to add
	 * @return true, if this scores enters the High Score table.
	 */
	public boolean addToScore(Score nsc)
	{
		boolean enters = false;

		// 0 points score will not enter
		if (nsc.getScore() < 1)
		{
			return enters;
		}

		if (this.scores.size() < maxNrScores)
		{
			this.scores.add(nsc);
			enters = true;
		}
		else
		{

			int counter = 0;
			SortedSet<Score> newHighScores = new TreeSet<Score>();
			Iterator<Score> x = this.scores.iterator();

			while (x.hasNext())
			{
				counter++;
				Score hs = x.next();
				newHighScores.add(hs);
				if (counter > maxNrScores)
				{
					break;
				}
			}

			this.scores = newHighScores;

			// Are you in there?
			for (Score tmp : this.scores)
			{
				if (nsc.equals(tmp))
				{
					enters = true;
					break;
				}
			}

		}

		return enters;
	}

	/**
	 * Add a new Score to current High Score Table. Since scores is a Sorted
	 * Set, it will enter in the correct sorted place.
	 * 
	 * @param name
	 *            Name of PCharacter
	 * @param points
	 *            Final score of PCharacter
	 * @return true, if this scores enters the High Score table.
	 */
	public boolean addToScore(String name, int points)
	{
		Score sc = new Score(name, points);
		return this.addToScore(sc);
	}

	/**
	 * String that represents the high score table
	 * 
	 * @return String that represents the high score table
	 */
        @Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("HIGH SCORES");
		int counter = 0;

		for (Score sc : this.scores)
		{
			sb.append(OSProps.LINEFEED);
			sb.append(++counter);
			sb.append(") ");
			sb.append(sc);
		}

		sb.append(OSProps.LINEFEED);

		return sb.toString();
	}

	/**
	 * Get High Scores as an array made of Name,Date,Score. Array is sorted from
	 * best to worst score.
	 * 
	 * @return double array to use with Swing Tables.
	 */
	public Object[][] getHighScores()
	{

		int howmany = this.scores.size();
		Object[][] highScores = new Object[howmany][Score.NUMFIELDSSCORE];
		Iterator<Score> j = this.scores.iterator();
		int k = -1;

		while (j.hasNext())
		{
			Score ab = j.next();
			highScores[++k] = ab.toStrings((short) (k + 1));
		}

		return highScores;
	}

	/**
	 * Add default high scores to High Score table.
	 */
	public void setDefaultHighScores()
	{

		Score[] sc =
		{ new Score("JurpeMan", 40), new Score("JurpeMan", 34), new Score("JurpeMan", 5), new Score("JurpeMan", 43), new Score("JurpeMan", 20),
				new Score("JurpeMan", 4), new Score("JurpeMan", 10), new Score("JurpeMan", 114), new Score("JurpeMan", 14), new Score("JurpeMan", 94) };

		for (int j = 0; j < sc.length; j++)
		{
			this.addToScore(sc[j]);
		}

	}

	/**
	 * Saves actual HighScores
	 * 
	 * @param filename
	 *            complete name and path of file
	 */
	public void save(String filename)
	{
		try
		{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
			out.writeObject(this);
			out.close(); // Also flushes output
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	/**
	 * For test purposes only.
	 * 
	 * @param args
	 * @deprecated
	 */
	public static void main(String[] args)
	{
		HighScores hs = new HighScores(10);
		hs.setDefaultHighScores();
		hs.save("c:\\windows\\desktop\\HighScores.bin");
		System.out.println(hs);
	}

}

/**
 * Utility type for a score. Implemets a descending compareTo function.
 * 
 * 
 */

class Score implements Comparable<Score>, Serializable
{
	private static final long serialVersionUID = 3317L;
	protected static final int NUMFIELDSSCORE = 4;

	private int points;
	private String namePC;
	private Date date;

	/**
	 * New high score
	 * 
	 * @param xNamePC
	 *            Player Name
	 * @param xPoints
	 *            Points
	 */
	public Score(String xNamePC, int xPoints)
	{
		this.points = xPoints;
		this.namePC = xNamePC;
		this.date = new Date();
	}

	/**
	 * Comparer
	 */
	public int compareTo(Score rv)
	{
		int rvScore = rv.points;
		return (this.points > rvScore) ? (-1) : ((this.points == rvScore) ? (0) : (1));
	}

	/**
	 * To String
	 */
        @Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.namePC);
		sb.append(" (");
		sb.append(this.date.toString());
		sb.append(") ");
		sb.append("\t\t");
		sb.append(this.points);

		return sb.toString();
	}

	/**
	 * Return an array of this kind [Position][Name][Date of High Score][Points]
	 * 
	 * @param position
	 *            Position in High Score table. Determined by HighScores class
	 * @return Array of one entry in High Scores Table
	 */
	public Object[] toStrings(short position)
	{
		String[] strs = new String[NUMFIELDSSCORE];
		strs[0] = String.valueOf(position);
		strs[1] = this.namePC;
		strs[2] = this.getDate();
		strs[3] = String.valueOf(this.points);

		return strs;
	}

	/**
	 * Player name
	 * 
	 * @return Player Name
	 */
	public String getName()
	{
		return this.namePC;
	}

	/**
	 * Score
	 * 
	 * @return Points
	 */
	public int getScore()
	{
		return this.points;
	}

	/**
	 * True if two scores are equals (Name and Score are equal)
	 */
	public boolean equals(Object e)
	{
		if (e.getClass().getName().equals(this.getClass().getName()))
		{
			Score confr = (Score) e;
			if (this.namePC.equals(confr.getName()) && (this.points == confr.getScore()))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Return hash key for object. For our purposes, the hash is made upon the
	 * letter of the name of the scorer plus his score
	 * 
	 * @return hash code
	 */
	public int hashCode()
	{
		int hash = this.getScore();
		String name = this.getName();
		for (int j = 0; j <= name.length(); j++)
		{
			hash += name.charAt(j);
		}
		return hash;
	}

	/**
	 * Get the date in which the high score was entered
	 * 
	 * @return
	 */
	private String getDate()
	{
		// Format the current time.
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy 'at' hh:mm:ss");

		return formatter.format(this.date);
	}

}
