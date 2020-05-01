// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.rules;

import java.util.Hashtable;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.rules:
//            Property, ResultContainer

public abstract class Rule
{

    public void setProperty(String s, String s1)
    {
        properties.put(s, new Property(s, s1));
    }

    protected void setProperty(Property property)
    {
        properties.put(property.key, property);
    }

    public Hashtable getProperties()
    {
        return properties;
    }

    protected Rule()
    {
        properties = new Hashtable();
    }

    public abstract Object apply(Object obj, ResultContainer resultcontainer, Vector vector);

    protected Hashtable properties;
    static final String copyRight = new String(" (C) Copyright IBM Corporation 2000-2002 All rights reserved.");

}
