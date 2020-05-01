// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.diags;

import com.ibm.as400ad.code400.dom.constants.FieldType;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.help.*;
import com.ibm.as400ad.webfacing.runtime.model.def.*;
import com.ibm.as400ad.webfacing.runtime.view.CursorPosition;
import com.ibm.as400ad.webfacing.runtime.view.IndicatorAndRow;
import com.ibm.as400ad.webfacing.runtime.view.def.*;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;

public class RecordDefinitionComparer
{
    private static class RTNCSRLOCDefinitionComparer
        implements IComparer
    {

        public boolean identical(Object obj, Object obj1)
        {
            boolean flag = true;
            if(!obj.getClass().toString().equals(obj1.getClass().toString()))
            {
                RecordDefinitionComparer.out.println("Class types don't match for " + obj + " and " + obj1);
                flag = false;
            }
            if(obj instanceof RTNCSRLOCDefinition_WINDOW)
            {
                RTNCSRLOCDefinition_WINDOW rtncsrlocdefinition_window = (RTNCSRLOCDefinition_WINDOW)obj;
                RTNCSRLOCDefinition_WINDOW rtncsrlocdefinition_window1 = (RTNCSRLOCDefinition_WINDOW)obj1;
                if(!RecordDefinitionComparer.equal(rtncsrlocdefinition_window.getAbsoluteRowField(), rtncsrlocdefinition_window1.getAbsoluteRowField()) || !RecordDefinitionComparer.equal(rtncsrlocdefinition_window.getAbsoluteColumnField(), rtncsrlocdefinition_window1.getAbsoluteColumnField()) || !RecordDefinitionComparer.equal(rtncsrlocdefinition_window.getWindowRowField(), rtncsrlocdefinition_window1.getWindowRowField()) || !RecordDefinitionComparer.equal(rtncsrlocdefinition_window.getWindowColumnField(), rtncsrlocdefinition_window1.getWindowColumnField()))
                    flag = false;
            }
            if(obj instanceof RTNCSRLOCDefinition_RECNAME)
            {
                RTNCSRLOCDefinition_RECNAME rtncsrlocdefinition_recname = (RTNCSRLOCDefinition_RECNAME)obj;
                RTNCSRLOCDefinition_RECNAME rtncsrlocdefinition_recname1 = (RTNCSRLOCDefinition_RECNAME)obj1;
                if(!RecordDefinitionComparer.equal(rtncsrlocdefinition_recname.getRecordNameField(), rtncsrlocdefinition_recname1.getRecordNameField()) || !RecordDefinitionComparer.equal(rtncsrlocdefinition_recname.getFieldNameField(), rtncsrlocdefinition_recname1.getFieldNameField()) || !RecordDefinitionComparer.equal(rtncsrlocdefinition_recname.getCursorPosField(), rtncsrlocdefinition_recname1.getCursorPosField()))
                    flag = false;
            }
            if(!flag)
                RecordDefinitionComparer.out.println("RTNCSRLOCDefinition mismatch");
            return flag;
        }

        private RTNCSRLOCDefinitionComparer()
        {
        }

    }

    private static class ResponseIndicatorComparer
        implements IComparer
    {

        public boolean identical(Object obj, Object obj1)
        {
            ResponseIndicator responseindicator = (ResponseIndicator)obj;
            ResponseIndicator responseindicator1 = (ResponseIndicator)obj1;
            boolean flag = true;
            if(!responseindicator.getClass().toString().equals(responseindicator1.getClass().toString()))
            {
                RecordDefinitionComparer.out.println("Class types don't match for " + obj + " and " + obj1);
                flag = false;
            }
            if(responseindicator.getIndex() != responseindicator1.getIndex())
                flag = false;
            if(((responseindicator instanceof FieldResponseIndicator) || (responseindicator instanceof AIDKeyResponseIndicator) || (responseindicator instanceof BLANKSResponseIndicator)) && !RecordDefinitionComparer.equal(responseindicator.getKey().getId(), responseindicator1.getKey().getId()))
                flag = false;
            if((responseindicator instanceof HLPRTNResponseIndicator) && !RecordDefinitionComparer.equal(((HLPRTNResponseIndicator)responseindicator).getIndExpr(), ((HLPRTNResponseIndicator)responseindicator1).getIndExpr()))
                flag = false;
            if(!flag)
                RecordDefinitionComparer.out.println("Response indicator mismatch");
            return flag;
        }

        private ResponseIndicatorComparer()
        {
        }

    }

    private static class IndicatorAndRowComparer
        implements IComparer
    {

        public boolean identical(Object obj, Object obj1)
        {
            IndicatorAndRow indicatorandrow = (IndicatorAndRow)obj;
            IndicatorAndRow indicatorandrow1 = (IndicatorAndRow)obj1;
            if(!RecordDefinitionComparer.equal(indicatorandrow.getIndicatorExpression(), indicatorandrow1.getIndicatorExpression()) || indicatorandrow.getRowInSubfile() != indicatorandrow1.getRowInSubfile())
            {
                RecordDefinitionComparer.out.println("Indicator Expression or Row Mismatch");
                return false;
            } else
            {
                return true;
            }
        }

        private IndicatorAndRowComparer()
        {
        }

    }

    private static class CHKMSGIDDefinitionComparer
        implements IComparer
    {

        public boolean identical(Object obj, Object obj1)
        {
            CHKMSGIDDefinition chkmsgiddefinition = (CHKMSGIDDefinition)obj;
            CHKMSGIDDefinition chkmsgiddefinition1 = (CHKMSGIDDefinition)obj1;
            return RecordDefinitionComparer.equal(chkmsgiddefinition.getMsgId(), chkmsgiddefinition1.getMsgId()) && RecordDefinitionComparer.equal(chkmsgiddefinition.getLibraryName(), chkmsgiddefinition1.getLibraryName()) && RecordDefinitionComparer.equal(chkmsgiddefinition.getMsgFile(), chkmsgiddefinition1.getMsgFile()) && RecordDefinitionComparer.equal(chkmsgiddefinition.getMsgDataFieldName(), chkmsgiddefinition1.getMsgDataFieldName()) && RecordDefinitionComparer.equal(chkmsgiddefinition.getFieldName(), chkmsgiddefinition1.getFieldName());
        }

        private CHKMSGIDDefinitionComparer()
        {
        }

    }

