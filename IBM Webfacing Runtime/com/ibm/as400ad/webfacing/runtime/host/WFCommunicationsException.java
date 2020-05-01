// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import com.ibm.as400ad.webfacing.runtime.core.WFException;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import java.io.*;

public class WFCommunicationsException extends WFException
{

    public WFCommunicationsException(WebfacingInternalException webfacinginternalexception, String s)
    {
        super(s);
        _ioexc = webfacinginternalexception;
    }

    public WFCommunicationsException(IOException ioexception, String s)
    {
        super(s);
        _ioexc = ioexception;
    }

    public void printStackTrace(PrintStream printstream)
    {
        printstream.println(getMessage());
        printstream.println("\nOriginating IOException:");
        _ioexc.printStackTrace(printstream);
        printstream.println("\n\nSurfaced exception:");
        super.printStackTrace(printstream);
    }

    public void printStackTrace(PrintWriter printwriter)
    {
        printwriter.println(getMessage());
        printwriter.println("Originating IOException:");
        _ioexc.printStackTrace(printwriter);
        printwriter.println("\nSurfaced exception:");
        super.printStackTrace(printwriter);
    }

    Exception _ioexc;
}
