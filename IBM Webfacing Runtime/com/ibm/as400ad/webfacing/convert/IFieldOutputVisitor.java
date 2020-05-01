// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert;

import com.ibm.as400ad.webfacing.convert.model.FieldOnRow;
import com.ibm.as400ad.webfacing.convert.model.RecordLayout;
import com.ibm.as400ad.webfacing.convert.model.RecordLayoutRow;

// Referenced classes of package com.ibm.as400ad.webfacing.convert:
//            IFieldOutput

public interface IFieldOutputVisitor
{

    public abstract void addElement(Object obj);

    public abstract boolean fieldHasOutput(IFieldOutput ifieldoutput);

    public abstract String getBeanName();

    public abstract IFieldOutput getFieldOutput(FieldOnRow fieldonrow);

    public abstract RecordLayout getRecordLayout();

    public abstract boolean isControlBeforeSubfiles();

    public abstract void printBeginningLines();

    public abstract void printEndingLines();

    public abstract boolean processBeginOfRow(RecordLayoutRow recordlayoutrow);

    public abstract void processBeginOfSubfiles();

    public abstract void processBeginOfTraversal();

    public abstract void processEndOfRow(RecordLayoutRow recordlayoutrow);

    public abstract void processEndOfSubfiles();

    public abstract void processEndOfTraversal();

    public abstract void processFieldOnRow(boolean flag, FieldOnRow fieldonrow);

    public abstract void insertHeader();
}
