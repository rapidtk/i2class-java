// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.dhtml;

import com.ibm.as400ad.code400.dom.AnyNode;
import com.ibm.as400ad.code400.dom.RecordNode;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.convert.*;
import com.ibm.as400ad.webfacing.convert.gen.FieldOutputTraversal;
import com.ibm.as400ad.webfacing.convert.gen.WebResourceFileWriter;
import com.ibm.as400ad.webfacing.convert.model.RecordLayout;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.dhtml:
//            JSPGenerator

public class SubfileControlJSPGenerator extends JSPGenerator
    implements IWebResourceGenerator
{

    public SubfileControlJSPGenerator(RecordLayout recordlayout, IConversionFactory iconversionfactory, WebResourceFileWriter webresourcefilewriter)
    {
        super(recordlayout, iconversionfactory, webresourcefilewriter);
    }

    protected void generateCode(IFieldOutputVisitor ifieldoutputvisitor)
    {
        boolean flag = ifieldoutputvisitor.isControlBeforeSubfiles();
        FieldOutputTraversal fieldoutputtraversal = new FieldOutputTraversal();
        ifieldoutputvisitor.printBeginningLines();
        if(flag)
            fieldoutputtraversal.traverse(ifieldoutputvisitor);
        ifieldoutputvisitor.processBeginOfSubfiles();
        fieldoutputtraversal.traverse(ifieldoutputvisitor);
        ifieldoutputvisitor.processEndOfSubfiles();
        if(!flag)
            fieldoutputtraversal.traverse(ifieldoutputvisitor);
        ifieldoutputvisitor.printEndingLines();
    }

    public static String getRecordQualifiedID(RecordNode recordnode, String s)
    {
        return recordnode.getWebName() + "$" + s;
    }

    public static String getScrollbarBottomMarkerJavascriptID(RecordNode recordnode)
    {
        return WebfacingConstants.getLayerPrefix() + getRecordQualifiedID(recordnode, "cellBelowScrollbar");
    }

    public static String getScrollbarJavascriptID(RecordNode recordnode)
    {
        return WebfacingConstants.getLayerPrefix() + getRecordQualifiedID(recordnode, "scrollbarTable");
    }

    public static String getScrollbarJavascriptIDWithoutPrefix(RecordNode recordnode)
    {
        return getRecordQualifiedID(recordnode, "scrollbarTable");
    }

    public static String getScrollbarTopMarkerJavascriptID(RecordNode recordnode)
    {
        return WebfacingConstants.getLayerPrefix() + getRecordQualifiedID(recordnode, "scrollbarCell");
    }

    public static final String SCROLLBAR_BOTTOM_MARKER_JS_ID_SUFFIX = "cellBelowScrollbar";
    public static final String SCROLLBAR_JAVASCRIPT_ID_SUFFIX = "scrollbarTable";
    public static final String SCROLLBAR_TOP_MARKER_JS_ID_SUFFIX = "scrollbarCell";
}
