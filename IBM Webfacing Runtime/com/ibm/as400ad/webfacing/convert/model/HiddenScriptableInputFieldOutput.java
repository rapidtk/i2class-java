// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.dom.FieldNode;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.ClientScriptSourceCodeCollection;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            HiddenInputFieldOutput, InputFieldOutput

public class HiddenScriptableInputFieldOutput extends HiddenInputFieldOutput
{

    public HiddenScriptableInputFieldOutput(FieldNode fieldnode)
    {
        super(fieldnode);
    }

    public ClientScriptSourceCodeCollection getClientScript()
    {
        ClientScriptSourceCodeCollection clientscriptsourcecodecollection = super.getClientScript();
        String s = clientscriptsourcecodecollection.removeElementWithPrefix("cf(");
        int i = s.lastIndexOf(");");
        String s1 = s;
        if(i != -1)
            s1 = s.substring(0, i) + ",{check:\"LC\"});";
        clientscriptsourcecodecollection.addElement(s1);
        return clientscriptsourcecodecollection;
    }

    public boolean isScriptableInvisibleField()
    {
        return true;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000");

}
