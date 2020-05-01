// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.util;

import java.io.*;

// Referenced classes of package com.ibm.as400ad.webfacing.util:
//            HashedList, TraceLogger

public class TraceListProperties extends Thread
{

    public TraceListProperties()
    {
        hl = null;
        hl = new HashedList();
        try
        {
            BufferedReader bufferedreader = new BufferedReader(new FileReader("traceList.properties"));
            try
            {
                for(String s = bufferedreader.readLine(); s != null; s = bufferedreader.readLine())
                {
                    addFromList(s);
                    _filesize++;
                }

            }
            catch(IOException ioexception)
            {
                hl = null;
                hl = new HashedList();
                _filesize = 0;
                File file = new File("traceList.properties");
                file.delete();
            }
        }
        catch(FileNotFoundException filenotfoundexception) { }
    }

    private void addFromList(String s)
    {
        hl.add(s);
        if(size() > getMaxLogLevel())
            removeSession();
    }

    public void appendSession(String s)
        throws FileNotFoundException, IOException
    {
        if(!hl.hasElement(s))
        {
            hl.add(s);
            _filesize++;
            FileOutputStream fileoutputstream = new FileOutputStream("traceList.properties", true);
            PrintWriter printwriter = new PrintWriter(fileoutputstream);
            printwriter.println(s);
            printwriter.close();
        }
        if(size() > getMaxLogLevel())
            removeSession();
        if(_filesize > 2 * getMaxLogLevel())
        {
            File file = new File("traceList.properties");
            if(file.exists())
            {
                file.delete();
                file.createNewFile();
                FileOutputStream fileoutputstream1 = new FileOutputStream("traceList.properties");
                PrintWriter printwriter1 = new PrintWriter(fileoutputstream1);
                for(int i = 0; i < size(); i++)
                    printwriter1.println((String)hl.get(i));

                _filesize = size();
                printwriter1.close();
            }
        }
    }

    public static TraceListProperties getInstance()
    {
        _filesize = 0;
        if(singleton == null)
            singleton = new TraceListProperties();
        FileOutputStream fileoutputstream;
        try
        {
            fileoutputstream = new FileOutputStream("traceList.properties", true);
        }
        catch(FileNotFoundException filenotfoundexception)
        {
            File file = new File("traceList.properties");
            try
            {
                file.createNewFile();
            }
            catch(IOException ioexception) { }
        }
        return singleton;
    }

    public static TraceListProperties getInstance(String s)
    {
        String s1 = s;
        _filesize = 0;
        if(singleton == null)
            singleton = new TraceListProperties();
        FileOutputStream fileoutputstream;
        try
        {
            fileoutputstream = new FileOutputStream(s1 + "traceList.properties", true);
        }
        catch(FileNotFoundException filenotfoundexception)
        {
            File file = new File(s1 + "traceList.properties");
            try
            {
                file.createNewFile();
            }
            catch(IOException ioexception) { }
        }
        return singleton;
    }

    private int getMaxLogLevel()
    {
        return TraceLogger.getMaxLogLevel();
    }

    public boolean isActive(Object obj)
    {
        return hl.hasElement(obj);
    }

    private void removeSession()
    {
        hl.removeFirst();
    }

    private int size()
    {
        return hl.size();
    }

    private static TraceListProperties singleton = null;
    private HashedList hl;
    private static final String ListFileName = "traceList.properties";
    private static int _filesize;
    private static final int _delsize = 2;

}
