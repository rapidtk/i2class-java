// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;

import com.ibm.as400ad.webfacing.common.UIMMappingProperties;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.ILibraryFile;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.core.WFException;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.File;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.help:
//            HelpDefinition

public class UIMMap
{

    public UIMMap(String s)
    {
        _trace = WFSession.getTraceLogger();
        _uimMap = new HashMap();
        _docRoot = null;
        _docRoot = s;
        try
        {
            _objectToSourceMap = UIMMappingProperties.getMappingProperties();
        }
        catch(WFException wfexception)
        {
            _objectToSourceMap = null;
        }
    }

    private String checkQualifiedPath(String s, String s1)
    {
        for(Iterator iterator = getPathTokens(s); iterator.hasNext();)
        {
            String s2 = (String)iterator.next();
            String s3 = s2 + File.separator + s1;
            File file = new File(_docRoot, s3);
            if(file.exists())
                return s3;
        }

        return null;
    }

    private Iterator getPathTokens(String s)
    {
        Vector vector = new Vector();
        if(s.trim().length() == 0)
            return vector.iterator();
        for(int i = WebfacingConstants.indexOfUnquotedChar(s, ';'); i > -1; i = WebfacingConstants.indexOfUnquotedChar(s, ';'))
        {
            String s1 = s.substring(0, i);
            vector.addElement(processSinglePath(s1));
            s = s.substring(i + 1);
        }

        String s2 = s;
        vector.addElement(processSinglePath(s2));
        return vector.iterator();
    }

    public String getQualifiedPath(ILibraryFile ilibraryfile, String s)
    {
        String s1 = ilibraryfile.getLibraryName().trim();
        String s2 = ilibraryfile.getFileName().trim();
        if(_objectToSourceMap != null)
        {
            String s3 = _objectToSourceMap.getProperty(s1 + "/" + s2);
            if(s3 != null)
            {
                String s5 = checkQualifiedPath(s3, s);
                if(s5 != null)
                    return s5;
            }
            try
            {
                String s4 = _objectToSourceMap.getProperty("&LIB/" + s2);
                if(s4 != null)
                {
                    s4 = WebfacingConstants.replaceSubstring(s4, "&LIB", s1);
                    String s6 = checkQualifiedPath(s4, s);
                    if(s6 != null)
                        return s6;
                }
                s4 = _objectToSourceMap.getProperty(s1 + "/" + "&PNLGRP");
                if(s4 != null)
                {
                    s4 = WebfacingConstants.replaceSubstring(s4, "&PNLGRP", s2);
                    String s7 = checkQualifiedPath(s4, s);
                    if(s7 != null)
                        return s7;
                }
                s4 = _objectToSourceMap.getProperty("&LIB/&PNLGRP");
                if(s4 != null)
                {
                    s4 = WebfacingConstants.replaceSubstring(s4, "&LIB", s1);
                    s4 = WebfacingConstants.replaceSubstring(s4, "&PNLGRP", s2);
                    String s8 = checkQualifiedPath(s4, s);
                    if(s8 != null)
                        return s8;
                }
            }
            catch(IndexOutOfBoundsException indexoutofboundsexception) { }
        }
        return WebfacingConstants.replaceSpecialCharacters(s1) + "/" + "QPNLSRC" + "/" + WebfacingConstants.replaceSpecialCharacters(s2);
    }

    public String getQualifiedPath(HelpDefinition helpdefinition)
    {
        return getQualifiedPath(((ILibraryFile) (helpdefinition)), WebfacingConstants.replaceSpecialCharacters(helpdefinition.getDefinition()) + ".htm");
    }

    private String processSinglePath(String s)
    {
        try
        {
            s = s.trim();
            if(s.startsWith("/"))
                s = s.substring(1);
            s = WebfacingConstants.getCharacterReplacedPackageName(s);
            s = WebfacingConstants.replaceSubstring(s, ".", File.separator);
        }
        catch(IllegalArgumentException illegalargumentexception)
        {
            s = "";
        }
        catch(IndexOutOfBoundsException indexoutofboundsexception)
        {
            s = "";
        }
        return s;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2002, all rights reserved");
    private static final String FILENAME_WILDCARD = "&PNLGRP";
    private static final String LIBRARY_WILDCARD = "&LIB";
    protected ITraceLogger _trace;
    private UIMMappingProperties _objectToSourceMap;
    private Map _uimMap;
    private String _docRoot;

}
