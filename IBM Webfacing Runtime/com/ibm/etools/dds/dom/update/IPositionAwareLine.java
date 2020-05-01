// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.dds.dom.update;

import com.ibm.etools.dds.dom.IDdsElement;
import com.ibm.etools.dds.dom.IDdsLine;

// Referenced classes of package com.ibm.etools.dds.dom.update:
//            ILinePosition

public interface IPositionAwareLine
    extends IDdsLine
{

    public abstract ILinePosition getPosition();

    public abstract void setDomElement(IDdsElement iddselement);

    public abstract IDdsElement getDomElement();
}
