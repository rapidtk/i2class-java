// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.webfacing.runtime.host.HostJobInfo;
import com.ibm.as400ad.webfacing.runtime.host.XXXMSGIDRequest;
import com.ibm.as400ad.webfacing.runtime.model.IQueryFieldData;
import com.ibm.as400ad.webfacing.runtime.model.def.ConditionedKeyword;
import com.ibm.as400ad.webfacing.runtime.view.IXXXMSGIDMessageDefinition;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            ERRMSGMessageDefinition, MSGMessageDefinition, XXXMSGIDDefinition

public class ERRMSGIDMessageDefinition extends ERRMSGMessageDefinition
    implements IXXXMSGIDMessageDefinition
{

    public ERRMSGIDMessageDefinition(String s, XXXMSGIDDefinition xxxmsgiddefinition)
    {
        super(s, null);
        _errMsgId = null;
        _errMsgId = xxxmsgiddefinition;
        setIndicatorExpression(xxxmsgiddefinition.getIndicatorExpression());
    }

    public XXXMSGIDDefinition getMSGID()
    {
        return _errMsgId;
    }

    public void resolveMessageText(HostJobInfo hostjobinfo, IQueryFieldData iqueryfielddata)
    {
        hostjobinfo.addRequest(new XXXMSGIDRequest(this, getMSGID().getMsgData(iqueryfielddata)));
    }

    private XXXMSGIDDefinition _errMsgId;
}
