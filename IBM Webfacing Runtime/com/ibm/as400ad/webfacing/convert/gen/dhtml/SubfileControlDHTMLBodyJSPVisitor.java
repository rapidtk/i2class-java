// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.dhtml;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.convert.model.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.dhtml:
//            AbstractSubfileControlDHTMLBodyJSPVisitor, DHTMLBodyJSPVisitor, SubfileControlJSPGenerator, DHTMLSourceCodeCollection

public class SubfileControlDHTMLBodyJSPVisitor extends AbstractSubfileControlDHTMLBodyJSPVisitor
    implements ENUM_KeywordIdentifiers
{

    public SubfileControlDHTMLBodyJSPVisitor()
    {
    }

    public SubfileControlDHTMLBodyJSPVisitor(SubfileControlRecordLayout subfilecontrolrecordlayout, DHTMLSourceCodeCollection dhtmlsourcecodecollection)
    {
        super(subfilecontrolrecordlayout, dhtmlsourcecodecollection);
    }

    String getClassTableRowAttribute()
    {
        return "class=\"<%= (styleToFinishSubfile != null) ? styleToFinishSubfile : styleClassForRow %>\"";
    }

    protected int getSubfileNumDisplayLines()
    {
        return getSubfileRecordLayout().getNumDisplayLines();
    }

    protected void printCellForRecordSpace()
    {
        super._scc.addElement("<TD colspan=" + getSubfileRecordWidth() + ">&nbsp;</TD>");
        super._scc.addElement("<% lastCol += " + getSubfileRecordWidth() + "; %>");
    }

    protected void printEmptyRowsForInactiveSubfileRecord()
    {
        if(isSubfileFoldable())
            super._scc.addElement("<% if (" + getBeanName() + ".isSubfileFolded()) { // if the subfile is in folded mode%>");
        for(int i = 1; i <= getSubfileLastRow() - getSubfileFirstRow(); i++)
        {
            printCellForRecordSpace();
            if(getSubfileLastColumn() < getScreenOrWindowWidth())
                super._scc.addElement("<TD colspan=" + Integer.toString(getScreenOrWindowWidth() - getSubfileLastColumn()) + ">&nbsp;</TD>");
            super._scc.addElement("</TR>");
            printBeginOfRow((getSubfileFirstRow() + i) - 1);
        }

        if(isSubfileFoldable())
            super._scc.addElement("<% } //if isSubfileFolded()%>");
        printCellForRecordSpace();
        if(getSFLLIN() > 0)
            printPaddingForSFLLIN();
    }

    protected void printPaddingForSFLLIN()
    {
        String s = getJSPColumnForPositioningID();
        super._scc.addElement("<% int paddingForSFLLIN  = ((" + s + " - 1) + " + getSFLLIN() + " <= " + getScreenOrWindowWidth() + ") ? " + getSFLLIN() + " : " + getScreenOrWindowWidth() + " - (" + s + " - 1); %>");
        super._scc.addElement("<% if (paddingForSFLLIN > 0) { %>");
        super._scc.addElement("<TD colspan=" + DHTMLBodyJSPVisitor.wrapJSPExpr("paddingForSFLLIN") + ">&nbsp;</TD>");
        super._scc.addElement("<% } //if (paddingForSFLLIN > 0) %>");
    }

    public boolean processBeginOfRow(RecordLayoutRow recordlayoutrow)
    {
        int i = recordlayoutrow.getRowNumber();
        if(i != getSubfileFirstRow())
            return super.processBeginOfRow(recordlayoutrow);
        else
            return true;
    }

    public void processBeginOfTraversal()
    {
        super.processBeginOfTraversal();
        if(isProcessSubfiles())
        {
            super._scc.addElement("<%if (" + getBeanName() + ".isSubfileVisible()) {%>");
            printScrollbar();
            int i = getSubfileRecordLayout().getRecordsPerRow();
            int j = getSubfileRecordLayout().getRowsOfRecords();
            super._scc.addElement(getDHMTLRowInitialization());
            super._scc.addElement(getStyleForRowDeclaration());
            super._scc.addElement("<% String styleToFinishSubfile = null; //this is initialized and used if we reach the end of the subfile records %>");
            if(isSubfileFoldable())
            {
                if(i == 1)
                    super._scc.addElement("<% int visibileSubfileSize=" + getBeanName() + "." + "getVisibleRecordSize()" + "; %>");
                else
                    super._scc.addElement("<% int visibileSubfileSize=" + getBeanName() + "." + "getVisibleRecordSize()" + "/" + i + "; %>");
                super._scc.addElement("<% for (int row=1; row<=visibileSubfileSize; row++) { %>");
            } else
            {
                super._scc.addElement("<% for (int row=1; row<=" + j + "; " + "row" + "++) { %>");
            }
            super._scc.addElement(getStyleForRowInitialization());
            printBeginOfRow(getSubfileFirstRow());
            if(i == 1)
            {
                super._scc.addElement("<% int " + getRRNVarName() + "=" + "row" + ";  { %> ");
                super._scc.addElement("<% int col=1; %>");
            } else
            {
                super._scc.addElement("<% for (int col=1; col <= " + i + ";" + "col" + "++ ) { %>");
                super._scc.addElement("<% int " + getRRNVarName() + "= (" + "col" + "-1)*" + j + "+" + "row" + "; %>");
            }
            super._scc.addElement("<% if (styleToFinishSubfile == null && col == 1 && " + getBeanName() + ".isRecordPastEndOfSubfile(" + getRRNVarName() + ")) { " + "styleToFinishSubfile" + " = " + "styleClassForRow" + "; } %>");
            super._scc.addElement("<% lastCol = 0; %>");
            super._lastCol = 0;
            super._scc.addElement(getIsActiveRecordIfStatement());
        }
    }

    public void processEndOfRow(RecordLayoutRow recordlayoutrow)
    {
        if(isProcessSubfiles())
        {
            if(isSubfileFoldable() && recordlayoutrow.getRowNumber() == getSubfileFirstRow())
            {
                super._scc.addElement("<% if (!" + getBeanName() + ".isSubfileFolded()) { // if the subfile is not in folded mode%>");
                super._scc.addElement("<% if (lastCol < " + getSubfileRecordWidth() + ") { %>");
                super._scc.addElement("<TD colspan=<%= " + getSubfileRecordWidth() + " - lastCol%>>&nbsp;</TD>");
                super._scc.addElement("<% } %>");
                super._scc.addElement("<% } else { //if the subfile is in folded mode %>");
            }
            if(recordlayoutrow.getRowNumber() != getSubfileLastRow())
            {
                super._scc.addElement("<% if (lastCol + " + Integer.toString(getSubfileFirstColumn() - 1) + " < " + getScreenOrWindowWidth() + ") { %>");
                super._scc.addElement("<TD colspan=<%= " + getScreenOrWindowWidth() + " - (lastCol + " + Integer.toString(getSubfileFirstColumn() - 1) + ")%>>&nbsp;</TD>");
                super._scc.addElement("<% } %>");
                super._scc.addElement("</TR>");
            } else
            if(getSFLLIN() > 0)
                printPaddingForSFLLIN();
        } else
        {
            super.processEndOfRow(recordlayoutrow);
        }
    }

    public void processEndOfTraversal()
    {
        if(isProcessSubfiles())
        {
            if(getSFLLIN() <= 0)
            {
                super._scc.addElement("<% if (" + getSubfileRecordWidth() + "-lastCol > 0) { %>");
                super._scc.addElement("<TD colspan=<%=" + getSubfileRecordWidth() + "-lastCol%>>&nbsp;</TD>");
                super._scc.addElement("<% } %>");
            }
            if(isSubfileFoldable())
                super._scc.addElement("<% } // End of 'if' for SFLFOLD  %>");
            super._scc.addElement("<% } else { // else for if (isActiveRecords(rrn))%>");
            printEmptyRowsForInactiveSubfileRecord();
            super._scc.addElement("<% } // End of 'if' for Active record %> ");
            super._scc.addElement("<% } // End of 'for' loop for col or 'block' for rrn %>");
            int i = getScreenOrWindowWidth();
            int j = getSubfileRecordLayout().getLastColumnOnScreen();
            if(j < getScreenOrWindowWidth())
                super._scc.addElement("<TD colspan=" + Integer.toString(getScreenOrWindowWidth() - j) + ">&nbsp;</TD>");
            super._scc.addElement("</TR>");
            super._scc.addElement("<% } // End of for loop of row %>");
            super._scc.addElement("<TR><TD id='" + SubfileControlJSPGenerator.getScrollbarBottomMarkerJavascriptID(super._rn) + "'></TD></TR>");
            String s = "rrn";
            super._scc.addElement("<% for (int " + s + "=1; " + s + " <= " + getSubfileRecordLayout().getRRNLoopBound() + "; " + s + "++ ) { %>");
            super._scc.addAll(processScriptableInvisibleFields());
            super._scc.addElement("<% } %>");
            super._scc.addElement("<% } // End of 'if' for subfile visible %>");
        } else
        {
            super.processEndOfTraversal();
        }
    }

    public static final String VISIBLE_SUBFILE_VARNAME = "visibileSubfileSize";
    public static final String STYLE_TO_FINISH_SUBFILE_VARNAME = "styleToFinishSubfile";
    public static final String PADDING_FOR_SFLLIN_VARNAME = "paddingForSFLLIN";
}
