package net.littlelite.utils;

/**
 *
 * Alessio Saltarin General Library Utils
 * package name.alessiosaltarin.utils
 * Copyright Alessio Saltarin 2001-2008
 *
 */

/**
 * Singleton.java
 * 
 * 2001
 * 
 * @since 05/2001
 * @version 1.0 Template per i singleton
 */

final class Singleton
{

	private static Singleton ow = new Singleton();

	/**
	 * Per ottenere un handle alla classe instanziata (singleton)
	 * 
	 * @return Reference a Singleton
	 */
	public static Singleton getReference()
	{
		return ow;
	}

	public void initialize()
	{
		// implement
	}
}