    private static class HelpGroupComparer
        implements IComparer
    {

        public boolean identical(Object obj, Object obj1)
        {
            HelpGroup helpgroup = (HelpGroup)obj;
            HelpGroup helpgroup1 = (HelpGroup)obj1;
            if(!RecordDefinitionComparer.equal(helpgroup.getName(), helpgroup1.getName()) || helpgroup.getSequence() != helpgroup1.getSequence())
                return false;
            HelpDefinition helpdefinition = helpgroup.getDefinition();
            HelpDefinition helpdefinition1 = helpgroup1.getDefinition();
            if(helpdefinition != null || helpdefinition1 != null)
            {
                if(helpdefinition == null || helpdefinition1 == null)
                    return false;
                if(!RecordDefinitionComparer.equal(helpdefinition.getType(), helpdefinition1.getType()) || !RecordDefinitionComparer.equal(helpdefinition.getRecord(), helpdefinition1.getRecord()) || !RecordDefinitionComparer.equal(helpdefinition.getDefinition(), helpdefinition1.getDefinition()) || !RecordDefinitionComparer.equal(helpdefinition.getObject(), helpdefinition1.getObject()) || !RecordDefinitionComparer.equal(helpdefinition.getLibrary(), helpdefinition1.getLibrary()) || !RecordDefinitionComparer.equal(helpdefinition.getIndicatorExpression(), helpdefinition1.getIndicatorExpression()))
                    return false;
            }
            return true;
        }

        private HelpGroupComparer()
        {
        }

    }

    private static class HelpAreaComparer
        implements IComparer
    {

        public boolean identical(Object obj, Object obj1)
        {
            HelpArea helparea = (HelpArea)obj;
            HelpArea helparea1 = (HelpArea)obj1;
            if(!helparea.getTopLeft().equals(helparea1.getTopLeft()) || !helparea.getBottomRight().equals(helparea1.getBottomRight()))
                return false;
            if(!RecordDefinitionComparer.equal(helparea.getField(), helparea1.getField()) || helparea.getFieldChoice() != helparea1.getFieldChoice())
                return false;
            HelpDefinition helpdefinition = helparea.getHelpDefinition();
            HelpDefinition helpdefinition1 = helparea1.getHelpDefinition();
            if(helpdefinition != null || helpdefinition1 != null)
            {
                if(helpdefinition == null || helpdefinition1 == null)
                    return false;
                if(!RecordDefinitionComparer.equal(helpdefinition.getType(), helpdefinition1.getType()) || !RecordDefinitionComparer.equal(helpdefinition.getRecord(), helpdefinition1.getRecord()) || !RecordDefinitionComparer.equal(helpdefinition.getDefinition(), helpdefinition1.getDefinition()) || !RecordDefinitionComparer.equal(helpdefinition.getObject(), helpdefinition1.getObject()) || !RecordDefinitionComparer.equal(helpdefinition.getLibrary(), helpdefinition1.getLibrary()) || !RecordDefinitionComparer.equal(helpdefinition.getIndicatorExpression(), helpdefinition1.getIndicatorExpression()))
                    return false;
            }
            HelpKeyword helpkeyword = helparea.getHelpBoundary();
            HelpKeyword helpkeyword1 = helparea1.getHelpBoundary();
            if(helpkeyword != null || helpkeyword1 != null)
            {
                if(helpkeyword == null || helpkeyword1 == null)
                    return false;
                if(helpkeyword.getKeywordIdentifier() != helpkeyword1.getKeywordIdentifier() || !RecordDefinitionComparer.equal(helpkeyword.getIndicatorExpression(), helpkeyword1.getIndicatorExpression()))
                    return false;
            }
            helpkeyword = helparea.getHelpExcluded();
            helpkeyword1 = helparea1.getHelpExcluded();
            if(helpkeyword != null || helpkeyword1 != null)
            {
                if(helpkeyword == null || helpkeyword1 == null)
                    return false;
                if(helpkeyword.getKeywordIdentifier() != helpkeyword1.getKeywordIdentifier() || !RecordDefinitionComparer.equal(helpkeyword.getIndicatorExpression(), helpkeyword1.getIndicatorExpression()))
                    return false;
            }
            return true;
        }

        private HelpAreaComparer()
        {
        }

    }

    private static class DSPATR_PCFieldInfoComparer
        implements IComparer
    {

        public boolean identical(Object obj, Object obj1)
        {
            DSPATR_PCFieldInfo dspatr_pcfieldinfo = (DSPATR_PCFieldInfo)obj;
            DSPATR_PCFieldInfo dspatr_pcfieldinfo1 = (DSPATR_PCFieldInfo)obj1;
            return RecordDefinitionComparer.equal(dspatr_pcfieldinfo.getName(), dspatr_pcfieldinfo1.getName()) && RecordDefinitionComparer.equal(dspatr_pcfieldinfo.getIndExpr(), dspatr_pcfieldinfo1.getIndExpr());
        }

        private DSPATR_PCFieldInfoComparer()
        {
        }

    }

    private static class CommandKeyLabelComparer
        implements IComparer
    {
        private static class ConditionedLabelComparer
            implements IComparer
        {

            public boolean identical(Object obj, Object obj1)
            {
                return RecordDefinitionComparer.equal(((ConditionedLabel)obj).getLabel(), ((ConditionedLabel)obj1).getLabel());
            }

            private ConditionedLabelComparer()
            {
            }

        }


        public boolean identical(Object obj, Object obj1)
        {
            CommandKeyLabel commandkeylabel = (CommandKeyLabel)obj;
            CommandKeyLabel commandkeylabel1 = (CommandKeyLabel)obj1;
            if(!commandkeylabel.getClass().equals(commandkeylabel1.getClass()))
                return false;
            if(!RecordDefinitionComparer.equal(commandkeylabel.getKeyName(), commandkeylabel1.getKeyName()) || !RecordDefinitionComparer.equal(commandkeylabel.getKeyLabel(), commandkeylabel1.getKeyLabel()) || !RecordDefinitionComparer.equal(commandkeylabel.getRecordName(), commandkeylabel1.getRecordName()) || commandkeylabel.getPriority() != commandkeylabel1.getPriority())
                return false;
            if((commandkeylabel instanceof ConditionedCommandKeyLabel) != (commandkeylabel1 instanceof ConditionedCommandKeyLabel))
                return false;
            if(commandkeylabel instanceof ConditionedCommandKeyLabel)
            {
                ConditionedCommandKeyLabel conditionedcommandkeylabel = (ConditionedCommandKeyLabel)commandkeylabel;
                ConditionedCommandKeyLabel conditionedcommandkeylabel1 = (ConditionedCommandKeyLabel)commandkeylabel1;
                if(!RecordDefinitionComparer.equalIterators(conditionedcommandkeylabel.getConditionedLabels().iterator(), conditionedcommandkeylabel1.getConditionedLabels().iterator(), new ConditionedLabelComparer()))
                    return false;
            }
            return true;
        }

        private CommandKeyLabelComparer()
        {
        }

    }

