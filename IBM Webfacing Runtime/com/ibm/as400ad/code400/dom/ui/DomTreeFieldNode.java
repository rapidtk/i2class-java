// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import com.ibm.as400ad.code400.dom.FieldNode;
import com.ibm.as400ad.code400.dom.Logger;
import com.ibm.as400ad.code400.dom.constants.FieldType;
import com.ibm.as400ad.webfacing.convert.util.TranslatableStrings;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            DomTreeAnyNodeWithDescription, DomTreeCategoryNode, DomTreeIndicatorNode, DomTreeAttributesNode, 
//            DomTreeAnyNode, DomTreeAnyNodeWithKeywords

public class DomTreeFieldNode extends DomTreeAnyNodeWithDescription
{

    public DomTreeFieldNode(FieldNode fieldnode, String s)
    {
        super(fieldnode, s);
    }

    public Vector getPropertyNames()
    {
        return super.mergePropertyNames(propNames);
    }

    public Vector getPropertyValues()
    {
        return super.mergePropertyValues(getUniqueFieldPropertyValues());
    }

    public static String getFieldTypeName(FieldType fieldtype)
    {
        String s = "test";
        int i = fieldtype.typeId();
        if(i == 0)
            s = TranslatableStrings.getString("FLDTYPE_TXT", "Text constant");
        else
        if(i == 1)
            s = TranslatableStrings.getString("FLDTYPE_TXTBLK", "Text block");
        else
        if(i == 2)
            s = TranslatableStrings.getString("FLDTYPE_DATE", "Date constant");
        else
        if(i == 3)
            s = TranslatableStrings.getString("FLDTYPE_TIME", "Time constant");
        else
        if(i == 4)
            s = TranslatableStrings.getString("FLDTYPE_USRID", "User ID constant");
        else
        if(i == 5)
            s = TranslatableStrings.getString("FLDTYPE_SYSID", "System ID constant");
        else
        if(i == 6)
            s = TranslatableStrings.getString("FLDTYPE_MSGCON", "Message constant");
        else
        if(i == 7)
            s = TranslatableStrings.getString("FLDTYPE_PAGNBR", "Page number");
        else
        if(i == 8)
            s = TranslatableStrings.getString("FLDTYPE_ALPHA", "Alphanumeric");
        else
        if(i == 9)
            s = TranslatableStrings.getString("FLDTYPE_NUMERIC", "Numeric");
        else
        if(i == 10 || i == 11)
            s = TranslatableStrings.getString("FLDTYPE_FLOAT", "Floating point");
        else
        if(i == 12)
            s = TranslatableStrings.getString("FLDTYPE_PACKED", "Packed");
        else
        if(i == 13)
            s = TranslatableStrings.getString("FLDTYPE_BINARY", "Binary");
        else
        if(i == 14)
            s = TranslatableStrings.getString("FLDTYPE_HEX", "Hexadecimal");
        else
        if(i == 15)
            s = TranslatableStrings.getString("FLDTYPE_PFDATE", "Date");
        else
        if(i == 16)
            s = TranslatableStrings.getString("FLDTYPE_PFTIME", "Time");
        else
        if(i == 17)
            s = TranslatableStrings.getString("FLDTYPE_PFTIMESTAMP", "Timestamp");
        else
        if(i == 19 || i == 18)
            s = TranslatableStrings.getString("FLDTYPE_DBCS", "DBCS");
        return s;
    }

    public String getShiftString(char c)
    {
        String s = "test";
        switch(c)
        {
        case 88: // 'X'
            s = TranslatableStrings.getString("FLDSHIFT_ALPHA", "X - Alphabetic");
            break;

        case 65: // 'A'
            s = TranslatableStrings.getString("FLDSHIFT_ALPHANUMERIC", "A - Alphanumeric");
            break;

        case 78: // 'N'
            s = TranslatableStrings.getString("FLDSHIFT_NUMERICSHIFT", "N - Numeric Shift");
            break;

        case 83: // 'S'
            s = TranslatableStrings.getString("FLDSHIFT_SIGNEDNUMERIC", "S - Signed numeric");
            break;

        case 89: // 'Y'
            s = TranslatableStrings.getString("FLDSHIFT_NUMERIC", "Y - Numeric only");
            break;

        case 73: // 'I'
            s = TranslatableStrings.getString("FLDSHIFT_INHIBIT", "I - Inhibit keyboard");
            break;

        case 68: // 'D'
            s = TranslatableStrings.getString("FLDSHIFT_DIGITS", "D - Digits only");
            break;

        case 77: // 'M'
            s = TranslatableStrings.getString("FLDSHIFT_NUMERICCHAR", "M - Numeric only character");
            break;

        case 87: // 'W'
            s = TranslatableStrings.getString("FLDSHIFT_KATAKANA", "W - Katakana");
            break;

        case 74: // 'J'
            s = TranslatableStrings.getString("FLDSHIFT_DBCSONLY", "J - DBCS only");
            break;

        case 69: // 'E'
            s = TranslatableStrings.getString("FLDSHIFT_DBCSEITHER", "E - DBCS either");
            break;

        case 79: // 'O'
            s = TranslatableStrings.getString("FLDSHIFT_DBCSOPEN", "O - DBCS open");
            break;

        case 71: // 'G'
            s = TranslatableStrings.getString("FLDSHIFT_DBCSGRAPHIC", "G - DBCS graphic");
            break;

        case 70: // 'F'
            s = TranslatableStrings.getString("FLDSHIFT_FLOAT", "F - Floating point");
            break;

        case 66: // 'B'
        case 67: // 'C'
        case 72: // 'H'
        case 75: // 'K'
        case 76: // 'L'
        case 80: // 'P'
        case 81: // 'Q'
        case 82: // 'R'
        case 84: // 'T'
        case 85: // 'U'
        case 86: // 'V'
        default:
            s = (new Character(c)).toString();
            break;
        }
        return s;
    }

