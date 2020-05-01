// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import com.ibm.as400ad.code400.dom.*;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            DomTreeAnyNodeWithDescription, DomTreeCategoryNode, DomTreeRecordNode, DomTreeGroupNode, 
//            DomTreeAnyNode, DomTreeAnyNodeWithKeywords

public class DomTreeFileNode extends DomTreeAnyNodeWithDescription
{

    public DomTreeFileNode(FileNode filenode, String s)
    {
        super(filenode, s);
    }

    public Vector getPropertyNames()
    {
        return super.mergePropertyNames(propNames);
    }

    public Vector getPropertyValues()
    {
        return super.mergePropertyValues(getUniqueFilePropertyValues());
    }

    private Vector getUniqueFilePropertyValues()
    {
        Vector vector = new Vector();
        FileNode filenode = (FileNode)getNode();
        vector.addElement(filenode.getDDSTypeAsString());
        vector.addElement(filenode.getFileName());
        vector.addElement(filenode.getLibName());
        vector.addElement(filenode.getPackageDirName());
        vector.addElement(filenode.getPackageName());
        vector.addElement(filenode.getShortName());
        vector.addElement(filenode.getWebShortName());
        vector.addElement(filenode.isEmpty() ? "true" : "false");
        vector.addElement(filenode.isDSPF() ? "true" : "false");
        return vector;
    }

    public Vector mergePropertyNames(Vector vector)
    {
        return super.mergePropertyNames(DomTreeAnyNode.mergeVectors(vector, propNames));
    }

    public Vector mergePropertyValues(Vector vector)
    {
        return super.mergePropertyValues(DomTreeAnyNode.mergeVectors(vector, getUniqueFilePropertyValues()));
    }

    public void populateChildren(Logger logger)
    {
        super.logger = logger;
        logger.logDetail("DomTreeFileNode: Calling super.populateChildren()...");
        super.populateChildren(logger);
        FileNode filenode = (FileNode)getNode();
        logger.logDetail("DomTreeFileNode: Instantiating DomTreeCategoryNode()...");
        DomTreeCategoryNode domtreecategorynode = new DomTreeCategoryNode("Records");
        add(domtreecategorynode);
        Vector vector = filenode.getRecordsVector();
        logger.logDetail("DomTreeFileNode: Processing records...");
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
        logger.logDetail("DomTreeFileNode: Processing selected records...");
        domtreecategorynode = new DomTreeCategoryNode("Selected Records");
        add(domtreecategorynode);
        vector = filenode.getSelectedRecordsVector();
        if(vector != null)
        {
            for(int j = 0; j < vector.size(); j++)
            {
                RecordNode recordnode1 = (RecordNode)vector.elementAt(j);
                DomTreeRecordNode domtreerecordnode1 = new DomTreeRecordNode(recordnode1, recordnode1.getName());
                domtreerecordnode1.populateChildren(logger);
                domtreecategorynode.add(domtreerecordnode1);
            }

        }
        logger.logDetail("DomTreeFileNode: Processing groups...");
        domtreecategorynode = new DomTreeCategoryNode("Groups");
        add(domtreecategorynode);
        Vector vector1 = filenode.getGroupsVector();
        logger.logDetail("  DomTreeFileNode: Groups null? " + (vector1 == null));
        if(vector1 != null)
        {
            for(int k = 0; k < vector1.size(); k++)
            {
                GroupNode groupnode = (GroupNode)vector1.elementAt(k);
                DomTreeGroupNode domtreegroupnode = new DomTreeGroupNode(groupnode, groupnode.getName());
                domtreegroupnode.populateChildren(logger);
                domtreecategorynode.add(domtreegroupnode);
            }

        }
        logger.logDetail("DomTreeFileNode: End populateChildren");
    }

    private static final Vector propNames;

    static 
    {
        propNames = new Vector();
        propNames.addElement("DDS Type");
        propNames.addElement("File name");
        propNames.addElement("Lib name");
        propNames.addElement("Package dir name");
        propNames.addElement("Package name");
        propNames.addElement("Short name");
        propNames.addElement("Short web name");
        propNames.addElement("Is empty?");
        propNames.addElement("Is DSPF?");
    }
}
