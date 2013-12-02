package net.littlelite.jurpe.system.resources;

/**
J.U.R.P.E. @version@ Swing Demo
Copyright (C) LittleLite Software

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
import java.io.InputStream;
import java.net.URL;
import javax.swing.ImageIcon;

import net.littlelite.utils.FileManager;

public final class ResourceFinder
{
    /**
     * Resource finder. Explore the resources jar file.
     *
     * @return
     */
    public static ResourceFinder resources()
    {
        return new ResourceFinder();
    }

    /**
     * Private Constructor. Use ResourceFinder.resources() to get an instance of
     * this object
     *
     * @param archive
     */
    private ResourceFinder()
    {
    }

    /**
     * Extract a resource from the jar file and save it to a normal file
     *
     * @param resourcePath
     * @param outputFile
     */
    public void extractResource(String resourcePath, String outputFile)
    {
        InputStream buff = this.getClass().getResourceAsStream("/"+resourcePath);
        if (buff != null)
        {
            FileManager fm = FileManager.getReference();
            if (!fm.copyFile(buff, outputFile))
            {
                System.err.println("Could not extract " + resourcePath + ": " + fm.getErrorMsg());
            }
        }
        else
        {
            System.err.println("Could not extract " + resourcePath + ": resource not found.");
        }

    }

    public InputStream getResource(String resourcePath)
    {
        return this.getClass().getResourceAsStream("/"+resourcePath);
    }
    
    /**
     * Get resource as ImageIcon
     * @param resourcePath the resource path inside the resources jar file
     * @return the resource as an ImageIcon
     */
    public ImageIcon getResourceAsImage(String resourcePath)
    {
        URL resource = this.getClass().getResource("/"+resourcePath);
        ImageIcon image = null;
        if (resource != null)
        {
            image = new ImageIcon(resource);
        }
        else
        {
            System.err.println("Could not get " + resourcePath + " image.");
        }

        return image;
    }



}
