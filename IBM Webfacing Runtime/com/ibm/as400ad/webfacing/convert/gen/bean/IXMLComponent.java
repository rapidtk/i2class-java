// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import java.util.Iterator;

public interface IXMLComponent
{

    public abstract void add(IXMLComponent ixmlcomponent);

    public abstract void remove(IXMLComponent ixmlcomponent);

    public abstract Iterator getChildIterator();

    public abstract String getIndent();

    public abstract void setIndent(String s);
}
