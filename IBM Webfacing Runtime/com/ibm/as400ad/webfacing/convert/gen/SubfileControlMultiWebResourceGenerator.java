// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen;

import com.ibm.as400ad.webfacing.convert.IConversionFactory;
import com.ibm.as400ad.webfacing.convert.IRecordLayout;
import com.ibm.as400ad.webfacing.convert.model.SubfileControlRecordLayout;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen:
//            MultiWebResourceGenerator

public class SubfileControlMultiWebResourceGenerator extends MultiWebResourceGenerator
{

    public SubfileControlMultiWebResourceGenerator()
    {
    }

    public SubfileControlMultiWebResourceGenerator(IConversionFactory iconversionfactory, IRecordLayout irecordlayout)
    {
        super(iconversionfactory, irecordlayout);
        addGenerator(iconversionfactory.getXMLBeanGenerator(((SubfileControlRecordLayout)irecordlayout).getSubfileRecordLayout()));
    }
}
