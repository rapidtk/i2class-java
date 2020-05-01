// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import com.ibm.as400ad.code400.dom.*;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            DomTreeAnyNodeWithDescription, DomTreeCategoryNode, DomTreeFieldNode, DomTreeAnyNode, 
//            DomTreeAnyNodeWithKeywords

public class DomTreeRecordNode extends DomTreeAnyNodeWithDescription
{

    public DomTreeRecordNode(RecordNode recordnode, String s)
    {
        super(recordnode, s);
    }

    public Vector getPropertyNames()
    {
        return super.mergePropertyNames(propNames);
    }

    public Vector getPropertyValues()
    {
        return super.mergePropertyValues(getUniqueRecordPropertyValues());
    }

    private Vector getUniqueRecordPropertyValues()
    {
        Vector vector = new Vector();
        RecordNode recordnode = (RecordNode)getNode();
        vector.addElement(recordnode.getRecordTypeAsString());
        vector.addElement(recordnode.getDate() != null ? ((Object) (recordnode.getDate())) : " ");
        vector.addElement(Integer.toString(recordnode.getWindowHeight(0)));
        vector.addElement(Integer.toString(recordnode.getWindowHeight(1)));
        vector.addElement(Integer.toString(recordnode.getWindowWidth(0)));
        vector.addElement(Integer.toString(recordnode.getWindowWidth(1)));
        vector.addElement(Integer.toString(recordnode.getMaxWindowHeight(0)));
        vector.addElement(Integer.toString(recordnode.getMaxWindowHeight(1)));
        vector.addElement(Integer.toString(recordnode.getMaxWindowWidth(0)));
        vector.addElement(Integer.toString(recordnode.getMaxWindowWidth(1)));
        vector.addElement(recordnode.getBeanClassName());
        vector.addElement(recordnode.getBeanName());
        vector.addElement(recordnode.isPulldown() ? "true" : "false");
        vector.addElement(recordnode.isRelativePRTF() ? "true" : "false");
        vector.addElement(recordnode.isSelected() ? "true" : "false");
        vector.addElement(recordnode.isWindow() ? "true" : "false");
        vector.addElement(recordnode.isWindowed() ? "true" : "false");
        vector.addElement(recordnode.canHaveFields() ? "true" : "false");
        return vector;
    }

    public Vector mergePropertyNames(Vector vector)
    {
        return super.mergePropertyNames(DomTreeAnyNode.mergeVectors(vector, propNames));
    }

    public Vector mergePropertyValues(Vector vector)
    {
        return super.mergePropertyValues(DomTreeAnyNode.mergeVectors(vector, getUniqueRecordPropertyValues()));
    }

    public void populateChildren(Logger logger)
    {
        logger.logDetail("DomTreeRecordNode: starting populateChildren...");
        super.populateChildren(logger);
        RecordNode recordnode = (RecordNode)getNode();
        DomTreeCategoryNode domtreecategorynode = new DomTreeCategoryNode("Fields");
        add(domtreecategorynode);
        Vector vector = recordnode.getFieldsVector();
        if(vector != null)
        {
            for(int i = 0; i < vector.size(); i++)
            {
                FieldNode fieldnode = (FieldNode)vector.elementAt(i);
                DomTreeFieldNode domtreefieldnode = new DomTreeFieldNode(fieldnode, fieldnode.getName());
                domtreefieldnode.populateChildren(logger);
                domtreecategorynode.add(domtreefieldnode);
            }

        }
        logger.logDetail("DomTreeRecordNode: ending populateChildren...");
    }

    private static final Vector propNames;

    static 
    {
        propNames = new Vector();
        propNames.addElement("Type");
        propNames.addElement("Date");
        propNames.addElement("Window height[0]");
        propNames.addElement("Window height[1]");
        propNames.addElement("Window width[0]");
        propNames.addElement("Window width[1]");
        propNames.addElement("Window max height[0]");
        propNames.addElement("Window max height[1]");
        propNames.addElement("Window max width[0]");
        propNames.addElement("Window max width[1]");
        propNames.addElement("Bean class name");
        propNames.addElement("Bean name");
        propNames.addElement("Pulldown?");
        propNames.addElement("Relative?");
        propNames.addElement("Selected?");
        propNames.addElement("Window?");
        propNames.addElement("Windowed?");
        propNames.addElement("Can have fields?");
    }
}
