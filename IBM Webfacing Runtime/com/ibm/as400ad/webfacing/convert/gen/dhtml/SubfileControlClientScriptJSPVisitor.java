// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.dhtml;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.dom.RecordNode;
import com.ibm.as400ad.webfacing.convert.IFieldOutput;
import com.ibm.as400ad.webfacing.convert.model.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.dhtml:
//            AbstractSubfileControlClientScriptJSPVisitor, ClientScriptJSPVisitor, ClientScriptSourceCodeCollection

public class SubfileControlClientScriptJSPVisitor extends AbstractSubfileControlClientScriptJSPVisitor
{

    public SubfileControlClientScriptJSPVisitor()
    {
    }

    public SubfileControlClientScriptJSPVisitor(SubfileControlRecordLayout subfilecontrolrecordlayout, ClientScriptSourceCodeCollection clientscriptsourcecodecollection)
    {
        super(subfilecontrolrecordlayout, clientscriptsourcecodecollection);
        _sfrl = subfilecontrolrecordlayout.getSubfileRecordLayout();
        _sfrn = _sfrl.getRecordNode();
    }

    protected SubfileControlRecordLayout getControlRecordLayout()
    {
        return (SubfileControlRecordLayout)super._rl;
    }

    public IFieldOutput getFieldOutput(FieldOnRow fieldonrow)
    {
        if(isProcessingSubfiles())
        {
            IFieldOutput ifieldoutput = fieldonrow.getFieldOutput();
            SubfileFieldOutputDecorator subfilefieldoutputdecorator = new SubfileFieldOutputDecorator(ifieldoutput, getSubfileRecordLayout().getFirstColumn(), getSubfileRecordLayout().getWidth());
            return subfilefieldoutputdecorator;
        } else
        {
            return fieldonrow.getFieldOutput();
        }
    }

    public RecordLayout getRecordLayout()
    {
        if(isProcessingSubfiles())
            return _sfrl;
        else
            return super._rl;
    }

    protected SubfileRecordLayout getSubfileRecordLayout()
    {
        return _sfrl;
    }

    protected boolean isSubfileFoldable()
    {
        return super._rn.isSubfileFoldable();
    }

    public void processBeginOfTraversal()
    {
        if(isProcessingSubfiles())
        {
            String s = "rrn";
            super._scc.addElement("<%if (" + getBeanName() + ".isSubfileVisible()) {%>");
            super._scc.addElement("<% for (int " + s + "=1; " + s + " <= " + getSubfileRecordLayout().getRRNLoopBound() + "; " + s + "++ ) { %>");
            super._scc.addElement("\t<% if( " + getBeanName() + ".isActiveRecord(" + s + ")) { %>");
            super.processBeginOfTraversal();
        } else
        {
            super.processBeginOfTraversal();
        }
    }

    public void processEndOfRow(RecordLayoutRow recordlayoutrow)
    {
        if(isProcessingSubfiles() && isSubfileFoldable() && recordlayoutrow.getRowNumber() == _sfrl.getFirstRow())
            super._scc.addElement("<% if (" + getBeanName() + ".isSubfileFolded()) { %>");
    }

    public void processEndOfTraversal()
    {
        if(isProcessingSubfiles())
        {
            if(isSubfileFoldable())
                super._scc.addElement("<% } //For Subfile Foldable %>");
            super._scc.addElement("<% } } %>");
            super._scc.addElement("<% } // End of 'if' for subfile visible %>");
        }
    }

    private SubfileRecordLayout _sfrl;
    private RecordNode _sfrn;
    public static final String VISIBLE_SUBFILE_VARNAME = "visibileSubfileSize";
}
