// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.convert.ExportHandler;
import com.ibm.as400ad.webfacing.convert.IReferencedAIDKey;
import com.ibm.as400ad.webfacing.runtime.view.CommandKeyLabelList;
import java.util.*;

public class KeySequence
    implements ENUM_KeywordIdentifiers
{

    public KeySequence(RecordNode recordnode, CommandKeyLabelList commandkeylabellist)
    {
        _isKeySequencedWithWebSettings = false;
        _functionKeySequence = new ArrayList();
        _commandKeySequence = new ArrayList();
        _sequencedFunctionKeys = new ArrayList();
        _sequencedCommandKeys = new ArrayList();
        _hiddenFunctionKeys = null;
        _hiddenCommandKeys = null;
        _unsequencedFunctionKeys = new HashMap();
        _unsequencedCommandKeys = new HashMap();
        setKeySequence(recordnode);
        _visibleKeyLabelList = commandkeylabellist;
    }

    CommandKeyLabelList getVisibleKeyLabelList()
    {
        return _visibleKeyLabelList;
    }

    void addFunctionKey(IReferencedAIDKey ireferencedaidkey)
    {
        if(_isKeySequencedWithWebSettings)
            insert(_unsequencedFunctionKeys, ireferencedaidkey, false);
        else
            insert(_sequencedFunctionKeys, ireferencedaidkey, false);
    }

    void addCommandKey(IReferencedAIDKey ireferencedaidkey)
    {
        com.ibm.as400ad.webfacing.runtime.view.def.CommandKeyLabel commandkeylabel = _visibleKeyLabelList.getLabel(ireferencedaidkey.name());
        if(_visibleKeyLabelList != null && commandkeylabel != null)
            _visibleKeyLabelList.remove(ireferencedaidkey.name());
        boolean flag = ireferencedaidkey.getKeywordID() == 190 || ireferencedaidkey.getKeywordID() == 185;
        if(_isKeySequencedWithWebSettings)
            insert(_unsequencedCommandKeys, ireferencedaidkey, flag);
        else
            insert(_sequencedCommandKeys, ireferencedaidkey, flag);
    }

    public int compareAIDKey(Object obj, Object obj1)
    {
        String s = ((IReferencedAIDKey)obj).name();
        String s1 = ((IReferencedAIDKey)obj1).name();
        return stripCommandPrefix(s).compareTo(stripCommandPrefix(s1));
    }

    public static String stripCommandPrefix(String s)
    {
        if(s.length() == 4 && Character.isDigit(s.charAt(2)) && (s.substring(0, 2).equals("CA") || s.substring(0, 2).equals("CF")))
            return s.substring(2);
        else
            return s;
    }

    void insert(List list, IReferencedAIDKey ireferencedaidkey, boolean flag)
    {
        for(int i = 0; i < list.size(); i++)
        {
            int j = compareAIDKey(ireferencedaidkey, list.get(i));
            if(j < 0)
            {
                list.add(i, ireferencedaidkey);
                return;
            }
            if(j == 0)
            {
                if(flag)
                {
                    list.remove(i);
                    list.add(i, ireferencedaidkey);
                }
                return;
            }
        }

        list.add(ireferencedaidkey);
    }

    void insert(Map map, IReferencedAIDKey ireferencedaidkey, boolean flag)
    {
        if(!map.containsKey(ireferencedaidkey.name()) || flag)
            map.put(ireferencedaidkey.name(), ireferencedaidkey);
    }

    Iterator getSequencedFunctionKeys()
    {
        if(_isKeySequencedWithWebSettings)
            _sequencedFunctionKeys = sequenceAndSuppress(_unsequencedFunctionKeys, _functionKeySequence, false);
        return _sequencedFunctionKeys.iterator();
    }

    Iterator getSequencedCommandKeys()
    {
        if(_isKeySequencedWithWebSettings)
            _sequencedCommandKeys = sequenceAndSuppress(_unsequencedCommandKeys, _commandKeySequence, true);
        return _sequencedCommandKeys.iterator();
    }

    List sequenceAndSuppress(Map map, List list, boolean flag)
    {
        ArrayList arraylist = new ArrayList();
        Collection collection = map.values();
        for(Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            String s = (String)iterator.next();
            IReferencedAIDKey ireferencedaidkey = (IReferencedAIDKey)map.get(s);
            if(ireferencedaidkey != null)
            {
                arraylist.add(ireferencedaidkey);
                collection.remove(ireferencedaidkey);
            }
        }

        if(flag)
            _hiddenCommandKeys = collection.iterator();
        else
            _hiddenFunctionKeys = collection.iterator();
        return arraylist;
    }

    public void setKeySequence(RecordNode recordnode)
    {
        try
        {
            if(recordnode.hasWebSettings())
            {
                WebSettingsNodeEnumeration websettingsnodeenumeration = recordnode.getWebSettings();
                Object obj = null;
                while(websettingsnodeenumeration.hasMoreElements()) 
                {
                    WebSettingsNode websettingsnode = websettingsnodeenumeration.nextWebSettings();
                    int i = websettingsnode.getType();
                    String s = websettingsnode.getValue();
                    if(i == 11)
                    {
                        _isKeySequencedWithWebSettings = true;
                        StringTokenizer stringtokenizer = new StringTokenizer(s);
                        if(stringtokenizer.countTokens() > 0)
                            while(stringtokenizer.hasMoreTokens()) 
                            {
                                String s1 = (String)stringtokenizer.nextElement();
                                if(s1.length() > 0)
                                    if(s1.charAt(0) == 'C' && (s1.charAt(1) == 'A' || s1.charAt(1) == 'F'))
                                        _commandKeySequence.add(s1);
                                    else
                                        _functionKeySequence.add(s1);
                            }
                    }
                }
            }
        }
        catch(Throwable throwable)
        {
            if(recordnode != null)
                recordnode.logEvent(98, new String[] {
                    "Error with key sequence at node: " + recordnode.getName() + " = " + throwable
                });
            ExportHandler.err(1, "error in KeySequence.setKeySequence() while generating  = " + throwable);
        }
    }

    Iterator getHiddenFunctionKeys()
    {
        return _hiddenFunctionKeys;
    }

    Iterator getHiddenCommandKeys()
    {
        return _hiddenCommandKeys;
    }

    private boolean _isKeySequencedWithWebSettings;
    private List _functionKeySequence;
    private List _commandKeySequence;
    private List _sequencedFunctionKeys;
    private List _sequencedCommandKeys;
    private Iterator _hiddenFunctionKeys;
    private Iterator _hiddenCommandKeys;
    private Map _unsequencedFunctionKeys;
    private Map _unsequencedCommandKeys;
    private CommandKeyLabelList _visibleKeyLabelList;
}
