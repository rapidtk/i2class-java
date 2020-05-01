// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import java.io.Serializable;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.host:
//            HJIRequestSet, HJIDatSepRequest, HJIDecFmtRequest, HJITimSepRequest, 
//            HJIDatFmtRequest, HJIMessageRequest, HJISysNameRequest, HJISysTimeRequest, 
//            HJIPFieldMappingStringRequest, HJIUserRequest, HJICurSymRequest, HJIDateRequest, 
//            HJIResolveObjLibRequest, IHostRequest, HJIRequest, DateType, 
//            CenturyType, SeparatorType

public class HostJobInfo
    implements Serializable
{

    public char getDateSeparator()
    {
        ensureEditingInfoLoaded();
        return _dateSeparator;
    }

    public String getDateFormat()
    {
        ensureEditingInfoLoaded();
        return _dateFormat;
    }

    public char getDecimalSeparator()
    {
        ensureEditingInfoLoaded();
        return _decimalSeparator;
    }

    public char getThousandSeparator()
    {
        ensureEditingInfoLoaded();
        return _thousandSeparator;
    }

    public boolean isQdecfmtJValue()
    {
        ensureEditingInfoLoaded();
        return _qdecfmtJValue;
    }

    public void addRequest(HJIRequest hjirequest)
    {
        if(null == _pendingRequests)
            _pendingRequests = new HJIRequestSet();
        _pendingRequests.add(hjirequest);
    }

    private void ensureEditingInfoLoaded()
    {
        if(!_isEditingInfoLoaded)
        {
            HJIDatSepRequest hjidatseprequest = new HJIDatSepRequest();
            HJIDecFmtRequest hjidecfmtrequest = new HJIDecFmtRequest();
            HJITimSepRequest hjitimseprequest = new HJITimSepRequest();
            HJIDatFmtRequest hjidatfmtrequest = new HJIDatFmtRequest();
            addRequest(hjidatseprequest);
            addRequest(hjidecfmtrequest);
            addRequest(hjitimseprequest);
            addRequest(hjidatfmtrequest);
            submitRequests();
            setDecimalFormat(hjidecfmtrequest.getDecimalFormat());
            setDateSeparator(hjidatseprequest.getDateSeparator());
            setTimeSeparator(hjitimseprequest.getTimeSeparator());
            setDateFormat(hjidatfmtrequest.getDateFormat());
            _isEditingInfoLoaded = true;
        }
    }

    public String getMessageText(String s, String s1)
    {
        return getMessageText(s, s1, "     *LIBL", null);
    }

    public String getMessageText(String s, String s1, String s2)
    {
        return getMessageText(s, s1, s2, null);
    }

    public String getMessageText(String s, String s1, String s2, String s3)
    {
        HJIMessageRequest hjimessagerequest = new HJIMessageRequest(s, s1, s2, s3);
        submitSingleRequest(hjimessagerequest);
        return hjimessagerequest.getMessageText();
    }

    private void setDateSeparator(char c)
    {
        switch(c)
        {
        case 45: // '-'
            _dateSeparator = '-';
            break;

        case 46: // '.'
            _dateSeparator = '.';
            break;

        case 44: // ','
            _dateSeparator = ',';
            break;

        case 32: // ' '
            _dateSeparator = ' ';
            break;

        default:
            _dateSeparator = '/';
            break;
        }
    }

    private void setDecimalFormat(char c)
    {
        switch(c)
        {
        case 74: // 'J'
            _decimalSeparator = ',';
            _thousandSeparator = '.';
            _qdecfmtJValue = true;
            break;

        case 73: // 'I'
            _decimalSeparator = ',';
            _thousandSeparator = '.';
            _qdecfmtJValue = false;
            break;

        default:
            _decimalSeparator = '.';
            _thousandSeparator = ',';
            _qdecfmtJValue = false;
            break;
        }
    }

    public String getSystemName()
    {
        if(_systemName == null)
        {
            HJISysNameRequest hjisysnamerequest = new HJISysNameRequest();
            submitSingleRequest(hjisysnamerequest);
            _systemName = hjisysnamerequest.getSystemName();
        }
        return _systemName;
    }

    public String getSystemTime()
    {
        HJISysTimeRequest hjisystimerequest = new HJISysTimeRequest();
        submitSingleRequest(hjisystimerequest);
        return hjisystimerequest.getSystemTime();
    }

    public String getPFIELDMappingString()
    {
        if(_pfieldConvMap == null)
        {
            HJIPFieldMappingStringRequest hjipfieldmappingstringrequest = new HJIPFieldMappingStringRequest();
            submitSingleRequest(hjipfieldmappingstringrequest);
            _pfieldConvMap = hjipfieldmappingstringrequest.getPFIELDMappingString();
        }
        return _pfieldConvMap;
    }

    public String getUserID()
    {
        if(_userID == null)
        {
            HJIUserRequest hjiuserrequest = new HJIUserRequest();
            submitSingleRequest(hjiuserrequest);
            _userID = hjiuserrequest.getUserName();
        }
        return _userID;
    }

    public String getValidationMessage(int i)
    {
        return null;
    }

    public HostJobInfo(IHostRequest ihostrequest)
    {
        _isEditingInfoLoaded = false;
        _systemName = null;
        _userID = null;
        _currencySymbol = null;
        _pfieldConvMap = null;
        _connection = ihostrequest;
    }

    public void submitRequests()
    {
        if(null != _pendingRequests)
        {
            _pendingRequests.submit(_connection);
            _pendingRequests = null;
        }
    }

    void submitSingleRequest(HJIRequest hjirequest)
    {
        HJIRequestSet hjirequestset = new HJIRequestSet();
        hjirequestset.add(hjirequest);
        hjirequestset.submit(_connection);
    }

    public char getCurrencySymbol()
    {
        if(_currencySymbol == null)
        {
            HJICurSymRequest hjicursymrequest = new HJICurSymRequest();
            submitSingleRequest(hjicursymrequest);
            _currencySymbol = new Character(hjicursymrequest.getCurrencySymbol());
        }
        return _currencySymbol.charValue();
    }

    public String getDate(DateType datetype)
    {
        HJIDateRequest hjidaterequest = new HJIDateRequest(datetype);
        submitSingleRequest(hjidaterequest);
        return hjidaterequest.getDate();
    }

    public String getDate(DateType datetype, CenturyType centurytype)
    {
        HJIDateRequest hjidaterequest = new HJIDateRequest(datetype, centurytype);
        submitSingleRequest(hjidaterequest);
        return hjidaterequest.getDate();
    }

    public String getDate(DateType datetype, CenturyType centurytype, SeparatorType separatortype)
    {
        HJIDateRequest hjidaterequest = new HJIDateRequest(datetype, centurytype, separatortype);
        submitSingleRequest(hjidaterequest);
        return hjidaterequest.getDate();
    }

    public char getTimeSeparator()
    {
        ensureEditingInfoLoaded();
        return _timeSeparator;
    }

    private void setTimeSeparator(char c)
    {
        _timeSeparator = c;
    }

    private void setDateFormat(String s)
    {
        _dateFormat = s;
    }

    public String getObjLibName(String s, String s1, String s2)
    {
        return getObjLibName(s, s1, s2, false);
    }

    public String getObjLibName(String s, String s1, String s2, boolean flag)
    {
        HJIResolveObjLibRequest hjiresolveobjlibrequest = new HJIResolveObjLibRequest(s, s1, s2, flag);
        submitSingleRequest(hjiresolveobjlibrequest);
        return hjiresolveobjlibrequest.getObjLibName();
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    private IHostRequest _connection;
    private HJIRequestSet _pendingRequests;
    private char _dateSeparator;
    private String _dateFormat;
    private char _decimalSeparator;
    private char _thousandSeparator;
    private boolean _qdecfmtJValue;
    private boolean _isEditingInfoLoaded;
    private String _systemName;
    private String _userID;
    private Character _currencySymbol;
    private char _timeSeparator;
    private String _pfieldConvMap;
}
