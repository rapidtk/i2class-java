// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            KeywordParm, CHGINPDFTKeywordNode, AnyNodeWithKeywords, KeywordNodeEnumeration, 
//            KeywordNode, FieldNode

public class CheckAttributes
{

    public boolean isAB()
    {
        return _CHECK_AB;
    }

    public boolean isMF()
    {
        return _CHECK_MF || getChginpdftKeywordNode() != null && getChginpdftKeywordNode().hasMF();
    }

    public boolean isM10()
    {
        return _CHECK_M10;
    }

    public boolean isM11()
    {
        return _CHECK_M11;
    }

    public boolean isM11F()
    {
        return _CHECK_M11F;
    }

    public boolean isVN()
    {
        return _CHECK_VN;
    }

    public boolean isVNE()
    {
        return _CHECK_VNE;
    }

    public boolean isFE()
    {
        return _CHECK_FE || getChginpdftKeywordNode() != null && getChginpdftKeywordNode().hasFE();
    }

    public boolean isLC()
    {
        return _CHECK_LC || _LOWER || getChginpdftKeywordNode() != null && getChginpdftKeywordNode().hasLC();
    }

    public boolean isRB()
    {
        return _CHECK_RB;
    }

    public boolean isRZ()
    {
        return _CHECK_RZ;
    }

    public boolean isRL()
    {
        return _CHECK_RL;
    }

    public boolean isRLTB()
    {
        return _CHECK_RLTB;
    }

    public String getERIndExpr()
    {
        return _CHECK_ER_IndExpr;
    }

    public String getMEIndExpr()
    {
        if(_CHECK_ME_IndExpr != null)
            return _CHECK_ME_IndExpr;
        if(getChginpdftKeywordNode() != null && getChginpdftKeywordNode().hasME())
            return "";
        else
            return null;
    }

    public CheckAttributes(FieldNode fieldnode)
    {
        _CHECK_AB = false;
        _CHECK_MF = false;
        _CHECK_M10 = false;
        _CHECK_M10F = false;
        _CHECK_M11 = false;
        _CHECK_M11F = false;
        _CHECK_VN = false;
        _CHECK_VNE = false;
        _CHECK_FE = false;
        _CHECK_LC = false;
        _CHECK_RB = false;
        _CHECK_RZ = false;
        _CHECK_RL = false;
        _CHECK_RLTB = false;
        _CHECK_ER_IndExpr = null;
        _CHECK_ME_IndExpr = null;
        _chginpdftKeywordNode = null;
        _LOWER = false;
        for(KeywordNodeEnumeration keywordnodeenumeration = fieldnode.getKeywordsOfType(68); keywordnodeenumeration.hasMoreElements();)
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            if(keywordnode != null)
            {
                String s = keywordnode.getIndicatorString();
                if(null == s)
                    s = "";
                Vector vector = keywordnode.getParmsVector();
                if(vector != null && vector.size() > 0)
                {
                    for(int i = 0; i < vector.size(); i++)
                    {
                        KeywordParm keywordparm = (KeywordParm)vector.elementAt(i);
                        int j = keywordparm.getVarParmToken();
                        switch(j)
                        {
                        case 233: 
                            _CHECK_AB = true;
                            break;

                        case 239: 
                            _CHECK_ME_IndExpr = s;
                            break;

                        case 240: 
                            _CHECK_MF = true;
                            break;

                        case 241: 
                            _CHECK_M10 = true;
                            break;

                        case 242: 
                            _CHECK_M10F = true;
                            break;

                        case 243: 
                            _CHECK_M11 = true;
                            break;

                        case 244: 
                            _CHECK_M11F = true;
                            break;

                        case 245: 
                            _CHECK_VN = true;
                            break;

                        case 246: 
                            _CHECK_VNE = true;
                            break;

                        case 234: 
                            _CHECK_ER_IndExpr = s;
                            break;

                        case 235: 
                            _CHECK_FE = true;
                            break;

                        case 236: 
                            _CHECK_LC = true;
                            break;

                        case 237: 
                            _CHECK_RB = true;
                            break;

                        case 238: 
                            _CHECK_RZ = true;
                            break;

                        case 247: 
                            _CHECK_RL = true;
                            break;

                        case 248: 
                            _CHECK_RLTB = true;
                            break;
                        }
                    }

                }
            }
        }

        if(_CHECK_RL)
            _CHECK_RZ = false;
        _chginpdftKeywordNode = (CHGINPDFTKeywordNode)fieldnode.findKeywordById(69);
        _LOWER = fieldnode.findKeywordById(135) != null;
    }

    public CHGINPDFTKeywordNode getChginpdftKeywordNode()
    {
        return _chginpdftKeywordNode;
    }

    public boolean isM10F()
    {
        return _CHECK_M10F;
    }

    private boolean _CHECK_AB;
    private boolean _CHECK_MF;
    private boolean _CHECK_M10;
    private boolean _CHECK_M10F;
    private boolean _CHECK_M11;
    private boolean _CHECK_M11F;
    private boolean _CHECK_VN;
    private boolean _CHECK_VNE;
    private boolean _CHECK_FE;
    private boolean _CHECK_LC;
    private boolean _CHECK_RB;
    private boolean _CHECK_RZ;
    private boolean _CHECK_RL;
    private boolean _CHECK_RLTB;
    private String _CHECK_ER_IndExpr;
    private String _CHECK_ME_IndExpr;
    private CHGINPDFTKeywordNode _chginpdftKeywordNode;
    private boolean _LOWER;
}
