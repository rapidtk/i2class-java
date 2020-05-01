// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import com.ibm.as400ad.code400.dom.constants.IRecordType;
import com.ibm.as400ad.code400.dom.constants.RecordType;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import org.w3c.dom.*;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            AnyNodeWithDescription, FieldNodeEnumeration, FileNode, RecordNodeEnumeration, 
//            FieldNode, HelpspecNode, RecordNodeWdwInfo, AnyNodeWithKeywords, 
//            AnyNode, KeywordNode, AnyNodeWithComments, WebSettingsNodeEnumeration, 
//            WebSettingsNode, KeywordNodeEnumeration, XMLParser

public class RecordNode extends AnyNodeWithDescription
{

    protected RecordNode(AnyNode anynode)
    {
        super(anynode, 1);
        _beanname = null;
        _beanclass = null;
        wdwInfo = null;
        _hasFieldSelection = null;
        _hasDSPSIZWebSetting = null;
        _unnamedFieldIndex = 0;
        wdw_height = new int[2];
        wdw_width = new int[2];
    }

    public boolean canHaveFields()
    {
        boolean flag = true;
        switch(getFile().getDDSType())
        {
        case 76: // 'L'
        case 82: // 'R'
        default:
            break;

        case 80: // 'P'
            if(findKeywordById(398) != null)
                flag = false;
            break;

        case 68: // 'D'
            flag = recordType != 3;
            break;
        }
        return flag;
    }

    public String getBeanClassName()
    {
        if(null == _beanclass)
            _beanclass = getFile().getPackageName() + "." + getWebName();
        return _beanclass;
    }

    public String getBeanName()
    {
        return getWebName();
    }

    public String getDate()
    {
        return date;
    }

    public FieldNodeEnumeration getFields()
    {
        return new FieldNodeEnumeration(fields);
    }

    public Vector getFieldsVector()
    {
        return fields;
    }

    public FileNode getFile()
    {
        return (FileNode)getParent();
    }

    public Iterator getHelpspecs()
    {
        if(_helpspecs != null)
            return _helpspecs.iterator();
        else
            return null;
    }

    public int getMaxWindowHeight(int i)
    {
        return i != 0 ? 128 : 76;
    }

    public int getMaxWindowWidth(int i)
    {
        return i != 0 ? 25 : 22;
    }

    public int getMinWindowHeight()
    {
        return 1;
    }

    public int getMinWindowWidth()
    {
        return 1;
    }

    public RecordNode getNext()
    {
        Vector vector = getFile().getRecordsVector();
        RecordNode recordnode = null;
        if(vector != null)
        {
            Object obj = null;
            RecordNode recordnode2 = null;
            for(int i = 0; recordnode == null && i < vector.size(); i++)
            {
                RecordNode recordnode1 = recordnode2;
                recordnode2 = (RecordNode)vector.elementAt(i);
                if(recordnode1 == this)
                    recordnode = recordnode2;
            }

        }
        return recordnode;
    }

    public RecordNode getNextNonSFLRecord()
    {
        RecordNode recordnode = getNext();
        if(recordnode != null)
        {
            if(recordnode.getRecordType() != 1 && recordnode.getRecordType() != 3)
                return recordnode;
            else
                return recordnode.getNext();
        } else
        {
            return null;
        }
    }

    public RecordNode getPrevious()
    {
        Vector vector = getFile().getRecordsVector();
        RecordNode recordnode = null;
        if(vector != null)
        {
            Object obj = null;
            RecordNode recordnode1 = null;
            for(int i = 0; recordnode == null && i < vector.size(); i++)
            {
                Object obj1 = recordnode1;
                recordnode1 = (RecordNode)vector.elementAt(i);
                if(recordnode1 == this)
                    recordnode = (RecordNode)obj1;
            }

        }
        return recordnode;
    }

    public RecordNode getPreviousNonSFLRecord()
    {
        RecordNode recordnode = getPrevious();
        if(recordnode != null)
        {
            if(recordnode.getRecordType() != 1 && recordnode.getRecordType() != 3)
                return recordnode;
            else
                return recordnode.getPrevious();
        } else
        {
            return null;
        }
    }

    public RecordNodeEnumeration getRecords()
    {
        Vector vector = new Vector();
        vector.add(this);
        return new RecordNodeEnumeration(vector);
    }

    public int getRecordType()
    {
        return recordType;
    }

    public String getRecordTypeAsString()
    {
        return getType().toString();
    }

    public RecordNode getRelatedSFL()
    {
        return getPrevious();
    }

    public RecordNode getRelatedSFLCTL()
    {
        return getNext();
    }

