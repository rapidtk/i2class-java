// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;


// Referenced classes of package com.ibm.as400ad.code400.dom:
//            KeywordParm, AnyNode

public class KeywordParmString extends KeywordParm
{

    public KeywordParmString(AnyNode anynode)
    {
        super(anynode);
    }

    public String getVarString()
    {
        return varString;
    }

    public String getVarValueAsString()
    {
        if(varString != null)
            return varString;
        else
            return "";
    }

    protected void setVarValue(String s)
    {
        varString = s;
    }

    protected void setVarValueFromString(String s)
    {
        varString = s;
    }

    private String varString;
}
