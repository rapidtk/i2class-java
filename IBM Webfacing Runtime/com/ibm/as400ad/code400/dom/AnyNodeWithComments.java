// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            AnyNode, WebSettingsNodeEnumeration, WebSettingsNode, XMLParser

public abstract class AnyNodeWithComments extends AnyNode
{

    public AnyNodeWithComments(AnyNode anynode, int i)
    {
        super(anynode, i);
    }

    public String getComment(int i)
    {
        if(comments != null)
        {
            if(i < comments.length)
                return comments[i];
            else
                return null;
        } else
        {
            return null;
        }
    }

    public int getCommentLength()
    {
        return 73;
    }

    public int getWebSettingsCount()
    {
        int i = 0;
        if(webSettings != null)
            i = webSettings.size();
        return i;
    }

    public boolean getCanHaveComments()
    {
        return true;
    }

    public int getCommentCount()
    {
        if(comments == null)
            return 0;
        else
            return comments.length;
    }

    public String[] getComments()
    {
        return comments;
    }

    public WebSettingsNodeEnumeration getWebSettings()
    {
        if(getWebSettingsCount() > 0)
            return new WebSettingsNodeEnumeration(webSettings);
        else
            return null;
    }

    public Vector getWebSettingsVector()
    {
        return webSettings;
    }

    public boolean hasWebSettings()
    {
        return getWebSettingsCount() > 0;
    }

    public void restoreChildrenFromXML(XMLParser xmlparser, NodeList nodelist)
    {
        super.restoreChildrenFromXML(xmlparser, nodelist);
        Vector vector = XMLParser.getTags(nodelist, "comment");
        if(vector != null)
        {
            String as[] = new String[vector.size()];
            for(int i = 0; i < vector.size(); i++)
            {
                Node node = (Node)vector.elementAt(i);
                as[i] = xmlparser.getAttrValue(node, "value");
            }

            setComments(as);
        }
        Vector vector1 = XMLParser.getTags(nodelist, "websetting");
        if(vector1 != null)
        {
            Vector vector2 = new Vector();
            for(int j = 0; j < vector1.size(); j++)
            {
                WebSettingsNode websettingsnode = new WebSettingsNode(this);
                websettingsnode.restoreFromXML(xmlparser, (Node)vector1.elementAt(j));
                vector2.addElement(websettingsnode);
            }

            if(vector2.size() > 0)
                setWebSettings(vector2);
        }
    }

    public void saveAttributesAsXML(PrintWriter printwriter)
        throws IOException
    {
        super.saveAttributesAsXML(printwriter);
    }

    public void saveChildrenAsXML(String s, PrintWriter printwriter)
        throws IOException
    {
        super.saveChildrenAsXML(s, printwriter);
        if(comments != null && comments.length > 0)
        {
            for(int i = 0; i < comments.length; i++)
                printwriter.println(s + "<" + "comment" + " value=" + '"' + AnyNode.prepareStringForXML(comments[i]) + '"' + ">");

        }
        if(getWebSettingsCount() > 0)
        {
            for(int j = 0; j < webSettings.size(); j++)
                ((WebSettingsNode)webSettings.elementAt(j)).saveAsXML(s, printwriter);

        }
    }

    protected void setComments(Vector vector)
    {
        if(vector != null && vector.size() > 0)
        {
            String as[] = new String[vector.size()];
            for(int i = 0; i < vector.size(); i++)
                as[i] = (String)vector.elementAt(i);

            comments = as;
        }
    }

    protected void setComments(String as[])
    {
        comments = as;
    }

    protected void setWebSettings(Vector vector)
    {
        webSettings = vector;
    }

    private String comments[];
    private Vector webSettings;
    protected static final String XML_TAG_COMMENT = "comment";
}
