// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.runtime.filters;

import java.io.*;
import java.util.zip.GZIPOutputStream;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Referenced classes of package com.ibm.etools.iseries.webfacing.runtime.filters:
//            CharArrayHTTPResponseWrapper

public class CompressionFilter
    implements Filter
{

    public CompressionFilter()
    {
    }

    public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain filterchain)
        throws ServletException, IOException
    {
        HttpServletRequest httpservletrequest = (HttpServletRequest)servletrequest;
        HttpServletResponse httpservletresponse = (HttpServletResponse)servletresponse;
        if(!isClientGzipEnabled(httpservletrequest))
        {
            filterchain.doFilter(httpservletrequest, httpservletresponse);
        } else
        {
            httpservletresponse.setHeader("Content-Encoding", "gzip");
            CharArrayHTTPResponseWrapper chararrayhttpresponsewrapper = new CharArrayHTTPResponseWrapper(httpservletresponse);
            filterchain.doFilter(httpservletrequest, chararrayhttpresponsewrapper);
            char ac[] = chararrayhttpresponsewrapper.toCharArray();
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            GZIPOutputStream gzipoutputstream = new GZIPOutputStream(bytearrayoutputstream);
            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(gzipoutputstream);
            outputstreamwriter.write(ac);
            outputstreamwriter.close();
            javax.servlet.ServletOutputStream servletoutputstream = httpservletresponse.getOutputStream();
            bytearrayoutputstream.writeTo(servletoutputstream);
        }
    }

    public void init(FilterConfig filterconfig)
        throws ServletException
    {
        config = filterconfig;
    }

    protected FilterConfig getFilterConfig()
    {
        return config;
    }

    public void destroy()
    {
    }

    private boolean isClientGzipEnabled(HttpServletRequest httpservletrequest)
    {
        String s = httpservletrequest.getHeader("Accept-Encoding");
        return s != null && s.indexOf("gzip") != -1;
    }

    private FilterConfig config;
}
