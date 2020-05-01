// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import com.ibm.as400ad.code400.dom.constants.FieldType;
import java.io.*;
import java.util.*;
import org.w3c.dom.*;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            AnyNodeWithDescription, CheckAttributes, DisplayAttributes, RecordNode, 
//            IndicatorNode, AnyNode, AnyNodeWithKeywords, KeywordNode, 
//            FileNode, KeywordParm, XMLParser

public class FieldNode extends AnyNodeWithDescription
{

    protected FieldNode(AnyNode anynode)
    {
        super(anynode, 2);
        _displayAttributes = null;
        decimals = 0;
        displayLength = 0;
        length = 0;
        relativeRow = 0;
        fieldUsage = ' ';
        fieldShift = ' ';
        fieldType = (FieldType)FieldType.getFieldTypeFromId(20);
        indicators = null;
        isVisible = false;
        isDBReference = false;
        isSrcReference = false;
        isGraphic = false;
        isContinued = false;
        sampleText = new String("");
        indicatorString = null;
        _checkAttributes = null;
        _referenceExists = false;
        _helpId = 0;
    }

    protected FieldNode(AnyNode anynode, String s)
    {
        super(anynode, 2);
        _displayAttributes = null;
        decimals = 0;
        displayLength = 0;
        length = 0;
        relativeRow = 0;
        fieldUsage = ' ';
        fieldShift = ' ';
        fieldType = (FieldType)FieldType.getFieldTypeFromId(20);
        indicators = null;
        isVisible = false;
        isDBReference = false;
        isSrcReference = false;
        isGraphic = false;
        isContinued = false;
        sampleText = new String("");
        indicatorString = null;
        _checkAttributes = null;
        _referenceExists = false;
        _helpId = 0;
        setName(s);
    }

    public KeywordNode getAlias()
    {
        return findKeywordById(2);
    }

    public String getAliasAsString()
    {
        KeywordNode keywordnode = getAlias();
        String s = null;
        if(keywordnode != null)
            s = keywordnode.getParmsAsString();
        return s;
    }

    public CheckAttributes getCheckAttributes()
    {
        if(_checkAttributes == null)
            _checkAttributes = new CheckAttributes(this);
        return _checkAttributes;
    }

    public int getColumn(int i)
    {
        return column[i];
    }

    public int getDecimals()
    {
        return decimals;
    }

    public String getDhtmlName()
    {
        String s = getParentRecord().getWebName() + "$" + getWebName();
        return s;
    }

    public DisplayAttributes getDisplayAttributes()
    {
        if(_displayAttributes == null)
            _displayAttributes = new DisplayAttributes(this);
        return _displayAttributes;
    }

    public int getDisplayLength()
    {
        return displayLength;
    }

    public char getFieldIOCapability()
    {
        char c = fieldUsage;
        switch(c)
        {
        default:
            break;

        case 72: // 'H'
            c = 'B';
            break;

        case 77: // 'M'
            c = 'O';
            break;

        case 80: // 'P'
            if(getParentRecord().isSFL())
                return 'B';
            c = 'O';
            break;

        case 32: // ' '
            c = 'O';
            break;
        }
        return c;
    }

    public char getFieldShift()
    {
        return fieldShift;
    }

    public FieldType getFieldType()
    {
        return fieldType;
    }

    public char getFieldUsage()
    {
        return fieldUsage;
    }

    public int getHelpId()
    {
        return _helpId;
    }

    public IndicatorNode getIndicators()
    {
        return indicators;
    }

    public String getIndicatorString()
    {
        return indicatorString;
    }

    public int getLength()
    {
        return length;
    }

    public String getLengthAsString()
    {
        String s = Integer.toString(getLength());
        FieldType fieldtype = getFieldType();
        int i = fieldtype.typeId();
        if(i == 11 || i == 10 || i == 9 || i == 12 || i == 13)
            s = s + "." + Integer.toString(getDecimals());
        else
        if(i == 15 || i == 16 || i == 17)
            s = Integer.toString(getDisplayLength());
        if(FileNode.getFile().isPForLF())
        {
            KeywordNode keywordnode = findKeywordById(412);
            if(keywordnode == null)
                keywordnode = findKeywordById(500);
            if(keywordnode != null)
            {
                String s1 = keywordnode.getParmsAsString();
                if(s1.length() < 1)
                    s1 = "0";
                s = s + " ( " + s1 + " )";
            }
        }
        return s;
    }

