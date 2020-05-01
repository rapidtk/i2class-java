// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.runtime.controller.*;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.help.AppHelpTable;
import com.ibm.as400ad.webfacing.runtime.model.*;
import com.ibm.as400ad.webfacing.runtime.view.def.AIDKey;
import java.io.Serializable;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            ActiveRecordTable, RecordsOnDevice, IRecordOperations, IRecordSaveArea, 
//            IScreenBuilder, RecordViewBean, AIDKeyDictionary, CursorPosition, 
//            ICursorBufferData, LocationOnDevice, IBuildRecordViewBean, SubfileControlRecordViewBean

public class ScreenBuilderModel
    implements IRecordOperations, IRecordSaveArea, IScreenBuilder, ENUM_KeywordIdentifiers, Serializable
{

    public ScreenBuilderModel()
    {
        _activeRecordTables = new Hashtable();
        _currentART = null;
        _ARTBeingRead = null;
        _helpTable = null;
    }

    protected void addActiveRecordTable(IDSPFObject idspfobject, boolean flag)
    {
        idspfobject.setKEEP(flag);
        _currentART = new ActiveRecordTable(idspfobject, getHelpTable());
        _activeRecordTables.put(idspfobject.getName(), _currentART);
    }

    protected void addASSUMEActiveRecordTable(IDSPFObject idspfobject, boolean flag, boolean flag1, RecordsOnDevice recordsondevice)
    {
        idspfobject.setKEEP(flag);
        RecordsOnDevice recordsondevice1;
        if(flag1)
            recordsondevice1 = (RecordsOnDevice)recordsondevice.clone();
        else
            recordsondevice1 = recordsondevice;
        _currentART = new ActiveRecordTable(recordsondevice1, idspfobject, getHelpTable());
        _activeRecordTables.put(idspfobject.getName(), _currentART);
    }

    public void close(IDSPFObject idspfobject)
    {
        ActiveRecordTable activerecordtable = (ActiveRecordTable)_activeRecordTables.get(idspfobject.getName());
        if(activerecordtable != null)
        {
            IDSPFObject idspfobject1 = activerecordtable.getDSPFObject();
            if(idspfobject1 != null)
            {
                idspfobject1.close();
                _activeRecordTables.remove(idspfobject1.getName());
                if(!idspfobject1.isKEEP())
                {
                    if(activerecordtable == _currentART)
                        _currentART.clearScreen();
                } else
                {
                    activerecordtable.getRecordsOnDevice().closeDspfWithKeep(idspfobject);
                }
            }
        }
    }

    public Iterator getActiveKeys()
    {
        return getCurrentART().getActiveKeys();
    }

    public Iterator getActiveKeysWithoutEnter()
    {
        return getCurrentART().getActiveKeysWithoutEnter();
    }

    public String[] getRecordsWithPageUPDNKey()
    {
        return getCurrentART().getRecordsWithPageUPDNKey();
    }

    protected ActiveRecordTable getActiveRecordTable(IDSPFObject idspfobject)
    {
        return (ActiveRecordTable)_activeRecordTables.get(idspfobject.getName());
    }

    public String getAIDKey()
    {
        return getCurrentART().getAIDKey();
    }

    public Vector getChangedSubfileControlRecords()
    {
        return getCurrentART().getChangedSubfileControlRecords();
    }

    private ActiveRecordTable getCurrentART()
    {
        return _currentART;
    }

    public CursorPosition getCursor()
    {
        return _currentART.getCursor();
    }

    public ILibraryFile getDSPFObject()
    {
        return _currentART.getDSPFObject();
    }

    public AppHelpTable getHelpTable()
    {
        if(_helpTable == null)
            _helpTable = new AppHelpTable();
        return _helpTable;
    }

    public IIndicatorArea getIndicatorArea()
    {
        return getCurrentART().getIndicatorArea();
    }

    public String getJobCCSID()
    {
        return _jobCCSID;
    }

    public int getMaxColumn()
    {
        return !isWide() ? 80 : 132;
    }

    public int getMaxRow()
    {
        return !isWide() ? 24 : 27;
    }

    public Iterator[] getMessagesAndIDs()
    {
        return getCurrentART().getMessagesAndIDs();
    }

    public ArrayList getRecordLayersOnDevice()
    {
        return _currentART.getRecordsOnDevice().getLayers();
    }

    public int getSFLLowestRRN()
    {
        return getCurrentART().getSFLLowestRRN();
    }

    public boolean isHLPRTNActive()
    {
        return _currentART.isHLPRTNActive();
    }

    public boolean isHtmlHelp()
    {
        return _helpTable.isHtmlHelp();
    }

    public boolean isNoResponseIndOnAID()
    {
        return _currentART.isNoResponseIndOnAID();
    }

    public boolean isWide()
    {
        return getCurrentART().isCurrentDisplayWide();
    }

    public IInputBufferSaveArea mapRecordBeingRead()
        throws WebfacingInternalException
    {
        return _ARTBeingRead.mapRecordBeingRead();
    }

    public void open(IDSPFObject idspfobject)
    {
    }

    public void postProcessRead()
    {
        getCurrentART().postProcessDeviceRead();
    }

    public void prepareForRead()
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        getCurrentART().prepareForRead();
    }

    public boolean read(IDSPFObject idspfobject, String s, IIndicatorArea iindicatorarea)
        throws WFApplicationRuntimeError, WebfacingInternalException, WebfacingLevelCheckException
    {
        ActiveRecordTable activerecordtable = getActiveRecordTable(idspfobject);
        if(null == activerecordtable)
            throw new WFRecordReadBeforeWritten(idspfobject, s);
        boolean flag;
        if(_currentART == null)
        {
            flag = true;
        } else
        {
            IDSPFObject idspfobject1 = _currentART.getDSPFObject();
            flag = !idspfobject1.equalsDSPF(idspfobject);
        }
        boolean flag1 = activerecordtable.read(s, flag, iindicatorarea);
        _ARTBeingRead = activerecordtable;
        if(flag1 && flag && idspfobject.isRSTDSP())
        {
            suspend();
            _currentART = activerecordtable;
            restore(false);
        }
        return flag1;
    }

    protected void restore(boolean flag)
    {
        _currentART.restore(flag);
    }

    public void reverseProtectForImmediateWrite()
    {
        _currentART.unProtectForImmediateWrite();
    }

    public boolean setAIDKey(String s)
        throws WebfacingInternalException, WebfacingLevelCheckException
    {
        return _currentART.setAIDKeyOnDeviceRequest(s);
    }

    public void setCursor(CursorPosition cursorposition)
    {
        _currentART.setCursor(cursorposition);
    }

    private void setCurrentART(IDSPFObject idspfobject, boolean flag, boolean flag1)
    {
        if(_currentART == null)
        {
            addActiveRecordTable(idspfobject, flag);
        } else
        {
            IDSPFObject idspfobject1 = _currentART.getDSPFObject();
            RecordsOnDevice recordsondevice = _currentART.getRecordsOnDevice();
            if(!idspfobject1.equalsDSPF(idspfobject))
            {
                suspend();
                CursorPosition cursorposition = _currentART.getCursor();
                _currentART = getActiveRecordTable(idspfobject);
                boolean flag2 = false;
                if(cursorposition != null && (_currentART == null || _currentART.getCursor() == null))
                    flag2 = true;
                if((idspfobject1.isOpen() || idspfobject1.isKEEP()) && flag1)
                {
                    if(null == _currentART)
                        addASSUMEActiveRecordTable(idspfobject, flag, idspfobject1.isRSTDSP(), recordsondevice);
                    else
                    if(!idspfobject.isRSTDSP())
                        _currentART.setRecordsOnDeviceForRSTDSP_No(recordsondevice);
                    else
                        restore(true);
                } else
                if(null == _currentART)
                    addActiveRecordTable(idspfobject, flag);
                else
                    restore(true);
                if(flag2)
                    _currentART.setCursorToPrevLoc(cursorposition);
            } else
            if(!idspfobject1.isOpen())
                if(flag1)
                    addASSUMEActiveRecordTable(idspfobject, flag, idspfobject1.isRSTDSP(), recordsondevice);
                else
                    addActiveRecordTable(idspfobject, flag);
        }
    }

    public void setFieldValue(IFieldValue ifieldvalue)
        throws WebfacingInternalException, WebfacingLevelCheckException
    {
        _currentART.setFieldValue(ifieldvalue);
    }

    public void setJobCCSID(String s)
    {
        _jobCCSID = s;
    }

    public boolean setRelativeRecordNumber(String s, int i)
        throws WebfacingInternalException, WebfacingLevelCheckException
    {
        return _currentART.setRelativeRecordNumber(s, i);
    }

    protected void suspend()
    {
        _currentART.suspend();
    }

    public RecordViewBean write(IDSPFObject idspfobject, RecordDataBean recorddatabean, boolean flag, RecordBeanFactory recordbeanfactory, boolean flag1)
        throws WebfacingInternalException, WebfacingLevelCheckException, WFApplicationRuntimeError
    {
        RecordViewBean recordviewbean = recordbeanfactory.createRecordViewBean(recorddatabean);
        setCurrentART(idspfobject, recordviewbean.isKeywordSpecified(131L), recordviewbean.isKeywordSpecified(9L));
        return _currentART.add(recordviewbean, flag, flag1);
    }

    public boolean isInBiDiMode()
    {
        return _isInBiDiMode;
    }

    public void setIsInBiDiMode(boolean flag)
    {
        _isInBiDiMode = flag;
    }

    public boolean isHelpEnabled()
    {
        for(Iterator iterator = getActiveKeys(); iterator.hasNext();)
        {
            AIDKey aidkey = (AIDKey)iterator.next();
            if(aidkey.isHelpKey())
                return true;
        }

        return false;
    }

    public boolean hasHelpSpecifications()
    {
        return _helpTable.hasHelp();
    }

    public ICursorBufferData getCursorBufferData()
    {
        return _currentART.getCursorBufferData();
    }

    public LocationOnDevice getLocationOnDeviceAt(CursorPosition cursorposition)
    {
        return _currentART.getLocationOnDeviceAt(cursorposition);
    }

    public LocationOnDevice calculateLocationForCursor()
    {
        return _currentART.calculateLocationForCursor();
    }

    public void setSystemMenuOption(String s)
    {
        _currentART.setSystemMenuOption(s);
    }

    public IBuildRecordViewBean getRecordAt(CursorPosition cursorposition)
    {
        return _currentART.getRecordAt(cursorposition);
    }

    public String getActiveKeyName(String s)
    {
        if(AIDKeyDictionary.isKeyInDictionary(s))
        {
            for(Iterator iterator = getActiveKeys(); iterator.hasNext();)
            {
                AIDKey aidkey = (AIDKey)iterator.next();
                if(aidkey.getKeyCode().byteValue() == AIDKeyDictionary.getKeyCode(s))
                    return aidkey.getKeyName();
            }

        }
        return "";
    }

    public boolean isRecordOnTopLayer(RecordViewBean recordviewbean)
    {
        return getCurrentART().isRecordOnTopLayer(recordviewbean);
    }

    public boolean isRecordActive(String s)
    {
        return getCurrentART().isRecordActive(s);
    }

    public int getConceptualLayerZOrder(String s)
    {
        RecordViewBean recordviewbean = getCurrentART().getRecordViewBean(s);
        if(recordviewbean == null)
            return -1;
        else
            return recordviewbean.getConceptualLayerZOrder();
    }

    public RecordViewBean[] getFirstRollEnabledRecords()
    {
        return _currentART.getFirstRollEnabledRecords();
    }

    public SubfileControlRecordViewBean getSFLCTLRecordViewBeanForPaging(String s)
    {
        return _currentART.getSFLCTLRecordViewBeanForPaging(s);
    }

    public boolean isSubfileChanged(String s)
    {
        return _currentART.isSubfileChanged(s);
    }

    public static final String Copyright = "(C) Copyright IBM Corporation 1999-2003 all rights reserved";
    private Hashtable _activeRecordTables;
    private ActiveRecordTable _currentART;
    private ActiveRecordTable _ARTBeingRead;
    private String _jobCCSID;
    private boolean _isInBiDiMode;
    private AppHelpTable _helpTable;
}
