// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.convert.ExportHandler;
import com.ibm.as400ad.webfacing.convert.gen.bean.JavaSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.ClientScriptSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.rules.*;
import com.ibm.as400ad.webfacing.runtime.view.CommandKeyLabelList;
import com.ibm.as400ad.webfacing.runtime.view.def.*;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            FieldOutput, FieldWebSettings, SpecialCharHandler, FieldVisibility, 
//            FieldLines, RecordLayout

public class ConstantFieldOutput extends FieldOutput
{

    public ConstantFieldOutput(FieldNode fieldnode)
    {
        super(fieldnode);
        _fieldTextReplacedByBlanks = null;
        _fieldTextAfterMasking = null;
        _fieldName = null;
        initialize();
    }

    public ClientScriptSourceCodeCollection getClientScript()
    {
        ClientScriptSourceCodeCollection clientscriptsourcecodecollection = new ClientScriptSourceCodeCollection();
        return clientscriptsourcecodecollection;
    }

    public JavaSourceCodeCollection getDataBeanInitialization()
    {
        JavaSourceCodeCollection javasourcecodecollection = new JavaSourceCodeCollection();
        return javasourcecodecollection;
    }

    public String getFieldId()
    {
        return getFieldNode().getParentRecord().getWebName() + "$" + getFieldName();
    }

    public String getFieldName()
    {
        if(_fieldName == null)
            _fieldName = "Unnamed" + getFieldNode().getParentRecord().getUnnamedFieldIndex();
        return _fieldName;
    }

    public String getFieldText()
    {
        if(_fieldTextReplacedByBlanks == null)
            return "";
        else
            return _fieldTextReplacedByBlanks;
    }

    private String getFieldTextAfterMasking()
    {
        if(_fieldTextAfterMasking == null)
            if(super._webSettings.getText() != null)
                _fieldTextAfterMasking = super._webSettings.masking(super._webSettings.getText());
            else
                _fieldTextAfterMasking = super._webSettings.masking(getFieldText());
        return _fieldTextAfterMasking;
    }

    public String getFieldTextWithTransform(int i)
    {
        return getFieldTextWithTransform(i, super.INDEX_NOT_USED, super.INDEX_NOT_USED);
    }

    public String getFieldTextWithTransform(int i, int j, int k)
    {
        String s = getFieldTextAfterMasking();
        if(j != super.INDEX_NOT_USED && k != super.INDEX_NOT_USED)
            s = WebfacingConstants.softSubstring(s, j, k);
        return super._charHandler.transformSpecialChars(s, i);
    }

    public String getPrgDefineHTML()
    {
        return getSampleText();
    }

    public JavaSourceCodeCollection getViewBeanInitialization()
    {
        JavaSourceCodeCollection javasourcecodecollection = new JavaSourceCodeCollection();
        return javasourcecodecollection;
    }

    public boolean hasKeyLabelDetected()
    {
        if(_fieldTextReplacedByBlanks == null && getSampleText() == null)
            return false;
        else
            return !_fieldTextReplacedByBlanks.equals(getSampleText());
    }

    protected void initialize()
    {
        initializeFieldText();
        super.initialize();
    }

    void initializeFieldText()
    {
        if(_fieldTextReplacedByBlanks == null)
        {
            _fieldTextReplacedByBlanks = updateStringMatchedUnitList(getSampleText());
            _fieldTextReplacedByBlanks = updateDSPFMenuOptionList(_fieldTextReplacedByBlanks);
            if(_fieldTextReplacedByBlanks == null)
                _fieldTextReplacedByBlanks = "";
        }
    }

    protected void initializeWidthAndHeight()
    {
        if(super._webSettings.getText() != null)
            super._width = super._webSettings.getTextDisplayLength();
        else
            super._width = getFieldNode().getDisplayLength();
    }

    public boolean isComputed()
    {
        return false;
    }

