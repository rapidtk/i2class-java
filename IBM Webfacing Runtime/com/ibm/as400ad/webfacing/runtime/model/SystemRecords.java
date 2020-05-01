// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import com.ibm.as400ad.webfacing.runtime.controller.ILibraryFile;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model:
//            SystemRecord

public class SystemRecords
{

    public SystemRecords()
    {
    }

    public static SystemRecord getSystemRecord(ILibraryFile ilibraryfile, String s)
    {
        if(ilibraryfile != null)
        {
            String s1 = ilibraryfile.getLibraryName();
            if(s1 != null && s1.length() >= 4 && "QSYS".equals(s1.substring(0, 4)))
            {
                for(int i = 0; i < _systemRecords.length; i++)
                    if(_systemRecords[i].getRecord().equals(s) && _systemRecords[i].getFileName().equals(ilibraryfile.getFileName()) && _systemRecords[i].getFileName().equals(ilibraryfile.getFileName()))
                        return _systemRecords[i];

            }
        }
        return null;
    }

    public static boolean isSystemRecord(ILibraryFile ilibraryfile, String s)
    {
        return getSystemRecord(ilibraryfile, s) != null;
    }

    public static boolean isSystemMenuRecord(SystemRecord systemrecord)
    {
        if(systemrecord != null)
        {
            for(int i = 0; i < 7; i++)
                if(_systemRecords[i].getRecord().equals(systemrecord.getRecord()))
                    return true;

        }
        return false;
    }

    private static SystemRecord _systemRecords[] = {
        new SystemRecord("MENULY", "QDSPMNU"), new SystemRecord("MENULN", "QDSPMNU"), new SystemRecord("MENUSY", "QDSPMNU"), new SystemRecord("MENUSN", "QDSPMNU"), new SystemRecord("MENUOPTY", "QDSPMNU"), new SystemRecord("MENUOPTN", "QDSPMNU"), new SystemRecord("HELPFMT", "QDSPMNU"), new SystemRecord("HEADER", "QDDSPEXT"), new SystemRecord("INQFMT", "QDDSPEXT"), new SystemRecord("SCRN1CTL", "QDDSPEXT"), 
        new SystemRecord("INFFMT", "QDDSPEXT"), new SystemRecord("EMSGCTL", "QDDSPEXT"), new SystemRecord("OUTFMT", "QDDSPEXT"), new SystemRecord("SCRN2CTL", "QDDSPEXT")
    };

}
