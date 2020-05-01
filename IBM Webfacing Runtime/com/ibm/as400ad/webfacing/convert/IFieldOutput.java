// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert;

import com.ibm.as400ad.code400.dom.FieldNode;
import com.ibm.as400ad.webfacing.convert.gen.bean.JavaSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.ClientScriptSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.DHTMLSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.model.FieldArea;
import com.ibm.as400ad.webfacing.convert.model.FieldVisibility;
import com.ibm.as400ad.webfacing.convert.model.FieldWebSettings;
import com.ibm.as400ad.webfacing.convert.model.RecordLayout;
import com.ibm.etools.iseries.webfacing.convert.external.IFieldInput;
import com.ibm.etools.iseries.webfacing.convert.external.IRawWebSetting;
import java.util.ArrayList;
import java.util.Iterator;

public interface IFieldOutput
    extends IFieldInput
{

    public abstract ClientScriptSourceCodeCollection getClientScript();

    public abstract String getClientScriptLocation();

    public abstract int getColumn();

    public abstract String getConditioning();

    public abstract JavaSourceCodeCollection getDataBeanInitialization();

    public abstract DHTMLSourceCodeCollection getDHTML();

    public abstract int getDisplaySizeIndex();

    public abstract FieldArea getFieldArea();

    public abstract String getFieldId();

    public abstract String getFieldName();

    public abstract FieldNode getFieldNode();

    public abstract String getFieldText();

    public abstract String getFieldTextWithTransform(int i);

    public abstract int getHeight();

    public abstract String getIsFieldVisibleCall();

    public abstract DHTMLSourceCodeCollection getOutOfFlowHTML();

    public abstract String getQualifiedFieldName();

    public abstract int getRow();

    public abstract String getTagId();

    public abstract String getTDAttributes();

    public abstract JavaSourceCodeCollection getViewBeanInitialization();

    public abstract int getWidth();

    public abstract boolean hasKeyLabelDetected();

    public abstract boolean hasOutOfFlowHTML();

    public abstract boolean isScriptableInvisibleField();

    public abstract boolean isSingleDHTMLElement();

    public abstract boolean isWrapped();

    public abstract void setDisplaySizeIndex(int i);

    public abstract boolean useLargestRectangle();

    public abstract FieldVisibility getFieldVisibility();

    public abstract Iterator getSubWebSettings();

    public abstract IRawWebSetting getMainWebSetting();

    public abstract void addHtmlHeader(String s, String s1);

    public abstract ArrayList getHtmlHeader();

    public abstract String getSubTag(IRawWebSetting irawwebsetting);

    public abstract void setTag(String s);

    public abstract String getTagDHTML();

    public abstract void clearTagDHTML();

    public abstract void backupFieldDHTML();

    public abstract String getFieldDHTML();

    public abstract FieldWebSettings getFieldWebSettings();

    public abstract void setRecordLayout(RecordLayout recordlayout);

    public abstract RecordLayout getRecordLayout();
}
