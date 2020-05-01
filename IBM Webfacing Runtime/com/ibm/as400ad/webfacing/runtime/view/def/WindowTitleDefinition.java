// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.webfacing.runtime.view.RecordViewBean;
import java.io.Serializable;

public class WindowTitleDefinition
    implements Serializable
{

    public WindowTitleDefinition(String s, boolean flag, String s1)
    {
        this(s, flag, s1, "", "", "", "");
    }

    public WindowTitleDefinition(String s, boolean flag, String s1, String s2, String s3, String s4, String s5)
    {
        _indicatorExpression = s;
        _constantAsTitle = flag;
        _text = s1;
        _color = s2;
        _dspAtr = s3;
        _alignment = s4;
        _position = s5;
    }

    protected String getWindowTitle(RecordViewBean recordviewbean, int i)
    {
        if(_text.equals(""))
            return _text;
        String s;
        if(_constantAsTitle)
            s = _text;
        else
            s = recordviewbean.getFieldValue(_text);
        if(i != -1 && s.length() > i)
            return s.substring(0, i);
        else
            return s;
    }

    protected String getWindowTitleAlignment()
    {
        return _alignment;
    }

    protected boolean isActive(RecordViewBean recordviewbean)
    {
        return recordviewbean.evaluateIndicatorExpression(_indicatorExpression);
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2002.  All Rights Reserved.";
    private String _indicatorExpression;
    private boolean _constantAsTitle;
    private String _text;
    private String _color;
    private String _dspAtr;
    private String _alignment;
    private String _position;
}
