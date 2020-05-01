// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            ColourIndExprPair

public class DisplayAttributeBean
{

    public DisplayAttributeBean()
    {
        _reverseIndExpr = null;
        _highlightIndExpr = null;
        _protectIndExpr = null;
        _blinkIndExpr = null;
        _underlineIndExpr = null;
        _colSeparatorsIndExpr = null;
        _colourIndExprVector = null;
        _fieldName = null;
        _chginpdftKeywordNoUL = false;
        _pfield = null;
        _pfieldIndExpr = null;
    }

    public void addColourIndExpr(String s, String s1)
    {
        if(_colourIndExprVector == null)
            _colourIndExprVector = new Vector();
        _colourIndExprVector.addElement(new ColourIndExprPair(s, s1));
    }

    public String getProtectIndExpr()
    {
        return _protectIndExpr;
    }

    public String getBlinkIndExpr()
    {
        return _blinkIndExpr;
    }

    public Vector getColourIndExprVector()
    {
        return _colourIndExprVector;
    }

    public String getHighlightIndExpr()
    {
        return _highlightIndExpr;
    }

    public String getReverseIndExpr()
    {
        return _reverseIndExpr;
    }

    public String getUnderlineIndExpr()
    {
        return _underlineIndExpr;
    }

    public void setProtectIndExpr(String s)
    {
        _protectIndExpr = s;
    }

    public void setBlinkIndExpr(String s)
    {
        _blinkIndExpr = s;
    }

    public void setColourIndExprVector(Vector vector)
    {
        _colourIndExprVector = vector;
    }

    public void setHighlightIndExpr(String s)
    {
        _highlightIndExpr = s;
    }

    public void setReverseIndExpr(String s)
    {
        _reverseIndExpr = s;
    }

    public void setUnderlineIndExpr(String s)
    {
        _underlineIndExpr = s;
    }

    public DisplayAttributeBean(String s)
    {
        _reverseIndExpr = null;
        _highlightIndExpr = null;
        _protectIndExpr = null;
        _blinkIndExpr = null;
        _underlineIndExpr = null;
        _colSeparatorsIndExpr = null;
        _colourIndExprVector = null;
        _fieldName = null;
        _chginpdftKeywordNoUL = false;
        _pfield = null;
        _pfieldIndExpr = null;
        _fieldName = s;
    }

    public String getFieldName()
    {
        return _fieldName;
    }

    public String getColSeparatorsIndExpr()
    {
        return _colSeparatorsIndExpr;
    }

    public void setColSeparatorsIndExpr(String s)
    {
        _colSeparatorsIndExpr = s;
    }

    public boolean getChginpdftNoUL()
    {
        return _chginpdftKeywordNoUL;
    }

    public void setChginpdftNoUL()
    {
        _chginpdftKeywordNoUL = true;
    }

    public String getPFieldName()
    {
        return _pfield;
    }

    public String getPFieldIndExpr()
    {
        return _pfieldIndExpr;
    }

    public void setPField(String s, String s1)
    {
        _pfield = s1;
        _pfieldIndExpr = s;
    }

    private String _reverseIndExpr;
    private String _highlightIndExpr;
    private String _protectIndExpr;
    private String _blinkIndExpr;
    private String _underlineIndExpr;
    private String _colSeparatorsIndExpr;
    private Vector _colourIndExprVector;
    private String _fieldName;
    private boolean _chginpdftKeywordNoUL;
    private String _pfield;
    private String _pfieldIndExpr;
}
