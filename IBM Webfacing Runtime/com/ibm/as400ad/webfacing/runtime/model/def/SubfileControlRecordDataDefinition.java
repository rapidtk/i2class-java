// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model.def;

import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.runtime.controller.IReadOutputBuffer;
import com.ibm.as400ad.webfacing.runtime.controller.RecordBeanFactory;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.model.RecordDataBean;
import com.ibm.as400ad.webfacing.runtime.model.SubfileControlRecordDataBean;
import java.io.IOException;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model.def:
//            RecordDataDefinition

/**
 * @deprecated Class SubfileControlRecordDataDefinition is deprecated
 */

public class SubfileControlRecordDataDefinition extends RecordDataDefinition
{

    public SubfileControlRecordDataDefinition(String s)
    {
        super(s);
        _subfilePageSize = 0;
        _subfileSize = 0;
        _subfileSizeFieldName = null;
        _subfileRecordNumberFieldName = null;
        _subfileScrollFieldName = null;
        _recordsPerRow = 1;
        _isSubfileFoldByDefault = true;
        _rowPerSubfile = -1;
        _SFLROLVALFieldName = null;
    }

    public RecordDataBean createRecordDataBean(IReadOutputBuffer ireadoutputbuffer, RecordBeanFactory recordbeanfactory)
        throws WebfacingLevelCheckException, WebfacingInternalException, IOException
    {
        return new SubfileControlRecordDataBean(this, loadSubfileDefinition(recordbeanfactory), ireadoutputbuffer);
    }

    public int getPageSize()
    {
        return _subfilePageSize;
    }

    public int getRecordsPerRow()
    {
        return _recordsPerRow;
    }

    public int getRowPerSubfile()
    {
        return _rowPerSubfile;
    }

    public String getSFLROLVALFieldName()
    {
        return _SFLROLVALFieldName;
    }

    public String getSubfileRecordNumberFieldName()
    {
        return _subfileRecordNumberFieldName;
    }

    public String getSubfileScrollFieldName()
    {
        return _subfileScrollFieldName;
    }

    public int getSubfileSize(SubfileControlRecordDataBean subfilecontrolrecorddatabean)
    {
        if(_subfileSizeFieldName == null)
            return _subfileSize;
        int i = 0;
        try
        {
            i = Integer.parseInt(subfilecontrolrecorddatabean.getFieldValue(_subfileSizeFieldName).toString());
        }
        catch(Exception exception)
        {
            i = _subfilePageSize + 1;
        }
        if(i <= _subfilePageSize)
            i = _subfilePageSize + 1;
        return i;
    }

    public boolean isSubfileFoldByDefault()
    {
        return _isSubfileFoldByDefault;
    }

    private RecordDataDefinition loadSubfileDefinition(RecordBeanFactory recordbeanfactory)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        String s = getRecordClassName();
        s = PaddedStringBuffer.replaceSubstring(s, getName(), _subfileName);
        return (RecordDataDefinition)recordbeanfactory.loadRecordDataDefinition(s);
    }

    public void setPageSize(int i)
    {
        _subfilePageSize = i;
    }

    public void setRecordsPerRow(int i)
    {
        _recordsPerRow = i;
    }

    public void setRowPerSubfile(int i)
    {
        _rowPerSubfile = i;
    }

    public void setSFLROLVALFieldName(String s)
    {
        _SFLROLVALFieldName = s;
    }

    public void setSubfileFoldByDefault(boolean flag)
    {
        _isSubfileFoldByDefault = flag;
    }

    public void setSubfileName(String s)
    {
        _subfileName = s;
    }

    public void setSubfileRecordNumberFieldName(String s)
    {
        _subfileRecordNumberFieldName = s;
    }

    public void setSubfileScrollFieldName(String s)
    {
        _subfileScrollFieldName = s;
    }

    public void setSubfileSize(int i)
    {
        _subfileSize = i;
    }

    public void setSubfileSizeFieldName(String s)
    {
        _subfileSizeFieldName = s;
    }

    public String getSubfileName()
    {
        return _subfileName;
    }

    public String getSubfileSizeFieldName()
    {
        return _subfileSizeFieldName;
    }

    public int getSubfileSize()
    {
        return _subfileSize;
    }

    public static final String copyRight = "(C) Copyright IBM Corporation 1999-2003 all rights reserved";
    private int _subfilePageSize;
    private int _subfileSize;
    private String _subfileSizeFieldName;
    private String _subfileRecordNumberFieldName;
    private String _subfileScrollFieldName;
    private int _recordsPerRow;
    private boolean _isSubfileFoldByDefault;
    private int _rowPerSubfile;
    private String _subfileName;
    private String _SFLROLVALFieldName;
}
