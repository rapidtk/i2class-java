// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;


public interface IWFServerProtocol
{

    public static final int LOGON = 1;
    public static final int ACKLOGON = 2;
    public static final int LOGONFAILURE = 3;
    public static final int LOGOFF = 4;
    public static final int ACKLOGOFF = 5;
    public static final int READYFORINV = 6;
    public static final int APPINVCMD = 7;
    public static final int INVFAILURE = 8;
    public static final int APPDATA = 9;
    public static final int EXCDATA = 10;
    public static final int USERDATA = 11;
    public static final int ENDOFAPP = 12;
    public static final int JOBABORTED = 13;
    public static final int STARTDEBUG = 14;
    public static final int ENDDEBUG = 15;
    public static final int DEBUGSTARTED = 16;
    public static final int DEBUGENDED = 17;
    public static final int END_OF_SESSION = 18;
}
