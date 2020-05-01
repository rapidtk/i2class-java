// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import com.ibm.as400ad.code400.dom.AnyNode;
import com.ibm.as400ad.code400.dom.IndicatorNode;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            DomTreeAnyNode

public class DomTreeIndicatorNode extends DomTreeAnyNode
{

    public DomTreeIndicatorNode(IndicatorNode indicatornode)
    {
        super(indicatornode, indicatornode.getName());
    }

    public Vector getPropertyNames()
    {
        return super.mergePropertyNames(propNames);
    }

    public Vector getPropertyValues()
    {
        return super.mergePropertyValues(getUniqueIndicatorPropertyValues());
    }

    private Vector getUniqueIndicatorPropertyValues()
    {
        Vector vector = new Vector();
        IndicatorNode indicatornode = (IndicatorNode)getNode();
        vector.addElement(indicatornode.getAsString());
        return vector;
    }

    public Vector mergePropertyNames(Vector vector)
    {
        return super.mergePropertyNames(DomTreeAnyNode.mergeVectors(vector, propNames));
    }

    public Vector mergePropertyValues(Vector vector)
    {
        return super.mergePropertyValues(DomTreeAnyNode.mergeVectors(vector, getUniqueIndicatorPropertyValues()));
    }

    private static final Vector propNames;

    static 
    {
        propNames = new Vector();
        propNames.addElement("asString");
    }
}
