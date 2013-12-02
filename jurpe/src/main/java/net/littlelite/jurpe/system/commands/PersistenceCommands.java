package net.littlelite.jurpe.system.commands;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import net.littlelite.jurpe.characters.PCharacter;
import net.littlelite.jurpe.game.session.HighScores;
import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.utils.Serializer;

/**
 * Persistence related commands.
 * 
 * @see net.littlelite.jurpe.system.CoreCommands
 */
public class PersistenceCommands extends GenericCommands
{
	/**
	 * Serialize Playing Character and his references (Inventory, Skills and
	 * such) Extension is taken from Config file.
	 * 
	 * @param pc
	 *            Playing Character
	 * @param namepath
	 *            complete path where you want to save your character, without
	 *            extension.
	 * @return true if character has been correctly saved.
	 */
	public static boolean savePC(PCharacter pc, String namepath)
	{

		boolean okok = true;
		String completePathName = namepath + Config.CHAREXTENSION;

		File serializedPC = new File(completePathName);
		if (Serializer.serialize(serializedPC, pc))
		{
			pc.setSaved();
			log.addEntry("Character saved.");
		}
		else
		{
			okok = false;
			log.addEntry("Failed to save character.");
		}

		return okok;
	}

	/**
	 * Restore serialized PCharacter.
	 * 
	 * @param pcName
	 *            complete path to file with extension.
	 * @return true if character has been correctly restored.
	 * @throws JurpeException
	 */
	public static PCharacter loadPC(String pcName)
	{

		PCharacter pc = null;
		File serializedPC = new File(pcName);

		try
		{
			if (serializedPC.exists())
			{
				log.addDetail("Loading " + pcName);
				pc = (PCharacter) Serializer.deSerialize(serializedPC);
			}
			else
			{
				log.addDetail(pcName + " file does not exist!");
			}
		}
		catch (Exception ioe)
		{
			ioe.printStackTrace();
			log.addEntry("Can't load file " + serializedPC.getAbsolutePath());
		}

		return pc;
	}

	/**
	 * Persist high scores (filename is in Config.xml file)
	 * 
	 * @param hs
	 *            High Score Table
	 * @see HighScores
	 */
	public static void saveHighScores(HighScores hs)
	{
		hs.save(Config.HIGHSCORESFILE);
	}

	/**
	 * Loads High Score Table. If it's not present, it loads a default one.
	 * 
	 * @return Valid High Score handle
	 */
	public static HighScores loadHighScores()
	{
		HighScores highScores;
		String fileToLoad = Config.HIGHSCORESFILE;

		try
		{

			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileToLoad));
			highScores = (HighScores) in.readObject();
                        in.close();
		}
		catch (Exception ioe)
		{
			// We don't excessively worry about that
			highScores = new HighScores();
			highScores.setDefaultHighScores();
		}

		return highScores;

	}

}
