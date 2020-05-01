// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.dds.dom.update;

import com.ibm.etools.dds.dom.IDdsModel;
import java.io.IOException;
import java.io.PrintWriter;

// Referenced classes of package com.ibm.etools.dds.dom.update:
//            ILineContainer, IUpdatableFileLevel

public interface IUpdatableDdsModel
    extends IDdsModel
{

    public abstract void saveAsDDS(PrintWriter printwriter)
        throws IOException;

    public abstract void setVariantCharacters(String s);

    public abstract ILineContainer getLines();

    public abstract void setLines(ILineContainer ilinecontainer);

    public abstract IUpdatableFileLevel getUpdatableFileLevel();
}
