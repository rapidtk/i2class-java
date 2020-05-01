// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.dhtml;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.webfacing.convert.model.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.dhtml:
//            AbstractSubfileControlDHTMLBodyJSPVisitor, DHTMLBodyJSPVisitor, SubfileControlJSPGenerator, DHTMLSourceCodeCollection

public class FieldSelectionSubfileDHTMLBodyJSPVisitor extends AbstractSubfileControlDHTMLBodyJSPVisitor
{

    public FieldSelectionSubfileDHTMLBodyJSPVisitor()
    {
    }

    public FieldSelectionSubfileDHTMLBodyJSPVisitor(SubfileControlRecordLayout subfilecontrolrecordlayout, DHTMLSourceCodeCollection dhtmlsourcecodecollection)
    {
        super(subfilecontrolrecordlayout, dhtmlsourcecodecollection);
    }

    protected void printBeginOfRow(int i)
    {
        if(isProcessSubfiles())
        {
            int j = (i - getSubfileFirstRow()) + 1;
            super._scc.addElement("<% if( " + getBeanName() + ".isRowOfSubfileRecordDisplayed(" + getRRNVarName() + ", " + j + ")) { %>");
        }
        super.printBeginOfRow(i);
    }

    public void processBeginOfTraversal()
    {
        super.processBeginOfTraversal();
        if(isProcessSubfiles())
        {
            super._scc.addElement("<%if (" + getBeanName() + ".isSubfileVisible()) {%>");
            printScrollbar();
            super._scc.addElement("<% int col= 1;%>");
            super._scc.addElement("<% int row= 0;%>");
            super._scc.addElement(getDHMTLRowInitialization());
            super._scc.addElement(getStyleForRowDeclaration());
            super._scc.addElement("<% for (int " + getRRNVarName() + " = 1; " + getRRNVarName() + " <= " + getBeanName() + "." + "getVisibleRecordSize()" + "; " + getRRNVarName() + "++) { %>");
            super._scc.addElement(getIsActiveRecordIfStatement());
            super._scc.addElement("<% row++;%>");
            super._scc.addElement(getStyleForRowInitialization());
        }
    }

    public void processEndOfRow(RecordLayoutRow recordlayoutrow)
    {
        if(isProcessSubfiles())
        {
            super._scc.addElement("<% if (lastCol + " + Integer.toString(getSubfileFirstColumn() - 1) + " < " + getScreenOrWindowWidth() + ") { %>");
            super._scc.addElement("<TD colspan=<%= " + getScreenOrWindowWidth() + " - (lastCol + " + Integer.toString(getSubfileFirstColumn() - 1) + ")%>>&nbsp;</TD>");
            super._scc.addElement("<% } %>");
            super._scc.addElement("</TR>");
            super._scc.addElement("<% } //if isRowOfSubfileRecordDisplayed() %>");
        } else
        {
            super.processEndOfRow(recordlayoutrow);
        }
    }

    public void processEndOfTraversal()
    {
        if(isProcessSubfiles())
        {
            super._scc.addElement("<% } //if (isActiveRecord(" + getRRNVarName() + "))%>");
            super._scc.addElement("<% } // End of for loop of row %>");
            super._scc.addElement("<% row++; %>");
            super._scc.addElement(getStyleForRowInitialization());
            int i = (getSubfileFirstRow() + getSubfileRecordLayout().getSFLPAG()) - 1;
            super._scc.addElement("<% for (currentRow++; currentRow <= " + i + "; " + "currentRow" + "++) { //the variable is incremented initially so it is not the previous row printed %>");
            super._scc.addElement("<TR id=\"l<%=zOrder%>r<%=currentRow%>\" class=\"<%= styleClassForRow %>\"><TD colspan=" + getScreenOrWindowWidth() + ">&nbsp;</TD></TR>");
            super._scc.addElement("<% } //for to print unfilled rows of subfile %>");
            super._scc.addElement("<TR><TD id='" + SubfileControlJSPGenerator.getScrollbarBottomMarkerJavascriptID(super._rn) + "'></TD></TR>");
            super._scc.addElement("<% } // End of 'if' for subfile visible %>");
        } else
        {
            super.processEndOfTraversal();
        }
    }

    protected int getSubfileNumDisplayLines()
    {
        return getSubfileRecordLayout().getNumDisplayLines();
    }

    String getClassTableRowAttribute()
    {
        return "class=\"<%= styleClassForRow %>\"";
    }

    public boolean processBeginOfRow(RecordLayoutRow recordlayoutrow)
    {
        if(!isProcessSubfiles())
        {
            return super.processBeginOfRow(recordlayoutrow);
        } else
        {
            printBeginOfRow(recordlayoutrow.getRowNumber());
            return true;
        }
    }
}
