// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.webfacing.convert.rules.Unit;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.host.HostJobInfo;
import com.ibm.as400ad.webfacing.runtime.view.def.AIDKey;
import com.ibm.as400ad.webfacing.runtime.view.def.CommandKeyLabel;
import com.ibm.as400ad.webfacing.runtime.view.def.ConditionedCommandKeyLabel;
import java.io.Serializable;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            ICommandKeyLabel, AIDKeyDictionary, RecordViewBean, RecordFeedbackBean

public class CommandKeyLabelList
    implements Cloneable, Serializable
{

    public CommandKeyLabelList()
    {
        _list = new HashMap();
    }

    public Object clone()
    {
        CommandKeyLabelList commandkeylabellist = null;
        try
        {
            commandkeylabellist = (CommandKeyLabelList)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        commandkeylabellist._list = (HashMap)_list.clone();
        return commandkeylabellist;
    }

    public void add(ICommandKeyLabel icommandkeylabel)
    {
        _list.put(icommandkeylabel.getKeyCode(), icommandkeylabel);
    }

    public CommandKeyLabel getLabel(String s)
    {
        return (CommandKeyLabel)_list.get(new Byte(AIDKeyDictionary.getKeyCode(s)));
    }

    public Iterator getLabels()
    {
        return _list.values().iterator();
    }

    public boolean isHigherOrEqualPriority(ICommandKeyLabel icommandkeylabel)
    {
        Byte byte1 = icommandkeylabel.getKeyCode();
        ICommandKeyLabel icommandkeylabel1 = (ICommandKeyLabel)_list.get(byte1);
        return icommandkeylabel1 == null || icommandkeylabel1.getPriority() <= icommandkeylabel.getPriority();
    }

    public void mergeList(CommandKeyLabelList commandkeylabellist)
    {
        Iterator iterator = commandkeylabellist.getLabels();
        mergeList(iterator);
    }

    public void mergeList(Iterator iterator)
    {
        while(iterator.hasNext()) 
        {
            ICommandKeyLabel icommandkeylabel = (ICommandKeyLabel)iterator.next();
            if(isHigherOrEqualPriority(icommandkeylabel))
                add(icommandkeylabel);
        }
    }

    public void mergeConditionedLabelList(CommandKeyLabelList commandkeylabellist)
    {
        for(Iterator iterator = commandkeylabellist.getLabels(); iterator.hasNext();)
        {
            ConditionedCommandKeyLabel conditionedcommandkeylabel = (ConditionedCommandKeyLabel)iterator.next();
            ConditionedCommandKeyLabel conditionedcommandkeylabel1 = (ConditionedCommandKeyLabel)_list.get(conditionedcommandkeylabel.getKeyCode());
            if(conditionedcommandkeylabel1 != null)
                conditionedcommandkeylabel1.mergeContionedLabelList(conditionedcommandkeylabel.getConditionedLabels());
            else
                add(conditionedcommandkeylabel);
        }

    }

    public void remove(String s)
    {
        _list.remove(new Byte(AIDKeyDictionary.getKeyCode(s)));
    }

    public void updateCommandKeyLabels(Iterator iterator, boolean flag)
    {
        while(iterator.hasNext()) 
        {
            AIDKey aidkey = (AIDKey)iterator.next();
            Byte byte1 = aidkey.getKeyCode();
            if(!isHigherOrEqualPriority(aidkey))
                if(_list.get(byte1) instanceof CommandKeyLabel)
                    aidkey.setLabel((CommandKeyLabel)_list.get(byte1));
                else
                    aidkey.setLabel(((AIDKey)_list.get(byte1)).getLabel());
            if(flag && _list.get(byte1) != null)
                remove(aidkey.getKeyName());
        }
    }

    public void resolveConditionAndMergeList(Iterator iterator, RecordViewBean recordviewbean, boolean flag)
    {
        while(iterator.hasNext()) 
        {
            ConditionedCommandKeyLabel conditionedcommandkeylabel = (ConditionedCommandKeyLabel)iterator.next();
            Iterator iterator1 = conditionedcommandkeylabel.getLabel(recordviewbean, flag);
            Object obj = null;
            if(isHigherOrEqualPriority(conditionedcommandkeylabel) && iterator1 != null)
                while(iterator1.hasNext()) 
                {
                    CommandKeyLabel commandkeylabel = (CommandKeyLabel)iterator1.next();
                    if(null != commandkeylabel)
                        add(commandkeylabel);
                }
        }
    }

    public void resolveDynamicKeyLabelsAndMergeList(Iterator iterator, RecordViewBean recordviewbean)
    {
        while(iterator.hasNext()) 
        {
            CommandKeyLabel commandkeylabel = (CommandKeyLabel)iterator.next();
            String s2 = commandkeylabel.getKeyName();
            String s1 = commandkeylabel.getFieldName();
            if(null != s1)
            {
                HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
                hostjobinfo.submitRequests();
                String s = recordviewbean.getFeedbackBean().getFieldValue(s1);
                if(null == s2 || s2.equals("*AUTO"))
                {
                    ArrayList arraylist = new ArrayList();
                    CommandKeyLabel commandkeylabel1 = null;
                    s = CommandKeyLabel.parseDynamicKeyLabel(s, arraylist);
                    for(int i = 0; i < arraylist.size(); i++)
                    {
                        Unit unit = (Unit)arraylist.get(i);
                        s = unit.text;
                        String s3 = String.valueOf(unit.sequenceNumber);
                        if(s3.length() == 1)
                            s3 = "0" + s3;
                        if(null != s3 && null != s)
                            commandkeylabel1 = new CommandKeyLabel(s3, s, commandkeylabel.getRecordName(), commandkeylabel.getPriority(), commandkeylabel.getFieldName());
                        if(isHigherOrEqualPriority(commandkeylabel))
                            add(commandkeylabel1);
                    }

                } else
                {
                    if(null != s)
                        commandkeylabel.setKeyLabel(s);
                    if(isHigherOrEqualPriority(commandkeylabel))
                        add(commandkeylabel);
                }
            } else
            if(isHigherOrEqualPriority(commandkeylabel))
                add(commandkeylabel);
        }
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2001-2003");
    private HashMap _list;

}
