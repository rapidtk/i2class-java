// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import java.io.PrintStream;

public class ElapsedTime
{

    public ElapsedTime()
    {
    }

    public void setStartTime()
    {
        startTime = System.currentTimeMillis();
    }

    public void setEndTime()
    {
        endTime = System.currentTimeMillis();
    }

    public long getElapsedTime()
    {
        return endTime - startTime;
    }

    public String toString()
    {
        long l = getElapsedTime();
        long l1 = l % 1000L;
        long l2 = l / 1000L;
        long l3 = l2 / 60L;
        long l4 = l2 / 3600L;
        long l5 = (int)(l / 0x36ee80L);
        long l6 = (int)(l3 - l5 * 60L);
        long l7 = (int)(l2 - l5 * 60L * 60L - l6 * 60L);
        String s = "Elapsed time: " + l5 + " hours, " + l6 + " minutes, " + l7 + " seconds, " + l1 + " milliseconds";
        return s;
    }

    public static void main(String args[])
    {
        ElapsedTime elapsedtime = new ElapsedTime();
        elapsedtime.setET(5L, 4L, 3L, 100L);
        System.out.println(elapsedtime);
        elapsedtime.setET(25L, 14L, 53L, 999L);
        System.out.println(elapsedtime);
        elapsedtime.setET(25L, 0L, 53L, 0L);
        System.out.println(elapsedtime);
        elapsedtime.setET(0L, 0L, 13L, 0L);
        System.out.println(elapsedtime);
    }

    public void setET(long l, long l1, long l2, long l3)
    {
        long l4 = l3 + l2 * 1000L + l1 * 60L * 1000L + l * 60L * 60L * 1000L;
        setET(l4);
    }

    public void setET(long l)
    {
        startTime = 0L;
        endTime = l;
    }

    private long startTime;
    private long endTime;
}
