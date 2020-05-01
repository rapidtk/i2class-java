// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.core;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.IDSPFObject;
import com.ibm.as400ad.webfacing.runtime.controller.ILibraryFile;
import java.util.ResourceBundle;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.core:
//            WFApplicationRuntimeError

public class WFRecordReadBeforeWritten extends WFApplicationRuntimeError
{

    public WFRecordReadBeforeWritten(IDSPFObject idspfobject, String s)
    {
        super("CPF????", WebfacingConstants.replaceSubstring(WebfacingConstants.replaceSubstring(WebfacingConstants.replaceSubstring(_resmri.getString("WF0006"), "&1", s), "&2", idspfobject.getLibraryName()), "&3", idspfobject.getFileName()));
    }

    private static ResourceBundle _resmri;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
