// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.convert.gen.tag;

import com.ibm.as400ad.code400.dom.constants.IFieldType;
import com.ibm.as400ad.code400.dom.constants.IWebSettingType;
import com.ibm.as400ad.webfacing.convert.ICustomTagExtensions;
import com.ibm.etools.iseries.webfacing.convert.external.*;
import java.util.Hashtable;

public class CustomTagExtensions
    implements ICustomTagExtensions
{

    private void setFTmap(Hashtable hashtable)
    {
        _ftmap = hashtable;
        if(null == _ftmap)
            _ftmap = new Hashtable();
    }

    private void setWSmap(Hashtable hashtable)
    {
        _wsmap = hashtable;
        if(null == _wsmap)
            _wsmap = new Hashtable();
    }

    private void setSWSmap(Hashtable hashtable)
    {
        _swsmap = hashtable;
        if(null == _swsmap)
            _swsmap = new Hashtable();
    }

    public CustomTagExtensions()
    {
        setFTmap(null);
        setWSmap(null);
        setSWSmap(null);
    }

    public CustomTagExtensions(Hashtable hashtable, Hashtable hashtable1, Hashtable hashtable2)
    {
        setFTmap(hashtable);
        setWSmap(hashtable1);
        setSWSmap(hashtable2);
    }

    public IFieldTagGenerator getFieldTagGenerator(IFieldType ifieldtype)
    {
        String s = ifieldtype.toString();
        return getFieldTagGenerator(s);
    }

    public IFieldTagGenerator getFieldTagGenerator(String s)
    {
        return (IFieldTagGenerator)_ftmap.get(s);
    }

    public IWSTagGenerator getWebSettingTagGenerator(IWebSettingType iwebsettingtype)
    {
        String s = iwebsettingtype.toString();
        return getWebSettingTagGenerator(s);
    }

    public IWSTagGenerator getWebSettingTagGenerator(String s)
    {
        return (IWSTagGenerator)_wsmap.get(s);
    }

    public IWSSubTagGenerator getWebSettingAttributeTagGenerator(IWebSettingType iwebsettingtype)
    {
        String s = iwebsettingtype.toString();
        return getWebSettingAttributeTagGenerator(s);
    }

    public IWSSubTagGenerator getWebSettingAttributeTagGenerator(String s)
    {
        return (IWSSubTagGenerator)_swsmap.get(s);
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2000-2003 All rights reserved.");
    private Hashtable _ftmap;
    private Hashtable _wsmap;
    private Hashtable _swsmap;

}
