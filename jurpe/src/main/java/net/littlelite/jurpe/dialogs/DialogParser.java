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
package net.littlelite.jurpe.dialogs;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX2 implementation of IXMLConverter for metadata managing.
 */
public class DialogParser extends DefaultHandler
{

    private String xmlDialog;
    private String code;
    private DialogUnit unit;
    private String currentTag;
    private String currentValue;
    private String currentGotoTag;
    private String currentKeyTag;

    /**
     * Dialog Parser constructor.
     * @param xmlDialogFile The XML dialog file
     */
    public DialogParser(String xmlDialogFile)
    {
        this.xmlDialog = xmlDialogFile;
    }

    /**
     * Extract the given DialogUnit from the XML file
     * @param unitCode The dialog unit code to be extracted
     * @return The wanted dialog unit
     */
    public DialogUnit getDialogUnit(String unitCode)
    {
        this.reset();
        this.code = unitCode;
        this.parse();
        return this.unit;
    }
    
    /**
     * StartElement SAX overriding method
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        this.currentTag = qName;

        if (qName.equalsIgnoreCase("unit"))
        {
            // It's a new unit.
            String unitCode = this.scanAttributes(attributes, "code");

            if (unitCode.equalsIgnoreCase(this.code))
            {
                this.unit = new DialogUnit(this.code);
                //System.out.println("Found unit " + this.code);
            }

        }
        else if (qName.equalsIgnoreCase("answer"))
        {
            this.currentGotoTag = this.scanAttributes(attributes, "goto");
            this.currentKeyTag = this.scanAttributes(attributes, "key");
        }
    }
    
    /**
     * characters SAX overriding method
     */
    @Override
    public void characters(char[] ch, int start, int length)
    {
        this.currentValue = "";
        String tmpString;

        if (length > 0)
        {
            tmpString = new String(ch, start, length);
            tmpString = tmpString.replace("\n", "");
            tmpString.trim();

            if (!this.isEmptyString(tmpString))
            {
                this.currentValue = tmpString;
            }
        }

        if (this.currentValue.length() > 0)
        {
            //System.out.println("Value: ["+currentValue+"]");
            if (this.unit != null)
            {
                if (this.currentTag.equalsIgnoreCase("question"))
                {
                    this.unit.setQuestion(this.currentValue);
                    //System.out.println("=== Added QUESTION for unit " + this.code + " (" + this.currentValue + ")");
                }
                else if (this.currentTag.equalsIgnoreCase("answer"))
                {
                    this.unit.addAnswer(new Answer(this.currentValue, this.currentKeyTag, this.currentGotoTag));
                    //System.out.println("=== Added ANSWER for unit " + this.code + " (" + this.currentValue + ")");
                }
            }
        }
    }
    
    /**
     * endElement SAX overriding method
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws DoneParsingException
    {
        if (qName.equalsIgnoreCase("unit"))
        {
            // If we are creating a unit, it's time to return it
            if (this.unit != null)
            {
                throw new DoneParsingException();
            }
        }
    }
    
    private void reset()
    {
        this.unit = null;
        this.currentTag = null;
        this.currentValue = null;
        this.currentGotoTag = null;
        this.currentKeyTag = null;
    }

    private String scanAttributes(Attributes attributes, String attributeName)
    {
        String attributeValue = null;

        // Scan unit attributes
        for (int j = 0; j < attributes.getLength(); j++)
        {
            if (attributes.getLocalName(j).equalsIgnoreCase(attributeName))
            {
                attributeValue = attributes.getValue(j);
                break;
            }
        }

        return attributeValue;
    }

    private void parse()
    {
        String fileName = this.xmlDialog;
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setValidating(false);
        XMLReader xmlReader = null;

        try
        {
            SAXParser saxParser = spf.newSAXParser();
            xmlReader = saxParser.getXMLReader();
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }

        xmlReader.setContentHandler(this);
        xmlReader.setErrorHandler(new MyErrorHandler(System.err));

        try
        {
            String uri = convertToFileURL(fileName);
            xmlReader.parse(uri);
        }
        catch (DoneParsingException dpe)
        {
            //System.err.println("Ok, done parsing.");
        }
        catch (SAXException saxe)
        {
            System.err.println(saxe.getMessage());
            System.exit(1);
        }
        catch (IOException ioe)
        {
            System.err.println(ioe.getMessage());
            System.err.println("File " + fileName + " not found?");
            System.exit(1);
        }

    }

    private boolean isEmptyString(String chs)
    {
        boolean isEmpty = true;

        for (char ch : chs.toCharArray())
        {
            if (ch != ' ')
            {
                isEmpty = false;
                break;
            }
        }

        return isEmpty;
    }

    private String convertToFileURL(String filename)
    {
        String url = null;
        try
        {
            File sf = new File(filename);
            url = sf.toURI().toURL().toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return url;
    }

    class DoneParsingException extends SAXException
    {
        static final long serialVersionUID = 32L;
    }

    private static class MyErrorHandler implements ErrorHandler
    {

        private PrintStream out;

        MyErrorHandler(PrintStream out)
        {
            this.out = out;
        }

        private String getParseExceptionInfo(SAXParseException spe)
        {
            String info = " Line=" + spe.getLineNumber() + ": " + spe.getMessage();
            return info;
        }

        public void warning(SAXParseException spe)
        {
            out.println("Warning: " + getParseExceptionInfo(spe));
        }

        public void error(SAXParseException spe) throws SAXException
        {
            String message = "Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }

        public void fatalError(SAXParseException spe) throws SAXException
        {
            String message = "Fatal Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
    }
}

