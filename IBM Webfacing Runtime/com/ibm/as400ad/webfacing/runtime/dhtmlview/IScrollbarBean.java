// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.dhtmlview;

import com.ibm.as400ad.webfacing.runtime.view.IBuildSFLCTLViewBean;
import java.io.Serializable;
import javax.servlet.http.HttpSession;

public interface IScrollbarBean
    extends Serializable
{

    public abstract String getHTMLSource();

    public abstract String getPositioningHTMLSource();

    public abstract String getPositioningHTMLSource(int i);

    public abstract void setContext(HttpSession httpsession);

    public abstract void setControlRecordViewBean(IBuildSFLCTLViewBean ibuildsflctlviewbean);

    public abstract void setScrollbarJavascriptID(int i, String s);

    public abstract void setScrollbarJavascriptID(String s);

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2002, all rights reserved");

}
