// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            Mnemonics

public class BaseWindow extends JFrame
    implements WindowListener, ActionListener, ItemListener, ListSelectionListener
{

    public BaseWindow(String s)
    {
        super(s);
        getContentPane().setLayout(new BorderLayout());
        createComponents();
        addComponents();
        addWindowListener(this);
        setLocation(200, 100);
        Mnemonics.addMnemonicsForJFrame(this);
    }

    protected void createComponents()
    {
    }

    protected void addComponents()
    {
    }

    public void windowActivated(WindowEvent windowevent)
    {
    }

    public void windowDeactivated(WindowEvent windowevent)
    {
    }

    public void windowIconified(WindowEvent windowevent)
    {
    }

    public void windowDeiconified(WindowEvent windowevent)
    {
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }

    public void windowClosed(WindowEvent windowevent)
    {
    }

    public void windowClosing(WindowEvent windowevent)
    {
        dispose();
        System.exit(0);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
    }

    public void itemStateChanged(ItemEvent itemevent)
    {
    }

    public void valueChanged(ListSelectionEvent listselectionevent)
    {
    }

    public static void main(String args[])
    {
        BaseWindow basewindow = new BaseWindow("Base Window");
    }
}
