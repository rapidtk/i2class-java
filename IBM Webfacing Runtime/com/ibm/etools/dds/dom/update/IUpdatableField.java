// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.dds.dom.update;

import com.ibm.as400ad.code400.dom.constants.IFieldType;
import com.ibm.etools.dds.dom.IDdsField;

// Referenced classes of package com.ibm.etools.dds.dom.update:
//            IUpdateKeywords, IUpdateComments, IUpdatableCondition

public interface IUpdatableField
    extends IDdsField, IUpdateKeywords, IUpdateComments, IUpdatableCondition
{

    public abstract void setFieldType(IFieldType ifieldtype);

    public abstract void setDdsLength(int i);

    public abstract void setDecimalPositions(int i);
}
