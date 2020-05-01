// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.dds.dom;


public interface IDdsLine
{

    public abstract boolean isComment();

    public abstract boolean isEndOfFileComment();

    public abstract boolean isEndOfFile();

    public abstract boolean isNamedField();

    public abstract boolean isConstantField();

    public abstract boolean isRecord();

    public abstract boolean isKeyword();

    public abstract boolean isHelp();

    public abstract boolean isFieldLocationLine();

    public abstract boolean isConditioningLine();

    public abstract boolean isBlank();

    public abstract boolean isContinued();

    public abstract boolean isContinuedRespectingFollowingSpaces();

    public abstract boolean isContinuedWithNextNonBlank();

    public abstract String getSourceLine();

    public abstract String getPrefixArea();

    public abstract String getCommentString();

    public abstract String getPositionalArea();

    public abstract boolean isConditioned();

    public abstract String getConditionArea();

    public abstract char getReservedChar();

    public abstract String getNameArea();

    public abstract boolean isReferencedField();

    public abstract char getReferenceChar();

    public abstract boolean isFieldLengthBlank();

    public abstract String getFieldLengthAsString();

    public abstract int getFieldLengthAsInteger()
        throws NumberFormatException;

    public abstract char getDataTypeChar();

    public abstract char getUsageChar();

    public abstract boolean isDecimalPositionsBlank();

    public abstract String getDecimalPositionsAsString();

    public abstract int getDecimalPositionsAsInteger()
        throws NumberFormatException;

    public abstract boolean isFieldRowBlank();

    public abstract String getFieldRowAsString();

    public abstract int getFieldRowAsInteger()
        throws NumberFormatException;

    public abstract boolean isFieldColBlank();

    public abstract String getFieldColAsString();

    public abstract int getFieldColAsInteger()
        throws NumberFormatException;

    public abstract String getFunctionArea();

    public static final int OFFSET_POSITIONAL_AREA_BEGIN = 6;
    public static final int OFFSET_CONDITION_AREA_BEGIN = 6;
    public static final int OFFSET_CONDITION_AREA_END = 15;
    public static final int OFFSET_SPEC_TYPE_POS = 16;
    public static final int OFFSET_RESERVED_CHAR_POS = 17;
    public static final int OFFSET_NAME_AREA_BEGIN = 18;
    public static final int OFFSET_NAME_AREA_END = 27;
    public static final int OFFSET_REFERENCE_POS = 28;
    public static final int OFFSET_LENGTH_BEGIN = 29;
    public static final int OFFSET_LENGTH_END = 33;
    public static final int OFFSET_DATA_TYPE_SHIFT_POS = 34;
    public static final int OFFSET_DECIMAL_POS_BEGIN = 35;
    public static final int OFFSET_DECIMAL_POS_END = 36;
    public static final int OFFSET_USAGE_POS = 37;
    public static final int OFFSET_ROW_BEGIN = 38;
    public static final int OFFSET_ROW_END = 40;
    public static final int OFFSET_COLUMN_BEGIN = 41;
    public static final int OFFSET_COLUMN_END = 43;
    public static final int OFFSET_FUNCTION_AREA_BEGIN = 44;
    public static final int OFFSET_FUNCTION_AREA_END = 79;
}
