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

package net.littlelite.jurpedemo;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.Core;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpedemo.frames.JurpeMain;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class JurpeDemo extends SingleFrameApplication
{

    // Frame for layout
    private JFrame frame;
    // System class for holding run time objects
    private Core system;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup()
    {
        try
        {
            this.initialize();
            this.system.getLog().addDetail("All done.");
            this.showInterface();
        }
        catch (JurpeException ge)
        {
            System.err.println("Jurpe Exception: " + ge.getMessage());
        }
        catch (Exception e)
        {
            System.err.println(Config.APPNAME + "Generic Exception: " + e.getMessage());
        }

    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of JurpeDemo
     */
    public static JurpeDemo getApplication()
    {
        return Application.getInstance(JurpeDemo.class);
    }

    private void showInterface()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = JurpeDemoConfig.FRAMESIZE;
        this.frame.setLocation((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 2);
        this.frame.setMinimumSize(size);
        this.frame.setResizable(false);
        this.frame.validate();
        this.frame.setVisible(true);
        this.system.getLog().addDetail("System initialized.");
    }

    private void initialize() throws Exception, JurpeException
    {
        // Initialize J.U.R.P.E. @version@ Library
        this.system = new Core();
        this.system.init();

        // Initialize Game DemoConfiguration
        JurpeDemoConfig.loadValues();
        this.system.getLog().addDetail("Configuration values read.");
        
        // Open Game window
        this.frame = new JurpeMain(this.system);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args)
    {
        launch(JurpeDemo.class, args);
    }
}