    private Vector getUniqueFieldPropertyValues()
    {
        Vector vector = new Vector();
        FieldNode fieldnode = (FieldNode)getNode();
        vector.addElement(getFieldTypeName(fieldnode.getFieldType()));
        String s = fieldnode.getAliasAsString();
        if(s == null)
            vector.addElement("null");
        else
        if(s.length() == 0)
            vector.addElement("null (len 0)");
        else
            vector.addElement(s);
        vector.addElement(fieldnode.getDhtmlName());
        vector.addElement(Integer.toString(fieldnode.getRow(0)));
        vector.addElement(Integer.toString(fieldnode.getRow(1)));
        vector.addElement(Integer.toString(fieldnode.getRelativeRow()));
        vector.addElement(Integer.toString(fieldnode.getColumn(0)));
        vector.addElement(Integer.toString(fieldnode.getColumn(1)));
        vector.addElement(Integer.toString(fieldnode.getLength()));
        vector.addElement(fieldnode.getLengthAsString());
        vector.addElement(Integer.toString(fieldnode.getVarLength()));
        vector.addElement(Integer.toString(fieldnode.getDisplayLength()));
        vector.addElement(Integer.toString(fieldnode.getDecimals()));
        vector.addElement((new Character(fieldnode.getFieldShift())).toString());
        vector.addElement(getShiftString(fieldnode.getFieldShift()));
        vector.addElement((new Character(fieldnode.getFieldUsage())).toString());
        vector.addElement(fieldnode.getIndicatorString());
        vector.addElement(fieldnode.getSampleText());
        vector.addElement(fieldnode.isContinuedField() ? "true" : "false");
        vector.addElement(fieldnode.isDBReferenceField() ? "true" : "false");
        vector.addElement(fieldnode.isSourceReferenceField() ? "true" : "false");
        vector.addElement(fieldnode.isGraphicField() ? "true" : "false");
        vector.addElement(fieldnode.isNamed() ? "true" : "false");
        vector.addElement(fieldnode.isUnnamedConstantField() ? "true" : "false");
        vector.addElement(fieldnode.isOutputCapable() ? "true" : "false");
        vector.addElement(fieldnode.isVisible() ? "true" : "false");
        vector.addElement(Integer.toString(fieldnode.getMaxLength()));
        vector.addElement(Integer.toString(fieldnode.getMaxDecimals()));
        vector.addElement(Integer.toString(fieldnode.getMinLength()));
        vector.addElement(Integer.toString(fieldnode.getMinDecimals()));
        return vector;
    }

    public Vector mergePropertyNames(Vector vector)
    {
        return super.mergePropertyNames(DomTreeAnyNode.mergeVectors(vector, propNames));
    }

    public Vector mergePropertyValues(Vector vector)
    {
        return super.mergePropertyValues(DomTreeAnyNode.mergeVectors(vector, getUniqueFieldPropertyValues()));
    }

    public void populateChildren(Logger logger)
    {
        logger.logDetail("DomTreeFieldNode: starting populateChildren...");
        super.populateChildren(logger);
        FieldNode fieldnode = (FieldNode)getNode();
        DomTreeCategoryNode domtreecategorynode = new DomTreeCategoryNode("Indicators");
        add(domtreecategorynode);
        com.ibm.as400ad.code400.dom.IndicatorNode indicatornode = fieldnode.getIndicators();
        if(indicatornode != null)
        {
            DomTreeIndicatorNode domtreeindicatornode = new DomTreeIndicatorNode(indicatornode);
            domtreecategorynode.add(domtreeindicatornode);
        }
        domtreecategorynode = new DomTreeCategoryNode("Attributes");
        add(domtreecategorynode);
        com.ibm.as400ad.code400.dom.DisplayAttributes displayattributes = fieldnode.getDisplayAttributes();
        if(displayattributes != null)
        {
            DomTreeAttributesNode domtreeattributesnode = new DomTreeAttributesNode(displayattributes);
            domtreecategorynode.add(domtreeattributesnode);
        }
        logger.logDetail("DomTreeFieldNode: ending populateChildren...");
    }

