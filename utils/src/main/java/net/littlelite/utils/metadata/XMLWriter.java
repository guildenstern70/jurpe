package net.littlelite.utils.metadata;

/**
 *
 * Alessio Saltarin General Library Utils
 * package name.alessiosaltarin.utils
 * Copyright Alessio Saltarin 2001-2008
 *
 */

import net.littlelite.utils.*;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;

/**
 * XMLWriter.java
 * 
 * 2001
 * 
 * @since 01/06/2001
 * @version 1.0 Simple standalone XML writer
 */
public class XMLWriter
{

	private String fileCompletePath;
	private String lineFeed;
	private String root;
	private StringBuilder xmlDocument;

	private static final String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

	/**
	 * Constructs new writer.
	 * 
	 * @param fileName
	 *            complete path to output file.
	 */
	public XMLWriter(String fileName)
	{
		root = "";
		lineFeed = System.getProperty("line.separator");
		fileCompletePath = fileName;
		xmlDocument = new StringBuilder(xmlHeader);
		xmlDocument.append(lineFeed);
	}

	/**
	 * Constructs new writer, specifying document root.
	 * 
	 * @param fileName
	 *            complete path to output file
	 * @param docRoot
	 *            document root
	 */
	public XMLWriter(String fileName, String docRoot)
	{
		this(fileName);
		this.setRoot(docRoot);
	}

	/**
	 * Add node to XML file
	 */
	public void addNode(String name)
	{
		xmlDocument.append(this.soleTag(name));
	}

	/**
	 * Add node to XML file
	 */
	public void addNode(String name, AbstractMap<String, String> attributes)
	{
		xmlDocument.append(this.soleTag(name, attributes));
	}

	/**
	 * Add node to XML file
	 */
	public void addNode(String name, String attribute, String value)
	{
		xmlDocument.append(this.soleTag(name, attribute, value));
	}

	/**
	 * Add node to XML file
	 */
	public void addNode(String name, String content)
	{
		xmlDocument.append(this.xmlElement(name, content));
	}

	/**
	 * Add node to XML file
	 */
	public void addNode(String name, AbstractMap<String, String> attributes, String content)
	{
		xmlDocument.append(this.xmlElement(name, attributes, content));
	}

	/**
	 * Add node to XML file
	 */
	public void addNode(String name, String attribute, String value, String content)
	{
		xmlDocument.append(this.xmlElement(name, attribute, value, content));
	}

	/**
	 * Set document root.
	 * 
	 * @param docRoot
	 *            document root tag
	 */
	public void setRoot(String docRoot)
	{
		root = docRoot;
		this.openBranch(root);
	}

	/**
	 * Add closing tag for document root.
	 */
	public void closeRoot()
	{
		xmlDocument.append(this.closeTag(root));
	}

	public void openBranch(String branch)
	{
		xmlDocument.append(this.openTag(branch));
		xmlDocument.append(lineFeed);
	}

	public void closeBranch(String branch)
	{
		xmlDocument.append(lineFeed);
		xmlDocument.append(this.closeTag(branch));
	}

	public void save()
	{
		FileManager fm = FileManager.getReference();
		fm.saveFile(fileCompletePath, xmlDocument.toString());
		System.out.println(lineFeed);
		System.out.println("XML document saved");
	}

        @Override
	public String toString()
	{
		return xmlDocument.toString();
	}

	/**
	 * Tester.
	 * 
	 * @deprecated
	 */
	public static void main(String[] args)
	{
		XMLWriter xml = new XMLWriter("C:\\temp\\prova.xml");
		xml.setRoot("config");
		xml.addNode("titolo", "Prova");
		xml.addNode("versione", "1.23");

		AbstractMap<String, String> hm = new HashMap<String, String>();
		hm.put("href", "http://www.yond.it");
		hm.put("type", "link");

		xml.addNode("fire", hm);
		xml.addNode("glaw", "rock", "123", "prova");
		xml.closeRoot();
		xml.save();
	}

	// ************************ PROTECTED MEMBERS
	// ***************************************************

	protected String xmlElement(String name, String content)
	{

		StringBuilder xmlel = new StringBuilder(openTag(name));
		xmlel.append(content);
		xmlel.append(closeTag(name));
		xmlel.append(lineFeed);

		return xmlel.toString();
	}

	protected String xmlElement(String name, AbstractMap<String, String> attributes, String content)
	{
		StringBuilder result = new StringBuilder("<");
		result.append(name);
		result.append(resolveAttributes(attributes));
		result.append(">");
		result.append(content);
		result.append("</");
		result.append(name);
		result.append(">");
		result.append(lineFeed);
		return result.toString();
	}

	protected String xmlElement(String name, String attribute, String attributeValue, String content)
	{
		StringBuilder result = new StringBuilder("<");
		result.append(name);
		result.append(resolveAttribute(attribute, attributeValue));
		result.append(">");
		result.append(content);
		result.append("</");
		result.append(name);
		result.append(">");
		result.append(lineFeed);
		return result.toString();
	}

	protected String soleTag(String name)
	{
		StringBuilder tag = new StringBuilder("<");
		tag.append(name);
		tag.append("/>");
		tag.append(lineFeed);
		return tag.toString();
	}

	protected String soleTag(String name, AbstractMap<String, String> attributes)
	{
		StringBuilder tag = new StringBuilder("<");
		tag.append(name);
		tag.append(resolveAttributes(attributes));
		tag.append("/>");
		tag.append(lineFeed);
		return tag.toString();
	}

	protected String soleTag(String name, String attribute, String value)
	{
		StringBuilder tag = new StringBuilder("<");
		tag.append(name);
		tag.append(resolveAttribute(attribute, value));
		tag.append("/>");
		tag.append(lineFeed);
		return tag.toString();
	}

	protected String openTag(String content)
	{
		StringBuilder tag = new StringBuilder("<");
		tag.append(content);
		tag.append(">");
		return tag.toString();
	}

	protected String closeTag(String content)
	{
		StringBuilder untag = new StringBuilder();
		untag.append("</");
		untag.append(content);
		untag.append(">");
		return untag.toString();
	}

	// ************************ PRIVATE MEMBERS
	// *****************************************************

	/*
	 * Resolves a dictionary of attributes into a String i.e. [[Cat][1243]] ->
	 * Cat="1234"
	 */
	private String resolveAttributes(AbstractMap<String, String> attributes)
	{
		Iterator<String> i = attributes.keySet().iterator();
		StringBuilder attList = new StringBuilder();
		String attribute = null;

		while (i.hasNext())
		{
			attribute = i.next();
			attList.append(" ");
			attList.append(attribute);
			attList.append("=\"");
			attList.append(attributes.get(attribute));
			attList.append("\"");
		}

		return attList.toString();
	}

	private String resolveAttribute(String attribute, String value)
	{
		StringBuilder result = new StringBuilder(" ");

		result.append(attribute);
		result.append("=");
		result.append("\"");
		result.append(value);
		result.append("\"");

		return result.toString();
	}
}
