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
//            ConditionedCommandKeyLabel, VisibilityConditionedLabel, CommandKeyLabel, ConditionedLabel

public class VisibilityConditionedCommandKeyLabel extends ConditionedCommandKeyLabel
{

    public VisibilityConditionedCommandKeyLabel(String s, String s1, String s2, int i)
    {
        super(s, s1, s2, i);
    }

    public Iterator getLabel(RecordViewBean recordviewbean, boolean flag)
    {
        String s = getKeyLabel();
        String s3 = getKeyName();
        Object obj = null;
        CommandKeyLabelList commandkeylabellist = new CommandKeyLabelList();
        CommandKeyLabelList commandkeylabellist1 = new CommandKeyLabelList();
        boolean flag1 = false;
        boolean flag3 = false;
        ArrayList arraylist = getConditionedLabels();
        for(int i = 0; i < arraylist.size(); i++)
        {
            VisibilityConditionedLabel visibilityconditionedlabel = (VisibilityConditionedLabel)arraylist.get(i);
            String s4 = visibilityconditionedlabel.getFieldID();
            boolean flag2;
            if(null != s4 && recordviewbean.isFieldVisible(s4) && visibilityconditionedlabel.getLabel() != null)
                flag2 = true;
            else
                flag2 = false;
            if(visibilityconditionedlabel.isDynamic())
            {
                HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
                hostjobinfo.submitRequests();
                String s1 = recordviewbean.getFeedbackBean().getFieldValue(s4);
                if((null == s3 || s3.equals("*AUTO")) && !s1.trim().equals(""))
                {
                    Object obj1 = null;
                    ArrayList arraylist1 = new ArrayList();
                    s1 = CommandKeyLabel.parseDynamicKeyLabel(s1, arraylist1);
                    for(int j = 0; j < arraylist1.size(); j++)
                    {
                        Unit unit = (Unit)arraylist1.get(j);
                        s1 = unit.text;
                        String s5 = String.valueOf(unit.sequenceNumber);
                        if(s5.length() == 1)
                            s5 = "0" + s5;
                        if(null != s1)
                            if(flag2)
                                commandkeylabellist.add(new CommandKeyLabel(s5, s1, getRecordName(), getPriority(), s4));
                            else
                            if(!flag3 && !flag)
                                commandkeylabellist1.add(new CommandKeyLabel(s5, s1, getRecordName(), getPriority(), s4));
                    }

                } else
                if(null != s1 && !s1.trim().equals(""))
                    if(flag2)
                        commandkeylabellist.add(new CommandKeyLabel(s3, s1, getRecordName(), getPriority(), s4));
                    else
                    if(!flag3 && !flag)
                        commandkeylabellist1.add(new CommandKeyLabel(s3, s1, getRecordName(), getPriority(), s4));
            } else
            {
                String s2 = visibilityconditionedlabel.getLabel();
                if(null != s2)
                    if(flag2)
                        commandkeylabellist.add(new CommandKeyLabel(s3, s2, getRecordName(), getPriority(), s4));
                    else
                    if(!flag3 && !flag)
                    {
                        commandkeylabellist1.add(new CommandKeyLabel(s3, s2, getRecordName(), getPriority(), s4));
                        flag3 = true;
                    }
            }
            if(commandkeylabellist.getLabels().hasNext() && !s3.equals("*AUTO"))
                break;
            if(commandkeylabellist1.getLabels().hasNext())
                flag3 = true;
        }

        if(commandkeylabellist.getLabels().hasNext() || flag)
            return commandkeylabellist.getLabels();
        else
            return commandkeylabellist1.getLabels();
    }

    static final String copyRight = new String("(C) Copyright IBM Corporation 1999-2003, all rights reserved");

}
