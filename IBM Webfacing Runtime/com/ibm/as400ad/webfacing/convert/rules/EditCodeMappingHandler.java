// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.rules;

import com.ibm.as400ad.code400.dom.ExportSettings;
import com.ibm.as400ad.webfacing.convert.Util;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.*;

public class EditCodeMappingHandler
{

    private EditCodeMappingHandler()
    {
        _editCodingMapping = null;
        try
        {
            _editCodingMapping = new HashMap();
            Document document = ExportSettings.getExportSettings().getRulesDocument();
            if(document != null)
            {
                NodeList nodelist = document.getElementsByTagName("EDTCDE");
                if(nodelist != null)
                {
                    Element element = (Element)nodelist.item(0);
                    if(element != null)
                    {
                        NodeList nodelist1 = element.getElementsByTagName("Mapping");
                        for(int i = 0; i < nodelist1.getLength(); i++)
                        {
                            Element element1 = (Element)nodelist1.item(i);
                            char c = element1.getAttribute("userDefined").charAt(0);
                            char c1 = element1.getAttribute("systemCode").charAt(0);
                            if(c >= '5' && c <= '9' && (c1 >= '1' && c1 <= '4' || c1 >= 'A' && c1 <= 'D' || c1 >= 'J' && c1 <= 'Q' || c1 >= 'W' && c1 <= 'Z'))
                                _editCodingMapping.put(new Character(c), new Character(c1));
                        }

                    }
                }
            }
        }
        catch(Throwable throwable)
        {
            Util.logThrowableMessage("error in EditCodeMappingHandler", throwable, false);
        }
    }

    public static EditCodeMappingHandler getEditCodeMappingHandler()
    {
        if(_editCodeMappingHanlder == null)
            _editCodeMappingHanlder = new EditCodeMappingHandler();
        return _editCodeMappingHanlder;
    }

    public static void resetHandler()
    {
        _editCodeMappingHanlder = null;
    }

    public Map getEditCodeMapping()
    {
        return _editCodingMapping;
    }

    public static char getSystemEditCode(char c)
    {
        if(c >= '5' && c <= '9')
        {
            Map map = getEditCodeMappingHandler().getEditCodeMapping();
            if(map != null)
            {
                Character character = (Character)map.get(new Character(c));
                if(character != null)
                    return character.charValue();
            }
        }
        return c;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2000-2003 All rights reserved.");
    private Map _editCodingMapping;
    private static EditCodeMappingHandler _editCodeMappingHanlder = null;

}