    public int getUnnamedFieldIndex()
    {
        return _unnamedFieldIndex++;
    }

    public RecordNodeWdwInfo getWdwInfo(int i)
    {
        if(wdwInfo != null)
            return wdwInfo[i];
        else
            return null;
    }

    public int getWindowHeight(int i)
    {
        if(wdwInfo != null)
            return wdwInfo[i].rows;
        else
            return wdw_height[i];
    }

    public KeywordNode getWindowKwdForDSZ(int i)
    {
        Object obj = null;
        KeywordNode keywordnode1 = null;
        KeywordNode keywordnode2 = null;
        KeywordNode keywordnode3 = null;
        KeywordNode keywordnode4 = null;
        boolean flag = false;
        for(KeywordNode keywordnode = findKeywordById(226); keywordnode != null; keywordnode = findNextKeywordById(keywordnode, 226))
            if(keywordnode.getKeywordIndType() == 3)
            {
                if(keywordnode.getDisplaySizeCondition() == i)
                {
                    if(keywordnode1 == null)
                        keywordnode1 = keywordnode;
                } else
                if(keywordnode3 == null)
                    keywordnode3 = keywordnode;
            } else
            if(keywordnode2 == null)
            {
                keywordnode2 = keywordnode;
                if(keywordnode1 == null)
                    flag = true;
            }

        if(flag)
            keywordnode4 = keywordnode2;
        if(keywordnode1 != null)
            keywordnode4 = keywordnode1;
        else
        if(keywordnode2 != null)
            keywordnode4 = keywordnode2;
        else
            keywordnode4 = keywordnode3;
        return keywordnode4;
    }

    public int getWindowWidth(int i)
    {
        if(wdwInfo != null)
            return wdwInfo[i].cols;
        else
            return wdw_width[i];
    }

    public boolean hasDSPSIZWebSetting()
    {
        if(_hasDSPSIZWebSetting == null)
        {
            boolean flag = false;
            if(hasWebSettings())
            {
                for(WebSettingsNodeEnumeration websettingsnodeenumeration = getWebSettings(); websettingsnodeenumeration.hasMoreElements();)
                {
                    WebSettingsNode websettingsnode = websettingsnodeenumeration.nextWebSettings();
                    if(websettingsnode.getType() == 10)
                        flag = true;
                }

            }
            _hasDSPSIZWebSetting = new Boolean(flag);
        }
        return _hasDSPSIZWebSetting.booleanValue();
    }

    public boolean hasFieldSelection()
    {
        if(_hasFieldSelection == null)
        {
            _hasFieldSelection = new Boolean(false);
            for(FieldNodeEnumeration fieldnodeenumeration = getFields(); fieldnodeenumeration.hasMoreElements();)
            {
                FieldNode fieldnode = fieldnodeenumeration.nextField();
                String s = fieldnode.getIndicatorString();
                if(s != null && s.length() > 0)
                {
                    _hasFieldSelection = new Boolean(true);
                    break;
                }
            }

        }
        return _hasFieldSelection.booleanValue();
    }

    public boolean isMNUBAR()
    {
        return recordType == 5;
    }

    public boolean isPulldown()
    {
        return isPulldown;
    }