    private static class AIDKeyComparer
        implements IComparer
    {

        public boolean identical(Object obj, Object obj1)
        {
            AIDKey aidkey = (AIDKey)obj;
            AIDKey aidkey1 = (AIDKey)obj1;
            return RecordDefinitionComparer.equal(aidkey.getKeyName(), aidkey1.getKeyName()) && RecordDefinitionComparer.equal(aidkey.getKeyLabel(), aidkey1.getKeyLabel()) && RecordDefinitionComparer.equal(aidkey.getRecordName(), aidkey1.getRecordName()) && aidkey.getPriority() == aidkey1.getPriority() && RecordDefinitionComparer.equal(aidkey.getIndicatorExpression(), aidkey1.getIndicatorExpression()) && aidkey.isKeyShownOnClient() == aidkey1.isKeyShownOnClient();
        }

        private AIDKeyComparer()
        {
        }

    }

    private static class SFLMSGMessageDefinitionComparer
        implements IComparer
    {

        public boolean identical(Object obj, Object obj1)
        {
            SFLMSGMessageDefinition sflmsgmessagedefinition = (SFLMSGMessageDefinition)obj;
            SFLMSGMessageDefinition sflmsgmessagedefinition1 = (SFLMSGMessageDefinition)obj1;
            if(!RecordDefinitionComparer.equal(sflmsgmessagedefinition.getMessageText(), sflmsgmessagedefinition1.getMessageText()) || !RecordDefinitionComparer.equal(sflmsgmessagedefinition.getIndicatorExpression(), sflmsgmessagedefinition1.getIndicatorExpression()) || sflmsgmessagedefinition.getResponseIndicator() != sflmsgmessagedefinition1.getResponseIndicator())
                return false;
            if((sflmsgmessagedefinition instanceof SFLMSGIDMessageDefinition) != (sflmsgmessagedefinition1 instanceof SFLMSGIDMessageDefinition))
                return false;
            if(sflmsgmessagedefinition instanceof SFLMSGIDMessageDefinition)
            {
                XXXMSGIDDefinition xxxmsgiddefinition = ((SFLMSGIDMessageDefinition)sflmsgmessagedefinition).getMSGID();
                XXXMSGIDDefinition xxxmsgiddefinition1 = ((SFLMSGIDMessageDefinition)sflmsgmessagedefinition1).getMSGID();
                if(!RecordDefinitionComparer.equal(xxxmsgiddefinition.getIndicatorExpression(), xxxmsgiddefinition1.getIndicatorExpression()) || !RecordDefinitionComparer.equal(xxxmsgiddefinition.getMsgId(), xxxmsgiddefinition1.getMsgId()) || !RecordDefinitionComparer.equal(xxxmsgiddefinition.getLibraryName(), xxxmsgiddefinition1.getLibraryName()) || !RecordDefinitionComparer.equal(xxxmsgiddefinition.getMsgFile(), xxxmsgiddefinition1.getMsgFile()) || !RecordDefinitionComparer.equal(xxxmsgiddefinition.getResponseIndicator(), xxxmsgiddefinition1.getResponseIndicator()) || !RecordDefinitionComparer.equal(xxxmsgiddefinition.getMsgdataFieldName(), xxxmsgiddefinition1.getMsgdataFieldName()))
                    return false;
            }
            return true;
        }

        private SFLMSGMessageDefinitionComparer()
        {
        }

    }

    private static class ERRMSGMessageDefinitionComparer
        implements IComparer
    {

        public boolean identical(Object obj, Object obj1)
        {
            ERRMSGMessageDefinition errmsgmessagedefinition = (ERRMSGMessageDefinition)obj;
            ERRMSGMessageDefinition errmsgmessagedefinition1 = (ERRMSGMessageDefinition)obj1;
            if(!RecordDefinitionComparer.equal(errmsgmessagedefinition.getFieldName(), errmsgmessagedefinition1.getFieldName()) || !RecordDefinitionComparer.equal(errmsgmessagedefinition.getMessageText(), errmsgmessagedefinition1.getMessageText()) || !RecordDefinitionComparer.equal(errmsgmessagedefinition.getIndicatorExpression(), errmsgmessagedefinition1.getIndicatorExpression()) || errmsgmessagedefinition.getResponseIndicator() != errmsgmessagedefinition1.getResponseIndicator())
                return false;
            if((errmsgmessagedefinition instanceof ERRMSGIDMessageDefinition) != (errmsgmessagedefinition1 instanceof ERRMSGIDMessageDefinition))
                return false;
            if(errmsgmessagedefinition instanceof ERRMSGIDMessageDefinition)
            {
                XXXMSGIDDefinition xxxmsgiddefinition = ((ERRMSGIDMessageDefinition)errmsgmessagedefinition).getMSGID();
                XXXMSGIDDefinition xxxmsgiddefinition1 = ((ERRMSGIDMessageDefinition)errmsgmessagedefinition1).getMSGID();
                if(!RecordDefinitionComparer.equal(xxxmsgiddefinition.getIndicatorExpression(), xxxmsgiddefinition1.getIndicatorExpression()) || !RecordDefinitionComparer.equal(xxxmsgiddefinition.getMsgId(), xxxmsgiddefinition1.getMsgId()) || !RecordDefinitionComparer.equal(xxxmsgiddefinition.getLibraryName(), xxxmsgiddefinition1.getLibraryName()) || !RecordDefinitionComparer.equal(xxxmsgiddefinition.getMsgFile(), xxxmsgiddefinition1.getMsgFile()) || !RecordDefinitionComparer.equal(xxxmsgiddefinition.getResponseIndicator(), xxxmsgiddefinition1.getResponseIndicator()) || !RecordDefinitionComparer.equal(xxxmsgiddefinition.getMsgdataFieldName(), xxxmsgiddefinition1.getMsgdataFieldName()))
                    return false;
            }
            return true;
        }

        private ERRMSGMessageDefinitionComparer()
        {
        }

    }

