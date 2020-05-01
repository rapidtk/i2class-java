// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen;

import com.ibm.as400ad.code400.dom.AnyNode;
import com.ibm.as400ad.webfacing.convert.*;
import java.io.IOException;
import java.util.*;

public class MultiWebResourceGenerator
    implements IMultiWebResourceGenerator
{

    public MultiWebResourceGenerator()
    {
        _generators = null;
        _generators = new Vector();
    }

    public MultiWebResourceGenerator(IConversionFactory iconversionfactory, IRecordLayout irecordlayout)
    {
        this();
        try
        {
            addGenerator(iconversionfactory.getJSPGenerator(irecordlayout));
            addGenerator(iconversionfactory.getXMLBeanGenerator(irecordlayout));
        }
        catch(Throwable throwable)
        {
            if(irecordlayout != null && irecordlayout.getRecordNode() != null)
                irecordlayout.getRecordNode().logEvent(8);
            ExportHandler.err(1, "error in MultiWebResourceGenerator(IConversionFactory,RecordLayout,ExportSettings,Hashtable,Vector) while generating " + irecordlayout.getRecordNode().getWebName() + " = " + throwable);
        }
    }

    public void addGenerator(IWebResourceGenerator iwebresourcegenerator)
    {
        _generators.add(iwebresourcegenerator);
    }

    public void generate()
        throws IOException
    {
        IWebResourceGenerator iwebresourcegenerator;
        for(Iterator iterator = _generators.iterator(); iterator.hasNext(); iwebresourcegenerator.generate())
            iwebresourcegenerator = (IWebResourceGenerator)iterator.next();

    }

    protected static final boolean GENERATE_JAVA_BEAN = false;
    protected static final boolean GENERATE_XML_BEAN = true;
    private Vector _generators;
}
