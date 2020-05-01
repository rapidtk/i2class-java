// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifierStrings;
import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.*;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.help.AppHelpTable;
import com.ibm.as400ad.webfacing.runtime.help.HelpArea;
import com.ibm.as400ad.webfacing.runtime.model.*;
import com.ibm.as400ad.webfacing.runtime.model.def.*;
import com.ibm.as400ad.webfacing.runtime.view.def.AIDKey;
import com.ibm.as400ad.webfacing.runtime.view.def.AIDKeyResponseIndicator;
import com.ibm.as400ad.webfacing.runtime.view.def.ERRMSGMessageDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordFeedbackDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordViewDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.MSGMessageDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.ResponseIndicator;
import com.ibm.as400ad.webfacing.runtime.view.def.SubfileControlRecordFeedbackDefinition;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            RecordsOnDevice, RecordViewBean, SubfileControlRecordViewBean, CursorReturnData, 
//            IFieldMessageDefinition, LocationOnDevice, SubfileControlRecordFeedbackBean, CommandKeyLabelList, 
//            IClientAIDKey, PresentationLayer, CursorPosition, IRemoveRecord, 
//            RecordFeedbackBean, ConceptualLayer, VisibleRectangle, AIDKeyDictionary, 
//            ICursorBufferData, IBuildRecordViewBean

