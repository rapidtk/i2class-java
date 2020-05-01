// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import java.awt.Container;
import javax.swing.*;
import javax.swing.text.JTextComponent;

public class Mnemonics
{

    public Mnemonics()
    {
        mnemonics = new StringBuffer();
    }

    public char getUniqueMnemonic(String s)
    {
        int i = s.length();
        char c = ' ';
        if(i == 0)
            return c;
        String s1 = s;
        s = s.toLowerCase();
        char c1 = s.charAt(0);
        boolean flag = false;
        for(int j = 0; j < i && c == ' '; j++)
        {
            char c2 = s.charAt(j);
            boolean flag1 = false;
            if(c2 != ' ')
            {
                for(int k = 0; k < mnemonics.length(); k++)
                    if(mnemonics.charAt(k) == c2)
                        flag1 = true;

            }
            if(!flag1 && c2 != ' ')
            {
                mnemonics.append(c2);
                c = s1.charAt(j);
            }
        }

        if(c == ' ')
            c = getUniqueMnemonic("abcdefghijklmnopqrstuvwxyz0123456789");
        return c;
    }

    public void setMnemonic(JLabel jlabel, JTextComponent jtextcomponent)
    {
        jlabel.setDisplayedMnemonic(getUniqueMnemonic(jlabel.getText()));
        jlabel.setLabelFor(jtextcomponent);
    }

    public void setMnemonic(JLabel jlabel, JComboBox jcombobox)
    {
        jlabel.setDisplayedMnemonic(getUniqueMnemonic(jlabel.getText()));
        jlabel.setLabelFor(jcombobox);
    }

    public void setMnemonic(JCheckBox jcheckbox)
    {
        jcheckbox.setMnemonic(getUniqueMnemonic(jcheckbox.getText()));
    }

    public void setMnemonic(JRadioButton jradiobutton)
    {
        jradiobutton.setMnemonic(getUniqueMnemonic(jradiobutton.getText()));
    }

    public void setMnemonic(JButton jbutton)
    {
        jbutton.setMnemonic(getUniqueMnemonic(jbutton.getText()));
    }

    public static void addMnemonicsForMenuBar(JMenuBar jmenubar)
    {
        Mnemonics mnemonics1 = new Mnemonics();
        int i = jmenubar.getMenuCount();
        for(int j = 0; j < i; j++)
        {
            JMenu jmenu = jmenubar.getMenu(j);
            jmenu.setMnemonic(mnemonics1.getUniqueMnemonic(jmenu.getText()));
            addMnemonicsForMenu(jmenu);
        }

    }

    public static void addMnemonicsForMenu(JMenu jmenu)
    {
        Mnemonics mnemonics1 = new Mnemonics();
        int i = jmenu.getItemCount();
        for(int j = 0; j < i; j++)
        {
            javax.swing.JMenuItem jmenuitem = jmenu.getItem(j);
            if(jmenuitem != null && jmenuitem != jmenu)
            {
                jmenuitem.setMnemonic(mnemonics1.getUniqueMnemonic(jmenuitem.getText()));
                if(jmenuitem instanceof JMenu)
                    addMnemonicsForMenu((JMenu)jmenuitem);
            }
        }

    }

    public static void addMnemonicsForJFrame(JFrame jframe)
    {
        Mnemonics mnemonics1 = new Mnemonics();
        mnemonics1.addMnemonicsForContainer(jframe.getContentPane());
        JMenuBar jmenubar = jframe.getJMenuBar();
        if(jmenubar != null)
            addMnemonicsForMenuBar(jmenubar);
    }

    public static void addMnemonicsForJDialog(JDialog jdialog)
    {
        Mnemonics mnemonics1 = new Mnemonics();
        mnemonics1.addMnemonicsForContainer(jdialog.getContentPane());
    }

    public static void addMnemonicsForJApplet(JApplet japplet)
    {
        Mnemonics mnemonics1 = new Mnemonics();
        mnemonics1.addMnemonicsForContainer(japplet);
    }

    private void addMnemonicsForContainer(Container container)
    {
        java.awt.Component acomponent[] = container.getComponents();
        Object obj = null;
        Object obj1 = null;
        String s = null;
        Object obj2 = null;
        for(int i = 0; i < acomponent.length; i++)
        {
            String s1 = s != null ? s : "dummyValue";
            Class class1 = acomponent[i].getClass();
            for(s = class1.getName(); !s.startsWith("javax.swing") && !s.startsWith("java.awt"); s = class1.getName())
                class1 = class1.getSuperclass();

            if(s.equals("javax.swing.JPanel") || s.equals("javax.swing.Box"))
                addMnemonicsForContainer((Container)acomponent[i]);
            else
            if(s.equals("javax.swing.JRadioButton"))
                setMnemonic((JRadioButton)acomponent[i]);
            else
            if(s.equals("javax.swing.JCheckBox"))
                setMnemonic((JCheckBox)acomponent[i]);
            else
            if(s.equals("javax.swing.JButton"))
                setMnemonic((JButton)acomponent[i]);
            else
            if(s1.equals("javax.swing.JLabel"))
                if(s.equals("javax.swing.JTextField") || s.equals("javax.swing.JTextArea") || s.equals("javax.swing.JPasswordField"))
                    setMnemonic((JLabel)acomponent[i - 1], (JTextComponent)acomponent[i]);
                else
                if(s.equals("javax.swing.JComboBox"))
                    setMnemonic((JLabel)acomponent[i - 1], (JComboBox)acomponent[i]);
        }

    }

    private StringBuffer mnemonics;
}
