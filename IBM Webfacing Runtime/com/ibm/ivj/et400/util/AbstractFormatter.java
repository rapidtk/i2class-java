// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractFormatter.java

package com.ibm.ivj.et400.util;

import java.beans.*;

// Referenced classes of package com.ibm.ivj.et400.util:
//            Formatter, Attributes

public abstract class AbstractFormatter
    implements Formatter
{

    public AbstractFormatter()
    {
    }

    public AbstractFormatter(Attributes dataAttributes)
    {
    }

    public synchronized void addVetoableChangeListener(VetoableChangeListener listener)
    {
        getVetoPropertyChange().addVetoableChangeListener(listener);
    }

    public static String Copyright()
    {
        return "(C) Copyright IBM Corporation 1997, 2002. All Rights Reserved.";
    }

    public void fireVetoableChange(String propertyName, Object oldValue, Object newValue)
        throws PropertyVetoException
    {
        getVetoPropertyChange().fireVetoableChange(propertyName, oldValue, newValue);
    }

    public abstract String formatString(String s);

    public abstract Attributes getDataAttributes();

    protected VetoableChangeSupport getVetoPropertyChange()
    {
        if(vetoPropertyChange == null)
            vetoPropertyChange = new VetoableChangeSupport(this);
        return vetoPropertyChange;
    }

    public synchronized void removeVetoableChangeListener(VetoableChangeListener listener)
    {
        getVetoPropertyChange().removeVetoableChangeListener(listener);
    }

    public abstract void setDataAttributes(Attributes attributes)
        throws PropertyVetoException;

    protected transient VetoableChangeSupport vetoPropertyChange;
}
