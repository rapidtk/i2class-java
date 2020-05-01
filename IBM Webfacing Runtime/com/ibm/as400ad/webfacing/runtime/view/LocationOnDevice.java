// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import java.io.Serializable;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            CursorPosition

public class LocationOnDevice
    implements Serializable
{

    public LocationOnDevice(CursorPosition cursorposition, int i, String s, String s1)
    {
        _cursorPosition = null;
        _isValidForRTNCSRLOC = true;
        _cursorPosition = cursorposition;
        _layer = i;
        _record = s;
        _field = s1;
    }

    public LocationOnDevice(int i, String s, String s1)
    {
        this(null, i, s, s1);
    }

    public LocationOnDevice(CursorPosition cursorposition, int i, String s)
    {
        this(cursorposition, i, s, null);
    }

    public LocationOnDevice(int i, String s)
    {
        this(i, s, null);
    }

    public LocationOnDevice(CursorPosition cursorposition)
    {
        this(cursorposition, -1, null);
    }

    public LocationOnDevice()
    {
        this(null);
    }

    public String getTagID()
    {
        String s = "l" + _layer + "_" + getControlRecord() + "$" + _field;
        return s;
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("\nFieldOnDevice:");
        stringbuffer.append("\nlayer:" + _layer);
        stringbuffer.append("\nrecord:" + _record);
        stringbuffer.append("\nfield:" + _field);
        return stringbuffer.toString();
    }

    public int getLayer()
    {
        return _layer;
    }

    public String getRecord()
    {
        return _record;
    }

    public String getControlRecord()
    {
        return _record;
    }

    public String getField()
    {
        return _field;
    }

    public int getRRN()
    {
        return -1;
    }

    public CursorPosition getCursorPosition()
    {
        return _cursorPosition;
    }

    public boolean isBefore(LocationOnDevice locationondevice)
    {
        if(locationondevice == null)
            return true;
        if(getCursorPosition() == null || locationondevice.getCursorPosition() == null)
            throw new IllegalStateException("Cursor position must be initialized in this object and the parameter object.");
        else
            return getCursorPosition().isBefore(locationondevice.getCursorPosition());
    }

    public void setCursorPosition(CursorPosition cursorposition)
    {
        _cursorPosition = cursorposition;
    }

    public boolean isValidForRTNCSRLOC()
    {
        return _isValidForRTNCSRLOC;
    }

    public void setIsValidForRTNCSRLOC(boolean flag)
    {
        _isValidForRTNCSRLOC = flag;
    }

    static final String copyRight = new String("(C) Copyright IBM Corporation 2002");
    private int _layer;
    private String _record;
    private String _field;
    public static final int LAYER_NOT_SPECIFIED = -1;
    public static final int RRN_NOT_SPECIFIED = -1;
    private CursorPosition _cursorPosition;
    private boolean _isValidForRTNCSRLOC;

}
