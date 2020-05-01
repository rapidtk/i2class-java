// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.constants;


public interface IHyperlinkType
{

    public static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2001");
    public static final int UNKNOWN = 0;
    public static final int URL = 1;
    public static final int SUBMIT_FLD_AND_VALUE = 2;
    public static final int STATIC_URL = 3;
    public static final int CURSOR_AND_SUBMIT_KEY = 4;
    public static final int POSITION_CURSOR = 5;
    public static final int SUBMIT_KEY = 6;
    public static final int SUBMIT_JS_CALL = 7;
    public static final int CURSOR_AND_SUBMIT_JS_CALL = 8;
    public static final int JAVASCRIPT_URL = 9;
    public static final String WS_GRAPHIC_NAMES[] = {
        "UNKNOWN", "URL", "SUBMIT_FLD_AND_VALUE", "STATIC_URL", "CURSOR_AND_SUBMIT_KEY", "POSITION_CURSOR", "SUBMIT_KEY", "SUBMIT_JS_CALL", "CURSOR_AND_SUBMIT_JS_CALL", "JAVASCRIPT_URL"
    };

}
