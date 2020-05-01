// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.runtime.controller.IDSPFObject;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordViewDefinition;
import java.io.Serializable;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            CommandKeyLabelList, ClearedLines, PresentationLayer, VisibleRectangle, 
//            RecordViewBean, SubfileControlRecordViewBean, ICommandKeyLabel, IRemoveRecord, 
//            LocationOnDevice, CursorPosition

class ConceptualLayer
    implements Cloneable, ENUM_KeywordIdentifiers, Serializable
{

    public ConceptualLayer(IRemoveRecord iremoverecord)
    {
        _presentationLayers = new LinkedList();
        _windowLayer = false;
        _keptOnDisplay = false;
        _potentialKeyLabelList = new CommandKeyLabelList();
        _allRecords = iremoverecord;
    }

    private void addExclusively(RecordViewBean recordviewbean)
        throws WebfacingInternalException
    {
        if(recordviewbean.isWindowed())
            clear();
        if(recordviewbean.isCLRL_ALL() && recordviewbean.isOutputOnly())
            recordviewbean.setNoDedicatedSpaceOnScreen();
        if(recordviewbean.getFirstFieldLine() != -1 || recordviewbean.getFirstFieldLine() == -1 && recordviewbean.isWindowed())
            createPresentationLayer(recordviewbean, 0);
    }

    private boolean addOnTop(RecordViewBean recordviewbean, RecordViewBean recordviewbean1)
        throws WebfacingInternalException
    {
        boolean flag = false;
        if(recordviewbean.isKeywordSpecified(97L))
            flag = handleERASE(recordviewbean, recordviewbean1);
        if(recordviewbean.isCLRL())
            flag = addWithCLRL(recordviewbean, recordviewbean1) || flag;
        else
            flag = addWithOVERLAY(recordviewbean, recordviewbean1) || flag;
        if(recordviewbean.isKeywordActive(159L))
        {
            handlePROTECT(recordviewbean);
            flag = true;
        }
        return flag;
    }

    public boolean addRecord(RecordViewBean recordviewbean, RecordViewBean recordviewbean1, boolean flag)
        throws WebfacingInternalException
    {
        boolean flag1 = false;
        if((recordviewbean.isKeywordActive(152L) || recordviewbean.isCLRL()) && !recordviewbean.isCLRL_ALL())
        {
            flag1 = addOnTop(recordviewbean, recordviewbean1);
            RecordViewBean recordviewbean2 = recordviewbean.getPreviousViewBean();
            if(null != recordviewbean2)
                if(!recordviewbean.isCLRL() || recordviewbean.isCLRL() && recordviewbean2.getFirstFieldLine() == recordviewbean.getFirstFieldLine())
                {
                    recordviewbean2.removeFromCoveringRectangles(recordviewbean);
                    PresentationLayer presentationlayer = recordviewbean2.getPresentationLayer();
                    if(null != presentationlayer)
                        presentationlayer.remove(recordviewbean2);
                    _allRecords.remove(recordviewbean2);
                } else
                {
                    recordviewbean2.setNoDedicatedSpaceOnScreen();
                }
            removeEmptyLayers(flag);
        } else
        {
            addExclusively(recordviewbean);
        }
        return flag1;
    }

    private boolean addWithCLRL(RecordViewBean recordviewbean, RecordViewBean recordviewbean1)
        throws WebfacingInternalException
    {
        boolean flag = false;
        boolean flag1 = recordviewbean.isOutputOnly();
        if(flag1)
            recordviewbean.setNoDedicatedSpaceOnScreen();
        if(recordviewbean.isCLRL_NO())
        {
            if(recordviewbean.getFirstFieldLine() != -1)
            {
                if(!flag1)
                    flag = overlapCheckingForInputCapableCLRL_NO(recordviewbean);
                createPresentationLayer(recordviewbean, 2);
            }
            return flag;
        }
        int i = 0;
        if(recordviewbean.isCLRL_NN())
            i = recordviewbean.getCLRL_NN() - recordviewbean.getRecordLength();
        else
        if(recordviewbean.isCLRL_END())
            i = recordviewbean.getCLRL_END() - recordviewbean.getRecordLength();
        if(recordviewbean.isCLRL_NN() && i < 0)
        {
            ClearedLines clearedlines = new ClearedLines(recordviewbean.getStartingLineNumber(), (recordviewbean.getStartingLineNumber() + recordviewbean.getCLRL_NN()) - 1);
            if(flag1)
                overlapCheckingForOutputOnlyCLRLRecord(clearedlines, null, recordviewbean1);
            else
                flag = overlapCheckingForInputCapableCLRL_NN(recordviewbean, clearedlines);
            if(!clearedlines.hasNoRecordToCover())
            {
                clearedlines.updateFirstAndLastLines();
                createPresentationLayer(clearedlines, 1);
            }
            createPresentationLayer(recordviewbean, 2);
            return flag;
        }
        if((recordviewbean.isCLRL_END() || recordviewbean.isCLRL_NN()) && i == 0)
        {
            if(flag1)
                overlapCheckingForOutputOnlyCLRLRecord(recordviewbean, null, recordviewbean1);
            else
                flag = overlapCheckingForInputCapableCLRL_ENDOrNN(recordviewbean);
            ClearedLines clearedlines1 = null;
            if(recordviewbean.getSLNO() > 0 && recordviewbean.getFirstFieldLine() > recordviewbean.getSLNO())
            {
                List list = recordviewbean.getClearedLinesForCoveredByThis(null, null);
                clearedlines1 = (ClearedLines)list.get(0);
            }
            add(recordviewbean, clearedlines1, null);
            return flag;
        }
        if((recordviewbean.isCLRL_END() || recordviewbean.isCLRL_NN()) && i > 0)
        {
            ClearedLines clearedlines2 = null;
            if(recordviewbean.getRecordLength() == 0)
                clearedlines2 = new ClearedLines(recordviewbean.getSLNO(), (recordviewbean.getSLNO() + i) - 1);
            else
                clearedlines2 = new ClearedLines(recordviewbean.getLastFieldLine() + 1, recordviewbean.getLastFieldLine() + i);
            if(flag1)
                overlapCheckingForOutputOnlyCLRLRecord(recordviewbean, clearedlines2, recordviewbean1);
            else
                flag = overlapCheckingForInputCapableCLRL_ENDOrNN(recordviewbean, clearedlines2);
            if(recordviewbean.getRecordLength() == 0)
            {
                if(!clearedlines2.hasNoRecordToCover())
                {
                    clearedlines2.updateFirstAndLastLines();
                    createPresentationLayer(clearedlines2, 1);
                }
            } else
            {
                ClearedLines clearedlines3 = null;
                if(recordviewbean.getSLNO() > 0 && recordviewbean.getFirstFieldLine() > recordviewbean.getSLNO())
                {
                    List list1 = recordviewbean.getClearedLinesForCoveredByThis(null, null);
                    clearedlines3 = (ClearedLines)list1.get(0);
                }
                add(recordviewbean, clearedlines3, clearedlines2);
            }
            return flag;
        } else
        {
            return flag;
        }
    }

    private boolean addWithOVERLAY(RecordViewBean recordviewbean, RecordViewBean recordviewbean1)
        throws WebfacingInternalException
    {
        boolean flag = false;
        if(recordviewbean.getFirstFieldLine() != -1 || recordviewbean.getFirstFieldLine() == -1 && recordviewbean.isWindowed())
        {
            ClearedLines clearedlines = null;
            ClearedLines clearedlines1 = null;
            for(int i = _presentationLayers.size() - 1; i >= 0; i--)
            {
                PresentationLayer presentationlayer = (PresentationLayer)_presentationLayers.get(i);
                LinkedList linkedlist = presentationlayer.getRectangles();
                for(int j = 0; j < linkedlist.size(); j++)
                {
                    VisibleRectangle visiblerectangle = (VisibleRectangle)linkedlist.get(j);
                    if(visiblerectangle instanceof RecordViewBean)
                    {
                        RecordViewBean recordviewbean2 = (RecordViewBean)visiblerectangle;
                        if(recordviewbean2.isOverlappedBy(recordviewbean))
                        {
                            if(recordviewbean2.hasNoDedicatedSpaceOnScreen())
                            {
                                if(recordviewbean.updateCoveredByThis(recordviewbean2))
                                {
                                    presentationlayer.remove(recordviewbean2);
                                    j--;
                                    if(recordviewbean2 != recordviewbean1)
                                        _allRecords.remove(recordviewbean2);
                                }
                            } else
                            {
                                if(recordviewbean2.isCLRL_NO() || recordviewbean2.isCLRL_NN() && recordviewbean2.getCLRL_NN() - recordviewbean2.getRecordLength() < 0)
                                {
                                    presentationlayer.remove(recordviewbean2);
                                    j--;
                                    _allRecords.remove(recordviewbean2);
                                    int k = recordviewbean2.getStartingLineNumber();
                                    if(recordviewbean2.isCLRL_NN())
                                        k += recordviewbean2.getCLRL_NN();
                                    List list2 = recordviewbean.getClearedLinesForRecordDifference(k, recordviewbean2.getLastFieldLine());
                                    clearedlines = (ClearedLines)list2.get(0);
                                    clearedlines1 = (ClearedLines)list2.get(1);
                                    if(clearedlines != null)
                                        overlapCheckingForVisibleRectangles(clearedlines, clearedlines1, 0, recordviewbean1);
                                    else
                                    if(clearedlines1 != null)
                                        overlapCheckingForVisibleRectangles(clearedlines1, null, 0, recordviewbean1);
                                } else
                                {
                                    recordviewbean2.removeFromCoveringRectangles(recordviewbean);
                                    presentationlayer.remove(recordviewbean2);
                                    j--;
                                    _allRecords.remove(recordviewbean2);
                                    List list1 = recordviewbean2.getClearedLinesForRecordDifference(recordviewbean);
                                    clearedlines = (ClearedLines)list1.get(0);
                                    clearedlines1 = (ClearedLines)list1.get(1);
                                    if(clearedlines != null)
                                        overlapCheckingForVisibleRectangles(clearedlines, clearedlines1, i + 1, recordviewbean1);
                                    else
                                    if(clearedlines1 != null)
                                        overlapCheckingForVisibleRectangles(clearedlines1, null, i + 1, recordviewbean1);
                                }
                                if(!flag && needsToSetOffMDTs(recordviewbean2, recordviewbean))
                                    flag = true;
                            }
                        } else
                        if(!recordviewbean2.isLocatedBefore(recordviewbean) && !flag && !recordviewbean.isOutputOnly() && !recordviewbean2.isOutputOnly())
                            flag = true;
                    }
                }

            }

            List list = recordviewbean.getClearedLinesForCoveredByThis(clearedlines, clearedlines1);
            clearedlines = (ClearedLines)list.get(0);
            clearedlines1 = (ClearedLines)list.get(1);
            add(recordviewbean, clearedlines, clearedlines1);
        }
        return flag;
    }

    private void add(RecordViewBean recordviewbean, ClearedLines clearedlines, ClearedLines clearedlines1)
        throws WebfacingInternalException
    {
        PresentationLayer presentationlayer = getTopNonEmptyPresentationLayer();
        if(presentationlayer == null)
        {
            createPresentationLayer(recordviewbean, 0);
        } else
        {
            int i = recordviewbean.getFirstFieldLine();
            int j = recordviewbean.getLastFieldLine();
            if(clearedlines != null && !clearedlines.hasNoRecordToCover())
                i = clearedlines.getFirstFieldLine();
            if(clearedlines1 != null && !clearedlines1.hasNoRecordToCover())
                j = clearedlines1.getLastFieldLine();
            if(presentationlayer.getType() == 2)
                createPresentationLayer(recordviewbean, 1);
            else
            if(presentationlayer.hasNoRecordOverlappedBy(i, j) && hasNoUnderlyingRecordBetween(presentationlayer, i, j))
                presentationlayer.add(recordviewbean);
            else
                createPresentationLayer(recordviewbean, 1);
        }
        if(clearedlines != null && !clearedlines.hasNoRecordToCover())
        {
            clearedlines.updateFirstAndLastLines();
            ((PresentationLayer)_presentationLayers.getLast()).add(clearedlines);
        }
        if(clearedlines1 != null && !clearedlines1.hasNoRecordToCover())
        {
            clearedlines1.updateFirstAndLastLines();
            ((PresentationLayer)_presentationLayers.getLast()).add(clearedlines1);
        }
    }

    public void clear()
    {
        while(_presentationLayers.size() > 0) 
        {
            PresentationLayer presentationlayer = (PresentationLayer)_presentationLayers.removeFirst();
            for(LinkedList linkedlist = presentationlayer.getRectangles(); linkedlist.size() > 0;)
            {
                VisibleRectangle visiblerectangle = (VisibleRectangle)linkedlist.removeFirst();
                if(visiblerectangle instanceof RecordViewBean)
                    _allRecords.remove((RecordViewBean)visiblerectangle);
            }

        }
    }

    public Object clone()
    {
        ConceptualLayer conceptuallayer = null;
        try
        {
            conceptuallayer = (ConceptualLayer)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        conceptuallayer._presentationLayers = new LinkedList();
        int i = _presentationLayers.size();
        for(int j = 0; j < i; j++)
        {
            PresentationLayer presentationlayer = (PresentationLayer)((PresentationLayer)_presentationLayers.get(j)).clone();
            presentationlayer.setConceptualLayer(conceptuallayer);
            conceptuallayer._presentationLayers.add(presentationlayer);
        }

        return conceptuallayer;
    }

    public void createPresentationLayer(VisibleRectangle visiblerectangle, int i)
        throws WebfacingInternalException
    {
        PresentationLayer presentationlayer = new PresentationLayer(this, i, visiblerectangle);
        _presentationLayers.add(presentationlayer);
    }

    SubfileControlRecordViewBean getControlWithFirstDisplayedSubfile()
    {
        int i = 130;
        SubfileControlRecordViewBean subfilecontrolrecordviewbean = null;
        for(int j = _presentationLayers.size() - 1; j >= 0; j--)
        {
            PresentationLayer presentationlayer = (PresentationLayer)_presentationLayers.get(j);
            for(Iterator iterator = presentationlayer.getRecords(); iterator.hasNext();)
            {
                RecordViewBean recordviewbean = (RecordViewBean)iterator.next();
                if(recordviewbean instanceof SubfileControlRecordViewBean)
                {
                    SubfileControlRecordViewBean subfilecontrolrecordviewbean1 = (SubfileControlRecordViewBean)recordviewbean;
                    if(subfilecontrolrecordviewbean1.isSubfileVisible() && subfilecontrolrecordviewbean1.getSubfileAreaFirstRow() < i)
                    {
                        i = subfilecontrolrecordviewbean1.getSubfileAreaFirstRow();
                        subfilecontrolrecordviewbean = subfilecontrolrecordviewbean1;
                    }
                }
            }

        }

        return subfilecontrolrecordviewbean;
    }

    public RecordViewBean getFirst()
    {
        return (RecordViewBean)((PresentationLayer)_presentationLayers.getFirst()).getFirst();
    }

    public CommandKeyLabelList getPotentialKeyLabelList()
    {
        Iterator iterator = _potentialKeyLabelList.getLabels();
        CommandKeyLabelList commandkeylabellist = new CommandKeyLabelList();
        while(iterator.hasNext()) 
        {
            ICommandKeyLabel icommandkeylabel = (ICommandKeyLabel)iterator.next();
            String s = icommandkeylabel.getRecordName();
            List list = getRecordList();
            for(int i = 0; i < list.size(); i++)
            {
                RecordViewBean recordviewbean = (RecordViewBean)list.get(i);
                if(!recordviewbean.getRecordName().equals(icommandkeylabel.getRecordName()))
                    continue;
                commandkeylabellist.add(icommandkeylabel);
                break;
            }

        }
        return commandkeylabellist;
    }

    public LinkedList getPresentationLayers()
    {
        return _presentationLayers;
    }

    private PresentationLayer getTopNonEmptyPresentationLayer()
    {
        for(int i = _presentationLayers.size() - 1; i >= 0; i--)
        {
            PresentationLayer presentationlayer = (PresentationLayer)_presentationLayers.get(i);
            if(presentationlayer.isEmpty())
                _presentationLayers.remove(i);
            else
                return presentationlayer;
        }

        return null;
    }

    private boolean handleERASE(RecordViewBean recordviewbean, RecordViewBean recordviewbean1)
        throws WebfacingInternalException
    {
        boolean flag = false;
        for(Iterator iterator = recordviewbean.getRecordViewDefinition().getERASEKeywords(); iterator.hasNext();)
        {
            KeywordDefinition keyworddefinition = (KeywordDefinition)iterator.next();
            if(recordviewbean.evaluateIndicatorExpression(keyworddefinition.getIndicatorExpression()))
            {
                for(Iterator iterator1 = keyworddefinition.getParameters(); iterator1.hasNext();)
                {
                    boolean flag1 = false;
                    String s = (String)iterator1.next();
                    for(int i = _presentationLayers.size() - 1; i >= 0; i--)
                    {
                        PresentationLayer presentationlayer = (PresentationLayer)_presentationLayers.get(i);
                        LinkedList linkedlist = presentationlayer.getRectangles();
                        for(int j = 0; j < linkedlist.size(); j++)
                        {
                            VisibleRectangle visiblerectangle = (VisibleRectangle)linkedlist.get(j);
                            if(visiblerectangle instanceof RecordViewBean)
                            {
                                RecordViewBean recordviewbean2 = (RecordViewBean)visiblerectangle;
                                if(recordviewbean2.getRecordName().equals(s) && (!recordviewbean2.hasNoDedicatedSpaceOnScreen() || recordviewbean2.hasNoDedicatedSpaceOnScreen() && recordviewbean2 == recordviewbean1))
                                {
                                    recordviewbean2.removeFromCoveringRectangles(recordviewbean);
                                    presentationlayer.remove(recordviewbean2);
                                    j--;
                                    _allRecords.remove(recordviewbean2);
                                    if(recordviewbean2.getStartingLineNumber() != recordviewbean2.getFirstFieldLine())
                                    {
                                        ClearedLines clearedlines = new ClearedLines(recordviewbean2.getStartingLineNumber(), recordviewbean2.getFirstFieldLine());
                                        overlapCheckingForVisibleRectangles(clearedlines, null, 0, recordviewbean1);
                                        if(!clearedlines.hasNoRecordToCover())
                                            recordviewbean.addCoveredByThis(clearedlines.getCoveredByThis());
                                    }
                                    if(!flag && (!recordviewbean2.isOutputOnly() || recordviewbean2.isCSRLOCActiveAndValid() || recordviewbean2.getDspatrPC_Location().getField() != null))
                                        flag = true;
                                    boolean flag2 = true;
                                }
                            }
                        }

                    }

                }

            }
        }

        return flag;
    }

    private void handlePROTECT(RecordViewBean recordviewbean)
    {
        for(int i = _presentationLayers.size() - 1; i >= 0; i--)
        {
            PresentationLayer presentationlayer = (PresentationLayer)_presentationLayers.get(i);
            for(Iterator iterator = presentationlayer.getRecords(); iterator.hasNext();)
            {
                RecordViewBean recordviewbean1 = (RecordViewBean)iterator.next();
                if(recordviewbean1 != recordviewbean)
                {
                    recordviewbean1.setPROTECT();
                    _allRecords.removeProtectedRecord(recordviewbean1);
                }
            }

        }

    }

    private boolean hasNoUnderlyingRecordBetween(PresentationLayer presentationlayer, int i, int j)
    {
        int k = presentationlayer.getFirstRow();
        int l = presentationlayer.getLastRow();
        if(l + 1 < i)
        {
            for(int i1 = 0; i1 < _presentationLayers.size() - 1; i1++)
                if(!((PresentationLayer)_presentationLayers.get(i1)).hasNoRecordOverlappedBy(l + 1, i - 1))
                    return false;

        }
        if(j + 1 < k)
        {
            for(int j1 = 0; j1 < _presentationLayers.size() - 1; j1++)
                if(!((PresentationLayer)_presentationLayers.get(j1)).hasNoRecordOverlappedBy(j + 1, k - 1))
                    return false;

        }
        return true;
    }

    public boolean isWindowed()
    {
        return _windowLayer;
    }

    private boolean overlapCheckingForInputCapableCLRL_ENDOrNN(RecordViewBean recordviewbean)
    {
        boolean flag = false;
        for(int i = _presentationLayers.size() - 1; i >= 0; i--)
        {
            PresentationLayer presentationlayer = (PresentationLayer)_presentationLayers.get(i);
            LinkedList linkedlist = presentationlayer.getRectangles();
            for(int j = 0; j < linkedlist.size(); j++)
            {
                VisibleRectangle visiblerectangle = (VisibleRectangle)linkedlist.get(j);
                if(visiblerectangle instanceof RecordViewBean)
                {
                    RecordViewBean recordviewbean1 = (RecordViewBean)visiblerectangle;
                    if(recordviewbean1.isOverlappedBy(recordviewbean))
                    {
                        recordviewbean1.setNoDedicatedSpaceOnScreen();
                        _allRecords.remove(recordviewbean1);
                        if(needsToSetOffMDTs(recordviewbean1, recordviewbean))
                            flag = true;
                        boolean flag1 = recordviewbean.updateCoveredByThis(recordviewbean1);
                        if(flag1)
                        {
                            presentationlayer.remove(recordviewbean1);
                            j--;
                        }
                    }
                }
            }

        }

        return flag;
    }

    private boolean overlapCheckingForInputCapableCLRL_ENDOrNN(RecordViewBean recordviewbean, ClearedLines clearedlines)
    {
        boolean flag = false;
        for(int i = _presentationLayers.size() - 1; i >= 0; i--)
        {
            PresentationLayer presentationlayer = (PresentationLayer)_presentationLayers.get(i);
            LinkedList linkedlist = presentationlayer.getRectangles();
            for(int j = 0; j < linkedlist.size(); j++)
            {
                VisibleRectangle visiblerectangle = (VisibleRectangle)linkedlist.get(j);
                if(visiblerectangle instanceof RecordViewBean)
                {
                    RecordViewBean recordviewbean1 = (RecordViewBean)visiblerectangle;
                    if(recordviewbean1.isOverlappedBy(recordviewbean))
                    {
                        recordviewbean1.setNoDedicatedSpaceOnScreen();
                        _allRecords.remove(recordviewbean1);
                        if(needsToSetOffMDTs(recordviewbean1, recordviewbean))
                            flag = true;
                        boolean flag1 = recordviewbean.updateCoveredByThis(recordviewbean1);
                        if(flag1)
                        {
                            presentationlayer.remove(recordviewbean1);
                            j--;
                        }
                    }
                    if(recordviewbean1.isOverlappedBy(clearedlines))
                    {
                        recordviewbean1.setNoDedicatedSpaceOnScreen();
                        _allRecords.remove(recordviewbean1);
                        if(needsToSetOffMDTs(recordviewbean1, recordviewbean))
                            flag = true;
                        boolean flag2 = clearedlines.updateCoveredByThis(recordviewbean1);
                        if(flag2)
                        {
                            presentationlayer.remove(recordviewbean1);
                            j--;
                        }
                    }
                }
            }

        }

        return flag;
    }

    private boolean overlapCheckingForInputCapableCLRL_NN(RecordViewBean recordviewbean, ClearedLines clearedlines)
    {
        boolean flag = false;
        for(int i = _presentationLayers.size() - 1; i >= 0; i--)
        {
            PresentationLayer presentationlayer = (PresentationLayer)_presentationLayers.get(i);
            LinkedList linkedlist = presentationlayer.getRectangles();
            for(int j = 0; j < linkedlist.size(); j++)
            {
                VisibleRectangle visiblerectangle = (VisibleRectangle)linkedlist.get(j);
                if(visiblerectangle instanceof RecordViewBean)
                {
                    RecordViewBean recordviewbean1 = (RecordViewBean)visiblerectangle;
                    if(recordviewbean1.isOverlappedBy(recordviewbean))
                    {
                        recordviewbean1.setNoDedicatedSpaceOnScreen();
                        _allRecords.remove(recordviewbean1);
                        if(needsToSetOffMDTs(recordviewbean1, recordviewbean))
                            flag = true;
                        boolean flag1 = false;
                        if(recordviewbean1.isOverlappedBy(clearedlines))
                            flag1 = clearedlines.updateCoveredByThis(recordviewbean1);
                        if(flag1)
                        {
                            presentationlayer.remove(recordviewbean1);
                            j--;
                        }
                    }
                }
            }

        }

        return flag;
    }

    private boolean overlapCheckingForInputCapableCLRL_NO(RecordViewBean recordviewbean)
    {
        boolean flag = false;
        for(int i = _presentationLayers.size() - 1; i >= 0; i--)
        {
            PresentationLayer presentationlayer = (PresentationLayer)_presentationLayers.get(i);
            LinkedList linkedlist = presentationlayer.getRectangles();
            for(int j = 0; j < linkedlist.size(); j++)
            {
                VisibleRectangle visiblerectangle = (VisibleRectangle)linkedlist.get(j);
                if(visiblerectangle instanceof RecordViewBean)
                {
                    RecordViewBean recordviewbean1 = (RecordViewBean)visiblerectangle;
                    if(recordviewbean1.isOverlappedBy(recordviewbean))
                    {
                        recordviewbean1.setNoDedicatedSpaceOnScreen();
                        _allRecords.remove(recordviewbean1);
                        if(needsToSetOffMDTs(recordviewbean1, recordviewbean))
                            flag = true;
                    }
                }
            }

        }

        return flag;
    }

    private void overlapCheckingForOutputOnlyCLRLRecord(VisibleRectangle visiblerectangle, VisibleRectangle visiblerectangle1, RecordViewBean recordviewbean)
    {
        overlapCheckingForVisibleRectangles(visiblerectangle, visiblerectangle1, 0, recordviewbean);
    }

    private void overlapCheckingForVisibleRectangles(VisibleRectangle visiblerectangle, VisibleRectangle visiblerectangle1, int i, RecordViewBean recordviewbean)
    {
        for(int j = _presentationLayers.size() - 1; j >= i; j--)
        {
            PresentationLayer presentationlayer = (PresentationLayer)_presentationLayers.get(j);
            LinkedList linkedlist = presentationlayer.getRectangles();
            for(int k = 0; k < linkedlist.size(); k++)
            {
                VisibleRectangle visiblerectangle2 = (VisibleRectangle)linkedlist.get(k);
                if(visiblerectangle2 instanceof RecordViewBean)
                {
                    RecordViewBean recordviewbean1 = (RecordViewBean)visiblerectangle2;
                    boolean flag = false;
                    if(recordviewbean1.isOverlappedBy(visiblerectangle))
                        flag = visiblerectangle.updateCoveredByThis(recordviewbean1);
                    if(visiblerectangle1 != null && recordviewbean1.isOverlappedBy(visiblerectangle1))
                        flag = visiblerectangle1.updateCoveredByThis(recordviewbean1);
                    if(flag)
                    {
                        presentationlayer.remove(recordviewbean1);
                        k--;
                        if(recordviewbean1 != recordviewbean)
                            _allRecords.remove(recordviewbean1);
                    }
                }
            }

        }

    }

    private void removeEmptyLayers(boolean flag)
    {
        for(int i = 0; i < _presentationLayers.size(); i++)
            if(((PresentationLayer)_presentationLayers.get(i)).isEmpty())
                _presentationLayers.remove(i--);

        if(flag && !_presentationLayers.isEmpty())
        {
            PresentationLayer presentationlayer = (PresentationLayer)_presentationLayers.get(0);
            if(presentationlayer.getType() == 1)
                presentationlayer.setType(0);
        }
    }

    public void setDSPFActive(boolean flag)
    {
        for(int i = _presentationLayers.size() - 1; i >= 0; i--)
        {
            PresentationLayer presentationlayer = (PresentationLayer)_presentationLayers.get(i);
            for(Iterator iterator = presentationlayer.getRecords(); iterator.hasNext(); ((RecordViewBean)iterator.next()).setDSPFActive(flag));
        }

    }

    public void setFocusCapable(boolean flag)
    {
        for(int i = _presentationLayers.size() - 1; i >= 0; i--)
            ((PresentationLayer)_presentationLayers.get(i)).setFocusCapable(flag);

    }

    public void setWindowLayer(boolean flag)
    {
        _windowLayer = flag;
    }

    List getRecordList()
    {
        ArrayList arraylist = new ArrayList();
        for(Iterator iterator = _presentationLayers.iterator(); iterator.hasNext();)
        {
            PresentationLayer presentationlayer = (PresentationLayer)iterator.next();
            for(Iterator iterator1 = presentationlayer.getRecords(); iterator1.hasNext(); arraylist.add(iterator1.next()));
        }

        return arraylist;
    }

    public void updatePotentialKeyLabelList(RecordViewBean recordviewbean, CommandKeyLabelList commandkeylabellist)
    {
        if(!recordviewbean.isCmdKeyRetained())
            _potentialKeyLabelList.mergeList(recordviewbean.getActiveCommandKeys().iterator());
        _potentialKeyLabelList.mergeList(commandkeylabellist);
    }

    void setKeptOnDisplay()
    {
        _keptOnDisplay = true;
    }

    boolean isKeptOnDisplay()
    {
        return _keptOnDisplay;
    }

    IDSPFObject getDspf()
    {
        return _allRecords.getDSPFObject();
    }

    private static boolean needsToSetOffMDTs(RecordViewBean recordviewbean, RecordViewBean recordviewbean1)
    {
        return !recordviewbean.isOutputOnly() || !recordviewbean.getRecordName().equals(recordviewbean1.getRecordName()) && (recordviewbean.isCSRLOCActiveAndValid() || recordviewbean.getDspatrPC_Location().getField() != null);
    }

    LocationOnDevice getFirstFocusCapableField()
    {
        LocationOnDevice locationondevice = null;
        for(Iterator iterator = getPresentationLayers().iterator(); iterator.hasNext();)
        {
            PresentationLayer presentationlayer = (PresentationLayer)iterator.next();
            LocationOnDevice locationondevice1 = presentationlayer.getFirstFocusCapableField();
            if(locationondevice1 != null && locationondevice1.isBefore(locationondevice))
                locationondevice = locationondevice1;
        }

        return locationondevice;
    }

    LocationOnDevice getNamedFieldAt(CursorPosition cursorposition)
    {
        Object obj = null;
        for(Iterator iterator = getPresentationLayers().iterator(); iterator.hasNext();)
        {
            PresentationLayer presentationlayer = (PresentationLayer)iterator.next();
            LocationOnDevice locationondevice = presentationlayer.getNamedFieldAt(cursorposition);
            if(locationondevice != null)
                return locationondevice;
        }

        return null;
    }

    LocationOnDevice getLocationOnDeviceAt(CursorPosition cursorposition)
    {
        Object obj = null;
        LinkedList linkedlist = getPresentationLayers();
        for(ListIterator listiterator = linkedlist.listIterator(linkedlist.size()); listiterator.hasPrevious();)
        {
            PresentationLayer presentationlayer = (PresentationLayer)listiterator.previous();
            LocationOnDevice locationondevice = presentationlayer.getLocationOnDeviceAt(cursorposition);
            if(locationondevice != null)
                return locationondevice;
        }

        return null;
    }

    public RecordViewBean[] getFirstRollEnabledRecords()
    {
        Iterator iterator = getPresentationLayers().iterator();
        RecordViewBean recordviewbean = null;
        RecordViewBean recordviewbean1 = null;
        while(iterator.hasNext()) 
        {
            PresentationLayer presentationlayer = (PresentationLayer)iterator.next();
            RecordViewBean arecordviewbean1[] = presentationlayer.getFirstRollEnabledRecords();
            RecordViewBean recordviewbean2 = arecordviewbean1[0];
            RecordViewBean recordviewbean3 = arecordviewbean1[1];
            if(recordviewbean2 != null)
                if(recordviewbean == null)
                    recordviewbean = recordviewbean2;
                else
                if(recordviewbean2.getFirstFieldLine() < recordviewbean.getFirstFieldLine())
                    recordviewbean = recordviewbean2;
            if(recordviewbean3 != null)
                if(recordviewbean1 == null)
                    recordviewbean1 = recordviewbean3;
                else
                if(recordviewbean3.getFirstFieldLine() < recordviewbean1.getFirstFieldLine())
                    recordviewbean1 = recordviewbean3;
        }
        RecordViewBean arecordviewbean[] = {
            recordviewbean, recordviewbean1
        };
        return arecordviewbean;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    private LinkedList _presentationLayers;
    private IRemoveRecord _allRecords;
    private boolean _windowLayer;
    private boolean _keptOnDisplay;
    private CommandKeyLabelList _potentialKeyLabelList;
}
