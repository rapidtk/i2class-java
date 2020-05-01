// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import com.ibm.as400ad.webfacing.common.*;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.fldformat.EFieldFormatter;
import com.ibm.as400ad.webfacing.runtime.fldformat.FieldDataFormatter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model:
//            IFieldData, IFormattableFieldData

public class FieldData
    implements IFieldData
{

    public FieldData(String s, IFormattableFieldData iformattablefielddata, int i)
    {
        _isFirstCharDBCS = false;
        _value = s;
        _fieldData = iformattablefielddata;
        _jobCCSID = i;
        if(_fieldData.getKeyboardShift() == 'E' && _fieldData.isInputCapable())
        {
            PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(_value);
            if(paddedstringbuffer.isCharDBCS(0, _jobCCSID))
                _isFirstCharDBCS = true;
        }
    }

    private void formatValue()
    {
        if(_fieldData.isInputCapable())
            _value = getFieldDataFormatter().format(_jobCCSID, _fieldData, _value);
    }

    public void formatAndSetValue(String s)
    {
        _value = s;
        if(_fieldData.isInputCapable())
        {
            formatValue();
            applyNegativeNumberTransform();
        }
        _modified = true;
    }

    public void transformAndSetValue(String s)
    {
        _value = s;
        if(_fieldData.isInputCapable())
            applyNegativeNumberTransform();
        _modified = true;
    }

    public void toInputBuffer(DataOutputStream dataoutputstream)
    {
        try
        {
            if(!_modified && _fieldData.getKeyboardShift() == 'O' && _fieldData.isInputCapable())
                formatValue();
            if(WFSession.getInputCharMappingProperties() != null)
            {
                com.ibm.as400ad.webfacing.common.MappingProperties mappingproperties = WFSession.getInputCharMappingProperties();
                for(Enumeration enumeration = mappingproperties.propertyNames(); enumeration.hasMoreElements();)
                {
                    String s = (String)enumeration.nextElement();
                    String s1 = mappingproperties.getProperty(s);
                    if(s1 != null && s.length() == s1.length())
                        _value = WebfacingConstants.replaceSubstring(_value, s, s1);
                }

            }
            dataoutputstream.writeShort(2 * _value.length());
            dataoutputstream.writeChars(_value);
        }
        catch(IOException ioexception) { }
    }

    public String toString()
    {
        return _value;
    }

    private void applyNegativeNumberTransform()
    {
        if(_fieldData.isNumeric())
        {
            int i = _value.indexOf("-");
            if(i >= 0)
            {
                StringBuffer stringbuffer = new StringBuffer(_value.substring(i + 1, _value.length()));
                int j = _value.length() - 1;
                char c = _value.charAt(j);
                char c1 = '\0';
                switch(c)
                {
                case 32: // ' '
                case 48: // '0'
                    c1 = '}';
                    break;

                case 49: // '1'
                    c1 = 'J';
                    break;

                case 50: // '2'
                    c1 = 'K';
                    break;

                case 51: // '3'
                    c1 = 'L';
                    break;

                case 52: // '4'
                    c1 = 'M';
                    break;

                case 53: // '5'
                    c1 = 'N';
                    break;

                case 54: // '6'
                    c1 = 'O';
                    break;

                case 55: // '7'
                    c1 = 'P';
                    break;

                case 56: // '8'
                    c1 = 'Q';
                    break;

                case 57: // '9'
                    c1 = 'R';
                    break;
                }
                if('\0' != c1)
                    stringbuffer.setCharAt(stringbuffer.length() - 1, c1);
                _value = stringbuffer.toString();
            }
        }
    }

    public FieldDataFormatter getFieldDataFormatter()
    {
        char c = _fieldData.getKeyboardShift();
        FieldDataFormatter fielddataformatter = FieldDataFormatter.getFieldFormatterInstance(c, _fieldData.getDataType());
        if(c == 'E' && _value.length() > 0)
        {
            PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(_value);
            _isFirstCharDBCS = paddedstringbuffer.isCharDBCS(0, _jobCCSID);
            ((EFieldFormatter)fielddataformatter).setIsFirstCharDBCS(_isFirstCharDBCS);
        }
        return fielddataformatter;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2001-2002");
    protected boolean _modified;
    protected String _value;
    private IFormattableFieldData _fieldData;
    private int _jobCCSID;
    private boolean _isFirstCharDBCS;

}
