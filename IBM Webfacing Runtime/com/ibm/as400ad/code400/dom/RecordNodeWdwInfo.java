// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;


// Referenced classes of package com.ibm.as400ad.code400.dom:
//            KeywordNode

public class RecordNodeWdwInfo
    implements Cloneable
{

    public RecordNodeWdwInfo()
    {
    }

    public Object clone()
    {
        RecordNodeWdwInfo recordnodewdwinfo = null;
        try
        {
            recordnodewdwinfo = (RecordNodeWdwInfo)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        return recordnodewdwinfo;
    }

    public String szNamedWdw;
    public boolean wdw;
    public boolean def;
    public boolean hardlin;
    public boolean hardpos;
    public boolean dszSet;
    public boolean fWdwDFT;
    public boolean fNOMSGLIN;
    public boolean opaque;
    public boolean norstcsr;
    public short strLin;
    public short strPos;
    public short rows;
    public short cols;
    public String szRowField;
    public String szColField;
    public KeywordNode pKwd;
}
