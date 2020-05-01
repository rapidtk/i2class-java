// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.*;
import javax.swing.*;

public class LookAndFeelMenu extends JMenu
    implements ActionListener
{

    public LookAndFeelMenu(JFrame jframe)
    {
        super("Look-and-feel");
        UIs = UIManager.getInstalledLookAndFeels();
        lAndfMIs = new JRadioButtonMenuItem[UIs.length];
        lfGroup = new ButtonGroup();
        parentWindow = jframe;
        for(int i = 0; i < UIs.length; i++)
        {
            lAndfMIs[i] = new JRadioButtonMenuItem(UIs[i].getName());
            if(UIs[i].getName().equals(UIManager.getLookAndFeel().getName()))
            {
                lAndfMIs[i].setSelected(true);
                currLF = UIs[i].getClassName();
            }
            add(lAndfMIs[i]);
            lAndfMIs[i].addActionListener(this);
            lfGroup.add(lAndfMIs[i]);
        }

    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        for(int i = 0; i < UIs.length; i++)
            if(obj == lAndfMIs[i])
                try
                {
                    currLF = UIs[i].getClassName();
                    UIManager.setLookAndFeel(currLF);
                    SwingUtilities.updateComponentTreeUI(parentWindow);
                }
                catch(Exception exception)
                {
                    System.out.println("Error changing L & F: " + exception.getMessage());
                }

    }

    public boolean saveToPropertiesFile(Properties properties)
    {
        boolean flag = true;
        properties.put("uilf", currLF);
        return flag;
    }

    public boolean restoreFromPropertiesFile(Properties properties)
    {
        boolean flag = true;
        String s = (String)properties.get("uilf");
        if(s != null)
        {
            try
            {
                UIManager.setLookAndFeel(s);
                SwingUtilities.updateComponentTreeUI(parentWindow);
            }
            catch(Exception exception)
            {
                System.out.println("Error changing L & F: " + exception.getMessage());
            }
            for(int i = 0; i < UIs.length; i++)
            {
                if(!UIs[i].getClassName().equals(s))
                    continue;
                lAndfMIs[i].setSelected(true);
                break;
            }

        }
        return flag;
    }

    private JFrame parentWindow;
    private javax.swing.UIManager.LookAndFeelInfo UIs[];
    private JRadioButtonMenuItem lAndfMIs[];
    private ButtonGroup lfGroup;
    private String currLF;
    private static final String KEY_UILF = "uilf";
}
