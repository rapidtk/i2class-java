// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.FieldType;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.convert.rules.EditCodeMappingHandler;
import com.ibm.as400ad.webfacing.runtime.host.CenturyType;
import com.ibm.as400ad.webfacing.runtime.host.SeparatorType;
import com.ibm.as400ad.webfacing.util.Assert;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            ConstantFieldOutput, FieldOutput, FieldWebSettings, SpecialCharHandler

public class ComputedConstantFieldOutput extends ConstantFieldOutput
{

    public ComputedConstantFieldOutput(FieldNode fieldnode)
    {
        super(fieldnode);
    }

    public String getFieldTextWithTransform(int i)
    {
        return getFieldTextWithTransform(i, super.INDEX_NOT_USED, super.INDEX_NOT_USED);
    }

    public boolean isComputed()
    {
        return true;
    }

    private String getComputedConstant(String s, int i, int j)
    {
        FieldNode fieldnode = getFieldNode();
        FieldType fieldtype = fieldnode.getFieldType();
        if(fieldtype.isOfType(3))
            return getBeanName() + "." + getTimeCall(s, i, j);
        if(fieldtype.isOfType(2))
            return getBeanName() + "." + getDateCall(s, i, j);
        String s1 = getIndexParams(i, j);
        if(s1 == null)
            s1 = "";
        if(fieldtype.isOfType(5))
            return getBeanName() + ".getSystemName(" + s1 + ")";
        if(fieldtype.isOfType(4))
        {
            return getBeanName() + ".getUserID(" + s1 + ")";
        } else
        {
            Assert.fail("Field type " + fieldtype.toString() + " not allowed for ComputedConstantFieldOutput");
            return "\"" + getSampleText() + "\"";
        }
    }

    private String getDateCall(String s, int i, int j)
    {
        String s1 = "getDate(";
        StringBuffer stringbuffer = new StringBuffer(s1);
        FieldNode fieldnode = getFieldNode();
        SeparatorType separatortype = SeparatorType.NO_SEPARATOR;
        String s2 = "com.ibm.as400ad.webfacing.runtime.host.DateType.JOB_DATE";
        CenturyType centurytype = CenturyType.TWO_DIGITS;
        String s3 = "com.ibm.as400ad.webfacing.runtime.host.CenturyType.TWO_DIGITS";
        String s4 = null;
        String s5 = "'0'";
        String s6 = null;
        KeywordNodeEnumeration keywordnodeenumeration = fieldnode.getKeywordsOfType(81);
        KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
        KeywordParm keywordparm = keywordnode.getFirstParm();
        if(keywordparm != null)
        {
            int k = keywordparm.getVarKwdToken();
            if(k == 596)
                s2 = "com.ibm.as400ad.webfacing.runtime.host.DateType.SYS_DATE";
            if(k == 598)
            {
                centurytype = CenturyType.FOUR_DIGITS;
                s3 = "com.ibm.as400ad.webfacing.runtime.host.CenturyType.FOUR_DIGITS";
            }
            keywordparm = keywordnode.getParm(1);
            if(keywordparm != null && keywordparm.getVarKwdToken() == 598)
            {
                centurytype = CenturyType.FOUR_DIGITS;
                s3 = "com.ibm.as400ad.webfacing.runtime.host.CenturyType.FOUR_DIGITS";
            }
        }
        keywordnodeenumeration = fieldnode.getKeywordsOfType(93);
        if(keywordnodeenumeration.hasMoreElements())
        {
            KeywordNode keywordnode1 = keywordnodeenumeration.nextKeyword();
            KeywordParm keywordparm1 = keywordnode1.getFirstParm();
            if(keywordparm1 != null)
            {
                char c = EditCodeMappingHandler.getSystemEditCode(keywordparm1.getVarChar());
                if(c == 'Y')
                {
                    separatortype = SeparatorType.HAS_SEPARATOR;
                } else
                {
                    s4 = "'" + c + "'";
                    KeywordParm keywordparm2 = keywordnode1.getParm(1);
                    if(keywordparm2 != null)
                        s5 = "'" + keywordparm2.getVarChar() + "'";
                }
            }
        }
        keywordnodeenumeration = fieldnode.getKeywordsOfType(95);
        if(keywordnodeenumeration.hasMoreElements())
        {
            KeywordParm keywordparm3 = keywordnodeenumeration.nextKeyword().getFirstParm();
            if(keywordparm3 != null)
            {
                String s7 = keywordparm3.getVarStringUnquoted();
                if(s7 != null)
                    s6 = "\"" + keywordparm3.getJavaString() + "\"";
            }
        }
        stringbuffer.append(s2);
        if(separatortype == SeparatorType.HAS_SEPARATOR)
            stringbuffer.append(",").append(s3).append(",").append("com.ibm.as400ad.webfacing.runtime.host.SeparatorType.HAS_SEPARATOR");
        else
        if(s4 != null)
        {
            stringbuffer.append(",").append(s3);
            stringbuffer.append(",").append(s4);
            stringbuffer.append(",").append(s5);
        } else
        if(s6 != null)
        {
            stringbuffer.append(",").append(s3);
            stringbuffer.append(",").append(s6);
            stringbuffer.append(",").append(s);
        } else
        if(centurytype == CenturyType.FOUR_DIGITS)
            stringbuffer.append(",").append(s3).append(",").append("com.ibm.as400ad.webfacing.runtime.host.SeparatorType.NO_SEPARATOR");
        String s8 = getIndexParams(i, j);
        if(s6 != null)
        {
            if(s8 != null)
                stringbuffer.append(",").append(s8);
            stringbuffer.append(")");
            return WebfacingConstants.replaceSubstring(stringbuffer.toString(), "getDate", "getDateWithTransform");
        }
        stringbuffer.append(")");
        if(s8 != null)
            stringbuffer.append(".substring(").append(s8).append(")");
        return stringbuffer.toString();
    }

