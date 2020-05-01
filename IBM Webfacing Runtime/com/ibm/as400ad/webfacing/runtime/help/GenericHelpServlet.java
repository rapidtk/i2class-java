// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;

import java.io.IOException;
import java.io.PrintStream;
import javax.servlet.*;
import javax.servlet.http.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.help:
//            HelpException, IAppHelpTable

public abstract class GenericHelpServlet extends HttpServlet
{

    public GenericHelpServlet()
    {
        _helpLookup = null;
    }

    public void displayHelp(Object obj)
        throws HelpException
    {
        try
        {
            _servletContext.getRequestDispatcher((String)obj).include(_request, _response);
        }
        catch(Exception exception)
        {
            throw new HelpException("Error displaying help" + obj);
        }
    }

    public abstract void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException;

    private void doHelp()
    {
        try
        {
            Object obj = retrieveKeyParm();
            _helpLookup = retrieveHelpLookup();
            Object obj1 = _helpLookup.getOnlineHelp(obj);
            displayHelp(obj1);
        }
        catch(HelpException helpexception)
        {
            handleHelpException(helpexception);
        }
        catch(Exception exception)
        {
            handleException(exception);
        }
    }

    public abstract void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException;

    public IAppHelpTable getHelpLookup()
        throws HelpException
    {
        return _helpLookup;
    }

    public void handleException(Exception exception)
    {
        System.out.println("Exception: " + exception.toString());
    }

    public void handleHelpException(HelpException helpexception)
    {
        System.out.println("Exception: " + helpexception.toString());
    }

    public abstract IAppHelpTable retrieveHelpLookup()
        throws HelpException;

    public abstract Object retrieveKeyParm();

    public void setHelpLookup(IAppHelpTable iapphelptable)
    {
        _helpLookup = iapphelptable;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2001, 2002, all rights reserved");
    private IAppHelpTable _helpLookup;
    protected HttpServletRequest _request;
    protected HttpServletResponse _response;
    protected ServletContext _servletContext;
    protected HttpSession _session;

}
