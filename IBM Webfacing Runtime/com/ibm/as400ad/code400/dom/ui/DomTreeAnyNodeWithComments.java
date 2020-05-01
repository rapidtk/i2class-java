// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import com.ibm.as400ad.code400.dom.*;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            DomTreeAnyNode, DomTreeCategoryNode, DomTreeCommentNode, DomTreeWebSettingsNode

public class DomTreeAnyNodeWithComments extends DomTreeAnyNode
{

    public DomTreeAnyNodeWithComments(AnyNodeWithComments anynodewithcomments, String s)
    {
        super(anynodewithcomments, s);
    }

    public Vector getPropertyNames()
    {
        return super.mergePropertyNames(propNames);
    }

    public Vector getPropertyValues()
    {
        return super.mergePropertyValues(getUniqueCommentAnyNodePropertyValues());
    }

    private Vector getUniqueCommentAnyNodePropertyValues()
    {
        return null;
    }

    public Vector mergePropertyNames(Vector vector)
    {
        return super.mergePropertyNames(DomTreeAnyNode.mergeVectors(vector, propNames));
    }

    public Vector mergePropertyValues(Vector vector)
    {
        return super.mergePropertyValues(DomTreeAnyNode.mergeVectors(vector, getUniqueCommentAnyNodePropertyValues()));
    }

    public void populateChildren(Logger logger)
    {
        super.logger = logger;
        logger.logDetail("DomTreeAnyNodeWithComments: starting populateChildren...");
        super.populateChildren(logger);
        AnyNodeWithComments anynodewithcomments = (AnyNodeWithComments)getNode();
        DomTreeCategoryNode domtreecategorynode = new DomTreeCategoryNode("Comments");
        add(domtreecategorynode);
        int i = anynodewithcomments.getCommentCount();
        for(int j = 0; j < i; j++)
            domtreecategorynode.add(new DomTreeCommentNode(anynodewithcomments.getComment(j)));

        domtreecategorynode = new DomTreeCategoryNode("WebSettings");
        add(domtreecategorynode);
        int k = anynodewithcomments.getWebSettingsCount();
        Vector vector = anynodewithcomments.getWebSettingsVector();
        for(int l = 0; l < k; l++)
        {
            WebSettingsNode websettingsnode = (WebSettingsNode)vector.elementAt(l);
            DomTreeWebSettingsNode domtreewebsettingsnode = new DomTreeWebSettingsNode(websettingsnode, websettingsnode.getTypeAsString());
            domtreewebsettingsnode.populateChildren(logger);
            domtreecategorynode.add(domtreewebsettingsnode);
        }

        logger.logDetail("DomTreeAnyNodeWithComments: ending populateChildren...");
    }

    private static Vector propNames = null;

}
