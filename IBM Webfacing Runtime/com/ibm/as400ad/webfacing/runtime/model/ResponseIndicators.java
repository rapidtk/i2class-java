// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.model.def.IndicatorDataDefinition;
import com.ibm.as400ad.webfacing.runtime.view.IResponseIndicator;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model:
//            BaseIndicators, IIndicatorArea, IIndicatorData, IIndicatorValue

public class ResponseIndicators extends BaseIndicators
    implements IIndicatorArea, IIndicatorData, Cloneable
{

    public ResponseIndicators(IndicatorDataDefinition indicatordatadefinition)
    {
        super(indicatordatadefinition);
        _trace = WFSession.getTraceLogger();
    }

    void clearReferencedResponseIndicators()
    {
        Integer integer;
        for(Iterator iterator = getReferencedResponseIndicators(); iterator.hasNext(); setIndicator(integer.intValue(), false))
            integer = (Integer)iterator.next();

    }

    public void clearResponseIndicators(Iterator iterator)
    {
        IResponseIndicator iresponseindicator;
        for(; iterator.hasNext(); setIndicator(iresponseindicator.getIndex(), false))
            iresponseindicator = (IResponseIndicator)iterator.next();

    }

    public String getIndicatorType()
    {
        return "Response";
    }

    public Iterator getReferencedResponseIndicators()
    {
        return getIndicatorDef().getReferencedResponseIndicators();
    }

    public void mergeReferencedRIs(IIndicatorData iindicatordata)
    {
        Integer integer;
        for(Iterator iterator = iindicatordata.getReferencedResponseIndicators(); iterator.hasNext(); setIndicator(integer.intValue(), iindicatordata.getIndicator(integer.intValue())))
            integer = (Integer)iterator.next();

    }

    String produceIndicatorsForIOBuffer()
    {
        StringBuffer stringbuffer = new StringBuffer();
        List list = getIndicatorDef().getReferencedResponseIndicatorList();
        stringbuffer.append((char)(2 * list.size()));
        for(Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            Integer integer = (Integer)iterator.next();
            if(getIndicator(integer.intValue()))
                stringbuffer.append("1");
            else
                stringbuffer.append("0");
        }

        return stringbuffer.toString();
    }

    public void writeIndicatorArea(DataOutputStream dataoutputstream)
        throws IOException
    {
        byte abyte0[] = new byte[13];
        int i = 0;
        boolean aflag[] = getIndicatorArray();
        for(int j = 1; j <= 13; j++)
        {
            int k = 128;
            abyte0[j - 1] = 0;
            for(int i1 = 1; i1 <= 8; i1++)
            {
                if(aflag[i])
                    abyte0[j - 1] += k;
                k >>= 1;
                if(++i >= 99)
                    break;
            }

        }

        for(int l = 0; l <= 12; l++)
            dataoutputstream.writeByte(abyte0[l]);

    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    protected ITraceLogger _trace;
}
