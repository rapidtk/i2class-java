// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.dev;

import org.w3c.dom.*;

// Referenced classes of package com.ibm.as400ad.webfacing.dev:
//            FilterCode

public class XMLDefinitionFilter extends FilterCode
{

    public XMLDefinitionFilter(Document document1)
    {
        _packageFiltered = false;
        document = null;
        _packageFile = null;
        _recordName = null;
        document = document1;
    }

    public Document fixPackage()
    {
        NodeList nodelist = document.getElementsByTagName("record");
        Element element = (Element)nodelist.item(0);
        if(element != null)
        {
            String s = getPackageFile();
            s = "package com.ibm.as400ad.webfacing.runtime.qsys." + s.substring(_packageFile.lastIndexOf(".") + 1).toLowerCase();
            if(null != element.getAttribute("package"))
                element.removeAttribute("package");
            element.setAttribute("package", s);
        }
        return document;
    }

    public String filterLine(String s)
    {
        if(!_packageFiltered && s.indexOf("package") >= 0)
        {
            _packageFiltered = true;
            _packageFile = s.substring(s.lastIndexOf(".") + 1, s.lastIndexOf(";")).toLowerCase();
            return "package com.ibm.as400ad.webfacing.runtime.qsys." + _packageFile + ";";
        } else
        {
            return s;
        }
    }

    protected String getPackageFile()
    {
        if(null != _packageFile);
        NodeList nodelist = document.getElementsByTagName("record");
        Element element = (Element)nodelist.item(0);
        if(element != null)
        {
            _packageFile = element.getAttribute("package");
            _packageFile = _packageFile.substring(_packageFile.lastIndexOf(".") + 1).toLowerCase();
        }
        return _packageFile;
    }

    protected String getRecordName()
    {
        if(null != _recordName);
        NodeList nodelist = document.getElementsByTagName("record");
        Element element = (Element)nodelist.item(0);
        if(element != null)
        {
            _recordName = element.getAttribute("name");
            _recordName = _recordName.substring(_recordName.lastIndexOf(".") + 1).toLowerCase();
        }
        return _recordName;
    }

    private boolean _packageFiltered;
    protected Document document;
    private String _packageFile;
    private String _recordName;
}
