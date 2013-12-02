package net.littlelite.utils;

/**
 *
 * Alessio Saltarin General Library Utils
 * package name.alessiosaltarin.utils
 * Copyright Alessio Saltarin 2001-2008
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * 
 */
public class Serializer
{

	public static boolean serialize(File serFile, Object obj)
	{
		boolean okok = true;

		try
		{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(serFile));
			out.writeObject(obj);
			out.close(); // Also flushes output
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			okok = false;
		}

		return okok;
	}

	public static Object deSerialize(File objFile)
	{
		Object deserialized = null;

		try
		{

			ObjectInputStream in = new ObjectInputStream(new FileInputStream(objFile));
			deserialized = in.readObject();
		}
		catch (Exception ioe)
		{
			ioe.printStackTrace();
		}

		return deserialized;
	}

}