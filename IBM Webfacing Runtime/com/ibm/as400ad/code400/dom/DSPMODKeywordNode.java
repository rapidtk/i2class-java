// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;


// Referenced classes of package com.ibm.as400ad.code400.dom:
//            KeywordNode, FileNode, AnyNode

public final class DSPMODKeywordNode extends KeywordNode
{

    public DSPMODKeywordNode(AnyNode anynode)
    {
        super(anynode);
    }

    public short getDisplaySize()
    {
        FileNode filenode = FileNode.getFile();
        short word0 = 0;
        String s = getDisplaySizeName();
        if(s.equals(filenode.getPrimaryDisplaySizeName()))
            word0 = filenode.getPrimaryDisplaySize();
        else
            word0 = filenode.getSecondaryDisplaySize();
        return word0;
    }

    public String getDisplaySizeName()
    {
        String s = getParmsAsString();
        return s;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2001");

}
