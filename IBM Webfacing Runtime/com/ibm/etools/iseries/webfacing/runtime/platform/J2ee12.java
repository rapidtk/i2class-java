// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.runtime.platform;

import javax.servlet.ServletRequest;

// Referenced classes of package com.ibm.etools.iseries.webfacing.runtime.platform:
//            J2eeLevel

public class J2ee12
    implements J2eeLevel
{

    public J2ee12()
    {
    }

    public void wfSetCharacterEncoding(ServletRequest servletrequest)
    {
    }

    public String getJ2EELevel()
    {
        return "J2EE12";
    }
}
