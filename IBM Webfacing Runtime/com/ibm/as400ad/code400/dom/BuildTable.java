// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import java.io.*;

public class BuildTable
{

    public BuildTable()
    {
    }

    public static void main(String args[])
    {
        BufferedReader bufferedreader = null;
        PrintWriter printwriter = null;
        PrintWriter printwriter1 = null;
        PrintWriter printwriter2 = null;
        PrintWriter printwriter3 = null;
        boolean flag = false;
        byte byte0 = 32;
        int l1 = 0;
        int i2 = 0;
        int j2 = 0;
        int k2 = 0;
        boolean flag2 = false;
        startof[0] = 1;
        String s;
        if(args.length == 0)
            s = "EVFSTOKS.H";
        else
            s = args[0];
        File file = new File(s);
        if(!file.exists())
        {
            System.out.println("Error. input file " + s + " not found");
            System.exit(1);
        }
        try
        {
            FileInputStream fileinputstream = new FileInputStream(file);
            bufferedreader = new BufferedReader(new InputStreamReader(fileinputstream));
        }
        catch(Exception exception)
        {
            System.out.println("Error: input file " + s + " did not open");
            System.exit(1);
        }
        printwriter2 = openOutFile("EVFDWSST.java");
        printwriter = openOutFile("KwdMap.java");
        printwriter1 = openOutFile("ENUM_KeywordIdentifiers.java");
        printwriter3 = openOutFile("ENUM_KeywordStrings.java");
        System.out.println("\n Generating DDS kwd map file  KwdMap.java from source file " + s + "...");
        System.out.println("\n Generating DDS kwd enum file ENUM_KeywordIdentifiers.java from source file " + s + "...");
        System.out.println("\n Generating DDS token file    EVFDWSST.java from source file " + s + "...");
        System.out.println("\n Generating debug enum file   ENUM_KeywordStrings.java from source file " + s + "...");
        System.out.println();
        for(int l2 = 0; headingEnum[l2] != null; l2++)
            printwriter1.println(headingEnum[l2]);

        for(int i3 = 0; heading3[i3] != null; i3++)
            printwriter2.println(heading3[i3]);

        for(int j3 = 0; heading4[j3] != null; j3++)
            printwriter3.println(heading4[j3]);

        printwriter2.println("    public static final String rel_codes[] = ");
        printwriter2.println("    { \"EQ\", \"NE\", \"LT\", \"NL\", \"GT\", \"NG\", \"LE\", \"GE\" };");
        printwriter2.println("    String  event_id[] = ");
        printwriter2.println("    { \"*NONE\",\"*ULP\", \"*ULR\", \"*ULD\",");
        printwriter2.println("      \"*UMP\", \"*UMR\", \"*UMD\", \"*URP\", \"*URR\",");
        printwriter2.println("      \"*URD\", \"*SLP\", \"*SLR\", \"*SLD\", \"*SMP\",");
        printwriter2.println("      \"*SMR\", \"*SMD\", \"*SRP\", \"*SRR\", \"*SRD\" };");
        printwriter2.println("    String  key_id[] = ");
        printwriter2.println("    { \"*NONE\",\"CA01\", \"CA02\", \"CA03\",");
        printwriter2.println("      \"CA04\", \"CA05\", \"CA06\", \"CA07\", \"CA08\",");
        printwriter2.println("      \"CA09\", \"CA10\", \"CA11\", \"CA12\", \"CA13\",");
        printwriter2.println("      \"CA14\", \"CA15\", \"CA16\", \"CA17\", \"CA18\",");
        printwriter2.println("      \"CA19\", \"CA20\", \"CA21\", \"CA22\", \"CA23\",");
        printwriter2.println("      \"CA24\", \"CF01\", \"CF02\", \"CF03\", \"CF04\",");
        printwriter2.println("      \"CF05\", \"CF06\", \"CF07\", \"CF08\", \"CF09\",");
        printwriter2.println("      \"CF10\", \"CF11\", \"CF12\", \"CF13\", \"CF14\",");
        printwriter2.println("      \"CF15\", \"CF16\", \"CF17\", \"CF18\", \"CF19\",");
        printwriter2.println("      \"CF20\", \"CF21\", \"CF22\", \"CF23\", \"CF24\",");
        printwriter2.println("      \"ENTER\", \"ROLLUP\", \"ROLLDOWN\", \"HELP\",");
        printwriter2.println("      \"HOME\", \"PRINT\", \"CLEAR\", \"E00\", \"E01\",");
        printwriter2.println("      \"E02\", \"E03\", \"E04\", \"E05\", \"E06\", \"E07\",");
        printwriter2.println("      \"E08\", \"E09\", \"E10\", \"E11\", \"E12\", \"E13\",");
        printwriter2.println("      \"E14\", \"E15\" };");
        printwriter2.println("    String toktab[] = ");
        printwriter2.println("    {");
        printwriter3.println("    /*  debug info for parameter types : */");
        printwriter3.println("    public static final String TOKEN_STRINGS[] =");
        printwriter3.println("    {");
        while(!endOfFile) 
        {
            boolean flag1 = getWord(bufferedreader);
            char c2 = bufChar1;
            if(c2 == '/')
            {
                char c3 = peekChar(bufferedreader);
                if(c3 == '*')
                {
                    state |= 1;
                    process_comment(bufferedreader);
                    byte byte1 = 32;
                    i2 = 0;
                    continue;
                }
                unPeekChar(bufferedreader);
            }
            if(!flag1)
            {
                char c4 = bufChar1;
                if((state & 2) > 0)
                    printwriter2.print(c4);
                if(c4 == '\n' && ++j2 == 25)
                {
                    System.out.print('.');
                    j2 = 0;
                }
                if(c4 == '{')
                {
                    l1++;
                    if(i2 == 2)
                        state |= 2;
                }
                if(c4 == '}' && --l1 == 0 && (state & 2) > 0)
                {
                    state &= -3;
                    printwriter2.println(";");
                }
                char c = c4;
                i2 = 0;
            } else
            {
                char c1 = buf.charAt(buflen - 1);
                if((state & 2) > 0)
                {
                    String s1 = buf.toString().trim();
                    String s4 = s1;
                    int k = s1.lastIndexOf('_');
                    int i = s1.indexOf('_');
                    if(i == k)
                        i = -1;
                    if(i > 0 && k > 0)
                    {
                        Object obj1 = null;
                        if(s1.startsWith("PAR_"))
                        {
                            String s10 = s1.substring(i + 1, k);
                            for(int i1 = 0; i1 < NUMSPLATS; i1++)
                            {
                                if(!s10.equals(splats[i1]))
                                    continue;
                                buf.setCharAt(k--, '*');
                                s1 = buf.toString();
                                break;
                            }

                            if(s10.equals("BARCODE"))
                            {
                                String s5 = s1.substring(i + 9);
                                for(int j1 = 0; barsplats[j1] != null; j1++)
                                {
                                    if(!s5.equals(barsplats[j1]))
                                        continue;
                                    buf.setCharAt(k--, '*');
                                    s1 = buf.toString();
                                    break;
                                }

                            }
                            if(s10.equals("SFLRCDNBR"))
                            {
                                String s6 = s1.substring(i + 11);
                                if(s6.equals("TOP"))
                                {
                                    buf.setCharAt(k--, '*');
                                    s1 = buf.toString();
                                }
                            }
                            if(s10.equals("DATSEP") || s10.equals("TIMSEP"))
                            {
                                String s7 = s1.substring(i + 8);
                                for(int k1 = 0; PFConvsFrom[k1] != null; k1++)
                                {
                                    if(!s7.equals(PFConvsFrom[k1]))
                                        continue;
                                    s1 = s1.substring(0, k + 1) + PFConvsTo[k1];
                                    buf = new StringBuffer(s1);
                                    break;
                                }

                            }
                            if(s10.equals("COMP"))
                            {
                                String s8 = s1.substring(i + 6);
                                if(s8.equals("NULL"))
                                {
                                    buf.setCharAt(k--, '*');
                                    s1 = buf.toString();
                                }
                            }
                        }
                    }
                    if(k > 0)
                    {
                        printwriter2.print('"');
                        printwriter2.print(s1.substring(k + 1));
                        printwriter2.print('"');
                        if(k2 % 2 == 0)
                            printwriter3.println();
                        String s9 = "/*" + padLeft(Integer.toString(k2), 3) + "*/  \"" + buf + "\",";
                        s9 = s9 + "                                         ";
                        s9 = s9.substring(0, 37);
                        printwriter3.print(s9);
                        printwriter1.println("   public static final int " + s4 + " = " + tokenIdx++ + ";");
                        String s11 = s1.substring(k + 1);
                        if(!flag2 && s1.equals("KWD_MAX_TOKEN"))
                        {
                            endof[0] = k2 - 1;
                            flag2 = true;
                        }
                        if(s1.endsWith("TOKEN_START"))
                        {
                            if(s1.startsWith("PHY_"))
                                startof[2] = k2 + 1;
                            if(s1.startsWith("LOG_"))
                                startof[3] = k2 + 1;
                            if(s1.startsWith("ICF_"))
                                startof[4] = k2 + 1;
                        }
                        if(s1.equals("PRT_KWD_START"))
                            startof[1] = k2 + 1;
                        if(s1.endsWith("PAR_START"))
                        {
                            if(s1.startsWith("PHY_"))
                                endof[2] = k2 - 1;
                            if(s1.startsWith("LOG_"))
                                endof[3] = k2 - 1;
                            if(s1.startsWith("PRT_"))
                                endof[1] = k2 - 1;
                            if(s1.startsWith("ICF_"))
                                endof[4] = k2 - 1;
                        }
                        str_token[k2] = s1.substring(4);
                        int j5 = flag2 ? endof[0] : k2;
                        int k3;
                        for(k3 = 0; k3 <= j5; k3++)
                            if(str_token[k3].equals(s11))
                                break;

                        if(k3 > j5)
                            k3 = k2;
                        table2[k2++] = k3;
                    } else
                    {
                        printwriter2.print(s1);
                    }
                    i2 = 0;
                } else
                {
                    String s2 = buf.toString().trim();
                    switch(i2)
                    {
                    default:
                        break;

                    case 0: // '\0'
                        if(s2.equals("enum"))
                            i2++;
                        break;

                    case 1: // '\001'
                        if(s2.equals("tokens"))
                            i2++;
                        break;
                    }
                }
            }
        }
        for(int j = 0; j < 5; j++)
        {
            System.out.println("!");
            for(int l3 = startof[j]; l3 <= endof[j]; l3++)
            {
                for(int l = 0; l < 5; l++)
                    if(j == l)
                    {
                        table[l][l3] = l3;
                    } else
                    {
                        for(int l4 = startof[l]; l4 <= endof[l]; l4++)
                        {
                            if(!str_token[l3].equals(str_token[l4]))
                                continue;
                            table[l][l3] = l4;
                            break;
                        }

                    }

            }

        }

        printwriter2.println();
        printwriter2.println();
        for(int i4 = 0; heading1[i4] != null; i4++)
            printwriter.println(heading1[i4]);

        printwriter.println();
        printwriter.println("    public static final int MAP(int x)      {return kwdMapping[x];}");
        printwriter.println("    public static final int MAPDSPF(int x)  {return kwdIDMapping[0][x];}");
        printwriter.println("    public static final int MAPPRTF(int x)  {return kwdIDMapping[1][x];}");
        printwriter.println("    public static final int MAPPF(int x)    {return kwdIDMapping[2][x];}");
        printwriter.println("    public static final int MAPLF(int x)    {return kwdIDMapping[3][x];}");
        printwriter.println("    public static final int MAPICF(int x)   {return kwdIDMapping[4][x];}");
        printwriter.println();
        printwriter.print("    public static final int kwdMapping[] = ");
        printwriter.print("    {");
        for(int j4 = 0; j4 < k2; j4++)
        {
            if(j4 % 12 == 0)
            {
                printwriter.println();
                printwriter.print("         ");
            }
            printwriter.print(" " + padLeft(Integer.toString(table2[j4]), 3) + ",");
        }

        printwriter.println();
        printwriter.println("    };");
        printwriter.println();
        printwriter.println();
        printwriter.println("    public static final int kwdIDMapping[][] =");
        printwriter.println("    {");
        Object obj = null;
        for(int i5 = 0; i5 < 5; i5++)
        {
            printwriter.print("    { ");
            System.out.println(".");
            for(int k4 = 0; k4 < k2; k4++)
            {
                if(k4 % 12 == 0)
                {
                    printwriter.println();
                    printwriter.print("         ");
                }
                String s3;
                if(k4 == k2 - 1)
                    s3 = " " + padLeft(Integer.toString(table[i5][k4]), 3) + " ";
                else
                    s3 = " " + padLeft(Integer.toString(table[i5][k4]), 3) + ",";
                printwriter.print(s3);
            }

            if(i5 == 4)
                printwriter.println("} ");
            else
                printwriter.println("},");
        }

        try
        {
            bufferedreader.close();
        }
        catch(IOException ioexception) { }
        printwriter.println("    };");
        printwriter3.println();
        printwriter3.println("    };");
        printwriter.println("} // end class/interface definition");
        printwriter2.println("} // end class/interface definition");
        printwriter3.println("} // end class/interface definition");
        printwriter1.println("} // end class/interface definition");
        printwriter.flush();
        printwriter.close();
        printwriter3.flush();
        printwriter3.close();
        printwriter2.flush();
        printwriter2.close();
        printwriter1.flush();
        printwriter1.close();
    }

