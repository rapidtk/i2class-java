// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import com.ibm.as400ad.webfacing.runtime.model.def.IndicatorDataDefinition;
import java.util.List;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model:
//            BaseIndicators, IndicatorEvaluation, IIndicatorEvaluation, IIndicatorRead

public class OptionIndicators extends BaseIndicators
    implements IIndicatorEvaluation, IIndicatorRead, Cloneable
{

    OptionIndicators(IndicatorDataDefinition indicatordatadefinition)
    {
        super(indicatordatadefinition);
        _indicatorEval = new IndicatorEvaluation(this);
    }

    public boolean evaluateIndicatorExpression(String s)
    {
        return _indicatorEval.evaluateIndicatorExpression(s);
    }

    public String getIndicatorType()
    {
        return "Option";
    }

    public List getReferencedOptionIndicators()
    {
        return getIndicatorDef().getReferencedOptionIndicators();
    }

    public Object clone()
    {
        OptionIndicators optionindicators = null;
        optionindicators = (OptionIndicators)super.clone();
        optionindicators._indicatorEval = new IndicatorEvaluation(optionindicators);
        return optionindicators;
    }

    private IndicatorEvaluation _indicatorEval;
}
