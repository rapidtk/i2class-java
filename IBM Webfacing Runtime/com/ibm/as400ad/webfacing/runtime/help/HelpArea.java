// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.help:
//            GenericHelpArea, HelpDefinition, HelpKeyword

public class HelpArea extends GenericHelpArea
    implements ENUM_KeywordIdentifiers
{

    public HelpArea()
    {
        _type = 0;
        _field = null;
        _fieldChoice = 0;
        _fieldId = 0;
        _definition = null;
        _boundary = null;
        _isBoundary = false;
        _excluded = null;
        _isExcluded = false;
        _type = 1;
    }

    public HelpArea(int i)
    {
        super(0, 0, 0, 0);
        _type = 0;
        _field = null;
        _fieldChoice = 0;
        _fieldId = 0;
        _definition = null;
        _boundary = null;
        _isBoundary = false;
        _excluded = null;
        _isExcluded = false;
        _type = i;
    }

    public HelpArea(int i, int j, int k, int l)
    {
        super(i, j, k, l);
        _type = 0;
        _field = null;
        _fieldChoice = 0;
        _fieldId = 0;
        _definition = null;
        _boundary = null;
        _isBoundary = false;
        _excluded = null;
        _isExcluded = false;
        _type = 0;
    }

    public String getField()
    {
        return _field;
    }

    public int getFieldChoice()
    {
        return _fieldChoice;
    }

    public int getFieldId()
    {
        return _fieldId;
    }

    public HelpKeyword getHelpBoundary()
    {
        return _boundary;
    }

    public HelpDefinition getHelpDefinition()
    {
        return _definition;
    }

    public HelpKeyword getHelpExcluded()
    {
        return _excluded;
    }

    public int getType()
    {
        return _type;
    }

    public boolean isBoundary()
    {
        return _isBoundary;
    }

    public boolean isDefaultHelp()
    {
        return _type == 1 || getTopLeft().equals(new GenericHelpArea.Cursor(-1, -1)) && getBottomRight().equals(new GenericHelpArea.Cursor(-1, -1));
    }

    public boolean isExcluded()
    {
        return _isExcluded;
    }

    public boolean isForSecondaryHelpOnly()
    {
        return _type == 295 || getTopLeft().equals(new GenericHelpArea.Cursor(0, 0)) && getBottomRight().equals(new GenericHelpArea.Cursor( 0, 0));
    }

    public boolean isSameField(String s)
    {
        if(_field == null)
            return false;
        else
            return _field.equals(s);
    }

    public boolean isWithin(HelpArea helparea)
    {
        if(isDefaultHelp() || helparea.isDefaultHelp() || isForSecondaryHelpOnly() || helparea.isForSecondaryHelpOnly())
            return false;
        else
            return super.isWithin(helparea);
    }

    public void setField(String s)
    {
        _field = s;
    }

    public void setField(String s, int i)
    {
        setField(s);
        _fieldChoice = i;
    }

    public void setFieldId(int i)
    {
        _fieldId = i;
    }

    public void setHelpBoundary(HelpKeyword helpkeyword)
    {
        _boundary = helpkeyword;
    }

    public void setHelpDefinition(HelpDefinition helpdefinition)
    {
        _definition = helpdefinition;
    }

    public void setHelpExcluded(HelpKeyword helpkeyword)
    {
        _excluded = helpkeyword;
    }

    public void setIsBoundary(boolean flag)
    {
        _isBoundary = flag;
    }

    public void setIsExcluded(boolean flag)
    {
        _isExcluded = flag;
    }

    public String toString()
    {
        String s = "HLPARA(";
        if(_type == 295)
            s = s + "*NONE";
        else
        if(_type == 298)
            s = s + "*CNST " + _fieldId;
        else
        if(_type == 297)
        {
            s = s + "*FLD " + _fieldId;
            if(_fieldChoice >= 0)
                s = s + _fieldChoice;
        } else
        if(_type == 0)
            s = s + getTopLeft().toString() + ", " + getBottomRight().toString();
        else
        if(_type == 296)
            s = s + "*RCD";
        else
            s = " HLPARA(";
        s = s + ") ";
        s = s + _definition.toString();
        return s;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2001, 2002, all rights reserved");
    public static final int HLPARA_NONE = 295;
    public static final int HLPARA_RCD = 296;
    public static final int HLPARA_FLD = 297;
    public static final int HLPARA_CNST = 298;
    public static final int HLPARA_RECT = 0;
    public static final int HLPARA_DFT = 1;
    private int _type;
    private String _field;
    private int _fieldChoice;
    private int _fieldId;
    private HelpDefinition _definition;
    private HelpKeyword _boundary;
    private boolean _isBoundary;
    private HelpKeyword _excluded;
    private boolean _isExcluded;

}
