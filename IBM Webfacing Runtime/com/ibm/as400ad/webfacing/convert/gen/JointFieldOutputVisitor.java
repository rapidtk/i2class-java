// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen;

import com.ibm.as400ad.webfacing.convert.IFieldOutput;
import com.ibm.as400ad.webfacing.convert.IFieldOutputVisitor;
import com.ibm.as400ad.webfacing.convert.model.*;

public class JointFieldOutputVisitor
    implements IFieldOutputVisitor
{

    public JointFieldOutputVisitor()
    {
    }

    public JointFieldOutputVisitor(IFieldOutputVisitor ifieldoutputvisitor, IFieldOutputVisitor ifieldoutputvisitor1)
    {
        _a = ifieldoutputvisitor;
        _b = ifieldoutputvisitor1;
    }

    public void addElement(Object obj)
    {
        _a.addElement(obj);
        _b.addElement(obj);
    }

    public boolean fieldHasOutput(IFieldOutput ifieldoutput)
    {
        boolean flag = _a.fieldHasOutput(ifieldoutput);
        boolean flag1 = _b.fieldHasOutput(ifieldoutput);
        return flag || flag1;
    }

    public String getBeanName()
    {
        return _a.getBeanName();
    }

    public IFieldOutput getFieldOutput(FieldOnRow fieldonrow)
    {
        return _a.getFieldOutput(fieldonrow);
    }

    public RecordLayout getRecordLayout()
    {
        return _a.getRecordLayout();
    }

    public boolean isControlBeforeSubfiles()
    {
        boolean flag = _a.isControlBeforeSubfiles();
        boolean flag1 = _b.isControlBeforeSubfiles();
        return flag && flag1;
    }

    public void printBeginningLines()
    {
        _a.printBeginningLines();
        _b.printBeginningLines();
    }

    public void printEndingLines()
    {
        _a.printEndingLines();
        _b.printEndingLines();
    }

    public boolean processBeginOfRow(RecordLayoutRow recordlayoutrow)
    {
        boolean flag = _a.processBeginOfRow(recordlayoutrow);
        boolean flag1 = _b.processBeginOfRow(recordlayoutrow);
        return flag && flag1;
    }

    public void processBeginOfSubfiles()
    {
        _a.processBeginOfSubfiles();
        _b.processBeginOfSubfiles();
    }

    public void processBeginOfTraversal()
    {
        _a.processBeginOfTraversal();
        _b.processBeginOfTraversal();
    }

    public void processEndOfRow(RecordLayoutRow recordlayoutrow)
    {
        _a.processEndOfRow(recordlayoutrow);
        _b.processEndOfRow(recordlayoutrow);
    }

    public void processEndOfSubfiles()
    {
        _a.processEndOfSubfiles();
        _b.processEndOfSubfiles();
    }

    public void processEndOfTraversal()
    {
        _a.processEndOfTraversal();
        _b.processEndOfTraversal();
    }

    public void processFieldOnRow(boolean flag, FieldOnRow fieldonrow)
    {
        _a.processFieldOnRow(flag, fieldonrow);
        _b.processFieldOnRow(flag, fieldonrow);
    }

    public void insertHeader()
    {
        _a.insertHeader();
        _b.insertHeader();
    }

    IFieldOutputVisitor _a;
    IFieldOutputVisitor _b;
}
