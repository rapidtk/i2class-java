// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import com.ibm.as400ad.code400.dom.DisplayAttributes;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            DOMTreePropertySupplier

public class DomTreeAttributesNode extends DefaultMutableTreeNode
    implements DOMTreePropertySupplier
{

    public DomTreeAttributesNode(DisplayAttributes displayattributes)
    {
        super("Display Attributes", false);
        node = displayattributes;
    }

    public Vector getPropertyNames()
    {
        return propNames;
    }

    public Vector getPropertyValues()
    {
        Vector vector = new Vector();
        vector.addElement(node.getBLIndExpr());
        vector.addElement(node.getCSIndExpr());
        vector.addElement(node.getHIIndExpr());
        vector.addElement(node.getMDTIndExpr());
        vector.addElement(node.getNDIndExpr());
        vector.addElement(node.getOIDIndExpr());
        vector.addElement(node.getPCIndExpr());
        vector.addElement(node.getPRIndExpr());
        vector.addElement(node.getRIIndExpr());
        vector.addElement(node.getSPIndExpr());
        vector.addElement(node.getULIndExpr());
        return vector;
    }

    private DisplayAttributes node;
    private static final Vector propNames;

    static 
    {
        propNames = new Vector();
        propNames.addElement("BL ind expr");
        propNames.addElement("CS ind expr");
        propNames.addElement("HI ind expr");
        propNames.addElement("MDT ind expr");
        propNames.addElement("ND ind expr");
        propNames.addElement("OID ind expr");
        propNames.addElement("PC ind expr");
        propNames.addElement("PR ind expr");
        propNames.addElement("RI ind expr");
        propNames.addElement("SP ind expr");
        propNames.addElement("UL ind expr");
    }
}
