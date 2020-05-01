// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import com.ibm.as400ad.webfacing.runtime.model.def.IndicatorDataDefinition;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model:
//            IIndicatorUpdate, IIndicatorValue

abstract class BaseIndicators
    implements IIndicatorUpdate, IIndicatorValue, Cloneable
{

    BaseIndicators(IndicatorDataDefinition indicatordatadefinition)
    {
        _indicators = new boolean[99];
        _indicatorDef = indicatordatadefinition;
    }

    public Object clone()
    {
        BaseIndicators baseindicators = null;
        try
        {
            baseindicators = (BaseIndicators)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        baseindicators._indicators = new boolean[99];
        System.arraycopy(_indicators, 0, baseindicators._indicators, 0, 99);
        return baseindicators;
    }

    static void copyIndicators(IIndicatorValue iindicatorvalue, IIndicatorUpdate iindicatorupdate)
    {
        for(int i = 1; i <= 99; i++)
            iindicatorupdate.setIndicator(i, iindicatorvalue.getIndicator(i));

    }

    public boolean getIndicator(int i)
    {
        return _indicators[i - 1];
    }

    boolean[] getIndicatorArray()
    {
        return _indicators;
    }

    IndicatorDataDefinition getIndicatorDef()
    {
        return _indicatorDef;
    }

    public abstract String getIndicatorType();

    public void setIndicator(int i, boolean flag)
    {
        _indicators[i - 1] = flag;
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer(612);
        stringbuffer.append("\n" + getIndicatorType() + " indicator values:\n");
        for(int i = 1; i <= 99; i++)
        {
            if(i < 10)
                stringbuffer.append('0');
            stringbuffer.append(i + ":");
            if(_indicators[i - 1])
                stringbuffer.append("1  ");
            else
                stringbuffer.append("0  ");
            if(i % 10 == 0)
                stringbuffer.append('\n');
        }

        stringbuffer.append('\n');
        return stringbuffer.toString();
    }

    private IndicatorDataDefinition _indicatorDef;
    private boolean _indicators[];
}
