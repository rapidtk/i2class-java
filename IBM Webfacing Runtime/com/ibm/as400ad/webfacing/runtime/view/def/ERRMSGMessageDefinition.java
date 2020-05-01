// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.webfacing.runtime.view.IFieldMessageDefinition;
import com.ibm.as400ad.webfacing.runtime.view.LocationOnDevice;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            MSGMessageDefinition

public class ERRMSGMessageDefinition extends MSGMessageDefinition
    implements IFieldMessageDefinition
{

    public ERRMSGMessageDefinition(String s, String s1)
    {
        super(s1);
        _fieldName = s;
    }

    public String getFieldName()
    {
        return _fieldName;
    }

    public LocationOnDevice getLocationOnDevice()
    {
        return new LocationOnDevice(getRecordLayer(), getRecordName(), getFieldName());
    }

    private String _fieldName;
}
