// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.convert.gen.tag;

import com.ibm.etools.iseries.webfacing.convert.external.*;
import java.util.Iterator;

// Referenced classes of package com.ibm.etools.iseries.webfacing.convert.gen.tag:
//            WSSubTagInput

public class WSTagInput extends WSSubTagInput
    implements IWSTagInput
{

    public WSTagInput(IFieldTagInput ifieldtaginput, IRawWebSetting irawwebsetting)
    {
        super(ifieldtaginput, irawwebsetting);
    }

    public Iterator getSubWebSettings()
    {
        return super._fTagInput.getSubWebSettings();
    }

    public String getSubTag(IRawWebSetting irawwebsetting)
    {
        return super._fTagInput.getSubTag(irawwebsetting);
    }

    static final String copyRight = new String("(c) Copyright IBM Corporation 2002-2003 All rights reserved.");

}
