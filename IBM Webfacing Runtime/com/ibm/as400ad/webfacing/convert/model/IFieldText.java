// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;


public interface IFieldText
{

    public abstract String getFieldTextWithTransform();

    public abstract String getFieldTextWithTransform(int i);

    public abstract String getFieldTextWithTransform(int i, int j);

    public abstract int getWidth();
}
