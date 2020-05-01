// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.code400.dom.constants.FieldType;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.dhtmlview.DhtmlViewBean;
import com.ibm.as400ad.webfacing.runtime.fldformat.FieldDataFormatter;
import com.ibm.as400ad.webfacing.runtime.help.IDisplayHelpInfo;
import com.ibm.as400ad.webfacing.runtime.host.*;
import com.ibm.as400ad.webfacing.runtime.model.*;
import com.ibm.as400ad.webfacing.runtime.model.def.*;
import com.ibm.as400ad.webfacing.runtime.view.def.AIDKey;
import com.ibm.as400ad.webfacing.runtime.view.def.CHKMSGIDDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.DSPATR_PCFieldInfo;
import com.ibm.as400ad.webfacing.runtime.view.def.ERRMSGMessageDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.FieldViewDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordViewDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.MSGMessageDefinition;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            VisibleRectangle, RecordFeedbackBean, ColourIndExprPair, CoveredRecordInfo, 
//            ClearedLines, CursorPosition, LocationOnDevice, IFieldMessageDefinition, 
//            IVisibleRectangle, IBuildRecordViewBean, DisplayAttributeBean, PresentationLayer, 
//            IScreenBuilder, IRemoveRecord, IDisplayRecord

public class RecordViewBean extends VisibleRectangle
    implements IElement, IListable, IBuildRecordViewBean, IDisplayHelpInfo, ENUM_KeywordIdentifiers, Cloneable
{

    public RecordViewBean(IRecordViewDefinition irecordviewdefinition, RecordFeedbackBean recordfeedbackbean)
    {
        _needsToBeDisplayed = false;
        _conceptualLayerZOrder = -1;
        _recordFeedbackBean = null;
        _dspfActive = true;
        _ERRMSGs = new Vector();
        _ERRMSGState = false;
        _previousViewBean = null;
        _newViewBean = null;
        _definition = null;
        _systemRecord = null;
        _dspatrPC_Location = null;
        _chkmsgs = null;
        _protected = false;
        _protectedChangedForImmediateWrite = false;
        _exposedLines = new HashSet();
        _coveringRectangles = new ArrayList();
        _fieldsNotVisible = null;
        _isFormatted = false;
        _definition = irecordviewdefinition;
        _recordFeedbackBean = recordfeedbackbean;
        initRecordViewDefinition(recordfeedbackbean);
        _systemRecord = SystemRecords.getSystemRecord(recordfeedbackbean.getDSPFObject(), getRecordName());
        initializeCHKMSGIDs();
        initializeExposedLines();
        initFieldVisibility();
        initializeMSGIDs();
        Iterator iterator = getRecordViewDefinition().getFieldViewDefinitions();
        getFeedbackBean().initializeMDTs(iterator);
    }

    public void addCoveringRectangle(VisibleRectangle visiblerectangle)
    {
        if(!_coveringRectangles.contains(visiblerectangle))
            _coveringRectangles.add(visiblerectangle);
    }

    void applyCursorPosition(CursorPosition cursorposition)
    {
        if(isWdwDFT())
        {
            int i = getWdwHeight();
            int j = getWdwWidth();
            int k = getMaxRow();
            int l = getMaxColumn();
            if(cursorposition != null)
            {
                int i1 = cursorposition.getRow();
                int j1 = cursorposition.getColumn();
                if(i1 + i + 2 <= k)
                {
                    if(j1 + j + 4 <= l)
                    {
                        _wdwStartLine = i1 + 1;
                        _wdwStartColumn = j1;
                    } else
                    {
                        _wdwStartLine = i1 + 1;
                        _wdwStartColumn = l - j - 4;
                    }
                } else
                if(i1 - i >= 3)
                {
                    if(j1 + j + 4 <= l)
                    {
                        _wdwStartLine = i1 - i - 2;
                        _wdwStartColumn = j1;
                    } else
                    {
                        _wdwStartLine = i1 - i - 2;
                        _wdwStartColumn = l - j - 4;
                    }
                } else
                if(j1 + j + 5 <= l)
                {
                    if(i1 + i + 1 <= k)
                    {
                        _wdwStartLine = i1;
                        _wdwStartColumn = l - j - 4;
                    } else
                    {
                        _wdwStartLine = k - i - 1;
                        _wdwStartColumn = l - j - 4;
                    }
                } else
                if(j1 - j >= 6)
                {
                    if(i1 + i + 1 <= k)
                    {
                        _wdwStartLine = i1;
                        _wdwStartColumn = j1 - j - 5;
                    } else
                    {
                        _wdwStartLine = k - i - 1;
                        _wdwStartColumn = j1 - j - 5;
                    }
                } else
                {
                    _wdwStartLine = k - i - 1;
                    _wdwStartColumn = l - j - 4;
                }
            } else
            {
                _wdwStartLine = k - i - 1;
                _wdwStartColumn = l - j - 4;
            }
            if(_wdwStartLine < 1)
                _wdwStartLine = 1;
            if(_wdwStartColumn < 1)
                _wdwStartColumn = 1;
        }
    }

    void checkForRuntimeErrors()
        throws WFApplicationRuntimeError
    {
    }

    void clearAllERRMSGs()
    {
        _ERRMSGs.clear();
        _ERRMSGState = false;
    }

    protected void clearMDTs(boolean flag)
    {
        getFeedbackBean().clearMDTs(getRecordViewDefinition(), flag);
    }

    public Object clone()
    {
        RecordViewBean recordviewbean = (RecordViewBean)super.clone();
        recordviewbean._ERRMSGs = (Vector)_ERRMSGs.clone();
        recordviewbean._recordFeedbackBean = (RecordFeedbackBean)_recordFeedbackBean.clone();
        recordviewbean._coveringRectangles = (ArrayList)_coveringRectangles.clone();
        return recordviewbean;
    }

    public boolean containLocation(int i, int j)
    {
        return getFirstFieldLine() <= i && i <= getLastFieldLine();
    }

    protected void decorateFieldDataDefinitions(Iterator iterator, IRecordDataDefinition irecorddatadefinition)
    {
        while(iterator.hasNext()) 
        {
            FieldViewDefinition fieldviewdefinition = (FieldViewDefinition)iterator.next();
            String s = fieldviewdefinition.getName();
            fieldviewdefinition.setFieldDataDefinition(irecorddatadefinition.getFieldDefinition(s));
            if(fieldviewdefinition.getKeyboardShift() == 'S' && fieldviewdefinition.isInputCapable())
                fieldviewdefinition.addEditCode('Q');
        }
    }

    public boolean evaluateIndicatorExpression(String s)
    {
        try
        {
            if(!s.startsWith("PF"))
                return getFeedbackBean().evaluateIndicatorExpression(s);
            boolean flag = false;
            StringTokenizer stringtokenizer = new StringTokenizer(s, ",", true);
            String s1 = stringtokenizer.nextToken();
            stringtokenizer.nextToken();
            String s2 = stringtokenizer.nextToken();
            stringtokenizer.nextToken();
            String s3 = "";
            int i = getConvertedPFieldValue(s2);
            if(i % 8 == 7 && s1.equals("PFND") || i / 64 != 0 && s1.equals("PFPR"))
            {
                String s4 = stringtokenizer.hasMoreTokens() ? stringtokenizer.nextToken() : "";
                return getFeedbackBean().evaluateIndicatorExpression(s4);
            }
            if(s1.equals("PFMND") || s1.equals("PFMPR"))
            {
                String s5 = stringtokenizer.nextToken();
                if(s5.equals(","))
                    s5 = "";
                else
                    stringtokenizer.nextToken();
                String s6 = stringtokenizer.hasMoreTokens() ? stringtokenizer.nextToken() : "";
                if(getFeedbackBean().evaluateIndicatorExpression(s5))
                    return i % 8 == 7 && s1.equals("PFMND") || i / 64 != 0 && s1.equals("PFMPR");
                else
                    return getFeedbackBean().evaluateIndicatorExpression(s6);
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

    public String evaluateStyleClass(DisplayAttributeBean displayattributebean)
    {
        StringBuffer stringbuffer = new StringBuffer(30);
        boolean flag = false;
        int i = 32;
        if(displayattributebean.getPFieldName() != null)
            flag = processPFieldForAttrs(displayattributebean);
        String s = displayattributebean.getProtectIndExpr();
        if(s != null && (s.equals("") || evaluateIndicatorExpression(s)))
        {
            stringbuffer.append("wf_");
            stringbuffer.append("pr");
            stringbuffer.append(" ");
            i += 128;
        }
        s = displayattributebean.getBlinkIndExpr();
        if(s != null && (s.equals("") || evaluateIndicatorExpression(s)))
        {
            stringbuffer.append("wf_");
            stringbuffer.append("bl");
            stringbuffer.append(" ");
            i += 8;
        }
        s = displayattributebean.getColSeparatorsIndExpr();
        if(s != null && (s.equals("") || evaluateIndicatorExpression(s)))
        {
            stringbuffer.append("wf_");
            stringbuffer.append("cs");
            stringbuffer.append(" ");
            i += 16;
        }
        s = displayattributebean.getHighlightIndExpr();
        if(s != null && (s.equals("") || evaluateIndicatorExpression(s)))
        {
            stringbuffer.append("wf_");
            stringbuffer.append("hi");
            stringbuffer.append(" ");
            i += 2;
        }
        s = displayattributebean.getUnderlineIndExpr();
        if(s != null)
        {
            stringbuffer.append("wf_");
            if(s.equals("") || evaluateIndicatorExpression(s))
            {
                stringbuffer.append("ul");
                i += 4;
            } else
            {
                stringbuffer.append("borderOff");
            }
            stringbuffer.append(" ");
        } else
        if(flag || displayattributebean.getChginpdftNoUL())
        {
            stringbuffer.append("wf_");
            stringbuffer.append("borderOff");
            stringbuffer.append(" ");
        }
        if(flag)
            processPfieldForColor(displayattributebean, i);
        String s1 = null;
        if(displayattributebean.getColourIndExprVector() != null)
        {
            for(Iterator iterator = displayattributebean.getColourIndExprVector().iterator(); iterator.hasNext();)
            {
                ColourIndExprPair colourindexprpair = (ColourIndExprPair)iterator.next();
                if(colourindexprpair.getIndExpr().equals("") || evaluateIndicatorExpression(colourindexprpair.getIndExpr()))
                {
                    s1 = colourindexprpair.getColour();
                    break;
                }
            }

        }
        s = displayattributebean.getReverseIndExpr();
        boolean flag1 = false;
        if(s != null && (s.equals("") || evaluateIndicatorExpression(s)))
        {
            stringbuffer.append("wf_");
            stringbuffer.append("ri_");
            if(s1 != null)
                stringbuffer.append(s1);
            else
                stringbuffer.append("default");
            stringbuffer.append(" ");
            flag1 = true;
        }
        if(!flag1)
        {
            String s2 = displayattributebean.getFieldName();
            if(s2 != null && isFieldInERRMSGState(s2))
            {
                stringbuffer.append("wf_");
                stringbuffer.append("ri_");
                if(s1 != null)
                    stringbuffer.append(s1);
                else
                    stringbuffer.append("default");
                stringbuffer.append(" ");
                flag1 = true;
            }
        }
        if(!flag1)
        {
            stringbuffer.append("wf_");
            if(s1 != null)
                stringbuffer.append(s1);
            else
                stringbuffer.append("default");
            stringbuffer.append(" ");
        }
        stringbuffer.append("wf_");
        stringbuffer.append("field");
        return stringbuffer.toString();
    }

    ArrayList getActiveCommandKeys()
    {
        if(_activeCommandKeys == null)
        {
            WFSession.getTraceLogger().err(2, "_activeCommandKeys shouldn't be null when RecordViewBean.getActiveCommandKeys() is called");
            _activeCommandKeys = new ArrayList();
        }
        return _activeCommandKeys;
    }

    boolean[] getPageUpDownKeysStatus()
    {
        boolean flag = false;
        boolean flag1 = false;
        ArrayList arraylist = getActiveFunctionKeys();
        int i = arraylist.size();
        for(int j = 0; j < i; j++)
        {
            AIDKey aidkey = (AIDKey)arraylist.get(j);
            if(aidkey.getKeyName().equals("PAGEUP"))
                flag = true;
            else
            if(aidkey.getKeyName().equals("PAGEDOWN"))
                flag1 = true;
        }

        boolean aflag[] = {
            flag, flag1
        };
        return aflag;
    }

    private ArrayList getActiveCommandOrFunctionKeys(Iterator iterator, OptionIndicators optionindicators)
    {
        ArrayList arraylist = new ArrayList();
        if(iterator != null)
            while(iterator.hasNext()) 
            {
                AIDKey aidkey = (AIDKey)iterator.next();
                String s = aidkey.getIndicatorExpression();
                if(null == s)
                {
                    arraylist.add(aidkey.clone());
                } else
                {
                    boolean flag;
                    if(aidkey.getKeyName().equals("PAGEUP") || aidkey.getKeyName().equals("PAGEDOWN"))
                        flag = evaluateIndicatorExpression(s);
                    else
                        flag = optionindicators.evaluateIndicatorExpression(s);
                    if(flag)
                        arraylist.add(aidkey.clone());
                }
            }
        return arraylist;
    }

    ArrayList getActiveFunctionKeys()
    {
        if(_activeFunctionKeys == null)
        {
            WFSession.getTraceLogger().err(2, "_activeFunctionKeys shouldn't be null when RecordViewBean.getActiveFunctionKeys() is called");
            _activeFunctionKeys = new ArrayList();
        }
        return _activeFunctionKeys;
    }

    public String getBeanName()
    {
        return getRecordName();
    }

    public Object getBeanValue()
    {
        return getDisplayRecord();
    }

    public String getCHKMSG(String s)
    {
        String s1 = (String)_chkmsgs.get(s);
        if(s1 == null)
            return "";
        else
            return s1;
    }

    public List getClearedLinesForCoveredByThis(ClearedLines clearedlines, ClearedLines clearedlines1)
    {
        if(!super._coveredByThis.isEmpty())
        {
            for(int i = 0; i < super._coveredByThis.size(); i++)
            {
                CoveredRecordInfo coveredrecordinfo = (CoveredRecordInfo)super._coveredByThis.get(i);
                if(coveredrecordinfo.isOverlappedBy(this))
                {
                    if(coveredrecordinfo.getStartLine() < getFirstFieldLine())
                    {
                        if(clearedlines == null || clearedlines.hasNoRecordToCover())
                            clearedlines = new ClearedLines(coveredrecordinfo.getStartLine(), getFirstFieldLine() - 1);
                        else
                        if(coveredrecordinfo.getStartLine() < clearedlines.getFirstFieldLine())
                            clearedlines.setFirstFieldLine(coveredrecordinfo.getStartLine());
                        CoveredRecordInfo coveredrecordinfo1 = new CoveredRecordInfo(coveredrecordinfo.getRecordViewBean(), coveredrecordinfo.getStartLine(), getFirstFieldLine() - 1);
                        coveredrecordinfo1.getRecordViewBean().addCoveringRectangle(clearedlines);
                        clearedlines.addCoveredByThis(coveredrecordinfo1);
                        coveredrecordinfo.setStartLine(getFirstFieldLine());
                    }
                    if(coveredrecordinfo.getEndLine() > getLastFieldLine())
                    {
                        if(clearedlines1 == null || clearedlines1.hasNoRecordToCover())
                            clearedlines1 = new ClearedLines(getLastFieldLine() + 1, coveredrecordinfo.getEndLine());
                        else
                        if(coveredrecordinfo.getEndLine() > clearedlines1.getLastFieldLine())
                            clearedlines1.setLastFieldLine(coveredrecordinfo.getEndLine());
                        CoveredRecordInfo coveredrecordinfo2 = new CoveredRecordInfo(coveredrecordinfo.getRecordViewBean(), getLastFieldLine(), coveredrecordinfo.getEndLine());
                        coveredrecordinfo2.getRecordViewBean().addCoveringRectangle(clearedlines1);
                        clearedlines1.addCoveredByThis(coveredrecordinfo2);
                        coveredrecordinfo.setEndLine(getLastFieldLine());
                    }
                } else
                if(coveredrecordinfo.getStartLine() < getFirstFieldLine())
                {
                    if(clearedlines == null || clearedlines.hasNoRecordToCover())
                    {
                        clearedlines = new ClearedLines(coveredrecordinfo.getStartLine(), getFirstFieldLine() - 1);
                    } else
                    {
                        int j = coveredrecordinfo.getStartLine() >= clearedlines.getFirstFieldLine() ? clearedlines.getFirstFieldLine() : coveredrecordinfo.getStartLine();
                        clearedlines.setFirstFieldLine(j);
                    }
                    clearedlines.addCoveredByThis(coveredrecordinfo);
                    coveredrecordinfo.getRecordViewBean().addCoveringRectangle(clearedlines);
                    super._coveredByThis.remove(coveredrecordinfo);
                } else
                if(coveredrecordinfo.getEndLine() > getLastFieldLine())
                {
                    if(clearedlines1 == null || clearedlines1.hasNoRecordToCover())
                    {
                        clearedlines1 = new ClearedLines(getLastFieldLine() + 1, coveredrecordinfo.getEndLine());
                    } else
                    {
                        int k = coveredrecordinfo.getEndLine() <= clearedlines1.getLastFieldLine() ? clearedlines1.getLastFieldLine() : coveredrecordinfo.getEndLine();
                        clearedlines1.setLastFieldLine(k);
                    }
                    clearedlines1.addCoveredByThis(coveredrecordinfo);
                    coveredrecordinfo.getRecordViewBean().addCoveringRectangle(clearedlines1);
                    super._coveredByThis.remove(coveredrecordinfo);
                }
            }

        }
        if(clearedlines != null && clearedlines.hasNoRecordToCover())
            clearedlines = null;
        if(clearedlines1 != null && clearedlines1.hasNoRecordToCover())
            clearedlines1 = null;
        ArrayList arraylist = new ArrayList();
        arraylist.add(clearedlines);
        arraylist.add(clearedlines1);
        return arraylist;
    }

    public List getClearedLinesForRecordDifference(int i, int j)
    {
        ArrayList arraylist = new ArrayList();
        if(i < getFirstFieldLine())
            arraylist.add(new ClearedLines(i, getFirstFieldLine() - 1));
        else
            arraylist.add(null);
        if(getLastFieldLine() < j)
            arraylist.add(new ClearedLines(getLastFieldLine() + 1, j));
        else
            arraylist.add(null);
        return arraylist;
    }

    public List getClearedLinesForRecordDifference(RecordViewBean recordviewbean)
    {
        ArrayList arraylist = new ArrayList();
        if(getStartingLineNumber() < recordviewbean.getFirstFieldLine())
            arraylist.add(new ClearedLines(getStartingLineNumber(), recordviewbean.getFirstFieldLine() - 1));
        else
            arraylist.add(null);
        if(recordviewbean.getLastFieldLine() < getLastFieldLine())
            arraylist.add(new ClearedLines(recordviewbean.getLastFieldLine() + 1, getLastFieldLine()));
        else
            arraylist.add(null);
        return arraylist;
    }

    public String getClientScriptJSPName()
    {
        if(_systemRecord != null)
            return "/WF" + _systemRecord.getServletJavascript();
        else
            return "/RecordJSPs/" + WebfacingConstants.replaceSubstring(getSourceQualifiedRecordName(), "$", "/") + "JavaScript" + ".jsp";
    }

    public int getCLRL_END()
    {
        return (getMaxRow() - getStartingLineNumber()) + 1;
    }

    public int getCLRL_NN()
    {
        int i = (getMaxRow() - getStartingLineNumber()) + 1;
        if(i <= _definition.getCLRL_NN())
            return i;
        else
            return _definition.getCLRL_NN();
    }

    public int getConceptualLayerZOrder()
    {
        return _conceptualLayerZOrder;
    }

    public Collection getCoveredRecords(CoveredRecordInfo coveredrecordinfo, VisibleRectangle visiblerectangle)
    {
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();
        for(int i = 0; i < super._coveredByThis.size(); i++)
        {
            CoveredRecordInfo coveredrecordinfo1 = (CoveredRecordInfo)super._coveredByThis.get(i);
            if(coveredrecordinfo1.isOverlappedBy(coveredrecordinfo))
            {
                int j = coveredrecordinfo1.getStartLine();
                int k = coveredrecordinfo1.getEndLine();
                int l = coveredrecordinfo.getStartLine();
                int i1 = coveredrecordinfo.getEndLine();
                int j1 = j;
                int k1 = k;
                RecordViewBean recordviewbean = coveredrecordinfo1.getRecordViewBean();
                if(j < l)
                {
                    arraylist1.add(new CoveredRecordInfo(recordviewbean, j, l));
                    j1 = l;
                }
                if(k > i1)
                {
                    arraylist1.add(new CoveredRecordInfo(recordviewbean, i1, k));
                    k1 = i1;
                }
                if(j1 == j && k1 == k)
                    arraylist.add(coveredrecordinfo1);
                else
                    arraylist.add(new CoveredRecordInfo(recordviewbean, j1, k1));
                recordviewbean.removeCoveringRectangle(this);
                recordviewbean.addCoveringRectangle(visiblerectangle);
            } else
            {
                arraylist1.add(coveredrecordinfo1);
            }
        }

        super._coveredByThis = arraylist1;
        return arraylist;
    }

    public List getCoveringRectangles()
    {
        return _coveringRectangles;
    }

    public CursorPosition getCRSLOC()
    {
        KeywordDefinition keyworddefinition = getRecordViewDefinition().getKeywordDefinition(80L);
        Iterator iterator = keyworddefinition.getParameters();
        int i = Integer.parseInt(getFieldValue(iterator.next().toString()));
        int j = Integer.parseInt(getFieldValue(iterator.next().toString()));
        return new CursorPosition(i, j);
    }

    public String getDate(DateType datetype)
    {
        HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
        return hostjobinfo.getDate(datetype);
    }

    public String getDate(DateType datetype, CenturyType centurytype, char c, char c1)
    {
        HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
        String s = hostjobinfo.getDate(datetype, centurytype);
        byte byte0 = ((byte)(centurytype != CenturyType.TWO_DIGITS ? 8 : 6));
        return FieldDataFormatter.formatString(s, c, c1, "", 0, byte0);
    }

    public String getDate(DateType datetype, CenturyType centurytype, SeparatorType separatortype)
    {
        HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
        return hostjobinfo.getDate(datetype, centurytype, separatortype);
    }

    public String getDate(DateType datetype, CenturyType centurytype, String s)
    {
        HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
        byte byte0 = ((byte)(centurytype != CenturyType.TWO_DIGITS ? 8 : 6));
        return FieldDataFormatter.formatString(hostjobinfo.getDate(datetype, centurytype), '0', '0', s, 0, byte0);
    }

    public IDisplayRecord getDisplayRecord()
    {
        return new DhtmlViewBean(this);
    }

    public int getDisplayZIndex()
    {
        return _displayZIndex;
    }

    public LocationOnDevice getDspatrPC_Location()
    {
        if(_dspatrPC_Location == null)
            initDspatrPC_Location(true);
        return _dspatrPC_Location;
    }

    public HashSet getExposedLines()
    {
        return _exposedLines;
    }

    public RecordFeedbackBean getFeedbackBean()
    {
        return _recordFeedbackBean;
    }

    HashSet getFieldsNotVisibleFromDef(String as[])
    {
        HashSet hashset = new HashSet();
        if(as == null)
            return hashset;
        int i = as.length;
        ArrayList arraylist = new ArrayList();
        for(int k = 0; k < i; k++)
        {
            boolean flag = true;
            StringTokenizer stringtokenizer = new StringTokenizer(as[k], ":");
            String s = stringtokenizer.nextToken();
            arraylist.add(s);
            if(stringtokenizer.hasMoreTokens())
            {
                String s1 = stringtokenizer.nextToken();
                if(s1.equals("NEVER"))
                {
                    flag = false;
                } else
                {
                    flag = evaluateIndicatorExpression(s1);
                    if(flag && stringtokenizer.hasMoreTokens())
                    {
                        for(Iterator iterator = WebfacingConstants.condensedStrToOrderedIntList(stringtokenizer.nextToken()).iterator(); iterator.hasNext();)
                        {
                            int j = ((Integer)iterator.next()).intValue();
                            if(!hashset.contains(arraylist.get(j)))
                            {
                                flag = false;
                                break;
                            }
                        }

                    }
                }
            }
            if(!flag)
                hashset.add(s);
        }

        return hashset;
    }

    public String getFieldValue(String s)
    {
        if(!_isFormatted)
        {
            getFeedbackBean().formatFieldValues(getRecordViewDefinition());
            _isFormatted = true;
        }
        String s1 = _recordFeedbackBean.getFieldValue(s);
        if(s1 == null)
            s1 = "";
        RecordFeedbackBean recordfeedbackbean = getFeedbackBean();
        recordfeedbackbean.setBLANKS_RespIndOnWrite(s, s1);
        return s1;
    }

    public FieldViewDefinition getFieldViewDefinition(String s)
    {
        return getRecordViewDefinition().getFieldViewDefinition(s);
    }

    protected ERRMSGMessageDefinition getFirstActiveERRMSG(boolean flag)
    {
        ERRMSGMessageDefinition errmsgmessagedefinition = null;
        for(Iterator iterator = _ERRMSGs.iterator(); iterator.hasNext();)
        {
            ERRMSGMessageDefinition errmsgmessagedefinition1 = (ERRMSGMessageDefinition)iterator.next();
            if(getFieldViewDefinition(errmsgmessagedefinition1.getFieldName()).isInputCapable() || !flag)
            {
                errmsgmessagedefinition = errmsgmessagedefinition1;
                break;
            }
        }

        return errmsgmessagedefinition;
    }

    public String getFirstActiveHLPTITLEtext()
    {
        for(Iterator iterator = _definition.getHelpTitles(); iterator.hasNext();)
        {
            KeywordDefinition keyworddefinition = (KeywordDefinition)iterator.next();
            String s = keyworddefinition.getIndicatorExpression();
            if(s.equals("") || evaluateIndicatorExpression(s))
            {
                Iterator iterator1 = keyworddefinition.getParameters();
                if(iterator1.hasNext())
                    return (String)iterator1.next();
            }
        }

        return "";
    }

    public int getFirstColumn()
    {
        return _definition.getFirstColumn();
    }

    public int getFirstDisplayLine()
    {
        return getFirstFieldLine();
    }

    public int getFirstFieldLine()
    {
        if(_definition.getFirstFieldLine() == -1)
            return -1;
        else
            return _definition.getFirstFieldLine() + getSLNOVAROffset();
    }

    public int getFirstVisibleRectangleLine()
    {
        return getFirstFieldLine();
    }

    Iterator getHelpDefinitions()
    {
        return getRecordViewDefinition().getHelpDefinitions();
    }

    public Iterator getHelpGroups()
    {
        return getRecordViewDefinition().getHelpGroups();
    }

    public String getJspName()
    {
        if(_systemRecord != null)
            return "/WF" + _systemRecord.getServlet();
        else
            return "/RecordJSPs/" + WebfacingConstants.replaceSubstring(getSourceQualifiedRecordName(), "$", "/") + ".jsp";
    }

    public IKey getKey()
    {
        return new Key(getRecordName());
    }

    public int getLastColumn()
    {
        return _definition.getLastColumn();
    }

    public int getLastDisplayLine()
    {
        return getLastFieldLine();
    }

    public int getLastFieldLine()
    {
        if(_definition.getLastFieldLine() == -1)
            return -1;
        else
            return _definition.getLastFieldLine() + getSLNOVAROffset();
    }

    public int getMaxColumn()
    {
        return getRecordViewDefinition().getMaxColumn();
    }

    public int getMaxRow()
    {
        return getRecordViewDefinition().getMaxRow();
    }

    Iterator getMessages()
    {
        return _ERRMSGs.iterator();
    }

    public RecordViewBean getNewViewBean()
    {
        return _newViewBean;
    }

    Iterator getCommandKeyLabels()
    {
        return getRecordViewDefinition().getCommandKeyLabels();
    }

    Iterator getVisibilityConditionedCommandKeyLabels()
    {
        return getRecordViewDefinition().getVisibilityConditionedCommandKeyLabels();
    }

    Iterator getIndicatorConditionedCommandKeyLabels()
    {
        return getRecordViewDefinition().getIndicatorConditionedCommandKeyLabels();
    }

    RecordViewBean getPreviousViewBean()
    {
        return _previousViewBean;
    }

    public Integer getPrimaryFileDisplaySize()
    {
        return getRecordViewDefinition().getPrimaryFileDisplaySize();
    }

    public int getRecordLength()
    {
        if(getFirstFieldLine() == -1)
            return 0;
        else
            return (getLastFieldLine() - getStartingLineNumber()) + 1;
    }

    public String getRecordName()
    {
        return _definition.getName();
    }

    IRecordViewDefinition getRecordViewDefinition()
    {
        return _definition;
    }

    public Integer getSecondaryFileDisplaySize()
    {
        return getRecordViewDefinition().getSecondaryFileDisplaySize();
    }

    public int getSLNO()
    {
        KeywordDefinition keyworddefinition = _definition.getKeywordDefinition(209L);
        if(keyworddefinition != null)
        {
            String s = (String)keyworddefinition.getParameters().next();
            int i;
            if("*VAR".equals(s))
                i = _recordFeedbackBean.getSLNO();
            else
                i = Integer.parseInt(s);
            if(i > 0)
                return i;
        }
        return 0;
    }

    public int getSLNOVAROffset()
    {
        KeywordDefinition keyworddefinition = _definition.getKeywordDefinition(209L);
        if(keyworddefinition != null && "*VAR".equals((String)keyworddefinition.getParameters().next()))
        {
            int i = _recordFeedbackBean.getSLNO();
            if(i > 0)
                return i - 1;
        }
        return 0;
    }

    public String getSourceQualifiedRecordName()
    {
        return _recordFeedbackBean.getSourceQualifiedRecordName();
    }

    public int getStartingLineNumber()
    {
        int i = getSLNO();
        if(i == 0)
            return getFirstFieldLine();
        else
            return i;
    }

    public String getSystemName()
    {
        HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
        return hostjobinfo.getSystemName();
    }

    public String getSystemTime()
    {
        HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
        char c = hostjobinfo.getTimeSeparator();
        StringBuffer stringbuffer = new StringBuffer("0  ");
        stringbuffer.append(c).append("  ").append(c).append("  ");
        return getSystemTime(stringbuffer.toString());
    }

    public String getSystemTime(char c, char c1)
    {
        return FieldDataFormatter.formatString(getSystemTimeWithoutSep(), c, c1, "", 0, 6);
    }

    public String getSystemTime(String s)
    {
        return FieldDataFormatter.formatString(getSystemTimeWithoutSep(), '0', '0', s, 0, 6);
    }

    private String getSystemTimeWithoutSep()
    {
        HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
        return hostjobinfo.getSystemTime();
    }

    public String getUserID()
    {
        HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
        return hostjobinfo.getUserID();
    }

    public long getVersionDigits()
    {
        if(_definition.getVersionDigits() != 0L)
            return _definition.getVersionDigits();
        else
            return getFeedbackBean().getVersionDigits();
    }

    protected int getConvertedPFieldValue(String s)
    {
        char c = getFeedbackBean().getRecordData().getFieldValue(s).charAt(0);
        return convertToOriginalPFieldValue(c);
    }

    protected int convertToOriginalPFieldValue(char c)
    {
        HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
        String s = hostjobinfo.getPFIELDMappingString();
        int i = s.indexOf(c);
        if(i < 32 && i > -1)
            i += 32;
        else
        if(i >= 32)
            i += 128;
        else
            i = -1;
        return i;
    }

    public int getWdwFirstColumn()
    {
        if(isWdwDFT() || isWdwREF())
            return _wdwStartColumn;
        String s = getRecordViewDefinition().getWdwStartPosField();
        if(null != s)
            return Integer.parseInt(getFieldValue(s));
        else
            return getRecordViewDefinition().getWdwStartPos();
    }

    public int getWdwFirstLine()
    {
        if(isWdwDFT() || isWdwREF())
            return _wdwStartLine;
        String s = getRecordViewDefinition().getWdwStartLineField();
        if(null != s)
            return Integer.parseInt(getFieldValue(s));
        else
            return getRecordViewDefinition().getWdwStartLine();
    }

    public int getWdwHeight()
    {
        return getRecordViewDefinition().getWdwHeight();
    }

    public String getWdwRefName()
    {
        return getRecordViewDefinition().getWdwRefName();
    }

    public int getWdwWidth()
    {
        return getRecordViewDefinition().getWdwWidth();
    }

    public String getWindowTitle()
    {
        return _definition.getWindowTitle(this);
    }

    public String getWindowTitleAlignment()
    {
        return _definition.getWindowTitleAlignment(this);
    }

    /**
     * @deprecated Method getZOrder is deprecated
     */

    public int getZOrder()
    {
        return getDisplayZIndex() - 1;
    }

    private boolean hasERRMessages()
    {
        return getRecordViewDefinition().hasERRMessages();
    }

    public boolean hasNoDedicatedSpaceOnScreen()
    {
        return _noDedicatedSpaceOnScreen;
    }

    public boolean hasNoExposedLines()
    {
        return _exposedLines.isEmpty();
    }

    protected boolean hasWindowTitle()
    {
        return _definition.hasWindowTitle();
    }

    protected void initDspatrPC_Location(boolean flag)
    {
        _dspatrPC_Location = null;
        for(Iterator iterator = getRecordViewDefinition().getDspatrPCFieldInfos(); iterator.hasNext();)
        {
            DSPATR_PCFieldInfo dspatr_pcfieldinfo = (DSPATR_PCFieldInfo)iterator.next();
            if(evaluateIndicatorExpression(dspatr_pcfieldinfo.getIndExpr()) && isFieldVisible(dspatr_pcfieldinfo.getFieldName()))
            {
                _dspatrPC_Location = getLocationOnDevice(null, dspatr_pcfieldinfo.getFieldName());
                return;
            }
        }

        _dspatrPC_Location = new LocationOnDevice();
    }

    void initFieldVisibility()
    {
        _fieldsNotVisible = getFieldsNotVisibleFromDef(getRecordViewDefinition().getFieldVisDef());
    }

    private void initializeCHKMSGIDs()
    {
        HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
        _chkmsgs = new Hashtable();
        CHKMSGIDDefinition chkmsgiddefinition;
        for(Iterator iterator = _definition.getCHKMSGIDDefinitions(); iterator.hasNext(); hostjobinfo.addRequest(new CHKMSGIDRequest(chkmsgiddefinition, chkmsgiddefinition.getMsgData(_recordFeedbackBean))))
        {
            chkmsgiddefinition = (CHKMSGIDDefinition)iterator.next();
            chkmsgiddefinition.setRecordViewBean(this);
        }

    }

    public void initializeExposedLines()
    {
        _exposedLines = new HashSet();
        int i = getLastFieldLine();
        for(int j = getFirstFieldLine(); j <= i; j++)
            _exposedLines.add(new Integer(j));

    }

    private void initializeMSGIDs()
    {
        Iterator iterator = _recordFeedbackBean.getRecordDataDefinition().getMSGIDFieldDefinitions();
        HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
        while(iterator.hasNext()) 
        {
            FieldDataDefinition fielddatadefinition = (FieldDataDefinition)iterator.next();
            if(isFieldVisible(fielddatadefinition.getFieldName()))
            {
                for(Iterator iterator1 = fielddatadefinition.getMSGIDs(); iterator1.hasNext();)
                {
                    MSGIDDefinition msgiddefinition = (MSGIDDefinition)iterator1.next();
                    if(!msgiddefinition.hasIndicatorExpression() || evaluateIndicatorExpression(msgiddefinition.getIndicatorExpression()))
                    {
                        if(!msgiddefinition.isNONESet())
                            try
                            {
                                MSGIDRequest msgidrequest = new MSGIDRequest(msgiddefinition, fielddatadefinition, getFeedbackBean(), getFeedbackBean().getRecordData().getJobCCSID());
                                hostjobinfo.addRequest(msgidrequest);
                            }
                            catch(IllegalArgumentException illegalargumentexception)
                            {
                                String s = fielddatadefinition.getFieldName();
                                _recordFeedbackBean.setOnScreenFieldValue(s, _recordFeedbackBean.getRecordData().getFieldValue(s));
                                WFSession.getTraceLogger().err(3, illegalargumentexception.getMessage());
                            }
                        break;
                    }
                }

            }
        }
    }

    protected void initRecordViewDefinition(RecordFeedbackBean recordfeedbackbean)
    {
        IRecordDataDefinition irecorddatadefinition = recordfeedbackbean.getRecordDataDefinition();
        IRecordViewDefinition irecordviewdefinition = getRecordViewDefinition();
        irecordviewdefinition.setDataDefinition(irecorddatadefinition);
        Iterator iterator = irecordviewdefinition.getFieldViewDefinitions();
        decorateFieldDataDefinitions(iterator, irecorddatadefinition);
    }

    public boolean isCLRL()
    {
        return _definition.isCLRL();
    }

    public boolean isCLRL_ALL()
    {
        return _definition.isCLRL_ALL();
    }

    public boolean isCLRL_END()
    {
        return _definition.isCLRL_END();
    }

    public boolean isCLRL_NN()
    {
        return _definition.isCLRL_NN();
    }

    public boolean isCLRL_NO()
    {
        return _definition.isCLRL_NO();
    }

    boolean isCmdKeyRetained()
    {
        return getRecordViewDefinition().getKeywordDefinition(168L) != null;
    }

    public boolean isCSRLOCActiveAndValid()
    {
        boolean flag = isKeywordActive(80L);
        if(!flag)
            return false;
        KeywordDefinition keyworddefinition = getRecordViewDefinition().getKeywordDefinition(80L);
        Iterator iterator = keyworddefinition.getParameters();
        int i;
        int j;
        try
        {
            i = Integer.parseInt(getFieldValue(iterator.next().toString()));
            j = Integer.parseInt(getFieldValue(iterator.next().toString()));
        }
        catch(Exception exception)
        {
            return false;
        }
        int k;
        int l;
        if(isWindowed())
        {
            k = getWdwHeight();
            l = getWdwWidth();
        } else
        {
            k = getMaxRow();
            l = getMaxColumn();
        }
        return i <= k && j <= l && i >= 1 && j >= 1;
    }

    boolean isDisplayable()
    {
        return true;
    }

    public boolean isDSPFActive()
    {
        return isRecordValidForRTNCSRLOC();
    }

    public boolean isDspfDbcsCapable()
    {
        return _recordFeedbackBean.isDspfDbcsCapable();
    }

    public boolean isERRSFL()
    {
        return getRecordViewDefinition().isERRSFL();
    }

    private boolean isFieldInERRMSGState(String s)
    {
        for(int i = 0; i < _ERRMSGs.size(); i++)
            if(((IFieldMessageDefinition)_ERRMSGs.elementAt(i)).getFieldName().equals(s))
                return true;

        return false;
    }

    public boolean isFieldVisible(String s)
    {
        return !_fieldsNotVisible.contains(s);
    }

    boolean isFunKeyRetained()
    {
        return getRecordViewDefinition().getKeywordDefinition(169L) != null;
    }

    boolean isInERRMSGState()
    {
        return _ERRMSGState;
    }

    boolean isInRecordUpdateState()
    {
        return isInERRMSGState();
    }

    boolean isKeywordActive(long l)
    {
        IRecordViewDefinition irecordviewdefinition = getRecordViewDefinition();
        if(!irecordviewdefinition.isKeywordSpecified(l))
            return false;
        else
            return isKeywordConditioned(irecordviewdefinition.getKeywordDefinition(l));
    }

    boolean isKeywordConditioned(KeywordDefinition keyworddefinition)
    {
        if(keyworddefinition == null)
            return false;
        String s = keyworddefinition.getIndicatorExpression();
        if(s.equals(""))
            return true;
        else
            return evaluateIndicatorExpression(s);
    }

    boolean isKeywordSpecified(long l)
    {
        return getRecordViewDefinition().isKeywordSpecified(l);
    }

    public boolean isMDTOn(String s)
    {
        if(isProtected())
            return false;
        else
            return getFeedbackBean().isMDTOn(s);
    }

    public boolean isOutputOnly()
    {
        return _definition.getIsOutputOnly();
    }

    public boolean isProtected()
    {
        if(_noDedicatedSpaceOnScreen || _protected)
            return true;
        return getPresentationLayer() != null && !getPresentationLayer().isFocusCapable();
    }

    public boolean isReadableButNoDedicatedSpaceOnScreen()
    {
        return getFirstFieldLine() == -1 || isCLRL() && isOutputOnly();
    }

    public boolean isRecordValidForRTNCSRLOC()
    {
        return _dspfActive && !_noDedicatedSpaceOnScreen;
    }

    public boolean isRecordOnTopLayer()
    {
        return WFSession.getScreenBuilderModel().isRecordOnTopLayer(this);
    }

    public boolean isWdwDFT()
    {
        return getRecordViewDefinition().isWdwDFT();
    }

    public boolean isWdwREF()
    {
        return getRecordViewDefinition().isWdwREF();
    }

    public boolean isWide()
    {
        return getRecordViewDefinition().isWide();
    }

    public boolean isWindowed()
    {
        return getRecordViewDefinition().isWindowed();
    }

    boolean needsToBeDisplayed()
    {
        return _needsToBeDisplayed;
    }

    private void prepareAllERRMSGs(RecordViewBean recordviewbean)
    {
        clearAllERRMSGs();
        IRecordViewDefinition irecordviewdefinition = getRecordViewDefinition();
        Iterator iterator = irecordviewdefinition.getERRMSGs();
        HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
        while(iterator.hasNext()) 
        {
            ERRMSGMessageDefinition errmsgmessagedefinition = (ERRMSGMessageDefinition)iterator.next();
            String s = errmsgmessagedefinition.getFieldName();
            if(isFieldVisible(s) && recordviewbean.isFieldVisible(s) && recordviewbean.evaluateIndicatorExpression(errmsgmessagedefinition.getIndicatorExpression()))
            {
                errmsgmessagedefinition.resolveMessageText(hostjobinfo, getNewViewBean().getFeedbackBean());
                _ERRMSGs.add(errmsgmessagedefinition);
            }
        }
        if(!_ERRMSGs.isEmpty())
        {
            _ERRMSGState = true;
            recordviewbean.setERRMSGState(true);
        }
    }

    private boolean processPFieldForAttrs(DisplayAttributeBean displayattributebean)
    {
        try
        {
            String s = displayattributebean.getPFieldIndExpr();
            if(evaluateIndicatorExpression(s))
            {
                int i = getConvertedPFieldValue(displayattributebean.getPFieldName());
                if(i != -1)
                {
                    displayattributebean.setColourIndExprVector(null);
                    displayattributebean.setBlinkIndExpr(null);
                    displayattributebean.setColSeparatorsIndExpr(null);
                    displayattributebean.setHighlightIndExpr(null);
                    displayattributebean.setProtectIndExpr(null);
                    displayattributebean.setReverseIndExpr(null);
                    displayattributebean.setUnderlineIndExpr(null);
                    if(i % 8 != 7)
                    {
                        if(i / 128 != 0)
                        {
                            displayattributebean.setProtectIndExpr(s);
                            i %= 128;
                        }
                        i %= 32;
                        if(i / 16 != 0)
                        {
                            displayattributebean.setColSeparatorsIndExpr(s);
                            i %= 16;
                        }
                        if(i / 8 != 0)
                        {
                            displayattributebean.setBlinkIndExpr(s);
                            i %= 8;
                        }
                        if(i / 4 != 0)
                        {
                            displayattributebean.setUnderlineIndExpr(s);
                            i %= 4;
                        }
                        if(i / 2 != 0)
                        {
                            displayattributebean.setHighlightIndExpr(s);
                            i %= 2;
                        }
                        if(i != 0)
                            displayattributebean.setReverseIndExpr(s);
                    }
                    return true;
                } else
                {
                    return false;
                }
            } else
            {
                return false;
            }
        }
        catch(Exception exception)
        {
            WFSession.getTraceLogger().err(2, " Exception thrown while processing DSPATR(pfield): " + exception);
            return false;
        }
        catch(Throwable throwable)
        {
            WFSession.getTraceLogger().err(2, " Exception thrown while processing DSPATR(pfield): " + throwable);
        }
        return false;
    }

    private void processPfieldForColor(DisplayAttributeBean displayattributebean, int i)
    {
        try
        {
            switch(i % 128)
            {
            case 32: // ' '
            case 36: // '$'
                displayattributebean.addColourIndExpr("default", "");
                break;

            case 34: // '"'
            case 38: // '&'
                displayattributebean.addColourIndExpr("white", "");
                break;

            case 40: // '('
            case 42: // '*'
            case 44: // ','
            case 46: // '.'
                displayattributebean.addColourIndExpr("red", "");
                break;

            case 48: // '0'
            case 52: // '4'
                displayattributebean.addColourIndExpr("turquoise", "");
                break;

            case 50: // '2'
            case 54: // '6'
                displayattributebean.addColourIndExpr("yellow", "");
                break;

            case 58: // ':'
            case 62: // '>'
                displayattributebean.addColourIndExpr("blue", "");
                break;

            case 56: // '8'
            case 60: // '<'
                displayattributebean.addColourIndExpr("pink", "");
                break;

            case 33: // '!'
            case 35: // '#'
            case 37: // '%'
            case 39: // '\''
            case 41: // ')'
            case 43: // '+'
            case 45: // '-'
            case 47: // '/'
            case 49: // '1'
            case 51: // '3'
            case 53: // '5'
            case 55: // '7'
            case 57: // '9'
            case 59: // ';'
            case 61: // '='
            default:
                displayattributebean.addColourIndExpr("default", "");
                break;
            }
        }
        catch(Exception exception)
        {
            WFSession.getTraceLogger().err(2, " Exception thrown while processing DSPATR(pfield) for color: " + exception);
        }
        catch(Throwable throwable)
        {
            WFSession.getTraceLogger().err(2, " Exception thrown while processing DSPATR(pfield) for color: " + throwable);
        }
    }

    protected String printHtmlHeader(String s)
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("<b>" + _resmri.getString("Record_name__"));
        String s1 = getSourceQualifiedRecordName();
        s1 = s1.substring(0, s1.lastIndexOf("$"));
        s1 = WebfacingConstants.replaceSubstring(s1, "$", "/", 0);
        s1 = WebfacingConstants.replaceSubstring(s1, "$", "(");
        stringbuffer.append(s1 + ")" + s);
        stringbuffer.append("</b><br>");
        String s2 = WebfacingConstants.replaceSubstring(_resmri.getString("fields_occupies_lines_"), "&1", Integer.toString(getFirstFieldLine()));
        s2 = WebfacingConstants.replaceSubstring(s2, "&2", Integer.toString(getLastFieldLine()));
        stringbuffer.append(s2 + "<br>");
        stringbuffer.append("<b>");
        stringbuffer.append(_resmri.getString("NamedFields_"));
        stringbuffer.append("</b><br>");
        return stringbuffer.toString();
    }

    protected String printHtmlTableContent(Iterator iterator)
    {
        StringBuffer stringbuffer = new StringBuffer();
        if(iterator.hasNext())
        {
            stringbuffer.append("<table border=2><tr>");
            stringbuffer.append("<td>" + _resmri.getString("Field_name") + "</td>");
            stringbuffer.append("<td>" + _resmri.getString("Indicator_expression") + "</td>");
            stringbuffer.append("<td>" + _resmri.getString("Has_EDTCDE") + "</td>");
            stringbuffer.append("<td>" + _resmri.getString("Field_type_") + "</td>");
            stringbuffer.append("<td>" + _resmri.getString("Field_Length") + "</td>");
            stringbuffer.append("<td>" + _resmri.getString("Decimal_precision") + "</td>");
            stringbuffer.append("</tr>");
            for(; iterator.hasNext(); stringbuffer.append("</tr>"))
            {
                FieldViewDefinition fieldviewdefinition = (FieldViewDefinition)iterator.next();
                String s = fieldviewdefinition.getFieldName();
                stringbuffer.append("<tr>");
                stringbuffer.append("<td>" + s + "</td>");
                stringbuffer.append("<td>" + fieldviewdefinition.getIndicatorExpression() + "</td>");
                stringbuffer.append("<td>" + fieldviewdefinition.hasEditCodeEditWord() + "</td>");
                stringbuffer.append("<td>" + fieldviewdefinition.getDataType().toString() + "</td>");
                stringbuffer.append("<td>" + fieldviewdefinition.getFieldLength() + "</td>");
                stringbuffer.append("<td>" + fieldviewdefinition.getDecimalPrecision() + "</td>");
            }

            stringbuffer.append("</table>");
        }
        return stringbuffer.toString();
    }

    protected void processKeywordDirectives()
        throws WebfacingLevelCheckException
    {
    }

    public void removeCoveringRectangle(VisibleRectangle visiblerectangle)
    {
        if(_coveringRectangles.contains(visiblerectangle))
            _coveringRectangles.remove(visiblerectangle);
    }

    protected StringBuffer removeEditing(String s, StringBuffer stringbuffer)
    {
        return stringbuffer;
    }

    public void removeFromCoveringRectangles(VisibleRectangle visiblerectangle)
    {
        for(int i = 0; i < _coveringRectangles.size(); i++)
        {
            IVisibleRectangle ivisiblerectangle = (IVisibleRectangle)_coveringRectangles.get(i);
            ivisiblerectangle.removeFromCoveredRecords(this);
            visiblerectangle.addCoveredByThis(getCoveredByThis());
            if(ivisiblerectangle instanceof ClearedLines)
            {
                ClearedLines clearedlines = (ClearedLines)ivisiblerectangle;
                if(clearedlines.hasNoRecordToCover() && clearedlines.getPresentationLayer() != null)
                    clearedlines.getPresentationLayer().remove(clearedlines);
            }
        }

    }

    public void removePROTECT()
    {
        _protected = false;
    }

    public void removePROTECTForImmediateWrite()
    {
        if(_protectedChangedForImmediateWrite)
        {
            removePROTECT();
            _protectedChangedForImmediateWrite = false;
        }
    }

    void resetRecordUpdateState()
    {
        clearAllERRMSGs();
    }

    public void setCHKMSG(String s, String s1)
    {
        _chkmsgs.put(s, s1);
    }

    void setConceptualLayerZOrder(int i)
    {
        _conceptualLayerZOrder = i;
    }

    public void setDisplayZIndex(int i)
    {
        _displayZIndex = i;
    }

    void setDSPFActive(boolean flag)
    {
        _dspfActive = flag;
    }

    void setERRMSGState(boolean flag)
    {
        _ERRMSGState = flag;
    }

    void setNeedsToBeDisplayed(boolean flag)
    {
        _needsToBeDisplayed = flag;
    }

    public void setNewViewBean(RecordViewBean recordviewbean)
    {
        _newViewBean = recordviewbean;
    }

    public void setNoDedicatedSpaceOnScreen()
    {
        _noDedicatedSpaceOnScreen = true;
    }

    void setPreviousViewBean(RecordViewBean recordviewbean, IRemoveRecord iremoverecord)
    {
        _previousViewBean = recordviewbean;
        if(null != _previousViewBean)
        {
            if(_previousViewBean.getConceptualLayerZOrder() == -1)
            {
                iremoverecord.remove(_previousViewBean);
                _previousViewBean = null;
            } else
            {
                if(_previousViewBean.getStartingLineNumber() != getStartingLineNumber() || !isKeywordActive(163L))
                    getFeedbackBean().applyDefaultValues(false);
                else
                if(_previousViewBean.getPreviousViewBean() == null)
                    getFeedbackBean().applyDefaultValues(true);
                _previousViewBean.setNewViewBean(this);
                _previousViewBean.setPreviousViewBean(null, iremoverecord);
                if(_previousViewBean.hasERRMessages() && _previousViewBean.getStartingLineNumber() == getStartingLineNumber())
                    _previousViewBean.prepareAllERRMSGs(this);
            }
        } else
        {
            getFeedbackBean().applyDefaultValues(false);
        }
    }

    void setPropertiesForUpdateMode()
    {
        setNeedsToBeDisplayed(true);
    }

    public void setPROTECT()
    {
        _protected = true;
    }

    public void setPROTECTForImmediateWrite()
    {
        if(!_protected)
        {
            setPROTECT();
            _protectedChangedForImmediateWrite = true;
        }
    }

    void setWdwStartColumn(int i)
    {
        _wdwStartColumn = i;
    }

    void setWdwStartLine(int i)
    {
        _wdwStartLine = i;
    }

    public String toHTML()
    {
        StringBuffer stringbuffer = new StringBuffer();
        Iterator iterator = getRecordViewDefinition().getFieldViewDefinitions();
        if(iterator.hasNext())
        {
            stringbuffer.append("<br><hr><br>");
            stringbuffer.append(printHtmlHeader(getRecordName()));
            stringbuffer.append(printHtmlTableContent(iterator));
        }
        return stringbuffer.toString();
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("\n- - - - - - - - - - - - - - - - - - - - - - -");
        stringbuffer.append("\n");
        stringbuffer.append(_resmri.getString("RECORD_VIEW_BEAN"));
        stringbuffer.append("\n");
        stringbuffer.append(_resmri.getString("Record_name__"));
        stringbuffer.append(getRecordName());
        stringbuffer.append(": ");
        String s = WebfacingConstants.replaceSubstring(_resmri.getString("fields_occupies_lines_"), "&1", Integer.toString(getFirstFieldLine()));
        s = WebfacingConstants.replaceSubstring(s, "&2", Integer.toString(getLastFieldLine()));
        stringbuffer.append(s);
        Iterator iterator = getRecordViewDefinition().getFieldViewDefinitions();
        stringbuffer.append("\n");
        stringbuffer.append(_resmri.getString("Fields_"));
        FieldViewDefinition fieldviewdefinition;
        for(; iterator.hasNext(); stringbuffer.append(", " + _resmri.getString("Has_EDTCDE") + ": " + fieldviewdefinition.hasEditCodeEditWord()))
        {
            fieldviewdefinition = (FieldViewDefinition)iterator.next();
            String s1 = fieldviewdefinition.getFieldName();
            stringbuffer.append("\n" + s1);
            stringbuffer.append(", " + getFieldValue(s1));
            stringbuffer.append(", " + _resmri.getString("Indicator_expression") + ": " + fieldviewdefinition.getIndicatorExpression());
        }

        stringbuffer.append("\n- - - - - - - - - - - - - - - - - - - - - - -\n");
        return stringbuffer.toString();
    }

    LocationOnDevice getFirstFocusCapableField()
    {
        if(isProtected() || isOutputOnly())
            return null;
        LocationOnDevice locationondevice = null;
        for(Iterator iterator = getRecordViewDefinition().getFieldViewDefinitions(); iterator.hasNext();)
        {
            FieldViewDefinition fieldviewdefinition = (FieldViewDefinition)iterator.next();
            if(isFieldFocusCapable(fieldviewdefinition))
            {
                LocationOnDevice locationondevice1 = getLocationOnDevice(fieldviewdefinition);
                if(locationondevice1.isBefore(locationondevice))
                    locationondevice = locationondevice1;
            }
        }

        return locationondevice;
    }

    LocationOnDevice getLocationOnDeviceAt(CursorPosition cursorposition)
    {
        for(Iterator iterator = getRecordViewDefinition().getFieldViewDefinitions(); iterator.hasNext();)
        {
            FieldViewDefinition fieldviewdefinition = (FieldViewDefinition)iterator.next();
            if(isFieldVisible(fieldviewdefinition.getFieldName()))
            {
                CursorPosition cursorposition1 = new CursorPosition(cursorposition.getRow() - getSLNOVAROffset(), cursorposition.getColumn());
                if(isCursorWithinField(cursorposition1, fieldviewdefinition))
                {
                    CursorPosition cursorposition2 = fieldviewdefinition.getPosition();
                    int i;
                    if(fieldviewdefinition.getHeight() > 1)
                        i = fieldviewdefinition.getWidth();
                    else
                        i = getMaxColumn();
                    int j = (cursorposition.getRow() - cursorposition2.getRow()) * i + (cursorposition.getColumn() - cursorposition2.getColumn());
                    cursorposition.setColumnOffset(j);
                    return getLocationOnDevice(cursorposition, fieldviewdefinition.getFieldName());
                }
            }
        }

        return getLocationOnDevice(cursorposition);
    }

    LocationOnDevice getLocationOnDeviceAt(String s, int i)
    {
        for(Iterator iterator = getRecordViewDefinition().getFieldViewDefinitions(); iterator.hasNext();)
        {
            FieldViewDefinition fieldviewdefinition = (FieldViewDefinition)iterator.next();
            if(isFieldVisible(fieldviewdefinition.getFieldName()) && s.equals(fieldviewdefinition.getFieldName()))
            {
                int j;
                int k;
                if(fieldviewdefinition.getHeight() > 1)
                {
                    int l = fieldviewdefinition.getWidth();
                    k = fieldviewdefinition.getPosition().getColumn() + i % l;
                    j = fieldviewdefinition.getPosition().getRow() + i / l;
                } else
                {
                    int i1 = getMaxColumn();
                    k = (fieldviewdefinition.getPosition().getColumn() + i) % i1;
                    j = fieldviewdefinition.getPosition().getRow() + (fieldviewdefinition.getPosition().getColumn() + i) / i1;
                }
                j += getSLNOVAROffset();
                CursorPosition cursorposition = new CursorPosition(j, k);
                return getLocationOnDevice(cursorposition, fieldviewdefinition.getFieldName());
            }
        }

        return getLocationOnDevice(new CursorPosition("num:1:1::"));
    }

    LocationOnDevice getNamedFieldAt(CursorPosition cursorposition)
    {
        LocationOnDevice locationondevice = getLocationOnDeviceAt(cursorposition);
        if(locationondevice != null && locationondevice.getField() != null)
            return locationondevice;
        else
            return null;
    }

    LocationOnDevice getLocationOnDevice(CursorPosition cursorposition)
    {
        LocationOnDevice locationondevice = new LocationOnDevice(cursorposition, getDisplayZIndex(), getRecordName());
        if(!isRecordValidForRTNCSRLOC())
            locationondevice.setIsValidForRTNCSRLOC(false);
        return locationondevice;
    }

    LocationOnDevice getLocationOnDevice(FieldViewDefinition fieldviewdefinition)
    {
        return getLocationOnDevice(fieldviewdefinition.getPosition(), fieldviewdefinition.getFieldName());
    }

    LocationOnDevice getLocationOnDevice(CursorPosition cursorposition, String s)
    {
        LocationOnDevice locationondevice = new LocationOnDevice(cursorposition, getDisplayZIndex(), getRecordName(), s);
        if(!isRecordValidForRTNCSRLOC())
            locationondevice.setIsValidForRTNCSRLOC(false);
        return locationondevice;
    }

    boolean isFieldFocusCapable(FieldViewDefinition fieldviewdefinition)
    {
        String s = fieldviewdefinition.getFieldName();
        return fieldviewdefinition.isInputCapable() && isFieldVisible(s) && !isFieldProtected(fieldviewdefinition);
    }

    boolean isFieldProtected(FieldViewDefinition fieldviewdefinition)
    {
        if(isProtected())
            return true;
        else
            return isKeywordConditioned(fieldviewdefinition.getKeywordDefinition(275L));
    }

    boolean isCursorWithinField(CursorPosition cursorposition, FieldViewDefinition fieldviewdefinition)
    {
        int i = getRecordViewDefinition().getMaxColumn();
        int j = cursorposition.getRow();
        int k = cursorposition.getColumn();
        int l = fieldviewdefinition.getPosition().getRow();
        int i1 = fieldviewdefinition.getPosition().getColumn();
        int j1 = fieldviewdefinition.getWidth();
        int k1 = fieldviewdefinition.getHeight();
        if((i1 + j1) - 1 <= i)
        {
            if(j >= l && j < l + k1 && k >= i1 && k < i1 + j1)
                return true;
        } else
        {
            if(j == l && k >= i1 && k <= i)
                return true;
            int l1 = (i - i1) + 1;
            int i2 = (j1 - l1) / i;
            if(j > l && j <= l + i2)
                return true;
            int j2 = l + i2 + 1;
            int k2 = j1 - (l1 + i * i2);
            if(j == j2 && k <= k2)
                return true;
        }
        return false;
    }

    boolean isInSameWindow(RecordViewBean recordviewbean)
    {
        return isWindowed() && recordviewbean != null && recordviewbean.getDisplayZIndex() == getDisplayZIndex();
    }

    boolean isInScope(String s)
    {
        return Integer.parseInt(s) == getDisplayZIndex();
    }

    boolean isSystemMenuRecord()
    {
        return SystemRecords.isSystemMenuRecord(_systemRecord);
    }

    public String getValuesAfterEditing(String s)
    {
        FieldViewDefinition fieldviewdefinition = getFieldViewDefinition(s);
        return applyEditingOnValues(fieldviewdefinition);
    }

    protected String applyEditingOnValues(FieldViewDefinition fieldviewdefinition)
    {
        FieldDataFormatter fielddataformatter = FieldDataFormatter.getFieldFormatterInstance(fieldviewdefinition.getKeyboardShift(), fieldviewdefinition.getDataType());
        String s = fieldviewdefinition.getValues();
        StringBuffer stringbuffer = new StringBuffer();
        if(s != null && s.length() > 0)
        {
            for(StringTokenizer stringtokenizer = new StringTokenizer(s, ";"); stringtokenizer.hasMoreTokens();)
            {
                String s1 = (String)stringtokenizer.nextElement();
                if(s1.length() > 0)
                {
                    s1 = fielddataformatter.applyEditing(fieldviewdefinition, new StringBuffer(s1)).toString();
                    stringbuffer.append(s1);
                    stringbuffer.append(";");
                }
            }

        }
        return stringbuffer.toString();
    }

    public String getActiveKeyName(String s)
    {
        IScreenBuilder iscreenbuilder = WFSession.getScreenBuilderModel();
        if(iscreenbuilder != null)
            return iscreenbuilder.getActiveKeyName(s);
        else
            return "";
    }

    public boolean disableHyperlink(String s, String s1)
    {
        IScreenBuilder iscreenbuilder = WFSession.getScreenBuilderModel();
        if(iscreenbuilder != null)
        {
            if(!iscreenbuilder.isRecordOnTopLayer(this))
                return true;
            if(s != null && getActiveKeyName(s).equals(""))
                return true;
            if(s1 != null && !iscreenbuilder.isRecordActive(s1))
                return true;
        }
        return false;
    }

    public int getZOrderPrefix(String s)
    {
        IScreenBuilder iscreenbuilder = WFSession.getScreenBuilderModel();
        if(iscreenbuilder != null)
            return iscreenbuilder.getConceptualLayerZOrder(s) + 1;
        else
            return 0;
    }

    void initActiveCommandAndFunctionKeys(OptionIndicators optionindicators)
    {
        _activeCommandKeys = getActiveCommandOrFunctionKeys(getRecordViewDefinition().getCommandKeys(), optionindicators);
        _activeFunctionKeys = getActiveCommandOrFunctionKeys(getRecordViewDefinition().getFunctionKeys(), optionindicators);
    }

    public String getLabelForCmdKey(String s)
    {
        for(Iterator iterator = _activeCommandKeys.iterator(); iterator.hasNext();)
        {
            AIDKey aidkey = (AIDKey)iterator.next();
            if(aidkey.getKeyName().equals(s))
                return aidkey.getKeyLabel();
        }

        return s;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 1999-2003.  All Rights Reserved.";
    private static ResourceBundle _resmri;
    private boolean _needsToBeDisplayed;
    private int _conceptualLayerZOrder;
    private int _displayZIndex;
    RecordFeedbackBean _recordFeedbackBean;
    private boolean _dspfActive;
    private Vector _ERRMSGs;
    private boolean _ERRMSGState;
    private RecordViewBean _previousViewBean;
    private RecordViewBean _newViewBean;
    private int _wdwStartColumn;
    private int _wdwStartLine;
    protected IRecordViewDefinition _definition;
    private SystemRecord _systemRecord;
    protected LocationOnDevice _dspatrPC_Location;
    private Hashtable _chkmsgs;
    private boolean _protected;
    private boolean _protectedChangedForImmediateWrite;
    private boolean _noDedicatedSpaceOnScreen;
    private HashSet _exposedLines;
    private ArrayList _coveringRectangles;
    private HashSet _fieldsNotVisible;
    protected boolean _isFormatted;
    private ArrayList _activeCommandKeys;
    private ArrayList _activeFunctionKeys;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
