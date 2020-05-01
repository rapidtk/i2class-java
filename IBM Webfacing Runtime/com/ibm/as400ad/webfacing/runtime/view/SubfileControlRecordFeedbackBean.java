// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.IReadOutputBuffer;
import com.ibm.as400ad.webfacing.runtime.core.WFApplicationRuntimeError;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingLevelCheckException;
import com.ibm.as400ad.webfacing.runtime.model.*;
import com.ibm.as400ad.webfacing.runtime.model.def.*;
import com.ibm.as400ad.webfacing.runtime.view.def.FieldViewDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordFeedbackDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.RecordFeedbackDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.RecordViewDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.SubfileControlRecordFeedbackDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.SubfileControlRecordViewDefinition;
import java.io.IOException;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            RecordFeedbackBean, SubfileRecordFeedbackBean, CursorReturnData

public class SubfileControlRecordFeedbackBean extends RecordFeedbackBean
{
    class FieldIndexPair
    {

        public String getFieldName()
        {
            return _fieldName;
        }

        public int getPageIndex()
        {
            return _pageIndex;
        }

        String _fieldName;
        int _pageIndex;

        FieldIndexPair(String s)
            throws IllegalArgumentException
        {
            _fieldName = null;
            _pageIndex = 0;
            int i = s.lastIndexOf("$");
            if(i > 0)
            {
                String s1 = s.substring(i + 1);
                _pageIndex = Integer.parseInt(s1);
                _fieldName = s.substring(0, i);
            } else
            {
                throw new IllegalArgumentException("indexedFieldName parameter to FieldIndexPair constructor does not contain index.");
            }
        }
    }


    public SubfileControlRecordFeedbackBean(SubfileControlRecordFeedbackDefinition subfilecontrolrecordfeedbackdefinition, ISFLCTLRecordData isflctlrecorddata)
    {
        super(subfilecontrolrecordfeedbackdefinition, isflctlrecorddata);
        _rrn = 0;
        _isSubfileInactive = false;
        _isSflrcdnbrCursorSpecified = false;
        _sflrcdnbrRRN = 0;
        initFoldState();
    }

    void assignNewRecordData(IRecordData irecorddata)
    {
        super.assignNewRecordData(irecorddata);
        if(!isSubfileInactive())
            decorateSubfileDataBeans((ISFLCTLRecordData)irecorddata);
    }

    public void checkForInvalidSFLRCDNBR()
        throws WFApplicationRuntimeError
    {
        if(_isInvalidSFLRCDNBR)
        {
            SubfileControlRecordDataDefinition subfilecontrolrecorddatadefinition = (SubfileControlRecordDataDefinition)getRecordDataDefinition();
            String s = getSubfileRecordNumberFieldName();
            String s1 = WebfacingConstants.replaceSubstring(_resmri.getString("WF0037"), "&1", Integer.toString(getLastRecordNumber()));
            s1 = WebfacingConstants.replaceSubstring(s1, "&2", getFieldValue(s));
            throw new WFApplicationRuntimeError("CPF5218", s1);
        } else
        {
            return;
        }
    }

    public Object clone()
    {
        SubfileControlRecordFeedbackBean subfilecontrolrecordfeedbackbean = (SubfileControlRecordFeedbackBean)super.clone();
        int i = _subfileFeedbackBeans.length;
        subfilecontrolrecordfeedbackbean._subfileFeedbackBeans = new SubfileRecordFeedbackBean[i];
        for(int j = 0; j < i; j++)
            if(_subfileFeedbackBeans[j] != null)
                subfilecontrolrecordfeedbackbean._subfileFeedbackBeans[j] = (SubfileRecordFeedbackBean)_subfileFeedbackBeans[j].clone();

        return subfilecontrolrecordfeedbackbean;
    }

