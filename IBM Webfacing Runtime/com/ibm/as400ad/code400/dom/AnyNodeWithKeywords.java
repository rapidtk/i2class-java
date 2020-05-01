// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifierStrings;
import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import org.w3c.dom.*;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            AnyNodeWithComments, KeywordNode, KeywordParm, KeywordNodeEnumeration, 
//            CHGINPDFTKeywordNode, COLORKeywordNode, DSPATRKeywordNode, DSPMODKeywordNode, 
//            FileNode, KwdMap, XMLParser, AnyNode

public abstract class AnyNodeWithKeywords extends AnyNodeWithComments
    implements ENUM_KeywordIdentifiers
{

    public AnyNodeWithKeywords(AnyNode anynode, int i)
    {
        super(anynode, i);
    }

    protected KeywordNode getFirstKeyword()
    {
        if(keywords != null)
            return (KeywordNode)keywords.elementAt(0);
        else
            return null;
    }

    protected KeywordNode findCompositeKeyword(int i, int j, KeywordParm akeywordparm[])
    {
        KeywordNode keywordnode = null;
        KeywordNode keywordnode1 = findKeyword(this, i);
        if(akeywordparm != null)
            akeywordparm[0] = null;
        while(keywordnode == null && keywordnode1 != null) 
        {
            Vector vector = keywordnode1.getParmsVector();
            Object obj = null;
            boolean flag = false;
            if(vector != null)
            {
                for(int k = 0; !flag && k < vector.size(); k++)
                {
                    KeywordParm keywordparm = (KeywordParm)vector.elementAt(k);
                    if(keywordparm.getVarParmToken() == j)
                    {
                        flag = true;
                        keywordnode = keywordnode1;
                        if(akeywordparm != null)
                            akeywordparm[0] = keywordparm;
                    }
                }

            }
            if(!flag)
                keywordnode1 = findNextKeyword(this, keywordnode1, i);
        }
        return keywordnode;
    }

    protected void setKeywords(Vector vector)
    {
        keywords = vector;
    }

    protected void addKeyword(KeywordNode keywordnode)
    {
        if(keywords == null)
            keywords = new Vector();
        keywords.addElement(keywordnode);
    }

    public static KeywordNode findKeyword(AnyNodeWithKeywords anynodewithkeywords, int i)
    {
        Vector vector = anynodewithkeywords.getKeywordsVector();
        KeywordNode keywordnode = null;
        if(vector != null)
        {
            Object obj = null;
            int j = mapKwdId(i);
            for(int k = 0; keywordnode == null && k < vector.size(); k++)
            {
                KeywordNode keywordnode1 = (KeywordNode)vector.elementAt(k);
                if(keywordnode1.getKeywordId() == j)
                    keywordnode = keywordnode1;
            }

        }
        return keywordnode;
    }

    public KeywordNode findKeywordById(int i)
    {
        return findKeyword(this, i);
    }

    public static KeywordNode findNextKeyword(AnyNodeWithKeywords anynodewithkeywords, KeywordNode keywordnode, int i)
    {
        Vector vector = anynodewithkeywords.getKeywordsVector();
        KeywordNode keywordnode1 = null;
        if(vector != null)
        {
            boolean flag = false;
            Object obj = null;
            int j = mapKwdId(i);
            for(int k = 0; keywordnode1 == null && k < vector.size(); k++)
            {
                KeywordNode keywordnode2 = (KeywordNode)vector.elementAt(k);
                if(keywordnode2 == keywordnode)
                    flag = true;
                else
                if(flag && keywordnode2.getKeywordId() == j)
                    keywordnode1 = keywordnode2;
            }

        }
        return keywordnode1;
    }

    public KeywordNode findNextKeywordById(KeywordNode keywordnode, int i)
    {
        return findNextKeyword(this, keywordnode, i);
    }

    public KeywordNodeEnumeration getKeywords()
    {
        if(keywords != null)
            return new KeywordNodeEnumeration(getKeywordsVector());
        else
            return null;
    }

    public KeywordNodeEnumeration getKeywordsOfType(int i)
    {
        Vector vector = new Vector();
        KeywordNodeEnumeration keywordnodeenumeration = null;
        for(KeywordNode keywordnode = findKeyword(this, i); keywordnode != null; keywordnode = findNextKeyword(this, keywordnode, i))
            vector.addElement(keywordnode);

        keywordnodeenumeration = new KeywordNodeEnumeration(vector);
        return keywordnodeenumeration;
    }

    public Vector getKeywordsVector()
    {
        return keywords;
    }

    public KeywordNodeEnumeration getWindowBorderKeywords()
    {
        return getKeywordsOfType(224);
    }

    public static int mapKwdId(int i)
    {
        int j = i;
        char c = FileNode.getFile().getDDSType();
        switch(c)
        {
        case 68: // 'D'
            j = KwdMap.MAPDSPF(i);
            break;

        case 82: // 'R'
            j = KwdMap.MAPPRTF(i);
            break;

        case 80: // 'P'
            j = KwdMap.MAPPF(i);
            break;

        case 76: // 'L'
            j = KwdMap.MAPLF(i);
            break;

        case 73: // 'I'
            j = KwdMap.MAPICF(i);
            break;
        }
        return j;
    }

    public void restoreChildrenFromXML(XMLParser xmlparser, NodeList nodelist)
    {
        super.restoreChildrenFromXML(xmlparser, nodelist);
        Vector vector = XMLParser.getTags(nodelist, "keyword");
        if(vector != null)
        {
            Vector vector1 = new Vector();
            for(int i = 0; i < vector.size(); i++)
            {
                Node node = (Node)vector.elementAt(i);
                Node node1 = null;
                NamedNodeMap namednodemap = node.getAttributes();
                String s = (node1 = namednodemap.getNamedItem("id")) != null ? node1.getNodeValue() : null;
                Object obj = null;
                if(s != null)
                {
                    int j = Integer.parseInt(s);
                    xmlparser.logMessage(" RESTORING: <keyword> " + ENUM_KeywordIdentifierStrings.TOKEN_STRINGS[j]);
                    switch(KwdMap.MAP(j))
                    {
                    case 69: // 'E'
                        obj = new CHGINPDFTKeywordNode(this);
                        break;

                    case 77: // 'M'
                        obj = new COLORKeywordNode(this);
                        break;

                    case 88: // 'X'
                        obj = new DSPATRKeywordNode(this);
                        break;

                    case 89: // 'Y'
                        obj = new DSPMODKeywordNode(this);
                        break;

                    default:
                        obj = new KeywordNode(this);
                        break;
                    }
                } else
                {
                    obj = new KeywordNode(this);
                }
                ((KeywordNode) (obj)).restoreFromXML(xmlparser, node);
                vector1.addElement(obj);
            }

            if(vector1.size() > 0)
                setKeywords(vector1);
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
        if(keywords != null && keywords.size() > 0)
        {
            for(int i = 0; i < keywords.size(); i++)
                ((KeywordNode)keywords.elementAt(i)).saveAsXML(s, printwriter);

        }
    }

    public KeywordNode findKeywordById(int i, int j, int k)
    {
        KeywordNodeEnumeration keywordnodeenumeration = getKeywordsOfType(i);
        KeywordNode keywordnode = null;
        boolean flag = j == k;
        while(keywordnodeenumeration.hasMoreElements()) 
        {
            KeywordNode keywordnode1 = keywordnodeenumeration.nextKeyword();
            if(keywordnode1 != null)
            {
                int l = keywordnode1.getDisplaySizeCondition();
                if(flag)
                {
                    if(l == j || l == 2)
                        return keywordnode1;
                } else
                {
                    if(l == j)
                        return keywordnode1;
                    keywordnode = keywordnode1;
                }
            }
        }
        return keywordnode;
    }

    public KeywordNode findKeywordById(int i, int j, boolean flag)
    {
        for(KeywordNodeEnumeration keywordnodeenumeration = getKeywordsOfType(i); keywordnodeenumeration.hasMoreElements();)
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            if(keywordnode != null && (keywordnode.getDisplaySizeCondition() == j || !flag && keywordnode.getDisplaySizeCondition() == 2))
                return keywordnode;
        }

        return null;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000");
    private Vector keywords;

}
