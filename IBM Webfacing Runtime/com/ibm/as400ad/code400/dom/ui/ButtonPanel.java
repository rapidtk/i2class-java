// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            BorderedButton, BaseWindow

public class ButtonPanel extends JPanel
{

    public ButtonPanel(boolean flag)
    {
        if(flag)
            setLayout(new GridLayout(1, 3));
        else
            setLayout(new FlowLayout());
        okButton = new BorderedButton("Ok");
        cancelButton = new BorderedButton("Cancel");
        helpButton = new BorderedButton("Help");
        add(okButton);
        add(cancelButton);
        add(helpButton);
    }

    public static void main(String args[])
    {
        ButtonPanel buttonpanel = new ButtonPanel(args.length == 0);
        BaseWindow basewindow = new BaseWindow("Test Buttons");
        basewindow.getContentPane().setLayout(new BorderLayout());
        basewindow.getContentPane().add(buttonpanel, "South");
        basewindow.pack();
    }

    protected BorderedButton okButton;
    protected BorderedButton cancelButton;
    protected BorderedButton helpButton;
}