    private int getMaxCharLength(int i, int j)
    {
        switch(FileNode.getFile().getDDSType())
        {
        case 68: // 'D'
            if(!isVisible())
                return i;
            if(getFieldType().isOfType(19) && getFieldShift() == 'G')
                return FileNode.getFile().getPrimaryDisplaySize() <= 0 ? 959 : 1781;
            return FileNode.getFile().getPrimaryDisplaySize() <= 0 ? 1919 : 3563;

        case 82: // 'R'
            return j;

        case 73: // 'I'
        case 76: // 'L'
        case 80: // 'P'
            int k = j;
            int l = getFieldType().typeId();
            if(findKeywordById(412) != null || findKeywordById(500) != null)
                if(19 == l)
                {
                    if(getFieldShift() == 'G')
                        k -= 13;
                    else
                        k -= 26;
                } else
                if(8 == l || 14 == l)
                    k -= 26;
            if(findKeywordById(381) != null && (l == 19 || l == 8 || l == 14))
                k--;
            return k;

        case 69: // 'E'
        case 70: // 'F'
        case 71: // 'G'
        case 72: // 'H'
        case 74: // 'J'
        case 75: // 'K'
        case 77: // 'M'
        case 78: // 'N'
        case 79: // 'O'
        case 81: // 'Q'
        default:
            return j;
        }
    }

    public int getMaxDecimals()
    {
        int i = 0;
        FieldType fieldtype = getFieldType();
        int j = fieldtype.typeId();
        if(j == 9 || j == 12 || j == 10 || j == 11 || j == 13)
        {
            i = getMaxLength();
            if(getLength() < i)
                i = getLength();
        }
        return i;
    }

    public int getMaxLength()
    {
        int i = getMinLength();
        FieldType fieldtype = getFieldType();
        int j = fieldtype.typeId();
        if(j == 8 || j == 0 || j == 14)
            i = getMaxCharLength(32766, 32766);
        else
        if(j == 9 || j == 12)
            i = 31;
        else
        if(j == 10)
            i = 9;
        else
        if(j == 11)
            i = 17;
        else
        if(j == 13)
            i = 9;
        else
        if(j == 19)
            switch(getFieldShift())
            {
            default:
                break;

            case 69: // 'E'
            case 74: // 'J'
                i = getMaxCharLength(32762, 32762);
                if(FileNode.getFile().isDSPF())
                    i--;
                break;

            case 79: // 'O'
                i = getMaxCharLength(32763, 32763);
                break;

            case 71: // 'G'
                i = getMaxCharLength(16381, 16383);
                break;
            }
        return i;
    }

    public int getMinDecimals()
    {
        return 0;
    }

    public int getMinLength()
    {
        FieldType fieldtype = getFieldType();
        if(fieldtype.typeId() == 19)
            switch(getFieldShift())
            {
            case 69: // 'E'
            case 74: // 'J'
            case 79: // 'O'
                return 4;

            case 71: // 'G'
                return 1;
            }
        return 1;
    }

    public RecordNode getParentRecord()
    {
        return (RecordNode)getParent();
    }

    private AnyNode getReference()
    {
        return null;
    }

    public boolean getReferenceExists()
    {
        boolean flag = false;
        if(isDBReferenceField() || isSourceReferenceField())
            flag = _referenceExists;
        return flag;
    }

    public int getRelativeRow()
    {
        return relativeRow;
    }

    public int getRow(int i)
    {
        return row[i];
    }

    public String getSampleText()
    {
        return sampleText;
    }

    public static String getTranslatedString(ResourceBundle resourcebundle, String s, String s1)
    {
        String s2 = s1;
        if(resourcebundle != null)
            try
            {
                s2 = resourcebundle.getString(s);
            }
            catch(MissingResourceException missingresourceexception)
            {
                System.out.println("Error retrieving translated string with key: " + s);
            }
        return s2;
    }

    public int getVarLength()
    {
        int i = getLength();
        KeywordNode keywordnode = AnyNodeWithKeywords.findKeyword(this, 412);
        if(keywordnode != null)
        {
            KeywordParm keywordparm = keywordnode.findParameterByType(81);
            if(keywordparm != null && keywordparm.getVarNumber() < i)
                i = keywordparm.getVarNumber();
        }
        return i;
    }

