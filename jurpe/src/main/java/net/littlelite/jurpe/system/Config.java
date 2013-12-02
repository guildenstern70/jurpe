package net.littlelite.jurpe.system;

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
import java.awt.Color;

/**
 * This class contains configuration strings.
 * 
 * 
 * @todo make a difference between API needed files and demo needed files.
 */
public class Config
{

    /***************************************************************************
     * The following are general J.U.R.P.E. Library Attributes (should never be
     * changed/overriden)
     */
    /**
     * Application Name
     */
    public static final String APPNAME = "J.U.R.P.E.";
    /**
     * File in which serialize playing Character
     */
    public static final String SYSTEMFILE = "character";
    /**
     * Extension for character types
     */
    public static final String CHAREXTENSION = ".chr";
    /**
     * Version
     */
    public static final String VERSION = "v.0.4.17";
    /**
     * Resources jar file
     */
    public static final String RESOURCES = "jurperes.jar";
    /***************************************************************************
     * The following are default game values (can be overriden in inheriting
     * classes) and loaded from an external file
     */
    /**
     * PCharacter Points At Start
     */
    public static int CHARACTER_POINTS = 100;
    // Starting points when character is created
    /**
     * PCharacter points available
     */
    public static int AVAILABLE_POINTS = 100;
    // Starting points available to each generated playing character
    /**
     * Initial Money
     */
    public static int INITIAL_MONEY = 500;
    /**
     * Path in which application can find data (ie: xml files)
     */
    public static String DATPATH = "dat";
    /**
     * Name of Bottega.xml which contains Shop.
     */
    public static String SHOPXML = "shop.xml";
    /**
     * Name of file Abilita.xml
     */
    public static String SKILLXML = "skill.xml";
    /**
     * Complete path to tutorial file
     */
    public static String HIGHSCORESFILE = "HighScores.bin";
    /**
     * Name of jurpe background image
     */
    public static String MAGESGUILDDIALOG = "magesguild.xml";
    /**
     * Complete path to monster dictionary
     */
    public static String DICTIONARYXML = "dictionary.xml";
    /**
     * Maximunum level (in CP) for monsters in the bestiary
     */
    public static int MAXMONSTERLEVEL = 300;
    /**
     * Maximum number of rows in the system log
     */
    public static int MAXLOGSIZE = 1000;
    /**
     * Maximum number of monsters in the bestiary
     */
    public static int AVAILABLEMONSTERSPICS = 10;
    /**
     * Percentage of doors in dungeon
     */
    public static float DOORSPERCENTAGE = 0.4f;
    /**
     * Max number of lootables per dungeon level
     */
    public static int LOOTABLES = 5;
    /***************************************************************************
     * The following are default game values (can be overriden in inheriting
     * classes) but cannot be loaded from external XML file
     */
    /**
     * Dungeon dimensions
     */
    public static final int INITIAL_DUNGEON_LEVELS = 5;
    public static final short DUNGEON_WIDTH = 30; // dungeon map width in cells
    public static final short DUNGEON_HEIGHT = 25; // dungeon map height in cells
    public static final short VILLAGE_WIDTH = 22; // village map width in cells
    public static final short VILLAGE_HEIGHT = 15; // village map height in cells
    public static final int HEXCELL_HEIGHT = 8; // Hexagon geometry: 4 hexcells in height
    public static final int HEXCELL_WIDTH = 6; // 6 hexcells in width
    public static final int HEXAGON_HEIGHT = HEXCELL_HEIGHT * 2; // single hexagon height in pixels
    public static final int HEXAGON_WIDTH = HEXCELL_WIDTH * 3; // single hexagon width in pixels
    /**
     * Dungeon locations colors
     */
    public static final Color SHOP_COLOR = Color.YELLOW;
    public static final Color TRAINER_COLOR = new Color(227, 157, 229);
    public static final Color STAIRS_UP_COLOR = new Color(183, 233, 232);
    public static final Color STAIRS_DOWN_COLOR = new Color(60, 60, 62);
    public static final Color TREE_COLOR = new Color(171, 200, 172);
    public static final Color HOUSE_COLOR = new Color(246, 219, 186);
    public static final Color INN_COLOR = new Color(213, 92, 27);
    public static final Color MAGES_COLOR = new Color(54, 231, 123);
    public static final Color SIGN_COLOR = new Color(128, 128, 255);
    /**
     * Avatar and Dungeon Colors
     */
    public static final Color AVATAR_FORE = Color.BLUE;
    public static final Color MAZE_BACKGROUND = Color.WHITE;
    public static final Color GUNSIGHT_FORE = Color.CYAN;
    public static final Color MONSTER_FORE = Color.RED;
    public static final Color WALL_COLOR = Color.LIGHT_GRAY;
    public static final Color VISITED_COLOR = new Color(0.88f, 0.88f, 0.88f);
    public static final Color DOOR_COLOR = new Color(0.9f, 0.4f, 0.2f);
    public static final Color HEXAGON_COLOR = Color.GRAY;

    /**
     * Add to a file name the pre-configured path. IE:
     * Config.addDatPathTo(DemoConfig.BOTTEGAXML) = "dat/bottega.xml"
     *
     * @param resource
     *            file name without path
     * @return platform independant complete file path to resource
     */
    public static String addDatPathTo(String resource)
    {
        StringBuilder sb = new StringBuilder(DATPATH);
        sb.append(OSProps.FILESEPARATOR);
        sb.append(resource);
        return sb.toString();
    }

}
