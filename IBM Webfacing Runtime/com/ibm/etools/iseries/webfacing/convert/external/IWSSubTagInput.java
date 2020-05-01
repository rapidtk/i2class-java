// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.convert.external;


// Referenced classes of package com.ibm.etools.iseries.webfacing.convert.external:
//            IFieldInput, IRawWebSetting

public interface IWSSubTagInput
    extends IFieldInput
{

    public abstract IRawWebSetting getWebSetting();

    public abstract String replaceFieldValueSymbol(String s, boolean flag, String s1);

    public static final String copyRight = new String(" (C) Copyright IBM Corporation 2002. All rights reserved.");

}
