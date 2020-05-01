// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            DOMTreePropertySupplier

public class DomTreeCategoryNode extends DefaultMutableTreeNode
    implements DOMTreePropertySupplier
{

    public DomTreeCategoryNode(String s)
    {
        super(s, true);
        name = s;
    }

    public Vector getPropertyNames()
    {
        return propNames;
    }

    public Vector getPropertyValues()
    {
        Vector vector = new Vector(propNames.size());
        vector.addElement(name);
        return vector;
    }

    protected String name;
    private static Vector propNames;

    static 
    {
        propNames = new Vector();
        propNames.addElement("Child category");
    }
}
