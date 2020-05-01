// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.webfacing.runtime.controller.IDSPFObject;
import com.ibm.as400ad.webfacing.runtime.controller.RecordBeanFactory;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.model.IIndicatorArea;
import com.ibm.as400ad.webfacing.runtime.model.RecordDataBean;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            RecordViewBean

public interface IRecordOperations
{

    public abstract void close(IDSPFObject idspfobject);

    public abstract void open(IDSPFObject idspfobject);

    public abstract boolean read(IDSPFObject idspfobject, String s, IIndicatorArea iindicatorarea)
        throws WFApplicationRuntimeError, WebfacingInternalException, WebfacingLevelCheckException;

    public abstract void setJobCCSID(String s);

    public abstract RecordViewBean write(IDSPFObject idspfobject, RecordDataBean recorddatabean, boolean flag, RecordBeanFactory recordbeanfactory, boolean flag1)
        throws WebfacingInternalException, WebfacingLevelCheckException, WFApplicationRuntimeError;

    public abstract void setIsInBiDiMode(boolean flag);

    public static final String copyRight = "(C) Copyright IBM Corporation 1999-2003 all rights reserved";
}
