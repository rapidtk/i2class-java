// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import org.w3c.dom.*;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            AnyNodeWithKeywords, AnyNode, XMLParser

public class HelpspecNode extends AnyNodeWithKeywords
{

    public HelpspecNode(AnyNode anynode)
    {
        super(anynode, 6);
        _type = 0;
        _object = null;
        _library = null;
        _areaType = 0;
        _top = 0;
        _left = 0;
        _bottom = 0;
        _right = 0;
        _field = null;
        _fieldChoice = 0;
        _fieldId = 0;
        _indicator = "";
        _isBoundary = false;
        _isExcluded = false;
    }

    public int getAreaType()
    {
        return _areaType;
    }

    public String getField()
    {
        return _field;
    }

    public int getFieldChoice()
    {
        return _fieldChoice;
    }

    public int getFieldId()
    {
        return _fieldId;
    }

    public String getHelpLib()
    {
        return _library;
    }

    public String getHelpObj()
    {
        return _object;
    }

    public String getIndicatorString()
    {
        return _indicator;
    }

    public String getRectangleAsString()
    {
        return _top + ", " + _left + ", " + _bottom + ", " + _right;
    }

    public int getType()
    {
        return _type;
    }

    public boolean isBoundary()
    {
        return _isBoundary;
    }

    public boolean isExcluded()
    {
        return _isExcluded;
    }

    public boolean needHelpAreaRect()
    {
        return _areaType == 298 || _areaType == 297 || _areaType == 296;
    }

    public void restoreFromXML(XMLParser xmlparser, Node node)
    {
        NamedNodeMap namednodemap = node.getAttributes();
        if(namednodemap != null && namednodemap.getLength() > 0)
            super.restoreAttributesFromXML(xmlparser, namednodemap);
        Object obj = null;
        NodeList nodelist = node.getChildNodes();
        if(nodelist != null && nodelist.getLength() > 0)
            super.restoreChildrenFromXML(xmlparser, nodelist);
        else
            return;
    }

    public void setAreaType(int i)
    {
        _areaType = i;
    }

    public void setField(String s)
    {
        _field = s;
    }

    public void setFieldChoice(int i)
    {
        _fieldChoice = i;
    }

    public void setFieldId(int i)
    {
        _fieldId = i;
    }

    public void setHelpObjLib(String s)
    {
        setHelpObjLib(null, s);
    }

    public void setHelpObjLib(String s, String s1)
    {
        _object = s1;
        _library = s;
    }

    public void setIndicatorString(String s)
    {
        _indicator = s;
    }

    public void setIsBoundary(boolean flag)
    {
        _isBoundary = flag;
    }

    public void setIsExcluded(boolean flag)
    {
        _isExcluded = flag;
    }

    public void setRectangle(int i, int j, int k, int l)
    {
        _top = i;
        _left = j;
        _bottom = k;
        _right = l;
    }

    public void setType(int i)
    {
        _type = i;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2001");
    protected static final String XML_TAG_HELPSPEC = "helpspec";
    public static final int HLPPNLGRP = 116;
    public static final int HLPRCD = 117;
    private int _type;
    private String _object;
    private String _library;
    private int _areaType;
    private int _top;
    private int _left;
    private int _bottom;
    private int _right;
    private String _field;
    private int _fieldChoice;
    private int _fieldId;
    private String _indicator;
    private boolean _isBoundary;
    private boolean _isExcluded;

}
