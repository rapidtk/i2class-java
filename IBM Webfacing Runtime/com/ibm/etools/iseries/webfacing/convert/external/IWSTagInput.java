// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.convert.external;

import java.util.Iterator;

// Referenced classes of package com.ibm.etools.iseries.webfacing.convert.external:
//            IWSSubTagInput, IRawWebSetting

public interface IWSTagInput
    extends IWSSubTagInput
{

    public abstract Iterator getSubWebSettings();

    public abstract String getSubTag(IRawWebSetting irawwebsetting);

    public static final String copyRight = new String(" (C) Copyright IBM Corporation 2002. All rights reserved.");

}
