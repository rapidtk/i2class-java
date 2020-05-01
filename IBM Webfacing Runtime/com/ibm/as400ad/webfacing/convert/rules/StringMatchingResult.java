// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.rules;


// Referenced classes of package com.ibm.as400ad.webfacing.convert.rules:
//            ResultContainer

public class StringMatchingResult
{

    public StringMatchingResult()
    {
    }

    public StringMatchingResult(String s, ResultContainer resultcontainer)
    {
        replacedString = s;
        resultContainer = resultcontainer;
    }

    public String getReplacedString()
    {
        return replacedString;
    }

    public ResultContainer getResultContainer()
    {
        return resultContainer;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2000-2002 All rights reserved.");
    private String replacedString;
    private ResultContainer resultContainer;

}
