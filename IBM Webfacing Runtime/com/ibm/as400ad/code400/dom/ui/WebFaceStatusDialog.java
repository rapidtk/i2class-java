// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.convert.IStatusCallback;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.text.JTextComponent;

// Referenced classes of package com.ibm.as400ad.code400.dom.ui:
//            BaseDialog

public class WebFaceStatusDialog extends BaseDialog
    implements IStatusCallback
{

    public WebFaceStatusDialog(JFrame jframe)
    {
        super(jframe, "dummy");
        processedRecords = 0;
        totalSteps = 0;
        setTitle(rb.getString("WEBFACING_STATUS"));
        totalSteps = 3;
        pb.setMaximum(totalSteps);
        pb.setValue(1);
    }

    protected void addComponents()
    {
        getContentPane().setLayout(new BorderLayout());
        JPanel jpanel = new JPanel(new FlowLayout());
        jpanel.add(xmlfilePrompt);
        jpanel.add(xmlfileEntry);
        JPanel jpanel1 = new JPanel(new FlowLayout());
        jpanel1.add(ddsfilePrompt);
        jpanel1.add(ddsfileEntry);
        JPanel jpanel2 = new JPanel(new GridLayout(2, 1));
        jpanel2.add(jpanel);
        jpanel2.add(jpanel1);
        getContentPane().add(jpanel2, "North");
        getContentPane().add(pb, "Center");
        getContentPane().add(msgList, "South");
    }

    protected void createComponents()
    {
        rb = WebfacingConstants.RUNTIME_MRI_BUNDLE;
        pb = new JProgressBar();
        pb.setMinimum(1);
        pb.setStringPainted(true);
        msgList = new JComboBox();
        ddsfilePrompt = new JLabel(rb.getString("PROMPT_DDSFILE"));
        xmlfilePrompt = new JLabel(rb.getString("PROMPT_XMLFILE"));
        ddsfileEntry = new JTextField(40);
        xmlfileEntry = new JTextField(40);
        ddsfileEntry.setEditable(false);
        xmlfileEntry.setEditable(false);
    }

    public void windowClosing(WindowEvent windowevent)
    {
        dispose();
    }

    public void done()
    {
        pb.setValue(pb.getMaximum());
        pb.setString(rb.getString("DONE"));
        dispose();
    }

    public void fatalError(String s)
    {
        msgList.addItem(s);
        pb.setString(rb.getString("FATAL_ERROR"));
    }

    public void parsing()
    {
        pb.setValue(1);
        pb.setString(rb.getString("PARSING"));
    }

    public void populating()
    {
        pb.setValue(2);
        pb.setString(rb.getString("POPULATING"));
    }

    public void processingRecord(String s)
    {
        pb.setValue(processedRecords++);
        String s1 = rb.getString("PROCESSING_RECORD");
        s1 = sub(s1, "%1", s);
        pb.setString(s1);
    }

    public void statusMessage(String s)
    {
        msgList.addItem(s);
    }

    public void warningMessage(String s)
    {
        msgList.addItem(s);
    }

    public void startingWebFacing(int i)
    {
        pb.setMaximum(i);
        pb.setValue(0);
        pb.setString(rb.getString("WEBFACING"));
        System.out.println("inside startingWebFacing: " + i);
    }

    public static String sub(String s, String s1, String s2)
    {
        StringBuffer stringbuffer = new StringBuffer();
        int i = 0;
        boolean flag = false;
        for(int j = s.indexOf(s1, i); j != -1; j = s.indexOf(s1, i))
        {
            if(j >= 0)
                stringbuffer.append(s.substring(i, j));
            stringbuffer.append(s2);
            j += s1.length();
            i = j;
        }

        if(i >= 0)
            stringbuffer.append(s.substring(i));
        return stringbuffer.toString();
    }

    public void setDDSFileName(String s)
    {
        ddsfileEntry.setText(s);
    }

    public void setXMLFileName(String s)
    {
        xmlfileEntry.setText(s);
    }

    private ResourceBundle rb;
    private JProgressBar pb;
    private JComboBox msgList;
    private JTextField ddsfileEntry;
    private JTextField xmlfileEntry;
    private JLabel ddsfilePrompt;
    private JLabel xmlfilePrompt;
    private int processedRecords;
    private int totalSteps;
}
