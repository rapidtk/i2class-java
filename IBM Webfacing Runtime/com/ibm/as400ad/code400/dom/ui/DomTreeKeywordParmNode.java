// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import com.ibm.as400ad.code400.dom.KeywordParm;
import com.ibm.as400ad.code400.dom.Logger;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            DomTreeAnyNode, DomTreeCategoryNode

public class DomTreeKeywordParmNode extends DomTreeAnyNode
{

    public DomTreeKeywordParmNode(KeywordParm keywordparm, String s)
    {
        super(keywordparm, s);
    }

    public Vector getPropertyNames()
    {
        return super.mergePropertyNames(propNames);
    }

    public Vector getPropertyValues()
    {
        return super.mergePropertyValues(getUniqueParmPropertyValues());
    }

    private Vector getUniqueParmPropertyValues()
    {
        Vector vector = new Vector();
        KeywordParm keywordparm = (KeywordParm)getNode();
        vector.addElement(Integer.toString(keywordparm.getDataType()));
        vector.addElement(Integer.toString(keywordparm.getParmType()));
        vector.addElement(keywordparm.getParmValTypeAsString());
        vector.addElement(keywordparm.getVarValueAsString());
        return vector;
    }

    public Vector mergePropertyNames(Vector vector)
    {
        return super.mergePropertyNames(DomTreeAnyNode.mergeVectors(vector, propNames));
    }

    public Vector mergePropertyValues(Vector vector)
    {
        return super.mergePropertyValues(DomTreeAnyNode.mergeVectors(vector, getUniqueParmPropertyValues()));
    }

    public void populateChildren(Logger logger)
    {
        super.logger = logger;
        logger.logDetail("DomTreeKeywordParmNode: Starting populateChildren");
        super.populateChildren(logger);
        KeywordParm keywordparm = (KeywordParm)getNode();
        DomTreeCategoryNode domtreecategorynode = new DomTreeCategoryNode("Sub-Parameters");
        add(domtreecategorynode);
        Vector vector = keywordparm.getSubParms();
        if(vector != null)
        {
            for(int i = 0; i < vector.size(); i++)
            {
                KeywordParm keywordparm1 = (KeywordParm)vector.elementAt(i);
                DomTreeKeywordParmNode domtreekeywordparmnode = new DomTreeKeywordParmNode(keywordparm1, keywordparm1.getParmValTypeAsString());
                domtreekeywordparmnode.populateChildren(logger);
                domtreecategorynode.add(domtreekeywordparmnode);
            }

        }
        logger.logDetail("DomTreeKeywordParmNode: Ending populateChildren");
    }

    private static final Vector propNames;

    static 
    {
        propNames = new Vector();
        propNames.addElement("Data type");
        propNames.addElement("Parm type");
        propNames.addElement("Parm value type");
        propNames.addElement("Parm info");
    }
}
