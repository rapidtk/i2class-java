// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.code400.dom.constants.FieldType;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.core.Element;
import com.ibm.as400ad.webfacing.runtime.core.ElementContainer;
import com.ibm.as400ad.webfacing.runtime.host.HostJobInfo;
import com.ibm.as400ad.webfacing.runtime.model.IFormattableFieldData;
import com.ibm.as400ad.webfacing.runtime.model.def.FieldDataDefinition;
import com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition;
import com.ibm.as400ad.webfacing.runtime.view.CursorPosition;
import com.ibm.as400ad.webfacing.runtime.view.IFormattableFieldView;
import com.ibm.as400ad.webfacing.util.JointIterator;
import com.ibm.ivj.et400.util.EditcodeEditwordFormatter;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            ERRMSGMessageDefinition, ERRMSGIDMessageDefinition, MSGMessageDefinition, XXXMSGIDDefinition

public class FieldViewDefinition extends ElementContainer
    implements IFormattableFieldView
{

    public FieldViewDefinition(String s, int i, int j, int k)
    {
        super(s);
        _hasEDTCDE = false;
        _indicatorExpression = "";
        _editCode = '0';
        _editCodeParm = '0';
        _editFormatter = null;
        _editWord = "";
        _values = "";
        _endMaskingAt = 0;
        _masked = false;
        _startMaskingAt = 0;
        _ERRMSGList = new Vector();
        _ERRMSGIDList = new Vector();
        _position = null;
        _height = 1;
        setPosition(new CursorPosition(i, j));
        _width = k;
    }

    public void add(KeywordDefinition keyworddefinition)
    {
        if(keyworddefinition.getKeywordIdentifier() == 99L)
        {
            Iterator iterator = keyworddefinition.getParameters();
            String s = "";
            if(iterator.hasNext())
                s = (String)iterator.next();
            ERRMSGMessageDefinition errmsgmessagedefinition = new ERRMSGMessageDefinition(getFieldName(), s);
            errmsgmessagedefinition.setIndicatorExpression(keyworddefinition.getIndicatorExpression());
            if(iterator.hasNext())
                errmsgmessagedefinition.setResponseIndicator(Integer.parseInt((String)iterator.next()));
            _ERRMSGList.add(errmsgmessagedefinition);
        } else
        {
            add(keyworddefinition, com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition.class);
        }
    }

    public void addEditCode(char c)
    {
        if(c < '5' || c > '9')
        {
            _hasEDTCDE = true;
            _editCode = c;
        }
    }

    public void addEditCode(char c, char c1)
    {
        addEditCode(c);
        _editCodeParm = c1;
    }

    public void addEditWord(String s)
    {
        _hasEDTCDE = true;
        _editWord = s;
    }

    public void addValues(String s)
    {
        _values = s;
    }

    public void addERRMSGIDKeyword(XXXMSGIDDefinition xxxmsgiddefinition)
    {
        ERRMSGIDMessageDefinition errmsgidmessagedefinition = new ERRMSGIDMessageDefinition(getFieldName(), xxxmsgiddefinition);
        _ERRMSGIDList.add(errmsgidmessagedefinition);
    }

    public FieldType getDataType()
    {
        return _fieldDataDef.getDataType();
    }

    public String getDecimalFormattedString(StringBuffer stringbuffer)
    {
        if('S' != getKeyboardShift())
        {
            StringBuffer stringbuffer1 = new StringBuffer();
            stringbuffer1 = stringbuffer;
            int i = _fieldDataDef.getDecimalPrecision();
            if(i != 0)
            {
                int j = _fieldDataDef.getFieldLength() - i;
                if(stringbuffer1.charAt(0) == '-')
                    j++;
                stringbuffer1.insert(j, WFSession.getJobInfoRequestor().getDecimalSeparator());
            }
            return stringbuffer1.toString();
        } else
        {
            return stringbuffer.toString();
        }
    }

    public int getDecimalPrecision()
    {
        return _fieldDataDef.getDecimalPrecision();
    }

    public char getEditCode()
    {
        return _editCode;
    }

    public char getEditCodeParm()
    {
        return _editCodeParm;
    }

    public EditcodeEditwordFormatter getEditFormatter()
    {
        return _editFormatter;
    }

    public String getEditWord()
    {
        return _editWord;
    }

    public int getEndMaskingAt()
    {
        return _endMaskingAt;
    }

    public Iterator getERRMSGs()
    {
        return new JointIterator(_ERRMSGList.iterator(), _ERRMSGIDList.iterator());
    }

    public int getFieldLength()
    {
        return _fieldDataDef.getFieldLength();
    }

    public String getFieldName()
    {
        return super.getName();
    }

    public String getIndicatorExpression()
    {
        return _indicatorExpression;
    }

    public char getKeyboardShift()
    {
        return _fieldDataDef.getKeyboardShift();
    }

    public KeywordDefinition getKeywordDefinition(long l)
    {
        return (KeywordDefinition)get((new Long(l)).toString(), com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition.class);
    }

    public int getStartMaskingAt()
    {
        return _startMaskingAt;
    }

    public boolean hasDefaultValue()
    {
        return _fieldDataDef.hasDefaultValue();
    }

    public boolean hasEditCodeEditWord()
    {
        return _hasEDTCDE;
    }

    public boolean hasERRMessages()
    {
        return getERRMSGs().hasNext();
    }

    public boolean isInputCapable()
    {
        return _fieldDataDef.isInputCapable();
    }

    public boolean isInputOnly()
    {
        return _fieldDataDef.isInputOnly();
    }

    public boolean isKeywordSpecified(long l)
    {
        return getKeywordDefinition(l) != null;
    }

    public boolean isMasked()
    {
        return _masked;
    }

    public boolean isNumeric()
    {
        return _fieldDataDef.isNumeric();
    }

    public boolean isOutputCapable()
    {
        return _fieldDataDef.isOutputCapable();
    }

    public boolean isOutputOnly()
    {
        return _fieldDataDef.isOutputOnly();
    }

    public void setEditFormatter(EditcodeEditwordFormatter editcodeeditwordformatter)
    {
        _editFormatter = editcodeeditwordformatter;
    }

    public void setEndMaskingAt(int i)
    {
        _endMaskingAt = i;
    }

    public void setFieldDataDefinition(FieldDataDefinition fielddatadefinition)
    {
        _fieldDataDef = fielddatadefinition;
    }

    public void setIndicatorExpression(String s)
    {
        _indicatorExpression = s;
    }

    public void setMasked(boolean flag)
    {
        _masked = flag;
    }

    public void setStartMaskingAt(int i)
    {
        _startMaskingAt = i;
    }

    public CursorPosition getPosition()
    {
        return _position;
    }

    private void setPosition(CursorPosition cursorposition)
    {
        _position = cursorposition;
    }

    public int getHeight()
    {
        return _height;
    }

    public void setHeight(int i)
    {
        _height = i;
    }

    public int getWidth()
    {
        return _width;
    }

    public FieldDataDefinition getDataDefinition()
    {
        return _fieldDataDef;
    }

    public IFormattableFieldData getFormattableDataDefinition()
    {
        return _fieldDataDef;
    }

    public String getValues()
    {
        return _values;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 1999, 2002.  All Rights Reserved.";
    FieldDataDefinition _fieldDataDef;
    private boolean _hasEDTCDE;
    private String _indicatorExpression;
    private char _editCode;
    private char _editCodeParm;
    private transient EditcodeEditwordFormatter _editFormatter;
    private String _editWord;
    private String _values;
    private int _endMaskingAt;
    private boolean _masked;
    private int _startMaskingAt;
    private Vector _ERRMSGList;
    private Vector _ERRMSGIDList;
    private CursorPosition _position;
    private int _width;
    private int _height;
}