    public boolean hasBLANKS()
    {
        return findKeywordById(11) != null;
    }

    public boolean isContinuedField()
    {
        KeywordNode keywordnode = findKeywordById(76);
        if(null != keywordnode && !keywordnode.getHasError())
            isContinued = true;
        else
            isContinued = false;
        return isContinued;
    }

    public boolean isDBReferenceField()
    {
        return isDBReference;
    }

    public boolean isEvenLengthOnly()
    {
        boolean flag = false;
        if(fieldType.typeId() == 19 && (fieldShift == 'J' || fieldShift == 'E' || fieldShift == 'G'))
            flag = true;
        return flag;
    }

    public boolean isGraphicField()
    {
        return isGraphic;
    }

    public boolean isNamed()
    {
        String s = getName();
        return !isUnnamedConstantField() || s == null || s.length() == 0 || s.charAt(0) == 0;
    }

    public boolean isOutputCapable()
    {
        char c = getFieldUsage();
        return c == 'O' || c == 'B' || c == 0 || c == ' ';
    }

    public boolean isSourceReferenceField()
    {
        return isSrcReference;
    }

    public boolean isUnnamedConstantField()
    {
        boolean flag = false;
        FieldType fieldtype = getFieldType();
        if(null != fieldtype)
        {
            int i = fieldtype.typeId();
            if(i == 0 || i == 1 || i == 6 || i == 2 || i == 3 || i == 4 || i == 5 || i == 7)
                flag = true;
        }
        return flag;
    }

    public boolean isValidContinuedField()
    {
        boolean flag = false;
        if(isContinuedField())
        {
            RecordNode recordnode = getParentRecord();
            if(recordnode.isSFL() || recordnode.isSFLMSG())
            {
                flag = false;
            } else
            {
                char c = getFieldUsage();
                if(c != 'I' && c != 'B')
                    flag = false;
            }
        } else
        {
            FieldType fieldtype = getFieldType();
            char c1 = getFieldShift();
            if(fieldtype.typeId() == 8 && (c1 == ' ' || c1 == 'A') || fieldtype.typeId() == 19)
                flag = true;
        }
        return flag;
    }

    public boolean isVisible()
    {
        return isVisible;
    }

