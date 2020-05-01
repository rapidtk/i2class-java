// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.xml;


// Referenced classes of package com.ibm.etools.iseries.webfacing.xml:
//            IEntryProperty

public interface IEntry
{

    public abstract void addChildEntry(IEntry ientry);

    public abstract void addProperty(IEntryProperty ientryproperty);

    public abstract IEntry[] getChildEntries();

    public abstract IEntry[] getChildEntries(String s);

    public abstract IEntry getChildEntry(String s);

    public abstract String getName();

    public abstract int getNumberOfChildEntries();

    public abstract int getNumberOfProperties();

    public abstract IEntryProperty[] getProperties();

    public abstract IEntryProperty getProperty(String s);

    public abstract void printPersistentEntryString(StringBuffer stringbuffer, int i);

    public abstract void removeAllChildEntries();

    public abstract void removeAllProperties();

    public abstract boolean removeProperty(String s);

    public static final String copyright = "(c) Copyright IBM Corporation 2002. all rights reserved";
}
