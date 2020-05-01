// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.dds.dom.update;


public class ParmDataType
{

    protected ParmDataType()
    {
        _typeCode = _typeCounter++;
    }

    protected ParmDataType(ParmDataType parmdatatype)
    {
        _typeCode = parmdatatype._typeCode;
    }

    public String toString()
    {
        String as[] = {
            "Generic", "Graphic", "Hexadecimal", "Number", "PField Reference", "Quoted String", "Reserved Word", "Symbol", "Word"
        };
        return as[_typeCode];
    }

    private static int _typeCounter = 0;
    protected int _typeCode;

}
