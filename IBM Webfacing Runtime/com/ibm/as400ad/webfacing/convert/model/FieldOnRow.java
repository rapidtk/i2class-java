// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.webfacing.convert.IFieldOutput;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            FieldArea

public class FieldOnRow
{

    public FieldOnRow(FieldArea fieldarea, int i)
    {
        _row = i;
        _fieldArea = fieldarea;
        _fieldOutput = fieldarea.getFieldOutput();
    }

    public int getElementColumn()
    {
        return _fieldArea.getElementColumn(_row);
    }

    public int getElementHeight()
    {
        return _fieldArea.getElementHeight();
    }

    public int getElementWidth()
    {
        return _fieldArea.getElementWidth(_row);
    }

    public String getFieldName()
    {
        return getFieldOutput().getFieldName();
    }

    public IFieldOutput getFieldOutput()
    {
        return _fieldOutput;
    }

    public boolean isElementOnRow()
    {
        if(isElementStartOnRow())
            return true;
        else
            return _row >= _fieldArea.getElementStartingRow() && _row < _fieldArea.getElementStartingRow() + getElementHeight();
    }

    public boolean isElementStartOnRow()
    {
        if(_fieldOutput.isSingleDHTMLElement())
            return isFirstElementRow();
        else
            return _row >= _fieldArea.getStartingRow() && _row <= _fieldArea.getLastRow();
    }

    public boolean isFirstElementRow()
    {
        return _row == _fieldArea.getElementStartingRow();
    }

    public boolean isFirstFieldRow()
    {
        return _row == _fieldArea.getStartingRow();
    }

    private int _row;
    private FieldArea _fieldArea;
    private IFieldOutput _fieldOutput;
}