class ActiveRecordTable
    implements IRemoveRecord, ENUM_KeywordIdentifiers, ENUM_KeywordIdentifierStrings
{

    public ActiveRecordTable(IDSPFObject idspfobject, AppHelpTable apphelptable)
    {
        _records = new ElementContainer();
        _recordsOnDevice = null;
        _isERRMSGForFocusValid = false;
        _ERRMSGForFocus = null;
        _recordBeingRead = null;
        _AIDKey = "";
        _commandKeysFeedbackDef = null;
        _functionKeysFeedbackDef = null;
        _activeCommandKeys = new ArrayList();
        _activeFunctionKeys = new ArrayList();
        _fnKeyListNeedsUpdating = true;
        _foldKey = null;
        _foldKeyRVB = null;
        _indicatorArea = null;
        _cursor = null;
        _currentLOD = null;
        _firstWrite = true;
        _isSecondReadFromRecord = false;
        _subfileFoldOpDoneLast = false;
        _subfilePageOpDoneLast = false;
        _SFLLowestRRN = 0;
        _SFLRecordsProtected = new HashMap();
        _helpTable = null;
        _isHLPRTNActive = false;
        _cursorReturnData = null;
        _recordsOnDevice = new RecordsOnDevice(this);
        _DSPFObject = idspfobject;
        _helpTable = apphelptable;
    }

    public ActiveRecordTable(RecordsOnDevice recordsondevice, IDSPFObject idspfobject, AppHelpTable apphelptable)
    {
        _records = new ElementContainer();
        _recordsOnDevice = null;
        _isERRMSGForFocusValid = false;
        _ERRMSGForFocus = null;
        _recordBeingRead = null;
        _AIDKey = "";
        _commandKeysFeedbackDef = null;
        _functionKeysFeedbackDef = null;
        _activeCommandKeys = new ArrayList();
        _activeFunctionKeys = new ArrayList();
        _fnKeyListNeedsUpdating = true;
        _foldKey = null;
        _foldKeyRVB = null;
        _indicatorArea = null;
        _cursor = null;
        _currentLOD = null;
        _firstWrite = true;
        _isSecondReadFromRecord = false;
        _subfileFoldOpDoneLast = false;
        _subfilePageOpDoneLast = false;
        _SFLLowestRRN = 0;
        _SFLRecordsProtected = new HashMap();
        _helpTable = null;
        _isHLPRTNActive = false;
        _cursorReturnData = null;
        _recordsOnDevice = recordsondevice;
        _DSPFObject = idspfobject;
        _recordsOnDevice.setCurrentART(this, true);
        _helpTable = apphelptable;
    }

    public RecordViewBean add(RecordViewBean recordviewbean, boolean flag, boolean flag1)
        throws WebfacingInternalException, WebfacingLevelCheckException, WFApplicationRuntimeError
    {
        recordviewbean.setPreviousViewBean(getRecordViewBean(recordviewbean.getRecordName()), this);
        _writeOpDoneLast = true;
        _subfileFoldOpDoneLast = false;
        _subfilePageOpDoneLast = false;
        _isERRMSGForFocusValid = false;
        if(recordviewbean.isDisplayable())
        {
            recordviewbean.checkForRuntimeErrors();
            boolean flag2 = changeDisplayModeIfRequired(recordviewbean);
            OptionIndicators optionindicators = (OptionIndicators)recordviewbean.getFeedbackBean().getRecordData().getOptionIndEval();
            optionindicators = (OptionIndicators)optionindicators.clone();
            if(!flag2 && recordviewbean.isInRecordUpdateState())
            {
                IRecordData irecorddata = recordviewbean.getFeedbackBean().getRecordData();
                recordviewbean = recordviewbean.getPreviousViewBean();
                recordviewbean.setPropertiesForUpdateMode();
                if(recordviewbean.isWindowed())
                    _recordsOnDevice.popWindowsAfter(recordviewbean);
                recordviewbean.getFeedbackBean().assignNewRecordData(irecorddata);
            } else
            {
                if(!flag2 && isClearScreenRequired(recordviewbean))
                    clearScreen(recordviewbean);
                if(isClearHelpRequired(recordviewbean))
                    clearHelp(!recordviewbean.isKeywordActive(110L));
                recordviewbean.applyCursorPosition(_cursor);
                remove(recordviewbean.getPreviousViewBean());
                boolean flag3 = _recordsOnDevice.add(recordviewbean);
                _records.add(recordviewbean, com.ibm.as400ad.webfacing.runtime.view.RecordViewBean.class);
                clearMDTsIfNeeded(recordviewbean, flag3);
            }
            addHelpDefinitions(recordviewbean);
            if(!flag)
                protectForImmediateWrite();
            updateActiveCommandKeys(recordviewbean, optionindicators, flag1);
            maintainERRMSGs(recordviewbean);
            _firstWrite = false;
            setIsHLPRTNActive(recordviewbean);
        } else
        {
            recordviewbean = null;
        }
        updateSFLLowestRRNForAdd(recordviewbean);
        return recordviewbean;
    }

    private void addHelpDefinitions(RecordViewBean recordviewbean)
    {
        if(recordviewbean.isCLRL() && (recordviewbean.isCLRL_END() || recordviewbean.isCLRL_NN()))
        {
            int i = recordviewbean.getFirstFieldLine();
            int j = i + (recordviewbean.isCLRL_END() ? recordviewbean.getCLRL_END() : recordviewbean.getCLRL_NN());
            _helpTable.removeHelp(i, j);
        }
        String s = recordviewbean.getFirstActiveHLPTITLEtext();
        _helpTable.setHelpTitle(s);
        Iterator iterator = recordviewbean.getHelpDefinitions();
        ArrayList arraylist = new ArrayList();
        while(iterator.hasNext()) 
        {
            HelpArea helparea = (HelpArea)iterator.next();
            com.ibm.as400ad.webfacing.runtime.help.HelpDefinition helpdefinition = helparea.getHelpDefinition();
            if(helpdefinition.hasIndicatorExpression() && recordviewbean.evaluateIndicatorExpression(helpdefinition.getIndicatorExpression()) || !helpdefinition.hasIndicatorExpression())
            {
                com.ibm.as400ad.webfacing.runtime.help.HelpKeyword helpkeyword = helparea.getHelpBoundary();
                if(helpkeyword != null && (helpkeyword.hasIndicatorExpression() && recordviewbean.evaluateIndicatorExpression(helpkeyword.getIndicatorExpression()) || !helpkeyword.hasIndicatorExpression()))
                    helparea.setIsBoundary(true);
                com.ibm.as400ad.webfacing.runtime.help.HelpKeyword helpkeyword1 = helparea.getHelpExcluded();
                if(helpkeyword1 != null && (helpkeyword1.hasIndicatorExpression() && recordviewbean.evaluateIndicatorExpression(helpkeyword1.getIndicatorExpression()) || !helpkeyword1.hasIndicatorExpression()))
                    helparea.setIsExcluded(true);
                arraylist.add(helparea);
            }
        }
        _helpTable.addHelpList(recordviewbean.getRecordName(), arraylist);
    }

    private boolean changeDisplayModeIfRequired(RecordViewBean recordviewbean)
        throws WFApplicationRuntimeError
    {
        boolean flag = false;
        if(!_recordsOnDevice.isEmpty())
            if(!_recordsOnDevice.isCurrentDisplayWide() && recordviewbean.getPrimaryFileDisplaySize().intValue() == 1 && recordviewbean.getSecondaryFileDisplaySize() != null && !recordviewbean.isWide() && _recordsOnDevice.isASSUMEPending())
                flag = true;
            else
                flag = _recordsOnDevice.isCurrentDisplayWide() != recordviewbean.isWide();
        if(flag)
            clearScreen(recordviewbean);
        _recordsOnDevice.setIsCurrentDisplayWide(recordviewbean.isWide());
        return flag;
    }

    private void clearMDTsIfNeeded(RecordViewBean recordviewbean, boolean flag)
    {
        boolean flag1 = false;
        boolean flag2 = false;
        if(flag)
        {
            flag1 = true;
            flag2 = true;
        } else
        if(recordviewbean.isKeywordActive(137L))
        {
            flag1 = true;
            String s = recordviewbean.getRecordViewDefinition().getFirstParmOnKeyword(137L);
            if(s != null && s.equals(ENUM_KeywordIdentifierStrings.TOKEN_STRINGS[303]))
                flag2 = true;
        }
        if(flag1)
        {
            for(Iterator iterator = getReadableRecords(); iterator.hasNext();)
            {
                RecordViewBean recordviewbean1 = (RecordViewBean)iterator.next();
                if(recordviewbean1 != recordviewbean)
                    recordviewbean1.clearMDTs(flag2);
            }

        }
    }

    void clearScreen()
    {
        clearScreen(true, true, true);
    }

    void clearScreen(RecordViewBean recordviewbean)
    {
        if(recordviewbean != null)
            clearScreen(!recordviewbean.isCmdKeyRetained(), !recordviewbean.isFunKeyRetained(), !recordviewbean.isCLRL_ALL() || !recordviewbean.isOutputOnly());
        else
            clearScreen(true, true, true);
    }

    void clearScreen(boolean flag, boolean flag1, boolean flag2)
    {
        if(flag2)
        {
            _records.removeAllElements();
        } else
        {
            RecordViewBean recordviewbean;
            for(Iterator iterator = getReadableRecords(); iterator.hasNext(); recordviewbean.setConceptualLayerZOrder(-1))
                recordviewbean = (RecordViewBean)iterator.next();

        }
        _recordsOnDevice.clear();
        _SFLRecordsProtected.clear();
        if(flag)
            _activeCommandKeys.clear();
        if(flag1)
            _activeFunctionKeys.clear();
        _recordBeingRead = null;
    }

    public Iterator getActiveKeys()
    {
        Vector vector = new Vector();
        updateActiveFnKeysForROLLKeys();
        vector.add(new AIDKey("ENTER", _resmri.getString("Enter")));
        vector.addAll(_activeCommandKeys);
        vector.addAll(_activeFunctionKeys);
        return vector.iterator();
    }

    public Iterator getActiveKeysWithoutEnter()
    {
        Vector vector = new Vector();
        updateActiveFnKeysForROLLKeys();
        vector.addAll(_activeCommandKeys);
        vector.addAll(_activeFunctionKeys);
        return vector.iterator();
    }

    protected String[] getRecordsWithPageUPDNKey()
    {
        String s = "";
        String s1 = "";
        for(Iterator iterator = _recordsOnDevice.getTopConceptualLayer().getRecordList().iterator(); iterator.hasNext();)
        {
            RecordViewBean recordviewbean = (RecordViewBean)iterator.next();
            boolean aflag[] = recordviewbean.getPageUpDownKeysStatus();
            boolean flag = aflag[0];
            boolean flag1 = aflag[1];
            if(flag)
                s = s + (s.length() != 0 ? ",\"" + recordviewbean.getRecordName() + "\"" : "\"" + recordviewbean.getRecordName() + "\"");
            if(flag1)
                s1 = s1 + (s1.length() != 0 ? ",\"" + recordviewbean.getRecordName() + "\"" : "\"" + recordviewbean.getRecordName() + "\"");
        }

        String as[] = {
            s, s1
        };
        return as;
    }

    public String getAIDKey()
    {
        return _AIDKey;
    }

    public Vector getChangedSubfileControlRecords()
    {
        Vector vector = new Vector();
        Object obj = null;
        for(Iterator iterator = getReadableRecords(); iterator.hasNext();)
        {
            RecordViewBean recordviewbean = (RecordViewBean)iterator.next();
            if((recordviewbean instanceof SubfileControlRecordViewBean) && !((SubfileControlRecordViewBean)recordviewbean).isSubfileInactive() && isRecordOnTopLayer(recordviewbean))
            {
                IRecordData irecorddata = recordviewbean.getFeedbackBean().getRecordData();
                ISFLCTLInputBufferSaveArea isflctlinputbuffersavearea = (ISFLCTLInputBufferSaveArea)irecorddata;
                if(isflctlinputbuffersavearea.getChangedRecords() != null && isflctlinputbuffersavearea.getChangedRecords().size() > 0)
                    vector.add(irecorddata);
            }
        }

        if(vector.size() > 0)
            return vector;
        else
            return null;
    }

    public CursorPosition getCursor()
    {
        return _cursor;
    }

    private CursorReturnData getCursorReturnData()
    {
        if(_cursorReturnData == null)
            _cursorReturnData = new CursorReturnData(this);
        return _cursorReturnData;
    }

    public ICursorBufferData getCursorBufferData()
    {
        return getCursorReturnData();
    }

    private LocationOnDevice getDspatrPC_LocationInSFLRecords()
    {
        Iterator iterator = _recordsOnDevice.getTopConceptualLayer().getRecordList().iterator();
        for(Iterator iterator1 = WebfacingConstants.reverseIterator(iterator); iterator1.hasNext();)
        {
            RecordViewBean recordviewbean = (RecordViewBean)iterator1.next();
            if((recordviewbean instanceof SubfileControlRecordViewBean) && isRecordOnTopLayer(recordviewbean))
            {
                LocationOnDevice locationondevice = ((SubfileControlRecordViewBean)recordviewbean).getDspatrPC_LocationForSFLRecord();
                if(locationondevice.getField() != null && recordviewbean.getPresentationLayer() != null)
                    return locationondevice;
            }
        }

        return null;
    }

    private LocationOnDevice getDspatrPC_LocationIn(RecordViewBean recordviewbean)
    {
        LocationOnDevice locationondevice = recordviewbean.getDspatrPC_Location();
        if(locationondevice.getField() != null && !recordviewbean.isProtected() && recordviewbean.getPresentationLayer() != null)
            return locationondevice;
        else
            return null;
    }

    public IDSPFObject getDSPFObject()
    {
        return _DSPFObject;
    }

    private ERRMSGMessageDefinition getERRMSGForFocus()
    {
        if(!_isERRMSGForFocusValid)
        {
            ERRMSGMessageDefinition errmsgmessagedefinition = null;
            Iterator iterator = WebfacingConstants.reverseIterator(getReadableRecords());
            while(iterator.hasNext()) 
            {
                RecordViewBean recordviewbean = (RecordViewBean)iterator.next();
                if(!recordviewbean.isInERRMSGState())
                    continue;
                errmsgmessagedefinition = recordviewbean.getFirstActiveERRMSG(true);
                if(errmsgmessagedefinition == null)
                    continue;
                errmsgmessagedefinition.setRecordLayer(recordviewbean.getDisplayZIndex());
                break;
            }
            _isERRMSGForFocusValid = true;
            _ERRMSGForFocus = errmsgmessagedefinition;
        }
        return _ERRMSGForFocus;
    }

    public IIndicatorArea getIndicatorArea()
    {
        return _indicatorArea;
    }

    private RecordViewBean getLastWrittenRecord()
    {
        Iterator iterator = getReadableRecords();
        RecordViewBean recordviewbean;
        for(recordviewbean = null; iterator.hasNext(); recordviewbean = (RecordViewBean)iterator.next());
        return recordviewbean;
    }

    public Iterator[] getMessagesAndIDs()
    {
        Vector vector = new Vector();
        Vector vector1 = new Vector();
        for(Iterator iterator = getReadableRecords(); iterator.hasNext();)
        {
            RecordViewBean recordviewbean = (RecordViewBean)iterator.next();
            for(Iterator iterator1 = recordviewbean.getMessages(); iterator1.hasNext();)
            {
                MSGMessageDefinition msgmessagedefinition = (MSGMessageDefinition)iterator1.next();
                vector.add(msgmessagedefinition);
                if(msgmessagedefinition instanceof IFieldMessageDefinition)
                    try
                    {
                        vector1.add((new LocationOnDevice(recordviewbean.getDisplayZIndex(), recordviewbean.getRecordName(), ((IFieldMessageDefinition)msgmessagedefinition).getFieldName())).getTagID());
                    }
                    catch(Throwable throwable)
                    {
                        vector1.add(null);
                    }
                else
                    vector1.add(null);
            }

        }

        return (new Iterator[] {
            vector.iterator(), vector1.iterator()
        });
    }

    Iterator getReadableRecords()
    {
        return _records.getList(com.ibm.as400ad.webfacing.runtime.view.RecordViewBean.class).iterator();
    }

    private RecordViewBean getReadRecord()
    {
        Iterator iterator = getReadableRecords();
        RecordViewBean recordviewbean = null;
        RecordViewBean recordviewbean1 = null;
        while(iterator.hasNext()) 
        {
            recordviewbean = (RecordViewBean)iterator.next();
            if(!recordviewbean.isOutputOnly())
                recordviewbean1 = recordviewbean;
        }
        if(recordviewbean1 == null)
            recordviewbean1 = recordviewbean;
        return recordviewbean1;
    }

    private RecordViewBean getRecordBeingRead()
    {
        return getRecordViewBean(_recordBeingRead);
    }

    private IRecordData getRecordDataBean(String s)
    {
        return getRecordFeedbackBean(s).getRecordData();
    }

    private IRecordData getRecordDataBeanBeingRead()
        throws WebfacingInternalException
    {
        return getRecordDataBean(_recordBeingRead);
    }

    private RecordFeedbackBean getRecordFeedbackBean(String s)
    {
        return getRecordViewBean(s).getFeedbackBean();
    }

    private RecordFeedbackBean getRecordFeedbackBeanBeingRead()
    {
        return getRecordFeedbackBean(_recordBeingRead);
    }

    RecordsOnDevice getRecordsOnDevice()
    {
        return _recordsOnDevice;
    }

    RecordViewBean getRecordViewBean(String s)
    {
        return (RecordViewBean)_records.get(s, com.ibm.as400ad.webfacing.runtime.view.RecordViewBean.class);
    }

    RecordViewBean getRecordViewBeanBeingRead()
    {
        return getRecordViewBean(_recordBeingRead);
    }

    private RecordViewBean getSecondLastWrittenRecord()
    {
        Iterator iterator = getReadableRecords();
        RecordViewBean recordviewbean = null;
        RecordViewBean recordviewbean1 = null;
        while(iterator.hasNext()) 
        {
            recordviewbean1 = recordviewbean;
            recordviewbean = (RecordViewBean)iterator.next();
        }
        return recordviewbean1;
    }

    int getSFLLowestRRN()
    {
        return _SFLLowestRRN;
    }

    private boolean isClearHelpRequired(RecordViewBean recordviewbean)
    {
        return isClearScreenRequired(recordviewbean) || _firstWrite && recordviewbean.isKeywordSpecified(9L) || recordviewbean.isKeywordActive(110L);
    }

    private boolean isClearScreenRequired(RecordViewBean recordviewbean)
    {
        if(_firstWrite && !recordviewbean.isKeywordSpecified(9L))
            return true;
        return recordviewbean.isCLRL_ALL() || !recordviewbean.isKeywordActive(152L) && !recordviewbean.isWindowed() && (!recordviewbean.isCLRL() || recordviewbean.isCLRL_ALL());
    }

    public boolean isCurrentDisplayWide()
    {
        return getRecordsOnDevice().isCurrentDisplayWide();
    }

    public boolean isHLPRTNActive()
    {
        return _isHLPRTNActive;
    }

    public boolean isNoResponseIndOnAID()
    {
        return _noResponseIndOnAID;
    }

    private boolean isRecordActive(RecordViewBean recordviewbean)
    {
        return getRecordViewBean(recordviewbean.getRecordName()) == recordviewbean && isRecordOnTopLayer(recordviewbean);
    }

    boolean isRecordOnTopLayer(RecordViewBean recordviewbean)
    {
        return recordviewbean.getConceptualLayerZOrder() == _recordsOnDevice.getTopConceptualLayerIndex();
    }

    private void maintainERRMSGs(RecordViewBean recordviewbean)
    {
        if((!recordviewbean.isInRecordUpdateState() || !recordviewbean.isInERRMSGState()) && recordviewbean.getPreviousViewBean() != null && !recordviewbean.isInERRMSGState())
        {
            RecordViewBean recordviewbean1;
            for(Iterator iterator = getReadableRecords(); iterator.hasNext(); recordviewbean1.clearAllERRMSGs())
                recordviewbean1 = (RecordViewBean)iterator.next();

        }
    }

    public IInputBufferSaveArea mapRecordBeingRead()
        throws WebfacingInternalException
    {
        IRecordData irecorddata = getRecordDataBeanBeingRead();
        irecorddata.setHasBeenMapped(true);
        return irecorddata.getInputBufferSaveArea();
    }

    private void mergeRecordRIsIntoSIA(String s, RecordFeedbackBean recordfeedbackbean)
    {
        if(recordfeedbackbean.getRecordDataDefinition().hasSeparateIndicatorArea())
        {
            IIndicatorArea iindicatorarea = getIndicatorArea();
            iindicatorarea.clearResponseIndicators(_commandKeysFeedbackDef.getCommandKeyRespInds());
            iindicatorarea.clearResponseIndicators(_functionKeysFeedbackDef.getNonCommandAIDKeyRespInds());
            iindicatorarea.mergeReferencedRIs(recordfeedbackbean.getResponseIndData());
            setRIForRetainedKeys(s, _commandKeysFeedbackDef, _functionKeysFeedbackDef, iindicatorarea);
        }
    }

    void postProcessDeviceRead()
    {
        RecordViewBean recordviewbean;
        for(Iterator iterator = getReadableRecords(); iterator.hasNext(); recordviewbean.resetRecordUpdateState())
            recordviewbean = (RecordViewBean)iterator.next();

    }

    protected void prepareForRead()
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        _subfilePageOpDoneLast = false;
        RecordViewBean recordviewbean;
        for(Iterator iterator = getReadableRecords(); iterator.hasNext(); recordviewbean.getFeedbackBean().prepareForRead())
            recordviewbean = (RecordViewBean)iterator.next();

    }

    private void processCursorForRead()
        throws WebfacingInternalException, WebfacingLevelCheckException
    {
        RecordFeedbackBean recordfeedbackbean = getRecordFeedbackBean(_recordBeingRead);
        if(recordfeedbackbean != null)
            recordfeedbackbean.setCursor(getCursorReturnData());
        for(Iterator iterator = getReadableRecords(); iterator.hasNext();)
        {
            RecordViewBean recordviewbean = (RecordViewBean)iterator.next();
            RecordFeedbackBean recordfeedbackbean1 = recordviewbean.getFeedbackBean();
            if(recordfeedbackbean1 instanceof SubfileControlRecordFeedbackBean)
                ((SubfileControlRecordFeedbackBean)recordfeedbackbean1).setSFLCSRRRN(getCursorReturnData());
        }

    }

    private boolean processSFLFOLD(String s)
    {
        boolean flag = false;
        int i = 28;
        SubfileControlRecordFeedbackBean subfilecontrolrecordfeedbackbean = null;
        Iterator iterator = _recordsOnDevice.getTopConceptualLayer().getRecordList().iterator();
        while(iterator.hasNext()) 
        {
            RecordViewBean recordviewbean = (RecordViewBean)iterator.next();
            if(!(recordviewbean instanceof SubfileControlRecordViewBean))
                continue;
            SubfileControlRecordFeedbackBean subfilecontrolrecordfeedbackbean1 = (SubfileControlRecordFeedbackBean)recordviewbean.getFeedbackBean();
            if(!subfilecontrolrecordfeedbackbean1.shouldFoldStateChange(s))
                continue;
            if(subfilecontrolrecordfeedbackbean1.getSubfileDataDefinition().getName().equals(getCursorReturnData().getRecordName()))
            {
                subfilecontrolrecordfeedbackbean1.toggleSubfileFolded();
                flag = true;
                break;
            }
            if(recordviewbean.getFirstFieldLine() < i)
            {
                i = recordviewbean.getFirstFieldLine();
                subfilecontrolrecordfeedbackbean = subfilecontrolrecordfeedbackbean1;
            }
        }
        if(!flag && subfilecontrolrecordfeedbackbean != null)
        {
            subfilecontrolrecordfeedbackbean.toggleSubfileFolded();
            flag = true;
        }
        _subfileFoldOpDoneLast = flag;
        return flag;
    }

    public void protectForImmediateWrite()
    {
        RecordViewBean recordviewbean;
        for(Iterator iterator = getReadableRecords(); iterator.hasNext(); recordviewbean.setPROTECTForImmediateWrite())
            recordviewbean = (RecordViewBean)iterator.next();

    }

    public boolean read(String s, boolean flag, IIndicatorArea iindicatorarea)
        throws WebfacingInternalException, WebfacingLevelCheckException
    {
        _isERRMSGForFocusValid = false;
        _indicatorArea = iindicatorarea;
        RecordViewBean recordviewbean = null;
        _isSecondReadFromRecord = false;
        if(s.equals(""))
        {
            recordviewbean = getReadRecord();
            _recordBeingRead = recordviewbean.getRecordName();
        } else
        {
            _recordBeingRead = s;
            recordviewbean = getRecordBeingRead();
        }
        boolean flag1;
        if(recordviewbean == null)
        {
            flag1 = true;
        } else
        {
            if(_writeOpDoneLast)
                flag1 = true;
            else
            if(recordviewbean.needsToBeDisplayed())
            {
                if(recordviewbean.isKeywordSpecified(175L))
                {
                    flag1 = false;
                } else
                {
                    flag1 = true;
                    _isSecondReadFromRecord = true;
                }
            } else
            {
                flag1 = false;
            }
            recordviewbean.setNeedsToBeDisplayed(true);
            if(!flag1)
            {
                setAIDKey(_AIDKey);
                processCursorForRead();
            }
            if(!flag || _DSPFObject.isRSTDSP())
            {
                _recordsOnDevice.setActiveRecordForReadOrWrite(recordviewbean, false);
                if(flag1)
                {
                    for(Iterator iterator = getReadableRecords(); iterator.hasNext();)
                    {
                        RecordViewBean recordviewbean1 = (RecordViewBean)iterator.next();
                        if(isRecordOnTopLayer(recordviewbean1) && !recordviewbean.getRecordName().equals(recordviewbean1.getRecordName()))
                            recordviewbean1.setNeedsToBeDisplayed(false);
                    }

                }
            }
        }
        _writeOpDoneLast = false;
        return flag1;
    }

    public void remove(RecordViewBean recordviewbean)
    {
        if(null != recordviewbean)
            _records.remove(recordviewbean, com.ibm.as400ad.webfacing.runtime.view.RecordViewBean.class);
    }

    public void removeProtectedRecord(RecordViewBean recordviewbean)
    {
        remove(recordviewbean);
        if(recordviewbean instanceof SubfileControlRecordViewBean)
            _SFLRecordsProtected.put(recordviewbean.getRecordName(), recordviewbean);
    }

    void restore(boolean flag)
    {
        if(_DSPFObject.isRSTDSP() || !flag)
        {
            for(Iterator iterator = getReadableRecords(); iterator.hasNext(); ((RecordViewBean)iterator.next()).setDSPFActive(true));
        } else
        {
            clearScreen();
        }
    }

    public void setAIDKey(String s)
        throws WebfacingInternalException, WebfacingLevelCheckException
    {
        RecordFeedbackBean recordfeedbackbean = getRecordFeedbackBeanBeingRead();
        _noResponseIndOnAID = recordfeedbackbean.setAIDKey(s);
        mergeRecordRIsIntoSIA(s, recordfeedbackbean);
    }

    public boolean setAIDKeyOnDeviceRequest(String s)
        throws WebfacingInternalException, WebfacingLevelCheckException
    {
        processCursorForRead();
        if(processSFLFOLD(s))
        {
            _isSecondReadFromRecord = false;
            return true;
        }
        RecordFeedbackBean recordfeedbackbean = getRecordFeedbackBeanBeingRead();
        AIDKey aidkey = new AIDKey(s);
        if(aidkey.isFunctionKey())
            storeOnScreenValues();
        setAIDKey(s);
        _AIDKey = s;
        return false;
    }

    public void setCursor(CursorPosition cursorposition)
    {
        _cursor = cursorposition;
        String s = cursorposition.getTagId();
        if(s != null)
        {
            _currentLOD = getLocationOnDeviceAt(cursorposition);
            _cursor.setRow(_currentLOD.getCursorPosition().getRow());
            _cursor.setColumn(_currentLOD.getCursorPosition().getColumn());
        } else
        {
            _currentLOD = new LocationOnDevice(cursorposition);
        }
        _cursorReturnData = null;
    }

    void setCursorToPrevLoc(CursorPosition cursorposition)
    {
        _cursor = cursorposition;
    }

    public void setFieldValue(IFieldValue ifieldvalue)
    {
        getRecordFeedbackBean(ifieldvalue.record()).setOnScreenFieldValue(ifieldvalue);
    }

    public void setIsHLPRTNActive(RecordViewBean recordviewbean)
    {
        if(recordviewbean.isKeywordActive(118L))
            _isHLPRTNActive = true;
        else
            _isHLPRTNActive = false;
    }

    public void setRecordsOnDeviceForRSTDSP_No(RecordsOnDevice recordsondevice)
    {
        _recordsOnDevice = recordsondevice;
        _recordsOnDevice.setCurrentART(this, false);
    }

    public boolean setRelativeRecordNumber(String s, int i)
    {
        boolean flag = false;
        SubfileControlRecordViewBean subfilecontrolrecordviewbean = (SubfileControlRecordViewBean)getRecordViewBean(s);
        if(subfilecontrolrecordviewbean == null)
        {
            subfilecontrolrecordviewbean = (SubfileControlRecordViewBean)_SFLRecordsProtected.get(s);
            flag = true;
        }
        subfilecontrolrecordviewbean.clearMessages();
        _subfileFoldOpDoneLast = false;
        _subfilePageOpDoneLast = true;
        _isSecondReadFromRecord = false;
        if(getRecordsOnDevice().isControlWithFirstDisplayedSubfile(subfilecontrolrecordviewbean))
            _SFLLowestRRN = i;
        SubfileControlRecordFeedbackBean subfilecontrolrecordfeedbackbean = (SubfileControlRecordFeedbackBean)subfilecontrolrecordviewbean.getFeedbackBean();
        if(subfilecontrolrecordfeedbackbean.isSubfileComplete())
        {
            subfilecontrolrecordfeedbackbean.setRRN(i);
            return true;
        }
        if(!flag)
            subfilecontrolrecordfeedbackbean.storeSubfileOnScreenValues();
        if(subfilecontrolrecordfeedbackbean.getRRN() < i)
            _AIDKey = "ROLLUP";
        else
            _AIDKey = "ROLLDOWN";
        return false;
    }

    private void setRIForRetainedKeys(String s, IRecordFeedbackDefinition irecordfeedbackdefinition, IRecordFeedbackDefinition irecordfeedbackdefinition1, IIndicatorArea iindicatorarea)
    {
        AIDKeyResponseIndicator aidkeyresponseindicator = null;
        if(AIDKeyDictionary.isCommandKey(s))
            aidkeyresponseindicator = (AIDKeyResponseIndicator)irecordfeedbackdefinition.get(new Key(s), com.ibm.as400ad.webfacing.runtime.view.def.AIDKeyResponseIndicator.class);
        else
        if(AIDKeyDictionary.isNonCommandAID(s))
            aidkeyresponseindicator = (AIDKeyResponseIndicator)irecordfeedbackdefinition1.get(new Key(s), com.ibm.as400ad.webfacing.runtime.view.def.AIDKeyResponseIndicator.class);
        if(null != aidkeyresponseindicator)
            iindicatorarea.setIndicator(aidkeyresponseindicator.getIndex(), true);
    }

    private void storeOnScreenValues()
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        RecordViewBean recordviewbean;
        for(Iterator iterator = getReadableRecords(); iterator.hasNext(); recordviewbean.getFeedbackBean().storeOnScreenValues())
            recordviewbean = (RecordViewBean)iterator.next();

    }

    void suspend()
    {
        for(Iterator iterator = getReadableRecords(); iterator.hasNext(); ((RecordViewBean)iterator.next()).setDSPFActive(false));
    }

    public void unProtectForImmediateWrite()
    {
        RecordViewBean recordviewbean;
        for(Iterator iterator = getReadableRecords(); iterator.hasNext(); recordviewbean.removePROTECTForImmediateWrite())
            recordviewbean = (RecordViewBean)iterator.next();

    }

    private void updateSFLLowestRRNForAdd(RecordViewBean recordviewbean)
    {
        _SFLLowestRRN = 0;
        if(recordviewbean instanceof SubfileControlRecordViewBean)
        {
            SubfileControlRecordViewBean subfilecontrolrecordviewbean = (SubfileControlRecordViewBean)recordviewbean;
            if(subfilecontrolrecordviewbean.isSubfileVisible() && getRecordsOnDevice().isControlWithFirstDisplayedSubfile(subfilecontrolrecordviewbean))
                _SFLLowestRRN = subfilecontrolrecordviewbean.getRRN();
        }
    }

    private void clearHelp(boolean flag)
    {
        _helpTable.clear(flag);
    }

    private void updateActiveCommandKeys(RecordViewBean recordviewbean, OptionIndicators optionindicators, boolean flag)
        throws WebfacingInternalException
    {
        recordviewbean.initActiveCommandAndFunctionKeys(optionindicators);
        if(!recordviewbean.isCmdKeyRetained())
        {
            _activeCommandKeys = recordviewbean.getActiveCommandKeys();
            _commandKeysFeedbackDef = recordviewbean.getFeedbackBean().getRecordFeedbackDefinition();
            updateActiveCmdKeysForFoldKey(recordviewbean);
        }
        if(!recordviewbean.isFunKeyRetained())
        {
            _activeFunctionKeys = recordviewbean.getActiveFunctionKeys();
            _functionKeysFeedbackDef = recordviewbean.getFeedbackBean().getRecordFeedbackDefinition();
            _fnKeyListNeedsUpdating = true;
        }
        CommandKeyLabelList commandkeylabellist = new CommandKeyLabelList();
        commandkeylabellist.resolveDynamicKeyLabelsAndMergeList(recordviewbean.getCommandKeyLabels(), recordviewbean);
        commandkeylabellist.resolveConditionAndMergeList(recordviewbean.getVisibilityConditionedCommandKeyLabels(), recordviewbean, flag);
        commandkeylabellist.resolveConditionAndMergeList(recordviewbean.getIndicatorConditionedCommandKeyLabels(), recordviewbean, flag);
        commandkeylabellist.updateCommandKeyLabels(_activeCommandKeys.iterator(), true);
        _recordsOnDevice.getPotentialKeyLabelList(recordviewbean).updateCommandKeyLabels(_activeCommandKeys.iterator(), false);
        _recordsOnDevice.updatePotentialKeyLabelList(recordviewbean, commandkeylabellist);
    }

    private boolean isInTheKeyList(Iterator iterator, String s)
    {
        while(iterator.hasNext()) 
        {
            IClientAIDKey iclientaidkey = (IClientAIDKey)iterator.next();
            if(iclientaidkey.getKeyName().equals(s))
                return true;
        }
        return false;
    }

    public LocationOnDevice getLocationOnDeviceAt(CursorPosition cursorposition)
    {
        String s = cursorposition.getTagId();
        if(s != null)
        {
            try
            {
                int i = cursorposition.getColumnOffset();
                int j = s.indexOf("_");
                int k = Integer.parseInt(s.substring(1, j));
                String s1 = s.substring(j + 1);
                StringTokenizer stringtokenizer = new StringTokenizer(s1, "$$");
                String s2 = stringtokenizer.nextToken();
                String s3 = stringtokenizer.nextToken();
                RecordViewBean recordviewbean = null;
                ArrayList arraylist = getRecordsOnDevice().getLayers();
                PresentationLayer presentationlayer = (PresentationLayer)arraylist.get(k - 1);
                for(Iterator iterator = presentationlayer.getRecords(); iterator.hasNext();)
                {
                    recordviewbean = (RecordViewBean)iterator.next();
                    if(recordviewbean.getRecordName().equals(s2))
                        break;
                    recordviewbean = null;
                }

                if(stringtokenizer.hasMoreTokens() && (recordviewbean instanceof SubfileControlRecordViewBean))
                {
                    int l = Integer.parseInt(stringtokenizer.nextToken());
                    return ((SubfileControlRecordViewBean)recordviewbean).getLocationOnDeviceAt(s3, i, l);
                } else
                {
                    return recordviewbean.getLocationOnDeviceAt(s3, i);
                }
            }
            catch(Exception exception)
            {
                WFSession.getTraceLogger().err(2, "tagId " + s + " is invalid.");
            }
            return new LocationOnDevice(new CursorPosition("num:1:1::"));
        } else
        {
            return getRecordsOnDevice().getLocationOnDeviceAt(cursorposition);
        }
    }

    public LocationOnDevice getCurrentLocOnDevice()
    {
        if(_currentLOD != null)
            return _currentLOD;
        else
            return getLocationOnDeviceAt(new CursorPosition("num:1:1::"));
    }

    public LocationOnDevice getNamedFieldAt(CursorPosition cursorposition)
    {
        LocationOnDevice locationondevice = getRecordsOnDevice().getTopConceptualLayer().getNamedFieldAt(cursorposition);
        if(locationondevice != null)
            return locationondevice;
        else
            return new LocationOnDevice(cursorposition);
    }

    public LocationOnDevice getFirstFocusCapableField()
    {
        return getRecordsOnDevice().getTopConceptualLayer().getFirstFocusCapableField();
    }

    public RecordViewBean[] getFirstRollEnabledRecords()
    {
        return getRecordsOnDevice().getTopConceptualLayer().getFirstRollEnabledRecords();
    }

    public IBuildRecordViewBean getRecordAt(CursorPosition cursorposition)
    {
        List list = _recordsOnDevice.getTopConceptualLayer().getRecordList();
        for(int i = 0; i < list.size(); i++)
        {
            RecordViewBean recordviewbean = (RecordViewBean)list.get(i);
            if(recordviewbean.containLocation(cursorposition.getRow(), cursorposition.getColumn()))
                return recordviewbean;
        }

        return null;
    }

    public LocationOnDevice calculateLocationForCursor()
    {
        if(_isSecondReadFromRecord)
            return getNamedFieldAt(getCursor());
        if(_subfileFoldOpDoneLast || _subfilePageOpDoneLast)
        {
            LocationOnDevice locationondevice = getDspatrPC_LocationInSFLRecords();
            if(locationondevice == null)
                return getNamedFieldAt(getCursor());
            else
                return locationondevice;
        }
        ERRMSGMessageDefinition errmsgmessagedefinition = getERRMSGForFocus();
        if(errmsgmessagedefinition != null)
            return errmsgmessagedefinition.getLocationOnDevice();
        for(Iterator iterator = WebfacingConstants.reverseIterator(getReadableRecords()); iterator.hasNext();)
        {
            RecordViewBean recordviewbean = (RecordViewBean)iterator.next();
            if(isRecordOnTopLayer(recordviewbean))
            {
                if(recordviewbean.isCSRLOCActiveAndValid())
                    return getNamedFieldAt(recordviewbean.getCRSLOC());
                LocationOnDevice locationondevice2 = getDspatrPC_LocationIn(recordviewbean);
                if(locationondevice2 != null)
                    return locationondevice2;
                if(recordviewbean instanceof SubfileControlRecordViewBean)
                {
                    LocationOnDevice locationondevice3 = ((SubfileControlRecordViewBean)recordviewbean).getSflrcdnbrCursor();
                    if(locationondevice3 != null)
                        return locationondevice3;
                }
            }
        }

        LocationOnDevice locationondevice1 = getFirstFocusCapableField();
        if(locationondevice1 != null)
            return locationondevice1;
        else
            return new LocationOnDevice(new CursorPosition(1, 1));
    }

    public void setSystemMenuOption(String s)
    {
        if(s != null)
        {
            RecordViewBean recordviewbean = getLastWrittenRecord();
            if(recordviewbean != null)
            {
                for(Iterator iterator = recordviewbean.getFeedbackBean().getRecordDataDefinition().getInputCapableFieldDefinitions().iterator(); iterator.hasNext();)
                {
                    FieldDataDefinition fielddatadefinition = (FieldDataDefinition)iterator.next();
                    String s1 = fielddatadefinition.getFieldName();
                    if(s1.equals("CMDLINE") || s1.equals("OPTLINE"))
                        recordviewbean.getFeedbackBean().updateFieldValue(s1, s.trim());
                }

            }
        }
    }

    public boolean isRecordActive(String s)
    {
        return _records.get(s, com.ibm.as400ad.webfacing.runtime.view.RecordViewBean.class) != null;
    }

    public SubfileControlRecordViewBean getSFLCTLRecordViewBeanForPaging(String s)
    {
        SubfileControlRecordViewBean subfilecontrolrecordviewbean = (SubfileControlRecordViewBean)getRecordViewBean(s);
        if(subfilecontrolrecordviewbean == null)
            subfilecontrolrecordviewbean = (SubfileControlRecordViewBean)_SFLRecordsProtected.get(s);
        return subfilecontrolrecordviewbean;
    }

    public boolean isSubfileChanged(String s)
    {
        SubfileControlRecordViewBean subfilecontrolrecordviewbean = (SubfileControlRecordViewBean)getRecordViewBean(s);
        if(subfilecontrolrecordviewbean != null)
        {
            IRecordData irecorddata = subfilecontrolrecordviewbean.getFeedbackBean().getRecordData();
            ISFLCTLInputBufferSaveArea isflctlinputbuffersavearea = (ISFLCTLInputBufferSaveArea)irecorddata;
            if(isflctlinputbuffersavearea.getChangedRecords() != null && isflctlinputbuffersavearea.getChangedRecords().size() > 0)
                return true;
        }
        return false;
    }

    private void updateActiveFnKeysForROLLKeys()
    {
        if(!_fnKeyListNeedsUpdating)
            return;
        boolean flag = false;
        boolean flag1 = false;
        for(Iterator iterator = _recordsOnDevice.getTopConceptualLayer().getRecordList().iterator(); iterator.hasNext();)
        {
            RecordViewBean recordviewbean = (RecordViewBean)iterator.next();
            boolean aflag[] = recordviewbean.getPageUpDownKeysStatus();
            flag = flag || aflag[0];
            flag1 = flag1 || aflag[1];
        }

        boolean flag2 = false;
        boolean flag3 = false;
        Iterator iterator1 = _activeFunctionKeys.iterator();
        if(flag || flag1)
            while(iterator1.hasNext()) 
            {
                AIDKey aidkey = (AIDKey)iterator1.next();
                if(aidkey.getKeyName().equals("PAGEUP"))
                    flag2 = true;
                else
                if(aidkey.getKeyName().equals("PAGEDOWN"))
                    flag3 = true;
            }
        if(flag && !flag2)
            _activeFunctionKeys.add(new AIDKey("PAGEUP", _resmri.getString("PAGEUP")));
        if(flag1 && !flag3)
            _activeFunctionKeys.add(new AIDKey("PAGEDOWN", _resmri.getString("PAGEDOWN")));
        _fnKeyListNeedsUpdating = false;
    }

    private void updateActiveCmdKeysForFoldKey(RecordViewBean recordviewbean)
    {
        if(recordviewbean instanceof SubfileControlRecordViewBean)
        {
            SubfileControlRecordFeedbackBean subfilecontrolrecordfeedbackbean = (SubfileControlRecordFeedbackBean)recordviewbean.getFeedbackBean();
            if(subfilecontrolrecordfeedbackbean.isSubfileFoldable())
            {
                SubfileControlRecordFeedbackDefinition subfilecontrolrecordfeedbackdefinition = subfilecontrolrecordfeedbackbean.getSFLCTLFeedbackDefinition();
                String s = subfilecontrolrecordfeedbackdefinition.getSubfileFoldKey();
                if(s != null)
                {
                    _foldKey = s;
                    _foldKeyRVB = recordviewbean;
                } else
                {
                    String s1 = subfilecontrolrecordfeedbackdefinition.getSubfileDropKey();
                    if(s1 != null)
                    {
                        _foldKey = s1;
                        _foldKeyRVB = recordviewbean;
                    }
                }
            }
        }
        List list = _recordsOnDevice.getTopConceptualLayer().getRecordList();
        if(_foldKeyRVB == null || !list.contains(_foldKeyRVB))
        {
            _foldKey = null;
            _foldKeyRVB = null;
        } else
        if(_foldKey != null && !isInTheKeyList(_activeCommandKeys.iterator(), _foldKey))
            _activeCommandKeys.add(new AIDKey(_foldKey, _foldKeyRVB.getLabelForCmdKey(_foldKey)));
    }

    private static ResourceBundle _resmri;
    private ElementContainer _records;
    private RecordsOnDevice _recordsOnDevice;
    transient boolean _isERRMSGForFocusValid;
    private transient ERRMSGMessageDefinition _ERRMSGForFocus;
    private String _recordBeingRead;
    private String _AIDKey;
    private IRecordFeedbackDefinition _commandKeysFeedbackDef;
    private IRecordFeedbackDefinition _functionKeysFeedbackDef;
    private ArrayList _activeCommandKeys;
    private ArrayList _activeFunctionKeys;
    private boolean _fnKeyListNeedsUpdating;
    private String _foldKey;
    private RecordViewBean _foldKeyRVB;
    private IDSPFObject _DSPFObject;
    private boolean _writeOpDoneLast;
    private IIndicatorArea _indicatorArea;
    private CursorPosition _cursor;
    private LocationOnDevice _currentLOD;
    private boolean _noResponseIndOnAID;
    private boolean _firstWrite;
    private boolean _isSecondReadFromRecord;
    private boolean _subfileFoldOpDoneLast;
    private boolean _subfilePageOpDoneLast;
    private int _SFLLowestRRN;
    private Map _SFLRecordsProtected;
    private AppHelpTable _helpTable;
    private boolean _isHLPRTNActive;
    private CursorReturnData _cursorReturnData;
    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2002");

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
        CollectionFactory.setPrototype(com.ibm.as400ad.webfacing.runtime.view.RecordViewBean.class, new LinkedList());
    }
}