    public String getFieldTextWithTransform(int i, int j, int k)
    {
        String s = null;
        String s1 = super._webSettings.getText();
        if(s1 != null)
        {
            s = super._charHandler.transformSpecialChars(s1, i);
            if(j != super.INDEX_NOT_USED && k != super.INDEX_NOT_USED)
                s = s.substring(j, k);
        } else
        {
            String s2 = super._charHandler.getTransformParam(i);
            s = FieldOutput.wrapJSPExpr(getComputedConstant(s2, j, k));
        }
        return s;
    }

    private String getTimeCall(String s, int i, int j)
    {
        String s1 = "getSystemTime(";
        FieldNode fieldnode = getFieldNode();
        StringBuffer stringbuffer = new StringBuffer(s1);
        Object obj = null;
        String s3 = "'0'";
        String s4 = null;
        KeywordNodeEnumeration keywordnodeenumeration = fieldnode.getKeywordsOfType(93);
        if(keywordnodeenumeration.hasMoreElements())
        {
            stringbuffer = new StringBuffer(s1);
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            KeywordParm keywordparm1 = keywordnode.getFirstParm();
            String s2 = "'" + EditCodeMappingHandler.getSystemEditCode(keywordparm1.getVarChar()) + "'";
            keywordparm1 = keywordnode.getParm(1);
            if(keywordparm1 != null)
                s3 = "'" + keywordparm1.getVarChar() + "'";
            stringbuffer.append(s2);
            stringbuffer.append("," + s3);
        }
        keywordnodeenumeration = fieldnode.getKeywordsOfType(95);
        if(keywordnodeenumeration.hasMoreElements())
        {
            stringbuffer = new StringBuffer(s1);
            KeywordParm keywordparm = keywordnodeenumeration.nextKeyword().getFirstParm();
            if(keywordparm != null)
            {
                String s6 = keywordparm.getVarStringUnquoted();
                if(s6 != null)
                {
                    s4 = "\"" + keywordparm.getJavaString() + "\"";
                    stringbuffer.append(s4).append(",").append(s);
                }
            }
        }
        String s5 = getIndexParams(i, j);
        if(s4 != null)
        {
            if(s5 != null)
                stringbuffer.append(",").append(s5);
            stringbuffer.append(")");
            return WebfacingConstants.replaceSubstring(stringbuffer.toString(), "getSystemTime", "getSystemTimeWithTransform");
        }
        stringbuffer.append(")");
        if(s5 != null)
            stringbuffer.append(".substring(").append(s5).append(")");
        return stringbuffer.toString();
    }

    String getUsrDefineHTML()
    {
        String s = super._webSettings.getUsrDefineHTML();
        if(s != null)
            s = super._charHandler.replaceSpecialSymbols(s);
        return s;
    }

    public boolean hasKeyLabelDetected()
    {
        return false;
    }

    void initializeFieldText()
    {
    }

    protected void initializeWidthAndHeight()
    {
        String s = super._webSettings.getText();
        if(s != null)
            super._width = s.length();
    }

    static final String copyRight = "(c) Copyright IBM Corporation 1999-2003. All Rights Reserved";
}