    private void decorateSubfileDataBeans(ISFLCTLRecordData isflctlrecorddata)
    {
        List list = isflctlrecorddata.getSubfileRecords();
        int i = list.size();
        _subfileFeedbackBeans = new SubfileRecordFeedbackBean[i];
        for(int j = 0; j < i; j++)
        {
            IRecordData irecorddata = (IRecordData)list.get(j);
            if(irecorddata != null)
                _subfileFeedbackBeans[j] = new SubfileRecordFeedbackBean((SubfileControlRecordFeedbackDefinition)getRecordFeedbackDefinition(), irecorddata);
        }

    }

    private int getAdjustedRRN(int i)
    {
        if(isSubfileComplete())
            return (i + getRRN()) - 1;
        else
            return i;
    }

    public boolean evaluateIndicatorExpression(int i, String s)
    {
        SubfileRecordFeedbackBean subfilerecordfeedbackbean = getSubfileRecordFeedbackBean(i);
        if(null != subfilerecordfeedbackbean)
            return subfilerecordfeedbackbean.getOptionIndEval().evaluateIndicatorExpression(s);
        else
            return false;
    }

    protected FieldDataDefinition getFieldDataDefinition(String s)
    {
        FieldDataDefinition fielddatadefinition = null;
        if(isFieldOnSubfile(s))
        {
            FieldIndexPair fieldindexpair = new FieldIndexPair(s);
            fielddatadefinition = getSFLCTLRecordData().getSubfileRecordDefinition().getFieldDefinition(fieldindexpair.getFieldName());
        } else
        {
            fielddatadefinition = super.getFieldDataDefinition(s);
        }
        return fielddatadefinition;
    }

    public String getFieldValue(String s, int i)
    {
        int j = getAdjustedRRN(i);
        if(j < _subfileFeedbackBeans.length && _subfileFeedbackBeans[j] != null)
            return _subfileFeedbackBeans[j].getFieldValue(s);
        else
            return "";
    }

    public int getLastRecordNumber()
    {
        return getSFLCTLRecordData().getLastRecordNumber();
    }

    public int getNumberOfRecords()
    {
        return getSFLCTLRecordData().getNumberOfRecords();
    }

    protected int getPageSize()
    {
        return getSFLCTLRecordData().getPageSize();
    }

    protected int getPageSizeConsiderFold()
    {
        return getPageSizeWithFoldState(isSubfileFolded());
    }

    int getPageSizeWithFoldState(boolean flag)
    {
        return flag ? getPageSize() : getPageSize() * getRowPerSubfile();
    }

    public int getRowPerSubfile()
    {
        int i = ((SubfileControlRecordDataDefinition)getRecordDataDefinition()).getRowPerSubfile();
        if(i == -1)
            i = getSFLCTLFeedbackDefinition().getRowPerSubfile();
        return i;
    }

    public int getRRN()
    {
        return _rrn;
    }

    SubfileControlRecordFeedbackDefinition getSFLCTLFeedbackDefinition()
    {
        return (SubfileControlRecordFeedbackDefinition)super._definition;
    }

    public ISFLCTLRecordData getSFLCTLRecordData()
    {
        return (ISFLCTLRecordData)getRecordData();
    }

    public IRecordDataDefinition getSubfileDataDefinition()
    {
        return getSFLCTLRecordData().getSubfileRecordDefinition();
    }

    public SubfileRecordFeedbackBean getSubfileRecordFeedbackBean(int i)
    {
        int j = getAdjustedRRN(i);
        if(j < _subfileFeedbackBeans.length)
            return _subfileFeedbackBeans[j];
        else
            return null;
    }

    public String getSubfileRecordNumberFieldName()
    {
        String s = getSFLCTLFeedbackDefinition().getSubfileRecordNumberFieldName();
        if(s == null)
            s = ((SubfileControlRecordDataDefinition)getRecordDataDefinition()).getSubfileRecordNumberFieldName();
        return s;
    }

    public String getSubfileScrollFieldName()
    {
        String s = getSFLCTLFeedbackDefinition().getSubfileScrollFieldName();
        if(s == null)
            s = ((SubfileControlRecordDataDefinition)getRecordDataDefinition()).getSubfileScrollFieldName();
        return s;
    }

