// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import com.ibm.as400ad.code400.dom.*;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            DomTreeAnyNode, DomTreeFileNode

public class DomTreeRootNode extends DomTreeAnyNode
{

    public DomTreeRootNode()
    {
        super(null, "DDS Object Model");
        domFileNode = null;
    }

    public Vector getPropertyNames()
    {
        return propNames;
    }

    public Vector getPropertyValues()
    {
        Vector vector = new Vector(propNames.size());
        if(domFileNode == null)
            vector.addElement("No DOM loaded");
        else
            vector.addElement("DOM loade for " + domFileNode.getName());
        return vector;
    }

    public void populateChildren(FileNode filenode, Logger logger)
    {
        if(domFileNode != null)
            removeAllChildren();
        domFileNode = filenode;
        logger.logDetail("DomTreeRootNode: Starting population...");
        logger.logDetail("DomTreeRootNode: ______________________");
        logger.logDetail("DomTreeRootNode: Instantiating DomTreeFileNode()");
        DomTreeFileNode domtreefilenode = new DomTreeFileNode(filenode, "File");
        logger.logDetail("DomTreeRootNode: Calling populateChildren()");
        domtreefilenode.populateChildren(logger);
        logger.logDetail("DomTreeRootNode: Calling add(fileTreeNode)");
        add(domtreefilenode);
        logger.logDetail("DomTreeRootNode: We is all done!");
    }

    public void reset()
    {
        if(domFileNode != null)
            removeAllChildren();
    }

    FileNode domFileNode;
    private static Vector propNames;

    static 
    {
        propNames = new Vector();
        propNames.addElement("State");
    }
}
