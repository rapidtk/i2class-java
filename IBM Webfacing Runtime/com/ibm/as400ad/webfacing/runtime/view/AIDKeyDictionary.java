// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import java.util.Hashtable;

public class AIDKeyDictionary
{

    private AIDKeyDictionary()
    {
        initializeKeyLookupTable();
    }

    public static byte getKeyCode(String s)
    {
        Byte byte1 = (Byte)_keyToCodeTable.get(new String(s));
        if(byte1 == null)
            throw new RuntimeException("Invalid key string encountered - key = \"" + s + "\"");
        else
            return byte1.byteValue();
    }

    private static synchronized void initializeKeyLookupTable()
    {
        if(_keyToCodeTable != null)
        {
            return;
        } else
        {
            _keyToCodeTable = new Hashtable();
            _functionKeys = new Hashtable();
            _functionKeys.put("CF01", new Byte(ADBD_AIDKEY_CMD01));
            _functionKeys.put("CF02", new Byte(ADBD_AIDKEY_CMD02));
            _functionKeys.put("CF03", new Byte(ADBD_AIDKEY_CMD03));
            _functionKeys.put("CF04", new Byte(ADBD_AIDKEY_CMD04));
            _functionKeys.put("CF05", new Byte(ADBD_AIDKEY_CMD05));
            _functionKeys.put("CF06", new Byte(ADBD_AIDKEY_CMD06));
            _functionKeys.put("CF07", new Byte(ADBD_AIDKEY_CMD07));
            _functionKeys.put("CF08", new Byte(ADBD_AIDKEY_CMD08));
            _functionKeys.put("CF09", new Byte(ADBD_AIDKEY_CMD09));
            _functionKeys.put("CF10", new Byte(ADBD_AIDKEY_CMD10));
            _functionKeys.put("CF11", new Byte(ADBD_AIDKEY_CMD11));
            _functionKeys.put("CF12", new Byte(ADBD_AIDKEY_CMD12));
            _functionKeys.put("CF13", new Byte(ADBD_AIDKEY_CMD13));
            _functionKeys.put("CF14", new Byte(ADBD_AIDKEY_CMD14));
            _functionKeys.put("CF15", new Byte(ADBD_AIDKEY_CMD15));
            _functionKeys.put("CF16", new Byte(ADBD_AIDKEY_CMD16));
            _functionKeys.put("CF17", new Byte(ADBD_AIDKEY_CMD17));
            _functionKeys.put("CF18", new Byte(ADBD_AIDKEY_CMD18));
            _functionKeys.put("CF19", new Byte(ADBD_AIDKEY_CMD19));
            _functionKeys.put("CF20", new Byte(ADBD_AIDKEY_CMD20));
            _functionKeys.put("CF21", new Byte(ADBD_AIDKEY_CMD21));
            _functionKeys.put("CF22", new Byte(ADBD_AIDKEY_CMD22));
            _functionKeys.put("CF23", new Byte(ADBD_AIDKEY_CMD23));
            _functionKeys.put("CF24", new Byte(ADBD_AIDKEY_CMD24));
            _functionKeys.put("ENTER", new Byte(ADBD_AIDKEY_ENTER));
            _functionKeys.put("SLPAUTOENTER", new Byte(ADBD_AIDKEY_SLPAUTOENTER));
            _functionKeys.put("ROLLUP", new Byte(ADBD_AIDKEY_ROLLUP));
            _functionKeys.put("ROLLDOWN", new Byte(ADBD_AIDKEY_ROLLDOWN));
            _functionKeys.put("PAGEUP", new Byte(ADBD_AIDKEY_PAGEUP));
            _functionKeys.put("PAGEDOWN", new Byte(ADBD_AIDKEY_PAGEDOWN));
            _keyToCodeTable = (Hashtable)_functionKeys.clone();
            _keyToCodeTable.put("HOME", new Byte(ADBD_AIDKEY_HOME));
            _keyToCodeTable.put("CLEAR", new Byte(ADBD_AIDKEY_CLEAR));
            _keyToCodeTable.put("HELP", new Byte(ADBD_AIDKEY_HELP));
            _keyToCodeTable.put("RCDBACK", new Byte(ADBD_AIDKEY_RCDBACK));
            _keyToCodeTable.put("PRINT", new Byte(ADBD_AIDKEY_PRINT));
            _keyToCodeTable.put("CA01", new Byte(ADBD_AIDKEY_CMD01));
            _keyToCodeTable.put("CA02", new Byte(ADBD_AIDKEY_CMD02));
            _keyToCodeTable.put("CA03", new Byte(ADBD_AIDKEY_CMD03));
            _keyToCodeTable.put("CA04", new Byte(ADBD_AIDKEY_CMD04));
            _keyToCodeTable.put("CA05", new Byte(ADBD_AIDKEY_CMD05));
            _keyToCodeTable.put("CA06", new Byte(ADBD_AIDKEY_CMD06));
            _keyToCodeTable.put("CA07", new Byte(ADBD_AIDKEY_CMD07));
            _keyToCodeTable.put("CA08", new Byte(ADBD_AIDKEY_CMD08));
            _keyToCodeTable.put("CA09", new Byte(ADBD_AIDKEY_CMD09));
            _keyToCodeTable.put("CA10", new Byte(ADBD_AIDKEY_CMD10));
            _keyToCodeTable.put("CA11", new Byte(ADBD_AIDKEY_CMD11));
            _keyToCodeTable.put("CA12", new Byte(ADBD_AIDKEY_CMD12));
            _keyToCodeTable.put("CA13", new Byte(ADBD_AIDKEY_CMD13));
            _keyToCodeTable.put("CA14", new Byte(ADBD_AIDKEY_CMD14));
            _keyToCodeTable.put("CA15", new Byte(ADBD_AIDKEY_CMD15));
            _keyToCodeTable.put("CA16", new Byte(ADBD_AIDKEY_CMD16));
            _keyToCodeTable.put("CA17", new Byte(ADBD_AIDKEY_CMD17));
            _keyToCodeTable.put("CA18", new Byte(ADBD_AIDKEY_CMD18));
            _keyToCodeTable.put("CA19", new Byte(ADBD_AIDKEY_CMD19));
            _keyToCodeTable.put("CA20", new Byte(ADBD_AIDKEY_CMD20));
            _keyToCodeTable.put("CA21", new Byte(ADBD_AIDKEY_CMD21));
            _keyToCodeTable.put("CA22", new Byte(ADBD_AIDKEY_CMD22));
            _keyToCodeTable.put("CA23", new Byte(ADBD_AIDKEY_CMD23));
            _keyToCodeTable.put("CA24", new Byte(ADBD_AIDKEY_CMD24));
            _keyToCodeTable.put("01", new Byte(ADBD_AIDKEY_CMD01));
            _keyToCodeTable.put("02", new Byte(ADBD_AIDKEY_CMD02));
            _keyToCodeTable.put("03", new Byte(ADBD_AIDKEY_CMD03));
            _keyToCodeTable.put("04", new Byte(ADBD_AIDKEY_CMD04));
            _keyToCodeTable.put("05", new Byte(ADBD_AIDKEY_CMD05));
            _keyToCodeTable.put("06", new Byte(ADBD_AIDKEY_CMD06));
            _keyToCodeTable.put("07", new Byte(ADBD_AIDKEY_CMD07));
            _keyToCodeTable.put("08", new Byte(ADBD_AIDKEY_CMD08));
            _keyToCodeTable.put("09", new Byte(ADBD_AIDKEY_CMD09));
            _keyToCodeTable.put("10", new Byte(ADBD_AIDKEY_CMD10));
            _keyToCodeTable.put("11", new Byte(ADBD_AIDKEY_CMD11));
            _keyToCodeTable.put("12", new Byte(ADBD_AIDKEY_CMD12));
            _keyToCodeTable.put("13", new Byte(ADBD_AIDKEY_CMD13));
            _keyToCodeTable.put("14", new Byte(ADBD_AIDKEY_CMD14));
            _keyToCodeTable.put("15", new Byte(ADBD_AIDKEY_CMD15));
            _keyToCodeTable.put("16", new Byte(ADBD_AIDKEY_CMD16));
            _keyToCodeTable.put("17", new Byte(ADBD_AIDKEY_CMD17));
            _keyToCodeTable.put("18", new Byte(ADBD_AIDKEY_CMD18));
            _keyToCodeTable.put("19", new Byte(ADBD_AIDKEY_CMD19));
            _keyToCodeTable.put("20", new Byte(ADBD_AIDKEY_CMD20));
            _keyToCodeTable.put("21", new Byte(ADBD_AIDKEY_CMD21));
            _keyToCodeTable.put("22", new Byte(ADBD_AIDKEY_CMD22));
            _keyToCodeTable.put("23", new Byte(ADBD_AIDKEY_CMD23));
            _keyToCodeTable.put("24", new Byte(ADBD_AIDKEY_CMD24));
            return;
        }
    }

