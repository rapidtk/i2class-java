// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;


public class SubfileInfo
{

    public SubfileInfo(int i, int j, int k)
    {
        SFLPAG = 0;
        recordsPerRow = 0;
        totalRepeats = 0;
        SFLPAG = i;
        recordsPerRow = j;
        totalRepeats = k;
    }

    public int SFLPAG;
    public int recordsPerRow;
    public int totalRepeats;
}
