// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.controller;

import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingLevelCheckException;
import com.ibm.as400ad.webfacing.runtime.view.CursorPosition;
import com.ibm.as400ad.webfacing.runtime.view.IRecordSaveArea;
import java.util.Enumeration;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.controller:
//            IFieldValue

public class InputDeviceRequestHandler
{

    public InputDeviceRequestHandler(IRecordSaveArea irecordsavearea)
    {
        _recordSaveArea = irecordsavearea;
    }

    public boolean processDeviceInputRequest(String s, String s1, CursorPosition cursorposition, Enumeration enumeration)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        _recordSaveArea.prepareForRead();
        _recordSaveArea.setCursor(cursorposition);
        if(s == null)
            setChangedFieldValues(enumeration);
        else
            _recordSaveArea.setSystemMenuOption(s);
        return _recordSaveArea.setAIDKey(s1);
    }

    private void setChangedFieldValues(Enumeration enumeration)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        IFieldValue ifieldvalue;
        for(; enumeration.hasMoreElements(); _recordSaveArea.setFieldValue(ifieldvalue))
            ifieldvalue = (IFieldValue)enumeration.nextElement();

    }

    public boolean processRelativeRecordNumberRequest(String s, int i, CursorPosition cursorposition, Enumeration enumeration)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        _recordSaveArea.setCursor(cursorposition);
        setChangedFieldValues(enumeration);
        return _recordSaveArea.setRelativeRecordNumber(s, i);
    }

    public static final int RequestInputFromDevice = 0;
    public static final int ReadFromSaveArea = 1;
    IRecordSaveArea _recordSaveArea;
}
