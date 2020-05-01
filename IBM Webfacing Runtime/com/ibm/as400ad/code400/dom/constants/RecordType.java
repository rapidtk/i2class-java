// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.constants;


// Referenced classes of package com.ibm.as400ad.code400.dom.constants:
//            IRecordType

public class RecordType
    implements IRecordType
{

    public static IRecordType getRecordTypeFromId(int i)
    {
        Object obj = null;
        try
        {
            obj = _types[i];
            if(null == obj)
            {
                obj = new RecordType(i);
                _types[i] = ((IRecordType) (obj));
            }
        }
        catch(Throwable throwable) { }
        return ((IRecordType) (obj));
    }

    private RecordType()
    {
        _type = 0;
    }

    private RecordType(int i)
    {
        _type = i;
    }

    public boolean isRecordOfType(int i)
    {
        return isOfType(i);
    }

    public String toString()
    {
        String s = "UNKNOWN";
        if(_type >= 0 && _type <= 6)
            s = RT_STRINGS[_type];
        return s;
    }

    public int typeId()
    {
        return _type;
    }

    public boolean isOfType(int i)
    {
        boolean flag = false;
        if(i == _type)
            flag = true;
        return flag;
    }

    public boolean isSubFile()
    {
        boolean flag = false;
        if(1 == _type || 3 == _type)
            flag = true;
        return flag;
    }

    private static final String RT_STRINGS[] = {
        "RECORD", "SFL", "SFLCTL", "SFLMSG", "USRDFN", "MNUBAR", "SFLMSGCTL", "UNKNOWN"
    };
    private static IRecordType _types[] = new IRecordType[8];
    private int _type;

}
