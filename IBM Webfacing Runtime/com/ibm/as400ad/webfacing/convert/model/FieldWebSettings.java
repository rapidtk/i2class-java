// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.IHyperlinkType;
import com.ibm.as400ad.code400.dom.constants.IWebSettingType;
import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.convert.Util;
import com.ibm.as400ad.webfacing.runtime.dhtmlview.HTMLStringTransform;
import com.ibm.as400ad.webfacing.runtime.view.AIDKeyDictionary;
import com.ibm.etools.iseries.webfacing.convert.external.IRawWebSetting;
import com.ibm.etools.iseries.webfacing.convert.gen.tag.FieldRawWebSetting;
import com.ibm.etools.iseries.webfacing.convert.gen.tag.TagGeneratorLoader;
import java.io.*;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            FieldLines

public class FieldWebSettings
    implements IWebSettingType, IHyperlinkType
{

    public FieldWebSettings()
    {
        _subRawWebSettings = new ArrayList();
        _mainRawWebSetting = null;
        _hyperLinkType = 0;
        _submitFldName = null;
        _submitValue = null;
        _overrideBrowserHyperlinkAppearance = true;
        _rowSpan = -1;
        _colSpan = -1;
        _row = -1;
        _column = -1;
        _labelsForVALUES = new Vector();
        _graphic = null;
        _heightInPixel = -1;
        _htmlAfter = new String("");
        _htmlBefore = new String("");
        _htmlInside = new String("");
        _URL = null;
        _staticURL = null;
        _target = null;
        _cursoredRec = null;
        _cursoredFieldRRNString = null;
        _cursoredField = null;
        _cursoredFieldData = null;
        _submitFuncKey = null;
        _submitJsCall = null;
        _mask = false;
        _programDefine = false;
        _style = null;
        _text = null;
        _userDefine = null;
        _widthInPixel = -1;
        _textDisplayLength = -1;
        _dynLabelKey = null;
    }

    public FieldWebSettings(FieldNode fieldnode)
    {
        _subRawWebSettings = new ArrayList();
        _mainRawWebSetting = null;
        _hyperLinkType = 0;
        _submitFldName = null;
        _submitValue = null;
        _overrideBrowserHyperlinkAppearance = true;
        _rowSpan = -1;
        _colSpan = -1;
        _row = -1;
        _column = -1;
        _labelsForVALUES = new Vector();
        _graphic = null;
        _heightInPixel = -1;
        _htmlAfter = new String("");
        _htmlBefore = new String("");
        _htmlInside = new String("");
        _URL = null;
        _staticURL = null;
        _target = null;
        _cursoredRec = null;
        _cursoredFieldRRNString = null;
        _cursoredField = null;
        _cursoredFieldData = null;
        _submitFuncKey = null;
        _submitJsCall = null;
        _mask = false;
        _programDefine = false;
        _style = null;
        _text = null;
        _userDefine = null;
        _widthInPixel = -1;
        _textDisplayLength = -1;
        _dynLabelKey = null;
        _fn = fieldnode;
        RecordNode recordnode = _fn.getParentRecord();
        if(recordnode.isSFL() || recordnode.isSFLMSG())
            recordnode = recordnode.getRelatedSFLCTL();
        _curRecBeanName = recordnode.getBeanName();
    }

    public boolean copyGraphicFile()
    {
        String s = _graphic;
        boolean flag = false;
        if(s.substring(0, 4).toLowerCase().equals("http") || s.indexOf("getFieldValueWithTransform(") > 0)
        {
            flag = true;
        } else
        {
            ExportSettings exportsettings = ExportSettings.getExportSettings();
            String s1 = System.getProperty("file.separator");
            String s2 = s.substring(s.lastIndexOf(s1) + 1).trim();
            String s3 = exportsettings.getGeneratedImagesDirectory();
            String s4 = s3 + s2;
            if(s2 != null)
                try
                {
                    File file = new File(s);
                    if(file.exists() && file.isFile())
                    {
                        File file1 = new File(s3);
                        file1.mkdirs();
                        File file2 = new File(s4);
                        if(file.compareTo(file2) != 0)
                        {
                            FileInputStream fileinputstream = new FileInputStream(file);
                            FileOutputStream fileoutputstream = new FileOutputStream(file2);
                            if(fileinputstream.available() > 0)
                            {
                                byte abyte0[] = new byte[fileinputstream.available()];
                                fileinputstream.read(abyte0, 0, fileinputstream.available());
                                fileoutputstream.write(abyte0);
                                flag = true;
                            }
                            fileinputstream.close();
                            fileoutputstream.close();
                        } else
                        {
                            flag = true;
                        }
                    }
                }
                catch(Throwable throwable)
                {
                    Util.logThrowableMessage("error in FieldOutput.copyGraphicFile()", throwable, false);
                }
            if(!flag)
            {
                String as[] = new String[2];
                as[0] = s;
                String s5 = exportsettings.getRootExportDirectory().replace('/', File.separatorChar);
                if(s4.startsWith(s5))
                    as[1] = s4.substring(s5.length());
                else
                    as[1] = s4;
                _fn.logEvent(41, as);
            }
        }
        return flag;
    }

    public String createGraphic(String s)
    {
        String s1 = null;
        if(getGraphic() != null)
        {
            boolean flag = copyGraphicFile();
            if(flag)
            {
                String s2 = _graphic;
                s1 = s2;
                String s3 = System.getProperty("file.separator");
                Object obj = null;
                String s4 = "";
                String s5 = "";
                if(_widthInPixel > 0)
                    s4 = "WIDTH=\"" + _widthInPixel + "\"";
                if(_heightInPixel > 0)
                    s5 = "HEIGHT=\"" + _heightInPixel + "\"";
                if(s2.substring(0, 4).toLowerCase().equals("http"))
                {
                    PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(70);
                    paddedstringbuffer.concat("<img src=\"", s2, "\" BORDER=0 ", s4, " ", s5, " ALT=\"", s, "\" >");
                    s1 = paddedstringbuffer.toString();
                } else
                {
                    s2 = s2.substring(s2.lastIndexOf(s3) + 1).trim();
                    if(s2 != null)
                    {
                        s2 = "images/generated/" + s2;
                        PaddedStringBuffer paddedstringbuffer1 = new PaddedStringBuffer(80);
                        paddedstringbuffer1.concat("<img src=\"", s2, "\" BORDER=0 ", s4, " ", s5, " ALT=\"", s, "\" >");
                        s1 = paddedstringbuffer1.toString();
                    }
                }
            }
        }
        return s1;
    }

    public boolean createHyperLink(FieldLines fieldlines, String s)
    {
        if(getHyperlinkType() > 0)
        {
            for(int i = 0; i < fieldlines.size(); i++)
            {
                String s1 = createHyperLink(fieldlines.get(i), s);
                if(s1 == null || s1.equals(""))
                    return false;
                fieldlines.set(i, s1);
            }

            return true;
        } else
        {
            return false;
        }
    }

    public String createHyperLink(String s, String s1)
    {
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(50);
        String s2 = "<A ";
        String s3 = "href=\"#\"";
        if(_overrideBrowserHyperlinkAppearance)
            s2 = s2 + s1;
        String s8 = "<%=" + _curRecBeanName + ".getActiveKeyName(\"" + _submitFuncKey + "\") %>";
        if(_submitFuncKey != null && (_submitFuncKey.equalsIgnoreCase("enter") || _submitFuncKey.equalsIgnoreCase("logoff")))
            s8 = _submitFuncKey;
        if(_cursoredFieldData != null)
            _cursoredFieldData = "'" + _cursoredFieldData + "'";
        else
            _cursoredFieldData = "''";
        if(_cursoredRec != null && _cursoredField != null)
        {
            _cursoredField = "'document.SCREEN.l<%=" + _curRecBeanName + ".getZOrderPrefix(\"" + _cursoredRec + "\") %>_" + _cursoredRec + "$" + _cursoredField;
            _cursoredField += _cursoredFieldRRNString;
            _cursoredField += "'";
        }
        switch(_hyperLinkType)
        {
        default:
            break;

        case 1: // '\001'
            if(_URL != null)
                paddedstringbuffer.concat(" title=\"", _URL, "\"", getCondHyperlink(" href=\"" + _URL + "\"", null, null));
            break;

        case 2: // '\002'
            if(getSubmitValue() != null && getSubmitFieldId() != null)
                paddedstringbuffer.append(getCondHyperlink(s3 + " onClick=\"setFieldValueAndSubmit('" + getSubmitFieldId() + "','" + getSubmitValue() + "',this);\"", null, null));
            break;

        case 3: // '\003'
            if(_staticURL == null)
                break;
            String s9 = "";
            if(_target != null)
                s9 = " target=\"" + _target.trim() + "\" ";
            paddedstringbuffer.concat(" title=\"", _staticURL, "\"", s9, getCondHyperlink(" href=\"" + _staticURL + "\"", null, null));
            break;

        case 7: // '\007'
        case 9: // '\t'
            if(_submitJsCall != null && _submitJsCall.length() > 0)
                paddedstringbuffer.append(getCondHyperlink(s3 + " onClick=\"" + _submitJsCall + ";\"", null, null));
            // fall through

        case 6: // '\006'
            if(_submitFuncKey != null && _submitFuncKey.length() > 0)
            {
                String s4 = "validateAndSubmit('" + s8 + "')";
                paddedstringbuffer.append(getCondHyperlink(s3 + " onClick=\"" + s4 + ";\"", _submitFuncKey, null));
            }
            break;

        case 5: // '\005'
            if(_cursoredRec != null && _cursoredField != null)
            {
                String s5 = "setFocusAndValue(" + _cursoredField + "," + _cursoredFieldData + ")";
                paddedstringbuffer.append(getCondHyperlink(s3 + " onClick=\"" + s5 + ";\"", null, _cursoredRec));
            }
            break;

        case 4: // '\004'
            if(_cursoredRec != null && _cursoredField != null && _submitFuncKey != null && _submitFuncKey.length() > 0)
            {
                String s6 = "setFocusAndSubmitKey(" + _cursoredField + "," + _cursoredFieldData + ",'" + s8 + "',this)";
                paddedstringbuffer.append(getCondHyperlink(s3 + " onClick=\"" + s6 + ";\"", _submitFuncKey, _cursoredRec));
            }
            break;

        case 8: // '\b'
            if(_cursoredRec != null && _cursoredField != null && _submitJsCall != null && _submitJsCall.length() > 0)
            {
                String s7 = "setFocusAndValue(" + _cursoredField + "," + _cursoredFieldData + ");" + _submitJsCall;
                paddedstringbuffer.append(getCondHyperlink(s3 + " onClick=\"" + s7 + ";\"", null, _cursoredRec));
            }
            break;
        }
        String s10 = "";
        if(paddedstringbuffer.length() > 0)
        {
            paddedstringbuffer.insert(0, s2);
            s10 = replaceHyperlinkFieldValueSymbol(paddedstringbuffer.toString(), null, _fn) + ">" + s + "</A>";
        }
        return s10;
    }

    public int getColSpan()
    {
        return _colSpan;
    }

    public int getColumn()
    {
        return _column;
    }

    public String getGraphic()
    {
        return _graphic;
    }

    public String getHtmlAfter()
    {
        return _htmlAfter;
    }

    public String getHtmlBefore()
    {
        return _htmlBefore;
    }

    public String getHtmlInside()
    {
        return _htmlInside;
    }

    public Vector getLabelsForValues()
    {
        return _labelsForVALUES;
    }

    public String getLabelsForValuesInArrayFormat()
    {
        Vector vector = getLabelsForValues();
        if(vector != null && vector.size() > 0)
        {
            int i = vector.size();
            StringBuffer stringbuffer = new StringBuffer(i * 10 + 2);
            stringbuffer.append("[\"").append(HTMLStringTransform.transformUnquotedString(vector.get(0).toString(), true)).append("\"");
            for(int j = 1; j < i; j++)
                stringbuffer.append(",\"").append(HTMLStringTransform.transformUnquotedString(vector.get(j).toString(), true)).append("\"");

            stringbuffer.append("]");
            return stringbuffer.toString();
        } else
        {
            return "[]";
        }
    }

    public int getMaskHigh()
    {
        return _maskHigh;
    }

    public int getMaskLow()
    {
        return _maskLow;
    }

    public int getRow()
    {
        return _row;
    }

    public int getRowSpan()
    {
        return _rowSpan;
    }

    public String getStyle()
    {
        return _style;
    }

    public String getSubmitFieldId()
    {
        if(_submitFldName == null)
            return null;
        RecordNode recordnode = _fn.getParentRecord();
        String s = "document.SCREEN." + WebfacingConstants.getLayerPrefix() + recordnode.getBeanName() + "$" + replaceSpecialCharacters(_submitFldName);
        if(recordnode.isSFL() || recordnode.isSFLMSG())
            s = s + "$<%=rrn%>";
        return s;
    }

    public String getSubmitValue()
    {
        return _submitValue;
    }

    public String getText()
    {
        return _text;
    }

    public String getURL()
    {
        return _URL;
    }

    public String getUsrDefineHTML()
    {
        return _userDefine;
    }

    public int getHyperlinkType()
    {
        return _hyperLinkType;
    }

    public void initialize()
    {
        WebSettingsNodeEnumeration websettingsnodeenumeration = _fn.getWebSettings();
        Object obj = null;
        try
        {
            while(websettingsnodeenumeration.hasMoreElements()) 
            {
                WebSettingsNode websettingsnode = websettingsnodeenumeration.nextWebSettings();
                int i = websettingsnode.getType();
                String s = websettingsnode.getValue();
                switch(i)
                {
                case 2: // '\002'
                    StringTokenizer stringtokenizer = new StringTokenizer(s, " ");
                    if(stringtokenizer.countTokens() == 2)
                    {
                        _row = Integer.parseInt(stringtokenizer.nextToken());
                        _column = Integer.parseInt(stringtokenizer.nextToken());
                        _subRawWebSettings.add(new FieldRawWebSetting(2, s));
                    }
                    break;

                case 15: // '\017'
                    StringTokenizer stringtokenizer1 = new StringTokenizer(s, " ");
                    if(stringtokenizer1.countTokens() == 2)
                    {
                        _rowSpan = Integer.parseInt(stringtokenizer1.nextToken());
                        _colSpan = Integer.parseInt(stringtokenizer1.nextToken());
                        _subRawWebSettings.add(new FieldRawWebSetting(15, s));
                    }
                    break;

                case 1: // '\001'
                    if(s != null && !s.equals(""))
                    {
                        _style = s;
                        _subRawWebSettings.add(new FieldRawWebSetting(2, s));
                    }
                    break;

                case 3: // '\003'
                    int j = s.indexOf("|");
                    if(j < 1)
                        break;
                    _textDisplayLength = Integer.parseInt(s.substring(0, j));
                    if(s.length() > j + 1)
                        _text = s.substring(j + 1);
                    else
                        _text = "";
                    _subRawWebSettings.add(new FieldRawWebSetting(3, s));
                    break;

                case 4: // '\004'
                    _mainRawWebSetting = new FieldRawWebSetting(4, null);
                    break;

                case 12: // '\f'
                    _mainRawWebSetting = new FieldRawWebSetting(12, s);
                    initializeHyperlinks(s);
                    break;

                case 13: // '\r'
                    if(s == null)
                        break;
                    StringTokenizer stringtokenizer2 = new StringTokenizer(s, " ");
                    if(stringtokenizer2.countTokens() >= 2)
                    {
                        stringtokenizer2.nextToken();
                        s = stringtokenizer2.nextToken();
                    }
                    int k = s.indexOf("|");
                    int l = s.lastIndexOf("|");
                    if(k >= 1)
                        _widthInPixel = Integer.parseInt(s.substring(0, k));
                    if(l > k + 1)
                        _heightInPixel = Integer.parseInt(s.substring(k + 1, l));
                    if(l >= s.length() - 1)
                        break;
                    _graphic = s.substring(l + 1, s.length()).trim();
                    if(_graphic == null)
                        break;
                    _graphic = replaceQuotedFieldValueSymbol(_graphic);
                    if(_mainRawWebSetting != null && _mainRawWebSetting.getWebSettingId() == 12)
                        _subRawWebSettings.add(new FieldRawWebSetting(13, s));
                    else
                        _mainRawWebSetting = new FieldRawWebSetting(13, s);
                    break;

                case 5: // '\005'
                    if(s == null || s.equals(""))
                        break;
                    _maskLow = Integer.parseInt(s.substring(0, s.indexOf(" ")));
                    _maskHigh = Integer.parseInt(s.substring(s.indexOf(" ")).trim());
                    if(_maskLow >= 1 && _maskHigh >= _maskLow)
                    {
                        _mask = true;
                        _maskLow--;
                        _subRawWebSettings.add(new FieldRawWebSetting(5, s));
                    }
                    break;

                case 6: // '\006'
                    if(s != null)
                    {
                        _htmlBefore = WebfacingConstants.replaceInapplicableCharacters(s);
                        _subRawWebSettings.add(new FieldRawWebSetting(6, s));
                    }
                    break;

                case 7: // '\007'
                    if(s != null)
                    {
                        _htmlInside = WebfacingConstants.replaceInapplicableCharacters(s);
                        _subRawWebSettings.add(new FieldRawWebSetting(7, s));
                    }
                    break;

                case 8: // '\b'
                    if(s != null)
                    {
                        _htmlAfter = WebfacingConstants.replaceInapplicableCharacters(s);
                        _subRawWebSettings.add(new FieldRawWebSetting(8, s));
                    }
                    break;

                case 14: // '\016'
                    String s1;
                    for(StringTokenizer stringtokenizer3 = new StringTokenizer(s, "|"); stringtokenizer3.hasMoreTokens(); _labelsForVALUES.addElement(s1))
                    {
                        s1 = "";
                        String s2 = stringtokenizer3.nextToken();
                        if(s2.indexOf("=") < s2.length() - 1)
                            s1 = s2.substring(s2.indexOf("=") + 1);
                    }

                    if(_labelsForVALUES.size() > 0)
                        _subRawWebSettings.add(new FieldRawWebSetting(14, s));
                    break;

                case 16: // '\020'
                    _programDefine = true;
                    _mainRawWebSetting = new FieldRawWebSetting(16, s);
                    break;

                case 17: // '\021'
                    if(s != null && s.indexOf("1") == 0)
                        if(s.length() > 2)
                        {
                            s = s.substring(2);
                            s = replaceFieldValueSymbol(s, false, ((String) (null)), _fn);
                        } else
                        {
                            s = null;
                        }
                    if(s != null)
                    {
                        _userDefine = WebfacingConstants.replaceInapplicableCharacters(s);
                        _mainRawWebSetting = new FieldRawWebSetting(17, s);
                    }
                    break;

                case 20: // '\024'
                    _mainRawWebSetting = new FieldRawWebSetting(20, s);
                    _dynLabelKey = s;
                    break;

                case 9: // '\t'
                case 10: // '\n'
                case 11: // '\013'
                case 18: // '\022'
                case 19: // '\023'
                default:
                    TagGeneratorLoader taggeneratorloader = TagGeneratorLoader.getTagGeneratorLoader();
                    if(taggeneratorloader.isWSTagDefined(i))
                    {
                        _mainRawWebSetting = new FieldRawWebSetting(i, s);
                        break;
                    }
                    if(taggeneratorloader.isWSSubTagDefined(i))
                        _subRawWebSettings.add(new FieldRawWebSetting(17, s));
                    break;
                }
            }
        }
        catch(Throwable throwable)
        {
            Util.logThrowableMessage("error in FieldWebSettings.initialize()", throwable, false);
        }
    }

    public boolean isMask()
    {
        return _mask;
    }

    public boolean isPrgDefine()
    {
        return _programDefine;
    }

    public boolean isSingleDHTMLElement()
    {
        return getUsrDefineHTML() != null || isPrgDefine() || getGraphic() != null;
    }

    public String masking(String s)
    {
        if(isMask())
        {
            int i = getMaskHigh();
            int j = getMaskLow();
            String s1 = new String("");
            if(i > j && j < s.length())
            {
                if(s.length() < i)
                    i = s.length();
                s = (new StringBuffer(s)).replace(j, i, s1).toString();
            }
        }
        return s;
    }

    public String replaceQuotedFieldValueSymbol(String s)
    {
        if(s != null && !s.equals(""))
        {
            int i = s.indexOf("&{");
            for(int j = -1; i >= 0 && j < s.length() - 1; i = s.indexOf("&{", j + 1))
            {
                s = WebfacingConstants.replaceSubstring(s, s.substring(j + 1, i), HTMLStringTransform.transformQuotedString(s.substring(j + 1, i)));
                j = s.indexOf("}", i + 2);
            }

            s = replaceFieldValueSymbol(s, true, ((String) (null)), _fn);
        }
        return s;
    }

    public static String replaceSpecialCharacters(String s)
    {
        String s1 = s;
        try
        {
            s1 = WebfacingConstants.replaceSpecialCharacters(s1, null);
        }
        catch(Throwable throwable)
        {
            s1 = s;
        }
        return s1;
    }

    public int getTextDisplayLength()
    {
        return _textDisplayLength;
    }

    private void initializeHyperlinks(String s)
    {
        String s1 = s.trim();
        if(s1.endsWith(" 1"))
        {
            s1 = s1.substring(0, s1.length() - 1).trim();
            _overrideBrowserHyperlinkAppearance = false;
        }
        int i = s1.indexOf(" ");
        if(i > 0)
        {
            _hyperLinkType = Integer.parseInt(s1.substring(0, i).trim());
            String s2 = s1.substring(i).trim();
            if(s2 != null)
            {
                StringTokenizer stringtokenizer = new StringTokenizer(s2, "|", false);
                String s3 = null;
                String s4 = null;
                String s5 = null;
                int j = -1;
                int k = -1;
                int l = -1;
                int i1 = stringtokenizer.countTokens();
                if(i1 >= 1)
                {
                    s3 = stringtokenizer.nextToken();
                    j = s3.indexOf("=");
                }
                if(i1 >= 2)
                {
                    s4 = stringtokenizer.nextToken();
                    k = s4.indexOf("=");
                }
                if(i1 >= 3)
                {
                    s5 = stringtokenizer.nextToken();
                    l = s5.indexOf("=");
                }
                switch(_hyperLinkType)
                {
                default:
                    break;

                case 1: // '\001'
                    _URL = s3;
                    break;

                case 2: // '\002'
                    if(j > 0 && j < s3.length() - 1 && k > 0 && k < s4.length() - 1)
                    {
                        _submitFldName = s3.substring(j + 1).trim();
                        _submitValue = s4.substring(k + 1);
                        _submitValue = replaceFieldValueSymbol(_submitValue, false, ((String) (null)), _fn);
                    }
                    break;

                case 3: // '\003'
                    _staticURL = s3.trim();
                    _target = s4;
                    break;

                case 4: // '\004'
                case 5: // '\005'
                case 8: // '\b'
                    if(j > 0 && j < s3.length() - 4)
                    {
                        boolean flag = false;
                        boolean flag1 = false;
                        RecordNode recordnode = _fn.getParentRecord();
                        _cursoredRec = recordnode.getName();
                        if(recordnode.isSFL() || recordnode.isSFLMSG())
                        {
                            flag = true;
                            flag1 = true;
                        }
                        StringTokenizer stringtokenizer1 = new StringTokenizer(s3.substring(j + 3, s3.length() - 1), ".");
                        if(stringtokenizer1.countTokens() == 2)
                        {
                            String s6 = stringtokenizer1.nextToken().trim();
                            if(null == _cursoredRec || !_cursoredRec.equals(s6))
                            {
                                recordnode = FileNode.getFile().getRecord(s6);
                                if(null != recordnode)
                                    if(recordnode.isSFL() || recordnode.isSFLMSG())
                                    {
                                        flag1 = true;
                                    } else
                                    {
                                        flag1 = false;
                                        _cursoredRec = s6;
                                    }
                            }
                        }
                        _cursoredField = stringtokenizer1.nextToken().trim();
                        validateFieldInRecord(_fn, recordnode, _cursoredField);
                        if(flag1)
                        {
                            recordnode = recordnode.getRelatedSFLCTL();
                            _cursoredRec = recordnode.getName();
                            if(flag)
                                _cursoredFieldRRNString = "$<%=rrn%>";
                            else
                                _cursoredFieldRRNString = "$1";
                        } else
                        {
                            _cursoredFieldRRNString = "";
                        }
                        _cursoredRec = replaceSpecialCharacters(_cursoredRec);
                        _cursoredField = replaceSpecialCharacters(_cursoredField);
                    }
                    if(k < s4.length() - 1)
                    {
                        _cursoredFieldData = s4.substring(k + 1);
                        if(_cursoredFieldData != null)
                            _cursoredFieldData = replaceFieldValueSymbol(_cursoredFieldData, false, ((String) (null)), _fn);
                    }
                    if(l > 0 && l < s5.length() - 1)
                        if(_hyperLinkType == 4)
                            _submitFuncKey = s5.substring(l + 1).trim();
                        else
                            _submitJsCall = s5.substring(l + 1).trim();
                    if(_hyperLinkType == 4)
                        if(_cursoredRec == null || _cursoredField == null)
                            _hyperLinkType = 6;
                        else
                        if(_submitFuncKey == null)
                            _hyperLinkType = 5;
                    if(_hyperLinkType != 8)
                        break;
                    if(_cursoredRec == null || _cursoredField == null)
                    {
                        _hyperLinkType = 7;
                        break;
                    }
                    if(_submitJsCall == null)
                        _hyperLinkType = 5;
                    break;

                case 6: // '\006'
                    _submitFuncKey = s3.trim();
                    break;

                case 7: // '\007'
                case 9: // '\t'
                    _submitJsCall = s3.trim();
                    break;
                }
            }
        }
        if(_submitFuncKey != null && !_submitFuncKey.equalsIgnoreCase("logoff") && !_submitFuncKey.equalsIgnoreCase("enter") && !AIDKeyDictionary.isKeyInDictionary(_submitFuncKey))
            _submitFuncKey = null;
    }

    public static String replaceFieldValueSymbol(String s, boolean flag, String s1, FieldNode fieldnode)
    {
        String s2 = null;
        if(!flag)
            s2 = "UNQUOTED_TRANSFORM";
        else
            s2 = "IHTMLStringTransforms.TRIMMED_QUOTED_STRING_TRANSFORM";
        return replaceFieldValueSymbol(s, s1, fieldnode, s2);
    }

    public static String replaceFieldValueSymbol(String s, String s1, FieldNode fieldnode, String s2)
    {
        String s3 = s1;
        Object obj = null;
        Object obj1 = null;
        Object obj2 = null;
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        String s7 = "";
        Object obj3 = null;
        if(null != s && !s.equals(""))
        {
            RecordNode recordnode1 = null;
            if(null != fieldnode)
            {
                recordnode1 = fieldnode.getParentRecord();
                s3 = recordnode1.getName();
            } else
            {
                recordnode1 = FileNode.getFile().getRecord(s1);
            }
            RecordNode recordnode = recordnode1;
            String s4;
            if(null != recordnode1 && (recordnode1.isSFL() || recordnode1.isSFLMSG()))
            {
                s4 = recordnode1.getRelatedSFLCTL().getName();
                flag2 = true;
                flag1 = true;
            } else
            {
                s4 = s3;
            }
            String s5 = s4;
            int i = s.indexOf("&{");
            boolean flag3 = false;
            for(; i >= 0; i = s.indexOf("&{"))
            {
                int j = s.indexOf("}", i + 1);
                Object obj4 = null;
                if(j > i + 2)
                {
                    String s8 = s.substring(i + 2, j);
                    StringTokenizer stringtokenizer = new StringTokenizer(s8, ".");
                    if(3 == stringtokenizer.countTokens())
                    {
                        s5 = stringtokenizer.nextToken();
                        recordnode = FileNode.getFile().getRecord(s5);
                    }
                    String s6 = stringtokenizer.nextToken();
                    validateFieldInRecord(fieldnode, recordnode, s6);
                    if(!s3.equals(s5) && !s4.equals(s5))
                    {
                        flag = true;
                        RecordNode recordnode2 = FileNode.getFile().getRecord(s5);
                        if(null != recordnode2 && (recordnode2.isSFL() || recordnode2.isSFLMSG()))
                        {
                            flag1 = true;
                            s5 = recordnode2.getRelatedSFLCTL().getName();
                        } else
                        {
                            flag1 = false;
                        }
                    }
                    s5 = replaceSpecialCharacters(s5);
                    s6 = replaceSpecialCharacters(s6);
                    PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(100);
                    String s9 = "<%=";
                    String s10 = "%>";
                    if(flag)
                    {
                        s9 = "<% try{out.print(";
                        s10 = ");}catch(Throwable t){} %>";
                        s5 = "((com.ibm.as400ad.webfacing.runtime.view.IDisplayRecord)request.getAttribute(\"" + s5 + "\"))";
                    }
                    paddedstringbuffer.append(s9);
                    if(flag1)
                        if(flag && !flag2)
                            s7 = "1,";
                        else
                            s7 = "rrn,";
                    paddedstringbuffer.concat(s5, ".getFieldValueWithTransform(\"", s6, "\",", s7, s2, ")");
                    paddedstringbuffer.append(s10);
                    s = WebfacingConstants.replaceSubstring(s, s.substring(i, j + 1), paddedstringbuffer.toString());
                }
            }

        }
        return s;
    }

    public static String replaceHyperlinkFieldValueSymbol(String s, String s1, FieldNode fieldnode)
    {
        return replaceFieldValueSymbol(s, s1, fieldnode, "IHTMLStringTransforms.HYPERLINK_TRANSFORM");
    }

    public String getCondHyperlink(String s, String s1, String s2)
    {
        if(s1 == null || s1.equalsIgnoreCase("enter") || s1.equalsIgnoreCase("logoff"))
            s1 = "null";
        else
            s1 = "\"" + s1 + "\"";
        if(s2 == null)
            s2 = "null";
        else
            s2 = "\"" + s2 + "\"";
        return " <% if (!" + _curRecBeanName + ".disableHyperlink(" + s1 + "," + s2 + ")) {%>" + s + "<%}%> ";
    }

    Iterator getSubRawWebSettings()
    {
        return _subRawWebSettings.iterator();
    }

    IRawWebSetting getMainWebSetting()
    {
        return _mainRawWebSetting;
    }

    private static void validateFieldInRecord(FieldNode fieldnode, RecordNode recordnode, String s)
    {
        if(null != fieldnode && null != recordnode && null != s)
        {
            com.ibm.as400ad.code400.dom.FieldNodeEnumeration fieldnodeenumeration = recordnode.getFields();
            boolean flag = false;
            while(fieldnodeenumeration.hasMoreElements()) 
            {
                FieldNode fieldnode1 = (FieldNode)fieldnodeenumeration.nextElement();
                if(null != fieldnode1 && s.equals(fieldnode1.getName()))
                    flag = true;
            }
            if(!flag)
            {
                String as[] = new String[2];
                as[0] = s;
                as[1] = recordnode.getName();
                fieldnode.logEvent(43, as);
            }
        }
    }

    public String getDynLabelKey()
    {
        return _dynLabelKey;
    }

    public static final String copyRight = new String(" (C) Copyright IBM Corporation 1999-2003, all rights reserved");
    static final String FIELD_VALUE_SYMBOL = "&FieldValue";
    static final String DATE_VALUE_SYMBOL = "&Date";
    static final String SYSTEM_NAME_VALUE_SYMBOL = "&SystemName";
    static final String TIME_VALUE_SYMBOL = "&Time";
    static final String USER_ID_VALUE_SYMBOL = "&UserID";
    private ArrayList _subRawWebSettings;
    private IRawWebSetting _mainRawWebSetting;
    private FieldNode _fn;
    private String _curRecBeanName;
    private int _hyperLinkType;
    private String _submitFldName;
    private String _submitValue;
    private boolean _overrideBrowserHyperlinkAppearance;
    private int _rowSpan;
    private int _colSpan;
    private int _row;
    private int _column;
    private Vector _labelsForVALUES;
    private String _graphic;
    private int _heightInPixel;
    private String _htmlAfter;
    private String _htmlBefore;
    private String _htmlInside;
    private String _URL;
    private String _staticURL;
    private String _target;
    private String _cursoredRec;
    private String _cursoredFieldRRNString;
    private String _cursoredField;
    private String _cursoredFieldData;
    private String _submitFuncKey;
    private String _submitJsCall;
    private boolean _mask;
    private int _maskHigh;
    private int _maskLow;
    private boolean _programDefine;
    private String _style;
    private String _text;
    private String _userDefine;
    private int _widthInPixel;
    private int _textDisplayLength;
    private String _dynLabelKey;

}
