// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifierStrings;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.IReadOutputBuffer;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.dhtmlview.DhtmlSubfileControlViewBean;
import com.ibm.as400ad.webfacing.runtime.model.def.*;
import com.ibm.as400ad.webfacing.runtime.view.def.DSPATR_PCFieldInfo;
import com.ibm.as400ad.webfacing.runtime.view.def.FieldSelectionSubfileHeightInfo;
import com.ibm.as400ad.webfacing.runtime.view.def.FieldViewDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordViewDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.MSGMessageDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.RecordViewDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.SFLMSGMessageDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.SubfileControlRecordViewDefinition;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import com.ibm.as400ad.webfacing.util.JointIterator;
import java.io.IOException;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            RecordViewBean, SubfileControlRecordFeedbackBean, LocationOnDevice, IndicatorAndRow, 
//            SubfileLocationOnDevice, CursorPosition, IBuildSFLCTLViewBean, RecordFeedbackBean, 
//            VisibleRectangle, PresentationLayer, DisplayAttributeBean, IDisplaySFLCTLRecord, 
//            IRemoveRecord

public class SubfileControlRecordViewBean extends RecordViewBean
    implements IBuildSFLCTLViewBean
{

    public SubfileControlRecordViewBean(SubfileControlRecordViewDefinition subfilecontrolrecordviewdefinition, SubfileControlRecordFeedbackBean subfilecontrolrecordfeedbackbean)
    {
        super(subfilecontrolrecordviewdefinition, subfilecontrolrecordfeedbackbean);
        _dynamicVisibleRecordSize = -1;
        _rowsOccupiedBySubfileRecord = null;
        _isSubfileFormatted = false;
        _SFLMSGs = new Vector();
        if(hasFieldSelectionOnSubfile())
            _dynamicVisibleRecordSize = getLastRecordNumber();
        subfilecontrolrecordfeedbackbean.updateSubfileInfo(getVisibleRecordSize(), getMaximumVisibleRecordSize());
        if(hasFieldSelectionOnSubfile())
            initRowsOccupiedBySubfileRecordArray();
        prepareAllSFLMSGs(subfilecontrolrecordviewdefinition);
        initSubfileFieldVisibility();
        Iterator iterator = getSFLCTLRecordViewDefinition().getSubfileFieldViewDefinitions();
        subfilecontrolrecordfeedbackbean.initializeSFLMDT(iterator);
    }

    void checkForRuntimeErrors()
        throws WFApplicationRuntimeError
    {
        if(isSubfileVisible())
            getSFLCTLFeedbackBean().checkForInvalidSFLRCDNBR();
    }

    public void clearMessages()
    {
        if(isWindowed() || !isERRSFL())
        {
            _SFLMSGs.clear();
            super.clearAllERRMSGs();
        }
    }

    public Object clone()
    {
        SubfileControlRecordViewBean subfilecontrolrecordviewbean = (SubfileControlRecordViewBean)super.clone();
        if(_rowsOccupiedBySubfileRecord != null)
        {
            int i = _rowsOccupiedBySubfileRecord.length;
            subfilecontrolrecordviewbean._rowsOccupiedBySubfileRecord = new int[i];
            System.arraycopy(_rowsOccupiedBySubfileRecord, 0, subfilecontrolrecordviewbean._rowsOccupiedBySubfileRecord, 0, i);
        }
        subfilecontrolrecordviewbean._SFLMSGs = (Vector)_SFLMSGs.clone();
        return subfilecontrolrecordviewbean;
    }

    public boolean enablePageDown()
    {
        return isKeywordActive(155L);
    }

    public boolean enablePageUp()
    {
        return isKeywordActive(156L);
    }

    public boolean evaluateIndicatorExpression(int i, String s)
    {
        try
        {
            if(!s.startsWith("PF"))
                return getSFLCTLFeedbackBean().evaluateIndicatorExpression(i, s);
            boolean flag = false;
            StringTokenizer stringtokenizer = new StringTokenizer(s, ",", true);
            String s1 = stringtokenizer.nextToken();
            stringtokenizer.nextToken();
            String s2 = stringtokenizer.nextToken();
            stringtokenizer.nextToken();
            String s3 = "";
            int j = getConvertedPFieldValue(s2, i);
            if(j % 8 == 7 && s1.equals("PFND") || j / 64 != 0 && s1.equals("PFPR"))
            {
                String s4 = stringtokenizer.hasMoreTokens() ? stringtokenizer.nextToken() : "";
                return getSFLCTLFeedbackBean().evaluateIndicatorExpression(i, s4);
            }
            if(s1.equals("PFMND") || s1.equals("PFMPR"))
            {
                String s5 = stringtokenizer.nextToken();
                if(s5.equals(","))
                    s5 = "";
                else
                    stringtokenizer.nextToken();
                String s6 = stringtokenizer.hasMoreTokens() ? stringtokenizer.nextToken() : "";
                if(getSFLCTLFeedbackBean().evaluateIndicatorExpression(i, s5))
                    return j % 8 == 7 && s1.equals("PFMND") || j / 64 != 0 && s1.equals("PFMPR");
                else
                    return getSFLCTLFeedbackBean().evaluateIndicatorExpression(i, s5);
            } else
            {
                return false;
            }
        }
        catch(Exception exception)
        {
            WFSession.getTraceLogger().err(2, " Exception thrown while evaluating the indicator expression: " + exception);
            return false;
        }
        catch(Throwable throwable)
        {
            WFSession.getTraceLogger().err(2, " Exception thrown while evaluating the indicator expression: " + throwable);
        }
        return false;
    }

    public boolean evaluateIndicatorExpression(String s)
    {
        if(_currentSubfileIndex > 0)
            return evaluateIndicatorExpression(_currentSubfileIndex, s);
        else
            return super.evaluateIndicatorExpression(s);
    }

    public String evaluateStyleClass(int i, DisplayAttributeBean displayattributebean)
    {
        _currentSubfileIndex = i;
        String s = evaluateStyleClass(displayattributebean);
        _currentSubfileIndex = 0;
        return s;
    }

    protected int getConvertedPFieldValue(String s)
    {
        if(_currentSubfileIndex > 0)
            return getConvertedPFieldValue(s, _currentSubfileIndex);
        else
            return super.getConvertedPFieldValue(s);
    }

    private int getConvertedPFieldValue(String s, int i)
    {
        char c = getSFLCTLFeedbackBean().getFieldValue(s, i).charAt(0);
        return convertToOriginalPFieldValue(c);
    }

    public IDisplaySFLCTLRecord getDisplaySFLCTLRecord()
    {
        return new DhtmlSubfileControlViewBean(this);
    }

    public LocationOnDevice getDspatrPC_LocationForSFLRecord()
    {
        initDspatrPC_Location(false);
        return super._dspatrPC_Location;
    }

    public String getFieldValue(String s, int i)
    {
        if(!super._isFormatted)
        {
            getFeedbackBean().formatFieldValues(getRecordViewDefinition());
            super._isFormatted = true;
        }
        if(!_isSubfileFormatted)
        {
            getSFLCTLFeedbackBean().formatSubfileFieldValues(getSFLCTLRecordViewDefinition());
            _isSubfileFormatted = true;
        }
        String s1 = getRawFieldValue(s, i);
        if(s1 == null)
            s1 = "";
        SubfileControlRecordFeedbackBean subfilecontrolrecordfeedbackbean = getSFLCTLFeedbackBean();
        subfilecontrolrecordfeedbackbean.setBLANKS_RespIndOnWrite(s, i, s1);
        return s1;
    }

    public int getLastRecordNumber()
    {
        return getSFLCTLFeedbackBean().getLastRecordNumber();
    }

    Iterator getMessages()
    {
        return new JointIterator(super.getMessages(), getSFLMSGs());
    }

    public int getNumberOfRecords()
    {
        return getSFLCTLFeedbackBean().getNumberOfRecords();
    }

    public int getPageSize()
    {
        return getSFLCTLFeedbackBean().getPageSize();
    }

    public int getPageSizeConsiderFold()
    {
        return getSFLCTLFeedbackBean().getPageSizeConsiderFold();
    }

    protected String getRawFieldValue(String s, int i)
    {
        return getSFLCTLFeedbackBean().getFieldValue(s, i);
    }

    public int getRecordsInFirstPage()
    {
        if(isPageEqualToSize())
        {
            return 0;
        } else
        {
            int i = getRRN();
            int j = (i - 1) % getVisibleRecordSize();
            return j;
        }
    }

    private int getRecordsPerRow()
    {
        return getSFLCTLRecordViewDefinition().getSubfileRecordsPerRow();
    }

    public int getRowsOccupiedBySubfileRecord(int i)
    {
        if(hasFieldSelectionOnSubfile())
        {
            if(i < 1 || i > _rowsOccupiedBySubfileRecord.length - 1)
                return 0;
            else
                return _rowsOccupiedBySubfileRecord[i];
        } else
        {
            return getSFLCTLFeedbackBean().getRowPerSubfile();
        }
    }

    public int getRRN()
    {
        return getSFLCTLFeedbackBean().getRRN();
    }

    private SubfileControlRecordFeedbackBean getSFLCTLFeedbackBean()
    {
        return (SubfileControlRecordFeedbackBean)super._recordFeedbackBean;
    }

    private SubfileControlRecordViewDefinition getSFLCTLRecordViewDefinition()
    {
        return (SubfileControlRecordViewDefinition)super._definition;
    }

    public Iterator getSFLMSGs()
    {
        return _SFLMSGs.iterator();
    }

    private String getSFLROLVALFieldName()
    {
        String s = getSFLCTLRecordViewDefinition().getSFLROLVALFieldName();
        if(s == null)
            s = ((SubfileControlRecordDataDefinition)getFeedbackBean().getRecordDataDefinition()).getSFLROLVALFieldName();
        return s;
    }

    public int getSubfileAreaFirstRow()
    {
        return getSFLCTLRecordViewDefinition().getSubfileAreaFirstRow();
    }

    public int getSubfileAreaHeight()
    {
        return getSFLCTLRecordViewDefinition().getSubfileAreaHeight();
    }

    private String getSubfileFieldIndicatorExpression(String s)
    {
        return getSubfileFieldViewDefinition(s).getIndicatorExpression();
    }

    public FieldViewDefinition getSubfileFieldViewDefinition(String s)
    {
        return ((SubfileControlRecordViewDefinition)getRecordViewDefinition()).getSubfileFieldViewDefinition(s);
    }

    private Iterator getSubfileFieldViewDefinitions()
    {
        return ((SubfileControlRecordViewDefinition)getRecordViewDefinition()).getSubfileFieldViewDefinitions();
    }

    protected IRecordDataDefinition getSubfileRecordDataDefinition()
    {
        return ((SubfileControlRecordFeedbackBean)super._recordFeedbackBean).getSubfileDataDefinition();
    }

    public int getSubfileSize()
    {
        return getSFLCTLFeedbackBean().getSubfileSize();
    }

    public int getVisibleRecordSize()
    {
        if(hasFieldSelectionOnSubfile())
            return _dynamicVisibleRecordSize;
        int i = getRecordsPerRow();
        int j = getPageSize();
        if(i > 1 && j % i != 0)
            return (j / i + 1) * i;
        else
            return getPageSizeConsiderFold();
    }

    public int getMaximumVisibleRecordSize()
    {
        SubfileControlRecordFeedbackBean subfilecontrolrecordfeedbackbean = getSFLCTLFeedbackBean();
        return subfilecontrolrecordfeedbackbean.isSubfileFoldable() ? subfilecontrolrecordfeedbackbean.getPageSizeWithFoldState(false) : getVisibleRecordSize();
    }

    private boolean hasFieldSelectionOnSubfile()
    {
        return ((SubfileControlRecordViewDefinition)getRecordViewDefinition()).hasFieldSelectionOnSubfile();
    }

    public boolean hasSFLMessages()
    {
        return getSFLMSGs().hasNext();
    }

    protected void initDspatrPC_Location(boolean flag)
    {
        SubfileControlRecordViewDefinition subfilecontrolrecordviewdefinition = getSFLCTLRecordViewDefinition();
        for(int i = 1; i <= getVisibleRecordSize(); i++)
            if(isActiveRecord(i))
            {
                for(Iterator iterator = subfilecontrolrecordviewdefinition.getSubfileDspatrPCFieldInfos(); iterator.hasNext();)
                {
                    DSPATR_PCFieldInfo dspatr_pcfieldinfo = (DSPATR_PCFieldInfo)iterator.next();
                    if(evaluateIndicatorExpression(i, dspatr_pcfieldinfo.getIndExpr()) && isFieldVisible(i, dspatr_pcfieldinfo) && !isFieldHiddenDueToTruncation(getSubfileFieldViewDefinition(dspatr_pcfieldinfo.getFieldName())))
                    {
                        super._dspatrPC_Location = getSubfileLocationOnDevice(null, dspatr_pcfieldinfo.getFieldName(), i);
                        return;
                    }
                }

            }

        if(flag)
            super.initDspatrPC_Location(true);
        else
            super._dspatrPC_Location = new LocationOnDevice();
    }

    private boolean isFieldHiddenDueToTruncation(FieldViewDefinition fieldviewdefinition)
    {
        if(fieldviewdefinition == null)
            return false;
        if(!isSubfileFolded())
        {
            int i = getSubfileAreaFirstRow();
            int j = fieldviewdefinition.getPosition().getRow() + (fieldviewdefinition.getPosition().getColumn() + fieldviewdefinition.getFieldLength()) / getMaxColumn();
            if(j > i)
                return true;
        }
        return false;
    }

    protected void initRecordViewDefinition(RecordFeedbackBean recordfeedbackbean)
    {
        super.initRecordViewDefinition(recordfeedbackbean);
        IRecordDataDefinition irecorddatadefinition = getSubfileRecordDataDefinition();
        SubfileControlRecordViewDefinition subfilecontrolrecordviewdefinition = (SubfileControlRecordViewDefinition)getRecordViewDefinition();
        Iterator iterator = subfilecontrolrecordviewdefinition.getSubfileFieldViewDefinitions();
        decorateFieldDataDefinitions(iterator, irecorddatadefinition);
    }

    private void initRowsOccupiedBySubfileRecordArray()
    {
        FieldSelectionSubfileHeightInfo fieldselectionsubfileheightinfo = ((SubfileControlRecordViewDefinition)getRecordViewDefinition()).getFieldSelectionSubfileHeightInfo();
        int i = getLastRecordNumber();
        _rowsOccupiedBySubfileRecord = new int[i + 1];
        if(i == 0)
            return;
        int j = 0;
        for(int k = 1; k <= i; k++)
        {
            if(!isActiveRecord(k))
                _rowsOccupiedBySubfileRecord[k] = 0;
            else
            if(fieldselectionsubfileheightinfo.isSubfileFixedHeight())
            {
                _rowsOccupiedBySubfileRecord[k] = fieldselectionsubfileheightinfo.getMinimumHeight();
            } else
            {
                int l = fieldselectionsubfileheightinfo.getMinimumHeight();
                for(Iterator iterator = fieldselectionsubfileheightinfo.getIndicatorAndRowIterator(); iterator.hasNext();)
                {
                    IndicatorAndRow indicatorandrow = (IndicatorAndRow)iterator.next();
                    if(indicatorandrow.getRowInSubfile() > l && evaluateIndicatorExpression(k, indicatorandrow.getIndicatorExpression()))
                        l = indicatorandrow.getRowInSubfile();
                }

                _rowsOccupiedBySubfileRecord[k] = l;
            }
            j += _rowsOccupiedBySubfileRecord[k];
            if(j > getPageSize())
                _dynamicVisibleRecordSize = k - 1;
        }

    }

    void initSubfileFieldVisibility()
    {
        String as[] = getSFLCTLRecordViewDefinition().getSubfileFieldVisDef();
        if(hasFieldSelectionOnSubfile())
        {
            _subfileFieldsNotVisible = new HashSet[getMaximumVisibleRecordSize() + 1];
            _subfileFieldsNotVisible[0] = null;
            for(int i = 1; i <= getMaximumVisibleRecordSize(); i++)
                if(!isActiveRecord(i))
                {
                    _subfileFieldsNotVisible[i] = null;
                } else
                {
                    _currentSubfileIndex = i;
                    _subfileFieldsNotVisible[i] = getFieldsNotVisibleFromDef(as);
                }

            _currentSubfileIndex = 0;
        } else
        {
            _subfileFieldsNotVisible = new HashSet[1];
            _subfileFieldsNotVisible[0] = getFieldsNotVisibleFromDef(as);
        }
    }

    public boolean isActiveRecord(int i)
    {
        return getSFLCTLFeedbackBean().isActiveRecord(i);
    }

    boolean isDisplayable()
    {
        return isSubfileControlVisible() || isSubfileVisible();
    }

    public boolean isFieldVisible(int i, DSPATR_PCFieldInfo dspatr_pcfieldinfo)
    {
        _currentSubfileIndex = i;
        boolean flag = isFieldVisible(dspatr_pcfieldinfo.getFieldName());
        _currentSubfileIndex = 0;
        return flag;
    }

    public boolean isFieldVisible(int i, String s)
    {
        boolean flag = true;
        try
        {
            if(hasFieldSelectionOnSubfile())
                flag = !_subfileFieldsNotVisible[i].contains(s);
            else
                flag = !_subfileFieldsNotVisible[0].contains(s);
        }
        catch(ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) { }
        return flag;
    }

    public boolean isFieldVisible(String s)
    {
        if(_currentSubfileIndex > 0)
            return isFieldVisible(_currentSubfileIndex, s);
        else
            return super.isFieldVisible(s);
    }

    public boolean isPageEqualToSize()
    {
        return getSubfileSize() == getPageSize();
    }

    public boolean isRecordPastEndOfSubfile(int i)
    {
        return getSFLCTLFeedbackBean().isRecordPastEndOfSubfile(i);
    }

    public boolean isRowOfSubfileRecordDisplayed(int i, int j)
    {
        if(i < 1 || i > _rowsOccupiedBySubfileRecord.length - 1)
            return false;
        else
            return j <= _rowsOccupiedBySubfileRecord[i];
    }

    public boolean isScrollbarShown()
    {
        if(hasNoDedicatedSpaceOnScreen() || !getPresentationLayer().isFocusCapable())
            return false;
        if(getSFLCTLFeedbackBean().isSubfileInactive())
            return false;
        if(isSFLROLVALSpecifiedAndInvalid() && isSFLROLFieldNonDisplay())
            return false;
        if(getLastRecordNumber() <= 0)
            return false;
        if(getVisibleRecordSize() <= 0)
            return false;
        return pageNumberFor(getLastRecordNumber()) > 1 || enablePageDown() || enablePageUp();
    }

    public boolean isSFLENDActive()
    {
        return isKeywordActive(188L);
    }

    public boolean isSFLENDScrollBar()
    {
        if(getSFLCTLFeedbackBean().isSFLENDScrollBar())
            return true;
        KeywordDefinition keyworddefinition = getRecordViewDefinition().getKeywordDefinition(188L);
        if(keyworddefinition != null)
        {
            for(Iterator iterator = keyworddefinition.getParameters(); iterator.hasNext();)
            {
                String s = (String)iterator.next();
                if(s.equals(ENUM_KeywordIdentifierStrings.TOKEN_STRINGS[334]))
                    return true;
            }

        }
        return false;
    }

    public boolean isSFLENDSpecified()
    {
        return isKeywordSpecified(188L);
    }

    private boolean isSFLROLFieldNonDisplay()
    {
        if(isSFLROLVALSpecified())
        {
            String s = getSFLROLVALFieldName();
            FieldViewDefinition fieldviewdefinition = getFieldViewDefinition(s);
            if(fieldviewdefinition.isKeywordSpecified(277L))
            {
                String s1 = fieldviewdefinition.getKeywordDefinition(277L).getIndicatorExpression();
                if(s1.equals(""))
                    return true;
                else
                    return evaluateIndicatorExpression(s1);
            }
        }
        return false;
    }

    private boolean isSFLROLVALSpecified()
    {
        return isKeywordSpecified(204L) || ((SubfileControlRecordDataDefinition)getFeedbackBean().getRecordDataDefinition()).isKeywordSpecified(204L);
    }

    public boolean isSFLROLVALSpecifiedAndInvalid()
    {
        if(isSFLROLVALSpecified())
        {
            String s = getSFLROLVALFieldName();
            String s1 = getFieldValue(s).trim();
            if(s1.equals(""))
                return true;
            try
            {
                return (new Integer(s1)).intValue() <= 0;
            }
            catch(NumberFormatException numberformatexception)
            {
                WFSession.getTraceLogger().err(2, numberformatexception, "FieldValue can not be converted to Integer");
            }
            return false;
        } else
        {
            return false;
        }
    }

    public boolean isSubfileControlVisible()
    {
        return evaluateIndicatorExpression(getSFLCTLRecordViewDefinition().getSFLDSPCTLCondition());
    }

    public boolean isSubfileFolded()
    {
        return getSFLCTLFeedbackBean().isSubfileFolded();
    }

    public boolean isSubfileVisible()
    {
        boolean flag = evaluateIndicatorExpression(getSFLCTLRecordViewDefinition().getSFLDSPCondition());
        if(isKeywordActive(191L))
            return flag;
        if(isKeywordActive(184L) || isKeywordActive(180L))
            return false;
        return flag;
    }

    public int pageNumberFor(int i)
    {
        int k = getRecordsInFirstPage();
        int l = getVisibleRecordSize();
        int j;
        if(k != 0)
        {
            j = (i - k) / l;
            j += (i - k) % l == 0 ? 0 : 1;
            j++;
        } else
        {
            j = i / l;
            j += i % l == 0 ? 0 : 1;
        }
        return j;
    }

    private void prepareAllSFLMSGs(SubfileControlRecordViewDefinition subfilecontrolrecordviewdefinition)
    {
        _SFLMSGs.clear();
        if(evaluateIndicatorExpression(subfilecontrolrecordviewdefinition.getSFLDSPCondition()))
        {
            Iterator iterator = subfilecontrolrecordviewdefinition.getSFLMSGs();
            com.ibm.as400ad.webfacing.runtime.host.HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
            while(iterator.hasNext()) 
            {
                SFLMSGMessageDefinition sflmsgmessagedefinition = (SFLMSGMessageDefinition)iterator.next();
                if(evaluateIndicatorExpression(sflmsgmessagedefinition.getIndicatorExpression()))
                {
                    sflmsgmessagedefinition.resolveMessageText(hostjobinfo, super._recordFeedbackBean);
                    _SFLMSGs.add(sflmsgmessagedefinition);
                }
            }
        }
    }

    private String rightJustify(String s, int i)
    {
        if((s.length() == 0) | (s.length() >= i))
            return s;
        StringBuffer stringbuffer = new StringBuffer(20);
        for(int j = 0; j < i; j++)
            stringbuffer.insert(j, '0');

        stringbuffer.insert(i - s.length(), s);
        return stringbuffer.toString().substring(0, i);
    }

    void setPreviousViewBean(RecordViewBean recordviewbean, IRemoveRecord iremoverecord)
    {
        super.setPreviousViewBean(recordviewbean, iremoverecord);
        if(isKeywordActive(180L) || isKeywordActive(184L))
        {
            makeSubfileInactive();
            if(null != recordviewbean)
                ((SubfileControlRecordViewBean)recordviewbean).makeSubfileInactive();
        }
    }

    public String toHTML()
    {
        StringBuffer stringbuffer = new StringBuffer();
        Iterator iterator = getSFLCTLRecordViewDefinition().getFieldViewDefinitions();
        if(iterator.hasNext())
        {
            stringbuffer.append("<br><hr><br>");
            stringbuffer.append(printHtmlHeader(getRecordName()));
            stringbuffer.append(printHtmlTableContent(iterator));
        }
        Iterator iterator1 = getSubfileFieldViewDefinitions();
        if(iterator1.hasNext())
        {
            stringbuffer.append("<br><hr><br>");
            stringbuffer.append(printHtmlHeader(getSubfileRecordDataDefinition().getName()));
            stringbuffer.append(printHtmlTableContent(iterator1));
        }
        return stringbuffer.toString();
    }

    public boolean isMDTOn(int i, String s)
    {
        if(isProtected())
            return false;
        else
            return getSFLCTLFeedbackBean().isMDTOn(i, s);
    }

    boolean isSubfileInactive()
    {
        return getSFLCTLFeedbackBean().isSubfileInactive();
    }

    void makeSubfileInactive()
    {
        getSFLCTLFeedbackBean().makeSubfileInactive();
    }

    LocationOnDevice getFirstFocusCapableField()
    {
        if(isProtected() || isOutputOnly())
            return null;
        Object obj = null;
        boolean flag = getSubfileAreaFirstRow() == getFirstFieldLine();
        if(flag)
        {
            obj = getFirstSubfileFocusCapableField();
            if(obj == null)
                obj = super.getFirstFocusCapableField();
        } else
        {
            obj = super.getFirstFocusCapableField();
            if(obj == null)
                obj = getFirstSubfileFocusCapableField();
        }
        return ((LocationOnDevice) (obj));
    }

    private SubfileLocationOnDevice getFirstSubfileField(boolean flag, int i)
    {
        if(isActiveRecord(i))
        {
            SubfileLocationOnDevice subfilelocationondevice = null;
            for(Iterator iterator = getSubfileFieldViewDefinitions(); iterator.hasNext();)
            {
                FieldViewDefinition fieldviewdefinition = (FieldViewDefinition)iterator.next();
                if(!flag || flag && isFieldFocusCapable(fieldviewdefinition, i))
                {
                    SubfileLocationOnDevice subfilelocationondevice1 = getSubfileLocationOnDevice(fieldviewdefinition, i);
                    if(subfilelocationondevice1.isBefore(subfilelocationondevice))
                        subfilelocationondevice = subfilelocationondevice1;
                }
            }

            if(subfilelocationondevice != null)
            {
                subfilelocationondevice.setCursorPosition(null);
                return subfilelocationondevice;
            }
        }
        return null;
    }

    private SubfileLocationOnDevice getFirstSubfileFocusCapableField()
    {
        Object obj = null;
        for(int i = 1; i <= getVisibleRecordSize(); i++)
        {
            SubfileLocationOnDevice subfilelocationondevice = getFirstSubfileField(true, i);
            if(subfilelocationondevice != null)
                return subfilelocationondevice;
        }

        return null;
    }

    private boolean isFieldFocusCapable(FieldViewDefinition fieldviewdefinition, int i)
    {
        if(!isSubfileFolded() && getSubfileAreaFirstRow() != fieldviewdefinition.getPosition().getRow())
        {
            return false;
        } else
        {
            _currentSubfileIndex = i;
            boolean flag = super.isFieldFocusCapable(fieldviewdefinition);
            _currentSubfileIndex = 0;
            return flag;
        }
    }

    LocationOnDevice getLocationOnDeviceAt(CursorPosition cursorposition)
    {
        if(cursorposition.getRow() >= getSubfileAreaFirstRow() && cursorposition.getRow() < getSubfileAreaFirstRow() + getSubfileAreaHeight())
            return getSubfileLocationOnDeviceAt(cursorposition);
        else
            return super.getLocationOnDeviceAt(cursorposition);
    }

    LocationOnDevice getLocationOnDeviceAt(String s, int i, int j)
    {
        return getSubfileLocationOnDeviceAt(s, i, j);
    }

    private SubfileLocationOnDevice getSubfileLocationOnDeviceAt(CursorPosition cursorposition)
    {
        int i = getPageIndexFor(cursorposition);
        SubfileLocationOnDevice subfilelocationondevice = null;
        if(i == 0 || !isActiveRecord(i))
        {
            subfilelocationondevice = new SubfileLocationOnDevice(cursorposition, getDisplayZIndex(), getSubfileRecordDataDefinition().getName(), getRecordName());
        } else
        {
            CursorPosition cursorposition1 = getSubfileOffset(i);
            for(Iterator iterator = getSubfileFieldViewDefinitions(); iterator.hasNext();)
            {
                FieldViewDefinition fieldviewdefinition = (FieldViewDefinition)iterator.next();
                if(isFieldVisible(i, fieldviewdefinition.getFieldName()) && !isFieldHiddenDueToTruncation(fieldviewdefinition))
                {
                    CursorPosition cursorposition2 = new CursorPosition(cursorposition.getRow() - cursorposition1.getRow(), cursorposition.getColumn() - cursorposition1.getColumn());
                    if(isCursorWithinField(cursorposition2, fieldviewdefinition))
                    {
                        CursorPosition cursorposition3 = fieldviewdefinition.getPosition();
                        int j;
                        if(fieldviewdefinition.getHeight() > 1)
                            j = fieldviewdefinition.getWidth();
                        else
                            j = getMaxColumn();
                        int k = (cursorposition.getRow() - cursorposition1.getRow() - cursorposition3.getRow()) * j + (cursorposition.getColumn() - cursorposition1.getColumn() - cursorposition3.getColumn());
                        cursorposition.setColumnOffset(k);
                        return getSubfileLocationOnDevice(cursorposition, fieldviewdefinition.getFieldName(), i);
                    }
                }
            }

        }
        if(subfilelocationondevice == null)
            subfilelocationondevice = new SubfileLocationOnDevice(cursorposition, getDisplayZIndex(), getSubfileRecordDataDefinition().getName(), i, getRecordName());
        if(!isRecordValidForRTNCSRLOC())
            subfilelocationondevice.setIsValidForRTNCSRLOC(false);
        return subfilelocationondevice;
    }

    private SubfileLocationOnDevice getSubfileLocationOnDeviceAt(String s, int i, int j)
    {
        CursorPosition cursorposition = new CursorPosition("num:1:1::");
        int k = j;
        SubfileLocationOnDevice subfilelocationondevice = null;
        if(k == 0 || !isActiveRecord(k))
        {
            subfilelocationondevice = new SubfileLocationOnDevice(cursorposition, getDisplayZIndex(), getSubfileRecordDataDefinition().getName(), getRecordName());
        } else
        {
            CursorPosition cursorposition1 = getSubfileOffset(k);
            for(Iterator iterator = getSubfileFieldViewDefinitions(); iterator.hasNext();)
            {
                FieldViewDefinition fieldviewdefinition = (FieldViewDefinition)iterator.next();
                if(isFieldVisible(fieldviewdefinition.getFieldName()))
                {
                    if(s.equals(fieldviewdefinition.getFieldName()))
                    {
                        int l;
                        int i1;
                        if(fieldviewdefinition.getHeight() > 1)
                        {
                            int j1 = fieldviewdefinition.getWidth();
                            i1 = fieldviewdefinition.getPosition().getColumn() + (cursorposition1.getColumn() + i) % j1;
                            l = fieldviewdefinition.getPosition().getRow() + cursorposition1.getRow() + i / j1;
                        } else
                        {
                            int k1 = getMaxColumn();
                            i1 = (fieldviewdefinition.getPosition().getColumn() + cursorposition1.getColumn() + i) % k1;
                            l = fieldviewdefinition.getPosition().getRow() + cursorposition1.getRow() + (fieldviewdefinition.getPosition().getColumn() + cursorposition1.getColumn() + i) / k1;
                        }
                        CursorPosition cursorposition2 = new CursorPosition(l, i1);
                        return getSubfileLocationOnDevice(cursorposition2, fieldviewdefinition.getFieldName(), k);
                    }
                } else
                {
                    return getSubfileLocationOnDeviceAt(cursorposition);
                }
            }

        }
        if(subfilelocationondevice == null)
            subfilelocationondevice = new SubfileLocationOnDevice(cursorposition, getDisplayZIndex(), getSubfileRecordDataDefinition().getName(), k, getRecordName());
        if(!isRecordValidForRTNCSRLOC())
            subfilelocationondevice.setIsValidForRTNCSRLOC(false);
        return subfilelocationondevice;
    }

    CursorPosition getSubfileOffset(int i)
    {
        int j = 0;
        int k = 0;
        if(isKeywordSpecified(192L))
        {
            j = (i - 1) % getSubfileAreaHeight();
            int l = (i - 1) / getSubfileAreaHeight() + 1;
            SubfileControlRecordViewDefinition subfilecontrolrecordviewdefinition = getSFLCTLRecordViewDefinition();
            k = (l - 1) * (subfilecontrolrecordviewdefinition.getSubfileRecordWidth() + subfilecontrolrecordviewdefinition.getSFLLIN());
        } else
        if(hasFieldSelectionOnSubfile())
        {
            for(int i1 = 1; i1 < _rowsOccupiedBySubfileRecord.length && i1 < i; i1++)
                j += _rowsOccupiedBySubfileRecord[i1];

        } else
        {
            j = (i - 1) * getSubfileRecordHeight();
        }
        return new CursorPosition(j, k);
    }

    int getSubfileRecordHeight()
    {
        if(isKeywordSpecified(192L))
            return 1;
        if(!isSubfileFolded())
            return 1;
        else
            return getSFLCTLFeedbackBean().getRowPerSubfile();
    }

    int getPageIndexFor(CursorPosition cursorposition)
    {
        int i = 0;
        int j = (cursorposition.getRow() - getSubfileAreaFirstRow()) + 1;
        if(isKeywordSpecified(192L))
        {
            SubfileControlRecordViewDefinition subfilecontrolrecordviewdefinition = getSFLCTLRecordViewDefinition();
            int l = subfilecontrolrecordviewdefinition.getSubfileFirstColumn();
            int i1 = subfilecontrolrecordviewdefinition.getSubfileRecordWidth();
            int j1 = subfilecontrolrecordviewdefinition.getSFLLIN();
            int k1 = i1 + j1;
            int l1 = (cursorposition.getColumn() - l) + 1;
            if(l1 > 0)
            {
                int i2 = (l1 - 1) / k1 + 1;
                if(i2 <= subfilecontrolrecordviewdefinition.getSubfileRecordsPerRow() && l1 < (l + i2 * k1) - j1)
                    i = (i2 - 1) * getSubfileAreaHeight() + j;
            }
        } else
        if(hasFieldSelectionOnSubfile())
        {
            int k = 0;
            for(i = 1; i < _rowsOccupiedBySubfileRecord.length && j > k; i++)
                k += _rowsOccupiedBySubfileRecord[i];

            i--;
        } else
        {
            i = (j - 1) / getSubfileRecordHeight() + 1;
        }
        return i;
    }

    private SubfileLocationOnDevice getSubfileLocationOnDevice(FieldViewDefinition fieldviewdefinition, int i)
    {
        return getSubfileLocationOnDevice(fieldviewdefinition.getPosition(), fieldviewdefinition.getFieldName(), i);
    }

    private SubfileLocationOnDevice getSubfileLocationOnDevice(CursorPosition cursorposition, String s, int i)
    {
        SubfileLocationOnDevice subfilelocationondevice = new SubfileLocationOnDevice(cursorposition, getDisplayZIndex(), getSubfileRecordDataDefinition().getName(), s, i, getRecordName());
        if(!isRecordValidForRTNCSRLOC())
            subfilelocationondevice.setIsValidForRTNCSRLOC(false);
        return subfilelocationondevice;
    }

    public LocationOnDevice getSflrcdnbrCursor()
    {
        if(getSFLCTLFeedbackBean().isSflrcdnbrCursorSpecified())
        {
            int i = getSFLCTLFeedbackBean().getSflrcdnbrRRN();
            if(i != 0)
                return getSflrcdnbrField(i);
            else
                return null;
        } else
        {
            return null;
        }
    }

    public LocationOnDevice getSflrcdnbrField(int i)
    {
        int j = (i - getRRN()) + 1;
        SubfileLocationOnDevice subfilelocationondevice = getFirstSubfileField(true, j);
        if(subfilelocationondevice != null)
            return subfilelocationondevice;
        else
            return getFirstSubfileField(false, j);
    }

    public String getSubfileValuesAfterEditing(String s)
    {
        FieldViewDefinition fieldviewdefinition = getSubfileFieldViewDefinition(s);
        return applyEditingOnValues(fieldviewdefinition);
    }

    public int getFirstFieldLine()
    {
        if(isSubfileVisible())
            return super._definition.getFirstFieldLine();
        else
            return ((SubfileControlRecordViewDefinition)super._definition).getSFLCTLFirstFieldLine();
    }

    public int getLastFieldLine()
    {
        if(isSubfileVisible())
            return super._definition.getLastFieldLine();
        else
            return ((SubfileControlRecordViewDefinition)super._definition).getSFLCTLLastFieldLine();
    }

    public void updateForPaging(IReadOutputBuffer ireadoutputbuffer, int i)
        throws IOException, WebfacingLevelCheckException
    {
        getSFLCTLFeedbackBean().updateForPaging(ireadoutputbuffer, i);
        _isSubfileFormatted = false;
        Iterator iterator = getSFLCTLRecordViewDefinition().getSubfileFieldViewDefinitions();
        getSFLCTLFeedbackBean().initializeSFLMDT(iterator);
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 1999, 2002.  All Rights Reserved.";
    private static ResourceBundle _resmri;
    private int _currentSubfileIndex;
    private int _dynamicVisibleRecordSize;
    private int _rowsOccupiedBySubfileRecord[];
    private boolean _isSubfileFormatted;
    private Vector _SFLMSGs;
    private HashSet _subfileFieldsNotVisible[];

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
