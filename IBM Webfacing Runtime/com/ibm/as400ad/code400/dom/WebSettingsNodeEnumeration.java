// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            WebSettingsNode

public class WebSettingsNodeEnumeration
    implements Enumeration
{

    public WebSettingsNodeEnumeration(Vector vector)
    {
        webSettings = vector;
        index = 0;
    }

    public boolean hasMoreElements()
    {
        if(webSettings == null)
            return false;
        else
            return index < webSettings.size();
    }

    public Object nextElement()
    {
        if(webSettings == null)
            return null;
        if(index < webSettings.size())
            return webSettings.elementAt(index++);
        else
            return null;
    }

    public WebSettingsNode nextWebSettings()
    {
        return (WebSettingsNode)nextElement();
    }

    private int index;
    private Vector webSettings;
}
