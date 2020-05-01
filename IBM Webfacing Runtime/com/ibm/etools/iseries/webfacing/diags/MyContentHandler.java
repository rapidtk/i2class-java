// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.diags;

import java.io.PrintStream;
import org.xml.sax.*;

public class MyContentHandler
    implements ContentHandler
{

    public MyContentHandler()
    {
    }

    public void setDocumentLocator(Locator locator)
    {
        System.out.println("MyContentHandler.setDocumentLocator(" + locator + ")");
    }

    public void startDocument()
        throws SAXException
    {
        System.out.println("MyContentHandler.startDocument");
    }

    public void endDocument()
        throws SAXException
    {
        System.out.println("MyContentHandler.endDocument");
    }

    public void startPrefixMapping(String s, String s1)
        throws SAXException
    {
        System.out.println("MyContentHandler.startstartPrefixMapping(" + s + ", " + s1 + ")");
    }

    public void endPrefixMapping(String s)
        throws SAXException
    {
        System.out.println("MyContentHandler.endPrefixMapping(" + s + ")");
    }

    public void startElement(String s, String s1, String s2, Attributes attributes)
        throws SAXException
    {
        System.out.println("MyContentHandler.startElement(" + s + ", " + s1 + ", " + s2 + ", " + attributes + ")");
        showAtts(attributes);
    }

    public void endElement(String s, String s1, String s2)
        throws SAXException
    {
        System.out.println("MyContentHandler.endElement(" + s + ", " + s1 + ", " + s2 + ")");
    }

    public void characters(char ac[], int i, int j)
        throws SAXException
    {
    }

    public void ignorableWhitespace(char ac[], int i, int j)
        throws SAXException
    {
    }

    public void processingInstruction(String s, String s1)
        throws SAXException
    {
    }

    public void skippedEntity(String s)
        throws SAXException
    {
    }

    public void showAtts(Attributes attributes)
    {
        if(null != attributes && attributes.getLength() > 0)
        {
            for(int i = 0; i < attributes.getLength(); i++)
            {
                String s = attributes.getLocalName(i);
                String s1 = attributes.getQName(i);
                String s2 = attributes.getType(i);
                String s3 = attributes.getValue(i);
                String s4 = "i = " + i + "::: LocalName = " + s + ", QName = " + s1 + ", Type = " + s2 + ", value = " + s3;
                System.out.println(s4);
            }

        }
    }
}
