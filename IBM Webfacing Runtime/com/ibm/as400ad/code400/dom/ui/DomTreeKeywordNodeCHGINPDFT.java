// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import com.ibm.as400ad.code400.dom.CHGINPDFTKeywordNode;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            DomTreeKeywordNode, DomTreeAnyNode

public class DomTreeKeywordNodeCHGINPDFT extends DomTreeKeywordNode
{

    public DomTreeKeywordNodeCHGINPDFT(CHGINPDFTKeywordNode chginpdftkeywordnode, String s)
    {
        super(chginpdftkeywordnode, s);
    }

    public Vector getPropertyNames()
    {
        return super.mergePropertyNames(propNames);
    }

    public Vector getPropertyValues()
    {
        return super.mergePropertyValues(getUniqueCHGINPDFTPropertyValues());
    }

    private Vector getUniqueCHGINPDFTPropertyValues()
    {
        Vector vector = new Vector();
        CHGINPDFTKeywordNode chginpdftkeywordnode = (CHGINPDFTKeywordNode)getNode();
        vector.addElement(chginpdftkeywordnode.hasBL() ? "true" : "false");
        vector.addElement(chginpdftkeywordnode.hasCS() ? "true" : "false");
        vector.addElement(chginpdftkeywordnode.hasFE() ? "true" : "false");
        vector.addElement(chginpdftkeywordnode.hasHI() ? "true" : "false");
        vector.addElement(chginpdftkeywordnode.hasLC() ? "true" : "false");
        vector.addElement(chginpdftkeywordnode.hasME() ? "true" : "false");
        vector.addElement(chginpdftkeywordnode.hasMF() ? "true" : "false");
        vector.addElement(chginpdftkeywordnode.hasRI() ? "true" : "false");
        vector.addElement(chginpdftkeywordnode.hasUL() ? "true" : "false");
        return vector;
    }

    public Vector mergePropertyNames(Vector vector)
    {
        return super.mergePropertyNames(DomTreeAnyNode.mergeVectors(vector, propNames));
    }

    public Vector mergePropertyValues(Vector vector)
    {
        return super.mergePropertyValues(DomTreeAnyNode.mergeVectors(vector, getUniqueCHGINPDFTPropertyValues()));
    }

    private static final Vector propNames;

    static 
    {
        propNames = new Vector();
        propNames.addElement("Has BL");
        propNames.addElement("Has CS");
        propNames.addElement("Has FE");
        propNames.addElement("Has HI");
        propNames.addElement("Has LC");
        propNames.addElement("Has ME");
        propNames.addElement("Has MF");
        propNames.addElement("Has RI");
        propNames.addElement("Has UL");
    }
}
