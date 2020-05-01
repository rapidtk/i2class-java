// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import java.io.Serializable;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            VisibleRectangle, RecordViewBean

public class CoveredRecordInfo
    implements Cloneable, Serializable
{

    CoveredRecordInfo(RecordViewBean recordviewbean, int i, int j)
    {
        _coveredRecord = recordviewbean;
        _startLine = i;
        _endLine = j;
    }

    public Object clone()
    {
        CoveredRecordInfo coveredrecordinfo = null;
        try
        {
            coveredrecordinfo = (CoveredRecordInfo)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        return coveredrecordinfo;
    }

    public int getEndLine()
    {
        return _endLine;
    }

    public RecordViewBean getRecordViewBean()
    {
        return _coveredRecord;
    }

    public int getStartLine()
    {
        return _startLine;
    }

    public CoveredRecordInfo intersection(int i, int j)
    {
        int k = _startLine <= i ? i : _startLine;
        int l = _endLine >= j ? j : _endLine;
        if(k == _startLine && l == _endLine)
            return this;
        else
            return new CoveredRecordInfo(_coveredRecord, k, l);
    }

    public boolean isOverlappedBy(CoveredRecordInfo coveredrecordinfo)
    {
        return _startLine <= coveredrecordinfo.getEndLine() && _endLine >= coveredrecordinfo.getStartLine();
    }

    public boolean isOverlappedBy(VisibleRectangle visiblerectangle)
    {
        return _startLine <= visiblerectangle.getLastFieldLine() && _endLine >= visiblerectangle.getFirstFieldLine();
    }

    public void setEndLine(int i)
    {
        _endLine = i;
    }

    public void setStartLine(int i)
    {
        _startLine = i;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    private RecordViewBean _coveredRecord;
    private int _startLine;
    private int _endLine;
}
