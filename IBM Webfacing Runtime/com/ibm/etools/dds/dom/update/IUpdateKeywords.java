// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.dds.dom.update;

import com.ibm.etools.dds.dom.IDdsKeyword;
import com.ibm.etools.dds.dom.IKeywordContainer;

// Referenced classes of package com.ibm.etools.dds.dom.update:
//            IUpdatableElement, IPositionAwareLine

public interface IUpdateKeywords
    extends IKeywordContainer, IUpdatableElement
{

    public abstract void addKeyword(IDdsKeyword iddskeyword);

    public abstract void setFirstLine(IPositionAwareLine ipositionawareline);

    public abstract IPositionAwareLine getFirstLine();
}
