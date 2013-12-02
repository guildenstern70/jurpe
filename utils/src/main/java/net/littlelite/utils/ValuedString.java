package net.littlelite.utils;

/**
 *
 * Alessio Saltarin General Library Utils
 * package name.alessiosaltarin.utils
 * Copyright Alessio Saltarin 2001-2008
 *
 */

/**
 * String with an associated value
 * 
 * 
 */
public class ValuedString
{

	private String strName;

	private short value;

	/**
	 * Constructor for ValuedString object
	 * 
	 * @param name
	 *            Name of Object
	 * @param val
	 *            Value of Object
	 */
	public ValuedString(String name, short val)
	{
		this.strName = name;
		this.value = val;
	}

	/**
	 * Get name of this ValuedString
	 * 
	 * @return name
	 */
	public String getName()
	{
		return this.strName;
	}

	/**
	 * Get value of this ValuedString
	 * 
	 * @return value
	 */
	public short getValue()
	{
		return this.value;
	}

	/**
	 * Brief description of this ValuedString in the form Name (Value)
	 * 
	 * @return Description of this ValuedString in the form Name
	 */
        @Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(this.strName);
		sb.append(" (");
		sb.append(this.value);
		sb.append(")");
		return sb.toString();
	}

	/**
	 * Adds comparability Comparing is made on Value
	 * 
	 * @param rv
	 *            Compared ValuedString
	 * @return 0 if objects are equal (they have the same value). -1 if this
	 *         object has minor cost that rv 1 if this object costs more than rv
	 */
	public int compareTo(Object rv)
	{
		int rvCosto = ((ValuedString) rv).value;
		return (this.value < rvCosto ? -1 : (this.value == rvCosto ? 0 : 1));
	}

}
