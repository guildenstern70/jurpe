package net.littlelite.utils;

/**
 *
 * Alessio Saltarin General Library Utils
 * package name.alessiosaltarin.utils
 * Copyright (C) LittleLite Software
 *
 */

import java.util.AbstractList;

/**
 * Summary description for XMLNavigator.
 */
public interface IXMLNavigator
{

	/**
	 * Read XML file indicated in the constructor. Invoke parsing.
	 */
	void readXML();

	/**
	 * Write XML to System.out. Use it for debug purposes.
	 */
	void writeXML();

	/**
	 * Get XML contents as an ArrayList.
	 * 
	 * @return Container with strings read from XML file.
	 */
	AbstractList content();
}
