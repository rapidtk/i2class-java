// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;


public interface IMessageDefinition
{

    public abstract String getMessageText();

    public abstract String getRecordName();

    public abstract int getRecordLayer();
}
