// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.convert.gen.tag;

import com.ibm.as400ad.code400.dom.constants.IWebSettingType;
import com.ibm.etools.iseries.webfacing.convert.external.IRawWebSetting;

public class FieldRawWebSetting
    implements IRawWebSetting, IWebSettingType
{

    public FieldRawWebSetting(int i, String s)
    {
        _id = 0;
        _value = null;
        _id = i;
        _value = s;
    }

    public int getWebSettingId()
    {
        return _id;
    }

    public String getWebSettingValue()
    {
        return _value;
    }

    public static final String copyRight = new String(" (C) Copyright IBM Corporation 2000-2003 All rights reserved.");
    private int _id;
    private String _value;

}
