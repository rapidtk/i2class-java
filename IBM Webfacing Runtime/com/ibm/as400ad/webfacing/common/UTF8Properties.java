// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.common;

import java.io.*;
import java.util.*;

public class UTF8Properties extends Properties
{

    public UTF8Properties()
    {
    }

    public UTF8Properties(Properties properties)
    {
        super(properties);
    }

    private boolean continueLine(String s)
    {
        int i = 0;
        for(int j = s.length() - 1; j >= 0 && s.charAt(j--) == '\\';)
            i++;

        return i % 2 == 1;
    }

    public synchronized void load(InputStream inputstream)
        throws IOException
    {
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));
        int i = 0;
        do
        {
            String s;
            char c;
            do
            {
                do
                {
                    s = bufferedreader.readLine();
                    if(s == null)
                        return;
                    if(i == 0)
                    {
                        if(s.length() > 0 && s.charAt(0) == '\uFEFF')
                            s = s.substring(1);
                        i++;
                    }
                } while(s.length() <= 0);
                c = s.charAt(0);
            } while(c == '#' || c == '!');
            String s1;
            String s2;
            for(; continueLine(s); s = new String(s2 + s1))
            {
                s1 = bufferedreader.readLine();
                if(s1 == null)
                    s1 = new String("");
                s2 = s.substring(0, s.length() - 1);
                int l = 0;
                for(l = 0; l < s1.length(); l++)
                    if(" \t\r\n\f\u3000".indexOf(s1.charAt(l)) == -1)
                        break;

                s1 = s1.substring(l, s1.length());
            }

            int j = s.length();
            int k;
            for(k = 0; k < j; k++)
                if(" \t\r\n\f\u3000".indexOf(s.charAt(k)) == -1)
                    break;

            int i1;
            for(i1 = k; i1 < j; i1++)
            {
                char c1 = s.charAt(i1);
                if(c1 == '\\')
                {
                    i1++;
                    continue;
                }
                if("=: \t\r\n\f\u3000".indexOf(c1) != -1)
                    break;
            }

            int j1;
            for(j1 = i1; j1 < j; j1++)
                if(" \t\r\n\f\u3000".indexOf(s.charAt(j1)) == -1)
                    break;

            if(j1 < j && "=:".indexOf(s.charAt(j1)) != -1)
                j1++;
            for(; j1 < j; j1++)
                if(" \t\r\n\f\u3000".indexOf(s.charAt(j1)) == -1)
                    break;

            String s3 = s.substring(k, i1);
            String s4 = i1 >= j ? "" : s.substring(j1, j);
            s3 = loadConvert(s3);
            s4 = loadConvert(s4);
            put(s3, s4);
        } while(true);
    }

    private String loadConvert(String s)
    {
        int i = s.length();
        StringBuffer stringbuffer = new StringBuffer(i);
        for(int j = 0; j < i;)
        {
            char c = s.charAt(j++);
            if(c == '\\')
            {
                c = s.charAt(j++);
                if(c == 'u')
                {
                    int k = 0;
                    for(int l = 0; l < 4; l++)
                    {
                        c = s.charAt(j++);
                        switch(c)
                        {
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
                            k = ((k << 4) + c) - 48;
                            break;

                        case 97: // 'a'
                        case 98: // 'b'
                        case 99: // 'c'
                        case 100: // 'd'
                        case 101: // 'e'
                        case 102: // 'f'
                            k = ((k << 4) + 10 + c) - 97;
                            break;

                        case 65: // 'A'
                        case 66: // 'B'
                        case 67: // 'C'
                        case 68: // 'D'
                        case 69: // 'E'
                        case 70: // 'F'
                            k = ((k << 4) + 10 + c) - 65;
                            break;

                        case 58: // ':'
                        case 59: // ';'
                        case 60: // '<'
                        case 61: // '='
                        case 62: // '>'
                        case 63: // '?'
                        case 64: // '@'
                        case 71: // 'G'
                        case 72: // 'H'
                        case 73: // 'I'
                        case 74: // 'J'
                        case 75: // 'K'
                        case 76: // 'L'
                        case 77: // 'M'
                        case 78: // 'N'
                        case 79: // 'O'
                        case 80: // 'P'
                        case 81: // 'Q'
                        case 82: // 'R'
                        case 83: // 'S'
                        case 84: // 'T'
                        case 85: // 'U'
                        case 86: // 'V'
                        case 87: // 'W'
                        case 88: // 'X'
                        case 89: // 'Y'
                        case 90: // 'Z'
                        case 91: // '['
                        case 92: // '\\'
                        case 93: // ']'
                        case 94: // '^'
                        case 95: // '_'
                        case 96: // '`'
                        default:
                            throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        }
                    }

                    stringbuffer.append((char)k);
                } else
                {
                    if(c == 't')
                        c = '\t';
                    else
                    if(c == 'r')
                        c = '\r';
                    else
                    if(c == 'n')
                        c = '\n';
                    else
                    if(c == 'f')
                        c = '\f';
                    stringbuffer.append(c);
                }
            } else
            {
                stringbuffer.append(c);
            }
        }

        return stringbuffer.toString();
    }

    private String saveConvert(String s)
    {
        int i = s.length();
        StringBuffer stringbuffer = new StringBuffer(i * 2);
        for(int j = 0; j < i;)
        {
            char c = s.charAt(j++);
            switch(c)
            {
            case 92: // '\\'
                stringbuffer.append('\\');
                stringbuffer.append('\\');
                break;

            case 9: // '\t'
                stringbuffer.append('\\');
                stringbuffer.append('t');
                break;

            case 10: // '\n'
                stringbuffer.append('\\');
                stringbuffer.append('n');
                break;

            case 13: // '\r'
                stringbuffer.append('\\');
                stringbuffer.append('r');
                break;

            case 12: // '\f'
                stringbuffer.append('\\');
                stringbuffer.append('f');
                break;

            default:
                if("=: \t\r\n\f#!\u3000".indexOf(c) != -1)
                    stringbuffer.append('\\');
                stringbuffer.append(c);
                break;
            }
        }

        return stringbuffer.toString();
    }

    public synchronized void store(OutputStream outputstream, String s)
        throws IOException
    {
        BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
        if(s != null)
            writeln(bufferedwriter, "#" + s);
        writeln(bufferedwriter, "#" + (new Date()).toString());
        String s1;
        String s2;
        for(Enumeration enumeration = keys(); enumeration.hasMoreElements(); writeln(bufferedwriter, s1 + "=" + s2))
        {
            s1 = (String)enumeration.nextElement();
            s2 = (String)get(s1);
            s1 = saveConvert(s1);
            s2 = saveConvert(s2);
        }

        bufferedwriter.flush();
    }

    private static void writeln(BufferedWriter bufferedwriter, String s)
        throws IOException
    {
        bufferedwriter.write(s);
        bufferedwriter.newLine();
    }

    private static final String copyRight = new String("(C) Copyright IBM Corporation 1999-2002 all rights reserved");
    private static final String _UTF8whiteSpaceChars = " \t\r\n\f\u3000";
    private static final String _UTF8keyValueSeparators = "=: \t\r\n\f\u3000";
    private static final String _UTF8strictKeyValueSeparators = "=:";
    private static final String _UTF8specialSaveChars = "=: \t\r\n\f#!\u3000";

}
