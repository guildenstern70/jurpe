package net.littlelite.utils;

/**
 *
 * Alessio Saltarin General Library Utils
 * package name.alessiosaltarin.utils
 * Copyright Alessio Saltarin 2001,2002,2003
 *
 */

import java.util.AbstractList;

/**
 * Interface for XML metadata managing.
 * 
 * @author Alessio Saltarin
 */
public interface IXMLConverter
{

	/**
	 * Parse XML file, and populates internal variables with contents read from
	 * XML file
	 * 
	 * @param fileName
	 *            Path to file.
	 */
	void parse(String fileName);

	/**
	 * Get read XML elements as a List. The List contains variables of type
	 * XmlLeaf.
	 * 
	 * @see XmlLeaf
	 * @return null if ArrayList is empty.
	 */
	AbstractList<XmlLeaf> getCollectedXML();

	/**
	 * Get read XML elements as an XmlLeaf, under a particular category.
	 * 
	 * @see XmlLeaf
	 * @param category
	 *            category for these values. <br>
	 *            Ie: if
	 * 
	 * <pre>
	 *    
	 *     
	 *      
	 *      
	 *      
	 *      
	 *      
	 *          &lt;root&gt;
	 *           &lt;address&gt;
	 *             &lt;street&gt;Example Road&lt;/street&gt;
	 *             &lt;number&gt;3&lt;/number&gt;
	 *           &lt;/address&gt;
	 *          &lt;/root&gt;
	 *      
	 *      
	 *      
	 *      
	 *       
	 *      
	 *     
	 * </pre>, category is "address".
	 * @return XmlLeaf with category given. If more than one category is
	 *         present, it returns the first one. If category is not found, it
	 *         returns null.
	 */
	XmlLeaf getCollectedXMLLeaf(String category);

}
