// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert;

import com.ibm.as400ad.code400.dom.FileNode;
import com.ibm.as400ad.code400.dom.RecordNodeEnumeration;

// Referenced classes of package com.ibm.as400ad.webfacing.convert:
//            IConversionFactory

public interface IWebFaceConverter
{

    public abstract void convert(FileNode filenode, IConversionFactory iconversionfactory);

    public abstract void convert(RecordNodeEnumeration recordnodeenumeration, IConversionFactory iconversionfactory);
}
