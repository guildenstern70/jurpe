package net.littlelite.utils.metadata;

/**
 *
 * Alessio Saltarin General Library Utils
 * package name.alessiosaltarin.utils
 * Copyright Alessio Saltarin 2001-2008
 *
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * XMLLeaf: metadata container type. A leaf is composed by:
 * <ul>
 * <li>A category (String).
 * <li>A container of elements made by a name and a value.
 * </ul>
 * <br>
 * IE: <br>
 * Category: Shield <br>
 * Container: (Small Shield, 30$), (Large Shield, 34$) <br>
 * <br>
 * 
 * 
 */
public class XMLLeaf implements Cloneable
{
	private String category;
	private Map<String, String> values;

	/**
	 * Constructs a new XML leaf.
	 * 
	 * @param cCategoria
	 *            name of category
	 */
	public XMLLeaf(String elementCategory)
	{
		category = elementCategory;
		values = new HashMap<String, String>();
	}

	/**
	 * Add name/value pair to current leaf.
	 * 
	 * @param name
	 *            name of current item
	 * @param value
	 *            value of current item
	 */
	public void addValue(String name, String value)
	{
		values.put(name, value);
	}

	/**
	 * Get value associated to a particular item (name)
	 * 
	 * @param name
	 *            name of current item
	 * @return value (String)
	 */
	public String getValue(String name)
	{
		return values.get(name);
	}

	/**
	 * Get value associated to a particular item (name)
	 * 
	 * @param name
	 *            name of current item
	 * @return value (int)
	 */
	public int getIntValue(String name)
	{
		return Integer.parseInt(this.getValue(name));
	}

	/**
	 * Get value associated to a particular item (name)
	 * 
	 * @param name
	 *            name of current item
	 * @return value (double)
	 */
	public double getDoubleValue(String name)
	{
		return Double.parseDouble(this.getValue(name));
	}

	/**
	 * Get container of items for this leaf.
	 * 
	 * @return items in this leaf (Set)
	 */
	public Set<String> getNames()
	{
		return this.values.keySet();
	}

	/**
	 * Get category of this leaf.
	 * 
	 * @return category name
	 */
	public String getCategory()
	{
		return this.category;
	}

	/**
	 * Clone this leaf
	 * 
	 * @return cloned leaf
	 */
        @Override
	public Object clone()
	{
		Object o = null;

		try
		{
			o = super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			System.err.println("Can't clone");
		}

		return o;
	}

	/**
	 * Long description of leaf (category and values)
	 */
        @Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(category);
		if (!this.getNames().isEmpty())
		{
			sb.append(":\n");

			for (String sKey : this.values.keySet())
			{
				sb.append("\t");
				sb.append(sKey);
				sb.append("=");
				sb.append(values.get(sKey));
				sb.append("\n");
			}
		}

		return sb.toString();
	}
}
