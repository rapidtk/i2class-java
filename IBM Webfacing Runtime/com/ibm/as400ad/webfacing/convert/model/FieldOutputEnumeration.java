// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.webfacing.convert.IFieldOutput;
import com.ibm.as400ad.webfacing.convert.Util;
import java.util.*;

public class FieldOutputEnumeration
    implements Enumeration
{

    public FieldOutputEnumeration(List list)
    {
        _v = null;
        _e = null;
        _e = null;
        _v = list;
        if(null != _v)
            _e = list.iterator();
    }

    public boolean hasMoreElements()
    {
        boolean flag = false;
        if(null != _e)
            flag = _e.hasNext();
        return flag;
    }

    public Object nextElement()
    {
        Object obj = null;
        if(hasMoreElements() && null != _e)
            obj = _e.next();
        return obj;
    }

    public IFieldOutput nextFieldOutput()
    {
        IFieldOutput ifieldoutput = null;
        try
        {
            ifieldoutput = (IFieldOutput)nextElement();
        }
        catch(Throwable throwable)
        {
            Util.logThrowableMessage("error in DefaultRecordBeanGenerator.generateRecordBean()", throwable, false);
            ifieldoutput = null;
        }
        return ifieldoutput;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000");
    private List _v;
    private Iterator _e;

}
