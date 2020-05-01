// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import java.util.List;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            VisibleRectangle, CoveredRecordInfo

public class ClearedLines extends VisibleRectangle
    implements Cloneable
{

    public ClearedLines()
    {
    }

    public ClearedLines(int i, int j)
    {
        _firstLine = i;
        _lastLine = j;
    }

    public Object clone()
    {
        ClearedLines clearedlines = null;
        clearedlines = (ClearedLines)super.clone();
        return clearedlines;
    }

    public int getFirstFieldLine()
    {
        return _firstLine;
    }

    public int getLastFieldLine()
    {
        return _lastLine;
    }

    public int getStartingLineNumber()
    {
        return getFirstFieldLine();
    }

    public boolean hasNoRecordToCover()
    {
        return super._coveredByThis.isEmpty();
    }

    public void setFirstFieldLine(int i)
    {
        _firstLine = i;
    }

    public void setLastFieldLine(int i)
    {
        _lastLine = i;
    }

    public void updateFirstAndLastLines()
    {
        int i = _lastLine;
        int j = _firstLine;
        for(int k = 0; k < super._coveredByThis.size(); k++)
        {
            CoveredRecordInfo coveredrecordinfo = (CoveredRecordInfo)super._coveredByThis.get(k);
            if(coveredrecordinfo.getStartLine() < i)
                i = coveredrecordinfo.getStartLine();
            if(coveredrecordinfo.getEndLine() > j)
                j = coveredrecordinfo.getEndLine();
        }

        _firstLine = i;
        _lastLine = j;
    }

    private int _firstLine;
    private int _lastLine;
}
