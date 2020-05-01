// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.xml;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

// Referenced classes of package com.ibm.etools.iseries.webfacing.xml:
//            XMLException, XAttribute, XElement, XDocumentReader, 
//            XDocument

public class XDocumentStore
{

    public XDocumentStore()
    {
        _utf8 = false;
        _file = null;
        _url = null;
        _iMode = 0;
        _reader = null;
        _strLine = null;
        _parserElementCurrent = null;
        _parserElementRoot = null;
        _parserTextAttributeCurrent = null;
        _iLine = -1;
        _iColumn = -1;
        _inputStream = null;
        _lineReader = null;
    }

    public File createFile(URL url)
        throws XMLException
    {
        String s = url.getFile().replace('/', File.separatorChar);
        int i = s.indexOf(":");
        if(i != -1 && s.startsWith(File.separator))
            s = s.substring(1);
        File file = new File(s);
        boolean flag = file.exists();
        if(!flag)
            try
            {
                boolean flag1 = file.createNewFile();
                return file;
            }
            catch(IOException ioexception)
            {
                throw new XMLException("Unable to open", s, (String)null, -1, -1);
            }
        else
            return file;
    }

    protected void handleAttribute()
        throws XMLException
    {
        StringBuffer stringbuffer = new StringBuffer();
        StringBuffer stringbuffer1 = new StringBuffer();
        byte byte0 = -1;
        byte byte1 = 32;
        for(; _iColumn < _strLine.length(); _iColumn++)
        {
            char c = _strLine.charAt(_iColumn);
            if(c == ' ' || c == '\t' || c == '=')
                break;
            if(c == '\n' || c == '\r')
                throw new XMLException("Unexpected end of line", _url.toString(), _strLine, _iLine, _iColumn);
            stringbuffer.append(c);
        }

        for(; _iColumn < _strLine.length(); _iColumn++)
        {
            char c1 = _strLine.charAt(_iColumn);
            if(c1 == ' ' || c1 == '\t' || c1 != '=')
                continue;
            _iColumn++;
            break;
        }

        for(; _iColumn < _strLine.length(); _iColumn++)
        {
            char c2 = _strLine.charAt(_iColumn);
            if(c2 == ' ' || c2 == '\t' || c2 != '"')
                continue;
            int i = ++_iColumn;
            break;
        }

        for(; _iColumn < _strLine.length(); _iColumn++)
        {
            char c3 = _strLine.charAt(_iColumn);
            if(c3 == '"')
            {
                _iColumn++;
                _iMode = 5;
                break;
            }
            if(c3 == '&')
            {
                StringBuffer stringbuffer2 = new StringBuffer();
                for(_iColumn++; _strLine.charAt(_iColumn) != ';'; _iColumn++)
                    stringbuffer2.append(_strLine.charAt(_iColumn));

                String s = stringbuffer2.toString();
                if(s.equals("quot"))
                    c3 = '"';
                else
                if(s.equals("lt"))
                    c3 = '<';
                else
                if(s.equals("gt"))
                    c3 = '>';
                else
                if(s.equals("amp"))
                    c3 = '&';
            }
            stringbuffer1.append(c3);
        }

        if(stringbuffer.length() > 0)
        {
            XAttribute xattribute = new XAttribute(_parserElementCurrent, stringbuffer.toString(), stringbuffer1.toString());
            if(_parserElementCurrent != null)
                _parserElementCurrent.addAttribute(xattribute);
        }
    }

    protected void handleComment()
    {
        String s = null;
        int i = _strLine.indexOf("-->", _iColumn);
        if(i > _iColumn)
        {
            s = _strLine.substring(_iColumn, i);
            _iColumn = i + 3;
            _iMode = 0;
        } else
        {
            s = _strLine.substring(_iColumn);
            _iColumn = _strLine.length();
            _iMode = 2;
        }
        if(s != null && _parserTextAttributeCurrent != null)
            _parserTextAttributeCurrent._strValue = _parserTextAttributeCurrent._strValue + s;
    }

