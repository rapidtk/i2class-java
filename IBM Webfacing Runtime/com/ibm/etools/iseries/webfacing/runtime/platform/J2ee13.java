// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.runtime.platform;

import java.io.UnsupportedEncodingException;
import javax.servlet.ServletRequest;

// Referenced classes of package com.ibm.etools.iseries.webfacing.runtime.platform:
//            J2eeLevel

public class J2ee13
    implements J2eeLevel
{

    public J2ee13()
    {
    }

    public void wfSetCharacterEncoding(ServletRequest servletrequest)
    {
        try
        {
            servletrequest.setCharacterEncoding("UTF-8");
        }
        catch(UnsupportedEncodingException unsupportedencodingexception) { }
    }

    public String getJ2EELevel()
    {
        return "J2EE13";
    }
}
