// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import com.ibm.as400ad.code400.dom.WebSettingsNode;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            DomTreeAnyNode

public class DomTreeWebSettingsNode extends DomTreeAnyNode
{

    public DomTreeWebSettingsNode(WebSettingsNode websettingsnode, String s)
    {
        super(websettingsnode, s);
    }

    public Vector getPropertyNames()
    {
        return super.mergePropertyNames(propNames);
    }

    public Vector getPropertyValues()
    {
        return super.mergePropertyValues(getUniqueWebSettingsPropertyValues());
    }

    private Vector getUniqueWebSettingsPropertyValues()
    {
        Vector vector = new Vector();
        WebSettingsNode websettingsnode = (WebSettingsNode)getNode();
        vector.addElement(websettingsnode.getValue());
        vector.addElement(websettingsnode.getTypeAsString());
        vector.addElement(websettingsnode.isPermanent() ? "true" : "false");
        return vector;
    }

    public Vector mergePropertyNames(Vector vector)
    {
        return super.mergePropertyNames(DomTreeAnyNode.mergeVectors(vector, propNames));
    }

    public Vector mergePropertyValues(Vector vector)
    {
        return super.mergePropertyValues(DomTreeAnyNode.mergeVectors(vector, getUniqueWebSettingsPropertyValues()));
    }

    private static final Vector propNames;

    static 
    {
        propNames = new Vector();
        propNames.addElement("Value");
        propNames.addElement("Type");
        propNames.addElement("Permanent?");
    }
}
