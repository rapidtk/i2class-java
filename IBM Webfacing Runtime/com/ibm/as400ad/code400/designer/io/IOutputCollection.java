// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.designer.io;

import java.util.Enumeration;
import java.util.Hashtable;

public interface IOutputCollection
{

    public abstract void addElement(Object obj);

    public abstract Enumeration elements();

    public abstract String getNewline();

    public abstract boolean newline();

    public abstract boolean print(String s);

    public abstract boolean println(String s);

    public abstract int size();

    public abstract void removeElement(Object obj);

    public abstract void addElementAtBeginning(Hashtable hashtable);
}
