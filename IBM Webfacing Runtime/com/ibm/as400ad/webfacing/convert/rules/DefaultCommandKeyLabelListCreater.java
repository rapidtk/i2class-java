// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.rules;

import com.ibm.as400ad.code400.dom.ExportSettings;
import com.ibm.as400ad.webfacing.runtime.view.CommandKeyLabelList;
import com.ibm.as400ad.webfacing.runtime.view.def.CommandKeyLabel;
import java.io.PrintStream;
import org.w3c.dom.*;

public class DefaultCommandKeyLabelListCreater
{

    private DefaultCommandKeyLabelListCreater()
    {
        _defaultCommandKeyLabelList = null;
        _defaultCommandKeyLabelList = new CommandKeyLabelList();
        try
        {
            Document document = ExportSettings.getExportSettings().getRulesDocument();
            if(document != null)
            {
                int i = 0;
                NodeList nodelist = document.getElementsByTagName("DefaultText");
                for(int j = 0; j < nodelist.getLength(); j++)
                {
                    String s = null;
                    String s1 = null;
                    Element element = (Element)nodelist.item(j);
                    s = element.getAttribute("key");
                    s1 = element.getAttribute("text");
                    if(s != null && s1 != null && !s1.equals(""))
                    {
                        if(s.charAt(0) == 'F')
                            s = s.substring(1);
                        if(s.length() == 1)
                            s = "0" + s;
                        CommandKeyLabel commandkeylabel = new CommandKeyLabel(s, s1, i);
                        _defaultCommandKeyLabelList.add(commandkeylabel);
                    }
                }

            }
        }
        catch(Exception exception)
        {
            System.out.println("Exception in DefaultCommandKeyLabelListCreater()  " + exception.getClass().getName() + ": " + exception.getMessage());
        }
    }

    public CommandKeyLabelList getDefaultCommandKeyLabelList()
    {
        return _defaultCommandKeyLabelList;
    }

    public static DefaultCommandKeyLabelListCreater getDefaultCMDKeyLabelListCreater()
    {
        if(_creater == null)
            _creater = new DefaultCommandKeyLabelListCreater();
        return _creater;
    }

    public static void resetCreater()
    {
        _creater = null;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2000-2002 All rights reserved.");
    private CommandKeyLabelList _defaultCommandKeyLabelList;
    private static DefaultCommandKeyLabelListCreater _creater = null;

}
