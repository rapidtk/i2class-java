// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert;

import com.ibm.as400ad.code400.dom.*;
import java.io.File;
import java.io.IOException;

// Referenced classes of package com.ibm.as400ad.webfacing.convert:
//            ConversionLogger, IWebFaceConverter, ExportHandler, Util, 
//            IStatusCallback, IConversionFactory, IMultiWebResourceGenerator

public class WebFaceConverter
    implements IWebFaceConverter
{

    public WebFaceConverter()
    {
        es = ExportSettings.getExportSettings();
    }

    public void convert(FileNode filenode, IConversionFactory iconversionfactory)
    {
        try
        {
            RecordNodeEnumeration recordnodeenumeration = filenode.getSelectedRecords();
            convert(recordnodeenumeration, iconversionfactory);
        }
        catch(Throwable throwable)
        {
            if(filenode != null)
                filenode.logEvent(8);
            String s = "Error in WebFaceConverter.convert(FileNode, icf)";
            ExportHandler.err(1, s + " : " + throwable);
            ExportHandler.err(1, throwable);
            Util.logThrowableMessage(s, throwable, false);
        }
    }

    public void convert(RecordNodeEnumeration recordnodeenumeration, IConversionFactory iconversionfactory)
    {
        if(null != recordnodeenumeration)
        {
            RecordNode recordnode = null;
            FileNode filenode = null;
            if(ExportHandler.getStatusCallback() != null)
                ExportHandler.getStatusCallback().startingWebFacing(recordnodeenumeration.getNumberOfRecords());
            try
            {
                filenode = FileNode.getFile();
                String s = filenode.getName();
                ConversionLogger conversionlogger = new ConversionLogger();
                conversionlogger.checkKeywords(filenode);
                String s2 = Integer.toString(recordnodeenumeration.getNumberOfRecords());
                int i = 0;
                createWebResourceDirectories();
                while(recordnodeenumeration.hasMoreElements()) 
                {
                    recordnode = recordnodeenumeration.nextRecord();
                    i++;
                    recordnode.logEvent(3);
                    conversionlogger.checkKeywords(recordnode);
                    if(null != recordnode && !recordnode.isSFL() && !recordnode.isSFLMSG())
                    {
                        String s3 = recordnode.getName();
                        ExportHandler.milestoneTimerElapseTime("Convert " + s3);
                        if(ExportHandler.getStatusCallback() != null)
                            ExportHandler.getStatusCallback().processingRecord(s3);
                        generateWebResources(iconversionfactory, recordnode);
                        ExportHandler.milestoneTimerElapseTime("Finished " + s3);
                    }
                }
            }
            catch(Throwable throwable)
            {
                if(recordnode != null)
                    recordnode.logEvent(8);
                else
                if(filenode != null)
                    filenode.logEvent(8);
                String s1 = "Error in WebFaceConverter.convert(RecordNodeEnumeration)";
                ExportHandler.err(1, s1 + " = " + throwable);
                ExportHandler.err(1, throwable);
                Util.logThrowableMessage(s1, throwable, false);
                if(ExportHandler.getStatusCallback() != null)
                    ExportHandler.getStatusCallback().fatalError(throwable.getClass().getName() + ": " + throwable.getMessage());
            }
            if(ExportHandler.getStatusCallback() != null)
                ExportHandler.getStatusCallback().done();
        }
    }

    private void createWebResourceDirectories()
    {
        makeDir(es.getJspDirectoryWithPackage());
        makeDir(es.getJavaDirectoryWithPackage());
    }

    private void generateWebResources(IConversionFactory iconversionfactory, RecordNode recordnode)
        throws IOException
    {
        try
        {
            IRecordLayout irecordlayout = iconversionfactory.getRecordLayout(recordnode);
            if(!ConversionLogger.isAbortRecordConversion())
            {
                IMultiWebResourceGenerator imultiwebresourcegenerator = iconversionfactory.getMultiWebResourceGenerator(irecordlayout);
                imultiwebresourcegenerator.generate();
            }
        }
        catch(IOException ioexception)
        {
            if(recordnode != null)
                recordnode.logEvent(8);
            String s = "Error in WebFaceConverter.generateWebResources() for record " + recordnode.getName();
            ExportHandler.err(1, s + " = " + ioexception);
            ExportHandler.err(1, ioexception);
            Util.logThrowableMessage(s, ioexception, false);
            throw ioexception;
        }
    }

    public boolean makeDir(String s)
    {
        boolean flag = true;
        File file = new File(s);
        Util.showDebugMessage("inside makeDir for " + s);
        if(!file.exists())
        {
            flag = file.mkdirs();
            Util.showDebugMessage("successfully made directory " + s);
        } else
        {
            Util.showDebugMessage("ERROR: make directory for " + s + "failed!");
        }
        return flag;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000, 2001");
    private ExportSettings es;

}
