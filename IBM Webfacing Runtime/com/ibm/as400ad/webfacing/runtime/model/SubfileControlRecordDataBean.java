// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import com.ibm.as400ad.webfacing.runtime.controller.IReadOutputBuffer;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingLevelCheckException;
import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;
import com.ibm.as400ad.webfacing.runtime.model.def.SubfileControlRecordDataDefinition;
import java.io.IOException;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model:
//            RecordDataBean, SubfileRecordDataBean, ISFLCTLRecordData, ISFLCTLInputBufferSaveArea, 
//            ResponseIndicators

public class SubfileControlRecordDataBean extends RecordDataBean
    implements ISFLCTLRecordData, ISFLCTLInputBufferSaveArea
{

    public SubfileControlRecordDataBean(SubfileControlRecordDataDefinition subfilecontrolrecorddatadefinition, IRecordDataDefinition irecorddatadefinition, IReadOutputBuffer ireadoutputbuffer)
        throws IOException, WebfacingLevelCheckException, WebfacingInternalException
    {
        super(subfilecontrolrecorddatadefinition, ireadoutputbuffer);
        _rrn = 0;
        _subfileDefinition = irecorddatadefinition;
        initializeSFLCTLFromOutputBuffer(ireadoutputbuffer);
    }

    public void addToChangedRecords(int i)
    {
        if(!_changedRecords.contains(_subfileRecordArray[i]))
            _changedRecords.add(_subfileRecordArray[i]);
    }

    public Object clone()
    {
        SubfileControlRecordDataBean subfilecontrolrecorddatabean = (SubfileControlRecordDataBean)super.clone();
        int i = _subfileRecordArray.length;
        subfilecontrolrecorddatabean._subfileRecordArray = new SubfileRecordDataBean[i];
        for(int j = 0; j < i; j++)
            if(_subfileRecordArray[j] != null)
                subfilecontrolrecorddatabean._subfileRecordArray[j] = (SubfileRecordDataBean)_subfileRecordArray[j].clone();

        i = _changedRecords.size();
        subfilecontrolrecorddatabean._changedRecords = new Vector(i);
        for(int k = 0; k < i; k++)
        {
            int l = ((SubfileRecordDataBean)_changedRecords.elementAt(k)).getRRN();
            subfilecontrolrecorddatabean._changedRecords.add(subfilecontrolrecorddatabean._subfileRecordArray[l]);
        }

        return subfilecontrolrecorddatabean;
    }

    protected SubfileRecordDataBean createSubfileRecord(IReadOutputBuffer ireadoutputbuffer)
        throws IOException, WebfacingLevelCheckException
    {
        return new SubfileRecordDataBean(_subfileDefinition, ireadoutputbuffer);
    }

    public Vector getChangedRecords()
    {
        return _changedRecords;
    }

    public int getLastRecordNumber()
    {
        return _lastRRN;
    }

    public int getNumberOfRecords()
    {
        return _numberOfRecords;
    }

    public int getPageSize()
    {
        return _pageSize;
    }

    public ISFLCTLInputBufferSaveArea getSFLCTLInputBufferSaveArea()
    {
        return this;
    }

    public IRecordDataDefinition getSubfileRecordDefinition()
    {
        return _subfileDefinition;
    }

    public List getSubfileRecords()
    {
        return Arrays.asList(_subfileRecordArray);
    }

    public int getSubfileSize()
    {
        return _subfileSize;
    }

    private void initializeSFLCTLFromOutputBuffer(IReadOutputBuffer ireadoutputbuffer)
        throws IOException, WebfacingLevelCheckException, WebfacingInternalException
    {
        _numberOfRecords = ireadoutputbuffer.getNumberOfSubfileRecords();
        _lastRRN = ireadoutputbuffer.getLastSflRRN();
        _sflComplete = ireadoutputbuffer.isSflComplete();
        SubfileControlRecordDataDefinition subfilecontrolrecorddatadefinition = (SubfileControlRecordDataDefinition)getRecordDataDefinition();
        _subfileSize = subfilecontrolrecorddatadefinition.getSubfileSize(this);
        _pageSize = subfilecontrolrecorddatadefinition.getPageSize();
        initializeSubfilesFromOutputBuffer(ireadoutputbuffer);
        if(getRecordDataDefinition().hasSeparateIndicatorArea() && _numberOfRecords > 0 && ireadoutputbuffer.hasSubfileIndicatorArea())
        {
            int i = 1;
            ireadoutputbuffer.positionStreamToSubfileIndicatorArea();
            for(int j = 0; j < _numberOfRecords; j++)
            {
                while(_subfileRecordArray[i] == null) 
                    i++;
                _subfileRecordArray[i++].readIndicatorArea(ireadoutputbuffer);
            }

        }
        _changedRecords = new Vector();
    }

    private void initializeSubfilesFromOutputBuffer(IReadOutputBuffer ireadoutputbuffer)
        throws IOException, WebfacingLevelCheckException
    {
        Vector vector;
        if(_numberOfRecords > _pageSize)
            vector = new Vector(_numberOfRecords + 1, 1);
        else
            vector = new Vector(_pageSize + 1, 1);
        ireadoutputbuffer.positionStreamToSubfileIOBuffer();
        vector.add(null);
        if(_sflComplete)
            _rrn = 1;
        for(int j = 0; j < _numberOfRecords; j++)
        {
            SubfileRecordDataBean subfilerecorddatabean = createSubfileRecord(ireadoutputbuffer);
            int i = subfilerecorddatabean.getRRN();
            if(!_sflComplete)
            {
                if(_rrn == 0 && j == 0)
                    _rrn = i;
                i = (i - _rrn) + 1;
            }
            int k = (i + 1) - vector.capacity();
            if(k > 0)
                vector.ensureCapacity(i + 1);
            vector.setSize(i + 1);
            vector.setElementAt(subfilerecorddatabean, i);
        }

        _subfileRecordArray = new SubfileRecordDataBean[vector.size()];
        vector.copyInto(_subfileRecordArray);
    }

    protected void reinitializeMappedRecord()
    {
        _changedRecords.clear();
        super.reinitializeMappedRecord();
        for(int i = 1; i < _subfileRecordArray.length; i++)
            if(_subfileRecordArray[i] != null)
                _subfileRecordArray[i].getResponseIndicators().clearReferencedResponseIndicators();

    }

    public void updateSubfileInfo(int i, int j)
    {
        if(!_sflComplete)
            if(i < _rrn)
            {
                int k = _rrn - i;
                int i1 = k + _subfileRecordArray.length;
                if(i1 > j + 1)
                    i1 = j + 1;
                SubfileRecordDataBean asubfilerecorddatabean3[] = new SubfileRecordDataBean[i1];
                for(int j1 = 0; j1 < k; j1++)
                    asubfilerecorddatabean3[j1] = null;

                System.arraycopy(_subfileRecordArray, 0, asubfilerecorddatabean3, k, i1 - k);
                _subfileRecordArray = asubfilerecorddatabean3;
                _rrn = i;
            } else
            if(i > _rrn)
            {
                if(_rrn > 0)
                    if(_subfileRecordArray.length <= (i - _rrn) + 1)
                    {
                        SubfileRecordDataBean asubfilerecorddatabean[] = new SubfileRecordDataBean[1];
                        asubfilerecorddatabean[0] = null;
                        _subfileRecordArray = asubfilerecorddatabean;
                    } else
                    {
                        int l = _subfileRecordArray.length - (i - _rrn);
                        if(l > j + 1)
                            l = j + 1;
                        SubfileRecordDataBean asubfilerecorddatabean2[] = new SubfileRecordDataBean[l];
                        asubfilerecorddatabean2[0] = null;
                        if(l > 1)
                            System.arraycopy(_subfileRecordArray, (i - _rrn) + 1, asubfilerecorddatabean2, 1, l - 1);
                        _subfileRecordArray = asubfilerecorddatabean2;
                    }
                _rrn = i;
            } else
            if(_subfileRecordArray.length > j + 1)
            {
                SubfileRecordDataBean asubfilerecorddatabean1[] = new SubfileRecordDataBean[j + 1];
                System.arraycopy(_subfileRecordArray, 0, asubfilerecorddatabean1, 0, j + 1);
                _subfileRecordArray = asubfilerecorddatabean1;
            }
    }

    public boolean isSubfileComplete()
    {
        return _sflComplete;
    }

    public void updateForPaging(IReadOutputBuffer ireadoutputbuffer, int i)
        throws IOException, WebfacingLevelCheckException
    {
        _rrn = i;
        _numberOfRecords = ireadoutputbuffer.getNumberOfSubfileRecords();
        _changedRecords.clear();
        initializeSubfilesFromOutputBuffer(ireadoutputbuffer);
        if(getRecordDataDefinition().hasSeparateIndicatorArea() && _numberOfRecords > 0 && ireadoutputbuffer.hasSubfileIndicatorArea())
        {
            int j = 1;
            ireadoutputbuffer.positionStreamToSubfileIndicatorArea();
            for(int k = 0; k < _numberOfRecords; k++)
            {
                while(_subfileRecordArray[j] == null) 
                    j++;
                _subfileRecordArray[j++].readIndicatorArea(ireadoutputbuffer);
            }

        }
    }

    protected int _subfileSize;
    private int _pageSize;
    private SubfileRecordDataBean _subfileRecordArray[];
    private int _rrn;
    private int _lastRRN;
    private int _numberOfRecords;
    private Vector _changedRecords;
    private boolean _sflComplete;
    private IRecordDataDefinition _subfileDefinition;
}
