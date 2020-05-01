// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;


// Referenced classes of package com.ibm.as400ad.webfacing.runtime.help:
//            IDisplayHelpInfo

public class UIMHelpBean
    implements IDisplayHelpInfo
{

    public UIMHelpBean()
    {
        _beanName = null;
        _bean = null;
        _firstDspLine = 0;
        _lastDspLine = 0;
        _jspName = null;
    }

    public String getBeanName()
    {
        return _beanName;
    }

    public Object getBeanValue()
    {
        return _bean;
    }

    public int getFirstDisplayLine()
    {
        return _firstDspLine;
    }

    public String getJspName()
    {
        return _jspName;
    }

    public int getLastDisplayLine()
    {
        return _lastDspLine;
    }

    public void setBean(Object obj)
    {
        _bean = obj;
    }

    public void setBeanName(String s)
    {
        _beanName = s;
    }

    public void setFirstDisplayLine(int i)
    {
        _firstDspLine = i;
    }

    public void setJspName(String s)
    {
        _jspName = s;
    }

    public void setLastDisplayLine(int i)
    {
        _lastDspLine = i;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2002, all rights reserved");
    private String _beanName;
    private Object _bean;
    private int _firstDspLine;
    private int _lastDspLine;
    private String _jspName;

}
