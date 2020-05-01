// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.dds.dom;

import java.util.Iterator;

// Referenced classes of package com.ibm.etools.dds.dom:
//            IDdsKeyword, IDdsKeywordParm

public interface IKeywordContainer
{

    public abstract IDdsKeyword getFirstKeyword();

    public abstract IDdsKeyword findCompositeKeyword(int i, int j, IDdsKeywordParm aiddskeywordparm[]);

    public abstract IDdsKeyword findKeywordById(int i);

    public abstract IDdsKeyword findNextKeywordById(IDdsKeyword iddskeyword, int i);

    public abstract IDdsKeyword getKeywordAtPosition(int i);

    public abstract Iterator getKeywordIterator();

    public abstract Iterator getKeywordsOfType(int i);

    public abstract int getKeywordCount();

    public abstract Iterator getWindowBorderKeywords();

    public abstract IDdsKeyword findKeywordById(int i, int j);

    public abstract IDdsKeyword findKeywordById(int i, int j, boolean flag);
}
