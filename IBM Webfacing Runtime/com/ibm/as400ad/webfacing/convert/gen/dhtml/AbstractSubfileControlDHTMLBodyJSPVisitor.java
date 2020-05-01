// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.dhtml;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.dom.AnyNode;
import com.ibm.as400ad.code400.dom.RecordNode;
import com.ibm.as400ad.webfacing.convert.IFieldOutput;
import com.ibm.as400ad.webfacing.convert.model.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.dhtml:
//            DHTMLBodyJSPVisitor, SubfileControlJSPGenerator, DHTMLSourceCodeCollection

public abstract class AbstractSubfileControlDHTMLBodyJSPVisitor extends DHTMLBodyJSPVisitor
{

    public AbstractSubfileControlDHTMLBodyJSPVisitor()
    {
        _isProcessSubfiles = false;
    }

    public AbstractSubfileControlDHTMLBodyJSPVisitor(SubfileControlRecordLayout subfilecontrolrecordlayout, DHTMLSourceCodeCollection dhtmlsourcecodecollection)
    {
        super(subfilecontrolrecordlayout, dhtmlsourcecodecollection);
        _isProcessSubfiles = false;
        _sfrl = subfilecontrolrecordlayout.getSubfileRecordLayout();
        _sfrn = _sfrl.getRecordNode();
        _info = _sfrl.subfileInfo;
        _isControlBeforeSubfiles = subfilecontrolrecordlayout.isSFLDSPCTLSpecified() && subfilecontrolrecordlayout.getLastRow() < _sfrl.getFirstRow();
    }

    abstract String getClassTableRowAttribute();

    int getColumn(FieldOnRow fieldonrow)
    {
        if(!isProcessSubfiles())
            return fieldonrow.getElementColumn();
        else
            return (fieldonrow.getElementColumn() - getSubfileFirstColumn()) + 1;
    }

    protected SubfileControlRecordLayout getControlRecordLayout()
    {
        return (SubfileControlRecordLayout)super._rl;
    }

    protected String getDHMTLRowInitialization()
    {
        return "<% int currentRow=" + Integer.toString(getSubfileFirstRow() - 1) + "; %>";
    }

    public IFieldOutput getFieldOutput(FieldOnRow fieldonrow)
    {
        if(isProcessSubfiles())
        {
            IFieldOutput ifieldoutput = fieldonrow.getFieldOutput();
            SubfileFieldOutputDecorator subfilefieldoutputdecorator = new SubfileFieldOutputDecorator(ifieldoutput, getSubfileFirstColumn(), getSubfileRecordWidth());
            subfilefieldoutputdecorator.setColumnOffset(getSubfileFirstColumn());
            subfilefieldoutputdecorator.setSFLLIN(getSFLLIN());
            return subfilefieldoutputdecorator;
        } else
        {
            return fieldonrow.getFieldOutput();
        }
    }

    protected String getHTMLTableRowAttributes(int i)
    {
        if(isProcessSubfiles())
            return " id=\"l<%=zOrder%>r<%=currentRow+1" + getSLNOVAROffsetStr() + "%>\" " + getClassTableRowAttribute();
        else
            return super.getHTMLTableRowAttributes(i);
    }

    protected String getIsActiveRecordIfStatement()
    {
        return "<% if( " + getBeanName() + ".isActiveRecord(" + getRRNVarName() + ")) { %>";
    }

    protected String getJSPColumnForPositioningID()
    {
        if(getSFLLIN() == -1)
            return getSubfileRecordWidth() + "*(" + "col" + "-1)+" + Integer.toString(getSubfileFirstColumn()) + "+lastCol";
        else
            return "(" + getSubfileRecordWidth() + "+" + getSFLLIN() + ")*(" + "col" + "-1)+" + Integer.toString(getSubfileFirstColumn()) + "+lastCol";
    }

    public RecordLayout getRecordLayout()
    {
        if(isProcessSubfiles())
            return _sfrl;
        else
            return super._rl;
    }

    protected String getRRNVarName()
    {
        return "rrn";
    }

    protected int getSFLLIN()
    {
        return getSubfileRecordLayout().getSFLLIN();
    }

    protected String getStyleForRowDeclaration()
    {
        return "<% String styleClassForRow; %>";
    }

    protected String getStyleForRowInitialization()
    {
        return "<% styleClassForRow = row % 2 == 1 ? \"subfileRecord1\" : \"subfileRecord2\"; %>";
    }

    protected int getSubfileFirstColumn()
    {
        return getSubfileRecordLayout().getFirstColumn();
    }

    protected int getSubfileFirstRow()
    {
        return getSubfileRecordLayout().getFirstRow();
    }

