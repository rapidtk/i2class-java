// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert;

import com.ibm.as400ad.code400.dom.constants.IFieldType;
import com.ibm.as400ad.code400.dom.constants.IWebSettingType;
import com.ibm.etools.iseries.webfacing.convert.external.*;

public interface ICustomTagExtensions
{

    public abstract IFieldTagGenerator getFieldTagGenerator(IFieldType ifieldtype);

    public abstract IFieldTagGenerator getFieldTagGenerator(String s);

    public abstract IWSTagGenerator getWebSettingTagGenerator(IWebSettingType iwebsettingtype);

    public abstract IWSTagGenerator getWebSettingTagGenerator(String s);

    public abstract IWSSubTagGenerator getWebSettingAttributeTagGenerator(IWebSettingType iwebsettingtype);

    public abstract IWSSubTagGenerator getWebSettingAttributeTagGenerator(String s);

    public static final String Copyright = "(c) Copyright IBM Corporation 2003.  All Rights Reserved.";
}
