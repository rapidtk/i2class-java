// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.common;

import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.core.WFException;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

// Referenced classes of package com.ibm.as400ad.webfacing.common:
//            BaseProperties

public class MappingProperties extends BaseProperties
{

    public MappingProperties()
    {
    }

    public MappingProperties(String s)
        throws IOException, FileNotFoundException
    {
        super(s);
    }

    public MappingProperties(URL url)
        throws IOException, FileNotFoundException
    {
        super(url);
    }

    public void addFilenameToSourcenameMapping(String s, String s1)
    {
        setPropertyString(FILETOSOURCEMAPPING + s, s1);
    }

    public BaseProperties createNewProperties()
        throws IOException
    {
        return new MappingProperties();
    }

    public Enumeration filenames()
    {
        return enumerationByPrefix(FILETOSOURCEMAPPING);
    }

    public static MappingProperties getMappingProperties(String s)
    {
        MappingProperties mappingproperties = new MappingProperties();
        try
        {
            mappingproperties.loadFromClassPath(s);
            if(mappingproperties.isEmpty())
                mappingproperties = null;
        }
        catch(WFException wfexception)
        {
            if(s.equals(DSPFOBJECTMAPPING_FILENAME))
                WFSession.getTraceLogger().err(2, "Could not load " + s);
            mappingproperties = null;
        }
        return mappingproperties;
    }

    String mapFilenameToSourcename(String s)
    {
        return getPropertyString(FILETOSOURCEMAPPING + s);
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999=2003, all rights reserved.");
    private static String FILETOSOURCEMAPPING = "{F2SMapping}";
    public static String DSPFOBJECTMAPPING_FILENAME = "DSPFObjectMapping.properties";
    public static String INPUTCHARMAPPING_FILENAME = "inputCharMapping.properties";
    public static String OUTPUTCHARMAPPING_FILENAME = "outputCharMapping.properties";

}
