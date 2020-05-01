// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;


// Referenced classes of package com.ibm.as400ad.code400.dom:
//            KeywordParm, AnyNode

public class KeywordParmLong extends KeywordParm
{

    public KeywordParmLong(AnyNode anynode)
    {
        super(anynode);
    }

    public long getVarLong()
    {
        return varLong;
    }

    public String getVarValueAsString()
    {
        return Long.toString(varLong);
    }

    protected void setVarValue(long l)
    {
        varLong = l;
    }

    protected void setVarValueFromString(String s)
    {
        if(s == null)
        {
            varLong = 0L;
        } else
        {
            s = s.trim();
            if(s.length() == 0)
                varLong = 0L;
            else
                varLong = Long.parseLong(s);
        }
    }

    private long varLong;
}
