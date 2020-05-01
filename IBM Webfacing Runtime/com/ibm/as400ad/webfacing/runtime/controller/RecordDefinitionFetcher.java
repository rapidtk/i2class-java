// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.controller;

import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingLevelCheckException;
import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordFeedbackDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordViewDefinition;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import com.ibm.etools.iseries.webfacing.xml.XDocument;
import com.ibm.etools.iseries.webfacing.xml.XMLParser;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import org.xml.sax.SAXException;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.controller:
//            BeanDefXMLReader, IRecordDefinitionFetcher, XMLDefinitionLoader, ICacheable, 
//            WFSession

public class RecordDefinitionFetcher
    implements IRecordDefinitionFetcher
{

    public RecordDefinitionFetcher()
    {
        _myClassLoader = null;
        beanReader = null;
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
        useSAX = true;
        _myClassLoader = getClass().getClassLoader();
        if(null == _myClassLoader)
            _myClassLoader = ClassLoader.getSystemClassLoader();
        useSAX = false;
    }

    public IRecordDataDefinition requestDataDefinition(String s)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        IRecordDataDefinition irecorddatadefinition = null;
        irecorddatadefinition = loadXMLDefinition(s);
        if(null == irecorddatadefinition)
            irecorddatadefinition = (IRecordDataDefinition)loadJavaDefinition(s + "Data");
        return irecorddatadefinition;
    }

    public IRecordViewDefinition requestViewDefinition(String s)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        IRecordViewDefinition irecordviewdefinition = null;
        irecordviewdefinition = (IRecordViewDefinition)loadJavaDefinition(s + "View");
        return irecordviewdefinition;
    }

    public IRecordFeedbackDefinition requestFeedbackDefinition(String s)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        IRecordFeedbackDefinition irecordfeedbackdefinition = null;
        irecordfeedbackdefinition = (IRecordFeedbackDefinition)loadJavaDefinition(s + "Feedback");
        return irecordfeedbackdefinition;
    }

    public String checkQualifiedPath(String s, String s1)
    {
        for(Iterator iterator = getPathTokens(s); iterator.hasNext();)
        {
            String s2 = (String)iterator.next();
            String s3 = s2 + s1;
            s3 = s3.replace('/', '.');
            try
            {
                String s4 = s3.replace('.', '/');
                InputStream inputstream = _myClassLoader.getResourceAsStream(s4 + ".xml");
                if(inputstream == null)
                {
                    String s5 = s3.replace('.', '\\');
                    inputstream = _myClassLoader.getResourceAsStream(s5 + ".xml");
                }
                if(inputstream != null)
                {
                    inputstream.close();
                    return s2;
                } else
                {
                    Class class1 = Class.forName(s3 + "Data");
                    return s2;
                }
            }
            catch(Throwable throwable)
            {
                s2 = null;
            }
        }

        return null;
    }

    protected static Iterator getPathTokens(String s)
    {
        Vector vector = new Vector();
        if(s.trim().length() == 0)
            return vector.iterator();
        for(int i = WebfacingConstants.indexOfUnquotedChar(s, ';'); i > -1; i = WebfacingConstants.indexOfUnquotedChar(s, ';'))
        {
            String s1 = s.substring(0, i);
            vector.addElement(processSinglePath(s1));
            s = s.substring(i + 1);
        }

        String s2 = s;
        vector.addElement(processSinglePath(s2));
        return vector.iterator();
    }

    protected static String processSinglePath(String s)
    {
        try
        {
            s = s.trim();
            if(s.startsWith("/"))
                s = s.substring(1);
            s = WebfacingConstants.getCharacterReplacedPackageName(s);
            s = WebfacingConstants.replaceSubstring(s, ".", "/");
        }
        catch(IllegalArgumentException illegalargumentexception)
        {
            s = "";
        }
        catch(IndexOutOfBoundsException indexoutofboundsexception)
        {
            s = "";
        }
        return s;
    }

    public IRecordDataDefinition SAXLoadXMLDefinition(String s)
        throws SAXException
    {
        IRecordDataDefinition irecorddatadefinition = null;
        try
        {
            if(null == beanReader)
                beanReader = new BeanDefXMLReader();
            irecorddatadefinition = beanReader.getBeanDef(s);
        }
        catch(SAXException saxexception)
        {
            throw saxexception;
        }
        return irecorddatadefinition;
    }

    public IRecordDataDefinition DOMLoadXMLDefinition(String s)
    {
        IRecordDataDefinition irecorddatadefinition = null;
        if(null != s)
        {
            XDocument xdocument = loadXMLDocument(s);
            if(xdocument != null)
            {
                irecorddatadefinition = XMLDefinitionLoader.loadDataDefinition(xdocument);
                if(irecorddatadefinition != null)
                {
                    irecorddatadefinition.setViewDefinition(XMLDefinitionLoader.loadViewDefinition(xdocument));
                    irecorddatadefinition.setFeedbackDefinition(XMLDefinitionLoader.loadFeedbackDefinition(xdocument));
                }
            }
        }
        return irecorddatadefinition;
    }

    protected IRecordDataDefinition loadXMLDefinition(String s)
    {
        IRecordDataDefinition irecorddatadefinition = null;
        if(useSAX)
            try
            {
                irecorddatadefinition = SAXLoadXMLDefinition(s);
            }
            catch(Throwable throwable)
            {
                useSAX = false;
                irecorddatadefinition = DOMLoadXMLDefinition(s);
            }
        else
            irecorddatadefinition = DOMLoadXMLDefinition(s);
        return irecorddatadefinition;
    }

    protected XDocument loadXMLDocument(String s)
    {
        Object obj = null;
        Object obj1 = null;
        try
        {
            String s1 = s.replace('.', '/') + ".xml";
            InputStream inputstream = _myClassLoader.getResourceAsStream(s1);
            if(inputstream == null)
            {
                String s2 = s.replace('.', '\\') + ".xml";
                inputstream = _myClassLoader.getResourceAsStream(s2);
            }
            if(null != inputstream)
            {
                XMLParser xmlparser = new XMLParser(inputstream);
                xmlparser.parse();
                XDocument xdocument = xmlparser.getDocument();
                xmlparser = null;
                inputstream.close();
                if(null != xdocument)
                    return xdocument;
            }
        }
        catch(Throwable throwable) { }
        return null;
    }

    protected Object loadJavaDefinition(String s)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        Class class1 = loadClass(s);
        Constructor constructor;
        try
        {
            Class aclass[] = new Class[0];
            constructor = class1.getConstructor(aclass);
        }
        catch(NoSuchMethodException nosuchmethodexception)
        {
            WFSession.getTraceLogger().err(2, nosuchmethodexception);
            throw new WebfacingLevelCheckException(WebfacingConstants.replaceSubstring(_resmri.getString("WF0019"), "&1", s));
        }
        catch(SecurityException securityexception)
        {
            WFSession.getTraceLogger().err(2, securityexception);
            PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(_resmri.getString("WF0020"));
            paddedstringbuffer.replaceSubstring("&1", s);
            paddedstringbuffer.replaceSubstring("&2", securityexception.toString());
            throw new WebfacingLevelCheckException(paddedstringbuffer.toString());
        }
        try
        {
            Object aobj[] = new Object[0];
            return constructor.newInstance(aobj);
        }
        catch(InstantiationException instantiationexception)
        {
            WFSession.getTraceLogger().err(2, instantiationexception);
            throw new WebfacingLevelCheckException(WebfacingConstants.replaceSubstring(_resmri.getString("WF0021"), "&1", s));
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            WFSession.getTraceLogger().err(2, illegalaccessexception);
            PaddedStringBuffer paddedstringbuffer1 = new PaddedStringBuffer(_resmri.getString("WF0022"));
            paddedstringbuffer1.replaceSubstring("&1", s);
            paddedstringbuffer1.replaceSubstring("&2", illegalaccessexception.toString());
            throw new WebfacingLevelCheckException(paddedstringbuffer1.toString());
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            if(invocationtargetexception.getTargetException() instanceof WebfacingLevelCheckException)
                throw (WebfacingLevelCheckException)invocationtargetexception.getTargetException();
            if(invocationtargetexception.getTargetException() instanceof WebfacingInternalException)
                throw (WebfacingInternalException)invocationtargetexception.getTargetException();
            else
                throw new WebfacingInternalException(invocationtargetexception.getTargetException().getMessage());
        }
    }

    private Class loadClass(String s)
        throws WebfacingLevelCheckException
    {
        try
        {
            Class class1 = Class.forName(s);
            return class1;
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            WFSession.getTraceLogger().err(2, classnotfoundexception);
            throw new WebfacingLevelCheckException(WebfacingConstants.replaceSubstring(_resmri.getString("WF0015"), "&1", s));
        }
        catch(ExceptionInInitializerError exceptionininitializererror)
        {
            WFSession.getTraceLogger().err(2, exceptionininitializererror);
            throw new WebfacingLevelCheckException(WebfacingConstants.replaceSubstring(_resmri.getString("WF0016"), "&1", s));
        }
        catch(LinkageError linkageerror)
        {
            WFSession.getTraceLogger().err(2, linkageerror);
            throw new WebfacingLevelCheckException(WebfacingConstants.replaceSubstring(_resmri.getString("WF0017"), "&1", s));
        }
        catch(InternalError internalerror)
        {
            WFSession.getTraceLogger().err(2, internalerror);
        }
        throw new WebfacingLevelCheckException(WebfacingConstants.replaceSubstring(_resmri.getString("WF0018"), "&1", s));
    }

    public void clearCache()
    {
    }

    public ICacheable getCacheHead()
    {
        return null;
    }

    public void setCacheLimit(int i)
    {
    }

    public int getCacheLimit()
    {
        return 0;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2003, all rights reserved.");
    protected ClassLoader _myClassLoader;
    private BeanDefXMLReader beanReader;
    private ResourceBundle _resmri;
    private boolean useSAX;

}
