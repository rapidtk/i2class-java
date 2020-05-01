// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.ui;

import java.awt.*;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class FormPanel extends JPanel
{

    public FormPanel()
    {
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        stretchable = false;
        currRow = 1;
        finalColspan = 1;
        gbc.weightx = 0.0D;
        gbc.weighty = 0.0D;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = 0;
        gbc.anchor = 16;
        gbc.insets = new Insets(1, 1, 1, 1);
        setLayout(gbl);
    }

    protected void addPart(JComponent jcomponent, int i, int j, int k, int l)
    {
        gbc.gridy = i;
        gbc.gridx = j;
        gbc.gridheight = k;
        gbc.gridwidth = l;
        if(stretchable)
        {
            gbc.fill = 1;
            gbc.weightx = 1.0D;
            gbc.weighty = 1.0D;
        }
        gbl.setConstraints(jcomponent, gbc);
        add(jcomponent);
        gbc.gridwidth = 1;
        gbc.fill = 0;
        gbc.weightx = 0.0D;
        gbc.weighty = 0.0D;
    }

    public void addRow(JComponent jcomponent)
    {
        addPart(jcomponent, currRow++, 0, 1, finalColspan);
    }

    public void addRow(JComponent jcomponent, JComponent jcomponent1)
    {
        addPart(jcomponent, currRow, 0, 1, 1);
        addPart(jcomponent1, currRow++, 1, 1, finalColspan);
    }

    public void addRow(JComponent jcomponent, JComponent jcomponent1, JComponent jcomponent2)
    {
        addPart(jcomponent, currRow, 0, 1, 1);
        addPart(jcomponent1, currRow, 1, 1, 1);
        addPart(jcomponent2, currRow++, 2, 1, finalColspan);
    }

    public void addRow(JComponent jcomponent, JComponent jcomponent1, JComponent jcomponent2, JComponent jcomponent3)
    {
        addPart(jcomponent, currRow, 0, 1, 1);
        addPart(jcomponent1, currRow, 1, 1, 1);
        addPart(jcomponent2, currRow, 2, 1, 1);
        addPart(jcomponent3, currRow++, 3, 1, finalColspan);
    }

    public void addRow(JComponent jcomponent, int i)
    {
        finalColspan = i;
        addRow(jcomponent);
        finalColspan = 1;
    }

    public void addRow(JComponent jcomponent, JComponent jcomponent1, int i)
    {
        finalColspan = i;
        addRow(jcomponent, jcomponent1);
        finalColspan = 1;
    }

    public void addRow(JComponent jcomponent, JComponent jcomponent1, JComponent jcomponent2, int i)
    {
        finalColspan = i;
        addRow(jcomponent, jcomponent1, jcomponent2);
        finalColspan = 1;
    }

    public void addRow(JComponent jcomponent, JComponent jcomponent1, JComponent jcomponent2, JComponent jcomponent3, int i)
    {
        finalColspan = i;
        addRow(jcomponent, jcomponent1, jcomponent2, jcomponent3);
        finalColspan = 1;
    }

    public void addStretchableRow(JComponent jcomponent)
    {
        stretchable = true;
        addRow(jcomponent);
        stretchable = false;
    }

    public void addStretchableRow(JComponent jcomponent, JComponent jcomponent1)
    {
        stretchable = true;
        addRow(jcomponent, jcomponent1);
        stretchable = false;
    }

    public void addStretchableRow(JComponent jcomponent, JComponent jcomponent1, JComponent jcomponent2)
    {
        stretchable = true;
        addRow(jcomponent, jcomponent1, jcomponent2);
        stretchable = false;
    }

    public void addStretchableRow(JComponent jcomponent, JComponent jcomponent1, JComponent jcomponent2, JComponent jcomponent3)
    {
        stretchable = true;
        addRow(jcomponent, jcomponent1, jcomponent2, jcomponent3);
        stretchable = false;
    }

    public void addStretchableRow(JComponent jcomponent, int i)
    {
        stretchable = true;
        addRow(jcomponent, i);
        stretchable = false;
    }

    public void addStretchableRow(JComponent jcomponent, JComponent jcomponent1, int i)
    {
        stretchable = true;
        addRow(jcomponent, jcomponent1, i);
        stretchable = false;
    }

    public void addStretchableRow(JComponent jcomponent, JComponent jcomponent1, JComponent jcomponent2, int i)
    {
        stretchable = true;
        addRow(jcomponent, jcomponent1, jcomponent2, i);
        stretchable = false;
    }

    public void addStretchableRow(JComponent jcomponent, JComponent jcomponent1, JComponent jcomponent2, JComponent jcomponent3, int i)
    {
        stretchable = true;
        addRow(jcomponent, jcomponent1, jcomponent2, jcomponent3, i);
        stretchable = false;
    }

    protected GridBagLayout gbl;
    protected GridBagConstraints gbc;
    protected boolean stretchable;
    protected int currRow;
    protected int finalColspan;
}