    public static char peekChar(BufferedReader bufferedreader)
    {
        char c = ' ';
        try
        {
            bufferedreader.mark(1);
            c = (char)bufferedreader.read();
            if(c == '\uFFFF')
                endOfFile = true;
        }
        catch(Exception exception)
        {
            System.out.println("Exception in peekChar! " + exception.getClass().getName() + ": " + exception.getMessage());
        }
        return c;
    }

    public static void unPeekChar(BufferedReader bufferedreader)
    {
        try
        {
            bufferedreader.reset();
        }
        catch(Exception exception)
        {
            System.out.println("Exception in unPeekChar! " + exception.getClass().getName() + ": " + exception.getMessage());
        }
    }

    public static boolean getWord(BufferedReader bufferedreader)
    {
        int i = 1;
        byte byte0 = 32;
        buf = new StringBuffer();
        try
        {
            int j = bufferedreader.read();
            char c = (char)j;
            if(j == -1)
                endOfFile = true;
            while(!endOfFile && c == ' ') 
            {
                int k = bufferedreader.read();
                if(k == -1)
                    endOfFile = true;
                else
                    c = (char)k;
            }
            buf.append(c);
            bufChar1 = c;
            if(endOfFile)
                return false;
            for(boolean flag = !Character.isLetterOrDigit(c) || c == '_'; !flag;)
            {
                bufferedreader.mark(1);
                int l = bufferedreader.read();
                char c1 = (char)l;
                if(l == -1)
                {
                    endOfFile = true;
                    flag = true;
                } else
                if(Character.isLetterOrDigit(c1) || c1 == '_')
                {
                    buf.append(c1);
                    i++;
                } else
                {
                    flag = true;
                }
            }

            if(!endOfFile && i != 1)
            {
                bufferedreader.reset();
                i--;
            }
            buflen = i;
            if(Character.isLetterOrDigit(bufChar1) || bufChar1 == '_')
                return true;
        }
        catch(IOException ioexception)
        {
            System.out.println("Error in getWord: " + ioexception.getClass().getName() + ":" + ioexception.getMessage());
        }
        return false;
    }

