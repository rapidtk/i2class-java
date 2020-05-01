// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import com.ibm.as400ad.code400.dom.AnyNodeWithDescription;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            DomTreeAnyNodeWithKeywords, DomTreeAnyNode

public class DomTreeAnyNodeWithDescription extends DomTreeAnyNodeWithKeywords
{

    public DomTreeAnyNodeWithDescription(AnyNodeWithDescription anynodewithdescription, String s)
    {
        super(anynodewithdescription, s);
    }

    public Vector getPropertyNames()
    {
        return super.mergePropertyNames(propNames);
    }

    public Vector getPropertyValues()
    {
        return super.mergePropertyValues(getUniqueDescriptionAnyNodePropertyValues());
    }

    private Vector getUniqueDescriptionAnyNodePropertyValues()
    {
        Vector vector = new Vector();
        AnyNodeWithDescription anynodewithdescription = (AnyNodeWithDescription)getNode();
        String s = anynodewithdescription.getDescription();
        if(s == null)
            vector.addElement("null");
        else
        if(s.length() == 0)
            vector.addElement("null (len 0)");
        else
            vector.addElement(s);
        return vector;
    }

    public Vector mergePropertyNames(Vector vector)
    {
        return super.mergePropertyNames(DomTreeAnyNode.mergeVectors(vector, propNames));
    }

    public Vector mergePropertyValues(Vector vector)
    {
        return super.mergePropertyValues(DomTreeAnyNode.mergeVectors(vector, getUniqueDescriptionAnyNodePropertyValues()));
    }

    private static final Vector propNames;

    static 
    {
        propNames = new Vector();
        propNames.addElement("Description");
    }
}
