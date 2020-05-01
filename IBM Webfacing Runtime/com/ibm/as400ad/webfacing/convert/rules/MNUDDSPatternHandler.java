// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.rules;

import com.ibm.as400ad.code400.dom.ExportSettings;
import com.ibm.as400ad.webfacing.convert.Util;
import org.w3c.dom.*;

public class MNUDDSPatternHandler
{

    private MNUDDSPatternHandler()
    {
        _option = null;
        _sep = null;
        try
        {
            Document document = ExportSettings.getExportSettings().getRulesDocument();
            if(document != null)
            {
                NodeList nodelist = document.getElementsByTagName("MNUDDS");
                Element element = (Element)nodelist.item(0);
                if(element != null)
                {
                    NodeList nodelist1 = element.getElementsByTagName("Option");
                    Element element1 = (Element)nodelist1.item(0);
                    _option = element1.getAttribute("format");
                    NodeList nodelist2 = element.getElementsByTagName("Pattern");
                    Element element2 = (Element)nodelist2.item(0);
                    _sep = element2.getAttribute("separator");
                }
            }
        }
        catch(Throwable throwable)
        {
            Util.logThrowableMessage("error in MNUDDSPatternHandler", throwable, false);
        }
    }

    public String getOption()
    {
        if(_option == null || _option.trim().length() == 0)
            return null;
        else
            return _option;
    }

    public int getOptionNo(String s)
    {
        if(s != null && s.length() > 0 && _sep != null)
        {
            int i = s.indexOf(_sep);
            if(i > 0)
                try
                {
                    return Integer.parseInt(s.substring(0, i).trim());
                }
                catch(Throwable throwable) { }
        }
        return -1;
    }

    public static MNUDDSPatternHandler getMNUDDSPatternHandler()
    {
        if(_MNUDDSPatternHanlder == null)
            _MNUDDSPatternHanlder = new MNUDDSPatternHandler();
        return _MNUDDSPatternHanlder;
    }

    public static void resetHandler()
    {
        _MNUDDSPatternHanlder = null;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2000-2003 All rights reserved.");
    public static final String HYPERLINK = "hypertextlink";
    private String _option;
    private String _sep;
    private static MNUDDSPatternHandler _MNUDDSPatternHanlder = null;

}