    public static void process_comment(BufferedReader bufferedreader)
    {
        char c = ' ';
        byte byte0 = 32;
        boolean flag = false;
        StringBuffer stringbuffer = new StringBuffer();
        try
        {
            while((state & 1) > 0) 
            {
                int i = bufferedreader.read();
                char c1 = (char)i;
                if(i == -1)
                {
                    endOfFile = true;
                    return;
                }
                if(c == 42 && c1 == '/')
                    state &= -2;
                else
                    c = c1;
                stringbuffer.append(c1);
            }
        }
        catch(IOException ioexception)
        {
            System.out.println("Error in process_comment: " + ioexception.getMessage());
        }
    }

    public static PrintWriter openOutFile(String s)
    {
        exitNbr++;
        PrintWriter printwriter = null;
        try
        {
            File file = new File(s);
            System.out.println("Opening output file " + s + ". Exists? " + file.exists());
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            printwriter = new PrintWriter(fileoutputstream);
        }
        catch(Exception exception)
        {
            System.out.println("Error: output file " + s + " did not open: " + exception.getClass().getName() + ": " + exception.getMessage());
            System.exit(exitNbr);
        }
        return printwriter;
    }

    public static String padLeft(String s, int i)
    {
        int j = s.length();
        if(j >= i)
            return s;
        StringBuffer stringbuffer = new StringBuffer();
        int k = i - j;
        for(int l = 0; l < k; l++)
            stringbuffer.append(' ');

        for(int i1 = 0; i1 < j; i1++)
            stringbuffer.append(s.charAt(i1));

        return stringbuffer.toString();
    }

