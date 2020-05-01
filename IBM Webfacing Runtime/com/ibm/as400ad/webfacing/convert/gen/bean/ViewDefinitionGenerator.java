// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordParmType;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.convert.*;
import com.ibm.as400ad.webfacing.convert.gen.WebResourceFileWriter;
import com.ibm.as400ad.webfacing.convert.model.*;
import com.ibm.as400ad.webfacing.convert.rules.DefaultCommandKeyLabelListCreater;
import com.ibm.as400ad.webfacing.runtime.view.CommandKeyLabelList;
import com.ibm.as400ad.webfacing.runtime.view.def.*;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            DefinitionGenerator, KeyLabelsInPropertyFile, ReferencedAIDKey, KeySequence, 
//            JavaSourceCodeCollection, JavaFieldSourceCode, JavaClassSourceCode

public class ViewDefinitionGenerator extends DefinitionGenerator
    implements IWebResourceGenerator, ENUM_KeywordIdentifiers, ENUM_KeywordParmType
{

    public ViewDefinitionGenerator(RecordLayout recordlayout, WebResourceFileWriter webresourcefilewriter)
    {
        super(recordlayout, webresourcefilewriter);
        _exportSettings = null;
        _defaultCommandKeyLabelList = null;
        _definedKeyLabelList = null;
        _visibilityCondtionedKeyLabelList = new CommandKeyLabelList();
        _visibleCommandKeyLabelList = new CommandKeyLabelList();
        _indicatorConditionedKeyLabelList = new CommandKeyLabelList();
        _keyLabelsInPropertyFile = new KeyLabelsInPropertyFile();
        _defaultCommandKeyLabelList = DefaultCommandKeyLabelListCreater.getDefaultCMDKeyLabelListCreater().getDefaultCommandKeyLabelList();
        _definedKeyLabelList = getMergedCommandKeyLabelList(getCommandKeyLabelListFromNode(getRecordNode()), getCommandKeyLabelListFromNode(getFileNode()));
    }

    private void findCommandKeyKeywords(AnyNodeWithKeywords anynodewithkeywords, KeySequence keysequence)
    {
        try
        {
            Vector vector = anynodewithkeywords.getKeywordsVector();
            if(vector != null)
            {
                for(int i = 0; i < vector.size(); i++)
                {
                    KeywordNode keywordnode = (KeywordNode)vector.elementAt(i);
                    switch(keywordnode.getKeywordId())
                    {
                    case 14: // '\016'
                    case 15: // '\017'
                    case 16: // '\020'
                    case 17: // '\021'
                    case 18: // '\022'
                    case 19: // '\023'
                    case 20: // '\024'
                    case 21: // '\025'
                    case 22: // '\026'
                    case 23: // '\027'
                    case 24: // '\030'
                    case 25: // '\031'
                    case 26: // '\032'
                    case 27: // '\033'
                    case 28: // '\034'
                    case 29: // '\035'
                    case 30: // '\036'
                    case 31: // '\037'
                    case 32: // ' '
                    case 33: // '!'
                    case 34: // '"'
                    case 35: // '#'
                    case 36: // '$'
                    case 37: // '%'
                    case 38: // '&'
                    case 39: // '\''
                    case 40: // '('
                    case 41: // ')'
                    case 42: // '*'
                    case 43: // '+'
                    case 44: // ','
                    case 45: // '-'
                    case 46: // '.'
                    case 47: // '/'
                    case 48: // '0'
                    case 49: // '1'
                    case 50: // '2'
                    case 51: // '3'
                    case 52: // '4'
                    case 53: // '5'
                    case 54: // '6'
                    case 55: // '7'
                    case 56: // '8'
                    case 57: // '9'
                    case 58: // ':'
                    case 59: // ';'
                    case 60: // '<'
                    case 61: // '='
                        keysequence.addCommandKey(new ReferencedAIDKey(keywordnode));
                        break;

                    case 185: 
                    case 190: 
                        keysequence.addCommandKey(new ReferencedAIDKey(keywordnode));
                        break;
                    }
                }

            }
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in ViewBeanGenerator.findCommandKeyKeywords() while generating " + getBeanName() + " = " + throwable);
        }
    }

    private void findFunctionKeyKeywords(AnyNodeWithKeywords anynodewithkeywords, KeySequence keysequence)
    {
        try
        {
            Vector vector = anynodewithkeywords.getKeywordsVector();
            if(vector != null)
            {
                for(int i = 0; i < vector.size(); i++)
                {
                    KeywordNode keywordnode = (KeywordNode)vector.elementAt(i);
                    switch(keywordnode.getKeywordId())
                    {
                    default:
                        break;

                    case 73: // 'I'
                    case 107: // 'k'
                    case 123: // '{'
                    case 155: 
                    case 156: 
                    case 172: 
                    case 173: 
                        keysequence.addFunctionKey(new ReferencedAIDKey(keywordnode));
                        break;

                    case 158: 
                        KeywordParm keywordparm = keywordnode.getFirstParm();
                        if(keywordparm != null && (keywordparm.getParmType() == 2 || keywordparm.getParmType() == 23))
                            keysequence.addFunctionKey(new ReferencedAIDKey(keywordnode));
                        break;
                    }
                }

            }
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in ViewBeanGenerator.findFunctionKeyKeywords() while generating " + getBeanName() + " = " + throwable);
        }
    }

    private void generateAIDKeyDefinition(IReferencedAIDKey ireferencedaidkey, CommandKeyLabelList commandkeylabellist, boolean flag)
    {
        try
        {
            StringBuffer stringbuffer = new StringBuffer();
            String s = ireferencedaidkey.name();
            String s1 = ireferencedaidkey.name();
            int i = -1;
            if(commandkeylabellist != null && commandkeylabellist.getLabel(s) != null && commandkeylabellist.getLabel(s).getKeyLabel() != null)
            {
                s1 = commandkeylabellist.getLabel(s).getKeyLabel();
                i = commandkeylabellist.getLabel(s).getPriority();
            }
            stringbuffer.append("new AIDKey(\"" + s + "\", \"" + WebfacingConstants.getJavaString(s1) + "\", \"" + super.getBeanName() + "\"," + i);
            if(ireferencedaidkey.indicatorString() != null)
                stringbuffer.append(", \"" + ireferencedaidkey.indicatorString() + "\"");
            if(flag)
                stringbuffer.append(", false");
            stringbuffer.append(")");
            addLineToConstructor("add(" + stringbuffer.toString() + ");");
            if(ireferencedaidkey.name().equals("PAGEDOWN") || ireferencedaidkey.name().equals("ROLLUP"))
                addLineToConstructor("add(new KeywordDefinition(KWD_PAGEDOWN" + (ireferencedaidkey.indicatorString() == null ? "" : ", \"" + ireferencedaidkey.indicatorString() + "\"") + "));");
            if(ireferencedaidkey.name().equals("PAGEUP") || ireferencedaidkey.name().equals("ROLLDOWN"))
                addLineToConstructor("add(new KeywordDefinition(KWD_PAGEUP" + (ireferencedaidkey.indicatorString() == null ? "" : ", \"" + ireferencedaidkey.indicatorString() + "\"") + "));");
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in ViewBeanGenerator.generateAIDKeyDefinition() while generating " + getBeanName() + " = " + throwable);
        }
    }

    protected void generateAIDKeyDefinitions()
    {
        try
        {
            CommandKeyLabelList commandkeylabellist = new CommandKeyLabelList();
            commandkeylabellist.mergeList(_defaultCommandKeyLabelList);
            commandkeylabellist.mergeList(_definedKeyLabelList);
            commandkeylabellist.mergeList(_visibleCommandKeyLabelList);
            KeySequence keysequence = new KeySequence(getRecordNode(), (CommandKeyLabelList)_visibleCommandKeyLabelList.clone());
            findFunctionKeyKeywords(getFileNode(), keysequence);
            findFunctionKeyKeywords(getRecordNode(), keysequence);
            findCommandKeyKeywords(getFileNode(), keysequence);
            findCommandKeyKeywords(getRecordNode(), keysequence);
            for(Iterator iterator = keysequence.getSequencedCommandKeys(); iterator.hasNext(); generateAIDKeyDefinition((IReferencedAIDKey)iterator.next(), commandkeylabellist, false));
            for(Iterator iterator1 = keysequence.getSequencedFunctionKeys(); iterator1.hasNext(); generateAIDKeyDefinition((IReferencedAIDKey)iterator1.next(), commandkeylabellist, false));
            Iterator iterator2 = keysequence.getHiddenFunctionKeys();
            if(iterator2 != null)
                for(; iterator2.hasNext(); generateAIDKeyDefinition((IReferencedAIDKey)iterator2.next(), commandkeylabellist, true));
            iterator2 = keysequence.getHiddenCommandKeys();
            if(iterator2 != null)
                for(; iterator2.hasNext(); generateAIDKeyDefinition((IReferencedAIDKey)iterator2.next(), commandkeylabellist, true));
            generateCommandKeyLabelDefinitions(keysequence.getVisibleKeyLabelList());
            generateConditionedCommandKeyLabelDefinitions(_visibilityCondtionedKeyLabelList);
            generateConditionedCommandKeyLabelDefinitions(_indicatorConditionedKeyLabelList);
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in ViewBeanGenerator.generateAIDKeyDefinitions() while generating " + getBeanName() + " = " + throwable);
        }
    }

    private void generateCLRLKeyword(RecordNode recordnode)
    {
        KeywordNodeEnumeration keywordnodeenumeration = recordnode.getKeywordsOfType(74);
        if(keywordnodeenumeration.hasMoreElements() && !recordnode.isWindow())
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            String s = keywordnode.getParmsAsString();
            RecordLayout recordlayout = getRecordLayout();
            addLineToConstructor("KeywordDefinition clrlKwd = new KeywordDefinition(KWD_CLRL);");
            addLineToConstructor("clrlKwd.addParameter(\"" + s + "\");");
            if(s.equals("*NO") || !s.equals("*ALL") && !s.equals("*END") && Integer.parseInt(s) <= recordlayout.getLastRow() - getRecordLayout().getFirstRow())
            {
                addLineToConstructor("setFirstColumn(" + recordlayout.getFirstColumn() + ");");
                addLineToConstructor("setLastColumn(" + recordlayout.getLastColumn() + ");");
                if(recordlayout.isCLRLWindow())
                {
                    for(int i = 0; i < recordlayout.getNumberOfCLRLWindowTitles(); i++)
                    {
                        String s1;
                        if(recordlayout.isCLRLWindowTitleConstant(i))
                            s1 = "true";
                        else
                            s1 = "false";
                        addLineToConstructor("WindowTitleDefinition clrlWdwTitleDef" + (i + 1) + " = new WindowTitleDefinition(\"" + recordlayout.getCLRLWindowTitleIndicatorExpression(i) + "\", " + s1 + ", \"" + recordlayout.getCLRLWindowTitle(i) + "\");");
                        addLineToConstructor("add(clrlWdwTitleDef" + (i + 1) + ");");
                    }

                }
            }
            addLineToConstructor("add(clrlKwd);");
        }
    }

    protected void generateDisplaySizeInfo()
    {
        if(getRecordLayout().getDisplaySizeIndex() == 1)
            addLineToConstructor("setIsWide(true);");
        addLineToConstructor("setPrimaryFileDisplaySize(new Integer(" + getFileNode().getPrimaryDisplaySize() + "));");
        if(getFileNode().isDspSizConditioned())
            addLineToConstructor("setSecondaryFileDisplaySize(new Integer(" + getFileNode().getSecondaryDisplaySize() + "));");
    }

    protected void generateDspatrPCInfo()
    {
        for(FieldOutputEnumeration fieldoutputenumeration = getRecordLayout().getDisplayableFields(); fieldoutputenumeration.hasMoreElements();)
        {
            IFieldOutput ifieldoutput = fieldoutputenumeration.nextFieldOutput();
            String s = ifieldoutput.getFieldNode().getDisplayAttributes().getPCIndExpr();
            if(s != null)
                addLineToConstructor("add(new DSPATR_PCFieldInfo(\"" + ifieldoutput.getFieldName() + "\", \"" + s + "\"));");
        }

    }

    protected void generateFieldViewDefinitions()
    {
        try
        {
            boolean flag = true;
            for(FieldOutputEnumeration fieldoutputenumeration = getRecordLayout().getDisplayableFields(); fieldoutputenumeration.hasMoreElements();)
            {
                IFieldOutput ifieldoutput = fieldoutputenumeration.nextFieldOutput();
                if(ifieldoutput.getFieldNode().isUnnamedConstantField())
                {
                    if(!((FieldOutput)ifieldoutput).isComputed())
                    {
                        ((ConstantFieldOutput)ifieldoutput).updateStringMatchedKeyLabelList();
                        updateRecordStringMatchedKeyLabelList((ConstantFieldOutput)ifieldoutput);
                    }
                } else
                {
                    if(flag && (ifieldoutput.getFieldNode().getFieldUsage() == 'B' || ifieldoutput.getFieldNode().getFieldUsage() == 'I'))
                        flag = false;
                    getConstructor().addAll(ifieldoutput.getViewBeanInitialization());
                    addLineToConstructor("add(" + ifieldoutput.getQualifiedFieldName() + ");");
                }
            }

            setOutputOnly(flag);
            if(getRecordLayout().getFieldVisDefList().size() > 0)
                addLineToConstructor("setFieldVisDef(" + FIELD_VIS_DEF_NAME + ");");
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in ViewBeanGenerator.generateFieldViewDefinitions() while generating " + getBeanName() + " = " + throwable);
        }
    }

    void generateFieldVisDef(JavaClassSourceCode javaclasssourcecode)
    {
        generateFieldVisDef(((List) (getRecordLayout().getFieldVisDefList())), FIELD_VIS_DEF_NAME, javaclasssourcecode);
    }

    void generateFieldVisDef(List list, String s, JavaClassSourceCode javaclasssourcecode)
    {
        if(list.size() > 0)
        {
            JavaSourceCodeCollection javasourcecodecollection = new JavaSourceCodeCollection();
            javasourcecodecollection.addLine("{");
            Iterator iterator = list.iterator();
            if(iterator.hasNext())
            {
                String s1;
                for(s1 = (String)iterator.next(); iterator.hasNext(); s1 = (String)iterator.next())
                    javasourcecodecollection.addLine(s1 + ",");

                javasourcecodecollection.addLine(s1);
            }
            javasourcecodecollection.addLine("}");
            JavaFieldSourceCode javafieldsourcecode = new JavaFieldSourceCode(s, "String[]", "public", javasourcecodecollection, generateFieldVisDefComment());
            javafieldsourcecode.addModifier("final");
            javafieldsourcecode.addModifier("static");
            javaclasssourcecode.addField(javafieldsourcecode);
        }
    }

    JavaSourceCodeCollection generateFieldVisDefComment()
    {
        JavaSourceCodeCollection javasourcecodecollection = new JavaSourceCodeCollection();
        javasourcecodecollection.addLine("/*");
        javasourcecodecollection.addLine(" * This array is used to evaluate field visibility in the record view bean, and finally");
        javasourcecodecollection.addLine(" * to determine visibility of fields on the screen. There is one String in the array");
        javasourcecodecollection.addLine(" * for each field in the record that is displayed conditionally. The Strings include the ");
        javasourcecodecollection.addLine(" * indicator expression to condition the field, and the indices into the this array of");
        javasourcecodecollection.addLine(" * the fields that overlap and may override the field.  For example take");
        javasourcecodecollection.addLine(" *");
        javasourcecodecollection.addLine(" *   /* (3) /* \"FIELD5:N31:0-2\",");
        javasourcecodecollection.addLine(" *");
        javasourcecodecollection.addLine(" * This would signify that FIELD5 (at index 3 in the array) has an indicator expression ");
        javasourcecodecollection.addLine(" * of N31, and the fields at indices 0, 1, and 2 may override FIELD5 if they are ");
        javasourcecodecollection.addLine(" * displayed.  In other words FIELD5 would be visible as long as N31 is evaluated ");
        javasourcecodecollection.addLine(" * as true and the fields at indices 0, 1, and 2 are not visible. ");
        javasourcecodecollection.addLine(" *");
        javasourcecodecollection.addLine(" * Note that a-b is used to indicate indices a through b inclusive, where a and b are ");
        javasourcecodecollection.addLine(" * indices of this array and a < b.  Otherwise, noncontiguous indices are seperated by");
        javasourcecodecollection.addLine(" * commas (ie. \"N45:2,5-7,9\").");
        javasourcecodecollection.addLine(" *");
        javasourcecodecollection.addLine(" * IMPORTANT : no indicator expression for a field is indicated by a space character.");
        javasourcecodecollection.addLine(" * For example, if you wished FIELD5 to not be conditioned by any indicator expression");
        javasourcecodecollection.addLine(" * you could have a field visibility definition entry of");
        javasourcecodecollection.addLine(" *");
        javasourcecodecollection.addLine(" *   /* (3) /* \"FIELD5: :0-2\"");
        javasourcecodecollection.addLine(" *");
        javasourcecodecollection.addLine(" * Do not exclude the space character.");
        javasourcecodecollection.addLine(" *");
        javasourcecodecollection.addLine(" * If a named field is never visible this may be represented by putting NEVER");
        javasourcecodecollection.addLine(" * in the place of the indicator expression (ie. \"FIELD6:NEVER\").");
        javasourcecodecollection.addLine(" */");
        return javasourcecodecollection;
    }

    protected void generateOnlineHelpInfo()
    {
        FileNode filenode = getFileNode();
        RecordNode recordnode = getRecordNode();
        getConstructor().addKeywordWithNoParam(filenode.getKeywordsOfType(118));
        getConstructor().addKeywordWithNoParam(recordnode.getKeywordsOfType(118));
        getConstructor().addKeywordWithNoParam(recordnode.getKeywordsOfType(110));
        KeywordNodeEnumeration keywordnodeenumeration = recordnode.getKeywordsOfType(122);
        int i;
        for(i = 1; keywordnodeenumeration.hasMoreElements(); i++)
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            String s = keywordnode.getFirstParm().getJavaString();
            String s2 = keywordnode.getIndicatorString();
            addLineToConstructor("KeywordDefinition kwdHLPTITLE$" + i + " = new KeywordDefinition(KWD_HLPTITLE" + (s2 != null ? ", \"" + s2 + "\"" : "") + ");");
            addLineToConstructor("kwdHLPTITLE$" + i + ".addParameter(\"" + s + "\");");
            addLineToConstructor("add(kwdHLPTITLE$" + i + ");");
        }

        if(i == 1)
        {
            KeywordNode keywordnode1 = filenode.findKeywordById(122);
            if(null != keywordnode1)
            {
                String s1 = keywordnode1.getFirstParm().getJavaString();
                addLineToConstructor("KeywordDefinition kwdHLPTITLE = new KeywordDefinition(KWD_HLPTITLE);");
                addLineToConstructor("kwdHLPTITLE.addParameter(\"" + s1 + "\");");
                addLineToConstructor("add(kwdHLPTITLE);");
            }
        }
        Iterator iterator = recordnode.getHelpspecs();
        i = 1;
        if(iterator != null)
            while(iterator.hasNext()) 
            {
                HelpspecNode helpspecnode = (HelpspecNode)iterator.next();
                if(helpspecnode != null)
                {
                    int j = helpspecnode.getType();
                    if(j == 117 || j == 116)
                    {
                        int k = helpspecnode.getAreaType();
                        String s5 = "HelpArea helpArea$" + i + " = new HelpArea(";
                        if(helpspecnode.needHelpAreaRect())
                        {
                            RecordLayout recordlayout = getRecordLayout();
                            if(k == 296)
                            {
                                s5 = s5 + recordlayout.getFirstRow() + ", " + 1 + ", " + recordlayout.getLastRow() + ", " + recordlayout.getScreenOrWindowWidth();
                            } else
                            {
                                for(FieldOutputEnumeration fieldoutputenumeration = recordlayout.getDisplayableFields(); fieldoutputenumeration.hasMoreElements();)
                                {
                                    IFieldOutput ifieldoutput = fieldoutputenumeration.nextFieldOutput();
                                    FieldNode fieldnode = ifieldoutput.getFieldNode();
                                    if(k != 297 ? k != 298 || !fieldnode.isNamed() && fieldnode.getHelpId() == helpspecnode.getFieldId() : !fieldnode.isUnnamedConstantField() && fieldnode.getName().equals(helpspecnode.getField()))
                                    {
                                        int l = ifieldoutput.getRow();
                                        int i1 = ifieldoutput.getColumn();
                                        int j1 = (l + ifieldoutput.getHeight()) - 1;
                                        int k1 = (i1 + ifieldoutput.getWidth()) - 1;
                                        s5 = s5 + l + ", " + i1 + ", " + j1 + ", " + k1;
                                        break;
                                    }
                                }

                            }
                        } else
                        {
                            s5 = s5 + helpspecnode.getRectangleAsString();
                        }
                        s5 = s5 + ");";
                        addLineToConstructor(s5);
                        if(k == 297)
                        {
                            String s8 = "\"" + helpspecnode.getField() + "\"";
                            if(helpspecnode.getFieldChoice() > 0)
                                s8 = s8 + ", " + helpspecnode.getFieldChoice();
                            addLineToConstructor("helpArea$" + i + ".setField(" + s8 + ");");
                        }
                        String s9 = WebfacingConstants.trimQuotes(helpspecnode.getName());
                        String s11 = j != 117 ? "HelpPanelGroup" : "HelpRecord";
                        String s12 = helpspecnode.getHelpObj() == null ? "" : ", \"" + helpspecnode.getHelpObj() + "\"";
                        String s13 = helpspecnode.getHelpLib() == null ? "" : ", \"" + helpspecnode.getHelpLib() + "\"";
                        addLineToConstructor("HelpDefinition help$" + i + " = new " + s11 + "(\"" + recordnode.getName() + "\", \"" + s9 + "\"" + s12 + s13 + ");");
                        if(helpspecnode.getIndicatorString() != null)
                            addLineToConstructor("help$" + i + ".setIndicatorExpression(\"" + helpspecnode.getIndicatorString() + "\");");
                        addLineToConstructor("helpArea$" + i + ".setHelpDefinition(help$" + i + ");");
                        if(helpspecnode.isBoundary())
                        {
                            KeywordNode keywordnode4 = helpspecnode.findKeywordById(109);
                            String s14 = keywordnode4.getIndicatorString();
                            addLineToConstructor("HelpKeyword kwdHLPBDY$" + i + " = new HelpKeyword(KWD_HLPBDY" + (s14 != null ? ", \"" + s14 + "\"" : "") + ");");
                            addLineToConstructor("helpArea$" + i + ".setHelpBoundary(kwdHLPBDY$" + i + ");");
                        }
                        if(helpspecnode.isExcluded())
                        {
                            KeywordNode keywordnode5 = helpspecnode.findKeywordById(113);
                            String s15 = keywordnode5.getIndicatorString();
                            addLineToConstructor("HelpKeyword kwdHLPEXCLD$" + i + " = new HelpKeyword(KWD_HLPEXCLD" + (s15 != null ? ", \"" + s15 + "\"" : "") + ");");
                            addLineToConstructor("helpArea$" + i + ".setHelpExcluded(kwdHLPEXCLD$" + i + ");");
                        }
                        addLineToConstructor("add(helpArea$" + i + ");");
                        i++;
                    }
                }
            }
        KeywordNode keywordnode2 = filenode.findKeywordById(117);
        if(keywordnode2 == null)
            keywordnode2 = filenode.findKeywordById(116);
        if(keywordnode2 != null)
        {
            String s3 = keywordnode2.getKeywordId() != 117 ? "HelpPanelGroup" : "HelpRecord";
            String s4 = null;
            String s6 = "";
            String s10 = "";
            Vector vector = keywordnode2.getParmsVector();
            switch(vector.size())
            {
            case 2: // '\002'
                StringTokenizer stringtokenizer = new StringTokenizer(((KeywordParm)vector.elementAt(1)).getVarString(), "/");
                if(stringtokenizer.countTokens() == 2)
                    s6 = ", \"" + stringtokenizer.nextToken() + "\"";
                s10 = ", \"" + stringtokenizer.nextToken() + "\"";
                // fall through

            case 1: // '\001'
                s4 = ((KeywordParm)vector.elementAt(0)).getVarString();
                s4 = WebfacingConstants.trimQuotes(s4);
                // fall through

            default:
                addLineToConstructor("HelpArea helpArea$" + i + " = new HelpArea();");
                addLineToConstructor("HelpDefinition help$" + i + " = new " + s3 + "(\"" + recordnode.getName() + "\", \"" + s4 + "\"" + s10 + s6 + ");");
                break;
            }
            if(keywordnode2.getIndicatorString() != null)
                addLineToConstructor("help$" + i + ".setIndicatorExpression(\"" + keywordnode2.getIndicatorString() + "\");");
            addLineToConstructor("helpArea$" + i + ".setHelpDefinition(help$" + i + ");");
            addLineToConstructor("add(helpArea$" + i + ");");
        }
        for(RecordNodeEnumeration recordnodeenumeration = filenode.getRecords(); recordnodeenumeration.hasMoreElements();)
        {
            RecordNode recordnode1 = (RecordNode)recordnodeenumeration.nextElement();
            String s7 = recordnode1.getName();
            KeywordNode keywordnode3 = recordnode1.findKeywordById(120);
            if(keywordnode3 != null)
            {
                Vector vector1 = keywordnode3.getParmsVector();
                addLineToConstructor("HelpGroup helpGroup$" + s7 + " = new HelpGroup(\"" + ((KeywordParm)vector1.elementAt(0)).getVarString() + "\", \"" + s7 + "\", " + ((KeywordParm)vector1.elementAt(1)).getVarNumber() + ");");
                addLineToConstructor("add(helpGroup$" + s7 + ");");
            }
        }

    }

    private void generateCommandKeyLabelDefinitions(CommandKeyLabelList commandkeylabellist)
    {
        if(null != commandkeylabellist)
        {
            CommandKeyLabel commandkeylabel;
            for(Iterator iterator = commandkeylabellist.getLabels(); iterator.hasNext(); addLineToConstructor("add(new CommandKeyLabel(\"" + commandkeylabel.getKeyName() + "\", \"" + WebfacingConstants.getJavaString(commandkeylabel.getKeyLabel()) + "\", \"" + super.getBeanName() + "\"," + commandkeylabel.getPriority() + "));"))
                commandkeylabel = (CommandKeyLabel)iterator.next();

        }
    }

    private void generateConditionedCommandKeyLabelDefinitions(CommandKeyLabelList commandkeylabellist)
    {
        if(null != commandkeylabellist)
        {
            int i = 0;
            for(Iterator iterator = commandkeylabellist.getLabels(); iterator.hasNext();)
            {
                ConditionedCommandKeyLabel conditionedcommandkeylabel = (ConditionedCommandKeyLabel)iterator.next();
                ArrayList arraylist = conditionedcommandkeylabel.getConditionedLabels();
                if(arraylist.size() > 0)
                {
                    String s;
                    String s1;
                    if(conditionedcommandkeylabel instanceof IndicatorConditionedCommandKeyLabel)
                    {
                        s = "IndicatorConditionedCommandKeyLabel";
                        s1 = "icckl";
                    } else
                    {
                        s = "VisibilityConditionedCommandKeyLabel";
                        s1 = "vcckl";
                    }
                    if(i == 0)
                    {
                        addLineToConstructor(s + " " + s1 + ";");
                        i++;
                    }
                    String s2 = null;
                    if(conditionedcommandkeylabel.getKeyLabel() != null)
                        s2 = "\"" + WebfacingConstants.getJavaString(conditionedcommandkeylabel.getKeyLabel()) + "\"";
                    addLineToConstructor(s1 + " = new " + s + "(\"" + conditionedcommandkeylabel.getKeyName() + "\", " + s2 + ", \"" + super.getBeanName() + "\"," + conditionedcommandkeylabel.getPriority() + ");");
                    for(int j = 0; j < arraylist.size(); j++)
                    {
                        ConditionedLabel conditionedlabel = (ConditionedLabel)arraylist.get(j);
                        if(conditionedlabel instanceof IndicatorConditionedLabel)
                        {
                            IndicatorConditionedLabel indicatorconditionedlabel = (IndicatorConditionedLabel)conditionedlabel;
                            addLineToConstructor("icckl.addAConditionedLabel(new IndicatorConditionedLabel(\"" + WebfacingConstants.getJavaString(indicatorconditionedlabel.getLabel()) + "\",\"" + indicatorconditionedlabel.getIndicatorExpression() + "\"));");
                        } else
                        {
                            VisibilityConditionedLabel visibilityconditionedlabel = (VisibilityConditionedLabel)conditionedlabel;
                            addLineToConstructor("vcckl.addAConditionedLabel(new VisibilityConditionedLabel(\"" + WebfacingConstants.getJavaString(visibilityconditionedlabel.getLabel()) + "\",\"" + visibilityconditionedlabel.getFieldID() + "\"));");
                        }
                    }

                    addLineToConstructor("add(" + s1 + ");");
                }
            }

        }
    }

    protected void generateSetFirstFieldLine()
    {
        int i = getRecordLayout().getFirstRow();
        addLineToConstructor("setFirstFieldLine(" + i + ");");
    }

    protected void generateSetLastFieldLine()
    {
        int i = getRecordLayout().getLastRow();
        addLineToConstructor("setLastFieldLine(" + i + ");");
    }

    protected void generateSetWdwStart()
    {
        if(getRecordNode().isWindow())
        {
            RecordNodeWdwInfo recordnodewdwinfo = getRecordNode().getWdwInfo(getRecordLayout().getDisplaySizeIndex());
            if(recordnodewdwinfo.def && !recordnodewdwinfo.fWdwDFT)
            {
                if(recordnodewdwinfo.hardlin)
                {
                    addLineToConstructor("setWdwStartLine(" + recordnodewdwinfo.strLin + ");");
                } else
                {
                    String s = recordnodewdwinfo.szRowField;
                    try
                    {
                        s = WebfacingConstants.replaceSpecialCharacters(s);
                    }
                    catch(Throwable throwable) { }
                    addLineToConstructor("setWdwStartLineField(\"" + s + "\");");
                }
                if(recordnodewdwinfo.hardpos)
                {
                    addLineToConstructor("setWdwStartPos(" + recordnodewdwinfo.strPos + ");");
                } else
                {
                    String s1 = recordnodewdwinfo.szColField;
                    try
                    {
                        s1 = WebfacingConstants.replaceSpecialCharacters(s1);
                    }
                    catch(Throwable throwable1) { }
                    addLineToConstructor("setWdwStartPosField(\"" + s1 + "\");");
                }
            }
        }
    }

    private void generateSLNOKeyword(RecordNode recordnode)
    {
        KeywordNodeEnumeration keywordnodeenumeration = recordnode.getKeywordsOfType(209);
        if(keywordnodeenumeration.hasMoreElements())
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            addLineToConstructor("KeywordDefinition slnoKwd = new KeywordDefinition(KWD_SLNO);");
            addLineToConstructor("slnoKwd.addParameter(\"" + keywordnode.getParmsAsString() + "\");");
            addLineToConstructor("add(slnoKwd);");
        }
    }

    protected void generateViewKeywords()
    {
        try
        {
            RecordNode recordnode = getRecordNode();
            String s = recordnode.getName();
            getConstructor().addKeywordWithNoParam(recordnode.getKeywordsOfType(169));
            getConstructor().addKeywordWithNoParam(recordnode.getKeywordsOfType(168));
            getConstructor().addKeywordWithNoParam(recordnode.getKeywordsOfType(152));
            getConstructor().addKeywordWithNoParam(recordnode.getKeywordsOfType(159));
            getConstructor().addKeywordWithNoParam(recordnode.getKeywordsOfType(163));
            generateCLRLKeyword(recordnode);
            generateSLNOKeyword(recordnode);
            getConstructor().addKeywordDefinition(recordnode.getKeywordsOfType(137));
            KeywordNodeEnumeration keywordnodeenumeration = recordnode.getKeywordsOfType(97);
            String s1 = "EraseKwd";
            boolean flag = false;
            for(int i = 1; keywordnodeenumeration.hasMoreElements(); i++)
            {
                boolean flag1 = true;
                String s2 = Integer.toString(i);
                KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
                int j = 0;
                KeywordParm keywordparm = keywordnode.getParm(j);
                String s5 = keywordnode.getIndicatorString();
                if(s5 == null)
                    s5 = "";
                addLineToConstructor("KeywordDefinition " + s1 + s2 + " = new KeywordDefinition(KWD_ERASE, \"" + s5 + "\");");
                for(; keywordparm != null; keywordparm = keywordnode.getParm(++j))
                {
                    String s6 = keywordparm.getVarString();
                    try
                    {
                        s6 = WebfacingConstants.replaceSpecialCharacters(s6);
                    }
                    catch(Throwable throwable2) { }
                    addLineToConstructor(s1 + s2 + ".addParameter(\"" + s6 + "\");");
                }

                addLineToConstructor("add(" + s1 + s2 + ");");
            }

            getConstructor().addKeywordWithNoParam(recordnode.getKeywordsOfType(175));
            if(recordnode.isWindow())
            {
                RecordNodeWdwInfo recordnodewdwinfo = recordnode.getWdwInfo(getRecordLayout().getDisplaySizeIndex());
                addLineToConstructor("KeywordDefinition wdwKwd = new KeywordDefinition(KWD_WINDOW);");
                addLineToConstructor("wdwKwd.addParameter(\"" + recordnodewdwinfo.rows + "\");");
                addLineToConstructor("wdwKwd.addParameter(\"" + recordnodewdwinfo.cols + "\");");
                if(recordnodewdwinfo.def)
                {
                    if(recordnodewdwinfo.fWdwDFT)
                        addLineToConstructor("wdwKwd.addParameter(\"*DFT\");");
                } else
                {
                    String s3 = recordnodewdwinfo.szNamedWdw;
                    try
                    {
                        s3 = WebfacingConstants.replaceSpecialCharacters(s3);
                    }
                    catch(Throwable throwable1) { }
                    addLineToConstructor("wdwKwd.addParameter(\"" + s3 + "\");");
                }
                addLineToConstructor("add(wdwKwd);");
                if(recordnodewdwinfo.def)
                {
                    getConstructor().addKeywordWithNoParam(recordnode.getKeywordsOfType(171));
                    generateWDWTITLEKeyword(recordnode);
                }
            }
            FileNode filenode = getFileNode();
            if(filenode.isKEEPSpecified())
            {
                keywordnodeenumeration = recordnode.getKeywordsOfType(131);
                if(keywordnodeenumeration.hasMoreElements())
                {
                    addLineToConstructor("KeywordDefinition keepKwd = new KeywordDefinition(KWD_KEEP);");
                    addLineToConstructor("keepKwd.addParameter(\"YES\");");
                    addLineToConstructor("add(keepKwd);");
                } else
                {
                    addLineToConstructor("add(new KeywordDefinition(KWD_KEEP));");
                }
            }
            if(filenode.isASSUMESpecified())
            {
                keywordnodeenumeration = recordnode.getKeywordsOfType(9);
                if(keywordnodeenumeration.hasMoreElements())
                {
                    addLineToConstructor("KeywordDefinition assumeKwd = new KeywordDefinition(KWD_ASSUME);");
                    addLineToConstructor("assumeKwd.addParameter(\"YES\");");
                    addLineToConstructor("add(assumeKwd);");
                } else
                {
                    addLineToConstructor("add(new KeywordDefinition(KWD_ASSUME));");
                }
            }
            getConstructor().addKeywordWithNoParam(filenode.getKeywordsOfType(101));
            keywordnodeenumeration = recordnode.getKeywordsOfType(80);
            if(keywordnodeenumeration.hasMoreElements())
            {
                KeywordNode keywordnode1 = keywordnodeenumeration.nextKeyword();
                String s4 = keywordnode1.getIndicatorString();
                if(s4 == null)
                    s4 = "";
                addLineToConstructor("KeywordDefinition csrloc = new KeywordDefinition(KWD_CSRLOC, \"" + s4 + "\");");
                int k = 0;
                KeywordParm keywordparm1 = keywordnode1.getParm(k);
                String s7 = keywordparm1.getVarString();
                try
                {
                    s7 = WebfacingConstants.replaceSpecialCharacters(s7);
                }
                catch(Throwable throwable3) { }
                addLineToConstructor("csrloc.addParameter(\"" + s7 + "\");");
                k++;
                keywordparm1 = keywordnode1.getParm(k);
                s7 = keywordparm1.getVarString();
                try
                {
                    s7 = WebfacingConstants.replaceSpecialCharacters(s7);
                }
                catch(Throwable throwable4) { }
                addLineToConstructor("csrloc.addParameter(\"" + s7 + "\");");
                addLineToConstructor("add(csrloc);");
            }
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in ViewDefinitionGenerator.generateViewKeywords() while generating " + getBeanName() + " = " + throwable);
        }
    }

    private void generateWDWTITLEKeyword(RecordNode recordnode)
    {
        KeywordNodeEnumeration keywordnodeenumeration = recordnode.getKeywordsOfType(225);
        if(keywordnodeenumeration.hasMoreElements())
        {
            String s = "wdwTitleKwd";
            int i = 1;
            for(; keywordnodeenumeration.hasMoreElements(); addLineToConstructor("add(" + s + i++ + ");"))
            {
                KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
                int j = 0;
                KeywordParm keywordparm = keywordnode.getParm(j);
                String s1 = keywordnode.getIndicatorString();
                if(s1 == null)
                    s1 = "";
                String s2 = "true";
                String s3 = "";
                String s4 = "";
                String s5 = "";
                String s6 = "";
                String s7 = "";
                for(; keywordparm != null; keywordparm = keywordnode.getParm(++j))
                    switch(keywordparm.getVarKwdToken())
                    {
                    case 365: 
                    case 366: 
                    case 367: 
                    case 368: 
                    case 369: 
                    case 372: 
                    default:
                        break;

                    case 373: 
                        KeywordParm keywordparm1 = keywordparm.getFirstSubParm();
                        if(keywordparm1 == null)
                            break;
                        s3 = keywordparm1.getVarString();
                        if(keywordparm1.getParmType() == 68)
                        {
                            s2 = "false";
                            s3 = s3.substring(1);
                            s3 = WebfacingConstants.replaceSpecialCharacters(s3);
                        } else
                        {
                            s3 = keywordparm1.getJavaString();
                        }
                        break;

                    case 360: 
                        s4 = "*CENTER";
                        break;

                    case 361: 
                        s4 = "*LEFT";
                        break;

                    case 362: 
                        s4 = "*RIGHT";
                        break;

                    case 370: 
                        KeywordParm keywordparm2 = keywordparm.getFirstSubParm();
                        if(keywordparm2 != null)
                            s5 = keywordparm2.getVarKwdTokenAsString().substring(10);
                        break;

                    case 371: 
                        Vector vector = keywordparm.getSubParms();
                        int k = 0;
                        KeywordParm keywordparm3 = (KeywordParm)vector.get(k++);
                        if(keywordparm3 != null)
                            s6 = keywordparm3.getVarKwdTokenAsString().substring(11);
                        for(; k < vector.size(); k++)
                        {
                            KeywordParm keywordparm4 = (KeywordParm)vector.get(k++);
                            if(keywordparm4 != null)
                                s6 = s6.concat(" ").concat(keywordparm4.getVarKwdTokenAsString().substring(11));
                        }

                        break;

                    case 363: 
                        s7 = "*TOP";
                        break;

                    case 364: 
                        s7 = "*BOTTOM";
                        break;
                    }

                addLineToConstructor("WindowTitleDefinition " + s + i + " = new WindowTitleDefinition(\"" + s1 + "\", " + s2 + ", \"" + s3 + "\", \"" + s5 + "\", \"" + s6 + "\", \"" + s4 + "\", \"" + s7 + "\");");
            }

        }
    }

    protected String getBeanBaseClassSuffix()
    {
        return "RecordViewDefinition";
    }

    protected String getBeanName()
    {
        return super.getBeanName() + "View";
    }

    private CommandKeyLabelList getCommandKeyLabelListFromNode(AnyNodeWithKeywords anynodewithkeywords)
    {
        CommandKeyLabelList commandkeylabellist = new CommandKeyLabelList();
        try
        {
            Vector vector = anynodewithkeywords.getKeywordsVector();
            Vector vector1 = new Vector();
            if(vector != null)
            {
                for(int i = 0; i < vector.size();)
                {
                    KeywordNode keywordnode = (KeywordNode)vector.elementAt(i);
                    switch(keywordnode.getKeywordId())
                    {
                    case 14: // '\016'
                    case 15: // '\017'
                    case 16: // '\020'
                    case 17: // '\021'
                    case 18: // '\022'
                    case 19: // '\023'
                    case 20: // '\024'
                    case 21: // '\025'
                    case 22: // '\026'
                    case 23: // '\027'
                    case 24: // '\030'
                    case 25: // '\031'
                    case 26: // '\032'
                    case 27: // '\033'
                    case 28: // '\034'
                    case 29: // '\035'
                    case 30: // '\036'
                    case 31: // '\037'
                    case 32: // ' '
                    case 33: // '!'
                    case 34: // '"'
                    case 35: // '#'
                    case 36: // '$'
                    case 37: // '%'
                    case 38: // '&'
                    case 39: // '\''
                    case 40: // '('
                    case 41: // ')'
                    case 42: // '*'
                    case 43: // '+'
                    case 44: // ','
                    case 45: // '-'
                    case 46: // '.'
                    case 47: // '/'
                    case 48: // '0'
                    case 49: // '1'
                    case 50: // '2'
                    case 51: // '3'
                    case 52: // '4'
                    case 53: // '5'
                    case 54: // '6'
                    case 55: // '7'
                    case 56: // '8'
                    case 57: // '9'
                    case 58: // ':'
                    case 59: // ';'
                    case 60: // '<'
                    case 61: // '='
                        String s = keywordnode.getName();
                        String s2 = null;
                        int j = 0;
                        KeywordParm keywordparm = keywordnode.getParm(j);
                        if(keywordparm != null)
                            keywordparm = keywordnode.getParm(++j);
                        if(keywordparm != null && keywordparm.getVarString() != null)
                        {
                            s2 = keywordparm.getVarString();
                            if(s2 == null)
                                s2 = new String("");
                            else
                                s2 = keywordparm.getVarStringUnquoted();
                        }
                        if(s != null && s2 != null)
                        {
                            CommandKeyLabel commandkeylabel = new CommandKeyLabel(s, s2, 1);
                            if(commandkeylabel != null)
                                commandkeylabellist.add(commandkeylabel);
                        }
                        vector1.addElement(keywordnode);
                        // fall through

                    default:
                        i++;
                        break;
                    }
                }

            }
            if(anynodewithkeywords.hasWebSettings())
            {
                WebSettingsNodeEnumeration websettingsnodeenumeration = anynodewithkeywords.getWebSettings();
                Object obj = null;
                while(websettingsnodeenumeration.hasMoreElements()) 
                {
                    WebSettingsNode websettingsnode = websettingsnodeenumeration.nextWebSettings();
                    int k = websettingsnode.getType();
                    String s1 = websettingsnode.getValue();
                    if(k == 9)
                    {
                        StringTokenizer stringtokenizer = new StringTokenizer(s1, "|");
                        if(stringtokenizer.countTokens() > 0)
                            while(stringtokenizer.hasMoreTokens()) 
                            {
                                String s3 = (String)stringtokenizer.nextElement();
                                int l = s3.indexOf("=");
                                String s4 = null;
                                String s5 = null;
                                if(l >= 0)
                                {
                                    s4 = s3.substring(0, l);
                                    if(Integer.parseInt(s4) < 10)
                                        s4 = "0" + s4;
                                    if(s3.length() > l + 1)
                                        s5 = s3.substring(l + 1);
                                    else
                                        s5 = new String("");
                                }
                                if(s4 != null && s5 != null)
                                {
                                    CommandKeyLabel commandkeylabel1 = new CommandKeyLabel(s4, s5, 3);
                                    if(commandkeylabel1 != null)
                                        commandkeylabellist.add(commandkeylabel1);
                                }
                            }
                    }
                }
            }
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in ViewBeanGenerator.getCommandKeyLabelListFromNode() while generating " + getBeanName() + " = " + throwable);
        }
        return commandkeylabellist;
    }

    protected String getInitRecordDefinitionMethodParams()
    {
        return "RecordDataBean recDataBean";
    }

    private CommandKeyLabelList getMergedCommandKeyLabelList(CommandKeyLabelList commandkeylabellist, CommandKeyLabelList commandkeylabellist1)
    {
        try
        {
            commandkeylabellist1.mergeList(commandkeylabellist);
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in ViewBeanGenerator.getMergedCommandKeyLabelList() while generating " + getBeanName() + " = " + throwable);
        }
        return commandkeylabellist1;
    }

    protected void setOutputOnly(boolean flag)
    {
        addLineToConstructor("setIsOutputOnly(" + flag + ");");
    }

    protected void specifyClassAttributes(JavaClassSourceCode javaclasssourcecode)
    {
        super.specifyClassAttributes(javaclasssourcecode);
        javaclasssourcecode.addImport("com.ibm.as400ad.webfacing.runtime.view.def.*");
        javaclasssourcecode.addImport("com.ibm.as400ad.webfacing.runtime.help.*");
        generateFieldVisDef(javaclasssourcecode);
    }

    protected void specifyNullConstructorBody()
    {
        generateDisplaySizeInfo();
        generateViewKeywords();
        generateFieldViewDefinitions();
        generateAIDKeyDefinitions();
        generateSetFirstFieldLine();
        generateSetLastFieldLine();
        generateSetWdwStart();
        generateDspatrPCInfo();
        generateOnlineHelpInfo();
    }

    private void updateRecordStringMatchedKeyLabelList(ConstantFieldOutput constantfieldoutput)
    {
        try
        {
            _visibleCommandKeyLabelList.mergeList(constantfieldoutput.getCommandKeyLabels());
            _visibilityCondtionedKeyLabelList.mergeConditionedLabelList(constantfieldoutput.getVisibilityConditonedCommandKeyLabels());
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in ViewBeanGenerator.updateStringMatchedKeyLabelList() while generating " + getBeanName() + " = " + throwable);
        }
    }

    private static final String _beanBaseClassSuffix = "RecordViewDefinition";
    private ExportSettings _exportSettings;
    private CommandKeyLabelList _defaultCommandKeyLabelList;
    private CommandKeyLabelList _definedKeyLabelList;
    private CommandKeyLabelList _visibilityCondtionedKeyLabelList;
    private CommandKeyLabelList _visibleCommandKeyLabelList;
    private CommandKeyLabelList _indicatorConditionedKeyLabelList;
    private KeyLabelsInPropertyFile _keyLabelsInPropertyFile;
    private static String FIELD_VIS_DEF_NAME = "FIELD_VIS_DEF";

}
