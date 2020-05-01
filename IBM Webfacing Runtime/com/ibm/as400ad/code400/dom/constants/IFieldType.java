// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.constants;


// Referenced classes of package com.ibm.as400ad.code400.dom.constants:
//            IType

public interface IFieldType
    extends IType
{

    public abstract boolean isFieldOfType(int i);

    public static final String copyRight = new String("(C) Copyright IBM Corporation 2000-2003 All rights reserved.");
    public static final int FT_UNKNOWN = 20;
    public static final int FT_ALPHA = 8;
    public static final int FT_NUMERIC = 9;
    public static final int FT_FLOATS = 10;
    public static final int FT_FLOATD = 11;
    public static final int FT_PACKED = 12;
    public static final int FT_BINARY = 13;
    public static final int FT_HEX = 14;
    public static final int FT_DATE_FIELD = 15;
    public static final int FT_TIME_FIELD = 16;
    public static final int FT_TIMESTAMP_FIELD = 17;
    public static final int FT_PUREDBCS = 18;
    public static final int FT_DBCS = 19;
    public static final int FT_TEXT_CONSTANT = 0;
    public static final int FT_TXTBLK = 1;
    public static final int FT_DATE_CONSTANT = 2;
    public static final int FT_TIME_CONSTANT = 3;
    public static final int FT_USER_ID_CONSTANT = 4;
    public static final int FT_SYSTEM_ID_CONSTANT = 5;
    public static final int FT_MESSAGE_CONSTANT = 6;
    public static final int FT_PAGE_NUMBER = 7;

}
