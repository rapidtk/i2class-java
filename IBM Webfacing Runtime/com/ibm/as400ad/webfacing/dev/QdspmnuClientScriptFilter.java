// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.dev;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;

// Referenced classes of package com.ibm.as400ad.webfacing.dev:
//            FilterCode

public class QdspmnuClientScriptFilter extends FilterCode
{

    public QdspmnuClientScriptFilter()
    {
        _shiftReplaced = false;
    }

    public String filterLine(String s)
    {
        if(_shiftReplaced)
            return s;
        int i = s.indexOf("datalength");
        if(i >= 0)
        {
            int j = s.indexOf("});");
            if(j >= 0)
                return WebfacingConstants.replaceSubstring(s, "});", ",shift:\\\"Y\\\"});", j);
        }
        return s;
    }

    private boolean _shiftReplaced;
}
