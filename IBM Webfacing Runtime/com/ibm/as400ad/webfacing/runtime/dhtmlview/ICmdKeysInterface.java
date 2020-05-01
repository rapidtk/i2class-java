// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.dhtmlview;

import java.util.Iterator;

public interface ICmdKeysInterface
{

    public abstract Iterator getPageOrFieldKeyList();

    public abstract Iterator getApplicationKeyList();

    public abstract String getButtonWidth();

    public abstract String getButtonHeight();

    public abstract int getColumnCount();

    public abstract String getKeyUniqueId();

    public abstract boolean isKeyNameShown();

    public abstract boolean isFlyOverShown();

    public abstract boolean isInAppArea();

    public static final String copyright = new String("(c) Copyright IBM Corporation 2002-2003, all rights reserved");

}
