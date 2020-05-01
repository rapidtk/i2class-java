// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;


// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            LocationOnDevice, CursorPosition

public class SubfileLocationOnDevice extends LocationOnDevice
{

    public SubfileLocationOnDevice(CursorPosition cursorposition, int i, String s, String s1, int j, String s2)
    {
        super(cursorposition, i, s, s1);
        _rrn = j;
        _subfileControl = s2;
    }

    public SubfileLocationOnDevice(CursorPosition cursorposition, int i, String s, int j, String s1)
    {
        this(cursorposition, i, s, null, j, s1);
    }

    public SubfileLocationOnDevice(CursorPosition cursorposition, int i, String s, String s1)
    {
        this(cursorposition, i, s, -1, s1);
    }

    public SubfileLocationOnDevice(int i, String s, String s1, int j, String s2)
    {
        this(null, i, s, s1, j, s2);
    }

    public SubfileLocationOnDevice(LocationOnDevice locationondevice, int i, String s)
    {
        super(locationondevice.getLayer(), locationondevice.getRecord(), locationondevice.getField());
        _rrn = i;
        _subfileControl = s;
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append(super.toString());
        stringbuffer.append("\nrrn:" + _rrn);
        return stringbuffer.toString();
    }

    public int getRRN()
    {
        return _rrn;
    }

    public String getControlRecord()
    {
        return _subfileControl;
    }

    public String getTagID()
    {
        String s = super.getTagID() + "$" + _rrn;
        return s;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2002");
    private int _rrn;
    private String _subfileControl;

}
