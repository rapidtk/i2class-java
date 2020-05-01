// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;


// Referenced classes of package com.ibm.as400ad.code400.dom:
//            KeywordNode, AnyNode

public final class CHGINPDFTKeywordNode extends KeywordNode
{

    public boolean hasBL()
    {
        boolean flag = false;
        if(null != findParameterByToken(249))
            flag = true;
        return flag;
    }

    public boolean hasCS()
    {
        boolean flag = false;
        if(null != findParameterByToken(250))
            flag = true;
        return flag;
    }

    public boolean hasFE()
    {
        boolean flag = false;
        if(null != findParameterByToken(254))
            flag = true;
        return flag;
    }

    public boolean hasHI()
    {
        boolean flag = false;
        if(null != findParameterByToken(251))
            flag = true;
        return flag;
    }

    public boolean hasLC()
    {
        boolean flag = false;
        if(null != findParameterByToken(255))
            flag = true;
        return flag;
    }

    public boolean hasME()
    {
        boolean flag = false;
        if(null != findParameterByToken(256))
            flag = true;
        return flag;
    }

    public boolean hasMF()
    {
        boolean flag = false;
        if(null != findParameterByToken(257))
            flag = true;
        return flag;
    }

    public boolean hasRI()
    {
        boolean flag = false;
        if(null != findParameterByToken(252))
            flag = true;
        return flag;
    }

    public boolean hasUL()
    {
        boolean flag = false;
        if(null != findParameterByToken(253))
            flag = true;
        return flag;
    }

    public CHGINPDFTKeywordNode(AnyNode anynode)
    {
        super(anynode);
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2001");

}
