// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.dom.ExportSettings;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.view.CommandKeyLabelList;
import com.ibm.as400ad.webfacing.runtime.view.def.CommandKeyLabel;
import java.io.*;
import java.util.*;

public class KeyLabelsInPropertyFile
{

    public KeyLabelsInPropertyFile()
    {
        _labelsInFile = new CommandKeyLabelList();
    }

    public void initKeyLabelListInPropertyFile()
    {
        PropertyResourceBundle propertyresourcebundle = null;
        try
        {
            File file = new File(_filename);
            String s = _filename.substring(0, _filename.lastIndexOf(File.separatorChar));
            File file1 = new File(s);
            if(!file1.exists())
                file1.mkdirs();
            if(!file.exists())
                file.createNewFile();
            propertyresourcebundle = new PropertyResourceBundle(new FileInputStream(_filename));
        }
        catch(Throwable throwable)
        {
            System.out.println("Can not open the property file in KeyLabelsInPropertyFile.initKeyLabeListInFile(Hashtable)");
        }
        if(propertyresourcebundle != null)
        {
            for(Enumeration enumeration = propertyresourcebundle.getKeys(); enumeration.hasMoreElements();)
            {
                String s1 = (String)enumeration.nextElement();
                String s2 = (String)propertyresourcebundle.getObject(s1);
                if(s1 != null && s2 != null)
                {
                    s1 = WebfacingConstants.replaceSubstring(s1, "_", " ");
                    if(Character.isDigit(s2.charAt(1)) && s2.charAt(0) == 'F')
                        s2 = s2.substring(1);
                    updateKeyLabelListInPropertyFile(new CommandKeyLabel(s2, s1, -1));
                }
            }

        }
    }

    public void updateKeyLabelListInPropertyFile(CommandKeyLabel commandkeylabel)
    {
        _labelsInFile.add(commandkeylabel);
    }

    public void writeKeyLabelListIntoPropertyFile()
    {
        try
        {
            BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(_filename));
            for(Iterator iterator = _labelsInFile.getLabels(); iterator.hasNext();)
            {
                CommandKeyLabel commandkeylabel = (CommandKeyLabel)iterator.next();
                String s = commandkeylabel.getKeyName();
                String s1 = commandkeylabel.getKeyLabel();
                if(s1 != null)
                {
                    s1 = s1.toUpperCase();
                    s1 = WebfacingConstants.replaceSubstring(s1, " ", "_");
                    s1 = WebfacingConstants.replaceSubstring(s1, "=", "\\=");
                    if(Character.isDigit(s.charAt(0)))
                        s = "F" + s;
                    bufferedwriter.write(s1 + "=" + s);
                    bufferedwriter.newLine();
                    bufferedwriter.flush();
                }
            }

            bufferedwriter.close();
        }
        catch(Throwable throwable)
        {
            System.out.println("Can not write the property file in KeyLabelsInPropertyFile.writeKeyLabelListIntoPropertyFile()");
        }
    }

    private CommandKeyLabelList _labelsInFile;
    private static String _filename = ExportSettings.getExportSettings().getCmdKeyNamesFile();

}
