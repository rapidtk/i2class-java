// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import com.ibm.as400ad.code400.dom.constants.ENUM_Color;
import com.ibm.as400ad.code400.dom.constants.ENUM_DisplayAttribute;
import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifierStrings;
import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import org.w3c.dom.*;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            AnyNode, KeywordParm, KeywordParmEnumeration, IndicatorNode, 
//            KeywordParmChar, KeywordParmLong, KeywordParmFloat, KeywordParmString, 
//            DSPATRKeywordNode, XMLParser

public class KeywordNode extends AnyNode
    implements ENUM_Color, ENUM_KeywordIdentifiers, ENUM_DisplayAttribute
{

    public KeywordNode()
    {
        super(3);
        _inherited = false;
        subcolorCache = -99;
        subdspatrCache = -99;
    }

    public KeywordNode(AnyNode anynode)
    {
        super(anynode, 3);
        _inherited = false;
        subcolorCache = -99;
        subdspatrCache = -99;
    }

    protected void addParameter(KeywordParm keywordparm)
    {
        if(parms == null)
            parms = new Vector();
        parms.addElement(keywordparm);
    }

    public KeywordParm findParameterByPosition(int i)
    {
        KeywordParm keywordparm = null;
        if(parms != null && i <= parms.size())
            keywordparm = (KeywordParm)parms.elementAt(i - 1);
        return keywordparm;
    }

    public KeywordParm findParameterByToken(int i)
    {
        KeywordParm keywordparm = null;
        if(parms != null)
        {
            Object obj = null;
            for(int j = 0; keywordparm == null && j < parms.size(); j++)
            {
                KeywordParm keywordparm1 = (KeywordParm)parms.elementAt(j);
                if(keywordparm1.getVarParmToken() == i)
                    keywordparm = keywordparm1;
            }

        }
        return keywordparm;
    }

    public KeywordParm findParameterByType(int i)
    {
        KeywordParm keywordparm = null;
        if(parms != null)
        {
            Object obj = null;
            for(int j = 0; keywordparm == null && j < parms.size(); j++)
            {
                KeywordParm keywordparm1 = (KeywordParm)parms.elementAt(j);
                if(keywordparm1.getParmType() == i)
                    keywordparm = keywordparm1;
            }

        }
        return keywordparm;
    }

    public KeywordParm findSubParameter(int i, int j)
    {
        KeywordParm keywordparm = null;
        KeywordParm keywordparm1 = findParameterByToken(i);
        if(keywordparm1 != null)
        {
            Vector vector = keywordparm1.getSubParms();
            if(vector != null)
            {
                Object obj = null;
                for(int k = 0; keywordparm == null && k < vector.size(); k++)
                {
                    KeywordParm keywordparm2 = (KeywordParm)vector.elementAt(k);
                    if(keywordparm2.getParmType() == j)
                        keywordparm = keywordparm2;
                }

            }
        }
        return keywordparm;
    }

    public String getAsText()
    {
        return asText;
    }

    public int getColorParm()
    {
        return 0;
    }

    public int getColorSubParm()
    {
        if(subcolorCache != -99)
            return subcolorCache;
        KeywordParm keywordparm = findParameterByToken(370);
        if(keywordparm != null)
        {
            KeywordParm keywordparm1 = keywordparm.getFirstSubParm();
            if(keywordparm1 != null)
                subcolorCache = keywordparm1.getVarParmToken();
            else
                subcolorCache = 0;
        } else
        {
            subcolorCache = 0;
        }
        return subcolorCache;
    }

    public int getDisplaySizeCondition()
    {
        return displaySizeCondition;
    }

    public int getDspatrParm()
    {
        return 0;
    }

    public int getDspatrSubParm()
    {
        if(subdspatrCache != -99)
            return subdspatrCache;
        KeywordParm keywordparm = findParameterByToken(371);
        if(keywordparm == null)
            return 0;
        Vector vector = keywordparm.getSubParms();
        subdspatrCache = 0;
        if(vector != null)
        {
            for(int i = 0; i < vector.size(); i++)
            {
                KeywordParm keywordparm1 = (KeywordParm)vector.elementAt(i);
                subdspatrCache |= DSPATRKeywordNode.mapTokenToDspAtr(keywordparm1.getVarParmToken());
            }

        } else
        {
            return 0;
        }
        return subdspatrCache;
    }

    public KeywordParm getFirstParm()
    {
        return getParm(0);
    }

    private IndicatorNode getIndicators()
    {
        return indicators;
    }

    public String getIndicatorString()
    {
        return indicatorString;
    }

    public int getKeywordId()
    {
        return id;
    }

    public String getKeywordIdAsString()
    {
        if(id > 0 && id < ENUM_KeywordIdentifierStrings.TOKEN_STRINGS.length)
            return ENUM_KeywordIdentifierStrings.TOKEN_STRINGS[id];
        else
            return "UNKNOWN";
    }

    public int getKeywordIndType()
    {
        return kwdindtype;
    }

    public short getNumericParameter()
    {
        KeywordParm keywordparm = getFirstParm();
        if(keywordparm != null)
            return keywordparm.getVarNumber();
        else
            return -1;
    }

    public KeywordParm getParm(int i)
    {
        KeywordParm keywordparm = null;
        if(parms != null && parms.size() > i)
            keywordparm = (KeywordParm)parms.elementAt(i);
        return keywordparm;
    }

    public KeywordParmEnumeration getParms()
    {
        return new KeywordParmEnumeration(parms);
    }

    public String getParmsAsString()
    {
        return parmsString;
    }

    public Vector getParmsVector()
    {
        return parms;
    }

    public KeywordParm getSecondParm()
    {
        return getParm(1);
    }

    protected boolean isInherited()
    {
        return _inherited;
    }

    public void logEvent(int i, String as[])
    {
        if(!isInherited())
            super.logEvent(i, as);
    }

    public void restoreFromXML(XMLParser xmlparser, Node node)
    {
        NamedNodeMap namednodemap = node.getAttributes();
        if(namednodemap != null && namednodemap.getLength() > 0)
            super.restoreAttributesFromXML(xmlparser, namednodemap);
        Node node1 = null;
        String s = (node1 = namednodemap.getNamedItem("id")) != null ? node1.getNodeValue() : null;
        String s1 = (node1 = namednodemap.getNamedItem("text")) != null ? node1.getNodeValue() : null;
        String s2 = (node1 = namednodemap.getNamedItem("parmstring")) != null ? node1.getNodeValue() : null;
        String s3 = (node1 = namednodemap.getNamedItem("indstring")) != null ? node1.getNodeValue() : null;
        String s4 = (node1 = namednodemap.getNamedItem("indtype")) != null ? node1.getNodeValue() : null;
        String s5 = (node1 = namednodemap.getNamedItem("dspsizcondition")) != null ? node1.getNodeValue() : null;
        String s6 = (node1 = namednodemap.getNamedItem("inherited")) != null ? node1.getNodeValue() : null;
        if(s6 != null && s6.equalsIgnoreCase("true"))
            setInherited(true);
        if(s != null)
            setKeywordId(Integer.parseInt(s));
        if(s4 != null)
            setKeywordIndType(Integer.parseInt(s4));
        if(s1 != null)
            setAsText(s1);
        setDisplaySizeCondition(2);
        if(s5 != null)
            setDisplaySizeCondition(Integer.parseInt(s5));
        if(s3 != null)
            if(getKeywordIndType() == 3)
            {
                if(s3.equals("*DS3") || s3.equals("0"))
                    setDisplaySizeCondition(0);
                else
                    setDisplaySizeCondition(1);
            } else
            {
                setIndicatorString(s3);
            }
        if(s2 != null)
            setParmsAsString(s2);
        NodeList nodelist = node.getChildNodes();
        if(nodelist != null && nodelist.getLength() > 0)
            super.restoreChildrenFromXML(xmlparser, nodelist);
        else
            return;
        Node node2 = XMLParser.getTag(nodelist, "indicator");
        if(node2 != null)
        {
            IndicatorNode indicatornode = new IndicatorNode(this);
            indicatornode.restoreFromXML(xmlparser, node2);
            setIndicators(indicatornode);
        }
        Vector vector = XMLParser.getTags(nodelist, "parm");
        if(vector != null)
        {
            Vector vector1 = new Vector();
            for(int i = 0; i < vector.size(); i++)
            {
                Object obj = null;
                Node node3 = (Node)vector.elementAt(i);
                Node node4 = null;
                NamedNodeMap namednodemap1 = node3.getAttributes();
                String s7 = (node4 = namednodemap1.getNamedItem("parmtype")) != null ? node4.getNodeValue() : null;
                if(s7 != null)
                {
                    int j = KeywordParm.mapParmType(Integer.parseInt(s7));
                    switch(j)
                    {
                    case 9: // '\t'
                        obj = new KeywordParmChar(this);
                        break;

                    case 7: // '\007'
                        obj = new KeywordParmLong(this);
                        break;

                    case 8: // '\b'
                        obj = new KeywordParmFloat(this);
                        break;

                    case 0: // '\0'
                    case 5: // '\005'
                        obj = new KeywordParmString(this);
                        break;

                    case 1: // '\001'
                    case 2: // '\002'
                    case 3: // '\003'
                    case 4: // '\004'
                    case 6: // '\006'
                    default:
                        obj = new KeywordParm(this);
                        break;
                    }
                } else
                {
                    obj = new KeywordParm(this);
                }
                ((KeywordParm) (obj)).restoreFromXML(xmlparser, node3);
                vector1.addElement(obj);
            }

            if(vector1.size() > 0)
                setParms(vector1);
        }
    }

    public void saveAsXML(String s, PrintWriter printwriter)
        throws IOException
    {
        printwriter.print(s + "<" + "keyword");
        super.saveAttributesAsXML(printwriter);
        String s1 = getKeywordIdAsString();
        s1 = s1.substring(s1.indexOf('_') + 1);
        printwriter.print(" name=\"" + s1 + "\" " + "id" + "=\"" + getKeywordId() + "\" " + "dspsizcondition" + "=\"" + getDisplaySizeCondition() + "\" " + "indtype" + "=\"" + getKeywordIndType() + '"');
        if(asText != null && asText.length() > 0)
        {
            printwriter.println();
            printwriter.print(s + "         " + "text" + "=\"" + AnyNode.prepareStringForXML(asText) + '"');
        }
        if(indicatorString != null)
        {
            printwriter.println();
            printwriter.print(s + "         " + "indstring" + "=\"" + AnyNode.prepareStringForXML(indicatorString) + '"');
        }
        if(parmsString != null && parmsString.length() > 0)
        {
            printwriter.println();
            printwriter.print(s + "         " + "parmstring" + "=\"" + AnyNode.prepareStringForXML(parmsString) + '"');
        }
        printwriter.println(">");
        String s2 = s + "  ";
        if(indicators != null)
            indicators.saveAsXML(s2, printwriter);
        if(parms != null && parms.size() > 0)
        {
            for(int i = 0; i < parms.size(); i++)
                ((KeywordParm)parms.elementAt(i)).saveAsXML(s2, printwriter);

        }
        super.saveChildrenAsXML(s2, printwriter);
        printwriter.println(s + "</" + "keyword" + ">");
    }

    protected void setAsText(String s)
    {
        asText = s;
    }

    protected void setDisplaySizeCondition(int i)
    {
        displaySizeCondition = (short)i;
    }

    protected void setIndicators(IndicatorNode indicatornode)
    {
        indicators = indicatornode;
    }

    protected void setIndicatorString(String s)
    {
        indicatorString = s;
    }

    protected void setInherited(boolean flag)
    {
        _inherited = flag;
    }

    protected void setKeywordId(int i)
    {
        id = (short)i;
    }

    protected void setKeywordIndType(int i)
    {
        kwdindtype = (short)i;
    }

    protected void setParms(Vector vector)
    {
        parms = vector;
    }

    protected void setParmsAsString(String s)
    {
        parmsString = s;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2003, all rights reserved.");
    protected static final String XML_ATTR_DSPSIZCONDITION = "dspsizcondition";
    protected static final String XML_ATTR_ID = "id";
    protected static final String XML_ATTR_INDSTRING = "indstring";
    protected static final String XML_ATTR_INDTYPE = "indtype";
    protected static final String XML_ATTR_PARMSTRING = "parmstring";
    protected static final String XML_ATTR_TEXT = "text";
    protected static final String XML_ATTR_VALUE = "value";
    protected static final String XML_ATTR_INHERITED = "inherited";
    protected static final String XML_TAG_KEYWORD = "keyword";
    protected static final String XML_TAG_PARM = "parm";
    private boolean _inherited;
    private short id;
    private short kwdindtype;
    private short displaySizeCondition;
    private String asText;
    private String indicatorString;
    private String parmsString;
    private IndicatorNode indicators;
    private Vector parms;
    private transient int subcolorCache;
    private transient int subdspatrCache;

}
