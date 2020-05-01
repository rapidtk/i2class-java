// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import com.ibm.as400ad.code400.dom.AnyNode;
import com.ibm.as400ad.code400.dom.Logger;
import java.util.NoSuchElementException;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            DOMTreePropertySupplier

public class DomTreeAnyNode extends DefaultMutableTreeNode
    implements DOMTreePropertySupplier
{

    public DomTreeAnyNode()
    {
        super("dummy", true);
        isDummy = true;
    }

    public DomTreeAnyNode(AnyNode anynode, String s)
    {
        super(s, true);
        node = anynode;
    }

    public void expand()
    {
        removeAllChildren();
    }

    public AnyNode getNode()
    {
        return node;
    }

    public Vector getPropertyNames()
    {
        return propNames;
    }

    public Vector getPropertyValues()
    {
        return getUniqueAnyNodePropertyValues();
    }

    public Vector getUniqueAnyNodePropertyValues()
    {
        Vector vector = new Vector(propNames.size());
        if(node == null)
        {
            for(int i = 0; i < propNames.size(); i++)
                vector.addElement("dummy value");

            return vector;
        } else
        {
            vector.addElement(node.getName());
            vector.addElement(node.getWebName());
            vector.addElement(node.getNodeTypeAsString());
            vector.addElement(node.getHasError() ? "true" : "false");
            vector.addElement(node.toString());
            return vector;
        }
    }

    public boolean hasDummyChild()
    {
        boolean flag = false;
        try
        {
            DomTreeAnyNode domtreeanynode = (DomTreeAnyNode)getFirstChild();
            if(domtreeanynode != null && domtreeanynode.isDummy())
                flag = true;
        }
        catch(NoSuchElementException nosuchelementexception) { }
        return flag;
    }

    public boolean isDummy()
    {
        return isDummy;
    }

    public Vector mergePropertyNames(Vector vector)
    {
        return mergeVectors(vector, propNames);
    }

    public Vector mergePropertyValues(Vector vector)
    {
        return mergeVectors(vector, getUniqueAnyNodePropertyValues());
    }

    public static Vector mergeVectors(Vector vector, Vector vector1)
    {
        if(vector == null)
            return vector1;
        if(vector1 == null)
            return vector;
        Vector vector2 = new Vector(vector.size() + vector1.size());
        for(int i = 0; i < vector.size(); i++)
            vector2.addElement(vector.elementAt(i));

        for(int j = 0; j < vector1.size(); j++)
            vector2.addElement(vector1.elementAt(j));

        return vector2;
    }

    public void populateChildren(Logger logger1)
    {
        logger = logger1;
        logger1.logDetail("DomTreeAnyNode: populateChildren()");
    }

    public void setNode(AnyNode anynode)
    {
        node = anynode;
    }

    protected AnyNode node;
    protected boolean isRoot;
    protected boolean isDummy;
    protected Logger logger;
    private static Vector propNames;

    static 
    {
        propNames = new Vector();
        propNames.addElement("Name");
        propNames.addElement("Web name");
        propNames.addElement("Node type");
        propNames.addElement("Has error");
        propNames.addElement("ToString");
    }
}