    protected int getSubfileSize()
    {
        return getSFLCTLRecordData().getSubfileSize();
    }

    private void initFoldState()
    {
        _isSubfileFolded = isKeywordActive(190L) || !isKeywordActive(185L);
    }

    boolean isSubfileFoldable()
    {
        return getSFLCTLFeedbackDefinition().isKeywordSpecified(190L) || getSFLCTLFeedbackDefinition().isKeywordSpecified(185L);
    }

    public boolean isActiveRecord(int i)
    {
        int j = getAdjustedRRN(i);
        return j > 0 && !_isInvalidSFLRCDNBR && j < _subfileFeedbackBeans.length && null != _subfileFeedbackBeans[j];
    }

    private static boolean isFieldOnSubfile(String s)
    {
        return s.lastIndexOf("$") > 0;
    }

    public boolean isRecordPastEndOfSubfile(int i)
    {
        return i + getRRN() > getLastRecordNumber() + 1;
    }

    public boolean isSFLRCDNBRInvalid()
    {
        return _isInvalidSFLRCDNBR;
    }

    public boolean isSubfileFolded()
    {
        return _isSubfileFolded;
    }

    public void prepareForRead()
    {
        super.prepareForRead();
        for(int i = 1; i < _subfileFeedbackBeans.length; i++)
            if(_subfileFeedbackBeans[i] != null)
                _subfileFeedbackBeans[i].prepareForRead();

    }

    public boolean setAIDKey(String s)
    {
        boolean flag = super.setAIDKey(s);
        ISFLCTLRecordData isflctlrecorddata = getSFLCTLRecordData();
        SubfileControlRecordDataDefinition subfilecontrolrecorddatadefinition = (SubfileControlRecordDataDefinition)isflctlrecorddata.getRecordDataDefinition();
        String s1 = getSubfileScrollFieldName();
        if(s1 != null)
        {
            String s2;
            if(s.equals("PAGEUP") || s.equals("ROLLDOWN"))
                s2 = "1";
            else
            if(s.equals("PAGEDOWN") || s.equals("ROLLUP"))
            {
                if(!isSFLENDScrollBar())
                {
                    s2 = Integer.toString(getSFLCTLRecordData().getLastRecordNumber() + 1);
                } else
                {
                    int i = ((getLastRecordNumber() + 1) - getRRN()) / getPageSizeConsiderFold();
                    int j = getRRN() + i * getPageSizeConsiderFold();
                    s2 = Integer.toString(j);
                }
            } else
            {
                s2 = Integer.toString(getRRN());
            }
            updateFieldValue(s1, s2);
        }
        SubfileControlRecordFeedbackDefinition subfilecontrolrecordfeedbackdefinition = (SubfileControlRecordFeedbackDefinition)getRecordFeedbackDefinition();
        String s3 = subfilecontrolrecordfeedbackdefinition.getSubfileModeFieldName();
        if(s3 != null)
            if(isSubfileFolded())
                updateFieldValue(s3, "0");
            else
                updateFieldValue(s3, "1");
        return flag;
    }

    public void setBLANKS_RespIndOnWrite(String s, int i, String s1)
    {
        getSubfileRecordFeedbackBean(i).setBLANKS_RespIndOnWrite(s, s1);
    }

