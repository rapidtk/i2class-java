// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import com.ibm.as400ad.webfacing.convert.ICustomTagExtensions;
import com.ibm.etools.iseries.webfacing.convert.gen.tag.CustomTagExtensions;
import java.io.*;
import org.apache.xerces.parsers.AbstractDOMParser;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            FileNode, WebfaceInvoker, XMLParser

public final class ExportSettings
{

    public static void setJavaSourceFolderName(String s)
    {
        JAVA_SOURCE_FOLDER_NAME = s;
    }

    public static void setWebContentFolderName(String s)
    {
        WEB_CONTENT_FOLDER_NAME = s;
    }

    private ExportSettings(String s)
    {
        _beandirectory = null;
        _cmdkeynamesfile = null;
        _conversionrules = null;
        _tagcheck = false;
        _gentags = false;
        projectRootDirectory = null;
        _generatedimagesdirectory = null;
        _menunamesfile = null;
        _rulesfile = null;
        _ruletsfile = null;
        _tracedirectory = null;
        _xPort = 0L;
        _jsprootdirectory = null;
        _rootexportdirectory = null;
        srcEventFileName = null;
        _javadirectory = null;
        wfDir = null;
        _conversionfactory = "com.ibm.as400ad.webfacing.convert.ConversionFactory";
        _conversionjar = null;
        _sdkdirectory = "";
        projectRootDirectory = s;
    }

    public final String getBeanDirectoryWithPackage()
    {
        return _beandirectory + FileNode.getFile().getPackageDirName();
    }

    public final String getJavaDirectoryWithPackage()
    {
        return getJavaDirectory() + getPackageDirectory();
    }

    public final String getJspDirectoryWithPackage()
    {
        return getJspDirectory() + getPackageDirectory();
    }

    public static final ExportSettings getExportSettings()
    {
        return _es;
    }

    public static final void initializeExportSettings(String s)
    {
        if(!s.endsWith(File.separator))
            s = s.concat(File.separator);
        _es = new ExportSettings(s);
    }

    public static final void initializeExportRuntimeSettings(String s)
    {
        _es = new ExportSettings(s);
    }

    public final long getxPortNumber()
    {
        return _xPort;
    }

    public final String getBeanDirectory()
    {
        return _beandirectory;
    }

    public final String getCmdKeyNamesFile()
    {
        if(null == _cmdkeynamesfile)
        {
            _cmdkeynamesfile = projectRootDirectory.concat("config");
            _cmdkeynamesfile = _cmdkeynamesfile.concat(File.separator);
            _cmdkeynamesfile = _cmdkeynamesfile.concat("commandkey.keynames");
        }
        return _cmdkeynamesfile;
    }

    public final ICustomTagExtensions getCustomTagExtensions()
    {
        if(null == _cte)
            _cte = new CustomTagExtensions(null, null, null);
        return _cte;
    }

    public final void setCustomTagExtensions(ICustomTagExtensions icustomtagextensions)
    {
        _cte = icustomtagextensions;
    }

    public final String getRulesFile()
    {
        if(null == _rulesfile)
        {
            _rulesfile = projectRootDirectory.concat("config");
            _rulesfile = _rulesfile.concat(File.separator);
            _rulesfile = _rulesfile.concat("conversion.rules");
        }
        return _rulesfile;
    }

    public final String getRuntimeRulesFile()
    {
        if(null == _rulesfile)
            _rulesfile = "runtime.rules";
        return _rulesfile;
    }

    public final InputStream getRuntimeRulesFileStream()
    {
        return getClass().getClassLoader().getResourceAsStream("runtime.rules");
    }

    public final Document getRuntimeRulesDocument()
    {
        if(null == _conversionrules)
            try
            {
                DOMParser domparser = new DOMParser();
                InputSource inputsource = new InputSource();
                inputsource.setByteStream(getRuntimeRulesFileStream());
                domparser.parse(inputsource);
                _conversionrules = domparser.getDocument();
            }
            catch(Throwable throwable) { }
        return _conversionrules;
    }