    protected void handleCommentStart()
    {
        String s = null;
        int i = _iColumn + 4;
        int j = _strLine.indexOf("-->");
        if(j > _iColumn + 4)
        {
            s = _strLine.substring(_iColumn + 4, j);
            _iColumn = j + 3;
            _iMode = 0;
        } else
        {
            s = _strLine.substring(_iColumn + 4);
            _iColumn = _strLine.length();
            _iMode = 2;
        }
        if(s != null)
        {
            XElement xelement = new XElement(_parserElementCurrent, "#comment");
            if(_parserElementCurrent != null)
                _parserElementCurrent.addChildElement(xelement);
            XAttribute xattribute = new XAttribute(xelement, "text", s);
            xelement.addAttribute(xattribute);
            if(_iMode == 2)
                _parserTextAttributeCurrent = xattribute;
        }
    }

    protected void handleDoctype()
    {
        int i = _strLine.indexOf(">");
        if(i > _iColumn)
        {
            _iColumn = i + 1;
            _iMode = 0;
        } else
        {
            _iColumn = _strLine.length();
        }
    }

    protected void handleElement()
    {
        for(; _iColumn < _strLine.length(); _iColumn++)
        {
            char c = _strLine.charAt(_iColumn);
            if(c != ' ' && c != '\t' && c != '\r' && c != '\n')
            {
                if(c == '>')
                {
                    _iColumn++;
                    _iMode = 0;
                } else
                if(_strLine.indexOf("/>", _iColumn) == _iColumn)
                {
                    _iColumn += 2;
                    _iMode = 0;
                    if(_parserElementCurrent != null)
                        _parserElementCurrent = _parserElementCurrent._entryParent;
                } else
                {
                    _iMode = 1;
                }
                break;
            }
        }

    }

    protected void handleElementEnd()
        throws XMLException
    {
        int i = _strLine.indexOf('>', _iColumn);
        if(i > _iColumn + 2)
        {
            String s = _strLine.substring(_iColumn + 2, i);
            if(!s.equals(_parserElementCurrent.getName()))
                throw new XMLException("Expecting end of line" + _parserElementCurrent._strName, _url.toString(), _strLine, _iLine, _iColumn);
            _iColumn = i + 1;
            _iMode = 0;
            if(_parserElementCurrent != null)
                _parserElementCurrent = _parserElementCurrent._entryParent;
        } else
        {
            throw new XMLException("Expecting  \">\"", _url.toString(), _strLine, _iLine, _iColumn);
        }
    }

    protected void handleElementStart()
        throws XMLException
    {
        if(_strLine.indexOf("<!--", _iColumn) == _iColumn)
            _iMode = 3;
        else
        if(_strLine.indexOf("<!", _iColumn) == _iColumn)
            _iMode = 4;
        else
        if(_strLine.indexOf("<?", _iColumn) == _iColumn)
            _iMode = 10;
        else
        if(_strLine.indexOf("</", _iColumn) == _iColumn)
        {
            _iMode = 7;
        } else
        {
            _iColumn++;
            StringBuffer stringbuffer = new StringBuffer();
            for(; _iColumn < _strLine.length(); _iColumn++)
                if(_strLine.charAt(_iColumn) != ' ' && _strLine.charAt(_iColumn) != '\t')
                    break;

            byte byte0 = 32;
            for(; _iColumn < _strLine.length(); _iColumn++)
            {
                char c = _strLine.charAt(_iColumn);
                if(c == '>')
                {
                    _iColumn++;
                    _iMode = 0;
                    break;
                }
                if(c == '\r' || c == '\n')
                    throw new XMLException("Expecting \">\"", _url.toString(), _strLine, _iLine, _iColumn);
                if(_strLine.indexOf("/>", _iColumn) == _iColumn)
                {
                    _iColumn += 2;
                    _iMode = 0;
                    break;
                }
                if(c != ' ' && c != '\t')
                {
                    stringbuffer.append(c);
                    _iMode = 5;
                    continue;
                }
                _iMode = 5;
                break;
            }

            if(stringbuffer.length() > 0)
            {
                XElement xelement = new XElement(_parserElementCurrent, stringbuffer.toString());
                if(_parserElementCurrent != null)
                    _parserElementCurrent.addChildElement(xelement);
                else
                    _parserElementRoot = xelement;
                _parserElementCurrent = xelement;
            }
        }
    }

