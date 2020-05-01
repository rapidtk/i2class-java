// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import com.ibm.as400ad.code400.dom.*;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            DomTreeAnyNodeWithComments, DomTreeCategoryNode, DomTreeRecordNode, DomTreeAnyNode

public class DomTreeGroupNode extends DomTreeAnyNodeWithComments
{

    public DomTreeGroupNode(GroupNode groupnode, String s)
    {
        super(groupnode, s);
    }

    public Vector getPropertyNames()
    {
        return super.mergePropertyNames(propNames);
    }

    public Vector getPropertyValues()
    {
        return super.mergePropertyValues(getUniqueGroupPropertyValues());
    }

    private Vector getUniqueGroupPropertyValues()
    {
        Vector vector = new Vector();
        GroupNode groupnode = (GroupNode)getNode();
        vector.addElement(Integer.toString(groupnode.getCommentLength()));
        vector.addElement(groupnode.isEmpty() ? "true" : "false");
        return vector;
    }

    public Vector mergePropertyNames(Vector vector)
    {
        return super.mergePropertyNames(DomTreeAnyNode.mergeVectors(vector, propNames));
    }

    public Vector mergePropertyValues(Vector vector)
    {
        return super.mergePropertyValues(DomTreeAnyNode.mergeVectors(vector, getUniqueGroupPropertyValues()));
    }

    public void populateChildren(Logger logger)
    {
        super.logger = logger;
        logger.logDetail("DomTreeGroupNode: starting populateChildren...");
        super.populateChildren(logger);
        GroupNode groupnode = (GroupNode)getNode();
        DomTreeCategoryNode domtreecategorynode = new DomTreeCategoryNode("Records");
        add(domtreecategorynode);
        Vector vector = groupnode.getRecordsVector();
        if(vector != null)
        {
            for(int i = 0; i < vector.size(); i++)
            {
                RecordNode recordnode = (RecordNode)vector.elementAt(i);
                DomTreeRecordNode domtreerecordnode = new DomTreeRecordNode(recordnode, recordnode.getName());
                domtreerecordnode.populateChildren(logger);
                domtreecategorynode.add(domtreerecordnode);
            }

        }
        logger.logDetail("DomTreeGroupNode: ending populateChildren...");
    }

    private static final Vector propNames;

    static 
    {
        propNames = new Vector();
        propNames.addElement("Comment length");
        propNames.addElement("Is empty?");
    }
}
