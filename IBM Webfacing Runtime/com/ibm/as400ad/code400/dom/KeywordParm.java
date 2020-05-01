// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifierStrings;
import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordParmType;
import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordParmValType;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import org.w3c.dom.*;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            AnyNode, KeywordParmChar, KeywordParmLong, KeywordParmFloat, 
//            KeywordParmString, XMLParser

public class KeywordParm extends AnyNode
    implements ENUM_KeywordParmType, ENUM_KeywordParmValType
{

    public KeywordParm(AnyNode anynode)
    {
        super(anynode, 4);
    }

    protected void addSubParm(KeywordParm keywordparm)
    {
        if(subParms == null)
            subParms = new Vector();
        subParms.addElement(keywordparm);
    }

    public int getDataType()
    {
        return datatype;
    }

    public KeywordParm getFirstSubParm()
    {
        KeywordParm keywordparm = null;
        if(subParms != null && subParms.size() > 0)
            keywordparm = (KeywordParm)subParms.elementAt(0);
        return keywordparm;
    }

    public String getJavaString()
    {
        return WebfacingConstants.getJavaString(getVarStringUnquoted());
    }

    public String getXMLString()
    {
        return WebfacingConstants.getXMLString(getVarStringUnquoted());
    }

    public int getParmType()
    {
        return parmtype;
    }

    public int getParmValType()
    {
        return mapParmType(getParmType());
    }

    public String getParmValTypeAsString()
    {
        return ENUM_KeywordParmValType.PI_STRINGS[getParmValType()];
    }

    public Vector getSubParms()
    {
        return subParms;
    }

    public int getVarBarCode()
    {
        return var;
    }

    public char getVarChar()
    {
        return (char)var;
    }

    public int getVarCmdKey()
    {
        return var;
    }

    public int getVarEvent()
    {
        return var;
    }

    public float getVarFloatNumber()
    {
        return -1F;
    }

    public int getVarKwdToken()
    {
        return var;
    }

    public String getVarKwdTokenAsString()
    {
        int i = getVarKwdToken();
        if(i > 0 && i < ENUM_KeywordIdentifierStrings.TOKEN_STRINGS.length)
            return ENUM_KeywordIdentifierStrings.TOKEN_STRINGS[i];
        else
            return null;
    }

    public long getVarLongNumber()
    {
        return -1L;
    }

    public short getVarNumber()
    {
        return (short)var;
    }

    public int getVarParmToken()
    {
        return var;
    }

    public int getVarRelOp()
    {
        return var;
    }

    public String getVarString()
    {
        return null;
    }

    public String getVarStringUnquoted()
    {
        String s = getVarString().trim();
        int i = s.length();
        int j = s.indexOf('\'');
        if(j != -1 && s.charAt(i - 1) == '\'')
            i--;
        return s.substring(j + 1, i);
    }

    public String getVarUnknown()
    {
        return null;
    }

    public String getVarValueAsString()
    {
        return Integer.toString(var);
    }

    public static int mapParmType(int i)
    {
        byte byte0 = 0;
        switch(i)
        {
        case 0: // '\0'
        case 3: // '\003'
        case 4: // '\004'
        case 5: // '\005'
        case 6: // '\006'
        case 7: // '\007'
        case 8: // '\b'
        case 9: // '\t'
        case 10: // '\n'
        case 11: // '\013'
        case 14: // '\016'
        case 15: // '\017'
        case 16: // '\020'
        case 17: // '\021'
        case 19: // '\023'
        case 20: // '\024'
        case 21: // '\025'
        case 24: // '\030'
        case 28: // '\034'
        case 29: // '\035'
        case 30: // '\036'
        case 31: // '\037'
        case 33: // '!'
        case 34: // '"'
        case 35: // '#'
        case 38: // '&'
        case 41: // ')'
        case 42: // '*'
        case 47: // '/'
        case 54: // '6'
        case 62: // '>'
        case 63: // '?'
        case 64: // '@'
        case 65: // 'A'
        case 66: // 'B'
        case 68: // 'D'
        case 69: // 'E'
        case 70: // 'F'
        case 74: // 'J'
        case 75: // 'K'
        case 76: // 'L'
        case 77: // 'M'
        case 78: // 'N'
        case 79: // 'O'
        case 80: // 'P'
        case 82: // 'R'
        case 88: // 'X'
        case 89: // 'Y'
        case 90: // 'Z'
            byte0 = 5;
            break;

        case 60: // '<'
            byte0 = 3;
            break;

        case 12: // '\f'
        case 18: // '\022'
        case 23: // '\027'
        case 25: // '\031'
        case 26: // '\032'
        case 27: // '\033'
        case 32: // ' '
        case 36: // '$'
        case 37: // '%'
        case 39: // '\''
        case 40: // '('
        case 43: // '+'
        case 44: // ','
        case 45: // '-'
        case 48: // '0'
        case 49: // '1'
        case 50: // '2'
        case 51: // '3'
        case 52: // '4'
        case 53: // '5'
        case 55: // '7'
        case 56: // '8'
        case 57: // '9'
        case 58: // ':'
        case 59: // ';'
        case 61: // '='
        case 67: // 'C'
        case 71: // 'G'
        case 72: // 'H'
        case 83: // 'S'
        case 84: // 'T'
        case 85: // 'U'
        case 86: // 'V'
        case 87: // 'W'
            byte0 = 6;
            break;

        case 81: // 'Q'
            byte0 = 7;
            break;

        case 22: // '\026'
        case 46: // '.'
        case 73: // 'I'
            byte0 = 9;
            break;

        case 1: // '\001'
            byte0 = 1;
            break;

        case 2: // '\002'
            byte0 = 2;
            break;

        case 13: // '\r'
            byte0 = 4;
            break;

        case 91: // '['
            byte0 = 10;
            break;

        case 92: // '\\'
            byte0 = 11;
            break;

        default:
            byte0 = 0;
            break;
        }
        return byte0;
    }

    public void restoreFromXML(XMLParser xmlparser, Node node)
    {
        NamedNodeMap namednodemap = node.getAttributes();
        if(namednodemap != null && namednodemap.getLength() > 0)
            super.restoreAttributesFromXML(xmlparser, namednodemap);
        Node node1 = null;
        String s = (node1 = namednodemap.getNamedItem("valuetype")) != null ? node1.getNodeValue() : null;
        String s1 = (node1 = namednodemap.getNamedItem("parmtype")) != null ? node1.getNodeValue() : null;
        String s3 = (node1 = namednodemap.getNamedItem("value")) != null ? node1.getNodeValue() : null;
        xmlparser.logMessage("     RESTORING: parm: dt = " + s + ", pt = " + s1 + ", pv = " + s3);
        if(s != null)
            setDataType(Integer.parseInt(s));
        if(s1 != null)
            setParmType(Integer.parseInt(s1));
        setVarValueFromString(s3);
        NodeList nodelist = node.getChildNodes();
        if(nodelist != null && nodelist.getLength() > 0)
            super.restoreChildrenFromXML(xmlparser, nodelist);
        else
            return;
        Vector vector = XMLParser.getTags(nodelist, "parm");
        if(vector != null)
        {
            Vector vector1 = new Vector();
            for(int i = 0; i < vector.size(); i++)
            {
                Object obj = null;
                Node node2 = (Node)vector.elementAt(i);
                Node node3 = null;
                NamedNodeMap namednodemap1 = node2.getAttributes();
                String s2 = (node3 = namednodemap1.getNamedItem("parmtype")) != null ? node3.getNodeValue() : null;
                if(s2 != null)
                {
                    int j = mapParmType(Integer.parseInt(s2));
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
                ((KeywordParm) (obj)).restoreFromXML(xmlparser, node2);
                vector1.addElement(obj);
            }

            if(vector1.size() > 0)
                setSubParms(vector1);
        }
    }

    public void saveAsXML(String s, PrintWriter printwriter)
        throws IOException
    {
        printwriter.print(s + "<" + "parm");
        super.saveAttributesAsXML(printwriter);
        printwriter.print(" valuetype=\"" + getDataType() + "\" " + "parmtype" + "=\"" + getParmType() + "\" " + "value" + "=\"" + AnyNode.prepareStringForXML(getVarValueAsString()) + '"');
        String s1 = s + "  ";
        if(subParms != null && subParms.size() > 0)
        {
            printwriter.println(">");
            for(int i = 0; i < subParms.size(); i++)
                ((KeywordParm)subParms.elementAt(i)).saveAsXML(s1, printwriter);

            printwriter.println(s + "</" + "parm" + ">");
        } else
        {
            printwriter.println("/>");
        }
    }

    protected void setDataType(int i)
    {
        datatype = (short)i;
    }

    protected void setParmType(int i)
    {
        parmtype = (short)i;
    }

    protected void setSubParms(Vector vector)
    {
        subParms = vector;
    }

    protected void setVarValue(int i)
    {
        var = i;
    }

    protected void setVarValueFromString(String s)
    {
        if(s == null)
        {
            var = 0;
        } else
        {
            s = s.trim();
            if(s.length() == 0)
                var = 0;
            else
                var = Integer.parseInt(s);
        }
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2003, all rights reserved.");
    private short parmtype;
    private short datatype;
    private Vector subParms;
    private int var;
    protected static final String XML_ATTR_DATATYPE = "valuetype";
    protected static final String XML_ATTR_PARMTYPE = "parmtype";
    protected static final String XML_ATTR_VALUE = "value";
    protected static final String XML_TAG_PARM = "parm";
    protected static final String XML_TAG_SUBPARMS = "subparms";

}
