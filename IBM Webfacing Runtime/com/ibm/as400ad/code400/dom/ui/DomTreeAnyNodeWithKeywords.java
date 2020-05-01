// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import com.ibm.as400ad.code400.dom.*;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            DomTreeAnyNodeWithComments, DomTreeCategoryNode, DomTreeKeywordNode, DomTreeAnyNode

public class DomTreeAnyNodeWithKeywords extends DomTreeAnyNodeWithComments
{

    public DomTreeAnyNodeWithKeywords(AnyNodeWithKeywords anynodewithkeywords, String s)
    {
        super(anynodewithkeywords, s);
    }

    public Vector getPropertyNames()
    {
        return super.mergePropertyNames(propNames);
    }

    public Vector getPropertyValues()
    {
        return super.mergePropertyValues(getUniqueKeywordAnyNodePropertyValues());
    }

    private Vector getUniqueKeywordAnyNodePropertyValues()
    {
        return null;
    }

    public Vector mergePropertyNames(Vector vector)
    {
        return super.mergePropertyNames(DomTreeAnyNode.mergeVectors(vector, propNames));
    }

    public Vector mergePropertyValues(Vector vector)
    {
        return super.mergePropertyValues(DomTreeAnyNode.mergeVectors(vector, getUniqueKeywordAnyNodePropertyValues()));
    }

    public void populateChildren(Logger logger)
    {
        super.logger = logger;
        logger.logDetail("DomTreeAnyNodeWithKeyword: starting populateChildren...");
        super.populateChildren(logger);
        AnyNodeWithKeywords anynodewithkeywords = (AnyNodeWithKeywords)getNode();
        DomTreeCategoryNode domtreecategorynode = new DomTreeCategoryNode("Keywords");
        add(domtreecategorynode);
        Vector vector = anynodewithkeywords.getKeywordsVector();
        logger.logDetail("  kwds? " + (vector != null) + ". How many? " + (vector != null ? vector.size() : 0));
        if(vector != null)
        {
            for(int i = 0; i < vector.size(); i++)
            {
                KeywordNode keywordnode = (KeywordNode)vector.elementAt(i);
                DomTreeKeywordNode domtreekeywordnode = null;
                if(keywordnode instanceof CHGINPDFTKeywordNode)
                    domtreekeywordnode = new DomTreeKeywordNode((CHGINPDFTKeywordNode)keywordnode, keywordnode.getKeywordIdAsString());
                else
                    domtreekeywordnode = new DomTreeKeywordNode(keywordnode, keywordnode.getKeywordIdAsString());
                domtreekeywordnode.populateChildren(logger);
                domtreecategorynode.add(domtreekeywordnode);
            }

        }
        logger.logDetail("DomTreeAnyNodeWithKeyword: ending populateChildren...");
    }

    private static Vector propNames = null;

}