    protected void handleText()
    {
        String s = null;
        int i = _strLine.indexOf("<", _iColumn);
        if(i > _iColumn)
        {
            s = _strLine.substring(_iColumn, i);
            _iColumn = i;
            _iMode = 0;
        } else
        {
            s = _strLine.substring(_iColumn);
            _iColumn = _strLine.length();
            _iMode = 8;
        }
        if(s != null && _parserTextAttributeCurrent != null)
            _parserTextAttributeCurrent._strValue = _parserTextAttributeCurrent._strValue + "\n" + s;
    }

    protected void handleTextStart()
    {
        String s = null;
        int i = _iColumn;
        int j = _strLine.indexOf("<", _iColumn);
        if(j > _iColumn)
        {
            s = _strLine.substring(_iColumn, j);
            _iColumn = j;
            _iMode = 0;
        } else
        {
            s = _strLine.substring(_iColumn);
            _iColumn = _strLine.length();
            _iMode = 8;
        }
        if(s != null)
        {
            XElement xelement = new XElement(_parserElementCurrent, "#text");
            if(_parserElementCurrent != null)
                _parserElementCurrent.addChildElement(xelement);
            XAttribute xattribute = new XAttribute(xelement, "text", s);
            xelement.addAttribute(xattribute);
            if(_iMode == 8)
                _parserTextAttributeCurrent = xattribute;
        }
    }

    protected void handleUnknown()
    {
        for(; _iColumn < _strLine.length(); _iColumn++)
        {
            char c = _strLine.charAt(_iColumn);
            if(c != ' ' && c != '\t' && c != '\n' && c != '\r')
            {
                if(c == '<')
                    _iMode = 6;
                else
                    _iMode = 9;
                break;
            }
        }

    }

    protected void handleXml()
    {
        if((null != _url || null != _file) && !_utf8 && _strLine.indexOf("UTF-8") > 0)
        {
            int i = _lineReader.getLineNumber();
            try
            {
                Object obj = null;
                if(null != _url)
                {
                    URLConnection urlconnection = _url.openConnection();
                    if(urlconnection != null)
                        obj = urlconnection.getInputStream();
                } else
                {
                    obj = new FileInputStream(_file);
                }
                if(obj != null)
                {
                    try
                    {
                        _inputStream.close();
                    }
                    catch(Exception exception) { }
                    _inputStream = ((InputStream) (obj));
                    InputStreamReader inputstreamreader = new InputStreamReader(_inputStream, "UTF-8");
                    _utf8 = true;
                    _lineReader = new LineNumberReader(inputstreamreader);
                    for(int k = 0; k < i; k++)
                        _lineReader.readLine();

                }
            }
            catch(UnsupportedEncodingException unsupportedencodingexception) { }
            catch(IOException ioexception) { }
        }
        int j = _strLine.indexOf("?>");
        if(j > _iColumn)
        {
            _iColumn = j + 2;
            _iMode = 0;
        } else
        {
            _iColumn = _strLine.length();
        }
    }

    public boolean load(XDocument xdocument, InputStream inputstream, boolean flag)
        throws XMLException
    {
        _utf8 = flag;
        _parserElementCurrent = xdocument;
        _inputStream = inputstream;
        Object obj = null;
        if(_inputStream != null)
        {
            try
            {
                InputStreamReader inputstreamreader;
                if(_utf8)
                    inputstreamreader = new InputStreamReader(_inputStream, "UTF-8");
                else
                    inputstreamreader = new InputStreamReader(_inputStream);
                _lineReader = new LineNumberReader(inputstreamreader);
            }
            catch(Throwable throwable) { }
            _iMode = 0;
            do
            {
                try
                {
                    _strLine = _lineReader.readLine();
                }
                catch(IOException ioexception)
                {
                    _strLine = null;
                }
                if(_strLine != null)
                    processLine();
            } while(_strLine != null);
            if(_parserElementCurrent != null && !_parserElementCurrent.getName().equals("root"))
                throw new XMLException("Expecting end element " + _parserElementCurrent.getName(), _url.toString(), _strLine, _iLine, _iColumn);
            try
            {
                _inputStream.close();
            }
            catch(Exception exception) { }
            return true;
        }
        if(obj instanceof String)
        {
            _reader = new XDocumentReader((String)obj);
            _iMode = 0;
            do
            {
                _strLine = _reader.readLine();
                if(_strLine != null)
                    processLine();
            } while(_strLine != null);
            if(_parserElementCurrent != null)
                throw new XMLException("Expecting end element " + _parserElementCurrent.getName(), _url.toString(), _strLine, _iLine, _iColumn);
            else
                return true;
        } else
        {
            return false;
        }
    }