    protected SubfileInfo getSubfileInfo()
    {
        return _info;
    }

    protected int getSubfileLastColumn()
    {
        return getSubfileRecordLayout().getLastColumn();
    }

    protected int getSubfileLastRow()
    {
        return getSubfileRecordLayout().getLastRow();
    }

    protected abstract int getSubfileNumDisplayLines();

    protected SubfileRecordLayout getSubfileRecordLayout()
    {
        return _sfrl;
    }

    protected int getSubfileRecordWidth()
    {
        return getSubfileRecordLayout().getWidth();
    }

    public boolean isControlBeforeSubfiles()
    {
        return _isControlBeforeSubfiles;
    }

    protected boolean isProcessSubfiles()
    {
        return _isProcessSubfiles;
    }

    protected boolean isSubfileFoldable()
    {
        return super._rn.isSubfileFoldable();
    }

    protected void printBeginOfRow(int i)
    {
        super.printBeginOfRow(i);
        if(isProcessSubfiles())
        {
            super._scc.addElement("<% currentRow++; %>");
            if(getSubfileFirstColumn() > 1)
                super._scc.addElement("<TD colspan=" + Integer.toString(getSubfileFirstColumn() - 1) + ">&nbsp;</TD>");
        }
    }

    protected void printEmptyRow(int i)
    {
        if(isProcessSubfiles())
        {
            super._scc.addElement("<TR" + getHTMLTableRowAttributes(i) + ">");
            super._scc.addElement("<% currentRow++; %>");
            super._scc.addElement("<TD colspan=" + getScreenOrWindowWidth() + ">&nbsp;</TD>");
            super._scc.addElement("</TR>");
        } else
        {
            super.printEmptyRow(i);
        }
    }

    protected void printScrollbar()
    {
        super._scc.addElement("<TR>");
        String s = getBeanName() + "$" + "Scrollbar";
        super._scc.addElement("<jsp:useBean id='" + s + "' scope='request' type=\"com.ibm.as400ad.webfacing.runtime.dhtmlview.IScrollbarBean\" />");
        super._scc.addElement("<% " + s + ".setScrollbarJavascriptID(" + super._rn.getWebName() + ".getDisplayZIndex(),\"" + SubfileControlJSPGenerator.getScrollbarJavascriptIDWithoutPrefix(super._rn) + "\");%>");
        super._scc.addElement("<TD colspan='" + getScreenOrWindowWidth() + "' height='0' style='line-height:0%; font-size:0pt'>");
        super._scc.addElement("</TD>");
        int i = getSubfileNumDisplayLines() + 1;
        super._scc.addElement("<TD id='" + SubfileControlJSPGenerator.getScrollbarTopMarkerJavascriptID(super._rn) + "' colspan=1 rowspan=" + i + " cellspacing='0' cellpadding='0'>");
        super._scc.addElement("<!-- begin of scrollbar --->");
        super._scc.addElement("<%=" + s + ".getHTMLSource()%>");
        super._scc.addElement("<!-- end of scrollbar --->");
        super._scc.addElement("</TD>");
        super._scc.addElement("</TR>" + super._scc.getNewline());
    }

    public void processBeginOfSubfiles()
    {
        if(isControlBeforeSubfiles() && getControlRecordLayout().getLastRow() > 0)
        {
            for(int i = getControlRecordLayout().getLastRow(); i < getSubfileFirstRow() - 1; i++)
                printEmptyRow(i + 1);

        }
        setIsProcessSubfiles(true);
    }

    public void processEndOfSubfiles()
    {
        setIsProcessSubfiles(false);
        if(!isControlBeforeSubfiles())
        {
            for(int i = getSubfileFirstRow() + getSubfileNumDisplayLines(); i <= getControlRecordLayout().getFirstRow() - 1; i++)
                printEmptyRow(i);

        }
    }

    protected void setIsProcessSubfiles(boolean flag)
    {
        _isProcessSubfiles = flag;
    }

    private boolean _isProcessSubfiles;
    private SubfileRecordLayout _sfrl;
    private RecordNode _sfrn;
    private SubfileInfo _info;
    private boolean _isControlBeforeSubfiles;
    public static final String ROW_VARNAME = "row";
    public static final String COL_VARNAME = "col";
    public static final String DHTMLROW_VARNAME = "currentRow";
    public static final String STYLE_FOR_ROW_VARNAME = "styleClassForRow";
    public static final String SUBFILE_STYLE_1_CONSTANT = "subfileRecord1";
    public static final String SUBFILE_STYLE_2_CONSTANT = "subfileRecord2";
}
