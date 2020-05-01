// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model:
//            IIndicatorValue

class IndicatorEvaluation
    implements Serializable
{
    class Indicator
        implements Serializable
    {

        public boolean evaluate()
        {
            if(negated)
                return !getOptionIndicator(index);
            else
                return getOptionIndicator(index);
        }

        public String toString()
        {
            if(negated)
                return "N" + index;
            else
                return "" + index;
        }

        private int index;
        private boolean negated;

        Indicator(String s, boolean flag)
            throws WebfacingInternalException
        {
            negated = flag;
            try
            {
                index = Integer.parseInt(s);
            }
            catch(NumberFormatException numberformatexception)
            {
                throw new WebfacingInternalException("Indicator in expression " + s + "is not a valid string representation");
            }
            if(index < 1 || index > 99)
                throw new WebfacingInternalException("Invalid indicator " + s + ": index out of range 1 to 99.");
            else
                return;
        }
    }


    public IndicatorEvaluation(IIndicatorValue iindicatorvalue)
    {
        _indicatorData = null;
        _indicatorData = iindicatorvalue;
    }

    private boolean evaluateCondition(Vector vector)
    {
        for(int i = 0; i < vector.size(); i++)
            if(!((Indicator)vector.elementAt(i)).evaluate())
                return false;

        return true;
    }

    private boolean evaluateExpression(Vector vector)
    {
        if(vector.isEmpty())
            return true;
        for(int i = 0; i < vector.size(); i++)
            if(evaluateCondition((Vector)vector.elementAt(i)))
                return true;

        return false;
    }

    public boolean evaluateIndicatorExpression(String s)
    {
        try
        {
            Vector vector = parseExpression(s);
            return evaluateExpression(vector);
        }
        catch(WebfacingInternalException webfacinginternalexception)
        {
            WFSession.getTraceLogger().err(2, "Error: '" + webfacinginternalexception.toString() + "' found while parsing indicator expression: " + s);
        }
        return false;
    }

    public boolean getOptionIndicator(int i)
    {
        return _indicatorData.getIndicator(i);
    }

    private Vector parseCondition(String s)
        throws WebfacingInternalException
    {
        Vector vector = new Vector();
        for(StringTokenizer stringtokenizer = new StringTokenizer(s, "N ", true); stringtokenizer.hasMoreTokens();)
        {
            String s1 = stringtokenizer.nextToken();
            if(s1.equals("N"))
                vector.addElement(new Indicator(stringtokenizer.nextToken(), true));
            else
            if(!s1.trim().equals(""))
                vector.addElement(new Indicator(s1, false));
        }

        return vector;
    }

    private Vector parseExpression(String s)
        throws WebfacingInternalException
    {
        Vector vector = new Vector();
        String s1;
        for(StringTokenizer stringtokenizer = new StringTokenizer(s, "O", false); stringtokenizer.hasMoreTokens(); vector.addElement(parseCondition(s1)))
            s1 = stringtokenizer.nextToken();

        return vector;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2000, 2002.  All Rights Reserved.";
    IIndicatorValue _indicatorData;
}
