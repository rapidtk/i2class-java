// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.controller;

import com.ibm.as400ad.webfacing.common.*;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingLevelCheckException;
import com.ibm.as400ad.webfacing.runtime.help.HelpDefinition;
import com.ibm.as400ad.webfacing.runtime.model.*;
import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;
import com.ibm.as400ad.webfacing.runtime.view.RecordFeedbackBean;
import com.ibm.as400ad.webfacing.runtime.view.RecordViewBean;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordFeedbackDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordViewDefinition;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.servlet.ServletContext;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.controller:
//            CachingRecordDefinitionFetcher, ILibraryFile, IRecordDefinitionFetcher, IReadOutputBuffer, 
//            ICacheable

public class RecordBeanFactory
{

    public static RecordBeanFactory getRecordBeanFactory(ServletContext servletcontext)
    {
        RecordBeanFactory recordbeanfactory = null;
        if(null != servletcontext)
            recordbeanfactory = (RecordBeanFactory)servletcontext.getAttribute("WFRecordBeanFactory");
        if(null == recordbeanfactory)
        {
            recordbeanfactory = new RecordBeanFactory(servletcontext);
            if(null != servletcontext)
                servletcontext.setAttribute("WFRecordBeanFactory", recordbeanfactory);
            else
                System.err.println("ServletContext is null. RecordBeanFactory created but not saved");
        }
        return recordbeanfactory;
    }

    private RecordBeanFactory(ServletContext servletcontext)
    {
        _resmri = null;
        _objectToSourceMap = null;
        _myClassLoader = null;
        _definitionFetcher = null;
        _servletContext = null;
        _servletContext = servletcontext;
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
        _objectToSourceMap = MappingProperties.getMappingProperties(MappingProperties.DSPFOBJECTMAPPING_FILENAME);
        _myClassLoader = getClass().getClassLoader();
        if(null == _myClassLoader)
            _myClassLoader = ClassLoader.getSystemClassLoader();
        WFAppProperties wfappproperties = WFAppProperties.getWFAppProperties(_servletContext);
        int i = wfappproperties.getBeanCacheSize();
        _definitionFetcher = new CachingRecordDefinitionFetcher(i);
    }

    private RecordBeanFactory()
    {
        _resmri = null;
        _objectToSourceMap = null;
        _myClassLoader = null;
        _definitionFetcher = null;
        _servletContext = null;
    }

    public RecordDataBean createRecordDataBean(IReadOutputBuffer ireadoutputbuffer)
        throws IOException, WebfacingLevelCheckException, WebfacingInternalException
    {
        IRecordDataDefinition irecorddatadefinition = loadRecordDataDefinition(ireadoutputbuffer);
        return irecorddatadefinition.createRecordDataBean(ireadoutputbuffer, this);
    }

    public RecordDataBean createRecordDataBean(HelpDefinition helpdefinition)
        throws IOException, WebfacingLevelCheckException, WebfacingInternalException
    {
        IRecordDataDefinition irecorddatadefinition = loadRecordDataDefinition(helpdefinition);
        return irecorddatadefinition.createRecordDataBean();
    }

