// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.webfacing.runtime.core.IElementContainer;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.runtime.help.HelpArea;
import com.ibm.as400ad.webfacing.runtime.help.HelpGroup;
import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;
import com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition;
import com.ibm.as400ad.webfacing.runtime.view.RecordFeedbackBean;
import com.ibm.as400ad.webfacing.runtime.view.RecordViewBean;
import java.util.Iterator;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            AIDKey, CHKMSGIDDefinition, CommandKeyLabel, VisibilityConditionedCommandKeyLabel, 
//            IndicatorConditionedCommandKeyLabel, DSPATR_PCFieldInfo, FieldViewDefinition, WindowTitleDefinition

public interface IRecordViewDefinition
    extends IElementContainer
{

    public abstract void add(HelpArea helparea);

    public abstract void add(HelpGroup helpgroup);

    public abstract void add(KeywordDefinition keyworddefinition);

    public abstract void add(AIDKey aidkey);

    public abstract void add(CHKMSGIDDefinition chkmsgiddefinition);

    public abstract void add(CommandKeyLabel commandkeylabel);

    public abstract void add(VisibilityConditionedCommandKeyLabel visibilityconditionedcommandkeylabel);

    public abstract void add(IndicatorConditionedCommandKeyLabel indicatorconditionedcommandkeylabel);

    public abstract void add(DSPATR_PCFieldInfo dspatr_pcfieldinfo);

    public abstract void add(FieldViewDefinition fieldviewdefinition);

    public abstract void add(WindowTitleDefinition windowtitledefinition);

    public abstract RecordViewBean createViewBean(RecordFeedbackBean recordfeedbackbean)
        throws WebfacingInternalException;

    public abstract Iterator getCHKMSGIDDefinitions();

    public abstract int getCLRL_NN();

    public abstract Iterator getCommandKeys();

    public abstract Iterator getDspatrPCFieldInfos();

    public abstract Iterator getERASEKeywords();

    public abstract Iterator getERRMSGs();

    public abstract FieldViewDefinition getFieldViewDefinition(String s);

    public abstract Iterator getFieldViewDefinitions();

    public abstract String[] getFieldVisDef();

    public abstract int getFirstColumn();

    public abstract int getFirstFieldLine();

    public abstract String getFirstParmOnKeyword(long l);

    public abstract Iterator getFunctionKeys();

    public abstract Iterator getHelpDefinitions();

    public abstract Iterator getHelpGroups();

    public abstract boolean getIsOutputOnly();

    public abstract Iterator getKeySequence();

    public abstract KeywordDefinition getKeywordDefinition(long l);

    public abstract Iterator getKeywordDefinitions();

    public abstract int getLastColumn();

    public abstract int getLastFieldLine();

    public abstract int getMaxColumn();

    public abstract int getMaxRow();

    public abstract Iterator getCommandKeyLabels();

    public abstract Iterator getVisibilityConditionedCommandKeyLabels();

    public abstract Iterator getIndicatorConditionedCommandKeyLabels();

    public abstract Integer getPrimaryFileDisplaySize();

    public abstract Integer getSecondaryFileDisplaySize();

    public abstract long getVersionDigits();

    public abstract int getWdwHeight();

    public abstract String getWdwRefName();

    public abstract int getWdwStartLine();

    public abstract String getWdwStartLineField();

    public abstract int getWdwStartPos();

    public abstract String getWdwStartPosField();

    public abstract int getWdwWidth();

    public abstract int getWindowStartLine();

    public abstract String getWindowTitle(RecordViewBean recordviewbean);

    public abstract String getWindowTitleAlignment(RecordViewBean recordviewbean);

    public abstract boolean hasERRMessages();

    public abstract boolean hasWindowTitle();

    public abstract boolean isCLRL();

    public abstract boolean isCLRL_ALL();

    public abstract boolean isCLRL_END();

    public abstract boolean isCLRL_NN();

    public abstract boolean isCLRL_NO();

    public abstract boolean isKeywordSpecified(long l);

    public abstract boolean isWdwDFT();

    public abstract boolean isWdwREF();

    public abstract boolean isWide();

    public abstract boolean isWindowed();

    public abstract void setDataDefinition(IRecordDataDefinition irecorddatadefinition);

    public abstract void setFieldVisDef(String as[]);

    public abstract void setFirstColumn(int i);

    public abstract void setFirstFieldLine(int i);

    public abstract void setIsOutputOnly(boolean flag);

    public abstract void setIsWide(boolean flag);

    public abstract void setLastColumn(int i);

    public abstract void setLastFieldLine(int i);

    public abstract void setPrimaryFileDisplaySize(Integer integer);

    public abstract void setSecondaryFileDisplaySize(Integer integer);

    public abstract void setVersionDigits(long l);

    public abstract void setWdwStartLine(int i);

    public abstract void setWdwStartLineField(String s);

    public abstract void setWdwStartPos(int i);

    public abstract void setWdwStartPosField(String s);

    public abstract boolean isERRSFL();

    public abstract Iterator getHelpTitles();

    public abstract Iterator getWindowTitles();

    public static final int UNDEFINED = -1;
    public static final String COMMAND_KEY = "Command Keys";
    public static final String FUNCTION_KEY = "Function Keys";
    public static final String KEY_SEQUENCE = "Key Sequence";
    public static final String COMMAND_KEY_LABELS = "Command Key Labels";
    public static final String VISIBILITY_CONDITIONED_LABELS = "Visibility Conditioned Key Labels";
    public static final String INDICATOR_CONDITIONED_LABELS = "Indicator Conditioned Key Labels";
    public static final int CLRL_ALL = 28;
    public static final int CLRL_END = 29;
    public static final int CLRL_NO = 30;
}
