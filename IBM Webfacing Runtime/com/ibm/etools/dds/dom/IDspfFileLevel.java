// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.dds.dom;


// Referenced classes of package com.ibm.etools.dds.dom:
//            IDdsFileLevel

public interface IDspfFileLevel
    extends IDdsFileLevel
{

    public abstract short getPrimaryDisplaySize();

    public abstract String getPrimaryDisplaySizeName();

    public abstract boolean isSecondaryDisplaySizeSpecified();

    public abstract short getSecondaryDisplaySize();

    public abstract String getSecondaryDisplaySizeName();

    public static final int DSZ_24x80 = 0;
    public static final int DSZ_27x132 = 1;
}
