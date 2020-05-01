// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import com.ibm.as400ad.code400.dom.constants.ENUM_DDSTypes;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import java.io.*;
import java.util.*;
import org.w3c.dom.*;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            AnyNodeWithDescription, RecordNodeEnumeration, RecordNode, GroupNode, 
//            HelpspecNode, KeywordNode, KeywordParm, FieldNode, 
//            RecordNodeWdwInfo, ExportSettings, AnyNode, XMLParser, 
//            AnyNodeWithKeywords, KeywordNodeEnumeration, FieldNodeEnumeration, Logger

public class FileNode extends AnyNodeWithDescription
    implements ENUM_DDSTypes
{

    public FileNode(int i)
    {
        super(null, 0);
        _shortName = null;
        _webShortName = null;
        _packagename = null;
        __packagename = null;
        _packagedirname = null;
        _libname = null;
        _filname = null;
        _mbrType = null;
        _ddstype = 'D';
        _exportSettings = ExportSettings.getExportSettings();
        _records = null;
        _selectedRecords = null;
        _groups = null;
        _dspsizConditioned = false;
        _primarydsz = 0;
        _secondarydsz = 1;
        _assumeSpecified = false;
        _keepSpecified = false;
        _primarydszname = "*DS3";
        _secondarydszname = "*DS4";
        _file = this;
    }

    public char getDDSType()
    {
        return _ddstype;
    }

    public String getDDSTypeAsString()
    {
        switch(_ddstype)
        {
        case 68: // 'D'
            return "DSPF";

        case 82: // 'R'
            return "PRTF";

        case 80: // 'P'
            return "PF";

        case 76: // 'L'
            return "LF";

        case 73: // 'I'
            return "ICF";

        case 69: // 'E'
        case 70: // 'F'
        case 71: // 'G'
        case 72: // 'H'
        case 74: // 'J'
        case 75: // 'K'
        case 77: // 'M'
        case 78: // 'N'
        case 79: // 'O'
        case 81: // 'Q'
        default:
            return "Unknown";
        }
    }

    public static FileNode getFile()
    {
        return _file;
    }

    public String getFileName()
    {
        if(null == _filname)
        {
            _filname = getName();
            if(_filname.lastIndexOf(':') >= 0)
            {
                _filname = "$LOCAL$";
            } else
            {
                if(_filname.lastIndexOf('/') >= 0)
                    _filname = _filname.substring(_filname.lastIndexOf('/') + 1);
                if(_filname.lastIndexOf('(') > 0)
                    _filname = _filname.substring(0, _filname.lastIndexOf('('));
            }
            _filname = _filname.toUpperCase();
        }
        return _filname;
    }

    public Vector getGroupsVector()
    {
        return _groups;
    }

    public String getLibName()
    {
        if(null == _libname)
        {
            _libname = getName();
            if(_libname.lastIndexOf(':') >= 0)
            {
                _libname = "$LOCAL$";
            } else
            {
                if(_libname.lastIndexOf('>') >= 0)
                    _libname = _libname.substring(_libname.lastIndexOf('>') + 1);
                if(_libname.lastIndexOf('/') > 0)
                    _libname = _libname.substring(0, _libname.lastIndexOf('/'));
            }
            _libname = _libname.toUpperCase();
        }
        return _libname;
    }

    public static Logger getLogger()
    {
        return _logger;
    }

    public final String getMemberType()
    {
        return _mbrType;
    }

    public String getPackageDirName()
    {
        if(null == _packagedirname)
        {
            _packagedirname = getPackageName();
            _packagedirname = _packagedirname.replace('.', File.separatorChar);
        }
        return _packagedirname;
    }

    public String getPackageName()
    {
        if(null == _packagename)
        {
            _packagename = getName();
            if(_packagename.indexOf(':') >= 0 && _packagename.indexOf('"') < 0)
                _packagename = getWebShortName();
            else
                _packagename = parseNameAndReplaceChars(_packagename);
        }
        return _packagename;
    }

    public short getPrimaryDisplaySize()
    {
        return _primarydsz;
    }

    public String getPrimaryDisplaySizeName()
    {
        return _primarydszname;
    }

    public RecordNodeEnumeration getRecords()
    {
        if(_records != null)
            return new RecordNodeEnumeration(_records);
        else
            return null;
    }

    public Vector getRecordsVector()
    {
        return _records;
    }

    public short getSecondaryDisplaySize()
    {
        return _secondarydsz;
    }

    public String getSecondaryDisplaySizeName()
    {
        return _secondarydszname;
    }

    public RecordNodeEnumeration getSelectedRecords()
    {
        if(_selectedRecords != null)
            return new RecordNodeEnumeration(_selectedRecords);
        else
            return null;
    }

    public Vector getSelectedRecordsVector()
    {
        return _selectedRecords;
    }

    public String getShortName()
    {
        if(null == _shortName)
        {
            _shortName = getName();
            if(_shortName.lastIndexOf('\\') >= 0)
                _shortName = _shortName.substring(_shortName.lastIndexOf('\\') + 1);
            if(_shortName.lastIndexOf('(') >= 0)
                _shortName = _shortName.substring(_shortName.lastIndexOf('(') + 1);
            if(_shortName.lastIndexOf(')') > 0)
                _shortName = _shortName.substring(0, _shortName.lastIndexOf(')'));
            if(_shortName.lastIndexOf('.') > 0)
                _shortName = _shortName.substring(0, _shortName.lastIndexOf('.'));
            _shortName.toUpperCase();
        }
        return _shortName;
    }

    protected RecordNode getWdwDefKwd(String s, short word0, short aword0[])
    {
        if(_records == null)
            return null;
        RecordNode recordnode = null;
        boolean flag = false;
        for(int i = 0; !flag && i < _records.size(); i++)
            if(((RecordNode)_records.elementAt(i)).getName().equals(s))
            {
                flag = true;
                recordnode = (RecordNode)_records.elementAt(i);
            }

        if(flag)
        {
            RecordNodeWdwInfo recordnodewdwinfo = recordnode.getWdwInfo(word0);
            if(null != recordnodewdwinfo && recordnodewdwinfo.wdw && recordnodewdwinfo.def)
            {
                aword0[0] = word0;
                return recordnode;
            }
            int j = word0 != 0 ? 0 : 1;
            recordnodewdwinfo = recordnode.getWdwInfo(j);
            if(null != recordnodewdwinfo && recordnodewdwinfo.wdw && recordnodewdwinfo.def && !recordnodewdwinfo.wdw)
            {
                aword0[0] = (short)j;
                return recordnode;
            }
            recordnode = null;
        }
        return recordnode;
    }

    public String getWebShortName()
    {
        if(null == _webShortName)
        {
            _webShortName = getShortName();
            _webShortName = replaceSpecialCharacters(_webShortName);
        }
        return _webShortName;
    }

    public boolean isASSUMESpecified()
    {
        return _assumeSpecified;
    }

    public boolean isDSPF()
    {
        return _ddstype == 'D';
    }

    public boolean isDspSizConditioned()
    {
        return _dspsizConditioned;
    }

    public boolean isEmpty()
    {
        return _records == null || _records.size() == 0;
    }

    public boolean isKEEPSpecified()
    {
        return _keepSpecified;
    }

    public boolean isPForLF()
    {
        return _ddstype == 'P' || _ddstype == 'L';
    }

    public boolean isPRTF()
    {
        return _ddstype == 'R';
    }

    private String parseNameAndReplaceChars(String s)
    {
        if(s.charAt(0) == '<')
            s = s.substring(s.indexOf('>') + 1, s.length() - 1);
        int i = WebfacingConstants.indexOfUnquotedChar(s, '(');
        s = s.substring(0, i) + '/' + s.substring(i + 1);
        s = WebfacingConstants.getCharacterReplacedPackageName(s);
        return s;
    }

    protected void postPopulate()
    {
        setWdwInfo(true);
        setWdwInfo(false);
        setKEEPandASSUME();
        setHelpInfo();
    }

    public boolean restoreFromXML(XMLParser xmlparser, Node node)
    {
        boolean flag = true;
        NamedNodeMap namednodemap = node.getAttributes();
        if(namednodemap != null && namednodemap.getLength() > 0)
            super.restoreAttributesFromXML(xmlparser, namednodemap);
        Node node1 = null;
        String s = (node1 = namednodemap.getNamedItem("membertype")) != null ? node1.getNodeValue() : null;
        if(null != s && s.length() > 2)
            _mbrType = s;
        else
            _mbrType = "NONE";
        xmlparser.logMessage("      +-> member type = " + _mbrType);
        String s1 = (node1 = namednodemap.getNamedItem("type")) != null ? node1.getNodeValue() : null;
        if(s1 != null)
            if(s1.equals("DSPF"))
                setDDSType('D');
            else
            if(s1.equals("PRTF"))
                setDDSType('R');
            else
            if(s1.equals("PF"))
                setDDSType('P');
            else
            if(s1.equals("LF"))
                setDDSType('L');
            else
                setDDSType('D');
        String s2 = (node1 = namednodemap.getNamedItem("primarydsz")) != null ? node1.getNodeValue() : null;
        if(s2 != null)
            setPrimaryDisplaySize(Short.parseShort(s2));
        String s3 = (node1 = namednodemap.getNamedItem("secondarydsz")) != null ? node1.getNodeValue() : null;
        if(s3 != null)
            setSecondaryDisplaySize(Short.parseShort(s3));
        _primarydszname = (node1 = namednodemap.getNamedItem("primarydszname")) != null ? node1.getNodeValue() : null;
        _secondarydszname = (node1 = namednodemap.getNamedItem("secondarydszname")) != null ? node1.getNodeValue() : null;
        if(null == _primarydszname)
            if(s2.equals("0"))
                _primarydszname = "*DS3";
            else
                _primarydszname = "*DS4";
        if(null == _secondarydszname)
            if(s2.equals("1"))
            {
                _secondarydszname = "*DS3";
                _secondarydsz = 0;
            } else
            {
                _secondarydszname = "*DS4";
                _secondarydsz = 1;
            }
        NodeList nodelist = node.getChildNodes();
        if(nodelist != null && nodelist.getLength() > 0)
            super.restoreChildrenFromXML(xmlparser, nodelist);
        else
            return true;
        Vector vector = XMLParser.getTags(nodelist, "record");
        if(vector != null)
        {
            Vector vector1 = new Vector();
            for(int i = 0; i < vector.size(); i++)
            {
                RecordNode recordnode = new RecordNode(this);
                Node node2 = (Node)vector.elementAt(i);
                xmlparser.logMessage("   RESTORING: <file> record: " + node2.getNodeValue());
                recordnode.restoreFromXML(xmlparser, node2);
                vector1.addElement(recordnode);
            }

            if(vector1.size() > 0)
            {
                setRecords(vector1);
                Vector vector4 = new Vector();
                for(int k = 0; k < vector1.size(); k++)
                    if(((RecordNode)vector1.elementAt(k)).isSelected())
                        vector4.addElement(vector1.elementAt(k));

                if(vector4.size() > 0)
                    setSelectedRecords(vector4);
            }
        }
        Vector vector2 = XMLParser.getTags(nodelist, "group");
        if(vector2 != null)
        {
            Vector vector3 = new Vector();
            for(int j = 0; j < vector2.size(); j++)
            {
                GroupNode groupnode = new GroupNode(this);
                xmlparser.logMessage("   RESTORING: <file> group " + ((Node)vector2.elementAt(j)).getNodeName());
                groupnode.restoreFromXML(xmlparser, (Node)vector2.elementAt(j));
                vector3.addElement(groupnode);
            }

            if(vector3.size() > 0)
                setGroups(vector3);
        }
        xmlparser.logMessage("RESTORING: End restoring <file>");
        return flag;
    }

    public void saveAsXML(String s, PrintWriter printwriter)
        throws IOException
    {
        printwriter.print(s + "<" + "file");
        super.saveAttributesAsXML(printwriter);
        printwriter.print(" type=\"" + getDDSTypeAsString() + "\" " + "primarydsz" + "=\"" + Short.toString(getPrimaryDisplaySize()) + "\" ");
        if(isDspSizConditioned())
            printwriter.print("secondarydsz=\"" + Short.toString(getSecondaryDisplaySize()) + "\" ");
        printwriter.println(">");
        String s1 = s + "  ";
        super.saveChildrenAsXML(s1, printwriter);
        if(_records != null && _records.size() > 0)
        {
            for(int i = 0; i < _records.size(); i++)
                ((RecordNode)_records.elementAt(i)).saveAsXML(s1, printwriter);

        }
        if(_groups != null && _groups.size() > 0)
        {
            for(int j = 0; j < _groups.size(); j++)
                ((GroupNode)_groups.elementAt(j)).saveAsXML(s1, printwriter);

        }
        printwriter.println(s + "</" + "file" + ">");
    }

    protected void setDDSType(char c)
    {
        _ddstype = c;
    }

    protected void setExportSettings(ExportSettings exportsettings)
    {
        _exportSettings = exportsettings;
    }

    protected void setGroups(Vector vector)
    {
        _groups = vector;
    }

    protected void setHelpInfo()
    {
        if(_records == null)
            return;
        for(int i = 0; i < _records.size(); i++)
        {
            RecordNode recordnode = (RecordNode)_records.elementAt(i);
            Iterator iterator = recordnode.getHelpspecs();
            if(iterator != null)
            {
                while(iterator.hasNext()) 
                {
                    HelpspecNode helpspecnode = (HelpspecNode)iterator.next();
                    KeywordNodeEnumeration keywordnodeenumeration = helpspecnode.getKeywords();
                    if(keywordnodeenumeration != null)
                        while(keywordnodeenumeration.hasMoreElements()) 
                        {
                            KeywordNode keywordnode = (KeywordNode)keywordnodeenumeration.nextElement();
                            int j = keywordnode.getKeywordId();
                            Vector vector = keywordnode.getParmsVector();
                            switch(j)
                            {
                            case 110: // 'n'
                            case 111: // 'o'
                            case 112: // 'p'
                            case 114: // 'r'
                            case 115: // 's'
                            default:
                                break;

                            case 116: // 't'
                            case 117: // 'u'
                                helpspecnode.setType(j);
                                if(vector.size() == 2)
                                {
                                    StringTokenizer stringtokenizer = new StringTokenizer(((KeywordParm)vector.elementAt(1)).getVarString(), "/");
                                    if(stringtokenizer.countTokens() == 2)
                                        helpspecnode.setHelpObjLib(stringtokenizer.nextToken(), stringtokenizer.nextToken());
                                    else
                                        helpspecnode.setHelpObjLib(stringtokenizer.nextToken());
                                } else
                                if(vector.size() != 1)
                                    break;
                                helpspecnode.setIndicatorString(keywordnode.getIndicatorString());
                                break;

                            case 108: // 'l'
                                if(vector.size() == 4)
                                {
                                    helpspecnode.setRectangle(((KeywordParm)vector.elementAt(0)).getVarNumber(), ((KeywordParm)vector.elementAt(1)).getVarNumber(), ((KeywordParm)vector.elementAt(2)).getVarNumber(), ((KeywordParm)vector.elementAt(3)).getVarNumber());
                                    break;
                                }
                                if(vector.size() <= 0)
                                    break;
                                int k = ((KeywordParm)vector.elementAt(0)).getVarParmToken();
                                helpspecnode.setAreaType(k);
                                if(k == 297)
                                {
                                    helpspecnode.setField(((KeywordParm)vector.elementAt(1)).getVarString());
                                    if(vector.size() == 3)
                                        helpspecnode.setFieldChoice(((KeywordParm)vector.elementAt(2)).getVarNumber());
                                    break;
                                }
                                if(k == 298)
                                    helpspecnode.setFieldId(((KeywordParm)vector.elementAt(1)).getVarNumber());
                                break;

                            case 109: // 'm'
                                helpspecnode.setIsBoundary(true);
                                break;

                            case 113: // 'q'
                                helpspecnode.setIsExcluded(true);
                                break;
                            }
                        }
                }
                for(FieldNodeEnumeration fieldnodeenumeration = recordnode.getFields(); fieldnodeenumeration.hasMoreElements();)
                {
                    FieldNode fieldnode = (FieldNode)fieldnodeenumeration.nextElement();
                    KeywordNode keywordnode1 = fieldnode.findKeywordById(115);
                    if(keywordnode1 != null)
                        ((FieldNode)keywordnode1.getParent()).setHelpId(keywordnode1.getParm(0).getVarNumber());
                }

            }
        }

    }

    protected void setKEEPandASSUME()
    {
        if(_records == null)
            return;
        for(int i = 0; !_keepSpecified && i < _records.size(); i++)
        {
            RecordNode recordnode = (RecordNode)_records.elementAt(i);
            KeywordNode keywordnode = recordnode.findKeywordById(131);
            _keepSpecified = keywordnode != null;
        }

        for(int j = 0; !_assumeSpecified && j < _records.size(); j++)
        {
            RecordNode recordnode1 = (RecordNode)_records.elementAt(j);
            KeywordNode keywordnode1 = recordnode1.findKeywordById(9);
            _assumeSpecified = keywordnode1 != null;
        }

    }

    public static void setLogger(Logger logger)
    {
        _logger = logger;
    }

    protected void setPrimaryDisplaySize(short word0)
    {
        _primarydsz = word0;
    }

    protected void setRecords(Vector vector)
    {
        _records = vector;
    }

    protected void setSecondaryDisplaySize(short word0)
    {
        _secondarydsz = word0;
        _dspsizConditioned = true;
    }

    protected void setSelectedRecords(Vector vector)
    {
        _selectedRecords = vector;
    }

    protected void setWdwInfo(boolean flag)
    {
        if(_records == null)
            return;
        Object obj = null;
        boolean aflag[] = new boolean[2];
        for(int i = 0; i < _records.size(); i++)
        {
            RecordNode recordnode = (RecordNode)_records.elementAt(i);
            recordnode.setIsWindow(false);
            if(recordnode.getRecordType() != 1 && recordnode.getRecordType() != 3 && recordnode.getRecordType() != 5 && recordnode.getRecordType() != 4 && !recordnode.isPulldown())
            {
                KeywordNode keywordnode = recordnode.findKeywordById(162);
                recordnode.setIsPulldown(keywordnode != null);
                if(keywordnode != null)
                {
                    RecordNode recordnode1 = recordnode.getPrevious();
                    if(recordnode.getRecordType() == 2 && recordnode1 != null && (recordnode1.getRecordType() == 1 || recordnode1.getRecordType() == 3))
                        recordnode1.setIsPulldown(true);
                } else
                {
                    RecordNodeWdwInfo arecordnodewdwinfo[] = new RecordNodeWdwInfo[2];
                    arecordnodewdwinfo[0] = recordnode.getWdwInfo(0);
                    if(arecordnodewdwinfo[0] == null)
                        arecordnodewdwinfo[0] = new RecordNodeWdwInfo();
                    arecordnodewdwinfo[1] = recordnode.getWdwInfo(1);
                    if(arecordnodewdwinfo[1] == null)
                        arecordnodewdwinfo[1] = new RecordNodeWdwInfo();
                    KeywordNode keywordnode1 = recordnode.findKeywordById(226);
                    int j = 0;
                    boolean flag1 = false;
                    aflag[0] = aflag[1] = false;
                    for(; keywordnode1 != null && j < 2; keywordnode1 = recordnode.findNextKeywordById(keywordnode1, 226))
                    {
                        recordnode.setIsWindow(true);
                        int l = 0;
                        if(keywordnode1.getKeywordIndType() == 3)
                            l = keywordnode1.getDisplaySizeCondition();
                        else
                            l = getPrimaryDisplaySize();
                        int k = 0;
                        j++;
                        if(!arecordnodewdwinfo[l].dszSet && !aflag[l])
                        {
                            aflag[l] = true;
                            arecordnodewdwinfo[l].wdw = true;
                            arecordnodewdwinfo[l].fWdwDFT = false;
                            arecordnodewdwinfo[l].fNOMSGLIN = false;
                            Vector vector = keywordnode1.getParmsVector();
                            for(int j1 = 0; j1 < vector.size(); j1++)
                            {
                                KeywordParm keywordparm = (KeywordParm)vector.elementAt(j1);
                                if(flag && keywordparm.getParmType() == 34 || !flag && keywordparm.getParmType() != 34)
                                    break;
                                arecordnodewdwinfo[l].dszSet = true;
                                k++;
                                switch(keywordparm.getParmType())
                                {
                                case 34: // '"'
                                    arecordnodewdwinfo[l].szNamedWdw = keywordparm.getVarString();
                                    short aword0[] = new short[1];
                                    RecordNode recordnode3 = getWdwDefKwd(keywordparm.getVarString(), (short)l, aword0);
                                    if(recordnode3 != null)
                                    {
                                        short word0 = aword0[0];
                                        arecordnodewdwinfo[l].strPos = recordnode3.getWdwInfo(word0).strPos;
                                        arecordnodewdwinfo[l].strLin = recordnode3.getWdwInfo(word0).strLin;
                                        arecordnodewdwinfo[l].cols = recordnode3.getWdwInfo(word0).cols;
                                        arecordnodewdwinfo[l].rows = recordnode3.getWdwInfo(word0).rows;
                                        arecordnodewdwinfo[l].fNOMSGLIN = recordnode3.getWdwInfo(word0).fNOMSGLIN;
                                    } else
                                    {
                                        arecordnodewdwinfo[l].strLin = 1;
                                        arecordnodewdwinfo[l].strPos = 2;
                                        arecordnodewdwinfo[l].cols = 50;
                                        arecordnodewdwinfo[l].rows = 10;
                                        arecordnodewdwinfo[l].fNOMSGLIN = false;
                                    }
                                    int k1 = l != 0 ? 0 : 1;
                                    if(!arecordnodewdwinfo[k1].dszSet && !aflag[k1])
                                    {
                                        RecordNode recordnode4 = getWdwDefKwd(keywordparm.getVarString(), (short)k1, aword0);
                                        if(recordnode4 != null)
                                        {
                                            short word1 = aword0[0];
                                            arecordnodewdwinfo[k1].szNamedWdw = keywordparm.getVarString();
                                            arecordnodewdwinfo[k1].strPos = recordnode4.getWdwInfo(word1).strPos;
                                            arecordnodewdwinfo[k1].strLin = recordnode4.getWdwInfo(word1).strLin;
                                            arecordnodewdwinfo[k1].cols = recordnode4.getWdwInfo(word1).cols;
                                            arecordnodewdwinfo[k1].rows = recordnode4.getWdwInfo(word1).rows;
                                            arecordnodewdwinfo[k1].fNOMSGLIN = recordnode4.getWdwInfo(word1).fNOMSGLIN;
                                        }
                                    }
                                    break;

                                case 50: // '2'
                                    arecordnodewdwinfo[l].def = true;
                                    arecordnodewdwinfo[l].hardlin = true;
                                    arecordnodewdwinfo[l].strLin = keywordparm.getVarNumber();
                                    break;

                                case 51: // '3'
                                    arecordnodewdwinfo[l].hardpos = true;
                                    arecordnodewdwinfo[l].strPos = keywordparm.getVarNumber();
                                    break;

                                case 68: // 'D'
                                    arecordnodewdwinfo[l].def = true;
                                    if(k == 1)
                                    {
                                        if(arecordnodewdwinfo[l].strLin == 0)
                                            arecordnodewdwinfo[l].strLin = 1;
                                        arecordnodewdwinfo[l].szRowField = keywordparm.getVarString().substring(1);
                                    } else
                                    {
                                        if(arecordnodewdwinfo[l].strPos == 0)
                                            arecordnodewdwinfo[l].strPos = 2;
                                        arecordnodewdwinfo[l].szColField = keywordparm.getVarString().substring(1);
                                    }
                                    break;

                                case 83: // 'S'
                                    arecordnodewdwinfo[l].rows = keywordparm.getVarNumber();
                                    break;

                                case 84: // 'T'
                                    arecordnodewdwinfo[l].cols = keywordparm.getVarNumber();
                                    break;

                                case 2: // '\002'
                                    if(keywordparm.getVarParmToken() == 365)
                                    {
                                        arecordnodewdwinfo[l].def = true;
                                        arecordnodewdwinfo[l].fWdwDFT = true;
                                        if(arecordnodewdwinfo[l].strLin == 0)
                                            arecordnodewdwinfo[l].strLin = 1;
                                        if(arecordnodewdwinfo[l].strPos == 0)
                                            arecordnodewdwinfo[l].strPos = 2;
                                    } else
                                    if(keywordparm.getVarParmToken() == 367)
                                        arecordnodewdwinfo[l].fNOMSGLIN = true;
                                    break;
                                }
                                if(!arecordnodewdwinfo[l].def || k == 5)
                                    break;
                            }

                            recordnode.setWdwInfo(arecordnodewdwinfo[l], l);
                        }
                    }

                    if(recordnode.isWindowed() && !flag)
                    {
                        int i1 = 0;
                        if(arecordnodewdwinfo[i1].strLin == 0)
                        {
                            arecordnodewdwinfo[i1].szNamedWdw = arecordnodewdwinfo[i1 + 1].szNamedWdw;
                            arecordnodewdwinfo[i1].def = arecordnodewdwinfo[i1 + 1].def;
                            arecordnodewdwinfo[i1].strLin = arecordnodewdwinfo[i1 + 1].strLin;
                            arecordnodewdwinfo[i1].strPos = arecordnodewdwinfo[i1 + 1].strPos;
                            arecordnodewdwinfo[i1].rows = arecordnodewdwinfo[i1 + 1].rows;
                            arecordnodewdwinfo[i1].cols = arecordnodewdwinfo[i1 + 1].cols;
                            arecordnodewdwinfo[i1].fWdwDFT = arecordnodewdwinfo[i1 + 1].fWdwDFT;
                            arecordnodewdwinfo[i1].fNOMSGLIN = arecordnodewdwinfo[i1 + 1].fNOMSGLIN;
                            arecordnodewdwinfo[i1].szColField = arecordnodewdwinfo[i1 + 1].szColField;
                            arecordnodewdwinfo[i1].szRowField = arecordnodewdwinfo[i1 + 1].szRowField;
                            arecordnodewdwinfo[i1].hardlin = arecordnodewdwinfo[i1 + 1].hardlin;
                            arecordnodewdwinfo[i1].hardpos = arecordnodewdwinfo[i1 + 1].hardpos;
                            arecordnodewdwinfo[i1].opaque = arecordnodewdwinfo[i1 + 1].opaque;
                            arecordnodewdwinfo[i1].norstcsr = arecordnodewdwinfo[i1 + 1].norstcsr;
                        } else
                        if(arecordnodewdwinfo[i1 + 1].strLin == 0)
                        {
                            arecordnodewdwinfo[i1 + 1].szNamedWdw = arecordnodewdwinfo[i1].szNamedWdw;
                            arecordnodewdwinfo[i1 + 1].def = arecordnodewdwinfo[i1].def;
                            arecordnodewdwinfo[i1 + 1].strLin = arecordnodewdwinfo[i1].strLin;
                            arecordnodewdwinfo[i1 + 1].strPos = arecordnodewdwinfo[i1].strPos;
                            arecordnodewdwinfo[i1 + 1].rows = arecordnodewdwinfo[i1].rows;
                            arecordnodewdwinfo[i1 + 1].cols = arecordnodewdwinfo[i1].cols;
                            arecordnodewdwinfo[i1 + 1].fWdwDFT = arecordnodewdwinfo[i1].fWdwDFT;
                            arecordnodewdwinfo[i1 + 1].fNOMSGLIN = arecordnodewdwinfo[i1].fNOMSGLIN;
                            arecordnodewdwinfo[i1 + 1].szColField = arecordnodewdwinfo[i1].szColField;
                            arecordnodewdwinfo[i1 + 1].szRowField = arecordnodewdwinfo[i1].szRowField;
                            arecordnodewdwinfo[i1 + 1].hardlin = arecordnodewdwinfo[i1].hardlin;
                            arecordnodewdwinfo[i1 + 1].hardpos = arecordnodewdwinfo[i1].hardpos;
                            arecordnodewdwinfo[i1 + 1].opaque = arecordnodewdwinfo[i1].opaque;
                            arecordnodewdwinfo[i1 + 1].norstcsr = arecordnodewdwinfo[i1].norstcsr;
                        }
                        recordnode.setWdwInfo(arecordnodewdwinfo[0], 0);
                        recordnode.setWdwInfo(arecordnodewdwinfo[1], 1);
                        if(recordnode.getRecordType() == 2)
                        {
                            RecordNode recordnode2 = recordnode.getPrevious();
                            recordnode2.setWdwInfo((RecordNodeWdwInfo)recordnode.getWdwInfo(0).clone(), 0);
                            recordnode2.setWdwInfo((RecordNodeWdwInfo)recordnode.getWdwInfo(1).clone(), 1);
                            recordnode2.getWdwInfo(0).wdw = false;
                            recordnode2.getWdwInfo(1).wdw = false;
                            recordnode2.setIsWindow(true);
                        }
                    }
                }
            }
        }

    }

    public static void cleanup()
    {
        _file = null;
        _logger = null;
    }

    public RecordNode getRecord(String s)
    {
        RecordNode recordnode = null;
        RecordNodeEnumeration recordnodeenumeration = getRecords();
        Object obj = null;
        while(null == recordnode && recordnodeenumeration.hasMoreElements()) 
        {
            RecordNode recordnode1 = (RecordNode)recordnodeenumeration.nextElement();
            if(recordnode1.getName().equals(s))
                recordnode = recordnode1;
        }
        return recordnode;
    }

    static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2003, All rights reserved.");
    private String _shortName;
    private String _webShortName;
    private String _packagename;
    private String __packagename;
    private String _packagedirname;
    private String _libname;
    private String _filname;
    private String _mbrType;
    private char _ddstype;
    private static FileNode _file = null;
    private ExportSettings _exportSettings;
    private static Logger _logger = null;
    private Vector _records;
    private Vector _selectedRecords;
    private Vector _groups;
    private boolean _dspsizConditioned;
    private short _primarydsz;
    private short _secondarydsz;
    private boolean _assumeSpecified;
    private boolean _keepSpecified;
    protected static final String XML_ATTR_PRIMDSZ = "primarydsz";
    protected static final String XML_ATTR_SECDSZ = "secondarydsz";
    protected static final String XML_ATTR_TYPE = "type";
    protected static final String XML_ATTR_MEMBERTYPE = "membertype";
    protected static final String XML_TAG_FILE = "file";
    protected static final int MAX_WINDOW_PARMS = 5;
    protected static final int TOPROW_PARM = 1;
    protected static final int WDFT_COL = 2;
    protected static final int WDFT_DEPTH = 10;
    protected static final int WDFT_ROW = 1;
    protected static final int WDFT_WIDTH = 50;
    private String _primarydszname;
    private String _secondarydszname;
    protected static final String XML_ATTR_PRIMDSZNAM = "primarydszname";
    protected static final String XML_ATTR_SECDSZNAM = "secondarydszname";

}
