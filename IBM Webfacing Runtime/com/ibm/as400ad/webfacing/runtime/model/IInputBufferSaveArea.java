// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import com.ibm.as400ad.webfacing.runtime.controller.ILibraryFile;
import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;
import java.io.OutputStream;

public interface IInputBufferSaveArea
{

    public abstract ILibraryFile getDSPFObject();

    public abstract IRecordDataDefinition getRecordDataDefinition();

    public abstract String getRecordName();

    public abstract String getUntransformedRecordName();

    public abstract void writeIOBuffer(OutputStream outputstream);
}
