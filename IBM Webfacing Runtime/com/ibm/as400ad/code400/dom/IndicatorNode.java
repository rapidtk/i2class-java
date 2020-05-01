// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import java.io.IOException;
import java.io.PrintWriter;
import org.w3c.dom.*;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            AnyNode, XMLParser

public class IndicatorNode extends AnyNode
{

    public String getAsString()
    {
        return asString;
    }

    protected void setAsString(String s)
    {
        asString = s;
    }

    public IndicatorNode(AnyNode anynode)
    {
        super(anynode, 5);
    }

    public void restoreFromXML(XMLParser xmlparser, Node node)
    {
        NamedNodeMap namednodemap = node.getAttributes();
        if(namednodemap != null && namednodemap.getLength() > 0)
            super.restoreAttributesFromXML(xmlparser, namednodemap);
        Node node1 = null;
        String s = (node1 = namednodemap.getNamedItem("text")) != null ? node1.getNodeValue() : null;
        if(s != null)
            setAsString(s);
        NodeList nodelist = node.getChildNodes();
        if(nodelist != null && nodelist.getLength() > 0)
            super.restoreChildrenFromXML(xmlparser, nodelist);
        else
            return;
    }

    public void saveAsXML(String s, PrintWriter printwriter)
        throws IOException
    {
        printwriter.println(s + "<" + "indicator");
        super.saveAttributesAsXML(printwriter);
        printwriter.print(" text=\"" + (asString != null ? asString : "") + '"');
        printwriter.println(s + ">");
        super.saveChildrenAsXML(s, printwriter);
        printwriter.println(s + "</" + "indicator" + ">");
    }

    public String toString()
    {
        return asString;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2003, all rights reserved.");
    private String asString;
    protected static final String XML_ATTR_TEXT = "text";
    protected static final String XML_TAG_INDICATOR = "indicator";

}
