// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import com.ibm.as400ad.code400.dom.constants.ENUM_NodeType;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.convert.util.EventLog;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import org.w3c.dom.*;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            IConstants, FileNode, Logger, XMLParser

public abstract class AnyNode
    implements IConstants, ENUM_NodeType
{

    public AnyNode(int i)
    {
        hasError = false;
        linenum = 0;
        type = (short)i;
    }

    public AnyNode(AnyNode anynode, int i)
    {
        hasError = false;
        linenum = 0;
        parent = anynode;
        type = (short)i;
    }

    public boolean getCanHaveComments()
    {
        return false;
    }

    public boolean getHasError()
    {
        return hasError;
    }

    public int getLineNum()
    {
        return linenum;
    }

    public String getName()
    {
        return name;
    }

    public int getNodeType()
    {
        return type;
    }

    public String getNodeTypeAsString()
    {
        return ENUM_NodeType.NT_STRINGS[type];
    }

    public AnyNode getParent()
    {
        return parent;
    }

    public String getWebName()
    {
        return replaceSpecialCharacters(getName());
    }

    public void logEvent(int i)
    {
        logEvent(i, null);
    }

    public void logEvent(int i, String as[])
    {
        try
        {
            EventLog.getEventLog(FileNode.getFile().getName()).logEvent(i, as, linenum, getName());
        }
        catch(Throwable throwable)
        {
            Logger logger = FileNode.getLogger();
            if(logger != null)
                logger.logMessage(throwable.getMessage());
        }
    }

    public static String prepareStringForXML(String s)
    {
        if(s == null)
            return "";
        if(s.length() == 0)
            return "";
        if(s.length() == 1 && s.charAt(0) == 0)
            return "";
        StringBuffer stringbuffer = new StringBuffer();
        for(int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if(c == '&')
                stringbuffer.append("&amp;");
            else
            if(c == '"')
                stringbuffer.append("&quot;");
            else
            if(c == '\'')
                stringbuffer.append("&apos;");
            else
            if(c == '<')
                stringbuffer.append("&lt;");
            else
            if(c == '>')
                stringbuffer.append("&gt;");
            else
                stringbuffer.append(c);
        }

        return stringbuffer.toString();
    }

    public static final String replaceCharacterWithString(String s, int i, String s1)
    {
        char ac[] = {
            (char)i
        };
        String s2 = replaceStringWithString(s, new String(ac), s1);
        return s2;
    }

    public final String replaceSpecialCharacters(String s)
    {
        String s1 = replaceSpecialCharacters(s, null);
        return s1;
    }

    public final String replaceSpecialCharacters(String s, String s1)
    {
        String s2 = s;
        try
        {
            s2 = WebfacingConstants.replaceSpecialCharacters(s2, s1);
        }
        catch(Throwable throwable)
        {
            s2 = s;
        }
        return s2;
    }

    public static final String replaceStringWithString(String s, String s1, String s2)
    {
        String s3 = WebfacingConstants.replaceSubstring(s, s1, s2);
        return s3;
    }

    public void restoreAttributesFromXML(XMLParser xmlparser, NamedNodeMap namednodemap)
    {
        Node node = null;
        String s = (node = namednodemap.getNamedItem("name")) != null ? node.getNodeValue() : null;
        String s1 = (node = namednodemap.getNamedItem("error")) != null ? node.getNodeValue() : null;
        String s2 = (node = namednodemap.getNamedItem("linenum")) != null ? node.getNodeValue() : null;
        if(s != null)
            setName(s);
        if(s1 != null)
            setHasError(s1.equals("true"));
        if(s2 != null && s2.length() > 0)
            setLineNum(Integer.parseInt(s2));
    }

    public void restoreChildrenFromXML(XMLParser xmlparser, NodeList nodelist)
    {
    }

    public void saveAttributesAsXML(PrintWriter printwriter)
        throws IOException
    {
        printwriter.print(" ");
        if(name != null && name.length() > 0)
            printwriter.print("name=\"" + prepareStringForXML(name) + "\" ");
        printwriter.print("error=\"" + getHasError() + "\" " + "linenum" + "=\"" + Integer.toString(getLineNum()) + '"');
    }

    public void saveChildrenAsXML(String s, PrintWriter printwriter)
        throws IOException
    {
    }

    protected void setHasError(boolean flag)
    {
        hasError = flag;
    }

    protected void setLineNum(int i)
    {
        linenum = i;
    }

    protected void setName(String s)
    {
        name = s;
    }

    protected void setNodeType(int i)
    {
        type = (short)i;
    }

    protected void setParent(AnyNode anynode)
    {
        parent = anynode;
    }

    public String toString()
    {
        return getNodeTypeAsString() + ": " + getName();
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999-2003");
    private static Locale theLocale = Locale.getDefault();
    private static char lang = ' ';
    private String name;
    private short type;
    private AnyNode parent;
    protected static final String EQUALSQUOTE = "=\"";
    private boolean hasError;
    private int linenum;
    static final String NAMESEPARATOR = "$";
    protected static final char QUOTE = 34;
    protected static final String QUOTEBLANK = "\" ";
    static final String SQRSEPARATOR = "$";
    protected static final String XML_ATTR_ERROR = "error";
    protected static final String XML_ATTR_LINENUM = "linenum";
    protected static final String XML_ATTR_NAME = "name";
    protected static final String XML_ATTR_PRIMARYDSZ = "primarydsz";
    protected static final String XML_ATTR_SECONDARYDSZ = "secondarydsz";

}
