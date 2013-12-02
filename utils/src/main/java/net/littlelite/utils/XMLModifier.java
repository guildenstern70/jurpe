package net.littlelite.utils;

/**
 *
 * Alessio Saltarin General Library Utils
 * package name.alessiosaltarin.utils
 * Copyright Alessio Saltarin 2001,2002,2003
 *
 */

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Modify and save nodes in XML files
 */
public class XMLModifier
{

	private String xmlpath;

	Document root;

	/**
	 * Construct a DOM Document reading and parsing xmlFilePath
	 * 
	 * @param xmlFilePath
	 *            XML File to modify
	 */
	public XMLModifier(String xmlFilePath)
	{
		this.xmlpath = xmlFilePath;
		File xmlFile = new File(this.xmlpath);
		this.root = this.loadDocument(xmlFile);
	}

	/**
	 * Get a specific node value. If tag has more than one child, the first one
	 * is returned.
	 * 
	 * @param tagname
	 * @return XML Node Value
	 */
	public String getNodeValue(String tagname)
	{
		Node n = this.getNode(tagname);
		return (n.getNodeValue());
	}

	/**
	 * Set a new value for the specific element named with tagname
	 * 
	 * @param tagname
	 *            The name of the XML node to change
	 * @param newvalue
	 *            New value for the element
	 */
	public void setNodeValue(String tagname, String newvalue)
	{
		Node n = null;
		try
		{
			n = this.getNode(tagname);
			n.setNodeValue(newvalue);
		}
		catch (NullPointerException npe)
		{
			System.err.println("Can't find tag " + tagname + " in " + this.xmlpath);
		}
	}

	/**
	 * Saves a DOM document into the physical XML file.
	 */
	public void saveXML()
	{
		DOMUtils.writeDomToFile(this.xmlpath, this.root);
	}

	/**
	 * Returns the first child node of element named tagname
	 * 
	 * @param tagname
	 *            Name of the element
	 * @return the first child node of element named tagname
	 */
	private Node getNode(String tagname)
	{
		NodeList nl = this.root.getElementsByTagName(tagname);
		return nl.item(0).getFirstChild();
	}

	private Document loadDocument(File xmlfile)
	{
		return DOMUtils.parse(xmlfile);
	}

	/**
	 * For test purposes only.
	 * 
	 * @deprecated
	 * @param args
	 *            arguments
	 */
	public static void main(String[] args)
	{
		XMLModifier xm = new XMLModifier(args[0]);
		// Get Value
		System.out.println("File: " + args[0] + " - Tag: " + args[1]);
		System.out.println("Il valore e' " + xm.getNodeValue(args[1]));
		// Modify value
		System.out.println("Il NUOVO valore sara' " + args[2]);
		xm.setNodeValue(args[1], args[2]);
		System.out.println("Ok, set.");
		// Save new file
		System.out.println("Backing up...");
		FileManager fm = FileManager.getReference();
		fm.copyFile(args[0], args[0] + ".bak");
		System.out.println("Saving file...");
		xm.saveXML();
	}
}
