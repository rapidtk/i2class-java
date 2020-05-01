// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.convert.rules.*;
import com.ibm.as400ad.webfacing.runtime.view.AIDKeyDictionary;
import com.ibm.as400ad.webfacing.runtime.view.ICommandKeyLabel;
import java.util.*;

public class CommandKeyLabel
    implements ICommandKeyLabel, Cloneable
{

    public CommandKeyLabel(String s, String s1, String s2, int i)
    {
        _recordName = null;
        _fieldName = null;
        if(s != null && !s.equals("*AUTO"))
            _keyCode = new Byte(AIDKeyDictionary.getKeyCode(s));
        _keyLabel = s1;
        _priority = i;
        _keyName = s;
        _recordName = s2;
    }

    public CommandKeyLabel(String s, String s1, String s2, int i, String s3)
    {
        _recordName = null;
        _fieldName = null;
        if(s != null && !s.equals("*AUTO"))
            _keyCode = new Byte(AIDKeyDictionary.getKeyCode(s));
        _keyLabel = s1;
        _priority = i;
        _keyName = s;
        _recordName = s2;
        _fieldName = s3;
    }

    public CommandKeyLabel(String s, String s1, int i)
    {
        this(s, s1, ((String) (null)), i);
    }

    public CommandKeyLabel(String s, String s1, int i, String s2)
    {
        this(s, s1, ((String) (null)), i);
        _fieldName = s2;
    }

    public String getKeyLabel()
    {
        return _keyLabel;
    }

    public void setKeyLabel(String s)
    {
        _keyLabel = s;
    }

    public CommandKeyLabel getLabel()
    {
        return this;
    }

    public String getKeyName()
    {
        return _keyName;
    }

    public void setKeyName(String s)
    {
        _keyName = s;
    }

    public int getPriority()
    {
        return _priority;
    }

    public Byte getKeyCode()
    {
        return _keyCode;
    }

    public String getRecordName()
    {
        return _recordName;
    }

    public void setRecordName(String s)
    {
        _recordName = s;
    }

    public String getFieldName()
    {
        return _fieldName;
    }

    public Object clone()
    {
        CommandKeyLabel commandkeylabel = null;
        try
        {
            commandkeylabel = (CommandKeyLabel)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        return commandkeylabel;
    }

    public void setFieldName(String s)
    {
        _fieldName = s;
    }

    public static String parseDynamicKeyLabel(String s, ArrayList arraylist)
    {
        String s1 = s;
        Vector vector = RuletFactory.getRuntimeRuletFactory().getRulets(RuletType.KEY_PATTERN);
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
                    for(; enumeration.hasMoreElements(); arraylist.add(unit))
                        unit = (Unit)enumeration.nextElement();

                }
            }

        }
        return s1;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2001-2002");
    protected String _keyLabel;
    protected String _keyName;
    protected int _priority;
    private Byte _keyCode;
    protected String _recordName;
    protected String _fieldName;
    public static final int UNDEFINED = -1;
    public static final int DEFAULT = 0;
    public static final int DDS_DEFINITION = 1;
    public static final int PATTERN_ON_SCREEN = 2;
    public static final int WEBSETTING = 3;
    private static final String priorities[] = {
        "UNDEFINED", "DEFAULT", "DDS_DEFINITION", "PATTERN_ON_SCREEN", "WEBSETTING"
    };

}
