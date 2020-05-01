// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import java.util.Date;
import java.util.Vector;
import org.apache.xerces.parsers.AbstractDOMParser;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.*;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            ElapsedTime, FileNode, Logger, ExportSettings

public class XMLParser
    implements ErrorHandler
{

    public XMLParser(String s, Logger logger1)
    {
        ddsDoc = null;
        xmlFile = null;
        logger = null;
        exc = null;
        XMLerror = false;
        tmpDirectory = null;
        wfDirectory = null;
        restoredVersion = 0.0D;
        xmlSaveDate = null;
        xmlFile = s;
        logger = logger1;
    }

    public void error(SAXParseException saxparseexception)
    {
        logException("XML Parsing error", saxparseexception);
        XMLerror = true;
        setLastException(saxparseexception);
    }

    public void fatalError(SAXParseException saxparseexception)
        throws SAXException
    {
        logException("XML Parsing fatal error", saxparseexception);
        XMLerror = true;
        setLastException(saxparseexception);
    }

    protected String getAttrValue(Node node, String s)
    {
        Object obj = null;
        Object obj1 = null;
        String s2 = null;
        NamedNodeMap namednodemap = node.getAttributes();
        if(namednodemap != null && namednodemap.getLength() > 0)
        {
            Node node1 = namednodemap.getNamedItem(s);
            String s1;
            if(node1 != null)
                s1 = node1.getNodeValue();
        }
        return s2;
    }

    public Document getDocument()
    {
        return ddsDoc;
    }

    public String getFileName()
    {
        if(ddsDoc == null)
            return null;
        String s = null;
        NodeList nodelist = ddsDoc.getElementsByTagName("file");
        if(nodelist != null && nodelist.getLength() == 1)
            s = getTagAttrValue(nodelist, "file", "name");
        return s;
    }

    public Exception getLastException()
    {
        return exc;
    }

    protected String getNodeTypeString(int i)
    {
        switch(i)
        {
        case 2: // '\002'
            return "ATTRIBUTE_NODE";

        case 4: // '\004'
            return "CDATA_SECTION_NODE";

        case 8: // '\b'
            return "COMMENT_NODE";

        case 11: // '\013'
            return "DOCUMENT_FRAGMENT_NODE";

        case 9: // '\t'
            return "DOCUMENT_NODE";

        case 10: // '\n'
            return "DOCUMENT_TYPE_NODE";

        case 1: // '\001'
            return "ELEMENT_NODE";

        case 6: // '\006'
            return "ENTITY_NODE";

        case 5: // '\005'
            return "ENTITY_REFERENCE_NODE";

        case 12: // '\f'
            return "NOTATION_NODE";

        case 7: // '\007'
            return "PROCESSING_INSTRUCTION_NODE";

        case 3: // '\003'
            return "TEXT_NODE";
        }
        return "unknown";
    }

    protected static Node getTag(NodeList nodelist, String s)
    {
        Object obj = null;
        Node node1 = null;
        int i = nodelist.getLength();
        for(int j = 0; node1 == null && j < i; j++)
        {
            Node node = nodelist.item(j);
            if(node.getNodeName().equals(s))
                node1 = node;
        }

        return node1;
    }

    protected String getTagAttrValue(NodeList nodelist, String s, String s1)
    {
        Node node = getTag(nodelist, s);
        String s2 = null;
        if(node != null)
            s2 = getAttrValue(node, s1);
        return s2;
    }

    protected static Vector getTags(NodeList nodelist, String s)
    {
        Vector vector = null;
        Object obj = null;
        int i = nodelist.getLength();
        for(int j = 0; j < i; j++)
        {
            Node node = nodelist.item(j);
            if(node.getNodeName().equals(s))
            {
                if(vector == null)
                    vector = new Vector();
                vector.addElement(node);
            }
        }

        return vector;
    }

    public String getXMLFileName()
    {
        return xmlFile;
    }

    public double getXMLRestoredVersion()
    {
        return restoredVersion;
    }

    public Date getXMLSaveDate()
    {
        return xmlSaveDate;
    }

    protected void logErrorMessage(String s)
    {
        logger.logErrorMessage(s);
    }

    protected void logException(String s, Exception exception)
    {
        logger.logException(s, exception, false);
    }

    protected void logMessage(String s)
    {
        logger.logMessage(s);
    }

    public boolean parse()
    {
        boolean flag = true;
        ElapsedTime elapsedtime = new ElapsedTime();
        elapsedtime.setStartTime();
        XMLerror = false;
        DOMParser domparser = new DOMParser();
        try
        {
            domparser.setFeature("http://xml.org/sax/features/validation", false);
            domparser.setErrorHandler(this);
            domparser.parse(xmlFile);
        }
        catch(SAXException saxexception)
        {
            logException("Error parsing xml", saxexception);
            setLastException(saxexception);
            flag = false;
            return flag;
        }
        catch(Exception exception)
        {
            logException("Error opening xml file " + xmlFile, exception);
            setLastException(exception);
            flag = false;
            return flag;
        }
        if(XMLerror)
        {
            logErrorMessage("Error during parse");
            return false;
        } else
        {
            ddsDoc = domparser.getDocument();
            elapsedtime.setEndTime();
            logMessage("Parsing done. Ok? " + flag + ". " + elapsedtime);
            return flag;
        }
    }

    public FileNode populate()
    {
        if(ddsDoc == null)
            return null;
        boolean flag = true;
        ElapsedTime elapsedtime = new ElapsedTime();
        elapsedtime.setStartTime();
        FileNode filenode = new FileNode(0);
        FileNode.setLogger(logger);
        NodeList nodelist = ddsDoc.getElementsByTagName("dds");
        if(nodelist != null && nodelist.getLength() > 0)
        {
            Node node = nodelist.item(0);
            NamedNodeMap namednodemap = node.getAttributes();
            Node node1 = namednodemap.getNamedItem("version");
            if(node1 != null)
                try
                {
                    Double double1 = new Double(node1.getNodeValue());
                    restoredVersion = double1.doubleValue();
                }
                catch(NumberFormatException numberformatexception)
                {
                    logErrorMessage("Error formatting version attr in <dds> tag");
                }
            Node node2 = namednodemap.getNamedItem("savedate");
            if(node2 != null)
                try
                {
                    xmlSaveDate = new Date((new Long(node2.getNodeValue())).longValue());
                }
                catch(NumberFormatException numberformatexception1)
                {
                    logErrorMessage("Error formatting savedate attr in <dds> tag");
                }
            NodeList nodelist1 = ddsDoc.getElementsByTagName("transientdata");
            if(nodelist1 != null && nodelist1.getLength() > 0)
            {
                Node node3 = nodelist1.item(0);
                ExportSettings exportsettings1 = ExportSettings.getExportSettings();
                exportsettings1.restoreFromXML(this, node3);
                filenode.setExportSettings(exportsettings1);
            } else
            if(wfDirectory != null && tmpDirectory != null)
            {
                ExportSettings exportsettings = ExportSettings.getExportSettings();
                exportsettings.setWFDirectory(wfDirectory);
                exportsettings.setRootExportDirectory(tmpDirectory);
                filenode.setExportSettings(exportsettings);
            } else
            {
                logErrorMessage("Xml tag not present: <transientdata>");
            }
            NodeList nodelist2 = ddsDoc.getElementsByTagName("file");
            if(nodelist2 == null || nodelist2.getLength() != 1)
            {
                logErrorMessage("Error restoring xml: exactly one <file> tag not found");
                flag = false;
            } else
            {
                filenode.restoreFromXML(this, nodelist2.item(0));
                filenode.postPopulate();
            }
        } else
        {
            flag = false;
            logException("Error in xml file", new Exception("it is empty"));
            return null;
        }
        elapsedtime.setEndTime();
        logger.logMessage("Populating done. Ok? " + flag + ". " + elapsedtime);
        return flag ? filenode : null;
    }

    private void setLastException(Exception exception)
    {
        exc = exception;
    }

    public void warning(SAXParseException saxparseexception)
    {
        logException("XML Parsing warning", saxparseexception);
        XMLerror = true;
        setLastException(saxparseexception);
    }

    public void setTransientData(String s, String s1)
    {
        wfDirectory = s;
        tmpDirectory = s1;
    }

    private Document ddsDoc;
    private String xmlFile;
    private Logger logger;
    private Exception exc;
    private boolean XMLerror;
    private String tmpDirectory;
    private String wfDirectory;
    private double restoredVersion;
    private Date xmlSaveDate;
}
