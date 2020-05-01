// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.designer.io;

import java.io.File;

public class Fileio
{

    public Fileio(String s)
    {
        fn = null;
        fn = s;
    }

    protected void finalize()
        throws Throwable
    {
        super.finalize();
    }

    public File getFile()
    {
        return new File(fn);
    }

    public String getFileName()
    {
        return fn;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000");
    private String fn;

}
