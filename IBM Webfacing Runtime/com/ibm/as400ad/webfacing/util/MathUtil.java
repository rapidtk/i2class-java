// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.util;


public class MathUtil
{

    public MathUtil()
    {
    }

    public static int[] divideIntoPieces(int i, int j)
    {
        if(j <= 0)
            return null;
        int ai[] = new int[j];
        double d = 0.0001D;
        double d1 = (float)i / (float)j;
        int k = (int)d1;
        double d2 = d1 - (double)k;
        double d3 = 0.0D;
        int l = 0;
        for(int i1 = 0; i1 < j; i1++)
        {
            int j1 = k;
            d3 += d2;
            if(d3 + d > 1.0D)
            {
                j1++;
                d3--;
            }
            ai[i1] = j1;
            l += j1;
        }

        int k1 = i - l;
        ai[j - 1] += k1;
        return ai;
    }

    public static boolean equals(double d, double d1)
    {
        return Math.abs(d - d1) < 9.9999999999999995E-008D;
    }

    public static boolean equals(double d, int i)
    {
        return Math.abs(d - (double)i) < 9.9999999999999995E-008D;
    }

    public static int getWholePartOf(double d)
    {
        int i = (int)d;
        if(equals(i + 1, d))
            i++;
        return i;
    }

    public static double positiveIntegral(double d, double d1, double d2, double d3)
    {
        if(d1 < 0.0D)
            throw new IllegalArgumentException("For method positiveIntegral, parameter n must be >= 0.");
        if(d < 0.0D)
            throw new IllegalArgumentException("For method positiveIntegral, parameter c must be >= 0.");
        if(d2 > d3)
            throw new IllegalArgumentException("For method positiveIntegral, parameter a must be < parameter b.");
        if(equals(d2, d3) || equals(d, 0))
            return 0.0D;
        double d4 = d1 + 1.0D;
        double d5 = d / d4;
        if(equals(d2, 0))
            return d5 * Math.pow(d3, d4);
        if(equals(d3, 0))
            return d5 * Math.pow(Math.abs(d2), d4);
        if(d2 > 0.0D && d3 > 0.0D)
            return d5 * Math.pow(d3, d4) - d5 * Math.pow(d2, d4);
        if(d2 < 0.0D && d3 < 0.0D)
        {
            return d5 * Math.pow(Math.abs(d2), d4) - d5 * Math.pow(Math.abs(d3), d4);
        } else
        {
            double d6 = d5 * Math.pow(d3, d4);
            double d7 = d5 * Math.pow(Math.abs(d2), d4);
            return d6 + d7;
        }
    }
}
