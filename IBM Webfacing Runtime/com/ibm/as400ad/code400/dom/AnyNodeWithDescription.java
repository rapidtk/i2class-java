// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import java.io.IOException;
import java.io.PrintWriter;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            AnyNodeWithKeywords, AnyNode, XMLParser

public abstract class AnyNodeWithDescription extends AnyNodeWithKeywords
{

    public String getDescription()
    {
        return description;
    }

    protected void setDescription(String s)
    {
        description = s;
    }

    public AnyNodeWithDescription(AnyNode anynode, int i)
    {
        super(anynode, i);
    }

    public void restoreAttributesFromXML(XMLParser xmlparser, NamedNodeMap namednodemap)
    {
        super.restoreAttributesFromXML(xmlparser, namednodemap);
        Node node = null;
        String s = (node = namednodemap.getNamedItem("description")) != null ? node.getNodeValue() : null;
        if(s != null)
            setDescription(s);
    }

    public void saveAttributesAsXML(PrintWriter printwriter)
        throws IOException
    {
        super.saveAttributesAsXML(printwriter);
        if(description != null && description.length() > 0)
            printwriter.print(" description=\"" + AnyNode.prepareStringForXML(description) + '"');
    }

    public void saveChildrenAsXML(String s, PrintWriter printwriter)
        throws IOException
    {
        super.saveChildrenAsXML(s, printwriter);
    }

    private String description;
    protected static final String XML_ATTR_DESCRIPTION = "description";
}
