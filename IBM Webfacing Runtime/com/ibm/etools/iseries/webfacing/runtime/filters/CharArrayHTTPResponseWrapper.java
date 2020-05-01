// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.runtime.filters;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class CharArrayHTTPResponseWrapper extends HttpServletResponseWrapper
{

    public CharArrayHTTPResponseWrapper(HttpServletResponse httpservletresponse)
    {
        super(httpservletresponse);
        charWriter = new CharArrayWriter();
    }

    public PrintWriter getWriter()
    {
        return new PrintWriter(charWriter);
    }

    public char[] toCharArray()
    {
        return charWriter.toCharArray();
    }

    public String toString()
    {
        return charWriter.toString();
    }

    private CharArrayWriter charWriter;
}
