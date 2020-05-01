// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import com.ibm.as400ad.webfacing.runtime.view.def.CHKMSGIDDefinition;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.host:
//            HJIMessageRequest

public class CHKMSGIDRequest extends HJIMessageRequest
{

    public CHKMSGIDRequest(CHKMSGIDDefinition chkmsgiddefinition, String s)
    {
        super(chkmsgiddefinition.getMsgId(), chkmsgiddefinition.getMsgFile(), chkmsgiddefinition.getLibraryName(), s);
        _chkmsgid = null;
        _chkmsgid = chkmsgiddefinition;
    }

    public void replyReceived(boolean flag)
    {
        if(!flag)
            _chkmsgid.setCHKMSG(getMessageText());
    }

    private CHKMSGIDDefinition _chkmsgid;
}
