// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.runtime.core.ElementContainer;
import com.ibm.as400ad.webfacing.runtime.view.CursorReturnData;
import com.ibm.as400ad.webfacing.runtime.view.RecordFeedbackBean;

public abstract class RTNCSRLOCDefinition extends ElementContainer
    implements ENUM_KeywordIdentifiers
{

    protected RTNCSRLOCDefinition()
    {
        super((new Long(174L)).toString() + _rtncsrlocIndex);
        _rtncsrlocIndex++;
    }

    public abstract void setCursor(RecordFeedbackBean recordfeedbackbean, CursorReturnData cursorreturndata);

    private static int _rtncsrlocIndex = 0;

}
