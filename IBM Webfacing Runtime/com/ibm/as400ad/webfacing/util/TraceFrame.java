// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.util;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class TraceFrame extends Frame
    implements WindowListener
{

    public TraceFrame()
    {
        setSize(400, 300);
        addWindowListener(this);
        setTitle("TraceFrame setTitle() goes here");
        show();
    }

    public TraceFrame(String s)
    {
        this();
        setTitle(s);
    }

    public void windowActivated(WindowEvent windowevent)
    {
    }

    public void windowClosed(WindowEvent windowevent)
    {
    }

    public void windowClosing(WindowEvent windowevent)
    {
        setVisible(false);
    }

    public void windowDeactivated(WindowEvent windowevent)
    {
    }

    public void windowDeiconified(WindowEvent windowevent)
    {
    }

    public void windowIconified(WindowEvent windowevent)
    {
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }
}