    public void restoreFromXML(XMLParser xmlparser, Node node)
    {
        NamedNodeMap namednodemap = node.getAttributes();
        if(namednodemap != null && namednodemap.getLength() > 0)
            super.restoreAttributesFromXML(xmlparser, namednodemap);
        Node node1 = null;
        String s = (node1 = namednodemap.getNamedItem("length")) != null ? node1.getNodeValue() : null;
        String s1 = (node1 = namednodemap.getNamedItem("displaylength")) != null ? node1.getNodeValue() : null;
        String s2 = (node1 = namednodemap.getNamedItem("decimals")) != null ? node1.getNodeValue() : null;
        String s3 = (node1 = namednodemap.getNamedItem("type")) != null ? node1.getNodeValue() : null;
        String s4 = (node1 = namednodemap.getNamedItem("usage")) != null ? node1.getNodeValue() : null;
        String s5 = (node1 = namednodemap.getNamedItem("shift")) != null ? node1.getNodeValue() : null;
        String s6 = (node1 = namednodemap.getNamedItem("dbref")) != null ? node1.getNodeValue() : null;
        String s7 = (node1 = namednodemap.getNamedItem("srcref")) != null ? node1.getNodeValue() : null;
        String s8 = (node1 = namednodemap.getNamedItem("relativerow")) != null ? node1.getNodeValue() : null;
        String s9 = (node1 = namednodemap.getNamedItem("sampletext")) != null ? node1.getNodeValue() : null;
        String s10 = (node1 = namednodemap.getNamedItem("indstring")) != null ? node1.getNodeValue() : null;
        String s11 = (node1 = namednodemap.getNamedItem("referenceexists")) != null ? node1.getNodeValue() : null;
        if(s11 != null)
            _referenceExists = s11.equals("true");
        if(s != null)
            setLength(Integer.parseInt(s));
        if(s1 != null)
            setDisplayLength(Integer.parseInt(s1));
        if(s2 != null)
            setDecimals(Integer.parseInt(s2));
        if(s3 != null)
            setFieldType(Integer.parseInt(s3));
        if(s4 != null)
            setFieldUsage(s4.charAt(0));
        if(s5 != null)
            setFieldShift(s5.charAt(0));
        if(s6 != null)
            setIsDBReference(s6.equals("true"));
        if(s7 != null)
            setIsSrcReference(s7.equals("true"));
        if(s8 != null)
            setRelativeRow(Integer.parseInt(s8));
        if(s9 != null && s9.length() > 0)
            setSampleText(s9);
        if(s10 != null)
            setIndicatorString(s10);
        NodeList nodelist = node.getChildNodes();
        if(nodelist != null && nodelist.getLength() > 0)
            super.restoreChildrenFromXML(xmlparser, nodelist);
        else
            return;
        Vector vector = XMLParser.getTags(nodelist, "position");
        int ai[] = new int[2];
        int ai1[] = new int[2];
        Object obj = null;
        Object obj1 = null;
        Object obj2 = null;
        boolean flag = false;
        if(vector != null && vector.size() > 0)
        {
            for(int i = 0; i < vector.size(); i++)
            {
                NamedNodeMap namednodemap1 = ((Node)vector.elementAt(i)).getAttributes();
                Node node2;
                String s14 = (node2 = namednodemap1.getNamedItem("dspsiz")) != null ? node2.getNodeValue() : null;
                String s12 = (node2 = namednodemap1.getNamedItem("row")) != null ? node2.getNodeValue() : null;
                String s13 = (node2 = namednodemap1.getNamedItem("column")) != null ? node2.getNodeValue() : null;
                short word0;
                if(s14 != null)
                    word0 = Short.parseShort(s14);
                else
                    word0 = 0;
                if(s12 != null)
                    ai[word0] = Integer.parseInt(s12);
                if(s13 != null)
                    ai1[word0] = Integer.parseInt(s13);
            }

            setRows(ai);
            setColumns(ai1);
        }
        Node node3 = XMLParser.getTag(nodelist, "indicator");
        if(node3 != null)
        {
            IndicatorNode indicatornode = new IndicatorNode(this);
            indicatornode.restoreFromXML(xmlparser, node3);
            setIndicators(indicatornode);
        }
    }

    public void saveAsXML(String s, PrintWriter printwriter)
        throws IOException
    {
        printwriter.print(s + "<" + "field");
        super.saveAttributesAsXML(printwriter);
        char c = getFieldUsage();
        if(c == 0)
            c = ' ';
        printwriter.print(" length=\"" + Integer.toString(getLength()) + "\" " + "displaylength" + "=\"" + Integer.toString(getDisplayLength()) + "\" " + "decimals" + "=\"" + Integer.toString(getDecimals()) + "\" " + "type" + "=\"" + Integer.toString(getFieldType().intValue()) + "\" " + "usage" + "=\"" + c + "\" " + "shift" + "=\"" + (fieldShift != 0 ? fieldShift : ' ') + "\" " + "dbref" + "=\"" + isDBReferenceField() + "\" " + "srcref" + "=\"" + isSourceReferenceField() + "\" " + "relativerow" + "=\"" + getRelativeRow() + '"');
        if(sampleText != null && sampleText.length() > 0)
        {
            printwriter.println();
            printwriter.print(s + "       " + "sampletext" + "=\"" + AnyNode.prepareStringForXML(sampleText) + '"');
        }
        if(indicatorString != null)
        {
            printwriter.println();
            printwriter.print(s + "       " + "indstring" + "=\"" + AnyNode.prepareStringForXML(indicatorString) + '"');
        }
        printwriter.println(">");
        String s1 = s + "  ";
        short word0 = FileNode.getFile().getPrimaryDisplaySize();
        short word1 = FileNode.getFile().getSecondaryDisplaySize();
        printwriter.println(s1 + "<" + "position" + " " + "dspsiz" + "=\"" + word0 + "\" " + "row" + "=\"" + row[word0] + "\" " + "column" + "=\"" + column[word0] + '"' + "/>");
        if(FileNode.getFile().isDspSizConditioned())
            printwriter.println(s1 + "<" + "position" + " " + "dspsiz" + "=\"" + word1 + "\" " + "row" + "=\"" + row[word1] + "\" " + "column" + "=\"" + column[word1] + '"' + "/>");
        if(indicators != null)
            indicators.saveAsXML(s1, printwriter);
        super.saveChildrenAsXML(s1, printwriter);
        printwriter.println(s + "</" + "field" + ">");
    }

