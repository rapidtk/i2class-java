// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.controller;

import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;
import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.controller:
//            DataDefinitionBeanHandler

public class BeanDefXMLReader
{

    public BeanDefXMLReader()
        throws SAXException
    {
        _myClassLoader = null;
        parser = null;
        _myClassLoader = getClass().getClassLoader();
        if(null == _myClassLoader)
            _myClassLoader = ClassLoader.getSystemClassLoader();
        try
        {
            parser = XMLReaderFactory.createXMLReader();
        }
        catch(SAXException saxexception)
        {
            throw saxexception;
        }
    }

    public IRecordDataDefinition getBeanDef(String s)
    {
        IRecordDataDefinition irecorddatadefinition = null;
        try
        {
            String s1 = s.replace('.', '/') + ".xml";
            InputStream inputstream = _myClassLoader.getResourceAsStream(s1);
            if(null != inputstream)
                irecorddatadefinition = getBeanDef(inputstream);
            else
                irecorddatadefinition = null;
        }
        catch(Throwable throwable)
        {
            System.err.println("Error in BeanDefXMLReader.getBeanDef(String) : \n" + throwable);
            throwable.printStackTrace(System.err);
            irecorddatadefinition = null;
        }
        return irecorddatadefinition;
    }

    public IRecordDataDefinition getBeanDef(InputStream inputstream)
    {
        IRecordDataDefinition irecorddatadefinition = null;
        try
        {
            DataDefinitionBeanHandler datadefinitionbeanhandler = new DataDefinitionBeanHandler();
            parser.setContentHandler(datadefinitionbeanhandler);
            InputSource inputsource = new InputSource(inputstream);
            parser.parse(inputsource);
            irecorddatadefinition = datadefinitionbeanhandler.getDataDefinition();
            inputstream.close();
        }
        catch(Throwable throwable)
        {
            System.err.println("Error in BeanDefXMLReader.getBeanDef(InputStream) : \n" + throwable);
            throwable.printStackTrace(System.err);
            irecorddatadefinition = null;
        }
        return irecorddatadefinition;
    }

    public static void main(String args[])
    {
        String args1[] = {
            "D:\\Program Files\\IBM\\wsa-base-20021125_2118-WB202-AD-V50D-GA\\eclipse\\runtime-workspace\\tx\\Java Source\\WFV5TEST\\QDDSSRC\\YWSSSSG2\\CTL003.xml", "D:\\Program Files\\IBM\\wsa-base-20021125_2118-WB202-AD-V50D-GA\\eclipse\\runtime-workspace\\tx\\Java Source\\HOCKINGS\\RPGAPP\\SLTCUSTD\\CUSTCTL.xml", "D:\\Program Files\\IBM\\wsa-base-20021125_2118-WB202-AD-V50D-GA\\eclipse\\runtime-workspace\\tx\\Java Source\\WFV5TEST\\QDDSSRC\\_zWSSSSG2\\HEADREC.xml", "D:\\Program Files\\IBM\\wsa-base-20021125_2118-WB202-AD-V50D-GA\\eclipse\\runtime-workspace\\tx\\Web Content\\WEB-INF\\classes\\V5MOREKWDS\\QDDSSRC\\_zWSWWQW2\\BLKRCD11.xml", "D:\\Program Files\\IBM\\wsa-base-20021125_2118-WB202-AD-V50D-GA\\eclipse\\runtime-workspace\\tx\\Web Content\\WEB-INF\\classes\\V5MOREKWDS\\QDDSSRC\\_zWSWWQW2\\CTLRCD11.xml", "D:\\Program Files\\IBM\\wsa-base-20021125_2118-WB202-AD-V50D-GA\\eclipse\\runtime-workspace\\tx\\Web Content\\WEB-INF\\classes\\V5MOREKWDS\\QDDSSRC\\_zWSWWQW2\\HEADREC.xml"
        };
        for(int i = 0; i < args1.length; i++)
        {
            File file = new File(args1[i]);
            System.out.println("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV");
            System.out.println(i + ": " + args1[i]);
            System.out.println("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV");
            try
            {
                FileInputStream fileinputstream = new FileInputStream(file);
                BeanDefXMLReader beandefxmlreader = new BeanDefXMLReader();
                beandefxmlreader.getBeanDef(fileinputstream);
            }
            catch(Throwable throwable)
            {
                System.out.println(throwable);
            }
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        }

    }

    private ClassLoader _myClassLoader;
    private XMLReader parser;
}
