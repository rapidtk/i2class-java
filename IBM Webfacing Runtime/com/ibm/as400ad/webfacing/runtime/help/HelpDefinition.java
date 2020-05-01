// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;

import com.ibm.as400ad.webfacing.runtime.controller.ILibraryFile;
import com.ibm.as400ad.webfacing.runtime.model.def.ConditionedKeyword;

public class HelpDefinition extends ConditionedKeyword
    implements ILibraryFile
{

    public HelpDefinition(String s, String s1, String s2, String s3)
    {
        _definition = null;
        _object = null;
        _library = null;
        _type = null;
        _record = null;
        _record = s;
        _definition = s1;
        _object = s2;
        _library = s3;
    }

    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj != null && (obj instanceof HelpDefinition))
        {
            boolean flag = true;
            HelpDefinition helpdefinition = (HelpDefinition)obj;
            if(_library != null && helpdefinition.getLibrary() != null)
                flag = flag && _library.equals(helpdefinition.getLibrary());
            if(_object != null && helpdefinition.getObject() != null)
                flag = flag && _object.equals(helpdefinition.getObject());
            return flag && _definition.equals(helpdefinition.getDefinition());
        } else
        {
            return false;
        }
    }

    public String getDefinition()
    {
        return _definition;
    }

    public String getFileName()
    {
        return getObject();
    }

    public String getLibrary()
    {
        return _library;
    }

    public String getLibraryName()
    {
        return _library != null ? _library : "*LIBL";
    }

    public String getObject()
    {
        return _object;
    }

    public String getRecord()
    {
        return _record;
    }

    public String getType()
    {
        return _type;
    }

    public boolean needCurrentDSPF()
    {
        return _library == null && _object == null;
    }

    public boolean needToResolve()
    {
        return _object != null && (_library != null && (_library.equals("*LIBL") || _library.equals("*CURLIB")) || _library == null);
    }

    public void setDefinition(String s)
    {
        _definition = s;
    }

    public void setLibraryName(String s)
    {
        _library = s;
    }

    public void setLibraryObject(ILibraryFile ilibraryfile)
    {
        _library = ilibraryfile.getLibraryName();
        _object = ilibraryfile.getFileName();
    }

    public void setObject(String s)
    {
        _object = s;
    }

    public void setRecord(String s)
    {
        _record = s;
    }

    public void setType(String s)
    {
        _type = s;
    }

    public String toString()
    {
        return _object + "/" + _definition;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2001, 2002, all rights reserved");
    private String _definition;
    private String _object;
    private String _library;
    private String _type;
    private String _record;

}
