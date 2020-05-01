// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import com.ibm.as400ad.code400.dom.constants.IWebSettingType;
import java.io.IOException;
import java.io.PrintWriter;
import org.w3c.dom.*;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            AnyNode, XMLParser

public class WebSettingsNode extends AnyNode
    implements IWebSettingType
{

    public int getType()
    {
        return wstype;
    }

    public boolean isPermanent()
    {
        return isPermanent;
    }

    protected void setValue(String s)
    {
        wsvalue = s;
    }

    protected void setType(int i)
    {
        wstype = i;
    }

    protected void setIsPermanent(boolean flag)
    {
        isPermanent = flag;
    }

    public WebSettingsNode(AnyNode anynode)
    {
        super(anynode, 33);
    }

    public WebSettingsNode(AnyNode anynode, int i, String s, boolean flag)
    {
        super(anynode, 33);
        wstype = i;
        wsvalue = s;
        isPermanent = flag;
    }

    public String getTypeAsString()
    {
        return mapTypeToName(wstype);
    }

    public String getValue()
    {
        return wsvalue;
    }

    public static String mapTypeToName(int i)
    {
        if(i < IWebSettingType.WS_TYPE_NAMES.length)
            return IWebSettingType.WS_TYPE_NAMES[i];
        else
            return "";
    }

    public void restoreFromXML(XMLParser xmlparser, Node node)
    {
        NamedNodeMap namednodemap = node.getAttributes();
        if(namednodemap != null && namednodemap.getLength() > 0)
            super.restoreAttributesFromXML(xmlparser, namednodemap);
        Node node1 = null;
        String s = (node1 = namednodemap.getNamedItem("permanent")) != null ? node1.getNodeValue() : null;
        String s1 = (node1 = namednodemap.getNamedItem("type")) != null ? node1.getNodeValue() : null;
        String s2 = (node1 = namednodemap.getNamedItem("value")) != null ? node1.getNodeValue() : null;
        if(s != null)
            setIsPermanent(s.equals("true"));
        if(s1 != null)
            setType((new Integer(s1)).intValue());
        if(s2 != null)
            setValue(s2);
        NodeList nodelist = node.getChildNodes();
        if(nodelist != null && nodelist.getLength() > 0)
            super.restoreChildrenFromXML(xmlparser, nodelist);
    }

    public void saveAsXML(String s, PrintWriter printwriter)
        throws IOException
    {
        printwriter.print(s + "<" + "websetting");
        super.saveAttributesAsXML(printwriter);
        printwriter.print(" permanent=\"" + isPermanent() + "\" " + "type" + "=\"" + getType() + "\" " + "value" + "=\"" + AnyNode.prepareStringForXML(getValue()) + '"');
        printwriter.println("/>");
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2003, all rights reserved.");
    private String wsvalue;
    private int wstype;
    private boolean isPermanent;
    protected static final String XML_ATTR_PERMANENT = "permanent";
    protected static final String XML_ATTR_TYPE = "type";
    protected static final String XML_ATTR_VALUE = "value";
    protected static final String XML_TAG_WEBSETTING = "websetting";

}
