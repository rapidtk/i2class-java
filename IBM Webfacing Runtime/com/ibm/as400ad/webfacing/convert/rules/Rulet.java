// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.rules;

import java.util.Vector;
import org.w3c.dom.Element;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.rules:
//            ResultContainer, RuletType, Rule

public abstract class Rulet
{

    protected Rulet(RuletType rulettype)
    {
        resultContainer = new ResultContainer();
        rules = new Vector();
        resultContainerPrototype = new ResultContainer();
        type = rulettype;
        rules = new Vector();
    }

    public abstract Object apply(Object obj);

    public RuletType getType()
    {
        return type;
    }

    public void removeAllRules()
    {
        rules.removeAllElements();
    }

    public abstract void loadRules(Element element);

    public void addARule(Rule rule)
    {
        rules.add(rule);
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2000-2002 All rights reserved.");
    protected ResultContainer resultContainer;
    private RuletType type;
    protected Vector rules;
    protected ResultContainer resultContainerPrototype;

}
