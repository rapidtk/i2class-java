// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.dhtmlview;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.dhtmlview:
//            ScreenBuilderHelper

public class ScreenBuilderServlet extends HttpServlet
{

    public ScreenBuilderServlet()
    {
    }

    public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException
    {
        ScreenBuilderHelper screenbuilderhelper = new ScreenBuilderHelper(httpservletrequest, httpservletresponse, getServletConfig().getServletContext());
        screenbuilderhelper.buildScreen();
    }

    public void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException
    {
        doGet(httpservletrequest, httpservletresponse);
    }

    static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2002, all rights reserved");

}
