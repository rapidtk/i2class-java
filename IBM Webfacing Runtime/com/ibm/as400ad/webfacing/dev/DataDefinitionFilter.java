// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.dev;


// Referenced classes of package com.ibm.as400ad.webfacing.dev:
//            DefinitionFilter

public class DataDefinitionFilter extends DefinitionFilter
{

    public DataDefinitionFilter()
    {
    }

    public String filterLine(String s)
    {
        s = super.filterLine(s);
        if(s.indexOf("super(") >= 0)
        {
            int i = s.lastIndexOf(";");
            if(i > 0)
            {
                StringBuffer stringbuffer = new StringBuffer(s);
                stringbuffer.append("\n   setFileMemberType(\"MNUDDS\");");
                return stringbuffer.toString();
            }
        }
        return s;
    }
}
