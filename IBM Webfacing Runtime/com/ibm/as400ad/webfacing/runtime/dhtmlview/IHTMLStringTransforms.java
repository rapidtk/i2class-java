// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.dhtmlview;


public interface IHTMLStringTransforms
{

    public static final String copyRight = new String(" (C) Copyright IBM Corporation 1999-2003, all rights reserved");
    public static final int DO_NOT_TRANSFORM = 0;
    public static final int QUOTED_STRING_TRANSFORM = 1;
    public static final int UNQUOTED_STRING_TRANSFORM = 2;
    public static final int TRIMMED_QUOTED_STRING_TRANSFORM = 3;
    public static final int TRIMMED_JAVA_STRING_TRANSFORM = 4;
    public static final int HYPERLINK_TRANSFORM = 5;

}