    public static String padRight(String s, int i)
    {
        int j = s.length();
        if(j >= i)
            return s;
        StringBuffer stringbuffer = new StringBuffer();
        for(int k = 0; k < j; k++)
            stringbuffer.append(s.charAt(k));

        int l = i - j;
        for(int i1 = 0; i1 < l; i1++)
            stringbuffer.append(' ');

        return stringbuffer.toString();
    }

    public static final int COMMENT = 1;
    public static final int OUTPUT = 2;
    public static final int NUMCOLS = 12;
    public static final String INFILE = "EVFSTOKS.H";
    public static final String ENUMFILE1_ROOT = "ENUM_KeywordIdentifiers";
    public static final String MAPFILE1_ROOT = "KwdMap";
    public static final String OUTFILE_ROOT = "EVFDWSST";
    public static final String BUGFILE_ROOT = "ENUM_KeywordStrings";
    public static final String ENUMFILE1 = "ENUM_KeywordIdentifiers.java";
    public static final String MAPFILE1 = "KwdMap.java";
    public static final String OUTFILE = "EVFDWSST.java";
    public static final String BUGFILE = "ENUM_KeywordStrings.java";
    public static final String splats[] = {
        "BOX", "CHOICE", "CLRL", "DATE", "DATFMT", "DFNLIN", "DFT", "DRAWER", "DSPMOD", "DSPSIZ", 
        "ENTFLDATR", "ERASEINP", "FLTPCN", "FONT", "HLPSHELF", "HLPARA", "JDUPSEQ", "LINE", "MAPVAL", "MDTOFF", 
        "MLTCHCFLD", "MNUBAR", "MOUBTN", "MSGID", "PRINT", "PRTQLTY", "PSHBTNCHC", "PSHBTNFLD", "PULLDOWN", "REFFLD", 
        "RTNCSRLOC", "SFLEND", "SFLMLTCHC", "SFLSNGCHC", "SLNO", "SNGCHCFLD", "TIMFMT", "UNIQUE", "UNLOCK", "WDWTITLE", 
        "WINDOW", "SUBPARM"
    };
    public static final int NUMSPLATS = splats.length;
    public static final String barsplats[] = {
        "HRZ", "VRT", "HRI", "HRITOP", "NOHRI", "AST", "NOAST", null
    };
    public static final String PFConvsFrom[] = {
        "BLANK", "COMMA", "JOB", "MINUS", "PERIOD", "SLASH", "COLON", null
    };
    public static final String PFConvsTo[] = {
        "' '", "','", "*JOB", "'-'", "'.'", "'/'", "':'", null
    };
    public static final int KWD = 0;
    public static final int KWT = 1;
    public static final int KWP = 2;
    public static final int KWL = 3;
    public static final int KWI = 4;
    public static final int NUMTYPES = 5;
    public static final int startof[] = {
        0, 0, 0, 0, 0
    };
    public static final int endof[] = {
        0, 0, 0, 0, 0
    };
    public static final String headingEnum[] = {
        "package com.ibm.as400ad.code400.dom;", "/**", " *---------------------------------------------------------------------*", " *  Keyword Identifiers.                                               *", " *---------------------------------------------------------------------*/", "/*", " * NOTE This file was generated automatically by BuildTable.java       *", " */", "public interface ENUM_KeywordIdentifiers", "{", 
        null
    };
    public static final String heading1[] = {
        "package com.ibm.as400ad.code400.dom;", "/**", " *---------------------------------------------------------------------*", " *  Keyword Mapping Table - automatically generated.                   *", " *     This table will give the DSPF equivilent of any keyword ID. If  *", " *     there is no DSPF equivilent, the keyword ID used as the index   *", " *     is returned.                                                    *", " *     Format is :    TargetID = kwdMapping[sourceID];                 *", " *                                                                     *", " *     The 2 dimensional table will map a keyword of any DDS type to   *", 
        " *     a keyword ID of any other type. If the keyword does not exist   *", " *     in the DDS of the requested type, a 0 is returned.              *", " *     Included are also Macro definitions to easily map to a specific *", " *     DDS type.                                                       *", " *     Format is :    TargetID = kwdIDMapping[target_type][sourceID];  *", " *---------------------------------------------------------------------*/", "/*", " * NOTE This file was generated automatically by BuildTable.java       *", " */", "public class KwdMap", 
        "{", null
    };
    public static final String heading3[] = {
        "package com.ibm.as400ad.code400.dom;", "/*", " * NOTE This file was generated automatically by BuildTable.java     *", " */", "public class EVFDWSST", "{", null
    };
    public static final String heading4[] = {
        "package com.ibm.as400ad.code400.dom;", "/**", " * ENUM.java                                                         *", " * NOTE This file was generated automatically by BuildTable.java     *", " */", "public interface ENUM_KeywordStrings", "{", null
    };
    public static final String trailer = "} // end class/interface definition";
    static StringBuffer buf = null;
    static char bufChar1 = ' ';
    static char buf2[] = new char[100];
    static int buflen = 0;
    static int state = 0;
    static String str_token[] = new String[1000];
    static int table2[] = new int[1000];
    static int table[][] = new int[5][1000];
    static boolean endOfFile = false;
    static String tokenEnums[] = new String[1000];
    static int tokenEnumInts[] = new int[1000];
    static int tokenIdx = 0;
    static int exitNbr = 0;

}
