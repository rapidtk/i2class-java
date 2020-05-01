// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.rules;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.rules:
//            StringPatternRulet, StringPatternRule, Property, Rulet, 
//            RuletType

public class DSPFMenuPatternRulet extends StringPatternRulet
{

    public DSPFMenuPatternRulet()
    {
        super(type);
    }

    public void loadRules(Element element)
    {
        NodeList nodelist = element.getElementsByTagName("Pattern");
        for(int i = 0; i < nodelist.getLength(); i++)
        {
            Element element1 = (Element)nodelist.item(i);
            String s = element1.getAttribute(Property.KEY_SEPARATOR);
            StringPatternRule stringpatternrule = new StringPatternRule();
            stringpatternrule.setProperties(new String(""), s);
            addARule(stringpatternrule);
        }

    }

    private static RuletType type;
    static final String copyRight = new String(" (C) Copyright IBM Corporation 2000-2002 All rights reserved.");

    static 
    {
        type = RuletType.DSPF_MENU_PATTERN;
    }
}