    public boolean isRelativePRTF()
    {
        return isRelative;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public boolean isSFL()
    {
        return recordType == 1;
    }

    public boolean isSFLCTL()
    {
        return recordType == 2 || recordType == 6;
    }

    public boolean isSFLMSG()
    {
        return recordType == 3;
    }

    public boolean isSFLMSGCTL()
    {
        return getRecordType() == 6;
    }

    public boolean isSubfileFoldable()
    {
        if(isSFLCTL())
            return getKeywordsOfType(190).hasMoreElements() || getKeywordsOfType(185).hasMoreElements();
        if(isSFL())
            return getRelatedSFLCTL().isSubfileFoldable();
        else
            return false;
    }

    public boolean isUSRDFN()
    {
        return recordType == 3;
    }

    public boolean isWindow()
    {
        return isWindow;
    }

    public boolean isWindowed()
    {
        return isWindow() || isPulldown();
    }

    public void restoreFromXML(XMLParser xmlparser, Node node)
    {
        NamedNodeMap namednodemap = node.getAttributes();
        if(namednodemap != null && namednodemap.getLength() > 0)
            super.restoreAttributesFromXML(xmlparser, namednodemap);
        Node node1 = null;
        String s = (node1 = namednodemap.getNamedItem("date")) != null ? node1.getNodeValue() : null;
        String s1 = (node1 = namednodemap.getNamedItem("type")) != null ? node1.getNodeValue() : null;
        String s2 = (node1 = namednodemap.getNamedItem("relative")) != null ? node1.getNodeValue() : null;
        String s3 = (node1 = namednodemap.getNamedItem("selected")) != null ? node1.getNodeValue() : null;
        if(s != null)
            setDate(s);
        if(s1 != null)
            setRecordType(Integer.parseInt(s1));
        if(s2 != null)
            setIsRelativePRTF(s2.equals("true"));
        if(s3 != null)
            setIsSelected(s3.equals("true"));
        NodeList nodelist = node.getChildNodes();
        if(nodelist != null && nodelist.getLength() > 0)
            super.restoreChildrenFromXML(xmlparser, nodelist);
        else
            return;
        Vector vector = XMLParser.getTags(nodelist, "field");
        if(vector != null && vector.size() > 0)
        {
            Vector vector1 = new Vector();
            for(int i = 0; i < vector.size(); i++)
            {
                FieldNode fieldnode = new FieldNode(this);
                fieldnode.restoreFromXML(xmlparser, (Node)vector.elementAt(i));
                vector1.addElement(fieldnode);
            }

            if(vector1.size() > 0)
                setFields(vector1);
        }
        Vector vector2 = XMLParser.getTags(nodelist, "helpspec");
        if(vector2 != null && vector2.size() > 0)
        {
            ArrayList arraylist = new ArrayList();
            for(int j = 0; j < vector2.size(); j++)
            {
                HelpspecNode helpspecnode = new HelpspecNode(this);
                helpspecnode.restoreFromXML(xmlparser, (Node)vector2.elementAt(j));
                arraylist.add(helpspecnode);
            }

            setHelpspecs(arraylist);
        }
    }

    public void saveAsXML(String s, PrintWriter printwriter)
        throws IOException
    {
        printwriter.print(s + "<" + "record");
        super.saveAttributesAsXML(printwriter);
        printwriter.print(" date=\"" + (getDate() != null ? getDate() : "") + "\" " + "type" + "=\"" + getRecordType() + "\" " + "relative" + "=\"" + isRelativePRTF() + "\" " + "selected" + "=\"" + isSelected() + '"');
        printwriter.println(">");
        String s1 = s + "  ";
        super.saveChildrenAsXML(s1, printwriter);
        if(fields != null)
        {
            for(int i = 0; i < fields.size(); i++)
            {
                FieldNode fieldnode = (FieldNode)fields.elementAt(i);
                fieldnode.saveAsXML(s1, printwriter);
            }

        }
        printwriter.println(s + "</" + "record" + ">");
    }

    protected void setDate(String s)
    {
        date = s;
    }

    protected void setFields(Vector vector)
    {
        fields = vector;
    }

    protected void setHelpspecs(List list)
    {
        _helpspecs = list;
    }

    protected void setIsPulldown(boolean flag)
    {
        isPulldown = flag;
    }

    protected void setIsRelativePRTF(boolean flag)
    {
        isRelative = flag;
    }

    protected void setIsSelected(boolean flag)
    {
        selected = flag;
    }

    protected void setIsWindow(boolean flag)
    {
        isWindow = flag;
    }

    protected void setRecordType(int i)
    {
        recordType = i;
    }

    protected void setWdwInfo(RecordNodeWdwInfo recordnodewdwinfo, int i)
    {
        if(wdwInfo == null)
            wdwInfo = new RecordNodeWdwInfo[2];
        wdwInfo[i] = recordnodewdwinfo;
    }

    protected void setWindowHeight(int i, int j)
    {
        wdw_height[i] = j;
    }

    protected void setWindowWidth(int i, int j)
    {
        wdw_width[i] = j;
    }

    public IRecordType getType()
    {
        return RecordType.getRecordTypeFromId(getRecordType());
    }

    static final String copyRight = new String("(C) Copyright IBM Corporation 1999-2003, All rights reserved");
    protected static final String XML_ATTR_DATE = "date";
    protected static final String XML_ATTR_RELATIVE = "relative";
    protected static final String XML_ATTR_SELECTED = "selected";
    protected static final String XML_ATTR_SIZE = "size";
    protected static final String XML_ATTR_TYPE = "type";
    protected static final String XML_TAG_RECORD = "record";
    private String _beanname;
    private String _beanclass;
    private String date;
    private Vector fields;
    private List _helpspecs;
    private boolean isWindow;
    private boolean isPulldown;
    private boolean isRelative;
    private boolean selected;
    private int recordType;
    private int wdw_height[];
    private int wdw_width[];
    private RecordNodeWdwInfo wdwInfo[];
    private Boolean _hasFieldSelection;
    private Boolean _hasDSPSIZWebSetting;
    private int _unnamedFieldIndex;

}
