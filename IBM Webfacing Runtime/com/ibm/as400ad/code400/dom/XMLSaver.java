// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import java.io.*;
import java.util.Date;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            Logger, IConstants, ExportSettings, FileNode

public class XMLSaver
    implements IConstants
{

    public static String[] convertStringVectorToArray(Vector vector)
    {
        String as[] = new String[vector.size()];
        for(int i = 0; i < as.length; i++)
            as[i] = (String)vector.elementAt(i);

        return as;
    }

    protected void logSaveMsg(String s)
    {
        if(traceSaveRestore)
            logMessage("SAVING: " + s);
    }

    public XMLSaver(String s, Logger logger1)
    {
        traceSaveRestore = true;
        savedOk = true;
        saveDate = null;
        if(logger1 == null)
            logger1 = new Logger();
        logger = logger1;
        saveFileName = s;
        logger1.logMessage("XMLSaveRestore instantiated for file: " + s);
        saveFile = new File(saveFileName);
    }

    public XMLSaver(String s, String s1)
    {
        this(s, s1, new Logger());
    }

    public XMLSaver(String s, String s1, Logger logger1)
    {
        traceSaveRestore = true;
        savedOk = true;
        saveDate = null;
        logger = logger1;
        if(!s.endsWith(File.separator))
            s = s + File.separator;
        saveFileName = s + s1;
        logger1.logMessage("XMLSaveRestore instantiated for file " + s1);
        saveFile = new File(saveFileName);
    }

    public File getSaveFile()
    {
        return saveFile;
    }

    public static String getXMLDir()
    {
        return Logger.getWDTInstallDir() + "\\tmp\\xml";
    }

    public void launchCODEBrowse()
    {
        Runtime runtime = Runtime.getRuntime();
        Process process;
        try
        {
            process = runtime.exec("CODEBRWS \"" + saveFileName + "\"");
        }
        catch(Exception exception)
        {
            logException("Exception in launchCODEBrowse", exception);
        }
    }

    protected void logErrorMessage(String s)
    {
        logger.logErrorMessage(s);
    }

    protected void logException(String s, Exception exception)
    {
        logger.logException(s, exception, true);
    }

    protected void logMessage(String s)
    {
        logger.logMessage(s);
    }

    public boolean saveAsXML(FileNode filenode)
    {
        savedOk = true;
        fileNode = filenode;
        logMessage(" ");
        logMessage("XMLSaveRestore: Begin of saveAsXML to " + saveFileName);
        logMessage("----------------------------------------------------");
        try
        {
            FileOutputStream fileoutputstream = new FileOutputStream(saveFile);
            saveWriter = new PrintWriter(fileoutputstream);
            saveWriter.println("<dds version=\"1.0\" savedate=\"" + System.currentTimeMillis() + "\">");
            ExportSettings exportsettings = ExportSettings.getExportSettings();
            filenode.saveAsXML("  ", saveWriter);
            saveWriter.println("</dds>");
            saveWriter.flush();
            fileoutputstream.close();
            saveWriter.close();
        }
        catch(Exception exception)
        {
            logException("ERROR SAVING AS XML TO FILE " + saveFileName, exception);
            savedOk = false;
        }
        logMessage("----------------------------------------------------");
        logMessage("XMLSaveRestore: Done saveAsXML to " + saveFileName + "! Result = " + savedOk);
        logMessage(" ");
        return savedOk;
    }

    private boolean traceSaveRestore;
    private String saveFileName;
    private File saveFile;
    private PrintWriter saveWriter;
    private boolean savedOk;
    private static final String CURR_VERSION = "1.0";
    private FileNode fileNode;
    private Logger logger;
    private Date saveDate;
}
