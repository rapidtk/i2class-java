// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import com.ibm.as400ad.code400.dom.ui.WebFaceStatusDialog;
import com.ibm.as400ad.webfacing.convert.ExportHandler;
import com.ibm.as400ad.webfacing.convert.ICustomTagExtensions;
import com.ibm.as400ad.webfacing.convert.util.EventLog;
import com.ibm.as400ad.webfacing.convert.util.TranslatableStrings;
import java.io.IOException;
import java.util.ResourceBundle;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            Logger, ElapsedTime, XMLParser, ExportSettings, 
//            AnyNode, FileNode

public class WebfaceInvoker
    implements Runnable
{

    public WebfaceInvoker(String s, ICustomTagExtensions icustomtagextensions)
    {
        xmlFile = s;
        _cte = icustomtagextensions;
    }

    public static void invokeWebFacing(FileNode filenode, ICustomTagExtensions icustomtagextensions)
        throws Throwable
    {
        try
        {
            ExportSettings exportsettings = ExportSettings.getExportSettings();
            String as[] = {
                exportsettings.getConversionFactory(), "com.ibm.as400ad.webfacing.convert.WebFaceConverter"
            };
            exportsettings.setCustomTagExtensions(icustomtagextensions);
            if(statusDlg == null)
                ExportHandler.exportInvoker(as, filenode);
            else
                ExportHandler.exportInvoker(as, filenode, statusDlg);
        }
        catch(Throwable throwable)
        {
            try
            {
                EventLog.logException(throwable, null != filenode ? filenode.getName() : "");
            }
            catch(IOException ioexception)
            {
                throw throwable;
            }
        }
    }

    public static boolean invokeWebFacing(String s, ICustomTagExtensions icustomtagextensions)
    {
        WebfaceInvoker webfaceinvoker = new WebfaceInvoker(s, icustomtagextensions);
        if(statusDlg == null)
            webfaceinvoker.run();
        return webfaceinvoker.ok;
    }

    public static boolean invokeWebFacing(String s, String s1, String s2, ICustomTagExtensions icustomtagextensions)
    {
        wdtRootDir = s.substring(0, s.indexOf("tmp") - 1);
        wfDirectory = s1;
        tmpDirectory = s2;
        return invokeWebFacing(s, icustomtagextensions);
    }

    public static boolean invokeWebFacing(String s, String s1, String s2, String s3, ICustomTagExtensions icustomtagextensions)
    {
        ExportSettings.initializeExportSettings(s3);
        return invokeWebFacing(s, s1, s2, icustomtagextensions);
    }

    public static boolean invokeWebFacing(String s, String s1, String s2, String s3, ResourceBundle resourcebundle, ICustomTagExtensions icustomtagextensions)
    {
        TranslatableStrings.setResourceBundle(resourcebundle);
        return invokeWebFacing(s, s1, s2, s3, icustomtagextensions);
    }

    public static void main(String args[])
    {
        if(args != null && args.length == 4)
            invokeWebFacing(restoreBlanks(args[0]), restoreBlanks(args[1]), restoreBlanks(args[2]), restoreBlanks(args[3]), ExportSettings.getExportSettings().getCustomTagExtensions());
        System.exit(0);
    }

    public void run()
    {
        FileNode filenode = null;
        Logger logger = new Logger();
        try
        {
            ElapsedTime elapsedtime = null;
            if(Logger.DBG)
            {
                logger.setLogExceptionsOnlyToSysOut(false);
                elapsedtime = new ElapsedTime();
                elapsedtime.setStartTime();
            }
            filenode = justParse(xmlFile, tmpDirectory, wfDirectory, logger);
            ok = filenode != null;
            if(ok)
            {
                if(statusDlg != null)
                    statusDlg.populating();
                try
                {
                    invokeWebFacing(filenode, _cte);
                }
                catch(Throwable throwable)
                {
                    logger.logThrowable("Event log could not be created to record fatal exception", throwable, true);
                }
                if(Logger.DBG)
                {
                    elapsedtime.setEndTime();
                    logger.logMessage("Entire parse, populate and webfacing done. " + elapsedtime);
                }
            }
        }
        finally
        {
            if(statusDlg != null)
                statusDlg.done();
            logger.close();
            if(filenode != null)
                FileNode.cleanup();
        }
    }

    public static FileNode justParse(String s, String s1, String s2, Logger logger)
    {
        ElapsedTime elapsedtime = null;
        ElapsedTime elapsedtime1 = null;
        boolean flag = false;
        FileNode filenode = null;
        try
        {
            if(null == logger)
            {
                logger = new Logger();
                flag = true;
            }
            if(Logger.DBG)
            {
                logger.setLogExceptionsOnlyToSysOut(false);
                elapsedtime = new ElapsedTime();
                elapsedtime1 = new ElapsedTime();
                elapsedtime.setStartTime();
                elapsedtime1.setStartTime();
                if(statusDlg != null)
                    statusDlg.parsing();
            }
            XMLParser xmlparser = new XMLParser(s, logger);
            xmlparser.setTransientData(s2, s1);
            boolean flag1 = xmlparser.parse();
            if(Logger.DBG)
            {
                elapsedtime.setEndTime();
                logger.logMessage("Done parsing. Ok? " + flag1 + ". " + elapsedtime);
            }
            if(!flag1)
            {
                FileNode filenode1 = null;
                return filenode1;
            }
            if(Logger.DBG)
            {
                elapsedtime.setStartTime();
                if(statusDlg != null)
                    statusDlg.setDDSFileName(xmlparser.getFileName());
                if(statusDlg != null)
                    statusDlg.populating();
            }
            filenode = xmlparser.populate();
            if(Logger.DBG)
            {
                elapsedtime.setEndTime();
                logger.logMessage("Done populating. " + elapsedtime);
                elapsedtime1.setEndTime();
                logger.logMessage("Entire parse and populate done. " + elapsedtime1);
            }
            if(filenode != null)
                logger.logMessage("Something went wrong: parser returned null for the filenode");
        }
        finally
        {
            if(Logger.DBG && statusDlg != null)
                statusDlg.done();
            if(flag)
                logger.close();
        }
        return filenode;
    }

    public static String removeBlanks(String s)
    {
        boolean flag = false;
        StringBuffer stringbuffer = new StringBuffer(s);
        do
        {
            int i = stringbuffer.toString().indexOf(" ");
            if(i >= 0)
                stringbuffer = stringbuffer.replace(i, i + 1, "_0020");
            else
                return stringbuffer.toString();
        } while(true);
    }

    public static String restoreBlanks(String s)
    {
        boolean flag = false;
        StringBuffer stringbuffer = new StringBuffer(s);
        do
        {
            int i = stringbuffer.toString().indexOf("_0020");
            if(i >= 0)
                stringbuffer = stringbuffer.replace(i, i + 5, " ");
            else
                return stringbuffer.toString();
        } while(true);
    }

    public static String getWDTInstallDir()
    {
        return wdtRootDir;
    }

    static final String copyRight = new String("(C) Copyright IBM Corporation 1999-2003, all rights reserved.");
    private static WebFaceStatusDialog statusDlg = null;
    private String xmlFile;
    private boolean ok;
    private static String wfDirectory;
    private static String tmpDirectory;
    private static String wdtRootDir;
    private static ICustomTagExtensions _cte;

}
