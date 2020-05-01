// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.webfacing.runtime.controller.IDSPFObject;
import java.io.Serializable;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            RecordViewBean

interface IRemoveRecord
    extends Serializable
{

    public abstract void remove(RecordViewBean recordviewbean);

    public abstract void removeProtectedRecord(RecordViewBean recordviewbean);

    public abstract IDSPFObject getDSPFObject();

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
}