    public boolean load(XDocument xdocument, URL url)
        throws XMLException
    {
        _url = url;
        _inputStream = null;
        try
        {
            URLConnection urlconnection = url.openConnection();
            if(urlconnection != null)
                _inputStream = urlconnection.getInputStream();
        }
        catch(IOException ioexception) { }
        return load(xdocument, _inputStream, false);
    }

    public boolean load(XDocument xdocument, File file, boolean flag)
        throws XMLException
    {
        _file = file;
        _utf8 = flag;
        _url = null;
        _inputStream = null;
        try
        {
            if(file != null)
                _inputStream = new FileInputStream(_file);
        }
        catch(IOException ioexception) { }
        return load(xdocument, _inputStream, false);
    }

    protected void processLine()
        throws XMLException
    {
        _iLine++;
        for(_iColumn = 0; _iColumn < _strLine.length();)
            switch(_iMode)
            {
            case 0: // '\0'
                handleUnknown();
                break;

            case 1: // '\001'
                handleAttribute();
                break;

            case 2: // '\002'
                handleComment();
                break;

            case 3: // '\003'
                handleCommentStart();
                break;

            case 4: // '\004'
                handleDoctype();
                break;

            case 5: // '\005'
                handleElement();
                break;

            case 6: // '\006'
                handleElementStart();
                break;

            case 7: // '\007'
                handleElementEnd();
                break;

            case 8: // '\b'
                handleText();
                break;

            case 9: // '\t'
                handleTextStart();
                break;

            case 10: // '\n'
                handleXml();
                break;
            }

    }

    public void save(XDocument xdocument, URL url)
        throws XMLException
    {
        URLConnection urlconnection = null;
        try
        {
            urlconnection = url.openConnection();
        }
        catch(IOException ioexception)
        {
            throw new XMLException(ioexception.getLocalizedMessage(), url.toString(), (String)null, -1, -1);
        }
        OutputStream outputstream = null;
        if(urlconnection != null)
            try
            {
                urlconnection.setDoOutput(true);
                outputstream = urlconnection.getOutputStream();
            }
            catch(IOException ioexception1)
            {
                outputstream = null;
            }
        if(outputstream != null)
            try
            {
                outputstream.write(xdocument.getPersistentString().getBytes());
            }
            catch(IOException ioexception2)
            {
                throw new XMLException(ioexception2.getLocalizedMessage(), url.toString(), (String)null, -1, -1);
            }
        else
            saveAsFile(xdocument, url);
    }

    public void saveAsFile(XDocument xdocument, URL url)
        throws XMLException
    {
        boolean flag = false;
        File file = createFile(url);
        String s = xdocument.getPersistentString();
        Object obj = null;
        try
        {
            FileWriter filewriter = new FileWriter(file);
            filewriter.write(s);
            filewriter.flush();
            filewriter.close();
        }
        catch(IOException ioexception)
        {
            throw new XMLException("Unable to write to file", url.getFile(), (String)null, -1, -1);
        }
    }

    public static final String copyright = "(c) Copyright IBM Corporation 2002. all rights reserved";
    private static final int MODE_UNKNOWN = 0;
    private static final int MODE_ATTRIBUTESTART = 1;
    private static final int MODE_COMMENT = 2;
    private static final int MODE_COMMENTSTART = 3;
    private static final int MODE_DOCTYPESTART = 4;
    private static final int MODE_ELEMENT = 5;
    private static final int MODE_ELEMENTSTART = 6;
    private static final int MODE_ELEMENTENDSTART = 7;
    private static final int MODE_TEXT = 8;
    private static final int MODE_TEXTSTART = 9;
    private static final int MODE_XMLSTART = 10;
    protected boolean _utf8;
    protected File _file;
    protected URL _url;
    protected int _iMode;
    protected XDocumentReader _reader;
    protected String _strLine;
    protected XElement _parserElementCurrent;
    protected XElement _parserElementRoot;
    protected XAttribute _parserTextAttributeCurrent;
    protected int _iLine;
    protected int _iColumn;
    protected InputStream _inputStream;
    protected LineNumberReader _lineReader;
}
