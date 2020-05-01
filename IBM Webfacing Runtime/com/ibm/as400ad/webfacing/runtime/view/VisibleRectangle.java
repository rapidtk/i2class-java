// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            CoveredRecordInfo, IVisibleRectangle, RecordViewBean, PresentationLayer

public abstract class VisibleRectangle
    implements IVisibleRectangle, Cloneable, Serializable
{

    public VisibleRectangle()
    {
        _coveredByThis = new ArrayList();
    }

    public void addCoveredByThis(CoveredRecordInfo coveredrecordinfo)
    {
        _coveredByThis.add(coveredrecordinfo);
    }

    public void addCoveredByThis(Collection collection)
    {
        _coveredByThis.addAll(collection);
    }

    public Object clone()
    {
        VisibleRectangle visiblerectangle = null;
        try
        {
            visiblerectangle = (VisibleRectangle)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        int i = _coveredByThis.size();
        visiblerectangle._coveredByThis = new ArrayList(i);
        for(int j = 0; j < i; j++)
            visiblerectangle._coveredByThis.add(((CoveredRecordInfo)_coveredByThis.get(j)).clone());

        return visiblerectangle;
    }

    public List getCoveredByThis()
    {
        return _coveredByThis;
    }

    public int getFirstVisibleRectangleLine()
    {
        return getFirstFieldLine();
    }

    public PresentationLayer getPresentationLayer()
    {
        return _presentationLayer;
    }

    public boolean isLocatedBefore(VisibleRectangle visiblerectangle)
    {
        return getStartingLineNumber() < visiblerectangle.getStartingLineNumber();
    }

    public boolean isOverlappedBy(VisibleRectangle visiblerectangle)
    {
        int i = visiblerectangle.getStartingLineNumber();
        int j = visiblerectangle.getLastFieldLine();
        return getStartingLineNumber() <= i && i <= getLastFieldLine() || getStartingLineNumber() <= j && j <= getLastFieldLine() || getStartingLineNumber() >= i && getLastFieldLine() <= j;
    }

    public void removeFromCoveredRecords(RecordViewBean recordviewbean)
    {
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < _coveredByThis.size(); i++)
        {
            CoveredRecordInfo coveredrecordinfo = (CoveredRecordInfo)_coveredByThis.get(i);
            if(coveredrecordinfo.getRecordViewBean() == recordviewbean)
            {
                _coveredByThis.remove(i--);
                arraylist.addAll(recordviewbean.getCoveredRecords(coveredrecordinfo, this));
            }
        }

        _coveredByThis.addAll(arraylist);
    }

    public void setPresentationLayer(PresentationLayer presentationlayer)
    {
        _presentationLayer = presentationlayer;
    }

    public boolean updateCoveredByThis(RecordViewBean recordviewbean)
    {
        int i = getStartingLineNumber() <= recordviewbean.getStartingLineNumber() ? recordviewbean.getStartingLineNumber() : getStartingLineNumber();
        int j = getLastFieldLine() >= recordviewbean.getLastFieldLine() ? recordviewbean.getLastFieldLine() : getLastFieldLine();
        HashSet hashset = recordviewbean.getExposedLines();
        int k = 0;
        int l = 0;
        for(int i1 = i; !hashset.isEmpty() && i1 <= j; i1++)
            if(hashset.remove(new Integer(i1)))
            {
                if(k == 0)
                {
                    k = i1;
                    l = i1;
                } else
                {
                    l = i1;
                }
            } else
            if(k != 0)
            {
                _coveredByThis.add(new CoveredRecordInfo(recordviewbean, k, l));
                recordviewbean.addCoveringRectangle(this);
                k = 0;
                l = 0;
            }

        if(k != 0)
        {
            _coveredByThis.add(new CoveredRecordInfo(recordviewbean, k, l));
            recordviewbean.addCoveringRectangle(this);
        }
        if(hashset.isEmpty())
        {
            recordviewbean.removeFromCoveringRectangles(this);
            return true;
        } else
        {
            return false;
        }
    }

    public abstract int getStartingLineNumber();

    public abstract int getLastFieldLine();

    public abstract int getFirstFieldLine();

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    protected List _coveredByThis;
    private PresentationLayer _presentationLayer;
}
