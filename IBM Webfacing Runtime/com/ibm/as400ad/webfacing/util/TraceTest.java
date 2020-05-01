// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.util;

import java.io.PrintStream;

// Referenced classes of package com.ibm.as400ad.webfacing.util:
//            TraceLogger, TraceFrame

public class TraceTest
{

    TraceTest()
    {
    }

    public static void main(String args[])
    {
        String s = "";
        if(args.length > 0)
            s = args[0];
        TraceLogger tracelogger = new TraceLogger(s);
        tracelogger.milestoneTimerStart("Initial Load");
        tracelogger.setDebugLevel(4);
        tracelogger.setErrorLevel(3);
        tracelogger.setEventLevel(3);
        tracelogger.setMessageLevel(3);
        tracelogger.setTimings(true);
        System.out.println("this is normal System.out, text-1");
        System.err.println("this is normal System.err, text-1");
        tracelogger.milestoneTimerElapseTime("Initial messages");
        try
        {
            Thread.sleep(100L);
        }
        catch(InterruptedException interruptedexception) { }
        catch(Exception exception)
        {
            System.out.println("unexpected exception");
        }
        try
        {
            Thread.sleep(200L);
        }
        catch(InterruptedException interruptedexception1) { }
        catch(Exception exception1)
        {
            System.out.println("unexpected exception");
        }
        try
        {
            Thread.sleep(300L);
        }
        catch(InterruptedException interruptedexception2) { }
        catch(Exception exception2)
        {
            System.out.println("unexpected exception");
        }
        tracelogger.err(1, "this is a JT_ERR message, level 1, NO FRAME");
        try
        {
            Thread.sleep(5000L);
        }
        catch(InterruptedException interruptedexception3) { }
        catch(Exception exception3)
        {
            System.out.println("unexpected exception");
        }
        tracelogger.milestoneTimerElapseTime("Beginning of Frames");
        TraceFrame traceframe = new TraceFrame();
        tracelogger.err(1, "this is a JT_ERR message, level 1, FRAME", traceframe);
        tracelogger.err(2, "this is a JT_ERR message, level 2, FRAME", traceframe);
        tracelogger.err(3, "this is a JT_ERR message, level 3, FRAME", traceframe);
        try
        {
            Thread.sleep(5000L);
        }
        catch(InterruptedException interruptedexception4) { }
        catch(Exception exception4) { }
        tracelogger.dbg(1, "this is JT_DBG message, about to flush(), then exit");
        tracelogger.milestoneTimerElapseTimeAndStop("End of all tracing");
        tracelogger.flush();
        System.exit(0);
    }
}
