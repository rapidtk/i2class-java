// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.dhtmlview;

import java.util.Iterator;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.dhtmlview:
//            ICmdKeysInterface

public class CmdKeysInterface
    implements ICmdKeysInterface
{

    public CmdKeysInterface(Iterator iterator, Iterator iterator1, String s, String s1, int i, boolean flag, boolean flag1, 
            boolean flag2)
    {
        _ApplicationKeyListIterator = null;
        _PageOrFieldKeyListIterator = null;
        _bWidth = null;
        _bHeight = null;
        _bColumnCount = 1;
        _showKeyName = false;
        _showFlyOver = false;
        _isInAppArea = false;
        _multKeyIndex = 0;
        _PageOrFieldKeyListIterator = iterator;
        _ApplicationKeyListIterator = iterator1;
        _bWidth = s;
        _bHeight = s1;
        _bColumnCount = i;
        _showKeyName = flag;
        _showFlyOver = flag1;
        _isInAppArea = flag2;
    }

    public void reload(Iterator iterator, Iterator iterator1, String s, String s1, int i, boolean flag, boolean flag1, 
            boolean flag2)
    {
        _PageOrFieldKeyListIterator = iterator;
        _ApplicationKeyListIterator = iterator1;
        _bWidth = s;
        _bHeight = s1;
        _bColumnCount = i;
        _showKeyName = flag;
        _showFlyOver = flag1;
        _isInAppArea = flag2;
    }

    public int getColumnCount()
    {
        return _bColumnCount;
    }

    public String getButtonHeight()
    {
        return _bHeight;
    }

    public String getButtonWidth()
    {
        return _bWidth;
    }

    public Iterator getPageOrFieldKeyList()
    {
        return _PageOrFieldKeyListIterator;
    }

    public Iterator getApplicationKeyList()
    {
        return _ApplicationKeyListIterator;
    }

    public String getKeyUniqueId()
    {
        String s;
        if(_isInAppArea)
            s = "ak" + _multKeyIndex++ % 100;
        else
            s = "k";
        return s;
    }

    public boolean isKeyNameShown()
    {
        return _showKeyName;
    }

    public boolean isFlyOverShown()
    {
        return _showFlyOver;
    }

    public boolean isInAppArea()
    {
        return _isInAppArea;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2002, all rights reserved");
    private Iterator _ApplicationKeyListIterator;
    private Iterator _PageOrFieldKeyListIterator;
    private String _bWidth;
    private String _bHeight;
    private int _bColumnCount;
    private boolean _showKeyName;
    private boolean _showFlyOver;
    private boolean _isInAppArea;
    private int _multKeyIndex;

}
