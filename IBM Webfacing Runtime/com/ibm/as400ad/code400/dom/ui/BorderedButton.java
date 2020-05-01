// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import javax.swing.*;

public class BorderedButton extends JButton
{

    public BorderedButton(String s)
    {
        super(s);
        setBorder(BorderFactory.createRaisedBevelBorder());
    }
}
