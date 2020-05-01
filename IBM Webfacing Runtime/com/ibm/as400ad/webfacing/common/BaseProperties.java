// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.common;

import com.ibm.as400ad.webfacing.runtime.core.WFException;
import java.io.*;
import java.net.URL;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.common:
//            UTF8Properties, WebfacingConstants, WebFacingFileFilter

public abstract class BaseProperties extends UTF8Properties
{

    public BaseProperties()
    {
        this((String)null);
    }

    public BaseProperties(String s)
    {
        readonly = false;
        inputname = null;
        outputname = null;
        is = null;
        init(s);
    }

    public BaseProperties(URL url)
        throws FileNotFoundException, IOException
    {
        readonly = false;
        inputname = null;
        outputname = null;
        is = null;
        is = url.openStream();
        init(null);
        setReadOnly(true);
    }

    protected abstract BaseProperties createNewProperties()
        throws IOException;

    protected Enumeration enumerationByPrefix(String s)
    {
        Vector vector = new Vector();
        for(Enumeration enumeration = propertyNames(); enumeration.hasMoreElements();)
        {
            String s1 = (String)enumeration.nextElement();
            if(s1.startsWith(s))
            {
                s1 = s1.substring(s.length());
                vector.addElement(s1);
            }
        }

        return vector.elements();
    }

    public String getDescription()
    {
        String s = getPropertyString("{DESCRIPTION}");
        if(null == s)
            s = new String("No description");
        return s;
    }

    public static String getInstallDir()
    {
        return installDir;
    }

    public String getName()
    {
        return getPropertyString("{NAME}");
    }

    public static Vector getObjectFileNames(String s, WebFacingFileFilter webfacingfilefilter)
    {
        File file = new File(s);
        String as[] = file.list();
        Vector vector = new Vector();
        for(int j = 0; j < as.length; j++)
        {
            File file1 = new File(s + System.getProperty("file.separator") + as[j]);
            if(file1.isDirectory())
            {
                String as1[] = file1.list(webfacingfilefilter);
                for(int k = 0; k < as1.length; k++)
                {
                    String s1 = as1[k];
                    int i = s1.lastIndexOf('.');
                    if(i > 0 && i < s1.length() - 1)
                        s1 = s1.substring(0, i);
                    if(s1 != null)
                        vector.addElement(s1);
                }

            }
        }

        return vector;
    }

    public final String getPropertiesPackageName()
    {
        return "conf";
    }

    public String getPropertyString(String s)
    {
        return super.getProperty(s);
    }

    public String getPropertyString(String s, String s1)
    {
        return super.getProperty(s, s1);
    }

    public boolean getReadOnly()
    {
        return readonly;
    }

    private void init(String s)
    {
        try
        {
            outputname = s;
            if(null != s)
                is = new DataInputStream(new FileInputStream(s));
            if(null != is)
            {
                load(is);
                is.close();
            }
            if(isBuiltIn())
                setReadOnly(true);
        }
        catch(Throwable throwable)
        {
            String s1 = WebfacingConstants.replaceSubstring(_resmri.getString("WF0029"), "&1", s) + "(" + throwable.toString() + ")";
            throw new Error(s1);
        }
    }

    public boolean isBuiltIn()
    {
        return "{IBM}".equals(getPropertyString("{AUTHOR}"));
    }

    public boolean isBuiltInUseOnly()
    {
        return "{IBMUSE}".equals(getPropertyString("{AUTHOR}"));
    }

    protected void loadDirectFromClassPath(String s)
        throws WFException
    {
        try
        {
            ClassLoader classloader = getClass().getClassLoader();
            if(null == classloader)
                classloader = ClassLoader.getSystemClassLoader();
            InputStream inputstream = classloader.getResourceAsStream(s);
            if(null != inputstream)
            {
                load(inputstream);
                inputstream.close();
                init(null);
            } else
            {
                throw new Error("");
            }
        }
        catch(Throwable throwable)
        {
            String s1 = WebfacingConstants.replaceSubstring(_resmri.getString("WF0029"), "&1", s);
            if(throwable.toString().length() > 5)
                s1 = s1 + "(" + throwable.toString() + ")";
            throw new WFException(s1);
        }
    }

    protected void loadFromClassPath(String s)
        throws WFException
    {
        String s1 = getPropertiesPackageName() + System.getProperty("file.separator", "\\") + s;
        loadDirectFromClassPath(s1);
    }

    public Enumeration propertyNames()
    {
        return super.propertyNames();
    }

    public void setDescription(String s)
    {
        setPropertyString("{DESCRIPTION}", s);
    }

    public static void setInstallDir(String s)
    {
        installDir = s;
    }

    public void setName(String s)
    {
        setPropertyString("{NAME}", s);
    }

    public String setProp(String s, String s1)
    {
        return (String)super.put(s, s1);
    }

    public String setPropertyString(String s, String s1)
    {
        return (String)super.put(s, s1);
    }

    protected void setReadOnly(boolean flag)
    {
        readonly = flag;
    }

    public void store()
        throws IOException
    {
        if(null != outputname && !getReadOnly())
        {
            String s = getDescription();
            if(null == s)
                s = outputname;
            setPropertyString("{AUTHOR}", "{USER}");
            BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(new FileOutputStream(outputname));
            store(bufferedoutputstream, s);
            bufferedoutputstream.flush();
            bufferedoutputstream.close();
        } else
        {
            throw new IOException(WebfacingConstants.replaceSubstring(_resmri.getString("WF0070"), "&1", outputname));
        }
    }

    public void storeAs(String s)
        throws IOException
    {
        if(null != s && (!getReadOnly() || getReadOnly() && !s.equals(outputname)))
        {
            setReadOnly(false);
            outputname = s;
            store();
        }
    }

    public BaseProperties storeAsNew(String s)
        throws IOException
    {
        BaseProperties baseproperties = createNewProperties();
        String s1;
        for(Enumeration enumeration = propertyNames(); enumeration.hasMoreElements(); baseproperties.setPropertyString(s1, getPropertyString(s1)))
            s1 = (String)enumeration.nextElement();

        baseproperties.storeAs(s);
        return baseproperties;
    }

    public String toString()
    {
        return getDescription();
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999=2003, all rights reserved.");
    private static ResourceBundle _resmri;
    private boolean readonly;
    private String inputname;
    protected String outputname;
    private transient InputStream is;
    private static final String BUILTIN = "{IBM}";
    private static final String AUTHOR = "{AUTHOR}";
    private static final String DESCRIPTION = "{DESCRIPTION}";
    private static final String NAME = "{NAME}";
    private static String installDir = null;
    private static final String BUILTIN_USE = "{IBMUSE}";
    private static final String PROPERTIES_PACKAGE_NAME = "conf";
    private static final String FILE_NAME = "unknown";

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
