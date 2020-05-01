// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.help.HelpArea;
import com.ibm.as400ad.webfacing.runtime.help.HelpGroup;
import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;
import com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition;
import com.ibm.as400ad.webfacing.runtime.view.RecordFeedbackBean;
import com.ibm.as400ad.webfacing.runtime.view.RecordViewBean;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            ERRMSGMessageDefinition, FieldViewDefinition, WindowTitleDefinition, IRecordViewDefinition, 
//            AIDKey, CommandKeyLabel, MSGMessageDefinition, CHKMSGIDDefinition, 
//            VisibilityConditionedCommandKeyLabel, IndicatorConditionedCommandKeyLabel, DSPATR_PCFieldInfo

/**
 * @deprecated Class RecordViewDefinition is deprecated
 */

public class RecordViewDefinition extends ElementContainer
    implements ENUM_KeywordIdentifiers, IRecordViewDefinition
{

    public RecordViewDefinition(String s)
    {
        super(s);
        _versionDigits = 0L;
        _recordDataDef = null;
        _ERASEList = new Vector();
        _ERRMSGList = new Vector();
        _firstColumn = 1;
        _lastColumn = 1;
        _wdwStartLine = 1;
        _wdwStartLineField = null;
        _wdwStartPos = 1;
        _wdwStartPosField = null;
        _primaryFileDisplaySize = null;
        _secondaryFileDisplaySize = null;
        _isWdwDFT = false;
        _isWdwREF = false;
        _isWide = false;
        _isWindowed = false;
        _wdwHeight = -1;
        _wdwRefName = null;
        _wdwWidth = -1;
        _wdwTitles = new ArrayList();
        _CLRLParm = 0;
        _fieldVisDef = null;
        _helpTitles = new ArrayList();
    }

    public void add(HelpArea helparea)
    {
        super.add(helparea, com.ibm.as400ad.webfacing.runtime.help.HelpArea.class);
    }

    public void add(HelpGroup helpgroup)
    {
        super.add(helpgroup);
    }

    public void add(KeywordDefinition keyworddefinition)
    {
        if(keyworddefinition.getKeywordIdentifier() == 226L)
        {
            _isWindowed = true;
            Iterator iterator = keyworddefinition.getParameters();
            _wdwHeight = Integer.parseInt((String)iterator.next());
            _wdwWidth = Integer.parseInt((String)iterator.next());
            if(iterator.hasNext())
            {
                String s = (String)iterator.next();
                if(s.equals("*DFT"))
                {
                    _isWdwDFT = true;
                } else
                {
                    _isWdwREF = true;
                    _wdwRefName = s;
                }
            }
        } else
        if(keyworddefinition.getKeywordIdentifier() == 97L)
            _ERASEList.add(keyworddefinition);
        else
        if(keyworddefinition.getKeywordIdentifier() == 74L)
        {
            Iterator iterator1 = keyworddefinition.getParameters();
            if(iterator1.hasNext())
            {
                String s1 = (String)iterator1.next();
                if(s1.equals("*NO"))
                    _CLRLParm = 30;
                else
                if(s1.equals("*END"))
                    _CLRLParm = 29;
                else
                if(s1.equals("*ALL"))
                    _CLRLParm = 28;
                else
                    _CLRLParm = Integer.parseInt(s1);
            }
        } else
        if(keyworddefinition.getKeywordIdentifier() == 122L)
            _helpTitles.add(keyworddefinition);
        else
            super.add(keyworddefinition);
    }

    public void add(AIDKey aidkey)
    {
        if(aidkey.getRecordName() == null)
            aidkey.setRecordName(getName());
        if(aidkey.isCommandKey())
            super.add(aidkey, "Command Keys");
        else
            super.add(aidkey, "Function Keys");
        super.add(aidkey, "Key Sequence");
    }

    public void add(CHKMSGIDDefinition chkmsgiddefinition)
    {
        super.add(chkmsgiddefinition);
    }

    public void add(CommandKeyLabel commandkeylabel)
    {
        if(commandkeylabel.getRecordName() == null)
            commandkeylabel.setRecordName(getName());
        super.add(commandkeylabel, "Command Key Labels");
    }

    public void add(VisibilityConditionedCommandKeyLabel visibilityconditionedcommandkeylabel)
    {
        super.add(visibilityconditionedcommandkeylabel, "Visibility Conditioned Key Labels");
    }

    public void add(IndicatorConditionedCommandKeyLabel indicatorconditionedcommandkeylabel)
    {
        super.add(indicatorconditionedcommandkeylabel, "Indicator Conditioned Key Labels");
    }

    public void add(DSPATR_PCFieldInfo dspatr_pcfieldinfo)
    {
        super.add(dspatr_pcfieldinfo);
    }

    public void add(FieldViewDefinition fieldviewdefinition)
    {
        if(fieldviewdefinition.hasERRMessages())
        {
            ERRMSGMessageDefinition errmsgmessagedefinition;
            for(Iterator iterator = fieldviewdefinition.getERRMSGs(); iterator.hasNext(); _ERRMSGList.add(errmsgmessagedefinition))
            {
                errmsgmessagedefinition = (ERRMSGMessageDefinition)iterator.next();
                errmsgmessagedefinition.setRecordName(getName());
            }

        }
        super.add(fieldviewdefinition);
    }

    public void add(WindowTitleDefinition windowtitledefinition)
    {
        _wdwTitles.add(windowtitledefinition);
    }

    public RecordViewBean createViewBean(RecordFeedbackBean recordfeedbackbean)
        throws WebfacingInternalException
    {
        return new RecordViewBean(this, recordfeedbackbean);
    }

    public Iterator getCHKMSGIDDefinitions()
    {
        return iterator(com.ibm.as400ad.webfacing.runtime.view.def.CHKMSGIDDefinition.class);
    }

    public int getCLRL_NN()
    {
        return _CLRLParm;
    }

    public Iterator getCommandKeys()
    {
        return iterator("Command Keys");
    }

    public Iterator getDspatrPCFieldInfos()
    {
        return iterator(com.ibm.as400ad.webfacing.runtime.view.def.DSPATR_PCFieldInfo.class);
    }

    public Iterator getERASEKeywords()
    {
        return _ERASEList.iterator();
    }

    public Iterator getERRMSGs()
    {
        return _ERRMSGList.iterator();
    }

    public FieldViewDefinition getFieldViewDefinition(String s)
    {
        return (FieldViewDefinition)get(s, com.ibm.as400ad.webfacing.runtime.view.def.FieldViewDefinition.class);
    }

    public Iterator getFieldViewDefinitions()
    {
        return iterator(com.ibm.as400ad.webfacing.runtime.view.def.FieldViewDefinition.class);
    }

    public String[] getFieldVisDef()
    {
        return _fieldVisDef;
    }

    public int getFirstColumn()
    {
        return _firstColumn;
    }

    public int getFirstFieldLine()
    {
        return _firstFieldLine;
    }

    public String getFirstParmOnKeyword(long l)
    {
        KeywordDefinition keyworddefinition = getKeywordDefinition(l);
        if(keyworddefinition != null)
        {
            Iterator iterator = keyworddefinition.getParameters();
            if(iterator.hasNext())
            {
                Object obj = iterator.next();
                if(obj instanceof String)
                    return (String)obj;
            }
        }
        return null;
    }

    public Iterator getFunctionKeys()
    {
        return iterator("Function Keys");
    }

    public Iterator getHelpDefinitions()
    {
        return iterator(com.ibm.as400ad.webfacing.runtime.help.HelpArea.class);
    }

    public Iterator getHelpGroups()
    {
        return iterator(com.ibm.as400ad.webfacing.runtime.help.HelpGroup.class);
    }

    public boolean getIsOutputOnly()
    {
        return _isOutputOnly;
    }

    public Iterator getKeySequence()
    {
        return iterator("Key Sequence");
    }

    public KeywordDefinition getKeywordDefinition(long l)
    {
        return (KeywordDefinition)get((new Long(l)).toString(), com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition.class);
    }

    public Iterator getKeywordDefinitions()
    {
        return iterator(com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition.class);
    }

    public int getLastColumn()
    {
        return _lastColumn;
    }

    public int getLastFieldLine()
    {
        return _lastFieldLine;
    }

    public int getMaxColumn()
    {
        return !_isWide ? 80 : 132;
    }

    public int getMaxRow()
    {
        return !_isWide ? 24 : 27;
    }

    public Iterator getCommandKeyLabels()
    {
        return iterator("Command Key Labels");
    }

    public Iterator getVisibilityConditionedCommandKeyLabels()
    {
        return iterator("Visibility Conditioned Key Labels");
    }

    public Iterator getIndicatorConditionedCommandKeyLabels()
    {
        return iterator("Indicator Conditioned Key Labels");
    }

    public Integer getPrimaryFileDisplaySize()
    {
        return _primaryFileDisplaySize;
    }

    public Integer getSecondaryFileDisplaySize()
    {
        return _secondaryFileDisplaySize;
    }

    public long getVersionDigits()
    {
        return _versionDigits;
    }

    public int getWdwHeight()
    {
        return _wdwHeight;
    }

    public String getWdwRefName()
    {
        return _wdwRefName;
    }

    public int getWdwStartLine()
    {
        return _wdwStartLine;
    }

    public String getWdwStartLineField()
    {
        return _wdwStartLineField;
    }

    public int getWdwStartPos()
    {
        return _wdwStartPos;
    }

    public String getWdwStartPosField()
    {
        return _wdwStartPosField;
    }

    public int getWdwWidth()
    {
        return _wdwWidth;
    }

    public int getWindowStartLine()
    {
        return _wdwStartLine;
    }

    public String getWindowTitle(RecordViewBean recordviewbean)
    {
        String s = "";
        if(!_wdwTitles.isEmpty())
        {
            for(int i = 0; s.equals("") && i < _wdwTitles.size(); i++)
            {
                WindowTitleDefinition windowtitledefinition = (WindowTitleDefinition)_wdwTitles.get(i);
                if(windowtitledefinition.isActive(recordviewbean))
                    s = windowtitledefinition.getWindowTitle(recordviewbean, _wdwWidth);
            }

        }
        return s;
    }

    public String getWindowTitleAlignment(RecordViewBean recordviewbean)
    {
        String s = "";
        if(!_wdwTitles.isEmpty())
        {
            for(int i = 0; s.equals("") && i < _wdwTitles.size(); i++)
            {
                WindowTitleDefinition windowtitledefinition = (WindowTitleDefinition)_wdwTitles.get(i);
                if(windowtitledefinition.isActive(recordviewbean))
                    s = windowtitledefinition.getWindowTitleAlignment();
            }

        }
        return s;
    }

    public boolean hasERRMessages()
    {
        return !_ERRMSGList.isEmpty();
    }

    public boolean hasWindowTitle()
    {
        return !_wdwTitles.isEmpty();
    }

    public boolean isCLRL()
    {
        return _CLRLParm != 0;
    }

    public boolean isCLRL_ALL()
    {
        return _CLRLParm == 28;
    }

    public boolean isCLRL_END()
    {
        return _CLRLParm == 29;
    }

    public boolean isCLRL_NN()
    {
        return _CLRLParm >= 1 && _CLRLParm <= 27;
    }

    public boolean isCLRL_NO()
    {
        return _CLRLParm == 30;
    }

    public boolean isKeywordSpecified(long l)
    {
        if(l == 97L)
            return _ERASEList.size() != 0;
        else
            return getKeywordDefinition(l) != null;
    }

    public boolean isWdwDFT()
    {
        return _isWdwDFT;
    }

    public boolean isWdwREF()
    {
        return _isWdwREF;
    }

    public boolean isWide()
    {
        return _isWide;
    }

    public boolean isWindowed()
    {
        return _isWindowed;
    }

    public void setDataDefinition(IRecordDataDefinition irecorddatadefinition)
    {
        _recordDataDef = irecorddatadefinition;
    }

    public void setFieldVisDef(String as[])
    {
        _fieldVisDef = as;
    }

    public void setFirstColumn(int i)
    {
        _firstColumn = i;
    }

    public void setFirstFieldLine(int i)
    {
        _firstFieldLine = i;
    }

    public void setIsOutputOnly(boolean flag)
    {
        _isOutputOnly = flag;
    }

    public void setIsWide(boolean flag)
    {
        _isWide = flag;
    }

    public void setLastColumn(int i)
    {
        _lastColumn = i;
    }

    public void setLastFieldLine(int i)
    {
        _lastFieldLine = i;
    }

    public void setPrimaryFileDisplaySize(Integer integer)
    {
        _primaryFileDisplaySize = integer;
    }

    public void setSecondaryFileDisplaySize(Integer integer)
    {
        _secondaryFileDisplaySize = integer;
    }

    public void setVersionDigits(long l)
    {
        _versionDigits = l;
    }

    public void setWdwStartLine(int i)
    {
        _wdwStartLine = i;
    }

    public void setWdwStartLineField(String s)
    {
        _wdwStartLineField = s;
    }

    public void setWdwStartPos(int i)
    {
        _wdwStartPos = i;
    }

    public void setWdwStartPosField(String s)
    {
        _wdwStartPosField = s;
    }

    public boolean isERRSFL()
    {
        return isKeywordSpecified(101L);
    }

    public Iterator getHelpTitles()
    {
        return _helpTitles.iterator();
    }

    public Iterator getWindowTitles()
    {
        return _wdwTitles.listIterator();
    }

    public String toString()
    {
        String s = "Record View Definition " + getName() + " " + this;
        return s;
    }

    private long _versionDigits;
    IRecordDataDefinition _recordDataDef;
    private boolean _isOutputOnly;
    private Vector _ERASEList;
    private Vector _ERRMSGList;
    private int _firstFieldLine;
    private int _lastFieldLine;
    private int _firstColumn;
    private int _lastColumn;
    private int _wdwStartLine;
    private String _wdwStartLineField;
    private int _wdwStartPos;
    private String _wdwStartPosField;
    private Integer _primaryFileDisplaySize;
    private Integer _secondaryFileDisplaySize;
    private boolean _isWdwDFT;
    private boolean _isWdwREF;
    private boolean _isWide;
    private boolean _isWindowed;
    private int _wdwHeight;
    private String _wdwRefName;
    private int _wdwWidth;
    private List _wdwTitles;
    static final int CLRL_ALL = 28;
    static final int CLRL_END = 29;
    static final int CLRL_NO = 30;
    private int _CLRLParm;
    private String _fieldVisDef[];
    private List _helpTitles;
}
