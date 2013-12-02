package net.littlelite.jurpedemo;

/**
J.U.R.P.E. @version@ Swing Demo
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
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;
import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.EnabledDisabled;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.resources.ResourceFinder;
import net.littlelite.jurpedemo.frames.JurpeLookAndFeel;

/**
 * Configuration values for net.littlelite.jurpedemo. Static values are initially filled with
 * default values. They may be loaded from an external file
 * DemoConfig.DEMOCONFIGXML. When you use DemoConfig, you initialize also
 * net.littlelite.jurpe.system.Config. The loaded values are the ones of Config and the ones of
 * DemoConfig.
 * 
 * @see JurpeDemoConfig
 */
public class JurpeDemoConfig extends net.littlelite.jurpe.system.Config
{

    /**
     * GUI Window Dimensions
     */
    public static final Dimension FRAMESIZE = new Dimension(605, 530);
    public static final Dimension FRAMESIZE_LARGE = new Dimension(650, 530);
    /**
     * Panel Dimensions
     */
    public static final Dimension PANELSIZE = new Dimension(550, 400);

    /**
     * Tutorial file
     */
    public static String HTMLTUTORIAL = "tutorial.html";
    /**
     * Temporary village file
     */
    public static String TEMPVILLAGEFILE = "village.map";
    /**
     * Name of jurpeicon image
     */
    public static String JURPESWORDS = "jurpe-swords.jpg";
    /**
     * Name of inn image
     */
    public static String INNIMAGE = "newinn.jpg";
    /**
     * Name of jurpe topbar image
     */
    public static String JURPETOPBARJPG = "flame.jpg";
    /**
     * Name of jurpe mages guild image
     */
    public static String MAGESJPG = "guildmage.jpg";
    /**
     * Name of jurpe background image
     */
    public static String JURPEBAKGIF = "jurpebak.gif";
    /**
     * Name of grimoire image
     */
    public static String GRIMOIRE = "grimoire.png";
    /**
     * Name of jurpeicon image
     */
    public static String JURPEJPG = "net.littlelite.jurpe.jpg";
    /**
     * Game Strings
     */
    public static String GAMESTRINGSXML = "gamestrings.xml";
    /**
     * Background color for Windows Look And Feel
     */
    public static String WINDOWSCOLOR = "223,215,132";
    /**
     * Background color for Metal/CDE Look And Feel
     */
    public static String METALCOLOR = "204,204,204";
    /**
     * Startup application Look And Feel.
     */
    public static JurpeLookAndFeel LOOKANDFEEL = JurpeLookAndFeel.LOOKS;
    /**
     * If combat fast log is enabled
     */
    public static EnabledDisabled FASTLOG = EnabledDisabled.ENABLED;

    /**
     * Get RGB coordinates for the 2 default GUI background colors.
     *
     * @param metal
     *            If true, it will return the Metal backround, else the beige
     *            one.
     * @return Color in RGB coordinates
     * @throws JurpeException
     */
    public static Color getBackGroundColor(boolean metal) throws JurpeException
    {
        if (metal)
        {
            return JurpeDemoConfig.getColorFromRGBString(JurpeDemoConfig.METALCOLOR);
        }

        return JurpeDemoConfig.getColorFromRGBString(JurpeDemoConfig.WINDOWSCOLOR);
    }

    public static void saveProperties(Properties props) throws JurpeException
    {
        String propertiesFile = "JurpeDemo.properties";
        String datPropertiesFile = Config.addDatPathTo(propertiesFile);

        try
        {
            FileOutputStream fos = new FileOutputStream(datPropertiesFile);
            props.store(fos, "--");
            fos.close();
        }
        catch (IOException fex)
        {
            throw new JurpeException("Cannot save JurpeDemo.properties: " + fex.getMessage());
        }

    }

    /**
     * Get the JurpeDemo properties
     * @return the properties hash table
     * @throws JurpeException
     */
    public static Properties getProperties() throws JurpeException
    {
        String propertiesFile = "JurpeDemo.properties";
        String datPropertiesFile = Config.addDatPathTo(propertiesFile);

        // If properties file is not found on dat directory,
        // it is extracted from jar and copied
        File f = new File(datPropertiesFile);
        if (!f.exists())
        {
            ResourceFinder.resources().extractResource(propertiesFile, datPropertiesFile);
        }

        Properties ewords = new Properties();

        try
        {
            FileInputStream fis = new FileInputStream(datPropertiesFile);
            ewords.load(fis);
            fis.close();
        }
        catch (IOException fex)
        {
            throw new JurpeException("Missing JurpeDemo.properties: " + fex.getMessage());
        }

        return ewords;
    }

    /**
     * Use this static method to initialize values from the properties file
     * @throws JurpeException
     */
    public static void loadValues() throws JurpeException
    {
        Properties ewords = getProperties();

        // Jurpe values
        CHARACTER_POINTS = Integer.parseInt(ewords.getProperty("Jurpe.CharacterPoints"));
        AVAILABLE_POINTS = Integer.parseInt(ewords.getProperty("Jurpe.AvailablePoints"));
        INITIAL_MONEY = Integer.parseInt(ewords.getProperty("Jurpe.InitialMoney"));
        DATPATH = ewords.getProperty("Jurpe.DatPath");
        SHOPXML = ewords.getProperty("Jurpe.ShopXML");
        SKILLXML = ewords.getProperty("Jurpe.SkillXML");
        DICTIONARYXML = ewords.getProperty("Jurpe.DictionaryXML");
        MAXLOGSIZE = Integer.parseInt(ewords.getProperty("Jurpe.MaxLogSize"));
        DOORSPERCENTAGE = Float.parseFloat(ewords.getProperty("Jurpe.DoorsPercentage"));
        LOOTABLES = Integer.parseInt(ewords.getProperty("Jurpe.Lootables"));

        // Democonfig values
        HTMLTUTORIAL = ewords.getProperty("Jurpe.HtmlTutorial");
        JURPESWORDS = ewords.getProperty("Jurpe.Swords");
        INNIMAGE = ewords.getProperty("Jurpe.Inn");
        JURPETOPBARJPG = ewords.getProperty("Jurpe.TopBar");
        JURPEBAKGIF = ewords.getProperty("Jurpe.Bak");
        JURPEJPG = ewords.getProperty("Jurpe.Logo");
        WINDOWSCOLOR = ewords.getProperty("Jurpe.WindowsColor");
        METALCOLOR = ewords.getProperty("Jurpe.MetalColor");
        FASTLOG = EnabledDisabled.valueOf(ewords.getProperty("Jurpe.FastLog"));
        LOOKANDFEEL = JurpeLookAndFeel.valueOf(ewords.getProperty("Jurpe.LookAndFeel"));

    }

    private static Color getColorFromRGBString(String rgb) throws JurpeException
    {

        StringTokenizer st = new StringTokenizer(rgb, ",");
        int rrr, ggg, bbb = 0;

        try
        {
            rrr = Integer.parseInt(st.nextToken());
            ggg = Integer.parseInt(st.nextToken());
            bbb = Integer.parseInt(st.nextToken());
        }
        catch (NumberFormatException nfe)
        {
            throw new JurpeException("Invalid Windows Background color");
        }

        return new Color(rrr, ggg, bbb);
    }

    private JurpeDemoConfig()
    {
    }
}
