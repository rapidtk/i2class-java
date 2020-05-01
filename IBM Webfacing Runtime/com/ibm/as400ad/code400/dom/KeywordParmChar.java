// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;


// Referenced classes of package com.ibm.as400ad.code400.dom:
//            KeywordParm, AnyNode

public class KeywordParmChar extends KeywordParm
{

    public KeywordParmChar(AnyNode anynode)
    {
        super(anynode);
    }

    public char getVarChar()
    {
        return varChar;
    }

    public String getVarValueAsString()
    {
        return (new Character(varChar)).toString();
    }

    protected void setVarValue(char c)
    {
        varChar = c;
    }

    protected void setVarValueFromString(String s)
    {
        if(s == null || s.length() == 0)
            varChar = ' ';
        else
            varChar = s.charAt(0);
    }

    private char varChar;
}
