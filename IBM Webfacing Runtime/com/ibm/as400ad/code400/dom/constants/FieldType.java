// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.constants;

import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Field;

// Referenced classes of package com.ibm.as400ad.code400.dom.constants:
//            IFieldType

public class FieldType
    implements IFieldType, Serializable
{

    public static IFieldType getFieldTypeFromId(int i)
    {
        Object obj = null;
        try
        {
            obj = _types[i];
            if(null == obj)
            {
                obj = new FieldType(i);
                _types[i] = ((IFieldType) (obj));
            }
        }
        catch(Throwable throwable) { }
        return ((IFieldType) (obj));
    }

    private FieldType(int i)
    {
        _type = i;
    }

    public boolean equals(int i)
    {
        return _type == i;
    }

    public static FieldType getFieldType(int i)
    {
        Class class1 = com.ibm.as400ad.code400.dom.constants.IFieldType.class;
        Field afield[] = class1.getDeclaredFields();
        try
        {
            for(int j = 0; j < afield.length; j++)
                if(afield[j].getType() == (com.ibm.as400ad.code400.dom.constants.FieldType.class))
                {
                    FieldType fieldtype = (FieldType)afield[j].get(null);
                    if(fieldtype.equals(i))
                        return fieldtype;
                }

        }
        catch(Exception exception)
        {
            System.out.println("Error");
        }
        return null;
    }

    public int intValue()
    {
        return _type;
    }

    public String toString()
    {
        return FT_STRINGS[_type];
    }

    public static FieldType fromString(String s)
    {
        FieldType fieldtype = null;
        String s1 = s.trim();
        for(int i = 0; null == fieldtype && i < FT_STRINGS.length; i++)
            if(s1.equals(FT_STRINGS[i]))
                fieldtype = new FieldType(i);

        return fieldtype;
    }

    public int typeId()
    {
        return _type;
    }

    public boolean isOfType(int i)
    {
        return i == _type;
    }

    public boolean isFieldOfType(int i)
    {
        return isOfType(i);
    }

    public static final String Copyright = "(c) Copyright IBM Corp. 2001-2003.  All Rights Reserved.";
    private static IFieldType _types[] = new IFieldType[21];
    private int _type;
    private static final String FT_STRINGS[] = {
        "FT_TEXT_CONSTANT", "FT_TXTBLK", "FT_DATE_CONSTANT", "FT_TIME_CONSTANT", "FT_USER_ID_CONSTANT", "FT_SYSTEM_ID_CONSTANT", "FT_MESSAGE_CONSTANT", "FT_PAGE_NUMBER", "FT_ALPHA", "FT_NUMERIC", 
        "FT_FLOATS", "FT_FLOATD", "FT_PACKED", "FT_BINARY", "FT_HEX", "FT_DATE_FIELD", "FT_TIME_FIELD", "FT_TIMESTAMP_FIELD", "FT_PUREDBCS", "FT_DBCS", 
        "FT_UNKNOWN"
    };

}
