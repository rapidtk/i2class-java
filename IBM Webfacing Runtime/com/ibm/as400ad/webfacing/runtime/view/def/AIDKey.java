// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.webfacing.runtime.view.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            CommandKeyLabel

public class AIDKey
    implements ICommandKeyLabel, IClientAIDKey, Cloneable
{

    public AIDKey(String s)
    {
        this(s, "");
    }

    public AIDKey(String s, String s1)
    {
        this(s, s1, -1);
    }

    public AIDKey(String s, String s1, int i)
    {
        this(s, s1, null, i, ((String) (null)));
    }

    public AIDKey(String s, String s1, int i, String s2)
    {
        _shownOnClient = true;
        _keyName = "";
        _overridingURI = "";
        _overridingTargetFrame = "";
        _overridingLabel = "";
        _keyName = s;
        _isFunctionKey = AIDKeyDictionary.isFunctionKey(s);
        _isCommandKey = AIDKeyDictionary.isCommandKey(s);
        _label = new CommandKeyLabel(s, s1, i);
        _indicatorExpression = s2;
    }

    public AIDKey(String s, String s1, String s2, int i)
    {
        this(s, s1, s2, i, ((String) (null)));
    }

    public AIDKey(String s, String s1, String s2, int i, boolean flag)
    {
        this(s, s1, s2, i);
        _shownOnClient = flag;
    }

    public AIDKey(String s, String s1, String s2, int i, String s3)
    {
        _shownOnClient = true;
        _keyName = "";
        _overridingURI = "";
        _overridingTargetFrame = "";
        _overridingLabel = "";
        _keyName = s;
        _isFunctionKey = AIDKeyDictionary.isFunctionKey(s);
        _isCommandKey = AIDKeyDictionary.isCommandKey(s);
        _label = new CommandKeyLabel(s, s1, s2, i);
        _indicatorExpression = s3;
    }

    public AIDKey(String s, String s1, String s2, int i, String s3, boolean flag)
    {
        this(s, s1, s2, i, s3);
        _shownOnClient = flag;
    }

    public Object clone()
    {
        AIDKey aidkey = null;
        try
        {
            aidkey = (AIDKey)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        aidkey._label = (CommandKeyLabel)_label.clone();
        return aidkey;
    }

    public boolean isCommandKey()
    {
        return _isCommandKey;
    }

    public boolean isEnterKey()
    {
        return getKeyCode().byteValue() == AIDKeyDictionary.ADBD_AIDKEY_ENTER;
    }

    public boolean isFunctionKey()
    {
        return _isFunctionKey;
    }

    public Byte getKeyCode()
    {
        return _label.getKeyCode();
    }

    public String getKeyLabel()
    {
        return _label.getKeyLabel();
    }

    public String getKeyName()
    {
        return _keyName;
    }

    public String transformFKeyName(String s)
    {
        try
        {
            if(AIDKeyDictionary.isKeyInDictionary(s) && AIDKeyDictionary.isCommandKey(s))
                return "F" + Integer.parseInt(s.substring(2));
            else
                return s;
        }
        catch(Exception exception)
        {
            return s;
        }
    }

    public int getPriority()
    {
        return _label.getPriority();
    }

    public void setLabel(CommandKeyLabel commandkeylabel)
    {
        _label = commandkeylabel;
    }

    public CommandKeyLabel getLabel()
    {
        return _label;
    }

    public void setKeyLabel(String s)
    {
        _label.setKeyLabel(s);
    }

    public String getRecordName()
    {
        return _label.getRecordName();
    }

    public String getFieldName()
    {
        return _label.getFieldName();
    }

    public String getIndicatorExpression()
    {
        return _indicatorExpression;
    }

    public void setRecordName(String s)
    {
        _label.setRecordName(s);
    }

    public boolean isHelpKey()
    {
        return getKeyCode().byteValue() == AIDKeyDictionary.ADBD_AIDKEY_HELP;
    }

    public boolean isKeyShownOnClient()
    {
        return _shownOnClient;
    }

    public String getOverridingLabel()
    {
        return _overridingLabel;
    }

    public boolean isOverriden()
    {
        return _isOverriden;
    }

    public String getOverridingTargetFrame()
    {
        return _overridingTargetFrame;
    }

    public String getOverridingURI()
    {
        return _overridingURI;
    }

    public void setOverridingLabel(String s)
    {
        _overridingLabel = s;
    }

    public void setIsOverriden(boolean flag)
    {
        _isOverriden = flag;
    }

    public void setOverridingTargetFrame(String s)
    {
        _overridingTargetFrame = s;
    }

    public void setOverridingURI(String s)
    {
        _overridingURI = s;
    }

    public void setOverridingInfo(String s, String s1, String s2)
    {
        _isOverriden = true;
        if(s == null)
            _overridingURI = "";
        else
            _overridingURI = s;
        if(s1 == null)
            _overridingTargetFrame = "";
        else
            _overridingTargetFrame = s1;
        if(s2 == null)
            _overridingLabel = "";
        else
            _overridingLabel = s2;
    }

    public static final String copyright = new String("(c) Copyright IBM Corporation 2003, all rights reserved");
    private static AIDKeyDictionary _KeyDictionary;
    private boolean _shownOnClient;
    private String _indicatorExpression;
    private boolean _isFunctionKey;
    private boolean _isCommandKey;
    private CommandKeyLabel _label;
    private String _keyName;
    private boolean _isOverriden;
    private String _overridingURI;
    private String _overridingTargetFrame;
    private String _overridingLabel;

}
