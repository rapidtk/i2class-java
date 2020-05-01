// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.util;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.util:
//            TraceThread

public class TraceCatcher
{

    TraceCatcher()
    {
    }

    static String getResourceString(String s)
    {
        try
        {
            return _messagesBundle.getString(s);
        }
        catch(MissingResourceException missingresourceexception)
        {
            System.out.println("Error: Resource " + s + " not found");
        }
        return "Error: Resource " + s + " not found";
    }

    private static void handleException(Exception exception)
    {
        System.err.println(exception.toString());
        System.err.println(exception.getMessage());
        exception.printStackTrace();
    }

    private static boolean init_Initialization(String s)
    {
        if(s.equals(""))
        {
            Locale locale = Locale.getDefault();
            s = locale.getLanguage();
            country = locale.getCountry();
            variant = locale.getVariant();
        }
        _messagesBundle = null;
        try
        {
            _messagesBundle = ResourceBundle.getBundle("com.ibm.as400ad.webfacing.util.MessageStrings");
        }
        catch(MissingResourceException missingresourceexception)
        {
            System.out.println("No resource file for language=\"" + s + "\" country=\"" + country + "\" variant=\"" + variant + "\"");
            return false;
        }
        if(_messagesBundle == null)
        {
            System.out.println("Can't create ResourceBundle for language=\"" + s + "\" country=\"" + country + "\" variant=\"" + variant + "\"");
            return false;
        } else
        {
            return true;
        }
    }

    public static void main(String args[])
    {
        if(!init_Initialization(""))
            System.exit(-1);
        showMessage("COPYRIGHT_MSG", null);
        String s = System.getProperty("JT_PORT");
        if(s != null && !s.equals(""))
        {
            try
            {
                tracePort = Integer.parseInt(s);
            }
            catch(NumberFormatException numberformatexception)
            {
                showError("INVALID_JT_PORT", s);
                System.exit(-1);
            }
            if(tracePort < 8000 || tracePort > 8999)
            {
                showError("INVALID_PORT", Integer.toString(tracePort));
                System.exit(-1);
            }
        }
        s = System.getProperty("JT_LOG");
        if(s != null)
        {
            traceLog = true;
            if(traceLog)
                try
                {
                    osLog = new PrintWriter(new FileOutputStream(traceLogName), true);
                }
                catch(Exception exception)
                {
                    showMessage("CANNOT_OPEN_CATCHER_FILE", traceLogName);
                }
        }
        try
        {
            server = new ServerSocket(tracePort, 0);
        }
        catch(IOException ioexception)
        {
            showError("PORT_IN_USE_MSG", null);
            System.err.println(ioexception.getMessage());
            System.exit(-1);
        }
        catch(Exception exception1)
        {
            handleException(exception1);
            System.exit(-1);
        }
        TraceThread atracethread[] = new TraceThread[20];
        boolean flag = false;
        for(int i = 0; i < 20;)
            do
            {
                do
                    for(i = 0; i < 20; i++)
                    {
                        if(atracethread[i] != null && atracethread[i].socket == null)
                            atracethread[i] = null;
                        if(atracethread[i] != null)
                            continue;
                        try
                        {
                            socket = server.accept();
                        }
                        catch(Exception exception2)
                        {
                            handleException(exception2);
                            break;
                        }
                        atracethread[i] = new TraceThread(i, socket, osLog, args);
                        break;
                    }

                while(i < 20);
                System.out.println("*************** connection overload");
                System.exit(-1);
            } while(true);

    }

    private static void showError(String s, String s1)
    {
        String s2 = getResourceString("JT_CATCHER") + getResourceString(s);
        if(s1 != null)
            s2 = s2 + " " + s1;
        System.err.println(s2);
        if(osLog != null)
            osLog.println(s2);
    }

    private static void showMessage(String s, String s1)
    {
        String s2 = getResourceString("JT_CATCHER") + getResourceString(s);
        if(s1 != null)
            s2 = s2 + " " + s1;
        System.out.println(s2);
        if(osLog != null)
            osLog.println(s2);
    }

    public static final int MAX_CONNECTIONS = 20;
    private static String traceLogName = "JT_Catch.txt";
    private static int tracePort = 8800;
    private static String language = "";
    private static String country = "";
    private static String variant = "";
    private static boolean traceLog = false;
    private static PrintWriter osLog = null;
    private static ResourceBundle _messagesBundle;
    private static ServerSocket server = null;
    private static Socket socket = null;

}
