// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.Serializable;
import java.util.StringTokenizer;

public class CursorPosition
    implements Serializable
{

    public CursorPosition(int i, int j)
    {
        _scopeQualifier = null;
        _tagId = null;
        _columnOffset = 0;
        _row = i;
        _column = j;
    }

    public CursorPosition(String s)
    {
        _scopeQualifier = null;
        _tagId = null;
        _columnOffset = 0;
        try
        {
            StringTokenizer stringtokenizer = new StringTokenizer(s, ":");
            String s1 = stringtokenizer.nextToken();
            if(s1.equals("num"))
            {
                _row = Integer.parseInt(stringtokenizer.nextToken());
                _column = Integer.parseInt(stringtokenizer.nextToken());
                if(stringtokenizer.hasMoreTokens())
                    _scopeQualifier = stringtokenizer.nextToken();
            } else
            if(s1.equals("tag"))
            {
                _tagId = stringtokenizer.nextToken();
                _columnOffset = Integer.parseInt(stringtokenizer.nextToken());
            } else
            {
                _row = 1;
                _column = 1;
            }
        }
        catch(Exception exception)
        {
            WFSession.getTraceLogger().err(2, "cursorParam of " + s + " used to construct CursorPosition is invalid.");
            _row = 1;
            _column = 1;
        }
    }

    public int getRow()
    {
        return _row;
    }

    public int getColumn()
    {
        return _column;
    }

    public void setRow(int i)
    {
        _row = i;
    }

    public void setColumn(int i)
    {
        _column = i;
    }

    public void setColumnOffset(int i)
    {
        _columnOffset = i;
    }

    public String getTagId()
    {
        return _tagId;
    }

    public int getColumnOffset()
    {
        return _columnOffset;
    }

    public boolean isBefore(CursorPosition cursorposition)
    {
        return getRow() < cursorposition.getRow() || getRow() == cursorposition.getRow() && getColumn() < cursorposition.getColumn();
    }

    public boolean isEqual(CursorPosition cursorposition)
    {
        return getRow() == cursorposition.getRow() && getColumn() == cursorposition.getColumn();
    }

    public boolean isBeforeOrEqual(CursorPosition cursorposition)
    {
        return isBefore(cursorposition) || isEqual(cursorposition);
    }

    public String getScopeQualifier()
    {
        return _scopeQualifier;
    }

    public String getSetCursorParameters()
    {
        String s = _scopeQualifier == null ? "" : _scopeQualifier;
        return getRow() + ", " + getColumn() + ", '" + s + "'";
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2002");
    private int _row;
    private int _column;
    private String _scopeQualifier;
    private String _tagId;
    private int _columnOffset;
    public static final String CURSOR_SEPARATOR = ":";
    public static final String CRSR_STR_CONTENT_NUM = "num";
    public static final String CRSR_STR_CONTENT_TAG = "tag";
    public static final String DEFAULT_CURSOR_POSITION = "num:1:1::";

}