    private static class FieldViewDefinitionComparer
        implements IComparer
    {

        public boolean identical(Object obj, Object obj1)
        {
            FieldViewDefinition fieldviewdefinition = (FieldViewDefinition)obj;
            FieldViewDefinition fieldviewdefinition1 = (FieldViewDefinition)obj1;
            if(!RecordDefinitionComparer.equal(fieldviewdefinition.getFieldName(), fieldviewdefinition1.getFieldName()) || !fieldviewdefinition.getPosition().isEqual(fieldviewdefinition1.getPosition()) || fieldviewdefinition.getWidth() != fieldviewdefinition1.getWidth() || fieldviewdefinition.getHeight() != fieldviewdefinition1.getHeight() || !RecordDefinitionComparer.equal(fieldviewdefinition.getValues(), fieldviewdefinition1.getValues()))
                return false;
            if(fieldviewdefinition.getEditCode() != fieldviewdefinition1.getEditCode() || fieldviewdefinition.getEditCodeParm() != fieldviewdefinition1.getEditCodeParm() || !RecordDefinitionComparer.equal(fieldviewdefinition.getEditWord(), fieldviewdefinition1.getEditWord()))
                return false;
            if(!RecordDefinitionComparer.equalIterators(fieldviewdefinition.iterator(com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition.class), fieldviewdefinition1.iterator(com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition.class), new KeywordDefinitionComparer()))
                return false;
            if(!RecordDefinitionComparer.equalIterators(fieldviewdefinition.getERRMSGs(), fieldviewdefinition1.getERRMSGs(), new ERRMSGMessageDefinitionComparer()))
                return false;
            if(fieldviewdefinition.isMasked() != fieldviewdefinition1.isMasked())
                return false;
            return !fieldviewdefinition.isMasked() || !fieldviewdefinition1.isMasked() || fieldviewdefinition.getStartMaskingAt() == fieldviewdefinition1.getStartMaskingAt() && fieldviewdefinition.getEndMaskingAt() == fieldviewdefinition1.getEndMaskingAt();
        }

        private FieldViewDefinitionComparer()
        {
        }

    }

    private static class KeywordDefinitionComparer
        implements IComparer
    {

        public boolean identical(Object obj, Object obj1)
        {
            KeywordDefinition keyworddefinition = (KeywordDefinition)obj;
            KeywordDefinition keyworddefinition1 = (KeywordDefinition)obj1;
            if(keyworddefinition.getKeywordIdentifier() != keyworddefinition1.getKeywordIdentifier())
                return false;
            if(!RecordDefinitionComparer.equal(keyworddefinition.getIndicatorExpression(), keyworddefinition1.getIndicatorExpression()))
                return false;
            return RecordDefinitionComparer.equalIterators(keyworddefinition.getParameters(), keyworddefinition1.getParameters());
        }

        private KeywordDefinitionComparer()
        {
        }

    }

    private static class FieldDataDefinitionComparer
        implements IComparer
    {

        public boolean identical(Object obj, Object obj1)
        {
            FieldDataDefinition fielddatadefinition = (FieldDataDefinition)obj;
            FieldDataDefinition fielddatadefinition1 = (FieldDataDefinition)obj1;
            String s = fielddatadefinition.getFieldName();
            if(!RecordDefinitionComparer.equal(fielddatadefinition.getFieldName(), fielddatadefinition1.getFieldName()) || fielddatadefinition.getFieldLength() != fielddatadefinition1.getFieldLength() || fielddatadefinition.getDataType().intValue() != fielddatadefinition1.getDataType().intValue() || fielddatadefinition.getKeyboardShift() != fielddatadefinition1.getKeyboardShift())
            {
                System.err.println("Field name, length, type or shift is different for field " + fielddatadefinition.getFieldName() + " or " + fielddatadefinition1.getFieldName());
                return false;
            }
            if(fielddatadefinition.isInputCapable() != fielddatadefinition1.isInputCapable() || fielddatadefinition.isOutputCapable() != fielddatadefinition1.isOutputCapable())
            {
                System.err.println("Field usage is different for field " + fielddatadefinition.getFieldName() + " or " + fielddatadefinition1.getFieldName());
                return false;
            }
            if(fielddatadefinition.getDecimalPrecision() != fielddatadefinition1.getDecimalPrecision())
            {
                System.err.println("Field decimal precision is different for field " + fielddatadefinition.getFieldName() + " or " + fielddatadefinition1.getFieldName());
                return false;
            }
            if(!RecordDefinitionComparer.equal(fielddatadefinition.getDatFmt(), fielddatadefinition1.getDatFmt()) || !RecordDefinitionComparer.equal(fielddatadefinition.getDatSep(), fielddatadefinition1.getDatSep()) || !RecordDefinitionComparer.equal(fielddatadefinition.getTimFmt(), fielddatadefinition1.getTimFmt()) || !RecordDefinitionComparer.equal(fielddatadefinition.getTimSep(), fielddatadefinition1.getTimSep()))
            {
                System.err.println("Field date/time format/separator is different for field " + fielddatadefinition.getFieldName() + " or " + fielddatadefinition1.getFieldName());
                return false;
            }
            if(!RecordDefinitionComparer.equal(fielddatadefinition.getDFTVAL(), fielddatadefinition1.getDFTVAL()) || !RecordDefinitionComparer.equal(fielddatadefinition.getDFTVALIndicatorExpression(), fielddatadefinition1.getDFTVALIndicatorExpression()))
            {
                System.err.println("Field DFTVAL is different for field " + fielddatadefinition.getFieldName() + " or " + fielddatadefinition1.getFieldName());
                return false;
            }
            if(!RecordDefinitionComparer.equal(fielddatadefinition.getOVRDTA(), fielddatadefinition1.getOVRDTA()))
            {
                System.err.println("Field OVRDTA is different for field " + fielddatadefinition.getFieldName() + " or " + fielddatadefinition1.getFieldName());
                return false;
            }
            if(!RecordDefinitionComparer.equalIterators(fielddatadefinition.getMSGIDs(), fielddatadefinition1.getMSGIDs()))
            {
                System.err.println("Field MSGID is different for field " + fielddatadefinition.getFieldName() + " or " + fielddatadefinition1.getFieldName());
                Iterator iterator = fielddatadefinition.getMSGIDs();
                Object obj2 = null;
                MSGIDDefinition msgiddefinition;
                for(; iterator.hasNext(); RecordDefinitionComparer.out.println("a.MSGID= " + msgiddefinition.getMsgId(null) + " " + msgiddefinition.getMsgFile(null) + " " + msgiddefinition.getMsgLibrary(null) + " " + msgiddefinition.getIndicatorExpression() + " " + (new Boolean(msgiddefinition.isNONESet())).toString()))
                    msgiddefinition = (MSGIDDefinition)iterator.next();

                Iterator iterator1 = fielddatadefinition1.getMSGIDs();
                RecordDefinitionComparer.out.println("----");
                MSGIDDefinition msgiddefinition1;
                for(; iterator1.hasNext(); RecordDefinitionComparer.out.println("b.MSGID= " + msgiddefinition1.getMsgId(null) + " " + msgiddefinition1.getMsgFile(null) + " " + msgiddefinition1.getMsgLibrary(null) + " " + msgiddefinition1.getIndicatorExpression() + " " + (new Boolean(msgiddefinition1.isNONESet())).toString()))
                    msgiddefinition1 = (MSGIDDefinition)iterator1.next();

                RecordDefinitionComparer.out.println("----");
                return false;
            }
            if(!RecordDefinitionComparer.equal(fielddatadefinition.getCheckAttr(), fielddatadefinition1.getCheckAttr()))
            {
                System.err.println("Field CheckAttr is different for field " + fielddatadefinition.getFieldName() + " or " + fielddatadefinition1.getFieldName());
                return false;
            } else
            {
                return true;
            }
        }

        private FieldDataDefinitionComparer()
        {
        }

    }

