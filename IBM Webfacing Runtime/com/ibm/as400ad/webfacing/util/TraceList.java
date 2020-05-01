// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.util;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

// Referenced classes of package com.ibm.as400ad.webfacing.util:
//            HashedList, TraceLogger

public class TraceList extends Thread
{

    public TraceList(String s)
    {
        _hashedList = null;
        _hashedList = new HashedList();
        _pathName = s;
        File file = new File(s);
        if(file.isDirectory())
        {
            String as[] = list(file, _hashedList.getFileSorter());
            for(int i = 0; i < as.length; i++)
                addToList(as[i]);

        }
    }

    private void addToList(String s)
    {
        if(s.length() > 4 && s.substring(s.length() - 4, s.length()).equals(".log"))
        {
            String s1 = s.substring(0, s.length() - 4);
            _hashedList.add(s1);
            if(size() > getMaxLogLevel())
                removeSession();
        }
    }

    public void appendSession(String s)
        throws FileNotFoundException, IOException
    {
        if(!_hashedList.hasElement(s))
            _hashedList.add(s);
        if(size() > getMaxLogLevel())
        {
            String s1 = removeSession();
            String s2 = _pathName + System.getProperty("file.separator");
            File file = new File(s2 + s1 + ".log");
            if(file.exists())
                file.delete();
        }
    }

    private int getMaxLogLevel()
    {
        return TraceLogger.getMaxLogLevel();
    }

    private String[] list(File file, Comparator comparator)
    {
        File afile[] = file.listFiles();
        if(afile == null)
            return null;
        Arrays.sort(afile, comparator);
        String as[] = new String[afile.length];
        for(int i = 0; i < afile.length; i++)
            as[i] = afile[i].getName();

        return as;
    }

    private String removeSession()
    {
        return (String)_hashedList.removeFirst();
    }

    private int size()
    {
        return _hashedList.size();
    }

    private HashedList _hashedList;
    private static String _pathName = null;

}
