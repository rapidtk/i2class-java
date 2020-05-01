// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import java.util.Iterator;
import java.util.List;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            FieldOnRow, RecordLayout, FieldOutputEnumeration

public class RecordLayoutRow
{

    protected RecordLayoutRow(RecordLayout recordlayout, int i)
    {
        _rl = null;
        _row = 0;
        _rl = recordlayout;
        _row = i;
        _fieldsOnRow = recordlayout.getFieldsOnRow(i);
    }

    public FieldOutputEnumeration getFields()
    {
        FieldOutputEnumeration fieldoutputenumeration = null;
        if(null != _rl)
            fieldoutputenumeration = _rl.getFieldsForRow(_row);
        return fieldoutputenumeration;
    }

    public Iterator getFieldsOnRow()
    {
        return _fieldsOnRow.iterator();
    }

    public int getRowNumber()
    {
        return _row;
    }

    public boolean hasFieldsOnRow()
    {
        return _fieldsOnRow.size() > 0;
    }

    public boolean isElementStartOnRow()
    {
        for(Iterator iterator = getFieldsOnRow(); iterator.hasNext();)
        {
            FieldOnRow fieldonrow = (FieldOnRow)iterator.next();
            if(fieldonrow.isElementStartOnRow())
                return true;
        }

        return false;
    }

    public boolean isEmpty()
    {
        return _rl.isEmpty(_row);
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000");
    private RecordLayout _rl;
    private int _row;
    private List _fieldsOnRow;

}
