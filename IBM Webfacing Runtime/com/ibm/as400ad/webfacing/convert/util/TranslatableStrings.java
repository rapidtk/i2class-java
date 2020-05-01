// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.util;

import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import java.util.ResourceBundle;

public class TranslatableStrings
{

    public TranslatableStrings()
    {
    }

    public static ResourceBundle getResourceBundle()
        throws WebfacingInternalException
    {
        if(_rb != null)
            return _rb;
        try
        {
            _rb = ResourceBundle.getBundle("convertmri");
        }
        catch(Exception exception)
        {
            throw new WebfacingInternalException("Unable to get resource bundle convertmri: " + exception.getMessage());
        }
        return _rb;
    }

    public static void setResourceBundle(ResourceBundle resourcebundle)
    {
        _rb = resourcebundle;
    }

    public static String getString(String s)
        throws WebfacingInternalException
    {
        return getResourceBundle().getString(s);
    }

    public static String getString(String s, String s1)
    {
        try
        {
            return getString(s);
        }
        catch(Throwable throwable)
        {
            return s1;
        }
    }

    public static final String WEBFACING_CONVERSION_MRI = "convertmri";
    protected static ResourceBundle _rb;
}
