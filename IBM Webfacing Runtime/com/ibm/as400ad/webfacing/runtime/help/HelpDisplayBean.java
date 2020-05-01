// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;

import java.util.ArrayList;
import java.util.List;

public class HelpDisplayBean
{

    public HelpDisplayBean()
    {
        _helpTitle = null;
        _helpInfoList = null;
        _isHelpRecord = true;
        _displayOneAtATime = true;
        _hasExtendedHelp = false;
        _helpInfoList = new ArrayList();
    }

    public HelpDisplayBean(boolean flag)
    {
        this();
        _displayOneAtATime = flag;
    }

    public void addHelpInfo(Object obj)
    {
        _helpInfoList.add(obj);
    }

    public Object getHelpInfo(int i)
    {
        return _helpInfoList.get(i);
    }

    public List getHelpInfoList()
    {
        return _helpInfoList;
    }

    public String getHelpTitle()
    {
        return _helpTitle;
    }

    public boolean isDisplayOneAtATime()
    {
        return _displayOneAtATime;
    }

    public boolean isHelpRecord()
    {
        return _isHelpRecord;
    }

    public boolean hasExtendedHelp()
    {
        return _hasExtendedHelp;
    }

    public void setDisplayOneAtATime(boolean flag)
    {
        _displayOneAtATime = flag;
    }

    public void setHelpInfo(int i, Object obj)
    {
        _helpInfoList.set(i, obj);
    }

    public void setHelpInfoList(List list)
    {
        _helpInfoList = list;
    }

    public void setHelpTitle(String s)
    {
        _helpTitle = s;
    }

    public void setIsHelpRecord(boolean flag)
    {
        _isHelpRecord = flag;
    }

    public void setHasExtendedHelp(boolean flag)
    {
        _hasExtendedHelp = flag;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2001, 2002, all rights reserved");
    private String _helpTitle;
    private List _helpInfoList;
    private boolean _isHelpRecord;
    private boolean _displayOneAtATime;
    private boolean _hasExtendedHelp;

}
