// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.xml;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

// Referenced classes of package com.ibm.etools.iseries.webfacing.xml:
//            XElement, XDocumentStore, XMLException

public class XDocument extends XElement
{

    public XDocument()
    {
        super(null, "root");
    }

    public String getPersistentString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        if(super._vectorChildEntries != null)
        {
            for(int i = 0; i < super._vectorChildEntries.size(); i++)
                ((XElement)super._vectorChildEntries.elementAt(i)).printPersistentElementString(stringbuffer, 0);

        }
        return stringbuffer.toString();
    }

    public boolean load(URL url)
        throws XMLException
    {
        boolean flag = false;
        if(url != null)
        {
            XDocumentStore xdocumentstore = new XDocumentStore();
            flag = xdocumentstore.load(this, url);
        }
        return flag;
    }

    public boolean load(InputStream inputstream, boolean flag)
        throws XMLException
    {
        boolean flag1 = false;
        if(inputstream != null)
        {
            XDocumentStore xdocumentstore = new XDocumentStore();
            flag1 = xdocumentstore.load(this, inputstream, flag);
        }
        return flag1;
    }

    public boolean load(String s, boolean flag)
        throws XMLException, MalformedURLException
    {
        boolean flag1 = false;
        File file = new File(s);
        if(s != null)
        {
            XDocumentStore xdocumentstore = new XDocumentStore();
            flag1 = xdocumentstore.load(this, file, flag);
        }
        return flag1;
    }

    public void save(URL url)
        throws XMLException
    {
        boolean flag = true;
        if(url != null)
        {
            XDocumentStore xdocumentstore = new XDocumentStore();
            xdocumentstore.save(this, url);
        }
    }

    public static final String copyright = "(c) Copyright IBM Corporation 2002. all rights reserved";
}
