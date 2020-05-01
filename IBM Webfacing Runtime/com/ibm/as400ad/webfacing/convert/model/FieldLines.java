// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            IFieldText

public class FieldLines
{

    FieldLines()
    {
        _lines = new ArrayList();
        _counter = 0;
    }

    void add(Object obj)
    {
        _lines.add(obj);
    }

    void addFieldText(int i, int j, IFieldText ifieldtext)
    {
        if(ifieldtext != null)
        {
            int k = ifieldtext.getWidth();
            if((i + k) - 1 <= j)
            {
                _lines.add(ifieldtext.getFieldTextWithTransform());
            } else
            {
                int l = (j - i) + 1;
                _lines.add(ifieldtext.getFieldTextWithTransform(0, l));
                while(l < k) 
                    if(k - l <= j)
                    {
                        _lines.add(ifieldtext.getFieldTextWithTransform(l, k));
                        l = k;
                    } else
                    {
                        _lines.add(ifieldtext.getFieldTextWithTransform(l, l + j));
                        l += j;
                    }
            }
        }
    }

    String get(int i)
    {
        return (String)_lines.get(i);
    }

    boolean hasNext()
    {
        return _counter <= _lines.size() - 1;
    }

    Object next()
    {
        return _lines.get(_counter++);
    }

    void reset()
    {
        _counter = 0;
    }

    void set(int i, String s)
    {
        _lines.set(i, s);
    }

    int size()
    {
        return _lines.size();
    }

    Object reGet()
    {
        backup();
        return get(_counter);
    }

    void backup()
    {
        if(_counter > 0)
            _counter--;
    }

    private List _lines;
    private int _counter;
}
