// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.JTextComponent;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            BaseDialog, FormPanel, BorderedButton, MessageLine

public class FilePromptDialog extends BaseDialog
{
    protected class XMLFileFilter extends FileFilter
    {

        public boolean accept(File file)
        {
            return file.getName().endsWith(".xml");
        }

        public String getDescription()
        {
            return "*.xml";
        }

        protected XMLFileFilter()
        {
        }
    }


    public FilePromptDialog(JFrame jframe, String s)
    {
        super(jframe, s);
        wasCancelled = true;
        mustExist = false;
        mustNotExist = false;
        XMLFILTER = new XMLFileFilter();
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
            jfilechooser.setDialogTitle("Select Directory");
            jfilechooser.setFileSelectionMode(1);
            jfilechooser.setName(dirEntry.getText().trim());
            int i = jfilechooser.showSaveDialog(this);
            if(i == 0)
            {
                File file1 = jfilechooser.getSelectedFile();
                dirEntry.setText(file1.getAbsolutePath());
            }
        } else
        if(obj == fileBrowseButton)
        {
            JFileChooser jfilechooser1 = new JFileChooser();
            jfilechooser1.setCurrentDirectory(new File(dirEntry.getText().trim()));
            jfilechooser1.setApproveButtonText("Select");
            jfilechooser1.setDialogTitle("Select File");
            jfilechooser1.setFileSelectionMode(0);
            jfilechooser1.setName(fileEntry.getText().trim());
            jfilechooser1.setFileFilter(XMLFILTER);
            int j = jfilechooser1.showSaveDialog(this);
            if(j == 0)
            {
                File file2 = jfilechooser1.getSelectedFile();
                fileEntry.setText(file2.getName());
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
            if(flag && mustExist)
            {
                File file3 = new File(dirEntry.getText().trim(), fileEntry.getText().trim());
                if(!file3.exists())
                {
                    flag = false;
                    msgLine.setText("ERROR: File not found");
                    fileEntry.requestFocus();
                }
            }
            if(flag && mustNotExist)
            {
                File file4 = new File(dirEntry.getText().trim(), fileEntry.getText().trim());
                if(file4.exists())
                {
                    flag = false;
                    msgLine.setText("WARNING: File already exists");
                    fileEntry.requestFocus();
                    int k = JOptionPane.showConfirmDialog(this, "File exists and will be replaced. Are you sure?", "Confirmation", 0, 3);
                    if(k == 0)
                        flag = true;
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
        formpanel.addRow(fileLabel, fileEntry, fileBrowseButton);
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
        dirLabel = new JLabel("Directory: ");
        fileLabel = new JLabel("File: ");
        dirEntry = new JTextField(30);
        fileEntry = new JTextField(30);
        dirBrowseButton = new BorderedButton("Browse...");
        fileBrowseButton = new BorderedButton("Browse...");
        dirBrowseButton.addActionListener(this);
        fileBrowseButton.addActionListener(this);
        okButton = new BorderedButton("OK");
        okButton.addActionListener(this);
        msgLine = new MessageLine();
    }

    public String getDirectory()
    {
        return dirEntry.getText().trim();
    }

    public JTextField getDirWidget()
    {
        return dirEntry;
    }

    public String getFile()
    {
        return fileEntry.getText().trim();
    }

    public JTextField getFileWidget()
    {
        return fileEntry;
    }

    public void setDirectory(String s)
    {
        dirEntry.setText(s);
    }

    public void setFile(String s)
    {
        fileEntry.setText(s);
    }

    public void setMustExist(boolean flag)
    {
        mustExist = flag;
    }

    public void setMustNotExist(boolean flag)
    {
        mustNotExist = flag;
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
    private JLabel fileLabel;
    private JTextField dirEntry;
    private JTextField fileEntry;
    private JButton dirBrowseButton;
    private JButton fileBrowseButton;
    private JButton okButton;
    private MessageLine msgLine;
    private boolean wasCancelled;
    private boolean mustExist;
    private boolean mustNotExist;
    private XMLFileFilter XMLFILTER;
}
