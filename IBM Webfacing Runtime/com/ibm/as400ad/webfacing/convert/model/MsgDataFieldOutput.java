// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.DSPSIZConstants;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.convert.ExportHandler;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.ClientScriptSourceCodeCollection;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            OutputFieldOutput, FieldOutput, NamedFieldOutput

public class MsgDataFieldOutput extends OutputFieldOutput
{

    public MsgDataFieldOutput(FieldNode fieldnode)
    {
        super(fieldnode);
    }

    public ClientScriptSourceCodeCollection getClientScript()
    {
        if(isSingleLine())
        {
            ClientScriptSourceCodeCollection clientscriptsourcecodecollection = new ClientScriptSourceCodeCollection();
            String s = getBeanName();
            String s1 = clientscriptsourcecodecollection.getNewline();
            clientscriptsourcecodecollection.addElement("<%if (" + s + ".isSubfileVisible()) {%>" + s1);
            clientscriptsourcecodecollection.addElement("<%@ page import=\"com.ibm.as400ad.webfacing.runtime.dhtmlview.IHTMLStringTransforms\" %>" + s1);
            clientscriptsourcecodecollection.addElement("<% int TRIMMED_JAVA_STRING_TRANSFORM = IHTMLStringTransforms.TRIMMED_JAVA_STRING_TRANSFORM; " + s1);
            clientscriptsourcecodecollection.addElement("int lastRecordNumber = " + s + ".getLastRecordNumber(); " + s1);
            clientscriptsourcecodecollection.addElement("if (lastRecordNumber > 0) { %>" + s1);
            clientscriptsourcecodecollection.addElement("var allItems = new Array();" + s1);
            clientscriptsourcecodecollection.addElement("var itemIndex = -1;" + s1);
            clientscriptsourcecodecollection.addElement("<% int currentRRN = " + s + ".getRRN(); " + s1);
            clientscriptsourcecodecollection.addElement("int index=0; int maxl = 0 ; " + s1);
            clientscriptsourcecodecollection.addElement("for (int rrn=1; rrn<= lastRecordNumber; ++rrn) { " + s1);
            clientscriptsourcecodecollection.addElement("if (" + s + ".isActiveRecord(rrn-currentRRN+1)) { " + s1);
            clientscriptsourcecodecollection.addElement("if (currentRRN == rrn) { %>" + s1);
            clientscriptsourcecodecollection.addElement("itemIndex = <%=index%>;" + s1);
            clientscriptsourcecodecollection.addElement("<% } " + s1);
            clientscriptsourcecodecollection.addElement("String allItems_String = " + s + ".getFieldValueWithTransform(\"" + "MSGDATA" + "\", rrn-currentRRN+1,TRIMMED_JAVA_STRING_TRANSFORM); %>" + s1);
            clientscriptsourcecodecollection.addElement("allItems[<%=index%>]=\"<%= allItems_String%>\";" + s1);
            clientscriptsourcecodecollection.addElement("<% index++; " + s1);
            clientscriptsourcecodecollection.addElement("if(maxl < allItems_String.length()) { maxl = allItems_String.length() ; } " + s1);
            clientscriptsourcecodecollection.addElement("}} " + s1);
            clientscriptsourcecodecollection.addElement("if(maxl != 0) { %>" + s1 + "document.SCREEN.l<%=zOrder%>_" + s + "$$cbField.size = <%= maxl %>;" + s1 + "<% } %>" + s1);
            clientscriptsourcecodecollection.addElement("document.SCREEN.l<%=zOrder%>_" + s + "$$cbField.comboBoxField = new ComboBoxField(document.SCREEN.l<%=zOrder%>_" + s + "$$cbField, document.SCREEN.l<%=zOrder%>_" + s + "$$cbButton);" + s1);
            clientscriptsourcecodecollection.addElement("document.SCREEN.l<%=zOrder%>_" + s + "$$cbButton.comboBoxButton = new ComboBoxButton(document.SCREEN.l<%=zOrder%>_" + s + "$$cbField);" + s1);
            clientscriptsourcecodecollection.addElement("l<%=zOrder%>_" + s + "$$cbList.comboBoxList = new ComboBoxList(l<%=zOrder%>r" + getRow() + ", document.SCREEN.l<%=zOrder%>_" + s + "$$cbField, l<%=zOrder%>_" + s + "$$cbList, allItems, itemIndex);" + s1);
            clientscriptsourcecodecollection.addElement("<% } %>" + s1);
            clientscriptsourcecodecollection.addElement("<% } // End of 'if' for subfile visible %>" + s1);
            return clientscriptsourcecodecollection;
        } else
        {
            return super.getClientScript();
        }
    }

    public int getColumn()
    {
        return 2;
    }

    public String getFieldName()
    {
        return "MSGDATA";
    }

    public String getQualifiedFieldName()
    {
        return WebfacingConstants.replaceSubstring(super.getQualifiedFieldName(), super.getFieldName(), "MSGDATA");
    }

    public int getRow()
    {
        int i = -1;
        KeywordNode keywordnode = getFieldNode().getParentRecord().findKeywordById(198, getDisplaySizeIndex(), ((FileNode)getFieldNode().getParentRecord().getParent()).getPrimaryDisplaySize());
        if(keywordnode != null)
        {
            KeywordParm keywordparm = keywordnode.getFirstParm();
            if(keywordparm != null)
                i = keywordparm.getVarNumber();
        }
        if(i == -1)
        {
            ExportHandler.err(2, "In MsgDataFieldOutput.getRow(), row not initialized properly.");
            i = DSPSIZConstants.getScreenHeight(getDisplaySizeIndex());
        }
        return i;
    }

    public int getWidth()
    {
        return DSPSIZConstants.getScreenWidth(getDisplaySizeIndex()) - 4;
    }

    private boolean isSingleLine()
    {
        RecordNode recordnode = getFieldNode().getParentRecord().getRelatedSFLCTL();
        KeywordNode keywordnode = recordnode.findKeywordById(200, getDisplaySizeIndex(), ((FileNode)recordnode.getParent()).getPrimaryDisplaySize());
        short word0 = 0;
        if(keywordnode != null)
        {
            KeywordParm keywordparm = keywordnode.getFirstParm();
            if(keywordparm != null)
                word0 = keywordparm.getVarNumber();
        }
        return word0 == 1;
    }

    public static final int COLUMN = 2;
    public static final String MSGDATANAME = "MSGDATA";
}
