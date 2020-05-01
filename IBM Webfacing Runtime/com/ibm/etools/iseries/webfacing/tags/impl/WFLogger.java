// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.tags.impl;


// Referenced classes of package com.ibm.etools.iseries.webfacing.tags.impl:
//            WFBodyTagSupport

public class WFLogger extends WFBodyTagSupport
{

    public WFLogger()
    {
        type = MT;
        sev = -1;
        msg = MT;
    }

    public int doStartTag()
    {
        int i = 0;
        logMsg(type, sev, msg);
        return i;
    }

    public void release()
    {
        type = MT;
        sev = -1;
        msg = MT;
        super.release();
    }

    public String getMsg()
    {
        return msg;
    }

    public int getSev()
    {
        return sev;
    }

    public String getType()
    {
        return type;
    }

    public void setMsg(String s)
    {
        msg = s;
    }

    public void setSev(String s)
    {
        sev = Integer.parseInt(s);
    }

    public void setSev(int i)
    {
        sev = i;
    }

    public void setType(String s)
    {
        type = s;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2002.  All Rights Reserved.";
    private static String MT = "";
    private String type;
    private int sev;
    private String msg;

}
