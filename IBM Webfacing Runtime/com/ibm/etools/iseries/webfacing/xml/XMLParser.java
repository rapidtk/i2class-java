// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.xml;

import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Date;
import java.util.Vector;

// Referenced classes of package com.ibm.etools.iseries.webfacing.xml:
//            XDocument, XMLException, XElement, XAttribute

public class XMLParser
{

    public XMLParser(String s)
    {
        I = null;
        xmlFile = null;
        exc = null;
        XMLerror = false;
        xmlURL = null;
        is = null;
        theDoc = null;
        restoredVersion = 0.0D;
        xmlSaveDate = null;
        xmlFile = s;
    }

    public XMLParser(InputStream inputstream)
    {
        I = null;
        xmlFile = null;
        exc = null;
        XMLerror = false;
        xmlURL = null;
        is = null;
        theDoc = null;
        restoredVersion = 0.0D;
        xmlSaveDate = null;
        is = inputstream;
    }

    public XMLParser(URL url)
    {
        I = null;
        xmlFile = null;
        exc = null;
        XMLerror = false;
        xmlURL = null;
        is = null;
        theDoc = null;
        restoredVersion = 0.0D;
        xmlSaveDate = null;
        xmlURL = url;
    }

    public void error(XMLException xmlexception)
    {
        XMLerror = true;
        setLastException(xmlexception);
    }

    public void fatalError(XMLException xmlexception)
        throws XMLException
    {
        System.out.println("XML Parsing fatal error:" + xmlexception);
        XMLerror = true;
        setLastException(xmlexception);
    }

    public static String getAttrValue(XElement xelement, String s)
    {
        String s1 = "";
        XAttribute xattribute = xelement.getAttribute(s);
        if(xattribute != null)
            s1 = xattribute.getValue();
        return s1;
    }

    public XDocument getDocument()
    {
        return theDoc;
    }

    public Exception getLastException()
    {
        return exc;
    }

    protected static XElement getTag(XElement axelement[], String s)
    {
        Object obj = null;
        XElement xelement1 = null;
        int i = axelement.length;
        for(int j = 0; xelement1 == null && j < i; j++)
        {
            XElement xelement = axelement[j];
            if(xelement.getName().equals(s))
                xelement1 = xelement;
        }

        return xelement1;
    }

    protected static String getTagAttrValue(XElement axelement[], String s, String s1)
    {
        XElement xelement = getTag(axelement, s);
        String s2 = null;
        if(xelement != null)
            s2 = getAttrValue(xelement, s1);
        return s2;
    }

    protected static Vector getTags(XElement axelement[], String s)
    {
        Vector vector = null;
        Object obj = null;
        int i = axelement.length;
        for(int j = 0; j < i; j++)
        {
            XElement xelement = axelement[j];
            if(xelement.getName().equals(s))
            {
                if(vector == null)
                    vector = new Vector();
                vector.addElement(xelement);
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

    public boolean parse()
    {
        boolean flag = true;
        XMLerror = false;
        theDoc = new XDocument();
        try
        {
            if(null != is)
                theDoc.load(is, true);
            else
                flag = false;
        }
        catch(XMLException xmlexception)
        {
            setLastException(xmlexception);
            flag = false;
            return flag;
        }
        catch(Exception exception)
        {
            setLastException(exception);
            flag = false;
            return flag;
        }
        if(XMLerror)
            return false;
        else
            return flag;
    }

    private void setLastException(Exception exception)
    {
        exc = exception;
    }

    public void warning(XMLException xmlexception)
    {
        XMLerror = true;
        setLastException(xmlexception);
    }

    public static final String copyright = "(c) Copyright IBM Corporation 2002. all rights reserved";
    private XDocument I;
    private String xmlFile;
    private Exception exc;
    private boolean XMLerror;
    private URL xmlURL;
    private InputStream is;
    private XDocument theDoc;
    private double restoredVersion;
    private Date xmlSaveDate;
}
