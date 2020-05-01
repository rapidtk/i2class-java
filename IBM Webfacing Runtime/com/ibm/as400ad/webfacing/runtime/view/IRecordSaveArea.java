// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.webfacing.runtime.controller.IFieldValue;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingLevelCheckException;
import com.ibm.as400ad.webfacing.runtime.model.IIndicatorArea;
import com.ibm.as400ad.webfacing.runtime.model.IInputBufferSaveArea;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            ICursorBufferData, CursorPosition

public interface IRecordSaveArea
{

    public abstract String getAIDKey();

    public abstract Vector getChangedSubfileControlRecords();

    public abstract ICursorBufferData getCursorBufferData();

    public abstract IIndicatorArea getIndicatorArea();

    public abstract boolean isNoResponseIndOnAID();

    public abstract IInputBufferSaveArea mapRecordBeingRead()
        throws WebfacingInternalException;

    public abstract void postProcessRead();

    public abstract void prepareForRead()
        throws WebfacingLevelCheckException, WebfacingInternalException;

    public abstract boolean setAIDKey(String s)
        throws WebfacingInternalException, WebfacingLevelCheckException;

    public abstract void setCursor(CursorPosition cursorposition);

    public abstract void setFieldValue(IFieldValue ifieldvalue)
        throws WebfacingInternalException, WebfacingLevelCheckException;

    public abstract boolean setRelativeRecordNumber(String s, int i)
        throws WebfacingInternalException, WebfacingLevelCheckException;

    public abstract int getSFLLowestRRN();

    public abstract void setSystemMenuOption(String s);
}
