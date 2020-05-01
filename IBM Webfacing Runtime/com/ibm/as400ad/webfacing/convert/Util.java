// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert;

import com.ibm.as400ad.code400.dom.Logger;

// Referenced classes of package com.ibm.as400ad.webfacing.convert:
//            ExportHandler

public class Util
{

    public Util()
    {
    }

    public static void logThrowableMessage(String s, Throwable throwable, boolean flag)
    {
        Logger logger = ExportHandler.getLogger();
        if(logger != null)
            logger.logThrowable(s, throwable, flag);
    }

    public static void showDebugMessage(String s)
    {
        ExportHandler.dbg(2, s);
    }

    public static void showFrameMessage(String s)
    {
        ExportHandler.dbg(1, "FrameMessage:" + s);
    }
}
