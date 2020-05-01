// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import com.ibm.as400ad.code400.dom.*;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            DomTreeAnyNode, DomTreeCategoryNode, DomTreeKeywordParmNode

public class DomTreeKeywordNode extends DomTreeAnyNode
{

    public DomTreeKeywordNode(KeywordNode keywordnode, String s)
    {
        super(keywordnode, s);
    }

    public Vector getPropertyNames()
    {
        return super.mergePropertyNames(propNames);
    }

    public Vector getPropertyValues()
    {
        return super.mergePropertyValues(getUniqueKeywordPropertyValues());
    }

    private Vector getUniqueKeywordPropertyValues()
    {
        Vector vector = new Vector();
        KeywordNode keywordnode = (KeywordNode)getNode();
        vector.addElement(keywordnode.getAsText());
        vector.addElement(Integer.toString(keywordnode.getKeywordId()));
        vector.addElement(Integer.toString(keywordnode.getKeywordIndType()));
        String s = keywordnode.getParmsAsString();
        if(s == null)
            vector.addElement("null");
        else
        if(s.length() == 0)
            vector.addElement("null (len 0)");
        else
            vector.addElement(s);
        vector.addElement(keywordnode.getIndicatorString());
        vector.addElement(Integer.toString(keywordnode.getColorParm()));
        vector.addElement(Integer.toString(keywordnode.getColorSubParm()));
        vector.addElement(Integer.toString(keywordnode.getDspatrParm()));
        vector.addElement(Integer.toString(keywordnode.getDspatrSubParm()));
        vector.addElement(Integer.toString(keywordnode.getDisplaySizeCondition()));
        vector.addElement(Short.toString(keywordnode.getNumericParameter()));
        vector.addElement(Integer.toString(keywordnode.getColorParm()));
        vector.addElement(Integer.toString(keywordnode.getColorSubParm()));
        vector.addElement(Integer.toString(keywordnode.getDspatrParm()));
        vector.addElement(Integer.toString(keywordnode.getDspatrSubParm()));
        return vector;
    }

    public Vector mergePropertyNames(Vector vector)
    {
        return super.mergePropertyNames(DomTreeAnyNode.mergeVectors(vector, propNames));
    }

    public Vector mergePropertyValues(Vector vector)
    {
        return super.mergePropertyValues(DomTreeAnyNode.mergeVectors(vector, getUniqueKeywordPropertyValues()));
    }

    public void populateChildren(Logger logger)
    {
        super.logger = logger;
        logger.logDetail("DomTreeKeywordNode: Starting populateChildren");
        super.populateChildren(logger);
        KeywordNode keywordnode = (KeywordNode)getNode();
        DomTreeCategoryNode domtreecategorynode = new DomTreeCategoryNode("Parameters");
        add(domtreecategorynode);
        Vector vector = keywordnode.getParmsVector();
        if(vector != null)
        {
            for(int i = 0; i < vector.size(); i++)
            {
                KeywordParm keywordparm = (KeywordParm)vector.elementAt(i);
                DomTreeKeywordParmNode domtreekeywordparmnode = new DomTreeKeywordParmNode(keywordparm, keywordparm.getParmValTypeAsString());
                domtreekeywordparmnode.populateChildren(logger);
                domtreecategorynode.add(domtreekeywordparmnode);
            }

        }
        logger.logDetail("DomTreeKeywordNode: Ending populateChildren");
    }

    private static final Vector propNames;

    static 
    {
        propNames = new Vector();
        propNames.addElement("As text");
        propNames.addElement("Keyword id");
        propNames.addElement("Keyword indicator type");
        propNames.addElement("Parameters as string");
        propNames.addElement("Indicators as string");
        propNames.addElement("Color parm");
        propNames.addElement("Color subparm");
        propNames.addElement("Dspatr parm");
        propNames.addElement("Dspatr subparm");
        propNames.addElement("Display size condition");
        propNames.addElement("Numeric parameter");
        propNames.addElement("Color parm");
        propNames.addElement("Color subparm");
        propNames.addElement("Dspatr parm");
        propNames.addElement("DspatrSub subparm");
    }
}
