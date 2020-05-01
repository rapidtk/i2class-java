// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import java.io.Serializable;
import java.util.ResourceBundle;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            CursorPosition, ICursorBufferData, ActiveRecordTable, LocationOnDevice, 
//            RecordViewBean

public class CursorReturnData
    implements ICursorBufferData, Serializable
{

    public CursorReturnData(ActiveRecordTable activerecordtable)
    {
        _winRelativePosition = null;
        _winRelativePositionForBuffer = null;
        _locationOnDevice = null;
        _locationOnDevice = activerecordtable.getCurrentLocOnDevice();
        initPositions(activerecordtable);
    }

    public CursorPosition getAbsolutePosition()
    {
        return _absolutePosition;
    }

    public CursorPosition getWinRelativePosition()
    {
        return _winRelativePosition;
    }

    public CursorPosition getWinRelativePositionForBuffer()
    {
        return _winRelativePositionForBuffer;
    }

    private void setAbsolutePosition(CursorPosition cursorposition)
    {
        _absolutePosition = cursorposition;
    }

    private void setWinRelativePosition(CursorPosition cursorposition)
    {
        _winRelativePosition = cursorposition;
    }

    private void setWinRelativePositionForBuffer(CursorPosition cursorposition)
    {
        _winRelativePositionForBuffer = cursorposition;
    }

    private LocationOnDevice getLocationOnDevice()
    {
        return _locationOnDevice;
    }

    public String getRecordName()
    {
        String s = getLocationOnDevice().getRecord();
        if(s == null || !getLocationOnDevice().isValidForRTNCSRLOC())
            s = BLANK;
        return s;
    }

    public String getControlRecordName()
    {
        return getLocationOnDevice().getControlRecord();
    }

    public String getFieldName()
    {
        String s = getLocationOnDevice().getField();
        if(s == null || !getLocationOnDevice().isValidForRTNCSRLOC())
            s = BLANK;
        return s;
    }

    public int getRRN()
    {
        return getLocationOnDevice().getRRN();
    }

    private void initPositions(ActiveRecordTable activerecordtable)
    {
        CursorPosition cursorposition = activerecordtable.getCursor();
        int i = cursorposition.getRow();
        int j = cursorposition.getColumn();
        String s = getControlRecordName();
        RecordViewBean recordviewbean = null;
        if(s != null)
        {
            recordviewbean = activerecordtable.getRecordViewBean(getControlRecordName());
            if(recordviewbean != null && recordviewbean.isWindowed())
            {
                i += recordviewbean.getWdwFirstLine();
                j = j + recordviewbean.getWdwFirstColumn() + 1;
            }
        }
        setAbsolutePosition(new CursorPosition(i, j));
        RecordViewBean recordviewbean1 = activerecordtable.getRecordViewBeanBeingRead();
        if(recordviewbean1.isWindowed())
        {
            i -= recordviewbean1.getWdwFirstLine();
            j = j - recordviewbean1.getWdwFirstColumn() - 1;
        }
        setWinRelativePosition(new CursorPosition(i, j));
        if(!recordviewbean1.isWindowed())
        {
            i = 0;
            j = 0;
        } else
        if(!recordviewbean1.isInSameWindow(recordviewbean))
        {
            i = 255;
            j = 255;
        }
        setWinRelativePositionForBuffer(new CursorPosition(i, j));
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2000-2002");
    private static ResourceBundle _resmri;
    public static final String BLANK = new String("          ");
    private CursorPosition _absolutePosition;
    private CursorPosition _winRelativePosition;
    private CursorPosition _winRelativePositionForBuffer;
    private LocationOnDevice _locationOnDevice;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
