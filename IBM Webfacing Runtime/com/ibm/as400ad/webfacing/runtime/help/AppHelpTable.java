// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.view.CursorPosition;
import com.ibm.as400ad.webfacing.runtime.view.LocationOnDevice;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.help:
//            HelpList, HelpTable, HelpGroup, HelpArea, 
//            HelpPanelGroup, HelpDefinition, HelpRecord, HelpNotFoundException, 
//            IAppHelpTable, HelpException, GenericHelpArea

public class AppHelpTable
    implements IAppHelpTable, Cloneable
{

    public AppHelpTable()
    {
        _helpTable = null;
        _helpList = null;
        _defaultHelp = null;
        _primaryHelp = null;
        _currentHelp = null;
        _sublist = null;
        _sublistIdx = -1;
        _groupList = null;
        _title = "";
        _isHtmlHelp = false;
        _trace = WFSession.getTraceLogger();
        _helpList = new HelpList();
        _helpTable = new HelpTable();
    }

    private void addHelpGroupList(String s, HelpGroup helpgroup, HelpDefinition helpdefinition)
    {
        String s1 = s + "$" + "GROUP" + "$" + helpgroup.getName();
        _helpTable.addWithoutDuplicates(s1, helpdefinition, helpgroup.getSequence());
        _helpTable.addWithoutDuplicates(s + "$" + helpdefinition.getDefinition(), s1);
    }

    public void addHelpGroupList(String s, Object obj)
    {
        if(obj instanceof Iterator)
        {
            HelpGroup helpgroup;
            for(Iterator iterator = (Iterator)obj; iterator.hasNext(); addHelpGroupList(s, helpgroup, helpgroup.getDefinition()))
            {
                helpgroup = (HelpGroup)iterator.next();
                helpgroup.getDefinition().setObject(s);
            }

        }
    }

    public void addHelpList(Object obj, Object obj1)
    {
        if(obj1 instanceof List)
            try
            {
                List list = (List)obj1;
                if(list.size() == 0)
                    return;
                HelpArea helparea = (HelpArea)list.get(list.size() - 1);
                if(helparea.getHelpDefinition() instanceof HelpPanelGroup)
                    _isHtmlHelp = true;
                else
                    _isHtmlHelp = false;
                if(helparea.isDefaultHelp())
                {
                    _defaultHelp = helparea.getHelpDefinition();
                    list.remove(helparea);
                    if(list.size() == 0)
                        return;
                }
                removeHelpWithDupArea(list);
                removeHelpWithDupAreaFromHelpList(list);
                List list1 = createSublists(list);
                List list2 = (List)list1.get(list1.size() - 1);
                helparea = (HelpArea)list2.get(list2.size() - 1);
                if(!_helpList.isEmpty() && !helparea.isBoundary())
                {
                    _helpList.addFirstToSublist(list2, 0);
                    list1.remove(list2);
                }
                _helpList.addToList(list1);
            }
            catch(IndexOutOfBoundsException indexoutofboundsexception)
            {
                return;
            }
    }

    public void clear(boolean flag)
    {
        _helpTable.clear();
        _helpList.clear();
        if(flag)
            _defaultHelp = null;
    }

    public Object clone()
    {
        AppHelpTable apphelptable = null;
        try
        {
            apphelptable = (AppHelpTable)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        apphelptable._helpList = (HelpList)_helpList.clone();
        apphelptable._helpTable = (HelpTable)_helpTable.clone();
        return apphelptable;
    }

    private List createSublists(List list)
    {
        ArrayList arraylist = new ArrayList();
        LinkedList linkedlist = new LinkedList();
        for(Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            HelpArea helparea = (HelpArea)iterator.next();
            linkedlist.add(helparea);
            if(helparea.isBoundary())
            {
                arraylist.add(linkedlist);
                linkedlist = new LinkedList();
            }
        }

        if(!linkedlist.isEmpty())
            arraylist.add(linkedlist);
        return arraylist;
    }

    private List findHelpGroupList(HelpDefinition helpdefinition)
    {
        Object obj = _helpTable.get(_currentHelp.getObject() + "$" + _currentHelp.getDefinition(), 0);
        if(obj == null)
        {
            return null;
        } else
        {
            List list = _helpTable.getList(obj);
            return list;
        }
    }

    private HelpDefinition findHelpInGroup(boolean flag, boolean flag1)
    {
        _groupList = findHelpGroupList(_currentHelp);
        if(_groupList == null)
            return null;
        int i = _groupList.indexOf(_currentHelp);
        if(i == -1)
            return null;
        if(flag)
        {
            if(--i <= -1)
                if(flag1)
                    i = _groupList.size() - 1;
                else
                    return null;
        } else
        if(++i >= _groupList.size())
            if(flag1)
                i = 0;
            else
                return null;
        HelpDefinition helpdefinition = (HelpDefinition)_groupList.get(i);
        return (HelpRecord)helpdefinition;
    }

    private HelpDefinition findHelpInSublist(boolean flag)
    {
        if(_sublist == null)
            return null;
        if(_sublist.size() <= 0 || _sublist.size() == 1 && _sublistIdx == 0)
            return null;
        HelpDefinition helpdefinition = null;
        boolean flag1 = false;
        do
        {
            if(flag)
            {
                if(--_sublistIdx <= -1)
                {
                    flag1 = true;
                    _sublistIdx = _sublist.size() - 1;
                }
            } else
            if(++_sublistIdx >= _sublist.size())
            {
                flag1 = true;
                _sublistIdx = 0;
            }
            helpdefinition = ((HelpArea)_sublist.get(_sublistIdx)).getHelpDefinition();
            if(_groupList == null || _groupList != null && _groupList.indexOf(helpdefinition) == -1)
                break;
            if(!_currentHelp.equals(helpdefinition) || !flag1)
                continue;
            helpdefinition = null;
            break;
        } while(true);
        return helpdefinition;
    }

    public Object findNext()
    {
        HelpDefinition helpdefinition = findHelpInGroup(false, false);
        if(helpdefinition == null)
        {
            helpdefinition = findHelpInSublist(false);
            if(helpdefinition == null)
            {
                helpdefinition = findHelpInGroup(false, true);
                if(helpdefinition == null)
                    helpdefinition = _primaryHelp;
            }
        }
        _currentHelp = helpdefinition;
        ArrayList arraylist = new ArrayList();
        arraylist.add(_currentHelp);
        return arraylist;
    }

    public Object findPrevious()
    {
        HelpDefinition helpdefinition = findHelpInGroup(true, false);
        if(helpdefinition == null)
        {
            helpdefinition = findHelpInSublist(true);
            if(helpdefinition == null)
            {
                helpdefinition = findHelpInGroup(true, true);
                if(helpdefinition == null)
                    helpdefinition = _primaryHelp;
            }
        }
        _currentHelp = helpdefinition;
        ArrayList arraylist = new ArrayList();
        arraylist.add(_currentHelp);
        return arraylist;
    }

    public HelpDefinition findPrimaryHelp(int i, int j, String s, String s1)
        throws HelpNotFoundException
    {
        Iterator iterator = _helpList.getListAsIterator();
        Object obj = null;
        boolean flag = false;
        while(iterator.hasNext()) 
        {
            List list = (List)iterator.next();
            Object obj1 = null;
            for(int k = 0; k < list.size(); k++)
            {
                HelpArea helparea = (HelpArea)list.get(k);
                if(s1 != null && helparea.isSameField(s1) || helparea.isWithin(i, j))
                {
                    _currentHelp = helparea.getHelpDefinition();
                    _sublist = list;
                    _sublistIdx = k;
                    return _currentHelp;
                }
            }

        }
        if(!_isHtmlHelp && _defaultHelp != null)
        {
            _currentHelp = _defaultHelp;
            return _currentHelp;
        } else
        {
            throw new HelpNotFoundException(2);
        }
    }

    public Object getExtendedHelp(List list)
    {
        ArrayList arraylist = new ArrayList();
        if(_defaultHelp != null)
            arraylist.add(_defaultHelp);
        if(list != null)
        {
            for(Iterator iterator = list.iterator(); iterator.hasNext();)
            {
                HelpArea helparea = (HelpArea)iterator.next();
                if(!helparea.isExcluded())
                    arraylist.add(helparea.getHelpDefinition());
            }

        }
        return arraylist;
    }

    public Object getExtendedHelp()
    {
        return getExtendedHelp(_sublist);
    }

    public String getHelpTitle()
    {
        return _title;
    }

    public Object getOnlineHelp(Object obj)
        throws HelpException, HelpNotFoundException
    {
        return getOnlineHelp(obj, true);
    }

    public Object getOnlineHelp(Object obj, boolean flag)
        throws HelpException, HelpNotFoundException
    {
        ArrayList arraylist = new ArrayList();
        LocationOnDevice locationondevice = (LocationOnDevice)obj;
        CursorPosition cursorposition = locationondevice.getCursorPosition();
        try
        {
            _primaryHelp = findPrimaryHelp(cursorposition.getRow(), cursorposition.getColumn(), locationondevice.getControlRecord(), locationondevice.getField());
        }
        catch(HelpNotFoundException helpnotfoundexception)
        {
            if(_isHtmlHelp && hasHelp())
                return getExtendedHelp(_helpList.isEmpty() ? null : _helpList.getSublist(0));
            else
                throw helpnotfoundexception;
        }
        arraylist.add(_primaryHelp);
        if(!flag)
        {
            arraylist.addAll(getPageDownList());
            _primaryHelp = null;
        }
        return arraylist;
    }

    public List getPageDownList()
    {
        ArrayList arraylist = new ArrayList();
        for(HelpDefinition helpdefinition = null; (helpdefinition = (HelpDefinition)findNext()) != null;)
        {
            if(helpdefinition == _primaryHelp)
                break;
            arraylist.add(helpdefinition);
        }

        return arraylist;
    }

    public List getPageUpList()
    {
        ArrayList arraylist = new ArrayList();
        for(HelpDefinition helpdefinition = null; (helpdefinition = (HelpDefinition)findPrevious()) != null;)
        {
            if(helpdefinition == _primaryHelp)
                break;
            arraylist.add(helpdefinition);
        }

        return arraylist;
    }

    public boolean hasHelp()
    {
        return !_helpList.isEmpty() || _defaultHelp != null;
    }

    public boolean hasExtendedHelp()
    {
        return _sublist != null && (_sublist.size() > 1 || _defaultHelp != null);
    }

    public boolean isHtmlHelp()
    {
        return _isHtmlHelp;
    }

    public void removeHelp(int i, int j)
    {
        for(Iterator iterator = _helpList.getListAsIterator(); iterator.hasNext();)
        {
            List list = (List)iterator.next();
            for(Iterator iterator1 = list.iterator(); iterator1.hasNext();)
            {
                HelpArea helparea = (HelpArea)iterator1.next();
                if(helparea.getTop() >= i && helparea.getBottom() <= j)
                    iterator1.remove();
            }

            if(list.isEmpty())
                iterator.remove();
        }

    }

    private void removeHelpWithDupArea(List list)
    {
        for(int i = list.size() - 1; i >= 0; i--)
        {
            HelpArea helparea = (HelpArea)list.get(i);
            int j = list.indexOf(helparea);
            if(j != i)
            {
                if(i + 1 < list.size())
                    removeHelpWithNoArea(list.listIterator(i + 1));
                list.remove(i);
            }
        }

    }

    private void removeHelpWithDupAreaFromHelpList(List list)
    {
        for(Iterator iterator = _helpList.getListAsIterator(); iterator.hasNext();)
        {
            List list1 = (List)iterator.next();
            for(Iterator iterator1 = list1.iterator(); iterator1.hasNext();)
            {
                Object obj = iterator1.next();
                if(list.contains(obj))
                {
                    iterator1.remove();
                    removeHelpWithNoArea(iterator1);
                }
            }

            if(list1.isEmpty())
                iterator.remove();
        }

    }

    private void removeHelpWithNoArea(Iterator iterator)
    {
        for(; iterator.hasNext(); iterator.remove())
        {
            HelpArea helparea = (HelpArea)iterator.next();
            if(!helparea.isForSecondaryHelpOnly())
                return;
        }

    }

    public void setHelpTitle(String s)
    {
        _title = s;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2001, 2002, all rights reserved");
    private static ResourceBundle _resmri;
    private static final String GROUP = "GROUP";
    private HelpTable _helpTable;
    private HelpList _helpList;
    private HelpDefinition _defaultHelp;
    private HelpDefinition _primaryHelp;
    private HelpDefinition _currentHelp;
    private List _sublist;
    private int _sublistIdx;
    private List _groupList;
    private String _title;
    private boolean _isHtmlHelp;
    protected ITraceLogger _trace;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
