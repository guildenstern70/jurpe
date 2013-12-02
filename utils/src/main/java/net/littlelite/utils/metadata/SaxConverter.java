package net.littlelite.utils.metadata;

/**
 *
 * Alessio Saltarin General Library Utils
 * package name.alessiosaltarin.utils
 * Copyright Alessio Saltarin 2001-2008
 *
 */
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;

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
 * 
 * 
 */
public class SaxConverter extends DefaultHandler implements IXMLConverter
{

    private static AbstractList<XMLLeaf> xmlElements;
    private XMLLeaf currentLeaf;
    private String currentName,  currentValue,  docRoot;

    /**
     * Constructs new XML leaf.
     * 
     * @see XmlLeaf
     */
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
    {

        if (docRoot == null)
        {
            docRoot = qName;
        }
        else if (currentLeaf == null)
        {
            currentLeaf = new XMLLeaf(qName);
        }
        else
        {
            currentName = qName;
        }
    }

    /**
     * Inserts characters.
     */
    @Override
    public void characters(char[] ch, int start, int length)
    {
        if (length > 0)
        {
            currentValue = new String(ch, start, length);
            if (currentValue.equals("\n"))
            {
                currentValue = "";
            }
        }
        else
        {
            currentValue = "[empty]";
        }
    }

    /**
     * Add current element to ArrayList.
     */
    @Override
    public void endElement(String uri, String localName, String qName)
    {

        if (qName.equals(docRoot))
        {
            return;
        }
        else if (qName.equals(currentLeaf.getCategory()))
        {
            xmlElements.add((XMLLeaf) currentLeaf.clone());
            currentLeaf = null;
        }
        else
        {
            currentLeaf.addValue(currentName, currentValue);
            currentName = null;
            currentValue = null;
        }
    }

    /**
     * Parse XML file.
     * 
     * @param fileName
     *            Path to file. It is converted to a URL.
     */
    public void parse(String fileName)
    {
        xmlElements = new ArrayList<XMLLeaf>();
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

    /**
     * Get read XML elements as an ArrayList. The ArrayList contains variables
     * of type XmlLeaf.
     * 
     * @see XmlLeaf
     * @return null if ArrayList is empty.
     */
    public AbstractList<XMLLeaf> getCollectedXML()
    {
        if (SaxConverter.xmlElements == null)
        {
            return new ArrayList<XMLLeaf>();
        }

        return SaxConverter.xmlElements;
    }

    /**
     * Get read XML elements as an XmlLeaf, under a particular category.
     * 
     * @see XmlLeaf
     * @param category
     *            : category for these values. <br>
     *            Ie: if
     * 
     *            <pre>
     * 
     * 
     * 
     *          &lt;root&gt;
     *           	&lt;address&gt;
     *             		&lt;street&gt;Example Road&lt;/street&gt;
     *             		&lt;number&gt;3&lt;/number&gt;
     *           	&lt;/address&gt;
     *          &lt;/root&gt;
     * 
     * 
     * 
     * 
     * </pre>
     * 
     *            , category is "address".
     * @return XmlLeaf with category given. If more than one category is
     *         present, it returns the first one. If category is not found, it
     *         returns null.
     */
    public XMLLeaf getCollectedXMLLeaf(String category)
    {
        if (SaxConverter.xmlElements == null)
        {
            return new XMLLeaf("Unknown");
        }

        XMLLeaf tmp = null;
        AbstractList<XMLLeaf> al = this.getCollectedXML();

        for (XMLLeaf leaf : al)
        {
            if (leaf.getCategory().equals(category))
            {
                tmp = leaf;
            }
            break;
        }

        return tmp;
    }

    /**
     * For test purposes only
     * 
     * @deprecated
     * @param args
     */
    public static void main(String[] args)
    {
        SaxConverter sx = new SaxConverter();
        /*
         * if (args.length <1) { System.out.println("Specify input file:
         * SaxConverter [file]"); System.exit(-1); }
         */
        sx.parse("dat/gamestrings.xml");
        AbstractList<XMLLeaf> al = sx.getCollectedXML();
        Iterator<XMLLeaf> i = al.iterator();
        while (i.hasNext())
        {
            XMLLeaf tmp = i.next();
            System.out.println(tmp);
        }
    }

    /** ****** PRIVATE MEMBERS ***************************** */
    private String convertToFileURL(String filename)
    {
        String url = null;
        try
        {
            File sf = new File(filename);
            url = sf.toURI().toURL().toString();
        // url = sf.toURL().toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return url;
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
