// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import org.w3c.dom.*;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            AnyNodeWithComments, RecordNodeEnumeration, FileNode, RecordNode, 
//            AnyNode, XMLParser

public class GroupNode extends AnyNodeWithComments
{

    public RecordNodeEnumeration getRecords()
    {
        if(records != null)
            return new RecordNodeEnumeration(records);
        else
            return null;
    }

    public boolean isEmpty()
    {
        return records == null || records.size() == 0;
    }

    protected GroupNode(AnyNode anynode)
    {
        super(anynode, 21);
    }

    public int getCommentLength()
    {
        return 54;
    }

    public FileNode getFile()
    {
        return (FileNode)getParent();
    }

    public Vector getRecordsVector()
    {
        return records;
    }

    public void restoreFromXML(XMLParser xmlparser, Node node)
    {
        NamedNodeMap namednodemap = node.getAttributes();
        if(namednodemap != null && namednodemap.getLength() > 0)
            super.restoreAttributesFromXML(xmlparser, namednodemap);
        NodeList nodelist = node.getChildNodes();
        if(nodelist != null && nodelist.getLength() > 0)
            super.restoreChildrenFromXML(xmlparser, nodelist);
        else
            return;
        Vector vector = XMLParser.getTags(nodelist, "member");
        if(vector != null)
        {
            Vector vector1 = new Vector();
            for(int i = 0; i < vector.size(); i++)
            {
                NamedNodeMap namednodemap1 = ((Node)vector.elementAt(i)).getAttributes();
                Node node1 = null;
                String s = (node1 = namednodemap1.getNamedItem("name")) != null ? node1.getNodeValue() : null;
                if(s != null && s.length() > 0)
                    vector1.addElement(s);
            }

            if(vector1.size() > 0)
            {
                Vector vector2 = new Vector();
                Vector vector3 = getFile().getRecordsVector();
                for(int j = 0; j < vector1.size(); j++)
                {
                    boolean flag = false;
                    for(int k = 0; !flag && k < vector3.size(); k++)
                        if(((String)vector1.elementAt(j)).equals(((RecordNode)vector3.elementAt(k)).getName()))
                        {
                            flag = true;
                            vector2.addElement(vector3.elementAt(k));
                        }

                }

                if(vector2.size() > 0)
                    setRecords(vector2);
            }
        }
    }

    public void saveAsXML(String s, PrintWriter printwriter)
        throws IOException
    {
        printwriter.println(s + "<" + "group");
        super.saveAttributesAsXML(printwriter);
        printwriter.println(s + ">");
        String s1 = s + "  ";
        if(records != null && records.size() > 0)
        {
            for(int i = 0; i < records.size(); i++)
            {
                RecordNode recordnode = (RecordNode)records.elementAt(i);
                printwriter.println(s1 + "<" + "member" + " " + "name" + "=\"" + recordnode.getName() + '"' + "/>");
            }

        }
        super.saveChildrenAsXML(s1, printwriter);
        printwriter.println(s + "</" + "group" + ">");
    }

    protected void setRecords(Vector vector)
    {
        records = vector;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2001");
    private Vector records;
    protected static final String XML_TAG_GROUP = "group";
    protected static final String XML_TAG_RECORD = "member";

}
