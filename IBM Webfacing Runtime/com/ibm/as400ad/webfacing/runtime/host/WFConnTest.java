// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import java.io.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.host:
//            IHostRequest, IHJIErrors, IHJIRequestType, IWFInputBuffer

public class WFConnTest
    implements IHostRequest, IHJIErrors, IHJIRequestType
{

    public WFConnTest()
    {
    }

    public InputStream request(IWFInputBuffer iwfinputbuffer)
        throws IOException
    {
        System.out.println("Length of HJI Request: " + iwfinputbuffer.length());
        iwfinputbuffer.toStream(System.out);
        System.out.println('\n');
        FileOutputStream fileoutputstream = new FileOutputStream("HJITest.request");
        iwfinputbuffer.toStream(fileoutputstream);
        DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream("HJITest.reply"));
        dataoutputstream.writeInt(24);
        dataoutputstream.writeInt(0);
        dataoutputstream.writeInt(1);
        dataoutputstream.writeInt(19);
        dataoutputstream.writeBytes("AAA");
        dataoutputstream.writeInt(0);
        dataoutputstream.writeInt(17);
        dataoutputstream.writeInt(20);
        dataoutputstream.writeChars("0123456789");
        dataoutputstream.flush();
        return new FileInputStream("HJITest.reply");
    }
}
