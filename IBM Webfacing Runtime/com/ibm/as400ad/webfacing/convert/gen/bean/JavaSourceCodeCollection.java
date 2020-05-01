// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.designer.io.SourceCodeCollection;
import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import java.util.*;

public class JavaSourceCodeCollection extends SourceCodeCollection
{

    public JavaSourceCodeCollection()
    {
        _indentLevel = 0;
        KWDDEF_NUM = 0;
        _modifiers = new HashSet();
    }

    public void addCodeForXXXMSGIDKeyword(KeywordNodeEnumeration keywordnodeenumeration, String s, String s1, String s2)
    {
        for(int i = 1; keywordnodeenumeration.hasMoreElements(); i++)
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            KeywordParmEnumeration keywordparmenumeration = keywordnode.getParms();
            PaddedStringBuffer paddedstringbuffer = null;
            paddedstringbuffer = new PaddedStringBuffer(35);
            paddedstringbuffer.concat(s1, "$", s, "MSGID", Integer.toString(i));
            String s3 = paddedstringbuffer.toString();
            addLine("XXXMSGIDDefinition " + s3 + " = new XXXMSGIDDefinition();");
            String s4 = keywordnode.getIndicatorString();
            if(s4 != null)
            {
                paddedstringbuffer = new PaddedStringBuffer(35);
                paddedstringbuffer.concat(s3, ".setIndicatorExpression(\"", s4, "\");");
                addLine(paddedstringbuffer.toString());
            }
            KeywordParm keywordparm = keywordparmenumeration.nextParm();
            String s5 = keywordparm.getVarString();
            paddedstringbuffer = new PaddedStringBuffer(35);
            paddedstringbuffer.concat(s3, ".setMsgId(\"", s5, "\");");
            addLine(paddedstringbuffer.toString());
            keywordparm = keywordparmenumeration.nextParm();
            s5 = keywordparm.getVarString();
            int j = s5.indexOf("/");
            String s7 = null;
            if(j >= 0)
            {
                String s8 = s5.substring(0, j);
                s7 = s5.substring(j + 1);
                paddedstringbuffer = new PaddedStringBuffer(35);
                paddedstringbuffer.concat(s3, ".setLibraryName(\"", s8, "\");");
                addLine(paddedstringbuffer.toString());
            } else
            {
                s7 = s5;
            }
            paddedstringbuffer = new PaddedStringBuffer(35);
            paddedstringbuffer.concat(s3, ".setMsgFile(\"", s7, "\");");
            addLine(paddedstringbuffer.toString());
            keywordparm = keywordparmenumeration.nextParm();
            if(keywordparm != null)
            {
                if(keywordparm.getVarParmToken() != 0)
                {
                    paddedstringbuffer = new PaddedStringBuffer(35);
                    paddedstringbuffer.concat(s3, ".setResponseIndicator(\"", Integer.toString(keywordparm.getVarParmToken()), "\");");
                    addLine(paddedstringbuffer.toString());
                    keywordparm = keywordparmenumeration.nextParm();
                }
                if(keywordparm != null)
                {
                    String s6 = keywordparm.getVarString();
                    paddedstringbuffer = new PaddedStringBuffer(35);
                    paddedstringbuffer.concat(s3, ".setMsgDataFieldName(\"", WebfacingConstants.replaceSpecialCharacters(s6.substring(1)), "\");");
                    addLine(paddedstringbuffer.toString());
                }
            }
            String s9 = s2 != null ? s2 + "." : "";
            paddedstringbuffer = new PaddedStringBuffer(35);
            paddedstringbuffer.concat(s9, "add", s, "MSGIDKeyword(", s3, ");");
            addLine(paddedstringbuffer.toString());
            addLine("");
        }

    }

    public void addCodeForXXXMSGKeyword(KeywordNodeEnumeration keywordnodeenumeration, String s, String s1, String s2)
    {
        String s3 = s + "msgKwd";
        int i = 1;
        Object obj = null;
        while(keywordnodeenumeration.hasMoreElements()) 
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            KeywordParm keywordparm = keywordnode.getFirstParm();
            String s4 = keywordnode.getIndicatorString();
            String s5 = Integer.toString(i);
            if(keywordparm != null)
            {
                PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(85);
                if(s4 == null)
                    s4 = "";
                paddedstringbuffer.concat("KeywordDefinition ", s1, s3, s5, " = new KeywordDefinition(KWD_" + s + "MSG, \"" + s4 + "\");");
                addLine(paddedstringbuffer.toString());
                paddedstringbuffer = new PaddedStringBuffer(85);
                String s6 = keywordparm.getJavaString();
                paddedstringbuffer.concat(s1, s3, s5, ".addParameter(\"", s6, "\");");
                addLine(paddedstringbuffer.toString());
                keywordparm = keywordnode.getParm(1);
                if(keywordparm != null)
                {
                    PaddedStringBuffer paddedstringbuffer1 = new PaddedStringBuffer(85);
                    paddedstringbuffer1.concat(s1, s3, s5, ".addParameter(\"", Integer.toString(keywordparm.getVarNumber()), "\");");
                    addLine(paddedstringbuffer1.toString());
                }
            }
            PaddedStringBuffer paddedstringbuffer2 = new PaddedStringBuffer(85);
            String s7 = s2 != null ? s2 + "." : "";
            paddedstringbuffer2.concat(s7, "add(", s1, s3, s5, ");");
            addLine(paddedstringbuffer2.toString());
            i++;
        }
    }

    public void addKeywordDefinition(KeywordNodeEnumeration keywordnodeenumeration)
    {
        if(keywordnodeenumeration.hasMoreElements())
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            String s = "kwdDef" + Integer.toString(KWDDEF_NUM++);
            String s1 = keywordnode.getIndicatorString();
            if(s1 != null)
                addLine("KeywordDefinition " + s + " = new KeywordDefinition(" + keywordnode.getKeywordIdAsString() + ", \"" + s1 + "\");");
            else
                addLine("KeywordDefinition " + s + " = new KeywordDefinition(" + keywordnode.getKeywordIdAsString() + ");");
            Vector vector = keywordnode.getParmsVector();
            if(vector != null && vector.size() > 0)
            {
                for(int i = 0; i < vector.size(); i++)
                {
                    KeywordParm keywordparm = (KeywordParm)vector.elementAt(i);
                    String s2 = keywordparm.getVarKwdTokenAsString();
                    if(s2 != null)
                        addLine(s + ".addParameter(\"" + s2 + "\");");
                }

            }
            addLine("add(" + s + ");");
        }
    }

    public void addKeywordWithNoParam(KeywordNodeEnumeration keywordnodeenumeration)
    {
        if(keywordnodeenumeration.hasMoreElements())
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            String s = keywordnode.getIndicatorString();
            if(s != null)
                addLine("add(new KeywordDefinition(" + keywordnode.getKeywordIdAsString() + ", \"" + s + "\"));");
            else
                addLine("add(new KeywordDefinition(" + keywordnode.getKeywordIdAsString() + "));");
        }
    }

    public void addLine(String s)
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append(getNewline());
        for(int i = 0; i < _indentLevel; i++)
            stringbuffer.append("   ");

        stringbuffer.append(s);
        addElement(stringbuffer);
    }

    public void addModifier(String s)
    {
        _modifiers.add(s);
    }

    Set getModifiers()
    {
        return _modifiers;
    }

    public void setIndentLevel(int i)
    {
        _indentLevel = i;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000");
    private int _indentLevel;
    private static final String INDENT = "   ";
    private int KWDDEF_NUM;
    public static final String ABSTRACT = "abstract";
    public static final String FINAL = "final";
    public static final String NATIVE = "native";
    public static final String STATIC = "static";
    public static final String SYNCHRONIZED = "synchronized";
    public static final String VOLATILE = "volatile";
    private HashSet _modifiers;
    public static final String PUBLIC = "public";
    public static final String PROTECTED = "protected";
    public static final String PACKAGE = "";
    public static final String PRIVATE = "private";

}