    protected void setColumns(int ai[])
    {
        column = ai;
    }

    protected void setDecimals(int i)
    {
        decimals = i;
    }

    protected void setDisplayLength(int i)
    {
        displayLength = i;
    }

    public void setFieldShift(char c)
    {
        fieldShift = c;
    }

    protected void setFieldType(int i)
    {
        fieldType = (FieldType)FieldType.getFieldTypeFromId(i);
    }

    protected void setFieldType(FieldType fieldtype)
    {
        fieldType = fieldtype;
    }

    protected void setFieldUsage(char c)
    {
        fieldUsage = c;
    }

    public void setHelpId(int i)
    {
        _helpId = i;
    }

    protected void setIndicators(IndicatorNode indicatornode)
    {
        indicators = indicatornode;
    }

    protected void setIndicatorString(String s)
    {
        indicatorString = s;
    }

    protected void setIsContinuedField(boolean flag)
    {
        isContinued = flag;
    }

    protected void setIsDBReference(boolean flag)
    {
        isDBReference = flag;
    }

    protected void setIsGraphic(boolean flag)
    {
        isGraphic = flag;
    }

    protected void setIsSrcReference(boolean flag)
    {
        isSrcReference = flag;
    }

    protected void setIsVisible(boolean flag)
    {
        isVisible = flag;
    }

    protected void setLength(int i)
    {
        length = i;
    }

    protected void setRelativeRow(int i)
    {
        relativeRow = i;
    }

    protected void setRows(int ai[])
    {
        row = ai;
    }

    protected void setSampleText(String s)
    {
        sampleText = s;
    }

    public boolean supportsEditing()
    {
        int i = fieldType.typeId();
        return isOutputCapable() && (i == 2 || i == 3 || i == 7 || i == 9 || i == 13 || i == 12);
    }

    public int getMaxDisplayChars()
    {
        if(getFieldType().typeId() != 19)
            return getDisplayLength();
        if(getFieldShift() == 'J')
            return getLength() / 2 - 1;
        else
            return getLength();
    }

    static final String copyRight = new String("(C) Copyright IBM Corporation 1999-2003, all rights reserved");
    protected static final String XML_ATTR_COL = "column";
    protected static final String XML_ATTR_CONTINUED = "continued";
    protected static final String XML_ATTR_DBREF = "dbref";
    protected static final String XML_ATTR_DECIMALS = "decimals";
    protected static final String XML_ATTR_DISPLAYLENGTH = "displaylength";
    protected static final String XML_ATTR_GRAPHIC = "graphic";
    protected static final String XML_ATTR_INDSTRING = "indstring";
    protected static final String XML_ATTR_LENGTH = "length";
    protected static final String XML_ATTR_RELATIVEROW = "relativerow";
    protected static final String XML_ATTR_ROW = "row";
    protected static final String XML_ATTR_SAMPLETEXT = "sampletext";
    protected static final String XML_ATTR_SHIFT = "shift";
    protected static final String XML_ATTR_SRCREF = "srcref";
    protected static final String XML_ATTR_TYPE = "type";
    protected static final String XML_ATTR_USAGE = "usage";
    protected static final String XML_ATTR_VALUE = "value";
    protected static final String XML_ATTR_VISIBLE = "visible";
    protected static final String XML_TAG_FIELD = "field";
    protected static final String XML_ATTR_DSPSIZE = "dspsiz";
    protected static final String XML_ATTR_INDTYPE = "indtype";
    protected static final String XML_TAG_POSITION = "position";
    protected static final String XML_ATTR_REFERENCEEXISTS = "referenceexists";
    private DisplayAttributes _displayAttributes;
    private int column[] = {
        0, 0
    };
    private int row[] = {
        0, 0
    };
    private int decimals;
    private int displayLength;
    private int length;
    private int relativeRow;
    private char fieldUsage;
    private char fieldShift;
    private FieldType fieldType;
    private IndicatorNode indicators;
    private boolean isVisible;
    private boolean isDBReference;
    private boolean isSrcReference;
    private boolean isGraphic;
    private boolean isContinued;
    private String sampleText;
    private String indicatorString;
    private CheckAttributes _checkAttributes;
    private boolean _referenceExists;
    private int _helpId;

}
