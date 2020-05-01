// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.dds.dom.update;

import com.ibm.etools.dds.dom.IDdsFileLevel;
import com.ibm.etools.dds.dom.IDdsRecord;

// Referenced classes of package com.ibm.etools.dds.dom.update:
//            IUpdatableElement, IUpdateComments, IUpdateKeywords

public interface IUpdatableFileLevel
    extends IDdsFileLevel, IUpdatableElement, IUpdateComments, IUpdateKeywords
{

    public abstract void setName(String s);

    public abstract void addRecord(IDdsRecord iddsrecord);
}
