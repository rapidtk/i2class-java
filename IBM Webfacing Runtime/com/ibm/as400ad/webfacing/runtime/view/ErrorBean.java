// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;

public class ErrorBean
    implements Serializable
{

    public ErrorBean()
    {
        exception = null;
        endUserMessage = null;
        adminMessage = null;
        details = null;
        detailLevel = 3;
        detailLevel = 0;
    }

    public ErrorBean(String s)
    {
        this();
        endUserMessage = s;
        detailLevel = 1;
    }

    public ErrorBean(String s, String s1)
    {
        this(s);
        adminMessage = s1;
        detailLevel = 2;
    }

    public ErrorBean(String s, String s1, Vector vector)
    {
        this(s, s1);
        details = vector;
        detailLevel = 3;
    }

    public ErrorBean(Throwable throwable)
    {
        this();
        exception = throwable;
        detailLevel = 3;
    }

    public void appendDetail(String s)
    {
        details.addElement(s);
    }

    public void extractExceptionInfo()
    {
        extractExceptionInfo(false);
    }

    public void extractExceptionInfo(boolean flag)
    {
        if(exception != null)
        {
            if(!flag)
            {
                endUserMessage = "";
                adminMessage = "";
                details = new Vector();
            }
            if(exception.getMessage() != null)
                endUserMessage = endUserMessage + exception.getMessage();
            adminMessage = adminMessage + exception.toString();
            StringWriter stringwriter = new StringWriter();
            exception.printStackTrace(new PrintWriter(stringwriter));
            StringTokenizer stringtokenizer = new StringTokenizer(stringwriter.toString(), "\n");
            Object obj = null;
            while(stringtokenizer.hasMoreTokens()) 
            {
                String s = stringtokenizer.nextToken();
                if(!s.trim().equals(""))
                    details.addElement(s);
            }
        }
    }

    public String getAdminMessage()
    {
        return adminMessage;
    }

    public int getDetailLevel()
    {
        return detailLevel;
    }

    public Vector getDetails()
    {
        return details;
    }

    public String getDetails(int i)
        throws ArrayIndexOutOfBoundsException
    {
        return (String)details.elementAt(i);
    }

    public String getEndUserMessage()
    {
        return endUserMessage;
    }

    public Throwable getException()
    {
        return exception;
    }

    public void setAdminMessage(String s)
    {
        adminMessage = s;
    }

    public void setDetailLevel(int i)
    {
        detailLevel = i;
    }

    public void setDetails(int i, String s)
        throws ArrayIndexOutOfBoundsException
    {
        details.add(i, s);
    }

    public void setDetails(Vector vector)
    {
        details = vector;
    }

    public void setEndUserMessage(String s)
    {
        endUserMessage = s;
    }

    public void setException(Exception exception1)
    {
        exception = exception1;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2000, 2002.  All Rights Reserved.";
    private Throwable exception;
    private String endUserMessage;
    private String adminMessage;
    private Vector details;
    private int detailLevel;
}
