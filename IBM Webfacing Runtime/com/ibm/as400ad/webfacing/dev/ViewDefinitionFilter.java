// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.dev;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import java.util.Hashtable;
import java.util.ResourceBundle;

// Referenced classes of package com.ibm.as400ad.webfacing.dev:
//            DefinitionFilter, FilterCode

public class ViewDefinitionFilter extends DefinitionFilter
{

    public ViewDefinitionFilter()
    {
        this(null);
    }

    public ViewDefinitionFilter(String s)
    {
        _classDefinitionFound = false;
        _firstOpenBraceFound = false;
        _mriPropertiesFileIncluded = false;
        _className = null;
        _mappedParmsSetup = false;
        _mappedParms = null;
        _className = s;
    }

    public String filterLine(String s)
    {
        s = super.filterLine(s);
        if(!_classDefinitionFound && s.indexOf("public class") >= 0)
            _classDefinitionFound = true;
        if(_classDefinitionFound && !_firstOpenBraceFound && s.indexOf("{") >= 0)
            _firstOpenBraceFound = true;
        if(_classDefinitionFound && _firstOpenBraceFound && !_mriPropertiesFileIncluded)
        {
            s = s + System.getProperty("line.separator") + "private static java.util.ResourceBundle _resmri = java.util.ResourceBundle.getBundle(\"com/ibm/as400ad/webfacing/runtime/mri\");";
            _mriPropertiesFileIncluded = true;
        }
        if(s.indexOf("new AIDKey") >= 0)
        {
            setMappedParms();
            int i = s.indexOf(",");
            int j = s.substring(i + 1).indexOf(",") + i + 1;
            String s1 = s.substring(i + 1, j);
            int k = s1.indexOf("\"");
            int l = s1.lastIndexOf("\"");
            String s2 = null;
            try
            {
                String s3 = s1.substring(k + 1, l);
                String s4 = getMappedParm(s3);
                if(s4 != null)
                {
                    _resmri.getString(s4);
                    s2 = " _resmri.getString(\"" + s4 + "\")";
                } else
                {
                    return null;
                }
            }
            catch(Exception exception) { }
            if(s2 != null)
                s = replaceSubString(s, s1, s2);
        }
        return s;
    }

    private String getMappedParm(String s)
    {
        if(s.indexOf("CA0") >= 0 || s.indexOf("CA1") >= 0)
            return s;
        else
            return (String)_mappedParms.get(s.toUpperCase());
    }

    private void setMappedParms()
    {
        if(!_mappedParmsSetup)
        {
            _mappedParmsSetup = true;
            _mappedParms = new Hashtable();
            _mappedParms.put("CF03", "Exit");
            _mappedParms.put("CF12", "Cancel");
            _mappedParms.put("CANCEL", "Cancel");
            _mappedParms.put("EXIT", "Exit");
            _mappedParms.put("HELP", "Help");
            if(!"qdspmnu".equals(getPackageFile()) || "qdspmnu".equals(getPackageFile()) && (_className == null || _className.indexOf("HELPFMT") >= 0))
            {
                _mappedParms.put("CLEAR", "CLEAR");
                _mappedParms.put("PAGEUP", "PAGEUP");
                _mappedParms.put("PAGEDOWN", "PAGEDOWN");
            }
        }
    }

    private boolean _classDefinitionFound;
    private boolean _firstOpenBraceFound;
    private boolean _mriPropertiesFileIncluded;
    private String _className;
    private boolean _mappedParmsSetup;
    private static ResourceBundle _resmri;
    private Hashtable _mappedParms;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