    private static interface IComparer
    {

        public abstract boolean identical(Object obj, Object obj1);
    }


    public RecordDefinitionComparer()
    {
    }

    public static boolean equal(Object obj, Object obj1)
    {
        if(obj == null && obj1 == null)
            return true;
        if(obj == null || obj1 == null)
            return false;
        else
            return obj.equals(obj1);
    }

    public static boolean equalIterators(Iterator iterator, Iterator iterator1)
    {
        if(iterator == null && iterator1 == null)
            return true;
        if(iterator == null || iterator1 == null)
            return false;
        while(iterator.hasNext() || iterator1.hasNext()) 
        {
            if(iterator.hasNext() != iterator1.hasNext())
                return false;
            Object obj = iterator.next();
            Object obj1 = iterator1.next();
            if(!obj.equals(obj1))
                return false;
        }
        return true;
    }

    public static boolean equalIterators(Iterator iterator, Iterator iterator1, IComparer icomparer)
    {
        if(iterator == null && iterator1 == null)
            return true;
        if(iterator == null || iterator1 == null)
            return false;
        while(iterator.hasNext() || iterator1.hasNext()) 
        {
            if(iterator.hasNext() != iterator1.hasNext())
                return false;
            Object obj = iterator.next();
            Object obj1 = iterator1.next();
            if(!icomparer.identical(obj, obj1))
                return false;
        }
        return true;
    }

    public static boolean identicalRecordDataDefinitions(IRecordDataDefinition irecorddatadefinition, IRecordDataDefinition irecorddatadefinition1, PrintWriter printwriter)
    {
        if(null != printwriter)
            out = printwriter;
        return identicalRecordDataDefinitions(irecorddatadefinition, irecorddatadefinition1);
    }

    public static boolean identicalRecordDataDefinitions(IRecordDataDefinition irecorddatadefinition, IRecordDataDefinition irecorddatadefinition1)
    {
        boolean flag = true;
        if(!equal(irecorddatadefinition.getName(), irecorddatadefinition1.getName()))
        {
            out.println("Record names are different!");
            flag = false;
        }
        if(irecorddatadefinition.getVersionDigits() != irecorddatadefinition1.getVersionDigits())
        {
            out.println("Version digits are different in record " + irecorddatadefinition.getName());
            flag = false;
        }
        if(!equal(irecorddatadefinition.getFileMemberType(), irecorddatadefinition1.getFileMemberType()))
        {
            out.println("File member types are different in record " + irecorddatadefinition.getName());
            flag = false;
        }
        if(!equalIterators(irecorddatadefinition.getFieldDefinitions().iterator(), irecorddatadefinition1.getFieldDefinitions().iterator(), new FieldDataDefinitionComparer()))
        {
            out.println("Field definitions are different in record " + irecorddatadefinition.getName());
            flag = false;
        }
        if(irecorddatadefinition.hasSeparateIndicatorArea() != irecorddatadefinition1.hasSeparateIndicatorArea())
        {
            out.println("Separate indicator areas different in record " + irecorddatadefinition.getName());
            flag = false;
        }
        IndicatorDataDefinition indicatordatadefinition = irecorddatadefinition.getIndicatorDefinition();
        IndicatorDataDefinition indicatordatadefinition1 = irecorddatadefinition1.getIndicatorDefinition();
        if(!equalIterators(indicatordatadefinition.getReferencedOptionIndicators().iterator(), indicatordatadefinition1.getReferencedOptionIndicators().iterator()) || !equalIterators(indicatordatadefinition.getReferencedResponseIndicators(), indicatordatadefinition1.getReferencedResponseIndicators()) || !equalIterators(indicatordatadefinition.getReferencedAIDResponseIndicators(), indicatordatadefinition1.getReferencedAIDResponseIndicators()))
        {
            out.println("Indicator definitions are different in record " + irecorddatadefinition.getName());
            flag = false;
        }
        if(irecorddatadefinition.getInputIOBufferLength() != irecorddatadefinition1.getInputIOBufferLength() || irecorddatadefinition.getOutputIOBufferLength() != irecorddatadefinition1.getOutputIOBufferLength())
        {
            out.println("Buffer lengths are different in record " + irecorddatadefinition.getName());
            flag = false;
        }
        if((irecorddatadefinition instanceof SubfileControlRecordDataDefinition) != (irecorddatadefinition1 instanceof SubfileControlRecordDataDefinition))
        {
            out.println("Only one definition is subfile control!");
            flag = false;
        }
        if((irecorddatadefinition instanceof SubfileControlRecordDataDefinition) && (irecorddatadefinition1 instanceof SubfileControlRecordDataDefinition))
        {
            SubfileControlRecordDataDefinition subfilecontrolrecorddatadefinition = (SubfileControlRecordDataDefinition)irecorddatadefinition;
            SubfileControlRecordDataDefinition subfilecontrolrecorddatadefinition1 = (SubfileControlRecordDataDefinition)irecorddatadefinition1;
            if(!equal(subfilecontrolrecorddatadefinition.getSubfileName(), subfilecontrolrecorddatadefinition1.getSubfileName()) || subfilecontrolrecorddatadefinition.getPageSize() != subfilecontrolrecorddatadefinition1.getPageSize() || !equal(subfilecontrolrecorddatadefinition.getSubfileSizeFieldName(), subfilecontrolrecorddatadefinition1.getSubfileSizeFieldName()) || subfilecontrolrecorddatadefinition.getSubfileSize() != subfilecontrolrecorddatadefinition1.getSubfileSize())
            {
                out.println("Subfile attributes are different in record " + irecorddatadefinition.getName());
                flag = false;
            }
        }
        return flag;
    }

