// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.util;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

// Referenced classes of package com.ibm.as400ad.webfacing.util:
//            TraceCatcher

class TraceThread
    implements Runnable
{

    public TraceThread(int i, Socket socket1, PrintWriter printwriter, String as[])
    {
        socket = null;
        thrd = null;
        threadNumber = 0;
        osLog = null;
        appNames = null;
        numberOfAppNames = 0;
        socket = socket1;
        osLog = printwriter;
        if(as.length > 0)
        {
            numberOfAppNames = as.length;
            appNames = new String[numberOfAppNames];
            for(int j = 0; j < as.length; j++)
                appNames[j] = "_" + as[j];

        }
        thrd = new Thread(this);
        thrd.start();
        threadNumber = i;
    }

    private void handleException(Exception exception)
    {
        System.err.println(threadNumber + ":" + TraceCatcher.getResourceString("JT_CATCHER") + TraceCatcher.getResourceString("CAUGHT_EXCEPTION") + exception.toString());
        System.err.println(threadNumber + ":" + TraceCatcher.getResourceString("JT_CATCHER") + exception.getMessage());
        exception.printStackTrace(System.err);
        if(osLog != null)
        {
            osLog.println(threadNumber + ":" + TraceCatcher.getResourceString("JT_CATCHER") + TraceCatcher.getResourceString("CAUGHT_EXCEPTION") + exception.toString());
            osLog.println(threadNumber + ":" + TraceCatcher.getResourceString("JT_CATCHER") + exception.getMessage());
            exception.printStackTrace(osLog);
        }
    }

    public void run()
    {
        showMessage("CONNECTION_ESTABLISHED_MSG");
        Object obj = null;
        BufferedReader bufferedreader = null;
        try
        {
            java.io.InputStream inputstream = socket.getInputStream();
            bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
            System.setIn(inputstream);
        }
        catch(Exception exception)
        {
            showError("UNABLE_TO_ASSIGN_SOCKET_TO SYSTEM_IN");
            handleException(exception);
            return;
        }
        try
        {
            for(String s = ""; s != null;)
            {
                s = bufferedreader.readLine();
                showData(s);
            }

        }
        catch(SocketException socketexception)
        {
            showMessage("CONNECTION_LOST_MSG");
        }
        catch(Exception exception2)
        {
            handleException(exception2);
        }
        try
        {
            if(socket != null)
                socket.close();
        }
        catch(Exception exception1)
        {
            handleException(exception1);
        }
        socket = null;
    }

    private void showData(String s)
    {
        if(numberOfAppNames >= 1)
        {
            int i = 0;
            for(i = 0; i < numberOfAppNames; i++)
            {
                int j = appNames[i].length();
                if(5 + j > s.length())
                    continue;
                String s1 = s.substring(5, 5 + j);
                if(s1.equals(appNames[i]))
                    break;
            }

            if(i == numberOfAppNames && s.length() >= 6 && s.charAt(1) == ':' && s.charAt(5) == '_')
                return;
        }
        System.out.println(threadNumber + ":" + s);
        if(osLog != null)
            osLog.println(threadNumber + ":" + s);
    }

    private void showError(String s)
    {
        System.err.println(threadNumber + ":" + TraceCatcher.getResourceString("JT_CATCHER") + TraceCatcher.getResourceString(s));
        if(osLog != null)
            osLog.println(threadNumber + ":" + TraceCatcher.getResourceString("JT_CATCHER") + TraceCatcher.getResourceString(s));
    }

    private void showMessage(String s)
    {
        System.out.println(threadNumber + ":" + TraceCatcher.getResourceString("JT_CATCHER") + TraceCatcher.getResourceString(s));
        if(osLog != null)
            osLog.println(threadNumber + ":" + TraceCatcher.getResourceString("JT_CATCHER") + TraceCatcher.getResourceString(s));
    }

    public Socket socket;
    private Thread thrd;
    private int threadNumber;
    private PrintWriter osLog;
    private String appNames[];
    private int numberOfAppNames;
}
