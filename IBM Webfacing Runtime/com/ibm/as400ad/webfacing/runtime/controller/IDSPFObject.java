// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.controller;


// Referenced classes of package com.ibm.as400ad.webfacing.runtime.controller:
//            ILibraryFile

public interface IDSPFObject
    extends ILibraryFile
{

    public abstract boolean equalsDSPF(IDSPFObject idspfobject);

    public abstract String getName();

    public abstract boolean isRSTDSP();

    public abstract boolean isKEEP();

    public abstract void setKEEP(boolean flag);

    public abstract void close();

    public abstract boolean isOpen();

    public abstract String getHostName();

    public abstract boolean isIGCDTA();

    public abstract boolean isInBidiMode();
}
