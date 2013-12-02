package net.littlelite.utils;

/**
 *
 * Alessio Saltarin General Library Utils
 * package name.alessiosaltarin.utils
 * Copyright Alessio Saltarin 2001-2008
 *
 */

import java.util.Date;
import java.util.Random;

/**
 * Random Numbers Generator
 * 
 * 
 * @version 1.0
 * @since 29/05/2001
 */
public class AxsRand
{

	/**
	 * Get reference to the singleton class AxsRand
	 * 
	 * @return AxsRand object
	 */
	public static AxsRand getReference()
	{
		return axs;
	}

	/**
	 * Returns a random number between 0 and maxint-1
	 * 
	 * @param maxint
	 *            Maximum number allowed
	 * @return Random number between 1 and maxint
	 */
	public int randInt(int maxint)
	{
		return rndgen.nextInt(maxint);
	}

	/**
	 * Returns a random number between 0 and 127
	 * 
	 * @return random number between 0 and 127
	 */
	public byte randByte()
	{
		return (byte) (rndgen.nextInt(128));
	}

	/**
	 * Returns a random number between 0 and b-1 (with b <=127)
	 * 
	 * @param b
	 *            Maximum number to be generated
	 * @return random number between 0 and b-1 (with b <=127)
	 */
	public byte randByte(byte b)
	{
		return (byte) (rndgen.nextInt(b));
	}

	/**
	 * Returns a random float number
	 * 
	 * @return Random float number
	 */
	public float randFloat()
	{
		return rndgen.nextFloat();
	}

	/**
	 * Return a random true or false
	 * 
	 * @return random true or false
	 */
	public boolean randBoolean()
	{
		return rndgen.nextBoolean();
	}

	/**
	 * Returns a random double number
	 * 
	 * @return random double number
	 */
	public double randDouble()
	{
		return rndgen.nextDouble();
	}

	/**
	 * Returns a random char (between ASCII=32 and ASCII=132)
	 * 
	 * @return Random printable char
	 */
	public char randChar()
	{
		char tmpchar = (char) (rndgen.nextInt(100) + 32);
		return tmpchar;
	}

	/**
	 * For test purposes only
	 * 
	 * @param args
	 * @deprecated
	 */
	public static void main(String[] args)
	{
		AxsRand axsrand = AxsRand.getReference();

		for (int j = 0; j < 10; j++)
		{
			System.out.println(axsrand.randInt(1000));
		}

		for (int j = 0; j < 10; j++)
		{
			System.out.println(axsrand.randDouble());
		}

		for (int j = 0; j < 10; j++)
		{
			System.out.println(axsrand.randFloat());
		}

		for (int j = 0; j < 10; j++)
		{
			System.out.println(axsrand.randChar());
		}

	}

	private static AxsRand axs = new AxsRand();

	private Date seedGenerator;

	private Random rndgen;

	private AxsRand()
	{
		long seed;

		seedGenerator = new Date();
		seed = seedGenerator.getTime();
		rndgen = new Random(seed);
	}
}