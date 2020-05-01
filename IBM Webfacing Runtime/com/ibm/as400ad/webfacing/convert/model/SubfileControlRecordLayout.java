// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.convert.IConversionFactory;
import com.ibm.as400ad.webfacing.convert.IRecordLayout;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            RecordLayout, SubfileFieldOutputDecorator, SubfileRecordLayout

public class SubfileControlRecordLayout extends RecordLayout
    implements IRecordLayout
{

    public SubfileControlRecordLayout(RecordNode recordnode, IConversionFactory iconversionfactory)
    {
        super(recordnode, iconversionfactory);
    }

    public int getSFLPAG()
    {
        return getSubfileRecordLayout().getSFLPAG();
    }

    public SubfileRecordLayout getSubfileRecordLayout()
    {
        return _subfileRecordLayout;
    }

    public String getViewInterface()
    {
        return "com.ibm.as400ad.webfacing.runtime.view.IDisplaySFLCTLRecord";
    }

    public boolean isSFLDSPCTLSpecified()
    {
        return _SFLDSPCTLKeyword != null;
    }

    protected void prepare()
    {
        super.prepare();
        _SFLDSPCTLKeyword = getRecordNode().findKeywordById(187);
    }

    protected String replaceFieldId(String s, int i, String s1, String s2, boolean flag)
    {
        if(s2 != null)
        {
            SubfileFieldOutputDecorator subfilefieldoutputdecorator = new SubfileFieldOutputDecorator(_subfileRecordLayout.getFieldOutput(s1), getFirstColumn(), getWidth());
            if(subfilefieldoutputdecorator != null)
            {
                String s3 = subfilefieldoutputdecorator.getClientScriptLocation() + subfilefieldoutputdecorator.getTagId();
                String s4 = "&" + s2 + "{" + s1 + "}";
                if(flag)
                    s4 = "&{" + s2 + "." + s1 + "}";
                s = WebfacingConstants.replaceSubstring(s, s4, s3, i);
                int j = s.indexOf("=", i + s3.length());
                if(j > 0)
                {
                    int k = s.indexOf(";", j);
                    if(k > 0)
                    {
                        StringBuffer stringbuffer = new StringBuffer(s.substring(0, i));
                        String s5 = "rrn";
                        String s6 = s.substring(i, k + 1);
                        String s7 = "";
                        if(k + 1 < s.length())
                            s7 = s.substring(k + 1);
                        stringbuffer.append("\n<% for (int " + s5 + "=1; " + s5 + " <= " + getSubfileRecordLayout().getRRNLoopBound() + "; " + s5 + "++ ) { %>");
                        stringbuffer.append("\n" + s6);
                        stringbuffer.append("\n<% } %>");
                        stringbuffer.append("\n" + s7);
                        return stringbuffer.toString();
                    }
                }
            }
            return s;
        } else
        {
            return super.replaceFieldId(s, i, s1, s2, flag);
        }
    }

    public void setSubfileRecordLayout(SubfileRecordLayout subfilerecordlayout)
    {
        _subfileRecordLayout = subfilerecordlayout;
    }

    public boolean subfileHasFieldSelection()
    {
        return getSubfileRecordLayout().hasFieldSelection();
    }

    SubfileRecordLayout _subfileRecordLayout;
    private KeywordNode _SFLDSPCTLKeyword;
}
