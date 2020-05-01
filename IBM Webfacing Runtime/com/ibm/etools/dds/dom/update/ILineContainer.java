// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.dds.dom.update;

import com.ibm.etools.dds.dom.IDdsLine;
import java.util.Iterator;

// Referenced classes of package com.ibm.etools.dds.dom.update:
//            ILinePosition, IPositionAwareLine, ISourceLocations

public interface ILineContainer
{

    public abstract IPositionAwareLine getLine(ILinePosition ilineposition);

    public abstract ILinePosition nextPosition(ILinePosition ilineposition);

    public abstract ILinePosition previousPosition(ILinePosition ilineposition);

    public abstract int getIndex(ILinePosition ilineposition);

    public abstract ILinePosition getPosition(int i);

    public abstract IPositionAwareLine getLine(int i);

    public abstract int size();

    public abstract boolean isEmpty();

    public abstract IPositionAwareLine add(IDdsLine iddsline);

    public abstract IPositionAwareLine insertAt(IDdsLine iddsline, int i);

    public abstract void remove(IPositionAwareLine ipositionawareline);

    public abstract void removeAt(int i);

    public abstract void removeAt(ILinePosition ilineposition);

    public abstract Iterator iterator();

    public abstract String getString(ISourceLocations isourcelocations);
}
