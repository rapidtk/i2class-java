// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.dds.dom.update;

import com.ibm.etools.dds.dom.IDdsElement;

public interface IUpdatableElement
    extends IDdsElement
{

    public abstract void setParent(IDdsElement iddselement);

    public abstract void setName(String s);
}
