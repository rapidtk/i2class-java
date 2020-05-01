// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

public class DomTreeCommentNode extends DefaultMutableTreeNode
{

    public DomTreeCommentNode(String s)
    {
        super("Comment", false);
        comment = s;
    }

    public Vector getPropertyNames()
    {
        return propNames;
    }

    public Vector getPropertyValues()
    {
        Vector vector = new Vector(propNames.size());
        vector.addElement(comment);
        return vector;
    }

    protected String comment;
    private static Vector propNames;

    static 
    {
        propNames = new Vector();
        propNames.addElement("Comment");
    }
}
