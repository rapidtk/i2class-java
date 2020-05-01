// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            DomTreeAnyNode, MessageLine

public class DomTree extends JTree
    implements TreeWillExpandListener
{

    public DomTree(DomTreeAnyNode domtreeanynode, MessageLine messageline)
    {
        super(domtreeanynode);
        msgLine = null;
        msgLine = messageline;
        setShowsRootHandles(true);
        setRootVisible(true);
        getSelectionModel().setSelectionMode(1);
    }

    public void collapse(DomTreeAnyNode domtreeanynode)
    {
        TreePath treepath = new TreePath(domtreeanynode.getPath());
        setExpandedState(treepath, false);
    }

    public void expand(DomTreeAnyNode domtreeanynode)
    {
        TreePath treepath = new TreePath(domtreeanynode.getPath());
        setExpandedState(treepath, true);
    }

    public void treeWillCollapse(TreeExpansionEvent treeexpansionevent)
    {
    }

    public void treeWillExpand(TreeExpansionEvent treeexpansionevent)
    {
        TreePath treepath = treeexpansionevent.getPath();
        DomTreeAnyNode domtreeanynode = (DomTreeAnyNode)treepath.getLastPathComponent();
        msgLine.setText(domtreeanynode.toString() + " hasDummyChild? " + domtreeanynode.hasDummyChild());
        if(domtreeanynode.hasDummyChild())
            domtreeanynode.expand();
    }

    private MessageLine msgLine;
}
