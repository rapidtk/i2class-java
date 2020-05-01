// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;


public interface IClientAIDKey
{

    public abstract String getKeyLabel();

    public abstract String getKeyName();

    public abstract String transformFKeyName(String s);

    public abstract boolean isKeyShownOnClient();

    public abstract boolean isOverriden();

    public abstract String getOverridingURI();

    public abstract String getOverridingTargetFrame();

    public abstract String getOverridingLabel();

    public static final String copyright = new String("(c) Copyright IBM Corporation 2002, all rights reserved");

}
