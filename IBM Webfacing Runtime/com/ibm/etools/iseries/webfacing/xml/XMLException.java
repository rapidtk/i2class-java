// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.xml;


public class XMLException extends Exception
{

    public XMLException(String s, String s1, String s2, int i, int j)
    {
        super(s);
        _strMessage = null;
        _strFilename = null;
        _strLine = null;
        _iLineNumber = -1;
        _iColumnNumber = -1;
        _strMessage = s;
        _strFilename = s1;
        _strLine = s2;
        _iLineNumber = i;
        _iColumnNumber = j;
    }

    public XMLException(String s, String s1, StringBuffer stringbuffer, int i, int j)
    {
        super(s);
        _strMessage = null;
        _strFilename = null;
        _strLine = null;
        _iLineNumber = -1;
        _iColumnNumber = -1;
        _strMessage = s;
        _strFilename = s1;
        _strLine = stringbuffer.toString();
        _iLineNumber = i;
        _iColumnNumber = j;
    }

    public static final String copyright = "(c) Copyright IBM Corporation 2002. all rights reserved";
    public String _strMessage;
    public String _strFilename;
    public String _strLine;
    public int _iLineNumber;
    public int _iColumnNumber;
}
