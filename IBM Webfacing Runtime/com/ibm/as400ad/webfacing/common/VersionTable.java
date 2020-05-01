// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.common;

import java.util.HashMap;

// Referenced classes of package com.ibm.as400ad.webfacing.common:
//            Version

public final class VersionTable
{

    public VersionTable()
    {
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999=2003, all rights reserved.");
    public static final HashMap VERSIONS;
    public static final long V5R1M0SP4c;
    public static final long V5R1M0SP5;
    public static final long V5R1M1SP0;
    public static final long V5R1M1SP1;

    static 
    {
        VERSIONS = new HashMap();
        V5R1M0SP4c = Version.getVersionDigits(5, 1, 0, 4, 'c');
        V5R1M0SP5 = Version.getVersionDigits(5, 1, 0, 5);
        V5R1M1SP0 = Version.getVersionDigits(5, 1, 1, 0);
        V5R1M1SP1 = Version.getVersionDigits(5, 1, 1, 1);
        VERSIONS.put("V5R1M0SP4c", new Long(V5R1M0SP4c));
        VERSIONS.put("V5R1M0SP5", new Long(V5R1M0SP5));
        VERSIONS.put("V5R1M1SP0", new Long(V5R1M1SP0));
        VERSIONS.put("V5R1M1SP1", new Long(V5R1M1SP1));
    }
}
