// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.dhtmlview;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.runtime.view.*;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class HtmlReportGenerator
{

    public HtmlReportGenerator()
    {
    }

    public static void writeDDSDetails(HttpServletResponse httpservletresponse, String s, IScreenBuilder iscreenbuilder)
        throws WebfacingInternalException
    {
        PrintWriter printwriter;
        try
        {
            httpservletresponse.setContentType("text/html");
            printwriter = httpservletresponse.getWriter();
        }
        catch(IOException ioexception)
        {
            throw new WebfacingInternalException(WebfacingConstants.replaceSubstring(_resmri.getString("WF0024"), "&1", "HtmlReportGenerator.writeDDSDetails()"));
        }
        printwriter.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\">");
        printwriter.println("<html>");
        printwriter.println("<head>");
        printwriter.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
        printwriter.println("<title>" + s + " - " + _resmri.getString("DDS_details_") + "</title>");
        printwriter.println("</head>");
        printwriter.println("<body>");
        printwriter.println("<h1 align='center'> " + _resmri.getString("DDS_details_about_") + "</h1>");
        Iterator iterator = iscreenbuilder.getRecordLayersOnDevice().iterator();
        boolean flag = false;
        Object obj = null;
        while(iterator.hasNext()) 
        {
            IDeviceLayer idevicelayer = (IDeviceLayer)iterator.next();
            RecordViewBean recordviewbean;
            for(Iterator iterator1 = idevicelayer.getRecords(); iterator1.hasNext(); printwriter.println(recordviewbean.toHTML()))
                recordviewbean = (RecordViewBean)iterator1.next();

        }
        printwriter.println("</body>");
        printwriter.println("</html>");
    }

    private static ITraceLogger _trace = WFSession.getTraceLogger();
    private static ResourceBundle _resmri;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
