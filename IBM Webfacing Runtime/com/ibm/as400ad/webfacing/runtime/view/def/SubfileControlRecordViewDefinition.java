// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition;
import com.ibm.as400ad.webfacing.runtime.view.*;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            RecordViewDefinition, SFLMSGMessageDefinition, SFLMSGIDMessageDefinition, FieldViewDefinition, 
//            MSGMessageDefinition, FieldSelectionSubfileHeightInfo, DSPATR_PCFieldInfo, XXXMSGIDDefinition

/**
 * @deprecated Class SubfileControlRecordViewDefinition is deprecated
 */

public class SubfileControlRecordViewDefinition extends RecordViewDefinition
    implements ENUM_KeywordIdentifiers
{

    public SubfileControlRecordViewDefinition(String s)
    {
        super(s);
        _fieldSelectionSubfileHeightInfo = null;
        _SFLDSPCTLCondition = "";
        _SFLDSPCondition = "";
        _subfileRecordsPerRow = 1;
        _subfileAreaFirstRow = 0;
        _subfileAreaHeight = 0;
        _subfileFirstColumn = 0;
        _subfileRecordWidth = 0;
        _SFLLIN = -1;
        _SFLMSGList = new Vector();
        _subfileFieldVisDef = null;
        _SFLROLVALFieldName = null;
    }

    public void add(KeywordDefinition keyworddefinition)
    {
        if(keyworddefinition.getKeywordIdentifier() == 186L)
        {
            String s = keyworddefinition.getIndicatorExpression();
            if(null != s)
                setSFLDSPCondition(s);
        } else
        if(keyworddefinition.getKeywordIdentifier() == 187L)
        {
            String s1 = keyworddefinition.getIndicatorExpression();
            if(null != s1)
                setSFLDSPCTLCondition(s1);
        } else
        if(keyworddefinition.getKeywordIdentifier() == 195L)
        {
            Iterator iterator = keyworddefinition.getParameters();
            String s2 = "";
            if(iterator.hasNext())
                s2 = (String)iterator.next();
            SFLMSGMessageDefinition sflmsgmessagedefinition = new SFLMSGMessageDefinition(s2);
            sflmsgmessagedefinition.setIndicatorExpression(keyworddefinition.getIndicatorExpression());
            if(iterator.hasNext())
                sflmsgmessagedefinition.setResponseIndicator(Integer.parseInt((String)iterator.next()));
            _SFLMSGList.add(sflmsgmessagedefinition);
        } else
        {
            super.add(keyworddefinition);
        }
    }

    public void addForSubfile(DSPATR_PCFieldInfo dspatr_pcfieldinfo)
    {
        super.add(dspatr_pcfieldinfo, "Subfile DSPATR(PC)");
    }

    public void addSFLMSGIDKeyword(XXXMSGIDDefinition xxxmsgiddefinition)
    {
        SFLMSGIDMessageDefinition sflmsgidmessagedefinition = new SFLMSGIDMessageDefinition(xxxmsgiddefinition);
        _SFLMSGList.add(sflmsgidmessagedefinition);
    }

    public void addSubfileFieldViewDefinition(FieldViewDefinition fieldviewdefinition)
    {
        add(fieldviewdefinition, com.ibm.as400ad.webfacing.runtime.view.def.SubfileControlRecordViewDefinition.class);
    }

    public RecordViewBean createViewBean(RecordFeedbackBean recordfeedbackbean)
        throws WebfacingInternalException
    {
        try
        {
            return new SubfileControlRecordViewBean(this, (SubfileControlRecordFeedbackBean)recordfeedbackbean);
        }
        catch(ClassCastException classcastexception)
        {
            throw new WebfacingInternalException(WebfacingConstants.replaceSubstring(_resmri.getString("WF0044"), "&1", getName()));
        }
    }

    public FieldSelectionSubfileHeightInfo getFieldSelectionSubfileHeightInfo()
    {
        return _fieldSelectionSubfileHeightInfo;
    }

    public String getSFLDSPCondition()
    {
        return _SFLDSPCondition;
    }

    public String getSFLDSPCTLCondition()
    {
        return _SFLDSPCTLCondition;
    }

    public Iterator getSFLMSGs()
    {
        return _SFLMSGList.iterator();
    }

    public String getSFLROLVALFieldName()
    {
        return _SFLROLVALFieldName;
    }

    public int getSubfileAreaFirstRow()
    {
        return _subfileAreaFirstRow;
    }

    public int getSubfileAreaHeight()
    {
        return _subfileAreaHeight;
    }

    public Iterator getSubfileDspatrPCFieldInfos()
    {
        return iterator("Subfile DSPATR(PC)");
    }

    public FieldViewDefinition getSubfileFieldViewDefinition(String s)
    {
        return (FieldViewDefinition)get(s, com.ibm.as400ad.webfacing.runtime.view.def.SubfileControlRecordViewDefinition.class);
    }

    public Iterator getSubfileFieldViewDefinitions()
    {
        return iterator(com.ibm.as400ad.webfacing.runtime.view.def.SubfileControlRecordViewDefinition.class);
    }

    public String[] getSubfileFieldVisDef()
    {
        return _subfileFieldVisDef;
    }

    public int getSubfileRecordsPerRow()
    {
        return _subfileRecordsPerRow;
    }

    public boolean hasFieldSelectionOnSubfile()
    {
        return _fieldSelectionSubfileHeightInfo != null;
    }

    public void setFieldSelectionSubfileHeightInfo(FieldSelectionSubfileHeightInfo fieldselectionsubfileheightinfo)
    {
        _fieldSelectionSubfileHeightInfo = fieldselectionsubfileheightinfo;
    }

    private void setSFLDSPCondition(String s)
    {
        _SFLDSPCondition = s;
    }

    private void setSFLDSPCTLCondition(String s)
    {
        _SFLDSPCTLCondition = s;
    }

    public void setSFLROLVALFieldName(String s)
    {
        _SFLROLVALFieldName = s;
    }

    public void setSubfileAreaFirstRow(int i)
    {
        _subfileAreaFirstRow = i;
    }

    public void setSubfileAreaHeight(int i)
    {
        _subfileAreaHeight = i;
    }

    public void setSubfileFieldVisDef(String as[])
    {
        _subfileFieldVisDef = as;
    }

    public void setSubfileRecordsPerRow(int i)
    {
        _subfileRecordsPerRow = i;
    }

    public int getSubfileFirstColumn()
    {
        return _subfileFirstColumn;
    }

    public void setSubfileFirstColumn(int i)
    {
        _subfileFirstColumn = i;
    }

    public int getSubfileRecordWidth()
    {
        return _subfileRecordWidth;
    }

    public void setSubfileRecordWidth(int i)
    {
        _subfileRecordWidth = i;
    }

    public int getSFLLIN()
    {
        return _SFLLIN;
    }

    public void setSFLLIN(int i)
    {
        _SFLLIN = i;
    }

    public boolean isKeywordSpecified(long l)
    {
        if(l == 192L)
            return getSFLLIN() != -1;
        else
            return super.isKeywordSpecified(l);
    }

    public int getSFLCTLFirstFieldLine()
    {
        if(getFirstFieldLine() == _subfileAreaFirstRow)
        {
            int i = (_subfileAreaFirstRow + _subfileAreaHeight) - 1;
            if(getLastFieldLine() == i)
                return -1;
            else
                return i + 1;
        } else
        {
            return getFirstFieldLine();
        }
    }

    public int getSFLCTLLastFieldLine()
    {
        if(getFirstFieldLine() == _subfileAreaFirstRow)
        {
            if(getLastFieldLine() == (_subfileAreaFirstRow + _subfileAreaHeight) - 1)
                return -1;
            else
                return getLastFieldLine();
        } else
        {
            return _subfileAreaFirstRow - 1;
        }
    }

    private static ResourceBundle _resmri;
    private FieldSelectionSubfileHeightInfo _fieldSelectionSubfileHeightInfo;
    private String _SFLDSPCTLCondition;
    private String _SFLDSPCondition;
    private int _subfileRecordsPerRow;
    private int _subfileAreaFirstRow;
    private int _subfileAreaHeight;
    private int _subfileFirstColumn;
    private int _subfileRecordWidth;
    private static final int SFLLIN_NOT_SPECIFIED = -1;
    private int _SFLLIN;
    private static final String SUBFILE_DSPATR_PCS = "Subfile DSPATR(PC)";
    private Vector _SFLMSGList;
    private String _subfileFieldVisDef[];
    private String _SFLROLVALFieldName;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