    private void setInitialRRN(int i)
    {
        String s = getSubfileRecordNumberFieldName();
        _isInvalidSFLRCDNBR = false;
        if(s != null)
        {
            int j = 0;
            try
            {
                j = Integer.parseInt(getFieldValue(s));
            }
            catch(NumberFormatException numberformatexception)
            {
                _isInvalidSFLRCDNBR = true;
            }
            if(j < 1 || j > getLastRecordNumber())
                _isInvalidSFLRCDNBR = true;
            if(!_isInvalidSFLRCDNBR)
            {
                _sflrcdnbrRRN = j;
                boolean flag = false;
                KeywordDefinition keyworddefinition = getRecordFeedbackDefinition().getKeywordDefinition(202L);
                if(keyworddefinition == null)
                    keyworddefinition = getRecordDataDefinition().getKeywordDefinition(202L);
                for(Iterator iterator = keyworddefinition.getParameters(); iterator.hasNext();)
                {
                    String s1 = (String)iterator.next();
                    if(s1.equals("PAR_SFLRCDNBR_TOP"))
                        flag = true;
                    else
                    if(s1.equals("PAR_SFLRCDNBR_CURSOR"))
                        _isSflrcdnbrCursorSpecified = true;
                }

                if(flag)
                {
                    setRRN(j);
                } else
                {
                    int k = j / i;
                    int l = j % i == 0 ? k : k + 1;
                    setRRN((l - 1) * i + 1);
                }
            } else
            {
                setRRN(1);
            }
        } else
        {
            setRRN(1);
        }
    }

    public void setOffAllMDT()
    {
        super.setOffAllMDT();
        for(int i = 1; i < _subfileFeedbackBeans.length; i++)
            if(_subfileFeedbackBeans[i] != null && _subfileFeedbackBeans[i].hasChangedFields())
                _subfileFeedbackBeans[i].setOffAllMDT();

    }

    protected void setOffMDTsExceptProtected(RecordViewDefinition recordviewdefinition)
    {
        super.setOffMDTsExceptProtected(recordviewdefinition);
        for(int i = 1; i < _subfileFeedbackBeans.length; i++)
            if(_subfileFeedbackBeans[i] != null)
                _subfileFeedbackBeans[i].setOffMDTsExceptProtected(recordviewdefinition);

    }

    public void setOnScreenFieldValue(String s, int i, String s1)
    {
        getSubfileRecordFeedbackBean(i).setOnScreenFieldValue(s, s1);
    }

    public void setOnScreenFieldValue(String s, String s1)
    {
        if(isFieldOnSubfile(s))
        {
            FieldIndexPair fieldindexpair = new FieldIndexPair(s);
            setOnScreenFieldValue(fieldindexpair.getFieldName(), fieldindexpair.getPageIndex(), s1);
        } else
        {
            super.setOnScreenFieldValue(s, s1);
        }
    }

    protected void setRRN(int i)
    {
        _rrn = i;
    }

    public void setSFLCSRRRN(CursorReturnData cursorreturndata)
    {
        SubfileControlRecordFeedbackDefinition subfilecontrolrecordfeedbackdefinition = (SubfileControlRecordFeedbackDefinition)getRecordFeedbackDefinition();
        String s = subfilecontrolrecordfeedbackdefinition.getSubfileCursorRRNFieldName();
        if(s != null)
        {
            int i;
            if(getRecordName().equals(cursorreturndata.getControlRecordName()))
            {
                if(cursorreturndata.getRRN() != -1)
                    i = (getRRN() - 1) + cursorreturndata.getRRN();
                else
                    i = 0;
            } else
            {
                i = 0;
            }
            updateFieldValue(s, Integer.toString(i));
        }
    }

    boolean shouldFoldStateChange(String s)
    {
        SubfileControlRecordFeedbackDefinition subfilecontrolrecordfeedbackdefinition = getSFLCTLFeedbackDefinition();
        return s.equals(subfilecontrolrecordfeedbackdefinition.getSubfileFoldKey()) || s.equals(subfilecontrolrecordfeedbackdefinition.getSubfileDropKey());
    }

    public void storeOnScreenValues()
    {
        super.storeOnScreenValues();
        if(!isSubfileInactive())
            storeSubfileOnScreenValues();
    }

