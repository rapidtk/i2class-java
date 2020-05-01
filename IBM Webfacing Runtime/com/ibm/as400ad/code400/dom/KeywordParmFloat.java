// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;


// Referenced classes of package com.ibm.as400ad.code400.dom:
//            KeywordParm, AnyNode

public class KeywordParmFloat extends KeywordParm
{

    public KeywordParmFloat(AnyNode anynode)
    {
        super(anynode);
    }

    public float getVarFloat()
    {
        return varFloat;
    }

    public String getVarValueAsString()
    {
        return Float.toString(varFloat);
    }

    protected void setVarValue(float f)
    {
        varFloat = f;
    }

    protected void setVarValueFromString(String s)
    {
        if(s == null)
        {
            varFloat = 0.0F;
        } else
        {
            s = s.trim();
            if(s.length() == 0)
                varFloat = 0.0F;
            else
                varFloat = Float.parseFloat(s);
        }
    }

    private float varFloat;
}
