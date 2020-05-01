// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.convert.ExportHandler;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            JavaSourceCodeCollection

class ResponseIndicatorList
    implements ENUM_KeywordIdentifiers
{
    class AIDKeyResponseIndicator extends ResponseIndicator
    {

        void addTo(List list, Map map)
        {
            String s = Integer.toString(getIndicator());
            if(map.containsKey(s))
            {
                if(!(map.get(s) instanceof AIDKeyResponseIndicator))
                {
                    map.remove(s);
                    map.put(s, this);
                }
            } else
            {
                map.put(s, this);
                list.add(s);
            }
        }

        AIDKeyResponseIndicator(KeywordNode keywordnode, KeywordParm keywordparm)
        {
            super(keywordnode, keywordparm);
        }
    }

    class ResponseIndicator
        implements ENUM_KeywordIdentifiers
    {

        String getName()
        {
            return _kwd.getName();
        }

        int getKeyword()
        {
            return _kwd.getKeywordId();
        }

        int getIndicator()
        {
            return _indicator;
        }

        String getIndExpr()
        {
            return _indExpr;
        }

        void addTo(List list, Map map)
        {
            String s = Integer.toString(getIndicator());
            if(!map.containsKey(s))
            {
                map.put(s, this);
                list.add(s);
            }
        }

        private int _indicator;
        private KeywordNode _kwd;
        private String _indExpr;

        ResponseIndicator(KeywordNode keywordnode, KeywordParm keywordparm)
        {
            _indExpr = null;
            _kwd = keywordnode;
            _indicator = keywordparm.getVarNumber();
            _indExpr = keywordnode.getIndicatorString();
        }
    }


    ResponseIndicatorList()
    {
        _node = null;
        _list = new Vector(20, 10);
        _size = 0;
    }

    protected JavaSourceCodeCollection generateFeedbackResponseIndicator()
    {
        JavaSourceCodeCollection javasourcecodecollection = new JavaSourceCodeCollection();
        javasourcecodecollection.setIndentLevel(1);
        try
        {
            for(Iterator iterator = _list.iterator(); iterator.hasNext();)
            {
                StringBuffer stringbuffer = new StringBuffer();
                ResponseIndicator responseindicator = (ResponseIndicator)iterator.next();
                if(responseindicator.getKeyword() != 176 && responseindicator.getKeyword() != 177 && responseindicator.getKeyword() != 99 && responseindicator.getKeyword() != 100 && responseindicator.getKeyword() != 195 && responseindicator.getKeyword() != 196 && responseindicator.getKeyword() != 144)
                {
                    stringbuffer.append("new ");
                    switch(responseindicator.getKeyword())
                    {
                    case 223: 
                        stringbuffer.append("AnyAIDKeyResponseIndicator(");
                        break;

                    case 62: // '>'
                        stringbuffer.append(getCHANGE_RIConstructorPrefix());
                        break;

                    case 11: // '\013'
                        stringbuffer.append(getBLANKS_RIConstructorPrefix());
                        break;

                    case 92: // '\\'
                        stringbuffer.append("ResponseIndicator(");
                        break;

                    case 118: // 'v'
                        stringbuffer.append("HLPRTNResponseIndicator(");
                        if(responseindicator.getIndExpr() != null)
                            stringbuffer.append("\"" + responseindicator.getIndExpr() + "\", ");
                        break;

                    default:
                        stringbuffer.append("AIDKeyResponseIndicator(\"" + responseindicator.getName() + "\", ");
                        break;
                    }
                    stringbuffer.append(responseindicator.getIndicator());
                    stringbuffer.append(")");
                    javasourcecodecollection.addLine(getAddToDefCall() + "(" + stringbuffer + ");");
                }
            }

        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, throwable, "error in ResponseIndicatorList.generateFeedbackResponseIndicator() = " + throwable);
        }
        return javasourcecodecollection;
    }

    protected static JavaSourceCodeCollection generateFeedbackResponseIndicator(AnyNodeWithKeywords anynodewithkeywords)
    {
        ResponseIndicatorList responseindicatorlist = new ResponseIndicatorList();
        responseindicatorlist.populateFrom(anynodewithkeywords);
        return responseindicatorlist.generateFeedbackResponseIndicator();
    }

    protected void generateIndicatorDataDefinition(Vector vector, JavaSourceCodeCollection javasourcecodecollection)
    {
        try
        {
            if(_list.size() == 0 && vector.size() == 0)
            {
                javasourcecodecollection.addLine("add(new IndicatorDataDefinition());");
            } else
            {
                javasourcecodecollection.addLine("IndicatorDataDefinition indicators = new IndicatorDataDefinition();");
                for(Enumeration enumeration = vector.elements(); enumeration.hasMoreElements(); javasourcecodecollection.addLine("indicators.addReferencedOptionIndicator(" + (String)enumeration.nextElement() + ");"));
                Vector vector1 = new Vector(_list.size());
                Hashtable hashtable = new Hashtable();
                for(int i = 0; i < _list.size(); i++)
                {
                    ResponseIndicator responseindicator = (ResponseIndicator)_list.elementAt(i);
                    responseindicator.addTo(vector1, hashtable);
                }

                _size = hashtable.size();
                ResponseIndicator responseindicator1;
                for(Enumeration enumeration1 = vector1.elements(); enumeration1.hasMoreElements(); javasourcecodecollection.addLine("indicators.addReferencedResponseIndicator(" + responseindicator1.getIndicator() + ", " + (responseindicator1 instanceof AIDKeyResponseIndicator) + ");"))
                    responseindicator1 = (ResponseIndicator)hashtable.get((String)enumeration1.nextElement());

                javasourcecodecollection.addLine("add(indicators);");
            }
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, throwable, "error in ResponseIndicatorList.generateIndicatorDataDefinition()  = " + throwable);
        }
    }

    protected String getAddToDefCall()
    {
        return "add";
    }

    private String getBLANKS_RIConstructorPrefix()
    {
        if(_node instanceof FieldNode)
            return "BLANKSResponseIndicator(\"" + _node.getWebName() + "\", ";
        else
            return "";
    }

    private String getCHANGE_RIConstructorPrefix()
    {
        if(_node instanceof RecordNode)
            return "AnyFieldResponseIndicator(";
        if(_node instanceof FieldNode)
            return "FieldResponseIndicator(\"" + _node.getWebName() + "\", ";
        else
            return "";
    }

    public int getSize()
    {
        if(_size == 0)
            return _list.size();
        else
            return _size;
    }

    public void setSize(int i)
    {
        _size = i;
    }

    protected void populateFrom(AnyNodeWithKeywords anynodewithkeywords)
    {
        _node = anynodewithkeywords;
        Vector vector = anynodewithkeywords.getKeywordsVector();
        if(vector != null)
        {
            for(int i = 0; i < vector.size(); i++)
            {
                KeywordNode keywordnode = (KeywordNode)vector.elementAt(i);
                switch(keywordnode.getKeywordId())
                {
                case 12: // '\f'
                case 13: // '\r'
                case 63: // '?'
                case 64: // '@'
                case 65: // 'A'
                case 66: // 'B'
                case 67: // 'C'
                case 68: // 'D'
                case 69: // 'E'
                case 70: // 'F'
                case 71: // 'G'
                case 72: // 'H'
                case 74: // 'J'
                case 75: // 'K'
                case 76: // 'L'
                case 77: // 'M'
                case 78: // 'N'
                case 79: // 'O'
                case 80: // 'P'
                case 81: // 'Q'
                case 82: // 'R'
                case 83: // 'S'
                case 84: // 'T'
                case 85: // 'U'
                case 86: // 'V'
                case 87: // 'W'
                case 88: // 'X'
                case 89: // 'Y'
                case 90: // 'Z'
                case 91: // '['
                case 93: // ']'
                case 94: // '^'
                case 95: // '_'
                case 96: // '`'
                case 97: // 'a'
                case 98: // 'b'
                case 101: // 'e'
                case 102: // 'f'
                case 103: // 'g'
                case 104: // 'h'
                case 105: // 'i'
                case 106: // 'j'
                case 108: // 'l'
                case 109: // 'm'
                case 110: // 'n'
                case 111: // 'o'
                case 112: // 'p'
                case 113: // 'q'
                case 114: // 'r'
                case 115: // 's'
                case 116: // 't'
                case 117: // 'u'
                case 119: // 'w'
                case 120: // 'x'
                case 121: // 'y'
                case 122: // 'z'
                case 124: // '|'
                case 125: // '}'
                case 126: // '~'
                case 127: // '\177'
                case 128: 
                case 129: 
                case 130: 
                case 131: 
                case 132: 
                case 133: 
                case 134: 
                case 135: 
                case 136: 
                case 137: 
                case 138: 
                case 139: 
                case 140: 
                case 141: 
                case 142: 
                case 143: 
                case 145: 
                case 146: 
                case 147: 
                case 148: 
                case 149: 
                case 150: 
                case 151: 
                case 152: 
                case 153: 
                case 154: 
                case 157: 
                case 159: 
                case 160: 
                case 161: 
                case 162: 
                case 163: 
                case 164: 
                case 165: 
                case 166: 
                case 167: 
                case 168: 
                case 169: 
                case 170: 
                case 171: 
                case 174: 
                case 175: 
                case 178: 
                case 179: 
                case 180: 
                case 181: 
                case 182: 
                case 183: 
                case 184: 
                case 185: 
                case 186: 
                case 187: 
                case 188: 
                case 189: 
                case 190: 
                case 191: 
                case 192: 
                case 193: 
                case 194: 
                case 197: 
                case 198: 
                case 199: 
                case 200: 
                case 201: 
                case 202: 
                case 203: 
                case 204: 
                case 205: 
                case 206: 
                case 207: 
                case 208: 
                case 209: 
                case 210: 
                case 211: 
                case 212: 
                case 213: 
                case 214: 
                case 215: 
                case 216: 
                case 217: 
                case 218: 
                case 219: 
                case 220: 
                case 221: 
                case 222: 
                default:
                    break;

                case 11: // '\013'
                case 62: // '>'
                case 92: // '\\'
                case 118: // 'v'
                case 176: 
                case 177: 
                case 223: 
                    KeywordParm keywordparm = keywordnode.getFirstParm();
                    if(keywordparm != null && keywordparm.getParmType() == 23)
                        _list.add(new ResponseIndicator(keywordnode, keywordparm));
                    break;

                case 100: // 'd'
                case 196: 
                    KeywordParm keywordparm1 = keywordnode.getParm(2);
                    if(keywordparm1 != null && keywordparm1.getParmType() == 23)
                        _list.add(new ResponseIndicator(keywordnode, keywordparm1));
                    break;

                case 99: // 'c'
                case 144: 
                case 195: 
                    KeywordParm keywordparm2 = keywordnode.getParm(1);
                    if(keywordparm2 != null && keywordparm2.getParmType() == 23)
                        _list.add(new ResponseIndicator(keywordnode, keywordparm2));
                    break;

                case 14: // '\016'
                case 15: // '\017'
                case 16: // '\020'
                case 17: // '\021'
                case 18: // '\022'
                case 19: // '\023'
                case 20: // '\024'
                case 21: // '\025'
                case 22: // '\026'
                case 23: // '\027'
                case 24: // '\030'
                case 25: // '\031'
                case 26: // '\032'
                case 27: // '\033'
                case 28: // '\034'
                case 29: // '\035'
                case 30: // '\036'
                case 31: // '\037'
                case 32: // ' '
                case 33: // '!'
                case 34: // '"'
                case 35: // '#'
                case 36: // '$'
                case 37: // '%'
                case 38: // '&'
                case 39: // '\''
                case 40: // '('
                case 41: // ')'
                case 42: // '*'
                case 43: // '+'
                case 44: // ','
                case 45: // '-'
                case 46: // '.'
                case 47: // '/'
                case 48: // '0'
                case 49: // '1'
                case 50: // '2'
                case 51: // '3'
                case 52: // '4'
                case 53: // '5'
                case 54: // '6'
                case 55: // '7'
                case 56: // '8'
                case 57: // '9'
                case 58: // ':'
                case 59: // ';'
                case 60: // '<'
                case 61: // '='
                case 73: // 'I'
                case 107: // 'k'
                case 123: // '{'
                case 155: 
                case 156: 
                case 158: 
                case 172: 
                case 173: 
                    KeywordParm keywordparm3 = keywordnode.getFirstParm();
                    if(keywordparm3 != null && keywordparm3.getParmType() == 23)
                        _list.add(new AIDKeyResponseIndicator(keywordnode, keywordparm3));
                    break;
                }
            }

        }
    }

    protected int size()
    {
        if(_size == 0)
            return _list.size();
        else
            return _size;
    }

    public Vector getList()
    {
        return _list;
    }

    private AnyNodeWithKeywords _node;
    private Vector _list;
    private int _size;
    static final String RECORD_CHANGE_RI_CLASS = "AnyFieldResponseIndicator";
    static final String FIELD_CHANGE_RI_CLASS = "FieldResponseIndicator";
    static final String VLDCMDKEY_RI_CLASS = "AnyAIDKeyResponseIndicator";
    static final String BLANKS_RI_CLASS = "BLANKSResponseIndicator";
    static final String HLPRTN_RI_CLASS = "HLPRTNResponseIndicator";
    static final String AID_KEY_RI_CLASS = "AIDKeyResponseIndicator";
    static final String DUP_RI_CLASS = "ResponseIndicator";
}
