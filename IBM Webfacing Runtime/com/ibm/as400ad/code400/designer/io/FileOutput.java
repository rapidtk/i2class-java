// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.designer.io;


// Referenced classes of package com.ibm.as400ad.code400.designer.io:
//            OutputCollection

public interface FileOutput
{

    public abstract boolean checkError();

    public abstract void close();

    public abstract boolean flush();

    public abstract boolean newLine();

    public abstract boolean print(String s);

    public abstract boolean println(String s);

    public abstract boolean printOutputCollection(OutputCollection outputcollection);
}
