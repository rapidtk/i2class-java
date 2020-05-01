// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.constants;


// Referenced classes of package com.ibm.as400ad.code400.dom.constants:
//            IType

public interface IRecordType
    extends IType
{

    public abstract boolean isRecordOfType(int i);

    public abstract boolean isSubFile();

    public static final String copyRight = new String("(C) Copyright IBM Corporation 1999, 2001");
    public static final int RECORD = 0;
    public static final int SFL = 1;
    public static final int SFLCTL = 2;
    public static final int SFLMSG = 3;
    public static final int USRDFN = 4;
    public static final int MNUBAR = 5;
    public static final int SFLMSGCTL = 6;
    public static final int RT_UNKNOWN = 7;

}
