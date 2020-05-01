// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.webfacing.convert.rules.Unit;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.host.HostJobInfo;
import com.ibm.as400ad.webfacing.runtime.view.*;
import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            ConditionedCommandKeyLabel, IndicatorConditionedLabel, CommandKeyLabel, ConditionedLabel

public class IndicatorConditionedCommandKeyLabel extends ConditionedCommandKeyLabel
{

    public IndicatorConditionedCommandKeyLabel(String s, String s1, String s2, int i)
    {
        super(s, s1, s2, i);
    }

    public Iterator getLabel(RecordViewBean recordviewbean, boolean flag)
    {
        String s = getKeyLabel();
        String s1 = null;
        String s2 = getKeyName();
        String s3 = null;
        CommandKeyLabelList commandkeylabellist = new CommandKeyLabelList();
        CommandKeyLabelList commandkeylabellist1 = new CommandKeyLabelList();
        boolean flag1 = false;
        ArrayList arraylist = getConditionedLabels();
        for(int i = 0; i < arraylist.size(); i++)
        {
            IndicatorConditionedLabel indicatorconditionedlabel = (IndicatorConditionedLabel)arraylist.get(i);
            boolean flag2;
            if(recordviewbean.evaluateIndicatorExpression(indicatorconditionedlabel.getIndicatorExpression()) && indicatorconditionedlabel.getLabel() != null)
                flag2 = true;
            else
                flag2 = false;
            if(indicatorconditionedlabel.isDynamic() && null != s3)
            {
                HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
                hostjobinfo.submitRequests();
                s = recordviewbean.getFeedbackBean().getFieldValue(s3);
                if((null == s2 || s2.equals("*AUTO")) && !s.trim().equals(""))
                {
                    Object obj = null;
                    ArrayList arraylist1 = new ArrayList();
                    s = CommandKeyLabel.parseDynamicKeyLabel(s, arraylist1);
                    for(int j = 0; j < arraylist1.size(); j++)
                    {
                        Unit unit = (Unit)arraylist1.get(j);
                        s = unit.text;
                        String s4 = String.valueOf(unit.sequenceNumber);
                        if(s4.length() == 1)
                            s4 = "0" + s4;
                        if(null != s)
                            if(flag2)
                                commandkeylabellist.add(new CommandKeyLabel(s4, s, getRecordName(), getPriority(), s3));
                            else
                            if(!flag)
                                commandkeylabellist1.add(new CommandKeyLabel(s4, s, getRecordName(), 1, s3));
                    }

                } else
                if(null != s && !s.trim().equals(""))
                    if(flag2)
                        commandkeylabellist.add(new CommandKeyLabel(s2, s, getRecordName(), getPriority(), s3));
                    else
                    if(!flag)
                        commandkeylabellist1.add(new CommandKeyLabel(s2, s, getRecordName(), 1, s3));
                s = null;
            } else
            if(flag2)
                s = indicatorconditionedlabel.getLabel();
            else
            if(!flag)
                s1 = indicatorconditionedlabel.getLabel();
        }

        if(null != s)
            commandkeylabellist.add(new CommandKeyLabel(s2, s, getRecordName(), getPriority(), s3));
        if(null != s1 && !flag)
            commandkeylabellist.add(new CommandKeyLabel(s2, s1, getRecordName(), 1, s3));
        if(commandkeylabellist.getLabels().hasNext() || flag)
            return commandkeylabellist.getLabels();
        else
            return commandkeylabellist1.getLabels();
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2003");

}
