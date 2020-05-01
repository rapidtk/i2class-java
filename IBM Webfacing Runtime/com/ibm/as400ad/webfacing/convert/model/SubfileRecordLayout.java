// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.convert.*;
import java.util.List;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            RecordLayout, SubfileInfo, SubfileFieldOutputDecorator, FieldOutputEnumeration, 
//            FieldArea

public class SubfileRecordLayout extends RecordLayout
    implements IRecordLayout, ENUM_KeywordIdentifiers
{

    public SubfileRecordLayout(RecordNode recordnode, IConversionFactory iconversionfactory)
    {
        super(recordnode, iconversionfactory);
        _minimumHeight = -1;
    }

    public int getLastColumnOnScreen()
    {
        if(!isSFLLINSpecified())
        {
            return getLastColumn();
        } else
        {
            int i = (getFirstColumn() - 1) + (getWidth() + getSFLLIN()) * getRecordsPerRow();
            return i <= getScreenOrWindowWidth() ? i : getScreenOrWindowWidth();
        }
    }

    public int getMinumumHeight()
    {
        if(_minimumHeight == -1)
        {
            _minimumHeight = 0;
            for(FieldOutputEnumeration fieldoutputenumeration = getDisplayableFields(); fieldoutputenumeration.hasMoreElements();)
            {
                IFieldOutput ifieldoutput = fieldoutputenumeration.nextFieldOutput();
                if(ifieldoutput.getFieldNode().getIndicatorString() == null)
                {
                    FieldArea fieldarea = ifieldoutput.getFieldArea();
                    int i = (fieldarea.getLastRow() - getFirstRow()) + 1;
                    if(i > _minimumHeight)
                        _minimumHeight = i;
                }
            }

        }
        return _minimumHeight;
    }

    public int getNumDisplayLines()
    {
        int i;
        if(hasFieldSelection())
            i = getSFLPAG();
        else
            i = getRowsOfRecords() * getHeight();
        return i;
    }

    public int getRecordsPerRow()
    {
        return _recordsPerRow;
    }

    public int getRowsOfRecords()
    {
        return _rowsOfRecords;
    }

    public String getRRNLoopBound()
    {
        String s;
        if(getRecordNode().hasFieldSelection() || getRecordNode().isSubfileFoldable() || isSFLLINSpecified())
            s = getRecordNode().getRelatedSFLCTL().getBeanName() + "." + "getVisibleRecordSize()";
        else
            s = String.valueOf(getSFLPAG());
        return s;
    }

    public int getSFLLIN()
    {
        return _SFLLIN;
    }

    public int getSFLPAG()
    {
        return _SFLPAG;
    }

    public boolean hasFieldSelection()
    {
        return getRecordNode().hasFieldSelection();
    }

    public void init()
    {
        super._outOfFlowFields = null;
    }

    public boolean isSFLLINSpecified()
    {
        return getSFLLIN() != -1;
    }

    protected void prepare()
    {
        super.prepare();
        _recordsPerRow = -1;
        _rowsOfRecords = -1;
        _SFLPAG = -1;
        _SFLLIN = -1;
        RecordNode recordnode = getRecordNode().getRelatedSFLCTL();
        KeywordNode keywordnode = recordnode.findKeywordById(200, getDisplaySizeIndex(), ((FileNode)recordnode.getParent()).getPrimaryDisplaySize());
        if(keywordnode != null)
        {
            KeywordParm keywordparm = keywordnode.getFirstParm();
            if(keywordparm != null)
                _SFLPAG = keywordparm.getVarNumber();
        }
        boolean flag = getDisplaySizeIndex() == ((FileNode)recordnode.getParent()).getSecondaryDisplaySize();
        keywordnode = recordnode.findKeywordById(192, getDisplaySizeIndex(), flag);
        if(keywordnode != null)
        {
            KeywordParm keywordparm1 = keywordnode.getFirstParm();
            if(keywordparm1 != null)
                _SFLLIN = keywordparm1.getVarNumber();
        }
        if(!isSFLLINSpecified())
            _recordsPerRow = 1;
        else
            _recordsPerRow = ((getScreenOrWindowWidth() + _SFLLIN + 1) - getFirstColumn()) / (getWidth() + _SFLLIN);
        _rowsOfRecords = _SFLPAG / _recordsPerRow + (_SFLPAG % _recordsPerRow != 0 ? 1 : 0);
        subfileInfo = new SubfileInfo(_SFLPAG, _recordsPerRow, _rowsOfRecords);
    }

    protected void setDisplaySizeIndex(RecordNode recordnode)
    {
        super.setDisplaySizeIndex(recordnode.getRelatedSFLCTL());
    }

    protected void addScriptableInvisibleFields(IFieldOutput ifieldoutput)
    {
        super._scriptableInvisibleFields.add(new SubfileFieldOutputDecorator(ifieldoutput, getFirstColumn(), getWidth()));
    }

    private int _minimumHeight;
    public static final String GET_VISIBLE_RECORD_METHOD_CALL = "getVisibleRecordSize()";
    private int _recordsPerRow;
    private int _rowsOfRecords;
    private int _SFLLIN;
    private int _SFLPAG;
    public SubfileInfo subfileInfo;
}
