package net.littlelite.jurpe.system.generation;

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

import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.utils.AxsRand;

/**
 * Dice. This class implements dice abstraction for the game
 * 
 * @todo: Singleton?
 * 
 */
public class Dice
{
	private int faces;

	private AxsRand rnd;

	/**
	 * Constructs a six faces dice.
	 */
	public Dice()
	{
		rnd = AxsRand.getReference();
		faces = 6;
	}

	/**
	 * Builds a die with "facce" faces
	 * 
	 * @param facce
	 *            faces of this die.
	 */
	public Dice(int facce)
	{
		faces = facce;
		rnd = AxsRand.getReference();
	}

	/**
	 * Rolls 6 faces die.
	 * 
	 * @return Random number between 1 and 6
	 */
	public int throwDice()
	{
		return (rnd.randInt(faces) + 1);
	}

	/**
	 * Rolls "volte" dice
	 * 
	 * @param volte
	 *            Number of times to roll dice
	 * @return Sum of dice rolls
	 */

	public int throwDice(int volte)
	{

		int num = 0;

		for (int j = 0; j < volte; j++)
		{
			num += throwDice();
		}
		return num;

	}

	/**
	 * Roll a normal six faces die
	 * 
	 * @return Random number between 1 and 6
	 */
	public static short roll() throws JurpeException
	{
		Dice d = new Dice();
		short t = (short) (d.throwDice());
		if (t < 1 || t > 6)
		{
			throw new JurpeException("Abnormal dice: " + t);
		}

		return t;
	}

}