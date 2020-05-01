// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.dds.dom;

import java.io.IOException;
import java.io.PrintWriter;

// Referenced classes of package com.ibm.etools.dds.dom:
//            IDdsFileLevel

public interface IDdsModel
{

    public abstract void saveAsXML(String s, PrintWriter printwriter)
        throws IOException;

    public abstract boolean isDspf();

    public abstract boolean isPrtf();

    public abstract boolean isPf();

    public abstract boolean isLf();

    public abstract boolean isDeviceFile();

    public abstract boolean isDatabaseFile();

    public abstract String getVariantCharacters();

    public abstract IDdsFileLevel getFileLevel();
}
