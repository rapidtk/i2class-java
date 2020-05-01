// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.dds.dom.update;

import com.ibm.etools.dds.dom.IDdsKeywordParmExpr;

// Referenced classes of package com.ibm.etools.dds.dom.update:
//            IUpdatableDdsModel, IUpdatableKeyword, IUpdatableField, IUpdatableRecord, 
//            IUpdatableHelpSpec, ILineContainer

public interface IDdsDomFactory
{

    public abstract IUpdatableDdsModel createRoot(String s, String s1);

    public abstract IUpdatableKeyword createKeyword(int i);

    public abstract IDdsKeywordParmExpr createKwdParmExpr();

    public abstract IUpdatableField createField();

    public abstract IUpdatableRecord createRecord();

    public abstract IUpdatableHelpSpec createHelpSpec();

    public abstract ILineContainer createLineContainer();
}
