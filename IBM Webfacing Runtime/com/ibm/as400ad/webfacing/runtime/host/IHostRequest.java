// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import java.io.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.host:
//            IWFInputBuffer

public interface IHostRequest
    extends Serializable
{

    public abstract InputStream request(IWFInputBuffer iwfinputbuffer)
        throws IOException, WebfacingInternalException;

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
}
