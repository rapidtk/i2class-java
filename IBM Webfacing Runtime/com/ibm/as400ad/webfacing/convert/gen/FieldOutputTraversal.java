// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen;

import com.ibm.as400ad.webfacing.convert.*;
import com.ibm.as400ad.webfacing.convert.model.*;
import java.util.Iterator;

public class FieldOutputTraversal
    implements IFieldOutputTraversal
{

    public FieldOutputTraversal()
    {
    }

    public void traverse(IFieldOutputVisitor ifieldoutputvisitor)
    {
        ifieldoutputvisitor.processBeginOfTraversal();
        RecordLayout recordlayout = ifieldoutputvisitor.getRecordLayout();
        int i = recordlayout.getFirstRow();
        if(i > 0)
            for(; i <= recordlayout.getLastRow(); i++)
            {
                RecordLayoutRow recordlayoutrow = recordlayout.getRecordLayoutRow(i);
                if(recordlayoutrow.hasFieldsOnRow() && ifieldoutputvisitor.processBeginOfRow(recordlayoutrow))
                {
                    for(Iterator iterator = recordlayoutrow.getFieldsOnRow(); iterator.hasNext();)
                    {
                        FieldOnRow fieldonrow = (FieldOnRow)iterator.next();
                        IFieldOutput ifieldoutput = ifieldoutputvisitor.getFieldOutput(fieldonrow);
                        if(ifieldoutputvisitor.fieldHasOutput(ifieldoutput) && !ifieldoutput.getFieldVisibility().isNeverVisible())
                            ifieldoutputvisitor.processFieldOnRow(!ifieldoutput.getFieldVisibility().isAlwaysVisible(), fieldonrow);
                    }

                    ifieldoutputvisitor.processEndOfRow(recordlayoutrow);
                }
            }

        ifieldoutputvisitor.processEndOfTraversal();
    }
}
