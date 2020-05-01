// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.core;


// Referenced classes of package com.ibm.as400ad.webfacing.runtime.core:
//            IDefinitionElement, IKey

public class Element
    implements IDefinitionElement
{

    public Element()
    {
        key = null;
        name = DEFAULT_NAME;
    }

    public Element(String s)
    {
        key = null;
        name = DEFAULT_NAME;
        name = s;
    }

    protected IKey createKey()
    {
        return new IKey() {

            public String getId()
            {
                return name;
            }

        };
    }

    public IKey getKey()
    {
        if(key == null)
            key = createKey();
        return key;
    }

    public String getName()
    {
        return name;
    }

    public String toString()
    {
        return getName();
    }

    private static String DEFAULT_NAME = new String("Element");
    private IKey key;
    private String name;


}
