// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.designer.io;

import java.io.*;
import java.util.Enumeration;

// Referenced classes of package com.ibm.as400ad.code400.designer.io:
//            Fileio, FileOutput, OutputCollection

public class OutputFile extends Fileio
    implements FileOutput
{

    public OutputFile(String s)
        throws IOException
    {
        super(s);
        _bw = null;
        _encoding = null;
        openForAppend(false);
    }

    public OutputFile(String s, String s1)
        throws IOException
    {
        super(s);
        _bw = null;
        _encoding = null;
        _encoding = s1;
        openForAppend(false);
    }

    public OutputFile(String s, boolean flag)
        throws IOException
    {
        super(s);
        _bw = null;
        _encoding = null;
        openForAppend(flag);
    }

    public OutputFile(String s, boolean flag, String s1)
        throws IOException
    {
        super(s);
        _bw = null;
        _encoding = null;
        _encoding = s1;
        openForAppend(flag);
    }

    public boolean checkError()
    {
        return false;
    }

    public void close()
    {
        if(null != _bw)
            try
            {
                _bw.close();
                _bw = null;
            }
            catch(IOException ioexception)
            {
                System.out.println("Error when closing the output stream");
            }
    }

    protected void finalize()
        throws Throwable
    {
        close();
        super.finalize();
    }

    public boolean flush()
    {
        boolean flag = false;
        if(null != _bw)
            try
            {
                _bw.flush();
            }
            catch(IOException ioexception)
            {
                flag = true;
            }
        return flag;
    }

    public boolean newLine()
    {
        return print(System.getProperty("line.separator"));
    }

    private final void openForAppend(boolean flag)
        throws IOException
    {
        if(null == _bw)
            try
            {
                FileOutputStream fileoutputstream = new FileOutputStream(getFileName(), flag);
                if(_encoding != null)
                    _bw = new BufferedWriter(new OutputStreamWriter(fileoutputstream, _encoding));
                else
                    _bw = new BufferedWriter(new OutputStreamWriter(fileoutputstream));
            }
            catch(IOException ioexception)
            {
                System.out.println(" : FAILED");
                System.out.println(ioexception);
                throw ioexception;
            }
    }

    public boolean print(String s)
    {
        boolean flag = true;
        if(null == _bw)
            try
            {
                openForAppend(true);
            }
            catch(IOException ioexception) { }
        if(null != _bw)
            try
            {
                _bw.write(s);
                flag = false;
            }
            catch(IOException ioexception1)
            {
                flag = true;
            }
        return flag;
    }

    public boolean println(String s)
    {
        boolean flag = true;
        if(null == _bw)
            try
            {
                openForAppend(true);
            }
            catch(IOException ioexception) { }
        if(null != _bw)
            try
            {
                _bw.write(s);
                _bw.newLine();
                flag = false;
            }
            catch(IOException ioexception1)
            {
                flag = true;
            }
        return flag;
    }

    public boolean printOutputCollection(OutputCollection outputcollection)
    {
        boolean flag = false;
        if(null != outputcollection)
        {
            for(Enumeration enumeration = outputcollection.elements(); !flag && enumeration.hasMoreElements();)
            {
                String s = enumeration.nextElement().toString().trim();
                if(!s.equals(""))
                    flag = println(s);
            }

        }
        return flag;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000");
    private BufferedWriter _bw;
    private String _encoding;

}
