// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.dds.dom.update;

import com.ibm.etools.dds.dom.IDdsKeyword;

// Referenced classes of package com.ibm.etools.dds.dom.update:
//            IUpdatableElement, IUpdatableParmContainer, IUpdatableCondition, ISourceLocations

public interface IUpdatableKeyword
    extends IDdsKeyword, IUpdatableElement, IUpdatableParmContainer, IUpdatableCondition
{

    public abstract void createSystemParameter(int i, String s);

    public abstract void addWordParameter(String s);

    public abstract void setImplicit();

    public abstract void setSourceLocations(ISourceLocations isourcelocations);

    public abstract void resolveParms();

    public abstract ISourceLocations getSourceLocations();
}
