// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.DSPSIZConstants;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.convert.*;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            ConstantFieldOutput, FieldOutput, OutputFieldOutput, FieldOutputEnumeration, 
//            FieldOnRow, NamedFieldOutput, FieldArea, RecordLayoutRow, 
//            FieldVisibility

public class RecordLayout
    implements IRecordLayout
{
    private class CLRLWindowTitleDefinition
    {

        public boolean isConstantAsTitle()
        {
            return _constantAsTitle;
        }

        public String getTitle()
        {
            return _text;
        }

        public String getIndicatorExpression()
        {
            return _indicatorExpression;
        }

        private boolean _constantAsTitle;
        private String _text;
        private String _indicatorExpression;

        public CLRLWindowTitleDefinition(boolean flag, String s, String s1)
        {
            _constantAsTitle = flag;
            _text = s;
            _indicatorExpression = s1;
        }
    }


    public RecordLayout(RecordNode recordnode, IConversionFactory iconversionfactory)
    {
        _allFields = null;
        _fieldsOnRows = null;
        _firstColumn = -1;
        _firstRow = -1;
        _hasKeyLabelDetected = false;
        _lastColumn = -1;
        _lastRow = -1;
        _rn = null;
        _rows = null;
        _CLRLWindowTitles = new ArrayList();
        _DSPSIZIndex = 0;
        _fieldVisDefList = null;
        setDisplaySizeIndex(recordnode);
        int i = 1;
        _rows = new ArrayList(i);
        _rows.add(null);
        _rn = recordnode;
        _allFields = new ArrayList();
        init();
        addAllFieldsToLayout(iconversionfactory);
        prepare();
        initFieldVisDefList();
    }

    private void addAllFieldsToLayout(IConversionFactory iconversionfactory)
    {
        FieldNodeEnumeration fieldnodeenumeration = _rn.getFields();
        ConversionLogger conversionlogger = new ConversionLogger();
        while(null != fieldnodeenumeration && fieldnodeenumeration.hasMoreElements()) 
        {
            com.ibm.as400ad.code400.dom.FieldNode fieldnode = fieldnodeenumeration.nextField();
            if(null != fieldnode)
            {
                conversionlogger.checkKeywords(fieldnode);
                IFieldOutput ifieldoutput = iconversionfactory.getFieldOutput(fieldnode);
                if(null != ifieldoutput)
                {
                    ifieldoutput.setRecordLayout(this);
                    ifieldoutput.setDisplaySizeIndex(getDisplaySizeIndex());
                    addField(ifieldoutput);
                    if(!_hasKeyLabelDetected && ifieldoutput.hasKeyLabelDetected())
                    {
                        _hasKeyLabelDetected = true;
                        _rn.logEvent(40);
                    }
                }
            }
        }
    }

    protected void addField(IFieldOutput ifieldoutput)
    {
        if(ifieldoutput.getRow() > 0 && ifieldoutput.getRow() <= DSPSIZConstants.getScreenHeight(getDisplaySizeIndex()) && ifieldoutput.getColumn() <= DSPSIZConstants.getScreenWidth(getDisplaySizeIndex()))
            addFieldAt(ifieldoutput.getRow(), ifieldoutput.getColumn(), ifieldoutput);
        _allFields.add(ifieldoutput);
        if(ifieldoutput.hasOutOfFlowHTML())
        {
            if(_outOfFlowFields == null)
                _outOfFlowFields = new ArrayList();
            _outOfFlowFields.add(ifieldoutput);
        }
        if(ifieldoutput.isScriptableInvisibleField())
        {
            if(_scriptableInvisibleFields == null)
                _scriptableInvisibleFields = new ArrayList();
            addScriptableInvisibleFields(ifieldoutput);
        }
    }

    private void addFieldAt(int i, int j, IFieldOutput ifieldoutput)
    {
        List list = getRowColList(i, j);
        list.add(ifieldoutput);
    }

    protected void addScriptableInvisibleFields(IFieldOutput ifieldoutput)
    {
        _scriptableInvisibleFields.add(ifieldoutput);
    }

    private void checkIfCLRLUsedAsWindow()
    {
        KeywordNodeEnumeration keywordnodeenumeration = _rn.getKeywordsOfType(74);
        if(keywordnodeenumeration.hasMoreElements() && !_rn.isWindow())
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            String s = keywordnode.getParmsAsString();
            if((s.equals("*NO") || !s.equals("*ALL") && !s.equals("*END") && Integer.parseInt(s) <= _lastRow - _firstRow) && _firstRow + 1 < _lastRow && _firstColumn + 1 < _lastColumn)
            {
                String s1 = null;
                int i;
                for(i = _firstRow + 1; i < _lastRow; i++)
                {
                    List list = getRowColList(i, _firstColumn);
                    if(list.isEmpty() || !(list.get(0) instanceof ConstantFieldOutput))
                        break;
                    ConstantFieldOutput constantfieldoutput = (ConstantFieldOutput)list.get(0);
                    List list2 = getRowColList(i, (_lastColumn - constantfieldoutput.getWidth()) + 1);
                    if(list2.isEmpty() || !(list2.get(0) instanceof ConstantFieldOutput))
                        break;
                    ConstantFieldOutput constantfieldoutput1 = (ConstantFieldOutput)list2.get(0);
                    if(constantfieldoutput.getWidth() != constantfieldoutput1.getWidth() || !constantfieldoutput.getFieldText().equals(constantfieldoutput1.getFieldText()))
                        break;
                    if(s1 == null)
                    {
                        s1 = constantfieldoutput.getFieldText();
                        continue;
                    }
                    if(!s1.equals(constantfieldoutput.getFieldText()))
                        break;
                }

                if(i == _lastRow)
                {
                    List list1 = getRowColList(_firstRow, _firstColumn);
                    for(int j = 0; j < list1.size(); j++)
                    {
                        FieldOutput fieldoutput = (FieldOutput)list1.get(j);
                        if(fieldoutput.getWidth() == (_lastColumn - _firstColumn) + 1)
                        {
                            String s4 = fieldoutput.getConditioning();
                            if(s4 == null)
                                s4 = "";
                            if(fieldoutput instanceof ConstantFieldOutput)
                            {
                                String s5 = fieldoutput.getFieldText();
                                char c = s5.charAt(1);
                                boolean flag2 = true;
                                for(int j1 = 2; flag2 && j1 < fieldoutput.getWidth() - 1; j1++)
                                    if(s5.charAt(j1) != c)
                                        flag2 = false;

                                String s2;
                                if(flag2)
                                {
                                    s2 = " ";
                                } else
                                {
                                    s2 = fieldoutput.getFieldText();
                                    if(s2.indexOf('"') >= 0)
                                        s2 = WebfacingConstants.getJavaString(s2);
                                }
                                boolean flag = true;
                                _CLRLWindowTitles.add(new CLRLWindowTitleDefinition(flag, s2, s4));
                            } else
                            if(fieldoutput instanceof OutputFieldOutput)
                            {
                                String s3 = fieldoutput.getFieldName();
                                boolean flag1 = false;
                                _CLRLWindowTitles.add(new CLRLWindowTitleDefinition(flag1, s3, s4));
                            }
                        }
                    }

                    if(!_CLRLWindowTitles.isEmpty())
                    {
                        for(int k = _firstRow + 1; k < _lastRow; k++)
                        {
                            List list3 = getRowColList(k, _firstColumn);
                            int l = (_lastColumn - ((ConstantFieldOutput)list3.get(0)).getWidth()) + 1;
                            if(l > _firstColumn)
                            {
                                List list4 = getRowColList(k, l);
                                list3.remove(0);
                                list4.remove(0);
                                List list5 = getRowList(k);
                                int i1;
                                for(i1 = _firstColumn; i1 <= _lastColumn; i1++)
                                    if(list5.size() > i1 && list5.get(i1) != null && ((List)list5.get(i1)).size() != 0)
                                        break;

                                if(i1 > _lastColumn)
                                    list5.clear();
                                List list6 = getList(_fieldsOnRows, k);
                                List list7 = getList(list6, _firstColumn);
                                List list8 = getList(list6, l);
                                list7.remove(0);
                                list8.remove(0);
                            }
                        }

                        _rn.logEvent(39);
                    }
                }
            }
        }
    }

    public FieldOutputEnumeration getAllFields()
    {
        return new FieldOutputEnumeration(_allFields);
    }

    public String getCLRLWindowTitle(int i)
    {
        return ((CLRLWindowTitleDefinition)_CLRLWindowTitles.get(i)).getTitle();
    }

    public String getCLRLWindowTitleIndicatorExpression(int i)
    {
        return ((CLRLWindowTitleDefinition)_CLRLWindowTitles.get(i)).getIndicatorExpression();
    }

    public FieldOutputEnumeration getDisplayableFields()
    {
        ArrayList arraylist = new ArrayList();
        int i = _rows.size();
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        for(int j = 1; j < i; j++)
            if(!isEmpty(j))
            {
                List list = getRowList(j);
                int l = list.size();
                for(int k = 1; k < l; k++)
                    if(!isEmpty(j, k))
                    {
                        List list1 = getRowColList(j, k);
                        for(Iterator iterator = list1.iterator(); iterator.hasNext(); arraylist.add(iterator.next()));
                    }

            }

        ((ArrayList)arraylist).trimToSize();
        if(0 == arraylist.size())
            arraylist = null;
        return new FieldOutputEnumeration(arraylist);
    }

    public int getDisplaySizeIndex()
    {
        return _DSPSIZIndex;
    }

    private IFieldOutput getFieldOutput(int i, int j, int k)
    {
        IFieldOutput ifieldoutput = null;
        if(!isEmpty(i, j, k))
            ifieldoutput = (IFieldOutput)getRowColList(i, j).get(k);
        return ifieldoutput;
    }

    protected IFieldOutput getFieldOutput(String s)
    {
        Object obj = null;
        if(s != null)
        {
            for(int i = 0; i < _allFields.size(); i++)
            {
                IFieldOutput ifieldoutput = (IFieldOutput)_allFields.get(i);
                if(ifieldoutput.getFieldNode().getName().equals(s))
                    return ifieldoutput;
            }

        }
        return null;
    }

    protected FieldOutputEnumeration getFieldsForRow(int i)
    {
        ArrayList arraylist = new ArrayList();
        int j = i;
        if(!isEmpty(j))
        {
            List list = getRowList(j);
            int k = list.size();
            for(int l = 1; l < k; l++)
                if(!isEmpty(j, l))
                {
                    List list1 = getRowColList(j, l);
                    for(Iterator iterator = list1.iterator(); iterator.hasNext(); arraylist.add(iterator.next()));
                }

        }
        ((ArrayList)arraylist).trimToSize();
        if(0 == arraylist.size())
            arraylist = null;
        return new FieldOutputEnumeration(arraylist);
    }

    protected List getFieldsOnRow(int i)
    {
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = null;
        if(i < _fieldsOnRows.size() && i > 0)
            arraylist1 = (ArrayList)_fieldsOnRows.get(i);
        if(arraylist1 != null)
        {
            for(Iterator iterator = arraylist1.iterator(); iterator.hasNext();)
            {
                ArrayList arraylist2 = (ArrayList)iterator.next();
                if(arraylist2 != null)
                {
                    for(Iterator iterator1 = arraylist2.iterator(); iterator1.hasNext();)
                    {
                        Object obj = iterator1.next();
                        if(obj != null)
                            arraylist.add(obj);
                    }

                }
            }

        }
        return arraylist;
    }

    public ArrayList getFieldVisDefList()
    {
        return _fieldVisDefList;
    }

    public final int getFirstColumn()
    {
        return _firstColumn;
    }

    public final int getFirstRow()
    {
        return _firstRow;
    }

    public int getHeight()
    {
        return (getLastRow() - getFirstRow()) + 1;
    }

    public final int getLastColumn()
    {
        return _lastColumn;
    }

    public final int getLastRow()
    {
        return _lastRow;
    }

    private final List getList(List list, int i)
    {
        ArrayList arraylist = null;
        for(int j = list.size(); j <= i; j++)
            list.add(null);

        arraylist = (ArrayList)list.get(i);
        if(arraylist == null)
        {
            arraylist = new ArrayList();
            list.set(i, arraylist);
        }
        return arraylist;
    }

    public int getNumberOfCLRLWindowTitles()
    {
        return _CLRLWindowTitles.size();
    }

    public Iterator getOutOfFlowHTMLFields()
    {
        if(_outOfFlowFields != null)
            return _outOfFlowFields.iterator();
        else
            return (new ArrayList()).iterator();
    }

    public RecordNode getRecordNode()
    {
        return _rn;
    }

    private final List getRowColList(int i, int j)
    {
        List list = getRowList(i);
        List list1 = getList(list, j);
        return list1;
    }

    private final List getRowList(int i)
    {
        return getList(_rows, i);
    }

    public int getScreenOrWindowWidth()
    {
        RecordNode recordnode = getRecordNode();
        if(recordnode.isWindow())
            return recordnode.getWindowWidth(getDisplaySizeIndex());
        else
            return getScreenWidth();
    }

    public int getScreenWidth()
    {
        return DSPSIZConstants.getScreenWidth(getDisplaySizeIndex());
    }

    public Iterator getScriptableInvisibleFields()
    {
        if(_scriptableInvisibleFields != null)
            return _scriptableInvisibleFields.iterator();
        else
            return (new ArrayList()).iterator();
    }

    public String getScriptFromWebSetting()
    {
        try
        {
            WebSettingsNodeEnumeration websettingsnodeenumeration = getRecordNode().getWebSettings();
            if(websettingsnodeenumeration != null)
                while(websettingsnodeenumeration.hasMoreElements()) 
                {
                    WebSettingsNode websettingsnode = websettingsnodeenumeration.nextWebSettings();
                    if(websettingsnode.getType() == 18)
                    {
                        String s = websettingsnode.getValue().trim();
                        char c = s.charAt(0);
                        boolean flag = false;
                        if(c == '1')
                        {
                            s = s.substring(1).trim();
                            flag = true;
                        }
                        s = WebfacingConstants.replaceInapplicableCharacters(s);
                        if(!s.equals("") && s.length() >= 4)
                        {
                            for(int i = -1; i < s.length() - 4 && s.indexOf("&", i + 1) >= 0;)
                            {
                                i = s.indexOf("&", i + 1);
                                int j = s.indexOf("{", i + 1);
                                int k = s.indexOf("}", i + 1);
                                if(j < k - 1)
                                {
                                    String s2 = null;
                                    String s1;
                                    if(flag)
                                    {
                                        StringTokenizer stringtokenizer = new StringTokenizer(s.substring(j + 1, k), ".");
                                        int l = stringtokenizer.countTokens();
                                        if(l == 2)
                                        {
                                            s2 = stringtokenizer.nextToken();
                                            s1 = stringtokenizer.nextToken();
                                        } else
                                        {
                                            s1 = stringtokenizer.nextToken();
                                        }
                                    } else
                                    {
                                        s1 = s.substring(j + 1, k);
                                        if(j - i > 1)
                                            s2 = s.substring(i + 1, j);
                                    }
                                    if((s1 == null || s1.indexOf(" ") < 0) && (s2 == null || s2.indexOf(" ") < 0))
                                        s = replaceFieldId(s, i, s1, s2, flag);
                                }
                            }

                            return s;
                        }
                    }
                }
        }
        catch(Throwable throwable)
        {
            if(!(throwable instanceof IndexOutOfBoundsException))
                ExportHandler.err(1, throwable, "Error in RecordLayout.getScriptFromWebSetting()");
            return "";
        }
        return "";
    }

    public String getViewInterface()
    {
        return "com.ibm.as400ad.webfacing.runtime.view.IDisplayRecord";
    }

    public int getWidth()
    {
        return (getLastColumn() - getFirstColumn()) + 1;
    }

    public void init()
    {
    }

    private void initFieldVisDefList()
    {
        _fieldVisDefList = new ArrayList();
        for(int i = getFirstRow(); i <= getLastRow(); i++)
        {
            RecordLayoutRow recordlayoutrow = getRecordLayoutRow(i);
            if(recordlayoutrow.hasFieldsOnRow())
            {
                for(Iterator iterator = recordlayoutrow.getFieldsOnRow(); iterator.hasNext();)
                {
                    FieldOnRow fieldonrow = (FieldOnRow)iterator.next();
                    if(fieldonrow.isFirstFieldRow())
                    {
                        FieldVisibility fieldvisibility = fieldonrow.getFieldOutput().getFieldVisibility();
                        fieldvisibility.buildVisDef(_fieldVisDefList, fieldonrow.getFieldOutput() instanceof NamedFieldOutput);
                    }
                }

            }
        }

    }

    public boolean isCLRLWindow()
    {
        return !_CLRLWindowTitles.isEmpty();
    }

    public boolean isCLRLWindowTitleConstant(int i)
    {
        return ((CLRLWindowTitleDefinition)_CLRLWindowTitles.get(i)).isConstantAsTitle();
    }

    protected final boolean isEmpty(int i)
    {
        boolean flag = true;
        if(null != _rows && _rows.size() > i && _rows.get(i) != null && ((List)_rows.get(i)).size() != 0)
            flag = false;
        return flag;
    }

    private final boolean isEmpty(int i, int j)
    {
        boolean flag = true;
        if(!isEmpty(i))
        {
            List list = getRowList(i);
            if(list.size() > j && list.get(j) != null && ((List)list.get(j)).size() != 0)
                flag = false;
        }
        return flag;
    }

    private final boolean isEmpty(int i, int j, int k)
    {
        boolean flag = true;
        if(!isEmpty(i, j))
        {
            List list = getRowColList(i, j);
            if(list.size() > k && list.get(k) != null)
                flag = false;
        }
        return flag;
    }

    protected void prepare()
    {
        if(_rn != null)
        {
            _fieldsOnRows = new ArrayList();
            ArrayList arraylist = new ArrayList();
            for(int i = 1; i <= DSPSIZConstants.getScreenHeight(getDisplaySizeIndex()); i++)
            {
                for(Iterator iterator = arraylist.iterator(); iterator.hasNext();)
                {
                    FieldArea fieldarea = (FieldArea)iterator.next();
                    if(fieldarea.getLastRow() >= i)
                        createFieldOnRow(fieldarea, i);
                    else
                    if(fieldarea.getEndAttrRow() < i)
                        iterator.remove();
                }

                RecordLayoutRow recordlayoutrow = getRecordLayoutRow(i);
                if(!recordlayoutrow.isEmpty())
                {
                    FieldArea fieldarea1;
                    for(FieldOutputEnumeration fieldoutputenumeration = recordlayoutrow.getFields(); fieldoutputenumeration.hasMoreElements(); arraylist.add(fieldarea1))
                    {
                        IFieldOutput ifieldoutput = (IFieldOutput)fieldoutputenumeration.nextElement();
                        fieldarea1 = ifieldoutput.getFieldArea();
                        updateRecordDimensions(fieldarea1);
                        createFieldOnRow(fieldarea1, i);
                        for(Iterator iterator1 = arraylist.iterator(); iterator1.hasNext();)
                        {
                            FieldArea fieldarea2 = (FieldArea)iterator1.next();
                            if(fieldarea2.overlaps(fieldarea1))
                                ((FieldOutput)fieldarea1.getFieldOutput()).getFieldVisibility().addOverlap(fieldarea2.getFieldOutput().getFieldVisibility());
                        }

                    }

                }
            }

            checkIfCLRLUsedAsWindow();
        }
    }

    protected String replaceFieldId(String s, int i, String s1, String s2, boolean flag)
    {
        IFieldOutput ifieldoutput = getFieldOutput(s1);
        if(ifieldoutput != null)
            return WebfacingConstants.replaceSubstring(s, "&{" + s1 + "}", ifieldoutput.getClientScriptLocation() + ifieldoutput.getTagId());
        else
            return s;
    }

    protected void setDisplaySizeIndex(RecordNode recordnode)
    {
        try
        {
            FileNode filenode = (FileNode)recordnode.getParent();
            if(recordnode.hasDSPSIZWebSetting())
            {
                if(filenode.isDspSizConditioned())
                {
                    _DSPSIZIndex = filenode.getSecondaryDisplaySize();
                } else
                {
                    _DSPSIZIndex = filenode.getPrimaryDisplaySize();
                    ExportHandler.err(2, "Error in RecordLayout.setDSPSIZIndex(RecordNode) : DSPSIZ Web Setting was set, but there was no secondary display size.");
                }
            } else
            {
                DSPMODKeywordNode dspmodkeywordnode = (DSPMODKeywordNode)recordnode.findKeywordById(89);
                if(dspmodkeywordnode != null)
                {
                    if(dspmodkeywordnode.getIndicatorString() == null)
                    {
                        _DSPSIZIndex = dspmodkeywordnode.getDisplaySize();
                    } else
                    {
                        recordnode.logEvent(37);
                        _DSPSIZIndex = filenode.getPrimaryDisplaySize();
                    }
                } else
                {
                    _DSPSIZIndex = filenode.getPrimaryDisplaySize();
                }
            }
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, throwable, "Error in RecordLayout.setDSPSIZIndex(RecordNode)");
        }
    }

    private void createFieldOnRow(FieldArea fieldarea, int i)
    {
        List list = getList(_fieldsOnRows, i);
        List list1 = getList(list, fieldarea.getColumn(i));
        FieldOnRow fieldonrow = new FieldOnRow(fieldarea, i);
        fieldarea.setCurrentFieldOnRow(fieldonrow);
        list1.add(fieldonrow);
    }

    public RecordLayoutRow getRecordLayoutRow(int i)
    {
        return new RecordLayoutRow(this, i);
    }

    private void updateRecordDimensions(FieldArea fieldarea)
    {
        int i = fieldarea.getBeginAttrRow();
        if(_firstRow < 0 || i < _firstRow)
            _firstRow = i;
        i = fieldarea.getLastRow();
        if(i > _lastRow)
            _lastRow = i;
        int j = fieldarea.getLeftmostColumn();
        if(_firstColumn < 0 || j < _firstColumn)
            _firstColumn = j;
        j += fieldarea.getWidth() - 1;
        if(j > _lastColumn)
            _lastColumn = j;
    }

    public boolean recordHasSLNOVAR()
    {
        KeywordNodeEnumeration keywordnodeenumeration = getRecordNode().getKeywordsOfType(209);
        if(keywordnodeenumeration.hasMoreElements())
            return keywordnodeenumeration.nextKeyword().getParmsAsString().indexOf("*VAR") >= 0;
        else
            return false;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000");
    private List _allFields;
    private List _fieldsOnRows;
    private int _firstColumn;
    private int _firstRow;
    private boolean _hasKeyLabelDetected;
    private int _lastColumn;
    private int _lastRow;
    private RecordNode _rn;
    private List _rows;
    protected List _outOfFlowFields;
    protected List _scriptableInvisibleFields;
    private List _CLRLWindowTitles;
    private int _DSPSIZIndex;
    private ArrayList _fieldVisDefList;

}
