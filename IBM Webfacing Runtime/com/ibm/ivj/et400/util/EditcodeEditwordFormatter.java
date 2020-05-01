// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EditcodeEditwordFormatter.java

package com.ibm.ivj.et400.util;

import java.beans.*;
import java.io.Serializable;
import java.text.*;
import java.util.Locale;

// Referenced classes of package com.ibm.ivj.et400.util:
//            AbstractFormatter, DataAttributes, Attributes

public class EditcodeEditwordFormatter extends AbstractFormatter
    implements PropertyChangeListener, VetoableChangeListener, Serializable
{

    public EditcodeEditwordFormatter()
    {
        editCode = '0';
        editWord = "";
        editCodeParmType = 0;
        currencySymbol = '$';
        thousandSeparator = (new DecimalFormatSymbols()).getGroupingSeparator();
        dateSeparator = '/';
        dataAttributes = null;
        bBlankIfZero = false;
        bFloatingMinus = false;
        bAsteriskProtected = false;
        bDateSeparator = false;
        bShowNegative = true;
        iEndZeroSuppressionIndex = -1;
        iExpansionIndex = -1;
        iFloatingCurrency = -1;
        iFormattedLength = -1;
        iStatusIndex = -1;
        i_FieldLength = 0;
        i_FieldPrecision = 0;
        strEditWord = "";
        collator = Collator.getInstance(Locale.getDefault());
        propertyChange = new PropertyChangeSupport(this);
        vetoPropertyChange = new VetoableChangeSupport(this);
        qdecfmtJValue = false;
        initialize();
    }

    public EditcodeEditwordFormatter(Attributes dataAttributes)
    {
        editCode = '0';
        editWord = "";
        editCodeParmType = 0;
        currencySymbol = '$';
        thousandSeparator = (new DecimalFormatSymbols()).getGroupingSeparator();
        dateSeparator = '/';
        this.dataAttributes = null;
        bBlankIfZero = false;
        bFloatingMinus = false;
        bAsteriskProtected = false;
        bDateSeparator = false;
        bShowNegative = true;
        iEndZeroSuppressionIndex = -1;
        iExpansionIndex = -1;
        iFloatingCurrency = -1;
        iFormattedLength = -1;
        iStatusIndex = -1;
        i_FieldLength = 0;
        i_FieldPrecision = 0;
        strEditWord = "";
        collator = Collator.getInstance(Locale.getDefault());
        propertyChange = new PropertyChangeSupport(this);
        vetoPropertyChange = new VetoableChangeSupport(this);
        qdecfmtJValue = false;
        initialize();
        try
        {
            setDataAttributes(dataAttributes);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public EditcodeEditwordFormatter(Attributes dataAttributes, char editCode, int editCodeParmType, String editWord)
    {
        this.editCode = '0';
        this.editWord = "";
        this.editCodeParmType = 0;
        currencySymbol = '$';
        thousandSeparator = (new DecimalFormatSymbols()).getGroupingSeparator();
        dateSeparator = '/';
        this.dataAttributes = null;
        bBlankIfZero = false;
        bFloatingMinus = false;
        bAsteriskProtected = false;
        bDateSeparator = false;
        bShowNegative = true;
        iEndZeroSuppressionIndex = -1;
        iExpansionIndex = -1;
        iFloatingCurrency = -1;
        iFormattedLength = -1;
        iStatusIndex = -1;
        i_FieldLength = 0;
        i_FieldPrecision = 0;
        strEditWord = "";
        collator = Collator.getInstance(Locale.getDefault());
        propertyChange = new PropertyChangeSupport(this);
        vetoPropertyChange = new VetoableChangeSupport(this);
        qdecfmtJValue = false;
        initialize();
        try
        {
            setDataAttributes(dataAttributes);
            setEditCodeParmType(editCodeParmType);
            setEditWord(editWord);
            setEditCode(editCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public EditcodeEditwordFormatter(Attributes dataAttributes, char editCode, int editCodeParmType, String editWord, char currencySymbol, char thousandSeparator, char dateSeparator)
    {
        this.editCode = '0';
        this.editWord = "";
        this.editCodeParmType = 0;
        this.currencySymbol = '$';
        this.thousandSeparator = (new DecimalFormatSymbols()).getGroupingSeparator();
        this.dateSeparator = '/';
        this.dataAttributes = null;
        bBlankIfZero = false;
        bFloatingMinus = false;
        bAsteriskProtected = false;
        bDateSeparator = false;
        bShowNegative = true;
        iEndZeroSuppressionIndex = -1;
        iExpansionIndex = -1;
        iFloatingCurrency = -1;
        iFormattedLength = -1;
        iStatusIndex = -1;
        i_FieldLength = 0;
        i_FieldPrecision = 0;
        strEditWord = "";
        collator = Collator.getInstance(Locale.getDefault());
        propertyChange = new PropertyChangeSupport(this);
        vetoPropertyChange = new VetoableChangeSupport(this);
        qdecfmtJValue = false;
        initialize();
        try
        {
            setDataAttributes(dataAttributes);
            setEditCodeParmType(editCodeParmType);
            setEditWord(editWord);
            setEditCode(editCode);
            setCurrencySymbol(currencySymbol);
            setThousandSeparator(thousandSeparator);
            setDateSeparator(dateSeparator);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener l)
    {
        propertyChange.addPropertyChangeListener(l);
    }

    public void addVetoableChangeListener(VetoableChangeListener l)
    {
        vetoPropertyChange.addVetoableChangeListener(l);
    }

    private void convertEditCodeToEditWord(int iFieldLength, int iFieldPrecision, char cEditCode, int iEditCodeParameterType)
    {
        StringBuffer strbufEditWord = new StringBuffer(iFieldLength);
        bAsteriskProtected = false;
        if(cEditCode == 'Y')
        {
            bDateSeparator = true;
            int iIndex = iFieldLength <= 4 ? iFieldLength + 1 : iFieldLength + 2;
            for(int i = 1; i <= iIndex; i++)
                strbufEditWord.insert(0, ' ');

            if(iFieldLength >= 8)
            {
                iIndex = strbufEditWord.length() - 1;
                strbufEditWord.setCharAt(iIndex - 4, '/');
                strbufEditWord.setCharAt(iIndex - 7, '/');
                strbufEditWord.setCharAt(iIndex - 9, '0');
            } else
            if(iFieldLength >= 7)
            {
                iIndex = strbufEditWord.length() - 1;
                strbufEditWord.setCharAt(iIndex - 2, '/');
                strbufEditWord.setCharAt(iIndex - 5, '/');
                strbufEditWord.setCharAt(iIndex - 7, '0');
            } else
            if(iFieldLength >= 5)
            {
                strbufEditWord.setCharAt(0, '0');
                strbufEditWord.setCharAt(2, '/');
                strbufEditWord.setCharAt(5, '/');
            } else
            if(iFieldLength >= 3)
            {
                strbufEditWord.setCharAt(0, '0');
                strbufEditWord.setCharAt(2, '/');
            }
            bBlankIfZero = false;
            bFloatingMinus = false;
            iStatusIndex = -1;
        } else
        if(cEditCode == 'W')
        {
            bDateSeparator = true;
            int iIndex = iFieldLength <= 7 ? iFieldLength + 1 : iFieldLength + 2;
            for(int i = 1; i <= iIndex; i++)
                strbufEditWord.insert(0, ' ');

            if(iFieldLength >= 8)
            {
                iIndex = strbufEditWord.length() - 1;
                strbufEditWord.setCharAt(iIndex - 2, '/');
                strbufEditWord.setCharAt(iIndex - 5, '/');
                strbufEditWord.setCharAt(iIndex - 7, '0');
            } else
            if(iFieldLength >= 7)
            {
                iIndex = strbufEditWord.length() - 1;
                strbufEditWord.setCharAt(iIndex - 3, '/');
                strbufEditWord.setCharAt(iIndex - 5, '0');
            } else
            if(iFieldLength >= 6)
            {
                iIndex = strbufEditWord.length() - 1;
                strbufEditWord.setCharAt(iIndex - 2, '/');
                strbufEditWord.setCharAt(iIndex - 4, '0');
            } else
            if(iFieldLength >= 5)
            {
                strbufEditWord.setCharAt(0, '0');
                strbufEditWord.setCharAt(2, '/');
            } else
            if(iFieldLength >= 3)
                strbufEditWord.setCharAt(2, '/');
            bBlankIfZero = false;
            bFloatingMinus = false;
            iStatusIndex = -1;
        } else
        {
            int iIndex = iFieldLength - iFieldPrecision;
            for(int i = 1; i <= iIndex; i++)
            {
                strbufEditWord.insert(0, ' ');
                if(i % 3 == 0 && i != iIndex && (cEditCode == '1' || cEditCode == '2' || cEditCode == 'A' || cEditCode == 'B' || cEditCode == 'J' || cEditCode == 'K' || cEditCode == 'N' || cEditCode == 'O'))
                    strbufEditWord.insert(0, ',');
            }

            int iEndZeroSuppressionIndex = strbufEditWord.length() - 1;
            if(iFieldPrecision == 0)
                iEndZeroSuppressionIndex--;
            if(qdecfmtJValue)
                iEndZeroSuppressionIndex--;
            if(iEndZeroSuppressionIndex >= 0)
            {
                if(iEditCodeParameterType == 1)
                    strbufEditWord.setCharAt(iEndZeroSuppressionIndex, '*');
                else
                    strbufEditWord.setCharAt(iEndZeroSuppressionIndex, '0');
            } else
            {
                iEndZeroSuppressionIndex = 0;
            }
            if(iEditCodeParameterType == 2)
                strbufEditWord.insert(iEndZeroSuppressionIndex, '$');
            else
            if(iEditCodeParameterType == 1)
                bAsteriskProtected = true;
            if(iFieldPrecision > 0)
            {
                strbufEditWord.append('.');
                for(int i = 0; i < iFieldPrecision; i++)
                    strbufEditWord.append(' ');

            }
        }
        switch(cEditCode)
        {
        case 50: // '2'
        case 52: // '4'
        case 66: // 'B'
        case 68: // 'D'
        case 75: // 'K'
        case 77: // 'M'
        case 79: // 'O'
        case 81: // 'Q'
        case 90: // 'Z'
            bBlankIfZero = true;
            break;

        default:
            bBlankIfZero = false;
            break;
        }
        bFloatingMinus = false;
        switch(cEditCode)
        {
        case 49: // '1'
        case 50: // '2'
        case 51: // '3'
        case 52: // '4'
        case 87: // 'W'
        case 89: // 'Y'
            iStatusIndex = -1;
            break;

        case 65: // 'A'
        case 66: // 'B'
        case 67: // 'C'
        case 68: // 'D'
            iStatusIndex = strbufEditWord.length();
            strbufEditWord.append("CR");
            break;

        case 74: // 'J'
        case 75: // 'K'
        case 76: // 'L'
        case 77: // 'M'
            iStatusIndex = strbufEditWord.length();
            strbufEditWord.append('-');
            break;

        case 78: // 'N'
        case 79: // 'O'
        case 80: // 'P'
        case 81: // 'Q'
            bFloatingMinus = true;
            iStatusIndex = -1;
            break;

        case 90: // 'Z'
            strbufEditWord = new StringBuffer(0);
            bShowNegative = false;
            break;

        case 53: // '5'
        case 54: // '6'
        case 55: // '7'
        case 56: // '8'
        case 57: // '9'
        case 58: // ':'
        case 59: // ';'
        case 60: // '<'
        case 61: // '='
        case 62: // '>'
        case 63: // '?'
        case 64: // '@'
        case 69: // 'E'
        case 70: // 'F'
        case 71: // 'G'
        case 72: // 'H'
        case 73: // 'I'
        case 82: // 'R'
        case 83: // 'S'
        case 84: // 'T'
        case 85: // 'U'
        case 86: // 'V'
        case 88: // 'X'
        default:
            strbufEditWord = new StringBuffer(0);
            break;
        }
        strEditWord = strbufEditWord.toString();
        i_FieldLength = iFieldLength;
        i_FieldPrecision = iFieldPrecision;
        iFloatingCurrency = strEditWord.indexOf('$');
        int iAsterisk = strEditWord.indexOf('*');
        int iZero = strEditWord.indexOf('0');
        if(iAsterisk != -1 && iZero != -1)
            this.iEndZeroSuppressionIndex = iZero >= iAsterisk ? iAsterisk : iZero;
        else
        if(iAsterisk == -1 && iZero != -1)
            this.iEndZeroSuppressionIndex = iZero;
        else
        if(iAsterisk != -1 && iZero == -1)
            this.iEndZeroSuppressionIndex = iAsterisk;
    }

    public static String Copyright()
    {
        return "(C) Copyright IBM Corporation 1997, 2002. All Rights Reserved.";
    }

    private void editwordSetup(String eWord)
    {
        char cDecimalPoint = ((DataAttributes)getDataAttributes()).getDecimalSymbol();
        bBlankIfZero = false;
        bFloatingMinus = false;
        bAsteriskProtected = false;
        bDateSeparator = false;
        bShowNegative = true;
        iEndZeroSuppressionIndex = -1;
        iExpansionIndex = -1;
        iFloatingCurrency = -1;
        iFormattedLength = -1;
        iStatusIndex = -1;
        i_FieldLength = 0;
        i_FieldPrecision = 0;
        if(eWord == null)
            eWord = "";
        if(collator.compare(eWord, "") == 0)
        {
            strEditWord = "";
            return;
        }
        strEditWord = eWord;
        int iLength = eWord.length();
        if(eWord.charAt(0) == '\'' && eWord.charAt(iLength - 1) == '\'')
            strEditWord = eWord.substring(1, iLength - 1);
        else
            strEditWord = new String(eWord);
        strEditWord = strEditWord.replace(getThousandSeparator(), ',');
        int iAsterisk = strEditWord.indexOf('*');
        int iZero = strEditWord.indexOf('0');
        if(iAsterisk != -1 && iZero != -1)
            iEndZeroSuppressionIndex = iZero >= iAsterisk ? iAsterisk : iZero;
        else
        if(iAsterisk == -1 && iZero != -1)
            iEndZeroSuppressionIndex = iZero;
        else
        if(iAsterisk != -1 && iZero == -1)
            iEndZeroSuppressionIndex = iAsterisk;
        iFloatingCurrency = strEditWord.indexOf(getCurrencySymbol());
        if(iFloatingCurrency != -1)
            strEditWord = strEditWord.substring(0, iFloatingCurrency) + "$" + strEditWord.substring(iFloatingCurrency + 1);
        if(iFloatingCurrency > 0 && iFloatingCurrency != iEndZeroSuppressionIndex - 1)
            iFloatingCurrency = -1;
        int iIndex = strEditWord.lastIndexOf(' ');
        if(iIndex == iEndZeroSuppressionIndex - 1)
            iStatusIndex = iEndZeroSuppressionIndex + 1;
        else
        if(iFloatingCurrency > 0 && iIndex == iFloatingCurrency - 1)
            iStatusIndex = iEndZeroSuppressionIndex + 1;
        else
            iStatusIndex = iIndex + 1;
        if(iEndZeroSuppressionIndex >= iStatusIndex)
            iEndZeroSuppressionIndex = -1;
        if(iFloatingCurrency >= iStatusIndex)
            iFloatingCurrency = -1;
        if(iStatusIndex > strEditWord.length())
        {
            iStatusIndex = -1;
            iExpansionIndex = -1;
        } else
        {
            int iMinusIndex = strEditWord.indexOf('-', iStatusIndex);
            int iCRIndex = strEditWord.indexOf("CR", iStatusIndex);
            if(iMinusIndex == -1 && iCRIndex == -1)
            {
                iExpansionIndex = iStatusIndex;
                iStatusIndex = -1;
            } else
            if(iMinusIndex < iCRIndex)
                iExpansionIndex = iMinusIndex != -1 ? iMinusIndex + 1 : iCRIndex + 2;
            else
                iExpansionIndex = iCRIndex != -1 ? iCRIndex + 2 : iMinusIndex + 1;
            if(iExpansionIndex > strEditWord.length() - 1)
                iExpansionIndex = -1;
        }
        if(iEndZeroSuppressionIndex != -1 && strEditWord.charAt(iEndZeroSuppressionIndex) == '*')
            bAsteriskProtected = true;
        for(iIndex = 0; iIndex < strEditWord.length(); iIndex++)
            if(strEditWord.charAt(iIndex) == ' ')
                i_FieldLength++;

        if(iEndZeroSuppressionIndex > -1)
            i_FieldLength++;
        iIndex = strEditWord.lastIndexOf(cDecimalPoint);
        if(iIndex > -1)
        {
            if(getEditWord().equals(""))
                strEditWord = strEditWord.replace(cDecimalPoint, '.');
            if(iEndZeroSuppressionIndex > iIndex)
                i_FieldPrecision++;
            for(; iIndex < strEditWord.length(); iIndex++)
                if(strEditWord.charAt(iIndex) == ' ')
                    i_FieldPrecision++;

        }
    }

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue)
    {
        getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
    }

    private String formatBody(char cLeftFiller, boolean bIsNegative, char cDecimalPoint, String strInput)
    {
        DataAttributes fa = (DataAttributes)getDataAttributes();
        int iDataLength = fa.getDataLength();
        int iDecimalPlaces = fa.getDecimalPlaces();
        char cEditCode = getEditCode();
        FieldPosition fieldPosition = new FieldPosition(0);
        int iNumberOfLeadingZeros = 0;
        if(iDataLength != iDecimalPlaces || 'Y' == cEditCode)
        {
            for(iNumberOfLeadingZeros = 0; iNumberOfLeadingZeros < strInput.length() && strInput.charAt(iNumberOfLeadingZeros) == '0'; iNumberOfLeadingZeros++);
            if(iDataLength == iNumberOfLeadingZeros && iDecimalPlaces == 0)
                iNumberOfLeadingZeros--;
        }
        int iEnd = iStatusIndex == -1 ? iExpansionIndex : iStatusIndex;
        iEnd = iEnd == -1 ? strEditWord.length() : iEnd;
        iEnd--;
        int iPivot;
        if(iDataLength == iDecimalPlaces && 'Y' != cEditCode)
            iPivot = iEnd - iDecimalPlaces - 1;
        else
            iPivot = iEndZeroSuppressionIndex == -1 ? iEnd : iEndZeroSuppressionIndex;
        int iNumPtr = strInput.length() - 2;
        StringBuffer strbufBody = new StringBuffer();
        int iIndex = -1;
        int iEditWordIndex;
        for(iEditWordIndex = iEnd; iEditWordIndex >= 0 && iNumPtr >= iNumberOfLeadingZeros; iEditWordIndex--)
        {
            char cCharacter = strEditWord.charAt(iEditWordIndex);
            if(iEditWordIndex > iPivot)
            {
                if(cCharacter == ' ')
                    strbufBody.insert(0, strInput.charAt(iNumPtr--));
                else
                    insertCharacter(cCharacter, strbufBody);
            } else
            if(iEditWordIndex != iFloatingCurrency || iFloatingCurrency <= -1)
                if(cCharacter == '0')
                    strbufBody.insert(0, strInput.charAt(iNumPtr--));
                else
                if(cCharacter == ' ')
                {
                    if('0' == strInput.charAt(iNumPtr) && iDataLength == iNumberOfLeadingZeros + 1)
                    {
                        strbufBody.insert(0, ' ');
                        iNumPtr--;
                    } else
                    {
                        strbufBody.insert(0, strInput.charAt(iNumPtr--));
                    }
                } else
                if(cCharacter == '*')
                {
                    if('0' == strInput.charAt(iNumPtr) && iDataLength == iNumberOfLeadingZeros + 1)
                    {
                        strbufBody.insert(0, '*');
                        iNumPtr--;
                    } else
                    {
                        strbufBody.insert(0, strInput.charAt(iNumPtr--));
                    }
                } else
                {
                    insertCharacter(cCharacter, strbufBody);
                }
        }

        for(; iEditWordIndex > iPivot; iEditWordIndex--)
        {
            char cCharacter = strEditWord.charAt(iEditWordIndex);
            if(cCharacter == ' ')
                strbufBody.insert(0, '0');
            else
                insertCharacter(cCharacter, strbufBody);
        }

        if(strbufBody.length() > 0 && (iFloatingCurrency > 0 || iFloatingCurrency == 0 && iDataLength - iDecimalPlaces <= 1 || iFloatingCurrency == 0 && strEditWord.indexOf('0') == 1 && cEditCode != '0'))
            strbufBody.insert(0, getCurrencySymbol());
        if(bFloatingMinus)
            if(bIsNegative && strbufBody.length() > 0)
                strbufBody.insert(0, '-');
            else
                strbufBody.insert(0, cLeftFiller);
        if(iFloatingCurrency > -1 && iFloatingCurrency <= iEditWordIndex)
            iEditWordIndex--;
        else
        if(iFloatingCurrency == 0)
            iIndex = 0;
        for(; iEditWordIndex > iIndex; iEditWordIndex--)
            strbufBody.insert(0, cLeftFiller);

        if(iFloatingCurrency == 0 && iDataLength - iDecimalPlaces > 1 && (cEditCode == '0' || strEditWord.indexOf('0') != 1))
            strbufBody.insert(0, getCurrencySymbol());
        return strbufBody.toString();
    }

    private String formatExpansion()
    {
        StringBuffer strbufExpansion = new StringBuffer();
        if(iExpansionIndex != -1)
        {
            int iEnd = strEditWord.length() - 1;
            for(int i = iExpansionIndex; i <= iEnd; i++)
            {
                char cCharacter = strEditWord.charAt(i);
                strbufExpansion.append(cCharacter != '&' ? cCharacter : ' ');
            }

        }
        return strbufExpansion.toString();
    }

    private StringBuffer formatNumeric(String strInput, char cDecimalPoint)
    {
        DataAttributes fa = (DataAttributes)getDataAttributes();
        int iDecimalPlaces = 0;
        StringBuffer strbufOutput = null;
        int iSignPosition = strInput.indexOf('-');
        char cSign;
        if(iSignPosition != -1)
        {
            cSign = '-';
        } else
        {
            iSignPosition = strInput.indexOf('+');
            cSign = '+';
        }
        if(iSignPosition != -1)
        {
            strbufOutput = new StringBuffer(strInput.length());
            if(iSignPosition != 0)
                strbufOutput.append(strInput.substring(0, iSignPosition));
            else
                strbufOutput.append(strInput.substring(1, strInput.length()));
        } else
        {
            strbufOutput = new StringBuffer(strInput);
        }
        int iDecimalPosition = strbufOutput.toString().indexOf(cDecimalPoint);
        if(iDecimalPosition != -1)
        {
            iDecimalPlaces = strbufOutput.length() - iDecimalPosition - 1;
            String strOutput = strbufOutput.toString();
            strbufOutput.setLength(0);
            if(iDecimalPosition != 0)
                strbufOutput.append(strOutput.substring(0, iDecimalPosition));
            if(iDecimalPosition != strOutput.length() - 1)
                strbufOutput.append(strOutput.substring(iDecimalPosition + 1, strOutput.length()));
        }
        int iNumberOfZeros;
        for(iNumberOfZeros = fa.getDecimalPlaces() - iDecimalPlaces; iNumberOfZeros-- > 0;)
            strbufOutput.append('0');

        if(getEditCode() != '0')
            iNumberOfZeros = fa.getDataLength() - strbufOutput.length();
        else
            iNumberOfZeros = i_FieldLength - strbufOutput.length();
        while(iNumberOfZeros-- > 0) 
            strbufOutput.insert(0, '0');
        strbufOutput.append(cSign);
        return strbufOutput;
    }

    private String formatStatus(boolean bNegative)
    {
        StringBuffer strbufStatus = new StringBuffer();
        if(iStatusIndex != -1)
        {
            int iEnd = iExpansionIndex != -1 ? iExpansionIndex - 1 : strEditWord.length() - 1;
            if(bNegative)
            {
                for(int i = iStatusIndex; i <= iEnd; i++)
                {
                    char cCharacter = strEditWord.charAt(i);
                    strbufStatus.append(cCharacter != '&' ? cCharacter : ' ');
                }

            } else
            {
                for(int i = iStatusIndex; i <= iEnd; i++)
                    strbufStatus.append(' ');

            }
        }
        return strbufStatus.toString();
    }

    public String formatString(String strInput)
    {
        DataAttributes da = (DataAttributes)getDataAttributes();
        if(strInput == null || collator.compare(strInput, "") == 0 || getEditCode() == '0' && collator.compare(getEditWord(), "") == 0)
            return strInput;
        if(getEditCode() == '0')
        {
            editwordSetup(getEditWord());
            strEditWord = getEditWord();
        } else
        {
            convertEditCodeToEditWord(da.getDataLength(), da.getDecimalPlaces(), getEditCode(), getEditCodeParmType());
        }
        try
        {
            int iFieldPrecision = da.getDecimalPlaces();
            char cDecimalPoint = da.getDecimalSymbol();
            StringBuffer strbufValue = formatNumeric(strInput, cDecimalPoint);
            if(strEditWord.length() == 0)
            {
                if(iFieldPrecision > 0 && bShowNegative)
                    strbufValue.insert(strbufValue.length() - iFieldPrecision - 1, cDecimalPoint);
                if(strbufValue.charAt(strbufValue.length() - 1) == '+')
                    strbufValue.setLength(strbufValue.length() - 1);
                else
                if(!bShowNegative && strbufValue.charAt(strbufValue.length() - 1) == '-')
                    strbufValue.setLength(strbufValue.length() - 1);
                if(!bShowNegative)
                {
                    for(int i = 0; i < strbufValue.length(); i++)
                    {
                        if(strbufValue.charAt(i) != '0')
                            break;
                        strbufValue.setCharAt(i, ' ');
                    }

                }
                return strbufValue.toString();
            }
            boolean bIsNegative = strbufValue.charAt(strbufValue.length() - 1) == '-';
            char cLeftFiller = bAsteriskProtected ? '*' : ' ';
            String strBody = formatBody(cLeftFiller, bIsNegative, cDecimalPoint, strbufValue.toString());
            String strStatus = formatStatus(bIsNegative);
            String strExpansion = formatExpansion();
            StringBuffer strbufOutput = new StringBuffer(strBody + strStatus + strExpansion);
            if(bBlankIfZero)
            {
                int iIndex = -1;
                int iCount = -1;
                String strValue = strbufValue.toString();
                do
                {
                    iCount++;
                    iIndex = strValue.indexOf('0', iIndex + 1);
                } while(iIndex != -1);
                if(iCount == strbufValue.length() - 1)
                {
                    for(int i = 0; i < strBody.length(); i++)
                        strbufOutput.setCharAt(i, cLeftFiller);

                }
            }
            String strOut = strbufOutput.toString();
            strOut = strOut.replace('&', ' ');
            return strOut;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return strInput;
    }

    public char getCurrencySymbol()
    {
        return currencySymbol;
    }

    public Attributes getDataAttributes()
    {
        if(dataAttributes == null)
            dataAttributes = new DataAttributes();
        return dataAttributes;
    }

    public char getDateSeparator()
    {
        return dateSeparator;
    }

    public char getEditCode()
    {
        return editCode;
    }

    public int getEditCodeParmType()
    {
        return editCodeParmType;
    }

    public String getEditWord()
    {
        if(editWord == null)
            editWord = "";
        return editWord;
    }

    protected PropertyChangeSupport getPropertyChange()
    {
        if(propertyChange == null)
            propertyChange = new PropertyChangeSupport(this);
        return propertyChange;
    }

    public char getThousandSeparator()
    {
        return thousandSeparator;
    }

    private void initialize()
    {
        addPropertyChangeListener(this);
        addVetoableChangeListener(this);
        strEditWord = new String();
        collator.setStrength(3);
        collator.setDecomposition(2);
    }

    private void insertCharacter(char cCharacter, StringBuffer strbufBody)
    {
        char cInsert = cCharacter;
        if(getEditWord().equals(""))
            switch(cCharacter)
            {
            case 38: // '&'
                cInsert = ' ';
                break;

            case 36: // '$'
                cInsert = getCurrencySymbol();
                break;

            case 47: // '/'
                cInsert = getDateSeparator();
                break;

            case 46: // '.'
                cInsert = ((DataAttributes)getDataAttributes()).getDecimalSymbol();
                break;

            case 45: // '-'
                cInsert = '-';
                break;

            case 44: // ','
                cInsert = getThousandSeparator();
                break;

            case 37: // '%'
            case 39: // '\''
            case 40: // '('
            case 41: // ')'
            case 42: // '*'
            case 43: // '+'
            default:
                cInsert = cCharacter;
                break;
            }
        strbufBody.insert(0, cInsert);
    }

    public void propertyChange(PropertyChangeEvent propertychangeevent)
    {
    }

    public void removePropertyChangeListener(PropertyChangeListener l)
    {
        propertyChange.removePropertyChangeListener(l);
    }

    public void removeVetoableChangeListener(VetoableChangeListener l)
    {
        vetoPropertyChange.removeVetoableChangeListener(l);
    }

    public void setCurrencySymbol(char symbol)
    {
        char oldValue = currencySymbol;
        currencySymbol = symbol;
        propertyChange.firePropertyChange("currencySymbol", new Character(oldValue), new Character(symbol));
    }

    public void setDataAttributes(Attributes a)
        throws PropertyVetoException
    {
        Attributes oldValue = dataAttributes;
        vetoPropertyChange.fireVetoableChange("dataAttributes", oldValue, a);
        dataAttributes = a;
    }

    public void setDateSeparator(char dateSep)
    {
        char oldValue = dateSeparator;
        dateSeparator = dateSep;
        propertyChange.firePropertyChange("dateSeparator", new Character(oldValue), new Character(dateSeparator));
    }

    public void setEditCode(char code)
        throws PropertyVetoException
    {
        Character c = new Character(code);
        char oldValue = editCode;
        vetoPropertyChange.fireVetoableChange("editCode", new Character(oldValue), new Character(Character.toUpperCase(code)));
        editCode = Character.toUpperCase(code);
    }

    public void setEditCodeParmType(int editCodeParameterType)
        throws PropertyVetoException
    {
        int oldValue = editCodeParmType;
        vetoPropertyChange.fireVetoableChange("editCodeParmType", new Integer(oldValue), new Integer(editCodeParameterType));
        editCodeParmType = editCodeParameterType;
    }

    public void setEditWord(String eWord)
    {
        if(eWord == null)
            editWord = "";
        else
            editWord = eWord;
    }

    public void setThousandSeparator(char thouSeparator)
    {
        char oldValue = thousandSeparator;
        thousandSeparator = thouSeparator;
        propertyChange.firePropertyChange("thousandSeparator", new Character(oldValue), new Character(thouSeparator));
    }

    public void vetoableChange(PropertyChangeEvent e)
        throws PropertyVetoException
    {
        if(e.getPropertyName().equals("dataAttributes"))
        {
            if(!(e.getNewValue() instanceof DataAttributes))
                throw new PropertyVetoException("dataAttributes is not an instance of com.ibm.etools.iseries.ui.DataAttributes", e);
        } else
        if(e.getPropertyName().equals("editCodeParmType"))
        {
            int value = ((Integer)e.getNewValue()).intValue();
            if(value < 0 || value > 2)
                throw new PropertyVetoException("invalid editCodeParmType, 0 - None, 1 - Asterisk, 2 - Currency", e);
        } else
        if(e.getPropertyName().equals("editCode"))
        {
            char code[] = {
                '0', '1', '2', '3', '4', 'A', 'B', 'C', 'D', 'J', 
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'W', 'X', 'Y', 
                'Z'
            };
            char value = ((Character)e.getNewValue()).charValue();
            boolean find = false;
            for(int i = 0; i < code.length; i++)
            {
                if(value != code[i])
                    continue;
                find = true;
                break;
            }

            if(!find)
                throw new PropertyVetoException("invalid editCode", e);
        }
    }

    public EditcodeEditwordFormatter(Attributes dataAttributes, char editCode, int editCodeParmType, String editWord, char currencySymbol, char thousandSeparator, char dateSeparator, 
            boolean qdecfmtJValue)
    {
        this.editCode = '0';
        this.editWord = "";
        this.editCodeParmType = 0;
        this.currencySymbol = '$';
        this.thousandSeparator = (new DecimalFormatSymbols()).getGroupingSeparator();
        this.dateSeparator = '/';
        this.dataAttributes = null;
        bBlankIfZero = false;
        bFloatingMinus = false;
        bAsteriskProtected = false;
        bDateSeparator = false;
        bShowNegative = true;
        iEndZeroSuppressionIndex = -1;
        iExpansionIndex = -1;
        iFloatingCurrency = -1;
        iFormattedLength = -1;
        iStatusIndex = -1;
        i_FieldLength = 0;
        i_FieldPrecision = 0;
        strEditWord = "";
        collator = Collator.getInstance(Locale.getDefault());
        propertyChange = new PropertyChangeSupport(this);
        vetoPropertyChange = new VetoableChangeSupport(this);
        this.qdecfmtJValue = false;
        initialize();
        try
        {
            setDataAttributes(dataAttributes);
            setEditCodeParmType(editCodeParmType);
            setEditWord(editWord);
            setEditCode(editCode);
            setCurrencySymbol(currencySymbol);
            setThousandSeparator(thousandSeparator);
            setDateSeparator(dateSeparator);
            setQdecfmtJValue(qdecfmtJValue);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean getQdecfmtJValue()
    {
        return qdecfmtJValue;
    }

    public void setQdecfmtJValue(boolean value)
    {
        qdecfmtJValue = value;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2002  All Rights Reserved.";
    public static final int EDITCODEPARM_NONE = 0;
    public static final int EDITCODEPARM_ASTERISK = 1;
    public static final int EDITCODEPARM_CURRENCY = 2;
    private char editCode;
    private String editWord;
    private int editCodeParmType;
    private char currencySymbol;
    private char thousandSeparator;
    private char dateSeparator;
    private Attributes dataAttributes;
    private boolean bBlankIfZero;
    private boolean bFloatingMinus;
    private boolean bAsteriskProtected;
    private boolean bDateSeparator;
    private boolean bShowNegative;
    private int iEndZeroSuppressionIndex;
    private int iExpansionIndex;
    private int iFloatingCurrency;
    private int iFormattedLength;
    private int iStatusIndex;
    private int i_FieldLength;
    private int i_FieldPrecision;
    private String strEditWord;
    private Collator collator;
    protected transient PropertyChangeSupport propertyChange;
    protected transient VetoableChangeSupport vetoPropertyChange;
    private boolean qdecfmtJValue;
}