    private String updateDSPFMenuOptionList(String s)
    {
        String s1 = s;
        try
        {
            Vector vector = RuletFactory.getRuletFactory().getRulets(RuletType.DSPF_MENU_PATTERN);
            if(vector != null)
            {
                for(int i = 0; i < vector.size(); i++)
                {
                    DSPFMenuPatternRulet dspfmenupatternrulet = (DSPFMenuPatternRulet)vector.elementAt(i);
                    StringMatchingResult stringmatchingresult = (StringMatchingResult)dspfmenupatternrulet.apply(s1);
                    if(stringmatchingresult != null && stringmatchingresult.getResultContainer() != null)
                    {
                        s1 = stringmatchingresult.getReplacedString();
                        for(Enumeration enumeration = stringmatchingresult.getResultContainer().elements(); enumeration.hasMoreElements();)
                        {
                            Unit unit = (Unit)enumeration.nextElement();
                            String s2 = String.valueOf(unit.sequenceNumber);
                        }

                    }
                }

            }
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in ViewBeanGenerator.updateMenuOptionList() while generating " + getBeanName() + " = " + throwable);
        }
        return s1;
    }

    private String updateStringMatchedUnitList(String s)
    {
        String s1 = s;
        try
        {
            _stringMatchedUnitList = new ArrayList();
            Vector vector = RuletFactory.getRuletFactory().getRulets(RuletType.KEY_PATTERN);
            if(vector != null)
            {
                for(int i = 0; i < vector.size(); i++)
                {
                    KeyPatternRulet keypatternrulet = (KeyPatternRulet)vector.elementAt(i);
                    StringMatchingResult stringmatchingresult = (StringMatchingResult)keypatternrulet.apply(s1);
                    if(stringmatchingresult != null && stringmatchingresult.getResultContainer() != null)
                    {
                        s1 = stringmatchingresult.getReplacedString();
                        Enumeration enumeration = stringmatchingresult.getResultContainer().elements();
                        if(enumeration.hasMoreElements())
                        {
                            PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s1);
                            paddedstringbuffer.truncateAllSpacesFromRight();
                            s1 = paddedstringbuffer.toString();
                        }
                        Unit unit;
                        for(; enumeration.hasMoreElements(); _stringMatchedUnitList.add(unit))
                            unit = (Unit)enumeration.nextElement();

                    }
                }

            }
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in ConstantFieldOutput.updateStringMatchedUnitList() while generating " + getBeanName() + " = " + throwable);
        }
        return s1;
    }

    public String getChangeCursor()
    {
        return getSetCursor();
    }

    public void updateStringMatchedKeyLabelList()
    {
        FieldVisibility fieldvisibility = getFieldVisibility();
        String s = getFieldNode().getIndicatorString();
        super._commandKeyLabelList = new CommandKeyLabelList();
        super._visibilityConditionedCommandKeyLabelList = new CommandKeyLabelList();
        for(int i = 0; i < _stringMatchedUnitList.size(); i++)
        {
            Unit unit = (Unit)_stringMatchedUnitList.get(i);
            String s1 = String.valueOf(unit.sequenceNumber);
            if(s1.length() == 1)
                s1 = "0" + s1;
            if(fieldvisibility.isAlwaysVisible())
                super._commandKeyLabelList.add(new CommandKeyLabel(s1, unit.text, 2));
            else
            if(fieldvisibility.isConditionallyVisible())
            {
                VisibilityConditionedCommandKeyLabel visibilityconditionedcommandkeylabel = (VisibilityConditionedCommandKeyLabel)super._visibilityConditionedCommandKeyLabelList.getLabel(s1);
                if(visibilityconditionedcommandkeylabel == null)
                    visibilityconditionedcommandkeylabel = new VisibilityConditionedCommandKeyLabel(s1, null, null, 2);
                visibilityconditionedcommandkeylabel.addAConditionedLabel(new VisibilityConditionedLabel(unit.text, getFieldName()));
                super._visibilityConditionedCommandKeyLabelList.add(visibilityconditionedcommandkeylabel);
            }
        }

    }

    FieldLines getGeneratedDHTML()
    {
        FieldLines fieldlines = super.getGeneratedDHTML();
        if(getFieldNode().getParentRecord().getFile().getMemberType().equals("MNUDDS"))
        {
            MNUDDSPatternHandler mnuddspatternhandler = MNUDDSPatternHandler.getMNUDDSPatternHandler();
            if(mnuddspatternhandler.getOption() != null)
            {
                int i = mnuddspatternhandler.getOptionNo(getSampleText());
                if(i > -1 && mnuddspatternhandler.getOption().toLowerCase().equals("hypertextlink"))
                    wrapWithATag(fieldlines, i);
            }
        }
        return fieldlines;
    }

    private void wrapWithATag(FieldLines fieldlines, int i)
    {
        for(int j = 0; j < fieldlines.size(); j++)
        {
            String s = fieldlines.get(j);
            s = "<A href=\"#\" onclick=\"setOptionAndSubmit('" + i + "');\">" + s + "</A>";
            fieldlines.set(j, s);
        }

    }

    void wrapWithSPANTag(FieldLines fieldlines, boolean flag)
    {
        String s4 = "";
        if(!flag)
            s4 = getStyleClass();
        for(int i = 0; i < fieldlines.size(); i++)
        {
            String s = fieldlines.get(i);
            String s1 = i != 0 ? "" : super._webSettings.getHtmlBefore();
            String s2 = i != 0 ? "" : "id='" + getTagId() + "'";
            String s3 = i != fieldlines.size() - 1 ? "" : super._webSettings.getHtmlAfter();
            if(RuletFactory.getRuletFactory().isTextConstantOptionButton() && !_stringMatchedUnitList.isEmpty() || RuletFactory.getRuletFactory().isTextConstantOptionWindowButton() && !_stringMatchedUnitList.isEmpty() && (getFieldNode().getParentRecord().isWindow() || getRecordLayout().isCLRLWindow()))
            {
                String s5 = "";
                if(i > 0)
                {
                    fieldlines.set(i, "");
                    continue;
                }
                for(int j = _stringMatchedUnitList.size() - 1; j >= 0; j--)
                {
                    Unit unit = (Unit)_stringMatchedUnitList.get(j);
                    String s6 = String.valueOf(unit.sequenceNumber);
                    if(s6.length() == 1)
                        s6 = "CA0" + s6;
                    else
                        s6 = "CA" + s6;
                    s5 = s5 + s6 + ",";
                }

                int k = _stringMatchedUnitList.size();
                k = ((k / fieldlines.size() + k % fieldlines.size()) * 3) / 2;
                String s7 = getRecordName();
                if(!s.equals(""))
                    s = s1 + "<span " + s4 + " " + super._webSettings.getHtmlInside() + ">" + nonDisplayConditionedText(s) + "</span>" + s3 + "<% if(" + s7 + ".isRecordOnTopLayer()) {%>" + "<jsp:include page=\"/WFCmdKeysBuilder?count=" + k + "&title=yes&cmdkeylist=[" + s5 + "]\" flush=\"true\"/>" + "<%}%>";
                else
                    s = "<% if(" + s7 + ".isRecordOnTopLayer()) {%>" + "<jsp:include page=\"/WFCmdKeysBuilder?count=" + k + "&title=yes&cmdkeylist=[" + s5 + "]\" flush=\"true\"/>" + "<%}%>";
            } else
            {
                s = s1 + "<span " + s2 + " " + s4 + getOnClick() + " " + super._webSettings.getHtmlInside() + ">" + nonDisplayConditionedText(s) + "</span>" + s3;
            }
            fieldlines.set(i, s);
        }

    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999-2003, all rights reserved");
    private String _fieldTextReplacedByBlanks;
    private String _fieldTextAfterMasking;
    private ArrayList _stringMatchedUnitList;
    private String _fieldName;
    public static final String CONSTANT_FIELD_NAME_PREFIX = "Unnamed";

}
