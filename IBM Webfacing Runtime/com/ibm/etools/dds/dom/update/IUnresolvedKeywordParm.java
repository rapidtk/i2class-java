// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.dds.dom.update;


// Referenced classes of package com.ibm.etools.dds.dom.update:
//            ISourceLocations, ParmDataType

public interface IUnresolvedKeywordParm
{

    public abstract ISourceLocations getSourceLocations();

    public abstract String getOriginalSource();

    public abstract String getStringValue();

    public abstract double getNumericValue();

    public abstract void setNumericValue(double d);

    public abstract void setStringValue(String s);

    public abstract void setType(ParmDataType parmdatatype);

    public abstract ParmDataType getType();

    public abstract boolean isReservedWord();

    public abstract boolean isPFieldReference();

    public abstract boolean isNumber();

    public abstract boolean isQuoted();

    public abstract boolean isWord();

    public abstract boolean isGeneric();

    public abstract boolean isEOF();

    public abstract boolean isHexadecimal();

    public abstract boolean isGraphic();

    public abstract boolean isSymbol();
}
