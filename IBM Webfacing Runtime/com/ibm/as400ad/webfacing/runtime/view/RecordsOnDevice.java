// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.IDSPFObject;
import com.ibm.as400ad.webfacing.runtime.core.WFApplicationRuntimeError;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import java.io.Serializable;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            ConceptualLayer, LocationOnDevice, RecordViewBean, IRemoveRecord, 
//            CommandKeyLabelList, SubfileControlRecordViewBean, CursorPosition

public class RecordsOnDevice
    implements Cloneable, Serializable
{

    public RecordsOnDevice(IRemoveRecord iremoverecord)
    {
        _handleAssume = false;
        _firstWindow = -1;
        _isCurrentDisplayWide = false;
        _readableButNoDedicatedSpaceOnScreen = null;
        clear();
        _allRecords = iremoverecord;
    }

    public boolean add(RecordViewBean recordviewbean)
        throws WebfacingInternalException, WFApplicationRuntimeError
    {
        int i = calcZOrder(recordviewbean);
        setActiveRecordForReadOrWrite(recordviewbean, true);
        boolean flag = getLayer(i).addRecord(recordviewbean, _readableButNoDedicatedSpaceOnScreen, i == 0);
        if(recordviewbean.isWindowed() && _firstWindow == -1)
            _firstWindow = i;
        if(recordviewbean.isReadableButNoDedicatedSpaceOnScreen())
        {
            _allRecords.remove(_readableButNoDedicatedSpaceOnScreen);
            _readableButNoDedicatedSpaceOnScreen = recordviewbean;
        }
        return flag;
    }

    private int calcZOrder(RecordViewBean recordviewbean)
        throws WFApplicationRuntimeError
    {
        int i = -1;
        if(_handleAssume)
        {
            if(recordviewbean.isKeywordSpecified(9L))
            {
                _keep = _topZOrder + 1;
                i = _keep;
            }
            _handleAssume = false;
        } else
        if(recordviewbean.isKeywordActive(171L))
        {
            if(_firstWindow == -1)
                i = _topZOrder + 1;
            else
                i = _firstWindow;
        } else
        {
            RecordViewBean recordviewbean1 = recordviewbean.getPreviousViewBean();
            if(null != recordviewbean1 && recordviewbean1.isDSPFActive())
            {
                i = recordviewbean1.getConceptualLayerZOrder();
                if(recordviewbean.isWdwDFT() || recordviewbean.isWdwREF())
                {
                    recordviewbean.setWdwStartLine(recordviewbean1.getWdwFirstLine());
                    recordviewbean.setWdwStartColumn(recordviewbean1.getWdwFirstColumn());
                }
            }
            if(i == -1)
                if(recordviewbean.isWindowed())
                {
                    if(recordviewbean.isWdwREF())
                    {
                        int j = _firstWindow;
                        if(j != -1)
                            for(; j <= _topZOrder; j++)
                            {
                                ConceptualLayer conceptuallayer = (ConceptualLayer)_zorder.get(j);
                                RecordViewBean recordviewbean2 = conceptuallayer.getFirst();
                                if(!recordviewbean.getWdwRefName().equals(recordviewbean2.getRecordName()) && !recordviewbean.getWdwRefName().equals(recordviewbean2.getWdwRefName()))
                                    continue;
                                i = j;
                                recordviewbean.setWdwStartLine(recordviewbean2.getWdwFirstLine());
                                recordviewbean.setWdwStartColumn(recordviewbean2.getWdwFirstColumn());
                                break;
                            }

                        if(j == -1 || j > _topZOrder)
                        {
                            String s = WebfacingConstants.replaceSubstring(_resmri.getString("WF0041"), "&1", recordviewbean.getWdwRefName());
                            s = WebfacingConstants.replaceSubstring(s, "&2", recordviewbean.getRecordName());
                            throw new WFApplicationRuntimeError("CPF500A", s);
                        }
                    } else
                    {
                        int k = _firstWindow;
                        if(k != -1)
                            for(; k <= _topZOrder; k++)
                            {
                                ConceptualLayer conceptuallayer1 = (ConceptualLayer)_zorder.get(k);
                                RecordViewBean recordviewbean3 = conceptuallayer1.getFirst();
                                if(!recordviewbean.getRecordName().equals(recordviewbean3.getWdwRefName()))
                                    continue;
                                i = k;
                                break;
                            }

                        if(i == -1)
                            i = _topZOrder + 1;
                    }
                } else
                {
                    if(_firstWindow > _keep)
                        i = _firstWindow - 1;
                    else
                        i = _keep;
                    if(i < 0)
                        i = 0;
                }
        }
        recordviewbean.setConceptualLayerZOrder(i);
        return i;
    }

    public void clear()
    {
        _zorder = new ArrayList();
        _topZOrder = -1;
        _keep = 0;
        _firstWindow = -1;
    }

    public Object clone()
    {
        RecordsOnDevice recordsondevice = null;
        try
        {
            recordsondevice = (RecordsOnDevice)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        int i = _zorder.size();
        recordsondevice._zorder = new ArrayList(i);
        for(int j = 0; j < i; j++)
            recordsondevice._zorder.add(((ConceptualLayer)_zorder.get(j)).clone());

        return recordsondevice;
    }

    private ConceptualLayer getLayer(int i)
    {
        ConceptualLayer conceptuallayer = null;
        if(i <= _topZOrder && i < _zorder.size())
        {
            conceptuallayer = (ConceptualLayer)_zorder.get(i);
        } else
        {
            if(_topZOrder != -1)
                ((ConceptualLayer)_zorder.get(_topZOrder)).setFocusCapable(false);
            _topZOrder = i;
            conceptuallayer = new ConceptualLayer(_allRecords);
            _zorder.add(i, conceptuallayer);
        }
        return conceptuallayer;
    }

    public ArrayList getLayers()
    {
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < _zorder.size(); i++)
        {
            ConceptualLayer conceptuallayer = (ConceptualLayer)_zorder.get(i);
            arraylist.addAll(conceptuallayer.getPresentationLayers());
        }

        return arraylist;
    }

    public CommandKeyLabelList getPotentialKeyLabelList(RecordViewBean recordviewbean)
    {
        return getLayer(recordviewbean.getConceptualLayerZOrder()).getPotentialKeyLabelList();
    }

    int getTopConceptualLayerIndex()
    {
        return _topZOrder;
    }

    ConceptualLayer getTopConceptualLayer()
    {
        return (ConceptualLayer)_zorder.get(_topZOrder);
    }

    boolean isASSUMEPending()
    {
        return _handleAssume;
    }

    boolean isControlWithFirstDisplayedSubfile(SubfileControlRecordViewBean subfilecontrolrecordviewbean)
    {
        return subfilecontrolrecordviewbean.equals(getLayer(_topZOrder).getControlWithFirstDisplayedSubfile());
    }

    public boolean isCurrentDisplayWide()
    {
        return _isCurrentDisplayWide;
    }

    public boolean isEmpty()
    {
        return _topZOrder == -1;
    }

    public void popAllWindows()
    {
        popLayers(_firstWindow);
        _firstWindow = -1;
    }

    public void popLayers(int i)
    {
        if(i > -1 && _topZOrder >= i)
        {
            for(; _topZOrder >= i; _topZOrder--)
            {
                ConceptualLayer conceptuallayer = (ConceptualLayer)_zorder.get(_topZOrder);
                conceptuallayer.clear();
                _zorder.remove(_topZOrder);
            }

            if(_topZOrder > -1)
                ((ConceptualLayer)_zorder.get(_topZOrder)).setFocusCapable(true);
        }
    }

    public void popWindowsAfter(RecordViewBean recordviewbean)
    {
        popLayers(recordviewbean.getConceptualLayerZOrder() + 1);
    }

    public void setActiveRecordForReadOrWrite(RecordViewBean recordviewbean, boolean flag)
    {
        if(recordviewbean.isWindowed())
        {
            if(flag && recordviewbean.isKeywordActive(171L))
                popAllWindows();
            else
                popWindowsAfter(recordviewbean);
        } else
        {
            popAllWindows();
        }
    }

    public void setCurrentART(IRemoveRecord iremoverecord, boolean flag)
    {
        _allRecords = iremoverecord;
        _handleAssume = flag;
        _firstWindow = -1;
        int i = _zorder.size();
        for(int j = 0; j < i; j++)
        {
            ConceptualLayer conceptuallayer = getLayer(j);
            if(!flag && conceptuallayer.getDspf().equalsDSPF(iremoverecord.getDSPFObject()) && !conceptuallayer.isKeptOnDisplay())
            {
                if(conceptuallayer.isWindowed() && _firstWindow == -1)
                    _firstWindow = j;
                conceptuallayer.setDSPFActive(true);
            } else
            {
                conceptuallayer.setDSPFActive(false);
            }
        }

    }

    public void closeDspfWithKeep(IDSPFObject idspfobject)
    {
        int i = _zorder.size();
        for(int j = 0; j < i; j++)
        {
            ConceptualLayer conceptuallayer = getLayer(j);
            if(conceptuallayer.getDspf().equalsDSPF(idspfobject))
                conceptuallayer.setKeptOnDisplay();
        }

    }

    public void setIsCurrentDisplayWide(boolean flag)
    {
        _isCurrentDisplayWide = flag;
    }

    public void updatePotentialKeyLabelList(RecordViewBean recordviewbean, CommandKeyLabelList commandkeylabellist)
    {
        getLayer(recordviewbean.getConceptualLayerZOrder()).updatePotentialKeyLabelList(recordviewbean, commandkeylabellist);
    }

    LocationOnDevice getLocationOnDeviceAt(CursorPosition cursorposition)
    {
        Object obj = null;
        Iterator iterator = getConceptualLayers();
        for(boolean flag = true; iterator.hasNext(); flag = false)
        {
            ConceptualLayer conceptuallayer = (ConceptualLayer)iterator.next();
            LocationOnDevice locationondevice = conceptuallayer.getLocationOnDeviceAt(cursorposition);
            if(locationondevice != null)
            {
                if(!flag)
                    locationondevice.setIsValidForRTNCSRLOC(false);
                return locationondevice;
            }
        }

        return new LocationOnDevice(cursorposition);
    }

    Iterator getConceptualLayers()
    {
        ArrayList arraylist = new ArrayList();
        for(int i = _zorder.size() - 1; i >= 0; i--)
        {
            ConceptualLayer conceptuallayer = (ConceptualLayer)_zorder.get(i);
            arraylist.add(conceptuallayer);
        }

        return arraylist.iterator();
    }

    private static ResourceBundle _resmri;
    private List _zorder;
    private int _topZOrder;
    private int _keep;
    private boolean _handleAssume;
    private IRemoveRecord _allRecords;
    private int _firstWindow;
    public static final int UNDEFINED = -1;
    private boolean _isCurrentDisplayWide;
    public static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2002");
    private RecordViewBean _readableButNoDedicatedSpaceOnScreen;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
