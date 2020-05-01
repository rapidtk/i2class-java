// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            BaseWindow, Mnemonics

public class BaseDialog extends JDialog
    implements WindowListener, ActionListener, ItemListener, ListSelectionListener
{

    public BaseDialog(JFrame jframe, String s)
    {
        super(jframe, s, true);
        getContentPane().setLayout(new BorderLayout());
        createComponents();
        addComponents();
        addWindowListener(this);
        setLocation(250, 150);
        pack();
        Mnemonics.addMnemonicsForJDialog(this);
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
        basewindow.setSize(300, 150);
        BaseDialog basedialog = new BaseDialog(basewindow, "Base Dialog");
        basedialog.setSize(50, 75);
        basedialog.setVisible(true);
        System.exit(0);
    }
}
