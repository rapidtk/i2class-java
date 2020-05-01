// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.dev;

import com.ibm.as400ad.code400.dom.ui.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.text.JTextComponent;

public class DirectoryPromptDialog extends BaseDialog
{

    public DirectoryPromptDialog(JFrame jframe, String s)
    {
        super(jframe, s);
        wasCancelled = true;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        wasCancelled = true;
        msgLine.clearText();
        if(obj == dirBrowseButton)
        {
            JFileChooser jfilechooser = new JFileChooser();
            jfilechooser.setCurrentDirectory(new File(dirEntry.getText().trim() + "\\.."));
            jfilechooser.setApproveButtonText("Select");
            jfilechooser.setDialogTitle("Select JSP Servlet Source Directory");
            jfilechooser.setFileSelectionMode(1);
            jfilechooser.setName(dirEntry.getText().trim());
            int i = jfilechooser.showOpenDialog(this);
            if(i == 0)
            {
                File file1 = jfilechooser.getSelectedFile();
                dirEntry.setText(file1.getAbsolutePath());
            }
        } else
        if(obj == dirDestBrowseButton)
        {
            JFileChooser jfilechooser1 = new JFileChooser();
            jfilechooser1.setCurrentDirectory(new File(dirDestEntry.getText().trim() + "\\.."));
            jfilechooser1.setApproveButtonText("Select");
            jfilechooser1.setDialogTitle("Select Servlet Destination Directory");
            jfilechooser1.setFileSelectionMode(1);
            jfilechooser1.setName(dirDestEntry.getText().trim());
            int j = jfilechooser1.showOpenDialog(this);
            if(j == 0)
            {
                File file2 = jfilechooser1.getSelectedFile();
                dirDestEntry.setText(file2.getAbsolutePath());
            }
        } else
        if(obj == okButton)
        {
            boolean flag = true;
            File file = new File(dirEntry.getText().trim());
            if(!file.exists())
            {
                flag = false;
                msgLine.setText("ERROR: Directory not found");
                dirEntry.requestFocus();
            }
            if(flag)
            {
                File file3 = new File(dirDestEntry.getText().trim());
                if(!file3.exists())
                {
                    flag = false;
                    msgLine.setText("ERROR: Directory not found");
                    dirEntry.requestFocus();
                }
            }
            if(flag)
            {
                wasCancelled = false;
                dispose();
            }
        }
    }

    protected void addComponents()
    {
        getContentPane().setLayout(new BorderLayout());
        FormPanel formpanel = new FormPanel();
        formpanel.addRow(dirLabel, dirEntry, dirBrowseButton);
        formpanel.addRow(dirDestLabel, dirDestEntry, dirDestBrowseButton);
        getContentPane().add(formpanel, "Center");
        JPanel jpanel = new JPanel(new GridLayout(1, 1));
        jpanel.add(okButton);
        JPanel jpanel1 = new JPanel(new BorderLayout());
        jpanel1.add(jpanel, "Center");
        jpanel1.add(msgLine, "South");
        getContentPane().add(jpanel1, "South");
    }

    protected void createComponents()
    {
        dirLabel = new JLabel("Source Directory: ");
        dirDestLabel = new JLabel("Destination Directory: ");
        dirEntry = new JTextField(30);
        dirDestEntry = new JTextField(30);
        dirBrowseButton = new BorderedButton("Browse...");
        dirDestBrowseButton = new BorderedButton("Browse...");
        dirBrowseButton.addActionListener(this);
        dirDestBrowseButton.addActionListener(this);
        okButton = new BorderedButton("OK");
        okButton.addActionListener(this);
        msgLine = new MessageLine();
    }

    public String getDestDirectory()
    {
        return dirDestEntry.getText().trim();
    }

    public JTextField getDirDestWidget()
    {
        return dirDestEntry;
    }

    public String getDirectory()
    {
        return dirEntry.getText().trim();
    }

    public JTextField getDirWidget()
    {
        return dirEntry;
    }

    public void setDirDest(String s)
    {
        dirDestEntry.setText(s);
    }

    public void setDirectory(String s)
    {
        dirEntry.setText(s);
    }

    public boolean wasCancelled()
    {
        return wasCancelled;
    }

    public void windowClosing(WindowEvent windowevent)
    {
        wasCancelled = true;
        dispose();
    }

    private JLabel dirLabel;
    private JLabel dirDestLabel;
    private JTextField dirEntry;
    private JTextField dirDestEntry;
    private JButton dirBrowseButton;
    private JButton dirDestBrowseButton;
    private JButton okButton;
    private MessageLine msgLine;
    private boolean wasCancelled;
}
