// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            BaseWindow

public class MessageLine extends JPanel
{

    public MessageLine()
    {
        super(new GridLayout(1, 1));
        msgField = new JTextField();
        msgField.setEditable(false);
        msgField.setBorder(BorderFactory.createLoweredBevelBorder());
        msgField.setBackground(Color.cyan);
        add(msgField);
    }

    public void setText(String s)
    {
        msgField.setText(s);
    }

    public void clearText()
    {
        msgField.setText("");
    }

    public static void main(String args[])
    {
        BaseWindow basewindow = new BaseWindow("Test MessageLine");
        basewindow.getContentPane().setLayout(new BorderLayout());
        basewindow.getContentPane().add(new MessageLine(), "South");
        basewindow.pack();
    }

    private JTextField msgField;
}