    public final Document getRulesDocument()
    {
        if(null == _conversionrules)
            try
            {
                DOMParser domparser = new DOMParser();
                domparser.parse(getRulesFile());
                _conversionrules = domparser.getDocument();
            }
            catch(Throwable throwable) { }
        return _conversionrules;
    }

    public final boolean genTags()
    {
        if(!_tagcheck)
        {
            _gentags = false;
            try
            {
                Document document = getRulesDocument();
                NodeList nodelist = document.getElementsByTagName("CustomTags");
                if(nodelist.getLength() > 0)
                {
                    Element element = (Element)nodelist.item(0);
                    String s = element.getAttribute("Option");
                    if(null != s && s.equals("true"))
                        _gentags = true;
                }
            }
            catch(Throwable throwable) { }
            _tagcheck = true;
        }
        return _gentags;
    }

    public final String getGeneratedImagesDirectory()
    {
        if(null == _generatedimagesdirectory)
        {
            _generatedimagesdirectory = _rootexportdirectory + WEB_CONTENT_FOLDER_NAME + "/images/generated" + FILE_SEPARATOR;
            _generatedimagesdirectory = _generatedimagesdirectory.replace('/', FILE_SEPARATOR);
        }
        return _generatedimagesdirectory;
    }

    public final String getJavaDirectory()
    {
        return _javadirectory;
    }

    public final String getJspDirectory()
    {
        return getJspRootDirectory() + "RecordJSPs" + FILE_SEPARATOR;
    }

    public String getJspRootDirectory()
    {
        return _jsprootdirectory;
    }

    public final String getMenuNamesFile()
    {
        if(null == _menunamesfile)
        {
            _menunamesfile = _rootexportdirectory + "menu.menunames";
            _menunamesfile = _menunamesfile.replace('/', FILE_SEPARATOR);
        }
        return _menunamesfile;
    }

    public final String getPackageDirectory()
    {
        return FileNode.getFile().getPackageDirName() + FILE_SEPARATOR;
    }

    public final String getPackageName()
    {
        return FileNode.getFile().getPackageName();
    }

    public final String getRootExportDirectory()
    {
        return _rootexportdirectory;
    }

    public final String getSrcEventFileName()
    {
        return srcEventFileName;
    }

    public final String getTraceDirectory()
    {
        if(_tracedirectory != null)
        {
            return _tracedirectory;
        } else
        {
            _tracedirectory = WebfaceInvoker.getWDTInstallDir().concat("\\tmp\\");
            return _tracedirectory;
        }
    }

    public void makeDirs()
    {
        File file = new File(getRootExportDirectory());
        if(!file.exists())
            file.mkdirs();
        File file1 = new File(getJspRootDirectory());
        if(!file1.exists())
            file1.mkdirs();
        File file2 = new File(getJspDirectory());
        if(!file2.exists())
            file2.mkdirs();
        File file3 = new File(getJspDirectoryWithPackage());
        if(!file3.exists())
            file3.mkdirs();
        File file4 = new File(getBeanDirectory());
        if(!file4.exists())
            file4.mkdirs();
        File file5 = new File(getJavaDirectory());
        if(!file5.exists())
            file5.mkdirs();
        File file6 = new File(getJavaDirectoryWithPackage());
        if(!file6.exists())
            file6.mkdirs();
    }

