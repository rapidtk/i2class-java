// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.dev;


// Referenced classes of package com.ibm.as400ad.webfacing.dev:
//            FilterCode

public class DefinitionFilter extends FilterCode
{

    public DefinitionFilter()
    {
        _packageFiltered = false;
        _packageFile = null;
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
        return _packageFile;
    }

    private boolean _packageFiltered;
    private String _packageFile;
}
