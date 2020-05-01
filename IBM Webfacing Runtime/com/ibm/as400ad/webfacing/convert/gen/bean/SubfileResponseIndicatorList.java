// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.dom.AnyNodeWithKeywords;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            ResponseIndicatorList, JavaSourceCodeCollection

public class SubfileResponseIndicatorList extends ResponseIndicatorList
{

    public SubfileResponseIndicatorList()
    {
    }

    protected static JavaSourceCodeCollection generateFeedbackResponseIndicator(AnyNodeWithKeywords anynodewithkeywords)
    {
        SubfileResponseIndicatorList subfileresponseindicatorlist = new SubfileResponseIndicatorList();
        subfileresponseindicatorlist.populateFrom(anynodewithkeywords);
        return subfileresponseindicatorlist.generateFeedbackResponseIndicator();
    }

    protected String getAddToDefCall()
    {
        return "addForSubfile";
    }
}