    public RecordFeedbackBean createRecordFeedbackBean(IRecordData irecorddata)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        IRecordFeedbackDefinition irecordfeedbackdefinition = loadRecordFeedbackDefinition(irecorddata);
        return irecordfeedbackdefinition.createFeedbackBean(irecorddata);
    }

    public RecordViewBean createRecordViewBean(IRecordData irecorddata)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        IRecordViewDefinition irecordviewdefinition = loadRecordViewDefinition(irecorddata);
        return irecordviewdefinition.createViewBean(createRecordFeedbackBean(irecorddata));
    }

    private String getPackageName(ILibraryFile ilibraryfile, String s)
    {
        String s1 = null;
        String s2 = null;
        SystemRecord systemrecord = SystemRecords.getSystemRecord(ilibraryfile, s);
        if(systemrecord != null)
            s1 = systemrecord.getPackage();
        else
            s1 = getQualifiedPath(ilibraryfile, s);
        s2 = s1.replace('/', '.');
        return s2;
    }

    private String getQualifiedPath(ILibraryFile ilibraryfile, String s)
    {
        String s1 = ilibraryfile.getLibraryName().trim();
        String s2 = ilibraryfile.getFileName().trim();
        String s3 = "." + WebfacingConstants.replaceSpecialCharacters(s);
        if(_objectToSourceMap != null)
        {
            String s4 = _objectToSourceMap.getProperty(s1 + "/" + s2);
            if(s4 != null)
            {
                String s6 = _definitionFetcher.checkQualifiedPath(s4, s3);
                if(s6 != null)
                    return s6;
            }
            try
            {
                String s5 = _objectToSourceMap.getProperty("&LIB/" + s2);
                if(s5 != null)
                {
                    s5 = WebfacingConstants.replaceSubstring(s5, "&LIB", s1);
                    String s7 = _definitionFetcher.checkQualifiedPath(s5, s3);
                    if(s7 != null)
                        return s7;
                }
                s5 = _objectToSourceMap.getProperty(s1 + "/" + "&DSPF");
                if(s5 != null)
                {
                    s5 = WebfacingConstants.replaceSubstring(s5, "&DSPF", s2);
                    String s8 = _definitionFetcher.checkQualifiedPath(s5, s3);
                    if(s8 != null)
                        return s8;
                }
                s5 = _objectToSourceMap.getProperty("&LIB/&DSPF");
                if(s5 != null)
                {
                    s5 = WebfacingConstants.replaceSubstring(s5, "&LIB", s1);
                    s5 = WebfacingConstants.replaceSubstring(s5, "&DSPF", s2);
                    String s9 = _definitionFetcher.checkQualifiedPath(s5, s3);
                    if(s9 != null)
                        return s9;
                }
            }
            catch(IndexOutOfBoundsException indexoutofboundsexception) { }
        }
        return WebfacingConstants.replaceSpecialCharacters(s1) + "/" + "QDDSSRC" + "/" + WebfacingConstants.replaceSpecialCharacters(s2);
    }

    public IRecordDataDefinition loadRecordDataDefinition(String s)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        return _definitionFetcher.requestDataDefinition(s);
    }

    private IRecordDataDefinition loadRecordDataDefinition(IReadOutputBuffer ireadoutputbuffer)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        IDSPFObject idspfobject = ireadoutputbuffer.getDSPFObject();
        String s = ireadoutputbuffer.getRecordFormatName();
        String s1 = "UNMAPPED RECORD NAME";
        try
        {
            s = getPackageName(idspfobject, s) + "." + WebfacingConstants.replaceSpecialCharacters(s);
            String s2 = s + "Data";
        }
        catch(NullPointerException nullpointerexception)
        {
            throw new WebfacingInternalException(_resmri.getString("WF0023"));
        }
        IRecordDataDefinition irecorddatadefinition = loadRecordDataDefinition(s);
        irecorddatadefinition.setRecordClassName(s);
        return irecorddatadefinition;
    }

    private IRecordDataDefinition loadRecordDataDefinition(HelpDefinition helpdefinition)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        String s = helpdefinition.getDefinition();
        String s1 = "UNMAPPED RECORD NAME";
        try
        {
            s = getPackageName(helpdefinition, s) + "." + WebfacingConstants.replaceSpecialCharacters(s);
            String s2 = s + "Data";
        }
        catch(NullPointerException nullpointerexception)
        {
            throw new WebfacingInternalException(_resmri.getString("WF0023"));
        }
        IRecordDataDefinition irecorddatadefinition = loadRecordDataDefinition(s);
        irecorddatadefinition.setRecordClassName(s);
        return irecorddatadefinition;
    }

    private IRecordFeedbackDefinition loadRecordFeedbackDefinition(IRecordData irecorddata)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        IRecordFeedbackDefinition irecordfeedbackdefinition = irecorddata.getRecordDataDefinition().getFeedbackDefinition();
        if(null == irecordfeedbackdefinition)
        {
            String s = irecorddata.getRecordDataDefinition().getRecordClassName();
            irecordfeedbackdefinition = _definitionFetcher.requestFeedbackDefinition(s);
        }
        return irecordfeedbackdefinition;
    }

    private IRecordViewDefinition loadRecordViewDefinition(IRecordData irecorddata)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        IRecordViewDefinition irecordviewdefinition = irecorddata.getRecordDataDefinition().getViewDefinition();
        if(null == irecordviewdefinition)
        {
            String s = irecorddata.getRecordDataDefinition().getRecordClassName();
            irecordviewdefinition = _definitionFetcher.requestViewDefinition(s);
        }
        return irecordviewdefinition;
    }

    public ICacheable getCacheHead()
    {
        ICacheable icacheable = null;
        if(null != _definitionFetcher)
            icacheable = _definitionFetcher.getCacheHead();
        return icacheable;
    }

    public void setCacheLimit(int i)
    {
        if(null != _definitionFetcher)
            _definitionFetcher.setCacheLimit(i);
    }

    public int getCacheLimit()
    {
        int i = 0;
        if(null != _definitionFetcher)
            i = _definitionFetcher.getCacheLimit();
        return i;
    }

    public static final String copyRight = "(C) Copyright IBM Corporation 1999-2003 all rights reserved";
    private ResourceBundle _resmri;
    protected static final String MODEL_SUFFIX = "Data";
    protected static final String VIEW_SUFFIX = "View";
    protected static final String CONTROLLER_SUFFIX = "Control";
    protected static final String FEEDBACK_SUFFIX = "Feedback";
    private MappingProperties _objectToSourceMap;
    private ClassLoader _myClassLoader;
    private IRecordDefinitionFetcher _definitionFetcher;
    private static final String FILENAME_WILDCARD = "&DSPF";
    private static final String LIBRARY_WILDCARD = "&LIB";
    public static final String CONTEXT_KEY = "WFRecordBeanFactory";
    private ServletContext _servletContext;
}