    public void restoreFromXML(XMLParser xmlparser, Node node)
    {
        NodeList nodelist = node.getChildNodes();
        if(nodelist != null && nodelist.getLength() > 0)
        {
            String s = xmlparser.getTagAttrValue(nodelist, "wdt400rootdir", "value");
            String s1 = xmlparser.getTagAttrValue(nodelist, "exportrootdir", "value");
            String s2 = xmlparser.getTagAttrValue(nodelist, "portnum", "value");
            String s3 = xmlparser.getTagAttrValue(nodelist, "conversionfactory", "value");
            String s4 = xmlparser.getTagAttrValue(nodelist, "conversionjar", "value");
            String s5 = xmlparser.getTagAttrValue(nodelist, "sdkdir", "value");
            if(s2 != null)
                setxPortNumber(Long.parseLong(s2));
            if(s != null)
                setWFDirectory(s);
            if(s1 != null)
                setRootExportDirectory(s1);
            if(s3 != null)
                setConversionFactory(s3);
            if(s4 != null)
                setConversionJar(s4);
            if(null == s5)
                s5 = "";
            setSDKDirectory(s5);
            System.out.println("webfacingdir: " + getWFDirectory());
            System.out.println("exportrootdir: " + getRootExportDirectory());
            System.out.println("portnum: " + getxPortNumber());
            System.out.println("srceventfile: " + getSrcEventFileName());
            System.out.println("SDK Directory: " + getSDKDirectory());
        } else
        {
            System.out.println("Missing xml child tags for <transientdata>");
        }
    }

    private void setBeanDirectory(String s)
    {
        _beandirectory = s;
    }

    private void setJspRootDirectory(String s)
    {
        _jsprootdirectory = s;
    }

    public void setRootExportDirectory(String s)
    {
        if(!s.endsWith(File.separator))
            s = s + File.separator;
        _rootexportdirectory = s;
        setJspRootDirectory(s + WEB_CONTENT_FOLDER_NAME + File.separator);
        setBeanDirectory(s);
        setJavaDirectory(s + JAVA_SOURCE_FOLDER_NAME + File.separator);
        setSrcEventFileName(s + "codedsu.evt");
    }

    private void setSrcEventFileName(String s)
    {
        srcEventFileName = s;
    }

    private void setTraceDirectory(String s)
    {
        _tracedirectory = s;
    }

    public void setWFDirectory(String s)
    {
        wfDir = s;
        if(!wfDir.endsWith(File.separator))
            wfDir += File.separator;
    }

    public void setxPortNumber(long l)
    {
        _xPort = l;
    }

    public final String getWFDirectory()
    {
        return wfDir;
    }

    private void setJavaDirectory(String s)
    {
        _javadirectory = s;
    }

    public final String getConversionFactory()
    {
        return _conversionfactory;
    }

    public final String getConversionJar()
    {
        return _conversionjar;
    }

    private void setConversionFactory(String s)
    {
        if(s.length() > 3)
            _conversionfactory = s;
    }

    private void setConversionJar(String s)
    {
        if(s.length() > 3)
            _conversionjar = s;
    }

    public String getSDKDirectory()
    {
        return _sdkdirectory;
    }

    public void setSDKDirectory(String s)
    {
        _sdkdirectory = s;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999-2003, all rights reserved.");
    static final char FILE_SEPARATOR;
    private String _beandirectory;
    private String _cmdkeynamesfile;
    private ICustomTagExtensions _cte;
    private Document _conversionrules;
    private boolean _tagcheck;
    private boolean _gentags;
    private static ExportSettings _es = null;
    private String projectRootDirectory;
    private String _generatedimagesdirectory;
    private String _menunamesfile;
    private String _rulesfile;
    private String _ruletsfile;
    private String _tracedirectory;
    private long _xPort;
    private String _jsprootdirectory;
    private String _rootexportdirectory;
    protected static final String EQUALSQUOTE = "=\"";
    static final String GENERATED_IMAGES_WEB_DIR = "/images/generated";
    protected static final char QUOTE = 34;
    protected static final String QUOTEBLANK = "\" ";
    static final String RECORD_JSPS_WEB_DIR = "RecordJSPs";
    private String srcEventFileName;
    protected static final String XML_TAG_EXPORTSETTINGS = "transientdata";
    private static String JAVA_SOURCE_FOLDER_NAME = "Java Source";
    private static String WEB_CONTENT_FOLDER_NAME = "Web Content";
    private String _javadirectory;
    private String wfDir;
    private String _conversionfactory;
    private String _conversionjar;
    private String _sdkdirectory;

    static 
    {
        FILE_SEPARATOR = File.separatorChar;
    }
}
