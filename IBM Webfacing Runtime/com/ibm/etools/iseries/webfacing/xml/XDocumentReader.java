// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.xml;


public class XDocumentReader
{

    public XDocumentReader(String s)
    {
        _strText = null;
        _iPosition = 0;
        _strText = s;
    }

    public String readLine()
    {
        String s = null;
        if(_iPosition < _strText.length())
        {
            int i = _strText.indexOf("\n", _iPosition);
            if(i != -1)
            {
                s = _strText.substring(_iPosition, i);
                _iPosition = i + 1;
            } else
            {
                s = _strText.substring(_iPosition);
                _iPosition = _strText.length();
            }
        }
        return s;
    }

    public static final String copyright = "(c) Copyright IBM Corporation 2002. all rights reserved";
    protected String _strText;
    protected int _iPosition;
}
