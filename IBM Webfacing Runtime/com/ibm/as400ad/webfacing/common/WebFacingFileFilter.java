// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.common;

import java.io.File;
import java.io.FilenameFilter;

public class WebFacingFileFilter
    implements FilenameFilter
{

    public WebFacingFileFilter(String s)
    {
        fileExtension = s;
    }

    public boolean accept(File file, String s)
    {
        boolean flag = false;
        String s1 = getSuffix(s);
        if(s1 != null)
            flag = s1.equalsIgnoreCase(fileExtension);
        return flag;
    }

    private String getSuffix(String s)
    {
        String s1 = null;
        int i = s.lastIndexOf('.');
        if(i > 0 && i < s.length() - 1)
            s1 = s.substring(i + 1);
        return s1;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999=2003, all rights reserved.");
    String fileExtension;

}
