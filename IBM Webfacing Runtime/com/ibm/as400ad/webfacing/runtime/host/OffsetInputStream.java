// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import java.io.*;

public class OffsetInputStream extends FilterInputStream
{

    public OffsetInputStream(DataInputStream datainputstream)
    {
        super(datainputstream);
        _bytesRead = 0;
    }

    private DataInputStream din()
    {
        return (DataInputStream)super.in;
    }

    public char readChar()
        throws IOException
    {
        char c = din().readChar();
        _bytesRead += 2;
        return c;
    }

    public int readInt()
        throws IOException
    {
        int i = din().readInt();
        _bytesRead += 4;
        return i;
    }

    public void skipToOffset(int i)
        throws IOException
    {
        int j = i - _bytesRead;
        if(j > 0)
        {
            super.in.skip(j);
            _bytesRead = i;
        } else
        if(j < 0)
            throw new IOException("Illegal operation aborted.  Attempted to move backwards in OffsetInputStream.");
    }

    private int _bytesRead;
}
