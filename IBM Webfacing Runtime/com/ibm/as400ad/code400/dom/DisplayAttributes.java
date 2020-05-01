// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            KeywordParm, CHGINPDFTKeywordNode, AnyNodeWithKeywords, KeywordNodeEnumeration, 
//            KeywordNode, FieldNode

public class DisplayAttributes
{

    public DisplayAttributes(FieldNode fieldnode)
    {
        _DSPATR_BL_IndExpr = null;
        _DSPATR_CS_IndExpr = null;
        _DSPATR_HI_IndExpr = null;
        _DSPATR_ND_IndExpr = null;
        _DSPATR_RI_IndExpr = null;
        _DSPATR_MDT_IndExpr = null;
        _DSPATR_OID_IndExpr = null;
        _DSPATR_PR_IndExpr = null;
        _DSPATR_SP_IndExpr = null;
        _DSPATR_PC_IndExpr = null;
        _DSPATR_UL_IndExpr = null;
        _DSPATR_PFIELD_Name = null;
        _DSPATR_PFIELD_IndExpr = null;
        _chginpdftKeywordNode = null;
        fieldNode = null;
        initialized = false;
        fieldNode = fieldnode;
    }

    public String getBLIndExpr()
    {
        initialize();
        if(_DSPATR_BL_IndExpr != null)
            return _DSPATR_BL_IndExpr;
        if(getChginpdftKeywordNode() != null && getChginpdftKeywordNode().hasBL())
            return "";
        else
            return null;
    }

    public CHGINPDFTKeywordNode getChginpdftKeywordNode()
    {
        initialize();
        return _chginpdftKeywordNode;
    }

    public String getCSIndExpr()
    {
        initialize();
        if(_DSPATR_CS_IndExpr != null)
            return _DSPATR_CS_IndExpr;
        if(getChginpdftKeywordNode() != null && getChginpdftKeywordNode().hasCS())
            return "";
        else
            return null;
    }

    public String getHIIndExpr()
    {
        initialize();
        if(_DSPATR_HI_IndExpr != null)
            return _DSPATR_HI_IndExpr;
        if(getChginpdftKeywordNode() != null && getChginpdftKeywordNode().hasHI())
            return "";
        else
            return null;
    }

    public String getMDTIndExpr()
    {
        initialize();
        return _DSPATR_MDT_IndExpr;
    }

    public String getNDIndExpr()
    {
        initialize();
        return _DSPATR_ND_IndExpr;
    }

    public String getOIDIndExpr()
    {
        initialize();
        return _DSPATR_OID_IndExpr;
    }

    public String getPCIndExpr()
    {
        initialize();
        return _DSPATR_PC_IndExpr;
    }

    public String getPRIndExpr()
    {
        initialize();
        return _DSPATR_PR_IndExpr;
    }

    public String getRIIndExpr()
    {
        initialize();
        if(_DSPATR_RI_IndExpr != null)
            return _DSPATR_RI_IndExpr;
        if(getChginpdftKeywordNode() != null && getChginpdftKeywordNode().hasRI())
            return "";
        else
            return null;
    }

    public String getSPIndExpr()
    {
        initialize();
        return _DSPATR_SP_IndExpr;
    }

    public String getULIndExpr()
    {
        initialize();
        if(_DSPATR_UL_IndExpr != null)
            return _DSPATR_UL_IndExpr;
        if(getChginpdftKeywordNode() != null && getChginpdftKeywordNode().hasUL())
            return "";
        else
            return null;
    }

    public String getPFIELDName()
    {
        initialize();
        return _DSPATR_PFIELD_Name;
    }

    public String getPFIELDIndExpr()
    {
        initialize();
        return _DSPATR_PFIELD_IndExpr;
    }

    private void initialize()
    {
        if(initialized)
            return;
        initialized = true;
        KeywordNodeEnumeration keywordnodeenumeration = fieldNode.getKeywordsOfType(88);
        boolean flag = false;
        while(keywordnodeenumeration.hasMoreElements()) 
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
                        case 273: 
                            _DSPATR_BL_IndExpr = s;
                            break;

                        case 270: 
                            _DSPATR_CS_IndExpr = s;
                            break;

                        case 271: 
                            _DSPATR_HI_IndExpr = s;
                            break;

                        case 277: 
                            if(flag)
                                _DSPATR_ND_IndExpr = "PFMND," + _DSPATR_PFIELD_Name + "," + _DSPATR_PFIELD_IndExpr + "," + s;
                            else
                                _DSPATR_ND_IndExpr = s;
                            break;

                        case 279: 
                            _DSPATR_PC_IndExpr = s;
                            break;

                        case 274: 
                            _DSPATR_RI_IndExpr = s;
                            break;

                        case 272: 
                            _DSPATR_UL_IndExpr = s;
                            break;

                        case 276: 
                            _DSPATR_MDT_IndExpr = s;
                            break;

                        case 280: 
                            _DSPATR_OID_IndExpr = s;
                            break;

                        case 275: 
                            if(flag)
                                _DSPATR_PR_IndExpr = "PFMPR," + _DSPATR_PFIELD_Name + "," + _DSPATR_PFIELD_IndExpr + "," + s;
                            else
                                _DSPATR_PR_IndExpr = s;
                            break;

                        case 278: 
                            _DSPATR_SP_IndExpr = s;
                            break;

                        default:
                            if(keywordparm.getVarString().equals(""))
                                break;
                            flag = true;
                            _DSPATR_PFIELD_Name = WebfacingConstants.replaceSpecialCharacters(keywordparm.getVarString().substring(1));
                            _DSPATR_PFIELD_IndExpr = s;
                            if(_DSPATR_ND_IndExpr == null)
                                _DSPATR_ND_IndExpr = "PFND," + _DSPATR_PFIELD_Name + "," + _DSPATR_PFIELD_IndExpr;
                            else
                                _DSPATR_ND_IndExpr = "PFMND," + _DSPATR_PFIELD_Name + "," + _DSPATR_PFIELD_IndExpr + "," + _DSPATR_ND_IndExpr;
                            if(_DSPATR_PR_IndExpr == null)
                                _DSPATR_PR_IndExpr = "PFPR," + _DSPATR_PFIELD_Name + "," + _DSPATR_PFIELD_IndExpr;
                            else
                                _DSPATR_PR_IndExpr = "PFMPR," + _DSPATR_PFIELD_Name + "," + _DSPATR_PFIELD_IndExpr + "," + _DSPATR_PR_IndExpr;
                            break;
                        }
                    }

                }
            }
        }
        _chginpdftKeywordNode = (CHGINPDFTKeywordNode)fieldNode.findKeywordById(69);
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2000");
    private String _DSPATR_BL_IndExpr;
    private String _DSPATR_CS_IndExpr;
    private String _DSPATR_HI_IndExpr;
    private String _DSPATR_ND_IndExpr;
    private String _DSPATR_RI_IndExpr;
    private String _DSPATR_MDT_IndExpr;
    private String _DSPATR_OID_IndExpr;
    private String _DSPATR_PR_IndExpr;
    private String _DSPATR_SP_IndExpr;
    private String _DSPATR_PC_IndExpr;
    private String _DSPATR_UL_IndExpr;
    private String _DSPATR_PFIELD_Name;
    private String _DSPATR_PFIELD_IndExpr;
    private CHGINPDFTKeywordNode _chginpdftKeywordNode;
    private FieldNode fieldNode;
    private boolean initialized;

}