    static final String copyRight = "(c) Copyright IBM Corporation 1999-2003. All Rights Reserved";
    private static final Vector propNames;
    public static final String STR_FLD_TYPE_TXT = "FLDTYPE_TXT";
    public static final String STR_FLD_TYPE_TXTBLK = "FLDTYPE_TXTBLK";
    public static final String STR_FLD_TYPE_DATE = "FLDTYPE_DATE";
    public static final String STR_FLD_TYPE_TIME = "FLDTYPE_TIME";
    public static final String STR_FLD_TYPE_USRID = "FLDTYPE_USRID";
    public static final String STR_FLD_TYPE_SYSID = "FLDTYPE_SYSID";
    public static final String STR_FLD_TYPE_MSGCON = "FLDTYPE_MSGCON";
    public static final String STR_FLD_TYPE_PAGNBR = "FLDTYPE_PAGNBR";
    public static final String STR_FLD_TYPE_ALPHA = "FLDTYPE_ALPHA";
    public static final String STR_FLD_TYPE_NUMERIC = "FLDTYPE_NUMERIC";
    public static final String STR_FLD_TYPE_FLOAT = "FLDTYPE_FLOAT";
    public static final String STR_FLD_TYPE_PACKED = "FLDTYPE_PACKED";
    public static final String STR_FLD_TYPE_BINARY = "FLDTYPE_BINARY";
    public static final String STR_FLD_TYPE_HEX = "FLDTYPE_HEX";
    public static final String STR_FLD_TYPE_PFDATE = "FLDTYPE_PFDATE";
    public static final String STR_FLD_TYPE_PFTIME = "FLDTYPE_PFTIME";
    public static final String STR_FLD_TYPE_PFTIMESTAMP = "FLDTYPE_PFTIMESTAMP";
    public static final String STR_FLD_TYPE_DBCS = "FLDTYPE_DBCS";
    public static final String STR_ALPHA = "FLDSHIFT_ALPHA";
    public static final String STR_ALPHANUMERIC = "FLDSHIFT_ALPHANUMERIC";
    public static final String STR_NUMERICSHIFT = "FLDSHIFT_NUMERICSHIFT";
    public static final String STR_SIGNEDNUMERIC = "FLDSHIFT_SIGNEDNUMERIC";
    public static final String STR_NUMERIC = "FLDSHIFT_NUMERIC";
    public static final String STR_INHIBIT = "FLDSHIFT_INHIBIT";
    public static final String STR_DIGITS = "FLDSHIFT_DIGITS";
    public static final String STR_NUMERICCHAR = "FLDSHIFT_NUMERICCHAR";
    public static final String STR_KATAKANA = "FLDSHIFT_KATAKANA";
    public static final String STR_SINGLEPREC = "FLDSHIFT_SINGLEPREC";
    public static final String STR_DOUBLEPREC = "FLDSHIFT_DOUBLEPREC";
    public static final String STR_FLOAT = "FLDSHIFT_FLOAT";
    public static final String STR_DBCS_ONLY = "FLDSHIFT_DBCSONLY";
    public static final String STR_DBCS_OPEN = "FLDSHIFT_DBCSOPEN";
    public static final String STR_DBCS_GRAPHIC = "FLDSHIFT_DBCSGRAPHIC";
    public static final String STR_DBCS_EITHER = "FLDSHIFT_DBCSEITHER";

    static 
    {
        propNames = new Vector();
        propNames.addElement("Field Type");
        propNames.addElement("Alias name");
        propNames.addElement("DHTML name");
        propNames.addElement("Row[0]");
        propNames.addElement("Row[1]");
        propNames.addElement("Relative row");
        propNames.addElement("Column[0]");
        propNames.addElement("Column[1]");
        propNames.addElement("Length");
        propNames.addElement("Length as string");
        propNames.addElement("Variable length");
        propNames.addElement("Display length");
        propNames.addElement("Decimals");
        propNames.addElement("Shift");
        propNames.addElement("Shift string");
        propNames.addElement("Usage");
        propNames.addElement("Indicator string");
        propNames.addElement("Sample text");
        propNames.addElement("Continued?");
        propNames.addElement("DB ref?");
        propNames.addElement("Src ref?");
        propNames.addElement("Graphic?");
        propNames.addElement("Named?");
        propNames.addElement("Unnamed constant?");
        propNames.addElement("Output capable?");
        propNames.addElement("Visible?");
        propNames.addElement("Max length");
        propNames.addElement("Max decimals");
        propNames.addElement("Min length");
        propNames.addElement("Min decimals");
    }
}
