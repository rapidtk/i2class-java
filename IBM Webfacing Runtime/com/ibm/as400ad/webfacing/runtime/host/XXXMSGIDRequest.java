// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import com.ibm.as400ad.webfacing.runtime.view.IXXXMSGIDMessageDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.XXXMSGIDDefinition;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.host:
//            HJIMessageRequest

public class XXXMSGIDRequest extends HJIMessageRequest
{

    public XXXMSGIDRequest(IXXXMSGIDMessageDefinition ixxxmsgidmessagedefinition, String s)
    {
        super(ixxxmsgidmessagedefinition.getMSGID().getMsgId(), ixxxmsgidmessagedefinition.getMSGID().getMsgFile(), ixxxmsgidmessagedefinition.getMSGID().getLibraryName(), s);
        _msgDef = null;
        _msgDef = ixxxmsgidmessagedefinition;
    }

    public void replyReceived(boolean flag)
    {
        if(!flag)
            _msgDef.setMessageText(getMessageText());
        else
            _msgDef.setMessageText(getMessageId().substring(3) + "??");
    }

    private IXXXMSGIDMessageDefinition _msgDef;
}
