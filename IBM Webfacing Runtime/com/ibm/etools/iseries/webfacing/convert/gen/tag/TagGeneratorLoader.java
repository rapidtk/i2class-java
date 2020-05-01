// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.convert.gen.tag;

import com.ibm.as400ad.code400.dom.ExportSettings;
import com.ibm.as400ad.webfacing.convert.ICustomTagExtensions;
import com.ibm.etools.iseries.webfacing.convert.external.*;

public class TagGeneratorLoader
{

    private TagGeneratorLoader()
    {
        try
        {
            ExportSettings exportsettings = ExportSettings.getExportSettings();
            _cte = exportsettings.getCustomTagExtensions();
        }
        catch(Throwable throwable) { }
    }

    public static void resetTagGeneratorLoader()
    {
        _loader = null;
    }

    public static TagGeneratorLoader getTagGeneratorLoader()
    {
        if(_loader == null)
            _loader = new TagGeneratorLoader();
        return _loader;
    }

    public IFieldTagGenerator getFieldTagGenerator(String s)
    {
        IFieldTagGenerator ifieldtaggenerator = _cte.getFieldTagGenerator(s);
        return ifieldtaggenerator;
    }

    public IWSSubTagGenerator getWSSubTagGenerator(int i)
    {
        String s = String.valueOf(i);
        return getWSSubTagGenerator(s);
    }

    public IWSSubTagGenerator getWSSubTagGenerator(String s)
    {
        IWSSubTagGenerator iwssubtaggenerator = _cte.getWebSettingAttributeTagGenerator(s);
        return iwssubtaggenerator;
    }

    public IWSTagGenerator getWSTagGenerator(int i)
    {
        String s = String.valueOf(i);
        return getWSTagGenerator(s);
    }

    public IWSTagGenerator getWSTagGenerator(String s)
    {
        IWSTagGenerator iwstaggenerator = _cte.getWebSettingTagGenerator(s);
        return iwstaggenerator;
    }

    public boolean isWSSubTagDefined(int i)
    {
        boolean flag = false;
        if(null != getWSSubTagGenerator(i))
            flag = true;
        return flag;
    }

    public boolean isWSTagDefined(int i)
    {
        boolean flag = false;
        if(null != getWSTagGenerator(i))
            flag = true;
        return flag;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2000-2002 All rights reserved.");
    private ICustomTagExtensions _cte;
    private static TagGeneratorLoader _loader = null;

}
