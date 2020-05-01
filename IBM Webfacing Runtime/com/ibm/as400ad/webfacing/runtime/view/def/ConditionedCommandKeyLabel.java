// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.webfacing.runtime.view.RecordViewBean;
import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            CommandKeyLabel, ConditionedLabel

public abstract class ConditionedCommandKeyLabel extends CommandKeyLabel
{

    public ConditionedCommandKeyLabel(String s, String s1, String s2, int i)
    {
        super(s, s1, s2, i);
        _labelList = new ArrayList();
    }

    public void addAConditionedLabel(ConditionedLabel conditionedlabel)
    {
        _labelList.add(conditionedlabel);
    }

    public void mergeContionedLabelList(ArrayList arraylist)
    {
        _labelList.addAll(arraylist);
    }

    public ArrayList getConditionedLabels()
    {
        return _labelList;
    }

    public abstract Iterator getLabel(RecordViewBean recordviewbean, boolean flag);

    static final String copyRight = new String("(C) Copyright IBM Corporation 1999-2003, all rights reserved");
    private ArrayList _labelList;

}
