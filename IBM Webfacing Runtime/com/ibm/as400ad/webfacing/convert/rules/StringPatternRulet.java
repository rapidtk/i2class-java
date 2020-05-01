// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.rules;

import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.rules:
//            Rulet, ResultContainer, Rule, StringMatchingResult, 
//            RuletType

public abstract class StringPatternRulet extends Rulet
{

    public StringPatternRulet(RuletType rulettype)
    {
        super(rulettype);
    }

    public Object apply(Object obj)
    {
        if(!(obj instanceof String))
            return null;
        Enumeration enumeration = super.rules.elements();
        String s = (String)obj;
        if(enumeration.hasMoreElements())
        {
            ResultContainer resultcontainer = (ResultContainer)super.resultContainerPrototype.clone();
            do
            {
                Rule rule = (Rule)enumeration.nextElement();
                s = (String)rule.apply(s, resultcontainer, getAllRuleProperties());
            } while(enumeration.hasMoreElements());
            if(resultcontainer.size() > 0)
                return new StringMatchingResult(s, resultcontainer);
        }
        return new StringMatchingResult(s, null);
    }

    private Vector getAllRuleProperties()
    {
        Vector vector = new Vector();
        Rule rule;
        for(Enumeration enumeration = super.rules.elements(); enumeration.hasMoreElements(); vector.add(rule.getProperties()))
            rule = (Rule)enumeration.nextElement();

        return vector;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2000-2002 All rights reserved.");

}
