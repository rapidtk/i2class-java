// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.common;

import java.io.PrintStream;
import java.util.Random;

public class Encoder
{

    private static void encode(byte abyte0[], byte abyte1[], byte abyte2[], byte abyte3[], int i)
    {
        for(int j = 0; j < i; j++)
        {
            abyte3[j] = (byte)(abyte2[j] + abyte1[j % 8]);
            abyte3[j] = (byte)(abyte3[j] ^ abyte0[j % 7]);
        }

    }

    private static void decode(byte abyte0[], byte abyte1[], byte abyte2[], byte abyte3[], int i)
    {
        for(int j = 0; j < i; j++)
        {
            abyte3[j] = (byte)(abyte2[j] ^ abyte0[j % 7]);
            abyte3[j] = (byte)(abyte3[j] - abyte1[j % 8]);
        }

    }

    public static String byteToHexString(byte byte0)
    {
        int i = byte0;
        if(i < 0)
            i = 256 + i;
        int j = i / 16;
        int k = i % 16;
        return hexDigits[j] + hexDigits[k];
    }

    public static String byteArrayToHexString(byte abyte0[])
    {
        String s = "";
        for(int i = 0; i < abyte0.length; i++)
            s = s + byteToHexString(abyte0[i]);

        return s;
    }

    public static byte[] hexStringToByteArray(String s)
    {
        byte abyte0[] = new byte[s.length()];
        int i = 0;
        int j = 0;
        for(; i < s.length(); i += 2)
            try
            {
                abyte0[j++] = (byte)Integer.parseInt(s.substring(i, i + 2), 16);
            }
            catch(NumberFormatException numberformatexception) { }

        return abyte0;
    }

    public static String encodePassword(String s)
    {
        Random random = new Random();
        byte abyte0[] = new byte[4];
        byte abyte1[] = new byte[4];
        for(int i = 0; i < 4; i++)
        {
            abyte0[i] = (byte)random.nextInt(255);
            abyte1[i] = (byte)random.nextInt(255);
        }

        byte abyte2[] = new byte[8];
        byte abyte3[] = new byte[8];
        System.arraycopy(abyte0, 0, abyte2, 0, 4);
        System.arraycopy(abyte1, 0, abyte2, 4, 4);
        System.arraycopy(abyte1, 0, abyte3, 0, 4);
        System.arraycopy(abyte0, 0, abyte3, 4, 4);
        byte abyte4[] = new byte[MAX_PASSWORD_LEN + 1];
        String s1 = s + "\n";
        int j = s1.length();
        System.arraycopy(s1.getBytes(), 0, abyte4, 0, j);
        for(int k = j; k < MAX_PASSWORD_LEN + 1; k++)
            abyte4[k] = (byte)random.nextInt(255);

        encode(abyte2, abyte3, abyte4, abyte4, MAX_PASSWORD_LEN + 1);
        byte abyte5[] = new byte[ENCODED_BUFFER_LEN];
        System.arraycopy(abyte0, 0, abyte5, 0, 4);
        System.arraycopy(abyte1, 0, abyte5, 4, 4);
        System.arraycopy(abyte4, 0, abyte5, 8, MAX_PASSWORD_LEN + 1);
        encode(FIXED_MASK, FIXED_ADDER, abyte5, abyte5, ENCODED_BUFFER_LEN);
        return byteArrayToHexString(abyte5);
    }

    public static String decodePassword(String s)
    {
        byte abyte0[] = hexStringToByteArray(s);
        decode(FIXED_MASK, FIXED_ADDER, abyte0, abyte0, ENCODED_BUFFER_LEN);
        byte abyte1[] = new byte[4];
        byte abyte2[] = new byte[4];
        System.arraycopy(abyte0, 0, abyte1, 0, 4);
        System.arraycopy(abyte0, 4, abyte2, 0, 4);
        byte abyte3[] = new byte[8];
        byte abyte4[] = new byte[8];
        System.arraycopy(abyte1, 0, abyte3, 0, 4);
        System.arraycopy(abyte2, 0, abyte3, 4, 4);
        System.arraycopy(abyte2, 0, abyte4, 0, 4);
        System.arraycopy(abyte1, 0, abyte4, 4, 4);
        byte abyte5[] = new byte[MAX_PASSWORD_LEN + 1];
        System.arraycopy(abyte0, 8, abyte5, 0, MAX_PASSWORD_LEN + 1);
        decode(abyte3, abyte4, abyte5, abyte5, MAX_PASSWORD_LEN + 1);
        StringBuffer stringbuffer = new StringBuffer();
        for(int i = 0; i < MAX_PASSWORD_LEN + 1; i++)
        {
            Character character = new Character((char)abyte5[i]);
            stringbuffer.append(character.toString());
        }

        String s1 = stringbuffer.toString();
        if(s1.indexOf('\n') != -1)
            s1 = s1.substring(0, s1.indexOf('\n'));
        return s1;
    }

    public static void main(String args[])
    {
        String s = new String(args[0]);
        String s1 = encodePassword(s);
        System.out.println(s1);
        System.out.println(decodePassword(s1));
        System.exit(0);
    }

    public Encoder()
    {
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999=2003, all rights reserved.");
    private static int MAX_PASSWORD_LEN;
    private static int ENCODED_BUFFER_LEN;
    private static byte FIXED_MASK[] = {
        91, 99, -12, 46, 97, -55, 42, -41
    };
    private static byte FIXED_ADDER[] = {
        117, 79, 61, -30, -93, 107, -28, -109
    };
    private static String hexDigits[] = {
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
        "A", "B", "C", "D", "E", "F"
    };

    static 
    {
        MAX_PASSWORD_LEN = 512;
        ENCODED_BUFFER_LEN = MAX_PASSWORD_LEN + 4 + 4 + 1;
    }
}
