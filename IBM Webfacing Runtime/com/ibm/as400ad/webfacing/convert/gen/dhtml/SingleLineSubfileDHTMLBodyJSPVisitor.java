// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.dhtml;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.webfacing.convert.model.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.dhtml:
//            SubfileControlDHTMLBodyJSPVisitor, AbstractSubfileControlDHTMLBodyJSPVisitor, DHTMLBodyJSPVisitor, DHTMLSourceCodeCollection

public class SingleLineSubfileDHTMLBodyJSPVisitor extends SubfileControlDHTMLBodyJSPVisitor
{

    public SingleLineSubfileDHTMLBodyJSPVisitor()
    {
    }

    public SingleLineSubfileDHTMLBodyJSPVisitor(SubfileControlRecordLayout subfilecontrolrecordlayout, DHTMLSourceCodeCollection dhtmlsourcecodecollection)
    {
        super(subfilecontrolrecordlayout, dhtmlsourcecodecollection);
    }

    protected void printScrollbar()
    {
        int i = getSubfileFirstRow();
        super._lastCol++;
        super._scc.addElement("<TD><IMG src=\"styles/apparea/UpArrow.gif\" class=\"cbButton\" id=\"l<%=zOrder%>_" + getBeanName() + "$$cbButton\" onClick=\"this.comboBoxButton.showList();\"></TD>");
        super._lastCol++;
        super._scc.addElement("<TD><IMG src=\"styles/apparea/RightArrow.gif\" onClick=\"SingleLineSubfileScrollDown(l<%=zOrder%>_" + getBeanName() + "$$cbField, <%=" + getBeanName() + ".enablePageDown() %>);\"></TD>");
    }

    public boolean processBeginOfRow(RecordLayoutRow recordlayoutrow)
    {
        if(!isProcessSubfiles())
            return super.processBeginOfRow(recordlayoutrow);
        else
            return true;
    }

    public void processBeginOfTraversal()
    {
        if(isProcessSubfiles())
        {
            String s = getBeanName();
            super._scc.addElement("<%if (" + s + ".isSubfileVisible())%>");
            super._scc.addElement("<% { %>");
            super._scc.addElement("<TR id=\"l<%=zOrder%>r" + getSubfileFirstRow() + "\" style=\"visibility:hidden\">");
            super._lastCol = getSubfileFirstColumn() - 1;
            if(super._lastCol > 1)
                super._scc.addElement("<TD colspan=" + Integer.toString(super._lastCol - 1) + ">&nbsp;</TD>");
            super._scc.addElement("<TD><IMG src=\"styles/apparea/LeftArrow.gif\" onClick=\"SingleLineSubfileScrollUp(l<%=zOrder%>_" + s + "$$cbField, <%=" + s + ".enablePageUp() %>);\"></TD>");
            super._lastCol++;
            super._scc.addElement("<TD colspan=" + Integer.toString((getSubfileLastColumn() - getSubfileFirstColumn()) + 1) + ">");
            super._lastCol = getSubfileLastColumn();
        } else
        {
            super.processBeginOfTraversal();
        }
    }

    public void processEndOfRow(RecordLayoutRow recordlayoutrow)
    {
        if(!isProcessSubfiles())
            super.processEndOfRow(recordlayoutrow);
    }

    public void processEndOfTraversal()
    {
        if(isProcessSubfiles())
        {
            super._scc.addElement("</TD>");
            printScrollbar();
            super._scc.addElement("</TR>");
            super._scc.addElement("<% } // End of 'if' for subfile visible %>");
        } else
        {
            super.processEndOfTraversal();
        }
    }

    public void processFieldOnRow(boolean flag, FieldOnRow fieldonrow)
    {
        if(isProcessSubfiles())
            super._scc.addElement("<INPUT CLASS=\"cbField\" ID=\"l<%=zOrder%>_" + getBeanName() + "$$cbField\" onclick=\"this.comboBoxField.showList();\" READONLY TABINDEX=\"-1\" STYLE=\"width:100%\">");
        else
            super.processFieldOnRow(flag, fieldonrow);
    }
}