    public static boolean identicalRecordViewDefinitions(IRecordViewDefinition irecordviewdefinition, IRecordViewDefinition irecordviewdefinition1, PrintWriter printwriter)
    {
        if(null != printwriter)
            out = printwriter;
        return identicalRecordViewDefinitions(irecordviewdefinition, irecordviewdefinition1);
    }

    public static boolean identicalRecordViewDefinitions(IRecordViewDefinition irecordviewdefinition, IRecordViewDefinition irecordviewdefinition1)
    {
        boolean flag = true;
        if(!equal(irecordviewdefinition.getName(), irecordviewdefinition1.getName()))
        {
            out.println("Record names are different!");
            flag = false;
        }
        if(irecordviewdefinition.isWide() != irecordviewdefinition1.isWide())
        {
            out.println("Wide is different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(!equal(irecordviewdefinition.getPrimaryFileDisplaySize(), irecordviewdefinition1.getPrimaryFileDisplaySize()) || !equal(irecordviewdefinition.getSecondaryFileDisplaySize(), irecordviewdefinition1.getSecondaryFileDisplaySize()))
        {
            out.println("Display sizes are different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(irecordviewdefinition.getFirstFieldLine() != irecordviewdefinition1.getFirstFieldLine() || irecordviewdefinition.getLastFieldLine() != irecordviewdefinition1.getLastFieldLine() || irecordviewdefinition.getFirstColumn() != irecordviewdefinition1.getFirstColumn() || irecordviewdefinition.getLastColumn() != irecordviewdefinition1.getLastColumn())
        {
            out.println("First/last field line, column different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(irecordviewdefinition.getIsOutputOnly() != irecordviewdefinition1.getIsOutputOnly())
        {
            out.println("Output only is different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(!compareFieldVisDefs(irecordviewdefinition.getFieldVisDef(), irecordviewdefinition1.getFieldVisDef()))
        {
            out.println("FieldVisDefs are different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(!equalIterators(irecordviewdefinition.getKeywordDefinitions(), irecordviewdefinition1.getKeywordDefinitions(), new KeywordDefinitionComparer()))
        {
            out.println("Keyword definitions are different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(!equalIterators(irecordviewdefinition.getERASEKeywords(), irecordviewdefinition1.getERASEKeywords(), new KeywordDefinitionComparer()))
        {
            out.println("ERASE keyword definitions are different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(irecordviewdefinition.getCLRL_NN() != irecordviewdefinition1.getCLRL_NN())
        {
            out.println("CLRL keywords are different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(!equalIterators(irecordviewdefinition.getHelpTitles(), irecordviewdefinition1.getHelpTitles(), new KeywordDefinitionComparer()))
        {
            out.println("HLPTITLE keywords are different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(!equalIterators(irecordviewdefinition.getFieldViewDefinitions(), irecordviewdefinition1.getFieldViewDefinitions(), new FieldViewDefinitionComparer()))
        {
            out.println("Field view definitions are different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(!equalIterators(irecordviewdefinition.getKeySequence(), irecordviewdefinition1.getKeySequence(), new AIDKeyComparer()))
        {
            out.println("AIDKeys are different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(!equalIterators(irecordviewdefinition.getCommandKeyLabels(), irecordviewdefinition1.getCommandKeyLabels(), new CommandKeyLabelComparer()))
        {
            out.println("Command key labels are different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(!equalIterators(irecordviewdefinition.getIndicatorConditionedCommandKeyLabels(), irecordviewdefinition1.getIndicatorConditionedCommandKeyLabels(), new CommandKeyLabelComparer()))
        {
            out.println("Indicator conditioned command key labels are different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(!equalIterators(irecordviewdefinition.getVisibilityConditionedCommandKeyLabels(), irecordviewdefinition1.getVisibilityConditionedCommandKeyLabels(), new CommandKeyLabelComparer()))
        {
            out.println("Visibility conditioned command key labels are different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(irecordviewdefinition.isWindowed() != irecordviewdefinition1.isWindowed() || irecordviewdefinition.getWdwStartLine() != irecordviewdefinition1.getWdwStartLine() || !equal(irecordviewdefinition.getWdwStartLineField(), irecordviewdefinition1.getWdwStartLineField()) || irecordviewdefinition.getWdwStartPos() != irecordviewdefinition1.getWdwStartPos() || !equal(irecordviewdefinition.getWdwStartPosField(), irecordviewdefinition1.getWdwStartPosField()))
        {
            out.println("Window attributes are different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(!equalIterators(irecordviewdefinition.getWindowTitles(), irecordviewdefinition1.getWindowTitles()))
        {
            out.println("Window title definitions are different in record " + irecordviewdefinition.getName());
            Object obj = null;
            Object obj1 = null;
            Iterator iterator = irecordviewdefinition.getWindowTitles();
            String s;
            String s1;
            for(Iterator iterator1 = irecordviewdefinition.getWindowTitles(); iterator.hasNext() || iterator1.hasNext(); out.println(s + " --- " + s1))
            {
                s = (String)iterator.next();
                s1 = (String)iterator1.next();
            }

            flag = false;
        }
        if(!equalIterators(irecordviewdefinition.getDspatrPCFieldInfos(), irecordviewdefinition1.getDspatrPCFieldInfos(), new DSPATR_PCFieldInfoComparer()))
        {
            out.println("DSPATR_PCFieldInfos are different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(!equalIterators(irecordviewdefinition.getHelpDefinitions(), irecordviewdefinition1.getHelpDefinitions(), new HelpAreaComparer()))
        {
            out.println("Help definitions are different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(!equalIterators(irecordviewdefinition.getHelpGroups(), irecordviewdefinition1.getHelpGroups(), new HelpGroupComparer()))
        {
            out.println("Help groups are different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if(!equalIterators(irecordviewdefinition.getCHKMSGIDDefinitions(), irecordviewdefinition1.getCHKMSGIDDefinitions(), new CHKMSGIDDefinitionComparer()))
        {
            out.println("CHKMSGID definitions are different in record " + irecordviewdefinition.getName());
            flag = false;
        }
        if((irecordviewdefinition instanceof SubfileControlRecordViewDefinition) != (irecordviewdefinition1 instanceof SubfileControlRecordViewDefinition))
        {
            out.println("Only one definition is subfile control!");
            flag = false;
        }
        if((irecordviewdefinition instanceof SubfileControlRecordViewDefinition) && (irecordviewdefinition1 instanceof SubfileControlRecordViewDefinition))
        {
            SubfileControlRecordViewDefinition subfilecontrolrecordviewdefinition = (SubfileControlRecordViewDefinition)irecordviewdefinition;
            SubfileControlRecordViewDefinition subfilecontrolrecordviewdefinition1 = (SubfileControlRecordViewDefinition)irecordviewdefinition1;
            if(subfilecontrolrecordviewdefinition.getSubfileAreaFirstRow() != subfilecontrolrecordviewdefinition1.getSubfileAreaFirstRow() || subfilecontrolrecordviewdefinition.getSubfileAreaHeight() != subfilecontrolrecordviewdefinition1.getSubfileAreaHeight() || subfilecontrolrecordviewdefinition.getSubfileRecordsPerRow() != subfilecontrolrecordviewdefinition1.getSubfileRecordsPerRow() || subfilecontrolrecordviewdefinition.getSubfileFirstColumn() != subfilecontrolrecordviewdefinition1.getSubfileFirstColumn() || subfilecontrolrecordviewdefinition.getSubfileRecordWidth() != subfilecontrolrecordviewdefinition1.getSubfileRecordWidth() || subfilecontrolrecordviewdefinition.getSFLLIN() != subfilecontrolrecordviewdefinition1.getSFLLIN())
            {
                out.println("Subfile attributes are different in record " + irecordviewdefinition.getName());
                flag = false;
            }
            if(!equal(subfilecontrolrecordviewdefinition.getSFLROLVALFieldName(), subfilecontrolrecordviewdefinition1.getSFLROLVALFieldName()))
            {
                out.println("SFLROLVAL field names are different in record " + irecordviewdefinition.getName() + "(" + subfilecontrolrecordviewdefinition.getSFLROLVALFieldName() + "," + subfilecontrolrecordviewdefinition1.getSFLROLVALFieldName() + ")");
                flag = false;
            }
            if(!equal(subfilecontrolrecordviewdefinition.getSFLDSPCondition(), subfilecontrolrecordviewdefinition1.getSFLDSPCondition()) || !equal(subfilecontrolrecordviewdefinition.getSFLDSPCTLCondition(), subfilecontrolrecordviewdefinition1.getSFLDSPCTLCondition()))
            {
                out.println("SFLDSP or SFLDSPCTL conditions are different in record " + irecordviewdefinition.getName());
                flag = false;
            }
            if(!compareFieldVisDefs(subfilecontrolrecordviewdefinition.getSubfileFieldVisDef(), subfilecontrolrecordviewdefinition1.getSubfileFieldVisDef()))
            {
                out.println("Subfile field vis defs are different in record " + irecordviewdefinition.getName());
                flag = false;
            }
            if(!equalIterators(subfilecontrolrecordviewdefinition.getSFLMSGs(), subfilecontrolrecordviewdefinition1.getSFLMSGs(), new SFLMSGMessageDefinitionComparer()))
            {
                out.println("Subfile message definitions are different in record " + irecordviewdefinition.getName());
                flag = false;
            }
            if(!equalIterators(subfilecontrolrecordviewdefinition.getSubfileFieldViewDefinitions(), subfilecontrolrecordviewdefinition1.getSubfileFieldViewDefinitions(), new FieldViewDefinitionComparer()))
            {
                out.println("Subfile field view definitions are different in record " + irecordviewdefinition.getName());
                flag = false;
            }
            if(!equalIterators(subfilecontrolrecordviewdefinition.getSubfileDspatrPCFieldInfos(), subfilecontrolrecordviewdefinition1.getSubfileDspatrPCFieldInfos(), new DSPATR_PCFieldInfoComparer()))
            {
                out.println("Subfile DSPATR_PCFieldInfos are differentin record " + irecordviewdefinition.getName());
                flag = false;
            }
            FieldSelectionSubfileHeightInfo fieldselectionsubfileheightinfo = subfilecontrolrecordviewdefinition.getFieldSelectionSubfileHeightInfo();
            FieldSelectionSubfileHeightInfo fieldselectionsubfileheightinfo1 = subfilecontrolrecordviewdefinition1.getFieldSelectionSubfileHeightInfo();
            if(fieldselectionsubfileheightinfo != null || fieldselectionsubfileheightinfo1 != null)
                if(fieldselectionsubfileheightinfo == null || fieldselectionsubfileheightinfo1 == null)
                {
                    out.println("Only one record has field selection subfile heigh info in " + irecordviewdefinition.getName());
                    flag = false;
                } else
                {
                    if(fieldselectionsubfileheightinfo.getMinimumHeight() != fieldselectionsubfileheightinfo1.getMinimumHeight())
                    {
                        out.println("Minimum heights are different for field selection in " + irecordviewdefinition.getName());
                        flag = false;
                    }
                    if(!equalIterators(fieldselectionsubfileheightinfo.getIndicatorAndRowIterator(), fieldselectionsubfileheightinfo1.getIndicatorAndRowIterator(), new IndicatorAndRowComparer()))
                    {
                        out.println("Indicators and rows are different for field selection in " + irecordviewdefinition.getName());
                        flag = false;
                    }
                }
        }
        return flag;
    }

    private static boolean compareFieldVisDefs(String as[], String as1[])
    {
        if(as == null && as1 == null)
            return true;
        if(as == null || as1 == null)
            return false;
        if(as.length != as1.length)
            return false;
        for(int i = 0; i < as.length; i++)
            if(!equal(as[i], as1[i]))
                return false;

        return true;
    }

    public static boolean identicalRecordFeedbackDefinitions(IRecordFeedbackDefinition irecordfeedbackdefinition, IRecordFeedbackDefinition irecordfeedbackdefinition1, PrintWriter printwriter)
    {
        if(null != printwriter)
            out = printwriter;
        return identicalRecordFeedbackDefinitions(irecordfeedbackdefinition, irecordfeedbackdefinition1);
    }

    public static boolean identicalRecordFeedbackDefinitions(IRecordFeedbackDefinition irecordfeedbackdefinition, IRecordFeedbackDefinition irecordfeedbackdefinition1)
    {
        boolean flag = true;
        if(!equal(irecordfeedbackdefinition.getName(), irecordfeedbackdefinition1.getName()))
        {
            out.println("Record names are different!");
            flag = false;
        }
        if(!compareResponseIndicators(irecordfeedbackdefinition, irecordfeedbackdefinition1))
        {
            out.println("Response indicators are different in record " + irecordfeedbackdefinition.getName());
            flag = false;
        }
        if(!equalIterators(irecordfeedbackdefinition.getRTNCSRLOCDefinitions(), irecordfeedbackdefinition1.getRTNCSRLOCDefinitions(), new RTNCSRLOCDefinitionComparer()))
        {
            out.println("RTNCSRLOC definitions are different in record " + irecordfeedbackdefinition.getName());
            flag = false;
        }
        if((irecordfeedbackdefinition instanceof SubfileControlRecordFeedbackDefinition) != (irecordfeedbackdefinition1 instanceof SubfileControlRecordFeedbackDefinition))
        {
            out.println("Only one record is a subfile control definition!");
            flag = false;
        }
        if((irecordfeedbackdefinition instanceof SubfileControlRecordFeedbackDefinition) && (irecordfeedbackdefinition1 instanceof SubfileControlRecordFeedbackDefinition))
        {
            SubfileControlRecordFeedbackDefinition subfilecontrolrecordfeedbackdefinition = (SubfileControlRecordFeedbackDefinition)irecordfeedbackdefinition;
            SubfileControlRecordFeedbackDefinition subfilecontrolrecordfeedbackdefinition1 = (SubfileControlRecordFeedbackDefinition)irecordfeedbackdefinition1;
            if(subfilecontrolrecordfeedbackdefinition.getRowPerSubfile() != subfilecontrolrecordfeedbackdefinition1.getRowPerSubfile() || subfilecontrolrecordfeedbackdefinition.isSFLENDScrollBar() != subfilecontrolrecordfeedbackdefinition1.isSFLENDScrollBar())
            {
                out.println("Subfile attributes differ in record " + irecordfeedbackdefinition.getName());
                flag = false;
            }
            if(!compareResponseIndicators(subfilecontrolrecordfeedbackdefinition.getSubfileContainer(), subfilecontrolrecordfeedbackdefinition1.getSubfileContainer()))
            {
                out.println("Subfile response indicators are different in record " + irecordfeedbackdefinition.getName());
                flag = false;
            }
            if(!equalIterators(subfilecontrolrecordfeedbackdefinition.getKeywordDefinitions(), subfilecontrolrecordfeedbackdefinition1.getKeywordDefinitions(), new KeywordDefinitionComparer()))
            {
                out.println("Keyword definitions are different in record " + irecordfeedbackdefinition.getName());
                flag = false;
            }
            if(!equal(subfilecontrolrecordfeedbackdefinition.getSubfileFoldKey(), subfilecontrolrecordfeedbackdefinition1.getSubfileFoldKey()) || !equal(subfilecontrolrecordfeedbackdefinition.getSubfileDropKey(), subfilecontrolrecordfeedbackdefinition1.getSubfileDropKey()))
            {
                out.println("Subfile fold or drop keys are different in record " + irecordfeedbackdefinition.getName());
                flag = false;
            }
            if(!equal(subfilecontrolrecordfeedbackdefinition.getSubfileModeFieldName(), subfilecontrolrecordfeedbackdefinition1.getSubfileModeFieldName()) || !equal(subfilecontrolrecordfeedbackdefinition.getSubfileCursorRRNFieldName(), subfilecontrolrecordfeedbackdefinition1.getSubfileCursorRRNFieldName()) || !equal(subfilecontrolrecordfeedbackdefinition.getSubfileScrollFieldName(), subfilecontrolrecordfeedbackdefinition1.getSubfileScrollFieldName()) || !equal(subfilecontrolrecordfeedbackdefinition.getSubfileRecordNumberFieldName(), subfilecontrolrecordfeedbackdefinition1.getSubfileRecordNumberFieldName()))
            {
                out.println("Subfile mode, cursorRRN, scroll, or recordnumber field names differ in record " + irecordfeedbackdefinition.getName());
                flag = false;
            }
        }
        return flag;
    }

    private static boolean compareResponseIndicators(IElementContainer ielementcontainer, IElementContainer ielementcontainer1)
    {
        boolean flag = true;
        if(!equalIterators(ielementcontainer.iterator(com.ibm.as400ad.webfacing.runtime.view.def.AnyFieldResponseIndicator.class), ielementcontainer1.iterator(com.ibm.as400ad.webfacing.runtime.view.def.AnyFieldResponseIndicator.class), new ResponseIndicatorComparer()))
        {
            out.println("Any field response indicators are different");
            flag = false;
        }
        if(!equalIterators(ielementcontainer.iterator(com.ibm.as400ad.webfacing.runtime.view.def.FieldResponseIndicator.class), ielementcontainer1.iterator(com.ibm.as400ad.webfacing.runtime.view.def.FieldResponseIndicator.class), new ResponseIndicatorComparer()))
        {
            out.println("Field response indicators are different");
            flag = false;
        }
        if(!equalIterators(ielementcontainer.iterator(com.ibm.as400ad.webfacing.runtime.view.def.AnyAIDKeyResponseIndicator.class), ielementcontainer1.iterator(com.ibm.as400ad.webfacing.runtime.view.def.AnyAIDKeyResponseIndicator.class), new ResponseIndicatorComparer()))
        {
            out.println("Any AIDKey response indicators are different");
            flag = false;
        }
        if(!equalIterators(ielementcontainer.iterator(com.ibm.as400ad.webfacing.runtime.view.def.AIDKeyResponseIndicator.class), ielementcontainer1.iterator(com.ibm.as400ad.webfacing.runtime.view.def.AIDKeyResponseIndicator.class), new ResponseIndicatorComparer()))
        {
            out.println("AIDKey response indicators are different");
            flag = false;
        }
        if(!equalIterators(ielementcontainer.iterator(com.ibm.as400ad.webfacing.runtime.view.def.BLANKSResponseIndicator.class), ielementcontainer1.iterator(com.ibm.as400ad.webfacing.runtime.view.def.BLANKSResponseIndicator.class), new ResponseIndicatorComparer()))
        {
            out.println("BLANKS response indicators are different");
            flag = false;
        }
        if(!equalIterators(ielementcontainer.iterator(com.ibm.as400ad.webfacing.runtime.view.def.HLPRTNResponseIndicator.class), ielementcontainer1.iterator(com.ibm.as400ad.webfacing.runtime.view.def.HLPRTNResponseIndicator.class), new ResponseIndicatorComparer()))
        {
            out.println("HLPRTN response indicators are different");
            flag = false;
        }
        return flag;
    }

    private static PrintWriter out;

    static 
    {
        out = new PrintWriter(System.out);
    }

}