    public static boolean isCommandKey(String s)
    {
        return s.charAt(0) == 'C' && (s.charAt(1) == 'A' || s.charAt(1) == 'F');
    }

    public static boolean isFunctionKey(String s)
    {
        return _functionKeys.containsKey(s);
    }

    public static boolean isNonCommandAID(String s)
    {
        return !isCommandKey(s) && s.charAt(0) != 'E' && s.charAt(0) != 'S';
    }

    public static boolean isKeyInDictionary(String s)
    {
        return _keyToCodeTable.get(new String(s)) != null;
    }

    static Hashtable _keyToCodeTable;
    static Hashtable _functionKeys;
    static byte ADBD_AIDKEY_CLEAR = -67;
    public static byte ADBD_AIDKEY_ENTER = -15;
    public static byte ADBD_AIDKEY_HELP = -13;
    static byte ADBD_AIDKEY_HOME = -8;
    static byte ADBD_AIDKEY_RCDBACK = -8;
    static byte ADBD_AIDKEY_PRINT = -10;
    static byte ADBD_AIDKEY_SLPAUTOENTER = 63;
    static byte ADBD_AIDKEY_PAGEDOWN = -11;
    static byte ADBD_AIDKEY_ROLLDOWN = -12;
    static byte ADBD_AIDKEY_PAGEUP = -12;
    static byte ADBD_AIDKEY_ROLLUP = -11;
    static byte ADBD_AIDKEY_CMD01 = 49;
    static byte ADBD_AIDKEY_CMD02 = 50;
    static byte ADBD_AIDKEY_CMD03 = 51;
    static byte ADBD_AIDKEY_CMD04 = 52;
    static byte ADBD_AIDKEY_CMD05 = 53;
    static byte ADBD_AIDKEY_CMD06 = 54;
    static byte ADBD_AIDKEY_CMD07 = 55;
    static byte ADBD_AIDKEY_CMD08 = 56;
    static byte ADBD_AIDKEY_CMD09 = 57;
    static byte ADBD_AIDKEY_CMD10 = 58;
    static byte ADBD_AIDKEY_CMD11 = 59;
    static byte ADBD_AIDKEY_CMD12 = 60;
    static byte ADBD_AIDKEY_CMD13 = -79;
    static byte ADBD_AIDKEY_CMD14 = -78;
    static byte ADBD_AIDKEY_CMD15 = -77;
    static byte ADBD_AIDKEY_CMD16 = -76;
    static byte ADBD_AIDKEY_CMD17 = -75;
    static byte ADBD_AIDKEY_CMD18 = -74;
    static byte ADBD_AIDKEY_CMD19 = -73;
    static byte ADBD_AIDKEY_CMD20 = -72;
    static byte ADBD_AIDKEY_CMD21 = -71;
    static byte ADBD_AIDKEY_CMD22 = -70;
    static byte ADBD_AIDKEY_CMD23 = -69;
    static byte ADBD_AIDKEY_CMD24 = -68;

    static 
    {
        initializeKeyLookupTable();
    }
}
