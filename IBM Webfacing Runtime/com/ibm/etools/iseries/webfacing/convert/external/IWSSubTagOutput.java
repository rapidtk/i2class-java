// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.convert.external;


// Referenced classes of package com.ibm.etools.iseries.webfacing.convert.external:
//            ICommonTagOutput

public interface IWSSubTagOutput
    extends ICommonTagOutput
{

    public abstract void setSubTag(String s);

    public abstract String getHtmlHeader();

    public abstract String getTagString();

    public static final String copyRight = new String(" (C) Copyright IBM Corporation 2002-2003. All rights reserved.");

}
