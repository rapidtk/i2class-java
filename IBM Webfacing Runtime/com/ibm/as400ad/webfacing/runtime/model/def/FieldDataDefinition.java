// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model.def;

import com.ibm.as400ad.code400.dom.constants.FieldType;
import com.ibm.as400ad.code400.dom.constants.IFieldType;
import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.model.IFormattableFieldData;
import com.ibm.as400ad.webfacing.runtime.model.IIndicatorEvaluation;
import com.ibm.as400ad.webfacing.util.DBCSUtil;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model.def:
//            MSGIDDefinition

public class FieldDataDefinition
    implements IElement, IListable, IFormattableFieldData
{

    /**
     * @deprecated Method FieldDataDefinition is deprecated
     */

    public FieldDataDefinition(String s, char c, int i, FieldType fieldtype)
    {
        _keyboardShift = ' ';
        _checkAttributes = "";
        _timFmt = "ISO";
        _timSep = "JOB";
        _datFmt = "ISO";
        _datSep = "JOB";
        _defaultValue = null;
        _dataType = FieldType.getFieldTypeFromId(8);
        _isInOutputBuffer = true;
        _isMSGID = false;
        _MSGIDs = new Vector();
        _OVRDTAIndicatorExpression = null;
        _fieldName = s;
        _usage = c;
        _fieldDataLength = i;
        _dataType = fieldtype;
        _decimalPrecision = 0;
        if(isInputOnly())
            _isInOutputBuffer = false;
    }

    public FieldDataDefinition(String s, char c, int i, int j)
    {
        this(s, c, i, (FieldType)FieldType.getFieldTypeFromId(j));
    }

    public FieldDataDefinition(String s, char c, int i, FieldType fieldtype, char c1)
    {
        this(s, c, i, fieldtype);
        _keyboardShift = c1;
    }

    public FieldDataDefinition(String s, char c, int i, int j, char c1)
    {
        this(s, c, i, (FieldType)FieldType.getFieldTypeFromId(j));
        _keyboardShift = c1;
    }

    public void addMSGIDKeyword(MSGIDDefinition msgiddefinition)
    {
        _isInOutputBuffer = false;
        _isMSGID = true;
        _MSGIDs.add(msgiddefinition);
    }

    public static int calculateDataLength(String s, char c, int i)
    {
        int j = 0;
        if(c == 'G')
            j = s.length() * 2;
        else
        if(c == 'J' || c == 'E' && s.length() > 0 && DBCSUtil.isDBCS(s.charAt(0), i))
            j = s.length() * 2 + 2;
        else
        if(c == 'O')
        {
            boolean flag1 = false;
            for(int k = 0; k < s.length(); k++)
            {
                char c1 = s.charAt(k);
                boolean flag = DBCSUtil.isDBCS(c1, i);
                if(flag)
                    j += 2;
                else
                    j++;
                if(!flag1 && flag)
                    j += 2;
                flag1 = flag;
            }

        } else
        {
            j = s.length();
        }
        return j;
    }

    public FieldType getDataType()
    {
        return (FieldType)_dataType;
    }

    public int getDecimalPrecision()
    {
        return _decimalPrecision;
    }

    public String getDefaultValue(int i)
    {
        PaddedStringBuffer paddedstringbuffer = null;
        if(_defaultValue != null)
            paddedstringbuffer = new PaddedStringBuffer(_defaultValue);
        else
            paddedstringbuffer = new PaddedStringBuffer(_fieldDataLength);
        if(_dataType.isOfType(9))
            return paddedstringbuffer.padLeft('0', _fieldDataLength).toString();
        if(_dataType.isOfType(19))
            return padDBCSField(paddedstringbuffer, _fieldDataLength, _keyboardShift, i).toString();
        else
            return paddedstringbuffer.padRight(' ', _fieldDataLength).toString();
    }

    public String getDefaultValue(IIndicatorEvaluation iindicatorevaluation, int i)
    {
        String s = getDFTVALIndicatorExpression();
        if(hasDefaultValue() && (s == null || s != null && iindicatorevaluation.evaluateIndicatorExpression(s)))
            return getDefaultValue(i);
        else
            return null;
    }

    public String getDFTVALIndicatorExpression()
    {
        return _DFTVALindicatorExpression;
    }

    public int getFieldLength()
    {
        return _fieldDataLength;
    }

    public String getFieldName()
    {
        return _fieldName;
    }

    public IKey getKey()
    {
        return new Key(_fieldName);
    }

    public char getKeyboardShift()
    {
        return _keyboardShift;
    }

    public Iterator getMSGIDs()
    {
        return _MSGIDs.iterator();
    }

    public boolean hasDefaultValue()
    {
        return null != _defaultValue;
    }

    public boolean isInOutputBuffer()
    {
        return _isInOutputBuffer;
    }

    public boolean isInputCapable()
    {
        return _usage == 'I' || _usage == 'B';
    }

    public boolean isInputOnly()
    {
        return _usage == 'I';
    }

    public boolean isMSGID()
    {
        return _isMSGID;
    }

    public boolean isNumeric()
    {
        return _dataType.isOfType(9);
    }

    public boolean isOutputCapable()
    {
        return _usage == 'O' || _usage == 'B';
    }

    public boolean isOutputOnly()
    {
        return _usage == 'O';
    }

    public boolean isOVRDTAActive(IIndicatorEvaluation iindicatorevaluation)
    {
        return _OVRDTAIndicatorExpression == "" || _OVRDTAIndicatorExpression != null && iindicatorevaluation.evaluateIndicatorExpression(_OVRDTAIndicatorExpression);
    }

    public static PaddedStringBuffer padDBCSField(PaddedStringBuffer paddedstringbuffer, int i, char c, int j)
    {
        String s = paddedstringbuffer.toString();
        char c1;
        if(c == 'J' || c == 'G' || c == 'E' && s.length() > 0 && DBCSUtil.isDBCS(s.charAt(0), j))
            c1 = '\u3000';
        else
            c1 = ' ';
        int k;
        if(c == 'O')
            k = (s.length() + i) - calculateDataLength(s, c, j);
        else
        if(c == 'J' || c == 'E' && s.length() > 0 && DBCSUtil.isDBCS(s.charAt(0), j))
            k = (i - 2) / 2;
        else
            k = i;
        return paddedstringbuffer.padRight(c1, k);
    }

    public void setDataType(FieldType fieldtype)
    {
        _dataType = fieldtype;
    }

    public void setDecimalPrecision(int i)
    {
        _decimalPrecision = i;
    }

    public void setDefaultValue(String s)
    {
        _defaultValue = s;
    }

    public void setDefaultValue(String s, String s1)
    {
        _defaultValue = s;
        _DFTVALindicatorExpression = s1;
    }

    public void setOVRDTA(String s)
    {
        _OVRDTAIndicatorExpression = s;
    }

    public String toString()
    {
        String s;
        if(getDecimalPrecision() == 0)
            s = "" + getFieldLength();
        else
            s = getFieldLength() + "," + getDecimalPrecision();
        return "Name: " + _fieldName + " type: " + getDataType() + " usage: " + _usage + " length: " + s + " shift: " + getKeyboardShift();
    }

    public void validateDataLength(int i)
        throws WebfacingLevelCheckException
    {
    }

    public String getCheckAttr()
    {
        return _checkAttributes;
    }

    public String getTimSep()
    {
        return _timSep;
    }

    public String getTimFmt()
    {
        return _timFmt;
    }

    public String getDatSep()
    {
        return _datSep;
    }

    public String getDatFmt()
    {
        return _datFmt;
    }

    public void setCheckAttr(String s)
    {
        if(s != null)
            _checkAttributes = s;
    }

    public void setTimSep(String s)
    {
        _timSep = s;
    }

    public void setTimFmt(String s)
    {
        _timFmt = s;
    }

    public void setDatSep(String s)
    {
        _datSep = s;
    }

    public void setDatFmt(String s)
    {
        _datFmt = s;
    }

    public String getDFTVAL()
    {
        return _defaultValue;
    }

    public String getOVRDTA()
    {
        return _OVRDTAIndicatorExpression;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 1999-2003.  All Rights Reserved.";
    private String _fieldName;
    private int _fieldDataLength;
    private char _usage;
    private char _keyboardShift;
    private String _checkAttributes;
    private String _timFmt;
    private String _timSep;
    private String _datFmt;
    private String _datSep;
    private String _defaultValue;
    private int _decimalPrecision;
    private String _DFTVALindicatorExpression;
    private IFieldType _dataType;
    private boolean _isInOutputBuffer;
    private boolean _isMSGID;
    private Vector _MSGIDs;
    private String _OVRDTAIndicatorExpression;
}
