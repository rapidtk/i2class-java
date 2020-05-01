// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            CursorPosition, LocationOnDevice, IBuildRecordViewBean, RecordViewBean

public interface IScreenBuilder
{

    public abstract Iterator getActiveKeys();

    public abstract Iterator getActiveKeysWithoutEnter();

    public abstract String[] getRecordsWithPageUPDNKey();

    public abstract int getMaxColumn();

    public abstract int getMaxRow();

    public abstract Iterator[] getMessagesAndIDs();

    public abstract ArrayList getRecordLayersOnDevice();

    public abstract boolean isWide();

    public abstract String getJobCCSID();

    public abstract boolean isHLPRTNActive();

    public abstract boolean isInBiDiMode();

    public abstract boolean isHelpEnabled();

    public abstract boolean hasHelpSpecifications();

    public abstract void setCursor(CursorPosition cursorposition);

    public abstract LocationOnDevice getLocationOnDeviceAt(CursorPosition cursorposition);

    public abstract LocationOnDevice calculateLocationForCursor();

    public abstract boolean isHtmlHelp();

    public abstract IBuildRecordViewBean getRecordAt(CursorPosition cursorposition);

    public abstract String getActiveKeyName(String s);

    public abstract boolean isRecordOnTopLayer(RecordViewBean recordviewbean);

    public abstract boolean isRecordActive(String s);

    public abstract int getConceptualLayerZOrder(String s);

    public abstract RecordViewBean[] getFirstRollEnabledRecords();
}