    public void storeSubfileOnScreenValues()
    {
        for(int i = 1; i < _subfileFeedbackBeans.length; i++)
            if(_subfileFeedbackBeans[i] != null && (_subfileFeedbackBeans[i].isChangedIndirectly() || _subfileFeedbackBeans[i].hasChangedFields()))
            {
                if(_subfileFeedbackBeans[i].hasChangedFields())
                    _subfileFeedbackBeans[i].setChangedIndirectly(false);
                getSFLCTLRecordData().addToChangedRecords(i);
                _subfileFeedbackBeans[i].storeOnScreenValues();
            }

    }

    void toggleSubfileFolded()
    {
        _isSubfileFolded = !_isSubfileFolded;
    }

    private void updateMDTandBLANKS(String s, int i, boolean flag)
    {
        getSubfileRecordFeedbackBean(i).updateMDTandBLANKS(s, flag);
    }

    public void updateMDTandBLANKS(String s, boolean flag)
    {
        if(isFieldOnSubfile(s))
        {
            FieldIndexPair fieldindexpair = new FieldIndexPair(s);
            updateMDTandBLANKS(fieldindexpair.getFieldName(), fieldindexpair.getPageIndex(), flag);
        } else
        {
            super.updateMDTandBLANKS(s, flag);
        }
    }

    public boolean isSFLENDScrollBar()
    {
        return getSFLCTLFeedbackDefinition().isSFLENDScrollBar();
    }

    public void initializeSFLMDT(Iterator iterator)
    {
        while(iterator.hasNext()) 
        {
            FieldViewDefinition fieldviewdefinition = (FieldViewDefinition)iterator.next();
            KeywordDefinition keyworddefinition = fieldviewdefinition.getKeywordDefinition(276L);
            if(keyworddefinition != null)
            {
                int i = _subfileFeedbackBeans.length;
                for(int j = 1; j < i; j++)
                    if(_subfileFeedbackBeans[j] != null)
                        _subfileFeedbackBeans[j].initializeMDT(fieldviewdefinition);

            }
        }
    }

    boolean isMDTOn(int i, String s)
    {
        return getSubfileRecordFeedbackBean(i).isMDTOn(s);
    }

    boolean isSubfileInactive()
    {
        return _isSubfileInactive;
    }

    void makeSubfileInactive()
    {
        _isSubfileInactive = true;
    }

    public void setFieldValue(String s, String s1)
    {
        super.setFieldValue(s, s1);
    }

    public void formatSubfileFieldValues(SubfileControlRecordViewDefinition subfilecontrolrecordviewdefinition)
    {
        for(int i = 1; i < _subfileFeedbackBeans.length; i++)
            if(_subfileFeedbackBeans[i] != null)
                _subfileFeedbackBeans[i].formatFieldValues(subfilecontrolrecordviewdefinition);

    }

    boolean isSflrcdnbrCursorSpecified()
    {
        return _isSflrcdnbrCursorSpecified;
    }

    int getSflrcdnbrRRN()
    {
        return _sflrcdnbrRRN;
    }

    void updateSubfileInfo(int i, int j)
    {
        setInitialRRN(i);
        getSFLCTLRecordData().updateSubfileInfo(getRRN(), j);
        decorateSubfileDataBeans(getSFLCTLRecordData());
    }

    boolean isSubfileComplete()
    {
        return getSFLCTLRecordData().isSubfileComplete();
    }

    void updateForPaging(IReadOutputBuffer ireadoutputbuffer, int i)
        throws IOException, WebfacingLevelCheckException
    {
        getSFLCTLRecordData().updateForPaging(ireadoutputbuffer, i);
        setRRN(i);
        decorateSubfileDataBeans(getSFLCTLRecordData());
    }

    private static ResourceBundle _resmri;
    private SubfileRecordFeedbackBean _subfileFeedbackBeans[];
    private boolean _isSubfileFolded;
    private boolean _isInvalidSFLRCDNBR;
    private int _rrn;
    private boolean _isSubfileInactive;
    private boolean _isSflrcdnbrCursorSpecified;
    public static final int RRN_NOT_SPECIFIED_OR_INVALID = 0;
    private int _sflrcdnbrRRN;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
