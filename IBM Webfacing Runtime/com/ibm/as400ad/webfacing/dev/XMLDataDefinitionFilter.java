// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.dev;

import java.io.PrintStream;
import org.w3c.dom.*;

// Referenced classes of package com.ibm.as400ad.webfacing.dev:
//            XMLDefinitionFilter

public class XMLDataDefinitionFilter extends XMLDefinitionFilter
{

    public XMLDataDefinitionFilter(Document document)
    {
        super(document);
    }

    public Document filter()
    {
        NodeList nodelist = super.document.getElementsByTagName("data");
        Element element = (Element)nodelist.item(0);
        if(element != null)
        {
            System.out.println(" -- found the data node");
            String s = element.getAttribute("filemembertype");
            System.out.println(" -- mbrtype is: " + s);
            if(null != s)
            {
                element.removeAttribute("filemembertype");
                System.out.println(" -- removing mbrtype");
            }
            element.setAttribute("filemembertype", "MNUDDS");
            System.out.println(" -- new mbrtype set");
        }
        return super.document;
    }
}
