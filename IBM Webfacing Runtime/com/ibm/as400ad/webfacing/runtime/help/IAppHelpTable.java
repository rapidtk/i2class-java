// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;

import java.io.Serializable;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.help:
//            HelpException, HelpNotFoundException

public interface IAppHelpTable
    extends Serializable
{

    public abstract Object findNext()
        throws HelpException;

    public abstract Object findPrevious()
        throws HelpException;

    public abstract Object getOnlineHelp(Object obj)
        throws HelpException, HelpNotFoundException;

    public abstract Object getExtendedHelp()
        throws HelpException, HelpNotFoundException;
}
