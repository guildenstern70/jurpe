package net.littlelite.utils;

/**
 *
 * Alessio Saltarin General Library Utils
 * package name.alessiosaltarin.utils
 * Copyright (C) LittleLite Software
 *
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Saving/loading text files.
 * 
 * 2001/2002/2003
 * 
 * @since 04/2001
 * @version 1.2
 */
public final class FileManager
{

    /**
     * Returns handle to the object (singleton)
     *
     * @return FileManager reference
     */
    public static FileManager getReference()
    {
        return fm;
    }

    /**
     * Returns error message in case of error.
     *
     * @return error message
     */
    public String getErrorMsg()
    {
        return this.errorMsg;
    }

    /*
     * To convert the InputStream to String we use the
     * Reader.read(char[] buffer) method. We iterate until the
     * Reader return -1 which means there's no more data to
     * read. We use the StringWriter class to produce the string.
     */
    public String convertStreamToString(InputStream is)
    {

        try
        {
            if (is != null)
            {
                Writer writer = new StringWriter();

                char[] buffer = new char[1024];
                try
                {
                    Reader reader = new BufferedReader(
                            new InputStreamReader(is, "UTF-8"));
                    int n;
                    while ((n = reader.read(buffer)) != -1)
                    {
                        writer.write(buffer, 0, n);
                    }
                }
                finally
                {
                    is.close();
                }
                return writer.toString();
            }
            else
            {
                return "";
            }
        }
        catch (IOException ex)
        {
            this.errorMsg = "Read Stream Error: " + ex.getMessage();
            return "";
        }

    }

    /**
     * Contents of opened file as a string.
     *
     * @param fileName
     *            complete path to file
     * @return contents of the file
     */
    public String openFile(String fileName)
    {
        String ilFile = "";
        BufferedReader in = null;

        try
        {
            in = new BufferedReader(new FileReader(fileName));

            String linea;
            while ((linea = in.readLine()) != null)
            {
                ilFile += linea + "\n";
            }
        }
        catch (IOException e)
        {
            ilFile = "";
            this.errorMsg = "Open File Error: " + e.getMessage();
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return ilFile;

    }

    public boolean copyFile(InputStream genericInput, String outputFile)
    {

        DataInputStream in = new DataInputStream(new BufferedInputStream(genericInput));
        DataOutputStream out = null;

        try
        {
            out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));

            while (in.available() != 0)
            {
                out.writeByte(in.readByte());
            }

            return true;

        }
        catch (IOException e)
        {
            this.errorMsg = "Copy File Error: " + e.getMessage();
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException ioex)
                {
                    ioex.printStackTrace();
                }
            }

            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException ioex)
                {
                    ioex.printStackTrace();
                }
            }
        }

        this.errorMsg = "Input file does not exist";
        return false;

    }

    /**
     * Copy a file.
     *
     * @param sourceFile
     *            Full path to existing file
     * @param outputFile
     *            Full path to file to be created
     * @return true if file is successfully copied
     * @since September 2002
     */
    public boolean copyFile(String sourceFile, String outputFile)
    {
        try
        {
            InputStream is = new FileInputStream(sourceFile);
            return this.copyFile(is, outputFile);
        }
        catch (FileNotFoundException exc)
        {
            this.errorMsg = "Input file does not exist";
            return false;
        }

    }

    /**
     * Checks if file exists
     *
     * @param fileName
     *            complete path
     * @return true if file exists
     */
    public boolean existFile(String fileName)
    {
        boolean exist;

        try
        {
            @SuppressWarnings("unused")
            FileReader fr = new FileReader(fileName);
            exist = true;
        }
        catch (FileNotFoundException fex)
        {
            exist = false;
        }

        return exist;
    }

    /**
     * Saves a string into a file.
     *
     * @param fileName
     *            complete path to the new file
     * @param fileString
     *            file contents
     * @return true if saving was correctly done
     */
    public boolean saveFile(String fileName, String fileString)
    {
        return this.saveFile(fileName, fileString, false);
    }

    public boolean saveBinaryFile(String fileName, byte[] data)
    {
        FileOutputStream fileoutputstream = null;

        try
        {
            fileoutputstream = new FileOutputStream(fileName);
            for (int i = 0; i < data.length; i++)
            {
                fileoutputstream.write(data[i]);
            }
        }
        catch (IOException e)
        {
            this.errorMsg = "Save File Error: " + e.getMessage();
            return false;
        }
        finally
        {
            if (fileoutputstream != null)
            {
                try
                {
                    fileoutputstream.close();
                }
                catch (Exception exc)
                {
                }
            }
        }

        return true;
    }

    /**
     * Saves a string into a file.
     *
     * @param fileName
     *            complete path to the new file
     * @param fileString
     *            file contents
     * @param append
     *            true if you want to append to an existing file
     * @return true if saving was correctly done
     */
    public boolean saveFile(String fileName, String fileString, boolean append)
    {
        PrintWriter out = null;

        try
        {
            out = new PrintWriter(new BufferedOutputStream(new FileOutputStream(fileName, append)));
            out.print(fileString);

        }
        catch (IOException e)
        {
            this.errorMsg = "Save File Error: " + e.getMessage();
            return false;
        }
        finally
        {
            if (out != null)
            {
                out.close();
            }
        }

        return true;
    }

    /**
     * Appends contents to a file
     *
     * @param fileName
     *            complete path to the new file
     * @param fileString
     *            file contents
     * @return true if saving was correctly done
     */
    public boolean appendToFile(String fileName, String fileString)
    {
        return this.saveFile(fileName, fileString, true);
    }

    /**
     * For test purposes, only
     *
     * @param args
     *            x
     * @deprecated
     */
    @Deprecated
    public static void main(String[] args)
    {
        FileManager fman = FileManager.getReference();
        System.out.print(fman);
    }
    // ****************************************** PRIVATE
    // ******************************************
    private static FileManager fm = new FileManager();

    private FileManager()
    {
        this.errorMsg = "ok";
    }
    private String errorMsg;
}
