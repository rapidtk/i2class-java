// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.dhtml;

import com.ibm.as400ad.code400.dom.RecordNode;
import com.ibm.as400ad.webfacing.convert.model.SubfileControlRecordLayout;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.dhtml:
//            ClientScriptJSPVisitor, ClientScriptSourceCodeCollection

public class AbstractSubfileControlClientScriptJSPVisitor extends ClientScriptJSPVisitor
{

    public AbstractSubfileControlClientScriptJSPVisitor()
    {
        _isProcessSubfiles = false;
    }

    public AbstractSubfileControlClientScriptJSPVisitor(SubfileControlRecordLayout subfilecontrolrecordlayout, ClientScriptSourceCodeCollection clientscriptsourcecodecollection)
    {
        super(subfilecontrolrecordlayout, clientscriptsourcecodecollection);
        _isProcessSubfiles = false;
    }

    protected boolean isProcessingSubfiles()
    {
        return _isProcessSubfiles;
    }

    protected boolean isSubfileFoldable()
    {
        return super._rn.isSubfileFoldable();
    }

    public void processBeginOfSubfiles()
    {
        _isProcessSubfiles = true;
    }

    public void processEndOfSubfiles()
    {
        _isProcessSubfiles = false;
    }

    private boolean _isProcessSubfiles;
}
