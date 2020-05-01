// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.common;

import com.ibm.as400ad.webfacing.runtime.core.WFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

// Referenced classes of package com.ibm.as400ad.webfacing.common:
//            BaseProperties, MappingProperties

public class UIMMappingProperties extends BaseProperties
{

    public UIMMappingProperties()
    {
    }

    public UIMMappingProperties(String s)
        throws IOException, FileNotFoundException
    {
        super(s);
    }

    public UIMMappingProperties(URL url)
        throws IOException, FileNotFoundException
    {
        super(url);
    }

    protected BaseProperties createNewProperties()
        throws IOException
    {
        return new MappingProperties();
    }

    public static UIMMappingProperties getMappingProperties()
        throws WFException
    {
        UIMMappingProperties uimmappingproperties = new UIMMappingProperties();
        uimmappingproperties.loadFromClassPath(FILE_NAME);
        return uimmappingproperties;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999=2003, all rights reserved.");
    private static String FILE_NAME = "UIMObjectMapping.properties";

}
