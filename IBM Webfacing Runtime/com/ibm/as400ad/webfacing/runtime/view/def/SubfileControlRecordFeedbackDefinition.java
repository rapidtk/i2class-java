// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.model.IRecordData;
import com.ibm.as400ad.webfacing.runtime.model.ISFLCTLRecordData;
import com.ibm.as400ad.webfacing.runtime.view.RecordFeedbackBean;
import com.ibm.as400ad.webfacing.runtime.view.SubfileControlRecordFeedbackBean;
import java.util.ResourceBundle;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            RecordFeedbackDefinition, ResponseIndicator

/**
 * @deprecated Class SubfileControlRecordFeedbackDefinition is deprecated
 */

public class SubfileControlRecordFeedbackDefinition extends RecordFeedbackDefinition
{

    public SubfileControlRecordFeedbackDefinition(String s)
    {
        super(s);
        _subfileModeFieldName = null;
        _subfileCursorRRNFieldName = null;
        _subfileFoldKey = null;
        _subfileDropKey = null;
        _subfileContainer = new ElementContainer();
        _rowPerSubfile = 1;
        _subfileRecordNumberFieldName = null;
        _subfileScrollFieldName = null;
        _isSFLENDScrollBar = false;
    }

    public void addForSubfile(ResponseIndicator responseindicator)
    {
        getSubfileContainer().add(responseindicator);
    }

    public RecordFeedbackBean createFeedbackBean(IRecordData irecorddata)
        throws WebfacingInternalException
    {
        try
        {
            return new SubfileControlRecordFeedbackBean(this, (ISFLCTLRecordData)irecorddata);
        }
        catch(ClassCastException classcastexception)
        {
            throw new WebfacingInternalException(WebfacingConstants.replaceSubstring(_resmri.getString("WF0052"), "&1", getName()));
        }
    }

    public Object getForSubfile(IKey ikey, Object obj)
    {
        return getSubfileContainer().get(ikey, obj);
    }

    public int getRowPerSubfile()
    {
        return _rowPerSubfile;
    }

    public ElementContainer getSubfileContainer()
    {
        return _subfileContainer;
    }

    public String getSubfileCursorRRNFieldName()
    {
        return _subfileCursorRRNFieldName;
    }

    public String getSubfileDropKey()
    {
        return _subfileDropKey;
    }

    public String getSubfileFoldKey()
    {
        return _subfileFoldKey;
    }

    public String getSubfileModeFieldName()
    {
        return _subfileModeFieldName;
    }

    public String getSubfileRecordNumberFieldName()
    {
        return _subfileRecordNumberFieldName;
    }

    public String getSubfileScrollFieldName()
    {
        return _subfileScrollFieldName;
    }

    public boolean isSFLENDScrollBar()
    {
        return _isSFLENDScrollBar;
    }

    public void setRowPerSubfile(int i)
    {
        _rowPerSubfile = i;
    }

    public void setSFLENDScrollBar(boolean flag)
    {
        _isSFLENDScrollBar = flag;
    }

    public void setSubfileCursorRRNFieldName(String s)
    {
        _subfileCursorRRNFieldName = s;
    }

    public void setSubfileDropKey(String s)
    {
        _subfileDropKey = s;
    }

    public void setSubfileFoldKey(String s)
    {
        _subfileFoldKey = s;
    }

    public void setSubfileModeFieldName(String s)
    {
        _subfileModeFieldName = s;
    }

    public void setSubfileRecordNumberFieldName(String s)
    {
        _subfileRecordNumberFieldName = s;
    }

    public void setSubfileScrollFieldName(String s)
    {
        _subfileScrollFieldName = s;
    }

    private String _subfileModeFieldName;
    private String _subfileCursorRRNFieldName;
    private String _subfileFoldKey;
    private String _subfileDropKey;
    private static ResourceBundle _resmri;
    private ElementContainer _subfileContainer;
    private int _rowPerSubfile;
    private String _subfileRecordNumberFieldName;
    private String _subfileScrollFieldName;
    private boolean _isSFLENDScrollBar;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
