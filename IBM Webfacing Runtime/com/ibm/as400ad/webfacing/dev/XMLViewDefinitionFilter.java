// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.dev;

import java.io.PrintStream;
import java.util.Hashtable;
import org.w3c.dom.*;

// Referenced classes of package com.ibm.as400ad.webfacing.dev:
//            XMLDefinitionFilter

public class XMLViewDefinitionFilter extends XMLDefinitionFilter
{

    public XMLViewDefinitionFilter(Document document)
    {
        super(document);
        _mappedParmsSetup = false;
        _mappedParms = null;
    }

    public Document filterKeys()
    {
        setMappedParms();
        NodeList nodelist = super.document.getElementsByTagName("view");
        Element element = (Element)nodelist.item(0);
        if(null != element)
        {
            System.out.println(" -- found the view node");
            NodeList nodelist1 = element.getElementsByTagName("aidkeys");
            element = (Element)nodelist1.item(0);
            if(null != element)
            {
                for(boolean flag = true; flag;)
                {
                    flag = false;
                    NodeList nodelist2 = element.getElementsByTagName("aidkey");
                    int i = nodelist2.getLength();
                    for(int j = i - 1; j >= 0; j--)
                    {
                        Element element1 = (Element)nodelist2.item(j);
                        if(null != element1)
                        {
                            String s = element1.getAttribute("key");
                            String s1 = element1.getAttribute("label");
                            if(null != s1)
                            {
                                String s2 = (String)_mappedParms.get(s1);
                                if(null != s2)
                                    s1 = s2;
                                element1.setAttribute("label", s1);
                            }
                            if(null != getMappedParm(s))
                            {
                                element1.setAttribute("tran", "true");
                            } else
                            {
                                System.out.println("removing key " + s);
                                element.removeChild(element1);
                                flag = true;
                            }
                        }
                    }

                }

            }
        }
        return super.document;
    }

    private String getMappedParm(String s)
    {
        if(s.indexOf("CA0") >= 0 || s.indexOf("CA1") >= 0)
            return s;
        else
            return (String)_mappedParms.get(s.toUpperCase());
    }

    private void setMappedParms()
    {
        if(!_mappedParmsSetup)
        {
            _mappedParmsSetup = true;
            _mappedParms = new Hashtable();
            _mappedParms.put("CF03", "Exit");
            _mappedParms.put("CF12", "Cancel");
            _mappedParms.put("CA03", "Exit");
            _mappedParms.put("CA12", "Cancel");
            _mappedParms.put("CANCEL", "Cancel");
            _mappedParms.put("EXIT", "Exit");
            _mappedParms.put("HELP", "Help");
            if(!"qdspmnu".equals(getPackageFile()) || "qdspmnu".equals(getPackageFile()) && (getRecordName() == null || getRecordName().indexOf("HELPFMT") >= 0))
            {
                _mappedParms.put("CLEAR", "CLEAR");
                _mappedParms.put("PAGEUP", "PAGEUP");
                _mappedParms.put("PAGEDOWN", "PAGEDOWN");
            }
        }
    }

    private boolean _mappedParmsSetup;
    private Hashtable _mappedParms;
}
